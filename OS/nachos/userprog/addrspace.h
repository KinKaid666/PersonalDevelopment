// addrspace.h 
//	Data structures to keep track of executing user programs 
//	(address spaces).
//
//	For now, we don't keep any information about address spaces.
//	The user level CPU state is saved and restored in the thread
//	executing the user program (see thread.h).
//
// Copyright (c) 1992-1993 The Regents of the University of California.
// All rights reserved.  See copyright.h for copyright notice and limitation 
// of liability and disclaimer of warranty provisions.

#ifndef ADDRSPACE_H
#define ADDRSPACE_H

#include "copyright.h"
//#include "filesys.h"
#include "translate.h"
#include "bitmap.h"
#include "vminfo.h"
#include "thread.h"

#define UserStackSize		1024 	// increase this as necessary!
#define TOP_OF_ADDRSPACE	0x7fffffff

class Thread;
class Lock;
class Semaphore;

class AddrSpace {
  public:
    AddrSpace(OpenFile *executable,unsigned char pid);
					// Create an address space,
					// initializing it with the program
					// stored in the file "executable"
    ~AddrSpace();			// De-allocate an address space

    void InitRegisters();		// Initialize user-level CPU registers,
					// before jumping to user code

    void SaveState();			// Save/restore address space-specific
    void RestoreState();		// info on a context switch 

#ifndef VM
    //
    // Name:		allocPhysMemPage
    //
    // Description:	Tries to allocate the next free memory page and 
    //
    // Arguments:	None
    //
    // Return:		returns the address of the next free memory page
    //
    int allocPhysMemPage() ;

    //
    // Name:		freePhysMemPage
    //
    // Description:	Free a memory page previously alloced
    //
    // Arguments:	int pageNum: the page to free
    //
    // Return:		nothing
    //
    void freePhysMemPage( int pageNum ) ;
#endif

    //
    // Name:		userToKern
    //
    // Descriptions:	copys memory from user to kernel space
    //
    // Arguments:	addr	: data to be copied
    //			size	: size
    //			count	: number of items to copy
    //			buffer  : destination buffer
    //				  must be large enough
    //				  actual type is based on size
    //
    // Return:		returns an error code
    //
    int userToKern( int addr, int size, int count, void *buffer ) ;

    //
    // Name:		userToKern
    //
    // Descriptions:	copys memory from kernel to user space
    //
    // Arguments:	addr	: address to copy to in user space
    //			size	: size
    //			count	: number of items to copy
    //			buffer  : source buffer
    //				  actual type is based on size
    //
    // Return:		returns an error code
    //
    int kernToUser( int addr, int size, int count, void *buffer ) ;

    //
    // Name:		copyArgs
    //
    // Descriptions:	copys argc/argv to a clean addrspace, set arguments
    //			to main in thread's user state
    //
    // Arguments:	argc		: number of args
    //			argv		: array of fixed length strings
    //			tos     	: current top of stack
    //			userThread	: main thread for user program
    //
    // Return:		new top of stack
    //
    int copyArgs( int argc, char *argv, int tos, Thread* userThread ) ;

    //
    // Name:		allocStack
    //
    // Description:	allocate stack space for a new thread
    //
    // Arguments:	None
    //
    // Return:		int pointer to top of stack or NULL for error
    //
    int allocStack() ;

    //
    // Name:		freeStack
    //
    // Description:	free stack allocated by allocStack
    //
    // Arguments:	int pointer to stack that is to be freed
    //
    // Return:		returns and error code
    //
    int freeStack(int stackAddress) ;

    //
    // Name:		vmstat
    //
    // Arguments:	ptr to vminfo_t
    //
    // Description: 	returns information about memory allocation
    //
    // Returns:		0 for success, or an error
    //
#ifndef VM
    int vmstat(vminfo_t *info) ;
#endif

    //
    // Name:		TLB_Miss
    //
    // Description:	This will use KSwap to deal with the missing memory
    //			translation.  When this returns, everything is in place.
    //
    // Arguments:	The "bad" virtual address that needs a translation.
    //
    // Return:		void
    //
#ifdef VM
  void TLB_Miss(int address);
#endif

    //
    // Name:        getPID()
    //
    // Description: accessor for pid_
    //
    // Arguments:   None
    //
    // Returns:     pid
    //
    unsigned char getPID() { return pid_; }

    bool failed() { return failed_ ; }
  private:
#ifdef VM
    TranslationEntry TLB[4] ;		// Assume linear page table translation

    // For synchronization of swapping.  KSwap will call wait on this if
    // a swap needs to be made.
    Semaphore* swapLock ;
				
#else 
    TranslationEntry *pageTable ;
#endif
					// for now!
    unsigned int numPages;		// Number of pages in the virtual 
					// address space
    BitMap *allocedStacks;		// allocated stacks
    Lock   *asMutex;			// controll access to allocedStacks
    unsigned char pid_;			// pid for associated process
    bool    failed_ ;
};

#endif // ADDRSPACE_H
