// File:	 $Id: KSwap.cc,v 3.0 2001/11/04 19:47:06 trc2876 Exp $
// Author:	 Trevor Clarke, Eric Eells, Eric Ferguson, Dan Westgate
// Contributors: {}
// Description:
// Revisions:
//		 $Log: KSwap.cc,v $
//		 Revision 3.0  2001/11/04 19:47:06  trc2876
//		 force to 3.0
//
//		 Revision 1.81  2001/11/02 20:27:00  eae8264
//		 Removed VMStats
//		
//		 Revision 1.80  2001/10/31 22:25:09  etf2954
//		 Corrected findLRUPage algorithm
//		
//		 Revision 1.79  2001/10/31 22:19:08  etf2954
//		 work around :)
//		
//		 Revision 1.78  2001/10/31 20:38:23  trc2876
//		 forget it, TOP_OF_ADDRSPACE is 2^31-1 so we are fine w/ ints
//		
//		 Revision 1.77  2001/10/31 20:33:09  trc2876
//		 changed to unsigned int for addresses
//		
//		 Revision 1.76  2001/10/31 18:05:14  trc2876
//		 fixed bitmap problem w/ free mem bitmap
//		
//		 Revision 1.75  2001/10/31 17:09:57  trc2876
//		 *** empty log message ***
//		
//		 Revision 1.74  2001/10/31 04:05:07  eae8264
//		 Strufdff..
//		
//		 Revision 1.73  2001/10/31 03:21:35  eae8264
//		 Made change to LRU algo (bugfix).
//		
//		 Revision 1.72  2001/10/31 02:29:02  eae8264
//		 Added some stuff.
//		
//		 Revision 1.71  2001/10/31 01:08:06  eae8264
//		 Added stuff for conflict misses.
//		 Fixed major bug in findSegFor()
//		
//		 Revision 1.70  2001/10/31 00:19:23  trc2876
//		 new debug statements
//		
//		 Revision 1.69  2001/10/30 21:48:17  eae8264
//		 Fixed a memory leak in the remove Page entry func's.
//		
//		 Revision 1.68  2001/10/30 21:40:30  trc2876
//		 ib-inf
//		
//		 Revision 1.67  2001/10/30 19:15:45  eae8264
//		 Fixed a major bug.  We never marked physical memory free in
//		 the bitmap in freeMemForPID().
//		 Added some stuff pertaining to stats.  It brings up other
//		 concerns.
//		
//		 Revision 1.66  2001/10/30 01:38:00  eae8264
//		 Set up VMStats as a data member of KSwap.  Made calls to
//		 print.  Added this as a function of KSwap.
//		
//		 Revision 1.65  2001/10/30 01:23:51  trc2876
//		 changed assert to an if() continue;
//		
//		 Revision 1.64  2001/10/30 01:15:20  etf2954
//		 Correct the copyArgs
//		
//		 Revision 1.63  2001/10/29 00:04:25  etf2954
//		 corrected the uninitialized data section of the noff header
//		
//		 Revision 1.62  2001/10/28 23:41:45  eae8264
//		 Modified a debug statement to be more useful.
//		
//		 Revision 1.61  2001/10/28 22:28:15  eae8264
//		 Made swapfile interface so that all address and
//		 length specifications are in bytes for
//		 consistency.  The KSwap client had to, of course,
//		 change.
//		
//		 Revision 1.60  2001/10/28 18:48:22  trc2876
//		 added default: to switch in exception.cc to get rid of compiler warning
//		 switch copyArgs() to create a new page in memory for arguments
//		
//		 Revision 1.59  2001/10/28 18:23:13  eae8264
//		 Fixed another memory leak in freeMemFromPID()
//		
//		 Revision 1.58  2001/10/28 17:52:07  eae8264
//		 Fixed a couple bugs in freeMemFromPID()
//		 that caused the assertion failure in bitmap.
//		
//		 Revision 1.57  2001/10/28 17:00:46  eae8264
//		 Fixed a memory leak in freeMemFromPID()
//		
//		 Revision 1.56  2001/10/28 00:19:09  eae8264
//		 Added more detail to a debug statement in getPhysAddr()
//		
//		 Revision 1.55  2001/10/27 23:01:53  etf2954
//		 Correctly copied uninitialized data to the
//		 	swapfile, from the binary
//		
//		 Revision 1.54  2001/10/27 00:11:33  etf2954
//		 Fixed freeTmp page bug
//		
//		 Revision 1.53  2001/10/26 22:51:23  etf2954
//		 Added better debug info
//		
//		 Revision 1.52  2001/10/26 17:36:29  trc2876
//		 fixed userTokern and kernToUser problem
//		     after executing TLB_Miss, no attempt was may to get the new addr
//		
//		 Revision 1.51  2001/10/26 10:21:46  eae8264
//		 Fixed calculation of tmpPage in loadDataPages()
//		
//		 Revision 1.50  2001/10/26 00:43:17  trc2876
//		 Added code to copy data segments to the swapfile
//		
//		 Revision 1.49  2001/10/26 00:09:11  eae8264
//		 removed a test yield.
//		
//		 Revision 1.48  2001/10/26 00:05:24  etf2954
//		 Correction
//		
//		 Revision 1.47  2001/10/26 00:04:54  etf2954
//		 Added the call to load the data pages from the executable
//		
//		 Revision 1.46  2001/10/25 22:32:33  eae8264
//		 Fixed a big thread spawning bug for swapper thread.
//		
//		 Revision 1.45  2001/10/25 21:49:22  etf2954
//		 Added debug statements
//		
//		 Revision 1.44  2001/10/25 21:27:25  eae8264
//		 Another thing to allocMemForPID... a total mess.
//		
//		 Revision 1.43  2001/10/25 21:24:52  trc2876
//		 fixed potential null ptr dereference
//		
//		 Revision 1.42  2001/10/25 21:20:48  trc2876
//		 fixed bytes/pages problem
//		
//		 Revision 1.41  2001/10/25 21:17:24  eae8264
//		 Lot of bugfixes in allocMemForPID().
//		
//		 Revision 1.40  2001/10/25 19:28:02  eae8264
//		 Added assert for if swap doesn't exist.
//		
//		 Revision 1.39  2001/10/25 01:43:04  eae8264
//		 A little "poosible bug" fix in allocMemForPID().
//		
//		 Revision 1.38  2001/10/25 01:38:57  eae8264
//		 Added swap deallocation in Error of allocMemForPID()
//		
//		 Revision 1.37  2001/10/25 01:24:56  eae8264
//		 Added dealing with stack segment in allocMemForPID()
//		
//		 Revision 1.36  2001/10/25 00:58:01  trc2876
//		 code review
//		
//		 Revision 1.35  2001/10/25 00:46:12  eae8264
//		 Corrected and improved some calculations in swapper()
//		
//		 Revision 1.34  2001/10/25 00:29:15  etf2954
//		 corrected my = bug
//		
//		 Revision 1.33  2001/10/25 00:28:13  etf2954
//		 Wrote the findLRUPage()
//		
//		 Revision 1.32  2001/10/25 00:26:39  eae8264
//		 Corrected return value for allocMemForPID()
//		
//		 Revision 1.31  2001/10/25 00:17:39  etf2954
//		 Merged last two updates
//		
//		 Revision 1.30  2001/10/25 00:10:02  etf2954
//		 Changed RevPageEntry_t to a class that
//		 	inherited from TranslationEntry
//		
//		 Revision 1.28  2001/10/24 23:34:52  trc2876
//		 compile fixes and an algorithm fix in KSwap
//		
//		 Revision 1.27  2001/10/24 23:01:38  trc2876
//		 more misc compile fixes
//		
//		 Revision 1.26  2001/10/24 22:47:17  trc2876
//		 fixed TODO in KSwap
//		 finished changes to addrspace
//		
//		 Revision 1.25  2001/10/24 22:15:32  etf2954
//		 completed working *hopefully* allocMem function
//		
//		 Revision 1.24  2001/10/24 04:11:15  eae8264
//		 Fixed some apparent bugs.  Added helper func findSegFor
//		 so that swapper isn't huge.  Bugfixes include: virtual
//		 address calculation correction, use of 64-bit key rather
//		 than a 32-bit key for dklist, calculation of returned
//		 physical address in swapper() corrected.  Other minor
//		 things.
//		
//		 Revision 1.23  2001/10/23 23:57:36  eae8264
//		 Added call to build rev page table in swapper().
//		 Added a todo in getPhysAddr().
//		
//		 Revision 1.22  2001/10/23 23:40:54  etf2954
//		 Correct names in helper functions
//		
//		 Revision 1.21  2001/10/23 23:38:46  eae8264
//		 Changed var names.
//		
//		 Revision 1.20  2001/10/23 23:36:11  etf2954
//		 Added more revPageTbl helper functions
//		
//		 Revision 1.19  2001/10/23 22:37:46  etf2954
//		 Added two functions that access the revPageTbl_
//		
//		 Revision 1.17  2001/10/23 22:06:57  eae8264
//		 Added file address calculation in swapper().
//		 Changed name of item in struct... so had to
//		 change other stuff.
//		
//		 Revision 1.16  2001/10/23 21:37:35  etf2954
//		 Added more todos
//		
//		 Revision 1.15  2001/10/23 21:18:53  etf2954
//		 TODOs added
//		
//		 Revision 1.13  2001/10/23 20:56:38  eae8264
//		 Small change.  Just int to a ptr...
//		
//		 Revision 1.12  2001/10/23 20:46:55  eae8264
//		 Updated synchronization mechanisms and usage
//		 thereof.  (the mechanisms used with swapping...)
//		
//		 Revision 1.11  2001/10/23 20:16:42  etf2954
//		 *** empty log message ***
//		
//		 Revision 1.9  2001/10/23 19:45:41  eae8264
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
//		 Revision 1.6  2001/10/21 19:12:07  etf2954
//		 Added some functionality
//		
//		 Revision 1.4  2001/10/20 21:20:50  eae8264
//		 Fixed comments and stuff.
//		
//		 Revision 1.3  2001/10/20 21:02:45  etf2954
//		 Writing comments of the function
//		
//		 Revision 1.2  2001/10/20 20:10:37  eae8264
//		 fixed style, comments.
//		
//		 Revision 1.1  2001/10/20 18:30:00  etf2954
//		 Correct position in the directory structure
//		

#include <strings.h>
#include "KSwap.h"
#include "system.h"
#include "thread.h"
#include "system.h"
#include "argdefs.h"
#include "SwapFile.h"

void printSegTbl(DKList *lst);

//
// Name:	(constructor)
//
KSwap::KSwap()
{ 
    DEBUG( 'k', "Entering KSwap()\n" ) ;
    daemonThdSem_ = new Semaphore( "daemon Thread", 0 ) ;

    OpenFile* swap = fSystem->Open( "nachosSwap.swp" ) ;
    ASSERT( swap ) ;
    sysSwap_       = new SwapFile( swap, 0 ) ;
    sysSwapMutex_  = new Lock    ( "mainSwap Lock" ) ;

    freeMemPages_      = new BitMap( NumPhysPages ) ;
    freeMemPagesMutex_ = new Lock  ( "freeMemPages Lock" ) ;

    swapFiles_     = new List() ;
    segTblEntries_ = new List() ;
    revPageTbl_    = new DKList() ;
    requests_      = new List() ;

#ifdef VM_STATS
    stats_	   = new VMStats( "all processes" ) ;
#endif
    
    (new Thread( "SwapDaemon", 0, 2, NULL ))->Fork( swapper, 0 ) ;

    DEBUG( 'k', "Leaving  KSwap()\n" ) ;
} // (constructor)

//
//	(destructor)
//
KSwap::~KSwap()
{
    DEBUG( 'k', "Entering ~KSwap()\n" ) ;
    delete daemonThdSem_ ;
    delete freeMemPages_ ;
    delete freeMemPagesMutex_ ;
    delete swapFiles_ ;
    delete revPageTbl_ ;
    delete requests_ ;
    delete sysSwap_ ;
    delete sysSwapMutex_ ;

#ifdef VM_STATS
    delete stats_ ;
#endif

    while( !segTblEntries_->IsEmpty() )
    {
	delete (List*)(segTblEntries_->Remove()) ;
    }

    DEBUG( 'k', "Leaving  ~KSwap()\n" ) ;
} // (destructor)

//
//  Name:	updateRevPageTblEntry
//
void KSwap::updateRevPageTblEntry( TranslationEntry* entry )
{
    RevPageEntry* rent = getRevPageEntry( entry->physicalPage ) ;
    if(rent == NULL) {
	DEBUG('h',"No rev entry found for 0x%04x/0x%04x\n",entry->physicalPage,entry->virtualPage);
	printRevPageTbl();
	return;
    }
    rent->use = entry->use ;
    rent->dirty = entry->dirty ;
} // updateRevPageTblEntry

#ifdef VM_STATS
//
//  Name:	printStats
//
void KSwap::printStats()
{
    stats_->Print() ;
} // printStats
#endif

int KSwap::allocMem( int pid )
{
    DEBUG( 'k', "Entering KSwap::allocMem()\n" ) ;
    // 1.) find a free physical mem page
    // 2.) if non exist, make sure swap file is big enough 
    // 3.) then swap

    int memPage ;
    freeMemPagesMutex_->Acquire() ;
    memPage = freeMemPages_->Find() ;
    freeMemPagesMutex_->Release() ;

    // 
    // Check to see if the Find() call returned a -1
    //	if it is    -1, that means there are no free Physical Memory Pages
    //  if it isn't -1, then we can return that page

    if( memPage == -1 ) 
    {
#ifdef VM_STATS
        // Update stats
	stats_->addCap( 1 ) ;
	Process* proc = procMan->getProcess( pid ) ;
	if ( proc ) proc->getStats()->addCap( 1 ) ;
#endif
	DEBUG( 'y', "Mem Full!!!\n" );

	
	// 
	// Find a page in memory that is least recently used (LRU)

	memPage = findLRUPage() ;

	// Lookup that page
	// Is the page dirty?
	//	yes -> copy it to disc, update the segTblList and revPagelist
	// 	no  -> remove it from memory

	RevPageEntry* entry = getRevPageEntry( memPage ) ;
	if( entry )
	{
	    if( entry->dirty )
	    {
		//
		// Since the page we are goin to swap out is dirty,
		//	we need to save it to disc and update the revPageTbl
		SegTblEntry_t* inf ;
		int fileAddr, pageBound, physAddr, pageInSeg ;

		inf = findSegFor( entry->pid, (entry->virtualPage * PageSize)) ;

		//
		// Find the address in the file where that page is
		pageInSeg = ( (entry->virtualPage * PageSize) - inf->vStart_ ) /
				PageSize;
		pageBound = pageInSeg * PageSize ;
		
#ifdef VM_STATS
		inf->pgLRUout_->Mark( pageInSeg ) ;
#endif

		DEBUG( 'y', "Marking LRU page:%d for pid:%d\n",
		       pageInSeg, pid ) ;

		fileAddr = pageBound + inf->addrInFile_ ;
		physAddr = entry->physicalPage * PageSize ;
		sysSwapMutex_->Acquire() ;
		sysSwap_->swapOut( fileAddr, physAddr ) ;
		sysSwapMutex_->Release() ;
	    }
	    DEBUG( 'y', "Removing phys page %d\n", entry->physicalPage ) ;
	    removeRevPageEntry( entry->physicalPage ) ;
	    freeMemPagesMutex_->Acquire() ;
	    freeMemPages_->Mark(memPage) ;
	    freeMemPagesMutex_->Release() ;
	}
	else
	{
	    //
	    // PROBLEM: our findLRUPage() returned an invalid page
	    DEBUG('k',"findLRUPage() failed to return a good page, you should NEVER see this\n");
	}


    } 

    DEBUG( 'k', "Leaving  KSwap::allocMem()\n" ) ;
    return memPage ;
}

//
// Name:	freeMem
//
void KSwap::freeMem( int pageNum )
{
    DEBUG( 'k', "Entering KSwap::freeMem()\n" ) ;
    freeMemPagesMutex_->Acquire() ;
    freeMemPages_->Clear( pageNum ) ;
    freeMemPagesMutex_->Release() ;
    DEBUG( 'k', "Leaving  KSwap::freeMem()\n" ) ;
} // freeMem

void KSwap::freeMemFromPID( int pid )
{
    DEBUG( 'k', "Entering KSwap::freeMemFromPID()\n" ) ;
    // 1.) Remove all its memory from Physical mem
    // 2.) Delete all its memory in the swap file
    // 3.) Close its swap file executable
   
    DKListElement *tmp;
    for(DKListElement *cur = revPageTbl_->getBfirst(); cur != NULL; ) {
	tmp = cur->b_next;
	if((cur->b >> 32) & (unsigned long long)pid) {
	    removeRevPageEntry( cur->a );
	} 
	cur = tmp;
    }

    SegTblEntry_t* curEntry ;
    List* segTbl ;
    segTbl = (List *)segTblEntries_->KeySortedRemove( pid ) ;
    ListElement* elem = segTbl->getFirstItem() ;
    while ( elem != NULL )
    {
        curEntry = (SegTblEntry_t*)elem->item ;
	if( !curEntry->inExecFile_ )
	{
	    sysSwapMutex_->Acquire() ;
	    sysSwap_->deallocSegment( curEntry->addrInFile_, 
				      curEntry->vLength_ ) ;
	    sysSwapMutex_->Release() ;
	}

#ifdef VM_STATS
	delete curEntry->pgInitIn_ ;
	delete curEntry->pgLRUout_ ;
#endif

	delete curEntry ;
	elem = elem->next ;
    }
    delete segTbl ;
    delete (SwapFile*)swapFiles_->KeySortedRemove( pid ) ;
    DEBUG( 'k', "Leaving  KSwap::freeMemFromPID()\n" ) ;
}

//
// Name:	getPhysAddr
//
int KSwap::getPhysAddr( int pid, 
			int vAddr,
			Semaphore* swapLock,
			bool* readOnly )
{
    DEBUG( 'K', "Entering KSwap::getPhysAddr( pid:%d, vAddr:0x%04x, lk )\n",
    	   pid, vAddr ) ;
    int returnVal ;
    Request_t* req ;
    RevPageEntry* trans ;

    //
    // 1.) Check the revPageTbl if the page is in Physical Mem
    //	   if yes -> return the addr
    //	   if no  -> swap it in
    
    trans = getRevPageEntry( pid, ( vAddr / PageSize ) ) ;
    if ( trans ) {
	*readOnly = trans->readOnly ;
        return (trans->physicalPage * PageSize + vAddr % PageSize) ;
    }

    req = new Request_t ;
    req->pid_ = pid ;
    req->vAddr_ = vAddr ;
    req->notifier_ = swapLock;
    req->returnVarAddr_ = &returnVal ; // let swapper thread "give" us value
    req->readOnlyAddr_ = readOnly ;
    
    requestSwap( req ) ;
    swapLock->P() ;
    
    DEBUG( 'K', "Leaving  KSwap::getPhysAddr()\n" ) ;
    return returnVal ;
} // getPhysAddr

//
// Name:	swapper
//
void KSwap::swapper( int dummy )
{
    Request_t* req ;
    SegTblEntry_t* inf ;
    SwapFile* file ;
    int physPage, physPageAddr, fileAddr, pageBound, pageInSeg ;

    while ( TRUE )
    {
	DEBUG( 'k', "Entering KSwap::swapper() loop\n" ) ;
	kswap->daemonThdSem_->P(); // wait for request.
	req = (Request_t *)kswap->requests_->Remove() ;

	inf = kswap->findSegFor( req->pid_, req->vAddr_ ) ;
	if(inf == NULL) { // sanity check
	    DEBUG( 'y', "################Poop\n" );
	    req->notifier_->V() ;
	    continue;
	}

	physPage = kswap->allocMem( req->pid_ ) ; // makes space if necessary
	physPageAddr = physPage * PageSize ;
	kswap->addRevPageEntry( physPage,
				req->vAddr_ / PageSize,
				req->pid_,
				inf->inExecFile_ ) ;

	// pageInSeg: is the i'th page within the segment where vAddr can be
	// found
	// pageBound: is the addr of the page that req->vAddr refers to
	// ...this page boundary is the offset from the start of the 
	// segment
	pageInSeg = (req->vAddr_ - inf->vStart_) / PageSize ;
	pageBound =  pageInSeg * PageSize ;

#ifdef VM_STATS
	// VM stats info
	if ( !inf->pgInitIn_->Test( pageInSeg ) )
	{
	    procMan->getProcess( inf->pid_ )->getStats()->addCold( 1 ) ;
	    kswap->stats_->addCold( 1 ) ;
	}
	inf->pgInitIn_->Mark( pageInSeg ) ;
	DEBUG( 'y', "Testing LRU page:%d for pid:%d\n", pageInSeg, req->pid_ ) ;
	if ( inf->pgLRUout_->Test( pageInSeg ) )
	{
	    kswap->stats_->addConf( 1 ) ;
	    procMan->getProcess( inf->pid_ )->getStats()->addConf( 1 ) ;
	}
#endif

	// file addr is the addr of the beginning of the page 
	// (within the file) that vAddr is in
	fileAddr = pageBound + inf->addrInFile_ ;
	if ( inf->inExecFile_ )
	{
	    file = (SwapFile *)kswap->swapFiles_->LookAtSorted( req->pid_ ) ;
	    file->swapIn( fileAddr, physPageAddr ) ;
	}
	else
	{
	    kswap->sysSwapMutex_->Acquire() ;
	    kswap->sysSwap_->swapIn( fileAddr, physPageAddr ) ;
	    kswap->sysSwapMutex_->Release() ;
	}
	*(req->returnVarAddr_) = physPageAddr + (req->vAddr_ % PageSize) ;
	*(req->readOnlyAddr_) = inf->inExecFile_ ;
	req->notifier_->V() ;
	DEBUG( 'k', "Leaving  KSwap::swapper() loop\n" ) ;
    } // loop
} // swapper

//
// Name:	findSegFor()
//
SegTblEntry_t* KSwap::findSegFor( int pid, int vAddr )
{
    DEBUG( 'k', "Entering KSwap::findSegFor( pid:%d, vAddr:0x%04x )\n",
    	   pid, vAddr ) ;
    List* tbl ;
    ListElement* elem ;
    SegTblEntry_t* inf ;
    int lbound, ubound ;

    tbl = (List *)kswap->segTblEntries_->LookAtSorted( pid ) ;
    elem = tbl->getFirstItem() ;
    while ( elem != NULL )
    {
        inf = (SegTblEntry_t *)elem->item ;
        lbound = inf->vStart_ ;
        ubound = inf->vStart_ + inf->vLength_ ;

        if ( lbound <= vAddr && vAddr < ubound ) return inf ;
        elem = elem->next ;
    } // loop

    DEBUG( 'k', "Leaving  KSwap::findSegFor()\n" ) ;
    return NULL ;
} // findSegFor

//
// Name:	findLRUPage()
//
int KSwap::findLRUPage()
{
    DEBUG( 'k', "Entering KSwap::findLRUPage()\n" ) ;
    // 
    // Find the LRU page and return it
    //

    int page = -1 ;
    for(DKListElement *cur = revPageTbl_->getBfirst(); cur != NULL; ) {

	//
	// Check to see if the used bit is set
	//	also clearing all the used bits

	if( ((RevPageEntry*)cur->ndata)->use )
	{
	    ((RevPageEntry*)cur->ndata)->use = FALSE ;
	}
	else
	{
	    if( page == -1 ) // only if we don't have this set already
	        page = ((RevPageEntry*)cur->ndata)->physicalPage ;
	}

	cur = cur->b_next;
    }

    // 
    // If we do not find any pages that have the use bit set set it to the 
    //	first page
    if( page == -1 ) page = 0 ;
    DEBUG( 'k', "Leaving  KSwap::findLRUPage()\n" ) ;
    return page ;
}

//
// Name:	getRevPageEntry
//
RevPageEntry* KSwap::getRevPageEntry( int physPage )
{
    DEBUG('H',"Entering getRevPageEntry 0x%04x\n",physPage);
    return (RevPageEntry*)revPageTbl_->SortedRetrieve( physPage, DKList::A ) ;
}

//
// Name:	getRevPageEntry
//
RevPageEntry* KSwap::getRevPageEntry( int pid, int vPage )
{
    //
    // Look in the page table for the RevPageEntry_t on with our "packed 
    //	unsigned long long" 

    DEBUG('H',"Entering getRevPageEntry %i - 0x%04x\n",pid,vPage);
    unsigned long long pidVPage = vPage | ((unsigned long long)pid << 32 ) ;
    return (RevPageEntry*)revPageTbl_->SortedRetrieve( pidVPage, DKList::B ) ;
}

//
// Name:	removeRevPageEntry
//
void KSwap::removeRevPageEntry( int physPage )
{
    DEBUG('I',"Removing revpage 0x%04x\n",physPage);
    RevPageEntry* entry( NULL ) ;
    entry = ( RevPageEntry *)revPageTbl_->SortedRemove( physPage, DKList::A ) ;
    freeMem( entry->physicalPage ) ;
    delete entry ;
}

//
// Name:	removeRevPageEntry
//
void KSwap::removeRevPageEntry( int pid, int vPage )
{
    DEBUG('I',"Removing revpage %i - 0x%04x\n",pid,vPage);
    RevPageEntry* entry( NULL ) ;

    unsigned long long pidVPage = vPage | ((unsigned long long)pid << 32 ) ;
    entry = ( RevPageEntry *)revPageTbl_->SortedRemove( pidVPage, DKList::B ) ;
    freeMem( entry->physicalPage ) ;
    delete entry ;
}

//
// Name:	addRevPageEntry
//
void KSwap::addRevPageEntry( int  physPage,
			     int  vPage,
			     unsigned char pid, 
			     bool	   readOnly,
			     bool          dirty     )
{
    DEBUG('I',"Adding revpage 0x%04x - 0x%04x\n",vPage,physPage);
    RevPageEntry* entry = new RevPageEntry() ;
    entry->physicalPage = physPage ;
    entry->virtualPage  = vPage ;
    entry->pid		= pid ;
    entry->readOnly	= readOnly ;
    entry->dirty	= dirty ;
    entry->use		= false ;
    entry->valid	= true ;

    unsigned long long pidVPage = vPage | ((unsigned long long)pid << 32 ) ;
    revPageTbl_->SortedInsert( physPage, pidVPage, (void*)entry ) ;
}

// Name:	requestSwap
//
void KSwap::requestSwap( Request_t* reqInfo )
{
    DEBUG( 'k', "Entering KSwap::requestSwap()\n" ) ;
    requests_->Append( (void*)reqInfo ) ;
    daemonThdSem_->V() ;
    DEBUG( 'k', "Leaving  KSwap::requestSwap()\n" ) ;
} // requestSwap

//
// Name:	setCurSegTable
//
void KSwap::setCurSegTable( int pid )
{
    curSegTable_ = (List *)segTblEntries_->LookAtSorted( pid ) ;
} // setCurSegTable

//
// Name:	allocMemForPID
//
int KSwap::allocMemForPID( int          pid,
			   int totalSize,
			   NoffHeader*  nh,
			   OpenFile*    exec       )
{
    DEBUG( 'k', "Entering KSwap::allocMemForPID()\n" ) ;
    SegTblEntry_t *entry1(NULL), *entry2(NULL), *entry3(NULL), *entry4(NULL) ;
    List* segTbl ;
    int retVal = 0 ;
    int execSize = 0 ;
    int stackSize = 0 ;

    entry1 = new SegTblEntry_t ;
    entry1->vStart_ = nh->code.virtualAddr ;
    execSize += entry1->vLength_ = nh->code.size ;
    ASSERT( execSize > 0 ) ;
    entry1->inExecFile_ = TRUE ;
    entry1->addrInFile_ = nh->code.inFileAddr ;
    entry1->pid_ = pid ;

    if ( nh->initData.size > 0 )
    {
	entry2 = new SegTblEntry_t ;
	entry2->vStart_ = nh->initData.virtualAddr ;
	execSize += entry2->vLength_ = nh->initData.size ;
	entry2->inExecFile_ = FALSE ;
	sysSwapMutex_->Acquire() ;
	entry2->addrInFile_ = -1 ;
	entry2->addrInFile_ = sysSwap_->allocSegment( entry2->vLength_ ) ;
	sysSwapMutex_->Release() ;
        entry2->pid_ = pid ;
	if ( entry2->addrInFile_ == -1 ) goto Error ;
    }

    if ( nh->uninitData.size > 0 )
    {
	entry3 = new SegTblEntry_t ;
	entry3->vStart_ = nh->uninitData.virtualAddr ;
	execSize += entry3->vLength_ = nh->uninitData.size ;
	entry3->inExecFile_ = FALSE ;
	sysSwapMutex_->Acquire() ;
	entry3->addrInFile_ = -1 ;
	entry3->addrInFile_ = sysSwap_->allocSegment( entry3->vLength_ ) ;
	sysSwapMutex_->Release() ;
        entry3->pid_ = pid ;
	if ( entry3->addrInFile_ == -1 ) goto Error ;
    }

    stackSize = (totalSize * PageSize) - execSize ;
    ASSERT( stackSize > 0 ) ;
    entry4 = new SegTblEntry_t ;
    entry4->vStart_ = TOP_OF_ADDRSPACE - stackSize ;
    entry4->vLength_ = stackSize ;
    entry4->inExecFile_ = FALSE ;
    sysSwapMutex_->Acquire() ;
    entry4->addrInFile_ = -1 ;
    entry4->addrInFile_ = sysSwap_->allocSegment( entry4->vLength_ ) ;
    sysSwapMutex_->Release() ;
    entry4->pid_ = pid ;
    if ( entry4->addrInFile_ == -1 ) goto Error ;

    swapFiles_->SortedInsert( new SwapFile( exec, TRUE ), pid ) ;
    segTbl = new List() ;

#ifdef VM_STATS
    entry1->pgInitIn_ = new BitMap( divRoundUp(entry1->vLength_, PageSize) ) ;
    entry1->pgLRUout_ = new BitMap( divRoundUp(entry1->vLength_, PageSize) ) ;
#endif

    segTbl->SortedInsert( entry1, entry1->vStart_ ) ;
    if(entry2)
    {

#ifdef VM_STATS
        entry2->pgInitIn_ = new BitMap( divRoundUp(entry2->vLength_, PageSize) );
        entry2->pgLRUout_ = new BitMap( divRoundUp(entry2->vLength_, PageSize) );
#endif

	segTbl->SortedInsert( entry2, entry2->vStart_ ) ;
    }
    if(entry3)
    {
#ifdef VM_STATS
        entry3->pgInitIn_ = new BitMap( divRoundUp(entry3->vLength_, PageSize) );
        entry3->pgLRUout_ = new BitMap( divRoundUp(entry3->vLength_, PageSize) );
#endif
	segTbl->SortedInsert( entry3, entry3->vStart_ ) ;
    }
#ifdef VM_STATS
    entry4->pgInitIn_ = new BitMap( divRoundUp(entry4->vLength_, PageSize) ) ;
    entry4->pgLRUout_ = new BitMap( divRoundUp(entry4->vLength_, PageSize) ) ;
#endif
    segTbl->SortedInsert( entry4, entry4->vStart_ ) ;
    segTblEntries_->SortedInsert( segTbl, pid ) ;
    
    retVal = divRoundUp( execSize + stackSize, PageSize ) ;

    DEBUG( 'k', "Leaving  KSwap::allocMemForPID()\n" ) ;
    return retVal ;

Error:

    delete entry1 ;

    sysSwapMutex_->Acquire() ;
    if ( entry2 != NULL && entry2->addrInFile_ > 0 )
    {
        sysSwap_->deallocSegment( entry2->addrInFile_, entry2->vLength_ ) ;
	delete entry2 ;
    }
    if ( entry3 != NULL && entry3->addrInFile_ > 0 )
    {
        sysSwap_->deallocSegment( entry3->addrInFile_, entry3->vLength_ ) ;
	delete entry3 ;
    }
    if ( entry4 != NULL && entry4->addrInFile_ > 0 )
    {
        sysSwap_->deallocSegment( entry4->addrInFile_, entry4->vLength_ ) ;
	delete entry4 ;
    }
    sysSwapMutex_->Release() ;


    return -1 ;
} // allocMemForPID

//
// Name:	loadDataPages
//
int KSwap::loadDataPages( int         pid,
		          NoffHeader* nh,
		          OpenFile*   executable ) 
{
    SegTblEntry_t *entry(NULL);
    int tmpPage = allocMem( pid ) * PageSize;
    int i;
    List* segTbl = (List *)segTblEntries_->LookAtSorted(pid);

    DEBUG('s',"Temporary page 0x%04x allocated\n",tmpPage);

    if ( nh->initData.size > 0 ) {
	entry = (SegTblEntry_t *)segTbl->LookAtSorted(nh->initData.virtualAddr);
	for(i = 0; i < nh->initData.size / PageSize; i++) {
	    executable->ReadAt(machine->mainMemory + tmpPage,
			       PageSize,
			       nh->initData.inFileAddr + (i * PageSize));
	    sysSwap_->swapOut(entry->addrInFile_ + (i * PageSize),tmpPage);
	}
	if(nh->initData.size % PageSize) {
	    i = nh->initData.size / PageSize;
	    memset(machine->mainMemory + tmpPage,NULL,PageSize);
	    executable->ReadAt(machine->mainMemory + tmpPage,
			       nh->initData.size % PageSize,
			       nh->initData.inFileAddr + (i * PageSize));
	    sysSwap_->swapOut(entry->addrInFile_ + (i * PageSize),tmpPage);
	}
    }
    if ( nh->uninitData.size > 0 ) {
	entry = (SegTblEntry_t *)segTbl->LookAtSorted(nh->uninitData.virtualAddr);
	memset(machine->mainMemory + tmpPage,NULL,PageSize);
	for(i = 0; i < nh->uninitData.size / PageSize; i++) {

	    //
	    // uninitData is not in a binary so we just need to put a 
	    //	big chunck of zeros

	    sysSwap_->swapOut(entry->addrInFile_ + (i * PageSize),tmpPage);
	}
	if(nh->uninitData.size % PageSize) {
	    i = nh->initData.size / PageSize;
	    sysSwap_->swapOut(entry->addrInFile_ + (i * PageSize),tmpPage);
	}
    }

    freeMem(tmpPage / PageSize);
    DEBUG('s',"Temp page free'd\n");

    return 0 ;
}

//
// Name:	copyArgs
//
int KSwap::copyArgs( int argc, char *argv, AddrSpace *addrspace)
{
    List* segTbl = (List *)segTblEntries_->LookAtSorted(addrspace->getPID());
    SegTblEntry_t *entry(NULL);

    entry = new SegTblEntry_t ;
    entry->vStart_ = ARGS_VSTART ;
    entry->vLength_ = argc * MAX_ARG_LENGTH ;
    entry->pid_ = addrspace->getPID();
    entry->inExecFile_ = FALSE ;
    entry->addrInFile_ = -1 ;
    entry->addrInFile_ = sysSwap_->allocSegment( entry->vLength_ ) ;
#ifdef VM_STATS
    entry->pgInitIn_ = new BitMap( divRoundUp(entry->vLength_, PageSize) ) ;
    entry->pgLRUout_ = new BitMap( divRoundUp(entry->vLength_, PageSize) ) ;
#endif
    segTbl->SortedInsert( entry, entry->vStart_ ) ;

    int tmpPage = allocMem( entry->pid_ ) * PageSize;
    for(int i = 0; i < divRoundUp(entry->vLength_, PageSize); i++) {
	memcpy(machine->mainMemory + tmpPage, argv + (i * PageSize), entry->vLength_);
	sysSwap_->swapOut(entry->addrInFile_ + (i * PageSize),tmpPage);
    }
    freeMem(tmpPage / PageSize);

    return entry->vStart_;
}

static void
prptHelper(int a)
{
    RevPageEntry *e = (RevPageEntry *)a;

    DEBUG('h',"pid: %i\tvpage: 0x%04x\tppage: 0x%04x\tbits: ",e->pid,e->virtualPage,e->physicalPage);
    if(e->valid) DEBUG('h',"1");
    else DEBUG('h',"0");
    if(e->readOnly) DEBUG('h',"1");
    else DEBUG('h',"0");
    if(e->use) DEBUG('h',"1");
    else DEBUG('h',"0");
    if(e->dirty) DEBUG('h',"1");
    else DEBUG('h',"0");
    DEBUG('h',"\n");
}

void
KSwap::printRevPageTbl()
{
    revPageTbl_->map(prptHelper,DKList::DATA,DKList::A);
    DEBUG('h',"\n");
}

void
KSwap::printSegTbl(int pid)
{
    List* tbl ;
    ListElement* elem ;
    SegTblEntry_t* inf ;

    DEBUG('Y',"Segment table\n-------------\n");
    tbl = (List *)kswap->segTblEntries_->LookAtSorted( pid ) ;
    for(elem = tbl->getFirstItem(); elem != NULL; elem = elem->next) {
        inf = (SegTblEntry_t *)elem->item ;
	DEBUG('Y',"pid: %i\tstart: 0x%04x\tlength: 0x%04x\t",inf->pid_,inf->vStart_,inf->vLength_);
	if(inf->inExecFile_)
	    DEBUG('Y',"exec pos: 0x%04x\n",inf->addrInFile_);
	else
	    DEBUG('Y',"swap pos: 0x%04x\n",inf->addrInFile_);
    }
    DEBUG('Y',"\n");
}
