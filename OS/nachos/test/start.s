/* Start.s 
 *	Assembly language assist for user programs running on top of Nachos.
 *
 *	Since we don't want to pull in the entire C library, we define
 *	what we need for a user program here, namely Start and the system
 *	calls.
 */

#define IN_ASM
#include "syscall.h"

        .text   
        .align  2

/* -------------------------------------------------------------
 * __start
 *	Initialize running a C program, by calling "main". 
 *
 * 	NOTE: This has to be first, so that it gets loaded at location 0.
 *	The Nachos kernel always starts a program by jumping to location 0.
 * -------------------------------------------------------------
 */

	.globl __start
	.ent	__start
__start:
	jal	main
	move	$4,$0		
	jal	Exit	 /* if we return from main, exit(0) */
	.end __start

/* -------------------------------------------------------------
 * System call stubs:
 *	Assembly language assist to make system calls to the Nachos kernel.
 *	There is one stub per system call, that places the code for the
 *	system call into register r2, and leaves the arguments to the
 *	system call alone (in other words, arg1 is in r4, arg2 is 
 *	in r5, arg3 is in r6, arg4 is in r7)
 *
 * 	The return value is in r2. This follows the standard C calling
 * 	convention on the MIPS.
 * -------------------------------------------------------------
 */

	.globl Halt
	.ent	Halt
Halt:
	addiu $2,$0,SC_Halt
	syscall
	j	$31
	.end Halt

	.globl Exit
	.ent	Exit
Exit:
	addiu $2,$0,SC_Exit
	syscall
	j	$31
	.end Exit

	.globl Exec
	.ent	Exec
Exec:
	addiu $2,$0,SC_Exec
	syscall
	j	$31
	.end Exec

	.globl Join
	.ent	Join
Join:
	addiu $2,$0,SC_Join
	syscall
	j	$31
	.end Join

	.globl Create
	.ent	Create
Create:
	addiu $2,$0,SC_Create
	syscall
	j	$31
	.end Create

	.globl Open
	.ent	Open
Open:
	addiu $2,$0,SC_Open
	syscall
	j	$31
	.end Open

	.globl Read
	.ent	Read
Read:
	addiu $2,$0,SC_Read
	syscall
	j	$31
	.end Read

	.globl Write
	.ent	Write
Write:
	addiu $2,$0,SC_Write
	syscall
	j	$31
	.end Write

	.globl Close
	.ent	Close
Close:
	addiu $2,$0,SC_Close
	syscall
	j	$31
	.end Close

	.globl Fork
	.ent	Fork
Fork:
	addiu $2,$0,SC_Fork
	syscall
	j	$31
	.end Fork

	.globl Yield
	.ent	Yield
Yield:
	addiu $2,$0,SC_Yield
	syscall
	j	$31
	.end Yield

	.globl ThreadExit
	.ent	ThreadExit
ThreadExit:
	addiu $2,$0,SC_ThreadExit
	syscall
	j	$31
	.end ThreadExit

	.globl ProcInfo
	.ent	ProcInfo
	
ProcInfo:
	addiu $2,$0,SC_ProcInfo
	syscall
	j	$31
	.end ProcInfo

	.globl GetProcCount
	.ent	GetProcCount
GetProcCount:
	addiu $2,$0,SC_GetProcCount
	syscall
	j	$31
	.end GetProcCount

	.globl Stat
	.ent	Stat
Stat:
	addiu $2,$0,SC_Stat
	syscall
	j	$31
	.end Stat

	.globl Chmod
	.ent	Chmod
Chmod:
	addiu $2,$0,SC_Chmod
	syscall
	j	$31
	.end Chmod

	.globl CWD
	.ent	CWD
CWD:
	addiu $2,$0,SC_CWD
	syscall
	j	$31
	.end CWD

	.globl Unlink
	.ent	Unlink
Unlink:
	addiu $2,$0,SC_Unlink
	syscall
	j	$31
	.end Unlink

	.globl Mkdir
	.ent	Mkdir
Mkdir:
	addiu $2,$0,SC_Mkdir
	syscall
	j	$31
	.end Mkdir

	.globl Move
	.ent	Move
Move:
	addiu $2,$0,SC_Move
	syscall
	j	$31
	.end Move

	.globl Chdir
	.ent   Chdir
Chdir:
	addiu $2,$0,SC_Chdir
	syscall
	j	$31
	.end Chdir

	.globl ListDir
	.ent   ListDir
ListDir:
	addiu $2,$0,SC_ListDir
	syscall
	j	$31
	.end ListDir

	.globl VMStat
	.ent   VMStat	
VMStat:
	addiu $2,$0,SC_VMStat
	syscall
	j	$31
	.end VMStat

	.globl Kill
	.ent	Kill
Kill:
	addiu $2,$0,SC_Kill
	syscall
	j	$31
	.end Fork

	
/* dummy function to keep gcc happy */
        .globl  __main
        .ent    __main
__main:
        j       $31
        .end    __main

