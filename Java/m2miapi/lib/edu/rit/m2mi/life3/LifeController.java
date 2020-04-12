//******************************************************************************
//
// File:    LifeController.java
// Package: edu.rit.m2mi.life3
// Unit:    Interface edu.rit.m2mi.life3.LifeController
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
 * Interface LifeController specifies the interface for an object that controls
 * the computation in the Game of Life (version 3).
 * <P>
 * During each round, when a cell has finished calculating its next state, the
 * cell calls {@link #ready() ready()} on the controller. When a cell UI button
 * has finished updating the display, it also calls {@link #ready() ready()} on
 * the controller. When all cells and buttons have reported they're ready, the
 * controller decides whether to start the next round, and if so calls {@link
 * LifeCell#next() next()} on all the cells.
 * <P>
 * <B>M2MI Library Version 02-Jul-2002</B>
 *
 * @author  Alan Kaminsky
 * @version 22-May-2002
 */
public interface LifeController
	{

// Exported operations.

	/**
	 * Report that a cell or UI button is ready to go on to the next round.
	 */
	public void ready();

	}
