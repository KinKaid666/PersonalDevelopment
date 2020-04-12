#ifndef __BINARYTREE_H__
#define __BINARYTREE_H__

typedef struct bnode
{
    void         *data ;
    struct bnode *left ;
    struct bnode *right ;
} bnode_t ;

typedef int  (*bt_cmp)(void*,void*) ;
typedef void (*bt_print)(void*) ;

int b_insert( bnode_t **root,
              void    *element,
              bt_cmp  cmp ) ;
int b_height( const bnode_t *root ) ;

void b_free( bnode_t *root, void (*free_element)(void*) ) ;

enum { preorder, inorder, postorder } ;
void b_print( const bnode_t *root, bt_print print, int order ) ;
void b_print_pretty( const bnode_t *root, bt_print print ) ;

#endif

