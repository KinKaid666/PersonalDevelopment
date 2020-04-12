#include <limits.h>
#include <stdio.h>
#include <stdlib.h>

int main( int ac, char **av )
{
    printf( "INT_MIN      = %d\n"
            "INT_MIN * -1 = %d\n", INT_MIN, INT_MIN * -1 ) ;
    return EXIT_SUCCESS ;
}
