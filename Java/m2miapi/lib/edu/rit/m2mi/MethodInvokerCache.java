//******************************************************************************
//
// File:    MethodInvokerCache.java
// Package: edu.rit.m2mi
// Unit:    Class edu.rit.m2mi.MethodInvokerCache
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

import edu.rit.classfile.DirectClassLoader;

import java.util.HashMap;

/**
 * Class MethodInvokerCache encapsulates a cache of method invoker classes. Each
 * method invoker class is a specialized subclass of class {@link MethodInvoker
 * </CODE>MethodInvoker<CODE>}. The cache lets you retrieve the method invoker
 * class for a given target interface, target method, and method descriptor. If
 * there is none, the cache automatically creates a new one and saves it for
 * later reference. The cache also lets you discard all cached method invoker
 * classes for a given target interface.
 * <P>
 * Class MethodInvokerCache is not multiple thread safe. Make sure only one
 * thread at a time ever calls a method on a method invoker cache.
 * <P>
 * The M2MI layer goes to the method invoker cache to get the proper method
 * invoker class to perform each incoming M2MI invocation. If all objects that
 * implement a certain target interface become unexported, the M2MI layer
 * discards all method invoker classes for that interface to save storage.
 * <P>
 * <B>M2MI Library Version 02-Jul-2002</B>
 *
 * @author  Alan Kaminsky
 * @version 09-May-2002
 */
class MethodInvokerCache
	{

// Hidden helper classes.

	// Information about one target interface.
	private static class InterfaceInfo
		{
		// Class loader for all method invoker classes for this target
		// interface.
		public DirectClassLoader myClassLoader;

		// Second level map: key is the concatenation of the method name and
		// method descriptor (type String), value is the method invoker class
		// for this target interface, target method, and method descriptor (type
		// Class).
		public HashMap myMethodMap;
		}

// Hidden data members.

	// Parent class loader for all the class loaders in the cache.
	private ClassLoader myParentClassLoader;

	// First level map: key is the target interface name (type String), value is
	// the interface information record (type InterfaceInfo).
	private HashMap myInterfaceMap = new HashMap();

	// Number of method invoker classes that have been synthesized.
	private int myClassCount = 0;

// Exported constructors.

	/**
	 * Construct a new method invoker cache. Each synthesized method invoker
	 * class will be loaded into a class loader whose parent is the calling
	 * thread's context class loader.
	 */
	public MethodInvokerCache()
		{
		myParentClassLoader = Thread.currentThread().getContextClassLoader();
		}

	/**
	 * Construct a new method invoker cache. Each synthesized method invoker
	 * class will be loaded into a class loader whose parent is the given class
	 * loader.
	 *
	 * @param  theParentClassLoader  Parent class loader to use.
	 *
	 * @exception  NullPointerException
	 *     (unchecked exception) Thrown if <TT>theParentClassLoader</TT> is
	 *     null.
	 */
	public MethodInvokerCache
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
	 * Obtain the method invoker class for the given target interface, target
	 * method, and method descriptor. The previously cached method invoker class
	 * is returned if available, otherwise a new method invoker class is
	 * synthesized, cached, and returned.
	 *
	 * @param  theInterfaceName
	 *     Fully-qualified name of the target interface.
	 * @param  theMethodName
	 *     Name of the target method.
	 * @param  theMethodDescriptor
	 *     Descriptor for the target method, in the format specified by Section
	 *     4.3.3 of the <I>Java Virtual Machine Specification, Second
	 *     Edition.</I>
	 *
	 * @return  Method invoker class.
	 *
	 * @exception  NullPointerException
	 *     (unchecked exception) Thrown if any argument is null.
	 * @exception  SynthesisException
	 *     Thrown if there was a problem synthesizing the class.
	 */
	public Class getMethodInvokerClass
		(String theInterfaceName,
		 String theMethodName,
		 String theMethodDescriptor)
		throws SynthesisException
		{
		if
			(theInterfaceName == null ||
			 theMethodName == null ||
			 theMethodDescriptor == null)
			{
			throw new NullPointerException();
			}

		InterfaceInfo theInfo = (InterfaceInfo)
			myInterfaceMap.get (theInterfaceName);
		if (theInfo == null)
			{
			theInfo = new InterfaceInfo();
			theInfo.myClassLoader = new DirectClassLoader (myParentClassLoader);
			theInfo.myMethodMap = new HashMap();
			myInterfaceMap.put (theInterfaceName, theInfo);
			}

		String key2 = theMethodName + theMethodDescriptor;
		Class mi = (Class) theInfo.myMethodMap.get (key2);
		if (mi == null)
			{
			mi = MethodInvokerSynthesizer.synthesizeMethodInvokerClass
				("MethodInvoker$" + (++myClassCount),
				 theInterfaceName,
				 theMethodName,
				 theMethodDescriptor,
				 theInfo.myClassLoader);
			theInfo.myMethodMap.put (key2, mi);
			}
		return mi;
		}

	/**
	 * Discard all cached method invoker classes for the given target interface.
	 *
	 * @param  theInterfaceName
	 *     Fully-qualified name of the target interface.
	 */
	public void discardInterface
		(String theInterfaceName)
		{
		myInterfaceMap.remove (theInterfaceName);
		}

	}
