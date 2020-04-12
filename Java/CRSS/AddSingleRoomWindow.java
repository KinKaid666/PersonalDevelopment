/**
 * this is the window that gets user info for the addRoom action 
 *
 * @version	$Id: AddSingleRoomWindow.java,v 1.24 2000/05/10 17:18:47 etf2954 Exp $
 *
 * @author	Adam E Chipalowsky 
 *
 * Revisions:
 *		$Log: AddSingleRoomWindow.java,v $
 *		Revision 1.24  2000/05/10 17:18:47  etf2954
 *		Made this call the error on the dirty input
 *
 *		Revision 1.23  2000/05/10 17:14:25  etf2954
 *		made it check for bad input here on mod action
 *
 *		Revision 1.22  2000/05/10 15:40:39  etf2954
 *		Removed System.out's
 *
 *		Revision 1.21  2000/05/08 02:57:04  p361-45a
 *		fixed formatting
 *
 *		Revision 1.20  2000/05/05 17:11:15  aec1324
 *		changed "==" to ".equals()" for a string
 *		compairison down where the attributes are
 *		set
 *
 *		Revision 1.19  2000/05/05 14:50:52  pel2367
 *		sends the null section on cancel.
 *
 *		Revision 1.18  2000/05/05 02:23:55  pel2367
 *		now sends a null section on cancel.
 *
 *		Revision 1.17  2000/05/05 01:07:00  pel2367
 *		threw an exception
 *
 *		Revision 1.16  2000/05/03 15:05:52  etf2954
 *		nothing
 *
 *		Revision 1.15  2000/04/30 19:51:02  aec1324
 *		the rest of the fields are now updated when using
 *		this method to edit rooms
 *
 *		Revision 1.14  2000/04/29 21:13:14  aec1324
 *		got rid of some System.out's
 *
 *		Revision 1.13  2000/04/29 21:09:32  aec1324
 *		rewrote this so it is capable of both creation
 *              of a new room and be used as the edit window for
 *              a room
 *
 *		Revision 1.12  2000/04/24 00:54:55  etf2954
 *		Made it so the # is the first field when tabed, and it
 *		goes, NUMBER, NAME/DESCRIPTION, CAPACITY
 *
 *		Revision 1.11  2000/04/22 18:02:57  pel2367
 *		made the room number include dashes.
 *
 *		Revision 1.10  2000/04/21 15:15:47  etf2954
 *		Now the class only lets a room that is not in the database
 *		be added
 *
 *		Revision 1.9  2000/04/21 14:58:00  etf2954
 *		Added Error checking for blank fields
 *
 *		Revision 1.8  2000/04/21 02:54:05  etf2954
 *		Made it so that you can only enter a certain # of digit
 *		or only digits in some of the fields but time ran out
 *
 *		Revision 1.7  2000/04/20 23:05:50  aec1324
 *		added the ability to update the attributes
 *		table.
 *
 *		Revision 1.6  2000/04/20 21:39:17  etf2954
 *		Added the functionaliy to add a room with clean input
 *
 *		Revision 1.5  2000/04/20 16:28:10  p361-45a
 *		finished method headers
 *
 *		Revision 1.4  2000/04/19 02:19:46  etf2954
 *		did nothing..just looked around...noticed flaws .
 *
 *		Revision 1.3  2000/04/18 22:09:24  aec1324
 *		added more accessor methods
 *
 *		Revision 1.2  2000/04/18 21:42:05  etf2954
 *		*** empty log message ***
 *
 *		Revision 1.1  2000/04/18 21:15:15  aec1324
 *		Initial revision
 *
 */
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.io.*;
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
public class AddSingleRoomWindow extends JFrame {
    
    private JFrame frame;
    private JButton okButton = null;
    private JButton cancelButton = null;
    private TableColumn tableColumn;
    private Border loweredBevel;
    private MyTableModel myModel;
    private String btnString1 = "Enter";
    private String btnString2 = "Cancel";
    private JOptionPane optionPane;
    private JPanel panel;
    private JPanel innerPanel;
    private JLabel emptyLabel;
    private Dimension dimen = new Dimension( 660, 210 );
    private String finalRoomNumber;
    private String finalDescription;
    private FixedLengthDigitTextField roomNum4TextField;
    private FixedLengthDigitTextField roomNum3TextField;
    private FixedLengthTextField roomDescriptionTextField;
    private FixedLengthDigitTextField roomCapacityTextField;

    private AddRoomAction myParent;
    private Object attributes[];
    private int finalIntCapacity;
    private Room new_room = null;
    
    private String roomNumber;
    private String roomDescription;
    private String roomCapacity;
    private String roomAttributesTable;
    private String dash;
    private String spacer;

    private JLabel roomAtt;
    private JLabel roomNum;
    private JLabel roomDesc;
    private JLabel roomCap;
    private JLabel aDash1;
    private JLabel aDash2;
    private JLabel aSpacer;

    private GridBagLayout gridbag;
    private GridBagConstraints c;

    private JTable table;

    private String windowTitle;
    
    private ModRoomAction myModParent;
    private boolean isThisAddingARoom; 
	private Room oldModRoom;


    /**
     * constructor to create an object of type AddSingleRoomWindow
     *
	 * @param parent is a reference to this classes parent
     */
    public AddSingleRoomWindow( AddRoomAction parent ){
	myParent = parent;
	attributes = new Object[7];
	for( int i = 0; i < 7; i++ ){
	    attributes[i] = "0";
	}
	//set the title of this window to be Add Section
	windowTitle = "Add A Room";

	//for later use we need to know if we are creating 
	//a new room or changing an existing one.  Do that by setting 
	//this to true
	isThisAddingARoom = true;

	//set up the window.
	setUpWindow();

	
	
	
    }// AddSingleRoomWindow()

    public AddSingleRoomWindow( ModRoomAction parent, Room sentRoom ){
		
	oldModRoom = sentRoom;
	myModParent = parent;
	
	//set the title of this window to be Add Section
	windowTitle = "Edit Room";
	
	//later on we are going to need to know that we are changing
	//an existing room.  Do that by setting this to false
	isThisAddingARoom = false;

	//set up the window
	setUpWindow();

      	//fill in the text fields with the room to be edited information
	roomNum4TextField.setText( (sentRoom.getRoomNum()).substring(0,2) );
	roomNum3TextField.setText( (sentRoom.getRoomNum()).substring(3,7) );
	
	//fill in the room description
	roomDescriptionTextField.setText( sentRoom.getDescription() );
	
	//fill in the room capacity
	roomCapacityTextField.setText( (new Integer
					(sentRoom.getCapac())).toString() );

	//now set the attributes as they are in the sent room
	attributes = sentRoom.getAttrib();
	myModel.resetAttributesDataVector( attributes );	
	
	//declair the action listeners so we know when the user has pressed
	//ok or cancel
	cancelButton.addActionListener(new ActionListener() {
	  
	    /**
	     * allow the user to cancel the editing operation
	     *
	     * @param e a value of type 'ActionEvent'
	     */
	    public void actionPerformed(ActionEvent e){
		CloseWindowViaCancel();
	    }
	});
	
	okButton.addActionListener(new ActionListener() {

	    /**
	     * when the user clicks OK, the final assignments are to 
	     * be set and then the information is sent back to the action
	     * who clalled for this window to be opened
	     *
	     * @param e a value of type 'ActionEvent'
	     */
	    public void actionPerformed(ActionEvent e) {
		CloseWindowViaOk();
	    }
	});

    }//AddSingleRoomWindow

    /**
     * method to get information about a room from user
     *
     */
    public void getUserInfo(){
	
	cancelButton.addActionListener(new ActionListener() {
	    
	    /**
	     * allow the user to cancel the editing operation
	     *
	     * @param e a value of type 'ActionEvent'
	     */
	    public void actionPerformed(ActionEvent e) {
		CloseWindowViaCancel();
	    }
	});
	
	okButton.addActionListener(new ActionListener() {
	    
	    /**
	     * when the user clicks OK, the final assignments are to 
	     * be set and then the information is sent back to the action
	     * who clalled for this window to be opened
	     *
	     * @param e a value of type 'ActionEvent'
	     */
	    public void actionPerformed(ActionEvent e) {
		CloseWindowViaOk();
	    }
	});

    }//getUserInfo()
    
    /**
     * creates and displys the add single room window.  Also serves as the 
     * edit room window
     *
     */
    private void setUpWindow(){
 
	//create the strings
	roomNumber = "Enter room Number ";
	roomDescription = "Enter room description ";
	roomCapacity = "Enter room capacity";
	roomAttributesTable = "Attributes";
	dash = "-";
	spacer = "    ";
		
	//create the textfields
	roomNum4TextField = new FixedLengthDigitTextField( null, 2 );
	roomNum3TextField = new FixedLengthDigitTextField( null, 4 );
	roomDescriptionTextField = new FixedLengthTextField( 25 );
	roomCapacityTextField = new FixedLengthDigitTextField( null, 3 );
	
	roomAtt = new JLabel( roomAttributesTable );
	roomNum = new JLabel( roomNumber );
	roomDesc = new JLabel( roomDescription );
	roomCap = new JLabel( roomCapacity );
	aDash1 = new JLabel( dash );
	aDash2 = new JLabel( dash );
	aSpacer = new JLabel( spacer);

	//set up the layout tool
	gridbag = new GridBagLayout();
	c = new GridBagConstraints();
	
	//set up the title in the frame
	frame = new JFrame(windowTitle);
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
	
	okButton = new JButton( "Enter" );
	cancelButton = new JButton( "Cancel" );
	
	//create the user edit room number area
	innerPanel = new JPanel();
	innerPanel.add( roomNum4TextField );
	innerPanel.add( aDash1 );
	innerPanel.add( roomNum3TextField );

	//attributes label
	c.weightx = 1.0;
	c.weighty = 1.0;
	c.gridx = 7;
	c.gridy = 0;
	c.anchor = c.WEST;
	gridbag.setConstraints(roomAtt, c);
		
	// "Enter room number"
	c.weightx = 1.0;
	c.weighty = 1.0;
	c.gridx = 0;
	c.gridy = 1;
	c.anchor = c.WEST;
	gridbag.setConstraints(roomNum, c);
	
	// "Enter room name"
	c.weightx = 1.0;
	c.weighty = 1.0;
	c.gridx = 0;
	c.gridy = 2;
	c.anchor = c.WEST;
	gridbag.setConstraints(roomDesc, c);
	
	// "Enter room capacity"
	c.weightx = 1.0;
	c.weighty = 1.0;
	c.gridx = 0;
	c.gridy = 3;
	c.anchor = c.WEST;
	gridbag.setConstraints(roomCap, c);
	
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
	
	//the room name enter area
	c.gridx = 1;
	c.gridy = 2;
	c.anchor = c.WEST;
	c.gridwidth = 6;
	gridbag.setConstraints(roomDescriptionTextField, c);
	
	//the room capacity enter area
	c.gridx = 1;
	c.gridy = 3;
	c.anchor = c.WEST;
	c.gridwidth = 1;
	gridbag.setConstraints(roomCapacityTextField, c);
	
	//the table
	c.gridx = 7;
	c.gridy = 1;
	c.gridwidth = 1;
	c.gridheight = 3;
	gridbag.setConstraints(table, c);
	
	//the Enter button
	c.gridx = 6;
	c.gridy = 5;
	c.gridwidth = 1;
	c.gridheight = 1;
	gridbag.setConstraints(okButton, c);
	
	//the cancel button
	c.gridx = 7;
	c.gridy = 5;
	c.anchor = c.CENTER;
	gridbag.setConstraints(cancelButton, c);
	
	//add all the components to the frame
	panel.add( roomAtt );
	panel.add( roomNum );
	panel.add( roomDesc );
	panel.add( roomCap );
	panel.add( aSpacer );
	panel.add( innerPanel );
	//innerPanel.setNextFocusableComponent( roomDescriptionTextField );
	panel.add( roomDescriptionTextField );
	panel.add( roomCapacityTextField );
	panel.add( table );
	panel.add( okButton );
	panel.add( cancelButton );

	//pack everything up and tell it to display itself
	frame.getContentPane().add( panel, BorderLayout.CENTER);
	frame.pack();
	frame.setVisible(true);
		
    }// setUpWindow()

    /**
     * method to close window with no changes to anything
     *
     */
    private void CloseWindowViaCancel(){

	frame.setDefaultCloseOperation( DO_NOTHING_ON_CLOSE  );
	frame.dispose();

        if( myModParent != null ){
            myModParent.sendRoom( null );
        }

	//make sure there is a ModRoomAction to send this thing back to
        else if( myParent != null ){
            myParent.sendRoom( null );
        }
    } // CloseWindowViaCancel
    
    /**
     * method to update the database and everything else
     *
     */
    private void CloseWindowViaOk(){
		
	//
	// Check to make sure that the user
	// has entered data for the text fields
	//
	if( roomNum4TextField.getText().length() < 2 || 
	    roomNum3TextField.getText().length() < 4 ||
	    roomCapacityTextField.getText().length() == 0 ) {
	    
	    //
	    // Since the user has chosen to put in less than the actual
	    // Needed info, let's throw up a error box
	    //
	    JOptionPane error = new JOptionPane();
	    error.showMessageDialog( null, "You have left one or more "
				     +"fields incomplete!", 
				     "Error", JOptionPane.ERROR_MESSAGE ) ;
		} // if

	else { 
	    finalRoomNumber = roomNum4TextField.getText() + "-" +
		roomNum3TextField.getText();

	    finalDescription = roomDescriptionTextField.getText();
	    
	    finalIntCapacity = (( new Integer
			      (roomCapacityTextField.getText()).intValue())); 

	    new_room = new Room( finalRoomNumber, 
				 attributes, 
				 finalIntCapacity , 
				 finalDescription );	
				    
	    if( isThisAddingARoom ){
		
			if ( myParent.isAddable( new_room ) ) {
				myParent.sendRoom( new_room );
				frame.setDefaultCloseOperation( DO_NOTHING_ON_CLOSE  );
					frame.dispose();
			}
			
			else {
				JOptionPane error = new JOptionPane();
				error.showMessageDialog( null, "A room with that number "
						  +"is already in our database",
						  "Error", JOptionPane.ERROR_MESSAGE);
			} // inner inner if
	    }//inner if
	    else{
			if( myModParent.isAddable( new_room ) ||
				new_room.getRoomNum().equals( oldModRoom.getRoomNum() ) ){
				//if we are here we are changing an existing room
				//send the newly changed section back to whoever asked for it
				myModParent.sendRoom( new_room );
				
				frame.setDefaultCloseOperation( DO_NOTHING_ON_CLOSE  );
				frame.dispose();
			} else {
				JOptionPane error = new JOptionPane();
				error.showMessageDialog( null, "A room with that number "
						  +"is already in our database",
						  "Error", JOptionPane.ERROR_MESSAGE);
			}
	    }//inner else
	    
	} // outer else
    } // CloseWindowViaOk
    

    /**
     * This class will handle all of the actions that take place in the tabl
     *
     */
    class MyTableModel extends AbstractTableModel{
	private String[] columnNames = {"Room Extras", " "};
	
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
	 * returns column count
	 *
	 * @return column count
	 */
	public int getColumnCount() {
	    return columnNames.length;
	} // getColumnCount
	
	/**
	 * returns row count
	 *
	 * @return a value of type 'int'
	 */
	public int getRowCount() {
	    return data.length;
	} // getRowCount
	
	/**
	 * returns the column name
	 *
	 * @param col a value of type 'int'
	 * @return a value of type 'String'
	 */
	public String getColumnName(int col) {
	    return columnNames[col];
		} // getColumnName
	
		/**
		 * returns value at the specified row and column
		 *
		 * @param row a value of type 'int'
		 * @param col a value of type 'int'
		 * @return a value of type 'Object'
		 */
	public Object getValueAt(int row, int col) {
	    return data[row][col];
	} // getValueAt
	
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
	} // getColumnClass
		
	/**
	 * returns whether or not a specific cell is modifiable
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
	    } 
	    
	    else {
		return false;
	    } // if
	} // isCellEditable
	
	/**
	 * sets value at specified row and column
	 *
	 * @param value a value of type 'Object'
	 * @param row a value of type 'int'
	 * @param col a value of type 'int'
	 */
	public void setValueAt(Object value, int row, int col) {
	    
	    if( !(attributes[row]).equals("1") ){
		attributes[row] = "1";
	    }
	    else{
		attributes[row] = "0";
	    } // if
	    
	    if (data[0][col] instanceof Integer                        
		&& !(value instanceof Integer)) {                  
		
		try {
		    data[row][col] = new Integer(value.toString());
		    fireTableCellUpdated(row, col);
		} catch (NumberFormatException e) { }
	    } else {
		data[row][col] = value;
		fireTableCellUpdated(row, col);
	    } // if
	} // setValueAt
	
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
		
    } // MyTableModel

} // AddSingleRoomWindow



