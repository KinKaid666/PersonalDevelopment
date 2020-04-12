#include <stdio.h>
#include <stdlib.h>
class A
{
public:
    A() { printf("Creating A\n") ; }
    virtual ~A() { printf("Detroying A\n") ; }
} ;

class B : public A { ; } ;

class C : public B
{
public:
    C() { printf("Creating C\n") ; }
    virtual ~C() { printf("Detroying C\n") ; }
} ;

class badA
{
public:
    badA() { printf("Creating badA\n") ; }
    ~badA() { printf("Detroying badA\n") ; }
} ;

class badB : public badA { ; } ;

class badC : public badB
{
public:
    badC() { printf("Creating badC\n") ; }
    virtual ~badC() { printf("Detroying badC\n") ; }
} ;
int main( int ac, char **av )
{
    A *pa = new C ;
    delete pa ;
    badA *pba = new badC ;
    delete pba ;
    return EXIT_SUCCESS ;
}
