#include "allocator.h"

void *allocator_malloc( size_t          size, 
                        char            *file, 
                        unsigned int    line   )
{
    return (void *)NULL ;
}

void *allocator_calloc( size_t          nmemb, 
                        size_t          size, 
                        char            *file, 
                        unsigned int    line   )
{
    return (void *)NULL ;
}

void *allocator_realloc( void           *ptr,
                         size_t         size,
                         char           *file, 
                         unsigned int   line   )
{
    return (void *)NULL ;
}

void allocator_free( void               *ptr,
                     char               *file,
                     unsigned int       line   )
{
}

void print_allocator_statistics() 
{ ; }
