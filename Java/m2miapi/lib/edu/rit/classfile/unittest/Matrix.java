//******************************************************************************
//
// File:    Matrix.java
// Package: edu.rit.classfile.unittest
// Unit:    Interface edu.rit.classfile.unittest.Matrix
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
 * Interface Matrix simply defines a few methods with array arguments for
 * testing class {@link StubSynthesizer </CODE>StubSynthesizer<CODE>}.
 * <P>
 * Study the <A HREF="doc-files/Matrix.html">source code</A> for interface
 * Matrix.
 * <P>
 * <B>RIT Classfile Library Version 02-Jul-2002</B>
 *
 * @author  Alan Kaminsky
 * @version 11-Oct-2001
 */
public interface Matrix
	{

// Exported operations.

	/**
	 * Sets this matrix to its transpose.
	 */
	public void transpose();

	/**
	 * Sets this matrix to the product of itself and the given scalar value.
	 */
	public void product
		(int theScalar);

	/**
	 * Sets this matrix to the product of itself and the given vector.
	 */
	public void product
		(int[] theVector);

	/**
	 * Sets this matrix to the product of itself and the given matrix.
	 */
	public void product
		(int[][] theMatrix);

	}
