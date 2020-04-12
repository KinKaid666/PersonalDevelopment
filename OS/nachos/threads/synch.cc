// synch.cc 
//  Routines for synchronizing threads.  Three kinds of
//  synchronization routines are defined here: semaphores, locks 
//      and condition variables (the implementation of the last two
//  are left to the reader).
//
// Any implementation of a synchronization routine needs some
// primitive atomic operation.  We assume Nachos is running on
// a uniprocessor, and thus atomicity can be provided by
// turning off interrupts.  While interrupts are disabled, no
// context switch can occur, and thus the current thread is guaranteed
// to hold the CPU throughout, until interrupts are reenabled.
//
// Because some of these routines might be called with interrupts
// already disabled (Semaphore::V for one), instead of turning
// on interrupts at the end of the atomic operation, we always simply
// re-set the interrupt state back to its original value (whether
// that be disabled or enabled).
//
// Copyright (c) 1992-1993 The Regents of the University of California.
// All rights reserved.  See copyright.h for copyright notice and limitation 
// of liability and disclaimer of warranty provisions.

// $Id: synch.cc,v 3.0 2001/11/04 19:46:55 trc2876 Exp $

// $Log: synch.cc,v $
// Revision 3.0  2001/11/04 19:46:55  trc2876
// force to 3.0
//
// Revision 2.0  2001/10/11 02:53:25  trc2876
// force update to 2.0 tree
//
// Revision 1.3  2001/10/08 23:42:46  trc2876
// Added asMutex to AddrSpace and fixed addrspace zeroing
//
// Revision 1.2  2001/10/07 01:57:15  eae8264
// Added debug statements with character s
//
// Revision 1.1  2001/09/23 20:19:01  etf2954
// Initial revision
//

#include "copyright.h"
#include "synch.h"
#include "system.h"

//----------------------------------------------------------------------
// Semaphore::Semaphore
//  Initialize a semaphore, so that it can be used for synchronization.
//
//  "debugName" is an arbitrary name, useful for debugging.
//  "initialValue" is the initial value of the semaphore.
//----------------------------------------------------------------------

Semaphore::Semaphore(char* debugName, int initialValue)
{
    name = debugName;
    value = initialValue;
    queue = new List;
}

//----------------------------------------------------------------------
// Semaphore::Semaphore
//  De-allocate semaphore, when no longer needed.  Assume no one
//  is still waiting on the semaphore!
//----------------------------------------------------------------------

Semaphore::~Semaphore()
{
    delete queue;
}

//----------------------------------------------------------------------
// Semaphore::P
//  Wait until semaphore value > 0, then decrement.  Checking the
//  value and decrementing must be done atomically, so we
//  need to disable interrupts before checking the value.
//
//  Note that Thread::Sleep assumes that interrupts are disabled
//  when it is called.
//----------------------------------------------------------------------

void
Semaphore::P()
{
    IntStatus oldLevel = interrupt->SetLevel(IntOff);   // disable interrupts
    
    if(value < 1) {            // semaphore not available
      queue->Append((void *)currentThread);   // so go to sleep
      currentThread->Sleep();
    } else {
      value--;          // semaphore available, consume its value
    }
    
    (void) interrupt->SetLevel(oldLevel);   // re-enable interrupts
}

//----------------------------------------------------------------------
// Semaphore::V
//  Increment semaphore value, waking up a waiter if necessary.
//  As with P(), this operation must be atomic, so we need to disable
//  interrupts.  Scheduler::ReadyToRun() assumes that threads
//  are disabled when it is called.
//----------------------------------------------------------------------

void
Semaphore::V()
{
    Thread *thread;
    IntStatus oldLevel = interrupt->SetLevel(IntOff);

    thread = (Thread *)queue->Remove();
    if (thread != NULL) {
      // make thread ready; making it ready consumes the semaphore value
      // just returned by the thread executing this V
      scheduler->ReadyToRun(thread);
    } else {
      value++;          // otherwise the semaphore has one more resource
    }

    (void) interrupt->SetLevel(oldLevel);
}

//----------------------------------------------------------------------
// Lock::lock
//  allocate semaphore (binary semaphore) and set lock name
//----------------------------------------------------------------------
Lock::Lock(char* debugName) {
  name = debugName;
  sem = new Semaphore(debugName, 1);
}


//----------------------------------------------------------------------
// Lock::~lock
//  deallocate semaphore (binary semaphore) 
//----------------------------------------------------------------------
Lock::~Lock() {
  delete sem;
}

//----------------------------------------------------------------------
// Lock::Acquire
//  Do a P() on binary semaphore which is initially one
//----------------------------------------------------------------------
void Lock::Acquire() {
  sem->P();
//  DEBUG( 's', "Thread %d of process %d acquired mutex %s.\n", 
//  	currentThread->getID(),
//	currentThread->getAssociatedPID(),
//	sem->getName()
//	);
}

//----------------------------------------------------------------------
// Lock::Release
//  Do a V() on binary semaphore which will be zero since 
//  an acquire should be done first
//----------------------------------------------------------------------
void Lock::Release() {
  sem->V();
//  DEBUG( 's', "Thread %d of process %d released mutex %s.\n", 
//  	currentThread->getID(),
//	currentThread->getAssociatedPID(),
//	sem->getName()
//	);
}

//----------------------------------------------------------------------
// Condition::Condition
//  allocate queue to hold waiting threads
//  set name of condition variable
//----------------------------------------------------------------------
Condition::Condition(char* debugName) { 
  name = debugName;
  queue = new List;
}

//----------------------------------------------------------------------
// Condition::~Condition
//  deallocate queue to hold waiting threads
//----------------------------------------------------------------------
Condition::~Condition() { 
  delete queue;
}

//----------------------------------------------------------------------
// Condition::Wait
//  release conditionLock
//  provide atomic wait function by disabling interrupts
//  load thread into queue of waiting threads
//  and put it to sleep.  When the thread returns from the sleep
//  function, it must then reacquire the lock.
//----------------------------------------------------------------------
void Condition::Wait(Lock* conditionLock) { 
  
  conditionLock->Release();

  DEBUG( 's', "Thread %d of process %d waits, mutex %s.\n", 
  	currentThread->getID(),
	currentThread->getAssociatedPID(),
	conditionLock->getName()
	);
  IntStatus oldLevel = interrupt->SetLevel(IntOff);  // disable interrupts
  
  queue->Append((void *)currentThread);              // go to sleep
  currentThread->Sleep();

  (void) interrupt->SetLevel(oldLevel);              // re-enable interrupts

  conditionLock->Acquire();
  DEBUG( 's', "Thread %d of process %d acquires mutex %s.\n", 
  	currentThread->getID(),
	currentThread->getAssociatedPID(),
	conditionLock->getName()
	);
}

//----------------------------------------------------------------------
// Condition::Signal
//  provide atomic wait function by disabling interrupts
// Signal dequeues a thread from the queue (if there are any) and 
// puts it on the ready list.
//----------------------------------------------------------------------
void Condition::Signal(Lock* conditionLock) { 
  Thread *thread;
  IntStatus oldLevel = interrupt->SetLevel(IntOff);  // disable interrupts

  thread = (Thread *)queue->Remove();
  if (thread != NULL) {   // make thread ready, consuming the V immediately
        scheduler->ReadyToRun(thread);
  }

  (void) interrupt->SetLevel(oldLevel);             // re-enable interrupts
}

//----------------------------------------------------------------------
// Condition::Broadcast
//  provide atomic wait function by disabling interrupts
// Broadcast dequeues all threads from the queue (if there are any) and 
// puts them on the ready list.
//----------------------------------------------------------------------
void Condition::Broadcast(Lock* conditionLock) { 
  Thread *thread;

  IntStatus oldLevel = interrupt->SetLevel(IntOff);  // disable interrupts
  
  do {                                               //wake-up all threads
     thread = (Thread *)queue->Remove();
     if (thread != NULL) scheduler->ReadyToRun(thread); }
  while (thread != NULL);

  (void) interrupt->SetLevel(oldLevel);          // re-enable interrupts
}
