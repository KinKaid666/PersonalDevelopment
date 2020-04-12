// File:         $Id: SConsole.cc,v 3.0 2001/11/04 19:47:03 trc2876 Exp $
// Author:       Trevor Clarke, Eric Eells, Eric Ferguson, Dan Westgate
// Contributors: {}
// Description:
// Revisions:
//               $Log: SConsole.cc,v $
//               Revision 3.0  2001/11/04 19:47:03  trc2876
//               force to 3.0
//
//               Revision 2.0  2001/10/11 02:53:30  trc2876
//               force update to 2.0 tree
//
//               Revision 1.6  2001/10/06 18:55:53  etf2954
//               Added another mutex around the write call
//                 to assure no one is in the write call when
//                 another person enters
//
//               Revision 1.5  2001/10/05 21:53:26  eae8264
//               Fixed a synchronization error.
//
//               Revision 1.4  2001/10/05 01:42:07  eae8264
//               Made it so that Reads and Writes are possible.
//               There hasn't been too much testing.  We know this
//               works with shell v1.5.  This has not been thoroughly
//               tested with a shell that doesn't read one char at a
//               time...
//
//               Revision 1.3  2001/10/04 21:53:33  trc2876
//               BUGS list....
//
//               Revision 1.2  2001/10/04 01:40:31  trc2876
//               Write() works
//
//               Revision 1.1  2001/10/04 00:31:59  trc2876
//               renamed SynchConsole to SConsole
//
//               Revision 1.3  2001/10/04 00:28:17  trc2876
//               stupid gcc suxors my arse
//
//               Revision 1.2  2001/10/04 00:15:39  trc2876
//               console to console_
//
//               Revision 1.1  2001/10/04 00:04:29  trc2876
//               Added SynchConsole
//
//

#include <strings.h>
#include "SConsole.h"

Lock *SConsole::readAvailMutex = 0;
Lock *SConsole::someoneWritingMutex = 0;
Lock *SConsole::writeDoneMutex = 0;
Condition *SConsole::writeDoneNotifier = 0;
Condition *SConsole::readAvailNotifier = 0;

SConsole::SConsole()
  : console_(new Console(STDIN_FILE,STDOUT_FILE,ReadAvail,WriteDone,0))
{ 
    readAvailMutex = new Lock("read avail");
    writeDoneMutex = new Lock("write done");

    someoneWritingMutex = new Lock( "thrd in SC::write" ) ;
    readAvailNotifier = new Condition("read condition");
    writeDoneNotifier = new Condition("write condition");
} 
    
// 
// ( destructor ) 
// 
SConsole::~SConsole() 
{ 
    delete console_ ;
    delete readAvailMutex ;
    delete writeDoneMutex ;
    delete someoneWritingMutex ;
    delete readAvailNotifier ;
    delete writeDoneNotifier ;
}

//
// ReadAvail
//
void SConsole::ReadAvail(int foo)
{
    readAvailNotifier->Broadcast( readAvailMutex );
}

//
// WriteDone
//
void SConsole::WriteDone(int foo)
{
    writeDoneNotifier->Broadcast( writeDoneMutex );
}

//
// read
//
size_t SConsole::read(char *buffer,size_t nbytes)
{
    size_t r;
    bzero(buffer,nbytes);

    readAvailMutex->Acquire();
    for(r = 0; r < nbytes; r++) {
        readAvailNotifier->Wait( readAvailMutex );
	buffer[r] = console_->GetChar();
	if(buffer[r] == EOF || buffer[r] == '\n') {
	    buffer[r] = 0;
	    readAvailMutex->Release();
	    return r;
	}
    }
    readAvailMutex->Release();

    return r;
}

size_t SConsole::write(const char *buffer,size_t nbytes)
{
    size_t r;

    someoneWritingMutex->Acquire() ;
    writeDoneMutex->Acquire();
    for(r = 0; r < nbytes; r++) {
	console_->PutChar(buffer[r]);
	writeDoneNotifier->Wait( writeDoneMutex );
    }
    writeDoneMutex->Release();
    someoneWritingMutex->Release() ;

    return r;
}
