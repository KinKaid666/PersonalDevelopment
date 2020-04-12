// File:         $Id: DataCache.cc,v 3.9 2001/11/14 19:24:20 eae8264 Exp $
// Author:       p544-01b
// Contributors: 
// Description:	 The implementation file for DataCache
// Revisions:
//               $Log: DataCache.cc,v $
//               Revision 3.9  2001/11/14 19:24:20  eae8264
//               Added Free item functions for the cache subclasses.
//
//               Revision 3.8  2001/11/14 01:01:00  eae8264
//               Fixed a deadlock in both cache subclasses.  yay for me.
//
//               Revision 3.7  2001/11/13 00:41:16  trc2876
//               <hack>
//
//               Revision 3.6  2001/11/12 00:44:51  trc2876
//               code review
//
//               Revision 3.5  2001/11/12 00:04:27  eae8264
//               Fixed two curSize_++ errors.
//
//               Revision 3.3  2001/11/11 23:53:07  eae8264
//               Implementation for DataCache and some bug fixes in NodeCache.
//
//               Revision 3.2  2001/11/11 18:32:43  trc2876
//               Fixed compile errors
//               some have been // out because they don't fit the layout of the class and I'm not
//                   sure what is correct....they have been preceded by a // todo: fix this
//
//               MTreeNode->{dirty_,inUse_} have mutators and accessors and refs to them outside
//                   MTreeNode have been changed
//
//               Revision 3.1  2001/11/11 03:55:08  eae8264
//               Added some classes that don't compile for some reaason.
//               Nothing works anywhere, so I don't feel bad!
//

#include "DataCache.h"


//
// Name:	(constructor)
//
DataCache::DataCache( unsigned int cacheSize ) :	
	Cache( cacheSize )
{
    DEBUG( 'Q', "DataCache created with size %d\n", cacheSize ) ;
} // constructor

//
// Name: (destructor)
//
DataCache::~DataCache()
{
    ListElement* elem ;

    DEBUG( 'Q', "DataCache destroyed with size %d\n", curSize_ ) ;
    elem = cache_->getFirstItem() ;
    for ( ChunkInfo_t* inf ; elem != NULL ; elem = elem->next )
    {
	inf = (ChunkInfo_t *)elem->item ;
	writeOutChunk( inf->id_, true ) ;
	delete inf->chunk_ ;
	delete inf ;
    }
} // destructor

//
// Name: getChunk
//
unsigned char* DataCache::getChunk( T_WORD id )
{
    unsigned char* returnVal( NULL ) ;
    ChunkInfo_t* inf( NULL );

    ASSERT( id != 0 && (id * SectorSize <= diskSize_) ) ;

    DEBUG( 'Q', "DataCache::getChunk( id=%d ) ;", (int)id ) ;
    mutex_->Acquire() ;

    if ( (inf=(ChunkInfo_t *)cache_->LookAtSorted((int)id)) != NULL ) 
    {
	if ( inf->inUse_ )
	{
	    DEBUG( 'Q', " -> chunk in use!\n" ) ;
	    mutex_->Release() ;
	    return NULL ;
	}
	inf->inUse_ = true ;
	mutex_->Release() ;
	DEBUG( 'Q', " -> chunk obtained!\n" ) ;
	return inf->chunk_ ;
    }
 
    // <hack>
    int *foo = new int [42]; delete foo;
    // </hack>
    returnVal = new unsigned char[ SectorSize ] ;
    inf       = new ChunkInfo_t ;

    inf->chunk_    = returnVal ;
    inf->id_       = id        ;
    inf->diskAddr_ = diskSize_ - ( id * SectorSize ) ;
    inf->inUse_    = true      ;
    inf->dirty_    = false     ;

    getDataFromDisk( inf->diskAddr_, returnVal, SectorSize ) ;

    mutex_->Release() ;
    makeSpaceFor( 1 ) ;
    mutex_->Acquire() ;
    cache_->SortedInsert( (void *)inf, (int)id ) ;
    curSize_++ ;
    mutex_->Release() ;
    DEBUG( 'Q', " -> chunk obtained!\n" ) ;
    return returnVal ;
} // getChunk

//
// Name: giveUpChunk
//
void DataCache::giveUpChunk( T_WORD id )
{
    ChunkInfo_t* inf ;

    if ( id == 0 )
    {
	DEBUG( 'Q', "*** WARNING: Attempt to give up data id 0\n" ) ;
	return ;
    } 

    mutex_->Acquire() ;
    inf = (ChunkInfo_t *)cache_->LookAtSorted( (int)id ) ;
    ASSERT( inf ) ;

    ASSERT( inf->inUse_ ) ; // Sanity check...
    inf->inUse_ = false ;

    mutex_->Release() ;
} // giveUpChunk

//
// Name: freeChunk
//
void DataCache::freeChunk( T_WORD id )
{
    cache_->KeySortedRemove( id ) ;
} // freeChunk

//
// Name: writeOutChunk
//
void DataCache::writeOutChunk( T_WORD id, bool force )
{
    ChunkInfo_t* inf ;

    if ( id == 0 )
    {
	DEBUG( 'Q', "*** WARNING: Attempt to write data id 0\n" ) ;
	return ;
    } 

    mutex_->Acquire() ;
    inf = (ChunkInfo_t *)cache_->LookAtSorted( (int)id ) ;

    ASSERT( inf ) ;

    if ( force ) {
        writeDataToDisk( inf->diskAddr_, inf->chunk_, SectorSize ) ;
	inf->dirty_ = false ;
    }
    else
    {
	inf->dirty_ = true ;
    }

    mutex_->Release() ;
} // writeOutChunk

//
// Name: makeSpaceFor
//
void DataCache::makeSpaceFor( unsigned int numItems )
{
    ListElement* elem( cache_->getFirstItem() ) ;
    unsigned int numToRemove ;
    unsigned int freeSpace ;
    ChunkInfo_t* inf;

    ASSERT( numItems <= maxSize_ ) ;
    ASSERT( curSize_ <= maxSize_ ) ; // Sanity check

    freeSpace = maxSize_ - curSize_ ;

    if ( numItems <= freeSpace ) return ;

    numToRemove = numItems - freeSpace ;

    for ( unsigned int i = 0; i < numToRemove && elem != NULL; i++ )
    {
	inf = (ChunkInfo_t *)elem->item ;
	elem = elem->next ;
	if ( inf->dirty_ ) writeOutChunk( inf->id_, true ) ;
	cache_->KeySortedRemove( (int)inf->id_ ) ;
	delete inf->chunk_ ;
	delete inf ;
	curSize_-- ;
    }
} // makeSpaceFor
