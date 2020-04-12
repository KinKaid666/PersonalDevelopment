#include <GLUT/glut.h>         /* glut.h includes gl.h and glu.h*/
#include <math.h>
#include <stdio.h>
#include <stdlib.h>
#include <time.h>
       
#define SCREENHEIGHT    200
#define SCREENWIDTH     500

int    O_koch = 0 ;
double old_x  = 0 ;
double old_y  = 0 ;

void setRel( double x, double y ) { old_x = x ; old_y = y ; }

void lineRel( double x, double y )
{
    glBegin( GL_LINES ) ;
      glVertex2f(      old_x,      old_y ) ;
      glVertex2f( old_x += x, old_y += y ) ;
    glEnd() ;
}

void drawKoch(double dir, double len, int n)
{
    // Koch to order n the line of length len
    // from CP in the direction dir

    double dirRad = 0.0174533 * dir ; // in radians

    if(n == 0)
    {
        lineRel( len * cos(dirRad), len * sin(dirRad) ) ;
    }
    else
    {
        n-- ;                   // reduce the order
        len /= 3 ;              // and the length
        drawKoch(dir, len, n) ;
        dir += 60 ;
        drawKoch(dir, len, n) ;
        dir -= 120 ;
        drawKoch(dir, len, n) ;
        dir += 60 ;
        drawKoch(dir, len, n) ;
    }
}

/*
** string display function
*/
void displayText( GLint x, GLint y, char *s, GLfloat r, GLfloat g, GLfloat b )
{
    glColor3f( r, g, b ) ;
    glRasterPos2i( x, y ) ;
    for(; *s != '\0' ; s++ )
    {
        if( *s == '\n' )
        {
            glRasterPos2i( x, y - 15 ) ;
        }
        else
        {
            glutBitmapCharacter( GLUT_BITMAP_TIMES_ROMAN_24, *s ) ;
        }
    }
}

/*
** Simple display function to call drawKock
*/
void display( void )
{

    char   str[128] ;
    time_t start, stop ;

    /*
    ** Set clear color to black
    */
    glClearColor ( 0.0, 0.0, 0.0, 0.0 );

    /*
    ** Clear the current buffer
    */
    glClear( GL_COLOR_BUFFER_BIT );

    /*
    ** Get ready for glOrtho2D
    */
    glMatrixMode( GL_PROJECTION );
    glLoadIdentity( );

    /*
    ** Display original world by setting the viewWindow
    ** using gluOrtho2D.
    */
    gluOrtho2D( -20.0, 220.0, -20.0, 90.0 );

    setRel( 0, 0 ) ;
    start = time(NULL) ;
    drawKoch( 0, 200, O_koch ) ;
    stop = time(NULL) ;

    snprintf( str, sizeof( str ),
              "Order: %d\nRender time = %g second(s)",
              O_koch,
              difftime( stop, start ) ) ;
    displayText( -20, 80, str, 1, 1, 1 ) ;

    /*
    ** Set viewport - In this case, the whole
    ** screen, although window resizing has not
    ** been handled
    */
    glViewport( 0, 0, SCREENWIDTH, SCREENHEIGHT );

    /*
    ** Swap out the buffer we have been writing to and begin
    ** writing to the other 
    */
    glutSwapBuffers();
}

/*
** Keyboard callback function
*/
void keyboard( unsigned char key, int x, int y )
{
    exit(0) ;
}


/*
** Callback function for mouse interaction
*/
void mouse( int button, int state, int x, int y )
{

    /*
    ** using state GLUT_UP so we don't get a double increment on a mouse
    ** click
    */
    if( (button == 0) && (state == GLUT_UP) )
    {
        O_koch++ ;
        glutPostRedisplay();
    }
    else if( (button == 2) && (state == GLUT_UP) )
    {
        O_koch = 0 ;
        glutPostRedisplay();
    }
}

/*
** Main routine - GLUT setup and initialization
*/
int main( int ac, char **av )
{
    glutInit( &ac, av ) ;
    glutInitDisplayMode( GLUT_DOUBLE | GLUT_RGB ) ;
    glutInitWindowSize( SCREENWIDTH, SCREENHEIGHT ) ;
    glutInitWindowPosition( 0, 0 ) ;
    glutCreateWindow( "Koch's algorithm, hit any key to exit" ) ;

    /* register our callback functions */
    glutDisplayFunc ( display  ) ;
    glutKeyboardFunc( keyboard ) ;
    glutMouseFunc   ( mouse    ) ;

    /* LETS GET IT ON! :) */
    glutMainLoop() ;
    return 0 ;

}

