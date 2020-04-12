#include "argdefs.h"
#include "syscall.h"

int
main( int    argc,
      char  *argv )
{
    int i;
    for( i = 1 ; i < argc ; i++ )
    {
        Write( argv + i * MAX_ARG_LENGTH, MAX_ARG_LENGTH, 1 ) ; 
	Write( " ", 1, 1 );
    }
    Write( "\n", 1, 1 );
    Exit( 0 );
}
