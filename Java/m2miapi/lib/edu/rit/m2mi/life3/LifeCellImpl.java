//******************************************************************************
//
// File:    LifeCellImpl.java
// Package: edu.rit.m2mi.life3
// Unit:    Interface edu.rit.m2mi.life3.LifeCellImpl
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

package edu.rit.m2mi.life3;

import edu.rit.m2mi.M2MI;
import edu.rit.m2mi.Multihandle;
import edu.rit.m2mi.SynthesisException;

/**
 * Class LifeCellImpl encapsulates a cell in the Game of Life (version 3).
 * <P>
 * Each cell has a <B>listener group</B> represented by a multihandle for
 * interface {@link LifeCellListener </CODE>LifeCellListener<CODE>}. Attached to
 * this multihandle are the eight neighboring cells (but not the cell itself),
 * plus the cell's UI button. At the start of a round, each cell reports its
 * state to its listener group by invoking {@link
 * LifeCellListener#receiveState(boolean) receiveState()} on the multihandle.
 * <P>
 * <B>M2MI Library Version 02-Jul-2002</B>
 *
 * @author  Alan Kaminsky
 * @version 23-May-2002
 */
public class LifeCellImpl
	implements LifeCell
	{

// Hidden data members.

	// Number of neighboring cells.
	private int myNeighborCount = 0;

	// Current state of this cell.
	private boolean myState = false;

	// Number of invocations of start(), next(), and receiveState() that have
	// occurred so far this round.
	private int myRoundCount = 0;

	// Number of live neighboring cells this round.
	private int myLiveCount = 0;

	// State of this cell for the next round.
	private boolean myNextState = false;

	// Listener group.
	private LifeCellListener myListenerGroup;

	// Handle to the controller.
	private LifeController anyController;

// Exported constructors.

	/**
	 * Construct a new Game of Life cell. It will report its state to the given
	 * listener group.
	 *
	 * @param  theListenerGroup  Multihandle for interface LifeCellListener.
	 *
	 * @exception  SynthesisException
	 *     Thrown if there was a problem synthesizing or instantiating a handle
	 *     class.
	 */
	public LifeCellImpl
		(Multihandle theListenerGroup)
		throws SynthesisException
		{
		myListenerGroup = (LifeCellListener) theListenerGroup;
		anyController = (LifeController)
			M2MI.getOmnihandle (LifeController.class);
		}

// Exported operations.

	/**
	 * Tell this cell to listen to cell state reports sent to the given listener
	 * group.
	 *
	 * @param  theListenerGroup  Multihandle for interface LifeCellListener.
	 */
	public void listenTo
		(Multihandle theListenerGroup)
		{
		++ myNeighborCount;
		theListenerGroup.attach (this);
		}

// Exported operations inherited and implemented from interface
// LifeCellListener.

	/**
	 * Receive the state of a cell at the beginning of a round. Use that
	 * information to calculate the next state of this cell, and inform the
	 * controller when ready to go on to the next round.
	 *
	 * @param  state  State of the cell: true = live, false = dead.
	 */
	public synchronized void receiveState
		(boolean state)
		{
		if (state) ++ myLiveCount;
		++ myRoundCount;
		computeNextState();
		}

// Exported operations inherited and implemented from interface LifeCell.

	/**
	 * Set the state of this cell. Do not report the state to this cell's
	 * listeners.
	 *
	 * @param  state  State of the cell: true = live, false = dead.
	 */
	public synchronized void setState
		(boolean state)
		{
		myState = state;
		}

	/**
	 * Start a round. Report this cell's state to this cell's listeners.
	 */
	public synchronized void start()
		{
		myListenerGroup.receiveState (myState);
		++ myRoundCount;
		computeNextState();
		}

	/**
	 * Finish a round and start the next round. Set the state of this cell to
	 * its previously calculated next state and report the new state to this
	 * cell's listeners.
	 */
	public synchronized void next()
		{
		myState = myNextState;
		myListenerGroup.receiveState (myState);
		++ myRoundCount;
		computeNextState();
		}

// Hidden operations.

	/**
	 * Compute the next state. If all the necessary method invocations have
	 * happened this round -- either start() or next(), plus receiveState() from
	 * each neighboring cell -- then compute and remember the next state and
	 * report ready to the controller.
	 */
	private void computeNextState()
		{
		if (myRoundCount == myNeighborCount + 1)
			{
			myNextState =
				(myLiveCount == 3) ||
				(myLiveCount == 2 && myState);
			myRoundCount = 0;
			myLiveCount = 0;
			anyController.ready();
			}
		}

	}
