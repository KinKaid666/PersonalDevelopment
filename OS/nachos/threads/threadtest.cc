// threadtest.cc 
//  Simple test case for the threads assignment.
//
//  Create two threads, and have them context switch
//  back and forth between themselves by calling Thread::Yield, 
//  to illustratethe inner workings of the thread system.
//
// Copyright (c) 1992-1993 The Regents of the University of California.
// All rights reserved.  See copyright.h for copyright notice and limitation 
// of liability and disclaimer of warranty provisions.

// $Id: threadtest.cc,v 3.0 2001/11/04 19:46:56 trc2876 Exp $

// $Log: threadtest.cc,v $
// Revision 3.0  2001/11/04 19:46:56  trc2876
// force to 3.0
//
// Revision 2.0  2001/10/11 02:53:27  trc2876
// force update to 2.0 tree
//
// Revision 1.4  2001/10/03 23:19:11  eae8264
// Lots of stuff.  Halt works!  :)
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
// Revision 1.1  2001/09/23 20:19:01  etf2954
// Initial revision
//

#include "copyright.h"
#include "system.h"

#include "threadtest.h"

// count the number of threads that have exited.
int ThreadTest::exits = 0;

//----------------------------------------------------------------------
// ThreadTest::SimpleThread
//  Loop 5 times, yielding the CPU to another ready thread 
//  each iteration.
//
//  "which" is simply a number identifying the thread, for debugging
//  purposes.
//----------------------------------------------------------------------

void
ThreadTest::SimpleThread(int arg)
{
    ThreadTest* tester = (ThreadTest*)arg;
    
    int num;
    
    for (num = 0; num < 5; num++) {
    printf("*** thread %d looped %d times\n", tester->id, num);
        currentThread->Yield();
    }

    // Increment the thread exit count.
    exits++;
}

//----------------------------------------------------------------------
// ThreadTest::RunTest()
//  Set up a ping-pong between two threads, by forking a thread 
//  to call SimpleThread, and then calling SimpleThread ourselves.
//----------------------------------------------------------------------

void
ThreadTest::RunTest()
{
    ThreadTest::exits = 0;

    ThreadTest* tester1 = new ThreadTest(1);
    ThreadTest* tester2 = new ThreadTest(2);
    
    DEBUG('t', "Entering SimpleTest");

    Thread *t1 = new Thread("forked thread 1", 0, 0, 0);
    Thread *t2 = new Thread("forked thread 2", 0, 1, 0);
    
    t1->Fork(SimpleThread,(int) tester1);
    t2->Fork(SimpleThread,(int) tester2);

    while(exits < 2)
	currentThread->Yield();

    // When the two threads have exited halt the system.  This explicit
    // halt is needed because the alarm system generates a periodic
    // pending interrupt.  This will never allow Nachos to cleanly
    // determine that no kernel thread is ready-to-run.  The next alarm
    // might make a thread runnable.  It will be your responsibility to
    // halt the machine when it is appropriate.
    interrupt->Halt();
}

