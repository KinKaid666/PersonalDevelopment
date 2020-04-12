#include <GLUT/glut.h>
#include <stdlib.h>
#include "drawLine.h"

/* 
 * Draw line from x1, y1 to x2, y2 
*/
#define SETPIXEL( x, y )        glBegin( GL_POINTS ) ;  \
                                    glVertex2i( x, y ) ;\
				glEnd() ;

void drawLine( int x1, int y1, int x2, int y2) 
{
    int dx, dy ;
    if( x1 > x2 )
    {
        int temp ;
        temp = x2 ;
        x2 = x1 ;
        x1 = temp ;

        temp = y1 ;
        y1 = y2 ;
        y2 = temp ;
    }   

    dx = x2 - x1 ;
    dy = y2 - y1 ;

    if( dx == 0 )
    {
        for( ; y1 != y2 ; ) 
        {
            SETPIXEL( x1, y1 ) 
            if( dy < 0 )
                y1-- ;
            else
                y1++ ;
        } 
    }
    else if( dy == 0 )
    {
        for( ; x1 != x2 ; ) 
        {
            SETPIXEL( x1, y1 ) 
            if( dx < 0 )
                x1-- ;
            else
                x1++ ;
        }
    }
    else if( dx == dy )
    {
        for( ; x1 < x2 && y1 < y2 ; x1++, y1++ )
        {
            SETPIXEL( x1, y1 ) 
        }
    }
    else if( dx == (dy*-1) )
    {
        for( ; x1 < x2 && y1 > y2 ; x1++, y1-- )
        {
            SETPIXEL( x1, y1 ) 
        }
    }
    else if( dx > abs( dy ) )
    {
        if( dy > 0 )
        {
            int w = x2 - x1,
                h = y2 - y1,
                f = ( 2 * h ) - w ;

            for( ; x1 < x2 ; x1++ )
            {
                SETPIXEL( x1, y1 ) ;
                if( f < 0 )
                {
                    f += 2 * h ;
                }
                else
                {
                    y1++ ;
                    f += 2 * ( h - w ) ;
                }
            }
        }
        else
        {
            int w = x2 - x1,
                h = y2 - y1,
                f = ( 2 * h ) + w ;
            for( ; x1 < x2 ; x1++ )
            {
                SETPIXEL( x1, y1 ) 
                if( f < 0 )
                {
                    y1-- ;
                    f += 2 * ( h + w ) ;
                }
                else
                {
                    f += 2 * h ;
                }
            }
        }
    }
    else
    {
        if( dy > 0 )
        {
            int w = x2 - x1,
                h = y2 - y1,
                f = h - ( 2 * w ) ;

            for( ; y1 < y2 ; y1++ )
            {
                SETPIXEL( x1, y1 ) ;
                if( f < 0 )
                {
                    x1++ ;
                    f += 2 * ( h - w ) ;
                }
                else
                {
                    f += -2 * w ;
                }
            }
        }
        else
        {
            int w = x2 - x1,
                h = y2 - y1,
                f = ( 2 * w ) + h ;

            for( ; y1 > y2 ; y1-- )
            {
                SETPIXEL( x1, y1 ) ;
                if( f < 0 )
                {
                    f += 2 * w ;
                }
                else
                {
                    x1++ ;
                    f += 2 * ( h + w ) ;
                }
            }
        }
    }

    /* Don't forget the last point */
    SETPIXEL( x2, y2 ) 
}
