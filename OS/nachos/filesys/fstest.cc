// fstest.cc 
//	Simple test routines for the file system.  
//
//	We implement:
//	   Copy -- copy a file from UNIX to Nachos
//	   Print -- cat the contents of a Nachos file 
//	   Perftest -- a stress test for the Nachos file system
//		read and write a really large file in tiny chunks
//		(won't work on baseline system!)
//
// Copyright (c) 1992-1993 The Regents of the University of California.
// All rights reserved.  See copyright.h for copyright notice and limitation 
// of liability and disclaimer of warranty provisions.

// $Id: fstest.cc,v 3.12 2001/11/14 19:04:01 trc2876 Exp $

// $Log: fstest.cc,v $
// Revision 3.12  2001/11/14 19:04:01  trc2876
// almost fixed problem with extent allocation
// can rm now
//
// Revision 3.11  2001/11/14 17:20:59  eae8264
// Added empty cat and cp functions.  We're changing
// focus to User land stuff now.
//
// Revision 3.10  2001/11/14 17:13:49  etf2954
// wrote getFileDir correctly
//
// Revision 3.9  2001/11/14 16:25:07  trc2876
// *** empty log message ***
//
// Revision 3.8  2001/11/14 04:19:41  trc2876
// fixed scanf's in ksink
//
// Revision 3.7  2001/11/14 04:13:51  eae8264
// *** empty log message ***
//
// Revision 3.6  2001/11/14 04:10:43  trc2876
// forced commit
//
// Revision 3.5  2001/11/14 03:29:23  trc2876
// new ls functionality
//
// Revision 3.4  2001/11/14 02:18:08  trc2876
// doc changes and ls() prints always now
//
// Revision 3.3  2001/11/14 02:15:28  trc2876
// ls works (not tested from userland)
// nachos -l -d u    dumps the filesystem tree
// nachos -ld -d L   does an ls
//
// Revision 3.2  2001/11/13 04:02:43  trc2876
// Hint() added to OpenFile
//
// Revision 3.1  2001/11/12 01:45:10  trc2876
// broken coke...but it compiles
//
// Revision 3.0  2001/11/04 19:46:28  trc2876
// force to 3.0
//
// Revision 2.1  2001/11/02 21:42:59  etf2954
// First time for everything
//

#include "copyright.h"

#include "utility.h"
#include "filesys.h"
#include "system.h"
#include "thread.h"
#include "disk.h"
#include "stats.h"
#include "iostream.h"

#define TransferSize 	10 	// make it small, just to be difficult

void ls( char* ) ;
void cp( char*, char* ) ;
void cat( char* ) ;

void
KSinkCmdLine()
{
    char in1[ 64 ] ;
    char in2[ 64 ] ;
    char in3[ 64 ] ;
    in1[ 0 ] = 0 ;
    while ( 1 )
    {
        printf( "Real Kitchen Sink> " ) ;
	scanf( "%s", in1 ) ;
	if ( in1[0] == 'q' ) return;
	if ( !strcmp( in1, "mkdir" ) )
	{
	    scanf( "%s", in2 ) ;
	    fileSystem->mkdir( in2 ) ;
	}
	else if ( !strcmp( in1, "touch" ) )
	{
	    scanf( "%s", in2 ) ;
	    fileSystem->Create( in2 ) ;
	}
	else if ( !strcmp( in1, "ls" ) )
	{
	    scanf( "%s", in2 ) ;
	    ls( in2 ) ;
	}
	else if ( !strcmp( in1, "rm" ) )
	{
	    scanf( "%s", in2 ) ;
	    fileSystem->Remove( in2 );
	}
	else if ( !strcmp( in1, "dump" ) )
	{
	    int nodeID;
	    scanf( "%i", &nodeID );
	    fileSystem->printTree(nodeID,0);
	}
	else if ( !strcmp( in1, "cp" ) )
	{
	    scanf( "%s", in2 ) ;
	    scanf( "%s", in3 ) ;
	    cp( in2, in3 ) ;
	}
	else if ( !strcmp( in1, "cat" ) )
	{
	    scanf( "%s", in2 ) ;
	    cat( in2 ) ;
	}
	else 
	{
	    printf( "%s: invalid command\n", in1 ) ;
	    printf( "Commands available are:\n"
		    "mkdir <dir>\n"
		    "touch <file>\n"
		    "ls <path>\n"
		    "dump\n"
		   ) ;
	}
    }
} // KSinkCmdLine

void
cp( char* src, char* tgt )
{
    ASSERT( FALSE ) ;
}

void
cat( char* fi )
{
    ASSERT( FALSE ) ;
}

void
ls(char *path)
{
    char **result;
    int len;

    if((len = fileSystem->ListDir(path,&result)) == 0) {
	printf("total 0\n");
	return;
    }
    printf("total %i\n",len);
    for(int i=0; i < len; i++) {
	printf("%s\n",result[i]);
    }
}

//----------------------------------------------------------------------
// Copy
// 	Copy the contents of the UNIX file "from" to the Nachos file "to"
//----------------------------------------------------------------------

void
Copy(char *from, char *to)
{
    FILE *fp;
    OpenFile* openFile;
    int amountRead, fileLength;
    char *buffer;

// Open UNIX file
    if ((fp = fopen(from, "r")) == NULL) {	 
	printf("Copy: couldn't open input file %s\n", from);
	return;
    }

// Figure out length of UNIX file
    fseek(fp, 0, 2);		
    fileLength = ftell(fp);
    fseek(fp, 0, 0);

// Create a Nachos file of the same length
    DEBUG('f', "Copying file %s, size %d, to file %s\n", from, fileLength, to);
    if (fileSystem->Create(to, fileLength)) {	 // Create Nachos file
	printf("Copy: couldn't create output file %s\n", to);
	fclose(fp);
	return;
    }
    
    openFile = fileSystem->Open(to);
    ASSERT(openFile != NULL);
    
    openFile->Hint(fileLength);
// Copy the data in TransferSize chunks
    buffer = new char[TransferSize];
    while ((amountRead = fread(buffer, sizeof(char), TransferSize, fp)) > 0)
	openFile->Write(buffer, amountRead);	
    delete [] buffer;

// Close the UNIX and the Nachos files
    delete openFile;
    fclose(fp);
}

//----------------------------------------------------------------------
// Print
// 	Print the contents of the Nachos file "name".
//----------------------------------------------------------------------

void
Print(char *name)
{
    OpenFile *openFile;    
    int i, amountRead;
    char *buffer;

    if ((openFile = fileSystem->Open(name)) == NULL) {
	printf("Print: unable to open file %s\n", name);
	return;
    }
    
    buffer = new char[TransferSize];
    while ((amountRead = openFile->Read(buffer, TransferSize)) > 0)
	for (i = 0; i < amountRead; i++)
	    printf("%c", buffer[i]);
    delete [] buffer;

    delete openFile;		// close the Nachos file
    return;
}

//----------------------------------------------------------------------
// PerformanceTest
// 	Stress the Nachos file system by creating a large file, writing
//	it out a bit at a time, reading it back a bit at a time, and then
//	deleting the file.
//
//	Implemented as three separate routines:
//	  FileWrite -- write the file
//	  FileRead -- read the file
//	  PerformanceTest -- overall control, and print out performance #'s
//----------------------------------------------------------------------

#define FileName 	"TestFile"
#define Contents 	"1234567890"
#define ContentSize 	strlen(Contents)
#define FileSize 	((int)(ContentSize * 5000))

static void 
FileWrite()
{
    OpenFile *openFile;    
    int i, numBytes;

    printf("Sequential write of %d byte file, in %d byte chunks\n", 
	FileSize, ContentSize);
    if (!fileSystem->Create(FileName, 0)) {
      printf("Perf test: can't create %s\n", FileName);
      return;
    }
    openFile = fileSystem->Open(FileName);
    if (openFile == NULL) {
	printf("Perf test: unable to open %s\n", FileName);
	return;
    }
    for (i = 0; i < FileSize; i += ContentSize) {
        numBytes = openFile->Write(Contents, ContentSize);
	if (numBytes < 10) {
	    printf("Perf test: unable to write %s\n", FileName);
	    delete openFile;
	    return;
	}
    }
    delete openFile;	// close file
}

static void 
FileRead()
{
    OpenFile *openFile;    
    char *buffer = new char[ContentSize];
    int i, numBytes;

    printf("Sequential read of %d byte file, in %d byte chunks\n", 
	FileSize, ContentSize);

    if ((openFile = fileSystem->Open(FileName)) == NULL) {
	printf("Perf test: unable to open file %s\n", FileName);
	delete [] buffer;
	return;
    }
    for (i = 0; i < FileSize; i += ContentSize) {
        numBytes = openFile->Read(buffer, ContentSize);
	if ((numBytes < 10) || strncmp(buffer, Contents, ContentSize)) {
	    printf("Perf test: unable to read %s\n", FileName);
	    delete openFile;
	    delete [] buffer;
	    return;
	}
    }
    delete [] buffer;
    delete openFile;	// close file
}

void
PerformanceTest()
{
    printf("Starting file system performance test:\n");
    stats->Print();
    FileWrite();
    FileRead();
    if (!fileSystem->Remove(FileName)) {
      printf("Perf test: unable to remove %s\n", FileName);
      return;
    }
    stats->Print();
}

