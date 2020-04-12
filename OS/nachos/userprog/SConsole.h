// File:         $Id: SConsole.h,v 3.0 2001/11/04 19:47:03 trc2876 Exp $
// Author:       Trevor Clarke, Eric Eells, Eric Ferguson, Dan Westgate
// Contributors: {}
// Description: synchronize access to the console
// Revisions:
//               $Log: SConsole.h,v $
//               Revision 3.0  2001/11/04 19:47:03  trc2876
//               force to 3.0
//
//               Revision 2.0  2001/10/11 02:53:30  trc2876
//               force update to 2.0 tree
//
//               Revision 1.4  2001/10/06 18:55:53  etf2954
//               Added another mutex around the write call
//                 to assure no one is in the write call when
//                 another person enters
//
//               Revision 1.3  2001/10/05 01:42:07  eae8264
//               Made it so that Reads and Writes are possible.
//               There hasn't been too much testing.  We know this
//               works with shell v1.5.  This has not been thoroughly
//               tested with a shell that doesn't read one char at a
//               time...
//
//               Revision 1.2  2001/10/04 21:53:33  trc2876
//               BUGS list....
//
//               Revision 1.1  2001/10/04 00:32:00  trc2876
//               renamed SynchConsole to SConsole
//
//               Revision 1.5  2001/10/04 00:28:17  trc2876
//               stupid gcc suxors my arse
//
//               Revision 1.4  2001/10/04 00:25:52  trc2876
//               console is now stdin/stdout
//
//               Revision 1.3  2001/10/04 00:19:11  trc2876
//               fixed name errors
//
//               Revision 1.2  2001/10/04 00:15:39  trc2876
//               console to console_
//
//               Revision 1.1  2001/10/04 00:04:29  trc2876
//               Added SynchConsole
//
//

#ifndef _SConsole_H
#define _SConsole_H

#include "console.h"
#include "synch.h"

#define STDIN_FILE   NULL
#define STDOUT_FILE  NULL

class SConsole {

public: // Constructors

    //
    // Name:		(constructor)
    //
    // Description: 	Constructor
    //
    // Arguments:	none
    //
    SConsole() ;

public: // Destructors

    //
    // Name:		(destructor)
    //
    ~SConsole() ;

    //
    // Name:		ReadAvail
    //
    // Arguments:	unused int
    //
    // Description:	static function called when a char is ready to read
    //
    static void ReadAvail( int foo ) ;

    //
    // Name:		WriteDone
    //
    // Arguments:	unused int
    //
    // Description:	static function called when a char has been sent
    //
    static void WriteDone( int foo ) ;

    //
    // Name:		read
    //
    // Arguments:	char *: buffer to read into
    //			size_t: number of bytes to read
    //
    // Returns:		size_t: number of bytes read
    //
    // Description:	read in some characters
    //
    size_t read(char *buffer,size_t nbytes);

    //
    // Name:		write
    //
    // Arguments:	const char *: buffer to write from
    //			size_t: number of bytes to write
    //
    // Returns:		size_t: number of bytes writen
    //
    // Description:	write some characters
    //
    size_t write(const char *buffer,size_t nbytes);

private: // Data Members
    Console *console_;
    static Lock *readAvailMutex ;
    static Lock *writeDoneMutex ;
    static Lock *someoneWritingMutex ;
    static Condition *writeDoneNotifier ;
    static Condition *readAvailNotifier ;

}; // SConsole

#endif
