#include <stdio.h>
#include <stdlib.h>

unsigned long long fib(unsigned long long x) {
    switch(x) {
    case 0:
        return 0 ;
    case 1:
        return 0 ;
    case 2:
        return 1 ;
    default:
        return fib(x-1)+fib(x-2) ;
    }
}

int main( int ac, char **av ) {
    if( ac != 2 ) {
        fprintf(stderr, "usage: %s <number>\n", av[0]) ;
        exit(EXIT_FAILURE) ;
    }

    printf("%s(%s) = %llu\n", av[0],av[1],fib(strtoull(av[1], NULL, 10))) ;
}
