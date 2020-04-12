#include <stdio.h>
#include <stdlib.h>

void env( char **envp )
{
    printf( "Environment variable are as follows:\n" ) ;
    for( *envp ; *envp != NULL ; envp++ )
    {
	printf( "%s\n", *envp ) ;
    }
}

int main( int ac, char **av, char **ev )
{
    char *value = NULL ;
    if( ac != 2 )
    {
	fprintf( stderr, "usage: %s <environment-variable>\n", av[0] ) ;
	exit( EXIT_FAILURE ) ;
    }

    value = getenv( av[1] ) ;
    if( value == NULL )
    {
        printf( "** variable '%s' is not set **\n", av[1] ) ;
	env( ev ) ;
    }
    else
    {
	printf( "%s = %s\n", av[1], value ) ;
    }
    return EXIT_SUCCESS ;
}
