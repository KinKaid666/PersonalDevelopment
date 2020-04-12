// File:	 $Id: KSwap.h,v 3.0 2001/11/04 19:47:06 trc2876 Exp $
// Author:	 Trevor Clarke, Eric Eells, Eric Ferguson, Dan Westgate
// Contributors: {}
// Description:
// Revisions:
//		 $Log: KSwap.h,v $
//		 Revision 3.0  2001/11/04 19:47:06  trc2876
//		 force to 3.0
//
//		 Revision 1.36  2001/11/02 20:27:00  eae8264
//		 Removed VMStats
//		
//		 Revision 1.35  2001/10/31 20:38:23  trc2876
//		 forget it, TOP_OF_ADDRSPACE is 2^31-1 so we are fine w/ ints
//		
//		 Revision 1.34  2001/10/31 20:33:11  trc2876
//		 changed to unsigned int for addresses
//		
//		 Revision 1.33  2001/10/31 18:05:15  trc2876
//		 fixed bitmap problem w/ free mem bitmap
//		
//		 Revision 1.32  2001/10/31 02:29:03  eae8264
//		 Added some stuff.
//		
//		 Revision 1.31  2001/10/30 19:15:46  eae8264
//		 Fixed a major bug.  We never marked physical memory free in
//		 the bitmap in freeMemForPID().
//		 Added some stuff pertaining to stats.  It brings up other
//		 concerns.
//		
//		 Revision 1.30  2001/10/30 01:38:00  eae8264
//		 Set up VMStats as a data member of KSwap.  Made calls to
//		 print.  Added this as a function of KSwap.
//		
//		 Revision 1.29  2001/10/28 18:48:22  trc2876
//		 added default: to switch in exception.cc to get rid of compiler warning
//		 switch copyArgs() to create a new page in memory for arguments
//		
//		 Revision 1.28  2001/10/26 00:07:34  eae8264
//		 Commenty...
//		
//		 Revision 1.27  2001/10/26 00:04:54  etf2954
//		 Added the call to load the data pages from the executable
//		
//		 Revision 1.26  2001/10/25 21:23:49  eae8264
//		 Updated comment for allocMemForPID to reflect
//		 what totalSize is...
//		
//		 Revision 1.25  2001/10/25 00:17:57  eae8264
//		 Fixed comment for allocMemForPID()
//		
//		 Revision 1.24  2001/10/25 00:10:02  etf2954
//		 Changed RevPageEntry_t to a class that
//		 	inherited from TranslationEntry
//		
//		 Revision 1.23  2001/10/25 00:08:51  eae8264
//		 Added and implemented allocMemForPID
//		
//		 Revision 1.22  2001/10/24 23:34:52  trc2876
//		 compile fixes and an algorithm fix in KSwap
//		
//		 Revision 1.21  2001/10/24 23:01:38  trc2876
//		 more misc compile fixes
//		
//		 Revision 1.20  2001/10/24 22:57:52  eae8264
//		 Added func def for allocMemForPID()
//		
//		 Revision 1.19  2001/10/24 04:11:15  eae8264
//		 Fixed some apparent bugs.  Added helper func findSegFor
//		 so that swapper isn't huge.  Bugfixes include: virtual
//		 address calculation correction, use of 64-bit key rather
//		 than a 32-bit key for dklist, calculation of returned
//		 physical address in swapper() corrected.  Other minor
//		 things.
//		
//		 Revision 1.18  2001/10/23 23:40:54  etf2954
//		 Correct names in helper functions
//		
//		 Revision 1.16  2001/10/23 22:37:46  etf2954
//		 Added two functions that access the revPageTbl_
//		
//		 Revision 1.14  2001/10/23 21:37:35  etf2954
//		 Added more todos
//		
//		 Revision 1.13  2001/10/23 21:17:59  etf2954
//		 Removed findFreeMem as it the same as allocMem
//		
//		 Revision 1.12  2001/10/23 20:46:55  eae8264
//		 Updated synchronization mechanisms and usage
//		 thereof.  (the mechanisms used with swapping...)
//		
//		 Revision 1.11  2001/10/23 20:16:42  etf2954
//		 *** empty log message ***
//		
//		 Revision 1.10  2001/10/23 19:53:27  etf2954
//		 Wrote more core functionality
//		
//		 Revision 1.9  2001/10/23 19:45:42  eae8264
//		 Added function setCurSegTable to be called by Thread.
//		 Also added calls to sysSwapMutex_ in swapper().
//		
//		 Revision 1.8  2001/10/23 18:35:07  eae8264
//		 Corrected header comments, implemented getPhysAddr,
//		 swapper, added new helper func stub (findFreePage),
//		 put request synchronization in place... there may
//		 be problems with how we're using the Condition!!!!!!
//		
//		 Revision 1.7  2001/10/21 20:13:36  eae8264
//		 Changed KSwap so that struct definitions are in a
//		 separate file.
//		
//		 Revision 1.6  2001/10/21 19:12:08  etf2954
//		 Added some functionality
//		
//		 Revision 1.5  2001/10/20 22:11:28  etf2954
//		 Added some comments as to what each function does
//		
//		 Revision 1.4  2001/10/20 21:20:50  eae8264
//		 Fixed comments and stuff.
//		
//		 Revision 1.3  2001/10/20 21:02:45  etf2954
//		 Writing comments of the function
//		
//		 Revision 1.2  2001/10/20 19:18:52  etf2954
//		 Added constructor & destructor stub
//		
//		 Revision 1.1  2001/10/20 18:30:00  etf2954
//		 Correct position in the directory structure
//		


#ifndef _KSWAP_H
#define _KSWAP_H

#include "list.h"
#include "thread.h"
#include "synch.h"
#include "dklist.h"
#include "SwapFile.h"
#include "KSwapInfo.h"
#include "noff.h"
#include "VMStats.h"

#define ARGS_VSTART   0x30000000

class KSwap
{
public:

    //
    // Name:		(constructor)
    //
    // Description: 	Creates swap thread and schedules it, initializes vars
    //
    // Arguments:	None
    //
    KSwap() ;

    //
    // Name:		(destructor)
    //
    // Description: 
    //
    // Arguments:	None
    //
    ~KSwap() ;

    //
    // Name:		swapper
    //
    // Description: 	Swapper thread.  Waits on semaphore.  Requests call V()
    //			This handles requests for swapping in and out.  All
    //			swapping takes place here.
    //
    // Arguments:	A dummy argument
    //
    // Returns:		void
    //
    // Exceptions:	None
    //
    static void swapper( int dummy ) ;

#ifdef VM_STATS
    //
    // Name:		PrintStats
    //
    // Description: 	Prints the stats on misses to std out.
    //
    // Arguments:	None
    //
    // Returns:		void
    //
    // Exceptions:	None
    //
    void printStats() ;
#endif

    //
    // Name:		updateRevPageTblEntry
    //
    // Description: 	Prints the stats on misses to std out.
    //
    // Arguments:	None
    //
    // Returns:		void
    //
    // Exceptions:	None
    //
    void updateRevPageTblEntry( TranslationEntry* entry ) ;

    //
    // Name:		getPhysAddr
    //
    // Description: 	This will look in the reverse page table for a 
    //			translation.  If it's not there, it will make a request
    //			to swap the data in from the swap file.  This function
    //			will use syncrhonization so that it does not return
    //			until the data is swapped into physical memory.
    //			NOTE:  It is the client's responsibility to update the
    //			TLB.
    //
    // Arguments:	The pid for the process that needs a translation of
    //			vAddr.  Swap Lock is used if there needs to be a swap
    //			from the daemon thread.  Wait would then be called on 
    //			the lock.  The bool ptr is so we can return whether
    //			the data is in a readonly segment or not.
    //
    // Returns:		The physical address.
    //
    // Exceptions:	None
    //
    int getPhysAddr( int pid, int vAddr, Semaphore* swapLock, bool* readOnly ) ;

    //
    // Name:		freeMemFromPID
    //
    // Description: 	This frees all memory associated with process pid.
    //
    // Arguments:	The Process pid
    //
    // Returns:		void
    //
    // Exceptions:	None
    //
    void freeMemFromPID( int pid ) ;

    //
    // Name:		allocMemForPID
    //
    // Description: 	Allocates swap space and establishes segments in the
    //			segment table.
    //
    // Arguments:	pid, total executable size with stack (in pages),
    //			noff header, open file object for executable
    //
    // Returns:		-1 if swap is full, OR if success,
    //			number of pages allocated in the read/write swap
    //
    // Exceptions:	None
    //
    int allocMemForPID( int          pid,
			int totalSize,
			NoffHeader*  nh,
			OpenFile*    exec       ) ;

    //
    // Name:		loadDataPages
    //
    // Description: 	Copys all the writeable memory of pid from exec to the
    //			swap file
    //
    // Arguments:	-1 on  error
    //			 0 for success
    //
    // Returns:		
    //
    // Exceptions:	None
    //
    int loadDataPages( int         pid,
		       NoffHeader* nh,
		       OpenFile*   exec ) ;

    //
    // Name:		setCurSegTable
    //
    // Description: 	This sets the current segment table to the one for
    //			process pid.
    //
    // Arguments:	The process pid
    //
    // Returns:		void
    //
    // Exceptions:	None
    //
    void setCurSegTable( int pid ) ;

    //
    // Name:		copyArgs
    //
    // Description: 	create argc/argv segment and copy args to it
    //
    // Arguments:	argc/argv as in AddrSpace::copyArgs and a PID
    //
    // Returns:		address for argv
    //
    // Exceptions:	None
    //
    int copyArgs( int argc, char *argv, AddrSpace *addrspace );

private: // Helper Functions

    //
    // Name:		findLRUPage
    //
    // Description: 	This find the least recently used (LRU) page in memory
    //
    // Arguments:	None
    //
    // Returns:		the page
    //
    // Exceptions:	None
    //
    int findLRUPage() ;

    //
    // Name:		requestSwap
    //
    // Description: 	Simply adds a request to the list for the calling PID
    //			and calls V() on the request semaphore (daemonThdSem).
    //
    // Arguments:	The details of the request (see struct)
    //
    // Returns:		void
    //
    // Exceptions:	None
    //
    void requestSwap( Request_t* reqInfo ) ;

    //
    // Name:		findSegFor
    //
    // Description: 	Returns the segment struct associated with pid
    //			and vAddr.
    //
    // Arguments:	pid and vAddr
    //
    // Returns:		struct or NULL if not found (very bad!)
    //
    // Exceptions:	None
    //
    SegTblEntry_t*  findSegFor( int pid, int vAddr ) ; 

    //
    // Name:		getRevPageEntry
    //
    // Description: 	Returns the RevPageEntry_t struct that is associated 
    //			with that physical page
    //
    // Arguments:	The physical page number 
    //
    // Returns:		The RevPageEntry_t struct
    //
    // Exceptions:	None
    //
    RevPageEntry* getRevPageEntry( int physPage ) ;

    //
    // Name:		getRevPageEntry
    //
    // Description: 	Returns the RevPageEntry_t struct that is associated 
    //			with that pid and vAddr
    //
    // Arguments:	associated pid and address of beginning of a 
    //			virtual page.
    //
    // Returns:		The RevPageEntry_t struct
    //
    // Exceptions:	None
    //
    RevPageEntry* getRevPageEntry( int pid, int vAddr ) ;

    //
    // Name:		removeRevPageEntry
    //
    // Description: 	remove the RevPageEntry_t struct that is associated 
    //			with that physical page
    //
    // Arguments:	The physical page number
    //
    // Returns:		None
    //
    // Exceptions:	None
    //
    void removeRevPageEntry( int physPage ) ;

    //
    // Name:		removeRevPageEntry
    //
    // Description: 	Remove the RevPageEntry_t struct that is associated 
    //			with that pid and vAddr
    //
    // Arguments:	associated pid and address of beginning of a
    //			virtual page.
    //
    // Returns:		None
    //
    // Exceptions:	None
    //
    void removeRevPageEntry( int pid, int vAddr ) ;

    //
    // Name:		addRevPageEntry
    //
    // Description: 	Add this information to the revPageTbl
    //
    // Arguments:	physAddr:	the physical page number
    //			vPage:		the virtual  page number
    //			pid:		the associated pid
    //			readOnly:	is this read only code
    //			dirty:		is the data dirty (default= NO)
    //
    // Returns:		None
    //
    // Exceptions:	None
    //
    void addRevPageEntry( int physPage,
			  int vPage,
			  unsigned char pid, 
			  bool		readOnly,
			  bool		dirty = 0  ) ;

    //
    // Name:		allocMem
    //
    // Description: 	Takes place of allocMem that is in AddrSpace.
    //			This will return the number of the free page
    //			and will swap if needed
    //
    // Arguments:	pid -- used for vm stats only
    //
    // Returns:		void
    //
    // Exceptions:	None
    //
    int allocMem( int pid ) ;

    //
    // Name:		freeMem
    //
    // Description: 	Takes place of freeMem that is in AddrSpace.
    //
    // Arguments:	The page to free
    //
    // Returns:		void
    //
    // Exceptions:	None
    //
    void freeMem( int pageNum ) ;

    void printRevPageTbl();
    void printSegTbl(int pid);

private: // Data Members

    // Stats info is kept here
    VMStats* stats_ ;

    // All open swap files keyed on PID.  
    List* swapFiles_ ;
    
    // segment table for currently running process
    List* curSegTable_ ;
    
    // List of lists... all segment tables for all processes
    // This is a list of lists.  Outer list keyed on pid and
    // inner list keyed on virtual address of the start of the 
    // segment.
    List* segTblEntries_ ;
    
    // The reverse page table
    DKList* revPageTbl_ ;
    
    // The lists of swap requests.
    List* requests_ ;

    // For easy access to free page
    BitMap* freeMemPages_ ;

    // Sync for above object
    Lock*   freeMemPagesMutex_ ;

    // The main System swapfile
    SwapFile* sysSwap_ ;

    // Sync for systems swapfile
    Lock*   sysSwapMutex_ ;

    // Used to keep swap thread running as long as there's requests.
    Semaphore* daemonThdSem_ ; 

};

#endif
