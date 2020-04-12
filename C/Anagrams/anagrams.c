#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <fcntl.h>

typedef struct word
{
    char        *word ;
    char        *letters ;
} word_t ;

/*
** Name:        letterize
**
** Description: sorts chars in a string (bubble)
**
** Arguments:   cstr:   string to be letterized
*/
void letterize( char *cstr )
{
    int x, y, len = strlen( cstr ) ;
    for( x = 0 ; x < len ; x++ )
    {
        for( y = 0 ; y < len ; y++ )
        {
            if( cstr[x] < cstr[y] )
            {
                char ch = cstr[x] ;
                cstr[x] = cstr[y] ;
                cstr[y] = ch      ;
            }
        }
    }
}

/*
** Name:        sort
**
** Description: sorts array, based on compare
**
** Arguments:   array:          array to be sorted
**              len:            length of array
**              compare:        lexigraphical comparison to be made on elements
*/
void sort( int *array, size_t len, int (*compare)(int *a, int *b) )
{
    int x,y ;
    for( x = 0 ; x < len ; x++ )
    {
        for( y = 0 ; y < len ; y++ )
        {
            if( compare( array[x], array[y] ) > 0 )
            {
                int temp = array[x] ;
                array[x] = array[y] ;
                array[y] = temp     ;
            }
        }
    }
}

int wordcompare( word_t *x, word_t *y )
{
    return strcmp( x->letters, y->letters ) ;
}

/*
** Name:        generateArray
** 
** Description: reads in words from stdin and puts them into
**              wordlist 
**
** Arguments:   wordlist:       list of words
**
** Returns:     EXIT_SUCCESS:   success reading (defined stdlib.h)
**              EXIT_FAILURE:   failure reading
*/
int generateArray( FILE *file, word_t **wordlist )
{
    int index = 0 ;
    char buffer[BUFSIZ] ;
    while( fgets( buffer, sizeof(buffer), file ) )
    {
        int len = strlen( buffer ) ;
        wordlist[index] = malloc( sizeof( word_t ) ) ;
        memset( (void *)wordlist[index], 0, sizeof( word_t ) ) ;
        wordlist[index]->word = malloc( len ) ;
        memset( (void *)wordlist[index]->word, 0, len ) ;
        strncpy( wordlist[index]->word, buffer, len - 1 ) ;
        index++ ;
    }
    return EXIT_SUCCESS ;
}

/*
** Name:        letterizeArray
** 
** Description: parses list and adds coresponding 'letterized' version to
**              wordlist 
**
** Arguments:   wordlist:       list of words
**
** Returns:     EXIT_SUCCESS:   success parsing (defined stdlib.h)
**              EXIT_FAILURE:   failure parsing
*/
int letterizeArray( word_t **wordlist ) 
{
    int index = 0 ;
    while( wordlist[index] != NULL )
    {
        int len = strlen( wordlist[index]->word ) + 1 ;
        wordlist[index]->letters = malloc( len ) ;
        memset( (void *)wordlist[index]->letters, 0, len ) ;
        strncpy( wordlist[index]->letters, wordlist[index]->word, len ) ;
        letterize( wordlist[index]->letters ) ;
        index++ ;
    }
    return EXIT_SUCCESS ;
}

/*
** Name:        printArray
**
** Description: prints the contents of wordlist
**
** Arguments:   wordlist:       array to be printed
**              flags:          print flags (what to be printed)
*/
enum printFlags{ WORD = 0, BOTH } ;
void printArray( word_t **wordlist, int flags )
{
    int index = 0 ;
    while( wordlist[index] != NULL )
    {
        if( flags == WORD )
        {
            printf( "%s\n", wordlist[index]->word ) ;

            if( wordlist[index + 1] != NULL )
            {
                if( strcmp( wordlist[index + 1]->letters, 
                            wordlist[index    ]->letters ) != 0 )
                {
                    printf( "\n" ) ;
                }
            }
        }
        else if( flags == BOTH ) 
        {
            printf( "%s,%s\n", wordlist[index]->word,
                               wordlist[index]->letters ) ;
        }
        index++ ;
    }
}

/*
** Name:        freeArray
**
** Description: array to be free'd
**
** Arguments:   wordlist:       array to be free'd
*/
void freeArray( word_t **wordlist )
{
    int index = 0 ;
    while( wordlist[index] != NULL )
    {
        free( wordlist[index]->word    ) ;
        free( wordlist[index]->letters ) ;
        index++ ;
    }
    free( wordlist ) ;
}

/*
** Name:        main
*/
int main( int ac, char **av )
{
    FILE        *fin       = NULL ;
    word_t      **wordlist = NULL ;

    if( ac != 3 )
    {
        fprintf( stderr, "usage: %s <filename> <num-words>\n", av[0] ) ;
        exit( EXIT_FAILURE ) ;
    }

    if( (fin = fopen( av[1], "r" )) == NULL )
    {
        perror( av[1] ) ;
        exit( EXIT_FAILURE ) ;
    }

    /* allocate x number of pointers for word_t's */
    wordlist = calloc( (atoi(av[2]) + 1), sizeof( word_t* ) ) ;

    generateArray( fin, wordlist ) ;
    letterizeArray( wordlist ) ;
    sort( wordlist, atoi(av[2]), &wordcompare ) ;

    printArray( wordlist, WORD ) ;

    /* cleanup */
    freeArray( wordlist ) ;
    fclose( fin ) ;
    return EXIT_SUCCESS ;
}
