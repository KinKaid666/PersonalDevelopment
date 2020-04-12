#include <stdio.h>
#include <stdlib.h>
#include <time.h>

int main( int ac, char **av )
{
    struct tm tm ;

    /* best set it, since it's not static data */
    memset((void*)&tm, 0, sizeof(struct tm)) ;

    strptime( "1980/06/01 01:00",  "%Y/%m/%d %H:%M", &tm ) ;
    tm.tm_hour -= 1 ;
    time_t t = mktime(&tm) ;

    char buf[BUFSIZ] ;
    strftime( buf, BUFSIZ, "%Y/%m/%d %H:%M:%S", localtime(&t) ) ;
    printf( "%s\n", buf ) ;
    return 0 ;
}
