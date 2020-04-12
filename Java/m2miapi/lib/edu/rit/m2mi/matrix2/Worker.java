//******************************************************************************
//
// File:    Worker.java
// Package: edu.rit.m2mi.matrix2
// Unit:    Interface edu.rit.m2mi.matrix2.Worker
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

package edu.rit.m2mi.matrix2;

/**
 * Interface Worker specifies the interface for an exported M2MI object that
 * computes one element in the product of two matrices. The row and column
 * indexes for the element to be computed are specified when the worker object
 * is constructed (not in this interface).
 * <P>
 * <B>M2MI Library Version 02-Jul-2002</B>
 *
 * @author  Alan Kaminsky
 * @version 25-Apr-2002
 */
public interface Worker
	{

// Exported operations.

	/**
	 * Specify the product matrix to be computed. This worker will store its
	 * results by invoking {@link Matrix#setElement(int,int,float)
	 * <TT>setElement()</TT>} on the given {@link Matrix </TT>Matrix<TT>}
	 * handle.
	 *
	 * @param  theMatrix  Matrix handle in which to store results.
	 */
	public void compute
		(Matrix theMatrix);

	/**
	 * Obtain the row this worker needs from the first matrix in the product.
	 *
	 * @param  vals  Values in the row.
	 */
	public void putRow
		(float[] vals);

	/**
	 * Obtain the column this worker needs from the second matrix in the
	 * product.
	 *
	 * @param  vals  Values in the column.
	 */
	public void putColumn
		(float[] vals);

	}
