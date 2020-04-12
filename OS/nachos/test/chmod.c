// chmod.c
// Modifies the attributes of a file

#include "syscall.h"
#include "argdefs.h"
#include "lib.h"

unsigned int findPermissions(char *string);

// int Chmod( char* name, int nameSize, T_BYTE perms ) ;

int
main( int    argc,
      char   *argv )
{
  int nameLen;
  unsigned int perms = 0;

  if(argc != 3) {
    Write( "Usage: \"chmod (permissions) filename\"\n",40,1);
  }

  nameLen  = N_strlen( argv + (MAX_ARG_LENGTH * 2) );
  perms    = findPermissions( argv + (MAX_ARG_LENGTH) );

  if(perms >= 0) {
    if( Chmod( argv + (MAX_ARG_LENGTH * 2), nameLen, perms) ) {
      Write("File not found\n",15,1);
    }
  }

  Exit( 0 );
}

unsigned int findPermissions( char *string )
{
  unsigned int perms = 0;
  int i = 0;
  int argNum;
  argNum = N_strlen(string);

  for( i = 0; i < argNum; ++i)
  {
    if(string[i] == 'r')
      perms |= 4;
    else if(string[i] == 'w')
      perms |= 2;
    else if(string[i] == 'x')
      perms |= 1;
    else {
      Write("Valid permissions are 'r' 'w' 'x'\n",21,1);
      perms = -1;
      break;
    }
  }
  return perms;
}
