#include <stdlib.h>
#include <stdio.h>

/*
** Name:        itoa
**
** Description: Converts value to a string in base
**
** Arguments:   value:  integer to be converted
**              buffer: buffer to store result
**              base:   base in which value is to be represented
**
** Returns:     Pointer to buffer, or NULL in case of error
*/
char* itoa( int   value,
            char *buffer,
            int   base ) {
    unsigned int u_value ;
    int quotient ;
    int positive = ( value >= 0 ) ;

    char *result_itr = buffer ;
    char *reverse_itr ;
    char  reverse_temp ;

    if( base < 2 || base > 36 ) {
        *buffer = '\0' ;
        return buffer ;
    }

    if( !positive ) {
        u_value = -value ;
    } else {
        u_value = value ;
    }

    while( u_value != 0 ) {
        quotient = u_value % base ;
        if( quotient > 9 ) {
            *result_itr++ = quotient - 10 + 'a' ;
        } else {
            *result_itr++ = quotient + '0' ;
        }
        u_value /= base ;
    }
    if( !positive ) {
        *result_itr++ = '-' ;
    }

    /* null terminate and get back to the last non-null char of string */
    *result_itr-- = '\0' ;

    /* reverse the string */
    reverse_itr = buffer ;
    do {
         reverse_temp = *reverse_itr ;
        *reverse_itr  = *result_itr  ;
        *result_itr   = reverse_temp ;
    } while( ++reverse_itr < --result_itr ) ;

    return buffer ;
}

int main( int ac, char **av ) {
    int in, base ;
    char out[BUFSIZ] ;

    if( ac != 3 ) {
        fprintf( stderr, "usage: itoa <number> <base to convert to>\n" ) ;
        exit( 1 ) ;
    }

    in = atoi( av[1] ) ;
    base = atoi( av[2] ) ;
    if(base < 0 || base > 36) {
        printf( "invalid base : %d, base must be between 0 and 36\n", base ) ;
        exit( EXIT_FAILURE ) ;
    }
    itoa( in, out, base ) ;
    printf( "%d in base %d = '%s'\n", in, base, out ) ;
    return EXIT_SUCCESS ;
}

