// exception.cc 
//	Entry point into the Nachos kernel from user programs.
//	There are two kinds of things that can cause control to
//	transfer back to here from user code:
//
//	syscall -- The user code explicitly requests to call a procedure
//	in the Nachos kernel.  Right now, the only function we support is
//	"Halt".
//
//	exceptions -- The user code does something that the CPU can't handle.
//	For instance, accessing memory that doesn't exist, arithmetic errors,
//	etc.  
//
//	Interrupts (which can also cause control to transfer from user
//	code into the Nachos kernel) are handled elsewhere.
//
// For now, this only handles the Halt() system call.
// Everything else core dumps.
//
// Copyright (c) 1992-1993 The Regents of the University of California.
// All rights reserved.  See copyright.h for copyright notice and limitation 
// of liability and disclaimer of warranty provisions.

#include "copyright.h"
#include "system.h"
#include "syscall.h"
#include "ProcMan.h"
#include "addrspace.h"
#include "filestat.h"

//----------------------------------------------------------------------
// ExceptionHandler
// 	Entry point into the Nachos kernel.  Called when a user program
//	is executing, and either does a syscall, or generates an addressing
//	or arithmetic exception.
//
// 	For system calls, the following is the calling convention:
//
// 	system call code -- r2
//		arg1 -- r4
//		arg2 -- r5
//		arg3 -- r6
//		arg4 -- r7
//
//	The result of the system call, if any, must be put back into r2. 
//
// And don't forget to increment the pc before returning. (Or else you'll
// loop making the same system call forever!
//
//	"which" is the kind of exception.  The list of possible exceptions 
//	are in machine.h.
//----------------------------------------------------------------------

void
ExceptionHandler(ExceptionType which)
{
    int type = machine->ReadRegister(2);
    Process *curProc;

    if(which == SyscallException)
    {
      if(procMan->getCurrentProc()) {
	// don't try to do something if we are exiting the process
	//
	procMan->getCurrentProc()->exiting->Acquire();
	procMan->getCurrentProc()->exiting->Release();
      }
      DEBUG('X',"Syscall %i\n",type);
      switch( type )
      {
      case SC_Halt:
	DEBUG('x', "ShutDown, initiated by user program.\n");
#ifdef VM_STATS
	kswap->printStats();
#endif
	interrupt->Halt();
   	break;
      case SC_Exit: { // done
	DEBUG('x', "Exit, initiated by user program.\n");
	// let other threads know that we are exiting
	//
	procMan->getCurrentProc()->exiting->Acquire();
	Thread *killerThread = new Thread("Killa'",0,0,0);
	procMan->getCurrentProc()->exitStatus( machine->ReadRegister(4) );
	killerThread->Fork(ProcessManager::threadKiller,
			  (int)(procMan->getCurrentProc()));
	currentThread->Yield();
	break;
      }
      case SC_Exec: // done
	DEBUG('x', "Exec, initiated by user program.\n");
	machine->WriteRegister( 2, procMan->exec( machine->ReadRegister(4),
						  machine->ReadRegister(5)));
	break;
      case SC_Join: // done
	DEBUG('x', "Join, initiated by user program.\n");
	machine->WriteRegister( 
	  2,procMan->join( machine->ReadRegister(4) ) );
	break;
      case SC_Create: { // done
	DEBUG('x', "Create, initiated by user program.\n");
	int size = machine->ReadRegister(5);
	char *buf = new char [size + 1];
	curProc = procMan->getCurrentProc();
	curProc->getAddrSpace()->userToKern(machine->ReadRegister(4),1,size,buf);
	buf[size] = '\0' ;
	( fileSystem->Create( buf ) ) ?
	    machine->WriteRegister(2,0) :
	    machine->WriteRegister(2,-1) ;
	delete buf;
	break;
      }
      case SC_Open: { // done
	DEBUG('x', "Open, initiated by user program.\n");
	int size = machine->ReadRegister(5);
	char *buf = new char [size + 1];
	curProc = procMan->getCurrentProc();
	curProc->getAddrSpace()->userToKern(machine->ReadRegister(4),1,size,buf);
	buf[size] = '\0' ;
	machine->WriteRegister(2,curProc->openFile(buf));
	delete buf;
	break;
      }
      case SC_Read: { // done
	DEBUG('x', "Read, initiated by user program.\n");
	int size = machine->ReadRegister(5);
	int returnCode;
	char *buf = new char [size];

	curProc = procMan->getCurrentProc();
	returnCode = curProc->read(buf,size,machine->ReadRegister(6));
	if ( returnCode > -1 ) 
	    curProc->getAddrSpace()->kernToUser(machine->ReadRegister(4),
	    					1, size, buf
						);
	machine->WriteRegister( 2, returnCode );
	delete buf;
	break;
      }
      case SC_Write: { // done
	DEBUG('x', "Write, initiated by user program.\n");
	int size = machine->ReadRegister(5);
	char *buf = new char [size];
	curProc = procMan->getCurrentProc();
	curProc->getAddrSpace()->userToKern(machine->ReadRegister(4),1,size,buf);
	machine->WriteRegister(2,
			       curProc->write(buf,size,machine->ReadRegister(6))
			       );
	delete buf;
	break;
      }
      case SC_Close:
	DEBUG('x', "Close, initiated by user program.\n");
	curProc = procMan->getCurrentProc();
	curProc->closeFile(machine->ReadRegister(4));
	break;
      case SC_Fork:  // done
	DEBUG('x', "Fork, initiated by user program.\n");
	machine->WriteRegister( 
           2, procMan->fork( machine->ReadRegister(4)));
	break;
      case SC_Yield: // done
	DEBUG('x', "Yield, initiated by user program. [%i:%i]\n",currentThread->getAssociatedPID(),currentThread->getID());
	currentThread->Yield();
	break;
      case SC_ThreadExit: // done
	DEBUG('x', "ThreadExit, initiated by user program.\n");
        machine->WriteRegister( 
           2, ( procMan->threadExit( (unsigned char)machine->ReadRegister(4))));
	break;
      case SC_ProcInfo:  { // done 
	DEBUG('x', "ProcInfo, initiated by user program.\n");

	char *info;
	int addr;
	int curProcNum, numProcs;

	addr = machine->ReadRegister(4);
	info = (char *)procMan->getProcInfo();
	numProcs = procMan->getProcCount();
	curProc = procMan->getCurrentProc();
	for ( curProcNum = 0; curProcNum < numProcs; curProcNum++ ) {
	    curProc->getAddrSpace()->kernToUser( addr, 4, 1, info );
	    info += 4; addr += 4;
	    curProc->getAddrSpace()->kernToUser( addr, 4, 1, info );
	    info += 4; addr += 4;
	    curProc->getAddrSpace()->kernToUser( addr, 4, 1, info );
	    info += 4; addr += 4;
	    curProc->getAddrSpace()->kernToUser( addr, 4, 1, info );
	    info += 4; addr += 4;
	    curProc->getAddrSpace()->kernToUser( addr, 4, 1, info );
	    info += 4; addr += 4;
	    curProc->getAddrSpace()->kernToUser( addr, 1, MAX_ARG_LENGTH, info );
	    info += MAX_ARG_LENGTH; addr += MAX_ARG_LENGTH;
	} // loop
	machine->WriteRegister( 2, numProcs );
	delete info;
	break;
      }
      case SC_GetProcCount: // done
	DEBUG('x', "GetProcCount, initiated by user program.\n");
	machine->WriteRegister( 2, procMan->getProcCount( ) );
	break;
      case SC_VMStat: {
	DEBUG('x', "VMState, initiated by user program.\n");
	int addr = machine->ReadRegister(4);
	AddrSpace *addrSpace = procMan->getCurrentProc()->getAddrSpace();
	vminfo_t info;
#ifdef VM
	//TODO: Need to get meminfo from kswap instead
#else
	machine->WriteRegister( 2, addrSpace->vmstat(&info));
#endif
	addrSpace->kernToUser(addr,1,sizeof(vminfo_t),(void *)&info);
      }
      case SC_Kill:  // done
	DEBUG('x', "Kill, initiated by user program.\n");
	machine->WriteRegister( 
           2, procMan->killProc(machine->ReadRegister(4)));
	break;

      case SC_Stat:
      {
	DEBUG('x', "Stat, initiated by user program.\n");

	int addr = machine->ReadRegister(4);
	int size = machine->ReadRegister(6);
	char* userFilename = new char[size + 1];
	curProc = procMan->getCurrentProc();
	curProc->getAddrSpace()->userToKern(machine->ReadRegister(5),1,size,userFilename );
	userFilename[ size ] = '\0' ;
        MTreeMetaNode* node = fileSystem->getMetaNode( userFilename ) ;
	if( node )
	{
	    char* mode          = (char*)(WordToMachine((node->getmod()))) ;
	    char* filename      = (char*)node->filename()                  ;
	    char* directory     = (char*)node->directory()                 ;
	    char* nodeSize      = (char*)(WordToMachine(node->size()))     ;	
	    char* id            = (char*)(WordToMachine(node->id()))       ;
	    int   modeSize      = sizeof( T_BYTE ) ;
	    int   filenameSize  = size             ;
	    int   directorySize = sizeof( bool )   ;
	    int   nodeSizeSize  = sizeof( T_WORD ) ;
	    int   idSize        = nodeSizeSize     ;

	    curProc->getAddrSpace()->kernToUser( addr, 1, modeSize, mode );
	    addr += modeSize;
	    curProc->getAddrSpace()->kernToUser( addr, 1, filenameSize, filename );
	    addr += filenameSize;
	    curProc->getAddrSpace()->kernToUser( addr, 1, directorySize, directory );
	    addr += directorySize;
	    curProc->getAddrSpace()->kernToUser( addr, 1, nodeSizeSize, nodeSize );
	    addr += nodeSizeSize;
	    curProc->getAddrSpace()->kernToUser( addr, 1, idSize, id );
	    addr += idSize;

	    fileSystem->releaseNode( node ) ;
	    delete userFilename ;
	    machine->WriteRegister( 2, 0 ) ;
	}
	else
	{
	    fileSystem->releaseNode( node ) ;
	    delete userFilename ;
	    machine->WriteRegister( 2, -1 ) ;
	}

	break;
      }
      case SC_Chmod:
      {
	DEBUG('x', "Chmod, initiated by user program.\n");

	int size = machine->ReadRegister(5);
	char* userFilename = new char[size + 1];
	curProc = procMan->getCurrentProc();
	curProc->getAddrSpace()->userToKern(machine->ReadRegister(4),1,size,userFilename );
	userFilename[ size ] = '\0' ;
	T_BYTE permissions = machine->ReadRegister(6) ;

        MTreeMetaNode* node = fileSystem->getMetaNode( userFilename ) ;
	if( node )
	{
	    node->chmod( permissions ) ;
	    fileSystem->putNode(node);
	    fileSystem->releaseNode( node ) ;
	    delete userFilename ;
	    machine->WriteRegister( 2, 0 ) ;
	}
	else
	{
	    fileSystem->releaseNode( node ) ;
	    delete userFilename ;
	    machine->WriteRegister( 2, -1 ) ;
	}

	break;
      }
      case SC_Unlink:
      {
	DEBUG('x', "Unlink, initiated by user program.\n");

	int size = machine->ReadRegister(5);
	char* userFilename = new char[size + 1];
	curProc = procMan->getCurrentProc();
	curProc->getAddrSpace()->userToKern(machine->ReadRegister(4),1,size,userFilename );
	userFilename[ size ] = '\0' ;

	( fileSystem->Remove( userFilename ) == 0 ) ?
	    machine->WriteRegister( 2, 0  ) :
	    machine->WriteRegister( 2, -1 ) ;
	
        delete userFilename ;
	break;
      }
      case SC_Mkdir:
      {
	DEBUG('x', "Mkdir, initiated by user program.\n");
	int size = machine->ReadRegister(5);
	char* userDIRname = new char[size + 1];
	curProc = procMan->getCurrentProc();
	curProc->getAddrSpace()->userToKern(machine->ReadRegister(4),1,size,userDIRname );
	userDIRname[ size ] = '\0' ;
        ( fileSystem->mkdir( userDIRname ) == 0 ) ?
	    machine->WriteRegister( 2, 0  ) :
	    machine->WriteRegister( 2, -1 ) ;
	delete userDIRname ;
	break;
      }
      case SC_Move:
      {
	DEBUG('x', "Move, initiated by user program.\n");

	int size    = machine->ReadRegister(5);
	int newSize = machine->ReadRegister(7);

	char* userFilename = new char[size + 1];
	char* newFilename  = new char[newSize + 1];
	curProc = procMan->getCurrentProc();
	curProc->getAddrSpace()->userToKern(machine->ReadRegister(4),1,size,userFilename );
	curProc->getAddrSpace()->userToKern(machine->ReadRegister(6),1,size,newFilename );
	userFilename[ size ] = '\0' ;

        MTreeMetaNode* node = fileSystem->getMetaNode( userFilename ) ;
	if( node )
	{
	    int success = node->filename( userFilename ) ;
	    fileSystem->releaseNode( node ) ;
	    delete userFilename ;
	    delete newFilename ;
	    (success) ?
		machine->WriteRegister( 2, 0  ) :
		machine->WriteRegister( 2, -2 ) ;
	}
	else
	{
	    fileSystem->releaseNode( node ) ;
	    delete userFilename ;
	    delete newFilename ;
	    machine->WriteRegister( 2, -1  ) ;
	}
	break;
      }
      case SC_Chdir:
      {
          int size    = machine->ReadRegister(5);
          char* newDirectory = new char[size + 1];

	  curProc = procMan->getCurrentProc();
	  curProc->getAddrSpace()->userToKern(machine->ReadRegister(4),1,size,newDirectory );
	  newDirectory[size] = '\0';

	  fileSystem->freeComps(fileSystem->cwd,fileSystem->cwdLen);
	  fileSystem->cwdLen = fileSystem->parsePath(newDirectory,NULL,0,&(fileSystem->cwd));
	  delete newDirectory;
	  break;
      }
      case SC_CWD:
      {
	  // TODO
	  break;
      }
      case SC_ListDir:
      {
	  int plen = machine->ReadRegister(5);
	  char *path = new char[plen + 1];

	  curProc = procMan->getCurrentProc();
	  curProc->getAddrSpace()->userToKern(machine->ReadRegister(4),1,plen,path);
	  path[plen] = '\0';

	  char **dirInfo;
	  size_t dilen,i;
	  int spos(0);
	  if(plen == 0)
	      dilen = fileSystem->ListDir("/",&dirInfo);
	  else
	      dilen = fileSystem->ListDir(path,&dirInfo);
	  for(i = 0; i < dilen && spos < machine->ReadRegister(7); i++) {
	      curProc->getAddrSpace()->kernToUser(machine->ReadRegister(6) + spos,1,strlen(dirInfo[i]),dirInfo[i]);
	      spos += strlen(dirInfo[i]);
	      curProc->getAddrSpace()->kernToUser(machine->ReadRegister(6) + spos++,1,1,"\n");
	      delete dirInfo[i];
	      dirInfo[i] = NULL;
	  }
	  if(spos < machine->ReadRegister(7))
	      curProc->getAddrSpace()->kernToUser(machine->ReadRegister(6) + spos++,1,1,"\0");
	  delete path;
	  delete dirInfo;
	  machine->WriteRegister(2,dilen);
	  break;
      }
      default:
	printf("Unexpected user mode exception %d %d\n", which, type);
	ASSERT(FALSE);
      }
    }
    else if(which == PageFaultException)
    {
	// update stats
	stats->numPageFaults++;

        int badAddr = machine->ReadRegister( BadVAddrReg );
	curProc = procMan->getCurrentProc();
	curProc->getAddrSpace()->TLB_Miss( badAddr );
	return; // leave PC, NPC where they are... let the instruction run again
    }
    else
    {
	switch(which) {
	    case ReadOnlyException:
		DEBUG('X',"Read only exception: PC(0x%x)\n",machine->ReadRegister(PCReg));
		break;
	    case BusErrorException:
		DEBUG('X',"Bus error exception: PC(0x%x)\n",machine->ReadRegister(PCReg));
		break;
	    case AddressErrorException:
		DEBUG('X',"Address error exception: PC(0x%x)\n",machine->ReadRegister(PCReg));
		break;
	    case OverflowException:
		DEBUG('X',"Overflow exception: PC(0x%x)\n",machine->ReadRegister(PCReg));
		break;
	    case IllegalInstrException:
		DEBUG('X',"Illegal instr exception: PC(0x%x)\n",machine->ReadRegister(PCReg));
		break;
	    default:
		;
	}
    }
    int pc = machine->ReadRegister(PCReg);
    machine->WriteRegister(PrevPCReg, pc);
    pc = machine->ReadRegister(NextPCReg);
    machine->WriteRegister(PCReg, pc);
    pc += 4;
    machine->WriteRegister(NextPCReg, pc);
}


