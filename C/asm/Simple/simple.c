#include <stdio.h>
#include <stdlib.h>

int main(void)
{
    char* src  = "eric" ;
    char* dest = malloc(strlen(src)+1) ;
    int d0, d1, d2;
    __asm__ __volatile__(  "1:\tlodsb\n\t"
                           "stosb\n\t"
                           "testb %%al,%%al\n\t"
                           "jne 1b"
                         : "=&S" (d0), "=&D" (d1), "=&a" (d2)
                         : "0" (src),"1" (dest) 
                         : "memory");
    printf("src = %s\ndest = %s\n", src, dest);
    return 0;
}

