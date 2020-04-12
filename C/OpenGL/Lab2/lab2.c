#include <GLUT/glut.h>         /* glut.h includes gl.h and glu.h*/
#include <time.h>
       
#define SCREENHEIGHT    500
#define SCREENWIDTH     500
int num_clicks = 0 ;
       
void displayText( GLint x, GLint y, char *s, GLfloat r, GLfloat g, GLfloat b )
{
    glColor3f( r, g, b ) ;
    glRasterPos2i( x, y ) ;
    for(; *s != '\0' ; s++ ) 
    {
        glutBitmapCharacter( GLUT_BITMAP_TIMES_ROMAN_24, *s ) ;
    }
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
    if( state == GLUT_UP )
    {
        glutPostRedisplay();
        num_clicks++ ;
    }
}
       
       
/*
** Name:        drawClock
** Description: draws a standard analog clock
*/
void drawClock( int hour, int minute, int second )
{
    int index ;

       
    /* Setup a checkered pattern for our icon in the center of clock */
    GLubyte pattern [] = 
    {
        0xaa,0xaa,0xaa,0xaa,0xaa,0xaa,0xaa,0xaa,
        0x55,0x55,0x55,0x55,0x55,0x55,0x55,0x55,
        0xaa,0xaa,0xaa,0xaa,0xaa,0xaa,0xaa,0xaa,
        0x55,0x55,0x55,0x55,0x55,0x55,0x55,0x55,
        0xaa,0xaa,0xaa,0xaa,0xaa,0xaa,0xaa,0xaa,
        0x55,0x55,0x55,0x55,0x55,0x55,0x55,0x55,
        0xaa,0xaa,0xaa,0xaa,0xaa,0xaa,0xaa,0xaa,
        0x55,0x55,0x55,0x55,0x55,0x55,0x55,0x55,
        0xaa,0xaa,0xaa,0xaa,0xaa,0xaa,0xaa,0xaa,
        0x55,0x55,0x55,0x55,0x55,0x55,0x55,0x55,
        0xaa,0xaa,0xaa,0xaa,0xaa,0xaa,0xaa,0xaa,
        0x55,0x55,0x55,0x55,0x55,0x55,0x55,0x55,
        0xaa,0xaa,0xaa,0xaa,0xaa,0xaa,0xaa,0xaa,
        0x55,0x55,0x55,0x55,0x55,0x55,0x55,0x55,
        0xaa,0xaa,0xaa,0xaa,0xaa,0xaa,0xaa,0xaa,
        0x55,0x55,0x55,0x55,0x55,0x55,0x55,0x55,
    } ;
 
    /* 
    ** glPushMatrix & glPopMatrix to save the state of our Matrix
    ** so that when we Transfor/Rotate/Scale our matrix we are able
    ** to restore it it, to it's previous state
    */
    glPushMatrix() ;

    /* draw the hashes for the minutes */
    glColor3f( 0.4, 0.4, 0.4 ) ;
    for( index = 0 ; index < 60 ; index++ )
    {
        glRects( -1, 160, 1, 170 ) ;
        glRotatef( 6, 0, 0, 1.0 ) ;
    }

    /* draw the hashes for the hours */
    glColor3f( 0.8, 0.8, 0.8 ) ;
    for( index = 0 ; index < 12 ; index++ )
    {
        glRects( -4, 140, 4, 170 ) ;
        glRotatef( 30, 0, 0, 1.0 ) ;
    } 
    glPopMatrix() ;
       
    /* draw a little icon in the middle of our clock */
    /* Enable polygon stipple */
    glEnable( GL_POLYGON_STIPPLE );
    glColor3f( 0.0, 1.0, 1.0 ) ;
    glPolygonStipple( pattern );
    glBegin( GL_TRIANGLES ) ;
      glVertex2f( -10,   0 ) ;
      glVertex2f(  10,   0 ) ;
      glVertex2f(   0, -20 ) ;
      glVertex2f( -10,   0 ) ;
      glVertex2f(  10,   0 ) ;
      glVertex2f(   0,  20 ) ;
    glEnd() ;
    glDisable( GL_POLYGON_STIPPLE );

    /* draw a border around our clock */
    glEnable( GL_LINE_STIPPLE ) ;
    glLineStipple( 1, 0xaaaa ) ;
    glBegin( GL_LINE_LOOP ) ;
      glLineWidth( 4 ) ;
      glVertex2f( -198, -198 ) ;
      glVertex2f( -198,  198 ) ;
      glVertex2f(  198,  198 ) ;
      glVertex2f(  198, -198 ) ;
    glEnd() ;
    glDisable( GL_LINE_STIPPLE );

    /* draw curtin */
    /* 2 using GL_QUADS */
    glBegin( GL_QUADS ) ;
      glVertex2f( -198, -198 ) ;
      glVertex2f( -198, -168 ) ;
      glVertex2f( -188, -188 ) ;
      glVertex2f( -168, -198 ) ;
      glVertex2f( -198,  198 ) ;
      glVertex2f( -198,  168 ) ;
      glVertex2f( -188,  188 ) ;
      glVertex2f( -168,  198 ) ;
    glEnd() ;

    /* 1 using GL_QUAD_STRIP */
    glBegin( GL_QUAD_STRIP ) ;
      glVertex2f(  198, -198 ) ;
      glVertex2f(  198, -168 ) ;
      glVertex2f(  188, -188 ) ;
      glVertex2f(  198, -198 ) ;
      glVertex2f(  168, -198 ) ;
      glVertex2f(  188, -188 ) ;
    glEnd() ;

    /* 1 using GL_TRIANGLE_STRIP */
    glBegin( GL_TRIANGLE_STRIP ) ;
      glVertex2f(  198,  168 ) ;
      glVertex2f(  198,  198 ) ;
      glVertex2f(  188,  188 ) ;
      glVertex2f(  168,  198 ) ;
    glEnd() ;

    /* draw a blue stone at the 12 hour mark */
    glBegin( GL_TRIANGLE_FAN ) ;
      glColor3f( 0.0, 0.5, 1.0 ) ;
      glVertex2f(   0, 115 ) ;
      glVertex2f(   5, 130 ) ;
      glVertex2f(  15, 120 ) ;
      glVertex2f(  15, 110 ) ;
      glVertex2f(   5, 100 ) ;
      glVertex2f(  -5, 100 ) ;
      glVertex2f( -15, 110 ) ;
      glVertex2f( -15, 120 ) ;
      glVertex2f(  -5, 130 ) ;
      glVertex2f(   5, 130 ) ;
    glEnd() ;

    /* draw the Hour Hand */
    glPushMatrix() ;
    glRotatef( 360-((hour*30)+(minute*.5)), 0, 0, 1.0 ) ;
    glBegin( GL_POLYGON ) ;
      glColor3f( 0.0, 1.0, 0.2 ) ;
      glVertex2f(  0,  0 ) ;
      glVertex2f( -5, 60 ) ;
      glVertex2f(  0, 90 ) ;
      glVertex2f(  5, 60 ) ;
    glEnd() ;

    /* draw a border around the hand using GL_LINE_STRIP */
    glBegin( GL_LINE_STRIP ) ;
      glColor3f( 0.0, 0.0, 0.0 ) ;
      glVertex2f(  0,  0 ) ;
      glVertex2f( -5, 60 ) ;
      glVertex2f(  0, 90 ) ;
      glVertex2f(  5, 60 ) ;
      glVertex2f(  0,  0 ) ;
    glEnd() ;
    glPopMatrix() ;

    /* draw the Minute Hand */
    glPushMatrix() ;
    glRotatef( 360-(minute*6), 0, 0, 1.0 ) ;
    glBegin( GL_POLYGON ) ;
      glColor3f( 0.0, 1.0, 0.2 ) ;
      glVertex2f(  0,   0 ) ;
      glVertex2f( -5, 120 ) ;
      glVertex2f(  0, 170 ) ;
      glVertex2f(  5, 120 ) ;
    glEnd() ;

    /* draw a border around the hand using GL_LINES */
    glBegin( GL_LINES ) ;
      glColor3f( 0.0, 0.0, 0.0 ) ;
      glVertex2f(  0,   0 ) ;
      glVertex2f( -5, 120 ) ;
      glVertex2f( -5, 120 ) ;
      glVertex2f(  0, 170 ) ;
      glVertex2f(  0, 170 ) ;
      glVertex2f(  5, 120 ) ;
      glVertex2f(  0,   0 ) ;
    glEnd() ;
    glPopMatrix() ;

    /* draw the second Hand */
    glPushMatrix() ;
    glRotatef( 360-(second*6), 0, 0, 1.0 ) ;
    glBegin( GL_POLYGON ) ;
      glColor3f( 1.0, 0.0, 0.0 ) ;
      glVertex2f( -1,   0 ) ;
      glVertex2f( -1, 170 ) ;
      glVertex2f(  1, 170 ) ;
      glVertex2f(  1,   0 ) ;
    glEnd() ;
    glPopMatrix() ;
}

/*
** Display our clock
*/
void display( void )
{

    /* get the current minutes, and current hour for clock */
    time_t t = time( NULL ) ;
    struct tm *current_time = localtime( &t ) ;
    int current_hour   = current_time->tm_hour%12 ;
    int current_minute = current_time->tm_min ;
    int current_second = current_time->tm_sec ;

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
    gluOrtho2D( -200.0, 200.0, -200.0, 200.0 );

    /*
    ** Set viewport - In this case, the whole
    ** screen, although window resizing has not
    ** been handled
    */
    switch( num_clicks % 5 )
    {
    case 0: /* draw in complete window */
           glViewport(             0,              0, 
                         SCREENWIDTH, SCREENHEIGHT );
           drawClock(current_hour, current_minute, current_second);
           break ;

    case 1: /* draw in bottom left in NY time */
           glViewport(             0,              0, 
                       SCREENWIDTH/2, SCREENHEIGHT/2 );
           displayText( -185, -185, "Buffalo", 0.4, 0.4, 0.4 ) ;
           /* draw the clock now that our viewport is setup */
           drawClock(current_hour, current_minute, current_second);
           break ;

    case 2: /* draw in bottom right in GMT */
           glViewport(             0,              0, 
                       SCREENWIDTH/2, SCREENHEIGHT/2 );
           displayText( -185, -185, "Buffalo", 0.4, 0.4, 0.4 ) ;
           drawClock(current_hour, current_minute, current_second);
           glViewport( SCREENWIDTH/2,              0, 
                       SCREENWIDTH/2, SCREENHEIGHT/2 );
           displayText( -185, -185, "London", 0.4, 0.4, 0.4 ) ;
           drawClock(current_hour + 5, current_minute, current_second);
           break ;

    case 3: /* draw in upper right in Tokyo time*/
           glViewport(             0,              0, 
                       SCREENWIDTH/2, SCREENHEIGHT/2 );
           displayText( -185, -185, "Buffalo", 0.4, 0.4, 0.4 ) ;
           drawClock(current_hour, current_minute, current_second);
           glViewport( SCREENWIDTH/2,              0, 
                       SCREENWIDTH/2, SCREENHEIGHT/2 );
           displayText( -185, -185, "London", 0.4, 0.4, 0.4 ) ;
           drawClock(current_hour + 5, current_minute, current_second);
           glViewport( SCREENWIDTH/2, SCREENHEIGHT/2, 
                       SCREENWIDTH/2, SCREENHEIGHT/2 );
           displayText( -185, -185, "Tokyo", 0.4, 0.4, 0.4 ) ;
           drawClock(current_hour + 14, current_minute, current_second);
           break ;

    case 4: /* draw in upper left */
           glViewport(             0,              0, 
                       SCREENWIDTH/2, SCREENHEIGHT/2 );
           displayText( -185, -185, "Buffalo", 0.4, 0.4, 0.4 ) ;
           drawClock(current_hour, current_minute, current_second);
           glViewport( SCREENWIDTH/2,              0, 
                       SCREENWIDTH/2, SCREENHEIGHT/2 );
           displayText( -185, -185, "London", 0.4, 0.4, 0.4 ) ;
           drawClock(current_hour + 5, current_minute, current_second);
           glViewport( SCREENWIDTH/2, SCREENHEIGHT/2, 
                       SCREENWIDTH/2, SCREENHEIGHT/2 );
           displayText( -185, -185, "Tokyo", 0.4, 0.4, 0.4 ) ;
           drawClock(current_hour + 14, current_minute, current_second);
           glViewport(             0, SCREENHEIGHT/2, 
                       SCREENWIDTH/2, SCREENHEIGHT/2 );
           displayText( -185, -185, "Moscow", 0.4, 0.4, 0.4 ) ;
           drawClock(current_hour+8, current_minute, current_second);
           break ;
    } 

    
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
    glutInit( &ac, av );
    glutInitDisplayMode( GLUT_DOUBLE | GLUT_RGB );
    glutInitWindowSize( SCREENWIDTH, SCREENHEIGHT );
    glutInitWindowPosition( 0, 0 );
    glutCreateWindow( av[0] );
    
    /* register our callback functions */
    glutMouseFunc( mouse );
    glutDisplayFunc( display );
    glutIdleFunc( display );
    
    /* LETS GET IT ON! :) */
    glutMainLoop();
    return 0;
  
}

