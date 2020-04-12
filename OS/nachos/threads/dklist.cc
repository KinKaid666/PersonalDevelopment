// File:         $Id: dklist.cc,v 3.0 2001/11/04 19:46:54 trc2876 Exp $
// Author:       Trevor Clarke, Eric Eells, Eric Ferguson, Dan Westgate
// Contributors: {}
// Description:  Doubly keyed, singly linked list
// Revisions:
//               $Log: dklist.cc,v $
//               Revision 3.0  2001/11/04 19:46:54  trc2876
//               force to 3.0
//
//               Revision 2.6  2001/10/23 22:25:04  trc2876
//               make that unsigned long long
//
//               Revision 2.5  2001/10/23 22:22:55  trc2876
//               keys are now unsigned long
//
//               Revision 2.4  2001/10/23 21:38:22  trc2876
//               mofo, it works :)
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

#include "dklist.h"

#define SELECT_KEY(k__,a__,b__) ((k__ == A) ? a__ : b__)

DKListElement::DKListElement(unsigned long long keya,unsigned long long keyb,void *data) :
	a_next(NULL),
	b_next(NULL),
	a(keya),
	b(keyb),
	ndata(data)
{
}

DKList::DKList() : a_first(NULL),b_first(NULL)
{
}

DKList::~DKList()
{
    while(!IsEmpty())
	Remove(A);
}

void
DKList::Remove(key_t k)
{
    DKListElement *tmp = SELECT_KEY(k,a_first,b_first);

    if(tmp == NULL)
	return;
    SELECT_KEY(k,a_first,b_first) = SELECT_KEY(k,tmp->a_next,tmp->b_next);
    NodeRemove(tmp,SELECT_KEY(k,B,A),true);
}

void
DKList::NodeRemove(DKListElement *node,key_t k,bool deleteIt)
{
    DKListElement *cur = SELECT_KEY(k,a_first,b_first);

    if(cur == node) {
	SELECT_KEY(k,a_first,b_first) = SELECT_KEY(k,cur->a_next,cur->b_next);
	if(deleteIt) delete cur;
    } else {
	for(; SELECT_KEY(k,cur->a_next,cur->b_next) != NULL;
		    cur = SELECT_KEY(k,cur->a_next,cur->b_next)) {
	    if(SELECT_KEY(k,cur->a_next,cur->b_next) == node) {
		SELECT_KEY(k,cur->a_next,cur->b_next) =
			SELECT_KEY(k,cur->a_next->a_next,cur->b_next->b_next);
		if(deleteIt) delete node;
		break;
	    }
	}
    }
}

void
DKList::SortedInsert(unsigned long long keya, unsigned long long keyb, void *data)
{
    DKListElement *element = new DKListElement(keya,keyb,data);

    // insert into the first list
    if(a_first == NULL) {
	a_first = element;
    } else if(keya < a_first->a) {
	element->a_next = a_first;
	a_first = element;
    } else {
	for(DKListElement *cur = a_first; cur != NULL; cur = cur->a_next) {
	    if(cur == element) {
		continue;
	    }
	    if(cur->a_next == NULL) {
		cur->a_next = element;
		break;
	    } else if(keya < cur->a_next->a) {
		element->a_next = cur->a_next;
		cur->a_next = element;
		break;
	    }
	}
    }

    // insert into the second list
    if(b_first == NULL) {
	b_first = element;
    } else if(keyb < b_first->b) {
	element->b_next = b_first;
	b_first = element;
    } else {
	for(DKListElement *cur = b_first; cur != NULL; cur = cur->b_next) {
	    if(cur == element)
		continue;
	    if(cur->b_next == NULL) {
		cur->b_next = element;
		break;
	    } else if(keyb < cur->b_next->b) {
		element->b_next = cur->b_next;
		cur->b_next = element;
		break;
	    }
	}
    }
}

void *
DKList::SortedRetrieve(unsigned long long key, key_t k)
{
    if(SELECT_KEY(k,a_first,b_first) == NULL)
	return NULL;
    for(DKListElement *cur = SELECT_KEY(k,a_first,b_first); cur != NULL;
		cur = SELECT_KEY(k,cur->a_next,cur->b_next)) {
	if(key == SELECT_KEY(k,cur->a,cur->b))
	    return cur->ndata;
    }
    return NULL;
}

void *
DKList::SortedRemove(unsigned long long key, key_t k)
{
    void *ret=NULL;

    if(SELECT_KEY(k,a_first,b_first) != NULL) {
	for(DKListElement *cur = SELECT_KEY(k,a_first,b_first); cur != NULL;
		    cur = SELECT_KEY(k,cur->a_next,cur->b_next)) {
	    if(key == SELECT_KEY(k,cur->a,cur->b)) {
		ret = cur->ndata;
		NodeRemove(cur,A,false);
		NodeRemove(cur,B,true);
		break;
	    }
	}
    }
    return ret;
}

void
DKList::map(VoidFunctionPtr func,key_t arg,key_t k)
{
    for(DKListElement *cur = SELECT_KEY(k,a_first,b_first); cur != NULL;
		cur = SELECT_KEY(k,cur->a_next,cur->b_next)) {
	switch(arg) {
	    case A:
		func(cur->a);
		break;
	    case B:
		func(cur->b);
		break;
	    case DATA:
		func((int)(cur->ndata));
		break;
	}
    }
}
