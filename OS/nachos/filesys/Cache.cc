// File:         $Id: Cache.cc,v 3.1 2001/11/11 03:55:07 eae8264 Exp $
// Author:       p544-01b
// Contributors: 
// Description:	 The implementation file for Cache
// Revisions:
//               $Log: Cache.cc,v $
//               Revision 3.1  2001/11/11 03:55:07  eae8264
//               Added some classes that don't compile for some reaason.
//               Nothing works anywhere, so I don't feel bad!
//

#include "Cache.h"
#include "system.h"

//
// Name:	(constructor)
//
Cache::Cache( unsigned int cacheSize )
:	cache_( new List() ),
	maxSize_( cacheSize ),
	curSize_( 0 ),
	mutex_( new Lock( "cache lock" ) ),
	rwBuf_( new unsigned char[ SectorSize ] ),
	diskSize_( NumSectors * SectorSize )
{
    ASSERT( maxSize_ > 1 ) ;
} // constructor

//
// Name: (destructor)
//
Cache::~Cache()
{
    delete cache_ ;
    delete mutex_ ;
    delete rwBuf_ ;
} // destructor

//
// Name: getDataFromDisk
//
void Cache::getDataFromDisk( T_WORD address,
	 			unsigned char* buf,
				unsigned int len
		    	       )
{
    ASSERT( address < diskSize_ ) ;

    unsigned int diskSector( address / SectorSize ) ;
    unsigned int offsetInSector( address % SectorSize ) ;

    synchDisk->ReadSector( diskSector, (char *)rwBuf_ ) ;
    memcpy( buf, rwBuf_ + offsetInSector, len ) ;

} // getDataFromDisk

//
// Name: writeDataToDisk
//
void Cache::writeDataToDisk( T_WORD address,
		     		unsigned char* buf,
		     		unsigned int len
		    	       )
{
    ASSERT( address < diskSize_ ) ;

    unsigned int diskSector( address / SectorSize ) ;
    unsigned int offsetInSector( address % SectorSize ) ;

    synchDisk->ReadSector( diskSector, (char *)rwBuf_ ) ;
    memcpy( rwBuf_ + offsetInSector, buf, len ) ;
    synchDisk->WriteSector( diskSector, (char *)rwBuf_ ) ;

} // writeDataToDisk
