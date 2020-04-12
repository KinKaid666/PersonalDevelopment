/* File:        $Id: lsd.c,v 1.6 2003/09/29 03:37:00 etf2954 Exp $
** Author:      Eric Ferguson
** Description: An ls, but for directories
** Revision:
**              please use `rlog <filename>`
*/

#define DISPLAY_WIDTH   80
#define COLUMN_SPACER   2
#define PADDING_LEN_MAX 40
#define DIR_INFO_LEN    54
#define USERNAME_LEN    8
#define GROUP_NAME_LEN  8

/*
** k, I know it's terrible, but that's all the ceiling function does.
** integer division and add one (if the two are not the same number)
*/
#define divide_with_ceiling(x,y)        (((x)/(y))+((x)%(y)?1:0))

#include <stdarg.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>

#include <sys/stat.h>
#include <sys/types.h>
#include <dirent.h>
#include <errno.h>
#include <math.h>
#include <pwd.h>
#include <grp.h>

int  f_a ;
int  f_A ;
int  f_F ;
int  f_l ;
int  f_n ;

unsigned int num_entries ;

typedef struct listing
{
    struct listing *next ;
    struct stat    *stat   ;
    char *d_name ;
    int   d_name_len ;
} listing_t ;

void usage()
{
    printf( "usage: lsd [OPTION] [DIR]                                       \n"
            "Print directories under DIR (the current directory by default)  \n"
            "Sorts the directories alphabetically                            \n"
            "                                                                \n"
            "  -A           do not list implied . and ..                     \n"
            "  -a           do not hide entries starting with .              \n"
            "  -F           append a character for typing each entry         \n"
            "  -h           get this help message                            \n"
            "  -l           use a long listing format                        \n"
            "  -n           print numeric uid & gid (used with -l)           \n"
    ) ;
}

void exit_with_errors( char *fmt, ... )
{
    va_list args ;

    if( fmt == NULL ) return ;
    va_start( args, fmt ) ;
    vfprintf( stderr, fmt, args) ;
    va_end( args ) ;

    exit( EXIT_FAILURE ) ;
}

void insert( listing_t **head, struct dirent *dirent, struct stat *stat )
{
    listing_t *current = (listing_t *)malloc( sizeof( listing_t ) ),
              *itr     = *head ;

    if( current == NULL )
        exit_with_errors( "malloc(%d) failed\n", sizeof(listing_t) ) ;
    memset( (void *)current, '\0', sizeof( listing_t ) ) ;
    current->stat   = stat ;
    current->d_name = (char *)strdup(dirent->d_name) ;
    current->d_name_len = strlen(dirent->d_name) ;

    /*
    ** insert head
    */
    if( *head == NULL || strcmp( (*head)->d_name,
                                 current->d_name ) > 0 )
    {
        *head         = current  ;
        current->next = itr ;
    }
    else
    {
        /*
        ** find it's correct spot
        */
        listing_t *tmp = NULL ;

        while( itr->next != NULL && strcmp(itr->next->d_name,
                                           current->d_name) < 0 )
        {
            itr = itr->next ;
        }
        tmp           = itr->next ;
        itr->next     = current ;
        current->next = tmp ;
    }
    num_entries++ ;
}

void print_long_listing( char *d_name, struct stat *stat )
{
    int i, n ;
    static char perms[10],
           link[BUFSIZ + 1],
           username[USERNAME_LEN + 1],
           groupname[GROUP_NAME_LEN + 1] ;
    char *perm_itr = perms,
         t ;

    struct stat link_stat ;
    struct group  *group ;
    struct passwd *passwd ;
    strncpy( perms, "---------", 9 ) ;  /* reset for this entry */

    /*
    ** What type of file is it
    */
    switch( stat->st_mode & S_IFMT )
    {
    case S_IFDIR:
        putchar( 'd' ) ;
        break ;
    case S_IFLNK:
        putchar( 'l' ) ;
        break ;
    }

    /*
    ** What are the perms
    */
    for( i = 0 ; i < 3 ; i++ )
    {
        if( stat->st_mode & (S_IREAD >> i*3) )
            *perm_itr = 'r' ;
        perm_itr++ ;
        if( stat->st_mode & (S_IWRITE >> i*3) )
            *perm_itr = 'w' ;
        perm_itr++ ;
        if( stat->st_mode & (S_IEXEC >> i*3) )
            *perm_itr = 'x' ;
        perm_itr++ ;
    }
    if( (stat->st_mode & S_ISUID) != 0 )
    {
        if( perms[2] == '-' )
            perms[2] = 'S' ;
        else
            perms[2] = 's' ;
    }
    if( (stat->st_mode & S_ISGID) != 0 )
    {
        if( perms[5] == '-' )
            perms[5] = 'S' ;
        else
            perms[5] = 's' ;
    }
    if( (stat->st_mode & S_ISVTX) != 0 )
    {
        if( perms[8] == '-' )
            perms[8] = 'T' ;
        else
            perms[8] = 't' ;
    }

    if( !f_n && (passwd = getpwuid(stat->st_uid) ) != NULL )
        strncpy( username, passwd->pw_name, USERNAME_LEN ) ;
    else
        sprintf( username, "%d", stat->st_uid ) ;

    if( !f_n && (group = getgrgid(stat->st_gid) ) != NULL )
        strncpy( groupname, group->gr_name, GROUP_NAME_LEN ) ;
    else
        sprintf( groupname, "%d", stat->st_gid ) ;

    printf( "%-9s %3d %-8s %-8s %8d %.24s %s", perms,
                                               stat->st_nlink,
                                               username,
                                               groupname,
                                               (int)stat->st_size,
                                               "TOFIX", // ctime(stat->st_mtime)+4,
                                               d_name ) ;

    if( S_ISLNK(stat->st_mode) )
    {
        if( (n = readlink(d_name, link, sizeof(link))) < 0 )
            printf( " -> ???" ) ;
        else
            printf( " -> %.*s", n, link ) ;
    }
    if( f_F )
    {
        /*
        ** If it's a link, follow it and see what's it's pointing to
        */
        if( S_ISLNK(stat->st_mode) )
        {
            lstat( link, &link_stat ) ;
            if( S_ISLNK(link_stat.st_mode) )
                putchar( '@' ) ;
            else
                putchar( '/' ) ;
        }
        else
            putchar( '/' ) ;
    }

    putchar('\n') ;
}

void output( listing_t *list )
{
    listing_t *itr      = NULL ;        /* iterator */
    /*
    ** little column/row math to get some nice output :-D
    */
    if( !f_l )
    {
        int i, j,
            longest   = 0,
            num_lines = 0,
            num_rows  = 0,
            col_len   = 0 ;

        listing_t **entries = NULL ;    /* entries as array, for easy access */
        entries = (listing_t **)calloc( sizeof(listing_t *), num_entries ) ;
        if( entries == NULL )
            exit_with_errors( "cmalloc(%d,%d) failed\n", sizeof(listing_t *),
                                                         num_entries ) ;

        itr = list ;
        for( itr = list, i = 0 ; itr != NULL ; itr = itr->next, i++ )
        {
            if( itr->d_name_len > longest )
                longest = itr->d_name_len ;
            entries[i] = itr ;
        }

        col_len   = longest + f_F + COLUMN_SPACER ;
        num_rows  = DISPLAY_WIDTH / col_len ;
        num_lines = num_entries == 0 ?
                          0 : divide_with_ceiling(num_entries, num_rows) ;

        for( i = 0 ; i < num_lines ; i ++ )
        {
            for( j = 0 ; j < num_rows ; j++ )
            {
                int index = j * num_lines + i ;

                if( index < num_entries )
                {
                    /*
                    ** These are just fancy printf tricks check the non f_F to
                    ** see what's really going on
                    */
                    if( f_F )
                    {
                        char *t = S_ISDIR(entries[index]->stat->st_mode) ? "/" :
                                                                           "@" ;
                        printf( "%s%-*.*s", entries[index]->d_name,
                                            col_len -entries[index]->d_name_len,
                                            1,
                                            t ) ;
                    }
                    else
                    {
                        printf( "%-*.*s", col_len,
                                          entries[index]->d_name_len,
                                          entries[index]->d_name ) ;
                    }
                }
            }
            putchar('\n') ;
        }

        free( entries ) ;
    }
    else
    {

        itr = list ;
        while( itr != NULL )
        {
            print_long_listing( itr->d_name, itr->stat ) ;
            itr = itr->next ;
        }
    }
}

void free_list( listing_t *list )
{
    listing_t *tmp ;
    while( list != NULL )
    {
        tmp  = list ;
        list = list->next ;
        free( tmp->d_name ) ;
        free( tmp ) ;
    }
}

/*
** flags:
**    -a : list all .* files
**    -A : list almost all .* (do no include . or ..)
**    -F : append a trailing '/' to directory names and '@' to symlinks
**    -l : long listing
*/
int main( int ac, char **av )
{
    char                *d_name = NULL ;
    DIR                 *dir    = NULL ;
    struct dirent       *dirent = NULL ;
    struct stat         *stat   = NULL ;
    listing_t           *ls     = NULL ;
    int                 opt = 0 ;
    extern int          optind ;

    while( (opt = getopt(ac, av, "AFahln")) != -1 )
    {
        switch(opt)
        {
        case 'A':
            f_A = 1 ;
            break ;
        case 'F':
            f_F = 1 ;
            break ;
        case 'a':
            f_a = 1 ;
            f_A = 1 ;
            break ;
        case 'l':
            f_l = 1 ;
            break ;
        case 'n':
            f_n = 1 ;
            break ;
        case 'h':
        default :
            usage() ;
            exit(EXIT_FAILURE) ;
        }
    }

    if( av[optind] != NULL && chdir( av[optind] ) )
        exit_with_errors( "%s: %s: %s\n", av[0], av[optind], strerror(errno) ) ;

    d_name = (char *)getcwd(NULL,0) ;

    /*
    ** This should never error, because chdir would have, but...
    */
    if( (dir = opendir( d_name )) == NULL )
        exit_with_errors( "%s: %s: %s\n", av[0], av[optind], strerror(errno) ) ;

    /* walk though the return dir */
    while( (dirent = readdir(dir)) != NULL )
    {
        /*
        ** If we use stat instead of lstat it will try and follow our links
        */
        stat = (struct stat *)malloc( sizeof(struct stat) ) ;
        if( stat == NULL )
            exit_with_errors( "malloc(%d) failed\n", sizeof(struct stat) ) ;
        if( lstat( dirent->d_name, stat ) != -1 )
        {

            if( S_ISDIR(stat->st_mode) )
            {
                if( (strcmp( dirent->d_name, "."  ) == 0 ||
                     strcmp( dirent->d_name, ".." ) == 0 ) &&
                     !f_a )
                {
                    free(stat) ;
                    continue ;
                }
                if( (dirent->d_name[0] == '.') && !f_A )
                {
                    free(stat) ;
                    continue ;
                }
                insert( &ls, dirent, stat ) ;
            }
        }
        else
        {
            free(stat) ;
            exit_with_errors( "%s: %s: can't open %s\n", av[0],
                                                         dirent->d_name,
                                                         strerror(errno) ) ;
        }
    }
    closedir(dir) ;

    output( ls ) ;
    free_list(ls) ;
    free(d_name) ;

    return EXIT_SUCCESS ;
}
