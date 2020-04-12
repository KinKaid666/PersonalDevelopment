// File:         $Id: mtree.h,v 3.23 2001/11/15 01:44:43 trc2876 Exp $
// Author:       Trevor Clarke, Eric Eells, Eric Ferguson, Dan Westgate
// Contributors: {}
// Description:  Metadata tree
// Revisions:
//		 $Log: mtree.h,v $
//		 Revision 3.23  2001/11/15 01:44:43  trc2876
//		 *** empty log message ***
//
//		 Revision 3.22  2001/11/13 17:09:55  trc2876
//		 debugging functionality added to filesys.cc
//		
//		 Revision 3.21  2001/11/13 04:21:09  trc2876
//		 still broken
//		
//		 Revision 3.20  2001/11/13 03:13:11  trc2876
//		 AHHHHHH
//		
//		 Revision 3.19  2001/11/13 02:46:57  trc2876
//		 different dump
//		
//		 Revision 3.18  2001/11/13 00:33:13  trc2876
//		 core dumps on a new
//		
//		 Revision 3.17  2001/11/12 01:45:10  trc2876
//		 broken coke...but it compiles
//		
//		 Revision 3.16  2001/11/11 19:58:49  trc2876
//		 added FileSystem formatting code
//		
//		 Revision 3.15  2001/11/11 18:32:44  trc2876
//		 Fixed compile errors
//		 some have been // out because they don't fit the layout of the class and I'm not
//		     sure what is correct....they have been preceded by a // todo: fix this
//		
//		 MTreeNode->{dirty_,inUse_} have mutators and accessors and refs to them outside
//		     MTreeNode have been changed
//		
//		 Revision 3.14  2001/11/10 00:24:03  trc2876
//		 added len_ to MTreeDataNode
//		
//		 Revision 3.13  2001/11/09 00:49:47  trc2876
//		 added ETree::remove
//		 added MTree::child() mutator
//		
//		 Revision 3.12  2001/11/09 00:24:21  trc2876
//		 fixed compile warning and added children_ mutator
//		
//		 Revision 3.11  2001/11/09 00:18:28  trc2876
//		 removed lock stuff from disk
//		
//		 Revision 3.10  2001/11/09 00:12:09  trc2876
//		 added pickle/unpickle funcs
//		
//		 Revision 3.9  2001/11/08 23:44:34  eae8264
//		 Changed some things in NodeCache... other things
//		 had to change as well.
//		
//		 Revision 3.8  2001/11/08 22:40:48  trc2876
//		 added dirty bit
//		
//		 Revision 3.7  2001/11/08 22:12:41  eae8264
//		 Node Cache mostly implemented and added.
//		
//		 Revision 3.6  2001/11/08 18:29:58  eae8264
//		 small things...
//		
//		 Revision 3.5  2001/11/08 00:38:02  eae8264
//		 Removed virtual.
//		
//		 Revision 3.4  2001/11/08 00:04:09  trc2876
//		 added stubs
//		
//		 Revision 3.3  2001/11/06 00:24:22  trc2876
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

#ifndef MTREE_H_
#define MTREE_H_

#include <stdlib.h>
#include "utility.h"

#define NODE_SIZE 32

// Zero part of the header based on size and position
//
#define ZERO_HEADER(pos__,size__,header__) \
	header_ &= (((1 << size__) - 1) << pos__) ^ 0xff

// Pack information into the header based on size and position
// position and size are in bits
//
#define PACK_HEADER(pos__,size__,data__,header__) \
	header__ |= (data__ & ((1 << size__) - 1)) << pos__

// Unpack information from the header
//
#define UNPACK_HEADER(pos__,size__,header__) \
	((header__ >> pos__) & ((1 << size__) - 1))


// useful type definitions
//
typedef unsigned char T_BYTE; // 1 byte
typedef unsigned int  T_WORD; // 4 bytes

//
// Name:        class MTreeNode
//
// Description: superclass for nodes
//
class MTreeNode 
{
public:
    //
    // Name:        node_t
    //
    // Description: enum describing the type of a node
    //
    enum node_t { RAW      = 0x00,
		  INTERNAL = 0x01,
		  META     = 0x02,
		  DATA     = 0x03 };

public:
    //
    // Name:        (constructor)
    //
    // Arguments:   t:    the type of node to create
    //              nid:  the ID number for this node
    //
    MTreeNode(node_t t, T_WORD nid);

    //
    // Name:        (destructor)
    //
    virtual ~MTreeNode() { DEBUG('M',"Destruct MTreeNode 0x%04x\n",id_); }

    //
    // Name:        type()
    //
    // Description: accessor for the type part of the header_
    //
    // Returns:     the type of this node
    //
    node_t type();

    //
    // Name:        id()
    //
    // Description: accessor for the id_ variable
    //
    // Returns:     the ID number of this node
    //
    T_WORD id() { return id_; }

    //
    // Name:        id()
    //
    // Description: mutator for the id_ variable
    //
    // Arguments:   nid: the new ID number for this node
    //
    void id(T_WORD nid) { id_ = nid; }

    //
    // Name:        pickle()
    //
    // Description: put data into a char * for writing to disk
    //
    // Arguments:   buf: a preallocated 32byte array
    //
    virtual void pickle(unsigned char *buf);

    //
    // Name:        unpickle()
    //
    // Description: pull the data from a char *
    //
    // Arguments:   buf: data to unpickle
    //
    virtual void unpickle(unsigned char *buf);

    //
    // Name:        inUse()
    //
    // Description: accessor for inUse_
    //
    // Returns:     value of inUse_
    //
    bool inUse() { return inUse_; }

    //
    // Name:        inUse()
    //
    // Description: mutator for inUse_
    //
    // Arguments:   v: new value
    //
    void inUse(bool v) { inUse_ = v; }

    //
    // Name:        dirty()
    //
    // Description: accessor for dirty_
    //
    // Returns:     value of dirty_
    //
    bool dirty() { return dirty_; }

    //
    // Name:        dirty()
    //
    // Description: mutator for dirty_
    //
    // Arguments:   v: new value
    //
    void dirty(bool v) { dirty_ = v; }

protected:
    T_WORD id_;     // id of the node, used to find position on disk
    T_BYTE header_; // Type : Padding
    bool inUse_ ; // number of objects in system are using this node
    bool dirty_ ; // should we write to disk?
}; // class MTreeNode

class MTreeRawNode : public MTreeNode
{
public:
    //
    // Name:        (constructor)
    //
    // Arguments:   nid: ID number for this node
    //
    MTreeRawNode(T_WORD nid) : MTreeNode(MTreeNode::RAW,nid) {}

    //
    // Name:        pickle()
    //
    // Description: put data into a char * for writing to disk
    //
    // Arguments:   buf: a preallocated 32byte array
    //
    void pickle(unsigned char *buf);

    //
    // Name:        unpickle()
    //
    // Description: pull the data from a char *
    //
    // Arguments:   buf: data to unpickle
    //
    void unpickle(unsigned char *buf);

    T_BYTE data[31]; // raw data
}; // class MTreeRawNode

class MTreeInternalNode : public MTreeNode
{
public:
    //
    // Name:        (constructor)
    //
    // Arguments:   nid: ID number for this node
    //
    MTreeInternalNode(T_WORD nid) : MTreeNode(MTreeNode::INTERNAL,nid) {
	    children_[0] = children_[1] = children_[2] = children_[3] = 0x0000;
	}

    //
    // Name:        child()
    //
    // Description: accessor for the children_ of this node
    //
    // Arguments:   no: the index of the child
    //
    // Returns:     the ID of the requested child node
    //              0x0000 is returned when there is no child
    //              in the requested position or the requested
    //              position is invalid
    //
    T_WORD child(size_t no);

    //
    // Name:        child()
    //
    // Description: mutator for the children_ of this node
    //
    // Arguments:   no: the index of the child
    //              val: the new value
    //
    void child(size_t no, T_WORD val);

    //
    // Name:        nchildren()
    //
    // Description: how many children are there?
    //
    // Returns:     the number of children
    //
    size_t nchildren();

    //
    // Name:        pickle()
    //
    // Description: put data into a char * for writing to disk
    //
    // Arguments:   buf: a preallocated 32byte array
    //
    void pickle(unsigned char *buf);

    //
    // Name:        unpickle()
    //
    // Description: pull the data from a char *
    //
    // Arguments:   buf: data to unpickle
    //
    void unpickle(unsigned char *buf);

private:
    T_WORD children_[4]; // list of child nodes
			 // NOTE: this should be "packed"
			 //       all valid children
			 //       occur at the start of
			 //       the array...see
			 //       nchildren() for an example
			 //       as to why this is

public:
    T_BYTE special[15];  // special data
			 // this is public because
			 // it is unused by the default
			 // node def so it should NOT be
			 // validated by MTreeInternalNode
}; // class MTreeInternalNode

class MTreeMetaNode : public MTreeNode
{
public:
    //
    // Name:        (constructor)
    //
    // Arguments:   nid: ID number for this node
    //
    MTreeMetaNode(T_WORD nid) : MTreeNode(MTreeNode::META,nid) {
	    memset(filename_,0,4 * sizeof(T_WORD));
	}

    //
    // Name:        size()
    //
    // Description: accessor for the size_ variable
    //
    // Returns:     the size of this file
    //
    T_WORD size() { return size_; }

    //
    // Name:        size()
    //
    // Description: mutator for the size_ variable
    //
    // Arguments:   ns: the new size of this file
    //
    void size(T_WORD ns) { size_ = ns; }

    //
    // Name:        directory()
    //
    // Description: does this node represent a directory?
    //
    // Returns:     true/false
    //
    bool directory();

    //
    // Name:        directory()
    //
    // Description: should this node represent a directory?
    //
    // Arguments:   d: true/false
    //
    void directory(bool d);

    //
    // Name:        getmod()
    //
    // Description: set the permissions on this file
    //
    // Returns:     the permission number (like in POSIX)
    //
    T_BYTE getmod();

    //
    // Name:        chmod()
    //
    // Description: change the permissions on this file
    //
    // Arguments:   perms: the permission number (like in POSIX)
    //
    void chmod(T_BYTE perms);

    //
    // Name:        filenameNode()
    //
    // Description: set a filename node pointer
    //
    // Arguments:   num: index to change
    //              val: pointer
    //
    void filenameNode(T_WORD num, T_WORD val) { filename_[num] = val; }
    T_WORD filenameNode(T_WORD num) { return filename_[num]; }

    //
    // Name:        filename()
    //
    // Description: access the filename
    //              this function combines the filename
    //              it does not provide raw access to the filename nodes
    //
    // Returns:     NULL terminated filename
    //              this will be allocated with new and
    //              should be delete'd by the caller
    //
    char *filename();

    //
    // Name:        filename()
    //
    // Description: set the filename
    //              this function does not provide raw
    //              access to the filename nodes
    //
    // Arguments:   fn: NULL terminate filename string
    //
    // Returns:     the number of filename nodes added
    //              this will be a 0 if there was an error
    //
    size_t filename(const char *fn);

    //
    // Name:        pickle()
    //
    // Description: put data into a char * for writing to disk
    //
    // Arguments:   buf: a preallocated 32byte array
    //
    void pickle(unsigned char *buf);

    //
    // Name:        unpickle()
    //
    // Description: pull the data from a char *
    //
    // Arguments:   buf: data to unpickle
    //
    void unpickle(unsigned char *buf);

private:
    T_WORD size_;            // size of file in bytes
    T_WORD filename_[4];     // filename node pointers

public:
    T_BYTE special[8]; // special data
}; // class MTreeMetaNode

class MTreeDataNode : public MTreeNode
{
public:
    //
    // Name:        (constructor)
    //
    // Arguments:   nid: ID number for this node
    //
    MTreeDataNode(T_WORD nid) : MTreeNode(MTreeNode::DATA,nid), len_(0x0000) {
	    for(int i=0; i < 6; i++)
	    	data_[i] = 0x0000;
	    }

    //
    // Name:        data()
    //
    // Description: accessor for data_
    //
    // Arguments:   i: which data_ to get
    //
    // Returns:     value at index
    //
    T_WORD data(size_t i) { return (i < 6) ? data_[i] : 0x0000; }

    //
    // Name:        data()
    //
    // Description: mutator for data_
    //
    // Arguments:   i: which data_ to change
    //		    va: value to set
    //
    void data(size_t i, T_WORD val) { if(i < 6) data_[i] = val; }

    //
    // Name:        pickle()
    //
    // Description: put data into a char * for writing to disk
    //
    // Arguments:   buf: a preallocated 32byte array
    //
    void pickle(unsigned char *buf);

    //
    // Name:        unpickle()
    //
    // Description: pull the data from a char *
    //
    // Arguments:   buf: data to unpickle
    //
    void unpickle(unsigned char *buf);

    //
    // Name:        len()
    //
    // Description: accessor for len_
    //
    // Returns:     length of the data
    //
    T_WORD len() { return len_; };

    //
    // Name:        len()
    //
    // Description: mutator for len_
    //
    // Arguments:   v: new value for len_
    //
    void len(T_WORD v) { len_ = v; };

private:
    T_WORD len_;        // total length of data stored here
    T_WORD data_[6];    // pointers to data
			// these are extents -- data[0] is start data[1] is len

public:
    T_BYTE special[3]; // special data
}; // class MTreeDataNode

#endif // MTREE_H_
