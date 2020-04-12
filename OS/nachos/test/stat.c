/* stat.c
 *	Return info about a file.
 */
#include "argdefs.h"
#include "syscall.h"
#include "filestat.h"
#define FALSE 0
#define TRUE 1

void
intToStringLZ(unsigned int val, char *str, char ndigits)
{
    char digit;

    for(digit=ndigits; digit > 0; digit--) {
	str[digit-1] = (val % 10) + '0';
	val = val / 10;
    }
    str[ndigits] = '\0';
}

void
intToString(unsigned int val, char *str, char ndigits)
{
    char digit;
    char blank = 1;

    intToStringLZ(val,str,ndigits);

    for(digit = 0; digit < ndigits - 1; digit++) {
	if(str[digit] == '0') {
	    if(blank == TRUE) str[digit] = ' ';
	} else {
	    blank = FALSE;
	}
    }
}

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
    int strLen ;
    char buf[ sizeof( Stat_t ) + strLen ] ;
    
    if ( argc != 2 ) {
        Write( "usage: stat <filename>\n", 24, 1 );
	Exit( 1 );
    } // if

    strLen = sizeOfStr( argv + MAX_ARG_LENGTH ) ;

    Stat_t* fileStat = (Stat_t)buf ;
    if( Stat( (Stat_t*)buf, argv + MAX_ARG_LENGTH, strLen ) == 0 )
    {
	
    }
    else
    {
	Write( "file not found\n",15,1 ) ; 
    }

    Exit( 0 );
} // main
