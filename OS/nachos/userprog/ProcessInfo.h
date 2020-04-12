/* File:         $Id: ProcessInfo.h,v 3.0 2001/11/04 19:47:03 trc2876 Exp $
// Author:       Trevor Clarke, Eric Eells, Eric Ferguson, Dan Westgate
// Contributors: {}
// Description:
// Revisions:
//               $Log: ProcessInfo.h,v $
//               Revision 3.0  2001/11/04 19:47:03  trc2876
//               force to 3.0
//
//               Revision 2.1  2001/10/28 20:09:13  trc2876
//               Fixed error in kernToUser and userToKern....these were only working if you
//               copied a single page of data...they work on page-spanning data now
//
//               we can have more args and longer args..set these in userprog/argdefs.h
//               currently, these are set to 8 and 32 respectivly
//
//               Revision 2.0  2001/10/11 02:53:29  trc2876
//               force update to 2.0 tree
//
//               Revision 1.4  2001/10/06 00:15:57  trc2876
//               in progress Fork fixes
//
//               Revision 1.3  2001/10/05 01:57:18  trc2876
//               added #ifndef wrapper
//
//               Revision 1.2  2001/09/25 01:55:47  trc2876
//               last minute fixes....look at the damn diffs
//
//               Revision 1.1  2001/09/22 22:03:54  p544-01b
//               Initial revision
*/

#ifndef ProcesInfo_H_
#define ProcesInfo_H_

#include "argdefs.h"

typedef struct {
    int pid_ ;
    int ppid_ ;
    int threadCount_ ;
    int joinerCount_ ;
    int childCount_ ;
    char pname_[MAX_ARG_LENGTH] ;
} procInfo_t;

#endif
