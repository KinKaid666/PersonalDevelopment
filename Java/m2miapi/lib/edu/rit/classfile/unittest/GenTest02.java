//******************************************************************************
//
// File:    GenTest02.java
// Package: edu.rit.classfile.unittest
// Unit:    Class edu.rit.classfile.unittest.GenTest02
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

import java.io.FileOutputStream;

/**
 * Class GenTest02 demonstrates how the RIT Classfile Library can be used to
 * synthesize a simple class. The synthesized class is named
 * edu.rit.classfile.unittest.Test02. If this class were to be created by a Java
 * compiler, the Java source code would be:
 * <PRE>
 *     package edu.rit.classfile.unittest;
 *
 *     import java.io.PrintStream;
 *
 *     public class Test02
 *         {
 *         public void countdown()
 *             {
 *             PrintStream out = System.out;
 *             int i = 10;
 *             while (i >= 0)
 *                 {
 *                 out.print (i);
 *                 out.println ("...");
 *                 -- i;
 *                 }
 *             out.println ("Liftoff!");
 *             }
 *         }</PRE>
 * <P>
 * The GenTest02 main program synthesizes the classfile for this class and
 * stores it in the file <TT>"Test02.class"</TT> in the current directory. Study
 * the <A HREF="doc-files/GenTest02.html">source code</A> for class GenTest02 to
 * see how it does it.
 * <P>
 * <B>RIT Classfile Library Version 02-Jul-2002</B>
 *
 * @author  Alan Kaminsky
 * @version 05-Oct-2001
 */
public class GenTest02
	{

	public static void main
		(String[] args)
		{
		try
			{
			// 1. Get references to all classes, methods, and fields used by
			// class Test02.

			// 1.a. Get a reference to class java.lang.Object.
			NamedClassReference cObject =
				NamedClassReference.JAVA_LANG_OBJECT;

			// 1.b. Get a reference to constructor Object().
			ConstructorReference mObjectinit =
				new ConstructorReference (cObject);

			// 1.c. Get a reference to class java.io.PrintStream.
			NamedClassReference cPrintStream =
				new NamedClassReference ("java.io.PrintStream");

			// 1.d. Get a reference to method PrintStream.print(int).
			MethodReference mPrintInt =
				new MethodReference (cPrintStream, "print");
			mPrintInt.addArgumentType
				(PrimitiveReference.INT);

			// 1.e. Get a reference to method PrintStream.println(String).
			MethodReference mPrintlnString =
				new MethodReference (cPrintStream, "println");
			mPrintlnString.addArgumentType
				(NamedClassReference.JAVA_LANG_STRING);

			// 1.f. Get a reference to class java.lang.System.
			NamedClassReference cSystem =
				new NamedClassReference ("java.lang.System");

			// 1.g. Get a reference to field System.out of type PrintStream.
			NamedFieldReference fSystemOut =
				new NamedFieldReference (cSystem, "out", cPrintStream);

			// 2. Create the synthesized class description for class Test02.
			SynthesizedClassDescription cTest02 =
				new SynthesizedClassDescription
					("edu.rit.classfile.unittest.Test02");

			// 3. Create the synthesized constructor description for the default
			// constructor Test02().
			SynthesizedConstructorDescription mTest02init =
				new SynthesizedConstructorDescription (cTest02);

			// 3.a. The local variable layout for constructor Test02() is:
			// Index  Name  Contents
			//   0    this  "This" pointer (set by the JVM)
			// Record the number of words needed for local variables.
			mTest02init.setMaxLocals (1);

			// 3.b. Record the number of words needed for constructor Test02()'s
			// operand stack.
			mTest02init.setMaxStack (1);

			// 3.c. Add constructor Test02()'s bytecodes.
			//     {
			//     super();
			mTest02init.addInstruction (Op.ALOAD (0));
			mTest02init.addInstruction (Op.INVOKESPECIAL (mObjectinit));
			//     }
			mTest02init.addInstruction (Op.RETURN);

			// 4. Create the synthesized method description for method
			// Test02.countdown().
			SynthesizedMethodDescription mTest02countdown =
				new SynthesizedMethodDescription (cTest02, "countdown");

			// 4.a. The local variable layout for method Test02.countdown() is:
			// Index  Name  Contents
			//   0    this  "This" pointer (set by the JVM)
			//   1    out   Local variable
			//   2    i     Local variable
			// Record the number of words needed for local variables.
			mTest02countdown.setMaxLocals (3);

			// 4.b. Record the number of words needed for method
			// Test02.countdown()'s operand stack.
			mTest02countdown.setMaxStack (2);

			// 4.c. Add method Test02.countdown()'s bytecodes.
			//     {
			//     out = System.out;
			mTest02countdown.addInstruction (Op.GETSTATIC (fSystemOut));
			mTest02countdown.addInstruction (Op.ASTORE (1));
			//     i = 10;
			mTest02countdown.addInstruction (Op.LDC (10));
			mTest02countdown.addInstruction (Op.ISTORE (2));
			//     while (i >= 0)
			//         {
			Location L1 = new Location();
			Location L2 = new Location();
			mTest02countdown.addInstruction (Op.GOTO (L2));
			mTest02countdown.addInstruction (L1);
			//         out.print (i);
			mTest02countdown.addInstruction (Op.ALOAD (1));
			mTest02countdown.addInstruction (Op.ILOAD (2));
			mTest02countdown.addInstruction (Op.INVOKEVIRTUAL (mPrintInt));
			//         out.println ("...");
			mTest02countdown.addInstruction (Op.ALOAD (1));
			mTest02countdown.addInstruction (Op.LDC ("..."));
			mTest02countdown.addInstruction (Op.INVOKEVIRTUAL (mPrintlnString));
			//         -- i;
			mTest02countdown.addInstruction (Op.IINC (2, -1));
			//         }
			mTest02countdown.addInstruction (L2);
			mTest02countdown.addInstruction (Op.ILOAD (2));
			mTest02countdown.addInstruction (Op.IFGE (L1));
			//     out.println ("Liftoff!");
			mTest02countdown.addInstruction (Op.ALOAD (1));
			mTest02countdown.addInstruction (Op.LDC ("Liftoff!"));
			mTest02countdown.addInstruction (Op.INVOKEVIRTUAL (mPrintlnString));
			//     }
			mTest02countdown.addInstruction (Op.RETURN);

			// 5. Emit the classfile for class Test02 into file "Test02.class".
			FileOutputStream fos = new FileOutputStream ("Test02.class");
			cTest02.emit (fos);
			fos.close();
			}

		catch (Throwable exc)
			{
			System.err.println ("GenTest02: Uncaught exception");
			exc.printStackTrace (System.err);
			}
		}

	}
