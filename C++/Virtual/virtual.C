#include <stdio.h>
#include <stdlib.h>

class foo
{
public:
    virtual void identify()  { printf( "I am foo!\n" ) ; }
    void whoAreYou() { identify() ; } 
} ;

class bar
  : public foo
{
public:
    void identify() { printf( "I am bar!\n" ) ; }
} ;

int main( int ac, char **av )
{
    bar b ;
    b.whoAreYou() ;
}
