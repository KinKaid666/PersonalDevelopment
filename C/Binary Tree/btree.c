#include <limits>
#include <iostream>

#include <signal.h>     /* signal */
#include <stdio.h>
#include <stdlib.h>
#include <time.h>
#include <unistd.h>     /* alarm */

#include "binarytree.h"

#define DEFAULT_TEST_TREE_SIZE  10

bool stop_generating = false ;

void print_int_nl( void *x ) 
{
    printf( "%d\n", *(int *)x ) ;
}

void print_int( void *x ) 
{
    printf( "%d", *(int *)x ) ;
}

int cmp_ints( void *x, void *y )
{
    if( *(int *)x < *(int *)y ) { return -1 ;
    }
    else if( *(int *)x > *(int *)y )
    {
        return 1 ;
    }
    else
    {
        return 0 ;
    }
}

void handler( int sig )
{

    psignal( sig, "\nNumber generation for insert stopped, caught signal" ) ;
    stop_generating = true ;
}

int main( int ac, char **av )
{
    bnode_t *root = NULL ;
    srand(time(NULL)) ;
    signal( SIGINT, handler ) ;

    int size = (ac > 1 ) ? atoi(av[1]) : DEFAULT_TEST_TREE_SIZE ;
    if( ac == 1 )
    {
        printf("No default number of inserts given, hit Ctrl-c to stop number generation\n") ;
    }
    int *y ;
    unsigned long long insert_attempts = 0 ;
    unsigned long long insert_count    = 0 ;
    while( !stop_generating )
    {
        y = (int *)malloc( sizeof(int) ) ;
        *y = rand() % std::numeric_limits<int>::max() ;

        ++insert_attempts ;
        if( b_insert( &root, y, &cmp_ints ) )
        {
            if( ++insert_count % 1000000 == 0 )
            {
                std::clog << "." ; /* you best be printing this */
            }
        }

        if( ac > 1 && insert_attempts >= size )
        {
            stop_generating = true  ;
        }
    }
    printf("\n") ;

    printf( "Preorder Depth-First\n" ) ;
    b_print( root, print_int_nl, preorder  ) ;
    printf( "\nInorder Depth-First\n" ) ;
    b_print( root, print_int_nl, inorder   ) ;
    printf( "\nPostorder Depth-First\n" ) ;
    b_print( root, print_int_nl, postorder ) ;

    printf( "Inserted %llu unique values, out of %llu attempts (%2.2f%%)\n",
            insert_count,
            insert_attempts,
            ((insert_count * 1.0)/(insert_attempts * 1.0) * 100.00) ) ;
    printf( "Height of tree = %d\n",
            b_height( root ) ) ;

    printf( "\nTree looks like this:\n") ;
    b_print_pretty( root, print_int ) ;
    printf( "\n\n") ;

    b_free( root, free ) ;
    return EXIT_SUCCESS ;
}
