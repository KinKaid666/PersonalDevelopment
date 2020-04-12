/* File:        $Id: sendfile-tcp.c,v 1.2 2002/12/23 00:32:08 etf2954 Exp $
** Author:      Eric Ferguson
** Description: A simple file transfer program.
** Revision:
**              please use `rlog <filename>`
*/

#define HOST            1
#define PORT            2
#define FILENAME        3
#define BUFLEN          1024

#include <stdarg.h>
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>

#include <errno.h>
#include <netdb.h>
#include <netinet/in.h>
#include <strings.h>
#include <sys/types.h>
#include <sys/socket.h>

/*
** Name:        exit_with_errors
** Description: seudo printf that printfs fmt and the args to stderr
** Arguments:   fmt     format string used in *printf
**              ...     args to fill in fmt
**
*/
void exit_with_errors( char *fmt, ... )
{
    va_list args ;

    if( fmt == NULL ) return ;
    va_start( args, fmt ) ;
    vfprintf( stderr, fmt, args) ;
    va_end( args ) ;

    exit( EXIT_FAILURE ) ;
}

/*
** M A I N
*/
int main( int ac, char **av )
{
    FILE    *file ;                     /* file to be sent */
    char    *filename ;                 /* filename to be sent */
    u_int   filename_len, i ;           /* filename length */

    int                s ;              /* file descriptor for the socket */
    struct sockaddr_in server ;         /* server's ip address */
    int                port ;           /* server port number */
    struct hostent     *host ;          /* network host entry */

    char buf[BUFLEN] ;                  /* buffer for reading */
    int  error = 0  ;                   /* read error msg after filename */

    if( ac != 4 )
        exit_with_errors( "usage: sendfile-tcp remote-host port filename\n" ) ;

    if( (file = fopen( av[FILENAME], "r" )) == NULL )
    {
        perror( av[FILENAME] ) ;
        exit( EXIT_FAILURE ) ;
    }

    /*
    ** Get the filename
    */
    filename = av[FILENAME] + strlen(av[FILENAME]) ;
    while( filename > av[FILENAME] )
    {
        if( *(filename-1) == '/' )
            break ;
        filename-- ;
    }
    filename_len = strlen( filename ) ;

    if( filename_len == 0 )
    {
        fclose( file ) ;
        exit_with_errors( "'%s' is not a file\n", av[FILENAME] ) ;
    }
    else if( filename_len > 512 )
    {
        fclose( file ) ;
        exit_with_errors( "filename exceeds maximun file length\n" ) ;
    }

    /*
    ** setup net
    */
    port = atoi( av[PORT] ) ;
    if( port == 0 )
    {
        fclose( file ) ;
        exit_with_errors( "%s: invalid port\n", av[PORT] ) ;
    }

    if( (s = socket( AF_INET, SOCK_STREAM, 0 )) < 0 )
    {
        fclose( file ) ;
        exit_with_errors( "socket: %s\n", strerror( errno ) ) ;
    }

    memset( &server, 0, sizeof( struct sockaddr_in ) ) ;
    server.sin_family = AF_INET ;
    server.sin_port   = htons( port ) ;

    /* perform the dns lookup */
    if( (host = gethostbyname( av[1] )) == NULL )
    {
        fclose( file ) ;
        exit_with_errors( "bad host" ) ;
    }

    memcpy(&server.sin_addr, host->h_addr_list[0], host->h_length);
    if( connect( s, (struct sockaddr *)&server, sizeof( struct sockaddr )) < 0 )
    {
        fclose( file ) ;
        exit_with_errors( "connect: %s\n", strerror( errno ) ) ;
    }

    /* write our filename */
    if( write( s, filename, filename_len + 1 ) != (filename_len + 1) )
        fprintf( stderr, "write: %s\n", strerror( errno ) ) ;

    /*
    ** Check for errors, if there are errors, make sure we get 'em all
    */
    while( (i = read( s, buf, BUFLEN - 1 )) > 0 )
    {
        if( strlen(buf) > 0 )
        {
            error = 1 ;
            buf[i] = '\0' ;
            write( 2, buf, i ) ;
        }
        else
            break ;
    }

    if( error )
    {
        write( 2, "\n", 1 ) ;
        fclose(file) ;
        close(s) ;
        exit( EXIT_FAILURE ) ;
    }

    while( (i = fread( buf, sizeof( char ), BUFLEN, file )) > 0 )
    {
        if( write( s, buf, i ) != i )
            fprintf( stderr, "write: %s\n", strerror( errno ) ) ;
    }

    fclose( file ) ;
    close( s ) ;
    return EXIT_SUCCESS ;
}
