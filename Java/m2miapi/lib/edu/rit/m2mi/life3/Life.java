//******************************************************************************
//
// File:    Life.java
// Package: edu.rit.m2mi.life3
// Unit:    Class edu.rit.m2mi.life3.Life
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

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * Class Life is the main program for the Game of Life (version 3).
 * <P>
 * Usage: java edu.rit.m2mi.life3.Life [-p] <I>rows cols</I>
 * <BR>
 * -p: Use planar topology instead of toroidal topology
 * <P>
 * <B>M2MI Library Version 02-Jul-2002</B>
 *
 * @author  Alan Kaminsky
 * @version 23-May-2002
 */
public class Life
	{

	private static final int GAP = 10;

	/**
	 * Game of Life main program.
	 */
	public static void main
		(String[] args)
		{
		int exitCode = 0;
		int r, pr, nr, c, pc, nc;

		try
			{
			// Validate command line arguments.
			if (2 > args.length || args.length > 3)
				{
				System.err.println ("Usage: java edu.rit.m2mi.life3.Life [-p] <rows> <cols>");
				System.err.println ("-p: Use planar topology instead of toroidal topology");
				exitCode = 1;
				return;
				}
			int argn = 0;

			boolean toroidal = true;
			if (args[argn].equals ("-p"))
				{
				toroidal = false;
				++ argn;
				}

			int rows = 0;
			try
				{
				rows = Integer.parseInt (args[argn]);
				if (rows < 3)
					{
					throw new NumberFormatException();
					}
				++ argn;
				}
			catch (NumberFormatException exc)
				{
				System.err.println ("Life: <rows> must be an integer >= 3");
				exitCode = 1;
				return;
				}

			int cols = 0;
			try
				{
				cols = Integer.parseInt (args[argn]);
				if (cols < 3)
					{
					throw new NumberFormatException();
					}
				++ argn;
				}
			catch (NumberFormatException exc)
				{
				System.err.println ("Life: <cols> must be an integer >= 3");
				exitCode = 1;
				return;
				}

			// Initialize the M2MI layer.
			M2MI.initialize (1234L, null);

			// Set up cell listener groups.
			Multihandle[][] theListenerGroups = new Multihandle [rows] [];
			for (r = 0; r < rows; ++ r)
				{
				theListenerGroups[r] = new Multihandle [cols];
				for (c = 0; c < cols; ++ c)
					{
					theListenerGroups[r][c] =
						M2MI.getMultihandle (LifeCellListener.class);
					}
				}

			// Set up cells and UI buttons.
			LifeButton[][] theButtons = new LifeButton [rows] [];
			for
				(r = 0, pr = rows-1, nr = 1;
				 r < rows;
				 ++ r, pr = (pr+1)%rows, nr = (nr+1)%rows)
				{
				theButtons[r] = new LifeButton [cols];
				for
					(c = 0, pc = cols-1, nc = 1;
					 c < cols;
					 ++ c, pc = (pc+1)%cols, nc = (nc+1)%cols)
					{
					// Set up cell, telling it which listener group to report
					// to.
					LifeCellImpl theCell =
						new LifeCellImpl (theListenerGroups[r][c]);

					// Attach cell to each neighboring cell's listener group,
					// depending on the topology (planar or toroidal).
					if (toroidal || (pr < r && pc < c))
						{
						theCell.listenTo (theListenerGroups[pr][pc]);
						}
					if (toroidal || (pr < r))
						{
						theCell.listenTo (theListenerGroups[pr][c ]);
						}
					if (toroidal || (pr < r && c < nc))
						{
						theCell.listenTo (theListenerGroups[pr][nc]);
						}
					if (toroidal || (pc < c))
						{
						theCell.listenTo (theListenerGroups[r ][pc]);
						}
					if (toroidal || (c < nc))
						{
						theCell.listenTo (theListenerGroups[r ][nc]);
						}
					if (toroidal || (r < nr && pc < c))
						{
						theCell.listenTo (theListenerGroups[nr][pc]);
						}
					if (toroidal || (r < nr))
						{
						theCell.listenTo (theListenerGroups[nr][c ]);
						}
					if (toroidal || (r < nr && c < nc))
						{
						theCell.listenTo (theListenerGroups[nr][nc]);
						}

					// Set up button, giving it a unihandle to the cell.
					theButtons[r][c] =
						new LifeButton
							((LifeCell) M2MI.getUnihandle
								(theCell,
								 LifeCell.class));

					// Attach button to the cell's listener group.
					theListenerGroups[r][c].attach (theButtons[r][c]);
					}
				}

			// Set up panel with all UI buttons.
			LifePanel theLifePanel = new LifePanel (theButtons);

			// Set up controller.
			LifeControllerImpl theLifeController =
				new LifeControllerImpl (theLifePanel, rows, cols);
			M2MI.export (theLifeController, LifeController.class);

			// Set up main UI frame.
			final JFrame theFrame = new JFrame ("edu.rit.m2mi.life3.Life");
			JPanel theMainPanel = new JPanel();
			theMainPanel.setLayout
				(new BoxLayout (theMainPanel, BoxLayout.Y_AXIS));
			theMainPanel.setBorder
				(BorderFactory.createEmptyBorder (GAP, GAP, GAP, GAP));

			theMainPanel.add (theLifePanel);
			theMainPanel.add (Box.createVerticalStrut (GAP));
			theMainPanel.add (theLifeController);

			theFrame.getContentPane().add (theMainPanel);
			theFrame.addWindowListener
				(new WindowAdapter()
					{
					public void windowClosing
						(WindowEvent e)
						{
						theFrame.dispose();
						System.exit (0);
						}
					});
			theFrame.pack();
			theFrame.setVisible (true);

			Thread.currentThread().join();
			}

		catch (Throwable exc)
			{
			System.err.println ("Life: Uncaught exception");
			exc.printStackTrace (System.err);
			exitCode = 1;
			}

		finally
			{
			System.exit (exitCode);
			}
		}

	}
