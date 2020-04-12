#include <iostream>

int get_int() { static int x = 0 ; return x++ ; }

class Int
{
    friend std::ostream& operator<<( std::ostream&, const Int& ) ;
    int t_ ;
public:
    Int() : t_( get_int() ) { ; }
} ;

std::ostream& operator<<( std::ostream &o, const Int &i )
{
    o << i.t_ ;
    return o ;
}

Int a, b, c, d ;

int main( int ac, char **av )
{
    std::cout << a << " " 
              << b << " "
              << c << " "
              << d << std::endl ;
    return 0 ;
}
