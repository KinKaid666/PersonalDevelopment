//******************************************************************************
//
// File:    ChatFrame.java
// Package: edu.rit.m2mi.test
// Unit:    Class edu.rit.m2mi.test.ChatFrame
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

package edu.rit.m2mi.test;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import java.util.Iterator;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

/**
 * Class ChatFrame encapsulates the UI for a rudimentary M2MI-based chat
 * application.
 * <P>
 * <B>M2MI Library Version 02-Jul-2002</B>
 *
 * @author  Alan Kaminsky
 * @version 01-Jul-2002
 */
public class ChatFrame
	extends JFrame
	{

// Hidden constants.

	private static final int GAP = 10;

// Hidden data members.

	private JScrollPane myScrollPane;
	private JTextArea myChatLog;
	private JTextField myMessageField;
	private JButton mySendButton;

	// Vector of registered ChatFrameListeners.
	private Vector myListeners = new Vector();

// Exported constructors.

	/**
	 * Construct a new chat frame.
	 *
	 * @param  title     Title.
	 */
	public ChatFrame
		(String title)
		{
		super (title);

		JPanel theMainPanel = new JPanel();
		theMainPanel.setBorder
			(BorderFactory.createEmptyBorder (GAP, GAP, GAP, GAP));
		theMainPanel.setLayout
			(new BoxLayout (theMainPanel, BoxLayout.Y_AXIS));

		myChatLog = new JTextArea (12, 40);
		myChatLog.setEditable (false);
		myChatLog.setLineWrap (true);
		myChatLog.setWrapStyleWord (true);
		myScrollPane = new JScrollPane
			(myChatLog,
			 JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
			 JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		myScrollPane.setAlignmentX (0.0f);
		theMainPanel.add (myScrollPane);
		theMainPanel.add (Box.createVerticalStrut (GAP));

		JPanel theEntryPanel = new JPanel();
		theEntryPanel.setLayout
			(new BoxLayout (theEntryPanel, BoxLayout.X_AXIS));
		theEntryPanel.setAlignmentX (0.0f);

		myMessageField = new JTextField (40);
		myMessageField.addActionListener
			(new ActionListener()
				{
				public void actionPerformed
					(ActionEvent e)
					{
					doSend();
					}
				});
		theEntryPanel.add (myMessageField);
		theEntryPanel.add (Box.createHorizontalGlue());

		mySendButton = new JButton ("Send");
		mySendButton.addActionListener
			(new ActionListener()
				{
				public void actionPerformed
					(ActionEvent e)
					{
					doSend();
					}
				});
		theEntryPanel.add (Box.createHorizontalStrut (GAP));
		theEntryPanel.add (mySendButton);

		theMainPanel.add (theEntryPanel);

		getContentPane().add (theMainPanel);

		addWindowListener
			(new WindowAdapter()
				{
				public void windowClosing
					(WindowEvent e)
					{
					dispose();
					}
				public void windowClosed
					(WindowEvent e)
					{
					System.exit (0);
					}
				});
		pack();
		setVisible (true);
		/*
		** Removed by etf2954
		**
		myMessageField.requestFocusInWindow();
		*/
		}


// Exported operations.

	/**
	 * Register the given chat frame listener. Henceforth, whenever the user
	 * sends a line of text in this chat frame, the given chat frame listener's
	 * {@link ChatFrameListener#send(String) send()} method will be called with
	 * the line of text.
	 *
	 * @param  theListener  Chat frame listener to add.
	 *
	 * @exception  NullPointerException
	 *     (unchecked exception) Thrown if <TT>theListener</TT> is null.
	 */
	public synchronized void addListener
		(ChatFrameListener theListener)
		{
		if (theListener == null)
			{
			throw new NullPointerException();
			}
		myListeners.add (theListener);
		}

	/**
	 * Deregister the given chat frame listener. Henceforth, whenever the user
	 * sends a line of text in this chat frame, the given chat frame listener's
	 * {@link ChatFrameListener#send(String) send()} method will not be called
	 * with the line of text. If <TT>theListener</TT> is not already registered,
	 * <TT>removeListener()</TT> does nothing.
	 *
	 * @param  theListener  Chat frame listener to remove.
	 */
	public synchronized void removeListener
		(ChatFrameListener theListener)
		{
		myListeners.remove (theListener);
		}

	/**
	 * Add the given line of text to the end of this chat frame's chat log.
	 *
	 * @param  line  Line of text.
	 */
	public synchronized void addLineToLog
		(String line)
		{
		if (line != null)
			{
			String theText = myChatLog.getText();
			if (theText == null || theText.length() == 0)
				{
				theText = line;
				}
			else
				{
				theText = theText + "\n" + line;
				}
			final String newText = theText;
			SwingUtilities.invokeLater
				(new Runnable()
					{
					public void run()
						{
						myChatLog.setText (newText);
						JScrollBar scrollbar =
							myScrollPane.getVerticalScrollBar();
						scrollbar.setValue (scrollbar.getMaximum());
						}
					}
				);
			}
		}

// Hidden operations.

	/**
	 * Take action when the Send button is pressed.
	 */
	private synchronized void doSend()
		{
		String line = myMessageField.getText();
		if (line != null && line.length() > 0)
			{
			Iterator iter = myListeners.iterator();
			while (iter.hasNext())
				{
				((ChatFrameListener) (iter.next())) .send (line);
				}
			myMessageField.setText ("");
			}
		/*
		** Removed by etf2954
		**
		myMessageField.requestFocusInWindow();
		*/
		}

	}
