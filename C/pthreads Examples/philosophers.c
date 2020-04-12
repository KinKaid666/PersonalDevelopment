/* 
** CC -o philosophers philosophers.C -pthread
**------------------------------------------------------------------------------
** philosophers.C
**      Classic Dining Philosophers Problem (Dykstra)
**------------------------------------------------------------------------------
*/

#include <pthread.h>
#include <unistd.h>
#include <stdio.h>
#include <stdlib.h>
#include <time.h>
#include <assert.h>

#define N_PHILS         5
#define ASYNC_NUMBER    1
#define HOW_HUNGRY      2

/* 
** Declarations of philosopher struct
*/
typedef struct
{
    int id_        ;
    int leftFork_  ;
    int rightFork_ ;
    int hunger_    ;
} philosopher_t    ;

philosopher_t   philosophers[N_PHILS] ;
pthread_mutex_t forks       [N_PHILS] ;
pthread_t       threads     [N_PHILS] ;

/*
** Name:        phil_func
**
** Description: Philosopher picks up a fork, on his left or right, pending
**              his id and the ASYNC_NUMBER and then the other, and eats.
*/
void* phil_func( void* x )
{
    philosopher_t* argPhil = (philosopher_t*)x ;
    while( argPhil->hunger_ )
    {
        if( argPhil->id_ % ASYNC_NUMBER )
        {
            pthread_mutex_lock( &forks[ argPhil->leftFork_  ] ) ;
            pthread_mutex_lock( &forks[ argPhil->rightFork_ ] ) ;
        }
        else
        {
            pthread_mutex_lock( &forks[ argPhil->rightFork_ ] ) ;
            pthread_mutex_lock( &forks[ argPhil->leftFork_  ] ) ;
        }
        printf(
                "Philosopher %d: eating with forks %d & %d.\n",
                argPhil->id_,
                argPhil->leftFork_,
                argPhil->rightFork_
              ) ;

        /* EATING */
        //pthread_yield() ;

        argPhil->hunger_-- ;
        if( argPhil->id_ % ASYNC_NUMBER )
        {
            pthread_mutex_unlock( &forks[ argPhil->leftFork_  ] ) ;
            pthread_mutex_unlock( &forks[ argPhil->rightFork_ ] ) ;
        }
        else 
        {
            pthread_mutex_unlock( &forks[ argPhil->rightFork_ ] ) ;
            pthread_mutex_unlock( &forks[ argPhil->leftFork_  ] ) ;
        }
        printf( "Philosopher %d: thinks\n", argPhil->id_ ) ;

        /* THINKING */
        //pthread_yield() ;
    }
    printf( "Philosopher %d: done\n", argPhil->id_ ) ;
    return NULL ;
}

/*
** Name:        main
*/
int main( int ac, char **av )
{
    int i ;
    assert( ((ASYNC_NUMBER < N_PHILS) && (ASYNC_NUMBER > 0)) ) ;
    assert( HOW_HUNGRY != 0 ) ;
    srand( time( 0 ) ) ;
    for( i = 0 ; i < N_PHILS ; i++ )
    {
        pthread_mutex_init( &forks[i], NULL ) ;
        philosophers[i].id_        = i ;
        philosophers[i].leftFork_  = i ;
        philosophers[i].rightFork_ = (i + 1) % N_PHILS ;
        philosophers[i].hunger_    = rand()  % (N_PHILS*HOW_HUNGRY) ;
    }

    for( i = 0 ; i < N_PHILS ; i++ )
    {
        pthread_create( &threads[i], NULL, phil_func, (void*)&philosophers[i] );
    }

    for( i = 0 ; i < N_PHILS ; i++ )
    {
        pthread_join( threads[i], NULL ) ;
    }
}

