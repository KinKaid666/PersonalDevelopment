#include <GLUT/glut.h>         /* glut.h includes gl.h and glu.h*/
#include <stdio.h>
#include <time.h>
       
GLfloat material0_amb[] = { 0.24, 0.62, 0.90, 1.00 } ;
GLfloat material0_dif[] = { 0.11, 0.93, 0.86, 1.00 } ;
GLfloat material0_spe[] = { 0.93, 0.20, 0.23, 1.00 } ;
GLfloat material0_emi[] = { 0.34, 0.84, 0.48, 1.00 } ;
GLfloat material0_shi = 100 ;

GLfloat material1_amb[] = { 0.00, 1.00, 1.00, 1.00 } ;
GLfloat material1_dif[] = { 0.43, 0.47, 0.54, 1.00 } ;
GLfloat material1_spe[] = { 0.33, 0.33, 0.52, 1.00 } ;
GLfloat material1_emi[] = { 0.80, 0.80, 0.80, 1.00 } ;
GLfloat material1_shi = 10 ;

/* GL defaults per man page */
GLfloat no_amb[] = { 0.20, 0.20, 0.20, 1.00 } ;  
GLfloat no_dif[] = { 0.80, 0.80, 0.80, 1.00 } ;
GLfloat no_spe[] = { 0.00, 0.00, 0.00, 1.00 } ;
GLfloat no_emi[] = { 0.00, 0.00, 0.00, 0.00 } ;
GLfloat no_shi = 0 ;

int screen_height =  500 ;
int screen_width  =  500 ;

GLuint textures[2] ;
GLubyte checker_image[64][64][3] ;
unsigned char *marble_image ;
int marble_image_heigth = 80 ;
int marble_image_width  = 64 ;

GLfloat old_x = 0 ;
GLfloat old_y = 0 ;

int m_button = -1 ;

/* various flags */
int f_fog       = 0 ;
int f_light0    = 0 ;
int f_light1    = 0 ;
int f_texture0  = 0 ;
int f_texture1  = 0 ;
int f_material0 = 0 ;
int f_material1 = 0 ;

/* 
 * Written by Nate Robins, 1997
 * ppmRead: read a PPM raw (type P6) file.  The PPM file has a header
 * that should look something like:
 *
 *   P6
 *   # comment
 *   width height max_value
 *   rgbrgbrgb...
 *
 * where "P6" is the magic cookie which identifies the file type and
 * should be the only characters on the first line followed by a
 * carriage return.  Any line starting with a # mark will be treated
 * as a comment and discarded.  After the magic cookie, three integer
 * values are expected: width, height of the image and the maximum
 * value for a pixel (max_value must be < 256 for PPM raw files).  The
 * data section consists of width*height rgb triplets (one byte each)
 * in binary format (i.e., such as that written with fwrite() or
 * equivalent).
 *
 * The rgb data is returned as an array of unsigned chars (packed
 * rgb).  The malloc()'d memory should be free()'d by the caller.  If
 * an error occurs, an error message is sent to stderr and NULL is
 * returned.
 *
 */
unsigned char* ppmRead( char* filename, int* width, int* height ) {

   FILE* fp;
   int i, w, h, d;
   unsigned char* image;
   char head[70];               // max line <= 70 in PPM (per spec).

   fp = fopen( filename, "rb" );
   if ( !fp ) {
      perror(filename);
      return NULL;
   }

   // Grab first two chars of the file and make sure that it has the
   // correct magic cookie for a raw PPM file.
   fgets(head, 70, fp);
   if (strncmp(head, "P6", 2)) {
      fprintf(stderr, "%s: Not a raw PPM file\n", filename);
      return NULL;
   }

   // Grab the three elements in the header (width, height, maxval).
   i = 0;
   while( i < 3 ) {
      fgets( head, 70, fp );
      if ( head[0] == '#' )             // skip comments.
         continue;
      if ( i == 0 )
         i += sscanf( head, "%d %d %d", &w, &h, &d );
      else if ( i == 1 )
         i += sscanf( head, "%d %d", &h, &d );
      else if ( i == 2 )
         i += sscanf( head, "%d", &d );
   }

   // Grab all the image data in one fell swoop.
   image = (unsigned char*) malloc( sizeof( unsigned char ) * w * h * 3 );
   fread( image, sizeof( unsigned char ), w * h * 3, fp );
   fclose( fp );

   *width = w;
   *height = h;
   return image;

}

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
void drawPocketWatch( int hour, int minute, int second )
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
    int index = 0 ;


    glEnable( GL_COLOR_MATERIAL ) ;
    quadratic = gluNewQuadric();        
    gluQuadricDrawStyle( quadratic, GLU_FLAT ); 

    glMaterialfv( GL_FRONT, GL_AMBIENT,   no_amb );
    glMaterialfv( GL_FRONT, GL_DIFFUSE,   no_dif );
    glMaterialfv( GL_FRONT, GL_SPECULAR,  no_spe );
    glMaterialfv( GL_FRONT, GL_EMISSION,  no_emi );
    glMaterialf ( GL_FRONT, GL_SHININESS, no_shi );

    /* Draw original torus */
    glColor3ub( 0xe6, 0xe8, 0xfa ) ;
    if( f_texture0%2 ) 
    {
        glEnable( GL_TEXTURE_2D ) ;
        glBindTexture( GL_TEXTURE_2D, textures[1] ) ;
    }
    glutSolidTorus( 0.25, 0.75, 16, 32 ) ;

    /* draw top ring for chain */
    glPushMatrix() ;
    glTranslatef( 0, 1.00, 0 ) ;
    if( f_material1%2 )
    {
        glMaterialfv( GL_FRONT, GL_AMBIENT,   material1_amb );
        glMaterialfv( GL_FRONT, GL_DIFFUSE,   material1_dif );
        glMaterialfv( GL_FRONT, GL_SPECULAR,  material1_spe );
        glMaterialfv( GL_FRONT, GL_EMISSION,  material1_emi );
        glMaterialf ( GL_FRONT, GL_SHININESS, material1_shi );
        glutSolidTorus( 0.05, 0.15, 16, 32 ) ;
        glMaterialfv( GL_FRONT, GL_AMBIENT,   no_amb );
        glMaterialfv( GL_FRONT, GL_DIFFUSE,   no_dif );
        glMaterialfv( GL_FRONT, GL_SPECULAR,  no_spe );
        glMaterialfv( GL_FRONT, GL_EMISSION,  no_emi );
        glMaterialf ( GL_FRONT, GL_SHININESS, no_shi );
    }
    else
    {
        glutSolidTorus( 0.05, 0.15, 16, 32 ) ;
    }
    glPopMatrix() ;

    /* Draw back of watch */
    glPushMatrix() ;
    glTranslatef( 0, 0, -0.25 ) ;
    glNormal3f( 0, 0, -2 ) ;
    glRotatef( 180, 1, 0, 0 ) ;
    gluDisk( quadratic, 0.0, 0.75, 32, 32) ;
    gluDisk( quadratic, 0.0, 0.75, 32, 32) ;
    glPopMatrix() ;

    /* Draw front of watch */
    glPushMatrix() ;
    glTranslatef( 0, 0, 0.25 ) ;
    glColor3ub( 0xe6, 0xe8, 0xfa ) ;
    glutSolidTorus( .05, .75, 16, 32 ) ;
    if( f_texture0%2 ) glDisable( GL_TEXTURE_2D ) ;
    glColor3ub( 0xff, 0xff, 0xff ) ;
    if( f_texture1%2 ) 
    {
        glEnable( GL_TEXTURE_2D ) ;
        glBindTexture( GL_TEXTURE_2D, textures[0] ) ;
    }
    if( f_material0%2 )
    {
        glMaterialfv( GL_FRONT, GL_AMBIENT,   material0_amb );
        glMaterialfv( GL_FRONT, GL_DIFFUSE,   material0_dif );
        glMaterialfv( GL_FRONT, GL_SPECULAR,  material0_spe );
        glMaterialfv( GL_FRONT, GL_EMISSION,  material0_emi );
        glMaterialf ( GL_FRONT, GL_SHININESS, material0_shi );
        gluDisk( quadratic, 0.0, 0.75, 32, 32) ;
        glMaterialfv( GL_FRONT, GL_AMBIENT,   no_amb );
        glMaterialfv( GL_FRONT, GL_DIFFUSE,   no_dif );
        glMaterialfv( GL_FRONT, GL_SPECULAR,  no_spe );
        glMaterialfv( GL_FRONT, GL_EMISSION,  no_emi );
        glMaterialf ( GL_FRONT, GL_SHININESS, no_shi );
    }
    else
    {
        gluDisk( quadratic, 0.0, 0.75, 32, 32) ;
    }
    if( f_texture1%2 ) glDisable( GL_TEXTURE_2D ) ;
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
    drawPocketWatch( current_hour, current_minute, current_second ) ;

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
    GLfloat light0_pos[] = { 0, 1, 1, 0 } ;

    GLfloat light0_amb[] = { 0.30, 0.00, 0.30, 0.00 } ;
    GLfloat light0_dif[] = { 0.43, 0.47, 0.54, 1.00 } ;
    GLfloat light0_spe[] = { 0.33, 0.33, 0.52, 1.00 } ;
    GLfloat light0_emi[] = { 0.00, 0.00, 0.00, 0.00 } ;

    GLfloat light1_pos[] = { 0, 1, -1, 0 } ;
    GLfloat light1_amb[] = { 0, 0, 0, 0 } ;
    GLfloat light1_dif[] = { 1, 1, 1, 1 } ;
    GLfloat light1_spe[] = { 1, 1, 1, 1 } ;

    GLfloat light1_spot_direction[] = { 0, 0, 0 } ;
    GLint   light1_spot_exponent = 30 ;
    GLint   light1_spot_cutoff   = 180 ;

    GLfloat light1_con_attenuation = 1.00 ;
    GLfloat light1_lin_attenuation = 0.00 ;
    GLfloat light1_qua_attenuation = 0.00 ;

    GLfloat fog_color[] = { 0.00, 0.00, 0.00, 1.00 } ;

    int i, j, c;
    int index ;


    /* setup the checker_image !textures[1]! */
    for ( i = 0; i < 64; i++ ) {
        for ( j = 0 ; j < 64; j++ ) {
            if( ( ( ( ( i & 0x8 ) == 0 ) ^ ( ( j & 0x8 ) ) == 0 ) ) == 0 )
            {
                checker_image[ i ][ j ][ 0 ] = ( GLubyte ) 0   ;
                checker_image[ i ][ j ][ 1 ] = ( GLubyte ) 0   ;
                checker_image[ i ][ j ][ 2 ] = ( GLubyte ) 255 ;
            }
            else
            {
                checker_image[ i ][ j ][ 0 ] = ( GLubyte ) 0   ;
                checker_image[ i ][ j ][ 1 ] = ( GLubyte ) 255 ;
                checker_image[ i ][ j ][ 2 ] = ( GLubyte ) 0   ;
            }
        }
    }

    glGenTextures( 2, textures ) ;
    glBindTexture( GL_TEXTURE_2D, textures[0] ) ;
    glEnable(GL_TEXTURE_GEN_T) ;
    glEnable(GL_TEXTURE_GEN_S) ;
    glTexGeni(GL_S, GL_TEXTURE_GEN_MODE, GL_OBJECT_LINEAR) ;
    glTexGeni(GL_T, GL_TEXTURE_GEN_MODE, GL_OBJECT_LINEAR) ;
    glTexParameterf( GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT ) ;
    glTexParameterf( GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT ) ;
    glTexParameterf( GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST ) ;
    glTexParameterf( GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST ) ;
    glTexImage2D( GL_TEXTURE_2D, 0, 4, 64, 64, 0, GL_RGB,
                  GL_UNSIGNED_BYTE, checker_image ) ;

    /* setup the marble_image !textures[1]! */
    marble_image = ppmRead( "marble.ppm", &marble_image_heigth, 
                                          &marble_image_width   ) ;
    glBindTexture( GL_TEXTURE_2D, textures[1] ) ;
    glEnable(GL_TEXTURE_GEN_S) ;
    glEnable(GL_TEXTURE_GEN_T) ;
    glTexGeni(GL_S, GL_TEXTURE_GEN_MODE, GL_OBJECT_LINEAR ) ;
    glTexGeni(GL_T, GL_TEXTURE_GEN_MODE, GL_OBJECT_LINEAR ) ;
    glTexParameterf( GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT ) ;
    glTexParameterf( GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT ) ;
    glTexParameterf( GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST ) ;
    glTexParameterf( GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST ) ;
    glTexImage2D( GL_TEXTURE_2D, 0, 4, 64, 64, 0, GL_RGB,
                  GL_UNSIGNED_BYTE, marble_image ) ;

    glEnable( GL_LIGHTING ) ;
    glEnable( GL_NORMALIZE ) ;
    glEnable( GL_DEPTH_TEST ) ;

    glMatrixMode( GL_PROJECTION ) ;
    glLoadIdentity() ;

    glViewport( 0, 0, screen_width, screen_height ) ;
    gluPerspective( 30.0, 1.0, 3, 128.0 ) ;
    gluLookAt( eye_x,    eye_y,    eye_z,
               center_x, center_y, center_z,
               up_x,     up_y,     up_z      ) ;

    glMatrixMode( GL_MODELVIEW ) ;
    glLightfv( GL_LIGHT0, GL_POSITION, light0_pos ) ;
    glLightfv( GL_LIGHT0, GL_AMBIENT,  light0_amb ) ;
    glLightfv( GL_LIGHT0, GL_DIFFUSE,  light0_dif ) ;
    glLightfv( GL_LIGHT0, GL_SPECULAR, light0_spe ) ;

    glLightfv( GL_LIGHT1, GL_POSITION, light1_pos ) ;
    glLightfv( GL_LIGHT1, GL_AMBIENT,  light1_amb ) ;
    glLightfv( GL_LIGHT1, GL_DIFFUSE,  light1_dif ) ;
    glLightfv( GL_LIGHT1, GL_SPECULAR, light1_spe ) ;

    glLightfv( GL_LIGHT1, GL_SPOT_DIRECTION, light1_spot_direction ) ;
    glLighti ( GL_LIGHT1, GL_SPOT_EXPONENT,  light1_spot_exponent  ) ;
    glLighti ( GL_LIGHT1, GL_SPOT_CUTOFF,    light1_spot_cutoff    ) ;

    glLightf( GL_LIGHT1, GL_CONSTANT_ATTENUATION,  light1_con_attenuation ) ;
    glLightf( GL_LIGHT1, GL_LINEAR_ATTENUATION,    light1_lin_attenuation ) ;
    glLightf( GL_LIGHT1, GL_QUADRATIC_ATTENUATION, light1_qua_attenuation ) ;

    glFogfv( GL_FOG_COLOR,   fog_color ) ;
    glFogf ( GL_FOG_DENSITY, 3.15 ) ;
    glFogi ( GL_FOG_MODE,    GL_EXP ) ;

}

/*
** keyboard callback
*/
void keyboard( unsigned char key, int x, int y ) 
{
    switch ( key ) {
       case 27 : /* ESC or 'q' */
       case 'q':
          exit( 0 );
          break;
       case 'f': /* toggle fog */
          ( ++f_fog % 2 ) ? glEnable( GL_FOG ) : glDisable( GL_FOG ) ;
         break ; 
       case '0': /* toggle light0 */
          ( ++f_light0 % 2 ) ? glEnable( GL_LIGHT0 ) : glDisable( GL_LIGHT0 ) ;
         break ; 
       case '1': /* toggle light1 */
          ( ++f_light1 % 2 ) ? glEnable( GL_LIGHT1 ) : glDisable( GL_LIGHT1 ) ;
          break;
       case '2': /* toggle texture0 */
          ++f_texture0 ;
          break;
       case '3': /* toggle texture1 */
          ++f_texture1 ;
          break;
       case '4': /* toggle material0 */
          ++f_material0 ;
          break;
       case '5': /* toggle material1 */
          ++f_material1 ;
          break;
       case 'a':
          init( 0.0, 0.0, 5.0,
                0.0, 0.0, 0.0,
                0.0, 1.0, 0.0  ) ;
          break;
       case 's':
          init( 5.0, 0.0, 0.0,
                0.0, 0.0, 0.0,
                0.0, 1.0, 0.0  ) ;
          break;
       case 'd':
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
    free( marble_image ) ;
    return 0;
}

