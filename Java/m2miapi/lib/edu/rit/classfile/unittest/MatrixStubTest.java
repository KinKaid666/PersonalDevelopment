//******************************************************************************
//
// File:    MatrixStubTest.java
// Package: edu.rit.classfile.unittest
// Unit:    Class edu.rit.classfile.unittest.MatrixStubTest
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

import edu.rit.classfile.DirectClassLoader;

import java.io.OutputStream;
import java.io.PrintStream;

/**
 * Class MatrixStubTest is a main program that exercises the Stub Synthesizer in
 * class {@link StubSynthesizer </CODE>StubSynthesizer<CODE>}. It uses the Stub
 * Synthesizer to synthesize an implementation of interface {@link Matrix
 * </CODE>Matrix<CODE>}, loads the synthesized class into the JVM, creates an
 * instance, and calls its methods.
 * <P>
 * Study the <A HREF="doc-files/MatrixStubTest.html">source code</A> for class
 * MatrixStubTest.
 * <P>
 * <B>RIT Classfile Library Version 02-Jul-2002</B>
 *
 * @author  Alan Kaminsky
 * @version 11-Oct-2001
 */
public class MatrixStubTest
	{

	public static void main
		(String[] args)
		{
		try
			{
			PrintStream out = System.out;

			// Set up a direct class loader to load synthesized classes into
			// the JVM.
			DirectClassLoader loader = new DirectClassLoader();

			// Synthesize a stub implementation for interface Matrix, writing
			// the classfile into the direct class loader.
			OutputStream os = loader.writeClass ("MatrixImpl");
			StubSynthesizer.getStub (Matrix.class, "MatrixImpl", os);
			os.close();

			// Create an instance of the Matrix stub. We have to explicitly
			// specify the class loader to use.
			Matrix matrix = (Matrix)
				Class.forName ("MatrixImpl", true, loader).newInstance();

			// Call some methods on the matrix. Each one should cause the stub
			// to print out the method being called and its arguments.
			int[] v0 = null;
			int[] v1 = new int[] {};
			int[] v2 = new int[] {1};
			int[] v3 = new int[] {2, 3, 4, 5};

			int[][] m0  = null;
			int[][] m1  = new int[][] {};
			int[][] m2  = new int[][] {null};
			int[][] m3  = new int[][] {null, null, null};
			int[][] m4  = new int[][] {{}};
			int[][] m5  = new int[][] {{1}};
			int[][] m6  = new int[][] {{2, 3, 4}};
			int[][] m7  = new int[][] {{}, {}, {}};
			int[][] m8  = new int[][] {{1}, {2}, {3}};
			int[][] m9  = new int[][] {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
			int[][] m10 = new int[][] {null, {}, {1}, {2, 3}, {4, 5, 6}};

			out.print ("matrix.transpose() --> ");
			matrix.transpose();
			out.println();

			out.print ("matrix.product(42) --> ");
			matrix.product (42);
			out.println();

			out.print ("matrix.product(v0) --> ");
			matrix.product (v0);
			out.println();

			out.print ("matrix.product(v1) --> ");
			matrix.product (v1);
			out.println();

			out.print ("matrix.product(v2) --> ");
			matrix.product (v2);
			out.println();

			out.print ("matrix.product(v3) --> ");
			matrix.product (v3);
			out.println();

			out.print ("matrix.product(m0) --> ");
			matrix.product (m0);
			out.println();

			out.print ("matrix.product(m1) --> ");
			matrix.product (m1);
			out.println();

			out.print ("matrix.product(m2) --> ");
			matrix.product (m2);
			out.println();

			out.print ("matrix.product(m3) --> ");
			matrix.product (m3);
			out.println();

			out.print ("matrix.product(m4) --> ");
			matrix.product (m4);
			out.println();

			out.print ("matrix.product(m5) --> ");
			matrix.product (m5);
			out.println();

			out.print ("matrix.product(m6) --> ");
			matrix.product (m6);
			out.println();

			out.print ("matrix.product(m7) --> ");
			matrix.product (m7);
			out.println();

			out.print ("matrix.product(m8) --> ");
			matrix.product (m8);
			out.println();

			out.print ("matrix.product(m9) --> ");
			matrix.product (m9);
			out.println();

			out.print ("matrix.product(m10) --> ");
			matrix.product (m10);
			out.println();
			}

		catch (Throwable exc)
			{
			System.err.println ("MatrixStubTest: Uncaught exception");
			exc.printStackTrace (System.err);
			}
		}

	}
