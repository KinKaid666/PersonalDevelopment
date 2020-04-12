// thread.h 
//  Data structures for managing threads.  A thread represents
//  sequential execution of code within a program.
//  So the state of a thread includes the program counter,
//  the processor registers, and the execution stack.
//  
//  Note that because we allocate a fixed size stack for each
//  thread, it is possible to overflow the stack -- for instance,
//  by recursing to too deep a level.  The most common reason
//  for this occuring is allocating large data structures
//  on the stack.  For instance, this will cause problems:
//
//      void foo() { int buf[1000]; ...}
//
//  Instead, you should allocate all data structures dynamically:
//
//      void foo() { int *buf = new int[1000]; ...}
//
//
//  Bad things happen if you overflow the stack, and in the worst 
//  case, the problem may not be caught explicitly.  Instead,
//  the only symptom may be bizarre segmentation faults.  (Of course,
//  other problems can cause seg faults, so that isn't a sure sign
//  that your thread stacks are too small.)
//  
//  One thing to try if you find yourself with seg faults is to
//  increase the size of thread stack -- ThreadStackSize.
//
//      In this interface, forking a thread takes two steps.
//  We must first allocate a data structure for it: "t = new Thread".
//  Only then can we do the fork: "t->fork(f, arg)".
//
// Copyright (c) 1992-1993 The Regents of the University of California.
// All rights reserved.  See copyright.h for copyright notice and limitation 
// of liability and disclaimer of warranty provisions.

// $Id: thread.h,v 3.1 2001/11/13 00:33:14 trc2876 Exp $

// $Log: thread.h,v $
// Revision 3.1  2001/11/13 00:33:14  trc2876
// core dumps on a new
//
// Revision 3.0  2001/11/04 19:46:56  trc2876
// force to 3.0
//
// Revision 2.0  2001/10/11 02:53:27  trc2876
// force update to 2.0 tree
//
// Revision 1.9  2001/10/07 22:45:28  etf2954
// Added join functionality
//
// Revision 1.8  2001/10/07 20:45:18  eae8264
// Added forward reference.
//
// Revision 1.7  2001/10/04 00:52:32  eae8264
// There's too much stuff going on to know what I just did....
// stuff...
//
// Revision 1.6  2001/10/03 23:19:11  eae8264
// Lots of stuff.  Halt works!  :)
//
// Revision 1.5  2001/10/02 23:03:27  etf2954
// Added stack allocation and made Process a friend
//
// Revision 1.4  2001/10/02 22:23:55  etf2954
// Added Process as a friend to thread
//
// Revision 1.3  2001/09/27 20:02:28  eae8264
// Added support for composite tid/pid.  Had to modify constructor and
// clients of constructor.
//
// Revision 1.2  2001/09/27 17:02:55  eae8264
// Added the variable to threads to keep track of the associated process.
// It is taken in the constructor.  There is an accessor.  System needed
// to be modified because it created a thread and the constructor is, of
// course, different.
//
// Revision 1.1  2001/09/23 20:19:12  etf2954
// Initial revision
//


#ifndef THREAD_H
#define THREAD_H

#include "copyright.h"
#include "utility.h"

#ifdef USER_PROGRAM
#include "machine.h"
#include "addrspace.h"
#include "Process.h"
#include "synch.h"

class Lock ;
class Process;
class AddrSpace;

#define THREADTIMESLICE 2
#endif

#define THREADNAMESIZE 17

// CPU register state to be saved on context switch.  
// The SPARC and MIPS only need 10 registers, but the Snake needs 18.
// For simplicity, this is just the max over all architectures.
#define MachineStateSize 18 


// Size of the thread's private execution stack.
// WATCH OUT IF THIS ISN'T BIG ENOUGH!!!!!
#define StackSize   (8 * 1024)  // in words


// Thread state
enum ThreadStatus { JUST_CREATED, RUNNING, READY, BLOCKED };

// external function, dummy routine whose sole job is to call Thread::Print
extern void ThreadPrint(int arg);    

// The following class defines a "thread control block" -- which
// represents a single thread of execution.
//
//  Every thread has:
//     an execution stack for activation records ("stackTop" and "stack")
//     space to save CPU registers while not running ("machineState")
//     a "status" (running/ready/blocked)
//    
//  Some threads also belong to a user address space; threads
//  that only run in the kernel have a NULL address space.

class Thread {
  private:
    // NOTE: DO NOT CHANGE the order of these first two members.
    // THEY MUST be in this position for SWITCH to work.
    int* stackTop;           // the current stack pointer
    int machineState[MachineStateSize];  // all registers except for stackTop

  public:
    Thread(char* debugName, 
    	   unsigned char associatedPID,
	   unsigned char threadID,
	   Process* associatedProc
	   ); 
    ~Thread();              // deallocate a Thread
                    // NOTE -- thread being deleted
                    // must not be running when delete 
                    // is called
		    

    //
    // Name:	progLoad
    //
    // Description: The thread loading function for MIPS user programs...
    //		to be passes as the function to fork
    //
    // Arguments:	A pointer to an AddrSpace object.
    //
    // Returns:	None
    //
    // Exceptions:	None
    //
    static void progLoad( int arg );

    //
    // Name:		join
    //
    // Description: 	join on a process and wait for exit status
    //
    // Arguments:	the process pid
    //
    // Returns:		the exit status of that process
    //
    int join( unsigned char pid ) ;
    // basic thread operations

    void Fork(VoidFunctionPtr func, int arg);   // Make thread run (*func)(arg)
    void Yield();               // Relinquish the CPU if any 
                        // other thread is runnable
    void Sleep();               // Put the thread to sleep and 
                        // relinquish the processor

    void GoToSleepFor(int howLong);     // put thread to sleep for a while 

    void Finish();               // The thread is done executing
    
    void CheckOverflow();               // Check if thread has 
                        // overflowed its stack
    void setStatus(ThreadStatus st) { status = st; }
    char* getName() { return (name); }
    void Print() { printf("%s, ", name); }
    unsigned char getAssociatedPID() { return procID_; }
    unsigned char getID() { return TID_; }
    int getCompositeID() { return compositeID_; }
    int getUserReg( int regNum ) { return userRegisters[ regNum ]; }
    void setUserReg( int regNum, int newVal )
    	{ userRegisters[regNum] = newVal; }

  private:
    // some of the private data for this class is listed above
    
    int initStackPointer ;
    // To know the associated process...
    unsigned char procID_;
    
    // Thread must know it's id
    unsigned char TID_;
    
    // Ptr to associated process
    Process* assocProc;
    
    // the composite pid/tid
    // tid in bits 0-7, pid in bits 8-15
    int compositeID_;

    int* stack;             // Bottom of the stack 
                    // NULL if this is the main thread
                    // (If NULL, don't deallocate stack)
    ThreadStatus status;        // ready, running or blocked
    char name[THREADNAMESIZE];

    void StackAllocate(VoidFunctionPtr func, int arg);
                        // Allocate a stack for thread.
                    // Used internally by Fork()

#ifdef USER_PROGRAM
// A thread running a user program actually has *two* sets of CPU registers -- 
// one for its state while executing user code, one for its state 
// while executing kernel code.

    int userRegisters[NumTotalRegs];    // user-level CPU register state
    int Quantum;    // make sure thread get its quantum time-slice
    Lock* joinLock_ ;
    
  public:
    void SaveUserState();       // save user-level register state
    void RestoreUserState();        // restore user-level register state

    void SetQuantum(int Alloc) {Quantum = Alloc;}  // set the threads quantum
                        // decrement quantum and return true if used up
    bool QuantumUp() {return ((--Quantum <= 0) ? true : false);}
    
    AddrSpace *space;           // User code this thread is running.
#endif
};

// Magical machine-dependent routines, defined in switch.s

extern "C" {
// First frame on thread execution stack; 
//      enable interrupts
//  call "func"
//  (when func returns, if ever) call ThreadFinish()
void ThreadRoot();

// Stop running oldThread and start running newThread
void SWITCH(Thread *oldThread, Thread *newThread);

}

#endif // THREAD_H
