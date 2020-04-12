// File:         $Id: Process.h,v 3.5 2001/11/15 00:19:39 trc2876 Exp $
// Author:       Trevor Clarke, Eric Eells, Eric Ferguson, Dan Westgate
// Contributors: {}
// Description:
// Revisions:
//               $Log: Process.h,v $
//               Revision 3.5  2001/11/15 00:19:39  trc2876
//               cd works now
//
//               Revision 3.4  2001/11/14 23:25:13  trc2876
//               initial Chdir code implemented....needs testing
//               copyAll moved to Makefile
//
//               Revision 3.3  2001/11/14 02:31:22  p544-01b
//               Added Comments
//
//               Revision 3.2  2001/11/13 23:48:27  p544-01b
//               Added cd function
//
//               Revision 3.1  2001/11/13 21:44:07  p544-01b
//               Added currentDir variable
//
//               Revision 3.0  2001/11/04 19:47:03  trc2876
//               force to 3.0
//
//               Revision 2.6  2001/11/02 20:26:58  eae8264
//               Removed VMStats
//
//               Revision 2.5  2001/10/30 17:21:46  eae8264
//               Added getStats() accessor.
//
//               Revision 2.4  2001/10/30 16:32:49  eae8264
//               Added VM stats data member, and call to to print stats
//               in destructor.
//
//               Revision 2.3  2001/10/28 21:11:02  etf2954
//               Added out of mem check
//
//               Revision 2.2  2001/10/28 19:04:53  trc2876
//               moved MAX_ARGS and MAX_ARG_LENGTH to a separate header file
//               cleaned up some uses of "4" and "16" when it should be a #define from above
//               need to audit code for these problems so we can have larger args and more args
//
//               Revision 2.1  2001/10/27 20:59:19  etf2954
//               Added an init function to Process.cc
//               	this way, after a process is created,
//               	one must call init on it.
//
//               This avoids the problem when a process creates
//               	and starts its thread in its constructor
//               	and that thread page faults.
//               	The page fault then tries to get the current
//               	process, but since we're in the current process
//               	it has yet to be added to the list of current
//               	processes and we get a null back from
//               	getCurrentProcess()
//
//               Revision 2.0  2001/10/11 02:53:29  trc2876
//               force update to 2.0 tree
//
//               Revision 1.18  2001/10/07 22:45:57  etf2954
//               Added join functionality
//
//               Revision 1.17  2001/10/07 22:14:57  trc2876
//               fixed Exit() synch problem
//
//               Revision 1.16  2001/10/07 20:45:54  eae8264
//               Added forward reference.  Trevor basically told me what to
//               write.
//
//               Revision 1.15  2001/10/07 20:06:26  trc2876
//               Exit() is grrrrrrreat!!
//
//               Revision 1.14  2001/10/06 23:31:38  eae8264
//               Removed all ASSERTs in Process.cc and replaced them
//               with error codes that go back through ProcessManager
//               and back to exception.cc.  The modification to exception
//               are that Read, Write now return error codes.  Other
//               error codes are just expanding what was already being
//               returned to user-land and others are just unused.
//
//               Revision 1.13  2001/10/03 23:19:15  eae8264
//               Lots of stuff.  Halt works!  :)
//
//               Revision 1.12  2001/10/02 23:03:09  etf2954
//               Made thread a friend
//
//               Revision 1.11  2001/10/02 22:24:28  etf2954
//               Wrote correct constructors
//
//               Revision 1.10  2001/10/02 16:21:45  eae8264
//               Excuse the multiple file update.
//               ProcMan.cc:  Changed threadExit to use Scheduler::unscheduleThread
//               	     Fixed memory leaks.
//               	     Added some explicit casting (minor changes)
//               	     Some code restructuring (superficial)
//               Process.cc:  Had constructor call Scheduler::ReadyToRun on first thread.
//               	     Had exit() call Scheduler::ReadyToRun instead of Run.
//               Process.h:   Changed return values of pid and ppid's to unsigned chars.
//
//               Revision 1.9  2001/09/30 19:44:47  etf2954
//               Changed Process::exit( )  call to conform to exit( int )
//
//               Revision 1.8  2001/09/27 23:59:59  etf2954
//               stuff
//
//               Revision 1.7  2001/09/27 22:27:33  etf2954
//               used Lists instead of int []
//
//               Revision 1.6  2001/09/27 20:47:36  etf2954
//               Still adding stuff
//
//               Revision 1.5  2001/09/27 20:03:29  eae8264
//               Made lotsa changes!  :P
//
//               Revision 1.4  2001/09/27 15:55:00  eae8264
//               Got things to compile.  Implemented getProcInfo().  Added some accessors to Process.  Stuff
//
//               Revision 1.3  2001/09/27 03:38:50  etf2954
//               Added more base functionality
//
//               Revision 1.2  2001/09/26 01:26:32  etf2954
//               Added needed function for thread manipulations, and child process manipulation
//                  and file handle manipulation
//
//               Revision 1.1  2001/09/22 22:03:54  p544-01b
//               Initial revision
//

#ifndef _Process_H
#define _Process_H

#include "list.h"
#include "openfile.h"
#include "addrspace.h"
#include "thread.h"
#include "vminfo.h"
#include "synch.h"
#include "argdefs.h"

#ifdef VM_STATS
#include "VMStats.h"
#endif

class Condition ;
class Thread;
class Lock;
class AddrSpace;

#define MAX_CHILD_PROCS		4
#define MAX_FILE_HANDLES	6	// 4 + the main 2 (stdin, stdout)
#define MAX_THREADS		4
#define MAX_JOINS		4

class Process {

public: // Constructors

    //
    // Name:		(constructor)
    //
    // Description: 	Constructor that will take the arguments to the 
    //			process
    //
    // Arguments:	pid:	id of the process
    //			ppid:	id of the parent
    //			argc:	number of arguments
    //			argv:	args
    //
    // Note:		Client *must* ensure validity of executable name.
    //
    Process( unsigned char pid, unsigned char ppid, int argc, char* argv ) ;

public: // Destructors

    //
    // Name:		(destructor)
    //
    ~Process() ;

    //
    // Name:		exit
    //
    // Description:	tell all your threads to stop
    //
    // Arguments:	none
    //
    void exit() ;
    //
    // Name:		addJoiner
    //
    // Arguments:	a pointer to the threads
    //
    // Description:	Adds a thread id to the list of joiners, so that when 
    //				this process ends it can tell that thread
    //
    // Returns:		Error code, 0 for success
    //				    1 if max joiners, bad thread id, null thread
    //
    int addJoiner   ( Thread *thread ) ;
    int removeJoiner( int threadID ) ;

    //
    // Name:		addThread
    //
    // Arguments:	the id of the thread that has been created
    //
    // Description:	Adds a thread id to the list of threads
    //
    // Returns:		Error code, 0 for success
    //				    1 if max already reached
    //
    int addThread( Thread *thread ) ;

    // 
    // Name:		removeThread
    // 
    // Arguments:	the id of the threa that had to be removed
    //
    // Description:	Removes it from the list of the threads in the process.
    //
    // Returns:		returns a reference to the removed thread, NULL if
    //			threadID is invalid
    //
    Thread *removeThread( int threadID ) ;


    //
    // Name:		addChild
    //
    // Arguments:	the id of the child process that is being created
    //
    // Description:	Adds this childs id to the list of this processes 
    //			children.  This is used when the process 
    //			dies, it has to kill it's children first.
    //
    // Returns:		Error code, 0 for success
    //				    1 if max children already reached, bad pid
    //
    int addChild   ( unsigned char childPid ) ;
    int removeChild( unsigned char childPid ) ;


    //
    // Name:		openFile
    //
    // Arguments:	the name of the file you would like to open
    // 
    // Description:	Open file 'name' and add it to the list of files open.
    //				Also, pass back the id that you use to identify it
    //
    // Return:		the file id (can't be zero or 1),
    //			Error: 0 if max files open,
    //			Error: 1 if invalid name
    //
    int openFile ( char *name ) ;

    //
    // Name:		closeFile
    //
    // Arguments:	the id of the file to close
    //
    // Returns:		Error code, 0 for success
    //				    1 if invalid fileID
    //
    int closeFile( int fileID ) ;

    //
    // Name:		write
    //
    // Arguments:	buffer: what to write
    //			size:	# of bytes to write
    // 			fileID:	id of the file to write to
    //
    // Description:	Write size bytes of buffer into the file that 
    //				coresponds to fileID
    //
    // Returns:		Error code, 0 for success
    //				    1 if invalid fileID
    //				    2 if invalid size
    //				    3 if buffer invalid
    //
    int write( char *buffer, int size, int fileID ) ;

    //
    // Name:		read
    //
    // Arguments:	buffer:	where to read the data
    //			size:	number of bytes to read
    //			fileID:	id of the file to read from
    //
    // Description:	Read size bytes of the file that coresponds to fileID
    //				into buffer
    //
    // Returns:		actual number of bytes read
    //			Error:  -1 if invalid buffer
    //				-2 if invalid file id
    //				-3 if invalid size
    //
    int read( char *buffer, int size, int fileID ) ;

    //
    // Name:		init
    //
    // Arguments:	none
    //
    // Description:	starts up the main thread of execution
    //
    // Returns:		none
    //
    void init() ;

    inline unsigned char getPID()  { return pid_  ; } 
    inline unsigned char getPPID() { return ppid_ ; }
    
    inline int getThreadCount() { return threadCount_; }
    inline int getJoinCount() { return joinCount_; }
    inline int getChildCount() { return childCount_; }

    inline int getNextThreadID() { return TID_counter_++; }

    inline char* getCmdName() { return argv_ ; }

    inline AddrSpace *getAddrSpace() { return addrSpace_ ; } 

    inline void exitStatus( int s ) { exitStatus_ = s; }

    inline Condition* getJoinCondition() { return joinCondition_ ; }

    inline bool failed() { return failed_ ; }
    
#ifdef VM_STATS
    inline VMStats* getStats() { return stats_ ; }
#endif

public: 

    static unsigned int currentFileID ;

private: // Data Members

    int 	argc_ ;
    char       *argv_ ;
    AddrSpace  *addrSpace_ ;
    List       *threadList_ ;
    List       *fileList_ ;
    List       *joinList_ ;
    List       *childList_ ;
    int		threadCount_ ;
    int		childCount_ ;
    int		joinCount_ ;
    int		fileCount_ ;
    Condition  *joinCondition_ ;

    // we only want 8-bit precision so we can put the pid/tid in the same var
    unsigned char	pid_  ;
    unsigned char	ppid_ ;
    
    // thread id's should be unique to a proc
    // we only want 8-bit precision so we can put the pid/tid in the same var
    unsigned char	TID_counter_ ;

    // this stores our exit status for later use
    int			exitStatus_;
    bool		failed_ ;

#ifdef VM_STATS
    // This is the VM stats info for this process
    VMStats* stats_ ;
#endif

public:
    Lock *exiting; // are we in the process of exiting?

    friend class ProcessManager ;
}; // Process

#endif
