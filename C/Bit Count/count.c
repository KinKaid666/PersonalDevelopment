/* gcc -g -D WHICH=<which function>
          -D TIMES=<number of interations> count.c -o count */
#include <stdlib.h>
#include <stdio.h>

int count_bits1( unsigned int x )
{ int result = 0 ;
    int index ;
    for( index = 0 ; index < (sizeof(x)*8) ; index++ )
    {
        if( (1 << index) & x )
            result++ ;
    }
    return result ;
}

/*
** Make sure the argument is unsigned or you'll be left shifting in
** a bit that is equivalent to the sign bit and not a zero.
*/
int count_bits2( unsigned int x )
{
    int result = 0 ;
    while( x != 0 )
    {
        if( x % 2 ) result++ ;
        x >>= 1 ;
    }
    return result ;
}

int count_bits3( unsigned int x )
{
    int result = 0 ;
    if( x & 0x1        ) result++ ;
    if( x & 0x2        ) result++ ;
    if( x & 0x4        ) result++ ;
    if( x & 0x8        ) result++ ;
    if( x & 0x10       ) result++ ;
    if( x & 0x20       ) result++ ;
    if( x & 0x40       ) result++ ;
    if( x & 0x80       ) result++ ;
    if( x & 0x100      ) result++ ;
    if( x & 0x200      ) result++ ;
    if( x & 0x400      ) result++ ;
    if( x & 0x800      ) result++ ;
    if( x & 0x1000     ) result++ ;
    if( x & 0x2000     ) result++ ;
    if( x & 0x4000     ) result++ ;
    if( x & 0x8000     ) result++ ;
    if( x & 0x10000    ) result++ ;
    if( x & 0x20000    ) result++ ;
    if( x & 0x40000    ) result++ ;
    if( x & 0x80000    ) result++ ;
    if( x & 0x100000   ) result++ ;
    if( x & 0x200000   ) result++ ;
    if( x & 0x400000   ) result++ ;
    if( x & 0x800000   ) result++ ;
    if( x & 0x1000000  ) result++ ;
    if( x & 0x2000000  ) result++ ;
    if( x & 0x4000000  ) result++ ;
    if( x & 0x8000000  ) result++ ;
    if( x & 0x10000000 ) result++ ;
    if( x & 0x20000000 ) result++ ;
    if( x & 0x40000000 ) result++ ;
    if( x & 0x80000000 ) result++ ;
    return result ;
}

int main( int ac, char **av )
{
    int i ;
    int value = 0 ;

    if( ac > 1 )
    {
        value = atoi(av[1]) ;
    }
    else
    {
        fprintf( stderr, "usage: %s <which function>\n", av[0]) ;
        exit(1) ;
    }

    printf( "Running timing test for count_bits%d( ... )\n", WHICH ) ;
    for( i=0 ; i < TIMES ; i++ )
    {
        int count ;
        switch( WHICH )
        {
        case 1:
            count = count_bits1(value) ;
#if TIMES == 1
            printf( "%d\n", count ) ;
#endif
            break ;
        case 2:
            count = count_bits2(value) ;
#if TIMES == 1
            printf( "%d\n", count ) ;
#endif
            break ;
        case 3:
            count = count_bits3(value) ;
#if TIMES == 1
            printf( "%d\n", count ) ;
#endif
            break ;
        default:
            printf( "%d is not a value function\n", count ) ;
        }
    }
}
