// rm.c
// remove the file

#include "syscall.h"
#include "lib.h"


int
main( int   argc,
      char *argv )
{
    int nameLen;

    if(argc != 2)
    {
	Write("Usage: \"rm (file)\"\n",19,1);
	Exit( 0 ) ;
    }

    nameLen = N_strlen(argv + MAX_ARG_LENGTH);

    if(Unlink( argv + MAX_ARG_LENGTH, nameLen) == -1)
	Write("File not found\n",15,1);

    Exit( 0 );
}
