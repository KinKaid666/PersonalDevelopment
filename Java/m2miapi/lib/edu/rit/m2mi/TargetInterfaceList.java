//******************************************************************************
//
// File:    TargetInterfaceList.java
// Package: edu.rit.m2mi
// Unit:    Class edu.rit.m2mi.TargetInterfaceList
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

import java.io.Serializable;

/**
 * Class TargetInterfaceList encapsulates a list of target interfaces for an
 * M2MI handle. A target interface list can be constructed either from one or
 * more interface objects (type Class) or from one or more fully-qualified
 * interface names (type String). However, the target interface list stores only
 * the interface names.
 * <P>
 * <I>Note:</I> The target interfaces are stored in the target interface list in
 * a canonical order, not necessarily in the order provided when the target
 * interface list was constructed.
 * <P>
 * <B>M2MI Library Version 02-Jul-2002</B>
 *
 * @author  Alan Kaminsky
 * @version 20-May-2002
 */
class TargetInterfaceList
	implements Serializable
	{

// Hidden data members.

	private String[] myInterfaceNames;

// Exported constructors.

	/**
	 * Construct a new target interface list containing the given interface.
	 *
	 * @param  theInterface  Target interface.
	 *
	 * @exception  NullPointerException
	 *     (unchecked exception) Thrown if <TT>theInterface</TT> is null.
	 */
	public TargetInterfaceList
		(Class theInterface)
		{
		myInterfaceNames = new String [1];
		insert (theInterface.getName(), 0);
		}

	/**
	 * Construct a new target interface list containing the given interfaces.
	 *
	 * @param  theInterfaces  Array of one or more target interfaces.
	 *
	 * @exception  NullPointerException
	 *     (unchecked exception) Thrown if <TT>theInterfaces</TT> is null.
	 * @exception  IllegalArgumentException
	 *     (unchecked exception) Thrown if <TT>theInterfaces</TT> is zero
	 *     length.
	 */
	public TargetInterfaceList
		(Class[] theInterfaces)
		{
		int n = theInterfaces.length;
		if (n == 0)
			{
			throw new IllegalArgumentException();
			}
		myInterfaceNames = new String [n];
		for (int i = 0; i < n; ++ i)
			{
			insert (theInterfaces[i].getName(), i);
			}
		}

	/**
	 * Construct a new target interface list containing the given interface
	 * name.
	 *
	 * @param  theInterfaceName  Fully-qualified name of the target interface.
	 *
	 * @exception  NullPointerException
	 *     (unchecked exception) Thrown if <TT>theInterfaceName</TT> is null or
	 *     <TT>theClassLoader</TT> is null.
	 */
	public TargetInterfaceList
		(String theInterfaceName)
		{
		myInterfaceNames = new String [1];
		insert (theInterfaceName, 0);
		}

	/**
	 * Construct a new target interface list containing the given interface
	 * names.
	 *
	 * @param  theInterfaceNames  Array of one or more fully-qualified names of
	 *                            the target interfaces.
	 *
	 * @exception  NullPointerException
	 *     (unchecked exception) Thrown if <TT>theInterfaceNames</TT> is null or
	 *     <TT>theClassLoader</TT> is null.
	 * @exception  IllegalArgumentException
	 *     (unchecked exception) Thrown if <TT>theInterfaceNames</TT> is zero
	 *     length.
	 */
	public TargetInterfaceList
		(String[] theInterfaceNames)
		{
		int n = theInterfaceNames.length;
		if (n == 0)
			{
			throw new IllegalArgumentException();
			}
		myInterfaceNames = new String [n];
		for (int i = 0; i < n; ++ i)
			{
			insert (theInterfaceNames[i], 0);
			}
		}

	/**
	 * Insert the given interface name into <TT>myInterfaceNames</TT> such that
	 * the interface names appear in ascending lexicographic order.
	 *
	 * @param  name  Interface name.
	 * @param  n     Number of elements that have already been inserted.
	 */
	private void insert
		(String name,
		 int n)
		{
		// Starting at the end, move elements forward until we find a name less
		// than the given interface name.
		int i = n - 1;
		while (i >= 0 && myInterfaceNames[i].compareTo (name) >= 0)
			{
			myInterfaceNames[i+1] = myInterfaceNames[i];
			}

		// Insert the new element after the one we found.
		myInterfaceNames[i+1] = name;
		}

// Exported operations.

	/**
	 * Returns the size of this target interface list.
	 */
	public int size()
		{
		return myInterfaceNames.length;
		}

	/**
	 * Returns the given target interface name from this target interface list.
	 *
	 * @param  i  Index.
	 *
	 * @return  Target interface name at index <TT>i</TT>.
	 *
	 * @exception  ArrayIndexOutOfBoundsException
	 *     (unchecked exception) Thrown if <TT>i</TT> is not in the range 0 ..
	 *     <TT>size()-1</TT>.
	 */
	public String getInterfaceName
		(int i)
		{
		return myInterfaceNames[i];
		}

	/**
	 * Returns an array of the target interface names in this target interface
	 * list. Modifying the returned array will not affect this target interface
	 * list.
	 *
	 * @return  Array of target interface names.
	 */
	public String[] getInterfaceNames()
		{
		return (String[]) myInterfaceNames.clone();
		}

	/**
	 * Returns an array of the interfaces corresponding to the target interface
	 * names in this target interface list. The interfaces are searched for in
	 * the given class loader.
	 *
	 * @param  theClassLoader  Class loader in which to search.
	 *
	 * @return  Array of target interfaces.
	 *
	 * @exception  ClassNotFoundException
	 *     Thrown if any target interface cannot be found in
	 *     <TT>theClassLoader</TT>.
	 */
	public Class[] getInterfaces
		(ClassLoader theClassLoader)
		throws ClassNotFoundException
		{
		int n = myInterfaceNames.length;
		Class[] result = new Class [n];
		for (int i = 0; i < n; ++ i)
			{
			result[i] =
				Class.forName (myInterfaceNames[i], true, theClassLoader);
			}
		return result;
		}

// Exported operations inherited and overridden from class Object.

	/**
	 * Determine if this target interface list is equal to the given object. To
	 * be equal, the given object must be a non-null instance of class
	 * TargetInterfaceList containing the same fully-qualified target interface
	 * names as this target interface list.
	 *
	 * @param  obj  Object to test.
	 *
	 * @return  True if this target interface list is equal to <TT>obj</TT>,
	 *          false otherwise.
	 */
	public boolean equals
		(Object obj)
		{
		if (obj != null && obj instanceof TargetInterfaceList)
			{
			TargetInterfaceList that = (TargetInterfaceList) obj;
			int n = this.size();
			if (n == that.size())
				{
				for (int i = 0; i < n; ++ i)
					{
					if (! this.myInterfaceNames[i].equals
								(that.myInterfaceNames[i]))
						{
						return false;
						}
					}
				return true;
				}
			else
				{
				return false;
				}
			}
		else
			{
			return false;
			}
		}

	/**
	 * Returns a hash code for this target interface list.
	 */
	public int hashCode()
		{
		int n = size();
		int result = 0;
		for (int i = 0; i < n; ++ i)
			{
			result += myInterfaceNames[i].hashCode();
			}
		return result;
		}

	/**
	 * Returns a string version of this target interface list.
	 */
	public String toString()
		{
		StringBuffer buf = new StringBuffer();
		int n = size();
		buf.append ('(');
		for (int i = 0; i < n; ++ i)
			{
			if (i > 0)
				{
				buf.append (',');
				}
			buf.append (myInterfaceNames[i]);
			}
		buf.append (')');
		return buf.toString();
		}

	}
