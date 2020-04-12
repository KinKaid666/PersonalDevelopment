#include <stdio.h>
#include <stdlib.h>

void movedisc( int from, int to )
{
    printf( "Move %d->%d\n", from, to ) ;
}

void movetowers( int pegs, int from, int to, int temp )
{
    if( pegs > 0 )
    {
        movetowers( pegs-1, from, temp, to ) ;
        movedisc( from, to ) ;
        movetowers( pegs-1, temp, to, from ) ;
    }
}

int main( int ac, char **av )
{
    if( ac != 2 )
    {
        fprintf( stderr, "usage: hanoi <number of discs>\n" ) ;
        exit( EXIT_FAILURE ) ;
    }

    movetowers( atoi( av[1] ), 1, 3, 2 ) ;
    return EXIT_SUCCESS ;
}

