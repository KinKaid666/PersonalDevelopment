// File:         $Id: Process.cc,v 3.8 2001/11/15 04:51:02 etf2954 Exp $
// Author:       Trevor Clarke, Eric Eells, Eric Ferguson, Dan Westgate
// Contributors: {}
// Description:
// Revisions:
//               $Log: Process.cc,v $
//               Revision 3.8  2001/11/15 04:51:02  etf2954
//               Fixed rm
//
//               Revision 3.7  2001/11/15 00:19:39  trc2876
//               cd works now
//
//               Revision 3.6  2001/11/14 23:25:13  trc2876
//               initial Chdir code implemented....needs testing
//               copyAll moved to Makefile
//
//               Revision 3.5  2001/11/14 02:25:32  p544-01b
//               Release Node
//
//               Revision 3.4  2001/11/13 23:48:30  p544-01b
//               Added cd function
//
//               Revision 3.3  2001/11/07 00:23:45  eae8264
//               Added use of threadToBeDestroyed for init()
//               because of assertion in ~Thread().
//
//               Revision 3.2  2001/11/07 00:14:03  eae8264
//               Fixed when things were being deleted for failed
//               constructors.
//
//               Revision 3.1  2001/11/06 20:50:05  eae8264
//               Changed situation for initial process so that the
//               "init" thread is destroyed.  The init thread is
//               a thread used in initializing the file system.
//
//               Revision 3.0  2001/11/04 19:47:03  trc2876
//               force to 3.0
//
//               Revision 2.7  2001/11/02 20:26:58  eae8264
//               Removed VMStats
//
//               Revision 2.6  2001/10/30 16:32:48  eae8264
//               Added VM stats data member, and call to to print stats
//               in destructor.
//
//               Revision 2.5  2001/10/28 21:11:02  etf2954
//               Added out of mem check
//
//               Revision 2.4  2001/10/27 20:59:18  etf2954
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
//               Revision 2.3  2001/10/25 20:50:41  trc2876
//               nicely handle errors in AddrSpace creation
//
//               Revision 2.2  2001/10/24 23:27:35  trc2876
//               store pid in addrspace
//
//               Revision 2.1  2001/10/24 22:54:45  trc2876
//               added pid to AddrSpace to simplify KSwap calls
//
//               Revision 2.0  2001/10/11 02:53:29  trc2876
//               force update to 2.0 tree
//
//               Revision 1.40  2001/10/08 23:08:16  trc2876
//               code cleanup
//
//               Revision 1.39  2001/10/07 22:45:57  etf2954
//               Added join functionality
//
//               Revision 1.38  2001/10/07 22:14:57  trc2876
//               fixed Exit() synch problem
//
//               Revision 1.37  2001/10/07 20:45:54  eae8264
//               Added forward reference.  Trevor basically told me what to
//               write.
//
//               Revision 1.36  2001/10/07 20:06:26  trc2876
//               Exit() is grrrrrrreat!!
//
//               Revision 1.35  2001/10/07 17:33:28  trc2876
//               mofo!!! fixed the exec bug....only a few left
//
//               Revision 1.34  2001/10/07 02:26:52  eae8264
//               Minor changes
//
//               Revision 1.33  2001/10/06 23:31:38  eae8264
//               Removed all ASSERTs in Process.cc and replaced them
//               with error codes that go back through ProcessManager
//               and back to exception.cc.  The modification to exception
//               are that Read, Write now return error codes.  Other
//               error codes are just expanding what was already being
//               returned to user-land and others are just unused.
//
//               Revision 1.32  2001/10/06 22:14:32  etf2954
//               Corrected the exit call
//
//               Revision 1.31  2001/10/06 17:47:16  trc2876
//               Does exiting, etc. really work now?
//
//               Revision 1.30  2001/10/06 17:16:13  etf2954
//               Fixed removeing thread twice problem
//
//               Revision 1.29  2001/10/06 17:15:19  trc2876
//               working on threadKill problem...eric needs updated code
//
//               Revision 1.28  2001/10/06 15:19:20  trc2876
//               Renamed ThreadExit() syscall to ThreadKill()
//               added ThreadExit() syscall which exits current thread (still broken)
//               Fixed Fork() system call
//
//               Revision 1.27  2001/10/05 22:32:39  p544-01b
//               Added Assert to Check for Valid file name
//
//               Revision 1.26  2001/10/05 21:22:10  eae8264
//               Added a comment
//
//               Revision 1.25  2001/10/05 01:40:36  eae8264
//               Fixed constructor so that the init thread is not
//               scheduled twice.
//
//               Revision 1.24  2001/10/04 21:53:32  trc2876
//               BUGS list....
//
//               Revision 1.23  2001/10/04 00:52:36  eae8264
//               There's too much stuff going on to know what I just did....
//               stuff...
//
//               Revision 1.22  2001/10/04 00:31:59  trc2876
//               renamed SynchConsole to SConsole
//
//               Revision 1.21  2001/10/04 00:16:41  etf2954
//               Added write and read to their respective
//               stdout and stdin
//
//               Revision 1.20  2001/10/03 23:19:15  eae8264
//               Lots of stuff.  Halt works!  :)
//
//               Revision 1.19  2001/10/02 22:25:39  etf2954
//               fixed error
//
//               Revision 1.18  2001/10/02 22:24:28  etf2954
//               Wrote correct constructors
//
//               Revision 1.17  2001/10/02 21:48:49  trc2876
//               some todo's
//
//               Revision 1.16  2001/10/02 20:15:46  eae8264
//               Set up special cases for init proc.
//
//               Revision 1.15  2001/10/02 16:21:45  eae8264
//               Excuse the multiple file update.
//               ProcMan.cc:  Changed threadExit to use Scheduler::unscheduleThread
//               	     Fixed memory leaks.
//               	     Added some explicit casting (minor changes)
//               	     Some code restructuring (superficial)
//               Process.cc:  Had constructor call Scheduler::ReadyToRun on first thread.
//               	     Had exit() call Scheduler::ReadyToRun instead of Run.
//               Process.h:   Changed return values of pid and ppid's to unsigned chars.
//
//               Revision 1.14  2001/09/30 20:16:58  etf2954
//               Correctly assigned the first thread
//
//               Revision 1.13  2001/09/30 19:44:47  etf2954
//               Changed Process::exit( )  call to conform to exit( int )
//
//               Revision 1.12  2001/09/28 01:46:39  eae8264
//               Touched up exit().  It still has the entire system exit though, but it now
//               has what we initially planned.
//
//               Revision 1.11  2001/09/28 00:59:21  eae8264
//               I have the system go down in Process::exit() at least until we have
//               all the issues with threads worked out.
//
//               Revision 1.10  2001/09/27 23:59:59  etf2954
//               stuff
//
//               Revision 1.9  2001/09/27 22:42:50  etf2954
//               no idea
//
//               Revision 1.7  2001/09/27 20:47:35  etf2954
//               Still adding stuff
//
//               Revision 1.6  2001/09/27 20:03:29  eae8264
//               Made lotsa changes!  :P
//
//               Revision 1.5  2001/09/27 17:09:15  eae8264
//               Added some needed things to exit, exec.
//
//               Revision 1.4  2001/09/27 15:55:00  eae8264
//               Got things to compile.  Implemented getProcInfo().  Added some accessors to Process.  Stuff
//
//               Revision 1.3  2001/09/27 03:38:49  etf2954
//               Added more base functionality
//
//               Revision 1.2  2001/09/26 01:26:31  etf2954
//               Added needed function for thread manipulations, and child process manipulation
//                  and file handle manipulation
//
//               Revision 1.1  2001/09/22 22:03:43  p544-01b
//               Initial revision
//

#include "Process.h"
#include "filesys.h"
#include "syscall.h"
#include "system.h"
#include "machine.h"

unsigned int Process::currentFileID = 2 ; // skip 0 & 1 'cause they are reserved
					  // for stdin and stdout	

Process::Process( unsigned char pid, unsigned char ppid, int argc, char *argv )
  : argc_( argc ),
    argv_( argv ),
    threadCount_( 0 ),
    childCount_( 0 ),
    joinCount_( 0 ),
    fileCount_( 0 ),
    pid_ ( pid ),
    ppid_( ppid ),
    TID_counter_( 1 ),
    exitStatus_( 0 ),
    failed_( FALSE ),
#ifdef VM_STATS
    stats_( new VMStats( argv ) ),
#endif
    exiting(new Lock("Exiting"))
{
    DEBUG( 'p', "Creating Process: %d from parent %d\n", pid_, ppid ) ; 
    threadList_ = new List() ;
    fileList_   = new List() ;
    joinList_   = new List() ;
    childList_  = new List() ;

    joinCondition_ = new Condition( "Join Condition" ) ;


    OpenFile *file = fSystem->Open( getCmdName() ) ;
    addrSpace_ = new AddrSpace( file, pid_ ) ;
    if( addrSpace_->failed() )
    {
	failed_ = TRUE ;
	delete file ;
	return ;
    }
#ifndef VM
    delete file ;
#endif

    int tid = getNextThreadID();
    char* foo = new char[16];
    sprintf(foo,"Thread %i:%i",pid_,tid);
    Thread *thread = new Thread( foo,
				 pid_, 
				 tid,
				 this
				) ;

    DEBUG( 'p', "Thread %d created for process %d.\n", 
	thread->getID(),
	thread->getAssociatedPID()
	 );
    thread->space = addrSpace_ ;
    addThread( thread ) ;
    thread->setUserReg(PCReg, 0);
    thread->setUserReg(NextPCReg, 4);

    DEBUG( 'p', "Leaving .. ** ******" ) ;
}

//
// Name:	init
//
void Process::init() 
{
    Thread* firstThread = (Thread *)(threadList_->getFirstItem()->item) ;
    firstThread->Fork( ProcessManager::initThread, (int)addrSpace_ );
    if ( pid_ == 0 )
    {
        // we use threadToBeDestroyed because of assertion in ~Thread()
        threadToBeDestroyed = currentThread ;
	currentThread = NULL ;
        delete threadToBeDestroyed ; // remove init thread...
	threadToBeDestroyed = NULL ;
        currentThread = firstThread ;
        addrSpace_->InitRegisters() ;
        addrSpace_->RestoreState() ;
	
	// first process is otherwise scheduled twice
	scheduler->unscheduleThread( firstThread->getCompositeID() ) ;
    }
    else
    {
        int sp = firstThread->getUserReg( StackReg );
        firstThread->setUserReg( StackReg, 
			    addrSpace_->copyArgs( argc_, argv_, sp, firstThread )
			   ) ;
    }
}

// 
// ( destructor ) 
// 
Process::~Process() 
{ 
    DEBUG( 'p', "Deleted Process: %d\n", pid_ ) ;

#ifdef VM_STATS
    stats_->Print() ;
#endif

    while( !childList_->IsEmpty() )
    {
        procMan->killProc( (int)childList_->Remove() ) ;
	childCount_-- ;
    }

    delete   addrSpace_ ;
    delete[] argv_ ;
    delete   threadList_ ;
    delete   fileList_ ;
    delete   joinList_ ;
    delete   childList_ ;
    delete   joinCondition_ ;
    delete   exiting;

#ifdef VM_STATS
    delete   stats_ ;
#endif

}

//
// exit
//
void Process::exit()
{
    DEBUG( 'p', "Process::Exit called on Process: %d\n", pid_ )  ;

    Thread *curThread ;
    while( !threadList_->IsEmpty() )
    {
	curThread = (Thread *)(threadList_->getFirstItem()->item) ;
	procMan->threadKill( pid_, curThread->getID(), 0 );
	threadCount_-- ;
    }
    DEBUG( 'p', "ALL thread clear\n" ) ;
    machine->WriteRegister( 20, exitStatus_ ) ;
    joinCondition_->Broadcast( NULL ) ;
}

//
// addJoiner
//
int Process::addJoiner( Thread *thread )
{
    DEBUG( 'p', "Adding joiner %d to process %d\n", thread->getID(), pid_ ) ;
    if ( joinCount_ == MAX_JOINS || thread == NULL ) return 1;

    joinCount_++ ;
    
    joinList_->SortedInsert( (void *)thread, thread->getID() ) ;
    return 0;
}

//
// removeJoiner
//
int Process::removeJoiner( int threadID )
{
    DEBUG( 'p', "Removing joiner %d from process %d\n", threadID, pid_ ) ;
    
    if ( joinList_->KeySortedRemove( threadID ) == NULL ) return 1;
    joinCount_-- ;
    return 0;
}

//
// addThread
//
int Process::addThread( Thread *thread )
{
    DEBUG( 'p', "Adding thread %d to process %d\n", thread->getID(), pid_ ) ;
    if ( threadCount_ == MAX_THREADS ) return 1;
    threadCount_++ ;
    
    threadList_->SortedInsert( (void *)thread, thread->getID() ) ;
    return 0;
}

//
// removeThread
//
Thread *Process::removeThread( int threadID )
{
    DEBUG( 'p', "Removing thread %d from process %d\n", threadID, pid_ ) ;
    threadCount_-- ;
    
    return (Thread *) threadList_->KeySortedRemove( threadID ) ;
}

//
// addChild
//
int Process::addChild( unsigned char childPid )
{
    DEBUG( 'p', "Adding child %d to process %d\n", childPid, pid_ );
    if ( childCount_ == MAX_CHILD_PROCS ) return 1;

    childCount_++ ;

    childList_->SortedInsert( (void *)childPid, childPid ) ;
    return 0;
}

//
// removeChild
//
int Process::removeChild( unsigned char childPid )
{
    DEBUG( 'p', "Removing child %d from process %d\n", childPid, pid_ );

    if ( childList_->KeySortedRemove( childPid ) == NULL ) return 1;
    childCount_-- ;
    return 0;
}

//
// openFile
//
int Process::openFile( char *name ) 
{
    OpenFile* file = NULL;

    DEBUG( 'p', "Opening file %s, for process %d\n", name, pid_ ) ;

    if ( name == NULL ) return 1;
    if ( fileCount_ == MAX_FILE_HANDLES ) return 0;

    int fileID = currentFileID++ ;
    if ( currentFileID++ == 0 ) currentFileID = 2; // deal with wrap around

    file = fSystem->Open( name );
    if ( file == NULL ) return 1;
    fileList_->SortedInsert( (void *)file, fileID ) ;
    fileCount_++ ;

    return fileID ;
}

//
// closeFile
//
int Process::closeFile( int fileID )
{
    OpenFile* filePtr = NULL;
    DEBUG( 'p', "Closing file %d, for process %d", fileID, pid_ ) ;
    // cannot close stdin,stdout
    if ( fileID == 0 || fileID == 1 ) return 1;

    filePtr = (OpenFile *)fileList_->KeySortedRemove( fileID ) ;
    if ( filePtr == NULL ) return 1;

    fileCount_-- ;

    return 0;
}

//
// write
//
int Process::write( char *buffer, int size, int fileID )
{
    OpenFile *out;

    if ( fileID == 0 ) return 1;
    if ( size < 1 ) return 2;
    if ( buffer == NULL ) return 3;
    
    if( fileID == 1 )
    {
	synchConsole->write( buffer, size ) ;
    } else
    {
	out = ((OpenFile *)fileList_->LookAtSorted( fileID ));
	if ( out == NULL ) return 1;
	out->Hint( size ) ;
	out->Write( buffer, size );
    }
    return 0;
}

//
// read
//
int Process::read( char *buffer, int size, int fileID )
{
    OpenFile* file = NULL;

    if ( size < 1 ) return -3;
    if ( fileID == 1 ) return -2;
    if ( buffer == NULL ) return -1;

    if( fileID == 0 )
    {
	return synchConsole->read( buffer, size ) ;
    } else
    {
        file = (OpenFile *)fileList_->LookAtSorted( fileID );
	if ( file == NULL ) return -2;
        return file->Read( buffer, size );
    }
}
