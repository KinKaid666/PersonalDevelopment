#include <iostream>

class foo_base
{
public:
    foo_base()  { std::cout << "foo_base ctor." << std::endl ; }
    virtual ~foo_base() { std::cout << "foo_base dtor." << std::endl ; }
} ;

class foo
 : public foo_base
{
public:
    foo()  { std::cout << "foo ctor." << std::endl ; }
    ~foo() { std::cout << "foo dtor." << std::endl ; }
} ;

int main( int ac, char ** )
{
    foo *x = new foo() ;
    foo *y = new foo() ;
    delete (foo_base*)x ;
    delete            y ;
    return 0 ;
}
