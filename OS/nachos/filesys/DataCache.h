// File:         $Id: DataCache.h,v 3.5 2001/11/14 19:24:21 eae8264 Exp $
// Author:       p544-01b
// Contributors: {}
// Description:  This is the cache for our file system.
// Revisions:
//               $Log: DataCache.h,v $
//               Revision 3.5  2001/11/14 19:24:21  eae8264
//               Added Free item functions for the cache subclasses.
//
//               Revision 3.4  2001/11/11 23:53:07  eae8264
//               Implementation for DataCache and some bug fixes in NodeCache.
//
//               Revision 3.3  2001/11/11 23:04:16  eae8264
//               Changed some things.
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

#ifndef _DataCache_H
#define _DataCache_H

#include "Cache.h"

class DataCache
:
    private Cache
{

public: // Constructors

	//
	// Name:	(constructor)
	//
	// Description: Creates node cache with some options
	//
	// Arguments:	cacheSize-- number of nodes that cache can
	//			    handle (must be > 1).
	//
	DataCache( unsigned int cacheSize ) ;

public: // Destructors

	//
	// Name:	(destructor)
	//
	// Description: Clean up
	//
	// Arguments:	None
	//
	~DataCache() ;

public: // Client functions

	//
	// Name:	getChunk
	//
	// Description: Gets chunk from disk and puts it in cache if not already
	//		there.
	//
	// Arguments:	Unique node id,
	//
	// Returns:	Pointer to retrieved data chunk or NULL if in use.
	//
	unsigned char* getChunk( T_WORD id ) ;

	//
	// Name:	giveUpChunk
	//
	// Description: If nobody has a reference to this, remove it from 
	//		cache and write it to disk.
	//
	// Arguments:	The id of the chunk.
	//
	// Returns:	void
	//
	void giveUpChunk( T_WORD id ) ;

	//
	// Name:	freeChunk
	//
	// Description: Remove chunk from cache.
	//
	// Arguments:	The id of the chunk.
	//
	// Returns:	void
	//
	void freeChunk( T_WORD id ) ;

	//
	// Name:	writeOutChunk
	//
	// Description: Write node out to disk eventually.  If force
	//		is true, an immediate write will occur.
	//
	// Arguments:	Chunk id, whether or not to write immediately
	//
	// Returns:	void
	//
	void writeOutChunk( T_WORD id, bool force = false ) ;

private: // Helper Functions

	//
	// Name:	makeSpaceFor
	//
	// Description: Makes numItems amount of space available in the 
	//		cache.  If the space is already available, noop.
	//
	// Arguments:	The number of nodes to make space for in the cache.
	//
	// Returns:	void
	//
	void makeSpaceFor( unsigned int numItems ) ;

private: // Structure definition

	struct ChunkInfo_t
	{
	    unsigned char* chunk_ ;
	    T_WORD id_  ;
	    T_WORD diskAddr_ ;
	    bool inUse_ ;
	    bool dirty_ ;
	} ;

}; // DataCache

#endif
