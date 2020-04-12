#include <GLUT/glut.h>         /* glut.h includes gl.h and glu.h*/
#include <time.h>
       
#define S_ITERATIONS    1000000
#define SCREENHEIGHT    500
#define SCREENWIDTH     500
       
#define drawDot( x, y ) glBegin( GL_POINTS ) ;  \
                            glVertex2i( x, y ) ;\
                        glEnd() ;

typedef struct glintPoint 
{
    GLint x,y ;
} GLintPoint ;
    
void Sierpinski() 
{
    GLintPoint T[3] = {{10,10},{300,30},{200, 300}};
    int i ;
    int index = rand()%3;         
    GLintPoint point = T[index]; 
    drawDot(point.x, point.y);  
    for( i = 0; i < S_ITERATIONS; i++)
    {
         index = rand() %3 ;
         point.x = (point.x + T[index].x) / 2;
         point.y = (point.y + T[index].y) / 2;
         drawDot(point.x,point.y);  
    } 
    glFlush();  
}


/*
** Display our clock
*/
void display( void )
{

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
    gluOrtho2D( 0.0, 300.0, 0.0, 300.0 );

    Sierpinski() ;
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
** Main routine - GLUT setup and initialization
*/
int main( int ac, char **av )
{    
    srand(time(NULL)) ;
    glutInit( &ac, av );
    glutInitDisplayMode( GLUT_DOUBLE | GLUT_RGB );
    glutInitWindowSize( SCREENWIDTH, SCREENHEIGHT );
    glutInitWindowPosition( 0, 0 );
    glutCreateWindow( av[0] );
    
    /* register our callback functions */
    glutDisplayFunc( display );
    
    /* LETS GET IT ON! :) */
    glutMainLoop();
    return 0;
  
}

