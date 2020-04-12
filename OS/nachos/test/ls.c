// ls.c (Not written yet)
// print the contents of the current directory

#include "syscall.h"
#include "argdefs.h"
#include "lib.h"

int
main(int   argc,
     char *argv)
{
    char buf[256];
    if(argc == 2)
	ListDir(argv + MAX_ARG_LENGTH,N_strlen(argv + MAX_ARG_LENGTH),buf,256);
    else
	Write( "usage: ls <dir>\n", 16, 1 ) ;
    
    Write(buf,N_strlen(buf),1);

    Exit(0);
}
