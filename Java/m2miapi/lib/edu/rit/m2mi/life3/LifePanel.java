//******************************************************************************
//
// File:    LifePanel.java
// Package: edu.rit.m2mi.life3
// Unit:    Class edu.rit.m2mi.life3.LifePanel
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

import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

/**
 * Class LifePanel encapsulates the UI for a grid of cells in the Game of Life
 * (version 3). It is a rectangular array of {@link LifeButton
 * </CODE>LifeButton<CODE>}s.
 * <P>
 * <B>M2MI Library Version 02-Jul-2002</B>
 *
 * @author  Alan Kaminsky
 * @version 23-May-2002
 */
public class LifePanel
	extends JPanel
	{

// Hidden data members.

	private int myRows;
	private int myCols;
	private LifeButton[][] myButtons;

// Exported constructors.

	/**
	 * Construct a new Game of Life panel with the given buttons.
	 * <TT>theButtons</TT> must be a fully initialized two-dimensional array of
	 * type {@link LifeButton </CODE>LifeButton<CODE>} with the proper number of
	 * rows (first dimension) and columns (second dimension).
	 *
	 * @param  theButtons  Array of buttons.
	 */
	public LifePanel
		(LifeButton[][] theButtons)
		{
		super();

		myRows = theButtons.length;
		myCols = theButtons[0].length;
		myButtons = theButtons;

		setLayout (new GridLayout (myRows, myCols, 0, 0));
		setBorder (BorderFactory.createLineBorder (LifeButton.BORDER_COLOR, 1));
		setAlignmentX (0.5f);

		for (int r = 0; r < myRows; ++ r)
			{
			for (int c = 0; c < myCols; ++ c)
				{
				add (myButtons[r][c]);
				}
			}
		}

// Exported operations.

	/**
	 * Enable or disable all the Game of Life buttons from accepting manual
	 * changes.
	 *
	 * @param  enabled  True to enable, false to disable.
	 */
	public void setEnabled
		(boolean enabled)
		{
		for (int r = 0; r < myRows; ++ r)
			{
			for (int c = 0; c < myCols; ++ c)
				{
				myButtons[r][c].setEnabled (enabled);
				}
			}
		}

	/**
	 * Reset all the Game of Life buttons to the dead state.
	 */
	public void reset()
		{
		for (int r = 0; r < myRows; ++ r)
			{
			for (int c = 0; c < myCols; ++ c)
				{
				myButtons[r][c].reset();
				}
			}
		}

	}
