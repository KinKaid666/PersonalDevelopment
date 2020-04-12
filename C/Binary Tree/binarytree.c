#include <stdio.h>
#include <stdlib.h>
#include "binarytree.h"

int b_insert( bnode_t   **root,
              void      *element,
              bt_cmp    cmp )
{
    bnode_t *iter ;
    if( *root == NULL )
    {
        *root = (bnode_t *)malloc( sizeof(bnode_t) ) ;
        if( *root == NULL )
        {
            fprintf(stderr, "Memory exhausted.\n") ;
            exit(1) ; /* no mem */
        }
        (*root)->data = element ;
        (*root)->left = (*root)->right = NULL ;
        return 1 ;
    }
    iter = *root ;

    if( cmp( element, iter->data ) < 0 )
        return b_insert( &iter->left, element, cmp ) ;
    else if( cmp( element, iter->data ) > 0 )
        return b_insert( &iter->right, element, cmp ) ;
    /* else do nothing, element existed */
    return 0 ;
}

int b_height( const bnode_t *root )
{
    if( root == NULL ) return 0 ;
    else
    {
        int x = b_height( root->left ),
            y = b_height( root->right ) ;
        return 1 + ( x > y ? x : y ) ;
    }
}

void b_free( bnode_t *root, void (*free_element)(void*) )
{
    if( root != NULL )
    {
        b_free( root->left,  free_element ) ;
        b_free( root->right, free_element ) ;
        free_element( root->data ) ;
        free( root ) ;
    }
}

void b_print( const bnode_t *root, bt_print print, int order )
{
    if( root != NULL )
    {
        if( order == preorder  ) print( root->data ) ;
        b_print( root->left, print, order ) ;
        if( order == inorder   ) print( root->data ) ;
        b_print( root->right, print, order ) ;
        if( order == postorder ) print( root->data ) ;

    }
}

void b_print_pretty( const bnode_t *root, bt_print print )
{
    if( root != NULL )
    {
        printf( "{ " ) ;
        print( root->data ) ;
        printf( ", " ) ;
        b_print_pretty( root->left,  print ) ;
        printf(", ") ;
        b_print_pretty( root->right, print ) ;
        printf(" }") ;
    }
    else
    {
        printf("/") ;
    }
}
