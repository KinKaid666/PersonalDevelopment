/* shell.c
 *	The shell.
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

void
InitChArray( char* ary, char val, int length ) {
    int index;
    for ( index = 0; index < length; index++ ) ary[index] = val;
} // InitChArray

void
InitIntArray( int* ary, int val, int length ) {
    int index;
    for ( index = 0; index < length; index++ ) ary[index] = val;
} // InitChArray

void
Parse(char *buf, char** argv)
{
    int i,argc=0;

    argv[argc++] = buf;
    for(i = 1; i < (MAX_ARG_LENGTH * MAX_ARGS); i++) {
	if(buf[i] == ' ' || buf[i] == '\t') {
	    argv[argc++] = buf + i + 1;
	    buf[i] = '\0';
	}
    }
} // Parse


void
ParseAndExec( char* buf )
{
    char *argAddr[MAX_ARGS];
    char argv[MAX_ARG_LENGTH * MAX_ARGS];
    char *curArg;
    int argIndex, chIndex;
    int argc = 0;
    int done = FALSE;
    int i;
    int pid ;
    int join = TRUE ;

    InitIntArray( (int)argAddr, 0, MAX_ARGS );
    InitChArray( argv, 0, (MAX_ARG_LENGTH * MAX_ARGS) );
    Parse( buf, argAddr );
    for ( argIndex = 0; argIndex < MAX_ARGS && !done; argIndex++ ) {
        if ( (curArg = argAddr[argIndex]) != 0 ) {
	    chIndex = 0;
	    while ( (
	    	     argv[ chIndex + argIndex*MAX_ARG_LENGTH ] =
		     curArg[ chIndex ]
		     ) != 0 )
	        chIndex++;
	    argc++;
	} // if
	else done = TRUE;
    } // loop

    if ( argc > 0 ) {

        // Check to see if the command should be run in the
	//  background
	if( (argv + (argc-1)*MAX_ARG_LENGTH)[0] == '&' )
	{
	    join = FALSE ;
	    argc -= 1 ;
	}  

	pid = Exec( argc, argv ) ;
        if ( pid == -1 ) {
	    Write( argv, MAX_ARG_LENGTH, 1 );
	    Write( ": command not found\n", 21, 1 );
	} else if ( pid == -2 )
	{
	    Write( "Sorry, out of memory\n", 22, 1 ) ;
	} else if ( join )
	{
	    Join( pid ) ;
	} 
    } // if
} // ParseAndExec


int
main()
{
    const char *BANNER = "Welcome to the Kitchen Sink Shell (kssh) v1.3\n";
    const char* PROMPT = "kssh> ";
    char input[ (MAX_ARG_LENGTH * MAX_ARGS) ];
    int i;

    Write( BANNER, 46, 1);
    while ( 1 ) {
        Write( PROMPT, 6, 1 );
	Read( input, (MAX_ARG_LENGTH * MAX_ARGS), 0 );
	if ( input[0] != 0 ) ParseAndExec( input );
	InitChArray( input, 0, (MAX_ARG_LENGTH * MAX_ARGS) );
    } // loop

} // main
