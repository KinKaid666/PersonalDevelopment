#include "list.h"
#include <stdio.h>
#include <stdlib.h>

int int_cmp(const lnode_t *n1, const lnode_t *n2)
{
    int a = *(int*)n1->data, b = *(int*)n2->data ;
    if( a < b )
        return -1 ;
    else if( a > b )
        return 1 ;
    else
        return 0 ;
}

void int_free( void *data )
{
    int *x = (int*)data ;
    free(x) ;
}

void printlist( lnode_t *list )
{
    while( list != NULL )
    {
        printf( "%d ", *(int*)list->data ) ;
        list = list->next ;
    }
    printf("\n") ;
}
int main( int ac, char **av )
{
    lnode_t *list = NULL ;
    int x ;
    while( EOF != scanf("%d", &x) )
    {
        lnode_t *new_item = (lnode_t*)malloc(sizeof(lnode_t*)) ;
        new_item->data = (int*)malloc(sizeof(int)) ;
        *((int*)new_item->data) = x ;
        insert( &list, new_item, int_cmp ) ;
    }
    printlist( list ) ;
    printlist( reverse(list) ) ;

    free_list( list, int_free) ;
}
