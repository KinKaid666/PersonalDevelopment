//******************************************************************************
//
// File:    Coordinator.java
// Package: edu.rit.m2mi.matrix2
// Unit:    Interface edu.rit.m2mi.matrix2.Coordinator
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
 * Interface Coordinator specifies the interface for an exported M2MI object
 * that coordinates the computation of the product of two matrices. The source
 * matrices send their rows and columns, respectively, to the coordinator. The
 * coordinator in turn sends each row or column just to the workers that need
 * it.
 * <P>
 * <B>M2MI Library Version 02-Jul-2002</B>
 *
 * @author  Alan Kaminsky
 * @version 25-Apr-2002
 */
public interface Coordinator
	{

// Exported operations.

	/**
	 * Specify the product matrix to be computed. The workers will store their
	 * results by invoking {@link Matrix#setElement(int,int,float)
	 * <TT>setElement()</TT>} on the given {@link Matrix </TT>Matrix<TT>}
	 * handle.
	 *
	 * @param  theMatrix  Matrix handle in which to store results.
	 */
	public void compute
		(Matrix theMatrix);

	/**
	 * Obtain a row from the first matrix in the product. The values are passed
	 * on to just those workers that are computing elements in the given row.
	 *
	 * @param  row   Row index.
	 * @param  vals  Values in the row.
	 */
	public void putRow
		(int row,
		 float[] vals);

	/**
	 * Obtain a column from the first matrix in the product. The values are
	 * passed on to just those workers that are computing elements in the given
	 * column.
	 *
	 * @param  col   Column index.
	 * @param  vals  Values in the column.
	 */
	public void putColumn
		(int col,
		 float[] vals);

	}
