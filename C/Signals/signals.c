#include <signal.h>
#include <stdio.h>
#include <stdlib.h>

int sig_int  = 0 ;
int sig_quit = 0 ;
int done = 0 ;

void handler( int sig )
{
    /*
    ** Reset the signal handler
    */
    signal( sig, handler ) ;
    psignal( sig, "cause signal" ) ;
    if( sig == SIGINT )
    {
        if( --sig_int < 0 )
            exit( 0 ) ;
    }
    else
    {
        if( (sig_quit-- < 0) || (sig_int != 0) )
            exit( 0 ) ;
        else if( sig_quit == 0 )
            done = 1 ;
    }
}

int main( int ac, char **av )
{
    if( ac != 3 )
    {
        fprintf(stderr, "usage: %s <number ctrl-C> <number ctrl-\\>\n", av[0]) ;
        exit( EXIT_FAILURE ) ;
    }
    sig_int  = atoi( av[1] ) ;
    sig_quit = atoi( av[2] ) ;
    signal( SIGINT,  handler ) ;        /* control-c */
    signal( SIGQUIT, handler ) ;        /* control-\ */
    while( !done )
    { ; }
    return EXIT_SUCCESS ;
}
