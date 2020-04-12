#ifndef _FILESTAT_H_
#define _FILESTAT_H_

#ifndef TEST
#include "mtree.h"
#else
typedef unsigned char T_BYTE ;
typedef unsigned int  T_WORD ;
typedef int           bool   ;
#endif

// 
// This is an encapsulation of the information returned by the Stat system
// 	call
struct Stat_t
{
    T_BYTE	mode_      ;
    char*	filename_  ;
    bool	directory_ ;
    T_WORD	size_	   ;
    T_WORD	id_	   ;
} ;
#endif
