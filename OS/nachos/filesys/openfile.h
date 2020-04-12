// File:         $Id: openfile.h,v 3.8 2001/11/13 04:02:44 trc2876 Exp $
// Author:       p544-01b
// Contributors: {}
// Description:  This is the cache for our file system.
// Revisions:
// $Log: openfile.h,v $
// Revision 3.8  2001/11/13 04:02:44  trc2876
// Hint() added to OpenFile
//
// Revision 3.7  2001/11/12 21:33:04  trc2876
// OpenFile::Read written and compiles...code reviewed but untested
//
// Revision 3.6  2001/11/12 01:45:10  trc2876
// broken coke...but it compiles
//
// Revision 3.5  2001/11/11 23:38:27  p544-01b
// Added release node code
//
// Revision 3.4  2001/11/11 22:01:58  trc2876
// in progress code review for OpenFile
//
// Revision 3.3  2001/11/11 00:55:07  p544-01b
// added GetPhysAddress function
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
// Revision 2.1  2001/11/02 21:43:00  etf2954
// First time for everything
//

#ifndef OPENFILE_H
#define OPENFILE_H

#include "copyright.h"
#include "utility.h"

#ifdef FILESYS_STUB			// Temporarily implement calls to 
					// Nachos file system as calls to UNIX!
					// See definitions listed under #else
class OpenFile {
  public:
    OpenFile(int f) { file = f; currentOffset = 0; }	// open the file
    ~OpenFile() { Close(file); }			// close the file

    int ReadAt(char *into, int numBytes, int position) { 
    		Lseek(file, position, 0); 
		return ReadPartial(file, into, numBytes); 
		}	
    int WriteAt(char *from, int numBytes, int position) { 
    		Lseek(file, position, 0); 
		WriteFile(file, from, numBytes); 
		return numBytes;
		}	
    int Read(char *into, int numBytes) {
		int numRead = ReadAt(into, numBytes, currentOffset); 
		currentOffset += numRead;
		return numRead;
    		}
    int Write(char *from, int numBytes) {
		int numWritten = WriteAt(from, numBytes, currentOffset); 
		currentOffset += numWritten;
		return numWritten;
		}

    int Length() { Lseek(file, 0, 2); return Tell(file); }
    
  private:
    int file;
    int currentOffset;
};

#else // FILESYS

#include "mtree.h"

class OpenFile {
  public:
    OpenFile(MTreeInternalNode* fNode);	
                             // Open a file whose header is located
	         	     // at "sector" on the disk
    ~OpenFile();	     // Close the file

    void Seek(int position); 		// Set the position from which to 
					// start reading/writing -- UNIX lseek

    int Read(char *into, int numBytes); // Read/write bytes from the file,
					// starting at the implicit position.
					// Return the # actually read/written,
					// and increment position in file.
    int Write(char *from, int numBytes);

    int ReadAt(char *into, int numBytes, int position);
    					// Read/write bytes from the file,
					// bypassing the implicit position.
    int WriteAt(char *from, int numBytes, int position);

    void Hint(size_t len);              // tell the file you are going to
					// write len bytes at the current
					// seekPosition_
					// this helps with efficient extent
					// allocation

    int Length(); 			// Return the number of bytes in the
					// file (this interface is simpler 
					// than the UNIX idiom -- lseek to 
					// end of file, tell, lseek back    
  private:
    T_WORD fNodeID_;			// Node ID for this file 
    unsigned int seekPosition_;		// Current position within the file
};

#endif // FILESYS

#endif // OPENFILE_H
