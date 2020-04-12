/**
  * This is the Upper  pannel in the upper right panel of the Bottom Right 
  * pannel.  
  *
  * Author: Adam Chipalowsky
  *
  * @version    $Id: BottomRightUpperUpperPanel.java,v 1.16 2000/05/06 15:17:07 etf2954 Exp $
  *
  * @author     
  *
  * Revisions:
  *     $Log: BottomRightUpperUpperPanel.java,v $
  *     Revision 1.16  2000/05/06 15:17:07  etf2954
  *     Added the new pics
  *
  *     Revision 1.15  2000/05/06 15:11:20  etf2954
  *     Added new pics
  *
  *     Revision 1.14  2000/05/06 14:35:25  aec1324
  *     added assign and unassign to the last two remaining
  *     tool bar buttons
  *
  *     Revision 1.13  2000/05/03 16:10:03  aec1324
  *     added sort calls when the table headers are
  *     clicked on
  *
  *     Revision 1.12  2000/05/03 03:09:48  pel2367
  *     added implementation to the buttons - load,
  *     open, save
  *
  *     Revision 1.11  2000/04/28 17:05:39  pel2367
  *     accepted an operator in constructor,
  *     wrote the undo() call from the button push.
  *
  *     Revision 1.10  2000/04/24 16:03:00  p361-45a
  *     finished method headers
  *
  *     Revision 1.9  2000/04/24 01:45:09  etf2954
  *     added cool images
  *
  *     Revision 1.8  2000/04/18 14:56:12  aec1324
  *     added new images to the toolbar
  *
  *     Revision 1.7  2000/04/14 17:46:20  etf2954
  *     Commented out the GUIevent lines
  *
  *     Revision 1.6  2000/04/07 13:10:24  etf2954
  *     Added the Operator class
  *
  *     Revision 1.5  2000/04/01 22:05:13  aec1324
  *     tweked the spacing
  *
  *     Revision 1.4  2000/03/31 18:43:51  aec1324
  *     added a simple toolbar that will have buttons for
  *     quick commands
  *
  *     Revision 1.3  2000/03/30 00:26:31  aec1324
  *     switched color back, resized to fit better
  *
  *     Revision 1.2  2000/03/29 21:45:49  aec1324
  *     resized to fit nicer with the frames around it
  *
  *     Revision 1.1  2000/03/29 02:05:20  aec1324
  *     Initial revision
  *
  */

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;

import javax.swing.JToolBar;
import javax.swing.JButton;
import javax.swing.ImageIcon;

import javax.swing.BorderFactory; 
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

public class BottomRightUpperUpperPanel extends JPanel {
    private Dimension dimen = new Dimension( 500, 90 );
    private Insets insets;
    private Border etched;
    private static Operator _operator;

    /**
     * constructor to create an object of type BottomRightUpperUpperPanel
     *
     */
    public BottomRightUpperUpperPanel( Operator oPerator ) {
    
        _operator = oPerator;
	//this.setBackground( Color.orange );
	this.setPreferredSize( dimen );
      
	//sets the space around the toolbar
	insets = new Insets( 10, 10, 10, 10 );
	
	//create the border for the toolBar
	etched = BorderFactory.createEtchedBorder();

	//Create the toolbar.
        JToolBar toolBar = new JToolBar();
        addButtons(toolBar);

	//turn off floating toolbar
	toolBar.setFloatable(false);
	toolBar.setMargin( insets );
	
	//add the toolBar to the frame
	this.add(toolBar, BorderLayout.CENTER);
       
    }

    /**
     * method to add buttons to the toolbar
     *
     * @param toolBar a value of type 'JToolBar'
     */
    protected void addButtons(JToolBar toolBar) {
        JButton button = null;

        //first button
        button = new JButton(new ImageIcon("images/New24.gif"));
        button.setToolTipText("New Database");
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                _operator.newState();
            }
        });
        toolBar.add(button);

        //second button
        button = new JButton(new ImageIcon("images/Open24.gif"));
        button.setToolTipText("Open Database");
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                _operator.loadState();
		
            }
        });
        toolBar.add(button);

        //third button
        button = new JButton(new ImageIcon("images/Save24.gif"));
        button.setToolTipText("Save Database");
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                _operator.saveState();
            }
        });
        toolBar.add(button);

	//fourth button
	button = new JButton(new ImageIcon("images/Undo24.gif"));
        button.setToolTipText("Undo last action");
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                _operator.undo();
            }
        });
        toolBar.add(button);
	
	//fifth button
	button = new JButton(new ImageIcon("images/Redo24.gif"));
        button.setToolTipText("Redo Last Action");
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                _operator.redo();
            }
        });
        toolBar.add(button);

	//sixth button
	button = new JButton(new ImageIcon("images/Add24.gif"));
        button.setToolTipText("Assign section to a room");
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
		_operator.assign();
                
            }
        });
        toolBar.add(button);
	
	//seventh button
	button = new JButton(new ImageIcon("images/Remove24.gif"));
        button.setToolTipText("Unassign section from room");
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                _operator.unassign();
            }
        });
        toolBar.add(button);



    }



}
