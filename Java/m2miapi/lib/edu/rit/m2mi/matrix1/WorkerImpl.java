//******************************************************************************
//
// File:    WorkerImpl.java
// Package: edu.rit.m2mi.matrix1
// Unit:    Class edu.rit.m2mi.matrix1.WorkerImpl
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
 * Class WorkerImpl encapsulates an exported M2MI object that computes one
 * element in the product of two matrices. The row and column indexes for the
 * element to be computed are specified when the worker object is constructed.
 * <P>
 * <B>M2MI Library Version 02-Jul-2002</B>
 *
 * @author  Alan Kaminsky
 * @version 24-Apr-2002
 */
public class WorkerImpl
	implements Worker
	{

// Hidden data members.

	private int myRowIndex;
	private int myColumnIndex;
	private Matrix myResultMatrix = null;
	private float[] myRow = null;
	private float[] myColumn = null;

// Exported constructors.

	/**
	 * Construct a new worker that will compute the product matrix element at
	 * the given row and column.
	 *
	 * @param  row  Row index.
	 * @param  col  Column index.
	 */
	public WorkerImpl
		(int row,
		 int col)
		{
		myRowIndex = row;
		myColumnIndex = col;
		}

// Exported operations.

	/**
	 * Specify the product matrix to be computed. This worker will store its
	 * results by invoking {@link Matrix#setElement(int,int,float)
	 * <TT>setElement()</TT>} on the given {@link Matrix </TT>Matrix<TT>}
	 * handle.
	 *
	 * @param  theMatrix  Matrix handle in which to store results.
	 */
	public synchronized void compute
		(Matrix theMatrix)
		{
		synchronized (System.out)
			{
			printIdentity();
			System.out.print (".compute (");
			System.out.print (theMatrix);
			System.out.println (");");
			}
		myResultMatrix = theMatrix;
		computeElementIfPossible();
		}

	/**
	 * Obtain a row from the first matrix in the product.
	 *
	 * @param  row   Row index.
	 * @param  vals  Values in the row.
	 */
	public synchronized void putRow
		(int row,
		 float[] vals)
		{
		synchronized (System.out)
			{
			printIdentity();
			System.out.print (".putRow (");
			System.out.print (row);
			System.out.print (", ");
			printFloatArray (vals);
			System.out.println (");");
			}
		if (row == myRowIndex)
			{
			myRow = vals;
			computeElementIfPossible();
			}
		}

	/**
	 * Obtain a column from the second matrix in the product.
	 *
	 * @param  col   Column index.
	 * @param  vals  Values in the column.
	 */
	public synchronized void putColumn
		(int col,
		 float[] vals)
		{
		synchronized (System.out)
			{
			printIdentity();
			System.out.print (".putColumn (");
			System.out.print (col);
			System.out.print (", ");
			printFloatArray (vals);
			System.out.println (");");
			}
		if (col == myColumnIndex)
			{
			myColumn = vals;
			computeElementIfPossible();
			}
		}

// Hidden operations.

	private void computeElementIfPossible()
		{
		if
			(myResultMatrix != null &&
			 myRow != null &&
			 myColumn != null)
			{
			float result = 0.0f;
			int n = myRow.length;
			for (int i = 0; i < n; ++ i)
				{
				result += myRow[i] * myColumn[i];
				}
			myResultMatrix.setElement (myRowIndex, myColumnIndex, result);
			myResultMatrix = null;
			myRow = null;
			myColumn = null;
			}
		}

	private void printIdentity()
		{
		System.out.print ("WorkerImpl[");
		System.out.print (myRowIndex);
		System.out.print (",");
		System.out.print (myColumnIndex);
		System.out.print ("]");
		}

	private static void printFloatArray
		(float[] vals)
		{
		int n = vals.length;
		System.out.print ("{");
		for (int i = 0; i < n; ++ i)
			{
			if (i > 0)
				{
				System.out.print (", ");
				}
			System.out.print (vals[i]);
			}
		System.out.print ("}");
		}

	}
