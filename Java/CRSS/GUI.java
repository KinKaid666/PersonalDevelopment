/**
 * This is the main GUI.  It creates all the panels and gives access to thoes
 * panels for another class that may need to do such things as update tables
 * or what have you. 
 *
 *
 * @version	$Id: GUI.java,v 1.55 2000/05/08 16:25:22 p361-45a Exp $
 *
 * @author	Adam E Chipalowsky 
 * @author  Phil Light
 *
 * Revisions:
 *		$Log: GUI.java,v $
 *		Revision 1.55  2000/05/08 16:25:22  p361-45a
 *		fixed formatting
 *
 *		Revision 1.54  2000/05/08 02:16:20  aec1324
 *		traced an error through this class
 *
 *		Revision 1.53  2000/05/06 17:48:01  etf2954
 *		took out System.out's
 *
 *		Revision 1.52  2000/05/05 17:19:43  aec1324
 *		got the exit via corner box to ask for saving by
 *		calling the exit action in the operator
 *
 *		Revision 1.51  2000/05/05 00:43:44  aec1324
 *		changed the message text in exit confirm win
 *
 *		Revision 1.50  2000/05/05 00:42:34  aec1324
 *		added a slight change to the exit confirm win
 *
 *		Revision 1.49  2000/05/05 00:33:39  pel2367
 *		threw some new BadActionExceptions
 *
 *		Revision 1.48  2000/05/05 00:11:57  pel2367
 *		called a System.exit( 0 );
 *
 *		Revision 1.47  2000/05/05 00:06:16  aec1324
 *		added a exit program confirm window
 *
 *		Revision 1.46  2000/05/03 05:19:44  pel2367
 *		added highLightSection functionality
 *
 *		Revision 1.45  2000/05/03 01:50:02  pel2367
 *		moved the datafields sortStyle to their
 *		respective panels... modified accessors/mutators
 *		appropriately
 *
 *		Revision 1.44  2000/05/02 17:07:05  etf2954
 *		Added a setHiLightedRoom() function
 *
 *		Revision 1.43  2000/05/02 16:51:56  aec1324
 *		added another popupWindow to alert the
 *		user that some attributes no longer
 *		fit
 *
 *		Revision 1.42  2000/05/01 20:15:33  aec1324
 *		added another popup win for a alert when the user
 *		edits a room and the capacit no longer matches
 *		with what the room needs
 *
 *		Revision 1.41  2000/04/29 19:26:06  aec1324
 *		thought about adding another popupwindow just for the
 *		edit section but then decided against it
 *
 *		Revision 1.40  2000/04/28 21:49:58  aec1324
 *		added yet another popup window for the edit section
 *		to use when the user edits the attributes and ther
 *		current room that it is already assigned to
 *		cannot support thoes attributes
 *
 *		Revision 1.39  2000/04/28 21:23:39  pel2367
 *		added the sortStates
 *
 *		Revision 1.38  2000/04/28 20:56:37  aec1324
 *		added another popup window that allows us to
 *		alert the user that an assignment has been
 *		desreguarded and he/she should re assign
 *		at some point
 *
 *		Revision 1.37  2000/04/28 20:31:14  pel2367
 *		added unHighlightSections and unHighlightRooms
 *
 *		Revision 1.36  2000/04/28 14:39:15  etf2954
 *		Added a popup window to confirm room deletion
 *		if the room has assignments in it
 *
 *		Revision 1.35  2000/04/28 01:24:57  pel2367
 *		reworked the prompt for assignment override
 *		method
 *
 *		Revision 1.34  2000/04/28 00:48:38  pel2367
 *		promptForConfirmation is now
 *		promptForAssignConfirmation, and is rewritten
 *		accordingly.
 *
 *		Revision 1.33  2000/04/26 20:22:26  aec1324
 *		started looking at possable revisions for the
 *		edit section call
 *
 *		Revision 1.32  2000/04/26 06:11:34  cmb3548
 *		Removed rainbow influence on GUI
 *
 *		Revision 1.31  2000/04/25 21:18:01  aec1324
 *		got rid of a method called popupAddSectionWin
 *		cause we don't use it anymore
 *
 *		Revision 1.30  2000/04/24 23:33:17  aec1324
 *		made sure the refresh routine is correct in
 *		sending it a boolean array for the section list
 *
 *		Revision 1.29  2000/04/24 16:45:22  p361-45a
 *		finished method headers
 *
 *		Revision 1.28  2000/04/24 00:33:47  pel2367
 *		reworked getHighlighted Sect and Room.
 *
 *		Revision 1.27  2000/04/23 22:32:28  pel2367
 *		rewrote getHighlightedRoom()
 *
 *		Revision 1.26  2000/04/23 20:36:55  pel2367
 *		added return statements so that it would
 *		compile.
 *
 *		Revision 1.25  2000/04/23 20:03:26  pel2367
 *		still working on trying to find the selected
 *		room.
 *
 *		Revision 1.24  2000/04/23 18:16:31  pel2367
 *		worked on highlighting stuff.
 *
 *		Revision 1.23  2000/04/22 22:05:36  pel2367
 *		wrote getHighlightedRoom()
 *
 *		Revision 1.22  2000/04/22 19:38:00  pel2367
 *		wrote a skeleton for setHighlightedSection.
 *
 *		Revision 1.21  2000/04/22 16:23:00  aec1324
 *		added a method call to get what is highlighted
 *
 *		Revision 1.20  2000/04/22 00:51:39  aec1324
 *		fixed the interface to the updating room
 *		attributes
 *
 *		Revision 1.19  2000/04/20 16:07:53  p361-45a
 *		finished method headers
 *
 *		Revision 1.18  2000/04/20 02:27:38  aec1324
 *		corrected a method name
 *
 *		Revision 1.17  2000/04/19 20:43:58  aec1324
 *		stuff
 *
 *		Revision 1.16  2000/04/18 22:09:24  aec1324
 *		got rid of testing information
 *
 *		Revision 1.15  2000/04/15 21:15:59  aec1324
 *		added more methods for popup error and
 *		conformation messages
 *
 *		Revision 1.14  2000/04/14 02:51:42  etf2954
 *		Added the new "Export" menu
 *
 *		Revision 1.13  2000/04/13 02:22:57  aec1324
 *		added a new popup window
 *
 *		Revision 1.12  2000/04/13 01:59:54  etf2954
 *		Tried to change the look and feel
 *
 *		Revision 1.11  2000/04/13 00:44:20  aec1324
 *		tried to add a disable method, then deleted it
 *
 *		Revision 1.10  2000/04/09 20:31:38  aec1324
 *		FOR TESTING ONLY: told the operator about the
 *		gui
 *
 *		Revision 1.9  2000/04/08 22:47:28  aec1324
 *		added the ability to open a editSectionWindow
 *
 *		Revision 1.8  2000/04/07 16:48:08  aec1324
 *		added the ability to create and open an Edit room
 *		window
 *
 *		Revision 1.7  2000/04/07 13:10:24  etf2954
 *		Added the Operator class
 *
 *		Revision 1.6  2000/04/06 05:47:29  etf2954
 *		Gave the class access to GUIinterface
 *
 *		Revision 1.5  2000/04/05 17:42:38  aec1324
 *		added more interface methods
 *
 *		Revision 1.4  2000/04/02 18:56:23  aec1324
 *		added access to the Left Panel via a method
 *
 *		
 *		
 *
 */


import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import javax.swing.JOptionPane;
import java.beans.*; //Property change stuff

public class GUI extends JFrame {
    private JMenuBar spifMenu;
    private FileMenu fileMenu;
    private EditMenu editMenu;
    private ViewMenu viewMenu;
    private ExportMenu exportMenu;
    private HelpMenu helpMenu;
    private TopLeftPanel topLeftPanel;
    private TopRightPanel topRightPanel;
    private BottomLeftPanel bottomLeftPanel;
    private BottomRightPanel bottomRightPanel;
    private GridLayout gridLayout;
    private static Operator operator;
    private int _sectSort;
    private int _roomSort;
    
    
    /**
     * constructor to create an object of type GUI
     *
     * @param oPerator copy of the 'Operator'
     */
    public GUI( Operator oPerator ) {
	
	operator = oPerator;
	spifMenu = new JMenuBar();
	fileMenu = new FileMenu( this, operator );
	editMenu = new EditMenu( this, operator );
	viewMenu = new ViewMenu( this, operator );
	exportMenu = new ExportMenu( this, operator );
	helpMenu = new HelpMenu( this, operator);
	topLeftPanel = new TopLeftPanel( this, operator );
	topRightPanel = new TopRightPanel( this, operator );
	bottomLeftPanel = new BottomLeftPanel( this, operator );
	bottomRightPanel = new BottomRightPanel( operator );

	gridLayout = new GridLayout( 2, 2 );
	gridLayout.setHgap( 15 );
	gridLayout.setVgap( 15 );
	this.getContentPane().setLayout( gridLayout );
	this.setTitle( "SpifBall's Course Room Scheduler (The A-Team)");
	fileMenu.setMnemonic( 'F' );
	spifMenu.add( fileMenu );
	this.setJMenuBar( spifMenu );
	editMenu.setMnemonic( 'E' );
	spifMenu.add( editMenu );
	viewMenu.setMnemonic( 'V' );
	spifMenu.add( viewMenu );
	exportMenu.setMnemonic( 'X' );
	spifMenu.add( exportMenu );
	helpMenu.setMnemonic( 'H' );
	spifMenu.add( helpMenu );
	
	this.getContentPane().add( topLeftPanel );
	this.getContentPane().add( topRightPanel );
	this.getContentPane().add( bottomLeftPanel );
	this.getContentPane().add( bottomRightPanel );
	/*
	 *
	 * Now the program will exit when you hit the 'X'
	 *
	 */
	this.addWindowListener(new WindowAdapter(){
	    public void windowClosing( WindowEvent event ) {
		operator.exit();
		
	    }// window Closing
	}/* WindowAdapter */
	    
	);

	/**
	 * set it so the user cannot resize the frame
	 *
	 * @param nil a value of type 'false'
	 */
	setResizable( false );

    }//GUI
   
    /**
     * method to open confirmation pane to ask if the
     * 	user would like to continue deleting the room
     * 	even though it has assignments
     *
     * @param parent is the action that is calling this confirm dialog
     * @exception BadActionException if an error occurs
     */
    public void promptConfirmOnRemoveRoomAction( RemRoomAction parent )
        throws BadActionException{
		final JOptionPane optionPane = new JOptionPane(
				  "The room you are about to remove contains"+
				  " assigned \n" + 
				  "sections in it. \n" +
				  "Proceed?" ,
				  JOptionPane.QUESTION_MESSAGE,
				  JOptionPane.YES_NO_OPTION);
		
		final JDialog dialog = new JDialog(this, "Confirm", true);
		dialog.setContentPane(optionPane);
		
		optionPane.addPropertyChangeListener(
					       new PropertyChangeListener() {
		                                                       
		    /**
		     * method to change the property 
		     *
		     * @param e the event to change
		     */
		    public void propertyChange(PropertyChangeEvent e) {
			String prop = e.getPropertyName();
			
			if (dialog.isVisible() 
			    && (e.getSource() == optionPane)
			    && (prop.equals(JOptionPane.VALUE_PROPERTY) ||
				prop.equals(JOptionPane.INPUT_VALUE_PROPERTY))){
			
				dialog.setVisible(false);
			}
			}
		});
		
		//set up and make the window visable
		dialog.pack();
		dialog.setLocationRelativeTo(this);
		dialog.setVisible(true);
		
		int value = ((Integer)optionPane.getValue()).intValue();
		if (value == JOptionPane.YES_OPTION) {
		    parent.setQuery( true );
		    parent.go();
		} else if (value == JOptionPane.NO_OPTION) {
		    //if no was pressed, do something else
		    dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE );
		    throw new BadActionException();
		}
		
		//non-auto-closing dialog with custom message area
		//NOTE: if you don't intend to check the input,
		//then just use showInputDialog instead.

    } // promptConfirmOnRemoveRoomAction

    /**
     * this popups a "are you sure" window
     * this method should return some sort of conformation
     *
     * @param ass used to confirm specific assumption
     * @exception BadActionException if an error occurs
     */
    public void askForAssignConfirmation( AssignAction ass )
        throws BadActionException{
    	
	/**
	 * method to open confirmation pane
	 *
	 * @param selected a value of type '"The'
	 * @param nil a value of type 'JOptionPane.QUESTION_MESSAGE'
	 * @param nil a value of type 'JOptionPane.YES_NO_OPTION'
	 * @return a value of type 'JOptionPane'
	 */
	final JOptionPane optionPane = new JOptionPane(
			  "The selected room does not have \n" + 
              "the required add-ons. \n" +
              "Proceed?" ,
			  JOptionPane.QUESTION_MESSAGE,
			  JOptionPane.YES_NO_OPTION);
	
	/**
	 * method to open dialog pane
	 *
	 * @param nil a value of type 'this'
	 * @param you a value of type '"What's'
	 * @param nil a value of type 'true'
	 * @return a value of type 'JDialog'
	 */
	final JDialog dialog = new JDialog(this, "Confirm",true);
	dialog.setContentPane(optionPane);
	
	optionPane.addPropertyChangeListener(new PropertyChangeListener() {
	    /**
	     * change to new property
	     *
	     * @param e a value of type 'PropertyChangeEvent'
	     */
	    public void propertyChange(PropertyChangeEvent e) {
		String prop = e.getPropertyName();
		
		if (dialog.isVisible() 
		    && (e.getSource() == optionPane)
		    && (prop.equals(JOptionPane.VALUE_PROPERTY) ||
			prop.equals(JOptionPane.INPUT_VALUE_PROPERTY))) {
		
		    //If you were going to check something
		    //before closing the window, you'd do
		    //it here.
		    
		    //this this case we should change this so when the
		    //user just closes the window, its the same as cancel
		    
		    dialog.setVisible(false);
		}
	    }
	});
	
	//set up and make the window visable
	dialog.pack();
	dialog.setLocationRelativeTo(this);
	dialog.setVisible(true);
	
	int value = ((Integer)optionPane.getValue()).intValue();
	
	if (value == JOptionPane.YES_OPTION) {
    	ass.go();
	} 
	
	else if (value == JOptionPane.NO_OPTION) {
	    //if no was pressed, do something else
        //dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE );
        throw new BadActionException();
	} 
	
	//non-auto-closing dialog with custom message area
	//NOTE: if you don't intend to check the input,
	//then just use showInputDialog instead.

    }//askForConformation
    
    /**
     * shows a window asking if the user wants to continue.
     * Occurs when the edit section finds a conflict with
     * an assignment and the data fields have been changed
     *
     * @param sectionNum the section number
     * @param mod a value of type 'ModSectAction'
     */
    public void askForEditAssignConfirmation( String sectionNum, 
					      ModSectAction mod){ 
	/**
	 * method to open confirmation pane
	 *
	 * @param you a value of type '"Do'
	 * @param nil a value of type 'JOptionPane.QUESTION_MESSAGE'
	 * @param nil a value of type 'JOptionPane.YES_NO_CANCEL_OPTION'
	 * @return a value of type 'JOptionPane'
	 */
	final JOptionPane optionPane = new JOptionPane(
			  "Section "+sectionNum+" does not have \n" + 
			  "the required add-ons. \n" +
			  "Proceed?" ,
			  JOptionPane.QUESTION_MESSAGE,
			  JOptionPane.YES_NO_OPTION);
	
	/**
	 * method to open dialog pane
	 *
	 * @param nil a value of type 'this'
	 * @param you a value of type '"What's'
	 * @param nil a value of type 'true'
	 * @return a value of type 'JDialog'
	 */
	final JDialog dialog = new JDialog(this, "Confirm",true);
	
	dialog.setContentPane(optionPane);
	
	optionPane.addPropertyChangeListener(new PropertyChangeListener() {
	    
	/**
	 * change to new property
	 *
	 * @param e a value of type 'PropertyChangeEvent'
	 */
	public void propertyChange(PropertyChangeEvent e) {
	    String prop = e.getPropertyName();
	    
	    if (dialog.isVisible() 
		&& (e.getSource() == optionPane)
		&& (prop.equals(JOptionPane.VALUE_PROPERTY) ||
		    prop.equals(JOptionPane.INPUT_VALUE_PROPERTY))) {
		//If you were going to check something
		//before closing the window, you'd do
		//it here.
		
		//this this case we should change this so when the
		//user just closes the window, its the same as cancel
		
		dialog.setVisible(false);
	    }
	}
	});
	
	//set up and make the window visable
	dialog.pack();
	dialog.setLocationRelativeTo(this);
	dialog.setVisible(true);
	
	int value = ((Integer)optionPane.getValue()).intValue();
	
	//when the user clicks yes or no, we need to tell the mod action
	//to continue.  Send a code over to it so it knows what was 
	// clicked.  Then depending on what was clicked, it will do what
	// it needs to
	if (value == JOptionPane.YES_OPTION) {
	    mod.Continue( 1 );
	} 
	
	else if (value == JOptionPane.NO_OPTION) {
	    mod.Continue( 2 );
	} 
	
	//non-auto-closing dialog with custom message area
	//NOTE: if you don't intend to check the input,
	//then just use showInputDialog instead.

    }//askForEditAssignConformation


    //popup a default error message window
    /**
     * method to open default error message window
     *
     * @param errorMsg a value of type 'String'
     */
    public void defaultErrorMsgWindow( String errorMsg ){

	final JOptionPane optionPane = new JOptionPane(
			  "An error has been found:\n"
			  + errorMsg + "\n",
			  JOptionPane.ERROR_MESSAGE);
	
     
	/**
	 * You can't use pane.createDialog() because that
	 * method sets up the JDialog with a property change 
	 * listener that automatically closes the window 
	 * when a button is clicked.
	 *
	 * @param nil a value of type 'this'
	 * @param nil a value of type '"Error"'
	 * @param nil a value of type 'true'
	 * @return a value of type 'JDialog'
	 */
	final JDialog dialog = new JDialog(this, 
					   "Error",
					   true);
	dialog.setContentPane(optionPane);
	dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
	dialog.addWindowListener(new WindowAdapter() {
	    
	});
	

	optionPane.addPropertyChangeListener( new PropertyChangeListener() {
	    /**
	     * method to change property
	     *
	     * @param e a value of type 'PropertyChangeEvent'
	     */
	    public void propertyChange(PropertyChangeEvent e) {
		String prop = e.getPropertyName();
		
		if (dialog.isVisible() 
		    && (e.getSource() == optionPane)
		    && (prop.equals(JOptionPane.VALUE_PROPERTY) ||
			prop.equals(JOptionPane.INPUT_VALUE_PROPERTY))) {
		    //If you were going to check something
		    //before closing the window, you'd do
		    //it here.
		    dialog.setVisible(false);
		}
	    }
	});
	
	dialog.pack();
	dialog.setLocationRelativeTo(this);
	dialog.setVisible(true);
	
	int value = ((Integer)optionPane.getValue()).intValue();
	if (value == JOptionPane.YES_OPTION) {
	    //if the user presses yes here, do something...
	} 
	
	else if (value == JOptionPane.NO_OPTION) {
	    //if no was pressed, do something else
	} 
	
	//non-auto-closing dialog with custom message area
	//NOTE: if you don't intend to check the input,
	//then just use showInputDialog instead.
    }

    /**
     * A simple popup window that alerts the user that
     * a section has lost its assignment and needs to
     * be reassigned
     *
     * @param sectionName the section name
     */
    public void pleaseAssignLaterMsgWindow( String sectionName ){
	
	final JOptionPane optionPane = new JOptionPane(
			  "The times and/or days on the following section\n"
			  + sectionName + "\n"
			  + "has been changed, please reassign the section",
			  JOptionPane.ERROR_MESSAGE);
	     
	/**
	 * You can't use pane.createDialog() because that
	 * method sets up the JDialog with a property change 
	 * listener that automatically closes the window 
	 * when a button is clicked.
	 *
	 * @param nil a value of type 'this'
	 * @param nil a value of type '"Note"'
	 * @param nil a value of type 'true'
	 * @return a value of type 'JDialog'
	 */
	final JDialog dialog = new JDialog(this,"Note",true);
	dialog.setContentPane(optionPane);
	dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);

	dialog.addWindowListener(new WindowAdapter() {

	});

	optionPane.addPropertyChangeListener( new PropertyChangeListener() {
	
	    /**
	     * method to change property
	     *
	     * @param e a value of type 'PropertyChangeEvent'
	     */
	    public void propertyChange(PropertyChangeEvent e) {
		
		String prop = e.getPropertyName();
		
		if (dialog.isVisible() 
		    && (e.getSource() == optionPane)
		    && (prop.equals(JOptionPane.VALUE_PROPERTY) ||
			prop.equals(JOptionPane.INPUT_VALUE_PROPERTY))) {
		
		    //If you were going to check something
		    //before closing the window, you'd do
		    //it here.
		    dialog.setVisible(false);
		}
	    }
	});
	
	dialog.pack();
	dialog.setLocationRelativeTo(this);
	dialog.setVisible(true);
	
	int value = ((Integer)optionPane.getValue()).intValue();
	if (value == JOptionPane.YES_OPTION) {
	    //if the user presses yes here, do something...
	} 
	
	else if (value == JOptionPane.NO_OPTION) {
	    //if no was pressed, do something else
	} 
	
	//non-auto-closing dialog with custom message area
	//NOTE: if you don't intend to check the input,
	//then just use showInputDialog instead.
    }
    
    /**
     * brings us a window that alerts the user that some
     * sections were unassigned from the edited room
     *
     */
    public void assignmentsWereLostMsgWin(){
	
	final JOptionPane optionPane = new JOptionPane(
			  "one or more sections were unassigned because "+
			  "of capacity conflicts",
			  JOptionPane.ERROR_MESSAGE);
	     
	/**
	 * You can't use pane.createDialog() because that
	 * method sets up the JDialog with a property change 
	 * listener that automatically closes the window 
	 * when a button is clicked.
	 *
	 * @param nil a value of type 'this'
	 * @param nil a value of type '"Error"'
	 * @param nil a value of type 'true'
	 * @return a value of type 'JDialog'
	 */
	final JDialog dialog = new JDialog(this, "Note", true);

	dialog.setContentPane(optionPane);
	dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
	dialog.addWindowListener(new WindowAdapter() {

	});

	optionPane.addPropertyChangeListener( new PropertyChangeListener() {

	    /**
	     * method to change property
	     *
	     * @param e a value of type 'PropertyChangeEvent'
	     */
	    public void propertyChange(PropertyChangeEvent e) {
		String prop = e.getPropertyName();
		
		if (dialog.isVisible() 
		    && (e.getSource() == optionPane)
		    && (prop.equals(JOptionPane.VALUE_PROPERTY) ||
			prop.equals(JOptionPane.INPUT_VALUE_PROPERTY))) {
		    //If you were going to check something
		    //before closing the window, you'd do
		    //it here.
		    dialog.setVisible(false);
		}
	    }
	});
	
	dialog.pack();
	dialog.setLocationRelativeTo(this);
	dialog.setVisible(true);
	
	//non-auto-closing dialog with custom message area
	//NOTE: if you don't intend to check the input,
	//then just use showInputDialog instead.
    }
   
    /**
     *This is a popup window that is used to simply
     *alert the user that due to some changes, attributes
     *differ between a section and room
     *
     */
    public void noteSomeAttribDifferencesWin(){

	final JOptionPane optionPane = new JOptionPane(
			  "Please note there are some sections assigned to "+
			  "this room that require aditional attributes",
			  JOptionPane.ERROR_MESSAGE);
	
	
	/**
	 * You can't use pane.createDialog() because that
	 * method sets up the JDialog with a property change 
	 * listener that automatically closes the window 
	 * when a button is clicked.
	 *
	 * @param nil a value of type 'this'
	 * @param nil a value of type '"Error"'
	 * @param nil a value of type 'true'
	 * @return a value of type 'JDialog'
	 */
	final JDialog dialog = new JDialog(this, "Alert", true);

	dialog.setContentPane(optionPane);
	dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
	dialog.addWindowListener(new WindowAdapter() {

	});

	optionPane.addPropertyChangeListener( new PropertyChangeListener() {

	    /**
	     * method to change property
	     *
	     * @param e a value of type 'PropertyChangeEvent'
	     */
	    public void propertyChange(PropertyChangeEvent e) {
		String prop = e.getPropertyName();
		
		if (dialog.isVisible() 
		    && (e.getSource() == optionPane)
		    && (prop.equals(JOptionPane.VALUE_PROPERTY) ||
			prop.equals(JOptionPane.INPUT_VALUE_PROPERTY))) {

		    //If you were going to check something
		    //before closing the window, you'd do
		    //it here.
		    dialog.setVisible(false);
		}
	    }
	});

	dialog.pack();
	dialog.setLocationRelativeTo(this);
	dialog.setVisible(true);
	
	int value = ((Integer)optionPane.getValue()).intValue();

	if (value == JOptionPane.YES_OPTION) {
	    //if the user presses yes here, do something...
	} 

	else if (value == JOptionPane.NO_OPTION) {
	    //if no was pressed, do something else
	}
	
	//non-auto-closing dialog with custom message area
	//NOTE: if you don't intend to check the input,
	//then just use showInputDialog instead.
    }//noteSomeAttribDifferencesWin



    /**
     * pops up and ask weather or not the user wants to exit
     *
     */
    public void exitQuestionWindow( ExitAction ext){
        
	/**
	 * method to open confirmation pane
	 *
	 * @param you a value of type '"Do'
	 * @param nil a value of type 'JOptionPane.QUESTION_MESSAGE'
	 * @param nil a value of type 'JOptionPane.YES_NO_CANCEL_OPTION'
	 * @return a value of type 'JOptionPane'
	 */
	final JOptionPane optionPane = new JOptionPane(
			  "Changes have been made since last save."+
			  " Save before exit?",
			  JOptionPane.QUESTION_MESSAGE,
			  JOptionPane.YES_NO_OPTION);
	
	/**
	 * method to open dialog pane
	 *
	 * @param nil a value of type 'this'
	 * @param you a value of type '"What's'
	 * @param nil a value of type 'true'
	 * @return a value of type 'JDialog'
	 */
	final JDialog dialog = new JDialog(this, "Confirm", true);

	dialog.setContentPane(optionPane);
	
	optionPane.addPropertyChangeListener( new PropertyChangeListener() {
	   
	    /**
	     * change to new property
	     *
	     * @param e a value of type 'PropertyChangeEvent'
	     */
	    public void propertyChange(PropertyChangeEvent e) {
		
		String prop = e.getPropertyName();
		
		if (dialog.isVisible() 
		    && (e.getSource() == optionPane)
		    && (prop.equals(JOptionPane.VALUE_PROPERTY) ||
			prop.equals(JOptionPane.INPUT_VALUE_PROPERTY))) {
		    //If you were going to check something
		    //before closing the window, you'd do
		    //it here.
		    
		    //this this case we should change this so when the
		    //user just closes the window, its the same as cancel
		    
		    dialog.setVisible(false);
		}
	    }
	});
	
	//set up and make the window visable
	dialog.pack();
	dialog.setLocationRelativeTo(this);
	dialog.setVisible(true);
	
	int value = ((Integer)optionPane.getValue()).intValue();
	if (value == JOptionPane.YES_OPTION) {
	    ext.saveIt();
	} 
	
	else if (value == JOptionPane.NO_OPTION) {
	    ext.killProgram();
	} 
	
    }//askForConformation

    /**
     * returns top right panel
     *
     * @return a value of type 'TopRightPanel'
     */
    public TopRightPanel getTopRightPanel(){

		return topRightPanel;
    }

    /**
     * returns Top Left Panel
     *
     * @return a value of type 'TopLeftPanel'
     */
    public TopLeftPanel getTopLeftPanel(){

		return topLeftPanel;
    }

    /**
     * returns Bottom Left Panel
     *
     * @return a value of type 'BottomLeftPanel'
     */
    public BottomLeftPanel getBottomLeftPanel(){

		return bottomLeftPanel;
    }


    /**
     * returns the section number as a string.  its the represented
     * section number
     *
     */
    public String getHighlightedSection(){

        return topRightPanel.getHighlighted();
			
    }
    
    /**
     * will highlight a new section
     *
     * @param sectNum a value of type 'String'
     */
    public void setHighlightedSection( String sectNum ) {

    	topRightPanel.hiLight( sectNum );	
	}
    
    /**
     * method to return a highlighted room 
     *
     * @return a value of type 'String'
     */
    public String getHighlightedRoom(){

    	return ( topLeftPanel.getHighlighted() );
    }
    
    /**
     * will highlight a room
     *
     * @param roomNum a value of type 'String'
     */
    public void setHiLightedRoom( String roomNum ) {

    	topLeftPanel.hiLight( roomNum );	
	}
    
    
    /**
     * method to unselect everything in the Section 
     * window
     *
     *
     */
    public void unHighlightSections(){

    	topRightPanel.unHighlight();
    } // unHighlightSections
    
    public void unHighlightRooms(){

    	topLeftPanel.unHighlight();
    } // unHighlightRooms

    /*
     * The following methods are used to update the various tables
     */

    /**
     * method to update Room List 
     *
     * @param newData a value of type 'Object[][]'
     */
    public void updateRoomList( Object[][] newData ){

	topLeftPanel.setTableValue( newData );
    }
    
    /**
     * method to update Section List
     *
     * @param newData a value of type 'Object[][]'
     */
    public void updateSectionList( Object[][] newData ){

	topRightPanel.setTableValue( newData );
    }
    
    /**
     * method to update Room Occupancy
     *
     * @param newData a value of type 'Object[][]'
     */
    public void updateRoomOccupancy( Object[][] newData ){

	bottomLeftPanel.setTableValue( newData );
    }
    
    /**
     * method to update Room Attributes
     *
     * @param newData a value of type 'Object[]'
     */
    public void updateRoomAttributes( Object[] newData ){

	bottomRightPanel.updateRoomAttributes( newData );
    }
    
    /**
     * method to update Section Attributes
     *
     * @param newData a value of type 'Object[]'
     */
    public void updateSectionAttributes( Object[] newData ){

	bottomRightPanel.updateSectionAttributes( newData );
    }

    public int getSectionSortStyle(){

        return topRightPanel.getSort();
    } // getSectionSortStyle
    
    public int getRoomSortStyle(){

        return topLeftPanel.getSort();

    } // getRoomSortStyle
    
    public void setSectionSortStyle( int newStyle ) {

        topRightPanel.setSort( newStyle);
    } // setSectionSortStyle
    
    public void setRoomSortStyle( int newStyle ) {

        topLeftPanel.setSort( newStyle );
    } // setroomSortStyle

}// GUI
