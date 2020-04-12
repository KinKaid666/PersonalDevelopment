/* cat.c
 *	File cat utility.
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
#define BUF_SIZE 80

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
    int bytesLastRead = 0;
    int fileID;
    int fileSize;
    char* fileName;
    char buffer[BUF_SIZE];

    if ( argc != 2 ) {
        Write( "Usage: cat <fileName>\n", 22, 1 );
	Exit( 1 );
    } // if

    fileName = argv + MAX_ARG_LENGTH;
    fileSize = sizeOfStr( fileName );
    fileID = Open( fileName, fileSize + 1 );
    
    if ( fileID == 1 ) {
        Write( "Invalid file name\n", 19, 1 );
	Exit( 1 );
    } // if
    
    bytesLastRead = Read( buffer, BUF_SIZE, fileID );
    while ( bytesLastRead > 0 ) {
	Write( buffer, bytesLastRead, 1 );
        bytesLastRead = Read( buffer, BUF_SIZE, fileID );
    } // loop

    Close( fileID );

    Exit( 0 );
} // main
