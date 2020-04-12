#include <stdio.h>
#include <time.h>

int main( int ac, char **av )
{
    time_t t = time(NULL) ;
    char buf[BUFSIZ] ; 
    strftime( buf, BUFSIZ, "%Y/%m/%d %H:%M:%S", localtime(&t));
    printf( "%s\n", buf ) ;

}
