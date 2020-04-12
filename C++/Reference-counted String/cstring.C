// File:        $Id: cstring.C,v 1.12 2003/08/08 05:39:24 etf2954 Exp $
// Author:      Eric Ferguson

#include <ctype.h>
#include <stdlib.h>
#include <string.h>

#include "cstring.h"
#include "itoa.h"

/*
** Ctors
*/
CString::CString() :
    refs_( new Reference(1) ),
    data_( NULL ),
    length_( 0 )
{ ; }

CString::CString( const CString &c ) :
    refs_( c.refs_ ),
    data_( c.data_ ),
    length_( c.length_ )
{
    c.refs_->addReference() ;
}

CString::CString( const char *c ) :
    refs_( new Reference(1) ),
    data_( NULL ),
    length_( 0 )
{
    if( c != NULL )
    {
        data_   = strdup(c) ;
        length_ = strlen(c) ;
    }
}

CString::CString( char c, unsigned int len ) :
    refs_( new Reference(1) ),
    length_( len )
{
    data_ = (char *)malloc( len + 1 ) ;
    memset( (void *)data_, (int)c, len ) ;
    data_[len] = '\0' ;
}

/*
** Ctors based on numeric types
*/
CString::CString( int i, unsigned int base ) :
    refs_( new Reference(1) )
{
    int  len ;
    char buf[BUFSIZ] ;

    itoa( i, buf, base ) ;
    len = strlen( buf ) ;
    data_ = (char *)malloc( len + 1 ) ;
    strncpy( data_, buf, len + 1 ) ;
    length_ = len ;
}

CString::CString( double d )  :
    refs_( new Reference(1) )
{
    *this = d ;
}

/*
** Dtor
*/
CString::~CString()
{
    if( refs_->removeReference() == 0 )
    {
        if( !isNull() )
        {
            delete [] data_ ;
            data_ = NULL ;
        }
        length_ = 0 ;
        delete refs_ ;
        refs_ = NULL ;
    }
}

/*
** cow & clone
*/
void CString::cow()
{
    if( refs_->references() > 1 ) clone() ;
}

void CString::clone()
{
    refs_->removeReference() ;
    data_ = (data_ == NULL ? NULL : strdup( data_ )) ;
    refs_ = new Reference(1) ;
}

/*
** Mutators
*/
CString CString::operator=( char *a )
{
    cow() ;

    if( a == NULL )
    {
        data_ = NULL ;
        length_ = 0 ;
    }
    else
    {
        data_ = strdup( a ) ;
        length_ = strlen( a ) ;
    }
    return *this ;
}

CString CString::operator=( const CString &rhs )
{
    if( *this == rhs ) return *this ;
    cow() ;

    data_   = rhs.data_ ;
    refs_   = rhs.refs_ ;
    length_ = rhs.length_ ;
    refs_->addReference() ;
    return *this ;
}

CString CString::operator=( int i )
{
    cow() ;
    int  len ;
    char buf[BUFSIZ] ;

    itoa( i, buf, DECIMAL ) ;
    len = strlen( buf ) ;
    data_ = (char *)malloc( len + 1 ) ;
    strncpy( data_, buf, len + 1 ) ;
    length_ = len ;
    return *this ;
}

CString CString::operator=( double d )
{
    cow() ;
    char buf[BUFSIZ] ;

    length_ = snprintf( buf, BUFSIZ, "%g", d ) ;
    data_ = (char *)malloc( length_ + 1 ) ;
    strncpy( data_, buf, length_ + 1 ) ;
    return *this ;
}

std::ostream& operator<<( std::ostream &o, const CString &c )
{
    o << c.data_ ;
    return o ;
}

CString CString::operator+( char *a ) const
{
    CString temp( *this ) ;
    temp.append( a ) ;
    return temp ;
}

CString CString::operator+( const CString &rhs ) const
{
    CString temp( *this ) ;
    temp.append( rhs ) ;
    return temp ;
}

CString CString::operator+( char c ) const
{
    CString temp( *this ) ;
    temp.append( c ) ;
    return temp ;
}

CString CString::operator+( int i ) const
{
    CString temp( *this ) ;
    temp.append( i ) ;
    return temp ;
}

CString CString::operator+( double d ) const
{
    CString temp( *this ) ;
    temp.append( d ) ;
    return temp ;
}

CString CString::operator+=( char *a )
{
    append( a ) ;
    return *this ;
}

CString CString::operator+=( const CString &rhs )
{
    append( rhs ) ;
    return *this ;
}

CString CString::operator+=( char c )
{
    append( c ) ;
    return *this ;
}

CString CString::operator+=( int i )
{
    append( i ) ;
    return *this ;
}

CString CString::operator+=( double d )
{
    append( d ) ;
    return *this ;
}

void CString::append( char* c )
{
    cow() ;

    int c_len   = strlen( c ) ;
    int old_len = length_ ;
    length_    += c_len ;

    data_ = (char *)realloc( data_, length_ + 1 ) ;
    strncpy( data_ + old_len, c, c_len + 1 ) ;
}

void CString::append( const CString &rhs )
{
    cow() ;

    int old_len = length_ ;
    length_    += rhs.length_ ;

    data_ = (char *)realloc( data_, length_ + 1 ) ;
    strncpy( data_ + old_len, rhs.data_, rhs.length_ + 1 ) ;
}

void CString::append( char c, unsigned int len )
{
    cow() ;

    int old_len = length_ ;
    length_    += len ;

    data_ = (char *)realloc( data_, length_ + 1 ) ;
    memset( data_ + old_len, c, len ) ;
    data_[length_] = '\0' ;
}

void CString::append( int i, unsigned int base )
{
    char buf[BUFSIZ] ;

    itoa( i, buf, base ) ;

    /* cow() done in this function, no need to do it twice */
    append( buf ) ;
}

void CString::append( double d )
{
    cow() ;

    char buf[BUFSIZ] ;
    int  d_len   = snprintf( buf, BUFSIZ, "%g", d ) ;
    int  old_len = length_ ;
    length_     += d_len ;

    data_ = (char *)realloc( data_, length_ + 1 ) ;
    strncpy( data_ + old_len, buf, d_len ) ;
    data_[length_] = '\0' ;
}

char& CString::operator[]( int index )
{
    if( index >= 0 )
    {
        if( index > (length_ - 1) )
            return data_[length_ - 1] ;
        else
            return data_[index] ;
    }
    else
    {
        if( index < ( 0 - length_ ) )
            return data_[0] ;
        else
            return data_[index + length_] ;
    }
}

CString CString::slice( int start, int end ) const
{
    CString temp ;
    char    *cstr ;

    /* errors */
    if( start < 0 ) start += length_ ;
    if( end   < 0 ) end   += length_ ;
    if( start < 0 ) start = 0 ;
    if( end   < 0 ) end   = 0 ;
    if( end > length_ ) end = length_ ;
    if( end < start ) return temp ;

    temp.length_ = end - start + 1 ;
    cstr = (char *)malloc( temp.length_ + 1 ) ;
    strncpy( cstr, data_ + start, temp.length_ ) ;
    cstr[ temp.length_ ] = '\0' ;
    temp.data_ = cstr ;
    return temp ;
}

CString CString::operator*( int i ) const
{
    CString temp ;

    temp.length_ = i * length_ ;
    temp.data_ = (char *)malloc( temp.length_ + 1 ) ;

    char* data_itr = temp.data_ ;
    for( int x = 0 ; x < i ; x++ )
    {
        strncpy( data_itr, data_, length_ ) ;
        data_itr += length_ ;
    }
    temp.data_[temp.length_] = '\0' ;
    return temp ;
}

int CString::cmp( const char *s1, const char *s2 )
{
    if((s1 == NULL) || (s2 == NULL)) return s1 - s2 ;

    return strcmp(s1, s2) ;

}

int CString::ncmp( const char *s1, const char *s2, size_t length )
{
    if((s1 == NULL) || (s2 == NULL)) return s1 - s2 ;

    return strncmp(s1, s2, length) ;

}

int CString::operator==( const char *a )
{
    return ncmp( data_, a, length_ ) == 0 ;
}

int CString::operator==( const CString &rhs )
{
    return ncmp( data_, rhs.data_, length_ ) == 0 ;
}

int CString::operator!=( const char *a )
{
    return ncmp( data_, a, length_ ) != 0 ;
}

int CString::operator!=( const CString &rhs )
{
    return ncmp( data_, rhs.data_, length_ ) != 0 ;
}

int CString::operator<( const char *a )
{
    return ncmp( data_, a, length_ ) < 0 ;
}

int CString::operator<( const CString &rhs )
{
    return ncmp( data_, rhs.data_, length_ ) < 0 ;
}

int CString::operator> ( const char *a )
{
    return ncmp( data_, a, length_ ) > 0 ;

}

int CString::operator> ( const CString &rhs )
{
    return ncmp( data_, rhs.data_, length_ ) > 0 ;
}

int CString::operator<=( const char *a )
{
    return ncmp( data_, a, length_ ) <= 0 ;
}

int CString::operator<=( const CString &rhs )
{
    return ncmp( data_, rhs.data_, length_ ) <= 0 ;
}

int CString::operator>=( const char *a )
{
    return ncmp( data_, a, length_ ) >= 0 ;
}

int CString::operator>=( const CString &rhs )
{
    return ncmp( data_, rhs.data_, length_ ) >= 0 ;
}

CString CString::toUpper() const
{
    CString temp( *this ) ;
    temp.cow() ;

    for( int i = 0 ; i < temp.length_ ; i++ )
        temp.data_[i] = toupper( temp.data_[i] ) ;
    return temp ;
}

CString CString::toLower() const
{
    CString temp( *this ) ;
    temp.cow() ;

    for( int i = 0 ; i < temp.length_ ; i++ )
        temp.data_[i] = tolower( temp.data_[i] ) ;
    return temp ;
}

