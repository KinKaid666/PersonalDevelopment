//******************************************************************************
//
// File:    Unihandle.java
// Package: edu.rit.m2mi
// Unit:    Interface edu.rit.m2mi.Unihandle
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
 * Interface Unihandle specifies the interface implemented by every M2MI
 * unihandle object. A unihandle object also implements one or more target
 * interfaces. Calling a target interface method on a unihandle object causes
 * M2MI invocations to be performed on the one exported target object attached
 * to the unihandle.
 * <P>
 * An actual unihandle class is a synthesized subclass of the handle class
 * which implements interface Unihandle. The handle class in turn is a
 * synthesized subclass of class {@link BaseHandle </CODE>BaseHandle<CODE>}
 * which implements the handle's target interface(s).
 * <P>
 * A unihandle is always attached to some exported object. You can attach a
 * unihandle to a different object, but only in the same process that originally
 * created the unihandle. You can also detach a unihandle from its object, but
 * the unihandle then becomes unusable and you cannot later attach another
 * object to it. If you need to do that, use a multihandle instead.
 * <P>
 * <B>M2MI Library Version 02-Jul-2002</B>
 *
 * @author  Alan Kaminsky
 * @version 06-Jun-2002
 */
public interface Unihandle
	extends Handle
	{

// Exported operations.

	/**
	 * Determine if a method invocation on this unihandle will be executed by
	 * the given object. Specifically, the following must all be true:
	 * <OL TYPE=1>
	 * <LI>
	 * <TT>theObject</TT> is not null.
	 * <LI>
	 * <TT>theObject</TT> is exported.
	 * <LI>
	 * <TT>theObject</TT> is the object attached to this unihandle.
	 * </OL>
	 *
	 * @param  theObject  Object to test.
	 *
	 * @return  True if a method invocation on this unihandle will be executed
	 *          by <TT>theObject</TT>, false otherwise.
	 *
	 * @exception  IllegalStateException
	 *     (unchecked exception) Thrown if the M2MI layer is not initialized.
	 */
	public boolean invokes
		(Object theObject);

	/**
	 * Attach the given object to this unihandle. Afterwards, M2MI invocations
	 * on this unihandle will be executed by the given object instead of the
	 * formerly attached object. Also, M2MI invocations on an omnihandle for
	 * interface <I>I</I> or any superinterface thereof, where <I>I</I> is one
	 * of this unihandle's target interfaces, will be executed by the given
	 * object. The formerly attached object remains exported and can still be
	 * invoked by any omnihandles, multihandles, and other unihandles associated
	 * with it. If the given object is the one attached to this unihandle,
	 * <TT>attach()</TT> does nothing.
	 *
	 * @param  theObject  Object.
	 *
	 * @exception  IllegalStateException
	 *     (unchecked exception) Thrown if the M2MI layer is not initialized.
	 *     Thrown if this unihandle is not attached to an object exported in the
	 *     M2MI layer of the executing process.
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
	 * Detach the target object from this unihandle. Afterwards, M2MI
	 * invocations on this unihandle will no longer be executed by any object.
	 * The target object remains exported and can still be invoked by any
	 * omnihandles, multihandles, and other unihandles associated with it.
	 *
	 * @exception  IllegalStateException
	 *     (unchecked exception) Thrown if the M2MI layer is not initialized.
	 *     Thrown if this unihandle is not attached to an object exported in the
	 *     M2MI layer of the executing process.
	 */
	public void detach();

	/**
	 * Determine if this unihandle is equal to the given object. To be equal,
	 * the given object must be an instance of interface Unihandle referring to
	 * the same target object as this unihandle.
	 *
	 * @param  theObject  Object to test.
	 *
	 * @return  True if this unihandle is equal to <TT>theObject</TT>, false
	 *          otherwise.
	 */
	public boolean equals
		(Object theObject);

	/**
	 * Returns a hash code for this unihandle.
	 */
	public int hashCode();

	}
