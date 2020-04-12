// File:        $Id: itoa.C,v 1.4 2003/08/08 05:34:12 etf2954 Exp $
// Author:      Eric Ferguson

#include <stdlib.h>

char* itoa( int value, char *buffer, int base )
{
    unsigned int u_value ;
    int quotient ;
    int positive = ( value >= 0 ) ;

    char *result_itr = buffer ;
    char *reverse_itr ;
    char  reverse_temp ;

    if( base < 2 || base > 36 ) 
    {
        *buffer = '\0' ;
        return NULL ;
    }

    if( !positive ) 
        u_value = -value ;
    else
        u_value = value ;

    while( u_value != 0 )
    {
        quotient = u_value % base ;
        if( quotient > 9 ) 
            *result_itr++ = quotient - 10 + 'a' ;
        else
            *result_itr++ = quotient + '0' ;
        u_value /= base ;
    }
    if( !positive ) *result_itr++ = '-' ;

    /* null terminate and get back to the last non-null char of string */
    *result_itr-- = '\0' ; 

    /* reverse the string */
    reverse_itr = buffer ;
    do
    {
         reverse_temp = *reverse_itr  ;
        *reverse_itr  = *result_itr   ;
        *result_itr   =  reverse_temp ;
    } while( ++reverse_itr < --result_itr ) ;

    return buffer ;
}

