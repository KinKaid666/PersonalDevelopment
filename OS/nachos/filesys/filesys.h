// File:         $Id: filesys.h,v 3.28 2001/11/15 00:19:39 trc2876 Exp $
// Author:       p544-01b
// Contributors: 
// Description:  Contains original UNIX implementation stub.  Many features
//		 set around all-new design.  This header file is based on the
//		 file that came with the NachOS file system.
// Revisions:
// $Log: filesys.h,v $
// Revision 3.28  2001/11/15 00:19:39  trc2876
// cd works now
//
// Revision 3.27  2001/11/14 23:25:11  trc2876
// initial Chdir code implemented....needs testing
// copyAll moved to Makefile
//
// Revision 3.26  2001/11/14 22:00:25  trc2876
// more merging
//
// Revision 3.25  2001/11/14 21:57:36  trc2876
// merged changes
//
// Revision 3.24  2001/11/14 21:44:14  etf2954
// findRecursiveNode
//
// Revision 3.23  2001/11/14 21:01:09  trc2876
// wh00t
//
// Revision 3.22  2001/11/14 19:39:28  etf2954
// wrote printInternalNode
//
// Revision 3.21  2001/11/14 17:13:49  etf2954
// wrote getFileDir correctly
//
// Revision 3.20  2001/11/14 02:15:28  trc2876
// ls works (not tested from userland)
// nachos -l -d u    dumps the filesystem tree
// nachos -ld -d L   does an ls
//
// Revision 3.19  2001/11/13 17:09:54  trc2876
// debugging functionality added to filesys.cc
//
// Revision 3.18  2001/11/12 01:45:10  trc2876
// broken coke...but it compiles
//
// Revision 3.17  2001/11/11 22:18:03  trc2876
// added DataCache wrappers to FileSystem
//
// Revision 3.16  2001/11/11 21:29:20  trc2876
// filesystem formatting works
//
// Revision 3.15  2001/11/11 20:29:22  trc2876
// pulled format code out of FileSystem() and into FileSystem::format()
//
// Revision 3.14  2001/11/11 19:32:21  trc2876
// wrote removeChild
//
// Revision 3.13  2001/11/11 18:32:44  trc2876
// Fixed compile errors
// some have been // out because they don't fit the layout of the class and I'm not
//     sure what is correct....they have been preceded by a // todo: fix this
//
// MTreeNode->{dirty_,inUse_} have mutators and accessors and refs to them outside
//     MTreeNode have been changed
//
// Revision 3.12  2001/11/10 19:07:31  etf2954
// wrote Remove
//
// Revision 3.11  2001/11/10 00:54:08  trc2876
// fixed compile stuff
//
// Revision 3.10  2001/11/09 17:37:12  eae8264
// Small change, added todo, and removed some
// orig nachos stuff I missed.
//
// Revision 3.9  2001/11/09 17:30:12  eae8264
// Removed the original NachOS basic filesystem
// classes that we're not using.
// Also restructured filesys header file so that it's closer
// to out standards.  Removed all original NachOS file system
// code from filesys and openfile.
//
// Revision 3.8  2001/11/09 01:01:28  etf2954
// added corrects to use filestat.h
//
// Revision 3.7  2001/11/08 22:12:41  eae8264
// Node Cache mostly implemented and added.
//
// Revision 3.6  2001/11/08 18:42:16  trc2876
// added mtree class and some stubs to filesystem
// compiles
// not tested
// still needs some work (search for todo's)
//
// Revision 3.5  2001/11/08 01:50:05  etf2954
// new file
//
// Revision 3.4  2001/11/08 01:32:56  trc2876
// added getFileNode() that uses comps instead of fname
//
// Revision 3.3  2001/11/08 00:59:03  etf2954
// Added the mkdir function (header)
//
// Revision 3.2  2001/11/08 00:04:08  trc2876
// added stubs
//
// Revision 3.1  2001/11/07 22:55:08  trc2876
// in progress getMetaNode
//
// Revision 3.0  2001/11/04 19:46:28  trc2876
// force to 3.0
//
// Revision 2.1  2001/11/02 21:42:59  etf2954
// First time for everything
//

#ifndef FS_H
#define FS_H

#include "copyright.h"
#include "openfile.h"
#include "mtree.h"
#include "etree.h"
#include "NodeCache.h"
#include "DataCache.h"

#ifdef FILESYS_STUB 		// Temporarily implement file system calls as 
				// calls to UNIX, until the real file system
				// implementation is available
class FileSystem {
public:
    FileSystem(bool format) {}

    bool Create(char *name, int initialSize) { 
	int fileDescriptor = OpenForWrite(name);

	if (fileDescriptor == -1) return false;
	Close(fileDescriptor); 
	return true; 
	}

    OpenFile* Open(char *name) {
	  int fileDescriptor = OpenForReadWrite(name, false);

	  if (fileDescriptor == -1) return NULL;
	  return new OpenFile(fileDescriptor);
      }

    bool Remove(char *name) { return Unlink(name) == 0; }

};

#else // FILESYS
class FileSystem {

public:
    //
    // Name:		(constructor)
    //
    // Description:	Initialize the file system.  Must be called *after*
    //			synchDisk has been initialized.
    //
    // Arguments:	If formatIt, clear all data on disk.
    //
    FileSystem(bool formatIt);
    ~FileSystem();

    //
    // Name:		format()
    //
    // Description:	Formats the filesystem. If the constructor has formatIt==true
    //			then nothing happens and this MUST be called
    //			this is because ETree() accesses fileSystem->putNode() when
    //			told to create a new extent tree so fileSystem must be constructed
    //			already
    //
    // Arguments:	If format, clear all data on disk.
    //
    void format();

    //
    // Name:		Create
    //
    // Description:	Create a file (UNIX creat)
    //                  we ignore initialSize now
    //
    bool Create(char *name, int initialSize = 0);

    //
    // Name:		Open
    //
    // Description:	Open a file (UNIX open)
    //
    OpenFile* Open(char *name);

    //
    // Name:		Remove
    //
    // Description:	Delete a file (UNIX unlink)
    //
    // Returns:		0  : success
    //			-1 : file not found
    //			-2 : directory is not empty
    //			-3 : premissions not correct
    //
    int Remove(char *name);

    //
    // Name:		mkdir
    //
    // Description:	Delete a file (UNIX unlink)
    //
    bool mkdir(char *name);

    //
    // Name:            ListDir
    //
    // Description:     Get directory listing
    //
    int ListDir(char *path,char ***buf);

    //
    // Name:            findRecursiveNode
    //
    // Description:     finds a node from an internal tree
    //
    T_WORD findRecursiveNode( MTreeInternalNode* node, const char* name );

    //
    // Name:		List
    //
    // Description:	Print out file structure (kitchen sink.. for 
    //			debug only)
    //
    void List();

    //
    // Name:		getMetaNode
    //
    // Description:	Get the meta data node for file fname.
    //
    MTreeMetaNode *getMetaNode(const char *fname);

    //
    // Name:		getFileNode
    //
    // Description:	Get the internal node that heads a file subtree.
    //
    MTreeInternalNode *getFileNode(const char *fname);

    //
    // Name:		getFileNode
    //
    // Description:	Same as other getFileNode but uses comps instead
    //			of fname.  (comps are the components of the path)
    //
    MTreeInternalNode *getFileNode(const char **comps, size_t len);

    //
    // Name:		getNode
    //
    // Description:	Get a node given the universally unique node id.
    //			NOTE: Must call releaseNode when finished with the
    //			node.  The node must not be deleted by the client!!!
    //                  this also applies to any get*Node function
    //
    MTreeNode *getNode(T_WORD id);

    //
    // Name:		putNode
    //
    // Description:	Write a node to the file system.
    //
    void putNode(MTreeNode *node,bool force=false,bool ensureInCache=true);

    //
    // Name:		releaseNode
    //
    // Description:	Release the lock on a node.  MUST be called
    //			on any node retrieved with get*Node.
    //
    void releaseNode(MTreeNode *node);

    //
    // Name:		getData
    //
    // Description:	Get a data block given the universally unique block id.
    //			NOTE: Must call releaseData when finished with the
    //			block. 
    //
    unsigned char *getData(T_WORD id);

    //
    // Name:		putData
    //
    // Description:	Write a data block to the file system.
    //
    void putData(T_WORD id);

    //
    // Name:		releaseData
    //
    // Description:	Release the lock on a data block.  MUST be called
    //			on any block retrieved with getData
    //
    void releaseData(T_WORD id);


    // these allocate and release nodes on the filesystem
    //
    T_WORD allocateNode();
    void freeNode(T_WORD id);

    // these allocate and release data extents on the filesystem
    // start and len are in bytes
    //
    T_WORD allocateDataExtent(T_WORD start, T_WORD len);
    void freeDataExtent(T_WORD start, T_WORD len);

public:
   // String parsing utilities
   void freeComps(char **comps, size_t len);
   size_t parsePath(const char *fname,
		    char    **cpathcomps,
		    size_t    cpcount,
		    char   ***comps);

private:

   //
   // Name:            printInternalNode
   //
   // Description:     prints the internal node (recusively)
   //
   size_t printInternalNode( MTreeInternalNode* node, char*** buf ) ;

   void deleteFile( MTreeInternalNode* file ) ;

   //
   // Name:		addChild
   //
   // Description:	adds 'child' to 'parent'
   //
   // Arguments:	parent:	the node in which you want to add a child
   //			child:	the child you want to add to the parent
   //                   split: should we split the tree if necessary?
   // Returns:		 0 : success
   // 			-1 : failure
   //
   int addChild( MTreeInternalNode* parent, MTreeInternalNode* child, bool split=true ) ;

   //
   // Name:		removeChild
   //
   // Description:	removes 'child' from 'parent'
   //
   // Arguments:	parent:	the node in which you want to remove a child
   //			child:	the child you want to remove from the parent
   //                   split: should we split the tree if necessary?
   //
   // Returns:		 0 : success
   // 			-1 : failure
   //
   int removeChild( MTreeInternalNode* parent, MTreeInternalNode* child, bool combine=true ) ;

public:
    void printTree(T_WORD id, int Level);
private:

   // Free extent trees
   ETree *freeNodeExtents_;
   ETree *freeDataExtents_;
   
   // Node cache
   NodeCache* nodeCache_;

   // Data cache
   DataCache* dataCache_;

public:
   char		**cwd;
   size_t	cwdLen;
};

#endif // FILESYS

#endif // FS_H
