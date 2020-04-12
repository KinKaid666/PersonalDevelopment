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

    int                 s, n ;
    struct sockaddr_in  server ;
    char                buf[BUFSIZ] ;

    if( ac != 2 )
    {
        fprintf( stderr, "Usage: %s <daytime server ip>\n", av[ 0 ] ) ;
        exit( EXIT_FAILURE ) ;

    }
    if( (s = socket( AF_INET, SOCK_STREAM, 0)) < 0 )
    {
        fprintf( stderr, "socket: %s\n", strerror( errno ) ) ;
        exit( EXIT_FAILURE ) ;
    }

    memset( &server, 0, sizeof( server ) ) ;
    server.sin_family = AF_INET ;
    server.sin_port   = htons( DAYTIME_PORT ) ;

    if( inet_pton( AF_INET, av[1], &server.sin_addr ) <= 0 )
    {
        fprintf( stderr, "inet_pton: %s\n", strerror( errno ) ) ;
        exit( EXIT_FAILURE ) ;
    }

    if( connect( s, (struct sockaddr *)&server, sizeof( struct sockaddr )) < 0 )
    {
        fprintf( stderr, "connect: %s\n", strerror( errno ) ) ;
        exit( EXIT_FAILURE ) ;
    }

    while( (n = read( s, buf, sizeof( buf ) - 1 )) > 0 )
    {
        buf[n] = 0 ;    /* null terminate */
        fputs( buf, stdout ) ;
    }
    return EXIT_SUCCESS ;
}
