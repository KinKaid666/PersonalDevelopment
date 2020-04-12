// File:         $Id: bintree.cc,v 3.0 2001/11/04 19:47:07 trc2876 Exp $
// Author:       Trevor Clarke
// Contributors: Eric Eells, Eric Ferguson, Dan Westgate
// Description:  simple binary tree
// Revisions:
//               $Log: bintree.cc,v $
//               Revision 3.0  2001/11/04 19:47:07  trc2876
//               force to 3.0
//
//               Revision 1.8  2001/10/31 17:09:58  trc2876
//               *** empty log message ***
//
//               Revision 1.7  2001/10/31 03:29:15  trc2876
//               bigarray sorta works now
//
//               Revision 1.6  2001/10/30 21:20:04  trc2876
//               merge
//
//               Revision 1.5  2001/10/30 00:49:09  trc2876
//               Fixed extent packing
//
//               Revision 1.4  2001/10/29 23:57:44  trc2876
//               packing extents is done
//               extent system is still buggy
//
//               Revision 1.3  2001/10/29 00:02:11  trc2876
//               added walk function to BinTree
//
//               Revision 1.2  2001/10/28 23:35:52  trc2876
//               after merge
//
//               Revision 1.1.2.2  2001/10/28 23:20:57  trc2876
//               swapfile almost rewritten
//               need to write defragmenter
//               ready to merge
//
//               Revision 1.1.2.1  2001/10/28 22:32:42  trc2876
//               added BinTree class
//
//

#include <stdlib.h>
#include "bintree.h"
#include "system.h"

BinTreeNode::BinTreeNode(int key, void *data) :
    key_(key),
    data_(data),
    parent_(NULL),
    left_(NULL),
    right_(NULL)
{
}

BinTreeNode::~BinTreeNode()
{
    if(this == NULL)
	return;
    if(left_)
	delete left_;
    if(right_)
	delete right_;
}

bool
BinTreeNode::walk(walk_t type, bool (*func)(void *,void *),void *arg)
{
    switch(type) {
	case PREORDER:
	    if(func(data_,arg))
		return true;
	    if(left_ != NULL)
		if(left_->walk(type,func,arg))
		    return true;
	    if(right_ != NULL)
		if(right_->walk(type,func,arg))
		    return true;
	    break;
	case INORDER:
	    if(left_ != NULL)
		if(left_->walk(type,func,arg))
		    return true;
	    if(func(data_,arg))
		return true;
	    if(right_ != NULL)
		if(right_->walk(type,func,arg))
		    return true;
	    break;
	case POSTORDER:
	    if(left_ != NULL)
		if(left_->walk(type,func,arg))
		    return true;
	    if(right_ != NULL)
		if(right_->walk(type,func,arg))
		    return true;
	    if(func(data_,arg))
		return true;
	    break;
    }
    return false;
}

void
BinTreeNode::printIt(walk_t type, bool (*func)(void *,int,bool), int lvl)
{
    switch(type) {
	case PREORDER:
	    func(data_,lvl,false);
	    if(left_ == NULL) {
		func(NULL,lvl+1,true);
	    } else {
		left_->printIt(type,func,lvl+1);
	    }
	    if(right_ == NULL) {
		func(NULL,lvl+1,true);
	    } else {
		right_->printIt(type,func,lvl+1);
	    }
	    break;
	case INORDER:
	    if(left_ == NULL) {
		func(NULL,lvl+1,true);
	    } else {
		left_->printIt(type,func,lvl+1);
	    }
	    func(data_,lvl,false);
	    if(right_ == NULL) {
		func(NULL,lvl+1,true);
	    } else {
		right_->printIt(type,func,lvl+1);
	    }
	    break;
	case POSTORDER:
	    if(left_ == NULL) {
		func(NULL,lvl+1,true);
	    } else {
		left_->printIt(type,func,lvl+1);
	    }
	    if(right_ == NULL) {
		func(NULL,lvl+1,true);
	    } else {
		right_->printIt(type,func,lvl+1);
	    }
	    func(data_,lvl,false);
	    break;
    }
}

BinTree::BinTree() :
    root_(NULL)
{
}

BinTree::~BinTree()
{
    if(root_)
	delete root_;
}

void
BinTree::insert(int key, void *data)
{
    BinTreeNode *newNode(new BinTreeNode(key,data));

    if(root_ == NULL) {
	DEBUG('B',"Root is null, setting to 0x%04x\n",newNode);
	root_ = newNode;
    } else {
	BinTreeNode *node(root_);
	for(;;) {
	    if(node->key_ <= key) {
		if(node->right_ == NULL) {
		    node->right_ = newNode;
		    newNode->parent_ = node;
		    DEBUG('B',"BTree[i1] - p: 0x%x r: 0x%x l: 0x%x\n",node->parent_,node->right_,node->left_);
		    break;
		} else {
		    node = node->right_;
		}
	    } else {
		if(node->left_ == NULL) {
		    node->left_ = newNode;
		    newNode->parent_ = node;
		    DEBUG('B',"BTree[i2] - p: 0x%x r: 0x%x l: 0x%x\n",node->parent_,node->right_,node->left_);
		    break;
		} else {
		    node = node->left_;
		}
	    }
	} // for(;;)
    }
}

void
BinTree::reinsert(BinTreeNode *node)
{
    insert(node->key_,node->data_);
    if(node->left_ != NULL)
	reinsert(node->left_);
    if(node->right_ != NULL)
	reinsert(node->right_);
}

void *
BinTree::remove(int key, match_t match=EQ)
{
    BinTreeNode *node(findNode(key,match)),*parent(NULL);
    if(node == NULL)
	return NULL;
    void *ret = node->data_;
    if(node == root_) {
	DEBUG('B',"Removing root\n");
	root_ = NULL;
    } else {
	parent = node->parent_;
	if(parent && parent->left_ == node)
	    parent->left_ = NULL;
	else if(parent && parent->right_ == node)
	    parent->right_ = NULL;
    }
    if(node->left_ != NULL)
	reinsert(node->left_);
    if(node->right_ != NULL)
	reinsert(node->right_);
    delete node;

    return ret;
}

void *
BinTree::search(int key, match_t match=EQ)
{
    BinTreeNode *node(findNode(key,match));
    if(node == NULL)
	return NULL;
    return node->data_;
}

BinTreeNode *
BinTree::findNode(int key, match_t match)
{
    BinTreeNode *node(root_);

    switch(match) {
	case LT:
	    while(node != NULL) {
		if(node->key_ < key &&
		          ((node->right_ == NULL) ||
		           (node->right_->key_ >= key))) {
		    // if the current is the largest key that is
		    // stictly < the requested key then return it
		    break;
		} else if (node->key_ < key) {
		    // if the current key is strictly < the
		    // requested key, go to the next largest key
		    node = node->right_;
		} else {
		    // if the current key is >= the requested
		    // key, go to the next smallest key
		    node = node->left_;
		}
	    }
	    break;
	case LTE:
	    while(node != NULL) {
		if(node->key_ <= key &&
		          ((node->right_ == NULL) ||
		           (node->right_->key_ > key))) {
		    // if the current is the largest key that is
		    // <= the requested key then return it
		    break;
		} else if (node->key_ < key) {
		    // if the current key is strictly < the
		    // requested key, go to the next largest key
		    node = node->right_;
		} else {
		    // if the current key is strictly > the requested
		    // key, go to the next smallest key
		    node = node->left_;
		}
	    }
	    break;
	case EQ:
	    while(node != NULL) {
		if(node->key_ < key) {
		    // if the current key is strictly < the
		    // requested key, go to the next larger key
		    node = node->right_;
		} else if(node->key_ > key) {
		    // if the current key is strictly > the
		    // requested key, go to the next smaller key
		    node = node->left_;
		} else  {
		    // if the current key is == the
		    // requested key, return it
		    break;
		}
	    }
	    break;
	case GTE:
	    while(node != NULL) {
		if(node->key_ >= key &&
		          ((node->left_ == NULL) ||
		           (node->left_->key_ < key))) {
		    // if the current is the smallest key that is
		    // >= the requested key then return it
		    break;
		} else if (node->key_ > key) {
		    // if the current key is strictly > the
		    // requested key, go to the next smallest key
		    node = node->left_;
		} else {
		    // if the current key is strictly < the requested
		    // key, go to the next largest key
		    node = node->right_;
		}
	    }
	    break;
	case GT:
	    while(node != NULL) {
		if(node->key_ > key &&
		          ((node->left_ == NULL) ||
		           (node->left_->key_ <= key))) {
		    // if the current is the smallest key that is
		    // stictly > the requested key then return it
		    break;
		} else if (node->key_ > key) {
		    // if the current key is strictly > the
		    // requested key, go to the next smallest key
		    node = node->left_;
		} else {
		    // if the current key is <= the requested
		    // key, go to the next largest key
		    node = node->right_;
		}
	    }
	    break;
    }
    return node;
}

bool
BinTree::walk(BinTreeNode::walk_t type, bool (*func)(void *,void *),void *arg)
{
    if(root_ != NULL)
	return root_->walk(type,func,arg);
    return false;
}

void
BinTree::printIt(BinTreeNode::walk_t type,bool (*func)(void *,int,bool))
{
    root_->printIt(type,func,0);
}
