// thread.cc 
//  Routines to manage threads.  There are four main operations:
//
//  Fork -- create a thread to run a procedure concurrently
//      with the caller (this is done in two steps -- first
//      allocate the Thread object, then call Fork on it)
//  Finish -- called when the forked procedure finishes, to clean up
//  Yield -- relinquish control over the CPU to another ready thread
//  Sleep -- relinquish control over the CPU, but thread is now blocked.
//      In other words, it will not run again, until explicitly 
//      put back on the ready queue.
//
// Copyright (c) 1992-1993 The Regents of the University of California.
// All rights reserved.  See copyright.h for copyright notice and limitation 
// of liability and disclaimer of warranty provisions.

// $Id: thread.cc,v 3.1 2001/11/06 20:49:09 eae8264 Exp $

// $Log: thread.cc,v $
// Revision 3.1  2001/11/06 20:49:09  eae8264
// Put things in place so that there is an "init" thread
// so that the file system can initialize.
//
// Revision 3.0  2001/11/04 19:46:56  trc2876
// force to 3.0
//
// Revision 2.1  2001/10/30 22:07:34  etf2954
// Thread::Finish corrects
//
// Revision 2.0  2001/10/11 02:53:27  trc2876
// force update to 2.0 tree
//
// Revision 1.13  2001/10/08 21:48:18  trc2876
// cleaning up code and comments
//
// Revision 1.12  2001/10/08 19:50:02  trc2876
// changed preempter timer to use currentThread->Quantum istead of a fixed length
// switch
//
// Revision 1.11  2001/10/07 22:45:28  etf2954
// Added join functionality
//
// Revision 1.10  2001/10/07 20:06:22  trc2876
// Exit() is grrrrrrreat!!
//
// Revision 1.9  2001/10/07 17:33:23  trc2876
// mofo!!! fixed the exec bug....only a few left
//
// Revision 1.8  2001/10/04 00:52:32  eae8264
// There's too much stuff going on to know what I just did....
// stuff...
//
// Revision 1.7  2001/10/03 23:19:11  eae8264
// Lots of stuff.  Halt works!  :)
//
// Revision 1.6  2001/10/02 23:03:26  etf2954
// Added stack allocation and made Process a friend
//
// Revision 1.5  2001/10/02 22:23:55  etf2954
// Added Process as a friend to thread
//
// Revision 1.4  2001/09/30 20:06:14  eae8264
// Who knows...
//
// Revision 1.3  2001/09/27 20:02:27  eae8264
// Added support for composite tid/pid.  Had to modify constructor and
// clients of constructor.
//
// Revision 1.2  2001/09/27 17:02:55  eae8264
// Added the variable to threads to keep track of the associated process.
// It is taken in the constructor.  There is an accessor.  System needed
// to be modified because it created a thread and the constructor is, of
// course, different.
//
// Revision 1.1  2001/09/23 20:19:01  etf2954
// Initial revision
//

#include "copyright.h"
#include "thread.h"
#include "switch.h"
#include "synch.h"
#include "system.h"

#define STACK_FENCEPOST 0xdeadbeef  // this is put at the top of the
				    // execution stack, for detecting 
				    // stack overflows

//----------------------------------------------------------------------
// Thread::Thread
//  Initialize a thread control block, so that we can then call
//  Thread::Fork.
//
//  "threadName" is an arbitrary string, useful for debugging.
//----------------------------------------------------------------------

Thread::Thread(char* threadName,
	       unsigned char associatedPID,
	       unsigned char threadID,
	       Process* associatedProc)
{
    strncpy(name,threadName,THREADNAMESIZE);
    name[THREADNAMESIZE - 1] = '\0';
    joinLock_ = new Lock( "Join Lock" ) ;

    assocProc = associatedProc;
    procID_ = associatedPID;
    TID_ = threadID;
    compositeID_ = procID_ | 0;
    compositeID_ <<= 8;
    compositeID_ = compositeID_ | threadID;
    stackTop = NULL;
    stack = NULL;
    status = JUST_CREATED;
#ifdef USER_PROGRAM
    space = NULL;
#endif
}

//----------------------------------------------------------------------
// Thread::~Thread
//  De-allocate a thread.
//
//  NOTE: the current thread *cannot* delete itself directly,
//  since it is still running on the stack that we need to delete.
//
//      NOTE: if this is the main thread, we can't delete the stack
//      because we didn't allocate it -- we got it automatically
//      as part of starting up Nachos.
//----------------------------------------------------------------------

Thread::~Thread()
{
    DEBUG('t', "Deleting thread \"%s\"\n", name);

    ASSERT(this != currentThread);
    if (stack != NULL)
	DeallocBoundedArray((char *) stack, StackSize * sizeof(int));
    delete joinLock_ ;
}

//----------------------------------------------------------------------
// Thread::Fork
//  Invoke (*func)(arg), allowing caller and callee to execute 
//  concurrently.
//
//  NOTE: although our definition allows only a single integer argument
//  to be passed to the procedure, it is possible to pass multiple
//  arguments by making them fields of a structure, and passing a pointer
//  to the structure as "arg".
//
//  Implemented as the following steps:
//      1. Allocate a stack
//      2. Initialize the stack so that a call to SWITCH will
//      cause it to run the procedure
//      3. Put the thread on the ready queue
//  
//  "func" is the procedure to run concurrently.
//  "arg" is a single argument to be passed to the procedure.
//----------------------------------------------------------------------

void 
Thread::Fork(VoidFunctionPtr func, int arg)
{
    DEBUG('t', "Forking thread \"%s\" with func = 0x%x, arg = %d\n",
      name, (int) func, arg);
    
    StackAllocate(func, arg);

    IntStatus oldLevel = interrupt->SetLevel(IntOff);
#ifdef USER_PROGRAM
    Quantum = THREADTIMESLICE;          // give this some initial quantum for
                                        // running
#endif
    scheduler->ReadyToRun(this);    // ReadyToRun assumes that interrupts 
				    // are disabled!
    (void) interrupt->SetLevel(oldLevel);
}    

//----------------------------------------------------------------------
// Thread::CheckOverflow
//  Check a thread's stack to see if it has overrun the space
//  that has been allocated for it.  If we had a smarter compiler,
//  we wouldn't need to worry about this, but we don't.
//
//  NOTE: Nachos will not catch all stack overflow conditions.
//  In other words, your program may still crash because of an overflow.
//
//  If you get bizarre results (such as seg faults where there is no code)
//  then you *may* need to increase the stack size.  You can avoid stack
//  overflows by not putting large data structures on the stack.
//  Don't do this: void foo() { int bigArray[10000]; ... }
//----------------------------------------------------------------------

void
Thread::CheckOverflow()
{
    if (stack != NULL)
#ifdef HOST_SNAKE           // Stacks grow upward on the Snakes
    ASSERT(stack[StackSize - 1] == STACK_FENCEPOST);
#else
    ASSERT(*stack == (int)STACK_FENCEPOST);
#endif
}
//----------------------------------------------------------------------
// Thread::Finish
//  Called by ThreadRoot when a thread is done executing the 
//  forked procedure.
//
//  NOTE: we don't immediately de-allocate the thread data structure 
//  or the execution stack, because we're still running in the thread 
//  and we're still on the stack!  Instead, we set "threadToBeDestroyed", 
//  so that Scheduler::Run() will call the destructor, once we're
//  running in the context of a different thread.
//
//  NOTE: we disable interrupts, so that we don't get a time slice 
//  between setting threadToBeDestroyed, and going to sleep.
//----------------------------------------------------------------------

//
void
Thread::Finish ()
{
    // we removed the requirement that the current thread only
    // can call this....otherwise, our complex chain that handles
    // process exiting is tough to implement...
    (void) interrupt->SetLevel(IntOff);     
    
    DEBUG('t', "Finishing thread \"%s\"\n", getName());
    
    threadToBeDestroyed = this;
    if(assocProc)
    	if(assocProc->getAddrSpace())
		assocProc->getAddrSpace()->freeStack( initStackPointer ) ;
    if(this == currentThread && !noOS)
    			      // even tho any thread can call finish
    			      // at any time, we only switch if the
			      // current thread calls it
	Sleep();              // invokes SWITCH
    // reached only if calling thread is not currentThread
}

//----------------------------------------------------------------------
// Thread::Yield
//  Relinquish the CPU if any other thread is ready to run.
//  If so, put the thread on the end of the ready list, so that
//  it will eventually be re-scheduled.
//
//  NOTE: returns immediately if no other thread on the ready queue.
//  Otherwise returns when the thread eventually works its way
//  to the front of the ready list and gets re-scheduled.
//
//  NOTE: we disable interrupts, so that looking at the thread
//  on the front of the ready list, and switching to it, can be done
//  atomically.  On return, we re-set the interrupt level to its
//  original state, in case we are called with interrupts disabled. 
//
//  Similar to Thread::Sleep(), but a little different.
//----------------------------------------------------------------------

void
Thread::Yield ()
{
    Thread *nextThread;
    IntStatus oldLevel = interrupt->SetLevel(IntOff);
    
    ASSERT(this == currentThread);
    
    DEBUG('T', "Yielding thread \"%s\"\n", getName());

#ifdef USER_PROGRAM
    Quantum = 0;             // remove any remaining time slice
#endif
    nextThread = scheduler->FindNextToRun();
    if (nextThread != NULL) {
	scheduler->ReadyToRun(this);
	scheduler->Run(nextThread);
    }
    (void) interrupt->SetLevel(oldLevel);
}

//----------------------------------------------------------------------
// Thread::Sleep
//  Relinquish the CPU, because the current thread is blocked
//  waiting on a synchronization variable (Semaphore, Lock, or Condition).
//  Eventually, some thread will wake this thread up, and put it
//  back on the ready queue, so that it can be re-scheduled.
//
//  NOTE: if there are no threads on the ready queue, that means
//  we have no thread to run.  "Interrupt::Idle" is called
//  to signify that we should idle the CPU until the next I/O interrupt
//  occurs (the only thing that could cause a thread to become
//  ready to run).
//
//  NOTE: we assume interrupts are already disabled, because it
//  is called from the synchronization routines which must
//  disable interrupts for atomicity.   We need interrupts off 
//  so that there can't be a time slice between pulling the first thread
//  off the ready list, and switching to it.
//----------------------------------------------------------------------
void
Thread::Sleep ()
{
    Thread *nextThread;
    
    ASSERT(this == currentThread);
    ASSERT(interrupt->getLevel() == IntOff);
    
    DEBUG('T', "Sleeping thread \"%s\"\n", getName());

    status = BLOCKED;
    while ((nextThread = scheduler->FindNextToRun()) == NULL)
	interrupt->Idle();  // no one to run, wait for an interrupt
        
    scheduler->Run(nextThread); // returns when we've been signalled
}

//----------------------------------------------------------------------
// ThreadFinish, InterruptEnable, ThreadPrint
//  Dummy functions because C++ does not allow a pointer to a member
//  function.  So in order to do this, we create a dummy C function
//  (which we can pass a pointer to), that then simply calls the 
//  member function.
//----------------------------------------------------------------------

static void ThreadFinish()    { currentThread->Finish(); }
static void InterruptEnable() { interrupt->Enable(); }
void ThreadPrint(int arg){ Thread *t = (Thread *)arg; t->Print(); }

//----------------------------------------------------------------------
// Thread::StackAllocate
//  Allocate and initialize an execution stack.  The stack is
//  initialized with an initial stack frame for ThreadRoot, which:
//      enables interrupts
//      calls (*func)(arg)
//      calls Thread::Finish
//
//  "func" is the procedure to be forked
//  "arg" is the parameter to be passed to the procedure
//----------------------------------------------------------------------

void
Thread::StackAllocate (VoidFunctionPtr func, int arg)
{
    stack = (int *) AllocBoundedArray(StackSize * sizeof(int));

    // Get a handle to our process and the corresponding addrspace.
    //	then set the correct userRegister to our stack (that is
    //	in MIPS/USER land.
    if(assocProc) { // don't do this for kernel threads
	initStackPointer = assocProc->getAddrSpace()->allocStack() ;
	userRegisters[StackReg] = initStackPointer ;
    }

#ifdef HOST_SNAKE
    // HP stack works from low addresses to high addresses
    stackTop = stack + 16;  // HP requires 64-byte frame marker
    stack[StackSize - 1] = STACK_FENCEPOST;
#else
    // i386 & MIPS & SPARC stack works from high addresses to low addresses
#ifdef HOST_SPARC
    // SPARC stack must contains at least 1 activation record to start with.
    stackTop = stack + StackSize - 96;
#else  // HOST_MIPS  || HOST_i386
    stackTop = stack + StackSize - 4;   // -4 to be on the safe side!
#ifdef HOST_i386
    // the 80386 passes the return address on the stack.  In order for
    // SWITCH() to go to ThreadRoot when we switch to this thread, the
    // return addres used in SWITCH() must be the starting address of
    // ThreadRoot.
    *(--stackTop) = (int)ThreadRoot;
#endif
#endif  // HOST_SPARC
    *stack = STACK_FENCEPOST;
#endif  // HOST_SNAKE
    
    machineState[PCState] = (int) ThreadRoot;
    machineState[StartupPCState] = (int) InterruptEnable;
    machineState[InitialPCState] = (int) func;
    machineState[InitialArgState] = arg;
    machineState[WhenDonePCState] = (int) ThreadFinish;
}

#ifdef USER_PROGRAM
#include "machine.h"

//----------------------------------------------------------------------
// Thread::SaveUserState
//  Save the CPU state of a user program on a context switch.
//
//  Note that a user program thread has *two* sets of CPU registers -- 
//  one for its state while executing user code, one for its state 
//  while executing kernel code.  This routine saves the former.
//----------------------------------------------------------------------

void
Thread::SaveUserState()
{
    for (int i = 0; i < NumTotalRegs; i++)
	userRegisters[i] = machine->ReadRegister(i);
}

//----------------------------------------------------------------------
// Thread::RestoreUserState
//  Restore the CPU state of a user program on a context switch.
//
//  Note that a user program thread has *two* sets of CPU registers -- 
//  one for its state while executing user code, one for its state 
//  while executing kernel code.  This routine restores the former.
//----------------------------------------------------------------------

void
Thread::RestoreUserState()
{
    for (int i = 0; i < NumTotalRegs; i++)
	machine->WriteRegister(i, userRegisters[i]);
}
#endif

//----------------------------------------------------------------------
//Thread::GoToSleepFor
//Put a thread to sleep for a specified number of timer ticks
//----------------------------------------------------------------------
void
Thread::GoToSleepFor(int howLong)
{
    AlarmManager->SetAlarm(howLong,this);    // set an alarm with alarm manager
}

//----------------------------------------------------------------------
//Thread::progLoad
//Loads a MIPS program into MIPS-land registers
//----------------------------------------------------------------------
void
Thread::progLoad(int arg)
{
    AddrSpace* as = (AddrSpace *)arg;
    
    as->InitRegisters();
    as->RestoreState();
} // progLoad
//----------------------------------------------------------------------
//Thread::join
//joins on a process
//----------------------------------------------------------------------
int
Thread::join( unsigned char pid ) 
{
    Process* p = (Process*)procMan->getProcess( pid ) ;

    DEBUG('j', "Join %i to %i\n",procMan->getCurrentProc()->getPID(),pid);
    if( p == NULL ) return -1 ;
    p->getJoinCondition()->Wait( joinLock_ ) ;
    return  machine->ReadRegister( 20 ) ;
} // join
