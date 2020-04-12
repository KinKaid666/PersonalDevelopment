// File:         $Id: mtree.cc,v 3.22 2001/11/15 01:44:43 trc2876 Exp $
// Author:       Trevor Clarke, Eric Eells, Eric Ferguson, Dan Westgate
// Contributors: {}
// Description:  Metadata tree
// Revisions:
//		 $Log: mtree.cc,v $
//		 Revision 3.22  2001/11/15 01:44:43  trc2876
//		 *** empty log message ***
//
//		 Revision 3.21  2001/11/14 16:53:51  trc2876
//		 fixed MTreeMetaNode::filename() problem
//		
//		 Revision 3.20  2001/11/13 22:19:13  eae8264
//		 Fixed a synch bug... wedged machine instead of
//		 yield when waiting for a node.
//		
//		 Revision 3.19  2001/11/13 17:53:38  trc2876
//		 locking issues fixed
//		
//		 Revision 3.18  2001/11/13 03:13:10  trc2876
//		 AHHHHHH
//		
//		 Revision 3.17  2001/11/12 02:13:06  trc2876
//		 remove extra nodes in MTreeMetaNode::filename()
//		
//		 Revision 3.16  2001/11/12 02:10:28  trc2876
//		 Changed MTreeMetaNode->filename() to allow for copying over of existing filename
//		 Changed FileSystem::Create() to overwrite existing file
//		
//		 Revision 3.15  2001/11/12 02:01:41  trc2876
//		 can overwrite existing filename nodes
//		
//		 Revision 3.14  2001/11/10 01:24:35  trc2876
//		 *** empty log message ***
//		
//		 Revision 3.13  2001/11/10 00:54:09  trc2876
//		 fixed compile stuff
//		
//		 Revision 3.12  2001/11/10 00:24:03  trc2876
//		 added len_ to MTreeDataNode
//		
//		 Revision 3.11  2001/11/09 00:24:21  trc2876
//		 fixed compile warning and added children_ mutator
//		
//		 Revision 3.10  2001/11/09 00:18:28  trc2876
//		 removed lock stuff from disk
//		
//		 Revision 3.9  2001/11/09 00:12:09  trc2876
//		 added pickle/unpickle funcs
//		
//		 Revision 3.8  2001/11/08 23:44:34  eae8264
//		 Changed some things in NodeCache... other things
//		 had to change as well.
//		
//		 Revision 3.7  2001/11/08 22:40:47  trc2876
//		 added dirty bit
//		
//		 Revision 3.6  2001/11/08 18:29:58  eae8264
//		 small things...
//		
//		 Revision 3.5  2001/11/08 00:04:09  trc2876
//		 added stubs
//		
//		 Revision 3.4  2001/11/06 19:45:05  eae8264
//		 Fixed a compiler error.  Cassted an int to MTreeNode.
//		
//		 Revision 3.3  2001/11/06 00:24:21  trc2876
//		 oops....we don't want to preload all the meta data so those MTreeNode *'s should be
//		 node numbers....when we need to access a new node, we should grab it from disk or, preferably
//		 a cache
//		
//		 Revision 3.2  2001/11/06 00:17:04  trc2876
//		 added permission accessor/mutator
//		
//		 Revision 3.1  2001/11/05 23:50:46  trc2876
//		 added mtree files
//		 class MTreeNode
//		 class MTreeRawNode
//		 class MTreeMetaNode
//		 class MTreeDataNode
//		
//		 These are not complete or tested but they compile cleanly
//		
//

#include <assert.h>
#include <string.h>
#include "utility.h"
#include "machine.h"
#include "system.h"
#include "mtree.h"

//
// MTreeNode::MTreeNode()
//
MTreeNode::MTreeNode(MTreeNode::node_t t, T_WORD nid) : id_(nid), header_(0),
							inUse_(FALSE),
							dirty_(FALSE)
{
    // store the type in the first 2 bits of the header
    //
    PACK_HEADER(0, 2, (int)t, header_);
}

//
// MTreeNode::type()
//
MTreeNode::node_t
MTreeNode::type()
{
    return (node_t)UNPACK_HEADER(0, 2, header_);
}

//
// MTreeNode::pickle()
//
void
MTreeNode::pickle(unsigned char *buf)
{
    unsigned int tmp(WordToMachine(header_));
    memcpy(buf,(unsigned char *)&tmp,4);
}

//
// MTreeNode::unpickle()
//
void
MTreeNode::unpickle(unsigned char *buf)
{
    header_ = WordToHost(*((unsigned int *)buf));
}

//
// MTreeRawNode::pickle()
//
void
MTreeRawNode::pickle(unsigned char *buf)
{
    MTreeNode::pickle(buf);

    memcpy(buf + 4,data,31);
}

//
// MTreeRawNode::unpickle()
//
void
MTreeRawNode::unpickle(unsigned char *buf)
{
    MTreeNode::unpickle(buf);
    memcpy(data,buf + 4,31);
}

//
// MTreeInternalNode::child()
//
T_WORD
MTreeInternalNode::child(size_t no)
{
    return (no >= 4) ? 0x0000 : children_[no];
}

//
// MTreeInternalNode::child()
//
void
MTreeInternalNode::child(size_t no, T_WORD val)
{
    if(no < 4)
	children_[no] = val;
}

//
// MTreeInternalNode::nchildren()
//
size_t
MTreeInternalNode::nchildren()
{
    size_t n;
    for(n = 0; n < 4 && children_[n] != 0x0000; n++)
	;
    return n;
}

//
// MTreeInternalNode::pickle()
//
void
MTreeInternalNode::pickle(unsigned char *buf)
{
    MTreeNode::pickle(buf);

    unsigned int tmp;
    size_t i;
    
    for(i = 0; i < 4; i++) {
	tmp = WordToMachine(children_[i]);
	memcpy(buf + 4 + i * 4,(unsigned char *)&tmp,4);
    }
    memcpy(buf + 20,special,15);
}

//
// MTreeInternalNode::unpickle()
//
void
MTreeInternalNode::unpickle(unsigned char *buf)
{
    MTreeNode::unpickle(buf);
    size_t i;
    for(i = 0; i < 4; i++) {
	children_[i] = WordToHost(*((unsigned int *)(buf + 4 + i * 4)));
    }
    memcpy(special,buf + 20, 15);
}

//
// MTreeMetaNode::directory()
//
bool
MTreeMetaNode::directory()
{
    return UNPACK_HEADER(4, 1, header_);
}

//
// MTreeMetaNode::directory()
//
void
MTreeMetaNode::directory(bool d)
{
    ZERO_HEADER(4, 1, header_);
    PACK_HEADER(4, 1, d, header_);
}

//
// MTreeMetaNode::getmod()
//
T_BYTE
MTreeMetaNode::getmod()
{
    return (T_BYTE)UNPACK_HEADER(5, 3, header_);
}

//
// MTreeMetaNode::chmod()
//
void
MTreeMetaNode::chmod(T_BYTE perms)
{
    ZERO_HEADER(5, 3, header_);
    PACK_HEADER(5, 3, perms, header_);
}

//
// MTreeMetaNode::filename()
//
char *
MTreeMetaNode::filename()
{
    char *fn(NULL);
    size_t nnodes;
    MTreeNode *node(NULL);
    for(nnodes = 0; nnodes <4 && filename_[nnodes] != 0x0000; nnodes++)
	;
    
    fn = new char[nnodes * 31]; // maximum size the filename can be at
				// depth 1, including the NULL
    
    for(size_t i = 0; i < nnodes; i++) {
	if((node = fileSystem->getNode(filename_[i])) == NULL)
	    currentThread->Yield() ;
	switch(node->type()) {
	    case RAW:
		memccpy(fn + (i * 31), ((MTreeRawNode *)node)->data, NULL, 31);
		break;
	    case INTERNAL:
		// FEATURE: support nested filename nodes
	    default:
		// invalid node type
		fileSystem->releaseNode(node);
		delete fn;
		return NULL;
	}
	fileSystem->releaseNode(node);
    }
    return fn;
}

//
// MTreeMetaNode::filename()
//
size_t
MTreeMetaNode::filename(const char *fn)
{
    size_t nnodes(divRoundUp(strlen(fn),31));

    ASSERT(nnodes <= 4); // FEATURE: support nested filename nodes

    MTreeRawNode *node(NULL);
    size_t i;
    for(i = 0; i < nnodes; i++) {
	if(filename_[i] == 0x0000) {
	    node = new MTreeRawNode(fileSystem->allocateNode());
	    if(node->id() == 0x0000) {
		delete node;
		return 0; // trouble
	    }
	    filename_[i] = node->id();
	} else {
	    if((node = (MTreeRawNode *)fileSystem->getNode(filename_[i])) == NULL)
		return 0; // trouble
	}
	memccpy(node->data, fn + (i * 31), NULL, 31);
	fileSystem->putNode(node);
	fileSystem->releaseNode(node);
    }
    for(; i < 4; i++) {
	if(filename_[i] != 0x0000) {
	    fileSystem->freeNode(filename_[i]);
	    filename_[i] = 0x0000;
	}
    }

    return nnodes;
}

//
// MTreeMetaNode::pickle()
//
void
MTreeMetaNode::pickle(unsigned char *buf)
{
    MTreeNode::pickle(buf);

    unsigned int tmp;
    size_t i;
    
    tmp = WordToMachine(size_);
    memcpy(buf + 4,(unsigned char *)&tmp,4);
    for(i = 0; i < 4; i++) {
	tmp = WordToMachine(filename_[i]);
	memcpy(buf + 8 + i * 4,(unsigned char *)&tmp,4);
    }
    memcpy(buf + 24,special,8);
}

//
// MTreeMetaNode::unpickle()
//
void
MTreeMetaNode::unpickle(unsigned char *buf)
{
    MTreeNode::unpickle(buf);
    size_t i;
    size_ = WordToHost(*((unsigned int *)(buf + 4)));
    for(i = 0; i < 4; i++) {
	filename_[i] = WordToHost(*((unsigned int *)(buf + 8 + i * 4)));
    }
    memcpy(special,buf + 24,8);
}

//
// MTreeDataNode::pickle()
//
void
MTreeDataNode::pickle(unsigned char *buf)
{
    MTreeNode::pickle(buf);

    unsigned int tmp;
    size_t i;
    
    tmp = WordToMachine(len_);
    memcpy(buf + 4,(unsigned char *)&tmp,4);
    for(i = 0; i < 6; i++) {
	tmp = WordToMachine(data_[i]);
	memcpy(buf + 8 + i * 4,(unsigned char *)&tmp,4);
    }
    memcpy(buf + 28,special,3);
}

//
// MTreeDataNode::unpickle()
//
void
MTreeDataNode::unpickle(unsigned char *buf)
{
    MTreeNode::unpickle(buf);
    size_t i;
    len_ = WordToHost(*((unsigned int *)(buf + 4)));
    for(i = 0; i < 6; i++) {
	data_[i] = WordToHost(*((unsigned int *)(buf + 8 + i * 4)));
    }
    memcpy(special,buf + 28, 3);
}
