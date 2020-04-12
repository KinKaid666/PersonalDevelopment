/*------------------------------------------------------------------------------
** ping.C
** Simple example or two threads ping'n back and forth with the aid of a mutex
*-----------------------------------------------------------------------------*/

#include <pthread.h>
#include <unistd.h>
#include <stdio.h>
#include <stdlib.h>

#if defined(__APPLE__) || defined(__MACH__)
#define pthread_yield() pthread_yield_np()
#endif

void* threadfunc1( void* x )
{
    int i ;
    pthread_mutex_t* mutex = (pthread_mutex_t*)x ;

    pthread_mutex_lock( mutex ) ;
    for( i = 0 ; i < 5 ; i++ )
    {
        if( i == 2 )
        {
            pthread_mutex_unlock( mutex ) ;
        }
        printf( "thread 1: %i\n", i ) ;
        pthread_yield() ;
    }
    return (void*) NULL ;
}

void* threadfunc2( void* x )
{
    int i ;
    pthread_mutex_t* mutex = (pthread_mutex_t*)x ;

    pthread_mutex_lock( mutex ) ;
    for( i = 0 ; i < 5 ; i++ )
    {
        printf( "thread 2: %i\n", i ) ;
        pthread_yield() ;
    }
    pthread_mutex_unlock( mutex ) ;
    return (void*) NULL ;
}

int main( int ac, char **av )
{
    pthread_t thread1, thread2 ;
    pthread_mutex_t mutex ;

    pthread_mutex_init( &mutex, NULL ) ;

    pthread_create( &thread1, NULL, threadfunc1, (void*)&mutex ) ;
    pthread_create( &thread2, NULL, threadfunc2, (void*)&mutex ) ;

    pthread_join( thread1, NULL ) ;
    pthread_join( thread2, NULL ) ;
    pthread_mutex_destroy( &mutex ) ;

    return EXIT_SUCCESS ;
}
