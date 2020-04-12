#include <GLUT/glut.h>
#include <stdlib.h>
#include <math.h>

#define SETPIXEL(x,y)   glBegin( GL_POINTS ) ;   \
                            glVertex2i( x, y ) ; \
                        glEnd() ;

/* 
** Name:        edge_t
**
** Description: Object that will hold edge objects for edge table used in 
**              scanline polygon fill algorithm
*/
typedef struct edge
{
    int         y_min ;
    float       x_min ;
    int         y_max ;
    float       inv_m ;
    struct edge *next ;
} edge_t ;

enum { Y_MIN, X_MIN } ;

/*
** Name:        s_insert
**
** Description: sorted insert (based on y_min) of edge_t objects
*/
void s_insert( edge_t **head, edge_t *element, int sort_by )
{
    edge_t *iter = *head ;

    if( ( sort_by == Y_MIN ) && (( *head == NULL ) || 
                                ( (*head)->y_min > element->y_min)) ||
        ( sort_by == X_MIN ) && (( *head == NULL ) || 
                                ( (*head)->x_min > element->x_min)) )
    {
        element->next = *head ;
        *head = element ;
    }
    else /* all others */
    {
        if( sort_by == Y_MIN )
        {
            while( (iter->next != NULL) &&
                   (iter->next->y_min < element->y_min) )
            {
                iter = iter->next ;
            }
        }
        else
        {
            while( (iter->next != NULL) &&
                   (iter->next->y_min < element->y_min) )
            {
                iter = iter->next ;
            }
        }
        element->next = iter->next ;
        iter->next = element ;
    }
}

/*
** Name:        get_scanline
**
** Decsription: inserts edges into scanline based sorted by x_min that
**              have the save y_min as the head element 
*/
void get_scanline( edge_t **et, edge_t **scanline, int y_min ) 
{ 
    edge_t **head_et = et ;
    edge_t *iter     = *et ;

    if( *et == NULL ) return ;
    
    /* remove from et and put in scanline */
    while( (iter != NULL) &&
           (iter->y_min == y_min) )
    {
        edge_t *temp = iter->next ;
        iter->next == NULL ;
        s_insert( scanline, iter, X_MIN ) ;
        iter = temp ;
    }

    /* update the new head of et */
    *et = iter ;

}

/*
** Name:        sort_scanline
**
** Decsription: 
*/
void sort_scanline( edge_t **scanline ) 
{ 
    edge_t *sorted_list = NULL ;
    edge_t *iter  = *scanline ;

    /* remove from et and put in scanline */
    while( iter != NULL )
    {
        edge_t *temp = iter->next ;
        iter->next == NULL ;
        s_insert( &sorted_list, iter, X_MIN ) ;
        iter = temp ;
    }
    *scanline = sorted_list ;
}

/*
** Name:        remove_y_max
**
** Decription:  looks at scanline and removes any edges that have reached
**              y_max == y_min
**
** Returns:     1: if 1 or more edges were removed
**              0: if nothing was removed
*/
int remove_y_max( edge_t **scanline, int y_min )
{
    int modified  = 0 ;
    edge_t *iter = *scanline ;

    /* remove head case */
    while( (iter != NULL) &&
           (y_min >= iter->y_max) )
    {
        modified = 1 ;
        *scanline = iter->next ;
        free( iter ) ;
        iter = *scanline ;
    }

    if( iter != NULL ) 
    {
        while( iter->next != NULL )
        {
            if( y_min >= iter->next->y_max )
            {
                edge_t *temp = iter->next ;
                modified = 1 ;
                iter->next = iter->next->next ;
                free( temp ) ;
            }
            else
            {
                iter = iter->next ;
            }
        }
    }
    return modified ;
    
}

/*
** Name:        update_x_scanline
**
** Description: increments x_min by 1/m for every element in the scanline
**
*/
void update_x_scanline( edge_t *scanline )
{
    edge_t *iter = scanline ;
    while( iter != NULL )
    {
        iter->x_min += iter->inv_m ;
        iter = iter->next ;
    }
}

/*
** Name:        print_list
**
** Description: print out edge table (debug tool only)
*/
void print_list( edge_t *head )
{
    edge_t *iter = head ;
    while( iter != NULL )
    {
        printf( "Ymin: %d Xmin: %f Ymax: %d 1/m: %f\n",
                iter->y_min,
                iter->x_min,
                iter->y_max,
                iter->inv_m ) ;
        iter = iter->next ;
    }
}


/* 
** drawPolygon
*/
void drawPolygon( int n, int *x, int *y ) 
{
    int i, x_pix, y_pix ;
    int need_to_update = 0 ;
    float f ;
    edge_t *et = NULL ;
    edge_t *scanline = NULL ;

    /*
    ** Create edge table
    **
    ** Sorted insert (based on y_min) of edge_t objects
    */
    for( i = 0 ; i < n ; i++ )
    {
        int first  = i,
            second = ( i + 1 ) % n ;
        int dy = y[first] - y[second],
            dx = x[first] - x[second] ;
        edge_t *edge = (edge_t *)malloc( sizeof( edge_t )) ;

        if( y[first] < y[second] )
        {
            edge->x_min = x[first] ;
            edge->y_min = y[first] ;
            edge->y_max = y[second] ;
        }
        else
        {
            edge->x_min = x[second] ;
            edge->y_min = y[second] ;
            edge->y_max = y[first] ;
        }

        /* discard horizontal edges */
        if( dy != 0 ) 
        {
            /* force floating point arithemetic */
            edge->inv_m = dx / (dy * 1.0) ;
            s_insert( &et, edge, Y_MIN ) ;
        }
        else
        {
            free( edge ) ;
        }
    }

    if( et != NULL ) y_pix = et->y_min ;
    while( et != NULL )
    {
        get_scanline( &et, &scanline, y_pix ) ;
        need_to_update = 0 ;
        while( !need_to_update )
        {
            sort_scanline( &scanline ) ;

            /* change for more sided polygon */
            i = 0 ;
            for( f = scanline->x_min ; f < scanline->next->x_min ; f++ )
            {
                if( i == 0 )
                {
                    x_pix = ceilf( f ) ;
                    i++ ;
                }
                else
                {
                    x_pix = floorf(f) ;
                }
                SETPIXEL( x_pix, y_pix ) ;
            }
            y_pix++ ;
            need_to_update = remove_y_max( &scanline, y_pix ) ;
            update_x_scanline( scanline ) ;
        }
    }
}
