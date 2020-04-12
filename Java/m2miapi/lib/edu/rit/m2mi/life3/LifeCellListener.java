//******************************************************************************
//
// File:    LifeCellListener.java
// Package: edu.rit.m2mi.life3
// Unit:    Interface edu.rit.m2mi.life3.LifeCellListener
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

/**
 * Interface LifeCellListener specifies the interface for an exported object
 * that listens to cell state reports in the Game of Life (version 3).
 * <P>
 * <B>M2MI Library Version 02-Jul-2002</B>
 *
 * @author  Alan Kaminsky
 * @version 22-May-2002
 */
public interface LifeCellListener
	{

// Exported operations.

	/**
	 * Receive the state of a cell at the beginning of a round. Use that
	 * information as appropriate, e.g. to calculate the next state of this cell
	 * or to update the display, and inform the controller when ready to go on
	 * to the next round.
	 *
	 * @param  state  State of the cell: true = live, false = dead.
	 */
	public void receiveState
		(boolean state);

	}
