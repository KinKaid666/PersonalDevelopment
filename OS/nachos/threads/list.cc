// list.cc 
//
//      Routines to manage a singly-linked list of "things".
//
//  A "ListElement" is allocated for each item to be put on the
//  list; it is de-allocated when the item is removed. This means
//      we don't need to keep a "next" pointer in every object we
//      want to put on a list.
// 
//      NOTE: Mutual exclusion must be provided by the caller.
//      If you want a synchronized list, you must use the routines 
//  in synchlist.cc.
//
// Copyright (c) 1992-1993 The Regents of the University of California.
// All rights reserved.  See copyright.h for copyright notice and limitation 
// of liability and disclaimer of warranty provisions.

// $Id: list.cc,v 3.0 2001/11/04 19:46:54 trc2876 Exp $

// $Log: list.cc,v $
// Revision 3.0  2001/11/04 19:46:54  trc2876
// force to 3.0
//
// Revision 2.0  2001/10/11 02:53:23  trc2876
// force update to 2.0 tree
//
// Revision 1.1  2001/09/23 20:19:01  etf2954
// Initial revision
//

#include "copyright.h"
#include "list.h"

//----------------------------------------------------------------------
// ListElement::ListElement
//  Initialize a list element, so it can be added somewhere on a list.
//
//  "itemPtr" is the item to be put on the list.  It can be a pointer
//      to anything.
//  "sortKey" is the priority of the item, if any.
//----------------------------------------------------------------------

ListElement::ListElement(void *itemPtr, int sortKey)
{
     item = itemPtr;
     key = sortKey;
     next = NULL;   // assume we'll put it at the end of the list 
}

//----------------------------------------------------------------------
// List::List
//  Initialize a list, empty to start with.
//  Elements can now be added to the list.
//----------------------------------------------------------------------

List::List()
{ 
    first = last = NULL; 
}

//----------------------------------------------------------------------
// List::~List
//  Prepare a list for deallocation.  If the list still contains any 
//  ListElements, de-allocate them.  However, note that we do *not*
//  de-allocate the "items" on the list -- this module allocates
//  and de-allocates the ListElements to keep track of each item,
//  but a given item may be on multiple lists, so we can't
//  de-allocate them here.
//----------------------------------------------------------------------

List::~List()
{ 
    while (Remove() != NULL)
    ;    // delete all the list elements
}

//----------------------------------------------------------------------
// List::Append
//      Append an "item" to the end of the list.
//      
//  Allocate a ListElement to keep track of the item.
//      If the list is empty, then this will be the only element.
//  Otherwise, put it at the end.
//
//  "item" is the thing to put on the list, it can be a pointer to 
//      anything.
//----------------------------------------------------------------------

void
List::Append(void *item)
{
    ListElement *element = new ListElement(item, 0);

    if (IsEmpty()) {        // list is empty
    first = element;
    last = element;
    } else {            // else put it after last
    last->next = element;
    last = element;
    }
}

//----------------------------------------------------------------------
// List::Prepend
//      Put an "item" on the front of the list.
//      
//  Allocate a ListElement to keep track of the item.
//      If the list is empty, then this will be the only element.
//  Otherwise, put it at the beginning.
//
//  "item" is the thing to put on the list, it can be a pointer to 
//      anything.
//----------------------------------------------------------------------

void
List::Prepend(void *item)
{
    ListElement *element = new ListElement(item, 0);

    if (IsEmpty()) {        // list is empty
    first = element;
    last = element;
    } else {            // else put it before first
    element->next = first;
    first = element;
    }
}

//----------------------------------------------------------------------
// List::Remove
//      Remove the first "item" from the front of the list.
// 
// Returns:
//  Pointer to removed item, NULL if nothing on the list.
//----------------------------------------------------------------------

void *
List::Remove()
{
    return SortedRemove(NULL);  // Same as SortedRemove, but ignore the key
}

//----------------------------------------------------------------------
// List::Mapcar
//  Apply a function to each item on the list, by walking through  
//  the list, one element at a time.
//
//  Unlike LISP, this mapcar does not return anything!
//
//  "func" is the procedure to apply to each element of the list.
//----------------------------------------------------------------------

void
List::Mapcar(VoidFunctionPtr func)
{
    for (ListElement *ptr = first; ptr != NULL; ptr = ptr->next) {
       DEBUG('l', "In mapcar, about to invoke %x(%x)\n", func, ptr->item);
       (*func)((int)ptr->item);
    }
}

//----------------------------------------------------------------------
// List::IsEmpty
//      Returns TRUE if the list is empty (has no items).
//----------------------------------------------------------------------

bool
List::IsEmpty() 
{ 
    if (first == NULL)
        return TRUE;
    else
    return FALSE; 
}

//----------------------------------------------------------------------
// List::SortedInsert
//      Insert an "item" into a list, so that the list elements are
//  sorted in increasing order by "sortKey".
//      
//  Allocate a ListElement to keep track of the item.
//      If the list is empty, then this will be the only element.
//  Otherwise, walk through the list, one element at a time,
//  to find where the new item should be placed.
//
//  "item" is the thing to put on the list, it can be a pointer to 
//      anything.
//  "sortKey" is the priority of the item.
//----------------------------------------------------------------------

void
List::SortedInsert(void *item, int sortKey)
{
    ListElement *element = new ListElement(item, sortKey);
    ListElement *ptr;       // keep track

    if (IsEmpty()) {    // if list is empty, put
        first = element;
        last = element;
    } else if (sortKey < first->key) {  
        // item goes on front of list
    element->next = first;
    first = element;
    } else {        // look for first elt in list bigger than item
        for (ptr = first; ptr->next != NULL; ptr = ptr->next) {
            if (sortKey < ptr->next->key) {
        element->next = ptr->next;
            ptr->next = element;
        return;
        }
    }
    last->next = element;       // item goes at end of list
    last = element;
    }
}

//----------------------------------------------------------------------
// List::SortedRemove
//      Remove the first "item" from the front of a sorted list.
// 
// Returns:
//  Pointer to removed item, NULL if nothing on the list.
//  Sets *keyPtr to the priority value of the removed item
//  (this is needed by interrupt.cc, for instance).
//
//  "keyPtr" is a pointer to the location in which to store the 
//      priority of the removed item.
//----------------------------------------------------------------------

void *
List::SortedRemove(int *keyPtr)
{
    ListElement *element = first;
    void *thing;

    if (IsEmpty()) 
    return NULL;

    thing = first->item;
    if (first == last) {    // list had one item, now has none 
        first = NULL;
    last = NULL;
    } else {
        first = element->next;
    }
    if (keyPtr != NULL)
        *keyPtr = element->key;
    delete element;
    return thing;
}


//----------------------------------------------------------------------
// List::PeekSorted()
//  Peek at the key of the top element on the list
//
// Returns:
//  This function places the key for the top element on the list in the
//  location pointed to by KeyPtr.  The function returns TRUE is the list
//  is not empty, FALSE otherwise.  If the list is empty then *KeyPtr is
//  unmodified.
//
//----------------------------------------------------------------------
bool List::PeekSorted(int* KeyPtr)
{
    if (first == NULL)
        return FALSE;
    else
    {
        *KeyPtr =  first->key;
        return TRUE;
    }
}

//----------------------------------------------------------------------
// List::KeySortedRemove()
//  Remove the element that has Key as its sort index
//----------------------------------------------------------------------
void* List::KeySortedRemove(int Key)
{
    void* RetValue = NULL;
    
    if(!IsEmpty())
    {                                        // list is not empty
        ListElement* Element = first;        // start at head of list
	ListElement* previous;            // keep track of previous item

	// loop until the end of list or a key matches
        while((Element != NULL) && (Element->key != Key))
        {
	    previous = Element;
            Element = Element->next;
        }

	// If an element was found clean up the list and pointers
        if(Element != NULL)
        {
	    // if only one item, empty the list
	    if(first == last) 
	    {
		first = NULL;
		last = NULL;
	    }

	    // if first element fix front pointer
	    else if(Element == first) 
	    {
		first = Element->next;
	    }

	    // if last element fix tail pointer
	    else if(Element == last)
	    {
		previous->next = NULL;
		last = previous;
	    }

	    // drop it out of the middle
	    else 
	    {
		previous->next = Element->next;
	    }
	    
	    // return the actual item found
	    RetValue = Element->item;
            delete Element;
        }
    }
    
    return RetValue;
}   


//----------------------------------------------------------------------
// List::LookAtSorted()
//  Return a pointer to the element with Key as its sort index
//----------------------------------------------------------------------
void* List::LookAtSorted(int Key)
{
    ListElement* Element = first;

    while((Element != NULL) && (Element->key != Key))
        Element = Element->next;

    return (Element == NULL) ? NULL : Element->item;
}
