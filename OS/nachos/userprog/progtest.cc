// progtest.cc 
//	Test routines for demonstrating that Nachos can load
//	a user program and execute it.  
//
//	Also, routines for testing the Console hardware device.
//
// Copyright (c) 1992-1993 The Regents of the University of California.
// All rights reserved.  See copyright.h for copyright notice and limitation 
// of liability and disclaimer of warranty provisions.

#include "copyright.h"
#include "system.h"
#include "console.h"
#include "addrspace.h"
#include "synch.h"
#include "strings.h"
#include "Process.h"

//----------------------------------------------------------------------
// StartProcess
// 	Run a user program.  Open the executable, load it into
//	memory, and jump to it.
//----------------------------------------------------------------------

void
StartInitProcess(char *filename)
{
    OpenFile *executable = fSystem->Open(filename);
    char* fName = new char[MAX_ARG_LENGTH];
    char* destPtr = NULL;

    bzero( fName, MAX_ARG_LENGTH );

    if (executable == NULL) {
	printf("Unable to open file %s\n", filename);
	return;
    }

    destPtr = fName;
    strlcpy( destPtr, filename, MAX_ARG_LENGTH );
    procMan->exec( 1, (int)fName );

    machine->Run();			// jump to the user progam
    ASSERT(FALSE);			// machine->Run never returns;
					// the address space exits
					// by doing the syscall "exit"
}
