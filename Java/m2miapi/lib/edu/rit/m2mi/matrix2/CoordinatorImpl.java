//******************************************************************************
//
// File:    CoordinatorImpl.java
// Package: edu.rit.m2mi.matrix2
// Unit:    Class edu.rit.m2mi.matrix2.CoordinatorImpl
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

import edu.rit.m2mi.M2MI;
import edu.rit.m2mi.Multihandle;
import edu.rit.m2mi.SynthesisException;

/**
 * Class CoordinatorImpl encapsulates an exported M2MI object that coordinates
 * the computation of the product of two matrices. The source matrices send
 * their rows and columns, respectively, to the coordinator. The coordinator in
 * turn sends each row or column just to the workers that need it.
 * <P>
 * <B>M2MI Library Version 02-Jul-2002</B>
 *
 * @author  Alan Kaminsky
 * @version 16-May-2002
 */
public class CoordinatorImpl
	implements Coordinator
	{

// Hidden data members.

	private Worker[] myRowWorkers;
	private Worker[] myColumnWorkers;
	private Worker allWorkers;

// Exported constructors.

	/**
	 * Construct a new coordinator with the given number of rows and columns.
	 * Also construct the requisite worker objects.
	 *
	 * @param  rows  Number of rows.
	 * @param  cols  Number of columns.
	 *
	 * @exception  ClassNotFoundException
	 *     Thrown if interface Worker cannot be found in the M2MI layer's parent
	 *     class loader.
	 * @exception  SynthesisException
	 *     Thrown if there was a problem synthesizing handle classes.
	 */
	public CoordinatorImpl
		(int rows,
		 int cols)
		throws ClassNotFoundException, SynthesisException
		{
		int r, c;

		myRowWorkers = new Worker [rows];
		for (r = 0; r < rows; ++ r)
			{
			myRowWorkers[r] = (Worker) M2MI.getMultihandle (Worker.class);
			}

		myColumnWorkers = new Worker [cols];
		for (c = 0; c < cols; ++ c)
			{
			myColumnWorkers[c] = (Worker) M2MI.getMultihandle (Worker.class);
			}

		for (r = 0; r < rows; ++ r)
			{
			for (c = 0; c < cols; ++ c)
				{
				Worker theWorker = new WorkerImpl (r, c);
				((Multihandle) myRowWorkers[r]).attach (theWorker);
				((Multihandle) myColumnWorkers[c]).attach (theWorker);
				}
			}

		allWorkers = (Worker) M2MI.getOmnihandle (Worker.class);
		}

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
		(Matrix theMatrix)
		{
		synchronized (System.out)
			{
			System.out.print ("CoordinatorImpl.compute (");
			System.out.print (theMatrix);
			System.out.println (");");
			}
		allWorkers.compute (theMatrix);
		}

	/**
	 * Obtain a row from the first matrix in the product. The values are passed
	 * on to just those workers that are computing elements in the given row.
	 *
	 * @param  row   Row index.
	 * @param  vals  Values in the row.
	 */
	public void putRow
		(int row,
		 float[] vals)
		{
		synchronized (System.out)
			{
			System.out.print ("CoordinatorImpl.putRow (");
			System.out.print (row);
			System.out.print (", ");
			printFloatArray (vals);
			System.out.println (");");
			}
		myRowWorkers[row].putRow (vals);
		}

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
		 float[] vals)
		{
		synchronized (System.out)
			{
			System.out.print ("CoordinatorImpl.putColumn (");
			System.out.print (col);
			System.out.print (", ");
			printFloatArray (vals);
			System.out.println (");");
			}
		myColumnWorkers[col].putColumn (vals);
		}

// Hidden operations.

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
