// File:         $Id: VMStats.h,v 3.0 2001/11/04 19:47:07 trc2876 Exp $
// Author:       p544-01b
// Contributors: {}
// Description:  Keeps the stats for VM misses.
// Revisions:
//               $Log: VMStats.h,v $
//               Revision 3.0  2001/11/04 19:47:07  trc2876
//               force to 3.0
//
//               Revision 1.3  2001/10/30 18:28:46  eae8264
//               Changed all numbers to unsigned.
//
//               Revision 1.2  2001/10/30 16:36:11  eae8264
//               New and improved output.  Added constructor so that
//               a set of stats can be associated with a name.
//
//               Revision 1.1  2001/10/30 01:38:00  eae8264
//               Set up VMStats as a data member of KSwap.  Made calls to
//               print.  Added this as a function of KSwap.
//

#ifndef _VMStats_H
#define _VMStats_H

class VMStats {

public: // Constructors

	//
	// Name:	(constructor)
	//
	// Description: Sets vars to zero.
	//
	// Arguments:	None
	//
	VMStats() ;

	//
	// Name:	(constructor)
	//
	// Description: Sets vars to zero.
	//
	// Arguments:	String identifying what the stats are associated with.
	//		This will be used when printing out the info.
	//
	VMStats( char* about ) ;

public: // Client functions

	//
	// Name:	Print
	//
	// Description: Print the stats in a human-readable form.  It's okay
	//		to use printf because we're going down.  stats.cc does
	//		this.
	//
	// Arguments:	None
	//
	// Returns:	void
	//
	// Exceptions:	None
	//
	void Print() ;

	//
	// Name:	Add*
	//
	// Description: Adds to the corresponding value and the total.
	//		See private data comments for details of categories.
	//
	// Arguments:	Value to add
	//
	// Returns:	void
	//
	// Exceptions:	None
	//
	void addCold( unsigned int incr ) { coldMisses_ +=incr ; 
					    totalMisses_+=incr ; }

	void addCap ( unsigned int incr ) { capMisses_  +=incr ;
					    totalMisses_+=incr ; }

	void addConf( unsigned int incr ) { confMisses_ +=incr ;
					    totalMisses_+=incr ; }

private: // Data

	// The name for these stats to be associated with and the name's length
	char* about_  ;
	int aboutLen_ ;

	// Cold misses due to first reference
	unsigned int coldMisses_ ;

	// Capacity misses due to physical mem being full
	unsigned int capMisses_ ;
	
	// Conflict misses due to problems with our replacement algo
	unsigned int confMisses_ ;

	// Conflict misses due to problems with our replacement algo
	unsigned int totalMisses_ ;

}; // VMStats

#endif
