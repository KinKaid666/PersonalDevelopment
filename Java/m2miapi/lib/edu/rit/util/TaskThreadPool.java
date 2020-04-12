//******************************************************************************
//
// File:    TaskThreadPool.java
// Package: edu.rit.util
// Unit:    Class edu.rit.util.TaskThreadPool
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

import java.util.LinkedList;

/**
 * Class TaskThreadPool encapsulates a pool of {@link TaskThread
 * </TT>TaskThread<TT>}s. To perform a task ({@link java.lang.Runnable
 * </TT>Runnable<TT>} object), you call the task thread pool's {@link
 * #performTask(Runnable) <TT>performTask()</TT>} method. The task thread pool
 * then picks an idle task thread and hands the task to the task thread. The
 * task thread then calls the task's <TT>run()</TT> method. Thus, every task is
 * performed concurrently by its own separate thread.
 * <P>
 * If all existing task threads are busy when you tell the task thread pool to
 * perform a task, and the maximum number of task threads have not been created
 * yet, the task thread pool creates a new task thread automatically. The
 * maximum number of task threads is fixed when the task thread pool is
 * constructed. Once created, the task threads stay in the pool until the
 * application exits, and each thread consumes some amount of memory. You should
 * tailor the maximum number of task threads to be as small as necessary for
 * each application.
 * <P>
 * <B>M2MI Library Version 02-Jul-2002</B>
 *
 * @author  Alan Kaminsky
 * @version 22-May-2002
 */
public class TaskThreadPool
	{

// Hidden data members.

	/**
	 * Maximum number of task threads this task thread pool will create.
	 */
	private int myMaximumThreadCount;

	/**
	 * Number of task threads created.
	 */
	private int myCreatedThreadCount = 0;

	/**
	 * Number of task threads waiting for a task.
	 */
	private int myWaitCount = 0;

	/**
	 * Queue of tasks to be performed.
	 */
	private LinkedList myTaskQueue = new LinkedList();

// Exported constructors.

	/**
	 * Construct a new task thread pool.
	 *
	 * @param  max  Maximum number of task threads this task thread pool will
	 *              create.
	 *
	 * @exception  IllegalArgumentException
	 *     (unchecked exception) Thrown if <TT>max</TT> &lt; 1.
	 */
	public TaskThreadPool
		(int max)
		{
		if (max < 1)
			{
			throw new IllegalArgumentException();
			}
		myMaximumThreadCount = max;
		}

// Exported operations.

	/**
	 * Perform the given task by a task thread from this task thread pool.
	 *
	 * @param  theTask  Task ({@link java.lang.Runnable </TT>Runnable<TT>}
	 *                  object).
	 *
	 * @exception  NullPointerException
	 *     (unchecked exception) Thrown if <TT>theTask</TT> is null.
	 */
	public synchronized void performTask
		(Runnable theTask)
		{
		// Verify preconditions.
		if (theTask == null)
			{
			throw new NullPointerException();
			}

		// Record the task.
		myTaskQueue.addLast (theTask);

		// If there are waiting task threads, wake up one of them.
		if (myWaitCount > 0)
			{
			-- myWaitCount;
			notify();
			}

		// Otherwise, if the maximum number of task threads have not been
		// created yet, start a new one.
		else if (myCreatedThreadCount < myMaximumThreadCount)
			{
			++ myCreatedThreadCount;
			new TaskThread (this);
			}
		}

// Hidden operations.

	/**
	 * Returns the next task to be performed, blocking until one is available.
	 *
	 * @return  Task ({@link java.lang.Runnable </TT>Runnable<TT>} object).
	 */
	synchronized Runnable getTask()
		{
		// Wait until a task is available.
		while (myTaskQueue.isEmpty())
			{
			try
				{
				++ myWaitCount;
				wait();
				}
			catch (InterruptedException exc)
				{
				}
			}

		// Return the first task.
		return (Runnable) myTaskQueue.removeFirst();
		}

	}
