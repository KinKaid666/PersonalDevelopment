/* ps.c
 *	Return info about all processes.
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
#define MAX_PROC_COUNT 4
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
main( int    argc,
      char  *argv )
{
    int index;
    int numProcs = 0;
    int procSize = 36;
    char aNum[ 10 ];
    char buf[ MAX_PROC_COUNT * procSize ];
    procInfo_t* curInfo = (procInfo_t *)buf;
    

    if ( argc != 1 ) {
        Write( "ps: requires no arguments\n", 26, 1 );
	Exit( 1 );
    } // if
    
    for ( index = 0; index < MAX_PROC_COUNT * procSize; index++ )
        buf[ index ] = 0;
    
    numProcs = ProcInfo( (procInfo_t *)buf );

    Write( "Name            ", 16, 1 );
    Write( "       PID      PPID   Threads     Joins  Children\n", 51, 1 );

    for ( index = 0; index < numProcs; index++ ) {

	Write( curInfo->pname_, 16, 1 );
        intToString( curInfo->pid_, aNum, 10 );
        Write( aNum, 10, 1 );
	intToString( curInfo->ppid_, aNum, 10 );
	Write( aNum, 10, 1 );
	intToString( curInfo->threadCount_, aNum, 10 );
	Write( aNum, 10, 1 );
	intToString( curInfo->joinerCount_, aNum, 10 );
	Write( aNum, 10, 1 );
	intToString( curInfo->childCount_, aNum, 10 );
	Write( aNum, 10, 1 );

        Write( "\n", 1, 1 );
        curInfo++;
    } // loop

    Exit( 0 );
} // main
