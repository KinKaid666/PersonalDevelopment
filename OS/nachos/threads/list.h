// list.h 
//  Data structures to manage LISP-like lists.  
//
//      As in LISP, a list can contain any type of data structure
//  as an item on the list: thread control blocks, 
//  pending interrupts, etc.  That is why each item is a "void *",
//  or in other words, a "pointers to anything".
//
// Copyright (c) 1992-1993 The Regents of the University of California.
// All rights reserved.  See copyright.h for copyright notice and limitation 
// of liability and disclaimer of warranty provisions.

// $Id: list.h,v 3.0 2001/11/04 19:46:54 trc2876 Exp $

// $Log: list.h,v $
// Revision 3.0  2001/11/04 19:46:54  trc2876
// force to 3.0
//
// Revision 2.0  2001/10/11 02:53:23  trc2876
// force update to 2.0 tree
//
// Revision 1.3  2001/09/27 15:52:15  eae8264
// Added accessors for first and last item of list so iteration is possible.
//
// Revision 1.2  2001/09/27 03:38:35  etf2954
//
// nothing
//
// Revision 1.1  2001/09/23 20:19:12  etf2954
// Initial revision
//

#ifndef LIST_H
#define LIST_H

#include "copyright.h"
#include "utility.h"

// The following class defines a "list element" -- which is
// used to keep track of one item on a list.  It is equivalent to a
// LISP cell, with a "car" ("next") pointing to the next element on the list,
// and a "cdr" ("item") pointing to the item on the list.
//
// Internal data structures kept public so that List operations can
// access them directly.

class ListElement {
   public:
     ListElement(void *itemPtr, int sortKey);   // initialize a list element

     ListElement *next;     // next element on list, 
                // NULL if this is the last
     int key;               // priority, for a sorted list
     void *item;            // pointer to item on the list
};

// The following class defines a "list" -- a singly linked list of
// list elements, each of which points to a single item on the list.
//
// By using the "Sorted" functions, the list can be kept in sorted
// in increasing order by "key" in ListElement.

class List {
  public:
    List();         // initialize the list
    ~List();            // de-allocate the list

    void Prepend(void *item);   // Put item at the beginning of the list
    void Append(void *item);    // Put item at the end of the list
    void *Remove();         // Take item off the front of the list

    void Mapcar(VoidFunctionPtr func);  // Apply "func" to every element 
                    // on the list
    bool IsEmpty();     // is the list empty? 
    

    // Routines to put/get items on/off list in order (sorted by key)
    void SortedInsert(void *item, int sortKey); // Put item into list
    void *SortedRemove(int *keyPtr);        // Remove first item from list

    bool PeekSorted(int* KeyPtr);    // peek at key of top element
    void *KeySortedRemove(int Key);     // remove entry with this key
    void *LookAtSorted(int Key);       // return pointer to entry with this key
    
    ListElement* getFirstItem() { return first; };
    ListElement* getLastItem() { return last; };

        
  private:
    ListElement *first;     // Head of the list, NULL if list is empty
    ListElement *last;      // Last element of list
};

#endif // LIST_H
