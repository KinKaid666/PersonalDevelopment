//******************************************************************************
//
// File:    Omnihandle.java
// Package: edu.rit.m2mi
// Unit:    Interface edu.rit.m2mi.Omnihandle
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

/**
 * Interface Omnihandle specifies the interface implemented by every M2MI
 * omnihandle object. An omnihandle object also implements one or more target
 * interfaces. Calling a target interface method on an omnihandle object causes
 * M2MI invocations to be performed on all objects exported as the target
 * interface.
 * <P>
 * An actual omnihandle class is a synthesized subclass of the handle class
 * which implements interface Omnihandle. The handle class in turn is a
 * synthesized subclass of class {@link BaseHandle </CODE>BaseHandle<CODE>}
 * which implements the handle's target interface(s).
 * <P>
 * <B>M2MI Library Version 02-Jul-2002</B>
 *
 * @author  Alan Kaminsky
 * @version 06-Jun-2002
 */
public interface Omnihandle
	extends Handle
	{

// Exported operations.

	/**
	 * Determine if a method invocation on this omnihandle will be executed by
	 * the given object. Specifically, the following must all be true:
	 * <OL TYPE=1>
	 * <LI>
	 * <TT>theObject</TT> is not null.
	 * <LI>
	 * <TT>theObject</TT> is exported.
	 * <LI>
	 * The set of target interfaces (including their superinterfaces) with which
	 * <TT>theObject</TT> is exported, and the set of target interfaces
	 * (including their superinterfaces) of this omnihandle, have at least one
	 * interface in common.
	 * </OL>
	 * <P>
	 * Note that even if <TT>invokes()</TT> returns true for some object,
	 * invoking a method in a particular target interface on this omnihandle
	 * will <EM>not</EM> cause that object to execute the method <EM>unless</EM>
	 * that object has in fact been exported as that target interface (or a
	 * subinterface thereof). In other words, <TT>invokes()</TT> tells you
	 * whether an object and this omnihandle have <EM>some</EM> target
	 * interfaces in common, not <EM>all</EM> target interfaces in common.
	 *
	 * @param  theObject  Object to test.
	 *
	 * @return  True if a method invocation on this omnihandle will be executed
	 *          by <TT>theObject</TT>, false otherwise.
	 */
	public boolean invokes
		(Object theObject);

	/**
	 * Determine if this omnihandle is equal to the given object. To be equal,
	 * the given object must be an instance of interface Omnihandle with the
	 * same target interface(s) as this omnihandle.
	 *
	 * @param  theObject  Object to test.
	 *
	 * @return  True if this omnihandle is equal to <TT>theObject</TT>, false
	 *          otherwise.
	 */
	public boolean equals
		(Object theObject);

	/**
	 * Returns a hash code for this omnihandle.
	 */
	public int hashCode();

	}
