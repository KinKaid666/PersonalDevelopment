// system.h 
//  All global variables used in Nachos are defined here.
//
// Copyright (c) 1992-1993 The Regents of the University of California.
// All rights reserved.  See copyright.h for copyright notice and limitation 
// of liability and disclaimer of warranty provisions.

// $Id: system.h,v 3.1 2001/11/06 20:49:09 eae8264 Exp $

// $Log: system.h,v $
// Revision 3.1  2001/11/06 20:49:09  eae8264
// Put things in place so that there is an "init" thread
// so that the file system can initialize.
//
// Revision 3.0  2001/11/04 19:46:55  trc2876
// force to 3.0
//
// Revision 2.2  2001/11/03 20:42:23  etf2954
// Added filesystem code
//
// Revision 2.1  2001/10/20 19:48:54  etf2954
// Added a global KSwap object
//
// Revision 2.0  2001/10/11 02:53:27  trc2876
// force update to 2.0 tree
//
// Revision 1.10  2001/10/08 21:48:18  trc2876
// cleaning up code and comments
//
// Revision 1.9  2001/10/08 19:50:02  trc2876
// changed preempter timer to use currentThread->Quantum istead of a fixed length
// switch
//
// Revision 1.8  2001/10/04 00:31:55  trc2876
// renamed SynchConsole to SConsole
//
// Revision 1.7  2001/10/04 00:28:15  trc2876
// stupid gcc suxors my arse
//
// Revision 1.6  2001/10/04 00:09:29  trc2876
// added global console
//
// Revision 1.5  2001/09/27 22:42:47  etf2954
// no idea
//
// Revision 1.4  2001/09/27 21:24:29  etf2954
// corrected spelling mistake
//
// Revision 1.3  2001/09/25 01:55:45  trc2876
// last minute fixes....look at the damn diffs
//
// Revision 1.2  2001/09/23 20:24:31  etf2954
// Added the TIME_TO_YIELD define used in TimerInterruptHandler
//
// Revision 1.1  2001/09/23 20:19:12  etf2954
// Initial revision
//

#ifndef SYSTEM_H
#define SYSTEM_H

#include "copyright.h"
#include "utility.h"
#include "thread.h"
#include "scheduler.h"
#include "interrupt.h"
#include "stats.h"
#include "timer.h"
#include "filesys.h"
#include "ProcMan.h"
#include "alarms.h"
#include "SConsole.h"
#include "KSwap.h"

// Initialization and cleanup routines
extern void Initialize(int argc, char **argv);  // Initialization,
                                                // called before anything else
extern void Cleanup();              // Cleanup, called when
                                    // Nachos is done.

extern Thread *currentThread;           // the thread holding the CPU
extern Thread *threadToBeDestroyed;     // the thread that just finished
extern Scheduler *scheduler;            // the ready list
extern Interrupt *interrupt;            // interrupt status
extern Statistics *stats;               // performance metrics
extern Timer *timer;                    // the hardware alarm clock

extern AlarmClock* AlarmManager;        // the manager of alarms
extern unsigned long interruptCount;    // number of timer ints that have occured
extern ProcessManager *procMan ;        // Process list, etc.
extern SConsole *synchConsole ;         // synchronous access to the console

extern KSwap *kswap;			// Memory manager class

extern bool noOS;			// indicates that we are not running
					// nachos-- perhaps we are doing a file
					// copy from UNIX to nachos.

#ifdef USER_PROGRAM
#include "machine.h"
extern Machine* machine;    // user program memory and registers
#endif

#ifdef FILESYS_NEEDED       // FILESYS or FILESYS_STUB 
#include "filesys.h"
extern FileSystem  *fSystem;
#endif

#ifdef FILESYS
#include "synchdisk.h"
extern SynchDisk   *synchDisk;
extern FileSystem  *fileSystem;
#endif

#ifdef NETWORK
#include "post.h"
extern PostOffice* postOffice;
#endif

#endif // SYSTEM_H
