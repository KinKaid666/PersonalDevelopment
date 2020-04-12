#include <iostream>
#include <cstring.h>

using namespace std ;

int main( int ac, char **av )
{

    printf( "Testing... CString()\n" ) ; 
    {
        CString x ;
    }

    printf( "Testing... CString( const CString& )\n" ) ;
    {
        CString x ;
        CString y(x) ;
    }
    {
        CString x( "foobar" ) ;
        CString y(x) ;
    }

    printf( "Testing... CString( const char* )\n" ) ;
    {
        CString x((char *)NULL) ;
    }
    {
        CString x( "foobar" ) ;
    }

    printf( "Testing... CString( char   c, unsigned int len  = 1  )\n" ) ;
    {
        CString x( '*', 5 ) ;
        if( x != "*****" ) fprintf( stderr, "*** failed!\n" ) ;
    }

    printf( "Testing... CString( int    i, unsigned int base = 10 )\n" ) ;
    {
        CString x( 255, 16 ) ;
        if( !(x != "ff" || x != "FF") ) fprintf( stderr, "*** failed!\n" ) ;
    }

    printf( "Testing... CString( double d )\n" ) ;
    {
        CString x( 3.14159 ) ;
        if( x != "3.14159" ) fprintf( stderr, "*** failed!\n" ) ;
    }

    printf( "Testing... ~CString()\n" ) ;
    {
        CString x ;
    }
    {
        CString x ;
        CString y(x) ;
    }
    {
        CString x((char *)NULL) ;
        CString y(x) ;
    }
    {
        CString x("") ;
        CString y(x) ;
    }
    {
        CString x( "foobar" ) ;
        CString y(x) ;
    }

    printf( "Testing... CString operator=( char* )\n" ) ;
    {
        CString x ;
        x = "foobar" ;
        if( x != "foobar" ) fprintf( stderr, "*** failed!\n" ) ;
    }
    {
        CString x ;
        x = (char *)NULL ;
    }

    printf( "Testing... CString operator=( const CString& )\n" ) ;
    {
        CString x( "foobar" ), y ;
        y = x ;
        if( y != "foobar" ) fprintf( stderr, "*** failed!\n" ) ;
    }

    printf( "Testing... CString operator=( int    i )\n" ) ;
    {
        CString x ;
        x = 31337 ;
        if( x != "31337" ) fprintf( stderr, "*** failed!\n" ) ;
    }

    printf( "Testing... CString operator=( double d )\n" ) ;
    {
        CString x ;
        x = 3.14159 ;
        if( x != "3.14159" ) fprintf( stderr, "*** failed!\n" ) ;
    }

    printf( "Testing... CString operator+( char* ) const\n" ) ;
    {
        CString x( "foo" ), y ;
        y = x + "bar" ;
        if((y != "foobar") || (x != "foo")) fprintf( stderr, "*** failed!\n" ) ;
    }

    printf( "Testing... CString operator+( const CString& ) const\n" ) ;
    {
        CString x( "foo" ), y( "bar" ), z ;
        z = x + y ;
        if((z != "foobar") || (x != "foo") || (y != "bar"))
            fprintf( stderr, "*** failed!\n" ) ;
    }

    printf( "Testing... CString operator+( char   c ) const\n" ) ;
    {
        CString x("fooba"), y ;
        y = x + 'r' ;
        if((y != "foobar") || (x != "fooba"))
            fprintf( stderr, "*** failed!\n" ) ;
    }

    printf( "Testing... CString operator+( int    i ) const\n" ) ;
    {
        CString x( "foobar" ), y ;
        y = x + 31337 ;
        if((y != "foobar31337") || (x != "foobar"))
            fprintf( stderr, "*** failed!\n" ) ;
    }

    printf( "Testing... CString operator+( double d ) const\n" ) ;
    {
        CString x( "foobar" ), y ;
        y = x + 3.14159 ;
        if((y != "foobar3.14159") || (x != "foobar"))
            fprintf( stderr, "*** failed!\n" ) ;
    }

    printf( "Testing... CString operator+=( char* )\n" ) ;
    {
        CString x ;
        x += "foobar" ;
        if( x != "foobar" ) fprintf( stderr, "*** failed!\n" ) ;
    }

    printf( "Testing... CString operator+=( const CString& )\n" ) ;
    {
        CString x, y( "foobar" ) ;
        x += y ;
        if((x != "foobar") || (y != "foobar")) 
            fprintf( stderr, "*** failed!\n" ) ;
    }

    printf( "Testing... CString operator+=( char   c )\n" ) ;
    {
        CString x( "fooba" ) ;
        x += 'r' ;
        if( x != "foobar" )
            fprintf( stderr, "*** failed!\n" ) ;
    }

    printf( "Testing... CString operator+=( int    i )\n" ) ;
    {
        CString x ;
        x += 31337 ;
        if( x != "31337" )
            fprintf( stderr, "*** failed!\n" ) ;
    }

    printf( "Testing... CString operator+=( double d )\n" ) ;
    {
        CString x ;
        x += 3.14159 ;
        if( x != "3.14159" )
            fprintf( stderr, "*** failed!\n" ) ;
    }

    printf( "Testing... CString operator*( int i ) const\n" ) ;
    {
        CString x( "foobar" ), y ;
        y = x * 2 ; 
        if( x != "foobarfoobar" )
            fprintf( stderr, "*** failed!\n" ) ;
    }

    printf( "Testing... void append( char* )\n" ) ;
    {
        CString x ;
        x.append( "foobar" ) ;
        if( x != "foobar" )
            fprintf( stderr, "*** failed!\n" ) ;
    }

    printf( "Testing... void append( const  CString& )\n" ) ;
    {
        CString x, y( "foobar" ) ;
        x.append( y ) ;
        if((x != "foobar") || (y != "foobar"))
            fprintf( stderr, "*** failed!\n" ) ;
    }

    printf( "Testing... void append( char   c, unsigned int len  = 1  )\n" ) ;
    {
        CString x ;
        x.append( 'c' ) ;
        if( x != "c" ) fprintf( stderr, "*** failed!\n" ) ;
    }
    {
        CString x ;
        x.append( '*', 5 ) ;
        if( x != "*****" ) fprintf( stderr, "*** failed!\n" ) ;
    }

    printf( "Testing... void append( int    i, unsigned int base = 10 )\n" ) ;
    {
        CString x ;
        x.append( 31337 ) ;
        if( x != "31337" ) fprintf( stderr, "*** failed!\n" ) ;
    }
    {
        CString x ;
        x.append( 255, 16 ) ;
        if( !(x != "ff" || x != "FF") ) fprintf( stderr, "*** failed!\n" ) ;
    }

    printf( "Testing... void append( double d )\n" ) ;
    {
        CString x ;
        x.append( "3.14159" ) ;
        if( x != "3.14159" ) fprintf( stderr, "*** failed!\n" ) ;
    }

    printf( "Testing... char& operator[]( int index )\n" ) ;
    {
        /* 
        **      f   o   o   b   a   r 
        **      0   1   2   3   4   5 6 7
        ** -8-7-6  -5  -4  -3  -2  -1
        */

        CString x( "foobar" ) ;
        if( 
            (x[-7] != 'f') ||
            (x[-6] != 'f') ||
            (x[-5] != 'o') ||
            (x[-4] != 'o') ||
            (x[-3] != 'b') ||
            (x[-2] != 'a') ||
            (x[-1] != 'r') ||
            (x[ 0] != 'f') ||
            (x[ 1] != 'o') ||
            (x[ 2] != 'o') ||
            (x[ 3] != 'b') ||
            (x[ 4] != 'a') ||
            (x[ 5] != 'r') ||
            (x[ 6] != 'r')    
          ) 
            fprintf( stderr, "*** failed!\n" ) ;
    }

    printf( "Testing... CString slice( int start, int end ) const\n" ) ;
    {
        /* 
        **      f   o   o   b   a   r 
        **      0   1   2   3   4   5 6 7
        ** -8-7-6  -5  -4  -3  -2  -1
        */

        CString x( "foobar" ) ;
        if( x.slice(   0,  1) != "fo"     ) fprintf( stderr, "*** failed!\n" ) ;
        if( x.slice(  -2, -1) != "ar"     ) fprintf( stderr, "*** failed!\n" ) ;
        if( x.slice(   0, -1) != "foobar" ) fprintf( stderr, "*** failed!\n" ) ;
        if( x.slice(-200,200) != "foobar" ) fprintf( stderr, "*** failed!\n" ) ;
        if( x.slice(  -6,  5) != "foobar" ) fprintf( stderr, "*** failed!\n" ) ;
        if( x.slice(  -8,  8) != "foobar" ) fprintf( stderr, "*** failed!\n" ) ;
        if( x.slice(   8, -8) != NULL     ) fprintf( stderr, "*** failed!\n" ) ;
    }

    printf( "Testing... CString toUpper() const\n" ) ;
    {
        CString x( "foobar" ) ;
        if((x.toUpper() != "FOOBAR") || (x != "foobar"))
            fprintf( stderr, "*** failed!\n" ) ;
    }

    printf( "Testing... CString toLower() const\n" ) ;
    {
        CString x( "FOOBAR" ) ;
        if((x.toLower() != "foobar") || (x != "FOOBAR"))
            fprintf( stderr, "*** failed!\n" ) ;
    }

    printf( "Testing... operator const char*()\n" ) ;
    {
        CString x( "foobar" ) ;
        if( strlen(x) != 6 ) fprintf( stderr, "*** failed!\n" ) ;
    }

    printf( "Testing... int length() const\n" ) ;
    {
        CString x( "foobar" ) ;
        if( x.length() != 6 ) fprintf( stderr, "*** failed!\n" ) ;
    }

    printf( "Testing... int isNull()\n" ) ;
    {
        CString x, y( "foobar" ) ;
        if( !x.isNull() || y.isNull() ) fprintf( stderr, "*** failed!\n" ) ;
    }

    printf( "Testing... int operator==( const char*    )\n" ) ;
    printf( "Testing... int operator==( const CString& )\n" ) ;
    printf( "Testing... int operator!=( const char*    )\n" ) ;
    {
        CString x ;
        if( x != NULL ) fprintf( stderr, "*** failed!\n" ) ;
    }

    printf( "Testing... int operator!=( const CString& )\n" ) ;
    printf( "Testing... int operator< ( const char*    )\n" ) ;
    printf( "Testing... int operator< ( const CString& )\n" ) ;
    printf( "Testing... int operator> ( const char*    )\n" ) ;
    printf( "Testing... int operator> ( const CString& )\n" ) ;
    printf( "Testing... int operator<=( const char*    )\n" ) ;
    printf( "Testing... int operator<=( const CString& )\n" ) ;
    printf( "Testing... int operator>=( const char*    )\n" ) ;
    printf( "Testing... int operator>=( const CString& )\n" ) ;
}

