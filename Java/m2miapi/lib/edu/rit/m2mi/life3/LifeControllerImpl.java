//******************************************************************************
//
// File:    LifeControllerImpl.java
// Package: edu.rit.m2mi.life3
// Unit:    Class edu.rit.m2mi.life3.LifeControllerImpl
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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Class LifeController encapsulates the UI for controlling the computation in
 * the Game of Life (version 3). It provides buttons for starting, stopping,
 * single-stepping, and resetting the computation. It also displays the number
 * of generations that have been computed.
 * <P>
 * During a round, when a cell has finished calculating its next state, the cell
 * calls {@link #ready() ready()} on the controller. When a UI button finishes
 * updating the display, the button also calls {@link #ready() ready()} on the
 * controller. When all cells and buttons have reported they're ready, the
 * controller decides based on which buttons have been pushed whether to start
 * the next round, and if so calls {@link LifeCell#next() next()} on all the
 * cells.
 * <P>
 * <B>M2MI Library Version 02-Jul-2002</B>
 *
 * @author  Alan Kaminsky
 * @version 23-May-2002
 */
public class LifeControllerImpl
	extends JPanel
	implements LifeController
	{

// Hidden constants.

	// Gap between UI widgets.
	private static final int GAP = 10;

// Hidden data members.

	// Life UI panel.
	private LifePanel myLifePanel;

	// Total number of objects that call ready() each round.
	private int myReadyTotal;

	// UI widgets.
	private JLabel myGenerationLabel;
	private JButton myStartButton;
	private JButton myStopButton;
	private JButton myStepButton;
	private JButton myResetButton;

	// Controller state.
	private static final int STOPPED  = 0;
	private static final int STARTED  = 1;
	private static final int STEPPING = 2;
	private static final int STOPPING = 3;
	private int myState = STOPPED;

	// Number of objects that have reported they're ready this round.
	private int myReadyCount = 0;

	// Number of generations.
	private int myGenerationCount = 0;

	// Omnihandle for interface LifeCell.
	private LifeCell allCells;

// Exported constructors.

	/**
	 * Construct a new Game of Life controller.
	 *
	 * @param  theLifePanel  UI panel for the cells.
	 * @param  rows          Number of rows; must be &gt;= 1.
	 * @param  cols          Number of columns; must be &gt;= 1.
	 *
	 * @exception  SynthesisException
	 *     Thrown if there was a problem synthesizing handle classes.
	 */
	public LifeControllerImpl
		(LifePanel theLifePanel,
		 int rows,
		 int cols)
		throws SynthesisException
		{
		super();

		myLifePanel = theLifePanel;
		myReadyTotal = 2 * rows * cols;

		setLayout (new BoxLayout (this, BoxLayout.Y_AXIS));

		myGenerationLabel = new JLabel();
		myGenerationLabel.setAlignmentX (0.5f);
		add (myGenerationLabel);
		displayGenerationCount();

		add (Box.createVerticalStrut (GAP));

		JPanel theButtonPanel = new JPanel();
		theButtonPanel.setLayout
			(new BoxLayout (theButtonPanel, BoxLayout.X_AXIS));
		add (theButtonPanel);

		myStartButton = new JButton ("Start");
		theButtonPanel.add (myStartButton);
		myStartButton.addActionListener
			(new ActionListener()
				{
				public void actionPerformed
					(ActionEvent e)
					{
					doStart();
					}
				});

		myStopButton = new JButton ("Stop");
		theButtonPanel.add (myStopButton);
		myStopButton.addActionListener
			(new ActionListener()
				{
				public void actionPerformed
					(ActionEvent e)
					{
					doStop();
					}
				});

		myStepButton = new JButton ("Step");
		theButtonPanel.add (myStepButton);
		myStepButton.addActionListener
			(new ActionListener()
				{
				public void actionPerformed
					(ActionEvent e)
					{
					doStep();
					}
				});

		myResetButton = new JButton ("Reset");
		theButtonPanel.add (myResetButton);
		myResetButton.addActionListener
			(new ActionListener()
				{
				public void actionPerformed
					(ActionEvent e)
					{
					doReset();
					}
				});

		enableButtons();

		allCells = (LifeCell) M2MI.getOmnihandle (LifeCell.class);
		}

// Exported operations inherited and implemented from interface LifeController.

	/**
	 * Report that a cell or UI button is ready to go on to the next round.
	 */
	public synchronized void ready()
		{
		++ myReadyCount;
		if (myReadyCount == myReadyTotal)
			{
			myReadyCount = 0;
			switch (myState)
				{
				case STARTED:
					++ myGenerationCount;
					displayGenerationCount();
					allCells.next();
					break;
				case STEPPING:
					myState = STOPPING;
					enableButtons();
					++ myGenerationCount;
					displayGenerationCount();
					allCells.next();
					break;
				case STOPPING:
					myState = STOPPED;
					myLifePanel.setEnabled (true);
					enableButtons();
					break;
				}
			}
		}

// Hidden operations.

	/**
	 * Take action when the "Start" button is pressed.
	 */
	private synchronized void doStart()
		{
		myState = STARTED;
		myLifePanel.setEnabled (false);
		enableButtons();
		allCells.start();
		}

	/**
	 * Take action when the "Stop" button is pressed.
	 */
	private synchronized void doStop()
		{
		myState = STOPPING;
		enableButtons();
		}

	/**
	 * Take action when the "Step" button is pressed.
	 */
	private synchronized void doStep()
		{
		myState = STEPPING;
		myLifePanel.setEnabled (false);
		enableButtons();
		allCells.start();
		}

	/**
	 * Take action when the "Reset" button is pressed.
	 */
	private synchronized void doReset()
		{
		myLifePanel.reset();
		myGenerationCount = 0;
		displayGenerationCount();
		}

	/**
	 * Display the generation count.
	 */
	private void displayGenerationCount()
		{
		myGenerationLabel.setText ("Generation " + myGenerationCount);
		}

	/**
	 * Enable or disable the UI buttons based on this Game of Life controller's
	 * state.
	 */
	private void enableButtons()
		{
		boolean iamStopped = myState == STOPPED;
		boolean iamStarted = myState == STARTED;
		myStartButton.setEnabled (iamStopped);
		myStopButton.setEnabled (iamStarted);
		myStepButton.setEnabled (iamStopped);
		myResetButton.setEnabled (iamStopped);
		}

	}
