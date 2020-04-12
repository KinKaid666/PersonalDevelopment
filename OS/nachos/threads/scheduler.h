// scheduler.h 
//  Data structures for the thread dispatcher and scheduler.
//  Primarily, the list of threads that are ready to run.
//
// Copyright (c) 1992-1993 The Regents of the University of California.
// All rights reserved.  See copyright.h for copyright notice and limitation 
// of liability and disclaimer of warranty provisions.

// $Id: scheduler.h,v 3.0 2001/11/04 19:46:55 trc2876 Exp $

// $Log: scheduler.h,v $
// Revision 3.0  2001/11/04 19:46:55  trc2876
// force to 3.0
//
// Revision 2.0  2001/10/11 02:53:24  trc2876
// force update to 2.0 tree
//
// Revision 1.3  2001/10/08 21:48:18  trc2876
// cleaning up code and comments
//
// Revision 1.2  2001/10/02 16:14:43  eae8264
// Added function unscheduleThread() which is useful in our thread cleanup.
// Currently, it utilized the "hashing" of the List class to accomplish
// this.  One consequence of this is that ReadyToRun threads are now run
// in order according to PID and then by TID (because the list is sorted).
//
// Revision 1.1  2001/09/23 20:19:12  etf2954
// Initial revision
//

#ifndef SCHEDULER_H
#define SCHEDULER_H

#include "copyright.h"
#include "list.h"
#include "thread.h"

// The following class defines the scheduler/dispatcher abstraction -- 
// the data structures and operations needed to keep track of which 
// thread is running, and which threads are ready but not running.

class Scheduler {
  public:
    Scheduler();            // Initialize list of ready threads 
    ~Scheduler();           // De-allocate ready list

    void ReadyToRun(Thread* thread);    // Thread can be dispatched.

    Thread* FindNextToRun();        // Dequeue first thread on the ready 
                                    // list, if any, and return thread.

    void Run(Thread* nextThread);   // Cause nextThread to start running
    void Print();           // Print contents of ready list

    //
    // Name:	unscheduleThread
    //
    // Description: Removed thread from ready queue and returns it
    //
    // Arguments:	The Composite PID/TID of the thread.
    //
    // Returns:	The removed thread, or NULL if didn't exist.
    //
    // Exceptions:	None
    //
    Thread* unscheduleThread( int compositeTID );

  private:
    List *readyList;        // queue of threads that are ready to run,
                // but not running
};

#endif // SCHEDULER_H
