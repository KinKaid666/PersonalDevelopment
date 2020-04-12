//******************************************************************************
//
// File:    BaseHandle.java
// Package: edu.rit.m2mi
// Unit:    Class edu.rit.m2mi.BaseHandle
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
 * Class BaseHandle is the abstract base class for all M2MI handle objects. An
 * actual handle class is a synthesized subclass of class BaseHandle which
 * implements the handle's target interface(s).
 * <P>
 * Class BaseHandle contains implementations of certain methods in interface
 * {@link Handle </CODE>Handle<CODE>} as well as the <TT>toString()</TT> method
 * from class Object. Class BaseHandle also contains a number of protected
 * methods for the use of the synthesized handle subclasses.
 * <P>
 * <B>M2MI Library Version 02-Jul-2002</B>
 *
 * @author  Alan Kaminsky
 * @version 06-Jun-2002
 */
public abstract class BaseHandle
	implements Handle, Serializable
	{

// Hidden data members.

	/**
	 * Exported object identifier (EOID).
	 */
	protected Eoid myEoid;

	/**
	 * Target interface list.
	 */
	protected TargetInterfaceList myTargetInterfaceList;

// Hidden constructors.

	/**
	 * Construct a new handle. The handle initially has a null EOID and a null
	 * target interface list. The handle's EOID and target interface list must
	 * be set before using the handle.
	 */
	protected BaseHandle()
		{
		}

// Exported operations.

	/**
	 * Returns this handle's exported object identifier (EOID).
	 *
	 * @return  EOID.
	 */
	public Eoid getEoid()
		{
		return myEoid;
		}

	/**
	 * Returns this handle's target interface name(s).
	 *
	 * @return  Array of one or more fully-qualified names of the target
	 *          interfaces.
	 */
	public String[] getInterfaceNames()
		{
		return myTargetInterfaceList.getInterfaceNames();
		}

// Exported operations inherited and overridden from class Object.

	/**
	 * Returns a string version of this handle.
	 */
	public String toString()
		{
		StringBuffer result = new StringBuffer();
		result.append (getClass().getName());
		result.append ('(');
		result.append (myTargetInterfaceList);
		result.append (',');
		result.append (myEoid);
		result.append (')');
		return result.toString();
		}

// Hidden operations.

	/**
	 * Sets this handle's exported object identifier (EOID).
	 *
	 * @param  theEoid  EOID.
	 *
	 * @exception  NullPointerException
	 *     (unchecked exception) Thrown if <TT>theEoid</TT> is null.
	 */
	void setEoid
		(Eoid theEoid)
		{
		if (theEoid == null)
			{
			throw new NullPointerException();
			}
		myEoid = theEoid;
		}

	/**
	 * Sets this handle's target interface list. The target interface list is
	 * assumed to contain precisely the target interfaces that this handle
	 * implements.
	 *
	 * @param  theTargetInterfaceList  Target interface list.
	 *
	 * @exception  NullPointerException
	 *     (unchecked exception) Thrown if <TT>theTargetInterfaceList</TT> is
	 *     null.
	 */
	void setTargetInterfaceList
		(TargetInterfaceList theTargetInterfaceList)
		{
		if (theTargetInterfaceList == null)
			{
			throw new NullPointerException();
			}
		myTargetInterfaceList = theTargetInterfaceList;
		}

	/**
	 * Attach the given object to this handle, which must be a unihandle.
	 *
	 * @param  theObject  Object.
	 *
	 * @exception  IllegalStateException
	 *     (unchecked exception) Thrown if the M2MI layer is not initialized.
	 *     Thrown if this handle is not attached to an object exported in the
	 *     M2MI layer of the executing process.
	 * @exception  NullPointerException
	 *     (unchecked exception) Thrown if <TT>theObject</TT> is null.
	 * @exception  ClassCastException
	 *     (unchecked exception) Thrown if <TT>theObject</TT> is not an instance
	 *     of every one of this handle's target interface(s).
	 */
	protected void attachUnihandle
		(Object theObject)
		{
		M2MI.attachUnihandle (theObject, (Unihandle) this);
		}

	/**
	 * Detach the target object from this handle, which must be a unihandle.
	 *
	 * @exception  IllegalStateException
	 *     (unchecked exception) Thrown if the M2MI layer is not initialized.
	 *     Thrown if this handle is not attached to an object exported in the
	 *     M2MI layer of the executing process.
	 */
	protected void detachUnihandle()
		{
		M2MI.detachUnihandle ((Unihandle) this);
		}

	/**
	 * Attach the given object to this handle, which must be a multihandle.
	 *
	 * @param  theObject  Object.
	 *
	 * @exception  IllegalStateException
	 *     (unchecked exception) Thrown if the M2MI layer is not initialized.
	 * @exception  NullPointerException
	 *     (unchecked exception) Thrown if <TT>theObject</TT> is null.
	 * @exception  ClassCastException
	 *     (unchecked exception) Thrown if <TT>theObject</TT> is not an instance
	 *     of every one of this handle's target interface(s).
	 */
	protected void attachMultihandle
		(Object theObject)
		{
		M2MI.attachMultihandle (theObject, (Multihandle) this);
		}

	/**
	 * Detach the given object from this handle, which must be a multihandle.
	 *
	 * @param  theObject  Object.
	 *
	 * @exception  IllegalStateException
	 *     (unchecked exception) Thrown if the M2MI layer is not initialized.
	 * @exception  NullPointerException
	 *     (unchecked exception) Thrown if <TT>theObject</TT> is null.
	 */
	protected void detachMultihandle
		(Object theObject)
		{
		M2MI.detachMultihandle (theObject, (Multihandle) this);
		}

	/**
	 * Returns true if this BaseHandle's exported object identifier (EOID) is
	 * equal to the given object's EOID.
	 * 
	 * @param  obj  Object to test; assumed to be a non-null instance of class
	 *              BaseHandle.
	 *
	 * @return  True if the EOIDs are equal, false otherwise.
	 */
	protected boolean equalsEoid
		(Object obj)
		{
		return myEoid.equals (((BaseHandle) obj).myEoid);
		}

	/**
	 * Returns true if this BaseHandle's target interface list is equal to the
	 * given object's target interface list.
	 * 
	 * @param  obj  Object to test; assumed to be a non-null instance of class
	 *              BaseHandle.
	 *
	 * @return  True if the target interface lists are equal, false otherwise.
	 */
	protected boolean equalsTargetInterfaceList
		(Object obj)
		{
		return myTargetInterfaceList.equals
			(((BaseHandle) obj).myTargetInterfaceList);
		}

	/**
	 * Replace this BaseHandle, which must be an omnihandle, with an omnihandle
	 * transport object during object serialization.
	 *
	 * @return  Replacement object.
	 */
	protected Object writeReplaceOmnihandle()
		{
		return new OmnihandleTransport (myTargetInterfaceList);
		}

	/**
	 * Replace this BaseHandle, which must be a unihandle, with a unihandle
	 * transport object during object serialization.
	 *
	 * @return  Replacement object.
	 */
	protected Object writeReplaceUnihandle()
		{
		return new UnihandleTransport (myEoid, myTargetInterfaceList);
		}

	/**
	 * Replace this BaseHandle, which must be a multihandle, with a multihandle
	 * transport object during object serialization.
	 *
	 * @return  Replacement object.
	 */
	protected Object writeReplaceMultihandle()
		{
		return new MultihandleTransport (myEoid, myTargetInterfaceList);
		}

	/**
	 * Perform a method invocation originating from this handle on the
	 * appropriate objects, if any, in the M2MI layer.
	 *
	 * @param  theInterfaceName
	 *     Fully-qualified name of the target interface.
	 * @param  theMethodName
	 *     Name of the target method.
	 * @param  theMethodDescriptor
	 *     Descriptor for the target method, in the format specified by Section
	 *     4.3.3 of the <I>Java Virtual Machine Specification, Second
	 *     Edition.</I>
	 * @param  theArguments
	 *     Byte array containing the serialized arguments for the target method;
	 *     or null if there are no arguments.
	 *
	 * @exception  IllegalStateException
	 *     (unchecked exception) Thrown if the M2MI layer is not initialized.
	 * @exception  NullPointerException
	 *     (unchecked exception) Thrown if <TT>theInterfaceName</TT>,
	 *     <TT>theMethodName</TT>, or <TT>theMethodDescriptor</TT> is null.
	 * @exception  InvocationException
	 *     (unchecked exception) Thrown if the invocation was not successful.
	 *     The detail message and/or chained exception contains further
	 *     information.
	 */
	protected abstract void invoke
		(String theInterfaceName,
		 String theMethodName,
		 String theMethodDescriptor,
		 byte[] theArguments);

	/**
	 * Perform a method invocation originating from this handle, which must be
	 * an omnihandle, on the appropriate objects, if any, in the M2MI layer.
	 *
	 * @param  theInterfaceName
	 *     Fully-qualified name of the target interface.
	 * @param  theMethodName
	 *     Name of the target method.
	 * @param  theMethodDescriptor
	 *     Descriptor for the target method, in the format specified by Section
	 *     4.3.3 of the <I>Java Virtual Machine Specification, Second
	 *     Edition.</I>
	 * @param  theArguments
	 *     Byte array containing the serialized arguments for the target method;
	 *     or null if there are no arguments.
	 *
	 * @exception  IllegalStateException
	 *     (unchecked exception) Thrown if the M2MI layer is not initialized.
	 * @exception  NullPointerException
	 *     (unchecked exception) Thrown if <TT>theInterfaceName</TT>,
	 *     <TT>theMethodName</TT>, or <TT>theMethodDescriptor</TT> is null.
	 * @exception  InvocationException
	 *     (unchecked exception) Thrown if the invocation was not successful.
	 *     The detail message and/or chained exception contains further
	 *     information.
	 */
	protected void invokeOmnihandle
		(String theInterfaceName,
		 String theMethodName,
		 String theMethodDescriptor,
		 byte[] theArguments)
		{
		M2MI.invokeOmnihandle
			(myEoid,
			 theInterfaceName,
			 theMethodName,
			 theMethodDescriptor,
			 theArguments);
		}

	/**
	 * Perform a method invocation originating from this handle, which must be a
	 * unihandle, on the appropriate objects, if any, in the M2MI layer.
	 *
	 * @param  theInterfaceName
	 *     Fully-qualified name of the target interface.
	 * @param  theMethodName
	 *     Name of the target method.
	 * @param  theMethodDescriptor
	 *     Descriptor for the target method, in the format specified by Section
	 *     4.3.3 of the <I>Java Virtual Machine Specification, Second
	 *     Edition.</I>
	 * @param  theArguments
	 *     Byte array containing the serialized arguments for the target method;
	 *     or null if there are no arguments.
	 *
	 * @exception  IllegalStateException
	 *     (unchecked exception) Thrown if the M2MI layer is not initialized.
	 * @exception  NullPointerException
	 *     (unchecked exception) Thrown if <TT>theInterfaceName</TT>,
	 *     <TT>theMethodName</TT>, or <TT>theMethodDescriptor</TT> is null.
	 * @exception  InvocationException
	 *     (unchecked exception) Thrown if the invocation was not successful.
	 *     The detail message and/or chained exception contains further
	 *     information.
	 */
	protected void invokeUnihandle
		(String theInterfaceName,
		 String theMethodName,
		 String theMethodDescriptor,
		 byte[] theArguments)
		{
		M2MI.invokeUnihandle
			(myEoid,
			 theInterfaceName,
			 theMethodName,
			 theMethodDescriptor,
			 theArguments);
		}

	/**
	 * Perform a method invocation originating from this handle, which must be a
	 * multihandle, on the appropriate objects, if any, in the M2MI layer.
	 *
	 * @param  theInterfaceName
	 *     Fully-qualified name of the target interface.
	 * @param  theMethodName
	 *     Name of the target method.
	 * @param  theMethodDescriptor
	 *     Descriptor for the target method, in the format specified by Section
	 *     4.3.3 of the <I>Java Virtual Machine Specification, Second
	 *     Edition.</I>
	 * @param  theArguments
	 *     Byte array containing the serialized arguments for the target method;
	 *     or null if there are no arguments.
	 *
	 * @exception  IllegalStateException
	 *     (unchecked exception) Thrown if the M2MI layer is not initialized.
	 * @exception  NullPointerException
	 *     (unchecked exception) Thrown if <TT>theInterfaceName</TT>,
	 *     <TT>theMethodName</TT>, or <TT>theMethodDescriptor</TT> is null.
	 * @exception  InvocationException
	 *     (unchecked exception) Thrown if the invocation was not successful.
	 *     The detail message and/or chained exception contains further
	 *     information.
	 */
	protected void invokeMultihandle
		(String theInterfaceName,
		 String theMethodName,
		 String theMethodDescriptor,
		 byte[] theArguments)
		{
		M2MI.invokeMultihandle
			(myEoid,
			 theInterfaceName,
			 theMethodName,
			 theMethodDescriptor,
			 theArguments);
		}

	/**
	 * Determine if the given object is exported with this handle's EOID in the
	 * M2MI layer.
	 *
	 * @param  theObject  Object to test.
	 *
	 * @return  True if <TT>theObject</TT> is exported with this handle's EOID,
	 *          false otherwise.
	 *
	 * @exception  IllegalStateException
	 *     (unchecked exception) Thrown if the M2MI layer is not initialized.
	 */
	protected boolean isExportedEoid
		(Object theObject)
		{
		return M2MI.isExported (theObject, myEoid);
		}

	/**
	 * Determine if the given object is exported with one of this handle's
	 * target interfaces or any superinterfaces thereof in the M2MI layer.
	 *
	 * @param  theObject  Object to test.
	 *
	 * @return  True if <TT>theObject</TT> is exported with one of this handle's
	 *          target interfaces or superinterfaces, false otherwise.
	 *
	 * @exception  IllegalStateException
	 *     (unchecked exception) Thrown if the M2MI layer is not initialized.
	 */
	protected boolean isExportedInterface
		(Object theObject)
		{
		// Get list of target interfaces, which are the interfaces implemented
		// by the superclass of the class of this handle.
		Class[] theInterfaces = getClass().getSuperclass().getInterfaces();
		int n = theInterfaces.length;

		// Check each target interface.
		for (int i = 0; i < n; ++ i)
			{
			if (isExportedInterface (theObject, theInterfaces[i]))
				{
				return true;
				}
			}
		return false;
		}

	/**
	 * Determine if the given object is exported with the given target interface
	 * or any superinterface thereof in the M2MI layer.
	 *
	 * @param  theObject     Object to test.
	 * @param  theInterface  Target interface.
	 *
	 * @return  True if <TT>theObject</TT> is exported with
	 *          <TT>theInterface</TT> or any superinterface thereof, false
	 *          otherwise.
	 *
	 * @exception  IllegalStateException
	 *     (unchecked exception) Thrown if the M2MI layer is not initialized.
	 */
	private boolean isExportedInterface
		(Object theObject,
		 Class theInterface)
		{
		// Check target interface.
		if (M2MI.isExported (theObject, theInterface.getName()))
			{
			return true;
			}

		// Check superinterfaces.
		Class[] theSuperinterfaces = theInterface.getInterfaces();
		int n = theSuperinterfaces.length;
		for (int i = 0; i < n; ++ i)
			{
			if (M2MI.isExported (theObject, theSuperinterfaces[i].getName()))
				{
				return true;
				}
			}
		return false;
		}

	}
