#include "argdefs.h"
#include "syscall.h"

int bigarray[2048];

int
main( int    argc,
      char  *argv )
{
    int i;
    for( i = 0; i < 2048; i++) 
    {
        bigarray[i] = i ;
	if( i % 128 == 0 )
	    Write( ".",1,1) ;
    }
    Write( "\n",1,1 ) ;
    for( i = 0; i < 2048; i++) 
    {
        bigarray[i] = i ;
	if( i % 128 == 0 )
	    Write( ".",1,1) ;
    }
    Write( "\n",1,1 ) ;
    Exit( 0 );
}
