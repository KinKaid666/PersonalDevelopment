// Copy a file to a new location

#include "syscall.h"
#include "argdefs.h"
#include "lib.h"

// Helper function to get string size
int getSize( char string[]);

int
main( int    argc,
      char   *argv )
{ 
  int nameLen;
  int newNameLen;
  int fileId;
  int newFileId;
  struct Stat_t fileInfo;
  char frame[128];
  int bytesToRead;
  
  if(argc != 3)
  {
    Write( "Usage: \"cp (oldLocation) (newLocation)\"\n",40,1);
  }

  // Get filename lengths
  nameLen    = N_strlen( argv + (MAX_ARG_LENGTH) );
  newNameLen = N_strlen( argv + (MAX_ARG_LENGTH * 2) );

  // Open File to copy from
  fileId = Open(argv + (MAX_ARG_LENGTH), nameLen);

  // Create and open File to copy to
  Create(argv + (MAX_ARG_LENGTH * 2), newNameLen);
  newFileId = Open(argv + (MAX_ARG_LENGTH * 2), newNameLen);

  Stat(&fileInfo, argv + (MAX_ARG_LENGTH), nameLen);

  // Need to read in the entire file
  bytesToRead = fileInfo.size_;

  // Preform copying
  while( bytesToRead > 0)
  {
    if(bytesToRead >= 128)
    {
      Read( frame, 128, fileId);
      Write( frame, 128, newFileId);
      bytesToRead = bytesToRead - 128;
    } 
    else
    {
      Read( frame, bytesToRead, fileId);
      Write( frame, bytesToRead, newFileId);
      bytesToRead = 0;
    }
  }

  // close the files
  Close(fileId);
  Close(newFileId);

  Exit( 0 );
}
