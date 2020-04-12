// addrspace.cc 
//	Routines to manage address spaces (executing user programs).
//
//	In order to run a user program, you must:
//
//	1. link with the -N -T 0 option 
//	2. run coff2noff to convert the object file to Nachos format
//		(Nachos object code format is essentially just a simpler
//		version of the UNIX executable object code format)
//	3. load the NOFF file into the Nachos file system
//		(if you haven't implemented the file system yet, you
//		don't need to do this last step)
//
// Copyright (c) 1992-1993 The Regents of the University of California.
// All rights reserved.  See copyright.h for copyright notice and limitation 
// of liability and disclaimer of warranty provisions.

#include "copyright.h"
#include "system.h"
#include "addrspace.h"
#include "noff.h"

#include <strings.h>

#include "bitmap.h"
#include "machine.h"
#include "synch.h"
#include "Process.h"

#ifndef VM
BitMap allocedPages(NumPhysPages);
Lock allocedPagesMutex("allocedPages Lock");
#endif

//----------------------------------------------------------------------
// SwapHeader
// 	Do little endian to big endian conversion on the bytes in the 
//	object file header, in case the file was generated on a little
//	endian machine, and we're now running on a big endian machine.
//----------------------------------------------------------------------

static void 
SwapHeader (NoffHeader *noffH)
{
	noffH->noffMagic = WordToHost(noffH->noffMagic);
	noffH->code.size = WordToHost(noffH->code.size);
	noffH->code.virtualAddr = WordToHost(noffH->code.virtualAddr);
	noffH->code.inFileAddr = WordToHost(noffH->code.inFileAddr);
	noffH->initData.size = WordToHost(noffH->initData.size);
	noffH->initData.virtualAddr = WordToHost(noffH->initData.virtualAddr);
	noffH->initData.inFileAddr = WordToHost(noffH->initData.inFileAddr);
	noffH->uninitData.size = WordToHost(noffH->uninitData.size);
	noffH->uninitData.virtualAddr = WordToHost(noffH->uninitData.virtualAddr);
	noffH->uninitData.inFileAddr = WordToHost(noffH->uninitData.inFileAddr);
}

//----------------------------------------------------------------------
// AddrSpace::AddrSpace
// 	Create an address space to run a user program.
//	Load the program from a file "executable", and set everything
//	up so that we can start executing user instructions.
//
//	Assumes that the object code file is in NOFF format.
//
//	First, set up the translation from program memory to physical 
//	memory.  For now, this is really simple (1:1), since we are
//	only uniprogramming, and we have a single unsegmented page table
//
//	"executable" is the file containing the object code to load into memory
//----------------------------------------------------------------------

AddrSpace::AddrSpace(OpenFile *executable,unsigned char pid) :
	    swapLock(new Semaphore("as-swap-lock",0)),
	    allocedStacks(new BitMap(MAX_THREADS)),
	    asMutex(new Lock("allocedStacks mutex")),
	    pid_(pid),
	    failed_( FALSE )
{
    NoffHeader noffH;
    unsigned int size, codePages;

    executable->ReadAt((char *)&noffH, sizeof(noffH), 0);
    if ((noffH.noffMagic != NOFFMAGIC) && 
	(WordToHost(noffH.noffMagic) == NOFFMAGIC))
    	SwapHeader(&noffH);
    ASSERT(noffH.noffMagic == NOFFMAGIC);

    // The default script file will page align the first data page.
    // The last code page may have unused space in it.  All of the
    // data sections are contiguous.  (Actually, .data and .rdata
    // are both page aligned but the coff2noff program can only
    // handle one of them in the program so there is no data page
    // with unused space.)
    //
    // And it has to be increased to handle the stack.
    codePages = divRoundUp(noffH.code.size,PageSize);
    numPages = codePages +
  	       divRoundUp(noffH.initData.size + noffH.uninitData.size +
			  UserStackSize * MAX_THREADS, PageSize);
    size = numPages * PageSize;

#ifndef VM
    // check we're not trying to run anything too big -- at least until
    // we have virtual memory.
    ASSERT(numPages <= NumPhysPages);
#endif

    DEBUG('a', "Initializing address space, num pages %d, size %d\n", 
					numPages, size);
    if((kswap->allocMemForPID(pid_,numPages,&noffH,executable) == -1) ||
       (kswap->loadDataPages(pid_,&noffH,executable)) == -1) {
	failed_ = TRUE ;
	return;
    }
#ifndef VM
// first, set up the translation 
    pageTable = new TranslationEntry[numPages];
    for (i = 0; i < numPages; i++) {
	pageTable[i].virtualPage = i;
	pageTable[i].physicalPage = allocPhysMemPage();
	ASSERT(pageTable[i].physicalPage != -1);

	pageTable[i].valid = TRUE;
	pageTable[i].use = FALSE;
	pageTable[i].dirty = FALSE;
	// set code pages to read-only
	pageTable[i].readOnly = (i < codePages) ? TRUE : FALSE;
    }
#endif

// create a bitmap for the user stacks and alloc the first stack
//
    allocedStacks->Mark(0);
    
#ifndef VM
// zero out the entire address space, to zero the unitialized data segment 
// and the stack segment
    char *zeros = new char[size];
    bzero(zeros,size);
    kernToUser(0,1,size,zeros);
    delete zeros;

// then, copy in the code and data segments into memory
    if (noffH.code.size > 0) {
        DEBUG('a', "Initializing code segment, at 0x%x, size %d\n", 
			noffH.code.virtualAddr, noffH.code.size);
	char *tmp = new char[noffH.code.size];
        executable->ReadAt(tmp, noffH.code.size, noffH.code.inFileAddr);
	
	kernToUser(noffH.code.virtualAddr,1,noffH.code.size,tmp);
	delete tmp;
    }
    if (noffH.initData.size > 0) {
        DEBUG('a', "Initializing data segment, at 0x%x, size %d\n", 
			noffH.initData.virtualAddr, noffH.initData.size);
	char *tmp = new char[noffH.initData.size];
        executable->ReadAt(tmp, noffH.initData.size, noffH.initData.inFileAddr);

	kernToUser(noffH.initData.virtualAddr,1,noffH.initData.size,tmp);
	delete tmp;
    }
#endif
}

//----------------------------------------------------------------------
// AddrSpace::~AddrSpace
// 	Dealloate an address space.  Nothing for now!
//----------------------------------------------------------------------

AddrSpace::~AddrSpace()
{
#ifndef VM
    for ( unsigned int i = 0; i < numPages; i++) {
	freePhysMemPage(pageTable[i].physicalPage);
    }
    delete pageTable;
#endif
    if( !failed_ )
        kswap->freeMemFromPID(pid_);
    delete allocedStacks;
    delete swapLock;
    delete asMutex;
}

//----------------------------------------------------------------------
// AddrSpace::InitRegisters
// 	Set the initial values for the user-level register set.
//
// 	We write these directly into the "machine" registers, so
//	that we can immediately jump to user code.  Note that these
//	will be saved/restored into the currentThread->userRegisters
//	when this thread is context switched out.
//----------------------------------------------------------------------

void
AddrSpace::InitRegisters()
{
    int i;

    for (i = 0; i < NumTotalRegs; i++)
	machine->WriteRegister(i, 0);

    // Initial program counter -- must be location of "Start"
    machine->WriteRegister(PCReg, 0);	

    // Need to also tell MIPS where next instruction is, because
    // of branch delay possibility
    machine->WriteRegister(NextPCReg, 4);

   // Set the first stack register to the end of the address space, where we
   // allocated the stack; but subtract off a bit, to make sure we don't
   // accidentally reference off the end!
#ifdef VM
    machine->WriteRegister(StackReg, TOP_OF_ADDRSPACE - 16 + 1);
    DEBUG('a', "Initializing stack register to %d\n", TOP_OF_ADDRSPACE - 16);
#else
    machine->WriteRegister(StackReg, numPages * PageSize - 16);
    DEBUG('a', "Initializing stack register to %d\n", numPages * PageSize - 16);
#endif
}

//----------------------------------------------------------------------
// AddrSpace::SaveState
// 	On a context switch, save any machine state, specific
//	to this address space, that needs saving.
//
//	For now, nothing!
//----------------------------------------------------------------------

void AddrSpace::SaveState() 
{}

//----------------------------------------------------------------------
// AddrSpace::RestoreState
// 	On a context switch, restore the machine state so that
//	this address space can run.
//
//      For now, tell the machine where to find the page table.
//----------------------------------------------------------------------

void AddrSpace::RestoreState() 
{
#ifndef VM
    machine->pageTable = pageTable;
    machine->pageTableSize = numPages;
#endif
}

#ifndef VM
//----------------------------------------------------------------------
// AddrSpace::allocPhysMemPage
// 	Find a free memory page, mark it used, and return it
//----------------------------------------------------------------------
int AddrSpace::allocPhysMemPage()
{
    int tmp;

    // obtain a lock, copy the critical data, release the lock
    //allocedPagesMutex.Acquire();
    tmp = allocedPages.Find();
    //allocedPagesMutex.Release();

    return tmp;
}

//----------------------------------------------------------------------
// AddrSpace::freePhysMemPage
// 	Free a memory page by clearing it's allocedPages entry
//----------------------------------------------------------------------
void AddrSpace::freePhysMemPage(int pageNum)
{
    allocedPagesMutex.Acquire();
    allocedPages.Clear(pageNum);
    allocedPagesMutex.Release();
}
#endif

//----------------------------------------------------------------------
// AddrSpace::userToKern
// 	Copy count items of size from mips-land addr to kernel-land buffer
//----------------------------------------------------------------------
int AddrSpace::userToKern( int addr, int size, int count, void *buffer )
{
    int physicalAddress;
    unsigned int vpn,offset;
    for(int i = 0; i < count; i++) {
	vpn = (unsigned) (addr + size * i) / PageSize;
	offset = (unsigned) (addr + size * i) % PageSize;
#ifdef VM
findTLBEntry:
	for(int j = 0; j < TLBSize; j++) {
	    if(machine->tlb[j].valid &&
	      ((unsigned int)machine->tlb[j].virtualPage == vpn)) {
		physicalAddress = machine->tlb[j].physicalPage *
				  PageSize + offset;
		goto noMiss;
	    }
	}
	TLB_Miss(vpn * PageSize);
	goto findTLBEntry;
noMiss:

#else
	ASSERT(vpn < numPages);
	physicalAddress = pageTable[vpn].physicalPage * PageSize + offset;
#endif

	switch(size) {
	    case 1:
		*((char *)buffer + i * sizeof(char)) = machine->mainMemory[physicalAddress];
		break;
	    case 2:
		*((unsigned short *)buffer + i * sizeof(unsigned short)) =
			ShortToHost(*(unsigned short *) &machine->mainMemory[physicalAddress]);
		break;
	    case 4:
		*((unsigned int *)buffer + i * sizeof(unsigned int)) =
			WordToHost(*(unsigned int *) &machine->mainMemory[physicalAddress]);
		break;
	}
    }
    return 0 ; // TODO actual return type here
}

//----------------------------------------------------------------------
// AddrSpace::kernToUser
// 	Copy count items of size from kernel-land buffer to mips-land addr
//----------------------------------------------------------------------
int AddrSpace::kernToUser( int addr, int size, int count, void *buffer )
{
    int physicalAddress;
    unsigned int vpn,offset;
    for(int i = 0; i < (count * size); i++) {
	vpn = (unsigned) (addr + size * i) / PageSize;
	offset = (unsigned) (addr + size * i) % PageSize;
#ifdef VM
findTLBEntry:
	for(int j = 0; j < TLBSize; j++) {
	    if(machine->tlb[j].valid &&
	      ((unsigned int)machine->tlb[j].virtualPage == vpn)) {
		physicalAddress = machine->tlb[j].physicalPage *
				  PageSize + offset;
		goto noMiss;
	    }
	}
	TLB_Miss(vpn * PageSize);
	goto findTLBEntry;
noMiss:

#else
	ASSERT(vpn < numPages);
	physicalAddress = pageTable[vpn].physicalPage * PageSize + offset;
#endif

	switch(size) {
	    case 1:
		machine->mainMemory[physicalAddress] = ((unsigned char *)buffer)[i] & 0xff;
		break;
	    case 2:
		*(unsigned short *) &machine->mainMemory[physicalAddress] =
			ShortToMachine((unsigned short) ((unsigned short *)buffer)[i] & 0xffff);
		break;
	    case 4:
		*(unsigned int *) &machine->mainMemory[physicalAddress] =
			WordToMachine((unsigned int) ((unsigned int *)buffer)[i]);
		break;
	}
    }
    return 0 ; // TODO actual return type here
}

//----------------------------------------------------------------------
// AddrSpace::copyArgs
// 	Copy argc and argv to clean addressSpace
//	clean meaning no threads have been started
//	we put these on the initial thread's stack
//	some liberties taken: we assume the initial thread has
//		been created so we can access it's stack
//		also, no other threads should have been created
//----------------------------------------------------------------------
int AddrSpace::copyArgs(int argc, char *argv, int tos, Thread* userThread)
{
#ifdef VM
    int argvAddr;

    argvAddr = kswap->copyArgs(argc, argv, this);
    userThread->setUserReg( 4, argc );
    userThread->setUserReg( 5, argvAddr );

    return tos;
#else
    int argvAddr,argcAddr;
    tos -= argc * MAX_ARG_LENGTH;
    argvAddr = tos;
    tos -= 4;
    argcAddr = tos;

    kernToUser(argcAddr, 4, 1, &argc);
    kernToUser(argvAddr, 1, argc * MAX_ARG_LENGTH, argv);

    userThread->setUserReg( 4, argc );
    userThread->setUserReg( 5, argvAddr );

    tos -= tos % 4;

    return tos;
#endif
}

//----------------------------------------------------------------------
// AddrSpace::allocStack
// 	Return a pointer to a new stack, or NULL for an error
//----------------------------------------------------------------------
int AddrSpace::allocStack()
{
    int stackPage,stackAddress;
    
    asMutex->Acquire();
    if((stackPage = allocedStacks->Find()) == -1) {
	// sorry, no stack space left
	asMutex->Release();
	return NULL;
    }
    asMutex->Release();
    
    // our stacks grow up so we start at the end of our address space
    // and subtract the appropriate number of stacks
    // the 16 gives us a few words extra in case we overrun the stack.
#ifdef VM
    stackAddress = TOP_OF_ADDRSPACE - (stackPage * UserStackSize) - 16;
    stackAddress++; // fix alignment problem
#else
    stackAddress = (numPages * PageSize) - (stackPage * UserStackSize) - 16;
#endif
    
    DEBUG('j',"Stack address is 0x%08x\n",stackAddress);
    return stackAddress;
}

//----------------------------------------------------------------------
// AddrSpace::freeStack
// 	Free an allocated stack and zero it out
//----------------------------------------------------------------------
int AddrSpace::freeStack(int stackAddress)
{
    int stackPage;

    // find the stack page to free by inverting the function in allocStack
    //
#ifdef VM
    stackPage = (TOP_OF_ADDRSPACE - (stackAddress + 16)) / UserStackSize;
#else
    stackPage = ((numPages * PageSize) - (stackAddress + 16)) / UserStackSize;
#endif

    asMutex->Acquire();
    allocedStacks->Clear(stackPage);
    asMutex->Release();

#ifndef VM
    bzero(machine->mainMemory+(stackAddress+16-UserStackSize),UserStackSize);
#endif

    return 0;
}

#ifndef VM
//----------------------------------------------------------------------
// AddrSpace::vmstat
// 	Return information about memory allocation
//----------------------------------------------------------------------
int AddrSpace::vmstat(vminfo_t *info)
{
    allocedPagesMutex.Acquire();
    info->freePages = allocedPages.NumClear();
    info->totalPages = allocedPages.NumBits();
    DEBUG('V',"PAGES - Free: %i  Total: %i\n",info->freePages,info->totalPages);
    allocedPagesMutex.Release();
    asMutex->Acquire();
    info->freeStacks = allocedStacks->NumClear();
    info->totalStacks = allocedStacks->NumBits();
    asMutex->Release();
    DEBUG('V',"STACKS- Free: %i  Total: %i\n",info->freeStacks,info->totalStacks);
    return 0;
}

#endif

#ifdef VM
void AddrSpace::TLB_Miss(int address)
{
    int pAddr;
    int i;
    int curPCvPage;
    int curPCentry;
    bool ro; // is the page read only?

    pAddr = kswap->getPhysAddr( currentThread->getAssociatedPID(),
    				address,
				swapLock,
				&ro );

    ASSERT( pAddr != -1 );
    curPCvPage = machine->ReadRegister( PCReg ) / PageSize;
    
    for(i = 0; i < 4 && machine->tlb[i].virtualPage != curPCvPage; i++);
    curPCentry = i; // get the TLB index for the PC's page entry
    DEBUG( 'K', "PC/TLB entry is %d\n", curPCentry );

    for(i = 0; i < 4; i++) {
	if(machine->tlb[i].valid)
	    kswap->updateRevPageTblEntry(&(machine->tlb[i]));
	if(!machine->tlb[i].valid && i != curPCentry) {
	    machine->tlb[i].virtualPage = address / PageSize;
	    machine->tlb[i].physicalPage = pAddr / PageSize;
	    machine->tlb[i].valid = true;
	    machine->tlb[i].readOnly = ro;
	    machine->tlb[i].use = false;
	    machine->tlb[i].dirty = false;
	    return;
	}
    }
    for(i = 0; i < 4; i++) {
	if(!machine->tlb[i].use && i != curPCentry) {
	    machine->tlb[i].virtualPage = address / PageSize;
	    machine->tlb[i].physicalPage = pAddr / PageSize;
	    machine->tlb[i].valid = true;
	    machine->tlb[i].readOnly = ro;
	    machine->tlb[i].use = false;
	    machine->tlb[i].dirty = false;
	    return;
	}
    }

    i = (((Random()%3) + 1) + curPCentry ) % 4;
    ASSERT( curPCentry != i );
    machine->tlb[i].virtualPage = address / PageSize;
    machine->tlb[i].physicalPage = pAddr / PageSize;
    machine->tlb[i].valid = true;
    machine->tlb[i].readOnly = ro;
    machine->tlb[i].use = false;
    machine->tlb[i].dirty = false;
}
#endif
