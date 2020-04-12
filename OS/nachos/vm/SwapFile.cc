// File:         $Id: SwapFile.cc,v 3.0 2001/11/04 19:47:07 trc2876 Exp $
// Author:       
// Contributors: 
// Description:	The implementation file for SwapFile
// Revisions:
//               $Log: SwapFile.cc,v $
//               Revision 3.0  2001/11/04 19:47:07  trc2876
//               force to 3.0
//
//               Revision 1.32  2001/10/31 20:38:23  trc2876
//               forget it, TOP_OF_ADDRSPACE is 2^31-1 so we are fine w/ ints
//
//               Revision 1.31  2001/10/31 20:33:11  trc2876
//               changed to unsigned int for addresses
//
//               Revision 1.30  2001/10/31 03:29:15  trc2876
//               bigarray sorta works now
//
//               Revision 1.29  2001/10/31 00:48:03  trc2876
//               ok...forget the last commit....that's stupid
//               it's better to have an unbalanced extent tree than it is to fragment memory
//
//               Revision 1.27  2001/10/30 22:28:51  trc2876
//               unused free extent was not added back into the tree
//
//               Revision 1.26  2001/10/30 21:20:04  trc2876
//               merge
//
//               Revision 1.25  2001/10/30 21:08:53  trc2876
//               added readOnly_ check
//
//               Revision 1.24  2001/10/30 00:49:09  trc2876
//               Fixed extent packing
//
//               Revision 1.23  2001/10/29 23:57:43  trc2876
//               packing extents is done
//               extent system is still buggy
//
//               Revision 1.22  2001/10/28 23:35:52  trc2876
//               after merge
//
//               Revision 1.18.2.2  2001/10/28 23:20:57  trc2876
//               swapfile almost rewritten
//               need to write defragmenter
//               ready to merge
//
//               Revision 1.18.2.1  2001/10/28 21:10:30  trc2876
//               cleaned out dan code....this branch is a rewrite of SwapFile
//
//               Revision 1.18  2001/10/28 17:32:36  eae8264
//               Added some info to a debug statement.
//
//               Revision 1.17  2001/10/28 17:25:29  eae8264
//               Changed debug letter to 'z' since it conflicted
//               with console as 's'
//
//               Revision 1.16  2001/10/26 22:51:23  etf2954
//               Added better debug info
//
//               Revision 1.15  2001/10/26 22:50:45  trc2876
//               insidious bug #1 == deadbeef
//
//               Revision 1.14  2001/10/26 17:22:28  p544-01b
//               Added Comments
//
//               Revision 1.13  2001/10/26 00:52:47  trc2876
//               added \n to DEBUG statements
//
//               Revision 1.12  2001/10/25 21:17:31  p544-01b
//               Changed Debug
//
//               Revision 1.11  2001/10/25 21:08:34  trc2876
//               fixed page/byte error in deallocSegment
//
//               Revision 1.10  2001/10/25 20:51:18  p544-01b
//               Check for null Bitmap
//
//               Revision 1.9  2001/10/25 20:14:31  eae8264
//               Added coimments... fixed some bugs...
//
//               Revision 1.8  2001/10/25 19:37:35  eae8264
//               Fixed a soon-to-be bug in swapIn and swapOut
//
//               Revision 1.7  2001/10/25 01:03:47  etf2954
//               Code review, everythink okay
//
//               Revision 1.6  2001/10/23 23:33:48  p544-01b
//               Wrote Functions
//
//               Revision 1.5  2001/10/23 20:38:57  eae8264
//               stubs...
//
//               Revision 1.4  2001/10/23 18:31:10  eae8264
//               Made signatures match header file.
//
//               Revision 1.3  2001/10/20 22:11:28  etf2954
//               Added some comments as to what each function does
//
//               Revision 1.2  2001/10/20 20:08:52  eae8264
//               fixed up style and comments./..
//
//


#include "SwapFile.h"
#include "system.h"

//
// Name:	(constructor)
//
SwapFile::SwapFile( OpenFile* file, bool readOnly ) :
    fileHandle_(file),
    size_(file->Length()),
    readOnly_(readOnly),
    freeExtents_(new BinTree),
    mutex_(new Lock("SwapFile mutex"))
{
    if(!readOnly_) {
	int szInPg = divRoundUp(size_,PageSize);
	Extent *e = new Extent(0,szInPg);
	freeExtents_->insert(szInPg,(void *)e);
    }
} // (constructor)

//
// Name:        (destructor)
//
SwapFile::~SwapFile()
{
    delete mutex_;
}

//
// Name:	swapIn
//
void SwapFile::swapIn( int fAddr, int pAddr )
{
    mutex_->Acquire();
    fileHandle_->ReadAt(machine->mainMemory + pAddr, PageSize, fAddr);
    mutex_->Release();
} // swapIn

//
// Name:	swapOut
//
void SwapFile::swapOut( int fAddr, int pAddr )
{
    if(!readOnly_) {
	mutex_->Acquire();
	fileHandle_->WriteAt(machine->mainMemory + pAddr, PageSize, fAddr);
	mutex_->Release();
    }
} // SwapOut

//
// Name:	allocSegment
//
int SwapFile::allocSegment( int sizeInBytes )
{
    if(readOnly_)
	return -1;

    DEBUG('F',"\nEntering allocSegment\n");
    freeExtents_->printIt(BinTreeNode::PREORDER,printTree);
    DEBUG('g',"\n");
    int szInPg = divRoundUp(sizeInBytes,PageSize);
    mutex_->Acquire();
    Extent *e;
    if((e = (Extent *)freeExtents_->remove(szInPg,BinTree::EQ)) == NULL) {
	if((e = (Extent *)freeExtents_->remove(szInPg * 2,BinTree::GTE))
		    == NULL) {
	    if((e = (Extent *)freeExtents_->remove(szInPg * 2,BinTree::LTE))
			== NULL) {
		mutex_->Release();
		DEBUG('F',"Can't allocate 0x%04x pages\n",szInPg);
		return -1;
	    }
	}
	if(e->len < szInPg) { // sanity check
	    freeExtents_->insert(e->len,(void *)e);
	    mutex_->Release();
	    DEBUG('F',"Extent not big enough for 0x%04x pages\n",szInPg);
	    return -1;
	}
	Extent *ne(new Extent(e->pos + szInPg,e->len - szInPg));
	DEBUG('F',"Allocating extent 0x%04x/0x%04x for size 0x%04x\n",e->pos,e->len,szInPg);
	DEBUG('F',"New extent 0x%04x/0x%04x\n",ne->pos,ne->len);
	freeExtents_->insert(ne->len,(void *)ne);
    }
    mutex_->Release();

    return (e->pos * PageSize);
} // allocSegment

//
// Name:	deallocSegment
//
void SwapFile::deallocSegment( int fAddr, int lengthInBytes )
{
    if(readOnly_)
	return;

    Extent *e;
    
    if((e = new Extent(fAddr / PageSize, divRoundUp(lengthInBytes, PageSize)))
		== NULL)
	return;
    DEBUG('F',"De-allocating extent 0x%04x/0x%04x\n",e->pos,e->len);
    mutex_->Acquire();
    freeExtents_->insert(e->len,(void *)e);
    packExtents();
    mutex_->Release();
} // deallocSegment

//
// Name:        packExtents
//
void SwapFile::packExtents()
{
    ExtentInfo nfo;
    ListElement *cur;
    BinTree *newTree = new BinTree;
    Extent *e1,*e2;

    freeExtents_->walk(BinTreeNode::PREORDER,extentInfo,(void *)&nfo);
    for(cur = nfo.fixExtents->getFirstItem(); cur->next != NULL; ) {
	e1 = (Extent *)cur->item;
	e2 = (Extent *)cur->next->item;
	DEBUG('F',"Extent 1: 0x%04x/0x%04x  0x%04x\n",e1->pos,e1->len,e1->pos+e1->len);
	DEBUG('F',"Extent 2: 0x%04x/0x%04x\n",e2->pos,e2->len);
	if((e1->pos + e1->len) == e2->pos) {
	    DEBUG('F',"Combining extents\n");
	    e1->len += e2->len;
	    ListElement *tmp(cur->next);
	    cur->next = cur->next->next;
	    delete tmp;
	    delete e2;
	} else {
	    newTree->insert(e1->len,(void *)e1);
	    cur = cur->next;
	}
    }

    e1 = (Extent *)cur->item;
    newTree->insert(e1->len,(void *)e1);

    int oldNum(nfo.numExtents);
    delete nfo.fixExtents;
    nfo.fixExtents = NULL;
    nfo.numExtents = 0;
    newTree->walk(BinTreeNode::PREORDER,extentInfo,(void *)&nfo);
    int newNum(nfo.numExtents);
    float fragLvl(100.0 * (1.0 - ((float)newNum / (float)oldNum)));

    DEBUG('f',"Extent fragmentation %.1f%% - Nums: %i/%i\n",fragLvl,newNum,oldNum);

    delete freeExtents_;
    freeExtents_ = newTree;
} // packExtents

//
// Name:	extentInfo
//
bool SwapFile::extentInfo(void *node,void *arg)
{
    Extent     *e   = (Extent     *)node;
    ExtentInfo *nfo = (ExtentInfo *)arg;

    nfo->numExtents++;
    if(nfo->fixExtents != NULL) {
	DEBUG('G',"Adding 0x%04x/0x%04x to List\n",e->pos,e->len);
	nfo->fixExtents->SortedInsert((void *)e,e->pos);
    }

    return false;
}

//
// Name:	printTree
//
bool SwapFile::printTree(void *node,int lvl,bool invalid)
{
    for(int i = 0; i < lvl; i++)
	DEBUG('g'," ");
    if(invalid) {
	DEBUG('g',"--\n");
    } else {
	Extent *e = (Extent *)node;

	DEBUG('g',"0x%04x/0x%04x\n",e->pos,e->len);
    }

    return false;
}
