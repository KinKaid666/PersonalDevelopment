
/**
 * This is the window that popups when the user wants to add one section
 *
 * @version	$Id: AddSingleSectionWindow.java,v 1.31 2000/05/10 17:18:47 etf2954 Exp $
 *
 * @author	Adam E Chipalowsky 
 *
 * Revisions:
 *		$Log: AddSingleSectionWindow.java,v $
 *		Revision 1.31  2000/05/10 17:18:47  etf2954
 *		Made this call the error on dirty input
 *
 *		Revision 1.30  2000/05/10 15:40:39  etf2954
 *		Removed System.out's
 *
 *		Revision 1.29  2000/05/08 03:09:13  p361-45a
 *		fixed formatting
 *
 *		Revision 1.28  2000/05/05 14:50:52  pel2367
 *		sends the null section on cancel.
 *
 *		Revision 1.27  2000/05/05 02:23:55  pel2367
 *		now sends a null section on cancel.
 *
 *		Revision 1.26  2000/05/05 01:07:00  pel2367
 *		threw an exception
 *
 *		Revision 1.25  2000/05/04 17:36:09  aec1324
 *		some lines were over 80 chars, took care of it
 *
 *		Revision 1.24  2000/04/29 19:31:04  aec1324
 *		got rid of System.out....'s cause stuff works now and
 *		we don't need them any more
 *
 *		Revision 1.23  2000/04/28 17:05:57  aec1324
 *		fixed the problem with the days.  the days in the
 *		newly editied section were inverted. now it works
 *		correctly
 *
 *		Revision 1.22  2000/04/28 01:30:57  aec1324
 *		worked on the edit part of this window
 *
 *		Revision 1.21  2000/04/27 02:01:08  aec1324
 *		revised this class to allow two way communication
 *		the class can now act as an edit window
 *		It kind of works, but not really yet...by
 *		tomorrow it will be 100%
 *
 *		Revision 1.20  2000/04/24 23:33:17  aec1324
 *		made sure the section is creating a boolean
 *		array for the days
 *
 *		Revision 1.19  2000/04/24 02:01:17  aec1324
 *		got rid of the offsets for start and end timne
 *
 *		Revision 1.18  2000/04/24 00:54:55  etf2954
 *		Made it so the # is the first field when tabed, and it
 *		goes, NUMBER, NAME/DESCRIPTION, CAPACITY
 *
 *		Revision 1.17  2000/04/23 18:16:31  pel2367
 *		removed some commented-out code, tried and tried to
 *		understand why it was blowing up.  turns out that
 *		the problem is way back in CRSSAction.
 *
 *		Revision 1.16  2000/04/22 18:02:57  pel2367
 *		made the section number include dashes.
 *
 *		Revision 1.15  2000/04/22 00:51:39  aec1324
 *		got the start and end time working
 *
 *		Revision 1.14  2000/04/21 18:46:44  aec1324
 *		added some error checking and made it so the
 *		pull down start and end times will not allow
 *		the user to enter in "negative" time
 *
 *		Revision 1.13  2000/04/21 16:32:09  aec1324
 *		added days of teh week and start and end times
 *
 *		Revision 1.12  2000/04/20 23:05:50  aec1324
 *		added the ability to update the attributes
 *		table.
 *
 *		Revision 1.11  2000/04/20 20:20:23  etf2954
 *		Fixed error on the string concatination
 *
 *		Revision 1.10  2000/04/20 16:41:08  p361-45a
 *		did some method headers
 *
 *		Revision 1.9  2000/04/20 02:27:38  aec1324
 *		most data is now sent to the action
 *
 *		Revision 1.8  2000/04/19 20:43:58  aec1324
 *		stuff
 *
 *		Revision 1.7  2000/04/19 16:37:22  aec1324
 *		working with the way it talks to a section
 *
 *		Revision 1.6  2000/04/19 14:28:38  etf2954
 *		Tried to get the popup window to return text
 *
 *		Revision 1.5  2000/04/18 22:09:24  aec1324
 *		added more accessor methods
 *
 *		Revision 1.4  2000/04/18 21:21:30  aec1324
 *		got rid of the refrence to operator
 *
 *		Revision 1.3  2000/04/14 16:55:33  aec1324
 *		got rid of the action listener cause it didn't work
 *
 *		Revision 1.2  2000/04/14 02:53:02  aec1324
 *		attempting to add a listener
 *		also fixed the spacing problem in the text
 *		area
 *
 *		Revision 1.1  2000/04/13 02:22:57  aec1324
 *		Initial revision
 *
 */

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import javax.swing.JTextField;
import java.io.*;
import java.awt.Window;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumn;
import javax.swing.BorderFactory; 
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.text.*;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.DefaultTableModel;
import java.awt.Dimension;
import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;

import java.awt.event.*;

public class AddSingleSectionWindow  extends JFrame
    /*implements ActionListener*/ {
 
    private JFrame frame;
    private JButton okButton = null;
    private JButton cancelButton = null;
    private TableColumn tableColumn;
    private Border loweredBevel;
    private MyTableModel myModel;
    final String btnString1 = "Enter";
    final String btnString2 = "Cancel";
    private JOptionPane optionPane;
    private JPanel panel;
    private JPanel innerPanel;
    private JLabel emptyLabel;
    private JPanel checkBoxPanel;
    private Dimension dimen = new Dimension( 660, 390 );
    private String finalSectionNumber;
    private String finalCapacity;
    private String finalName;
    private AddSectAction myAddParent;
    private ModSectAction myModParent;
	private Section myModSection;
    private int finalInt;
    private String windowTitle;

    private Object attributes[];

    private JTextField secNum4TextField;
    private JTextField secNum3TextField;
    private JTextField sectionNameTextField;
    private JTextField secNum2TextField;
    private JTextField sectionCapacityTextField;

    private JComboBox startTime;
    private JComboBox endTime;
    
    private int sectionStartTime;
    private int sectionEndTime;

    private JCheckBox monday;
    private JCheckBox tuesday;
    private JCheckBox wednesday;
    private JCheckBox thursday;
    private JCheckBox friday;

    private int startTimeInt;
    private int endTimeInt;

    private boolean[] days;
    private JTable table;
    private String sectionNumber;
    private String sectionName;
    private String sectionCapacity;
    private String sectionAttributesTable;
    private String dash;
    private String spacer;

    private JLabel secAtt;
    private JLabel secNum;
    private JLabel secNam;
    private JLabel secCap;
    private JLabel aDash1;
    private JLabel aDash2;
    private JLabel aSpacer;
    private JLabel startTimeLabel;
    private JLabel endTimeLabel;
    private CheckBoxListener myListener;
    private Object[] times = {"8 am", "9 am", "10 am", "11 am",
			      "12 pm", "1 pm","2 pm", "3 pm", "4 pm",
			      "5 pm", "6 pm", "7 pm", "8 pm", "9 pm",
			      "10 pm"};
    
    private Section new_section = null;
    private boolean isThisAddingASection;

    /**
     * constructor to create an object of type AddSingleSectionWindow
     *
     * @param parent a value of type 'AddSectAction'
     */
    public AddSingleSectionWindow( AddSectAction parent ){
	
	myAddParent = parent;
	attributes = new Object[7];
	days = new boolean[5];
	
	//set all attributes to false (false means 0)
	for (int i=0; i<7; i++){
	    attributes[i] = "0";
	}
	
	//initialise the days boolean array to avoid null errors when 
	//an action is called later on
	for (int i=0; i<4; i++){
	    days[i] = true;
	}
	//set frieday to false (aka, section does not meet on friday, 
	//as default)
	days[4] = false;

	//for later use this class needs to know if it was called 
	//via ModSectAction or AddSectAction.  By setting this 
	//false, it will know
	isThisAddingASection = true;
	
	//initialise the start and end time, military time
	sectionStartTime = 8;
	sectionEndTime = 9;
	
	//set the title of this window to be Add Section
	windowTitle = "Add Section";
	
	//set up the window.
	setUpWindow();
	
	//set the first one as selected to start out with
	startTime.setSelectedIndex(0);
	endTime.setSelectedIndex(0);
    
    }//AddSingleSectionWindow
    
    /**
     * this constructor opens the window as does the other one except this
     * fills in the data values in each field and changes the title to "Edit
     * Eection"
     *
     * @param parent the action who called this
     * @param s the section in which to get properties to fill data members
     */
    public AddSingleSectionWindow( ModSectAction parent, Section sentSection ){
	
	myModParent = parent;
	attributes = new Object[7];
	days = new boolean[5];
	myModSection = sentSection;
	
	//for later use this class needs to know if it was called 
	//via ModSectAction or AddSectAction.  By setting this false, 
	//it will know
	isThisAddingASection = false;
	
	//set the title of this window to be Edit Section
	windowTitle = "Edit Section";
	
	//set up the window
	setUpWindow();
	
	//break apart the section number so each part of it can be placed it
	//its corresponding text field
	secNum4TextField.setText( (sentSection.getSectNum()).substring(0,4) );
	secNum3TextField.setText( (sentSection.getSectNum()).substring(5,8) );
	secNum2TextField.setText( (sentSection.getSectNum()).substring(9,11) );
	
	//update the text fields to what was passed in
	sectionNameTextField.setText( sentSection.getSectName() );

	//update the number of people in the section
	sectionCapacityTextField.setText( 
			(new Integer(sentSection.getEnrollmt())).toString() );

	//set all attributes to what they are in the "s" section
	//attributes = sentSection.getAttrib();
	attributes = sentSection.getAttrib();
	myModel.resetAttributesDataVector( attributes );

	//reset the start and end time to what the user put in the 
	//origional section  
	startTime.setSelectedIndex( (sentSection.getStartTime() - 8) );
	endTime.setSelectedIndex( (sentSection.getEndTime() - 9) );
	
	//set the day check boxes to what they should be
	days = sentSection.getDays();
	
	//set the days back to what they were in the origional section
	if( days[0] ){
	    monday.setSelected(days[0]);
	    days[0] = true;
	}

	else{
	    monday.setSelected(false);
	    days[0] = false;
	}

	if( days[1] ){
	    tuesday.setSelected(days[1]);
	    days[1] = true;
	}

	else{
	    tuesday.setSelected(false);
	    days[1] = false;
	}

	if( days[2] ){
	    wednesday.setSelected(days[2]); 
	    days[2] = true;  
	}

	else{
	    wednesday.setSelected(false);
	    days[2] = false;
	}

	if( days[3] ){
	    thursday.setSelected(days[3]); 
	    days[3] = true; 
	}

	else{
	    thursday.setSelected(false);
	    days[3] = false;
	}

	if( days[4] ){
	    friday.setSelected(days[4]); 
	    days[4] = true;  
	}

	else{
	    friday.setSelected(false);
	    days[4] = false;
	}

	//allow the user to cancel the editing operation
	cancelButton.addActionListener(new ActionListener() {
	    /**
	     * method to close window when cancel is chosen
	     *
	     * @param e a value of type 'ActionEvent'
	     */
	    public void actionPerformed(ActionEvent e){
			CloseWindowViaCancel();
	    }
	});
	
	okButton.addActionListener(new ActionListener() {
	    /**
	     * method to close window when ok is chosen
	     * the final assignments are to be set and then the 
	     * information is sent back to the action
	     * who clalled for this window to be opened
	     *
	     * @param e a value of type 'ActionEvent'
	     */
	    public void actionPerformed(ActionEvent e) {
		CloseWindowViaOk();
	    }
	});

    }//AddSingleSectionWindow (the edit constructor)


    private void setUpWindow(){
	//create the strings
	sectionNumber = "Enter section Number ";
	sectionName = "Enter section name ";
	sectionCapacity = "Enter section capacity";
	sectionAttributesTable = "Attributes";
	dash = "-";
	spacer = "    ";
		
	//create the textfields
	secNum4TextField = new FixedLengthDigitTextField( null, 4 );
	secNum3TextField = new FixedLengthDigitTextField( null, 3 );
	secNum2TextField = new FixedLengthDigitTextField( null, 2 );
	sectionNameTextField = 
	    new FixedLengthTextField( 25 );
	sectionCapacityTextField = 
	    new FixedLengthDigitTextField( null, 3 );

	//create labels for stuff...seems like everything
	secAtt = new JLabel( sectionAttributesTable );
	secNum = new JLabel( sectionNumber );
	secNam = new JLabel( sectionName );
	secCap = new JLabel( sectionCapacity );
	aDash1 = new JLabel( dash );
	aDash2 = new JLabel( dash );
	aSpacer = new JLabel( spacer);
	startTimeLabel = new JLabel( "Start time" );
	endTimeLabel = new JLabel( "End time" );
	
	//set up the check boxes
	monday = new JCheckBox("Monday");
	monday.setMnemonic(KeyEvent.VK_C); 
	monday.setSelected(true);
	
	tuesday = new JCheckBox("Tuesday");
	tuesday.setMnemonic(KeyEvent.VK_C); 
	tuesday.setSelected(true);
	
	wednesday = new JCheckBox("Wednesday");
	wednesday.setMnemonic(KeyEvent.VK_C); 
	wednesday.setSelected(true);
	
	thursday = new JCheckBox("Thursday");
	thursday.setMnemonic(KeyEvent.VK_C); 
	thursday.setSelected(true);
	
	friday = new JCheckBox("Friday");
	friday.setMnemonic(KeyEvent.VK_C); 
	friday.setSelected(false);
	
	// Register a listener for the check boxes.
	myListener = new CheckBoxListener();
	monday.addItemListener(myListener);
	tuesday.addItemListener(myListener);
	wednesday.addItemListener(myListener);
	thursday.addItemListener(myListener);
	friday.addItemListener(myListener);
	
	//create the pull down menu that will list the times
	startTime = new JComboBox();
	endTime = new JComboBox();
	
	//set the elements in the combo boxs
	for (int i = 0; i <times.length; i++) { 
	    //Populate it, skipping the last time for start time
	    if( i != 14 ){
		startTime.addItem(times[i]);
	    }
	    //skipping the first time for end time
	    if(i != 0){
		endTime.addItem(times[i]);
	    }
	}//for
		
	//set up the layout tool
	GridBagLayout gridbag = new GridBagLayout();
	GridBagConstraints c = new GridBagConstraints();
	
	//set up the title in the frame
	frame = new JFrame( windowTitle );
	emptyLabel = new JLabel("");
	
	//make it so the user cannot resize the frame.
	frame.setResizable( false );
	
	//create and add the check box table for the room attributes
	myModel = new MyTableModel();
	table = new JTable(myModel);
	
	//add a border around the table
	loweredBevel = BorderFactory.createLoweredBevelBorder();
	table.setBorder(loweredBevel);
	
	//set up the column widths
	tableColumn = table.getColumnModel().getColumn(0);
	tableColumn.setPreferredWidth(150);
	
	tableColumn = table.getColumnModel().getColumn(1);
	tableColumn.setPreferredWidth(50);
	
	panel = new JPanel();
	panel.setPreferredSize( dimen );
	panel.setLayout(gridbag);
	//panel.setBackground( Color.yellow );
	// create the buttons
	okButton = new JButton( "Enter" );
	cancelButton = new JButton( "Cancel" );
	
	//create the user edit section number area
	innerPanel = new JPanel();
	innerPanel.add( secNum4TextField );
	innerPanel.add( aDash1 );
	innerPanel.add( secNum3TextField );
	innerPanel.add( aDash2 );
	innerPanel.add( secNum2TextField );
	
	//create a panel that holds all the check boxed
	//this way they can all be bunched together and all will
	//be well.  I want a tropical plant
	
	checkBoxPanel = new JPanel();
	checkBoxPanel.setLayout(gridbag);
	
	//for monday
	c.gridx = 0;
	c.gridy = 0;
	c.anchor = c.WEST;
	gridbag.setConstraints(monday, c);
	
	//for tuesday
	c.gridx = 0;
	c.gridy = 1;
	c.anchor = c.WEST;
	gridbag.setConstraints(tuesday, c);
	
	//for wednesday
	c.gridx = 0;
	c.gridy = 2;
	c.anchor = c.WEST;
	gridbag.setConstraints(wednesday, c);
	
	//for thursday
	c.gridx = 0;
	c.gridy = 3;
	c.anchor = c.WEST;
	gridbag.setConstraints(thursday, c);
	
	//for friday
	c.gridx = 0;
	c.gridy = 4;
	c.anchor = c.WEST;
	gridbag.setConstraints(friday, c);
	
	//add the days together
	checkBoxPanel.add( monday );
	checkBoxPanel.add( tuesday );
	checkBoxPanel.add( wednesday );
	checkBoxPanel.add( thursday );
	checkBoxPanel.add( friday );
	
	//set the constrants on the gridbag
	//attributes label
	c.weightx = 1.0;
	c.weighty = 1.0;
	c.gridx = 7;
	c.gridy = 0;
	c.anchor = c.WEST;
	gridbag.setConstraints(secAtt, c);
	
	// "Enter section number"
	c.weightx = 1.0;
	c.weighty = 1.0;
	c.gridx = 0;
	c.gridy = 1;
	c.anchor = c.WEST;
	gridbag.setConstraints(secNum, c);
	
	// "Enter section name"
	c.weightx = 1.0;
	c.weighty = 1.0;
	c.gridx = 0;
	c.gridy = 2;
	c.anchor = c.WEST;
	gridbag.setConstraints(secNam, c);
	
	// "Enter section capacity"
	c.weightx = 1.0;
	c.weighty = 1.0;
	c.gridx = 0;
	c.gridy = 3;
	c.anchor = c.WEST;
	gridbag.setConstraints(secCap, c);
	
	// add the text fields
	c.gridx = 1;
	c.gridy = 1;
	c.anchor = c.WEST;
	c.gridwidth = 4;
	gridbag.setConstraints( innerPanel ,c );
	
	//a spacer around the table
	c.gridx = 6;
	c.gridy = 0;
	c.anchor = c.CENTER;
	gridbag.setConstraints(aSpacer, c);
	
	//the section name enter area
	c.gridx = 1;
	c.gridy = 2;
	c.anchor = c.WEST;
	c.gridwidth = 6;
	gridbag.setConstraints(sectionNameTextField, c);
	
	//the section capacity enter area
	c.gridx = 1;
	c.gridy = 3;
	c.anchor = c.WEST;
	c.gridwidth = 1;
	gridbag.setConstraints(sectionCapacityTextField, c);
	
	//the table
	c.gridx = 7;
	c.gridy = 1;
	c.gridwidth = 1;
	c.gridheight = 3;
	gridbag.setConstraints(table, c);
	
	//the Enter button
	c.gridx = 6;
	c.gridy = 8;
	c.gridwidth = 1;
	c.gridheight = 1;
	gridbag.setConstraints(okButton, c);
	
	//the cancel button
	c.gridx = 7;
	c.gridy = 8;
	c.anchor = c.CENTER;
	gridbag.setConstraints(cancelButton, c);
	
	//set the check box panel 
	c.gridx = 0;
	c.gridy = 5;
	c.gridwidth = 1;
	c.gridheight = 4;
	gridbag.setConstraints(checkBoxPanel, c);
	
	//the pull down menu for the start time
	c.gridx = 2;
	c.gridy = 6;
	c.gridwidth = 1;
	c.gridheight = 1;
	gridbag.setConstraints(startTime, c);
	
	//the label over start time pull down menu
	c.gridx = 2;
	c.gridy = 5;
	c.anchor = c.SOUTH;
	gridbag.setConstraints(startTimeLabel, c);
	
	//the pull down menu for the end time
	c.gridx = 3;
	c.gridy = 6;
	c.anchor = c.CENTER;
	gridbag.setConstraints(endTime, c);
	
	//the label of end time pull down menu
	c.gridx = 3;
	c.gridy = 5;
	c.anchor = c.SOUTH;
	gridbag.setConstraints(endTimeLabel, c);
	
	//add all the components to the main frame
	panel.add( secAtt );
	panel.add( secNum );
	panel.add( secNam );
	panel.add( secCap );
	panel.add( aSpacer );
	panel.add( innerPanel );
	//innerPanel.setNextFocusableComponent( sectionNameTextField );
	panel.add( sectionNameTextField );
	panel.add( sectionCapacityTextField );
	panel.add( table );
	panel.add( okButton );
	panel.add( cancelButton );
	panel.add( startTime );
	panel.add( endTime );
	panel.add( checkBoxPanel );
	panel.add( startTimeLabel );
	panel.add( endTimeLabel );
	
	
	//wrap it and pack it...send it out and make it visable!
	frame.getContentPane().add( panel, BorderLayout.CENTER);
	frame.pack();
	frame.setVisible(true);
	
	//set the first one as selected to start out with
	startTime.setSelectedIndex(0);
	endTime.setSelectedIndex(0);   
	
	//set up the action listeners for the start and end time
	//pull down wondows.  Note that when the user selects
	//a start time, it invalidates all the ent times that 
	//would not be possable.  For example, start time is 9am,
	//only end times passes 10am are valid, the menu will not
	//allow the user to select a less than end time.  It will
	//default to simply selecting a one hour after end time   
	startTime.addActionListener(new ActionListener() {
	    
	    /**
	     * set start and end times
	     *
	     * @param e a value of type 'ActionEvent'
	     */
	    public void actionPerformed(ActionEvent e) {
		
		//startTimeInt = startTime.getSelectedIndex();
		
		if( endTime.getSelectedIndex() 
		    < startTime.getSelectedIndex() ){
		    
		    endTime.setSelectedIndex(
					     startTime.getSelectedIndex());
		}//if
		
		//set the sectionStartTime so its ready to be added 
		//to a new section.  Represented in militarty time so 
		//it needs an offset of 8
		sectionStartTime = startTime.getSelectedIndex();
		
	    }//actionPerformed
	});
	
	endTime.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		
		if( endTime.getSelectedIndex() 
		    < startTime.getSelectedIndex() ){
		    
		    endTime.setSelectedIndex(
					     startTime.getSelectedIndex());
		}//if
		
		//set the sectionStartTime so its ready to be added 
		//to a new section.  Represented in militarty time so
		//it needs an offset of 9 cause it must be one more 
		//than the start
		sectionEndTime = endTime.getSelectedIndex() + 1;
	    }//actionPerformed
	});
	
    }//setUpWindow
    

    /**
     * method to get information about a section from user
     *
     */
    public void getUserInfo() {

    //allow the user to cancel the editing operation
    cancelButton.addActionListener(new ActionListener() {
	
	/**
	 * method to close window when cancel is chosen
	 *
	 * @param e a value of type 'ActionEvent'
	 */
	public void actionPerformed(ActionEvent e) {
	    CloseWindowViaCancel();
	    
	}
    });
    
    okButton.addActionListener(new ActionListener() {
	/**
	 * method to close window when ok is chosen
	 * the final assignments are to be set and then the 
	 * information is sent back to the action
	 * who clalled for this window to be opened
	 *
	 * @param e a value of type 'ActionEvent'
	 */
	public void actionPerformed(ActionEvent e) {
	    
	    CloseWindowViaOk();
	    
	    
	}
    });
    
    }// AddSingleSectionWindow()
    
    
    /**
     * closes window with no changes
     *
     */
    private void CloseWindowViaCancel(){
   
	Section tempSection = null;
        frame.setDefaultCloseOperation( DO_NOTHING_ON_CLOSE  );
        frame.dispose();

        if( myAddParent != null ){
            myAddParent.sendSection( tempSection );
        }

        else if( myModParent != null ){
            myModParent.sendSection( tempSection );
        }

    }
    
    /**
     * method to update the database and everything else
     *
     */
    private void CloseWindowViaOk(){
	
	//
	// Check to make sure that the user
	// has entered data for the text fields
	//
	if( secNum4TextField.getText().length() < 4 || 
	    secNum3TextField.getText().length() < 3 ||
	    secNum2TextField.getText().length() < 2 ||
	    sectionCapacityTextField.getText().length() == 0 ) {
	    
	    //
	    // Since the user has chosen to put in less than the actual
	    // Needed info, let's throw up a error box...yeah!
	    //
	    JOptionPane error = new JOptionPane();

	    error.showMessageDialog( null, ("You have left one or more "+
				     "fields incomplete!"), 
				     "Error", JOptionPane.ERROR_MESSAGE ) ;
	} // if

	else { 

	    finalSectionNumber = secNum4TextField.getText() + "-" +
		secNum3TextField.getText() + "-" +
		secNum2TextField.getText();
	    finalName = sectionNameTextField.getText();
	    
	    finalInt = (( new Integer
			  (sectionCapacityTextField.getText()).intValue()));
	    
	    
	    new_section = new Section( finalName, 
				       finalSectionNumber,
				       attributes, finalInt, 
				       sectionStartTime, sectionEndTime,  
				       days );

	    //figure out if we are adding a new section or 
	    //if this is simply a modification of a section
	    
	    if ( isThisAddingASection ){

	    //if we are here, we are adding a new section

			if ( myAddParent.isAddable( new_section ) ) {
				
				//send the newwly created section back to whoever asked 
				//for it
				myAddParent.sendSection( new_section );
				
				frame.setDefaultCloseOperation( DO_NOTHING_ON_CLOSE  );
				frame.dispose();
			}

			else {
				JOptionPane error = new JOptionPane();
				error.showMessageDialog( null, 
							 ("A section with that number " +
							  "is already in your database"),
							 "Error", 
							 JOptionPane.ERROR_MESSAGE) ;
			} // inner if
	    }//if

	    else{

			if( myModParent.isAddable( new_section ) ||
				new_section.getSectNum().equals( myModSection.getSectNum() ) ){

				//if we are here we are changing an existing section
				
				//send the newly changed section back to whoever asked for it
				myModParent.sendSection( new_section );
				
				frame.setDefaultCloseOperation( DO_NOTHING_ON_CLOSE  );
				frame.dispose();
			} else {
				JOptionPane error = new JOptionPane();
				error.showMessageDialog( null, 
							 ("A section with that number " +
							  "is already in your database"),
							 "Error", 
							 JOptionPane.ERROR_MESSAGE) ;
			}
	    }//else
	   
	} // outer if
	
    }//CloseWindowViaOK

    /**
     * a table model for the attributes check box table
     */
    class MyTableModel extends AbstractTableModel{
	
	private String[] columnNames = {"Room Extras", " "};
	
	//set the default data
	private Object[][] data = {
	    {" Overhead Projector",  new Boolean(false)},
	    {" Digitial Projector", new Boolean(false)},
	    {" Ethernet Connections", new Boolean(false)},
	    {" Wireless Ethernet", new Boolean(false)},
	    {" Speaker System", new Boolean(false)},
	    {" Network Printer", new Boolean(false)},
	    {" DVD player", new Boolean(false)}
	};
	    
	/**
	 * returns Column Count
	 *
	 * @return a value of type 'int'
	 */
	public int getColumnCount() {

	    return columnNames.length;
	}//getColumnCount
	
	/**
	 * returns Row Count
	 *
	 * @return a value of type 'int'
	 */
	public int getRowCount() {

	    return data.length;
	}//getRowCount
	
	/**
	 * returns Column Name
	 *
	 * @param col a value of type 'int'
	 * @return a value of type 'String'
	 */
	public String getColumnName(int col) {
	 
	    return columnNames[col];
	}//getColumnName
	
	/**
	 * returns value at specified spot
	 *
	 * @param row a value of type 'int'
	 * @param col a value of type 'int'
	 * @return a value of type 'Object'
	 */
	public Object getValueAt(int row, int col) {
	    
	    return data[row][col];
	}//getValueAt
	
	/**
	 * JTable uses this method to determine the default renderer/
	 * editor for each cell.  If we didn't implement this method,
	 * then the last column would contain text ("true"/"false"),
	 * rather than a check box.
	 *
	 * @param c a value of type 'int'
	 * @return a value of type 'Class'
	 */
	public Class getColumnClass(int c) {
	    
	    return getValueAt(0, c).getClass();
	}//getColumnClass
	
	/**
	 * returns whether a cell is modifiable
	 *
	 * @param row a value of type 'int'
	 * @param col a value of type 'int'
	 * @return a value of type 'boolean'
	 */
	public boolean isCellEditable(int row, int col) {
   
	    //Note that the data/cell address is constant,
            //no matter where the cell appears onscreen.
            if (col == 1) { 
                return true;
            
	    } else {
                return false;
            }
        }//isCellEditable

	/**
	 * returns the value of the specified spot 
	 *
	 * @param value a value of type 'Object'
	 * @param row a value of type 'int'
	 * @param col a value of type 'int'
	 */
	public void setValueAt(Object value, int row, int col) {
	    
	    //update the boolean array of attributes to be passed back
	    //to the Section
	    if( attributes[row] != "1" ){
		attributes[row] = "1";
	    }

	    else{
		attributes[row] = "0";
	    }

	    if (data[0][col] instanceof Integer                        
		&& !(value instanceof Integer)) {                  
	
		try {
		    data[row][col] = new Integer(value.toString());
		    fireTableCellUpdated(row, col);
		    
		} catch (NumberFormatException e) { }
				
	    } else {
		//reset the data vector and update it
		data[row][col] = value;
		fireTableCellUpdated(row, col);
	    }

	}//setValueAt

	/**
	 * This updates the attributes table by converting a 1D array
	 * into a 2D array
	 *
	 * @param newData a 1D array of strings (1's or 0's)
	 */
	public void resetAttributesDataVector( Object[] newData ){
	    
	    Object[][] tempData = new Object[7][2];
	    
	    // to update the 2D array, we need to convert the newData from
	    // a 1D array to a 2D array without replacing the lables in the
	    // columns
	    for( int i=0; i<7; i++){
		
		if( (newData[i]).equals("1") ){
		    data[i][1] = new Boolean(true);
		}

		else{
		    data[i][1] = new Boolean(false);
		}
	    }//for

	    //tell the listener to update itself
	    fireTableDataChanged();

	}//resetDataVector

    }//MyTableModel
   

    /**
     * Keeps track of what in the check box is checked
     */
    class CheckBoxListener implements ItemListener {
        
	public void itemStateChanged(ItemEvent e) {
            
	    //need to figure out first, who called this action, then based 
	    //on that wee need to updata an array to represent if if the day 
	    //is a meeting day.
	    
	    Object source = e.getItemSelectable();
	    
	    //as each source is identified, toggle the value in the days[].  
	    //for example, if its set to true for a section to meet on monday,
	    //set it to flse as meeting on monday
	    if( source == monday ){
		
		if( days[0] ){
		    days[0] = false;
		}
		
		else{
		    days[0] = true;
		} 
	    }
	    
	    else if( source == tuesday ){
		
		if( days[1] ){
		    days[1] = false;
		}
		
		else{
		    days[1] = true;
		}
	    }
	    
	    else if( source == wednesday ){
		
		if( days[2] ){
		    days[2] = false;
		}
		
		else{
		    days[2] = true;
		}
	    }
	    
	    else if( source == thursday ){
		
		if( days[3] ){
		    days[3] = false;
		}
		
		else{
		    days[3] = true;
		} 
	    }

	    else if( source == friday ){

		if( days[4] ){
		    days[4] = false;
		}
		
		else{
		    days[4] = true;
		}
	    }

        }//itemStateChenged
	
    }//CheckBoxListener
    
} // AddSingleSectionWindow






