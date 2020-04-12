// File:	 $Id: filesys.cc,v 3.65 2001/11/15 00:51:54 trc2876 Exp $
// Author:       p544-01b
// Contributors:
// Description:  Implementation for FileSystem
// Revisions:
// $Log: filesys.cc,v $
// Revision 3.65  2001/11/15 00:51:54  trc2876
// *** empty log message ***
//
// Revision 3.64  2001/11/15 00:19:39  trc2876
// cd works now
//
// Revision 3.63  2001/11/15 00:03:10  trc2876
// Chdir() done
//
// Revision 3.62  2001/11/14 23:25:10  trc2876
// initial Chdir code implemented....needs testing
// copyAll moved to Makefile
//
// Revision 3.61  2001/11/14 22:35:50  trc2876
// fixed getFileNode problem
//
// Revision 3.60  2001/11/14 21:57:35  trc2876
// merged changes
//
// Revision 3.59  2001/11/14 21:44:14  etf2954
// findRecursiveNode
//
// Revision 3.58  2001/11/14 21:01:09  trc2876
// wh00t
//
// Revision 3.57  2001/11/14 20:36:25  trc2876
// removed excess debug statements
//
// Revision 3.56  2001/11/14 20:29:29  trc2876
// *** empty log message ***
//
// Revision 3.55  2001/11/14 20:17:05  etf2954
// no idea
//
// Revision 3.54  2001/11/14 20:16:53  trc2876
// look at todo
//
// Revision 3.53  2001/11/14 19:51:10  trc2876
// fixed eric's goof
//
// Revision 3.52  2001/11/14 19:39:28  etf2954
// wrote printInternalNode
//
// Revision 3.51  2001/11/14 19:16:59  trc2876
// added code to drop invalid entries from the caches
//
// Revision 3.50  2001/11/14 19:08:05  trc2876
// fixed merge problem
//
// Revision 3.49  2001/11/14 19:04:01  trc2876
// almost fixed problem with extent allocation
// can rm now
//
// Revision 3.48  2001/11/14 17:13:49  etf2954
// wrote getFileDir correctly
//
// Revision 3.47  2001/11/14 16:25:07  trc2876
// *** empty log message ***
//
// Revision 3.46  2001/11/14 04:43:44  trc2876
// can add directory....look at Create now
//
// Revision 3.45  2001/11/14 04:28:25  trc2876
// refixed getFileNode locking problem
//
// Revision 3.44  2001/11/14 04:24:27  trc2876
// fixed synch problem in getFileNode
//
// Revision 3.43  2001/11/14 03:59:55  trc2876
// bigfile demonstrated
//
// Revision 3.42  2001/11/14 03:29:23  trc2876
// new ls functionality
//
// Revision 3.41  2001/11/14 03:11:23  trc2876
// changed Create file parsing to use parsePath
//
// Revision 3.40  2001/11/14 02:15:28  trc2876
// ls works (not tested from userland)
// nachos -l -d u    dumps the filesystem tree
// nachos -ld -d L   does an ls
//
// Revision 3.39  2001/11/13 22:55:59  etf2954
// fixed something, now we can add more than one file
//
// Revision 3.38  2001/11/13 17:53:38  trc2876
// locking issues fixed
//
// Revision 3.37  2001/11/13 17:42:52  trc2876
// still locking problems....but data looks good
//
// Revision 3.36  2001/11/13 17:09:54  trc2876
// debugging functionality added to filesys.cc
//
// Revision 3.35  2001/11/13 16:19:02  trc2876
// can copy one file now
//
// Revision 3.34  2001/11/13 04:21:09  trc2876
// still broken
//
// Revision 3.33  2001/11/13 03:34:35  trc2876
// *** empty log message ***
//
// Revision 3.32  2001/11/13 03:13:10  trc2876
// AHHHHHH
//
// Revision 3.31  2001/11/13 02:46:56  trc2876
// different dump
//
// Revision 3.30  2001/11/12 23:07:53  trc2876
// debugging cp
//
// Revision 3.29  2001/11/12 02:10:28  trc2876
// Changed MTreeMetaNode->filename() to allow for copying over of existing filename
// Changed FileSystem::Create() to overwrite existing file
//
// Revision 3.28  2001/11/12 01:45:10  trc2876
// broken coke...but it compiles
//
// Revision 3.27  2001/11/11 22:18:03  trc2876
// added DataCache wrappers to FileSystem
//
// Revision 3.26  2001/11/11 21:29:19  trc2876
// filesystem formatting works
//
// Revision 3.25  2001/11/11 20:29:21  trc2876
// pulled format code out of FileSystem() and into FileSystem::format()
//
// Revision 3.24  2001/11/11 19:58:49  trc2876
// added FileSystem formatting code
//
// Revision 3.23  2001/11/11 19:56:16  etf2954
// code review
//
// Revision 3.22  2001/11/11 19:32:21  trc2876
// wrote removeChild
//
// Revision 3.21  2001/11/11 18:32:44  trc2876
// Fixed compile errors
// some have been // out because they don't fit the layout of the class and I'm not
//     sure what is correct....they have been preceded by a // todo: fix this
//
// MTreeNode->{dirty_,inUse_} have mutators and accessors and refs to them outside
//     MTreeNode have been changed
//
// Revision 3.20  2001/11/10 20:35:49  etf2954
// code review
//
// Revision 3.19  2001/11/10 19:24:40  etf2954
// wrote Open
//
// Revision 3.18  2001/11/10 19:07:30  etf2954
// wrote Remove
//
// Revision 3.17  2001/11/10 00:54:08  trc2876
// fixed compile stuff
//
// Revision 3.16  2001/11/10 00:41:01  trc2876
// added free extents initialization to filesys constructor
// added 1 level deep file add
//
// Revision 3.15  2001/11/10 00:16:25  eae8264
// Changed NodeCache so that node parameters may be
// null, so that some client functions may be simplified.
// A client call was updated to work with the new sig.
//
// Revision 3.14  2001/11/10 00:11:38  etf2954
// mkdir done
//
// Revision 3.13  2001/11/09 21:56:32  etf2954
// removed one todo
//
// Revision 3.12  2001/11/09 17:37:12  eae8264
// Small change, added todo, and removed some
// orig nachos stuff I missed.
//
// Revision 3.11  2001/11/09 17:30:12  eae8264
// Removed the original NachOS basic filesystem
// classes that we're not using.
// Also restructured filesys header file so that it's closer
// to out standards.  Removed all original NachOS file system
// code from filesys and openfile.
//
// Revision 3.10  2001/11/09 01:07:22  trc2876
// removed comment on function call
//
// Revision 3.9  2001/11/09 01:01:28  etf2954
// added corrects to use filestat.h
//
// Revision 3.8  2001/11/09 00:49:47  trc2876
// added ETree::remove
// added MTree::child() mutator
//
// Revision 3.7  2001/11/08 23:25:29  trc2876
// added insert to etree and fixed linker error
//
// Revision 3.6  2001/11/08 21:55:06  eae8264
// Removed basic filesys code.
//
// Revision 3.5  2001/11/08 18:42:16  trc2876
// added mtree class and some stubs to filesystem
// compiles
// not tested
// still needs some work (search for todo's)
//
// Revision 3.4  2001/11/08 01:32:55  trc2876
// added getFileNode() that uses comps instead of fname
//
// Revision 3.3  2001/11/08 00:50:26  trc2876
// added code for getFileNode()
//
// Revision 3.2  2001/11/08 00:04:05  trc2876
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

#include <stdlib.h>
#include <string.h>
#include "copyright.h"

#include "disk.h"
#include "filesys.h"

//
// Name: (constructor)
//
FileSystem::FileSystem(bool formatIt) :
	freeNodeExtents_( NULL ),
	freeDataExtents_( NULL ),
 	nodeCache_( NULL ),
	dataCache_( new DataCache(32) ),
	cwd(NULL),
	cwdLen(0)
{ 
    MTreeInternalNode *rootNode(NULL);
    T_WORD fneRootID(0x0000),fdeRootID(0x0000);

    if(formatIt) { // format a new filesystem
	nodeCache_ = new NodeCache( false, 4 );
    } else {     // load a filesystem)
	nodeCache_ = new NodeCache( true, 64 );
	rootNode = (MTreeInternalNode *)getNode(0x0000);
	fneRootID = WordToHost(*((T_WORD *)((rootNode->special))));
	fdeRootID = WordToHost(*((T_WORD *)((rootNode->special + 4))));
	DEBUG('p',"Root node(0x%04x)- type: %i  children: 0x%04x, 0x%04x, 0x%04x, 0x%04x   special: 0x%04x, 0x%04x\n",rootNode->id(),rootNode->type(),rootNode->child(0),rootNode->child(1),rootNode->child(2),rootNode->child(3),fneRootID,fdeRootID);
	releaseNode(rootNode);
	freeNodeExtents_ = new ETree(fneRootID);
	freeDataExtents_ = new ETree(fdeRootID);
    }
}

//
// Name: (destructor)
//
FileSystem::~FileSystem()
{
    delete nodeCache_;
    delete dataCache_;
    delete freeNodeExtents_;
    delete freeDataExtents_;
    freeComps(cwd,cwdLen);
}

//
// Name: format()
//
#define NodeDist divRoundDown(NumSectors,4)
void
FileSystem::format()
{
    MTreeInternalNode *rootNode(NULL);
    T_WORD fneRootID(0x0000),fdeRootID(0x0000);
    DEBUG('P',"Filesystem size is %i blocks, %i bytes\n",NumSectors,NumSectors*SectorSize);

    // give the nodes 1/4 of the space and the data blocks the rest
    // this can be changed to allow more files or bigger files
    // it's possible to dynamically change this but the code is a pain
    // in the butt so it's left as a FEATURE
    //
    size_t nesize(NodeDist * (divRoundDown(SectorSize,32)));
    size_t desize(NumSectors - NodeDist);

    rootNode = new MTreeInternalNode(0x0000);
    fneRootID = 0x0001;
    fdeRootID = 0x0002;
    nesize -= 5;
    DEBUG('P',"Allocating %i nodes at 0x0005\n",nesize);
    freeNodeExtents_ = new ETree(0x0005, nesize, fneRootID);
    DEBUG('P',"Allocating %i blocks at 0x%04x\n",desize,(NumSectors * SectorSize) - desize);
    freeDataExtents_ = new ETree(0x0001, desize, fdeRootID);

    fneRootID = WordToMachine(fneRootID);
    fdeRootID = WordToMachine(fdeRootID);
    memcpy(rootNode->special,(T_BYTE *)&fneRootID,sizeof(T_WORD));
    memcpy(rootNode->special + 4,(T_BYTE *)&fdeRootID,sizeof(T_WORD));

    MTreeMetaNode *mnode(new MTreeMetaNode(0x0003));
    mnode->size(0);

    MTreeRawNode *fnnode(new MTreeRawNode(0x0004));
    mnode->filenameNode(0,fnnode->id());
    fnnode->data[0] = '/';
    fnnode->data[1] = '\0';
    putNode(fnnode);
    fnnode = NULL;

    mnode->directory(true);
    mnode->chmod(07);
    rootNode->child(0,mnode->id());
    putNode(mnode,true,false);
    DEBUG('P',"Inserting root node\n");
    putNode(rootNode,true,false);
    delete nodeCache_;
    nodeCache_ = new NodeCache( true, 64 );
}

//
// Name: Create
//
bool
FileSystem::Create(char *name, int initialSize)
{
    // NOTE: assuming name is null terminated
    // NOTE: ignoring initial size

    int pathLen;
    char **comps;

    if((pathLen = parsePath(name,cwd,cwdLen,&comps)) == 0)
	return false;

    //
    // Section: Creating a file
    //
    // FEATURE:	Right now, filename is limited to 124 bytes (including NULL 
    //		termination), should be no limit

    //
    // see if file exists, if not
    // allocate memory for the file
    MTreeInternalNode* file;
    MTreeMetaNode* metaNode;
    if((file = getFileNode((const char **)comps,pathLen)) != NULL) {
	metaNode = (MTreeMetaNode *)getNode(file->child(0));
	metaNode->filename(comps[pathLen-1]);
	metaNode->chmod(06);

	// remove existing data nodes
	// FEATURE: support nested data nodes
	//
	for(size_t j = 1; j < 4; j++) {
	    T_WORD c(file->child(j));
	    if(c == 0x0000)
		break;
	    MTreeDataNode *tmpnode((MTreeDataNode *)getNode(c));
	    for(size_t k = 0; k < 6; ) {
		if(tmpnode->data(k) != 0x0000) {
		    freeDataExtent(tmpnode->data(k++),tmpnode->data(k++));
		}
		else
		    break;
	    }
	    file->child(j,0x0000);
	    releaseNode(tmpnode);
	}
	metaNode->size(0);

	putNode(metaNode);
	putNode(file);
	releaseNode(metaNode);
	releaseNode(file);

	freeComps(comps,pathLen);
	return true;
    }

    //
    //	get the directory Node, making sure it exists
    MTreeInternalNode* dir(getFileNode((const char **)comps,pathLen - 1)) ; 
    if(!dir) return -1 ;


    file = new MTreeInternalNode(allocateNode());
    metaNode = new MTreeMetaNode(allocateNode());

    file->child( 0, metaNode->id() ) ; 
    metaNode->chmod(06);
    metaNode->size( 0 ) ;
    metaNode->filename( comps[pathLen - 1] ) ;


    //
    // Section: add the file to its parent directory
    //
    // FEATURE:	Right now, filename is limited to 124 bytes (including NULL 
    //		termination), should be no limit

    freeComps(comps,pathLen);
    int retValue = addChild( dir, file, true ) ;
    if( retValue == 0 )
    {
	putNode( dir      ) ;
	putNode( file     ) ;
	putNode( metaNode ) ;
	// get directory meta node and update size
	metaNode = (MTreeMetaNode *)getNode(dir->child(0));
	metaNode->size(metaNode->size() + 1);
	putNode(metaNode);
	releaseNode(metaNode);
	releaseNode( dir  ) ;
	return 0 ;
    }
    else 
    {
	// couldn't add the child
	delete file;
	delete metaNode;
	releaseNode( dir  ) ;
	return -1 ;
    }
}

//
// Name: Open
//
OpenFile *
FileSystem::Open(char *name)
{ 
    MTreeInternalNode* node = getFileNode( name ) ;
    if( node ) 
    {
	return new OpenFile( node ) ;
    }
    else 
    {
	return NULL ;
    }
}

//
// Name: Remove
//
int
FileSystem::Remove(char *name)
{ 
    int pathLen;
    char **comps;

    if((pathLen = parsePath(name,cwd,cwdLen,&comps)) == 0)
	return false;

    //
    //  get the current working directory
    //	get the directory Node, making sure it exists
    MTreeInternalNode* node   (getFileNode((const char **)comps,pathLen)) ; 
    if(node == NULL) {
	return -3;
	freeComps(comps,pathLen);
    }
    MTreeInternalNode* cwdNode(getFileNode((const char **)comps,pathLen - 1)) ;
    MTreeMetaNode* metaNode( (MTreeMetaNode *)getNode(node->child(0)) ) ;

    freeComps(comps,pathLen);

    if((!node) || (!cwdNode) || (!metaNode) || (metaNode->type() != MTreeNode::META))
    {
	releaseNode(node);
	releaseNode(cwdNode);
	releaseNode(metaNode);
	return -1 ;
    }

    if( metaNode->directory() && node->nchildren() != 1 )
    {
	releaseNode(node);
	releaseNode(cwdNode);
	releaseNode(metaNode);
        return -2 ; // directory is not empty
    }
 

    //
    // Remove that sum-bitch
    // 1.) Remove and free the meta node
    // 2.) Remove and free the Internal node
    // 3.) update the above dir

    int returnVal = removeChild( cwdNode, node ) ;
    if( returnVal == 0  ) 
    {
	// FEATURE: support nested data nodes
	for(size_t j = 1; j < 4; j++) {
	    T_WORD c(node->child(j));
	    if(c == 0x0000)
		break;
	    MTreeDataNode *tmpnode((MTreeDataNode *)getNode(c));
	    for(size_t k = 0; k < 6; ) {
		if(tmpnode->data(k) != 0x0000)
		    freeDataExtent(tmpnode->data(k++),tmpnode->data(k++));
		else
		    break;
	    }
	    node->child(j,0x0000);
	    releaseNode(tmpnode);
	}

	freeNode( metaNode->id() ) ;
	freeNode(     node->id() ) ;

	MTreeMetaNode *mnode((MTreeMetaNode *)getNode(cwdNode->child(0)));
	if(mnode != NULL && mnode->type() == MTreeNode::META)
	    mnode->size(mnode->size() - 1);
	putNode(mnode);
	releaseNode(mnode);

	putNode    ( cwdNode ) ;
    }
    else 
    {
	// failure
	delete metaNode ;
	delete node     ;
    }
    releaseNode(node);
    releaseNode(cwdNode);
    releaseNode(metaNode);
    return returnVal ;    
} 

//
// FileSystem::mkdir
//
bool
FileSystem::mkdir(char *name)
{
    int pathLen;
    char **comps;

    if((pathLen = parsePath(name,cwd,cwdLen,&comps)) == 0)
	return false;

    //
    // Section: Creating a directory
    //
    // LIMITATION:	Right now, filename is limited to 124 bytes 
    //			(including NULL termination), should be no limit

    //
    //	get the directory Node, making sure it exists
    MTreeInternalNode* olddir(getFileNode((const char **)comps,pathLen - 1)) ; 
    if(!olddir) return -1 ;

    //
    //	allocate memory for the file
    MTreeInternalNode* newdir(new MTreeInternalNode(allocateNode())) ;

    //
    // 	setup all the info for the file
    MTreeMetaNode* metaNode(new MTreeMetaNode(allocateNode())) ;

    // 
    //	initial size is set to zero
    metaNode->size( 0 ) ;

    metaNode->filename( comps[pathLen - 1] ) ;
    metaNode->directory( true ) ;
    metaNode->chmod(07);

    //
    // Add the files meta node as it's first child 
    newdir->child( 0, metaNode->id() ) ; 

    freeComps(comps,pathLen);

    int retValue = addChild( olddir, newdir ) ;
    if( retValue == 0 )
    {
	putNode( metaNode ) ;
	// get directory meta node and update size
	putNode( newdir   ) ;
	releaseNode(metaNode);
	metaNode = (MTreeMetaNode *)getNode(olddir->child(0));
	metaNode->size(metaNode->size() + 1);
	putNode(metaNode);
	putNode( olddir   ) ;
	releaseNode(metaNode);
	releaseNode( olddir  ) ;
    }
    else 
    {

	//PROBLEM, shit the addChild f'd up
	delete newdir ;
	delete metaNode ;
	releaseNode( olddir  ) ;
    }
    return retValue ;
}

//
// Name: ListDir
//
int
FileSystem::ListDir(char *path,char ***buf)
{
    MTreeInternalNode *dir(getFileNode(path));
    if(dir == NULL)
	return 0;
    MTreeMetaNode *mnode((MTreeMetaNode *)getNode(dir->child(0)));
    if(mnode == NULL || mnode->type() != MTreeNode::META) {
	releaseNode(mnode);
	releaseNode(dir);
	return 0;
    }
    if(!mnode->directory()) {
	// this is a file, stat it
	//
	char *fn(mnode->filename());
	*buf = new char*[1];
	(*buf)[0] = new char[12 + strlen(fn)];
	int perms(mnode->getmod());
	sprintf((*buf)[0],"-%c%c%c %5d %s",
			(perms & 4) ? 'r' : '-',
			(perms & 2) ? 'w' : '-',
			(perms & 1) ? 'x' : '-',
			mnode->size(),
			fn);
	delete fn;
	releaseNode(mnode);
	releaseNode(dir);
	return 1;
    }
    size_t spos(0),len;
    len = mnode->size();
    releaseNode(mnode);
    mnode = NULL;
    *buf = new char*[len];
    
    MTreeInternalNode *child(NULL);
    MTreeNode *grandChild(NULL);
    for(size_t i = 1; i < 4; i++) {
	if(dir->child(i) == 0x0000)
	    continue;
	if((child = (MTreeInternalNode *)getNode(dir->child(i))) == NULL)
	    continue;
	if((grandChild = getNode(child->child(0))) == NULL) {
	    releaseNode(child);
	    continue;
	}
	switch(grandChild->type()) {
	    case MTreeNode::META: {
		char *fn(((MTreeMetaNode *)grandChild)->filename());
		(*buf)[spos] = new char[12 + strlen(fn)];
		int perms(((MTreeMetaNode *)grandChild)->getmod());
		sprintf((*buf)[spos++],"%c%c%c%c %5d %s",
				((MTreeMetaNode *)grandChild)->directory() ? 'd' : '-',
				(perms & 4) ? 'r' : '-',
				(perms & 2) ? 'w' : '-',
				(perms & 1) ? 'x' : '-',
				((MTreeMetaNode *)grandChild)->size(),
				fn);
		delete fn;
		releaseNode(grandChild);
		releaseNode(child);
		break;
	    }
	    case MTreeNode::INTERNAL:
	    {
		releaseNode(grandChild);
		char** foo = (*buf)+spos ;
		spos += printInternalNode( (MTreeInternalNode*)child, &foo ) ;
		releaseNode(child);
		continue ;
	    }
	    default:
		releaseNode(grandChild);
		releaseNode(child);
		continue;
	}
    }
    releaseNode(dir);
    return len;
}

//
// Name:	printInternalNode
size_t 
FileSystem::printInternalNode( MTreeInternalNode* node, char*** buf )
{
    ASSERT( node ) ;

    size_t pos(0) ;
    MTreeNode *childX(NULL);
    MTreeNode *grandChild(NULL);
    for( int i = 0 ; i < 4 ; i++ )
    {

	if(node->child(i) == 0x0000)
	    continue;
	if((childX = (MTreeInternalNode *)getNode(node->child(i))) == NULL)
	    continue;
	if((grandChild = getNode(((MTreeInternalNode*)childX)->child(0))) == NULL) {
	    releaseNode(childX);
	    continue;
	}
	switch( grandChild->type() ) 
	{
	    case MTreeNode::META: {
		char *fn(((MTreeMetaNode *)grandChild)->filename());
		(*buf)[pos] = new char[12 + strlen(fn)];

		int perms(((MTreeMetaNode *)grandChild)->getmod());
		sprintf((*buf)[pos++],"%c%c%c%c %5d %s",
				((MTreeMetaNode *)grandChild)->directory() ? 'd' : '-',
				(perms & 4) ? 'r' : '-',
				(perms & 2) ? 'w' : '-',
				(perms & 1) ? 'x' : '-',
				((MTreeMetaNode *)grandChild)->size(),
				fn);
		delete fn;
		releaseNode(childX);
		releaseNode(grandChild);
		break;
	    }
	    case MTreeNode::INTERNAL:
	    {
		releaseNode(grandChild);
		char** foo = (*buf)+pos ;
		pos += printInternalNode( (MTreeInternalNode*)childX, &foo ) ;
		releaseNode(childX);
		continue ;
	    }
	    default:
	    {
		releaseNode(childX);
		releaseNode(grandChild);
		continue;
	    }
	}
    }
    return pos ;
}

//
// Name: List 
//
void
FileSystem::List()
{
    printTree(0,0);
}

//
// FileSystem::parsePath
//
size_t
FileSystem::parsePath(const char *fname, char **cpathcomps, size_t cpcount, char ***comps)
{
    size_t i(0),count(0);
    char *tmp;
    char *fncopy = strdup(fname);

    if(fncopy[0] != '/')
	count += cpcount;
    tmp = fncopy;
    while((tmp = strchr(++tmp,'/')) != NULL)
	count++;
    if(fncopy[strlen(fncopy)-1] != '/')
	count++;

    *comps = new char*[count];
    if(fncopy[0] != '/') {
	for(; i < cpcount; i++) {
	    (*comps)[i] = new char[strlen(cpathcomps[i])];
	    strcpy((*comps)[i],cpathcomps[i]);
	}
    }
    for(tmp = strtok(fncopy,"/"); i < count; tmp = strtok(NULL,"/")) {
	(*comps)[i] = new char[strlen(tmp)];
	strcpy((*comps)[i++],tmp);
    }

    free(fncopy);
    return count;
}

//
// FileSystem::freeComps
//
void
FileSystem::freeComps(char **comps, size_t len)
{
    for(size_t i = 0; i < len; i++) {
	delete comps[i];
	comps[i] = 0;
    }
    delete comps;
}

//
// FileSystem::getMetaNode
//
MTreeMetaNode *
FileSystem::getMetaNode(const char *fname)
{
    MTreeNode *fnode;
    
    if((fnode = getFileNode(fname)) == NULL || fnode->type() != MTreeNode::INTERNAL)
	fnode = NULL;
    else
	fnode = getNode(((MTreeInternalNode *)fnode)->child(0));
    return (MTreeMetaNode *)fnode;
}

//
// FileSystem::getFileNode
//
MTreeInternalNode *
FileSystem::getFileNode(const char *fname)
{
    size_t len;
    char **comps(NULL);
    MTreeInternalNode *node(NULL);
    
    len = parsePath(fname,cwd,cwdLen,&comps);
    node = getFileNode((const char **)comps,len);
    freeComps(comps,len);

    return node;
}

MTreeInternalNode *
FileSystem::getFileNode(const char **comps, size_t len)
{
    bool found = false ;
    bool nextComp = false ;
    size_t i,j;

    MTreeInternalNode *node((MTreeInternalNode *)getNode(0));
    MTreeNode *tmp(NULL);
    if(len > 0 && comps[0][0] == '/')
	i = 1;
    else if( len == 0 )
    {
	found = true ;
    }
    else
	i = 0;
    for(; i < len; i++) {
	nextComp = false;
	if(node->type() == MTreeNode::INTERNAL) {
	    for(j=1; j < 4 ; j++) {
		releaseNode(tmp);
		if(node->child(j) == 0x0000) {
		    // component not found
		    releaseNode(node);
		    return NULL;
		}
		tmp = getNode(node->child(j));
		if(tmp->type() != MTreeNode::INTERNAL ||
		   ((MTreeInternalNode *)tmp)->child(0) == 0x0000) {
		    // invalid tree structure
		    releaseNode(tmp);
		    releaseNode(node);
		    return NULL;
		}
		T_WORD cid(((MTreeInternalNode *)tmp)->child(0));
		releaseNode(tmp);
		tmp = getNode(cid);
		switch(tmp->type()) {
		    case MTreeNode::META:
		    {
			char *fn(((MTreeMetaNode *)tmp)->filename()) ;
			if( strcmp(fn,comps[i]) != 0 ) {
			    delete fn;
			    continue ;
			}
			delete fn;
			// found the correct not (index j)
			cid = node->child(j);
			releaseNode(node);
			node = (MTreeInternalNode *)getNode(cid);
			releaseNode(tmp);
			nextComp = true;
			if(i == len-1)
			    found = true ;
			break;
		    }
		    case MTreeNode::INTERNAL:
		    {
			releaseNode( tmp ) ;
			MTreeInternalNode* recNode = (MTreeInternalNode*)getNode( node->child(j)) ;
			cid = findRecursiveNode( recNode, comps[i] ) ;
			if( cid != 0x0000 ) 
			{
			    nextComp = true;
			    found = true ; 
			    releaseNode( node ) ;
			    node = (MTreeInternalNode*)getNode( cid ) ;
			}
			releaseNode( recNode ) ;
			break ;
		    }
		    default:
			// component not found
			releaseNode(tmp);
			releaseNode(node);
			return NULL;
		}
		if(nextComp)
	  	    break ;
	    }
	} else {
	    // error in the tree
	    releaseNode(tmp);
	    releaseNode(node);
	    return NULL;
	}
    }

    releaseNode(tmp);
    if( !found ) 
    {
	releaseNode( node ) ;
	return NULL ;
    }
    return node;
}

//
// Name:	findRecursiveNode
//
T_WORD
FileSystem::findRecursiveNode( MTreeInternalNode* node, const char* name ) 
{
    MTreeNode* tmp(NULL) ; 
    for(int j=0; j < 4 ; j++) {
	releaseNode(tmp);
	if(node->child(j) == 0x0000) {
	    // component not found
	    return 0x0000;
	}
	tmp = getNode(node->child(j));
	if(tmp->type() != MTreeNode::INTERNAL ||
	   ((MTreeInternalNode *)tmp)->child(0) == 0x0000) {
	    // invalid tree structure
	    releaseNode(tmp);
	    return 0x0000;
	}
	T_WORD cid(((MTreeInternalNode *)tmp)->child(0));
	releaseNode(tmp);
	tmp = getNode(cid);
	switch(tmp->type()) {
	    case MTreeNode::META:
		break;
	    case MTreeNode::INTERNAL:
	    default:
		// component not found
		releaseNode(tmp);
		return 0x0000;
	}
	char *fn(((MTreeMetaNode *)tmp)->filename()) ;
	if( strcmp(fn,name) != 0 ) {
	    delete fn;
	    releaseNode(tmp);
	    continue ;
	}
	delete fn;
	// found the correct not (index j)
	cid = node->child(j);
	releaseNode(tmp);
	return cid ;
    }
    return 0x0000 ;
}

//
// FileSystem::getNode
//
MTreeNode *
FileSystem::getNode(T_WORD id)
{
    return nodeCache_->getNode(id);
}

//
// FileSystem::putNode
//
void
FileSystem::putNode(MTreeNode *node,bool force,bool ensureInCache)
{
    nodeCache_->writeOutNode(node,force,ensureInCache);
}

//
// FileSystem::releaseNode
//
void
FileSystem::releaseNode(MTreeNode *node)
{
    nodeCache_->giveUpNode(node);
}

//
// FileSystem::getData
//
unsigned char *
FileSystem::getData(T_WORD id)
{
    return dataCache_->getChunk(id);
}

//
// FileSystem::putData
//
void
FileSystem::putData(T_WORD id)
{
    dataCache_->writeOutChunk(id);
}

//
// FileSystem::releaseData
//
void
FileSystem::releaseData(T_WORD id)
{
    dataCache_->giveUpChunk(id);
}

//
// FileSystem::allocateNode
//
T_WORD
FileSystem::allocateNode()
{
    return freeNodeExtents_->alloc(0,1);
}

//
// FileSystem::freeNode
//
void
FileSystem::freeNode(T_WORD id)
{
    nodeCache_->freeNode(id);
    freeNodeExtents_->free(id,1);
}

//
// FileSystem::allocateDataExtent
//
T_WORD
FileSystem::allocateDataExtent(T_WORD start, T_WORD len)
{
    return freeDataExtents_->alloc(start,len);
}

//
// FileSystem::freeDataExtent
//
void
FileSystem::freeDataExtent(T_WORD start, T_WORD len)
{
    for(T_WORD cid = start; cid < len; cid++)
	dataCache_->freeChunk(cid);
    freeDataExtents_->free(start, len);
}

//
// Name:	addChild
// 
int 
FileSystem::addChild( MTreeInternalNode* parent,
		      MTreeInternalNode* child,
		      bool split )
{

    ASSERT( parent ) ;
    ASSERT( child  ) ;
    int children = parent->nchildren() ;

    // 
    // if openChildSpot is equal to 4, that means all the child spaces
    // are full, so we need to add indirection
    //
    // <BRAINDUMP>
    //   if all the children are used up, check the last
    //   if it is a standard file, split the tree here and add a new
    //      internal indirection node
    //   if it is an internal indirection node, check the 3rd child, etc.
    //   if all are full, we crash for now (limiting ourselves to 6 files
    //      in a directory).....we should recurse and check the next level
    // </BRAINDUMP>
    //
    if( children == 4 && split ) {
	MTreeNode *curChild(NULL),*grandChild(NULL);
	for(int i=3; i >= 0; i--) {
	    curChild = getNode(parent->child(i));
	    grandChild = getNode(((MTreeInternalNode *)curChild)->child(0));

	    if(grandChild != NULL && grandChild->type() == MTreeNode::META) {
		MTreeInternalNode *newNode(new MTreeInternalNode(allocateNode()));
		newNode->child(0,curChild->id());
		newNode->child(1,child->id());
		parent->child(i,newNode->id());
		releaseNode(curChild);
		releaseNode(grandChild);
		putNode(newNode);
		return 0;
	    } else if(grandChild != NULL) {
		int ret(addChild((MTreeInternalNode *)curChild,child,false));
		if(ret == 0) {
		    releaseNode(grandChild);
		    releaseNode(curChild);
		    return 0;
		}
	    }
	    releaseNode(curChild);
	    releaseNode(grandChild);
	}
    } else if(children < 4) {
	parent->child( children, child->id() ) ; 
	putNode(parent);
	return 0;
    }
    return -1;
}

//
// Name:	removeChild
// 
int 
FileSystem::removeChild( MTreeInternalNode* parent,
		         MTreeInternalNode* child,
		         bool 		    combine   )
{
    ASSERT(parent);
    ASSERT(child);

    for(size_t i = 0; i < 4; i++) {
	if(parent->child(i) == child->id()) {
	    // pack the children
	    //
	    for(size_t j = i; j < 3; j++)
		parent->child(j,parent->child(j+1));
	    parent->child(3,0x0000);  // make sure the last child is not present now

	    return 0;
	}
    }
    return -1;
}

void
FileSystem::printTree(T_WORD id, int Level)
{
    if(id == 1) {
	freeNodeExtents_->printTree(0,Level);
	return;
    } else if(id == 2) {
	freeDataExtents_->printTree(0,Level);
	return;
    }

    int i;
    MTreeNode *node(getNode(id));

    for(i=0;i < Level; i++)
	DEBUG('u'," ");
    DEBUG('u',"0x%04x: ",id);
    if(node == NULL) {
	DEBUG('u',"NOT FOUND\n");
	return;
    }
    switch(node->type()) {
	case MTreeNode::RAW:
	    DEBUG('u',"RAW");
	    for(i = 0; i < 31; i++) {
		if(i % 2 == 0)
		    DEBUG('u'," ");
		DEBUG('u',"%02x",((MTreeRawNode *)node)->data[i]);
	    }
	    DEBUG('u',"\n");
	    releaseNode(node);
	    break;
	case MTreeNode::INTERNAL: {
	    DEBUG('u',"INTERNAL");
	    for(i = 0; i < 4; i++) {
		DEBUG('u'," 0x%04x",((MTreeInternalNode *)node)->child(i));
	    }
	    DEBUG('u',"\n");
	    T_WORD c[4] = { ((MTreeInternalNode *)node)->child(0),
			    ((MTreeInternalNode *)node)->child(1),
			    ((MTreeInternalNode *)node)->child(2),
			    ((MTreeInternalNode *)node)->child(3)};
	    releaseNode(node);
	    for(i = 0; i < 4; i++) {
		if(c[i] == 0x0000) {
		    for(int j=0; j < Level+1; j++)
			DEBUG('u'," ");
		    DEBUG('u',"NO CHILD\n");
		} else {
		    printTree(c[i],Level+1);
		}
	    }
	    break;
	} case MTreeNode::META: {
	    DEBUG('u',"META 0x%04x ",((MTreeMetaNode *)node)->size());
	    if(((MTreeMetaNode *)node)->directory()) DEBUG('u',"d");
	    else                                     DEBUG('u',"-");

	    int perms(((MTreeMetaNode *)node)->getmod());
	    if(perms & 0x4) DEBUG('u',"r");
	    else            DEBUG('u',"-");
	    if(perms & 0x2) DEBUG('u',"w");
	    else            DEBUG('u',"-");
	    if(perms & 0x1) DEBUG('u',"x");
	    else            DEBUG('u',"-");
	    for(i = 0; i < 4; i++) {
		DEBUG('u'," 0x%04x",((MTreeMetaNode *)node)->filenameNode(i));
	    }
	    DEBUG('u',"\n");
	    T_WORD c[4] = { ((MTreeMetaNode *)node)->filenameNode(0),
			    ((MTreeMetaNode *)node)->filenameNode(1),
			    ((MTreeMetaNode *)node)->filenameNode(2),
			    ((MTreeMetaNode *)node)->filenameNode(3)};
	    releaseNode(node);
	    for(i = 0; i < 4; i++) {
		if(c[i] == 0x0000) {
		    for(int j=0; j < Level+1; j++)
			DEBUG('u'," ");
		    DEBUG('u',"NO CHILD\n");
		} else {
		    printTree(c[i],Level+1);
		}
	    }
	    break;
	} case MTreeNode::DATA:
	    DEBUG('u',"DATA 0x%04x",((MTreeDataNode *)node)->len());
	    for(i = 0; i < 6; i += 2) {
		DEBUG('u'," 0x%04x/0x%04x",((MTreeDataNode *)node)->data(i),((MTreeDataNode *)node)->data(i+1));
	    }
	    DEBUG('u',"\n");
	    releaseNode(node);
	    break;
	default:
	    DEBUG('u',"UNKNOWN TYPE[0x%x]\n",node->type());
	    releaseNode(node);
    }
}

//
// Name:	deleteFile
//
void
FileSystem::deleteFile( MTreeInternalNode* file ) 
{
    //FEATURE: Doesn't delete indirect data pointers
    freeNode( file->child(0) ) ; // free the metaNode
    for( size_t i = 1 ; i < file->nchildren() ; i++ )
    {
        MTreeDataNode* data = (MTreeDataNode*)getNode( file->child( i ) ) ;
	if( data ) 
	{
	    for( int j = 0 ; j < 6 ; j += 2)
	    {
		if( data->data( j ) != 0x0000 )
		{
		    freeDataExtent( data->data( j ), data->data( j + 1 ) ) ;
		}
	    }
	    freeNode( data->id() ) ;
	    releaseNode( data ) ;
	}
    }
}
