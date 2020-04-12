// main.cc 
//  Bootstrap code to initialize the operating system kernel.
//
//  Allows direct calls into internal operating system functions,
//  to simplify debugging and testing.  In practice, the
//  bootstrap code would just initialize data structures,
//  and start a user program to print the login prompt.
//
//  Most of this file is not needed until later assignments.
//
// Usage: nachos -d <debugflags> -rs <random seed #>
//      -s -x <nachos file> -c <consoleIn> <consoleOut>
//      -f -cp <unix file> <nachos file>
//      -p <nachos file> -r <nachos file> -l -t
//              -n <network reliability> -i <machine id>
//              -o <other machine id>
//              -z
//
//    -d causes certain debugging messages to be printed (cf. utility.h)
//    -rs causes Yield to occur at random (but repeatable) spots
//    -z prints the copyright message
//
//  USER_PROGRAM
//    -s causes user programs to be executed in single-step mode
//    -x runs a user program
//    -c tests the console
//    -m set the number of pages of physical memory
//
//  FILESYS
//    -f causes the physical disk to be formatted
//    -cp copies a file from UNIX to Nachos
//    -p prints a Nachos file to stdout
//    -r removes a Nachos file from the file system
//    -l dumps the directory tree....use with -d u  to get output
//    -ls lists the contents of the Nachos directory
//    -t tests the performance of the Nachos file system
//
//  NETWORK
//    -n sets the network reliability
//    -i sets this machine's host id (needed for the network)
//    -o runs a simple test of the Nachos network software
//
//  NOTE -- flags are ignored until the relevant assignment.
//  Some of the flags are interpreted here; some in system.cc.
//
// Copyright (c) 1992-1993 The Regents of the University of California.
// All rights reserved.  See copyright.h for copyright notice and limitation 
// of liability and disclaimer of warranty provisions.

// $Id: main.cc,v 3.8 2001/11/14 22:00:25 trc2876 Exp $

// $Log: main.cc,v $
// Revision 3.8  2001/11/14 22:00:25  trc2876
// more merging
//
// Revision 3.7  2001/11/14 16:48:49  eae8264
// Trevor fixed a bug.
//
// Revision 3.6  2001/11/14 16:25:07  trc2876
// *** empty log message ***
//
// Revision 3.5  2001/11/14 04:15:40  eae8264
// Yeah... Ksink in the kernel.  These had to be changed.
//
// Revision 3.4  2001/11/14 02:18:08  trc2876
// doc changes and ls() prints always now
//
// Revision 3.3  2001/11/14 02:15:30  trc2876
// ls works (not tested from userland)
// nachos -l -d u    dumps the filesystem tree
// nachos -ld -d L   does an ls
//
// Revision 3.2  2001/11/13 22:58:33  trc2876
// in progress
//
// Revision 3.1  2001/11/12 01:50:28  trc2876
// *** empty log message ***
//
// Revision 3.0  2001/11/04 19:46:55  trc2876
// force to 3.0
//
// Revision 2.0  2001/10/11 02:53:23  trc2876
// force update to 2.0 tree
//
// Revision 1.3  2001/10/08 23:20:26  trc2876
// removed ConsoleTest
//
// Revision 1.2  2001/09/30 20:06:13  eae8264
// Who knows...
//
// Revision 1.1  2001/09/23 20:19:01  etf2954
// Initial revision
//

#define MAIN
#include "copyright.h"
#undef MAIN

#include "utility.h"
#include "system.h"

#include "threadtest.h"

// External functions used by this file

extern void KSinkCmdLine();
extern void ls(char *path);
extern void Copy(char *unixFile, char *nachosFile);
extern void Print(char *),PerformanceTest(void);
extern void StartInitProcess(char *file);
extern void MailTest(int networkID);

//----------------------------------------------------------------------
// main
//  Bootstrap the operating system kernel.  
//  
//  Check command line arguments
//  Initialize data structures
//  (optionally) Call test procedure
//
//  "argc" is the number of command line arguments (including the name
//      of the command) -- ex: "nachos -d +" -> argc = 3 
//  "argv" is an array of strings, one for each command line argument
//      ex: "nachos -d +" -> argv = {"nachos", "-d", "+"}
//----------------------------------------------------------------------

int
main(int argc, char **argv)
{
    int argCount;
                    // for a particular command

    DEBUG('t', "Entering main");
    (void) Initialize(argc, argv);
    
#ifdef THREADS
    ThreadTest::RunTest();
#endif

    for (argc--, argv++; argc > 0; argc -= argCount, argv += argCount) {
	argCount = 1;
	
        if (!strcmp(*argv, "-z"))               // print copyright
            printf (copyright);
#ifdef USER_PROGRAM
        if (!strcmp(*argv, "-x")) {         // run a user program
	    ASSERT(argc > 1);
            StartInitProcess(*(argv + 1));
            argCount = 2;
        } else if (!strcmp(*argv, "-c")) {      // test the console
	    if (argc == 1)
		printf("Not available\n");
		//ConsoleTest(NULL, NULL);
	    else {
		ASSERT(argc > 2);
		printf("Not available\n");
		//ConsoleTest(*(argv + 1), *(argv + 2));
		argCount = 3;
	    }
	    interrupt->Halt();
            // once we start the console, then 
	    // Nachos will loop forever waiting 
	    // for console input
	}
#endif // USER_PROGRAM
#ifdef FILESYS
	if (!strcmp(*argv, "-cp")) {        // copy from UNIX to Nachos
	    ASSERT(argc > 2);
	    Copy(*(argv + 1), *(argv + 2));
	    argCount = 3;
	    delete fileSystem;
	    exit(0);
	} else if (!strcmp(*argv, "-p")) {  // print a Nachos file
	    ASSERT(argc > 1);
	    Print(*(argv + 1));
	    argCount = 2;
	    delete fileSystem;
	    exit(0);
	} else if (!strcmp(*argv, "-r")) {  // remove Nachos file
	    ASSERT(argc > 1);
	    fileSystem->Remove(*(argv + 1));
	    delete fileSystem;
	    argCount = 2;
	} else if (!strcmp(*argv, "-l")) {  // dump file system
           fileSystem->List();
	   exit(0);
	} else if (!strcmp(*argv, "-ksink" )) { // run ksink
	    KSinkCmdLine();
	    delete fileSystem;
	    exit(0);
	} else if (!strcmp(*argv, "-ls")) {  // list Nachos directory
           ls(*(argv + 1));
	   exit(0);
	} else if (!strcmp(*argv, "-t")) {  // performance test
            PerformanceTest();
	}
#endif // FILESYS
#ifdef NETWORK
        if (!strcmp(*argv, "-o")) {
	    ASSERT(argc > 1);
            Delay(2);
            // delay for 2 seconds
	    // to give the user time to 
	    // start up another nachos
            MailTest(atoi(*(argv + 1)));
            argCount = 2;
        }
#endif // NETWORK
    }

    currentThread->Finish();

    // NOTE: if the procedure "main" 
    // returns, then the program "nachos"
    // will exit (as any other normal program
    // would).  But there may be other
    // threads on the ready list.  We switch
    // to those threads by saying that the
    // "main" thread is finished, preventing
    // it from returning.
    return(0);          // Not reached...
}
