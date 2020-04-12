// scheduler.cc 
//  Routines to choose the next thread to run, and to dispatch to
//  that thread.
//
//  These routines assume that interrupts are already disabled.
//  If interrupts are disabled, we can assume mutual exclusion
//  (since we are on a uniprocessor).
//
//  NOTE: We can't use Locks to provide mutual exclusion here, since
//  if we needed to wait for a lock, and the lock was busy, we would 
//  end up calling FindNextToRun(), and that would put us in an 
//  infinite loop.
//
//  Very simple implementation -- no priorities, straight FIFO.
//  Might need to be improved in later assignments.
//
// Copyright (c) 1992-1993 The Regents of the University of California.
// All rights reserved.  See copyright.h for copyright notice and limitation 
// of liability and disclaimer of warranty provisions.

// $Id: scheduler.cc,v 3.1 2001/11/06 20:49:09 eae8264 Exp $

// $Log: scheduler.cc,v $
// Revision 3.1  2001/11/06 20:49:09  eae8264
// Put things in place so that there is an "init" thread
// so that the file system can initialize.
//
// Revision 3.0  2001/11/04 19:46:55  trc2876
// force to 3.0
//
// Revision 2.2  2001/10/27 00:11:11  etf2954
// Added the tlb invalidation
//
// Revision 2.1  2001/10/23 20:23:01  etf2954
// Added call to set the current SegTable
// 	after a context switch is called
//
// Revision 2.0  2001/10/11 02:53:24  trc2876
// force update to 2.0 tree
//
// Revision 1.4  2001/10/08 21:48:18  trc2876
// cleaning up code and comments
//
// Revision 1.3  2001/10/08 19:50:02  trc2876
// changed preempter timer to use currentThread->Quantum istead of a fixed length
// switch
//
// Revision 1.2  2001/10/02 16:14:42  eae8264
// Added function unscheduleThread() which is useful in our thread cleanup.
// Currently, it utilized the "hashing" of the List class to accomplish
// this.  One consequence of this is that ReadyToRun threads are now run
// in order according to PID and then by TID (because the list is sorted).
//
// Revision 1.1  2001/09/23 20:19:01  etf2954
// Initial revision
//

#include "copyright.h"
#include "scheduler.h"
#include "system.h"

//----------------------------------------------------------------------
// Scheduler::Scheduler
//  Initialize the list of ready but not running threads to empty.
//----------------------------------------------------------------------

Scheduler::Scheduler()
{ 
    readyList = new List;
} 

//----------------------------------------------------------------------
// Scheduler::~Scheduler
//  De-allocate the list of ready threads.
//----------------------------------------------------------------------

Scheduler::~Scheduler()
{ 
    delete readyList; 
} 

//----------------------------------------------------------------------
// Scheduler::ReadyToRun
//  Mark a thread as ready, but not running.
//  Put it on the ready list, for later scheduling onto the CPU.
//
//  "thread" is the thread to be put on the ready list.
//----------------------------------------------------------------------

void
Scheduler::ReadyToRun (Thread *thread)
{
    DEBUG('t', "Putting thread %s on ready list.\n", thread->getName());

    thread->setStatus(READY);
    readyList->SortedInsert((void *)thread, thread->getCompositeID());
}

//----------------------------------------------------------------------
// Scheduler::FindNextToRun
//  Return the next thread to be scheduled onto the CPU.
//  If there are no ready threads, return NULL.
// Side effect:
//  Thread is removed from the ready list.
//----------------------------------------------------------------------

Thread *
Scheduler::FindNextToRun ()
{
    return (Thread *)readyList->Remove();
}

//----------------------------------------------------------------------
// Scheduler::Run
//  Dispatch the CPU to nextThread.  Save the state of the old thread,
//  and load the state of the new thread, by calling the machine
//  dependent context switch routine, SWITCH.
//
//      Note: we assume the state of the previously running thread has
//  already been changed from running to blocked or ready (depending).
// Side effect:
//  The global variable currentThread becomes nextThread.
//
//  "nextThread" is the thread to be put into the CPU.
//----------------------------------------------------------------------

void
Scheduler::Run (Thread *nextThread)
{
    Thread *oldThread = currentThread;
    
#ifdef USER_PROGRAM         // ignore until running user programs 
    if (currentThread->space != NULL) { // if this thread is a user program,
        currentThread->SaveUserState(); // save the user's CPU registers
    currentThread->space->SaveState();
    }
#endif
    
    oldThread->CheckOverflow();         // check if the old thread
                        // had an undetected stack overflow

    currentThread = nextThread;         // switch to the next thread
    currentThread->SetQuantum(THREADTIMESLICE); // give the thread it's
						// alloted time
    currentThread->setStatus(RUNNING);      // nextThread is now running
    if (kswap) kswap->setCurSegTable( currentThread->getAssociatedPID() ) ;
    for(int i = 0; i < TLBSize; i++) 
    {
    	machine->tlb[i].valid = FALSE;
    }
    
    DEBUG('t', "Switching from thread \"%s\" to thread \"%s\"\n",
      oldThread->getName(), nextThread->getName());
    
    // This is a machine-dependent assembly language routine defined 
    // in switch.s.  You may have to think
    // a bit to figure out what happens after this, both from the point
    // of view of the thread and from the perspective of the "outside world".

    SWITCH(oldThread, nextThread);
    
    DEBUG('t', "Now in thread \"%s\"\n", currentThread->getName());

    // If the old thread gave up the processor because it was finishing,
    // we need to delete its carcass.  Note we cannot delete the thread
    // before now (for example, in Thread::Finish()), because up to this
    // point, we were still running on the old thread's stack!
    if (threadToBeDestroyed != NULL) {
        delete threadToBeDestroyed;
    threadToBeDestroyed = NULL;
    }
    
#ifdef USER_PROGRAM
    if (currentThread->space != NULL) {     // if there is an address space
        currentThread->RestoreUserState();     // to restore, do it.
    currentThread->space->RestoreState();
    }
#endif
}

//----------------------------------------------------------------------
// Scheduler::Print
//  Print the scheduler state -- in other words, the contents of
//  the ready list.  For debugging.
//----------------------------------------------------------------------
void
Scheduler::Print()
{
    printf("Ready list contents:\n");
    readyList->Mapcar((VoidFunctionPtr) ThreadPrint);
}

//----------------------------------------------------------------------
// Scheduler::unscheduleThread
//  Removes thread from ready list and returns it
//----------------------------------------------------------------------
Thread*
Scheduler::unscheduleThread( int compositeTID ) {
    return (Thread *)readyList->KeySortedRemove( compositeTID );
} // unscheduleThread
