//******************************************************************************
//
// File:    Matrix.java
// Package: edu.rit.m2mi.matrix1
// Unit:    Interface edu.rit.m2mi.matrix1.Matrix
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

package edu.rit.m2mi.matrix1;

/**
 * Interface Matrix specifies the interface for an exported M2MI object that
 * encapsulates a matrix.
 * <P>
 * <B>M2MI Library Version 02-Jul-2002</B>
 *
 * @author  Alan Kaminsky
 * @version 24-Apr-2002
 */
public interface Matrix
	{

// Exported operations.

	/**
	 * Send all of this matrix's rows to the workers. Each row is sent by
	 * invoking {@link Worker#putRow(int,float[]) <TT>putRow()</TT>} on the
	 * appropriate {@link Worker </TT>Worker<TT>} handle(s).
	 */
	public void putRows();

	/**
	 * Send all of this matrix's columns to the workers. Each column is sent by
	 * invoking {@link Worker#putColumn(int,float[]) <TT>putColumn()</TT>} on
	 * the appropriate {@link Worker </TT>Worker<TT>} handle(s).
	 */
	public void putColumns();

	/**
	 * Set the element of this matrix at the given row and column to the given
	 * value.
	 *
	 * @param  row  Row index in the range 0 .. (number of rows - 1).
	 * @param  col  Column index in the range 0 .. (number of columns - 1).
	 * @param  val  Element value.
	 */
	public void setElement
		(int row,
		 int col,
		 float val);

	}
