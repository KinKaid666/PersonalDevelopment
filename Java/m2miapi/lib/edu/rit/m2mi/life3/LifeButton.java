//******************************************************************************
//
// File:    LifeButton.java
// Package: edu.rit.m2mi.life3
// Unit:    Class edu.rit.m2mi.life3.LifeButton
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
import edu.rit.m2mi.SynthesisException;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.SwingUtilities;

/**
 * Class LifeButton encapsulates the UI for one cell in the Game of Life
 * (version 3). The UI is a button. Clicking the button toggles the cell between
 * live and dead.
 * <P>
 * Each button is also a {@link LifeCellListener </CODE>LifeCellListener<CODE>}
 * that is part of the listener group for the corresponding cell. When the cell
 * reports its state, the button updates the display.
 * <P>
 * <B>M2MI Library Version 02-Jul-2002</B>
 *
 * @author  Alan Kaminsky
 * @version 23-May-2002
 */
public class LifeButton
	extends JButton
	implements LifeCellListener
	{

// Hidden constants.

	static final Color BORDER_COLOR = new Color (0.5f, 0.5f, 0.5f);
	private static final Dimension MIN_SIZE = new Dimension (20, 20);

// Hidden data members.

	// Cell corresponding to this UI button.
	private LifeCell myLifeCell;

	// Handle to the controller.
	private LifeController anyController;

// Exported constructors.

	/**
	 * Construct a new Game of Life cell UI button.
	 *
	 * @param  theLifeCell  Unihandle for the cell corresponding to this UI
	 *                      button.
	 *
	 * @exception  SynthesisException
	 *     Thrown if there was a problem synthesizing or instantiating a handle
	 *     class.
	 */
	public LifeButton
		(LifeCell theLifeCell)
		throws SynthesisException
		{
		super();

		myLifeCell = theLifeCell;
		anyController = (LifeController)
			M2MI.getOmnihandle (LifeController.class);

		setBorder (BorderFactory.createLineBorder (BORDER_COLOR, 1));
		setMinimumSize (MIN_SIZE);
		setPreferredSize (MIN_SIZE);
		setOpaque (true);
		setBackground (Color.white);

		addActionListener
			(new ActionListener()
				{
				public void actionPerformed
					(ActionEvent e)
					{
					boolean state = ! isSelected();
					setSelected (state);
					myLifeCell.setState (state);
					}
				});
		}

// Exported operations.

	/**
	 * Reset this Game of Life button to the dead state.
	 */
	public synchronized void reset()
		{
		setSelected (false);
		myLifeCell.setState (false);
		}

// Exported operations inherited and implemented from interface
// LifeCellListener.

	/**
	 * Receive the state of a cell at the beginning of a round. Use that
	 * information to update the display, and inform the controller when ready
	 * to go on to the next round.
	 *
	 * @param  state  State of the cell: true = live, false = dead.
	 */
	public synchronized void receiveState
		(boolean state)
		{
		if (isSelected() == state)
			{
			anyController.ready();
			}
		else
			{
			final boolean selected = state;
			SwingUtilities.invokeLater
				(new Runnable()
					{
					public void run()
						{
						setSelected (selected);
						anyController.ready();
						}
					});
			}
		}

// Hidden operations.

	/**
	 * Paint the state of this Game of Life button.
	 *
	 * @param  g  Graphics context.
	 */
	protected void paintComponent
		(Graphics g)
		{
		super.paintComponent (g);
		if (isSelected())
			{
			Dimension sz = getSize();
			Insets in = getInsets();
			int diam = Math.min
				(sz.width-in.left-in.right-2,
				 sz.height-in.top-in.bottom-2);
			Color oldColor = g.getColor();
			g.setColor (Color.red);
			g.fillOval ((sz.width-diam)/2, (sz.height-diam)/2, diam-1, diam-1);
			g.setColor (oldColor);
			}
		}

	}
