#include "list.h"

#include <stdlib.h>

/*
** Ordered Linked list insert
*/
void insert( lnode_t **head, lnode_t *node, lnode_cmp cmp )
{
    lnode_t *current ;
    if( head == NULL || node == NULL ) return ;

    while( (current = *head) != NULL && cmp(node,current) >= 0 )
        head = &current->next ;
    node->next = current ;
    *head = node ;
}

void free_list( lnode_t *list, free_func _free )
{
    while( list != NULL )
    {
        lnode_t *old = list ;
        list = list->next ;
        _free(old->data) ;
        free(old) ;
    }
}

lnode_t* reverse( lnode_t *list )
{
    lnode_t *previous = NULL,
            *current  = NULL ;
    
    while( list != NULL )
    {
        current       = list ;
        list          = list->next ;
        current->next = previous ;
        previous      = current ;
    }
    return current ;
}
