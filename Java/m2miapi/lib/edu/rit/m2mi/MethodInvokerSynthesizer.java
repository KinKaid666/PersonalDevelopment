//******************************************************************************
//
// File:    MethodInvokerSynthesizer.java
// Package: edu.rit.m2mi
// Unit:    Class edu.rit.m2mi.MethodInvokerSynthesizer
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

import edu.rit.classfile.ArrayOrClassReference;
import edu.rit.classfile.ArrayReference;
import edu.rit.classfile.ClassfileException;
import edu.rit.classfile.ConstructorReference;
import edu.rit.classfile.DirectClassLoader;
import edu.rit.classfile.Location;
import edu.rit.classfile.MethodReference;
import edu.rit.classfile.NamedClassReference;
import edu.rit.classfile.NamedFieldReference;
import edu.rit.classfile.Op;
import edu.rit.classfile.PrimitiveReference;
import edu.rit.classfile.SynthesizedClassDescription;
import edu.rit.classfile.SynthesizedConstructorDescription;
import edu.rit.classfile.SynthesizedMethodDescription;
import edu.rit.classfile.TypeReference;

//*DBG*/import java.io.FileOutputStream;
import java.io.OutputStream;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * Class MethodInvokerSynthesizer provides static methods for synthesizing M2MI
 * method invoker classes. Each method invoker class is a subclass of class
 * {@link MethodInvoker </CODE>MethodInvoker<CODE>}.
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
 * directly, the method invoker class for target method <TT>doSomething()</TT>
 * in target interface com.foo.Bar would look like this (the class name
 * <TT>"MethodInvoker$1"</TT> was chosen arbitrarily):
 * <PRE>
 *     public class MethodInvoker$1
 *         extends edu.rit.m2mi.MethodInvoker
 *         {
 *         public MethodInvoker$1()
 *             {
 *             super();
 *             }
 *
 *         public void run()
 *             {
 *             try
 *                 {
 *                 ByteArrayInputStream bais =
 *                     new ByteArrayInputStream (myArguments);
 *                 ObjectInputStream ois = new ObjectInputStream (bais);
 *                 ((com.foo.Bar) myTarget).doSomething
 *                     (ois.readInt(),
 *                      (String) ois.readObject());
 *                 }
 *             catch (java.io.IOException exc)
 *                 {
 *                 throw new InvocationException (exc);
 *                 }
 *             catch (ClassNotFoundException exc)
 *                 {
 *                 throw new InvocationException (exc);
 *                 }
 *             }
 *         }</PRE>
 * <P>
 * If it were to be compiled from Java source code instead of being synthesized
 * directly, the method invoker class for target method <TT>doNothing()</TT> in
 * target interface com.foo.Bar would look like this (the class name
 * <TT>"MethodInvoker$2"</TT> was chosen arbitrarily):
 * <PRE>
 *     public class MethodInvoker$2
 *         extends edu.rit.m2mi.MethodInvoker
 *         {
 *         public MethodInvoker$2()
 *             {
 *             super();
 *             }
 *
 *         public void run()
 *             {
 *             ((com.foo.Bar) myTarget).doNothing();
 *             }
 *         }</PRE>
 * <P>
 * When a target method is invoked via M2MI from some ultimate calling object,
 * the method's return value if any is not returned to the calling object, and
 * any exception the method throws is not thrown back in the calling object.
 * Consequently, the target method must return void and must not throw any
 * checked exceptions. A method invoker class cannot be constructed for a target
 * method unless these preconditions are true.
 * <P>
 * <B>M2MI Library Version 02-Jul-2002</B>
 *
 * @author  Alan Kaminsky
 * @version 08-May-2002
 */
class MethodInvokerSynthesizer
	{

// Hidden constants.

	private static final NamedClassReference METHOD_INVOKER_CLASS =
		new NamedClassReference
			("edu.rit.m2mi.MethodInvoker");
	private static final ConstructorReference METHOD_INVOKER_INIT =
		new ConstructorReference
			(METHOD_INVOKER_CLASS);

	private static final NamedClassReference BYTE_ARRAY_INPUT_STREAM_CLASS =
		new NamedClassReference
			("java.io.ByteArrayInputStream");
	private static final ConstructorReference BYTE_ARRAY_INPUT_STREAM_INIT =
		new ConstructorReference
			(BYTE_ARRAY_INPUT_STREAM_CLASS,
			 "([B)V");

	private static final NamedClassReference OBJECT_INPUT_STREAM_CLASS =
		new NamedClassReference
			("java.io.ObjectInputStream");
	private static final ConstructorReference OBJECT_INPUT_STREAM_INIT =
		new ConstructorReference
			(OBJECT_INPUT_STREAM_CLASS,
			 "(Ljava/io/InputStream;)V");
	private static final MethodReference OBJECT_INPUT_STREAM_READ_OBJECT =
		new MethodReference
			(OBJECT_INPUT_STREAM_CLASS,
			 "readObject",
			 "()Ljava/lang/Object;");
	private static final HashMap OBJECT_INPUT_STREAM_READ_TYPE =
		new HashMap();
	static
		{
		OBJECT_INPUT_STREAM_READ_TYPE.put
			(PrimitiveReference.BOOLEAN,
			 new MethodReference
				(OBJECT_INPUT_STREAM_CLASS, "readBoolean", "()Z"));

		OBJECT_INPUT_STREAM_READ_TYPE.put
			(PrimitiveReference.BYTE,
			 new MethodReference
				(OBJECT_INPUT_STREAM_CLASS, "readByte", "()B"));

		OBJECT_INPUT_STREAM_READ_TYPE.put
			(PrimitiveReference.CHAR,
			 new MethodReference
				(OBJECT_INPUT_STREAM_CLASS, "readChar", "()C"));

		OBJECT_INPUT_STREAM_READ_TYPE.put
			(PrimitiveReference.DOUBLE,
			 new MethodReference
				(OBJECT_INPUT_STREAM_CLASS, "readDouble", "()D"));

		OBJECT_INPUT_STREAM_READ_TYPE.put
			(PrimitiveReference.FLOAT,
			 new MethodReference
				(OBJECT_INPUT_STREAM_CLASS, "readFloat", "()F"));

		OBJECT_INPUT_STREAM_READ_TYPE.put
			(PrimitiveReference.INT,
			 new MethodReference
				(OBJECT_INPUT_STREAM_CLASS, "readInt", "()I"));

		OBJECT_INPUT_STREAM_READ_TYPE.put
			(PrimitiveReference.LONG,
			 new MethodReference
				(OBJECT_INPUT_STREAM_CLASS, "readLong", "()J"));

		OBJECT_INPUT_STREAM_READ_TYPE.put
			(PrimitiveReference.SHORT,
			 new MethodReference
				(OBJECT_INPUT_STREAM_CLASS, "readShort", "()S"));
		}

	private static final NamedClassReference IO_EXCEPTION_CLASS =
		new NamedClassReference
			("java.io.IOException");

	private static final NamedClassReference CLASS_NOT_FOUND_EXCEPTION_CLASS =
		new NamedClassReference
			("java.lang.ClassNotFoundException");

	private static final NamedClassReference INVOCATION_EXCEPTION_CLASS =
		new NamedClassReference
			("edu.rit.m2mi.InvocationException");
	private static final ConstructorReference INVOCATION_EXCEPTION_INIT =
		new ConstructorReference
			(INVOCATION_EXCEPTION_CLASS,
			 "(Ljava/lang/Throwable;)V");

// Prevent construction.

	private MethodInvokerSynthesizer()
		{
		}

// Exported operations.

	/**
	 * Synthesize a method invoker class. The method invoker class's
	 * fully-qualified name is <TT>theClassName</TT>. The method invoker class
	 * is a subclass of class {@link MethodInvoker </CODE>MethodInvoker<CODE>}.
	 * The method invoker class is customized to invoke the given target
	 * interface name, target method name, and target method descriptor. The
	 * method invoker class is loaded into the given class loader.
	 *
	 * @param  theClassName
	 *     Fully-qualified name of the method invoker class.
	 * @param  theInterfaceName
	 *     Fully-qualified name of the target interface.
	 * @param  theMethodName
	 *     Name of the target method.
	 * @param  theMethodDescriptor
	 *     Descriptor for the target method, in the format specified by Section
	 *     4.3.3 of the <I>Java Virtual Machine Specification, Second
	 *     Edition.</I>
	 * @param  theClassLoader
	 *     Class loader into which to load the synthesized method invoker class.
	 *
	 * @return  Synthesized method invoker class.
	 *
	 * @exception  NullPointerException
	 *     (unchecked exception) Thrown if any argument is null.
	 * @exception  SynthesisException
	 *     Thrown if there was a problem synthesizing the class.
	 */
	public static Class synthesizeMethodInvokerClass
		(String theClassName,
		 String theInterfaceName,
		 String theMethodName,
		 String theMethodDescriptor,
		 DirectClassLoader theClassLoader)
		throws SynthesisException
		{
		// Verify preconditions.
		if
			(theClassName == null ||
			 theInterfaceName == null ||
			 theMethodName == null ||
			 theMethodDescriptor == null ||
			 theClassLoader == null)
			{
			throw new NullPointerException();
			}

		try
			{
			// Start synthesizing the method invoker class.
			SynthesizedClassDescription theClassDescription =
				new SynthesizedClassDescription
					(theClassName,
					 METHOD_INVOKER_CLASS);

			// Synthesize the constructor.
			synthesizeConstructor (theClassDescription);

			// Synthesize the run() method.
			synthesizeRunMethod
				(theClassDescription,
				 theInterfaceName,
				 theMethodName,
				 theMethodDescriptor);

			// Load synthesized class description into the given class loader.
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
		catch (Throwable exc)
			{
			/*
			** Modified by etf2954
			**
			throw new SynthesisException
				("Cannot synthesize method invoker class, theClassName=\"" +
					theClassName +
					"\", theInterfaceName=\"" +
					theInterfaceName +
					"\", theMethodName=\"" +
					theMethodName +
					"\", theMethodDescriptor=\"" +
					theMethodDescriptor +
					"\"",
				 exc);
			*/
			throw new SynthesisException
				("Cannot synthesize method invoker class, theClassName=\"" +
					theClassName +
					"\", theInterfaceName=\"" +
					theInterfaceName +
					"\", theMethodName=\"" +
					theMethodName +
					"\", theMethodDescriptor=\"" +
					theMethodDescriptor +
					"\"");
			}
		}

// Hidden operations.

	/**
	 * Synthesize the constructor for a method invoker class.
	 *
	 * @param  theClassDescription
	 *     Synthesized class description.
	 *
	 * @exception  ClassfileException
	 *     Thrown if there is a problem synthesizing the constructor.
	 */
	private static void synthesizeConstructor
		(SynthesizedClassDescription theClassDescription)
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
		init.addInstruction (Op.INVOKESPECIAL (METHOD_INVOKER_INIT));
		//     }
		init.addInstruction (Op.RETURN);

		// Set max_stack and max_locals.
		init.setMaxStack (1);
		init.setMaxLocals (1);
		}

	/**
	 * Synthesize the <TT>run()</TT> method for a method invoker class.
	 *
	 * @param  theClassDescription
	 *     Synthesized class description.
	 * @param  theInterfaceName
	 *     Fully-qualified name of the target interface.
	 * @param  theMethodName
	 *     Name of the target method.
	 * @param  theMethodDescriptor
	 *     Descriptor for the target method, in the format specified by Section
	 *     4.3.3 of the <I>Java Virtual Machine Specification, Second
	 *     Edition.</I>
	 *
	 * @exception  ClassfileException
	 *     Thrown if there is a problem synthesizing the constructor.
	 */
	private static void synthesizeRunMethod
		(SynthesizedClassDescription theClassDescription,
		 String theInterfaceName,
		 String theMethodName,
		 String theMethodDescriptor)
		throws ClassfileException
		{
		// Start synthesizing the method.
		SynthesizedMethodDescription meth =
			new SynthesizedMethodDescription (theClassDescription, "run");

		// Get references to target interface and target method.
		NamedClassReference theTargetInterface =
			new NamedClassReference (theInterfaceName);
		MethodReference theTargetMethod =
			new MethodReference
				(theTargetInterface, theMethodName, theMethodDescriptor);

		// Check whether the target method has any arguments.
		List args = theTargetMethod.getArgumentTypes();
		Iterator iter = args.iterator();

		if (iter.hasNext())
			{
			// The target method has arguments.

			// Local variable layout:
			// 0  this
			// 1  bais, exc
			// 2  ois

			// Add instructions.
			Location L1 = new Location();
			Location L2 = new Location();
			Location L3 = new Location();
			Location L4 = new Location();
			//     {
			//     try
			//         {
			meth.addInstruction (L1);
			//         ByteArrayInputStream bais =
			//             new ByteArrayInputStream (myArguments);
			meth.addInstruction (Op.NEW (BYTE_ARRAY_INPUT_STREAM_CLASS));
			meth.addInstruction (Op.DUP);
			meth.addInstruction (Op.ALOAD (0));
			meth.addInstruction (Op.GETFIELD
				(new NamedFieldReference
					(theClassDescription,
					 "myArguments",
					 new ArrayReference (PrimitiveReference.BYTE, 1))));
			meth.addInstruction (Op.INVOKESPECIAL
				(BYTE_ARRAY_INPUT_STREAM_INIT));
			meth.addInstruction (Op.ASTORE (1));
			//         ObjectInputStream ois = new ObjectInputStream (bais);
			meth.addInstruction (Op.NEW (OBJECT_INPUT_STREAM_CLASS));
			meth.addInstruction (Op.DUP);
			meth.addInstruction (Op.ALOAD (1));
			meth.addInstruction (Op.INVOKESPECIAL (OBJECT_INPUT_STREAM_INIT));
			meth.addInstruction (Op.ASTORE (2));
			//         ((com.foo.Bar) myTarget).doSomething
			meth.addInstruction (Op.ALOAD (0));
			meth.addInstruction (Op.GETFIELD
				(new NamedFieldReference
					(theClassDescription,
					 "myTarget",
					 NamedClassReference.JAVA_LANG_OBJECT)));
			meth.addInstruction (Op.CHECKCAST (theTargetInterface));
			//         For each method argument:
			//             (ois.readInt(),
			//              (String) ois.readObject());
			while (iter.hasNext())
				{
				TypeReference argtype = (TypeReference) iter.next();
				MethodReference readType = (MethodReference)
					OBJECT_INPUT_STREAM_READ_TYPE.get (argtype);
				if (readType == null)
					{
					meth.addInstruction (Op.ALOAD (2));
					meth.addInstruction (Op.INVOKEVIRTUAL
						(OBJECT_INPUT_STREAM_READ_OBJECT));
					meth.addInstruction (Op.CHECKCAST
						((ArrayOrClassReference) argtype));
					}
				else
					{
					meth.addInstruction (Op.ALOAD (2));
					meth.addInstruction (Op.INVOKEVIRTUAL (readType));
					}
				}
			meth.addInstruction (Op.INVOKEINTERFACE (theTargetMethod));
			//         }
			meth.addInstruction (L2);
			meth.addInstruction (Op.GOTO (L4));
			//     catch (java.io.IOException exc)
			//         {
			meth.addInstruction (L3);
			meth.addInstruction (Op.ASTORE (1));
			//         throw new InvocationException (exc);
			meth.addInstruction (Op.NEW (INVOCATION_EXCEPTION_CLASS));
			meth.addInstruction (Op.DUP);
			meth.addInstruction (Op.ALOAD (1));
			meth.addInstruction (Op.INVOKESPECIAL (INVOCATION_EXCEPTION_INIT));
			meth.addInstruction (Op.ATHROW);
			//         }
			//     }
			meth.addInstruction (L4);
			meth.addInstruction (Op.RETURN);

			// Add exception handlers.
			meth.addExceptionHandler
				(L1, L2, L3, IO_EXCEPTION_CLASS);
			meth.addExceptionHandler
				(L1, L2, L3, CLASS_NOT_FOUND_EXCEPTION_CLASS);

			// Set max_stack and max_locals.
			meth.setMaxStack
				(Math.max (3, 1+theTargetMethod.getArgumentWordCount()));
			meth.setMaxLocals (3);
			}

		else
			{
			// The target method has no arguments.

			// Local variable layout:
			// 0  this

			// Add instructions.
			//     {
			//     ((com.foo.Bar) myTarget).doNothing();
			meth.addInstruction (Op.ALOAD (0));
			meth.addInstruction (Op.GETFIELD
				(new NamedFieldReference
					(theClassDescription,
					 "myTarget",
					 NamedClassReference.JAVA_LANG_OBJECT)));
			meth.addInstruction (Op.CHECKCAST (theTargetInterface));
			meth.addInstruction (Op.INVOKEINTERFACE (theTargetMethod));
			//     }
			meth.addInstruction (Op.RETURN);

			// Set max_stack and max_locals.
			meth.setMaxStack (1);
			meth.setMaxLocals (1);
			}
		}

	}
