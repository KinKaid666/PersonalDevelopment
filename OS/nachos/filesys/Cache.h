// File:         $Id: Cache.h,v 3.1 2001/11/11 03:55:07 eae8264 Exp $
// Author:       p544-01b
// Contributors: {}
// Description:  This represents the general cache scheme for our file system
//		 with some helper utility implementation.  This class is 
//		 abstract.
// Revisions:
//               $Log: Cache.h,v $
//               Revision 3.1  2001/11/11 03:55:07  eae8264
//               Added some classes that don't compile for some reaason.
//               Nothing works anywhere, so I don't feel bad!
//

#ifndef _Cache_H
#define _Cache_H

#include "list.h"
#include "synch.h"

class List ;
class Lock ;

class Cache
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
	Cache( unsigned int cacheSize ) ;

public: // Destructors

	//
	// Name:	(destructor)
	//
	// Description: Clean up
	//
	// Arguments:	None
	//
	virtual ~Cache() ;

protected: // Helper Functions

	//
	// Name:	makeSpaceFor
	//
	// Description: Makes numItems amount of space available in the 
	//		cache.  If the space is already available, noop.
	//
	// Arguments:	The number of cache items to make space for in the
	//		cache.
	//
	// Returns:	void
	//
	virtual void makeSpaceFor( unsigned int numItems ) = 0 ;

	//
	// Name:	getDataFromDisk
	//
	// Description: Just abstracts getting data from disk.
	//
	// Arguments:	Address on disk in bytes, the buffer to copy to
	// 		and the number of bytes.
	//		NOTE: if disk address is invalid, system dies!
	//
	// Returns:	void
	//
	void getDataFromDisk( T_WORD address,
			      unsigned char* buf,
			      unsigned int len
			     ) ;

	//
	// Name:	writeDataToDisk
	//
	// Description: Just abstracts writing data to disk.
	//
	// Arguments:	Address on disk in bytes, the buffer to copy from
	// 		and the number of bytes.
	//		NOTE: if disk address is invalid, system dies!
	//
	// Returns:	void
	//
	void writeDataToDisk( T_WORD address,
			      unsigned char* buf,
			      unsigned int len
			     ) ;

protected: // Data Members

	// The cache-- a dynamically allocated list of nodes.
	List* cache_ ;
	
	// Max and current cache size
	unsigned int maxSize_ ;
	unsigned int curSize_ ;
	
	// Mutex for getNode and giveUpNode
	Lock* mutex_ ;

	// The buffer used to copy data to and from the disk.  We put
	// it here so that we don't have to create it everytime we
	// do a read or write or otherwise so that there's not a 
	// buffer statically allocated for each function.
	unsigned char* rwBuf_ ;

	// The disk size-- stored here so the calculation doesn't have
	// to be repeatedly done.
	unsigned int diskSize_ ;
}; // Cache

#endif
