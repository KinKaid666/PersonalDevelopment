// File:         $Id: SwapFile.h,v 3.0 2001/11/04 19:47:07 trc2876 Exp $
// Author:       Trevor Clarke, Eric Eells, Eric Ferguson, Dan Westgate
// Contributors: {}
// Description:  SwapFile class
// Revisions:
//               $Log: SwapFile.h,v $
//               Revision 3.0  2001/11/04 19:47:07  trc2876
//               force to 3.0
//
//               Revision 1.16  2001/10/31 20:38:23  trc2876
//               forget it, TOP_OF_ADDRSPACE is 2^31-1 so we are fine w/ ints
//
//               Revision 1.15  2001/10/31 20:33:11  trc2876
//               changed to unsigned int for addresses
//
//               Revision 1.14  2001/10/31 03:29:15  trc2876
//               bigarray sorta works now
//
//               Revision 1.13  2001/10/29 23:57:44  trc2876
//               packing extents is done
//               extent system is still buggy
//
//               Revision 1.12  2001/10/28 23:35:52  trc2876
//               after merge
//
//               Revision 1.9.2.2  2001/10/28 23:20:57  trc2876
//               swapfile almost rewritten
//               need to write defragmenter
//               ready to merge
//
//               Revision 1.9.2.1  2001/10/28 21:10:30  trc2876
//               cleaned out dan code....this branch is a rewrite of SwapFile
//
//               Revision 1.9  2001/10/25 20:14:32  eae8264
//               Added coimments... fixed some bugs...
//
//               Revision 1.8  2001/10/25 01:03:47  etf2954
//               Code review, everythink okay
//
//               Revision 1.7  2001/10/23 23:34:06  p544-01b
//               Changed perameters to allocSegment
//
//               Revision 1.6  2001/10/23 20:38:48  eae8264
//               stubs.
//
//               Revision 1.5  2001/10/23 17:35:17  eae8264
//               Corrected some comments and updated signatures.
//               We still need to add alloc'ing functions....
//
//               Revision 1.4  2001/10/21 22:17:26  eae8264
//               Changed data members to follow "team standard"
//
//               Revision 1.3  2001/10/20 22:11:28  etf2954
//               Added some comments as to what each function does
//
//               Revision 1.2  2001/10/20 20:08:52  eae8264
//               fixed up style and comments./..
//

#ifndef _SwapFile_H
#define _SwapFile_H

#include "bintree.h"
#include "list.h"
#include "synch.h"

class SwapFile
{
public:
    //
    // Name:	(constructor)
    //
    // Description: Sets up things according to whether the file is read-only
    //		    or not.  i.e. we don't need a bitmap or a semaphore 
    // 		    if the file's read-only.
    //
    // Arguments:	A *valid* file pointer, and whether to treat it as 
    //			read-only or not.
    //
    SwapFile( OpenFile* file, bool readOnly );

    //
    // Name:	(destructor)
    //
    ~SwapFile();

    //
    // Name:		swapIn
    //
    // Description: 	Reads page from file.
    //
    // Arguments:	fAddr: file address to copy from
    //			pAddr: pAddr machine address to copy to
    //
    // Returns:		void
    //
    // Exceptions:	None
    //
    void swapIn( int fAddr, int pAddr );

    //
    // Name:		swapOut
    //
    // Description: 	Copies page from pAddr to file address fAddr
    //			NOTE:   if this object is read-only, this function 
    //				is a noop
    //
    // Arguments:	fAddr: file address
    //			pAddr: physical address of page.
    //
    // Returns:		void
    //
    // Exceptions:	None
    //
    void swapOut( int fAddr, int pAddr );

    //
    // Name:		allocSegment
    //
    // Description: 	Allocates a segment in the file and returns the address
    //			to this segment
    //
    // Arguments:	Size of segment in bytes
    //
    // Returns:	        The Start Address of the segment. A negative number
    //                  indicates an error.
    //
    // Exceptions:	None
    //
    int allocSegment( int sizeInBytes );

    //
    // Name:		deallocSegment
    //
    // Description: 	Removes segment
    //
    // Arguments:	segment address in file, and length of segment in bytes
    //
    // Returns:		void
    //
    // Exceptions:	None
    //
    void deallocSegment( int fAddr, int lengthInBytes );

    //
    // Name:		packExtents
    //
    // Description: 	pack small extents into larger extents
    //                  this should be run periodically to prevent
    //                  memory fragmentation
    //
    // Arguments:	None
    //
    // Returns:		void
    //
    // Exceptions:	None
    //
    void packExtents();

    //
    // Name:		extentInfo
    //
    // Description: 	helper function used to gather extent info
    //
    // Arguments:	node: current node in the traversal
    //			arg:  pointer to extent information
    //
    // Returns:		false always
    //
    static bool extentInfo(void *node,void *arg);

    static bool printTree(void *node,int lvl,bool invalid);

private:
    class Extent
    {
    public:
	Extent(int p, int l) : pos(p),len(l) {}
	int pos;
	int len;
    }; // class Extent

    class ExtentInfo
    {
    public:
	ExtentInfo() : numExtents(0),fixExtents(new List) {}
	~ExtentInfo() { if(fixExtents != NULL) delete fixExtents; }
	int numExtents;
	List *fixExtents;
    }; // class ExtentInfo

private:

    // File reference
    OpenFile* fileHandle_;

    // Size of file...
    int size_ ;

    // Specifies whether read-only or not.  Should only be read-only
    // if the file is pointing to an executable.
    bool readOnly_ ;
    
    // Tree containing free extents in the file
    BinTree *freeExtents_;

    // This is so that SwapIn and SwapOut can't access the file or the BinTree
    // at the same time.
    Lock *mutex_ ;

}; // SwapFile 

#endif
