// File:         $Id: dklist.h,v 3.0 2001/11/04 19:46:54 trc2876 Exp $
// Author:       Trevor Clarke, Eric Eells, Eric Ferguson, Dan Westgate
// Contributors: {}
// Description:  This is a doubly keyed, singly linked list. It is
//		 essentially two singly linked lists that shared the
//		 same key/data pairs but key on both
// Revisions:
//               $Log: dklist.h,v $
//               Revision 3.0  2001/11/04 19:46:54  trc2876
//               force to 3.0
//
//               Revision 2.6  2001/10/24 23:01:37  trc2876
//               more misc compile fixes
//
//               Revision 2.5  2001/10/23 22:25:04  trc2876
//               make that unsigned long long
//
//               Revision 2.4  2001/10/23 22:22:55  trc2876
//               keys are now unsigned long
//
//               Revision 2.3  2001/10/23 20:41:29  trc2876
//               interrum release
//
//               Revision 2.2  2001/10/23 19:53:58  trc2876
//               reordered args for consistency
//
//               Revision 2.1  2001/10/23 19:50:36  trc2876
//               added dklist
//
//

#ifndef DKLIST_H
#define DKLIST_H

#include "utility.h"

class DKListElement {
   public:
     DKListElement(unsigned long long keya, unsigned long long keyb, void *data);

     DKListElement *a_next;     // next element in first list
			        // NULL if this is the last
     DKListElement *b_next;     // next element in second list
			        // NULL if this is the last
     unsigned long long a;	// first value
     unsigned long long b;	// second value
     void *ndata;
};

class DKList {
  public:
    enum key_t {A=0,B=1,DATA};

  public:
    DKList();
    ~DKList();

    void Remove(key_t k);
    void NodeRemove(DKListElement *node,key_t k,bool deleteIt=true);
    void SortedInsert(unsigned long long keya, unsigned long long keyb, void *data);
    void *SortedRetrieve(unsigned long long key, key_t k);
    void *SortedRemove(unsigned long long key, key_t k);
    bool IsEmpty() { return (a_first == NULL) || (b_first == NULL); }
    void map(VoidFunctionPtr func,key_t arg,key_t k);

    DKListElement *getAfirst() { return a_first; }
    DKListElement *getBfirst() { return b_first; }

  private:
    DKListElement *a_first;
    DKListElement *b_first;
};

#endif // DKLIST_H
