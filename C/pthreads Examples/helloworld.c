#include <pthread.h> 
#include <stdio.h> 
#include <string.h> 
#include <unistd.h> 
#include <assert.h>  

void* hello_thread( void* arg ) {
    printf( "Hello, " );
}

void* world_thread( void* arg ) {
    int n;
    pthread_t tid = (pthread_t)arg;

    if( n = pthread_join( tid, NULL ) ) {
        fprintf( stderr, "pthread_join: %s\n", strerror( n ) );
        exit( 1 );
    }

    printf( "world\n" );
}

main( int argc, char *argv[] ) {
    int n;
    pthread_t htid, wtid;

    if( n = pthread_create( &htid, NULL, hello_thread, NULL ) ) {
        fprintf( stderr, "pthread_create: %s\n", strerror( n ) );
        exit( 1 );
    }

    if( n = pthread_create( &wtid, NULL, world_thread, (void *) htid ) ) {
        fprintf( stderr, "pthread_create: %s\n", strerror( n ) );
        exit( 1 );
    }

    // wait for thread to finish (which waits on the hello thread)
    if( n = pthread_join( wtid, NULL ) ) {
        fprintf( stderr, "pthread_join: %s\n", strerror( n ) );
        exit( 1 );
    }

    return( 0 );
}
