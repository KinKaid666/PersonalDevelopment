// File:        $Id: itoa.h,v 1.1 2002/08/01 15:42:32 etf2954 Exp $
// Author:      Eric Ferguson
// Description: A collection of functions to turn numeric data to strings

#ifndef __ITOA_H__
#define __ITOA_H__

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
char* itoa( int value, char *buffer, int base ) ;

#endif /* __ITOA_H__ */

