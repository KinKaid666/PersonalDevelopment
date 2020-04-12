/* gcc -g -D WHICH=<which function> 
          -D TIMES=<number of interations> strmatch.c -o strmatch */
#include <stdlib.h>
#include <stdio.h>

/* On^3 */
char *strmatch1( const char *s1, const char *s2 )
{
    char *result = (char *)malloc(256) ;
    char *res_itr ;
    const char *itr1, *itr2 ; 

    memset( result, '\0', 255 ) ;
    for( itr1 = s1 ; *itr1 != '\0' ; itr1++ )
    {
        for( itr2 = s2 ; *itr2 != '\0' ; itr2++ )
        {
            if( *itr1 == *itr2 )
            {
                res_itr = result ;
                while( *res_itr != '\0' && *res_itr != *itr1 )
                    res_itr++ ;
                if( *res_itr == '\0' ) 
                    *res_itr = *itr1 ;
            }
        }
    }
    return result ;
}

#define   setbit(x) (found[x/32] = found[x/32] |  (1 << x%32))
#define clearbit(x) (found[x/32] = found[x/32] & ~(1 << x%32))
#define checkbit(x) (found[x/32] & (1 << x%32))

/* On */
char *strmatch2( const char *s1, const char *s2 )
{
    char *result = NULL, *res_itr ;
    const char *itr1, *itr2 ;
    int size = 0, i ;
    int found[sizeof(char) * 8] ;

    for( i = 0 ; i < sizeof(found)/sizeof(int) ; i++ )
        found[i] = 0 ;

    for( itr2 = s2 ; *itr2 != '\0' ; itr2++ )
        setbit( *itr2 ) ;

    for( itr1 = s1 ; *itr1 != '\0' ; itr1++ )
        if( checkbit( *itr1 ) )
            size++ ;

    if( (result = (char *)malloc( size + 1 )) == NULL )
        return NULL ;
    
    for( itr1 = s1, res_itr = result ; *itr1 != '\0' ; itr1++ )
    {
        if( checkbit( *itr1 ) )
        {
            *res_itr++ = *itr1 ;
            clearbit( *itr1 ) ;
        }
    }
    *res_itr = '\0' ;
    return result ;
}
            
int main( int ac, char **av ) 
{
    int i ; 
    for( i = 0 ; i < TIMES ; i++ )
    {
        switch( WHICH )
        {
        case 1 :
        {
            char * res = strmatch1( av[1], av[2] ) ;
#if TIMES == 1
            printf( "strmatch1( \"%s\", \"%s\" ) = \"%s\"\n", av[1], 
                                                              av[2], 
                                                              res ) ;
#endif
            free( res ) ;
            break ;
        }
        case 2 :
        {
            char * res = strmatch2( av[1], av[2] ) ;
#if TIMES == 1
            printf( "strmatch2( \"%s\", \"%s\" ) = \"%s\"\n", av[1], 
                                                              av[2], 
                                                              res ) ;
#endif
            free( res ) ;
            break ;
        }
        }
    }
    return EXIT_SUCCESS ;
}

