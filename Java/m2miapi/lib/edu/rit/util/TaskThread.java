//******************************************************************************
//
// File:    TaskThread.java
// Package: edu.rit.util
// Unit:    Class edu.rit.util.TaskThread
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

package edu.rit.util;

/**
 * Class TaskThread encapsulates a generic task thread that resides in a {@link
 * TaskThreadPool </TT>TaskThreadPool<TT>}. The task thread continually repeats
 * this cycle: Wait until it gets a task ({@link java.lang.Runnable
 * </TT>Runnable<TT>} object) from its task thread pool; invoke the task's
 * <TT>run()</TT> method. Any exceptions thrown by the task's <TT>run()</TT>
 * method are dumped on the standard error stream. The task thread is a daemon
 * thread that will terminate automatically when the application exits.
 * <P>
 * <B>M2MI Library Version 02-Jul-2002</B>
 *
 * @author  Alan Kaminsky
 * @version 01-Apr-2002
 */
public class TaskThread
	extends Thread
	{

// Hidden data members.

	private TaskThreadPool myPool;

// Exported constructors.

	/**
	 * Construct a new task thread that resides in the given task thread pool.
	 * The task thread is marked as a daemon thread and started automatically.
	 *
	 * @param  thePool  Task thread pool.
	 *
	 * @exception  NullPointerException
	 *     (unchecked exception) Thrown if <TT>thePool</TT> is null.
	 */
	public TaskThread
		(TaskThreadPool thePool)
		{
		if (thePool == null)
			{
			throw new NullPointerException();
			}
		myPool = thePool;
		setDaemon (true);
		start();
		}

// Exported operations.

	/**
	 * Perform this task thread's processing. The task thread continually
	 * repeats this cycle: Wait until it gets a task ({@link java.lang.Runnable
	 * </TT>Runnable<TT>} object) from its task thread pool; invoke the task's
	 * <TT>run()</TT> method. Any exceptions thrown by the task's <TT>run()</TT>
	 * method are dumped on the standard error stream.
	 */
	public void run()
		{
		for (;;)
			{
			try
				{
				myPool.getTask().run();
				}
			catch (Throwable exc)
				{
				synchronized (System.err)
					{
					System.err.println
						("edu.rit.m2mi.TaskThread: Uncaught exception");
					exc.printStackTrace (System.err);
					}
				}
			}
		}

	}
