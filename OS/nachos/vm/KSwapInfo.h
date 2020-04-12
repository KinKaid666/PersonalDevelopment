#ifndef _KSwapInfo_H_
#define _KSwapInfo_H_

#include "synch.h"
#include "translate.h"
#include "bitmap.h"

// This is getPhysAddr will use to make a request to the swapper thread.
struct Request_t
{
    int pid_ ;
    int vAddr_ ;
    Semaphore* notifier_ ;
    int* returnVarAddr_ ;
    bool* readOnlyAddr_ ;
};

class RevPageEntry 
  : public TranslationEntry
{
public:
    int pid ;
} ;
/*
// This makes up the reverse page table.
struct RevPageEntry_t
{
    int physPage_ ;
    int virtualAddr_ ;
    unsigned char pid_ ;
    bool readOnly_ ;
    bool dirty_ ;
};
*/

// This will make up the segment table
struct SegTblEntry_t
{
    int vStart_ ;
    int vLength_ ;
    unsigned char pid_ ;
    bool inExecFile_ ;
    int addrInFile_ ;
    
#ifdef VM_STATS
    // This bitmap is used for VM stats. It indicates which pages have been
    // brought in at least once.
    BitMap* pgInitIn_ ;

    // This bitmap is used for VM stats. It indicates that a page has been
    // paged out at least once due to the LRU algorithm.
    BitMap* pgLRUout_ ;
#endif

};

#endif


