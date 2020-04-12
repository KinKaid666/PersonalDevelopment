// File:         $Id: bintree.h,v 3.0 2001/11/04 19:47:07 trc2876 Exp $
// Author:       Trevor Clarke
// Contributors: Eric Eells, Eric Ferguson, Dan Westgate
// Description:  simple binary tree
// Revisions:
//               $Log: bintree.h,v $
//               Revision 3.0  2001/11/04 19:47:07  trc2876
//               force to 3.0
//
//               Revision 1.5  2001/10/31 03:29:15  trc2876
//               bigarray sorta works now
//
//               Revision 1.4  2001/10/30 21:20:05  trc2876
//               merge
//
//               Revision 1.3  2001/10/29 00:02:12  trc2876
//               added walk function to BinTree
//
//               Revision 1.2  2001/10/28 23:35:52  trc2876
//               after merge
//
//               Revision 1.1.2.1  2001/10/28 22:32:42  trc2876
//               added BinTree class
//
//

#ifndef BINTREE_H_
#define BINTREE_H_

class BinTree;

class BinTreeNode
{
public:
    enum walk_t  {PREORDER,INORDER,POSTORDER};

protected:
    BinTreeNode(int key, void *data);
    ~BinTreeNode();

    bool walk(walk_t type, bool (*func)(void *,void *),void *arg);
    void printIt(walk_t type,bool (*func)(void *,int,bool),int lvl);

    int key_;
    void *data_;
    BinTreeNode *parent_;
    BinTreeNode *left_;
    BinTreeNode *right_;

    friend BinTree;
};

class BinTree
{
public:
    enum match_t {LT,LTE,EQ,GTE,GT};

public:
    BinTree();
    ~BinTree();

    void insert(int key, void *data);
    void *remove(int key, match_t match=EQ);
    void *search(int key, match_t match=EQ);
    bool walk(BinTreeNode::walk_t type, bool (*func)(void *,void *),void *arg);
    void printIt(BinTreeNode::walk_t type, bool (*func)(void *,int,bool));

protected:
    void reinsert(BinTreeNode *node);
    BinTreeNode *findNode(int key, match_t match);

private:
    BinTreeNode *root_;
};

#endif
