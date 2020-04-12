/* mkdir.c
 *	Make directories.
 *	
 *
 * 	NOTE: for some reason, user programs with global data structures 
 *	sometimes haven't worked in the Nachos environment.  So be careful
 *	out there!  One option is to allocate data structures as 
 * 	automatics within a procedure, but if you do this, you have to
 *	be careful to allocate a big enough stack to hold the automatics!
 */

#include "argdefs.h"
#include "syscall.h"
#define FALSE 0
#define TRUE 1

int
sizeOfStr( char* str ) {
    int index;
    for ( index = 0; str[index] != 0; index++ ) {}
    return index;
} // sizeOfStr

int
main( int    argc,
      char  *argv )
{
    int index;
    int errcode;
    int dirSize;
    char* dirName;

    if ( argc < 2 ) {
        Write( "Usage: mkdir dir1 [dirN...]\n", 29, 1 );
	Exit( 1 );
    } // if

    for ( index = 1; index < argc; index++ ) {
	dirName = argv + index * MAX_ARG_LENGTH;
	dirSize = sizeOfStr( dirName );
	errcode = Mkdir( dirName, dirSize );

	if ( errcode == -1 ) {
            Write( "Directory '", 11, 1 );
	    Write( dirName, sizeOfStr( dirName ), 1 );
	    Write( "' Already Exists\n", 17, 1 );
	} 
    
    } // loop

    Exit( 0 );
} // main
