//******************************************************************************
//
// File:    MatrixProduct1.java
// Package: edu.rit.m2mi.matrix1
// Unit:    Interface edu.rit.m2mi.matrix1.MatrixProduct1
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

import edu.rit.m2mi.M2MI;

import java.util.Random;

/**
 * Class MatrixProduct1 is a main program that computes the product of two
 * random matrices using M2MI.
 * <P>
 * Usage: java edu.rit.m2mi.matrix1.MatrixProduct1 <I>n seed</I>
 * <BR>
 * <I>n</I> = Matrix size; two random <I>n</I>x<I>n</I> matrices will be
 * multiplied
 * <BR>
 * <I>seed</I> = Random seed
 * <P>
 * <B>M2MI Library Version 02-Jul-2002</B>
 *
 * @author  Alan Kaminsky
 * @version 24-Apr-2002
 */
public class MatrixProduct1
	{

	/**
	 * Main program.
	 */
	public static void main
		(String[] args)
		{
		try
			{
			if (args.length != 2)
				{
				System.err.println ("Usage: java edu.rit.m2mi.matrix1.MatrixProduct1 <n> <seed>");
				return;
				}

			int n = Integer.parseInt (args[0]);
			long seed = Long.parseLong (args[1]);

			M2MI.initialize (1234L, null, System.currentTimeMillis(), 100);

			Random prng = new Random (seed);

			MatrixImpl a =
				new MatrixImpl ("matrixA", n, n, -10.0f, 10.0f, prng);
			MatrixImpl b =
				new MatrixImpl ("matrixB", n, n, -10.0f, 10.0f, prng);
			MatrixImpl c =
				new MatrixImpl ("matrixC", n, n);

			Matrix matrixA = (Matrix) M2MI.getUnihandle (a, Matrix.class);
			Matrix matrixB = (Matrix) M2MI.getUnihandle (b, Matrix.class);
			Matrix matrixC = (Matrix) M2MI.getUnihandle (c, Matrix.class);

			for (int i = 0; i < n; ++ i)
				{
				for (int j = 0; j < n; ++ j)
					{
					M2MI.export (new WorkerImpl (i, j), Worker.class);
					}
				}

			Worker allWorkers = (Worker) M2MI.getOmnihandle (Worker.class);

			allWorkers.compute (matrixC);
			matrixA.putRows();
			matrixB.putColumns();

			c.waitUntilFilled();

			a.print();
			b.print();
			c.print();
			}

		catch (Throwable exc)
			{
			System.err.println ("MatrixProduct1: Uncaught exception");
			exc.printStackTrace (System.err);
			}
		}

	}
