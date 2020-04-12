// File:         $Id: etree.cc,v 3.11 2001/11/14 20:35:51 trc2876 Exp $
// Author:       Trevor Clarke
// Contributors: Eric Eells, Eric Ferguson, Dan Westgate
// Description:  free extent tree for filesystem
// Revisions:
//               $Log: etree.cc,v $
//               Revision 3.11  2001/11/14 20:35:51  trc2876
//               fixed touch a;rm a;touch b problem
//
//               Revision 3.10  2001/11/14 20:29:29  trc2876
//               *** empty log message ***
//
//               Revision 3.9  2001/11/14 19:04:01  trc2876
//               almost fixed problem with extent allocation
//               can rm now
//
//               Revision 3.8  2001/11/14 02:40:45  trc2876
//               ETree::free problem fixed
//
//               Revision 3.7  2001/11/13 17:42:52  trc2876
//               still locking problems....but data looks good
//
//               Revision 3.6  2001/11/13 16:19:02  trc2876
//               can copy one file now
//
//               Revision 3.5  2001/11/13 00:33:13  trc2876
//               core dumps on a new
//
//               Revision 3.4  2001/11/12 23:33:29  trc2876
//               compiles...not debugged
//
//               Revision 3.3  2001/11/09 00:49:47  trc2876
//               added ETree::remove
//               added MTree::child() mutator
//
//               Revision 3.2  2001/11/08 23:25:28  trc2876
//               added insert to etree and fixed linker error
//
//               Revision 3.1  2001/11/08 21:17:15  trc2876
//               added etree.cc
//
//

#include <stdlib.h>
#include "etree.h"
#include "system.h"
#include "utility.h"

ETree::ETree(T_WORD start, T_WORD len, T_WORD id) : root_(id)
{
    ETreeNode *node(new ETreeNode(start, len));
    node->id(id);
    putNode(node);
}

ETree::ETree(T_WORD id) : root_(id)
{
}

T_WORD
ETree::alloc(T_WORD start, T_WORD len)
{
    T_WORD match(0x0000), closest(0x0000),ret(0);
    ETreeNode *node(NULL);

    if(search(start, len, getNode(root_), &match, &closest)) {
	node = getNode(match);
    } else {
	node = getNode(closest);
    }
    if(node == NULL) {
	DEBUG('E',"ETree::alloc: Can't find an extent of length 0x%04x\n",len);
	return 0x0000;
    }
    if(node->len() < len) {
	releaseNode(node);
	return 0x0000;
    }
    DEBUG('E',"Allocating extent 0x%04x/0x%04x for size 0x%04x\n",node->start(),node->len(),len);
    
    ret = node->start();
    if(node->len() > len) {
	node->start(ret + len);
	node->len(node->len() - len);
	DEBUG('E',"New free extent 0x%04x/0x%04x\n",node->start(),node->len());
	putNode(node);
    } else {
	remove(node,getNode(root_));
    }

    releaseNode(node);
    return ret;
}

void
ETree::free(T_WORD id, T_WORD len)
{
    T_WORD match(0x0000), closest(0x0000);
    ETreeNode *node(NULL);

    if(search(id + len, 1, getNode(root_), &match, &closest)) {
	node = getNode(match);
	node->start(id);
	node->len(node->len() + len);
    } else {
	node = new ETreeNode(id,len);
	node->id(alloc(0,1));
	if(node->id() == 0x0000)
	    return; // we have problems folks
	insert(node,getNode(root_));
    }
    putNode(node);
    releaseNode(node);
}

void
ETree::printTree(T_WORD id,int Level)
{
    if(id == 0x0000)
	id = root_;
    ETreeNode *node((ETreeNode *)getNode(id));
    if(node == NULL)
	return;
    int i;
    for(i = 0; i < Level; i++)
	DEBUG('u'," ");
    DEBUG('u',"0x%04x: EXTENT 0x%04x/0x%04x 0x%04x 0x%04x\n",id,node->start(),node->len(),node->left(),node->right());
    T_WORD lcid(node->left()),rcid(node->right());
    releaseNode(node);
    if(lcid != 0x0000)
	printTree(lcid,Level + 1);
    else {
	for(i = 0; i < Level+1; i++)
	    DEBUG('u'," ");
	DEBUG('u',"NO CHILD\n");
    }
    if(rcid != 0x0000)
	printTree(rcid,Level + 1);
    else {
	for(i = 0; i < Level+1; i++)
	    DEBUG('u'," ");
	DEBUG('u',"NO CHILD\n");
    }
}

bool
ETree::search(T_WORD start, T_WORD len, ETreeNode *node, T_WORD *matchID, T_WORD *closestID)
{
    if(node == NULL)		// if there is no current node, return with no match
	return false;

    ETreeNode *closest(NULL);

    // if we are looking for a start and have found it and it is of
    // sufficient length, set a match and return a success
    //
    if(start != 0x0000 && node->start() == start && node->len() >= len) {
	*matchID = node->id();
	releaseNode(node);
	return true;
    } else if(node->len() == len) {
	*matchID = node->id();
	releaseNode(node);
	return true;
    }
    
    if(*closestID != 0x0000)	// if there is a closest match found, grab it
	closest = getNode(*closestID);

    // if the current node has the length we need
    // or there is no closest match and the current is usable
    // or the current is a better match then the closest
    // then set the closest match
    //
    if((closest == NULL || closest->len() != len) || (
       (node->len() == len) ||
	   (closest == NULL && node->len() > len) ||
	   (closest != NULL &&
	       (((len * 2) - node->len()) < ((len * 2) - closest->len()))))) {
	*closestID = node->id();
    }

    // if we have not found a match then search the children
    //
    if(*matchID == 0x0000 && len < node->len()) {
	if(node->left() != 0x0000) {
	    ETreeNode *child(getNode(node->left()));
	    if(search(start, len, child, matchID, closestID)) {
		releaseNode(child);
		releaseNode(node);
		return true;
	    }
	    releaseNode(child);
	}
    }
    if(*matchID == 0x0000 && len > node->len()) {
	if(node->right() != 0x0000) {
	    ETreeNode *child(getNode(node->right()));
	    if(search(start, len, child, matchID, closestID)) {
		releaseNode(child);
		releaseNode(node);
		return true;
	    }
	    releaseNode(child);
	}
    }
    if(node != NULL)
	releaseNode(node);
    return false;
}

void
ETree::insert(ETreeNode *node, ETreeNode *root)
{
    if(node == NULL)
	return;
    if(node->len() < root->len()) { // insert to left
	if(root->left() == 0x0000) {
	    root->left(node->id());
	    putNode(root);
	    releaseNode(root);
	} else {
	    ETreeNode *child(getNode(root->left()));
	    releaseNode(root);
	    insert(node, child);
	}
    } else { // insert to right
	if(root->right() == 0x0000) {
	    root->right(node->id());
	    putNode(node);
	} else {
	    ETreeNode *child(getNode(root->left()));
	    releaseNode(root);
	    insert(node, child);
	}
    }
}

void
ETree::remove(ETreeNode *node, ETreeNode *root)
{
    if(node == NULL)
	return;

    if(node->len() < root->len()) { // remove from left
	if(root->left() != 0x0000) {
	    if(root->left() == node->id()) { // found it
		root->left(0x0000);
		putNode(root);
		releaseNode(root);
		if(node->left() != 0x0000) {
		    ETreeNode *subChild(getNode(node->left()));
		    root = getNode(root_);
		    insert(subChild,root);
		    putNode(subChild);
		    releaseNode(subChild);
		    releaseNode(root);
		}
		if(node->right() != 0x0000) {
		    ETreeNode *subChild(getNode(node->right()));
		    root = getNode(root_);
		    insert(subChild,root);
		    putNode(subChild);
		    releaseNode(subChild);
		    releaseNode(root);
		}
	    } else {
		ETreeNode *child(getNode(root->left()));
		releaseNode(root);
		remove(node, child);
		releaseNode(child);
	    }
	}
    } else { // remove from right
	if(root->right() != 0x0000) {
	    if(root->right() == node->id()) { // found it
		root->right(0x0000);
		putNode(root);
		releaseNode(root);
		if(node->left() != 0x0000) {
		    ETreeNode *subChild(getNode(node->left()));
		    root = getNode(root_);
		    insert(subChild,root);
		    putNode(subChild);
		    releaseNode(subChild);
		    releaseNode(root);
		}
		if(node->right() != 0x0000) {
		    ETreeNode *subChild(getNode(node->right()));
		    root = getNode(root_);
		    insert(subChild,root);
		    putNode(subChild);
		    releaseNode(subChild);
		    releaseNode(root);
		}
	    } else {
		ETreeNode *child(getNode(root->right()));
		releaseNode(root);
		remove(node, child);
		releaseNode(child);
	    }
	}
    }
}

ETreeNode *
ETree::getNode(T_WORD id)
{
    return (ETreeNode *)(fileSystem->getNode(id));
}

void
ETree::putNode(ETreeNode *node)
{
    fileSystem->putNode((MTreeNode *)node);
}

void
ETree::releaseNode(ETreeNode *node)
{
    fileSystem->releaseNode((MTreeNode *)node);
}
