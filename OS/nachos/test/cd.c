// cd.c
// change the current working directory

#include "syscall.h"
#include "argdefs.h"
#include "lib.h"

// Helper function to get string size
int getSize( char string[]);

int main (int    argc,
          char   *argv )
{
  int nameLen;

  if(argc != 2)
  {
    Write( "Usage: \"cd (new directory)\"\n",28,1);
    return -1;
  }

  nameLen = N_strlen(argv + MAX_ARG_LENGTH);
  Chdir(argv + MAX_ARG_LENGTH , nameLen);

  return 0;
}
