//******************************************************************************
//
// File:    MatrixImpl.java
// Package: edu.rit.m2mi.matrix2
// Unit:    Class edu.rit.m2mi.matrix2.MatrixImpl
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
import edu.rit.m2mi.SynthesisException;

import java.util.Random;

/**
 * Class MatrixImpl provides an exported M2MI object that encapsulates a matrix.
 * <P>
 * <B>M2MI Library Version 02-Jul-2002</B>
 *
 * @author  Alan Kaminsky
 * @version 07-May-2002
 */
public class MatrixImpl
	implements Matrix
	{

// Hidden data members.

	private String myName;
	private float[][] myRows;
	private int myUnfilledCount;
	private Coordinator anyCoordinator;

// Exported constructors.

	/**
	 * Construct a new zero matrix with the given number of rows and columns.
	 * Every element is initialized to 0.
	 *
	 * @param  name  Matrix name for debug printouts.
	 * @param  rows  Number of rows.
	 * @param  cols  Number of columns.
	 *
	 * @exception  SynthesisException
	 *     Thrown if there was a problem synthesizing handle classes.
	 */
	public MatrixImpl
		(String name,
		 int rows,
		 int cols)
		throws SynthesisException
		{
		myName = name;
		myRows = new float [rows] [];
		for (int r = 0; r < rows; ++ r)
			{
			myRows[r] = new float [cols];
			}
		myUnfilledCount = rows * cols;
		anyCoordinator = (Coordinator) M2MI.getOmnihandle (Coordinator.class);
		}

	/**
	 * Construct a new random matrix with the given number of rows and columns.
	 * Every element is initialized to a value <I>x</I> chosen uniformly at
	 * random in the range <I>lb</I> &lt;= <I>x</I> &lt;= <I>ub</I>.
	 *
	 * @param  name  Matrix name for debug printouts.
	 * @param  rows  Number of rows.
	 * @param  cols  Number of columns.
	 * @param  lb    Random value lower bound.
	 * @param  ub    Random value upper bound.
	 * @param  prng  Pseudorandom number generator.
	 *
	 * @exception  SynthesisException
	 *     Thrown if there was a problem synthesizing handle classes.
	 */
	public MatrixImpl
		(String name,
		 int rows,
		 int cols,
		 float lb,
		 float ub,
		 Random prng)
		throws SynthesisException
		{
		this (name, rows, cols);
		float delta = ub - lb;
		for (int r = 0; r < rows; ++ r)
			{
			for (int c = 0; c < cols; ++ c)
				{
				myRows[r][c] = prng.nextFloat() * delta + lb;
				}
			}
		}

// Exported operations inherited and implemented from interface Matrix.

	/**
	 * Send all of this matrix's rows to the coordinator. Each row is sent by
	 * invoking {@link Coordinator#putRow(int,float[]) <TT>putRow()</TT>} on the
	 * appropriate {@link Coordinator </TT>Coordinator<TT>} handle.
	 */
	public synchronized void putRows()
		{
		synchronized (System.out)
			{
			printIdentity();
			System.out.println (".putRows();");
			}
		int rows = myRows.length;
		for (int r = 0; r < rows; ++ r)
			{
			anyCoordinator.putRow (r, myRows[r]);
			}
		}

	/**
	 * Send all of this matrix's columns to the coordinator. Each column is sent
	 * by invoking {@link Coordinator#putColumn(int,float[])
	 * <TT>putColumn()</TT>} on the appropriate {@link Coordinator
	 * </TT>Coordinator<TT>} handle.
	 */
	public synchronized void putColumns()
		{
		synchronized (System.out)
			{
			printIdentity();
			System.out.println (".putColumns();");
			}
		int rows = myRows.length;
		int cols = myRows[0].length;
		float[] vals = new float [rows];
		for (int c = 0; c < cols; ++ c)
			{
			for (int r = 0; r < rows; ++ r)
				{
				vals[r] = myRows[r][c];
				}
			anyCoordinator.putColumn (c, vals);
			}
		}

	/**
	 * Set the element of this matrix at the given row and column to the given
	 * value.
	 *
	 * @param  row  Row index in the range 0 .. (number of rows - 1).
	 * @param  col  Column index in the range 0 .. (number of columns - 1).
	 * @param  val  Element value.
	 */
	public synchronized void setElement
		(int row,
		 int col,
		 float val)
		{
		synchronized (System.out)
			{
			printIdentity();
			System.out.print (".setElement (");
			System.out.print (row);
			System.out.print (", ");
			System.out.print (col);
			System.out.print (", ");
			System.out.print (val);
			System.out.println (");");
			}
		myRows[row][col] = val;
		-- myUnfilledCount;
		if (myUnfilledCount == 0)
			{
			notifyAll();
			}
		}

// Exported operations.

	/**
	 * Print this matrix on the standard output.
	 */
	public synchronized void print()
		{
		synchronized (System.out)
			{
			printIdentity();
			System.out.println (" =");
			int rows = myRows.length;
			int cols = myRows[0].length;
			for (int r = 0; r < rows; ++ r)
				{
				System.out.print (r == 0 ? "{{" : " {");
				for (int c = 0; c < cols; ++ c)
					{
					System.out.print (c == 0 ? "" : ", ");
					System.out.print (myRows[r][c]);
					}
				System.out.println (r == rows-1 ? "}}" : "},");
				}
			}
		}

	/**
	 * Wait until this matrix has been filled in. This method blocks until the
	 * number of times <TT>setElement()</TT> has been called since this matrix
	 * was constructed is equal to the number of elements in this matrix.
	 *
	 * @exception  InterruptedException
	 *     Thrown if the calling thread is interrupted while blocked in this
	 *     method.
	 */
	public synchronized void waitUntilFilled()
		throws InterruptedException
		{
		while (myUnfilledCount > 0)
			{
			wait();
			}
		}

// Hidden operations.

	private void printIdentity()
		{
		System.out.print (myName);
		}

	}
