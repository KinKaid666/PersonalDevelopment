// File:	$Id: cstring.h,v 1.9 2003/08/08 05:39:24 etf2954 Exp $
// Author:	Eric Ferguson
// Description:	A reference counted string class

#ifndef __CSTRING_H__
#define __CSTRING_H__

#include <iostream>
#include "reference.h"

#define BINARY		2
#define OCTAL		8
#define DECIMAL		10
#define HEXIDECIMAL	16

class CString
{
public:

    CString() ;
    CString( const CString& ) ;
    CString( const char* ) ;

    CString( char   c, unsigned int len  = 1  ) ;
    CString( int    i, unsigned int base = 10 ) ;
    CString( double d ) ;

    ~CString() ;

    CString operator=( char* ) ;
    CString operator=( const CString& ) ;
    CString operator=( int    i ) ;
    CString operator=( double d ) ;

    CString operator+( char* ) const ;
    CString operator+( const CString& ) const ;
    CString operator+( char   c ) const ;
    CString operator+( int    i ) const ;
    CString operator+( double d ) const ;

    CString operator+=( char* ) ;
    CString operator+=( const CString& ) ;
    CString operator+=( char   c ) ;
    CString operator+=( int    i ) ;
    CString operator+=( double d ) ;

    CString operator*( int i ) const ;

    void append( char* ) ;
    void append( const  CString& ) ;
    void append( char   c, unsigned int len  = 1  ) ;
    void append( int    i, unsigned int base = 10 ) ;
    void append( double d ) ;

    char& operator[]( int index ) ; 
    const char& operator[]( int index ) const ; 

    CString slice( int start, int end ) const ;

    CString toUpper() const ;
    CString toLower() const ;

    int operator==( const char*    ) ;
    int operator==( const CString& ) ;
    int operator!=( const char*    ) ;
    int operator!=( const CString& ) ;
    int operator< ( const char*    ) ;
    int operator< ( const CString& ) ;
    int operator> ( const char*    ) ;
    int operator> ( const CString& ) ;
    int operator<=( const char*    ) ;
    int operator<=( const CString& ) ;
    int operator>=( const char*    ) ;
    int operator>=( const CString& ) ;

    operator const char*() { return data_ ; }

    int length() const { return length_ ; }
    int isNull() { return data_ == NULL ; }

private:

    void cow()   ;
    void clone() ;

    int  cmp ( const char*, const char* ) ;
    int  ncmp( const char*, const char*, size_t length ) ;

private:

    Reference *refs_ ;
    char      *data_ ;
    int        length_ ;

    friend std::ostream& operator<<( std::ostream &, const CString& ) ;
} ;

#endif /* __CSTRING_H__ */

