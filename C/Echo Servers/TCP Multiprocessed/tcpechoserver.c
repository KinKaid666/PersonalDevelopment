#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include <errno.h>

#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>


#define PORT_MIN	1024
#define PORT_MAX	65535

/*
** Name:	processConnection
**
** Description:	Child process section of the code (will be used for each 
**		connection).
**
** Arguments:	ns:	the socket
*/
void processConnection( int ns )
{
    char	buf[BUFSIZ] ;		/* Data buffer */
    int		len ;			/* Buffer length */

    /*
    ** Now read and respond to messages until eof is reached,
    ** which means the client has closed the connection.
    */
    while( (len = read( ns, buf, sizeof( buf ) - 1 )) > 0 )
    {

	int i ;
	for( i = 0 ; i < len ; i++ )
	{
	    if( (buf[i] >= 'A') && (buf[i] <= 'Z') )
		buf[i] = tolower( buf[i] ) ;
	    else
		buf[i] = toupper( buf[i] ) ;
	}

	/* 
	** The arguments to read only allow it to fill up all but one
	** character, let's make sure and send back a null-terminated
	** string
	*/
	buf[len] = '\0' ;

	/* Send it back to the client */
	if( write( ns, buf, len + 1 ) != (len + 1) )
	{
	    fprintf( stderr, "write: %s\n", strerror( errno ) ) ;
	    exit( EXIT_FAILURE ) ;
	}
    }

    /*
    ** Either we've reached the eof or a read has failed
    */
    if( len < 0 )
    {
	fprintf( stderr, "read: %s\n", strerror( errno ) ) ;
	exit( EXIT_FAILURE ) ;
    }
}

int main( int ac, char **av )
{

    int			s ;		/* File descriptor for the socket */
    struct sockaddr_in	server ;	/* Server's IP address	*/
    int			port ;		/* Server's port number	*/

    /*
    ** Make sure the arguments are correct
    */
    if( ac < 2 )
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
    server.sin_family	= AF_INET ;

    /* Use htons (host to network) to correct for byte order */
    server.sin_port	= htons( port ) ;

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
    if( listen( s, 1 ) < 0 )
    {
	fprintf( stderr, "listen: %s\n", strerror( errno ) ) ;
	exit( EXIT_FAILURE ) ;
    }

    /*
    ** Begin accepting network connections
    */
    for( ; ; )
    {
	struct		sockaddr_in client ;	/* Client's IP adderss */
	socklen_t 	client_len ;		/* Length of client's addr */
	int		ns ;			/* New socket for connection */

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
	switch( fork() )
	{
	    case	-1 :	/* Error case */
		perror( "fork" ) ;
		exit( EXIT_FAILURE );
	    case 	 0 :	/* Child process */
		close( s ) ;
		processConnection( ns ) ; 
		exit( EXIT_SUCCESS ) ;
	    default	:	/* Parent process */
		break ;
	}

	/*
	** Close the connection socket and resume listneing on the
	** original socket.
	*/
	close( ns ) ;
    }
}
