// File:         $Id: NodeCache.cc,v 3.23 2001/11/14 19:24:21 eae8264 Exp $
// Author:       p544-01b
// Contributors: 
// Description:	 The implementation file for NodeCache
// Revisions:
//               $Log: NodeCache.cc,v $
//               Revision 3.23  2001/11/14 19:24:21  eae8264
//               Added Free item functions for the cache subclasses.
//
//               Revision 3.22  2001/11/14 01:01:00  eae8264
//               Fixed a deadlock in both cache subclasses.  yay for me.
//
//               Revision 3.21  2001/11/13 17:28:05  eae8264
//               Added some debug to show that read/write of buffers works.
//
//               Revision 3.20  2001/11/13 16:19:02  trc2876
//               can copy one file now
//
//               Revision 3.19  2001/11/13 03:13:10  trc2876
//               AHHHHHH
//
//               Revision 3.18  2001/11/13 02:52:25  eae8264
//               Fixed a synch prob with release in getNode
//
//               Revision 3.17  2001/11/12 23:07:53  trc2876
//               debugging cp
//
//               Revision 3.16  2001/11/12 00:04:27  eae8264
//               Fixed two curSize_++ errors.
//
//               Revision 3.15  2001/11/11 23:53:07  eae8264
//               Implementation for DataCache and some bug fixes in NodeCache.
//
//               Revision 3.14  2001/11/11 23:04:16  eae8264
//               Changed some things.
//
//               Revision 3.13  2001/11/11 21:29:19  trc2876
//               filesystem formatting works
//
//               Revision 3.12  2001/11/11 19:12:42  etf2954
//               fixed conflicts
//
//               Revision 3.11  2001/11/11 18:32:43  trc2876
//               Fixed compile errors
//               some have been // out because they don't fit the layout of the class and I'm not
//                   sure what is correct....they have been preceded by a // todo: fix this
//
//               MTreeNode->{dirty_,inUse_} have mutators and accessors and refs to them outside
//                   MTreeNode have been changed
//
//               Revision 3.10  2001/11/11 03:53:17  eae8264
//               Updated to work with new parent class Cache.
//
//               Revision 3.9  2001/11/10 21:04:45  etf2954
//               code review
//
//               Revision 3.8  2001/11/10 00:16:25  eae8264
//               Changed NodeCache so that node parameters may be
//               null, so that some client functions may be simplified.
//               A client call was updated to work with the new sig.
//
//               Revision 3.6  2001/11/09 02:40:44  eae8264
//               I manually "copied and pasted" the revision comments from
//               the output of cvs log with some formatting... I'm using a
//               new editor and wasn't reloading the file after checking
//               the files in.
//
//               Revision 3.5  2001/11/09 02:27:48  eae8264
//               Bug Fixes, Efficiency enhancement for writeOutNode,
//               subclass checking to be able to use appropriate
//               casting in writeOutNode, implementation for
//               makeSpaceFor().
//
//               Revision 3.4 2001/11/09 01:00:14  eae8264
//               Some restructuring because of a misunderstanding
//               of node persistence.  A few bug fixes and
//               enhancements.
//
//               Revision 3.3 2001/11/08 23:44:34  eae8264
//               Changed some things in NodeCache... other things
//               had to change as well.
//
//               Revision 3.2 2001/11/08 23:25:28  trc2876
//               added insert to etree and fixed linker error
//
//               Revision 3.1  2001/11/08 22:16:11  eae8264
//               Added.
//
//

#include "NodeCache.h"

//
// Name:	(constructor)
//
NodeCache::NodeCache( bool cacheRoot, unsigned int cacheSize )
:	
	Cache( cacheSize ),
	rootInCache_( cacheRoot )
{
    if ( rootInCache_ ) getNode( 0 )->inUse( false );
} // constructor

//
// Name: (destructor)
//
NodeCache::~NodeCache()
{
    ListElement* elem ;

    elem = cache_->getFirstItem() ;
    for ( MTreeNode* node ; elem != NULL ; elem = elem->next )
    {
	node = (MTreeNode *)elem->item ;
	if ( node->dirty() ) writeOutNode( node, true, false ) ;
	delete node ;
    }
    delete cache_ ;
    delete mutex_ ;
    delete rwBuf_ ; 
} // destructor

//
// Name: getNode
//
MTreeNode* NodeCache::getNode( T_WORD id )
{
    unsigned char* buffer( NULL ) ;
    MTreeNode* node( NULL ) ;
    MTreeNode* returnVal( NULL ) ;

    mutex_->Acquire() ;

    DEBUG('q',"NodeCache::getnode(0x%04x)\n",id);
    returnVal = (MTreeNode *)cache_->LookAtSorted( (int)id ) ;

    if ( returnVal )
    {
	
	DEBUG('q',"Found node\n");
	if ( returnVal->inUse() )
	{
	    mutex_->Release() ;
	    DEBUG('q',"Node in use\n");
	    return NULL ;
	}
	else
	{
	    returnVal->inUse( true ) ;
	    DEBUG('q',"Locked[1] node 0x%04x\n",returnVal->id());
	    mutex_->Release() ;
	    return returnVal ;
	}
    }
 
    buffer = new unsigned char[ NODE_SIZE ] ;
    getDataFromDisk( id * NODE_SIZE, buffer, NODE_SIZE ) ;

    switch ( (MTreeNode::node_t)UNPACK_HEADER(0, 2, buffer[0]) )
    {
	case MTreeNode::RAW:
	    node = new MTreeRawNode( id ) ;
	    ( (MTreeRawNode *)node )->unpickle( buffer ) ;
	    break ;
	case MTreeNode::INTERNAL:
	    node = new MTreeInternalNode( id ) ;
	    ( (MTreeInternalNode *)node )->unpickle( buffer ) ;
	    break ;
	case MTreeNode::META:
	    node = new MTreeMetaNode( id ) ;
	    ( (MTreeMetaNode *)node )->unpickle( buffer ) ;
	    break ;
	case MTreeNode::DATA:
	    node = new MTreeDataNode( id ) ;
	    ( (MTreeDataNode *)node )->unpickle( buffer ) ;
	    break ;
	default:
	    ASSERT( false ) ; // Should not happen, let's crash.
    } // switch

    if ( DebugIsEnabled( 'Q' ) ) // no reason to loop with noops
    {
	DEBUG( 'Q', "Reading node %d from disk\n", node->id() ) ;
	for ( char i = 0; i < NODE_SIZE; i++ ) DEBUG( 'Q', "%d-", buffer[i] ) ;
	DEBUG( 'Q', "\n" ) ;
    }

    node->inUse( true );
    mutex_->Release() ;
    makeSpaceFor( 1 ) ;
    mutex_->Acquire() ;
    cache_->SortedInsert( (void *)node, (int)id ) ;
    curSize_++ ;
    mutex_->Release() ;
    delete buffer ;
    DEBUG('q',"Locked[2] node 0x%04x\n",node->id());
    return node ;
} // getNode

//
// Name: giveUpNode
//
void NodeCache::giveUpNode( MTreeNode* node )
{
    if ( !node ) return ;
    mutex_->Acquire() ;

    if(!node->inUse()) {
	mutex_->Release();
	return;
    }
    node->inUse( false );
    DEBUG('q',"Released node 0x%04x\n",node->id());
    mutex_->Release() ;
} // giveUpNode

//
// Name: writeOutNode
//
void NodeCache::writeOutNode( MTreeNode* node, bool force, bool ensureInCache )
{
    unsigned char* buffer( NULL ) ;
    MTreeNode* tmp ;

    if ( !node ) return ;

    mutex_->Acquire() ;
    DEBUG('q',"Writing node 0x%04x\n",node->id());
    if ( force ) {
	buffer = new unsigned char[ NODE_SIZE ] ;
	switch ( node->type() )
	{
	    case MTreeNode::RAW:
		( (MTreeRawNode *)node )->pickle( buffer ) ;
	        DEBUG( 'Q', "Writing out RAW node %d to disk\n", node->id() ) ;
		break ;
	    case MTreeNode::INTERNAL:
		( (MTreeInternalNode *)node )->pickle( buffer ) ;
	        DEBUG( 'Q', "Writing out INT node %d to disk\n", node->id() ) ;
		break ;
	    case MTreeNode::META:
		( (MTreeMetaNode *)node )->pickle( buffer ) ;
	        DEBUG( 'Q', "Writing out MET node %d to disk\n", node->id() ) ;
		break ;
	    case MTreeNode::DATA:
		( (MTreeDataNode *)node )->pickle( buffer ) ;
	        DEBUG( 'Q', "Writing out DAT node %d to disk\n", node->id() ) ;
		break ;
	    default:
		ASSERT( false ) ; // Should not happen, let's crash.
	} // switch
	if ( DebugIsEnabled( 'Q' ) ) // no reason to loop with noops
	{
	    for ( int i = 0; i < NODE_SIZE; i++ ) DEBUG('Q', "%d-", buffer[i]) ;
	}
	DEBUG( 'Q', "\n" ) ;
        writeDataToDisk( node->id() * NODE_SIZE, buffer, NODE_SIZE ) ;
	node->dirty( false );
	delete buffer ;
    }
    else
    {
	node->dirty( true );
    }

    if ( ensureInCache )
    {
	if ( (tmp=(MTreeNode*)cache_->LookAtSorted((int)node->id())) != NULL )
	{
	    ASSERT( node == tmp ) ; // Sanity Check
	}
	else
	{
	    makeSpaceFor( 1 ) ;
	    cache_->SortedInsert( (void *)node, (int)node->id() ) ;
	}
    }
    mutex_->Release() ;
} // writeOutNode

//
// Name: freeNode
//
void NodeCache::freeNode( T_WORD id )
{
    ASSERT( !( rootInCache_ && id == 0 ) ) ;
    cache_->KeySortedRemove( id ) ;
} // freeNode

//
// Name: makeSpaceFor
//
void NodeCache::makeSpaceFor( unsigned int numItems )
{
    ListElement* elem( cache_->getFirstItem() ) ;
    if( !elem ) return ;	// list is empty, no need to free stuff
    unsigned int numToRemove ;
    unsigned int freeSpace ;
    MTreeNode* node ;

    ASSERT( !(numItems == maxSize_ && rootInCache_) && numItems <= maxSize_ ) ;
    ASSERT( curSize_ <= maxSize_ ) ; // Sanity check

    freeSpace = maxSize_ - curSize_ ;

    if ( numItems <= freeSpace ) return ;

    numToRemove = numItems - freeSpace ;

    if ( rootInCache_ ) elem = elem->next ;

    for ( unsigned int i = 0; i < numToRemove; i++ )
    {
	node = (MTreeNode *)elem->item ;
	elem = elem->next ;
	if ( node->dirty() ) writeOutNode( node, true, false ) ;
	cache_->KeySortedRemove( node->id() ) ;
	delete node ;
	curSize_-- ;
    }
} // makeSpaceFor

