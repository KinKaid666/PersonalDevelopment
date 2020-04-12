// mv.c
// move a file to a new location

#include "syscall.h"
#include "lib.h"

// Simple Function to get length of string
int getSize( char string[]);

int
main( int    argc,
      char  **argv )
{
  int fileLen;
  int newFileLen;

  if(argc != 3)
  {
    Write( "Usage: \"mv (oldLocation) (newLocation)\"\n",40,1);
  }
  else 
  {
    // Attempt to move file
    if( Move(argv[1],fileLen,argv[2],newFileLen) == -1)
    {
      Write("File not found\n",15,1);
    }
  }
}

int getSize( char string[])
{
  int i = 0;
  while( string[i] != '\0' && string[i] != '\n')
  {
    //cerr << "i: " << i << " " << string[i] << endl; 
    i++;
  }
  
  return i;
}


