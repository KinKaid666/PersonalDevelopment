// badSub.C 
//
//	Shows that the order of subobject construction in the order that the
//	members are listened in the class declaration, and not the order
//	in chich they appear in the ctor.

#include <iostream>

class bad_double
{
public:
    bad_double( int x = 0 )
    :  x_( x )
    ,  x_doubled_( x_ * 2 )
    { ; }

private:
    friend std::ostream& operator<<( std::ostream &o, const bad_double &f ) ;
    int x_doubled_ ;
    int x_ ;
} ;

class good_double
{
public:
    good_double( int x = 0 )
    :  x_( x )
    ,  x_doubled_( x_ * 2 )
    { ; }

private:
    friend std::ostream& operator<<( std::ostream &o, const good_double &b ) ;
    int x_ ;
    int x_doubled_ ;
} ;

std::ostream& operator<<( std::ostream &o, const bad_double &b )
{
   o << b.x_doubled_ ;
   return o ;
}

std::ostream& operator<<( std::ostream &o, const good_double &g )
{
   o << g.x_doubled_ ;
   return o ;
}

int main( int ac, char **av )
{
    bad_double  b(2) ;
    good_double g(2) ;
    std::cout << "incorrect = " << b << " correct = " << g << std::endl ;
    std::cout << "Why ..." << std::endl ;
    return 0 ;
}

