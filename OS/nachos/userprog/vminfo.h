/* File:         $Id: vminfo.h,v 3.0 2001/11/04 19:47:04 trc2876 Exp $
// Author:       Trevor Clarke, Eric Eells, Eric Ferguson, Dan Westgate
// Contributors: {}
// Description: struct for VMStat syscall
// Revisions:
//               $Log: vminfo.h,v $
//               Revision 3.0  2001/11/04 19:47:04  trc2876
//               force to 3.0
//
//               Revision 2.0  2001/10/11 02:53:32  trc2876
//               force update to 2.0 tree
//
//               Revision 1.2  2001/10/08 23:08:16  trc2876
//               code cleanup
//
//               Revision 1.1  2001/10/07 22:46:46  trc2876
//               forgot to add vminfo.h
//
*/

#ifndef vminfo_H_
#define vminfo_H_

// struct for passing memory allocation information
//

typedef struct {
    int freePages;
    int totalPages;
    int freeStacks;
    int totalStacks;
} vminfo_t;

#endif
