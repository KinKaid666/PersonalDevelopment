/**
 * This class is the window that is going to be poped up when 
 * 	the user tries to add from a file, but the file contains
 *	elements that already apear in our database
 *
 * @version $Id: AddListError.java,v 1.4 2000/05/08 01:47:11 p361-45a Exp $
 *
 * @author	Eric Ferguson
 *
 * Revisions:
 *		$Log: AddListError.java,v $
 *		Revision 1.4  2000/05/08 01:47:11  p361-45a
 *		expanded
 *
 *		Revision 1.3  2000/04/26 02:49:25  etf2954
 *		Fixed the size to make the text fit
 *
 *		Revision 1.2  2000/04/26 02:34:36  etf2954
 *		Wrote completely, looks okay, not really happy with it
 *
 *		Revision 1.1  2000/04/26 01:35:06  etf2954
 *		Initial revision
 *
 */

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.io.*;
import java.util.*;
import java.awt.Window;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumn;
import javax.swing.BorderFactory;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.text.*;

import java.awt.Dimension;
import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;

import java.awt.event.*;


public class AddListError extends JFrame {

	private JFrame frame;
	private JButton okButton = null;
	final String okButtonString = "Ok";
	private JPanel panel;
	private Dimension dimension = new Dimension( 370, 100 );
	private String errorMsg1;
	private String errorMsg2;
	private String titleStr;
	private JComboBox errantList;

	/**
	 * contructor that creats the window and displays it
	 *
	 * @param	roomSec	is a String that will appear in the window
	 *		telling the user what type of list error it is
	 * @param	roomSecVector	a vector that contains
	 *		all of the errant additions to the database!
	 */
	 public AddListError( String roomSec, LinkedList unAddable ) {

		// Create the msg's from the the first param
		errorMsg1 = "The follow " + roomSec + " couldn't be added";
		errorMsg2 = "because they already appear in our database";
		titleStr = roomSec + " Error";

		// Add those labels to the JLabels
		JLabel errorMsgLabel1 = new JLabel( errorMsg1 );
		JLabel errorMsgLabel2 = new JLabel( errorMsg2 );

		// Setup up the layout tool
		GridBagLayout gridBag = new GridBagLayout();
		GridBagConstraints c = new GridBagConstraints();

		// Setup the title in the frame
		frame = new JFrame( titleStr );
		frame.setResizable( true );
	
		panel = new JPanel();
		panel.setPreferredSize( dimension );
		panel.setLayout( gridBag );

		okButton = new JButton( okButtonString );

		//
		// Setup the list of all of the Section/Rooms that couldn't
		//	be added.
		//
		// Then add every string that appeared in our unAddable list
		errantList = new JComboBox();
		ListIterator iter = unAddable.listIterator( 0 );
		while( iter.hasNext() ) {
			errantList.addItem( ( String ) iter.next() );
		}

		// for the first string
		c.gridwidth = 3;
		c.gridx = 1;
		c.gridy = 0;
		c.anchor = c.WEST;
		gridBag.setConstraints( errorMsgLabel1, c );

		// for the second string
		c.gridwidth = 3;
		c.gridx = 1;
		c.gridy = 1;
		c.anchor = c.WEST;
		gridBag.setConstraints( errorMsgLabel2, c );

		// for the okay button
		c.gridx = 2;
		c.gridy = 2;
		c.anchor = c.CENTER;
		gridBag.setConstraints( okButton, c );

		// for the first string
		c.gridx = 4;
		c.gridy = 0;
		c.anchor = c.WEST;
		gridBag.setConstraints( errantList, c );

		//
		// Add all of the components to the panel
		panel.add( errorMsgLabel1 );
		panel.add( errorMsgLabel2 );
		panel.add( okButton );
		panel.add( errantList );

		//
		// Add an action listener to the okay button
		okButton.addActionListener( new ActionListener() {
		    public void actionPerformed( ActionEvent e ) {
			frame.setDefaultCloseOperation( DO_NOTHING_ON_CLOSE );
			    frame.dispose();
			} // actionPerformed
		}); // new ActionListener

		frame.getContentPane().add( panel, BorderLayout.CENTER );
		frame.pack();
		frame.setVisible( true );
	 } // Contructor
} // AddListError
