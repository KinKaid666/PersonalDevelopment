#include <stdio.h>
#include <stdlib.h>

#include "../FCSmartPointer.h"
#include "../FCRetainable.h"
class A : public FCRetainable
{
public:
    A() : id_(id++) { printf("\tCreating %u\n", getId()) ; }
    ~A()            { printf("\tDeleting %u\n", getId()) ; }

    void print() { printf("\tThis is object A(%u)\n", getId()) ; }

private:
    unsigned int id_ ;

    static unsigned int id ;
    unsigned int getId() { return id_ ; }
} ;

unsigned int A::id ;

void somefunc_01( A*   p ) { ; }
void somefunc_02( P<A> p ) { p->print() ; }

P<A> somefunc_03() { P<A> p ; p.NEW(new A()); return p; }
P<A> somefunc_04(P<A> p) { return p; }

int main( int ac, char **av )
{
    printf("Running A\n") ;
    {
        P<A> a ;
        a.NEW(new A()) ;
        a->print() ;
    }

    printf("  done w/ A\n") ;
    printf("Running B\n") ;
    {
        P<A> a(new A());
        a->print() ;
    }

    printf("  done w/ B\n") ;
    printf("Running C\n") ;
    {
        P<A> a = new A() ;
        a->print() ;
    }

    printf("  done w/ C\n") ;
    printf("Running D\n") ;
    {
        P<A> a, b ;
        a.NEW( new A() );
        a->print() ;
    }

    printf("  done w/ D\n") ;
    printf("Running E\n") ;
    {
        P<A> a ;
        a.NEW( new A() );
        (*a).print() ;
    } printf("  done w/ E\n") ;
    printf("Running F\n") ;
    {
        P<A> a ;
        a.NEW( new A() );
        somefunc_01( a ) ;
        a->print() ; } 
    printf("  done w/ F\n") ;
    printf("Running G\n") ;
    {
        P<A> a ;
        a.NEW( new A() );
        somefunc_02( a ) ;
        a->print() ;
    }

    printf("  done w/ G\n") ;
    printf("Running H\n") ;
    {
        P<A> a, b ;
        a.NEW(new A()) ;
        b.NEW(new A()) ;
        a = b ;
        a->print() ;
        b->print() ;
    }

    printf("  done w/ H\n") ;
    printf("Running I\n") ;
    {
        P<A> a = somefunc_03() ;
        a->print() ;
    }

    printf("  done w/ I\n") ;
    printf("Running J\n") ;
    {
        P<A> a ;
        a.NEW(new A()) ;
        P<A> b = somefunc_04(a) ;
    }
    printf("  done w/ J\n") ;
    printf("Running K\n") ;
    {
        P<A> a = somefunc_04(somefunc_03()) ;
        a->print() ;
    }
    printf("  done w/ K\n") ;
    printf("Running L\n") ;
    {
        P<A> a = somefunc_04(somefunc_04(somefunc_03())) ;
        a->print() ;
    }
    printf("  done w/ L\n") ;
}
