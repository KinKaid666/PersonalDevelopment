// File:         $Id: ProcMan.cc,v 3.1 2001/11/07 00:14:03 eae8264 Exp $
// Author:       Trevor Clarke, Eric Eells, Eric Ferguson, Dan Westgate
// Contributors: 
// Description:	The implementation file for ProcessManager
// Revisions:
//               $Log: ProcMan.cc,v $
//               Revision 3.1  2001/11/07 00:14:03  eae8264
//               Fixed when things were being deleted for failed
//               constructors.
//
//               Revision 3.0  2001/11/04 19:47:03  trc2876
//               force to 3.0
//
//               Revision 2.5  2001/10/28 22:23:34  etf2954
//               Added an error msg if we lack mem to start the initial process
//
//               Revision 2.4  2001/10/28 21:11:02  etf2954
//               Added out of mem check
//
//               Revision 2.3  2001/10/28 20:09:13  trc2876
//               Fixed error in kernToUser and userToKern....these were only working if you
//               copied a single page of data...they work on page-spanning data now
//
//               we can have more args and longer args..set these in userprog/argdefs.h
//               currently, these are set to 8 and 32 respectivly
//
//               Revision 2.2  2001/10/27 20:59:18  etf2954
//               Added an init function to Process.cc
//               	this way, after a process is created,
//               	one must call init on it.
//
//               This avoids the problem when a process creates
//               	and starts its thread in its constructor
//               	and that thread page faults.
//               	The page fault then tries to get the current
//               	process, but since we're in the current process
//               	it has yet to be added to the list of current
//               	processes and we get a null back from
//               	getCurrentProcess()
//
//               Revision 2.1  2001/10/25 20:50:40  trc2876
//               nicely handle errors in AddrSpace creation
//
//               Revision 2.0  2001/10/11 02:53:28  trc2876
//               force update to 2.0 tree
//
//               Revision 1.43  2001/10/09 05:18:34  eae8264
//               Added spaces at end of name in each returned
//               struct for getProcInfo.
//
//               Revision 1.42  2001/10/08 23:08:16  trc2876
//               code cleanup
//
//               Revision 1.41  2001/10/08 01:45:08  etf2954
//               fixed sync error in exec
//
//               Revision 1.40  2001/10/07 22:44:44  etf2954
//               wrote join
//
//               Revision 1.39  2001/10/07 20:36:31  etf2954
//               Changed the returns status of ::exec
//
//               Revision 1.38  2001/10/07 20:06:26  trc2876
//               Exit() is grrrrrrreat!!
//
//               Revision 1.37  2001/10/07 17:33:27  trc2876
//               mofo!!! fixed the exec bug....only a few left
//
//               Revision 1.36  2001/10/06 23:31:38  eae8264
//               Removed all ASSERTs in Process.cc and replaced them
//               with error codes that go back through ProcessManager
//               and back to exception.cc.  The modification to exception
//               are that Read, Write now return error codes.  Other
//               error codes are just expanding what was already being
//               returned to user-land and others are just unused.
//
//               Revision 1.35  2001/10/06 22:14:31  etf2954
//               Corrected the exit call
//
//               Revision 1.34  2001/10/06 20:54:54  p544-01b
//               Changed Funtion doNothing to initThread
//
//               Revision 1.33  2001/10/06 20:43:08  eae8264
//               Fixed name of index used in loop in getProcInfo.  The loop
//               and the inner loop were both using the same variable
//               causing problems.
//
//               Revision 1.32  2001/10/06 20:17:00  eae8264
//               Supported return of error code.
//
//               Revision 1.31  2001/10/06 20:09:39  trc2876
//               fixed threadKill
//
//               Revision 1.29  2001/10/06 17:47:16  trc2876
//               Does exiting, etc. really work now?
//
//               Revision 1.28  2001/10/06 15:19:20  trc2876
//               Renamed ThreadExit() syscall to ThreadKill()
//               added ThreadExit() syscall which exits current thread (still broken)
//               Fixed Fork() system call
//
//               Revision 1.27  2001/10/06 00:40:08  eae8264
//               Added debug statements so that arguments from user-land are printed
//               out in convention we're using.
//
//               Revision 1.26  2001/10/06 00:15:57  trc2876
//               in progress Fork fixes
//
//               Revision 1.25  2001/10/05 21:08:57  trc2876
//               Changed mutex Acquire/Release to be more agressive....watch out for these
//
//               Revision 1.24  2001/10/05 20:30:26  etf2954
//               Fixed sync problem in ThreadExit()
//
//               Revision 1.23  2001/10/04 23:11:14  etf2954
//               Fixed the exit() sys call
//
//               Revision 1.22  2001/10/04 21:53:32  trc2876
//               BUGS list....
//
//               Revision 1.21  2001/10/04 00:52:36  eae8264
//               There's too much stuff going on to know what I just did....
//               stuff...
//
//               Revision 1.20  2001/10/03 23:19:15  eae8264
//               Lots of stuff.  Halt works!  :)
//
//               Revision 1.19  2001/10/02 22:51:09  eae8264
//               Merged and added code to work with init proc.
//
//               Revision 1.18  2001/10/02 22:46:01  etf2954
//               Added getProcess( int pid) that returns a Process *
//
//               Revision 1.17  2001/10/02 22:24:28  etf2954
//               Wrote correct constructors
//
//               Revision 1.16  2001/10/02 20:15:46  eae8264
//               Set up special cases for init proc.
//
//               Revision 1.15  2001/10/02 16:21:45  eae8264
//               Excuse the multiple file update.
//               ProcMan.cc:  Changed threadExit to use Scheduler::unscheduleThread
//               	     Fixed memory leaks.
//               	     Added some explicit casting (minor changes)
//               	     Some code restructuring (superficial)
//               Process.cc:  Had constructor call Scheduler::ReadyToRun on first thread.
//               	     Had exit() call Scheduler::ReadyToRun instead of Run.
//               Process.h:   Changed return values of pid and ppid's to unsigned chars.
//
//               Revision 1.14  2001/10/01 00:45:50  eae8264
//               A small correction in string copying in getProcInfo
//
//               Revision 1.13  2001/09/30 21:48:58  eae8264
//               Added string copying in getProcInfo
//
//               Revision 1.12  2001/09/30 21:31:06  eae8264
//               Fixed compiler errors.  Who knows how that got in here??  :)
//
//               Revision 1.11  2001/09/30 20:06:49  eae8264
//               Finished fork.  Changed name of progtest func.
//
//               Revision 1.10  2001/09/30 19:44:47  etf2954
//               Changed Process::exit( )  call to conform to exit( int )
//
//               Revision 1.9  2001/09/28 01:52:53  eae8264
//               Added Mutual Exclusion for the remaining "important" functions.  At first
//               it wasn't needed.  Also, added threadExit with implementation.  Added
//               functionality for some bad arguments to return error codes.  This also
//               has been updated in the header file.
//
//               Revision 1.8  2001/09/27 22:30:55  eae8264
//               Sruff....  Good Stuff!
//
//               Revision 1.7  2001/09/27 20:03:29  eae8264
//               Made lotsa changes!  :P
//
//               Revision 1.6  2001/09/27 17:09:15  eae8264
//               Added some needed things to exit, exec.
//
//               Revision 1.5  2001/09/27 15:55:00  eae8264
//               Got things to compile.  Implemented getProcInfo().  Added some accessors to Process.  Stuff
//
//               Revision 1.4  2001/09/25 01:55:47  trc2876
//               last minute fixes....look at the damn diffs
//
//               Revision 1.3  2001/09/24 20:16:52  trc2876
//               added locking code to getProcCount() and getProcInfo()
//               added threadExit()
//               function ptr -> int in fork() since we need a user-land address here
//               moved new Process() outside critical section in exec() since it is not
//                  a critical resource
//
//               Revision 1.2  2001/09/23 00:48:15  eae8264
//               Did basic implementation.  Added syncrhonization.  Left out
//               implementation for stubs where "current running process" is
//               needed.  Other things need to be set in place for this.
//
//               Revision 1.1  2001/09/22 22:03:43  p544-01b
//               Initial revision
//
//

#include "ProcMan.h"
#include "Process.h"
#include "system.h"
#include "thread.h"
#include "filesys.h"
#include <strings.h>

//
// Initialize static vars
//
unsigned char ProcessManager::procID_counter_ = 0;


//
// Name:	(constructor)
//
ProcessManager::ProcessManager(): procCount_( 0 ),
				  processTable_( new List() ),
				  mutex_( new Lock( "ProcessManager Lock" ) ) {
} // constructor

//
// Name: (destructor)
//
ProcessManager::~ProcessManager() {
    delete processTable_;
    delete mutex_;
} // destructor

//
// Name: getProcCount
//
int ProcessManager::getProcCount() {
    DEBUG( 'p', "GETPROCCOUNT called ..." ) ;
    int tmp;

    // obtain a lock, copy the critical data, release the lock
    mutex_->Acquire();
    tmp = procCount_;
    mutex_->Release();

    // return the local (non-critical) data
    return tmp;
}

//
// Name: getProcInfo
//
// ProcCount must equal list size!  (and should)
procInfo_t* ProcessManager::getProcInfo() {
    DEBUG( 'p', "GETPROCINFO called ..." ) ;
    procInfo_t* returnVal = new procInfo_t[ procCount_ ];
    Process* proc = NULL;
    char* srcPtr = NULL;
    char* destPtr = NULL;
    int i;

    mutex_->Acquire();
    ListElement* curElem = processTable_->getFirstItem();

    for ( int listIndex = 0; curElem != NULL; listIndex++ ) {
	proc = (Process *)curElem->item;
	mutex_->Release();
        returnVal[ listIndex ].pid_ = proc->getPID();
        returnVal[ listIndex ].ppid_ = proc->getPPID();
        returnVal[ listIndex ].threadCount_ = proc->getThreadCount();
        returnVal[ listIndex ].joinerCount_ = proc->getJoinCount();
        returnVal[ listIndex ].childCount_ = proc->getChildCount();

	destPtr = &returnVal[ listIndex ].pname_[0];
	srcPtr = proc->getCmdName();
	for ( i = 0; ((destPtr[i] = srcPtr[i]) != 0) && i < MAX_ARG_LENGTH; i++ ) {}
        if ( i < MAX_ARG_LENGTH ) for ( ; i < MAX_ARG_LENGTH; i++ ) destPtr[i] = ' ';

	mutex_->Acquire();
	curElem = curElem->next;
    } // loop

    mutex_->Release();
    return returnVal;
} // getProcInfo

//
// Name: exit
//
int ProcessManager::exit( Process *proc ) {
    DEBUG( 'p', "EXIT called ...\n" ) ;

    // 
    // if this is the original process, or pid = 0
    // it will not have a parent, so do not delete it.

    if(proc == NULL) { // extra redundant bulletproofing
	mutex_->Acquire();
	if(--procCount_ <= 0) {
	    interrupt->Halt();
	}
	mutex_->Release();
	return 0;
    }
    if( proc->getPID() != 0 )
    {
	int parentPID = (int)proc->getPPID();
        mutex_->Acquire();
	Process* procParent = (Process*)processTable_->LookAtSorted(parentPID);
        mutex_->Release();

	procParent->removeChild( proc->getPID() );
    }
    proc->exit();
    mutex_->Acquire();
    processTable_->KeySortedRemove( (int)proc->getPID() );
    delete proc;
    if(--procCount_ <= 0) {
	interrupt->Halt();
    }
    mutex_->Release();

    return 0;
} // exit

//
// Name: exec
// 
// 
int ProcessManager::exec( int argc, int argv ) {
    DEBUG( 'p', "EXEC called...\n" ) ;
    Process* curProc;
    Process* newProc;
    OpenFile* executableFile = NULL;
    int i;

    if ( procCount_ == 0 ) {
        newProc = new Process( procID_counter_, 0, argc, (char*)argv ) ;
	if( newProc->failed() )
	{
	    printf( "Sorry, insufficient memory to run that process\n" ) ;
	    interrupt->Halt() ;
	}
	mutex_->Acquire();
        processTable_->SortedInsert( (void *)newProc, newProc->getPID() );
	newProc->init() ;
        mutex_->Release();
    } else {
	mutex_->Acquire();
	curProc = getCurrentProc();
	mutex_->Release();
	if ( curProc->getChildCount() == MAX_CHILD_PROCS ) return -1;
	char *args = new char[ argc*MAX_ARG_LENGTH ] ;
	bzero( args, argc*MAX_ARG_LENGTH ) ;
	for( i = 0 ; i < argc ; i++ )
	{
	    curProc->addrSpace_->userToKern( argv + (i*MAX_ARG_LENGTH), 
					     1, 
					     MAX_ARG_LENGTH,
					     (void *)(args +(i*MAX_ARG_LENGTH)) ) ;
	}
	DEBUG( 'P', "ProcessManager::exec(): 64 character args vector:\n" );
	for ( i = 0; i < argc*MAX_ARG_LENGTH; i++ )
	    if ( args[i] == 0 ) DEBUG( 'P', "_" );
	    else DEBUG( 'P', "%c", args[i] );
	DEBUG( 'P', "\n" );
	
	executableFile = fSystem->Open( args );
	if ( executableFile == NULL ) return -1;
	delete executableFile;
			 
	newProc = new Process(  procID_counter_,
    				currentThread->getAssociatedPID(),
				argc,
				args
			     ) ;

	if( newProc->failed() )
	{
	    delete newProc;
	    return -2;
	}
	mutex_->Acquire();
        processTable_->SortedInsert( (void *)newProc, newProc->getPID() );
	newProc->init() ;
        mutex_->Release();
	curProc->addChild( newProc->getPID() );
    } // if

    procCount_++;
    procID_counter_++;
    if ( procID_counter_ == 0 ) procID_counter_++; // only one init process...


    DEBUG( 'p', "Leaving EXEC\n" ) ;
    return newProc->getPID() ;
} // exec

//
// Name: join
//
int ProcessManager::join( unsigned char PIDtoJoin ) {
    DEBUG( 'p', "JOIN called ..." ) ;
    int returnVal = 0;

    mutex_->Acquire();
    Process* procToJoin = (Process *)processTable_->LookAtSorted( PIDtoJoin );
    mutex_->Release();

    if ( procToJoin == NULL ) returnVal = 1;
    else {
	procToJoin->addJoiner( currentThread ) ;
	returnVal = currentThread->join( PIDtoJoin ) ;
    }
    
    return returnVal;
} // join

//
// Name: fork
//
int ProcessManager::fork( int func ) {
    DEBUG( 'p', "FORK called ..." ) ;
    Process* curProc = getCurrentProc();
    
    int curPID = curProc->getPID();
    int newTID = curProc->getNextThreadID();
    char* foo = new char[16];
    sprintf(foo,"Thread %i:%i",curPID,newTID);
    Thread* newThread = new Thread( foo, curPID, newTID, curProc );
    
    newThread->space = curProc->getAddrSpace();

    int returnVal = curProc->addThread(newThread);
    if ( returnVal != 0 ) {
        delete newThread;
        return returnVal;
    } // if

    newThread->setUserReg(PCReg, func);
    newThread->setUserReg(NextPCReg, func+4);
    DEBUG('y',"Forking to addr %x\n",func);
    newThread->Fork( ProcessManager::initThread, (int)(newThread->space) );
    
    return 0;
} // fork

//
// Name: killProc
//
int ProcessManager::killProc( int procNum ) {
    DEBUG( 'p', "KILLPROC called on process %d\n", procNum ) ;
    Process* proc = NULL;
    int returnVal = 0;

    mutex_->Acquire();
    proc = (Process *)processTable_->LookAtSorted( procNum );
    mutex_->Release();
    if ( proc == NULL ) returnVal = 1;
    else {
        mutex_->Acquire();
	processTable_->KeySortedRemove( proc->getPID() );
        mutex_->Release();
	proc->exitStatus( 0 );
	proc->exit();
	delete proc;
	procCount_--;
    } // if


    DEBUG( 'p', "KILLPROC exited\n" ) ;
    return returnVal;
} // killProc

//
// Name: getCurrentProc
//
Process* ProcessManager::getCurrentProc() {
    return (Process*)
    	   processTable_->LookAtSorted( currentThread->getAssociatedPID() );
} // getCurrentProc

//
// Name: getProcess
//
Process* ProcessManager::getProcess( int pid ) 
{
    return (Process*) processTable_->LookAtSorted( pid ) ;
}

//
// Name: threadExit
//
int ProcessManager::threadExit( int exitStatus ) {
    DEBUG( 'p', "THREADEXIT\n" ) ;
    int returnVal = 0;
    Thread* threadToKill = NULL;
    Process* proc = NULL;

    proc = getCurrentProc();
    threadToKill = currentThread;
    proc->removeThread((int)(threadToKill->getID()));
    if(proc->getThreadCount() == 0) {
	proc->exitStatus(exitStatus);
	exit(proc);
    }
    
    if ( proc == NULL ) returnVal = 1;
    else {
	threadToKill->Finish();
    } // if
    
    
    DEBUG( 'p', "THREADEXIT done\n" ) ;
    return returnVal;
} // threadExit

//
// Name: threadKill
//
int ProcessManager::threadKill( unsigned char PID, 
				unsigned char threadID,
				int exitStatus ) {
    DEBUG( 'p', "THREADKILL on thread %d, from Process %d\n", threadID, PID ) ;
    int returnVal = 0;
    Thread* threadToKill = NULL;
    Process* proc = NULL;

    mutex_->Acquire();
    proc = (Process *)processTable_->LookAtSorted( PID );
    mutex_->Release();
    
    if ( proc == NULL ) {
	DEBUG( 'p', "OH, shit\n" ) ;
	returnVal = 1;
    }else {
        threadToKill = proc->removeThread( (int)threadID );
	scheduler->unscheduleThread( threadToKill->getCompositeID() );
	if( procCount_ == 1 && proc->getThreadCount() == 0 ) interrupt->Halt() ;
	threadToKill->Finish();
	currentThread->Yield();
    } // if
    
    DEBUG( 'p', "THREADKILL done on thread %d, from Process %d\n", threadID, PID ) ;
    return returnVal;
} // threadKill

//
// initThread
//
void
ProcessManager::initThread( int addressSpace )
{
    DEBUG('X',"initThread1 %i-%i: PC=%x\n",currentThread->getAssociatedPID(),currentThread->getID(),machine->ReadRegister(PCReg));
    currentThread->RestoreUserState();
    ((AddrSpace *)addressSpace)->RestoreState();
//    machine->WriteRegister(PCReg,currentThread->getUserReg(PCReg));
    DEBUG('X',"initThread2: PC=%x\n",machine->ReadRegister(PCReg));
    machine->Run();
} // initThread

//
// threadKiller
//
void
ProcessManager::threadKiller( int proc )
{
    procMan->exit((Process *)proc);
}
