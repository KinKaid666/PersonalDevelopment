#include <stdio.h>
#include <stdlib.h>

class handle
{
public:
    handle() : x_( 0 ) { ; }
    int& alwaysFalse() { return x_ ; }
private:
    int x_ ;
} ;

int main( int ac, char **av )
{
    handle h ;
    h.alwaysFalse() = 1 ;
    printf( "alwaysFalse() = %d.\n", h.alwaysFalse() ) ;
}
