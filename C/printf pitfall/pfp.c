#include <stdio.h>
#include <stdlib.h>

int main( int ac, char **av )
{
    long long llu = 1235467890123456789ULL ;

    printf( "bad :  d = %d llu = %llu\n", llu, llu ) ;
    printf( "good:  d = %d llu = %llu\n", (int)llu, llu ) ;
    return EXIT_SUCCESS ;
} 
