// File:         $Id: etree.h,v 3.8 2001/11/14 19:04:01 trc2876 Exp $
// Author:       Trevor Clarke
// Contributors: Eric Eells, Eric Ferguson, Dan Westgate
// Description:  free extent tree for the filesystem
// Revisions:
//               $Log: etree.h,v $
//               Revision 3.8  2001/11/14 19:04:01  trc2876
//               almost fixed problem with extent allocation
//               can rm now
//
//               Revision 3.7  2001/11/12 23:33:29  trc2876
//               compiles...not debugged
//
//               Revision 3.6  2001/11/10 00:43:46  trc2876
//               *** empty log message ***
//
//               Revision 3.5  2001/11/10 00:41:01  trc2876
//               added free extents initialization to filesys constructor
//               added 1 level deep file add
//
//               Revision 3.4  2001/11/09 00:49:47  trc2876
//               added ETree::remove
//               added MTree::child() mutator
//
//               Revision 3.3  2001/11/08 23:25:29  trc2876
//               added insert to etree and fixed linker error
//
//               Revision 3.2  2001/11/08 18:42:16  trc2876
//               added mtree class and some stubs to filesystem
//               compiles
//               not tested
//               still needs some work (search for todo's)
//
//               Revision 3.1  2001/11/08 01:39:19  trc2876
//               added etree.h
//
//

#ifndef ETREE_H_
#define ETREE_H_

#include <stdlib.h>
#include "machine.h"
#include "mtree.h"

class ETreeNode : public MTreeRawNode
{
public:
    ETreeNode(T_WORD s, T_WORD l) : MTreeRawNode(0) {
	    memset(data,0,sizeof(data));
	    start(s);
	    len(l);
	}
    
    T_WORD left() { return WordToHost(*((T_WORD *)((data)))); }
    T_WORD right() { return WordToHost(*((T_WORD *)((data+4)))); }
    T_WORD start() { return WordToHost(*((T_WORD *)((data+8)))); }
    T_WORD len() { return WordToHost(*((T_WORD *)((data+12)))); }
    void left(T_WORD v) {
	    T_WORD tmp(WordToMachine(v));
	    memcpy(data,(T_BYTE *)&tmp,sizeof(T_WORD));
	}
    void right(T_WORD v) {
	    T_WORD tmp(WordToMachine(v));
	    memcpy(data+4,(T_BYTE *)&tmp,sizeof(T_WORD));
	}
    void start(T_WORD v) {
	    T_WORD tmp(WordToMachine(v));
	    memcpy(data+8,(T_BYTE *)&tmp,sizeof(T_WORD));
	}
    void len(T_WORD v) {
	    T_WORD tmp(WordToMachine(v));
	    memcpy(data+12,(T_BYTE *)&tmp,sizeof(T_WORD));
	}
}; // ETreeNode

class ETree
{
public:
    ETree(T_WORD start, T_WORD len, T_WORD id);
    ETree(T_WORD id);

    T_WORD alloc(T_WORD start, T_WORD len);
    void free(T_WORD id, T_WORD len);

    void printTree(T_WORD id,int Level);

protected:
    bool search(T_WORD start, T_WORD len, ETreeNode *node, T_WORD *matchID, T_WORD *closestID);
    void insert(ETreeNode *node, ETreeNode *root);
    void remove(ETreeNode *node, ETreeNode *root);
    ETreeNode *getNode(T_WORD id);
    void putNode(ETreeNode *node);
    void releaseNode(ETreeNode *node);

private:
    T_WORD root_;
};

#endif
