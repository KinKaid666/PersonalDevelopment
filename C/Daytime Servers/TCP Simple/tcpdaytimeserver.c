#include <stdlib.h>
#include <stdio.h>
#include <time.h>
#include <string.h>
#include <errno.h>

#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>

#define DAYTIME_PORT    1300

int main( int ac, char **av )
{

    int                 s ;
    struct sockaddr_in  server ;

    if( (s = socket( AF_INET, SOCK_STREAM, 0)) < 0 )
    {
        fprintf( stderr, "socket: %s\n", strerror( errno ) ) ;
        exit( EXIT_FAILURE ) ;
    }

    memset( &server, 0, sizeof( server ) ) ;
    server.sin_family = AF_INET ;
    server.sin_port   = htons( DAYTIME_PORT ) ;

    if( bind( s, (struct sockaddr *)&server, sizeof( server )) < 0 )
    {
        fprintf( stderr, "bind: %s\n", strerror( errno ) ) ;
        exit( EXIT_FAILURE ) ;
    }

    if( listen( s, 1 ) < 0 )
    {
        fprintf( stderr, "listen: %s\n", strerror( errno ) ) ;
        exit( EXIT_FAILURE ) ;
    }

    /*
    ** Await connections
    */
    for( ; ; ) 
    {
        int     ns ;
        time_t  ticks ;
        char    buf[BUFSIZ] ;

        /* Use nulls because we do not care about client info */
        if( (ns = accept( s, (struct sockaddr *)NULL, NULL )) < 0 )
        {
            fprintf( stderr, "accept: %s\n", strerror( errno ) ) ;
            continue ;
        }

        /* Get current time */
        ticks = time( NULL ) ;
        snprintf( buf, sizeof( buf ), "%.24s\r\n", ctime( &ticks ) ) ;

        /* send back data */
        write( ns, buf, sizeof( buf )) ;
        close( ns ) ;
    }

    return EXIT_SUCCESS ;
}
