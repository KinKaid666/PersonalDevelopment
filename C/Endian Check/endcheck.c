#include <stdio.h>
#include <stdlib.h>

int main( int ac, char **av )
{
    union
    {
        short   s ;
        char    c[sizeof(short)] ;
    } un ;

    un.s = 0x0102 ;
    if( un.c[0] == 1 && un.c[1] == 2 )
        printf( "big-endian\n" ) ;
    else 
        printf( "little-endian\n" ) ;
}
