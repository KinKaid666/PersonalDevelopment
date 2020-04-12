//******************************************************************************
//
// File:    HandleSynthesizer.java
// Package: edu.rit.m2mi
// Unit:    Class edu.rit.m2mi.HandleSynthesizer
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

import edu.rit.classfile.ArrayReference;
import edu.rit.classfile.ClassReference;
import edu.rit.classfile.ClassfileException;
import edu.rit.classfile.ConstructorReference;
import edu.rit.classfile.DirectClassLoader;
import edu.rit.classfile.Location;
import edu.rit.classfile.MethodReference;
import edu.rit.classfile.NamedClassReference;
import edu.rit.classfile.NamedFieldReference;
import edu.rit.classfile.Op;
import edu.rit.classfile.PrimitiveReference;
import edu.rit.classfile.Reflection;
import edu.rit.classfile.SynthesizedClassDescription;
import edu.rit.classfile.SynthesizedConstructorDescription;
import edu.rit.classfile.SynthesizedMethodDescription;
import edu.rit.classfile.TypeReference;

//*DBG*/import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import java.lang.reflect.Method;

import java.util.Iterator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

/**
 * Class HandleSynthesizer provides static methods for synthesizing M2MI handle
 * classes.
 * <P>
 * Consider this example of a target interface. (Note that all M2MI-callable
 * methods must return void and must throw no checked exceptions.)
 * <PRE>
 *     package com.foo;
 *     public interface Bar
 *         {
 *         public void doSomething
 *             (int x,
 *              String y);
 *
 *         public void doNothing();
 *         }</PRE>
 * <P>
 * If it were to be compiled from Java source code instead of being synthesized
 * directly, the handle class for target interface com.foo.Bar would look like
 * this (the class name <TT>"Handle$1"</TT> was chosen arbitrarily):
 * <PRE>
 *     import edu.rit.m2mi.BaseHandle;
 *
 *     import java.io.ByteArrayOutputStream;
 *     import java.io.IOException;
 *     import java.io.ObjectOutputStream;
 *
 *     public abstract class Handle$1
 *         extends BaseHandle
 *         implements com.foo.Bar
 *         {
 *         private static final String __s1 = "com.foo.Bar";
 *         private static final String __s2 = "doSomething";
 *         private static final String __s3 = "(ILjava/lang/String;)V";
 *         private static final String __s4 = "doNothing";
 *         private static final String __s5 = "()V";
 *
 *         protected Handle$1()
 *             {
 *             super();
 *             }
 *
 *         public void doSomething
 *             (int x,
 *              String y)
 *             {
 *             try
 *                 {
 *                 ByteArrayOutputStream baos = new ByteArrayOutputStream();
 *                 ObjectOutputStream oos = new ObjectOutputStream (baos);
 *                 oos.writeInt (x);
 *                 oos.writeObject (y);
 *                 oos.close();
 *                 invoke
 *                     (__s1,                // theInterfaceName
 *                      __s2,                // theMethodName
 *                      __s3,                // theMethodDescriptor
 *                      baos.toByteArray()); // theArguments
 *                 }
 *             catch (IOException exc)
 *                 {
 *                 throw new InvocationException (exc);
 *                 }
 *             }
 *
 *         public void doNothing()
 *             {
 *             invoke
 *                 (__s1,  // theInterfaceName
 *                  __s4,  // theMethodName
 *                  __s5,  // theMethodDescriptor
 *                  null); // theArguments
 *             }
 *         }</PRE>
 * <P>
 * If it were to be compiled from Java source code instead of being synthesized
 * directly, the omnihandle class for target interface com.foo.Bar would look
 * like this (the class name <TT>"Omnihandle$1"</TT> was chosen arbitrarily):
 * <PRE>
 *     import edu.rit.m2mi.BaseHandle;
 *     import edu.rit.m2mi.HandleTransport;
 *     import edu.rit.m2mi.Omnihandle;
 *
 *     public class Omnihandle$1
 *         extends Handle$1
 *         implements Omnihandle
 *         {
 *         public Omnihandle$1()
 *             {
 *             super();
 *             }
 *
 *         public boolean invokes
 *             (Object theObject)
 *             {
 *             return isExportedInterface (theObject);
 *             }
 *
 *         public boolean equals
 *             (Object theObject)
 *             {
 *             return
 *                 theObject instanceof Omnihandle &amp;&amp;
 *                 equalsTargetInterfaceList (theObject);
 *             }
 *
 *         public int hashCode()
 *             {
 *             return myTargetInterfaceList.hashCode();
 *             }
 *
 *         private Object writeReplace()
 *             {
 *             return writeReplaceOmnihandle();
 *             }
 *
 *         protected void invoke
 *             (String theInterfaceName,
 *              String theMethodName,
 *              String theMethodDescriptor,
 *              byte[] theArguments)
 *             {
 *             invokeOmnihandle
 *                 (theInterfaceName,
 *                  theMethodName,
 *                  theMethodDescriptor,
 *                  theArguments);
 *             }
 *         }</PRE>
 * <P>
 * If it were to be compiled from Java source code instead of being synthesized
 * directly, the unihandle class for target interface com.foo.Bar would look
 * like this (the class name <TT>"Unihandle$1"</TT> was chosen arbitrarily):
 * <PRE>
 *     import edu.rit.m2mi.BaseHandle;
 *     import edu.rit.m2mi.HandleTransport;
 *     import edu.rit.m2mi.Unihandle;
 *
 *     public class Unihandle$1
 *         extends Handle$1
 *         implements Unihandle
 *         {
 *         public Unihandle$1()
 *             {
 *             super();
 *             }
 *
 *         public boolean invokes
 *             (Object theObject)
 *             {
 *             return isExportedEoid (theObject);
 *             }
 *
 *         public void attach
 *             (Object theObject)
 *             {
 *             attachUnihandle (theObject);
 *             }
 *
 *         public void detach()
 *             {
 *             detachUnihandle();
 *             }
 *
 *         public boolean equals
 *             (Object theObject)
 *             {
 *             return
 *                 theObject instanceof Unihandle &amp;&amp;
 *                 equalsEoid (theObject);
 *             }
 *
 *         public int hashCode()
 *             {
 *             return myEoid.hashCode();
 *             }
 *
 *         private Object writeReplace()
 *             {
 *             return writeReplaceUnihandle();
 *             }
 *
 *         protected void invoke
 *             (String theInterfaceName,
 *              String theMethodName,
 *              String theMethodDescriptor,
 *              byte[] theArguments)
 *             {
 *             invokeUnihandle
 *                 (theInterfaceName,
 *                  theMethodName,
 *                  theMethodDescriptor,
 *                  theArguments);
 *             }
 *         }</PRE>
 * <P>
 * If it were to be compiled from Java source code instead of being synthesized
 * directly, the multihandle class for target interface com.foo.Bar would look
 * like this (the class name <TT>"Multihandle$1"</TT> was chosen arbitrarily):
 * <PRE>
 *     import edu.rit.m2mi.BaseHandle;
 *     import edu.rit.m2mi.HandleTransport;
 *     import edu.rit.m2mi.Multihandle;
 *
 *     public class Multihandle$1
 *         extends Handle$1
 *         implements Multihandle
 *         {
 *         public Multihandle$1()
 *             {
 *             super();
 *             }
 *
 *         public boolean invokes
 *             (Object theObject)
 *             {
 *             return isExportedEoid (theObject);
 *             }
 *
 *         public void attach
 *             (Object theObject)
 *             {
 *             attachMultihandle (theObject);
 *             }
 *
 *         public void detach
 *             (Object theObject)
 *             {
 *             detachMultihandle (theObject);
 *             }
 *
 *         public boolean equals
 *             (Object theObject)
 *             {
 *             return
 *                 theObject instanceof Multihandle &amp;&amp;
 *                 equalsEoid (theObject);
 *             }
 *
 *         public int hashCode()
 *             {
 *             return myEoid.hashCode();
 *             }
 *
 *         private Object writeReplace()
 *             {
 *             return writeReplaceMultihandle();
 *             }
 *
 *         protected void invoke
 *             (String theInterfaceName,
 *              String theMethodName,
 *              String theMethodDescriptor,
 *              byte[] theArguments)
 *             {
 *             invokeMultihandle
 *                 (theInterfaceName,
 *                  theMethodName,
 *                  theMethodDescriptor,
 *                  theArguments);
 *             }
 *         }</PRE>
 * <P>
 * <B>M2MI Library Version 02-Jul-2002</B>
 *
 * @author  Alan Kaminsky
 * @version 06-Jun-2002
 */
class HandleSynthesizer
	{

// Hidden constants.

	private static final NamedClassReference EOID_CLASS =
		new NamedClassReference
			("edu.rit.m2mi.Eoid");
	private static final MethodReference EOID_EQUALS =
		new MethodReference
			(EOID_CLASS, "equals", "(Ljava/lang/Object;)Z");
	private static final MethodReference EOID_HASHCODE =
		new MethodReference
			(EOID_CLASS, "hashCode", "()I");

	private static final NamedClassReference TARGET_INTERFACE_LIST_CLASS =
		new NamedClassReference
			("edu.rit.m2mi.TargetInterfaceList");
	private static final MethodReference TARGET_INTERFACE_LIST_EQUALS =
		new MethodReference
			(TARGET_INTERFACE_LIST_CLASS, "equals", "(Ljava/lang/Object;)Z");
	private static final MethodReference TARGET_INTERFACE_LIST_HASHCODE =
		new MethodReference
			(TARGET_INTERFACE_LIST_CLASS, "hashCode", "()I");

	private static final NamedClassReference BASE_HANDLE_CLASS =
		new NamedClassReference
			("edu.rit.m2mi.BaseHandle");

	private static final NamedClassReference OMNIHANDLE_CLASS =
		new NamedClassReference ("edu.rit.m2mi.Omnihandle");

	private static final NamedClassReference UNIHANDLE_CLASS =
		new NamedClassReference ("edu.rit.m2mi.Unihandle");

	private static final NamedClassReference MULTIHANDLE_CLASS =
		new NamedClassReference ("edu.rit.m2mi.Multihandle");

	private static final NamedClassReference BYTE_ARRAY_OUTPUT_STREAM_CLASS =
		new NamedClassReference
			("java.io.ByteArrayOutputStream");
	private static final ConstructorReference BYTE_ARRAY_OUTPUT_STREAM_INIT =
		new ConstructorReference
			(BYTE_ARRAY_OUTPUT_STREAM_CLASS);
	private static final MethodReference
	BYTE_ARRAY_OUTPUT_STREAM_TO_BYTE_ARRAY =
		new MethodReference
			(BYTE_ARRAY_OUTPUT_STREAM_CLASS,
			 "toByteArray",
			 "()[B");

	private static final NamedClassReference OBJECT_OUTPUT_STREAM_CLASS =
		new NamedClassReference
			("java.io.ObjectOutputStream");
	private static final ConstructorReference OBJECT_OUTPUT_STREAM_INIT =
		new ConstructorReference
			(OBJECT_OUTPUT_STREAM_CLASS,
			 "(Ljava/io/OutputStream;)V");
	private static final MethodReference OBJECT_OUTPUT_STREAM_CLOSE =
		new MethodReference
			(OBJECT_OUTPUT_STREAM_CLASS,
			 "close");
	private static final MethodReference OBJECT_OUTPUT_STREAM_WRITE_OBJECT =
		new MethodReference
			(OBJECT_OUTPUT_STREAM_CLASS,
			 "writeObject",
			 "(Ljava/lang/Object;)V");
	private static final HashMap OBJECT_OUTPUT_STREAM_WRITE_TYPE =
		new HashMap();
	static
		{
		OBJECT_OUTPUT_STREAM_WRITE_TYPE.put
			(PrimitiveReference.BOOLEAN,
			 new MethodReference
				(OBJECT_OUTPUT_STREAM_CLASS, "writeBoolean", "(Z)V"));

		OBJECT_OUTPUT_STREAM_WRITE_TYPE.put
			(PrimitiveReference.BYTE,
			 new MethodReference
				(OBJECT_OUTPUT_STREAM_CLASS, "writeByte", "(B)V"));

		OBJECT_OUTPUT_STREAM_WRITE_TYPE.put
			(PrimitiveReference.CHAR,
			 new MethodReference
				(OBJECT_OUTPUT_STREAM_CLASS, "writeChar", "(C)V"));

		OBJECT_OUTPUT_STREAM_WRITE_TYPE.put
			(PrimitiveReference.DOUBLE,
			 new MethodReference
				(OBJECT_OUTPUT_STREAM_CLASS, "writeDouble", "(D)V"));

		OBJECT_OUTPUT_STREAM_WRITE_TYPE.put
			(PrimitiveReference.FLOAT,
			 new MethodReference
				(OBJECT_OUTPUT_STREAM_CLASS, "writeFloat", "(F)V"));

		OBJECT_OUTPUT_STREAM_WRITE_TYPE.put
			(PrimitiveReference.INT,
			 new MethodReference
				(OBJECT_OUTPUT_STREAM_CLASS, "writeInt", "(I)V"));

		OBJECT_OUTPUT_STREAM_WRITE_TYPE.put
			(PrimitiveReference.LONG,
			 new MethodReference
				(OBJECT_OUTPUT_STREAM_CLASS, "writeLong", "(J)V"));

		OBJECT_OUTPUT_STREAM_WRITE_TYPE.put
			(PrimitiveReference.SHORT,
			 new MethodReference
				(OBJECT_OUTPUT_STREAM_CLASS, "writeShort", "(S)V"));
		}

	private static final NamedClassReference IO_EXCEPTION_CLASS =
		new NamedClassReference
			("java.io.IOException");

	private static final NamedClassReference INVOCATION_EXCEPTION_CLASS =
		new NamedClassReference
			("edu.rit.m2mi.InvocationException");
	private static final ConstructorReference INVOCATION_EXCEPTION_INIT =
		new ConstructorReference
			(INVOCATION_EXCEPTION_CLASS,
			 "(Ljava/lang/Throwable;)V");

// Hidden constructors.

	private HandleSynthesizer()
		{
		}

// Exported operations.

//	/**
//	 * Unit test main program.
//	 */
//	public static void main
//		(String[] args)
//		{
//		try
//			{
//			DirectClassLoader theClassLoader =
//				new DirectClassLoader
//					(Thread.currentThread().getContextClassLoader());
//
//			Class theClass =
//				Class.forName ("edu.rit.m2mi.Handle_Bar");
//			System.out.print ("theClass = ");
//			System.out.println (theClass);
//
//			Class theHandleClass =
//				synthesizeHandleClass
//					("Handle$1",
//					 Bar.class,
//					 theClassLoader);
//			System.out.print ("theHandleClass = ");
//			System.out.println (theHandleClass);
//
//			Class theOmnihandleClass =
//				synthesizeOmnihandleClass
//					("Omnihandle$1",
//					 theHandleClass,
//					 Bar.class,
//					 theClassLoader);
//			System.out.print ("theOmnihandleClass = ");
//			System.out.println (theOmnihandleClass);
//
//			Class theUnihandleClass =
//				synthesizeUnihandleClass
//					("Unihandle$1",
//					 theHandleClass,
//					 Bar.class,
//					 theClassLoader);
//			System.out.print ("theUnihandleClass = ");
//			System.out.println (theUnihandleClass);
//
//			Class theMultihandleClass =
//				synthesizeMultihandleClass
//					("Multihandle$1",
//					 theHandleClass,
//					 Bar.class,
//					 theClassLoader);
//			System.out.print ("theMultihandleClass = ");
//			System.out.println (theMultihandleClass);
//			}
//
//		catch (Throwable exc)
//			{
//			System.err.println ("HandleSynthesizer: Uncaught exception");
//			exc.printStackTrace (System.err);
//			}
//		}

	/**
	 * Synthesize a handle class. The handle class's fully-qualified name is
	 * <TT>theClassName</TT>. The handle class is a subclass of class {@link
	 * BaseHandle </CODE>BaseHandle<CODE>}. The handle class implements all the
	 * interfaces in the given array. The handle class is loaded into the given
	 * class loader.
	 *
	 * @param  theClassName
	 *     Fully-qualified name of the handle class.
	 * @param  theInterfaces
	 *     Array of one or more target interfaces.
	 * @param  theClassLoader
	 *     Class loader into which to load the synthesized handle class.
	 *
	 * @return  Synthesized handle class.
	 *
	 * @exception  NullPointerException
	 *     (unchecked exception) Thrown if <TT>theClassName</TT> is null,
	 *     <TT>theInterfaces</TT> is null, any element of <TT>theInterfaces</TT>
	 *     is null, or <TT>theClassLoader</TT> is null.
	 * @exception  IllegalArgumentException
	 *     (unchecked exception) Thrown if <TT>theInterfaces</TT> is zero
	 *     length.
	 * @exception  InvalidMethodException
	 *     (unchecked exception) Thrown if any element of <TT>theInterfaces</TT>
	 *     is a class rather than an interface, if any method in any target
	 *     interface returns a value, or if any method in any target interface
	 *     throws any checked exceptions.
	 * @exception  SynthesisException
	 *     Thrown if there was a problem synthesizing the class.
	 */
	public static Class synthesizeHandleClass
		(String theClassName,
		 Class[] theInterfaces,
		 DirectClassLoader theClassLoader)
		throws SynthesisException
		{
		int i, n;

		// Verify preconditions.
		if
			(theClassName == null ||
			 theInterfaces == null ||
			 theClassLoader == null)
			{
			throw new NullPointerException();
			}
		n = theInterfaces.length;
		if (n == 0)
			{
			throw new IllegalArgumentException();
			}

		try
			{
			// Start synthesizing the handle class.
			SynthesizedClassDescription theClassDescription =
				new SynthesizedClassDescription
					(theClassName,
					 BASE_HANDLE_CLASS);
			theClassDescription.setAbstractClass();

			// For synthesizing string constants.
			StringConstantSynthesizer theStringConstantSynthesizer =
				new StringConstantSynthesizer (theClassDescription);

			// Get a reference to the invoke(String,String,String,byte[])
			// method.
			MethodReference invokeMethod =
				new MethodReference
					(theClassDescription,
					 "invoke",
					 "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;[B)V");

			// Synthesize the constructor.
			synthesizeProtectedConstructor
				(theClassDescription, BASE_HANDLE_CLASS);

			// Process all target interfaces.
			HashSet theMethodSet = new HashSet();
			for (i = 0; i < n; ++ i)
				{
				// The handle class implements the target interface.
				theClassDescription.addSuperinterface
					(Reflection.getClassReference (theInterfaces[i]));

				// Synthesize each method in the target interface and
				// (recursively) its superinterfaces.
				synthesizeHandleClassMethods
					(theClassDescription,
					 theMethodSet,
					 theInterfaces[i],
					 theStringConstantSynthesizer,
					 invokeMethod);
				}

			// Load synthesized class description into the given class loader.
			return loadClass (theClassDescription, theClassLoader);
			}

		catch (Exception exc)
			{
			StringBuffer msg = new StringBuffer();
			msg.append ("Cannot synthesize handle class, theClassName=");
			msg.append (theClassName);
			msg.append (", theInterfaces={");
			for (i = 0; i < n; ++ i)
				{
				if (i > 0)
					{
					msg.append (',');
					}
				msg.append (theInterfaces[i].getName());
				}
			msg.append ('}');
			/*
			** Modified by etf2954
			**
			throw new SynthesisException (msg.toString(), exc);
			*/
			throw new SynthesisException (msg.toString());
			}
		}

	/**
	 * Synthesize an omnihandle class. The omnihandle class's fully-qualified
	 * name is <TT>theClassName</TT>. The omnihandle class is a subclass of
	 * <TT>theHandleClass</TT>, which must be a handle class obtained from
	 * <TT>synthesizeHandleClass()</TT>. The omnihandle class implements the
	 * marker interface {@link Omnihandle </CODE>Omnihandle<CODE>}. The
	 * omnihandle class is loaded into the given class loader.
	 *
	 * @param  theClassName
	 *     Fully-qualified name of the omnihandle class.
	 * @param  theHandleClass
	 *     Handle class which is the superclass for the omnihandle class.
	 * @param  theClassLoader
	 *     Class loader into which to load the synthesized omnihandle class.
	 *
	 * @return  Synthesized omnihandle class.
	 *
	 * @exception  NullPointerException
	 *     (unchecked exception) Thrown if any argument is null.
	 * @exception  SynthesisException
	 *     Thrown if there was a problem synthesizing the class.
	 */
	public static Class synthesizeOmnihandleClass
		(String theClassName,
		 Class theHandleClass,
		 DirectClassLoader theClassLoader)
		throws SynthesisException
		{
		// Verify preconditions.
		if
			(theClassName == null ||
			 theHandleClass == null ||
			 theClassLoader == null)
			{
			throw new NullPointerException();
			}

		try
			{
			// Start synthesizing the omnihandle class.
			ClassReference theSuperclass =
				Reflection.getClassReference (theHandleClass);
			SynthesizedClassDescription theClassDescription =
				new SynthesizedClassDescription (theClassName, theSuperclass);
			theClassDescription.addSuperinterface (OMNIHANDLE_CLASS);

			// Synthesize the constructor.
			synthesizePublicConstructor (theClassDescription, theSuperclass);

			// Synthesize the invokes(Object) method.
			synthesizeInvokes (theClassDescription, "isExportedInterface");

			// Synthesize the equals(Object) method.
			synthesizeEquals
				(theClassDescription,
				 OMNIHANDLE_CLASS,
				 "equalsTargetInterfaceList");

			// Synthesize the hashCode() method.
			synthesizeHashCode
				(theClassDescription,
				 "myTargetInterfaceList",
				 TARGET_INTERFACE_LIST_CLASS,
				 TARGET_INTERFACE_LIST_HASHCODE);

			// Synthesize the writeReplace() method.
			synthesizeWriteReplace
				(theClassDescription, "writeReplaceOmnihandle");

			// Synthesize the invoke() method.
			synthesizeInvoke (theClassDescription, "invokeOmnihandle");

			// Load synthesized class description into the given class loader.
			return loadClass (theClassDescription, theClassLoader);
			}

		catch (Exception exc)
			{
			/*
			** Modified by etf2954
			throw new SynthesisException
				("Cannot synthesize omnihandle class, theClassName=" +
					theClassName +
					", theHandleClass=" +
					theHandleClass.getName(),
				 exc);
			*/
			throw new SynthesisException
				("Cannot synthesize omnihandle class, theClassName=" +
					theClassName +
					", theHandleClass=" +
					theHandleClass.getName());
			}
		}

	/**
	 * Synthesize a unihandle class. The unihandle class's fully-qualified name
	 * is <TT>theClassName</TT>. The unihandle class is a subclass of
	 * <TT>theHandleClass</TT>, which must be a handle class obtained from
	 * <TT>synthesizeHandleClass()</TT>. The unihandle class implements the
	 * marker interface {@link Unihandle </CODE>Unihandle<CODE>}. The unihandle
	 * class is loaded into the given class loader.
	 *
	 * @param  theClassName
	 *     Fully-qualified name of the unihandle class.
	 * @param  theHandleClass
	 *     Handle class which is the superclass for the unihandle class.
	 * @param  theClassLoader
	 *     Class loader into which to load the synthesized unihandle class.
	 *
	 * @return  Synthesized unihandle class.
	 *
	 * @exception  NullPointerException
	 *     (unchecked exception) Thrown if any argument is null.
	 * @exception  SynthesisException
	 *     Thrown if there was a problem synthesizing the class.
	 */
	public static Class synthesizeUnihandleClass
		(String theClassName,
		 Class theHandleClass,
		 DirectClassLoader theClassLoader)
		throws SynthesisException
		{
		// Verify preconditions.
		if
			(theClassName == null ||
			 theHandleClass == null ||
			 theClassLoader == null)
			{
			throw new NullPointerException();
			}

		try
			{
			// Start synthesizing the unihandle class.
			ClassReference theSuperclass =
				Reflection.getClassReference (theHandleClass);
			SynthesizedClassDescription theClassDescription =
				new SynthesizedClassDescription (theClassName, theSuperclass);
			theClassDescription.addSuperinterface (UNIHANDLE_CLASS);

			// Synthesize the constructor.
			synthesizePublicConstructor (theClassDescription, theSuperclass);

			// Synthesize the invokes(Object) method.
			synthesizeInvokes (theClassDescription, "isExportedEoid");

			// Synthesize the attach(Object) method.
			synthesizeUnihandleAttach (theClassDescription);

			// Synthesize the detach() method.
			synthesizeUnihandleDetach (theClassDescription);

			// Synthesize the equals(Object) method.
			synthesizeEquals
				(theClassDescription,
				 UNIHANDLE_CLASS,
				 "equalsEoid");

			// Synthesize the hashCode() method.
			synthesizeHashCode
				(theClassDescription,
				 "myEoid",
				 EOID_CLASS,
				 EOID_HASHCODE);

			// Synthesize the writeReplace() method.
			synthesizeWriteReplace
				(theClassDescription, "writeReplaceUnihandle");

			// Synthesize the invoke() method.
			synthesizeInvoke (theClassDescription, "invokeUnihandle");

			// Load synthesized class description into the given class loader.
			return loadClass (theClassDescription, theClassLoader);
			}

		catch (Throwable exc)
			{
			/*
			** Modified by etf2954
			**
			throw new SynthesisException
				("Cannot synthesize unihandle class, theClassName=" +
					theClassName +
					", theHandleClass=" +
					theHandleClass.getName(),
				 exc);
			*/
			throw new SynthesisException
				("Cannot synthesize unihandle class, theClassName=" +
					theClassName +
					", theHandleClass=" +
					theHandleClass.getName());
			}
		}

	/**
	 * Synthesize a multihandle class. The multihandle class's fully-qualified
	 * name is <TT>theClassName</TT>. The multihandle class is a subclass of
	 * <TT>theHandleClass</TT>, which must be a handle class obtained from
	 * <TT>synthesizeHandleClass()</TT>. The multihandle class implements the
	 * marker interface {@link Multihandle </CODE>Multihandle<CODE>}. The
	 * multihandle class is loaded into the given class loader.
	 *
	 * @param  theClassName
	 *     Fully-qualified name of the multihandle class.
	 * @param  theHandleClass
	 *     Handle class which is the superclass for the multihandle class.
	 * @param  theClassLoader
	 *     Class loader into which to load the synthesized multihandle class.
	 *
	 * @return  Synthesized multihandle class.
	 *
	 * @exception  NullPointerException
	 *     (unchecked exception) Thrown if any argument is null.
	 * @exception  SynthesisException
	 *     Thrown if there was a problem synthesizing the class.
	 */
	public static Class synthesizeMultihandleClass
		(String theClassName,
		 Class theHandleClass,
		 DirectClassLoader theClassLoader)
		throws SynthesisException
		{
		// Verify preconditions.
		if
			(theClassName == null ||
			 theHandleClass == null ||
			 theClassLoader == null)
			{
			throw new NullPointerException();
			}

		try
			{
			// Start synthesizing the multihandle class.
			ClassReference theSuperclass =
				Reflection.getClassReference (theHandleClass);
			SynthesizedClassDescription theClassDescription =
				new SynthesizedClassDescription (theClassName, theSuperclass);
			theClassDescription.addSuperinterface (MULTIHANDLE_CLASS);

			// Synthesize the constructor.
			synthesizePublicConstructor (theClassDescription, theSuperclass);

			// Synthesize the invokes(Object) method.
			synthesizeInvokes (theClassDescription, "isExportedEoid");

			// Synthesize the attach(Object) method.
			synthesizeMultihandleAttach (theClassDescription);

			// Synthesize the detach(Object) method.
			synthesizeMultihandleDetach (theClassDescription);

			// Synthesize the equals(Object) method.
			synthesizeEquals
				(theClassDescription,
				 MULTIHANDLE_CLASS,
				 "equalsEoid");

			// Synthesize the hashCode() method.
			synthesizeHashCode
				(theClassDescription,
				 "myEoid",
				 EOID_CLASS,
				 EOID_HASHCODE);

			// Synthesize the writeReplace() method.
			synthesizeWriteReplace
				(theClassDescription, "writeReplaceMultihandle");

			// Synthesize the invoke() method.
			synthesizeInvoke (theClassDescription, "invokeMultihandle");

			// Load synthesized class description into the given class loader.
			return loadClass (theClassDescription, theClassLoader);
			}

		catch (Throwable exc)
			{
			/*
			** Modified by etf2954
			**
			throw new SynthesisException
				("Cannot synthesize multihandle class, theClassName=" +
					theClassName +
					", theHandleClass=" +
					theHandleClass.getName(),
				 exc);
			*/
			throw new SynthesisException
				("Cannot synthesize multihandle class, theClassName=" +
					theClassName +
					", theHandleClass=" +
					theHandleClass.getName());
			}
		}

// Hidden operations.

	/**
	 * Synthesize the protected constructor for a handle class.
	 *
	 * @param  theClassDescription
	 *     Synthesized class description.
	 * @param  theSuperclass
	 *     Class reference for the superclass.
	 *
	 * @exception  ClassfileException
	 *     Thrown if there is a problem synthesizing the constructor.
	 */
	private static void synthesizeProtectedConstructor
		(SynthesizedClassDescription theClassDescription,
		 ClassReference theSuperclass)
		throws ClassfileException
		{
		// Start synthesizing the constructor.
		SynthesizedConstructorDescription init =
			new SynthesizedConstructorDescription (theClassDescription);
		init.setProtected();

		// Local variable layout:
		// 0  this

		// Add instructions.
		//     {
		//     super();
		init.addInstruction (Op.ALOAD (0));
		init.addInstruction (Op.INVOKESPECIAL
			(new ConstructorReference (theSuperclass)));
		//     }
		init.addInstruction (Op.RETURN);

		// Set max_stack and max_locals.
		init.setMaxStack (1);
		init.setMaxLocals (1);
		}

	/**
	 * Synthesize the public constructor for a handle subclass.
	 *
	 * @param  theClassDescription
	 *     Synthesized class description.
	 * @param  theSuperclass
	 *     Class reference for the superclass.
	 *
	 * @exception  ClassfileException
	 *     Thrown if there is a problem synthesizing the constructor.
	 */
	private static void synthesizePublicConstructor
		(SynthesizedClassDescription theClassDescription,
		 ClassReference theSuperclass)
		throws ClassfileException
		{
		// Start synthesizing the constructor.
		SynthesizedConstructorDescription init =
			new SynthesizedConstructorDescription (theClassDescription);

		// Local variable layout:
		// 0  this

		// Add instructions.
		//     {
		//     super();
		init.addInstruction (Op.ALOAD (0));
		init.addInstruction (Op.INVOKESPECIAL
			(new ConstructorReference (theSuperclass)));
		//     }
		init.addInstruction (Op.RETURN);

		// Set max_stack and max_locals.
		init.setMaxStack (1);
		init.setMaxLocals (1);
		}

	/**
	 * Synthesize each method in the given target interface and (recursively)
	 * its superinterfaces.
	 *
	 * @param  theClassDescription
	 *     Synthesized class description.
	 * @param  theMethodSet
	 *     Set of method names + descriptors (type String) that have already
	 *     been synthesized. Used to avoid implementing multiply inherited
	 *     methods more than once.
	 * @param  theInterface
	 *     Target interface.
	 * @param  theStringConstantSynthesizer
	 *     String constant synthesizer for <TT>theClassDescription</TT>.
	 * @param  invokeMethod
	 *     Method reference to the invoke(String,String,String,byte[]) method.
	 *
	 * @exception  InvalidMethodException
	 *     (unchecked exception) Thrown if <TT>theInterface</TT> is a class
	 *     rather than an interface, if any method in any target interface
	 *     returns a value, or if any method in any target interface throws any
	 *     checked exceptions.
	 * @exception  ClassfileException
	 *     Thrown if there is a problem synthesizing a method.
	 */
	private static void synthesizeHandleClassMethods
		(SynthesizedClassDescription theClassDescription,
		 HashSet theMethodSet,
		 Class theInterface,
		 StringConstantSynthesizer theStringConstantSynthesizer,
		 MethodReference invokeMethod)
		throws ClassfileException
		{
		int i, n;

		// Make sure the target interface really is an interface.
		if (! theInterface.isInterface())
			{
			throw new InvalidMethodException
				(theInterface.getName() +
				 " is not an interface");
			}

		// Process all superinterfaces of the target interface.
		Class[] theSuperinterfaces = theInterface.getInterfaces();
		n = theSuperinterfaces.length;
		for (i = 0; i < n; ++ i)
			{
			synthesizeHandleClassMethods
				(theClassDescription,
				 theMethodSet,
				 theSuperinterfaces[i],
				 theStringConstantSynthesizer,
				 invokeMethod);
			}

		// Get target interface reference.
		ClassReference theInterfaceReference =
			Reflection.getClassReference (theInterface);

		// Process all methods in the target interface.
		Method[] theMethods = theInterface.getDeclaredMethods();
		n = theMethods.length;
		for (i = 0; i < n; ++ i)
			{
			// Get method reference.
			MethodReference theMethodReference =
				Reflection.getMethodReference
					(theInterfaceReference, theMethods[i]);

			// Make sure the method returns void.
			if (theMethodReference.getReturnType() != null)
				{
				throw new InvalidMethodException
					("Interface " +
					 theInterfaceReference.getClassName() +
					 ", method " +
					 theMethodReference.getMethodName() +
					 ", descriptor " +
					 theMethodReference.getMethodDescriptor() +
					 " returns a value");
				}

			// Make sure the method throws no checked exceptions.
			if (theMethods[i].getExceptionTypes().length != 0)
				{
				throw new InvalidMethodException
					("Interface " +
					 theInterfaceReference.getClassName() +
					 ", method " +
					 theMethodReference.getMethodName() +
					 ", descriptor " +
					 theMethodReference.getMethodDescriptor() +
					 " throws checked exceptions");
				}

			// Check whether this method has been synthesized yet.
			String theDescriptor =
				theMethodReference.getMethodName() +
				theMethodReference.getMethodDescriptor();
			if (! theMethodSet.contains (theDescriptor))
				{
				// It hasn't. Synthesize it.
				theMethodSet.add (theDescriptor);
				synthesizeHandleClassMethod
					(theClassDescription,
					 theInterfaceReference.getClassName(),
					 theMethods[i],
					 theStringConstantSynthesizer,
					 invokeMethod);
				}
			}
		}

	/**
	 * Synthesize a method in the given handle class.
	 *
	 * @param  theClassDescription
	 *     Synthesized class description.
	 * @param  theInterfaceName
	 *     Fully-qualified name of the target interface.
	 * @param  theMethod
	 *     Method to synthesize.
	 * @param  theStringConstantSynthesizer
	 *     String constant synthesizer for <TT>theClassDescription</TT>.
	 * @param  invokeMethod
	 *     Method reference to the invoke(String,String,String,byte[]) method.
	 *
	 * @exception  ClassfileException
	 *     Thrown if there is a problem synthesizing a method.
	 */
	private static void synthesizeHandleClassMethod
		(SynthesizedClassDescription theClassDescription,
		 String theInterfaceName,
		 Method theMethod,
		 StringConstantSynthesizer theStringConstantSynthesizer,
		 MethodReference invokeMethod)
		throws ClassfileException
		{
		// Start synthesizing the method.
		SynthesizedMethodDescription meth =
			Reflection.synthesizeMethod (theClassDescription, theMethod);

		// Check whether the target method has any arguments.
		List args = meth.getArgumentTypes();
		Iterator iter = args.iterator();

		if (iter.hasNext())
			{
			// The target method has arguments.

			// Local variable layout:
			// 0    this
			// 1    1st method argument
			// 2    2nd method argument
			// ...
			// n    nth method argument
			// n+1  baos, exc
			// n+2  oos
			int baos = 1 /*this*/ + meth.getArgumentWordCount(); /*arguments*/
			int exc = baos;
			int oos = baos + 1;

			// Add instructions.
			Location L1 = new Location();
			Location L2 = new Location();
			Location L3 = new Location();
			Location L4 = new Location();
			//     {
			//     try
			//         {
			meth.addInstruction (L1);
			//         ByteArrayOutputStream baos = new ByteArrayOutputStream();
			meth.addInstruction (Op.NEW (BYTE_ARRAY_OUTPUT_STREAM_CLASS));
			meth.addInstruction (Op.DUP);
			meth.addInstruction (Op.INVOKESPECIAL
				(BYTE_ARRAY_OUTPUT_STREAM_INIT));
			meth.addInstruction (Op.ASTORE (baos));
			//         ObjectOutputStream oos = new ObjectOutputStream (baos);
			meth.addInstruction (Op.NEW (OBJECT_OUTPUT_STREAM_CLASS));
			meth.addInstruction (Op.DUP);
			meth.addInstruction (Op.ALOAD (baos));
			meth.addInstruction (Op.INVOKESPECIAL (OBJECT_OUTPUT_STREAM_INIT));
			meth.addInstruction (Op.ASTORE (oos));
			//         For each method argument:
			//             oos.write<Type> (<argument>);
			int lvindex = 1;
			while (iter.hasNext())
				{
				TypeReference argtype = (TypeReference) iter.next();
				MethodReference writeType = (MethodReference)
					OBJECT_OUTPUT_STREAM_WRITE_TYPE.get (argtype);
				if (writeType == null)
					{
					writeType = OBJECT_OUTPUT_STREAM_WRITE_OBJECT;
					}
				meth.addInstruction (Op.ALOAD (oos));
				meth.addInstruction (Op.TLOAD (lvindex, argtype));
				meth.addInstruction (Op.INVOKEVIRTUAL (writeType));
				lvindex += argtype.getWordCount();
				}
			//         oos.close();
			meth.addInstruction (Op.ALOAD (oos));
			meth.addInstruction (Op.INVOKEVIRTUAL (OBJECT_OUTPUT_STREAM_CLOSE));
			//         invoke
			meth.addInstruction (Op.ALOAD (0));
			//             (__s1,                // theInterfaceName
			meth.addInstruction (Op.GETSTATIC
				(theStringConstantSynthesizer.getStringConstant
					(theInterfaceName)));
			//              __s2,                // theMethodName
			meth.addInstruction (Op.GETSTATIC
				(theStringConstantSynthesizer.getStringConstant
					(meth.getMethodName())));
			//              __s3,                // theMethodDescriptor
			meth.addInstruction (Op.GETSTATIC
				(theStringConstantSynthesizer.getStringConstant
					(meth.getMethodDescriptor())));
			//              baos.toByteArray()); // theArguments
			meth.addInstruction (Op.ALOAD (baos));
			meth.addInstruction (Op.INVOKEVIRTUAL
				(BYTE_ARRAY_OUTPUT_STREAM_TO_BYTE_ARRAY));
			meth.addInstruction (Op.INVOKEVIRTUAL (invokeMethod));
			//         }
			meth.addInstruction (L2);
			meth.addInstruction (Op.GOTO (L4));
			//     catch (java.io.IOException exc)
			//         {
			meth.addInstruction (L3);
			meth.addInstruction (Op.ASTORE (exc));
			//         throw new InvocationException (exc);
			meth.addInstruction (Op.NEW (INVOCATION_EXCEPTION_CLASS));
			meth.addInstruction (Op.DUP);
			meth.addInstruction (Op.ALOAD (exc));
			meth.addInstruction (Op.INVOKESPECIAL (INVOCATION_EXCEPTION_INIT));
			meth.addInstruction (Op.ATHROW);
			//         }
			//     }
			meth.addInstruction (L4);
			meth.addInstruction (Op.RETURN);

			// Add exception handlers.
			meth.addExceptionHandler (L1, L2, L3, IO_EXCEPTION_CLASS);

			// Set max_stack and max_locals.
			meth.setMaxStack (5);
			meth.setMaxLocals (oos + 1);
			}

		else
			{
			// The target method has no arguments.

			// Local variable layout:
			// 0  this

			// Add instructions.
			//     {
			//     invoke
			meth.addInstruction (Op.ALOAD (0));
			//         (__s1,  // theInterfaceName
			meth.addInstruction (Op.GETSTATIC
				(theStringConstantSynthesizer.getStringConstant
					(theInterfaceName)));
			//          __s4,  // theMethodName
			meth.addInstruction (Op.GETSTATIC
				(theStringConstantSynthesizer.getStringConstant
					(meth.getMethodName())));
			//          __s5,  // theMethodDescriptor
			meth.addInstruction (Op.GETSTATIC
				(theStringConstantSynthesizer.getStringConstant
					(meth.getMethodDescriptor())));
			//          null); // theArguments
			meth.addInstruction (Op.ACONST_NULL);
			meth.addInstruction (Op.INVOKEVIRTUAL (invokeMethod));
			//     }
			meth.addInstruction (Op.RETURN);

			// Set max_stack and max_locals.
			meth.setMaxStack (5);
			meth.setMaxLocals (1);
			}
		}

	/**
	 * Synthesize the <TT>invokes()</TT> method for a handle subclass.
	 *
	 * @param  theClassDescription
	 *     Synthesized class description.
	 * @param  theDelegateMethodName
	 *     Name of the delegate method to call.
	 *
	 * @exception  ClassfileException
	 *     Thrown if there is a problem synthesizing the method.
	 */
	private static void synthesizeInvokes
		(SynthesizedClassDescription theClassDescription,
		 String theDelegateMethodName)
		throws ClassfileException
		{
		// Start synthesizing the method.
		// public boolean invokes
		//     (Object obj)
		SynthesizedMethodDescription meth =
			new SynthesizedMethodDescription
				(theClassDescription,
				 "invokes",
				 "(Ljava/lang/Object;)Z");

		// Local variable layout:
		// 0  this
		// 1  obj

		// Add instructions.
		//     {
		//     return isExportedInterface (obj);
		meth.addInstruction (Op.ALOAD (0));
		meth.addInstruction (Op.ALOAD (1));
		meth.addInstruction (Op.INVOKEVIRTUAL
			(new MethodReference
				(theClassDescription,
				 theDelegateMethodName,
				 "(Ljava/lang/Object;)Z")));
		//     }
		meth.addInstruction (Op.IRETURN);

		// Set max_stack and max_locals.
		meth.setMaxStack (2);
		meth.setMaxLocals (2);
		}

	/**
	 * Synthesize the <TT>attach(Object)</TT> method for a unihandle subclass.
	 *
	 * @param  theClassDescription
	 *     Synthesized class description.
	 *
	 * @exception  ClassfileException
	 *     Thrown if there is a problem synthesizing the method.
	 */
	private static void synthesizeUnihandleAttach
		(SynthesizedClassDescription theClassDescription)
		throws ClassfileException
		{
		// Start synthesizing the method.
		// public void attach
		//     (Object theObject)
		SynthesizedMethodDescription meth =
			new SynthesizedMethodDescription
				(theClassDescription,
				 "attach",
				 "(Ljava/lang/Object;)V");

		// Local variable layout:
		// 0  this
		// 1  theObject

		// Add instructions.
		//     {
		//     attachUnihandle (theObject);
		meth.addInstruction (Op.ALOAD (0));
		meth.addInstruction (Op.ALOAD (1));
		meth.addInstruction (Op.INVOKEVIRTUAL
			(new MethodReference
				(theClassDescription,
				 "attachUnihandle",
				 "(Ljava/lang/Object;)V")));
		//     }
		meth.addInstruction (Op.RETURN);

		// Set max_stack and max_locals.
		meth.setMaxStack (2);
		meth.setMaxLocals (2);
		}

	/**
	 * Synthesize the <TT>detach()</TT> method for a unihandle subclass.
	 *
	 * @param  theClassDescription
	 *     Synthesized class description.
	 *
	 * @exception  ClassfileException
	 *     Thrown if there is a problem synthesizing the method.
	 */
	private static void synthesizeUnihandleDetach
		(SynthesizedClassDescription theClassDescription)
		throws ClassfileException
		{
		// Start synthesizing the method.
		// public void detach()
		SynthesizedMethodDescription meth =
			new SynthesizedMethodDescription
				(theClassDescription, "detach", "()V");

		// Local variable layout:
		// 0  this

		// Add instructions.
		//     {
		//     detachUnihandle();
		meth.addInstruction (Op.ALOAD (0));
		meth.addInstruction (Op.INVOKEVIRTUAL
			(new MethodReference
				(theClassDescription, "detachUnihandle", "()V")));
		//     }
		meth.addInstruction (Op.RETURN);

		// Set max_stack and max_locals.
		meth.setMaxStack (1);
		meth.setMaxLocals (1);
		}

	/**
	 * Synthesize the <TT>attach(Object)</TT> method for a multihandle subclass.
	 *
	 * @param  theClassDescription
	 *     Synthesized class description.
	 *
	 * @exception  ClassfileException
	 *     Thrown if there is a problem synthesizing the method.
	 */
	private static void synthesizeMultihandleAttach
		(SynthesizedClassDescription theClassDescription)
		throws ClassfileException
		{
		// Start synthesizing the method.
		// public void attach
		//     (Object theObject)
		SynthesizedMethodDescription meth =
			new SynthesizedMethodDescription
				(theClassDescription,
				 "attach",
				 "(Ljava/lang/Object;)V");

		// Local variable layout:
		// 0  this
		// 1  theObject

		// Add instructions.
		//     {
		//     attachMultihandle (theObject);
		meth.addInstruction (Op.ALOAD (0));
		meth.addInstruction (Op.ALOAD (1));
		meth.addInstruction (Op.INVOKEVIRTUAL
			(new MethodReference
				(theClassDescription,
				 "attachMultihandle",
				 "(Ljava/lang/Object;)V")));
		//     }
		meth.addInstruction (Op.RETURN);

		// Set max_stack and max_locals.
		meth.setMaxStack (2);
		meth.setMaxLocals (2);
		}

	/**
	 * Synthesize the <TT>detach(Object)</TT> method for a multihandle subclass.
	 *
	 * @param  theClassDescription
	 *     Synthesized class description.
	 *
	 * @exception  ClassfileException
	 *     Thrown if there is a problem synthesizing the method.
	 */
	private static void synthesizeMultihandleDetach
		(SynthesizedClassDescription theClassDescription)
		throws ClassfileException
		{
		// Start synthesizing the method.
		// public void detach
		//     (Object theObject)
		SynthesizedMethodDescription meth =
			new SynthesizedMethodDescription
				(theClassDescription,
				 "detach",
				 "(Ljava/lang/Object;)V");

		// Local variable layout:
		// 0  this
		// 1  theObject

		// Add instructions.
		//     {
		//     detachMultihandle (theObject);
		meth.addInstruction (Op.ALOAD (0));
		meth.addInstruction (Op.ALOAD (1));
		meth.addInstruction (Op.INVOKEVIRTUAL
			(new MethodReference
				(theClassDescription,
				 "detachMultihandle",
				 "(Ljava/lang/Object;)V")));
		//     }
		meth.addInstruction (Op.RETURN);

		// Set max_stack and max_locals.
		meth.setMaxStack (2);
		meth.setMaxLocals (2);
		}

	/**
	 * Synthesize the <TT>equals()</TT> method for a handle subclass.
	 *
	 * @param  theClassDescription
	 *     Synthesized class description.
	 * @param  theHandleInterface
	 *     Handle interface (Omnihandle, Unihandle, or Multihandle).
	 * @param  theDelegateMethodName
	 *     Name of the delegate method to call.
	 *
	 * @exception  ClassfileException
	 *     Thrown if there is a problem synthesizing the method.
	 */
	private static void synthesizeEquals
		(SynthesizedClassDescription theClassDescription,
		 ClassReference theHandleInterface,
		 String theDelegateMethodName)
		throws ClassfileException
		{
		// Start synthesizing the method.
		// public boolean equals
		//     (Object obj)
		SynthesizedMethodDescription meth =
			new SynthesizedMethodDescription
				(theClassDescription,
				 "equals",
				 "(Ljava/lang/Object;)Z");

		// Local variable layout:
		// 0  this
		// 1  obj

		// Add instructions.
		Location L1 = new Location();
		//     {
		//     return
		//         obj instanceof Multihandle &&
		meth.addInstruction (Op.ALOAD (1));
		meth.addInstruction (Op.INSTANCEOF (theHandleInterface));
		meth.addInstruction (Op.IFEQ (L1));
		//         equalsEoid (obj);
		meth.addInstruction (Op.ALOAD (0));
		meth.addInstruction (Op.ALOAD (1));
		meth.addInstruction (Op.INVOKEVIRTUAL
			(new MethodReference
				(theClassDescription,
				 theDelegateMethodName,
				 "(Ljava/lang/Object;)Z")));
		meth.addInstruction (Op.IRETURN);
		//     }
		meth.addInstruction (L1);
		meth.addInstruction (Op.LDC (0));
		meth.addInstruction (Op.IRETURN);

		// Set max_stack and max_locals.
		meth.setMaxStack (2);
		meth.setMaxLocals (2);
		}

	/**
	 * Synthesize the <TT>hashCode()</TT> method for a handle subclass.
	 *
	 * @param  theClassDescription
	 *     Synthesized class description.
	 * @param  theFieldName
	 *     Field name.
	 * @param  theFieldClass
	 *     Field class.
	 * @param  theHashCodeMethod
	 *     Field class's hash code method.
	 *
	 * @exception  ClassfileException
	 *     Thrown if there is a problem synthesizing the method.
	 */
	private static void synthesizeHashCode
		(SynthesizedClassDescription theClassDescription,
		 String theFieldName,
		 ClassReference theFieldClass,
		 MethodReference theHashCodeMethod)
		throws ClassfileException
		{
		// Start synthesizing the method.
		// public int hashCode()
		SynthesizedMethodDescription meth =
			new SynthesizedMethodDescription
				(theClassDescription,
				 "hashCode",
				 "()I");

		// Local variable layout:
		// 0  this

		// Add instructions.
		//     {
		//     return myEoid.hashCode();
		meth.addInstruction (Op.ALOAD (0));
		meth.addInstruction (Op.GETFIELD
			(new NamedFieldReference
				(theClassDescription, theFieldName, theFieldClass)));
		meth.addInstruction (Op.INVOKEVIRTUAL (theHashCodeMethod));
		//     }
		meth.addInstruction (Op.IRETURN);

		// Set max_stack and max_locals.
		meth.setMaxStack (2);
		meth.setMaxLocals (1);
		}

	/**
	 * Synthesize the <TT>writeReplace()</TT> method for a handle subclass.
	 *
	 * @param  theClassDescription
	 *     Synthesized class description.
	 * @param  theDelegateMethodName
	 *     Name of the delegate method to call.
	 *
	 * @exception  ClassfileException
	 *     Thrown if there is a problem synthesizing the method.
	 */
	private static void synthesizeWriteReplace
		(SynthesizedClassDescription theClassDescription,
		 String theDelegateMethodName)
		throws ClassfileException
		{
		// Start synthesizing the method.
		// private Object writeReplace()
		SynthesizedMethodDescription meth =
			new SynthesizedMethodDescription
				(theClassDescription,
				 "writeReplace",
				 "()Ljava/lang/Object;");
		meth.setPrivate();

		// Local variable layout:
		// 0  this

		// Add instructions.
		//     {
		//     return writeReplaceOmnihandle();
		meth.addInstruction (Op.ALOAD (0));
		meth.addInstruction (Op.INVOKEVIRTUAL
			(new MethodReference
				(theClassDescription,
				 theDelegateMethodName,
				 "()Ljava/lang/Object;")));
		//     }
		meth.addInstruction (Op.ARETURN);

		// Set max_stack and max_locals.
		meth.setMaxStack (1);
		meth.setMaxLocals (1);
		}

	/**
	 * Synthesize the <TT>invoke()</TT> method for a handle subclass.
	 *
	 * @param  theClassDescription
	 *     Synthesized class description.
	 * @param  theDelegateMethodName
	 *     Name of the delegate method to call.
	 *
	 * @exception  ClassfileException
	 *     Thrown if there is a problem synthesizing the method.
	 */
	private static void synthesizeInvoke
		(SynthesizedClassDescription theClassDescription,
		 String theDelegateMethodName)
		throws ClassfileException
		{
		// Start synthesizing the method.
		// protected void invoke
		//     (String theInterfaceName,
		//      String theMethodName,
		//      String theMethodDescriptor,
		//      byte[] theArguments)
		SynthesizedMethodDescription meth =
			new SynthesizedMethodDescription
				(theClassDescription,
				 "invoke",
				 "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;[B)V");
		meth.setProtected();

		// Local variable layout:
		// 0  this
		// 1  theInterfaceName
		// 2  theMethodName
		// 3  theMethodDescriptor
		// 4  theArguments

		// Add instructions.
		//     {
		//     invokeOmnihandle
		meth.addInstruction (Op.ALOAD (0));
		//         (theInterfaceName,
		meth.addInstruction (Op.ALOAD (1));
		//          theMethodName,
		meth.addInstruction (Op.ALOAD (2));
		//          theMethodDescriptor,
		meth.addInstruction (Op.ALOAD (3));
		//          theArguments);
		meth.addInstruction (Op.ALOAD (4));
		meth.addInstruction (Op.INVOKEVIRTUAL
			(new MethodReference
				(theClassDescription,
				 theDelegateMethodName,
				 "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;[B)V")));
		//     }
		meth.addInstruction (Op.RETURN);

		// Set max_stack and max_locals.
		meth.setMaxStack (5);
		meth.setMaxLocals (5);
		}

	/**
	 * Load the given synthesized class description for a handle class into the
	 * given class loader.
	 *
	 * @param  theClassDescription
	 *     Synthesized class description.
	 * @param  theClassLoader
	 *     Class loader into which to load the synthesized class description.
	 *
	 * @return  Handle class.
	 *
	 * @exception  ClassfileException
	 *     Thrown if there was a problem emitting the class file.
	 * @exception  IOException
	 *     Thrown if an I/O error occurred.
	 * @exception  ClassNotFoundException
	 *     Thrown if there was a problem loading the class.
	 */
	private static Class loadClass
		(SynthesizedClassDescription theClassDescription,
		 DirectClassLoader theClassLoader)
		throws
			ClassfileException,
			IOException,
			ClassNotFoundException
		{
		OutputStream os =
			theClassLoader.writeClass
				(theClassDescription.getClassName());
		theClassDescription.emit (os);
		os.close();
		//*DBG*/String theSimpleName = theClassDescription.getSimpleName();
		//*DBG*/System.err.println ("Synthesizing class " + theSimpleName);
		//*DBG*/os = new FileOutputStream (theSimpleName + ".class");
		//*DBG*/theClassDescription.emit (os);
		//*DBG*/os.close();
		return
			Class.forName
				(theClassDescription.getClassName(),
				 true,
				 theClassLoader);
		}

	}
