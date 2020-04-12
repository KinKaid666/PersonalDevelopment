#ifndef __LIST_H__
#define __LIST_H__

typedef struct lnode
{
    void *data ;
    struct lnode *next ;
} lnode_t ;

typedef int (* lnode_cmp)( const lnode_t*, const lnode_t* ) ;
typedef void (*free_func)( void * ) ;

void insert( lnode_t**, lnode_t*, lnode_cmp ) ;
void free_list( lnode_t*, free_func ) ;
lnode_t *reverse( lnode_t* ) ;

#endif /* __LIST_H__ */

