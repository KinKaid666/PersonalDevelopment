//******************************************************************************
//
// File:    TestTest02.java
// Package: edu.rit.classfile.unittest
// Unit:    Class edu.rit.classfile.unittest.TestTest02
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

/**
 * Class TestTest02 is a main program that tests the class synthesized by the
 * {@link GenTest02 </CODE>GenTest02<CODE>} main program. It creates a new
 * instance of class Test02 and invokes its <TT>countdown()</TT> method. The
 * following should be printed:
 * <PRE>
 *     10...
 *     9...
 *     8...
 *     7...
 *     6...
 *     5...
 *     4...
 *     3...
 *     2...
 *     1...
 *     0...
 *     Liftoff!</PRE>
 * <P>
 * Compiling class TestTest02 verifies that the synthesized classfile works
 * properly with the Java compiler. Running the TestTest02 main program verifies
 * that the synthesized classfile works properly with the Java Virtual Machine.
 * <P>
 * Study the <A HREF="doc-files/TestTest02.html">source code</A> for class
 * TestTest02.
 * <P>
 * <B>RIT Classfile Library Version 02-Jul-2002</B>
 *
 * @author  Alan Kaminsky
 * @version 05-Oct-2001
 */
public class TestTest02
	{

	public static void main
		(String[] args)
		{
		try
			{
			Test02 obj = new Test02();
			obj.countdown();
			}

		catch (Throwable exc)
			{
			System.err.println ("TestTest02: Uncaught exception");
			exc.printStackTrace (System.err);
			}
		}

	}
