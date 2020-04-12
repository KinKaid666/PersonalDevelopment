#include "syscall.h"

void
main()
{
   int i ;
   for( i = 0 ; i < 10000 ; i++ ) Yield() ;
   Exit( 0 ) ;
}
