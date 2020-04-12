#include "syscall.h"

int
main()
{

   int i ;
   char *buf = "procCount" ;
   Join( Exec( 1, buf ) ) ;
   Exit( 0 ) ;
}
