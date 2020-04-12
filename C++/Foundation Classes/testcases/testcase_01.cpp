#include <stdio.h>
#include <stdlib.h>

#include "../FCSmartPointer.h"
#include "../FCRetainable.h"
class A : public FCRetainable
{
public:
    A() : id_(id++) { printf("Creating %u\n", getId()) ; }
    ~A()            { printf("Deleting %u\n", getId()) ; }

    void print() { printf("  This is object A(%u)\n", getId()) ; }

private:
    unsigned int id_ ;

    static unsigned int id ;
    unsigned int getId() { return id_ ; }
} ;

unsigned int A::id ;

void somefunc_01( A*   p ) { ; }
void somefunc_02( P<A> p ) { ; }

P<A> somefunc_03() { P<A> p ; p.NEW(new A()); return p; }
P<A> somefunc_04(P<A> p) { return p; }

int main( int ac, char **av )
{
    {
        P<A> a ;
        a.NEW(new A()) ;
        a->print() ;
    }

    printf("\n") ;

    {
        P<A> a(new A());
        a->print() ;
    }

    printf("\n") ;

    {
        P<A> a = new A() ;
        a->print() ;
    }

    printf("\n") ;

    {
        P<A> a, b ;
        a.NEW( new A() );
        a->print() ;
    }

    printf("\n") ;

    {
        P<A> a ;
        a.NEW( new A() );
        (*a).print() ;
    }

    printf("\n") ;

    {
        P<A> a ;
        a.NEW( new A() );
        somefunc_01( a ) ;
        a->print() ;
    }

    printf("\n") ;

    {
        P<A> a ;
        a.NEW( new A() );
        somefunc_02( a ) ;
        a->print() ;
    }

    printf("\n") ;

    {
        P<A> a, b ;
        a.NEW(new A()) ;
        b.NEW(new A()) ;
        a = b ;
        a->print() ;
        b->print() ;
    }

    printf("\n") ;

    {
        P<A> a = somefunc_03() ;
        a->print() ;
    }

    printf("\n") ;

    {
        P<A> a = somefunc_04(somefunc_03()) ;
        a->print() ;
    }

}
