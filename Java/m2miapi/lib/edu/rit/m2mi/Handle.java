//******************************************************************************
//
// File:    Handle.java
// Package: edu.rit.m2mi
// Unit:    Interface edu.rit.m2mi.Handle
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
 * Interface Handle specifies the interface implemented by every M2MI handle
 * object. A handle object also implements either interface {@link Omnihandle
 * </CODE>Omnihandle<CODE>}, {@link Unihandle </CODE>Unihandle<CODE>}, or {@link
 * Multihandle </CODE>Multihandle<CODE>} depending on which kind of handle it
 * is. A handle object also implements one or more target interfaces. Calling a
 * target interface method on a handle object causes M2MI invocations to be
 * performed on the target object or objects associated with the handle.
 * <P>
 * An actual handle class is a synthesized subclass of class {@link BaseHandle
 * </CODE>BaseHandle<CODE>} which implements the handle's interface(s).
 * <P>
 * <B>M2MI Library Version 02-Jul-2002</B>
 *
 * @author  Alan Kaminsky
 * @version 06-Jun-2002
 */
public interface Handle
	{

// Exported operations.

	/**
	 * Returns this handle's exported object identifier (EOID).
	 *
	 * @return  EOID.
	 */
	public Eoid getEoid();

	/**
	 * Returns this handle's target interface name(s). Modifying the returned
	 * array will not affect this handle.
	 * <P>
	 * The returned array contains the names of just the target interfaces that
	 * were explicitly listed when this handle was created. This handle in fact
	 * also implements all superinterfaces, if any, of these target interfaces.
	 *
	 * @return  Array of one or more fully-qualified names of the target
	 *          interfaces.
	 */
	public String[] getInterfaceNames();

	/**
	 * Determine if a method invocation on this handle will be executed by the
	 * given object. The exact criterion depends on what kind of handle this is.
	 * See {@link Omnihandle#invokes(Object) Omnihandle.invokes()}, {@link
	 * Unihandle#invokes(Object) Unihandle.invokes()}, or {@link
	 * Multihandle#invokes(Object) Multihandle.invokes()} for further
	 * information.
	 *
	 * @param  theObject  Object to test.
	 *
	 * @return  True if a method invocation on this handle will be executed by
	 *          <TT>theObject</TT>, false otherwise.
	 */
	public boolean invokes
		(Object theObject);

	}
