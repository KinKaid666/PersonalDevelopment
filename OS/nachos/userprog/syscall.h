/* syscalls.h 
 * 	Nachos system call interface.  These are Nachos kernel operations
 * 	that can be invoked from user programs, by trapping to the kernel
 *	via the "syscall" instruction.
 *
 *	This file is included by user programs and by the Nachos kernel. 
 *
 * Copyright (c) 1992-1993 The Regents of the University of California.
 * All rights reserved.  See copyright.h for copyright notice and limitation 
 * of liability and disclaimer of warranty provisions.
 */

#ifndef SYSCALLS_H
#define SYSCALLS_H

#include "copyright.h"
#ifndef IN_ASM
#include "ProcessInfo.h"
#include "filestat.h"
#include "vminfo.h"
#endif


/* system call codes -- used by the stubs to tell the kernel which system call
 * is being asked for
 */
#define SC_Halt		0
#define SC_Exit		1
#define SC_Exec		2
#define SC_Join		3
#define SC_Create	4
#define SC_Open		5
#define SC_Read		6
#define SC_Write	7
#define SC_Close	8
#define SC_Fork		9
#define SC_Yield	10

#define SC_ThreadExit   11
#define SC_Kill		12

#define SC_ProcInfo     13
#define SC_GetProcCount 14
#define SC_VMStat	15

#define SC_Stat		16
#define SC_Chmod	17
#define SC_Unlink	18
#define SC_Mkdir	19
#define SC_Move		20
#define SC_Chdir        21
#define SC_CWD          22
#define SC_ListDir	23

#ifndef IN_ASM

/* The system call interface.  These are the operations the Nachos
 * kernel needs to support, to be able to run user programs.
 *

 * Each of these is invoked by a user program by simply calling the 
 * procedure; an assembly language stub stuffs the system call code
 * into a register, and traps to the kernel.  The kernel procedures
 * are then invoked in the Nachos kernel, after appropriate error checking, 
 * from the system call entry point in exception.cc.
 */

/* Stop Nachos, and print out performance stats */
void Halt();		
 
/* Address space control operations: Exit, Exec, and Join */

/* This user program is done (status = 0 means exited normally). */
void Exit(int status);	

/* A unique identifier for an executing user program (address space) */
typedef int SpaceId;	
 
/* Run the executable, stored in the Nachos file "name", and return the 
 * address space identifier
 */
SpaceId Exec(int argc, char *argv);
 
/* Only return once the the user program "id" has finished.  
 * Return the exit status.
 */
int Join(SpaceId id); 	
 

/* File system operations: Create, Open, Read, Write, Close
 * These functions are patterned after UNIX -- files represent
 * both files *and* hardware I/O devices.
 *
 * If this assignment is done before doing the file system assignment,
 * note that the Nachos file system has a stub implementation, which
 * will work for the purposes of testing out these routines.
 */
 
/* A unique identifier for an open Nachos file. */
typedef int OpenFileId;	

/* when an address space starts up, it has two open files, representing 
 * keyboard input and display output (in UNIX terms, stdin and stdout).
 * Read and Write can be used directly on these, without first opening
 * the console device.
 */

#define ConsoleInput	0  
#define ConsoleOutput	1  
 
/* Create a Nachos file, with "name" */
void Create(char *name, int size);

/* Open the Nachos file "name", and return an "OpenFileId" that can 
 * be used to read and write to the file.
 */
OpenFileId Open(char *name, int size);

/* Write "size" bytes from "buffer" to the open file. Returns Error code. */
int Write(char *buffer, int size, OpenFileId id);

/* Read "size" bytes from the open file into "buffer".  
 * Return the number of bytes actually read -- if the open file isn't
 * long enough, or if it is an I/O device, and there aren't enough 
 * characters to read, return whatever is available (for I/O devices, 
 * you should always wait until you can return at least one character).
 * NOTE: Negative return codes for errors!  For details, see Process.h
 */
int Read(char *buffer, int size, OpenFileId id);

/* Close the file, we're done reading and writing to it. */
void Close(OpenFileId id);

/* User-level thread operations: Fork and Yield.  To allow multiple
 * threads to run within a user program. 
 */

/* Fork a thread to run a procedure ("func") in the *same* address space 
 * as the current thread. Returns error code.
 */
int Fork(void (*func)());

/* Yield the CPU to another runnable thread, whether in this address space 
 * or not. 
 */
void Yield();		

// returns error code
int ThreadExit( int exitStatus );

// Returns number of processes copied.
int ProcInfo(procInfo_t *addr);

int GetProcCount();

int VMStat(vminfo_t *info);

int Kill(unsigned char PID);

//
// Name:	Stat
// 
// Description:	gets info about a file
//
// Arguments:	stat:		struct to put the info
//		name:		the name of the file
//		nameSize:	length of the filename
//
// Returns:	0	: success
//		-1	: file not found 
//
int Stat( struct Stat_t* stat, char* name, int nameSize ) ;

//
// Name:	Chmod
//
// Description:	changes the permissions on the file
//
// Arguments:	name:		name of the file
//		nameSize:	length of the filename
//		perms:		new permissions
//
// Returns:	0	: success
//		-1	: file not found
//
int Chmod( char* name, int nameSize, T_BYTE perms ) ;

//
// Name:	Unlink
//
// Description:	remove a file from the system
//
// Arguments:	name:		name of the file
//		nameSize:	length of the filename
//
// Returns:	0	: success
//		-1	: file not found	
//
int Unlink( char* name, int nameSize ) ;

//
// Name:	Mkdir
//
// Description:	create a directory
//
// Arguments:	name:		name of the file
//		nameSize:	length of the filename
//
// Returns:	0	: success
//		-1	: unsuccessful
//
int Mkdir( char* name, int nameSize ) ; 

//
// Name:	Move
//
// Description:	move a file
//
// Arguments:	name:		name of the file
//		nameSize:	length of the filename
//		newName:	new name of the file
//		newNameSize:	length of the new filename
//
// Returns:	0	: success
//		-1	: file not found	
//		-2	: unknown error
//
int Move( char* name, int nameSize, char* newName, int newNameSize ) ;

//
// Name:	Cd
//
// Description:	Change Directory
//
// Arguments:	name:		name of the directory
//		nameSize:	length of the directory name
//
// Returns:	0	: success
//		-1	: directory not found	
//		-2	: unknown error
//
int Cd( char* name, int size);

#endif /* IN_ASM */

#endif /* SYSCALL_H */


