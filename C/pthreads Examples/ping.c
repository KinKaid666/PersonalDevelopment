/*******************************************************************************
** ping2.C
**
** Simple example or two threads ping'n back and forth
** With sytem scope.
**
*******************************************************************************/
#include <pthread.h>
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>

void*
threadfunc1( void *x )
{
    int i ;
    for( i = 0 ; i < 5 ; i++ ) 
    {
        printf( "thread 1: %i\n", i ) ;
        sleep( 1 ) ;  /* force a yield */
    }
}

void*
threadfunc2( void *x )
{
    int i ;
    for( i = 0 ; i < 5 ; i++ ) 
    {
        printf( "thread 2: %i\n", i ) ;
        sleep( 1 ) ;  /* force a yield */
    }
}

int
main( int ac, char **av )
{
    pthread_t thread1, thread2 ;
    pthread_attr_t attr ;

    pthread_attr_init( &attr ) ;
    pthread_attr_setscope( &attr, PTHREAD_SCOPE_PROCESS ) ;
    pthread_attr_setschedpolicy( &attr, SCHED_OTHER ) ;

    pthread_create( &thread1, &attr, threadfunc1, NULL ) ;
    pthread_create( &thread2, &attr, threadfunc2, NULL )  ;

    pthread_join( thread1, NULL ) ;
    pthread_join( thread2, NULL ) ;
    return EXIT_SUCCESS ;
}
