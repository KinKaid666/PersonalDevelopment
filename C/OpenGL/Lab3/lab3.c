#include <GLUT/glut.h>         /* glut.h includes gl.h and glu.h*/
#include <time.h>

#define MAX_SCALE       1.0

int screen_height =  500 ;
int screen_width  =  500 ;

int camera_pos = 0 ;

GLfloat old_x = 0 ;
GLfloat old_y = 0 ;

int m_button = -1 ;


/*
** Callback function for mouse interaction
*/
void mouse( int button, int state, int x, int y )
{
    if( state == GLUT_DOWN )
    {
        old_x = x ;
        old_y = y ;
        m_button = button ;
    }
    else
    {
        m_button = -1 ;
    }
}

/*
** Callback function for mouse motion
*/
void motion( int x, int y )
{
    GLfloat scale = 0.0 ;
    switch( m_button )
    {
    case GLUT_LEFT_BUTTON   :
        glRotatef( (y - old_y), 1, 0, 0 ) ;
        glRotatef( (x - old_x), 0, 1, 0 ) ;
        break ;
    case GLUT_MIDDLE_BUTTON :
        glTranslatef( (x - old_x)/screen_width, (old_y - y)/screen_height, 0 ) ;
        break ;
    case GLUT_RIGHT_BUTTON  :
        scale = ((old_y - y)/(screen_height*1.0)) + 1.0 ;
        glScalef( scale, scale, scale ) ;
        break ;
    }
    glutPostRedisplay() ;
    old_x = x ;
    old_y = y ;
}

void draw3dRectangle( GLfloat vertices[8][3] )
{
    int x ;
    glBegin( GL_QUADS ) ;
        /* front */
        glVertex3fv( vertices[0] ) ;
        glVertex3fv( vertices[1] ) ;
        glVertex3fv( vertices[2] ) ;
        glVertex3fv( vertices[3] ) ;

        /* back */
        glVertex3fv( vertices[4] ) ;
        glVertex3fv( vertices[5] ) ;
        glVertex3fv( vertices[5] ) ;
        glVertex3fv( vertices[7] ) ;

        /* left */
        glVertex3fv( vertices[0] ) ;
        glVertex3fv( vertices[3] ) ;
        glVertex3fv( vertices[4] ) ;
        glVertex3fv( vertices[7] ) ;

        /* right */
        glVertex3fv( vertices[1] ) ;
        glVertex3fv( vertices[2] ) ;
        glVertex3fv( vertices[5] ) ;
        glVertex3fv( vertices[6] ) ;

        /* top */
        glVertex3fv( vertices[0] ) ;
        glVertex3fv( vertices[1] ) ;
        glVertex3fv( vertices[6] ) ;
        glVertex3fv( vertices[7] ) ;

        /* bottom */
        glVertex3fv( vertices[2] ) ;
        glVertex3fv( vertices[3] ) ;
        glVertex3fv( vertices[4] ) ;
        glVertex3fv( vertices[5] ) ;
    glEnd() ;
}
                       
/*
** drawPocketWatch
*/
void drawPacketWatch( int hour, int minute, int second )
{
    GLfloat hour_hash_vertices[][3] = { 
                               { -0.02, 0.68, 0.26 },
                               {  0.02, 0.68, 0.26 },
                               {  0.02, 0.50, 0.26 },
                               { -0.02, 0.50, 0.26 },
                               { -0.02, 0.50, 0.23 },
                               {  0.02, 0.50, 0.23 },
                               {  0.02, 0.68, 0.23 },
                               { -0.02, 0.68, 0.23 },
                            } ;
    GLfloat minute_hash_vertices[][3] = { 
                               { -0.01, 0.68, 0.26 },
                               {  0.01, 0.68, 0.26 },
                               {  0.01, 0.60, 0.26 },
                               { -0.01, 0.60, 0.26 },
                               { -0.01, 0.60, 0.23 },
                               {  0.01, 0.60, 0.23 },
                               {  0.01, 0.68, 0.23 },
                               { -0.01, 0.68, 0.23 },
                            } ;
    GLfloat hour_hand_vertices[][3] = { 
                               { -0.02, 0.40, 0.28 },
                               {  0.02, 0.40, 0.28 },
                               {  0.02, 0.00, 0.28 },
                               { -0.02, 0.00, 0.28 },
                               { -0.02, 0.00, 0.25 },
                               {  0.02, 0.00, 0.25 },
                               {  0.02, 0.40, 0.25 },
                               { -0.02, 0.40, 0.25 },
                            } ;
    GLfloat minute_hand_vertices[][3] = { 
                               { -0.02, 0.69, 0.28 },
                               {  0.02, 0.69, 0.28 },
                               {  0.02, 0.00, 0.28 },
                               { -0.02, 0.00, 0.28 },
                               { -0.02, 0.00, 0.25 },
                               {  0.02, 0.00, 0.25 },
                               {  0.02, 0.69, 0.25 },
                               { -0.02, 0.69, 0.25 },
                            } ;
    GLfloat second_hand_vertices[][3] = { 
                               { -0.01, 0.69, 0.28 },
                               {  0.01, 0.69, 0.28 },
                               {  0.01, 0.00, 0.28 },
                               { -0.01, 0.00, 0.28 },
                               { -0.01, 0.00, 0.25 },
                               {  0.01, 0.00, 0.25 },
                               {  0.01, 0.69, 0.25 },
                               { -0.01, 0.69, 0.25 },
                            } ;
    GLUquadricObj *quadratic ;
    int index ;

    quadratic = gluNewQuadric();        
    gluQuadricDrawStyle( quadratic, GLU_FILL ); 
    gluQuadricNormals( quadratic, GLU_SMOOTH ); 

    /* Draw original torus */
    glColor3ub( 0xe6, 0xe8, 0xfa ) ;
    glutSolidTorus( 0.25, 0.75, 16, 32 ) ;

    /* draw top ring for chain */
    glPushMatrix() ;
    glTranslatef( 0, 1.00, 0 ) ;
    glutSolidTorus( 0.05, 0.15, 16, 32 ) ;
    glPopMatrix() ;

    /* Draw back of watch */
    glPushMatrix() ;
    glTranslatef( 0, 0, -0.25 ) ;
    gluDisk( quadratic, 0.0, 0.75, 32, 32) ;
    glPopMatrix() ;

    /* Draw front of watch */
    glPushMatrix() ;
    glTranslatef( 0, 0, 0.25 ) ;
    glColor3ub( 0xe6, 0xe8, 0xfa ) ;
    glutSolidTorus( .05, .75, 16, 32 ) ;
    glColor3ub( 0xff, 0xff, 0xff ) ;
    gluDisk( quadratic, 0.0, 0.75, 32, 32) ;
    glPopMatrix() ;

    /* Draw hashes on clock face */
    glPushMatrix() ;
    glColor3ub( 0x0, 0x0, 0x0 ) ;
    for( index = 0 ; index < 12 ; index++ )
    {
        draw3dRectangle( hour_hash_vertices ) ;
        glRotatef( 30, 0, 0, 1 ) ;
    }
    glPopMatrix() ;

    glPushMatrix() ;
    for( index = 0 ; index < 60 ; index++ )
    {
        draw3dRectangle( minute_hash_vertices ) ;
        glRotatef( 6, 0, 0, 1 ) ;
    }
    glPopMatrix() ;

    glColor3ub( 0xaa, 0xaa, 0xaa ) ;
    /* draw the hour hand */
    glPushMatrix() ; 
    glRotatef( 360-((hour*30)+(minute*.5)), 0, 0, 1.0 ) ;
    draw3dRectangle( hour_hand_vertices ) ;
    glPopMatrix() ;

    /* draw the minute hand */
    glPushMatrix() ; 
    glRotatef( 360-(minute*6), 0, 0, 1.0 ) ;
    draw3dRectangle( minute_hand_vertices ) ;
    glPopMatrix() ;

    /* draw the second hand */
    glPushMatrix() ; 
    glRotatef( 360-(second*6), 0, 0, 1.0 ) ;
    draw3dRectangle( second_hand_vertices ) ;
    glPopMatrix() ;

    gluDeleteQuadric( quadratic ) ;
}

/*
** Display 
*/
void display( void )
{
    /* get the current minutes, and current hour for clock */
    time_t t = time( NULL ) ;
    struct tm *current_time = localtime( &t ) ;
    int current_hour   = current_time->tm_hour%12 ;
    int current_minute = current_time->tm_min ;
    int current_second = current_time->tm_sec ;

    glClear( GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT) ;
    drawPacketWatch( current_hour, current_minute, current_second ) ;

    /*
    ** Swap out the buffer we have been writing to and begin
    ** writing to the other 
    */
    glutSwapBuffers();
}

/*
** Setup the viewpoints and such
*/
void init( GLfloat eye_x,    GLfloat eye_y,    GLfloat eye_z,
           GLfloat center_x, GLfloat center_y, GLfloat center_z,
           GLfloat up_x,     GLfloat up_y,     GLfloat up_z      )
{
    GLfloat light_pos[] = { 0, 1, 1, 0 } ;

    glEnable( GL_LIGHTING ) ;
    glEnable( GL_LIGHT0 ) ;
    glEnable( GL_DEPTH_TEST ) ;
    glEnable( GL_COLOR_MATERIAL ) ;

    glMatrixMode( GL_PROJECTION ) ;
    glLoadIdentity() ;

    glViewport( 0, 0, screen_width, screen_height ) ;
    gluPerspective( 30.0, 1.0, 3, 128.0 ) ;
    gluLookAt( eye_x,    eye_y,    eye_z,
               center_x, center_y, center_z,
               up_x,     up_y,     up_z      ) ;

    glMatrixMode( GL_MODELVIEW ) ;
    glLightfv( GL_LIGHT0, GL_POSITION, light_pos ) ;

}

/*
** keyboard callback
*/
void keyboard( unsigned char key, int x, int y ) 
{
   switch ( key ) {
      case 27:
         exit( 0 );
         break;
      case '1':
         init( 0.0, 0.0, 5.0,
               0.0, 0.0, 0.0,
               0.0, 1.0, 0.0  ) ;
         break;
      case '2':
         init( 5.0, 0.0, 0.0,
               0.0, 0.0, 0.0,
               0.0, 1.0, 0.0  ) ;
         break;
      case '3':
         init( 0.0, 5.0, 0.0,
               0.0, 0.0, 0.0,
               0.0, 5.0, -5.0  ) ;
         break;
   }
}
    
/*
** Main routine - GLUT setup and initialization
*/
int main( int ac, char **av )
{
    glutInit( &ac, av );
    glutInitDisplayMode( GLUT_DOUBLE | GLUT_RGB | GLUT_DEPTH ) ;
    glutInitWindowSize( screen_width, screen_height );
    glutInitWindowPosition( 0, 0 );
    glutCreateWindow( av[0] );

    /* setup initial camera perspective and lighting */
    init( 0.0, 0.0, 5.0,
          0.0, 0.0, 0.0,
          0.0, 1.0, 0.0  ) ;

    /* register our callback functions */
    glutDisplayFunc ( display  );
    glutIdleFunc    ( display  );
    glutKeyboardFunc( keyboard ) ;
    glutMotionFunc  ( motion   ) ;
    glutMouseFunc   ( mouse    ) ;

    /* LETS GET IT ON! :) */
    glutMainLoop();
    return 0;
}

