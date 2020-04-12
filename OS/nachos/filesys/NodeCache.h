// File:         $Id: NodeCache.h,v 3.10 2001/11/14 19:24:21 eae8264 Exp $
// Author:       p544-01b
// Contributors: {}
// Description:  This is the cache for our file system.
// Revisions:
//               $Log: NodeCache.h,v $
//               Revision 3.10  2001/11/14 19:24:21  eae8264
//               Added Free item functions for the cache subclasses.
//
//               Revision 3.9  2001/11/11 18:32:43  trc2876
//               Fixed compile errors
//               some have been // out because they don't fit the layout of the class and I'm not
//                   sure what is correct....they have been preceded by a // todo: fix this
//
//               MTreeNode->{dirty_,inUse_} have mutators and accessors and refs to them outside
//                   MTreeNode have been changed
//
//               Revision 3.8  2001/11/11 03:53:18  eae8264
//               Updated to work with new parent class Cache.
//
//               Revision 3.7  2001/11/10 00:16:25  eae8264
//               Changed NodeCache so that node parameters may be
//               null, so that some client functions may be simplified.
//               A client call was updated to work with the new sig.
//
//               Revision 3.6  2001/11/09 23:40:42  eae8264
//               Implemented getDataFromDisk, writeDataToDisk, and
//               made use of mutex on client functions.
//
//               Revision 3.5  2001/11/09 02:40:45  eae8264
//               I manually "copied and pasted" the revision comments from
//               the output of cvs log with some formatting... I'm using a
//               new editor and wasn't reloading the file after checking
//               the files in.
//
//               Revision 3.4  2001/11/09 02:27:48  eae8264
//               Bug Fixes, Efficiency enhancement for writeOutNode,
//               subclass checking to be able to use appropriate
//               casting in writeOutNode, implementation for
//               makeSpaceFor().
//
//               Revision 3.3 2001/11/09 01:00:14  eae8264
//               Some restructuring because of a misunderstanding
//               of node persistence.  A few bug fixes and
//               enhancements.
//
//               Revision 3.2 2001/11/08 23:44:34  eae8264
//               Changed some things in NodeCache... other things
//               had to change as well.
//
//               Revision 3.1  2001/11/08 22:16:11  eae8264
//               Added.
//

#ifndef _NodeCache_H
#define _NodeCache_H

#include "mtree.h"
#include "Cache.h"

class NodeCache
: virtual public Cache

{

public: // Constructors

	//
	// Name:	(constructor)
	//
	// Description: Creates node cache with some options
	//
	// Arguments:	cacheRoot-- always keep root in cache
	//		cacheSize-- number of nodes that cache can
	//			    handle (must be > 1).
	//
	NodeCache( bool cacheRoot, unsigned int cacheSize ) ;

public: // Destructors

	//
	// Name:	(destructor)
	//
	// Description: Clean up
	//
	// Arguments:	None
	//
	virtual ~NodeCache() ;

public: // Client functions

	//
	// Name:	getNode
	//
	// Description: Gets node from disk and puts it in cache if not already
	//		there.
	//
	// Arguments:	Unique node id
	//
	// Returns:	The node object -- null if node is in use
	//
	MTreeNode* getNode( T_WORD id ) ;

	//
	// Name:	giveUpNode
	//
	// Description: If nobody has a reference to this, remove it from 
	//		cache and write it to disk.
	//
	// Arguments:	Node reference-- function is a noop if NULL
	//
	// Returns:	void
	//
	void giveUpNode( MTreeNode* node ) ;

	//
	// Name:	freeNode
	//
	// Description: Remove node from cache.
	//
	// Arguments:	Node id -- must not be root node if forced in cache
	//
	// Returns:	void
	//
	void freeNode( T_WORD id ) ;

	//
	// Name:	writeOutNode
	//
	// Description: Write node out to disk eventually.  If force
	//		is true, an immediate write will occur.
	//
	// Arguments:	Node reference-- funciont is a noop if NULL,
	//		whether or not to force write now,
	//		and whether to make sure that the node is in 
	//		cache (this argument if for efficiency sake so
	//		that the cache doesn't have to be searched if
	//		it's not necessary).  The default for the third
	//		argument performs this search to "ensure" it is
	//		in cache.
	//
	// Returns:	void
	//
	void writeOutNode( MTreeNode* node,
			   bool force = false,
			   bool ensureInCache = true
			  ) ;

        //
        // Name:        makeSpaceFor (implementation for abstract)
        //
        // Description: Makes numItems amount of space available in the
        //              cache.  If the space is already available, noop.
        //
        // Arguments:   The number of cache items to make space for in the
        //              cache.
        //
        // Returns:     void
        //
        void makeSpaceFor( unsigned int numItems ) ;

private: // Data Members

	// Root node always in cache?
	bool rootInCache_ ;

}; // NodeCache

#endif
