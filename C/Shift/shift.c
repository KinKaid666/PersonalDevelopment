#include <stdio.h>
#include <stdlib.h>

char cstr[] = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz" ;

void swap( char *a, char *b )
{
    char temp = *a ;
    *a = *b ;
    *b = temp ;
}

void reverse( char *list, int len ) 
{
    char *end = list + len - 1 ;
    while( list < end )
    {
        swap( list++, end-- ) ;
    }
}

void shift( char *list, int pos, int len ) 
{
    reverse( list, pos ) ;
    reverse( list + pos, len - pos ) ;
    reverse( list, len ) ;
}

int main( int ac, char **av )
{
    int len = strlen( cstr ) ;
    int num ;
    if( ac != 2 ) 
    {
        fprintf( stderr, "usage: shift <number of places>\n" ) ;
        exit( EXIT_FAILURE ) ;
    }
    
    if( ((num = atoi(av[1])) <= 0 ) || (num > len) )
    {
        fprintf( stderr, "%s: incorrect number of places\n", av[0] ) ;
        exit( EXIT_FAILURE ) ;
    }
    printf( "Array before = %s\n", cstr ) ;
    shift( cstr, atoi(av[1]), len ) ;
    printf( "Array after  = %s\n", cstr ) ;
}
