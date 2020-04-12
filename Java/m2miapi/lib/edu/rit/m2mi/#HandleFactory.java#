//******************************************************************************
//
// File:    HandleFactory.java
// Package: edu.rit.m2mi
// Unit:    Class edu.rit.m2mi.HandleFactory
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

import edu.rit.classfile.ClassfileException;
import edu.rit.classfile.DirectClassLoader;

import java.io.IOException;

import java.util.HashMap;

/**
 * Class HandleFactory encapsulates a factory object that creates M2MI handle
 * objects. The handle factory maintains an internal cache of synthesized handle
 * classes as well as class loaders into which the handle classes are loaded.
 * The first time a particular handle class is called for, the handle factory
 * synthesizes the handle class (using class {@link HandleSynthesizer
 * </CODE>HandleSynthesizer<CODE>}) and stores the handle class in the cache.
 * Thereafter, the handle class is retrieved from the cache when it is called
 * for.
 * <P>
 * <I>Note:</I> Class HandleFactory is not multiple thread safe. Be sure only
 * one thread at a time calls methods on an instance of class HandleFactory.
 * <P>
 * <B>M2MI Library Version 02-Jul-2002</B>
 *
 * @author  Alan Kaminsky
 * @version 20-May-2002
 */
class HandleFactory
	{

// Hidden helper classes.

	// Cached information for a certain target interface.
	private static class Info
		{
		// Class loader into which the target interface's handle classes are
		// loaded.
		public DirectClassLoader myClassLoader;

		// Class name suffix.
		public String myClassNameSuffix;

		// Target interface's handle class.
		public Class myHandleClass;

		// Target interface's omnihandle class.
		public Class myOmnihandleClass;

		// Target interface's unihandle class.
		public Class myUnihandleClass;

		// Target interface's multihandle class.
		public Class myMultihandleClass;
		}

// Hidden data members.

	// Parent class loader for all the class loaders in the cache.
	private ClassLoader myParentClassLoader;

	// Handle class cache: A map from the target interface list (type
	// TargetInterfaceList) to a cached information record (type Info).
	private HashMap myCache = new HashMap();

	// Serial number for class name suffixes.
	private int mySerialNumber = 1;

// Exported constructors.

	/**
	 * Construct a new handle factory. Each synthesized handle class will be
	 * loaded into a class loader whose parent is the calling thread's context
	 * class loader.
	 */
	public HandleFactory()
		{
		myParentClassLoader = Thread.currentThread().getContextClassLoader();
		}

	/**
	 * Construct a new handle factory. Each synthesized handle class will be
	 * loaded into a class loader whose parent is the given class loader.
	 *
	 * @param  theParentClassLoader  Parent class loader to use.
	 *
	 * @exception  NullPointerException
	 *     (unchecked exception) Thrown if <TT>theParentClassLoader</TT> is
	 *     null.
	 */
	public HandleFactory
		(ClassLoader theParentClassLoader)
		{
		if (theParentClassLoader == null)
			{
			throw new NullPointerException();
			}
		myParentClassLoader = theParentClassLoader;
		}

// Exported operations.

	/**
	 * Manufacture an omnihandle for the given target interface(s). The
	 * omnihandle class is synthesized and cached if necessary.
	 *
	 * @param  theInterfaces  Array of one or more target interfaces.
	 *
	 * @return  Omnihandle.
	 *
	 * @exception  NullPointerException
	 *     (unchecked exception) Thrown if <TT>theInterfaces</TT> is null or any
	 *     element of <TT>theInterfaces</TT> is null.
	 * @exception  IllegalArgumentException
	 *     (unchecked exception) Thrown if <TT>theInterfaces</TT> is zero
	 *     length.
	 * @exception  InvalidMethodException
	 *     (unchecked exception) Thrown if any target interface is a class
	 *     rather than an interface, if any method in any target interface
	 *     returns a value, or if any method in any target interface throws any
	 *     checked exceptions.
	 * @exception  SynthesisException
	 *     Thrown if there was a problem synthesizing or instantiating the
	 *     omnihandle class.
	 */
	public Omnihandle createOmnihandle
		(Class[] theInterfaces)
		throws SynthesisException
		{
		TargetInterfaceList theTargetInterfaceList =
			new TargetInterfaceList (theInterfaces);
		try
			{
			Info theInfo = getInfo (theTargetInterfaceList);
			Class theClass = getOmnihandleClass (theInterfaces, theInfo);
			BaseHandle theHandle = (BaseHandle) theClass.newInstance();
			theHandle.setEoid (Eoid.WILDCARD);
			theHandle.setTargetInterfaceList (theTargetInterfaceList);
			return (Omnihandle) theHandle;
			}
		catch (Exception exc)
			{
			/*
			** Modified by etf2954
			throw new SynthesisException
				("Cannot instantiate omnihandle for interfaces " +
					theTargetInterfaceList,
				 exc);
			*/
			throw new SynthesisException
				("Cannot instantiate omnihandle for interfaces " +
					theTargetInterfaceList);
			}
		}

	/**
	 * Manufacture a unihandle for the given target interface(s) and exported
	 * object identifier (EOID). The unihandle class is synthesized and cached
	 * if necessary.
	 *
	 * @param  theInterfaces  Array of one or more target interfaces.
	 * @param  theEoid        EOID.
	 *
	 * @return  Unihandle.
	 *
	 * @exception  NullPointerException
	 *     (unchecked exception) Thrown if <TT>theInterfaces</TT> is null or any
	 *     element of <TT>theInterfaces</TT> is null.
	 * @exception  IllegalArgumentException
	 *     (unchecked exception) Thrown if <TT>theInterfaces</TT> is zero
	 *     length.
	 * @exception  InvalidMethodException
	 *     (unchecked exception) Thrown if any target interface is a class
	 *     rather than an interface, if any method in any target interface
	 *     returns a value, or if any method in any target interface throws any
	 *     checked exceptions.
	 * @exception  SynthesisException
	 *     Thrown if there was a problem synthesizing or instantiating the
	 *     unihandle class.
	 */
	public Unihandle createUnihandle
		(Class[] theInterfaces,
		 Eoid theEoid)
		throws SynthesisException
		{
		TargetInterfaceList theTargetInterfaceList =
			new TargetInterfaceList (theInterfaces);
		try
			{
			Info theInfo = getInfo (theTargetInterfaceList);
			Class theClass = getUnihandleClass (theInterfaces, theInfo);
			BaseHandle theHandle = (BaseHandle) theClass.newInstance();
			theHandle.setEoid (theEoid);
			theHandle.setTargetInterfaceList (theTargetInterfaceList);
			return (Unihandle) theHandle;
			}
		catch (Exception exc)
			{
			/*
			** Modified by etf2954
			**
			throw new SynthesisException
				("Cannot instantiate unihandle for interfaces " +
					theTargetInterfaceList,
				 exc);
			*/
			throw new SynthesisException
				("Cannot instantiate unihandle for interfaces " +
					theTargetInterfaceList);
			}
		}

	/**
	 * Manufacture a multihandle for the given target interface(s) and exported
	 * object identifier (EOID). The multihandle class is synthesized and cached
	 * if necessary.
	 *
	 * @param  theInterfaces  Array of one or more target interfaces.
	 * @param  theEoid        EOID.
	 *
	 * @return  Multihandle.
	 *
	 * @exception  NullPointerException
	 *     (unchecked exception) Thrown if <TT>theInterfaces</TT> is null or any
	 *     element of <TT>theInterfaces</TT> is null.
	 * @exception  IllegalArgumentException
	 *     (unchecked exception) Thrown if <TT>theInterfaces</TT> is zero
	 *     length.
	 * @exception  InvalidMethodException
	 *     (unchecked exception) Thrown if any target interface is a class
	 *     rather than an interface, if any method in any target interface
	 *     returns a value, or if any method in any target interface throws any
	 *     checked exceptions.
	 * @exception  SynthesisException
	 *     Thrown if there was a problem synthesizing or instantiating the
	 *     multihandle class.
	 */
	public Multihandle createMultihandle
		(Class[] theInterfaces,
		 Eoid theEoid)
		throws SynthesisException
		{
		TargetInterfaceList theTargetInterfaceList =
			new TargetInterfaceList (theInterfaces);
		try
			{
			Info theInfo = getInfo (theTargetInterfaceList);
			Class theClass = getMultihandleClass (theInterfaces, theInfo);
			BaseHandle theHandle = (BaseHandle) theClass.newInstance();
			theHandle.setEoid (theEoid);
			theHandle.setTargetInterfaceList (theTargetInterfaceList);
			return (Multihandle) theHandle;
			}
		catch (Exception exc)
			{
			/*
			** Modified by etf2954
			**
			throw new SynthesisException
				("Cannot instantiate multihandle for interfaces " +
					theTargetInterfaceList,
				 exc);
			*/
			throw new SynthesisException
				("Cannot instantiate multihandle for interfaces " +
					theTargetInterfaceList);
			}
		}

// Hidden operations.

	/**
	 * Get the cached information record for the given target interface list. If
	 * it does not exist, the cached information record is created and its class
	 * loader and class name suffix are initialized.
	 *
	 * @param  theTargetInterfaceList  Target interface list.
	 *
	 * @return  Cached information record.
	 */
	private Info getInfo
		(TargetInterfaceList theTargetInterfaceList)
		{
		Info theInfo = (Info) myCache.get (theTargetInterfaceList);
		if (theInfo == null)
			{
			theInfo = new Info();
			theInfo.myClassLoader = new DirectClassLoader (myParentClassLoader);
			theInfo.myClassNameSuffix = "$" + (mySerialNumber ++);
			myCache.put (theTargetInterfaceList, theInfo);
			}
		return theInfo;
		}

	/**
	 * Get the handle class for the given target interface(s). If it does not
	 * exist, the handle class is synthesized and stored in the cache.
	 *
	 * @param  theInterfaces
	 *     Array of one or more target interfaces.
	 * @param  theInfo
	 *     Cached information record.
	 *
	 * @return  Handle class.
	 *
	 * @exception  NullPointerException
	 *     (unchecked exception) Thrown if <TT>theInterfaces</TT> is null or any
	 *     element of <TT>theInterfaces</TT> is null.
	 * @exception  IllegalArgumentException
	 *     (unchecked exception) Thrown if <TT>theInterfaces</TT> is zero
	 *     length.
	 * @exception  InvalidMethodException
	 *     (unchecked exception) Thrown if any target interface is a class
	 *     rather than an interface, if any method in any target interface
	 *     returns a value, or if any method in any target interface throws any
	 *     checked exceptions.
	 * @exception  SynthesisException
	 *     Thrown if there was a problem synthesizing or instantiating the
	 *     handle class.
	 */
	private Class getHandleClass
		(Class[] theInterfaces,
		 Info theInfo)
		throws SynthesisException
		{
		if (theInfo.myHandleClass == null)
			{
			theInfo.myHandleClass =
				HandleSynthesizer.synthesizeHandleClass
					("Handle" + theInfo.myClassNameSuffix,
					 theInterfaces,
					 theInfo.myClassLoader);
			}
		return theInfo.myHandleClass;
		}

	/**
	 * Get the omnihandle class for the given target interface(s). If it does
	 * not exist, the omnihandle class is synthesized and stored in the cache.
	 *
	 * @param  theInterfaces
	 *     Array of one or more target interfaces.
	 * @param  theInfo
	 *     Cached information record.
	 *
	 * @return  Omnihandle class.
	 *
	 * @exception  NullPointerException
	 *     (unchecked exception) Thrown if <TT>theInterfaces</TT> is null or any
	 *     element of <TT>theInterfaces</TT> is null.
	 * @exception  IllegalArgumentException
	 *     (unchecked exception) Thrown if <TT>theInterfaces</TT> is zero
	 *     length.
	 * @exception  InvalidMethodException
	 *     (unchecked exception) Thrown if any target interface is a class
	 *     rather than an interface, if any method in any target interface
	 *     returns a value, or if any method in any target interface throws any
	 *     checked exceptions.
	 * @exception  SynthesisException
	 *     Thrown if there was a problem synthesizing or instantiating the
	 *     omnihandle class.
	 */
	private Class getOmnihandleClass
		(Class[] theInterfaces,
		 Info theInfo)
		throws SynthesisException
		{
		if (theInfo.myOmnihandleClass == null)
			{
			theInfo.myOmnihandleClass =
				HandleSynthesizer.synthesizeOmnihandleClass
					("Omnihandle" + theInfo.myClassNameSuffix,
					 getHandleClass (theInterfaces, theInfo),
					 theInfo.myClassLoader);
			}
		return theInfo.myOmnihandleClass;
		}

	/**
	 * Get the unihandle class for the given target interface(s). If it does not
	 * exist, the unihandle class is synthesized and stored in the cache.
	 *
	 * @param  theInterfaces
	 *     Array of one or more target interfaces.
	 * @param  theInfo
	 *     Cached information record.
	 *
	 * @return  Unihandle class.
	 *
	 * @exception  NullPointerException
	 *     (unchecked exception) Thrown if <TT>theInterfaces</TT> is null or any
	 *     element of <TT>theInterfaces</TT> is null.
	 * @exception  IllegalArgumentException
	 *     (unchecked exception) Thrown if <TT>theInterfaces</TT> is zero
	 *     length.
	 * @exception  InvalidMethodException
	 *     (unchecked exception) Thrown if any target interface is a class
	 *     rather than an interface, if any method in any target interface
	 *     returns a value, or if any method in any target interface throws any
	 *     checked exceptions.
	 * @exception  SynthesisException
	 *     Thrown if there was a problem synthesizing or instantiating the
	 *     unihandle class.
	 */
	private Class getUnihandleClass
		(Class[] theInterfaces,
		 Info theInfo)
		throws SynthesisException
		{
		if (theInfo.myUnihandleClass == null)
			{
			theInfo.myUnihandleClass =
				HandleSynthesizer.synthesizeUnihandleClass
					("Unihandle" + theInfo.myClassNameSuffix,
					 getHandleClass (theInterfaces, theInfo),
					 theInfo.myClassLoader);
			}
		return theInfo.myUnihandleClass;
		}

	/**
	 * Get the multihandle class for the given target interface(s). If it does
	 * not exist, the multihandle class is synthesized and stored in the cache.
	 *
	 * @param  theInterfaces
	 *     Array of one or more target interfaces.
	 * @param  theInfo
	 *     Cached information record.
	 *
	 * @return  Multihandle class.
	 *
	 * @exception  NullPointerException
	 *     (unchecked exception) Thrown if <TT>theInterfaces</TT> is null or any
	 *     element of <TT>theInterfaces</TT> is null.
	 * @exception  IllegalArgumentException
	 *     (unchecked exception) Thrown if <TT>theInterfaces</TT> is zero
	 *     length.
	 * @exception  InvalidMethodException
	 *     (unchecked exception) Thrown if any target interface is a class
	 *     rather than an interface, if any method in any target interface
	 *     returns a value, or if any method in any target interface throws any
	 *     checked exceptions.
	 * @exception  SynthesisException
	 *     Thrown if there was a problem synthesizing or instantiating the
	 *     multihandle class.
	 */
	private Class getMultihandleClass
		(Class[] theInterfaces,
		 Info theInfo)
		throws SynthesisException
		{
		if (theInfo.myMultihandleClass == null)
			{
			theInfo.myMultihandleClass =
				HandleSynthesizer.synthesizeMultihandleClass
					("Multihandle" + theInfo.myClassNameSuffix,
					 getHandleClass (theInterfaces, theInfo),
					 theInfo.myClassLoader);
			}
		return theInfo.myMultihandleClass;
		}

	}
