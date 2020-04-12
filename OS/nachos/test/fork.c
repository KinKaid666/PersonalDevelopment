#include "syscall.h"

static void 
print() 
{ 
   Write( "** begining2\n", 14, 1 ) ;
   Write( "** done2\n", 9, 1 ) ;
   ThreadExit( 0 ) ; // required that a thread calls this on ones self
}

int
main()
{
   int i ;
   Write( "** begining1\n", 14, 1 ) ;
   Fork( (int)print ) ;
   Write( "** done1\n", 9, 1 ) ;
   for( i = 0 ; i < 10000 ; i++ ) Yield() ; // wait for that thread to finsih
					    // since there is no threadjoin
   Exit( 0 ) ;
}
