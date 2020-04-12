#include "argdefs.h"
#include "syscall.h"

#define HUGE	8192
int bigarray[HUGE];

int
main( int    argc,
      char  *argv )
{
    int i;
    for( i = 0; i < HUGE; i++) 
    {
        bigarray[i] = i ;
	if( i % 128 ) Write( ".",1,1) ;
    }
    Write( "\n",1,1 ) ;
    Exit( 0 );
}
