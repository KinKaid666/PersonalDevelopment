// File:         $Id: ProcMan.h,v 3.0 2001/11/04 19:47:03 trc2876 Exp $
// Author:       Trevor Clarke, Eric Eells, Eric Ferguson, Dan Westgate
// Contributors: {}
// Description:
// Revisions:
//               $Log: ProcMan.h,v $
//               Revision 3.0  2001/11/04 19:47:03  trc2876
//               force to 3.0
//
//               Revision 2.0  2001/10/11 02:53:29  trc2876
//               force update to 2.0 tree
//
//               Revision 1.17  2001/10/07 20:36:31  etf2954
//               Changed the returns status of ::exec
//
//               Revision 1.16  2001/10/07 20:06:26  trc2876
//               Exit() is grrrrrrreat!!
//
//               Revision 1.15  2001/10/06 23:31:38  eae8264
//               Removed all ASSERTs in Process.cc and replaced them
//               with error codes that go back through ProcessManager
//               and back to exception.cc.  The modification to exception
//               are that Read, Write now return error codes.  Other
//               error codes are just expanding what was already being
//               returned to user-land and others are just unused.
//
//               Revision 1.14  2001/10/06 20:50:32  p544-01b
//               Changed name of function DoNothing to initThread
//
//               Revision 1.13  2001/10/06 20:17:00  eae8264
//               Supported return of error code.
//
//               Revision 1.12  2001/10/06 15:19:20  trc2876
//               Renamed ThreadExit() syscall to ThreadKill()
//               added ThreadExit() syscall which exits current thread (still broken)
//               Fixed Fork() system call
//
//               Revision 1.11  2001/10/06 00:15:57  trc2876
//               in progress Fork fixes
//
//               Revision 1.10  2001/10/02 22:46:01  etf2954
//               Added getProcess( int pid) that returns a Process *
//
//               Revision 1.9  2001/10/02 19:39:52  p544-01b
//               Made getCurrentProc public
//
//               Revision 1.8  2001/09/28 01:49:05  eae8264
//               Updated threadExit with the latest signature.
//
//               Revision 1.7  2001/09/27 22:30:55  eae8264
//               Sruff....  Good Stuff!
//
//               Revision 1.6  2001/09/27 20:03:29  eae8264
//               Made lotsa changes!  :P
//
//               Revision 1.5  2001/09/27 15:55:00  eae8264
//               Got things to compile.  Implemented getProcInfo().  Added some accessors to Process.  Stuff
//
//               Revision 1.4  2001/09/25 01:55:47  trc2876
//               last minute fixes....look at the damn diffs
//
//               Revision 1.3  2001/09/24 20:16:52  trc2876
//               added locking code to getProcCount() and getProcInfo()
//               added threadExit()
//               function ptr -> int in fork() since we need a user-land address here
//               moved new Process() outside critical section in exec() since it is not
//                  a critical resource
//
//               Revision 1.2  2001/09/23 00:48:15  eae8264
//               Added comments to stubs.  Added lock to class state.
//
//               Revision 1.1  2001/09/22 22:03:54  p544-01b
//               Initial revision
//

#ifndef _ProcessManager_H
#define _ProcessManager_H

#include "syscall.h"
#include "list.h"
#include "ProcessInfo.h"
#include "synch.h"
#include "Process.h"

class ProcessManager {

public: // Constructors

	//
	// Name:	(constructor)
	//
	// Description: Just initializes variables.
	//
	// Arguments:	None
	//
	ProcessManager();

public: // Destructors

	//
	// Name:	(destructor)
	//
	// Description: Kills and cleans up all processes.
	//
	// Arguments:	None
	//
	~ProcessManager();

public: // Client functions

	//
	// Name:	getProcCount
	//
	// Description: Returns how many processes are running.
	//		This will block if mutex_ is locked
	//
	// Arguments:	None
	//
	// Returns:	int - Number of processes
	//
	// Exceptions:	None
	//
	int getProcCount() ;

	//
	// Name:	getProcInfo
	//
	// Description: Returns a struct* with useful info about a process.
	//		Returns NULL otherwise.
	//		This will block if mutex_ is locked
	//
	// Arguments:	None
	//
	// Returns:	A pointer to a struct.
	//
	// Exceptions:	None
	//
	procInfo_t *getProcInfo();

	//
	// Name:	exit
	//
	// Description: The exit system call calls this.  The calling process
	//		will then be removed.
	//
	// Arguments:	the Process to exit
	//
	// Returns:	Error code, 0 for success
	//
	// Exceptions:	None
	//
	int exit( Process *proc );

	//
	// Name:	threadExit
	//
	// Description: The threadExit system call calls this. The calling
	//		thread exits...if there are no threads left in the
	//		process, it will be removed.
	//
	// Arguments:	the status of the thread exit.
	//
	// Returns:	Error code, 0 for success
	//			    1 if invalid process id specified
	//
	// Exceptions:	None
	//
	int threadExit( int exitStatus );

	//
	// Name:	threadKill
	//
	// Description: The threadKill system call calls this. The calling
	//		thread exits...if there are no threads left in the
	//		process, it will be removed.
	//
	// Arguments:	ID of the Process that the thread belongs to, 
	//		thread ID, 
	//		the status of the thread exit.
	//
	// Returns:	Error code, 0 for success
	//			    1 if invalid process id specified
	//
	// Exceptions:	None
	//
	int threadKill( unsigned char PID,
			unsigned char threadID,
			int exitStatus
		       );

	//
	// Name:	exec
	//
	// Description: Creates a new process and starts it.
	//
	// Arguments:	file name, number of args, arg vector (char**)
	//
	// Returns:	-1 for error
	//	 	pid of new process
	//
	// Exceptions:	None
	//
	int exec( int argc, int argv );

	//
	// Name:	join
	//
	// Description: The calling process will join the process specified
	//		with the argument addr.
	//
	// Arguments:	None
	//
	// Returns:	Error code, 0 for success, 
	//			    1 if invalid process id specified
	//		See additional error codes from Process.h
	//
	// Exceptions:	None
	//
	int join( unsigned char PIDtoJoin );

	//
	// Name:	fork
	//
	// Description: Starts a new thread for the calling process.  The
	//		pointer is the entry point in MIPS land.
	//
	// Arguments:	A function address
	//
	// Returns:	Error code, 0 for success
	//		See additional error codes from Process.h
	//
	// Exceptions:	None
	//
	int fork( int func );

	//
	// Name:	killProc
	//
	// Description: Orders that a process be removed.
	//
	// Arguments:	The process number.
	//
	// Returns:	Error code, 0 for success
	//			    1 if invalid process id specified
	//
	// Exceptions:	None
	//
	int killProc( int procNum );

	//
	// Name:	getCurrentProc
	//
	// Description: Get's the current process using the id contained within
	//		the system's currentThread
	//
	// Arguments:	None
	//
	// Returns:	Process pointer
	//
	// Exceptions:	None
	//
	Process* getCurrentProc();

	// 
	// Name:	getProcess
	//
	// Description:	get a process handle with associated pid
	//
	// Arguments:	the id of the process that you would like to get.
	//
	// Returns:	a handle to that process
	// 
        Process* getProcess( int pid ) ;

	//
	// Name:	initThread
	//
	// Description: static function used to create a new thread
	//
	// Arguments:	the address space
	//
	// Returns:	void
	//
	static void initThread(int addressSpace) ;

	//
	// Name:	threadKiller
	//
	// Description: static function used to kill a process
	//		it needs to exist to properly kill
	//		all threads in a proc...thus it's name
	//
	// Arguments:	pointer to the process, cast to int
	//
	// Returns:	void
	//
	static void threadKiller(int proc);

private: // Data Members

	// Number of processes...
	unsigned char procCount_;
	
	// Process table...
	List *processTable_;
	
	// To protect the process table (synchlist doesn't support keys) and 
	// the process counter.
	Lock *mutex_;
	
	// We only want the process id to have 8-bit precision so we can use
	// a sixteen bit proc/thread id for threads.
	// Note: char is unsigned so overflow is okay.  Will wrap to zero... 
	static unsigned char procID_counter_;

}; // ProcessManager

#endif
