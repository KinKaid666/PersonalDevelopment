// openfile.cc 
//	Routines to manage an open Nachos file.  As in UNIX, a
//	file must be open before we can read or write to it.
//	Once we're all done, we can close it (in Nachos, by deleting
//	the OpenFile data structure).
//
//	Also as in UNIX, for convenience, we keep the file header in
//	memory while the file is open.
//
// Copyright (c) 1992-1993 The Regents of the University of California.
// All rights reserved.  See copyright.h for copyright notice and limitation 
// of liability and disclaimer of warranty provisions.

// $Id: openfile.cc,v 3.26 2001/11/14 02:15:28 trc2876 Exp $

// $Log: openfile.cc,v $
// Revision 3.26  2001/11/14 02:15:28  trc2876
// ls works (not tested from userland)
// nachos -l -d u    dumps the filesystem tree
// nachos -ld -d L   does an ls
//
// Revision 3.25  2001/11/14 00:25:28  trc2876
// Write/Read work now
//
// Revision 3.24  2001/11/13 22:58:32  trc2876
// in progress
//
// Revision 3.23  2001/11/13 22:56:00  etf2954
// fixed something, now we can add more than one file
//
// Revision 3.22  2001/11/13 17:53:38  trc2876
// locking issues fixed
//
// Revision 3.21  2001/11/13 17:42:52  trc2876
// still locking problems....but data looks good
//
// Revision 3.20  2001/11/13 04:02:43  trc2876
// Hint() added to OpenFile
//
// Revision 3.19  2001/11/13 03:34:35  trc2876
// *** empty log message ***
//
// Revision 3.18  2001/11/13 00:33:13  trc2876
// core dumps on a new
//
// Revision 3.17  2001/11/13 00:29:18  eae8264
// Implemented Length.
//
// Revision 3.16  2001/11/12 23:42:43  trc2876
// data goes to disk but incorrectly
//
// Revision 3.15  2001/11/12 23:33:29  trc2876
// compiles...not debugged
//
// Revision 3.14  2001/11/12 23:07:54  trc2876
// debugging cp
//
// Revision 3.13  2001/11/12 21:52:34  trc2876
// OpenFile::Read written and compiles...code reviewed but untested
//
// Revision 3.12  2001/11/12 21:33:03  trc2876
// OpenFile::Read written and compiles...code reviewed but untested
//
// Revision 3.11  2001/11/12 01:45:10  trc2876
// broken coke...but it compiles
//
// Revision 3.10  2001/11/12 00:26:23  p544-01b
// changed value passed to get/put data functions
//
// Revision 3.9  2001/11/11 23:38:23  p544-01b
// Added release node code
//
// Revision 3.8  2001/11/11 22:05:05  trc2876
// in progress code review for OpenFile
//
// Revision 3.7  2001/11/11 22:01:57  trc2876
// in progress code review for OpenFile
//
// Revision 3.6  2001/11/11 21:47:01  p544-01b
// Write code entered
//
// Revision 3.5  2001/11/11 21:37:35  p544-01b
// Commented Read function
//
// Revision 3.4  2001/11/11 21:02:23  p544-01b
// Added some debug statements
//
// Revision 3.3  2001/11/11 20:44:06  p544-01b
// More code added for multiple sector reads
//
// Revision 3.2  2001/11/11 00:55:25  p544-01b
// Started implementation
//
// Revision 3.1  2001/11/09 17:30:12  eae8264
// Removed the original NachOS basic filesystem
// classes that we're not using.
// Also restructured filesys header file so that it's closer
// to out standards.  Removed all original NachOS file system
// code from filesys and openfile.
//
// Revision 3.0  2001/11/04 19:46:28  trc2876
// force to 3.0
//
// Revision 2.1  2001/11/02 21:42:59  etf2954
// First time for everything
//

#include "copyright.h"
#include "openfile.h"
#include "system.h"

//
// Name: OpenFile::OpenFile
//
OpenFile::OpenFile(MTreeInternalNode *fNode) :
    fNodeID_(fNode->id()),
    seekPosition_(0)
{ 
    fileSystem->releaseNode(fNode);
}

//
// Name: OpenFile::~OpenFile
//
OpenFile::~OpenFile()
{
}

//
// Name: OpenFile::Seek
//
void
OpenFile::Seek(int position)
{
    seekPosition_ = position;
}	

//
// Name: OpenFile::Read
//
int
OpenFile::Read(char *into, int numBytes)
{
    int totalBytes(0);

    // Step 1: get Meta node and ensure there are numBytes to read, if not
    //         read as many as we can
    //
    MTreeInternalNode *rnode((MTreeInternalNode *)fileSystem->getNode(fNodeID_));
    ASSERT(rnode);
    MTreeMetaNode *mnode((MTreeMetaNode *)fileSystem->getNode(rnode->child(0)));
    if(mnode == NULL) {
	fileSystem->releaseNode(rnode);
	return 0;
    }

    if((numBytes + seekPosition_) >= mnode->size())
	numBytes = mnode->size() - seekPosition_;
    fileSystem->releaseNode(mnode);
    mnode = NULL;

    // Step 2a: locate starting extent
    //
    MTreeDataNode *dnode(NULL);
    size_t i,j,virtualBytePosition(0);
    for(i = 1; i < 4 && numBytes > 0; i++) {
	dnode = (MTreeDataNode *)fileSystem->getNode(rnode->child(i));
	if(dnode == NULL)
	    break;
	if(dnode->type() == MTreeNode::DATA) {
	    if(seekPosition_ >= virtualBytePosition &&
	       seekPosition_ < (virtualBytePosition + (dnode->len() * SectorSize))) {
		// this is our data node
		//
		for(j = 0; j < 6 && dnode->data(j) != 0x0000 && numBytes > 0; j += 2) {
		    if(seekPosition_ < (virtualBytePosition + (dnode->data(j+1) * SectorSize))) {
			// Step 2b: read data from extent into buffer
			//
			unsigned char *chunk(NULL);
			T_WORD VstartBlock,PstartBlock,Ioffset,Elen;

			VstartBlock = divRoundDown(seekPosition_ + virtualBytePosition,SectorSize);
			PstartBlock = dnode->data(j) + VstartBlock;
			Ioffset = (seekPosition_ + virtualBytePosition) % SectorSize;
			Elen = dnode->data(j+1) - VstartBlock;

			for(size_t k = 0; numBytes > 0 && k < Elen; k++) {
			    chunk = fileSystem->getData(PstartBlock + k);
			    if((unsigned int)numBytes <= (SectorSize - Ioffset)) {
				memcpy(into + totalBytes,chunk + Ioffset,numBytes);
				totalBytes += numBytes;
				virtualBytePosition += numBytes;
				numBytes = 0;
			    } else {
				memcpy(into + totalBytes,chunk + Ioffset,(SectorSize - Ioffset));
				totalBytes += (SectorSize - Ioffset);
				numBytes -= (SectorSize - Ioffset);
				virtualBytePosition += (SectorSize - Ioffset);
			    }
			    Ioffset = 0;
			    fileSystem->putData(PstartBlock + k);
			    fileSystem->releaseData(PstartBlock + k);
			    chunk = NULL;
			}
		    } else {
			    virtualBytePosition += dnode->data(j+1) * SectorSize;
		    }
		}
	    } else {
		virtualBytePosition += dnode->len();
	    }
	}
	fileSystem->releaseNode(dnode);
    }
    fileSystem->releaseNode(rnode);
    seekPosition_ += totalBytes;

    return totalBytes;
}

//
// Name: OpenFile::Write
//
int
OpenFile::Write(char *from, int numBytes)
{
    int totalBytes(0);

    DEBUG('w',"Write: Step 1\n");
    MTreeInternalNode *rnode((MTreeInternalNode *)fileSystem->getNode(fNodeID_));
    ASSERT(rnode);

    // Step 2a: locate starting extent
    //
    DEBUG('w',"Write: Step 2a\n");
    MTreeDataNode *dnode(NULL);
    size_t i,j,virtualBytePosition(0);
    for(i = 1; i < 4 && numBytes > 0; i++) {
	if(rnode->child(i) == 0x0000) {
	    dnode = new MTreeDataNode(fileSystem->allocateNode());
	    if(dnode == NULL) {
		numBytes = 0;
		break;
	    }
	    rnode->child(i,dnode->id());
	    fileSystem->putNode(dnode);
	    fileSystem->putNode(rnode);
	    DEBUG('w',"Write: added new data node 0x%04x\n",dnode->id());
	} else {
	    dnode = (MTreeDataNode *)fileSystem->getNode(rnode->child(i));
	    if(dnode == NULL) {
		numBytes = 0;
		break;
	    }
	    DEBUG('w',"Write: check data node 0x%04x\n",dnode->id());
	}
	if(dnode->type() == MTreeNode::DATA) {
	    if(seekPosition_ >= virtualBytePosition &&
	       seekPosition_ <= (virtualBytePosition + (dnode->len() * SectorSize))) {
		DEBUG('w',"Write: data node found\n");
		// this is our data node
		//
		for(j = 0; j < 6 && numBytes > 0; j += 2) {
		    if(dnode->data(j) == 0x0000) {
			// no good
			continue ;
			T_WORD start(fileSystem->allocateDataExtent(0,divRoundUp(numBytes,SectorSize)));
			if(start != 0) {
			    dnode->data(j,start);
			    dnode->data(j+1,divRoundUp(numBytes,SectorSize));
			    dnode->len(dnode->len() + divRoundUp(numBytes,SectorSize));
			    rnode->child(j,dnode->id());
			    fileSystem->putNode(dnode);
			    fileSystem->putNode(rnode);
			    DEBUG('w',"Write: added new extent size 0x%04x\n",dnode->data(j+1));
			} else {
			    fileSystem->releaseNode(dnode);
			    numBytes = 0;
			    break;
			}
		    }
		    if(seekPosition_ < (virtualBytePosition + (dnode->data(j+1) * SectorSize))) {
			// Step 2b: write data from buffer into extent
			//
			DEBUG('w',"Write: Step 2b\n");
			unsigned char *chunk(NULL);
			T_WORD VstartBlock,PstartBlock,Ioffset,Elen;

			VstartBlock = divRoundDown(seekPosition_ + virtualBytePosition,SectorSize);
			PstartBlock = dnode->data(j) + VstartBlock;
			Ioffset = (seekPosition_ + virtualBytePosition) % SectorSize;
			Elen = dnode->data(j+1) - VstartBlock;

			for(size_t k = 0; numBytes > 0 && k < Elen; k++) {
			    chunk = fileSystem->getData(PstartBlock + k);
			    if((unsigned int)numBytes <= (SectorSize - Ioffset)) {
				memcpy(chunk + Ioffset,from + totalBytes,numBytes);
				totalBytes += numBytes;
				virtualBytePosition += numBytes;
				numBytes = 0;
			    } else {
				memcpy(chunk + Ioffset,from + totalBytes,(SectorSize - Ioffset));
				totalBytes += (SectorSize - Ioffset);
				numBytes -= (SectorSize - Ioffset);
				virtualBytePosition += (SectorSize - Ioffset);
			    }
			    Ioffset = 0;
			    fileSystem->putData(PstartBlock + k);
			    fileSystem->releaseData(PstartBlock + k);
			    chunk = NULL;
			}
		    } else {
			virtualBytePosition += dnode->data(j+1) * SectorSize;
		    }
		}
	    } else {
		virtualBytePosition += dnode->len();
	    }
	}
	fileSystem->releaseNode(dnode);
    }
    seekPosition_ += totalBytes;
    MTreeMetaNode *mnode((MTreeMetaNode *)fileSystem->getNode(rnode->child(0)));
    if(mnode == NULL) {
	fileSystem->releaseNode(rnode);
	return 0;
    }

    mnode->size(mnode->size() + totalBytes);
    fileSystem->releaseNode(mnode);
    fileSystem->releaseNode(rnode);

    return totalBytes;
}

//
// Name: OpenFile::Hint
//
void
OpenFile::Hint(size_t len)
{
    MTreeInternalNode *rnode((MTreeInternalNode *)fileSystem->getNode(fNodeID_));
    ASSERT(rnode);
    MTreeMetaNode *mnode((MTreeMetaNode *)fileSystem->getNode(rnode->child(0)));
    if(mnode == NULL) {
	fileSystem->releaseNode(rnode);
	return;
    }
    int blocks(divRoundUp(len - (mnode->size() - seekPosition_),SectorSize));
    fileSystem->releaseNode(mnode);
    // allocate blocks
    //
    for(size_t i = 1; i < 4 && blocks > 0; i++) {
	MTreeDataNode *dnode(NULL);
	if(rnode->child(i) == 0x0000) {
	    dnode = new MTreeDataNode(fileSystem->allocateNode());
	    if(dnode == NULL)
		break;
	    rnode->child(i,dnode->id());
	    fileSystem->putNode(dnode);
	    fileSystem->putNode(rnode);
	    DEBUG('w',"Hint: added new data node 0x%04x\n",dnode->id());
	} else {
	    dnode = (MTreeDataNode *)fileSystem->getNode(rnode->child(i));
	}
	for(size_t j = 0; j < 6; j += 2) {
	    if(dnode->data(j) == 0x0000) {
		T_WORD start(fileSystem->allocateDataExtent(0,blocks));
		if(start != 0) {
		    dnode->data(j,start);
		    dnode->data(j+1,blocks);
		    dnode->len(dnode->len() + blocks);
		    fileSystem->putNode(dnode);
		    fileSystem->releaseNode(dnode);
		    dnode = NULL;
		    blocks = 0;
		    break;
		    DEBUG('w',"Hint: added new extent size 0x%04x\n",dnode->data(j+1));
		} else {
		    fileSystem->releaseNode(dnode);
		    break;
		}
	    }
	}
    }
    fileSystem->releaseNode(rnode);
}

//
// Name: OpenFile::ReadAt
//
int
OpenFile::ReadAt(char *into, int numBytes, int position)
{
    Seek(position);
    return Read(into, numBytes);
}

//
// Name: OpenFile::WriteAt
//
int
OpenFile::WriteAt(char *from, int numBytes, int position)
{
    Seek(position);
    return Write(from, numBytes);
}

//
// Name: OpenFile::Length
//
int
OpenFile::Length() 
{ 
    MTreeInternalNode* iNode ; 
    MTreeMetaNode* mNode ;
    T_WORD returnVal ; 

    while( (iNode = (MTreeInternalNode*)fileSystem->getNode(fNodeID_)) == NULL )
	currentThread->Yield() ;

    while((mNode=(MTreeMetaNode*)fileSystem->getNode(iNode->child(0))) == NULL)
	currentThread->Yield() ;

    returnVal = mNode->size() ;
    fileSystem->releaseNode( iNode ) ;
    fileSystem->releaseNode( mNode ) ;

    return returnVal ;
}
