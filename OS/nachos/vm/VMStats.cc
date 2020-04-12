// File:         $Id: VMStats.cc,v 3.0 2001/11/04 19:47:07 trc2876 Exp $
// Author:       
// Contributors: 
// Description:	The implementation file for VMStats
// Revisions:
//               $Log: VMStats.cc,v $
//               Revision 3.0  2001/11/04 19:47:07  trc2876
//               force to 3.0
//
//               Revision 1.2  2001/10/30 16:36:11  eae8264
//               New and improved output.  Added constructor so that
//               a set of stats can be associated with a name.
//
//               Revision 1.1  2001/10/30 01:38:00  eae8264
//               Set up VMStats as a data member of KSwap.  Made calls to
//               print.  Added this as a function of KSwap.
//
//

#include "VMStats.h"
#include "utility.h"
#include <strings.h>

//
// Name: (constructor)
//
VMStats::VMStats():	about_   ( NULL ),
			aboutLen_   ( 0 ),
			coldMisses_ ( 0 ),
			capMisses_  ( 0 ),
			confMisses_ ( 0 ),
			totalMisses_( 0 )
{
} // (constructor)

//
// Name: (constructor)
//
VMStats::VMStats( char* about ):
			about_  ( about ),
			aboutLen_( strlen( about_ ) ),
			coldMisses_ ( 0 ),
			capMisses_  ( 0 ),
			confMisses_ ( 0 ),
			totalMisses_( 0 )
{
} // (constructor)

//
// Name: Print
//
void VMStats::Print()
{
    printf( "\nVM Statistics" ) ;
    if ( about_ ) printf( " for %s", about_ ) ;
    printf( "\n" ) ;
    printf( "=============" );
    if ( about_ ) for ( int i = 0; i < (aboutLen_ + 5); i++ ) printf( "=" ) ;
    printf( "\n" ) ;
    printf( "Cold Misses:\t\t%d\n",    coldMisses_  ) ;
    printf( "Capacity Misses:\t%d\n",  capMisses_   ) ;
    printf( "Conflict Misses:\t%d\n",  confMisses_  ) ;
    printf( "Total Misses:\t\t%d\n\n", totalMisses_ ) ;
} // Print
