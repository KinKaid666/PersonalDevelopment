//******************************************************************************
//
// File:    StubSynthesizer.java
// Package: edu.rit.classfile.unittest
// Unit:    Class edu.rit.classfile.unittest.StubSynthesizer
//
// This Java source file is copyright (C) 2001 by the Rochester Institute of
// Technology. All rights reserved. For further information, contact the author,
// Alan Kaminsky, at ark@it.rit.edu.
//
// This Java source file is part of the RIT Classfile Library ("The Library").
// The Library is free software; you can redistribute it and/or modify it under
// the terms of the GNU General Public License as published by the Free Software
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

package edu.rit.classfile.unittest;

import edu.rit.classfile.ArrayReference;
import edu.rit.classfile.ClassfileException;
import edu.rit.classfile.ClassReference;
import edu.rit.classfile.ConstructorReference;
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

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import java.lang.reflect.Method;

/**
 * Class StubSynthesizer demonstrates how the RIT Classfile Library can be used
 * to synthesize a stub that implements a given interface. To synthesize a stub
 * for an interface, call the {@link
 * #getStub(java.lang.Class,java.lang.String,java.io.OutputStream)
 * <TT>getStub()</TT>} method, specifying which interface you want a stub for,
 * what the name of the stub class is, and where to put the stub's classfile.
 * The StubSynthesizer uses reflection to determine all the methods in the given
 * interface and synthesizes an implementation for each one. Each method's
 * implementation simply prints its arguments on the standard output. For
 * methods that return a <TT>boolean</TT> value, the stub implementation returns
 * <TT>false</TT>. For methods that return a numerical value, the stub
 * implementation returns 0. For methods that return an object, the stub
 * implementation returns <TT>null</TT>. Study the <A
 * HREF="doc-files/StubSynthesizer.html">source code</A> for class
 * StubSynthesizer to see how it does it.
 * <P>
 * A similar technique could be used to create a stub synthesizer for a remote
 * method invocation system. Each stub method, instead of printing its arguments
 * on the standard output and returning a dummy value, would marshal its
 * arguments and send them over a network connection to the far end, receive a
 * marshalled return value back, unmarshal it, and return it.
 * <P>
 * Class StubSynthesizer also includes a main program driver to exercise the
 * <TT>getStub()</TT> method.
 * <P>
 * Usage: java edu.rit.classfile.unittest.StubSynthesizer <I>interface stubclass
 * file</I>
 * <BR>
 * <I>interface</I> = Fully-qualified name of the interface for which to synthesize a stub class.
 * <BR>
 * <I>stubclass</I> = Fully-qualified name of the stub class.
 * <BR>
 * <I>file</I> = Name of the file in which to store the stub's classfile.
 * <P>
 * <B>RIT Classfile Library Version 02-Jul-2002</B>
 *
 * @author  Alan Kaminsky
 * @version 10-Oct-2001
 */
public class StubSynthesizer
	{

// Hidden global variables.

	// References to all classes, methods, and fields the stub class will use.

	// Class java.lang.Object.
	private static final NamedClassReference cObject =
		NamedClassReference.JAVA_LANG_OBJECT;

	// Constructor Object().
	private static final ConstructorReference mObjectInit =
		new ConstructorReference (cObject);

	// Class java.io.PrintStream.
	private static final NamedClassReference cPrintStream =
		new NamedClassReference ("java.io.PrintStream");

	// Method PrintStream.print(boolean).
	private static final MethodReference mPrintBoolean =
		new MethodReference (cPrintStream, "print");
		static
			{
			try
				{
				mPrintBoolean.addArgumentType (PrimitiveReference.BOOLEAN);
				}
			catch (ClassfileException exc) {}
			}

	// Method PrintStream.print(char).
	private static final MethodReference mPrintChar =
		new MethodReference (cPrintStream, "print");
		static
			{
			try
				{
				mPrintChar.addArgumentType (PrimitiveReference.CHAR);
				}
			catch (ClassfileException exc) {}
			}

	// Method PrintStream.print(double).
	private static final MethodReference mPrintDouble =
		new MethodReference (cPrintStream, "print");
		static
			{
			try
				{
				mPrintDouble.addArgumentType (PrimitiveReference.DOUBLE);
				}
			catch (ClassfileException exc) {}
			}

	// Method PrintStream.print(float).
	private static final MethodReference mPrintFloat =
		new MethodReference (cPrintStream, "print");
		static
			{
			try
				{
				mPrintDouble.addArgumentType (PrimitiveReference.FLOAT);
				}
			catch (ClassfileException exc) {}
			}

	// Method PrintStream.print(int).
	private static final MethodReference mPrintInt =
		new MethodReference (cPrintStream, "print");
		static
			{
			try
				{
				mPrintInt.addArgumentType (PrimitiveReference.INT);
				}
			catch (ClassfileException exc) {}
			}

	// Method PrintStream.print(long).
	private static final MethodReference mPrintLong =
		new MethodReference (cPrintStream, "print");
		static
			{
			try
				{
				mPrintLong.addArgumentType (PrimitiveReference.LONG);
				}
			catch (ClassfileException exc) {}
			}

	// Method PrintStream.print(Object).
	private static final MethodReference mPrintObject =
		new MethodReference (cPrintStream, "print");
		static
			{
			try
				{
				mPrintObject.addArgumentType (cObject);
				}
			catch (ClassfileException exc) {}
			}

	// Method PrintStream.print(String).
	private static final MethodReference mPrintString =
		new MethodReference (cPrintStream, "print");
		static
			{
			try
				{
				mPrintString.addArgumentType
					(NamedClassReference.JAVA_LANG_STRING);
				}
			catch (ClassfileException exc) {}
			}

	// Class java.lang.System.
	private static final NamedClassReference cSystem =
		new NamedClassReference ("java.lang.System");

	// Field System.out.
	private static final NamedFieldReference fSystemOut =
		new NamedFieldReference (cSystem, "out", cPrintStream);

// Prevent construction.

	private StubSynthesizer()
		{
		}

// Exported operations.

	/**
	 * Main program driver to exercise the <TT>getStub()</TT> method.
	 * <P>
	 * Usage: java edu.rit.classfile.unittest.StubSynthesizer <I>interface
	 * stubclass file</I>
	 * <BR>
	 * <I>interface</I> = Fully-qualified name of the interface for which to
	 * synthesize a stub class.
	 * <BR>
	 * <I>stubclass</I> = Fully-qualified name of the stub class.
	 * <BR>
	 * <I>file</I> = Name of the file in which to store the stub's classfile.
	 */
	public static void main
		(String[] args)
		{
		FileOutputStream fos = null;
		try
			{
			if (args.length != 3)
				{
				System.err.println
					("Usage: java edu.rit.classfile.unittest.StubSynthesizer <interface> <stubclass> <file>");
				System.err.println
					("<interface> = Fully-qualified name of the interface for which to synthesize a stub class.");
				System.err.println
					("<stubclass> = Fully-qualified name of the stub class.");
				System.err.println
					("<file> = Name of the file in which to store the stub's classfile.");
				return;
				}
			fos = new FileOutputStream (args[2]);
			getStub (Class.forName (args[0]), args[1], fos);
			}
		catch (Throwable exc)
			{
			System.err.println ("StubSynthesizer: Uncaught exception");
			exc.printStackTrace (System.err);
			}
		finally
			{
			if (fos != null)
				{
				try { fos.close(); } catch (IOException exc) {}
				}
			}
		}

	/**
	 * Synthesize a stub class for the given interface.
	 *
	 * @param  theInterface     Interface which the stub class implements.
	 * @param  theClassName     Fully-qualified name for the stub class.
	 * @param  theOutputStream  Output stream to which to emit the synthesized
	 *                          stub classfile.
	 *
	 * @exception  NullPointerException
	 *     (unchecked exception) Thrown if any argument is null.
	 * @exception  IllegalArgumentException
	 *     (unchecked exception) Thrown if any of the following is true:
	 *     <UL>
	 *     <LI> <TT>theInterface</TT> is a class, not an interface.
	 *     <LI> <TT>theClassName</TT> is zero length.
	 *     </UL>
	 * @exception  ClassfileException
	 *     Thrown if there was a problem synthesizing the stub classfile.
	 * @exception  IOException
	 *     Thrown if an I/O error occurred.
	 */
	public static void getStub
		(Class theInterface,
		 String theClassName,
		 OutputStream theOutputStream)
		throws ClassfileException, IOException
		{
		// 1. Verify that the given interface really is an interface.
		if (! theInterface.isInterface())
			{
			throw new IllegalArgumentException();
			}

		// 2. Create the synthesized class description for the stub class.
		// Superclass is Object.
		SynthesizedClassDescription cStub =
			new SynthesizedClassDescription (theClassName);

		// 2.a. The stub class implements the given interface.
		cStub.addSuperinterface
			(new NamedClassReference (theInterface.getName()));

		// 3. Create the synthesized constructor description for the stub
		// class's default constructor.
		SynthesizedConstructorDescription mStubInit =
			new SynthesizedConstructorDescription (cStub);

		// 3.a. The local variable layout for the constructor is:
		// Index  Name  Contents
		//   0    this  "This" pointer (set by the JVM)
		// Record the number of words needed for local variables.
		mStubInit.setMaxLocals (1);

		// 3.b. Add the constructor's bytecodes.
		//     {
		//     super();
		mStubInit.addInstruction (Op.ALOAD (0));
		mStubInit.addInstruction (Op.INVOKESPECIAL (mObjectInit));
		//     }
		mStubInit.addInstruction (Op.RETURN);

		// 3.c. Record the number of words needed for the constructor's operand
		// stack. Only one thing is ever pushed onto the stack, a reference to
		// this object, so max_stack is 1.
		mStubInit.setMaxStack (1);

		// 4. Create the synthesized method description for each method in the
		// given interface.
		Method[] theMethods = theInterface.getMethods();
		for (int i = 0; i < theMethods.length; ++ i)
			{
			getStubMethod (cStub, theMethods[i]);
			}

		// 5. Emit the stub's classfile.
		cStub.emit (theOutputStream);
		}

// Hidden operations.

	/**
	 * Synthesize an implementation for the given method.
	 *
	 * @param  cStub      Synthesized stub class description.
	 * @param  theMethod  Method to implement.
	 *
	 * @exception  ClassfileException
	 *     Thrown if there was a problem synthesizing the stub classfile.
	 */
	private static void getStubMethod
		(SynthesizedClassDescription cStub,
		 Method theMethod)
		throws ClassfileException
		{
		int i;

		// 1. Create the synthesized method description for the stub method.
		String theMethodName = theMethod.getName();
		SynthesizedMethodDescription mStub =
			new SynthesizedMethodDescription (cStub, theMethodName);

		// 2. Specify the stub method's return type.
		TypeReference theReturnType =
			getTypeReference (theMethod.getReturnType());
		mStub.setReturnType (theReturnType);

		// 3. Add the stub method's argument types in order, if any.
		Class[] theArgumentClasses = theMethod.getParameterTypes();
		TypeReference[] theArgumentTypes =
			new TypeReference [theArgumentClasses.length];
		for (i = 0; i < theArgumentTypes.length; ++ i)
			{
			theArgumentTypes[i] = getTypeReference (theArgumentClasses[i]);
			mStub.addArgumentType (theArgumentTypes[i]);
			}

		// 4. Add the stub method's thrown exceptions in order, if any.
		Class[] theExceptionTypes = theMethod.getExceptionTypes();
		for (i = 0; i < theExceptionTypes.length; ++ i)
			{
			mStub.addThrownException
				((ClassReference) getTypeReference (theExceptionTypes[i]));
			}

		// 5. The stub method's local variable layout is:
		// Index     Contents
		// 0         "This" pointer (set by the JVM)
		// 1..argwc  Arguments if any (argwc == number of argument words)
		// argwc+1   Local variable, out
		// Record the number of words needed for local variables. This will be
		// increased later if necessary.
		int argwc = mStub.getArgumentWordCount();
		int out = argwc + 1;
		mStub.setMaxLocals (argwc + 2);

		// 6. Record the number of words needed for the operand stack. This will
		// be increased later if necessary.
		mStub.setMaxStack (2);

		// NOTE: It's important to get max_stack EXACTLY right. If max_stack is
		// too small, you'll get a verification error when the synthesized class
		// is loaded. On the other hand, bitter experience has shown that if
		// max_stack is larger than necessary, the synthesized class will verify
		// successfully but the method may not work right when it's called.

		// 7. Add the stub method's bytecode instructions.

		// 7.a. out = System.out;
		mStub.addInstruction (Op.GETSTATIC (fSystemOut));
		mStub.addInstruction (Op.ASTORE (out));

		// 7.b. out.print (<method name>);
		mStub.addInstruction (Op.ALOAD (out));
		mStub.addInstruction (Op.LDC (theMethodName));
		mStub.addInstruction (Op.INVOKEVIRTUAL (mPrintString));

		// 7.c. out.print ('(');
		mStub.addInstruction (Op.ALOAD (out));
		mStub.addInstruction (Op.LDC ('('));
		mStub.addInstruction (Op.INVOKEVIRTUAL (mPrintChar));

		// 7.d. Print each argument.
		int argIndex = 1;
		for (i = 0; i < theArgumentTypes.length; ++ i)
			{
			if (i > 0)
				{
				// out.print (',');
				mStub.addInstruction (Op.ALOAD (out));
				mStub.addInstruction (Op.LDC (','));
				mStub.addInstruction (Op.INVOKEVIRTUAL (mPrintChar));
				}
			// out.print (<argument i>);
			printValue (mStub, theArgumentTypes[i], argIndex, out, out+1);
			// Advance argument index.
			argIndex += theArgumentTypes[i].getWordCount();
			}

		// 7.e. out.print (')');
		mStub.addInstruction (Op.ALOAD (out));
		mStub.addInstruction (Op.LDC (')'));
		mStub.addInstruction (Op.INVOKEVIRTUAL (mPrintChar));

		// 7.f. Return a dummy value of the proper type.
		returnDummyValue (mStub, theReturnType);
		}

	/**
	 * Returns the type reference for the given class. If the given class
	 * denotes <TT>void</TT>, returns null.
	 */
	private static TypeReference getTypeReference
		(Class theClass)
		{
		String theClassName = theClass.getName();
		if (theClass.isPrimitive())
			{
			return PrimitiveReference.forClassName (theClassName);
			}
		else if (theClass.isArray())
			{
			return ArrayReference.forClassName (theClassName);
			}
		else
			{
			return new NamedClassReference (theClassName);
			}
		}

	/**
	 * Adds instructions to print a value. Also increases the stub method's
	 * <TT>max_stack</TT> if necessary to hold the words pushed on the operand
	 * stack. Also increases the stub method's <TT>max_locals</TT> if necessary
	 * to hold the local variables used.
	 *
	 * @param  mStub      Stub method's description.
	 * @param  valType    Value's type.
	 * @param  valIndex   Local variable index where the value is stored.
	 * @param  out        Local variable index of <TT>out</TT>.
	 * @param  freeIndex  Index of the first unused local variable.
	 *
	 * @exception  ClassfileException
	 *     Thrown if there was a problem synthesizing the stub classfile.
	 */
	private static void printValue
		(SynthesizedMethodDescription mStub,
		 TypeReference valType,
		 int valIndex,
		 int out,
		 int freeIndex)
		throws ClassfileException
		{
		if (valType instanceof ArrayReference)
			{
			printArrayValue
				(mStub,
				 (ArrayReference) valType,
				 valIndex,
				 out,
				 freeIndex);
			}
		else
			{
			printNonArrayValue (mStub, valType, valIndex, out);
			}
		}

	/**
	 * Adds instructions to print a value which is not an array. Also increases
	 * the stub method's <TT>max_stack</TT> if necessary to hold the words
	 * pushed on the operand stack.
	 *
	 * @param  mStub     Stub method's description.
	 * @param  valType   Value's type.
	 * @param  valIndex  Local variable index where the value is stored.
	 * @param  out       Local variable index of <TT>out</TT>.
	 *
	 * @exception  ClassfileException
	 *     Thrown if there was a problem synthesizing the stub classfile.
	 */
	private static void printNonArrayValue
		(SynthesizedMethodDescription mStub,
		 TypeReference valType,
		 int valIndex,
		 int out)
		throws ClassfileException
		{
		int max_stack = 2;
		mStub.addInstruction (Op.ALOAD (out));
		if (! (valType instanceof PrimitiveReference))
			{
			mStub.addInstruction (Op.ALOAD (valIndex));
			mStub.addInstruction (Op.INVOKEVIRTUAL (mPrintObject));
			}
		else if (valType == PrimitiveReference.BOOLEAN)
			{
			mStub.addInstruction (Op.ILOAD (valIndex));
			mStub.addInstruction (Op.INVOKEVIRTUAL (mPrintBoolean));
			}
		else if (valType == PrimitiveReference.CHAR)
			{
			mStub.addInstruction (Op.ILOAD (valIndex));
			mStub.addInstruction (Op.INVOKEVIRTUAL (mPrintChar));
			}
		else if (valType == PrimitiveReference.DOUBLE)
			{
			mStub.addInstruction (Op.DLOAD (valIndex));
			mStub.addInstruction (Op.INVOKEVIRTUAL (mPrintDouble));
			max_stack = 3;
			}
		else if (valType == PrimitiveReference.FLOAT)
			{
			mStub.addInstruction (Op.FLOAD (valIndex));
			mStub.addInstruction (Op.INVOKEVIRTUAL (mPrintFloat));
			}
		else if (valType == PrimitiveReference.LONG)
			{
			mStub.addInstruction (Op.LLOAD (valIndex));
			mStub.addInstruction (Op.INVOKEVIRTUAL (mPrintLong));
			max_stack = 3;
			}
		else
			{
			mStub.addInstruction (Op.ILOAD (valIndex));
			mStub.addInstruction (Op.INVOKEVIRTUAL (mPrintInt));
			}
		mStub.increaseMaxStack (max_stack);
		}

	/**
	 * Adds instructions to print a value which is an array. Also increases the
	 * stub method's <TT>max_stack</TT> if necessary to hold the words pushed on
	 * the operand stack. Also increases the stub method's <TT>max_locals</TT>
	 * if necessary to hold the local variables used.
	 *
	 * @param  mStub       Stub method's description.
	 * @param  arrayType   Array's type.
	 * @param  arrayIndex  Local variable index where the array reference is
	 *                     stored.
	 * @param  out         Local variable index of <TT>out</TT>.
	 * @param  freeIndex   Index of the first unused local variable.
	 *
	 * @exception  ClassfileException
	 *     Thrown if there was a problem synthesizing the stub classfile.
	 */
	private static void printArrayValue
		(SynthesizedMethodDescription mStub,
		 ArrayReference arrayType,
		 int arrayIndex,
		 int out,
		 int freeIndex)
		throws ClassfileException
		{
		// Get the array's dimensions and component type.
		int ndim = arrayType.getDimensions();
		TypeReference componentType =
			ndim == 1 ?
				arrayType.getComponentType() :
				new ArrayReference (arrayType.getComponentType(), ndim-1);

		// Local variable layout:
		// Index        Contents
		// out          Reference to System.out
		// freeIndex    Array length len (1 word)
		// freeIndex+1  Array index i (1 word)
		// freeIndex+2  Component of the array at index i (1 or 2 words)
		// Record the number of local variables needed.
		int nLocals = 2 + componentType.getWordCount();
		mStub.increaseMaxLocals (freeIndex+nLocals);

		// if (array == null)
		//     {
		Location L1 = new Location();
		Location L2 = new Location();
		mStub.addInstruction (Op.ALOAD (arrayIndex));
		mStub.addInstruction (Op.IFNONNULL (L1));

		//     out.print ("null");
		mStub.addInstruction (Op.ALOAD (out));
		mStub.addInstruction (Op.LDC ("null"));
		mStub.addInstruction (Op.INVOKEVIRTUAL (mPrintString));

		//     }
		mStub.addInstruction (Op.GOTO (L2));

		// else
		//     {
		mStub.addInstruction (L1);

		//     out.print ('{');
		mStub.addInstruction (Op.ALOAD (out));
		mStub.addInstruction (Op.LDC ('{'));
		mStub.addInstruction (Op.INVOKEVIRTUAL (mPrintChar));

		//     len = array length;
		mStub.addInstruction (Op.ALOAD (arrayIndex));
		mStub.addInstruction (Op.ARRAYLENGTH);
		mStub.addInstruction (Op.ISTORE (freeIndex));

		//     i = 0;
		mStub.addInstruction (Op.LDC (0));
		mStub.addInstruction (Op.ISTORE (freeIndex+1));

		//     while (i < len)
		//         {
		Location L3 = new Location();
		Location L4 = new Location();
		mStub.addInstruction (Op.GOTO (L4));
		mStub.addInstruction (L3);

		//         if (i != 0) out.print (',');
		Location L5 = new Location();
		mStub.addInstruction (Op.ILOAD (freeIndex+1));
		mStub.addInstruction (Op.IFEQ (L5));
		mStub.addInstruction (Op.ALOAD (out));
		mStub.addInstruction (Op.LDC (','));
		mStub.addInstruction (Op.INVOKEVIRTUAL (mPrintChar));
		mStub.addInstruction (L5);

		//         Get array component at index i.
		getArrayComponent
			(mStub, componentType, arrayIndex, freeIndex+1, freeIndex+2);

		//         Print it.
		printValue (mStub, componentType, freeIndex+2, out, freeIndex+nLocals);

		//         ++ i;
		mStub.addInstruction (Op.IINC (freeIndex+1, 1));

		//         }
		mStub.addInstruction (L4);
		mStub.addInstruction (Op.ILOAD (freeIndex+1));
		mStub.addInstruction (Op.ILOAD (freeIndex));
		mStub.addInstruction (Op.IF_ICMPLT (L3));

		//     out.print ('}');
		mStub.addInstruction (Op.ALOAD (out));
		mStub.addInstruction (Op.LDC ('}'));
		mStub.addInstruction (Op.INVOKEVIRTUAL (mPrintChar));

		//     }
		mStub.addInstruction (L2);
		}

	/**
	 * Adds instructions to get a component of an array.
	 *
	 * @param  mStub          Stub method's description.
	 * @param  componentType  Type of the array components.
	 * @param  arrayIndex     Local variable index where the array reference is
	 *                        stored.
	 * @param  iIndex         Local variable index where the array index is
	 *                        stored.
	 * @param  resultIndex    Local variable index where the array component is
	 *                        to be stored.
	 *
	 * @exception  ClassfileException
	 *     Thrown if there was a problem synthesizing the stub classfile.
	 */
	private static void getArrayComponent
		(SynthesizedMethodDescription mStub,
		 TypeReference componentType,
		 int arrayIndex,
		 int iIndex,
		 int resultIndex)
		throws ClassfileException
		{
		mStub.addInstruction (Op.ALOAD (arrayIndex));
		mStub.addInstruction (Op.ILOAD (iIndex));
		if (! (componentType instanceof PrimitiveReference))
			{
			mStub.addInstruction (Op.AALOAD);
			mStub.addInstruction (Op.ASTORE (resultIndex));
			}
		else if (componentType == PrimitiveReference.CHAR)
			{
			mStub.addInstruction (Op.CALOAD);
			mStub.addInstruction (Op.ISTORE (resultIndex));
			}
		else if (componentType == PrimitiveReference.DOUBLE)
			{
			mStub.addInstruction (Op.DALOAD);
			mStub.addInstruction (Op.DSTORE (resultIndex));
			}
		else if (componentType == PrimitiveReference.FLOAT)
			{
			mStub.addInstruction (Op.FALOAD);
			mStub.addInstruction (Op.FSTORE (resultIndex));
			}
		else if (componentType == PrimitiveReference.INT)
			{
			mStub.addInstruction (Op.IALOAD);
			mStub.addInstruction (Op.ISTORE (resultIndex));
			}
		else if (componentType == PrimitiveReference.LONG)
			{
			mStub.addInstruction (Op.LALOAD);
			mStub.addInstruction (Op.LSTORE (resultIndex));
			}
		else if (componentType == PrimitiveReference.SHORT)
			{
			mStub.addInstruction (Op.SALOAD);
			mStub.addInstruction (Op.ISTORE (resultIndex));
			}
		else // BYTE or BOOLEAN
			{
			mStub.addInstruction (Op.BALOAD);
			mStub.addInstruction (Op.ISTORE (resultIndex));
			}
		}

	/**
	 * Adds instructions to return a dummy value of the proper type.
	 *
	 * @param  mStub          Stub method's description.
	 * @param  theReturnType  Stub method's return type.
	 *
	 * @exception  ClassfileException
	 *     Thrown if there was a problem synthesizing the stub classfile.
	 */
	private static void returnDummyValue
		(SynthesizedMethodDescription mStub,
		 TypeReference theReturnType)
		throws ClassfileException
		{
		if (theReturnType == null)
			{
			// Return void.
			mStub.addInstruction (Op.RETURN);
			}
		else if (! (theReturnType instanceof PrimitiveReference))
			{
			// Return null.
			mStub.addInstruction (Op.ACONST_NULL);
			mStub.addInstruction (Op.ARETURN);
			}
		else if (theReturnType == PrimitiveReference.LONG)
			{
			// Return 0L.
			mStub.addInstruction (Op.LDC (0L));
			mStub.addInstruction (Op.LRETURN);
			}
		else if (theReturnType == PrimitiveReference.FLOAT)
			{
			// Return 0.0f.
			mStub.addInstruction (Op.LDC (0.0f));
			mStub.addInstruction (Op.FRETURN);
			}
		else if (theReturnType == PrimitiveReference.DOUBLE)
			{
			// Return 0.0.
			mStub.addInstruction (Op.LDC (0.0));
			mStub.addInstruction (Op.DRETURN);
			}
		else
			{
			// Return 0.
			mStub.addInstruction (Op.LDC (0));
			mStub.addInstruction (Op.IRETURN);
			}
		}

	}
