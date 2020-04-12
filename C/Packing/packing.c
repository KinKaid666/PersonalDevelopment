#include <stdlib.h>
#include <stdio.h>
typedef struct unpacked
{
    char a ;
    int  b ;
} unpacked_t ;

typedef struct packed
{
    char  a ;
    int   b ;
} __attribute__((packed)) packed_t ;

int main( int ac, char **av )
{
    printf( "sizeof(unpacked) = %d\n"
            "sizeof(  packed) = %d\n",
            sizeof(unpacked_t), sizeof(packed_t) ) ;
    return EXIT_SUCCESS ;
}
