/* File:        $Id: sendfile-tcp-server.c,v 1.1 2002/12/19 23:29:44 etf2954 Exp $
** Author:      Eric Ferguson
** Description: A simple file transfer server.
** Revision:
**              please use `rlog <filename>`
*/

#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include <errno.h>

#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>

#include <unistd.h>
#include <arpa/inet.h>

#define MAX_FILENAME_LEN        512
#define PORT_MIN                1024
#define PORT_MAX                65535

#define FILENAME_ERROR          0
#define CONTAINS_SLASHES        1
#define SUCCESSFUL_FILENAME     2

const char *errors[] =
{
    "Filename must be null terminated and be < 512 bytes",
    "Filename may not contain slashes",
    ""
} ;

int main( int ac, char **av )
{

    int                 s ;             /* File descriptor for the socket */
    struct sockaddr_in  server ;        /* Server's IP address  */
    int                 port ;          /* Server's port number */

    /*
    ** Make sure the arguments are correct
    */
    if( ac != 2 )
    {
        fprintf( stderr, "Usage: %s <port number>\n", av[ 0 ] ) ;
        exit( EXIT_FAILURE ) ;
    }

    port = atoi( av[ 1 ] ) ;
    if( port < PORT_MIN || port > PORT_MAX )
    {
        fprintf( stderr, "%d: Invalid port number\n", port ) ;
        exit( EXIT_FAILURE ) ;
    }

    /*
    ** Create a stream (TCP) socket.  The protocol is 0, so that the system
    ** will choose an appropiate protocol.
    */
    if( (s = socket( AF_INET, SOCK_STREAM, 0 )) < 0 )
    {
        fprintf( stderr, "socket: %s\n", strerror( errno ) ) ;
        exit( EXIT_FAILURE ) ;
    }

    /*
    ** Fill in the addr structure with the port number give on the
    ** cmd line.  The OS kernel will fill in the host portion of the
    ** address depending on which network interface is used
    */
    memset( &server, 0, sizeof( struct sockaddr_in ) ) ;
    server.sin_family   = AF_INET ;

    /* Use htons (host to network) to correct for byte order */
    server.sin_port     = htons( port ) ;

    /*
    ** Bind this port number to this socket so that other programs
    ** can connect to this socket by specifying the port number.
    */
    if( bind( s, (struct sockaddr *)&server, sizeof( struct sockaddr) ) < 0 )
    {
        fprintf( stderr, "bind: %s\n", strerror( errno ) ) ;
        exit( EXIT_FAILURE ) ;
    }

    /*
    ** Make sure this server is awaiting a connection
    */
    if( listen( s, 2 ) < 0 )
    {
        fprintf( stderr, "listen: %s\n", strerror( errno ) ) ;
        exit( EXIT_FAILURE ) ;
    }

    /*
    ** Begin accepting network connections
    */
    for( ; ; )
    {
        struct  sockaddr_in client ;    /* Client's IP adderss */
        socklen_t client_len ;          /* Length of client's addr */
        int     ns ;                    /* New socket for connection */
        char    buf[BUFSIZ] ;           /* Data buffer */
        int     len ;                   /* Buffer length */
        FILE    *file ;                 /* fd */
        char    filename[MAX_FILENAME_LEN] ;
                                        /* buffer for filename */

        printf( "Waiting for connections...\n\n" ) ;
        /*
        ** Wait for a client's connection request
        */
        memset( &client, 0, sizeof( struct sockaddr_in ) ) ;
        client_len = sizeof( struct sockaddr_in ) ;
        if( (ns = accept( s, (struct sockaddr *)&client, &client_len )) < 0 )
        {
            fprintf( stderr, "accept: %s\n", strerror( errno ) ) ;
            continue ;
        }

        printf( "Connection made from %s.%d\n", inet_ntoa( client.sin_addr ),
                                                client.sin_port ) ;

        /*
        ** Read the filename from the client
        */
        len = read( ns, filename, MAX_FILENAME_LEN ) ;

        /*
        ** Errors:
        **  no null:            filename is too long, or not null terminated
        **  contains slashes:   file contains '/'
        */
        if( memchr( filename, '\0', len ) == NULL )
        {
            write( ns, errors[FILENAME_ERROR],
                       strlen( errors[FILENAME_ERROR] ) + 1 ) ;
        }
        else if( strchr( filename, '/' ) != NULL )
        {
            write( ns, errors[CONTAINS_SLASHES],
                       strlen( errors[CONTAINS_SLASHES] ) + 1 ) ;
        }
        else if( (file = fopen( filename, "w" )) == NULL )
        {
            char error[1024] ;
            snprintf( error, sizeof( error ), "%s: %s",
                                               filename,
                                               strerror( errno ) ) ;
            write( ns, error, strlen( error ) + 1 ) ;
        }
        else
        {
            write( ns, errors[SUCCESSFUL_FILENAME],
                       strlen( errors[SUCCESSFUL_FILENAME] ) + 1 ) ;

            /*
            ** copy over the file
            */
            while( (len = read( ns, buf, BUFSIZ )) > 0 )
            {
                fwrite( buf, sizeof( char ), len, file ) ;
            }

            /* close up the file */
            fclose( file ) ;

            /*
            ** Either we've reached the eof or a read has failed
            */
            if( len < 0 )
            {
                fprintf( stderr, "read: %s\n", strerror( errno ) ) ;
            }

        }

        /*
        ** EOF as been reached (client has closed the connection).
        ** Close the connection socket and resume listening on the
        ** original socket.
        */
        close( ns ) ;
    }
}
