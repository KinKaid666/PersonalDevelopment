// system.cc 
//  Nachos initialization and cleanup routines.
//
// Copyright (c) 1992-1993 The Regents of the University of California.
// All rights reserved.  See copyright.h for copyright notice and limitation 
// of liability and disclaimer of warranty provisions.

// $Id: system.cc,v 3.8 2001/11/14 16:25:08 trc2876 Exp $

// $Log: system.cc,v $
// Revision 3.8  2001/11/14 16:25:08  trc2876
// *** empty log message ***
//
// Revision 3.7  2001/11/14 04:15:40  eae8264
// Yeah... Ksink in the kernel.  These had to be changed.
//
// Revision 3.6  2001/11/14 02:15:30  trc2876
// ls works (not tested from userland)
// nachos -l -d u    dumps the filesystem tree
// nachos -ld -d L   does an ls
//
// Revision 3.5  2001/11/13 22:58:33  trc2876
// in progress
//
// Revision 3.4  2001/11/12 01:50:28  trc2876
// *** empty log message ***
//
// Revision 3.3  2001/11/11 20:29:22  trc2876
// pulled format code out of FileSystem() and into FileSystem::format()
//
// Revision 3.2  2001/11/06 20:49:09  eae8264
// Put things in place so that there is an "init" thread
// so that the file system can initialize.
//
// Revision 3.1  2001/11/05 23:50:46  trc2876
// added mtree files
// class MTreeNode
// class MTreeRawNode
// class MTreeMetaNode
// class MTreeDataNode
//
// These are not complete or tested but they compile cleanly
//
// Revision 3.0  2001/11/04 19:46:55  trc2876
// force to 3.0
//
// Revision 2.3  2001/11/03 20:42:23  etf2954
// Added filesystem code
//
// Revision 2.2  2001/10/26 17:36:26  trc2876
// fixed userTokern and kernToUser problem
//     after executing TLB_Miss, no attempt was may to get the new addr
//
// Revision 2.1  2001/10/20 19:48:54  etf2954
// Added a global KSwap object
//
// Revision 2.0  2001/10/11 02:53:26  trc2876
// force update to 2.0 tree
//
// Revision 1.15  2001/10/08 21:48:18  trc2876
// cleaning up code and comments
//
// Revision 1.14  2001/10/08 19:50:02  trc2876
// changed preempter timer to use currentThread->Quantum istead of a fixed length
// switch
//
// Revision 1.13  2001/10/04 00:31:55  trc2876
// renamed SynchConsole to SConsole
//
// Revision 1.12  2001/10/04 00:28:15  trc2876
// stupid gcc suxors my arse
//
// Revision 1.11  2001/10/04 00:09:29  trc2876
// added global console
//
// Revision 1.10  2001/10/02 20:15:11  eae8264
// Removed initialization of currentThread.
//
// Revision 1.9  2001/09/27 22:42:47  etf2954
// no idea
//
// Revision 1.8  2001/09/27 21:29:08  trc2876
// There was already FileSystem defn in system.cc  I removed the extra
//
// Revision 1.7  2001/09/27 21:24:29  etf2954
// corrected spelling mistake
//
// Revision 1.6  2001/09/27 20:02:27  eae8264
// Added support for composite tid/pid.  Had to modify constructor and
// clients of constructor.
//
// Revision 1.5  2001/09/27 17:02:55  eae8264
// Added the variable to threads to keep track of the associated process.
// It is taken in the constructor.  There is an accessor.  System needed
// to be modified because it created a thread and the constructor is, of
// course, different.
//
// Revision 1.4  2001/09/25 01:55:45  trc2876
// last minute fixes....look at the damn diffs
//
// Revision 1.3  2001/09/23 21:15:21  etf2954
// fixed compiliation bugs
//
// Revision 1.2  2001/09/23 20:23:00  etf2954
// added the functionality so that we are always using at time
// and that it will go off if ++count is a multiple of 4 &&
// interupt status isn't idle
//
// Revision 1.1  2001/09/23 20:19:01  etf2954
// Initial revision
//

#include "copyright.h"
#include "system.h"

// This defines *all* of the global data structures used by Nachos.
// These are all initialized and de-allocated by this file.

Thread *currentThread;          // the thread we are running now
Thread *threadToBeDestroyed;    // the thread that just finished
Scheduler *scheduler;           // the ready list
Interrupt *interrupt;           // interrupt status
Statistics *stats;              // performance metrics
Timer *timer;                   // the hardware timer device,
                                // for invoking context switches
ProcessManager *procMan ;	// object for managing process lists, etc.
SConsole *synchConsole ;	// synchronous console access

KSwap *kswap;			// Memory Manager

bool noOS;

AlarmClock* AlarmManager;       // the alarm clock handler
unsigned long interruptCount=0; // how many timer interrupts have occured?

#ifdef FILESYS_NEEDED
FileSystem  *fSystem;
#endif

#ifdef FILESYS
SynchDisk   *synchDisk;
FileSystem  *fileSystem;
#endif

#ifdef USER_PROGRAM // requires either FILESYS or FILESYS_STUB
Machine *machine;   // user program memory and registers
#endif

#ifdef NETWORK
PostOffice *postOffice;
#endif


// External definition, to allow us to take a pointer to this function
extern void Cleanup();


//----------------------------------------------------------------------
// TimerInterruptHandler
//  Interrupt handler for the timer device.  The timer device is
//  set up to interrupt the CPU periodically (once every TimerTicks).
//  This routine is called each time there is a timer interrupt,
//  with interrupts disabled.
//
//  Note that instead of calling Yield() directly (which would
//  suspend the interrupt handler, not the interrupted thread
//  which is what we wanted to context switch), we set a flag
//  so that once the interrupt handler is done, it will appear as 
//  if the interrupted thread called Yield at the point it
//  was interrupted.
//
//  "dummy" is because every interrupt handler takes one argument,
//      whether it needs it or not.
//----------------------------------------------------------------------
static void
TimerInterruptHandler(int dummy)
{
    // has the current thread used up it's alloted Quantum?
    // if so, force it to Yield
    if(currentThread &&
       currentThread->QuantumUp() && interrupt->getStatus() != IdleMode)
	interrupt->YieldOnReturn();
}

//----------------------------------------------------------------------
// Initialize
//  Initialize Nachos global data structures.  Interpret command
//  line arguments in order to determine flags for the initialization.  
// 
//  "argc" is the number of command line arguments (including the name
//      of the command) -- ex: "nachos -d +" -> argc = 3 
//  "argv" is an array of strings, one for each command line argument
//      ex: "nachos -d +" -> argv = {"nachos", "-d", "+"}
//----------------------------------------------------------------------
void
Initialize(int argc, char **argv)
{
    int argCount;
    char* debugArgs = "";
    bool randomYield = FALSE;
    bool copyOnly = FALSE;
    noOS = FALSE;

#ifdef USER_PROGRAM
    bool debugUserProg = FALSE; // single step user program
    extern unsigned int NumPhysPages;   // pages in physical memory
#endif
#ifdef FILESYS_NEEDED
    bool format = FALSE;    // format disk
#endif
#ifdef NETWORK
    double rely = 1;        // network reliability
    int netname = 0;        // UNIX socket name
#endif
    
    for (argc--, argv++; argc > 0; argc -= argCount, argv += argCount) {
	argCount = 1;
	if (!strcmp(*argv, "-d")) {
	    if (argc == 1)
		debugArgs = "+";    // turn on all debug flags
	    else {
		debugArgs = *(argv + 1);
		argCount = 2;
	    }
	} else if (!strcmp(*argv, "-rs")) {
	    ASSERT(argc > 1);
	    RandomInit(atoi(*(argv + 1)));  // initialize pseudo-random
	    // number generator
	    randomYield = TRUE;
	    argCount = 2;
	}
	if (!strcmp(*argv, "-cp") || !strcmp(*argv, "-p") || !strcmp(*argv,"-l") || !strcmp(*argv,"-ls") || !strcmp( *argv, "-ksink" ) ) {
	    noOS = TRUE;
	    copyOnly = TRUE;
	}

#ifdef USER_PROGRAM
	if (!strcmp(*argv, "-s"))
	    debugUserProg = TRUE;

	// set the number of pages of physical memory
	if (!strcmp(*argv, "-m")) {
	    ASSERT(argc > 1);
	    NumPhysPages = atoi(*(argv + 1));
	    argCount = 2;
	}
#endif
#ifdef FILESYS_NEEDED
	if (!strcmp(*argv, "-f"))
	    format = TRUE;
#endif
#ifdef NETWORK
	if (!strcmp(*argv, "-l")) {
	    ASSERT(argc > 1);
	    rely = atof(*(argv + 1));
	    argCount = 2;
	} else if (!strcmp(*argv, "-i")) {
	    ASSERT(argc > 1);
	    netname = atoi(*(argv + 1));
	    argCount = 2;
	}
#endif
    }

    DebugInit(debugArgs);               // initialize DEBUG messages
    stats = new Statistics();           // collect statistics
    interrupt = new Interrupt;          // start up interrupt handling
    scheduler = new Scheduler();        // initialize the ready queue
    procMan   = new ProcessManager() ;  // initialize the Process table, etc.
    synchConsole   = new SConsole() ;   // new console

    timer = new Timer(TimerInterruptHandler, 0, randomYield); // begin the hardware timer

    AlarmManager = new AlarmClock();
    
    threadToBeDestroyed = NULL;

    // Set to NULL in our design
    currentThread = NULL;

    interrupt->Enable();
    CallOnUserAbort(Cleanup);           // if user hits ctl-C
    
#ifdef USER_PROGRAM
    machine = new Machine(debugUserProg);   // this must come first
#endif

#ifdef FILESYS
    currentThread = new Thread( "init", 0, 4, NULL );
    synchDisk = new SynchDisk("DISK");
    fileSystem = new FileSystem(format);
    if(format) {
	fileSystem->format();
	delete fileSystem;
	exit(0);
    }
    fSystem = fileSystem;
#endif

#ifdef FILESYS_NEEDED
    // we could fix the names of these variables later.
    //fSystem = new FileSystem(format);
#endif

    if ( !copyOnly )
	// KSwap needs to use files, initialize after file
	// system exists.
        kswap = new KSwap();

#ifdef NETWORK
    postOffice = new PostOffice(netname, rely, 10);
#endif

}

//----------------------------------------------------------------------
// Cleanup
//  Nachos is halting.  De-allocate global data structures.
//----------------------------------------------------------------------
void
Cleanup()
{
    printf("\nCleaning up...\n");
#ifdef NETWORK
    delete postOffice;
#endif
    
#ifdef USER_PROGRAM
    delete machine;
#endif

#ifdef FILESYS_NEEDED
    delete fSystem;
#endif

#ifdef FILESYS
    delete synchDisk;
#endif
    
    delete timer;
    delete scheduler;
    delete interrupt;
    delete procMan ;
    delete synchConsole ;
    delete kswap ;
    
    delete AlarmManager;
    
    Exit(0);
}

