//******************************************************************************
//
// File:    ExportMap.java
// Package: edu.rit.m2mi
// Unit:    Class edu.rit.m2mi.ExportMap
//
// This Java source file is copyright (C) 2002 by the Rochester Institute of
// Technology. All rights reserved. For further information, contact the author,
// Alan Kaminsky, at ark@it.rit.edu.
//
// This Java source file is part of the M2MI Library ("The Library"). The
// Library is free software; you can redistribute it and/or modify it under the
// terms of the GNU General Public License as published by the Free Software
// Foundation; either version 2 of the License, or (at your option) any later
// version.
//
// The Library is distributed in the hope that it will be useful, but WITHOUT
// ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
// FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
// details.
//
// A copy of the GNU General Public License is provided in the file gpl.txt. You
// may also obtain a copy of the GNU General Public License on the World Wide
// Web at http://www.gnu.org/licenses/gpl.html or by writing to the Free
// Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307
// USA.
//
//******************************************************************************

package edu.rit.m2mi;

import edu.rit.m2mp.IncomingMessageNotifier;
import edu.rit.m2mp.MessageFilter;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

/**
 * Class ExportMap encapsulates a collection of objects that have been exported
 * to the M2MI layer. The export map lets you record an exported object. The
 * object is exported with a certain target interface name, and each object is
 * exported with a certain exported object identifier (EOID). Once exported, the
 * export map lets you obtain a collection of those objects exported with a
 * certain target interface name, and the export map lets you obtain a
 * collection of those objects exported with a certain EOID. The export map also
 * lets you unexport an object.
 * <P>
 * Class ExportMap is not multiple thread safe. Make sure only one thread at a
 * time ever calls a method on an export map.
 * <P>
 * The M2MI layer goes to the export map to get the object or objects on which
 * to perform each incoming M2MI invocation. For an omnihandle invocation, the
 * M2MI layer gets the collection of all objects exported with the target
 * interface name. For a unihandle or multihandle invocation, the M2MI layer
 * gets the object or objects exported with the EOID given in the invocation.
 * <P>
 * As objects are exported and unexported, the export map registers and
 * deregisters the appropriate message filters with the incoming message
 * notifier for the underlying M2MP layer, if any. This ensures that incoming
 * M2MP messages containing M2MI invocations from other processes or hosts for
 * this process's exported objects are passed up to the M2MI layer, and that
 * invocations for objects not exported by this process are filtered out.
 * <P>
 * <B>M2MI Library Version 02-Jul-2002</B>
 *
 * @author  Alan Kaminsky
 * @version 05-Jun-2002
 */
class ExportMap
	{

// Hidden data members.

	/**
	 * Incoming message notifier for this M2MI layer, or null if there is none.
	 */
	private IncomingMessageNotifier myNotifier;

	/**
	 * EOID map: key is the EOID (type Eoid), value is a collection (type
	 * LinkedList) containing the objects exported with that EOID.
	 */
	private HashMap myEoidMap = new HashMap();

	/**
	 * Interface map: key is the target interface name (type String), value is a
	 * collection (type LinkedList) containing the objects exported with that
	 * target interface name.
	 */
	private HashMap myInterfaceMap = new HashMap();

// Exported constructors.

	/**
	 * Construct a new, empty export map with the given incoming message
	 * notifier.
	 *
	 * @param  theNotifier  Incoming message notifier for this M2MI layer, or
	 *                      null if there is none.
	 */
	public ExportMap
		(IncomingMessageNotifier theNotifier)
		{
		myNotifier = theNotifier;
		}

// Exported operations.

	/**
	 * Determine if any objects are exported with the given EOID.
	 *
	 * @param  theEoid  Exported object identifier.
	 *
	 * @return  True if any objects are exported with <TT>theEOID</TT>, false
	 *          otherwise.
	 */
	public boolean isExported
		(Eoid theEoid)
		{
		return myEoidMap.containsKey (theEoid);
		}

	/**
	 * Determine if the given object is exported with the given EOID.
	 *
	 * @param  theObject  Object to test.
	 * @param  theEoid    Exported object identifier.
	 *
	 * @return  True if <TT>theObject</TT> is exported with <TT>theEOID</TT>,
	 *          false otherwise.
	 */
	public boolean isExported
		(Object theObject,
		 Eoid theEoid)
		{
		return isExported (myEoidMap, theEoid, theObject);
		}

	/**
	 * Determine if any objects are exported with the given target interface.
	 *
	 * @param  theInterfaceName  Fully-qualified name of the target interface.
	 *
	 * @return  True if any objects are exported with <TT>theInterfaceName</TT>,
	 *          false otherwise.
	 */
	public boolean isExported
		(String theInterfaceName)
		{
		return myInterfaceMap.containsKey (theInterfaceName);
		}

	/**
	 * Determine if the given object is exported with the given target
	 * interface.
	 *
	 * @param  theObject         Object to test.
	 * @param  theInterfaceName  Fully-qualified name of the target interface.
	 *
	 * @return  True if <TT>theObject</TT> is exported with
	 *          <TT>theInterfaceName</TT>, false otherwise.
	 */
	public boolean isExported
		(Object theObject,
		 String theInterfaceName)
		{
		return isExported (myInterfaceMap, theInterfaceName, theObject);
		}

	/**
	 * Export the given object with the given EOID. If the given object is
	 * already exported with the given EOID, it simply remains exported with
	 * that EOID.
	 *
	 * @param  theObject  Object to be exported.
	 * @param  theEoid    Exported object identifier.
	 *
	 * @exception  NullPointerException
	 *     (unchecked exception) Thrown if any argument is null.
	 */
	public void export
		(Object theObject,
		 Eoid theEoid)
		{
		if (theObject == null || theEoid == null)
			{
			throw new NullPointerException();
			}
		addToMap (myEoidMap, theEoid, theObject);
		}

	/**
	 * Export the given object with the given target interface name. If the
	 * given object is already exported with the given target interface name, it
	 * simply remains exported with that target interface name.
	 * <P>
	 * <I>Note:</I> The given object is not exported with any superinterfaces of
	 * the target interface name. To accomplish that, you must call
	 * <TT>export()</TT> separately for each superinterface.
	 *
	 * @param  theObject         Object to be exported.
	 * @param  theInterfaceName  Fully-qualified name of the target interface.
	 *
	 * @exception  NullPointerException
	 *     (unchecked exception) Thrown if any argument is null.
	 */
	public void export
		(Object theObject,
		 String theInterfaceName)
		{
		if (theObject == null || theInterfaceName == null)
			{
			throw new NullPointerException();
			}
		addToMap (myInterfaceMap, theInterfaceName, theObject);
		}

	/**
	 * Unexport the given object. The object is unexported from all EOIDs and
	 * target interfaces with which the object had previously been exported. If
	 * the object had not been previously exported, this method does nothing.
	 *
	 * @param  theObject  Object to be unexported.
	 */
	public void unexport
		(Object theObject)
		{
		removeFromMap (myEoidMap, theObject);
		removeFromMap (myInterfaceMap, theObject);
		}

	/**
	 * Unexport the given object from the given EOID. The object remains
	 * exported with any other EOIDs and target interfaces with which the object
	 * had previously been exported. If the object had not been previously
	 * exported with the given EOID, this method does nothing.
	 *
	 * @param  theObject  Object to be unexported.
	 * @param  theEoid    Exported object identifier.
	 */
	public void unexport
		(Object theObject,
		 Eoid theEoid)
		{
		removeFromMap (myEoidMap, theEoid, theObject);
		}

	/**
	 * Unexport all objects from the given EOID. These objects remain exported
	 * with any other EOIDs and target interfaces with which these objects had
	 * previously been exported.
	 *
	 * @param  theEoid  Exported object identifier.
	 */
	public void unexport
		(Eoid theEoid)
		{
		myEoidMap.remove (theEoid);
		}

	/**
	 * Obtain a collection of the objects exported with the given EOID.
	 * <P>
	 * The collection's contents may be read but should not be modified
	 * directly. Objects should not be exported or unexported in this export map
	 * while the collection's contents are being read (e.g., while an iterator
	 * over the collection is in progress). If these conditions are violated,
	 * the results are not specified.
	 *
	 * @param  theEoid  Exported object identifier.
	 *
	 * @return  Collection of objects exported with <TT>theEoid</TT>, or null if
	 *          there are no such objects.
	 */
	public Collection getExportedObjects
		(Eoid theEoid)
		{
		return (Collection) myEoidMap.get (theEoid);
		}

	/**
	 * Obtain a collection of the objects exported with the given target
	 * interface name.
	 * <P>
	 * The collection's contents may be read but should not be modified
	 * directly. Objects should not be exported or unexported in this export map
	 * while the collection's contents are being read (e.g., while an iterator
	 * over the collection is in progress). If these conditions are violated,
	 * the results are not specified.
	 *
	 * @param  theInterfaceName  Fully-qualified name of the target interface.
	 *
	 * @return  Collection of objects exported with <TT>theInterfaceName</TT>,
	 *          or null if there are no such objects.
	 */
	public Collection getExportedObjects
		(String theInterfaceName)
		{
		return (Collection) myInterfaceMap.get (theInterfaceName);
		}

// Hidden operations.

	/**
	 * Returns true if the given object is exported in the given map with the
	 * given key.
	 *
	 * @param  theMap     Map.
	 * @param  theKey     Key.
	 * @param  theObject  Object to test.
	 */
	private boolean isExported
		(Map theMap,
		 Object theKey,
		 Object theObject)
		{
		// If the object is null, it's not exported.
		if (theObject == null)
			{
			return false;
			}

		// Get linked list of objects associated with the key.
		LinkedList theList = (LinkedList) theMap.get (theKey);

		// If there is no list, the object is not exported.
		if (theList == null)
			{
			return false;
			}

		// If there is a list, search for the object in the list.
		else
			{
			Iterator iter = theList.iterator();
			while (iter.hasNext())
				{
				if (iter.next() == theObject)
					{
					return true;
					}
				}
			return false;
			}
		}

	/**
	 * Add the given object to the collection of objects associated with the
	 * given key in the given map.
	 *
	 * @param  theMap     Map.
	 * @param  theKey     Key.
	 * @param  theObject  Object to add.
	 */
	private void addToMap
		(Map theMap,
		 Object theKey,
		 Object theObject)
		{
		// Get linked list of objects associated with the key.
		LinkedList theList = (LinkedList) theMap.get (theKey);

		// If there is no list, create it, and put the object in it.
		if (theList == null)
			{
			theList = new LinkedList();
			theList.addLast (theObject);
			theMap.put (theKey, theList);
			if (myNotifier != null)
				{
				myNotifier.addMessageFilter (getMessageFilter (theKey));
				}
			}

		// If there is a list, search for the object in the list, and if not
		// found add the object.
		else
			{
			Object obj = null;
			Iterator iter = theList.iterator();
			while (iter.hasNext() && (obj = iter.next()) != theObject)
				{
				obj = null;
				}
			if (obj == null)
				{
				theList.addLast (theObject);
				}
			}
		}

	/**
	 * Remove the given object from all collections of objects in the given map.
	 * If any collection becomes empty, remove its associated key from the map
	 * as well.
	 *
	 * @param  theMap     Map.
	 * @param  theObject  Object to remove.
	 */
	private void removeFromMap
		(Map theMap,
		 Object theObject)
		{
		// Scan all key-value pairs in the map.
		Iterator mapIter = theMap.entrySet().iterator();
		while (mapIter.hasNext())
			{
			// Get the current key and list of objects.
			Map.Entry theEntry = (Map.Entry) mapIter.next();
			Object theKey = theEntry.getKey();
			LinkedList theList = (LinkedList) theEntry.getValue();

			// Search for the object in the list.
			Iterator listIter = theList.iterator();
			while (listIter.hasNext())
				{
				if (theObject == listIter.next())
					{
					// Object was found. Remove it from the list.
					listIter.remove();

					// If the list became empty, remove its associated key from
					// the map.
					if (theList.isEmpty())
						{
						mapIter.remove();
						if (myNotifier != null)
							{
							myNotifier.removeMessageFilter
								(getMessageFilter (theKey));
							}
						}

					// No point searching the list any more.
					break;
					}
				}
			}
		}

	/**
	 * Remove the given object from the collection of objects associated with
	 * the given key in the given map. If the collection becomes empty, remove
	 * the given key from the map as well.
	 *
	 * @param  theMap     Map.
	 * @param  theKey     Key.
	 * @param  theObject  Object to remove.
	 */
	private void removeFromMap
		(Map theMap,
		 Object theKey,
		 Object theObject)
		{
		// Get the list of objects associated with the key.
		LinkedList theList = (LinkedList) theMap.get (theKey);

		if (theList != null)
			{
			// Search for the object in the list.
			Iterator listIter = theList.iterator();
			while (listIter.hasNext())
				{
				if (theObject == listIter.next())
					{
					// Object was found. Remove it from the list.
					listIter.remove();

					// If the list became empty, remove its associated key from
					// the map.
					if (theList.isEmpty())
						{
						theMap.remove (theKey);
						if (myNotifier != null)
							{
							myNotifier.removeMessageFilter
								(getMessageFilter (theKey));
							}
						}

					// No point searching the list any more.
					break;
					}
				}
			}
		}

	/**
	 * Obtain the message filter for an M2MP message carrying an M2MI invocation
	 * for the given export map key (target interface name or EOID).
	 *
	 * @param  theKey  Key.
	 *
	 * @return  Message filter.
	 */
	private static MessageFilter getMessageFilter
		(Object theKey)
		{
		return new MessageFilter (M2MI.getMessagePrefix (theKey));
		}

	}
