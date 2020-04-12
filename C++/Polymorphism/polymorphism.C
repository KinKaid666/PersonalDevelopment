#include <iostream>
#include "shapes.h"

#define SHAPES  3

int main( int ac, char **av )
{
    shape* shapes[SHAPES] ;
    shapes[0] = new diamond   () ;
    shapes[1] = new square    () ;
    shapes[2] = new rectangle () ;
    for( int x = 0 ; x < SHAPES ; x++ )
    {
        shapes[x]->identify() ;
        shapes[x]->draw    () ;
        std::cout << std::endl ;
    }

}
