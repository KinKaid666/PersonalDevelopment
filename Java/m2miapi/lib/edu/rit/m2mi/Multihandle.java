//******************************************************************************
//
// File:    Multihandle.java
// Package: edu.rit.m2mi
// Unit:    Interface edu.rit.m2mi.Multihandle
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
 * Interface Multihandle specifies the interface implemented by every M2MI
 * multihandle object. It provides methods to attach an object to a multihandle
 * and detach an object from a multihandle. A multihandle object also implements
 * one or more target interfaces. Calling a target interface method on a
 * multihandle object causes M2MI invocations to be performed on all the
 * exported target object(s) attached to the multihandle.
 * <P>
 * An actual multihandle class is a synthesized subclass of the handle class
 * which implements interface Multihandle. The handle class in turn is a
 * synthesized subclass of class {@link BaseHandle </CODE>BaseHandle<CODE>}
 * which implements the handle's target interface(s).
 * <P>
 * Objects can be attached to and detached from a multihandle at any time and in
 * any process, not just the process that created the multihandle.
 * <P>
 * <B>M2MI Library Version 02-Jul-2002</B>
 *
 * @author  Alan Kaminsky
 * @version 06-Jun-2002
 */
public interface Multihandle
	extends Handle
	{

// Exported operations.

	/**
	 * Determine if a method invocation on this multihandle will be executed by
	 * the given object. Specifically, the following must all be true:
	 * <OL TYPE=1>
	 * <LI>
	 * <TT>theObject</TT> is not null.
	 * <LI>
	 * <TT>theObject</TT> is exported.
	 * <LI>
	 * <TT>theObject</TT> has been attached to this multihandle.
	 * </OL>
	 *
	 * @param  theObject  Object to test.
	 *
	 * @return  True if a method invocation on this multihandle will be executed
	 *          by <TT>theObject</TT>, false otherwise.
	 *
	 * @exception  IllegalStateException
	 *     (unchecked exception) Thrown if the M2MI layer is not initialized.
	 */
	public boolean invokes
		(Object obj);

	/**
	 * Attach the given object to this multihandle. Afterwards, M2MI invocations
	 * on this multihandle will be executed by the given object (as well as any
	 * other objects attached to this multihandle). Also, M2MI invocations on an
	 * omnihandle for interface <I>I</I> or any superinterface thereof, where
	 * <I>I</I> is one of this multihandle's target interfaces, will be executed
	 * by the given object. If the given object is already attached to this
	 * multihandle, <TT>attach()</TT> does nothing.
	 *
	 * @param  theObject  Object.
	 *
	 * @exception  IllegalStateException
	 *     (unchecked exception) Thrown if the M2MI layer is not initialized.
	 * @exception  NullPointerException
	 *     (unchecked exception) Thrown if <TT>theObject</TT> is null.
	 * @exception  ClassCastException
	 *     (unchecked exception) Thrown if neither <TT>theObject</TT>'s class
	 *     nor any of its superclasses implements this multihandle's target
	 *     interface(s).
	 */
	public void attach
		(Object theObject);

	/**
	 * Detach the given object from this multihandle. Afterwards, M2MI
	 * invocations on this multihandle will no longer be executed by the given
	 * object. The given object remains exported and can still be invoked by any
	 * omnihandles, unihandles, and other multihandles associated with it. If
	 * the given object is not attached to this multihandle, <TT>detach()</TT>
	 * does nothing.
	 *
	 * @param  theObject  Object.
	 *
	 * @exception  IllegalStateException
	 *     (unchecked exception) Thrown if the M2MI layer is not initialized.
	 * @exception  NullPointerException
	 *     (unchecked exception) Thrown if <TT>theObject</TT> is null.
	 */
	public void detach
		(Object theObject);

	/**
	 * Determine if this multihandle is equal to the given object. To be equal,
	 * the given object must be an instance of interface Multihandle referring
	 * to the same set of target objects as this multihandle.
	 *
	 * @param  theObject  Object to test.
	 *
	 * @return  True if this multihandle is equal to <TT>theObject</TT>, false
	 *          otherwise.
	 */
	public boolean equals
		(Object theObject);

	/**
	 * Returns a hash code for this multihandle.
	 */
	public int hashCode();

	}
