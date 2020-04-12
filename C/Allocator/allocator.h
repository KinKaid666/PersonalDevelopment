#ifndef __ALLOCATOR_H__
#define __ALLOCATOR_H__

#include <stdlib.h>

#ifdef USE_ALLOCATOR


#define malloc(x)       allocator_malloc ( (x),      __FILE__, __LINE__ )
#define calloc(x,y)     allocator_calloc ( (x), (y), __FILE__, __LINE__ )
#define realloc(x)      allocator_realloc( (x), (y), __FILE__, __LINE__ )
#define free(x)         allocator_free   ( (x),      __FILE__, __LINE__ ) 


/*
** Name:                allocator_mallloc
** Description:         should not be called directly
**                      should be called by #defines, use malloc as usual
**
*/
void *allocator_malloc( size_t          size, 
                        char            *file, 
                        unsigned int    line   ) ;

void *allocator_calloc( size_t          nmemb, 
                        size_t          size, 
                        char            *file, 
                        unsigned int    line   ) ;

void *allocator_realloc( void           *ptr,
                         size_t         size,
                         char           *file, 
                         unsigned int   line   ) ;

void allocator_free(     void           *ptr,
                         char           *file,
                         unsigned int   line   ) ;


void print_allocator_statistics() ;
#endif

#endif /* __ALLOCATOR_H__ */
