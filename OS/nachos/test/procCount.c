#include "syscall.h"

int
main()
{
    int numProcs ;
    char a ;
    numProcs = GetProcCount() ;
    numProcs += 48 ;
    a = (char)numProcs ;
    Write( (void *)&a, 1, 1 ) ;
    Write( (void *)"\n", 1, 1 ) ;
    ThreadExit( 0 ) ;
}
