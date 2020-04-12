/**
 * Class that perfoms the add room action
 *
 * @version    $Id: AddRoomAction.java,v 1.34 2000/05/10 14:30:47 pel2367 Exp $
 *
 * @author     Christopher Bunk
 * @author     Adam Chipalowsky
 * @author     Dan Cooley
 * @author     Eric Ferguson
 * @author     Phil Light
 *
 * Revisions:
 *     $Log: AddRoomAction.java,v $
 *     Revision 1.34  2000/05/10 14:30:47  pel2367
 *     threw a badActionException when the user cancels the file
 *     browser. this keeps cancelled load from file from
 *     being pushed on the stack
 *
 *     Revision 1.33  2000/05/08 02:21:27  p361-45a
 *     expanded and other formatting issues
 *
 *     Revision 1.32  2000/05/05 02:23:55  pel2367
 *     tinkered, fixed some stuff but broke
 *     other stuff
 *
 *     Revision 1.31  2000/05/02 17:46:02  cmb3548
 *     Constructor now takes gui and dbase
 *
 *     Revision 1.30  2000/05/02 03:05:24  cmb3548
 *     New constructor added for adding rooms from file
 *
 *     Revision 1.29  2000/04/29 21:09:32  aec1324
 *     started implementation
 *
 *     Revision 1.28  2000/04/28 17:05:39  pel2367
 *     wrote undo()
 *
 *     Revision 1.27  2000/04/27 02:24:27  etf2954
 *     Changed the description on the filter to
 *     'Room Lists (*.room)'
 *
 *     Revision 1.26  2000/04/26 16:43:38  etf2954
 *     Added Extension File Filter to popup window
 *
 *     Revision 1.25  2000/04/26 02:40:37  etf2954
 *     Added the window popup if the file contains rooms that already exist
 *
 *     Revision 1.24  2000/04/25 15:47:36  etf2954
 *     Remove the errant refresh calls on the adding go function
 *
 *     Revision 1.23  2000/04/24 17:20:18  aec1324
 *     I think I took a look at how the file IO was
 *     working so it could be a model for the
 *     add section from file and in the
 *     process I cleaned up the coad some
 *
 *     Revision 1.22  2000/04/24 14:45:12  pel2367
 *     called for some additional refreshes at the end of a success
 *     but now it bombs.
 *
 *     Revision 1.21  2000/04/24 04:41:36  etf2954
 *     Fixed the updating room attributes
 *
 *     Revision 1.20  2000/04/24 03:07:33  etf2954
 *     Corrected the isAddable function
 *
 *     Revision 1.18  2000/04/24 02:17:08  etf2954
 *     Added the correct design functionality
 *
 *     Revision 1.17  2000/04/23 23:55:18  etf2954
 *     Function that adds rooms looks for the "<ROOMS>" header
 *     before it starts reading rooms
 *
 *     Revision 1.16  2000/04/23 23:39:46  etf2954
 *     Added clean functinality for added from file
 *
 *     Revision 1.15  2000/04/23 22:32:28  pel2367
 *     fixed the GUI name (now _gooey)
 *     likewise for _dbase
 *
 *     Revision 1.14  2000/04/23 20:36:55  pel2367
 *     commented out the refreshRoom() at end of go()
 *     but still getting errors.
 *
 *     Revision 1.13  2000/04/22 22:05:36  pel2367
 *     something fizzled.
 *
 *     Revision 1.12  2000/04/22 17:36:05  etf2954
 *     Added part of the functionality or the reading from the file
 *
 *     Revision 1.11  2000/04/21 16:31:35  etf2954
 *     Added the functinality for reading from a file
 *
 *     Revision 1.10  2000/04/21 15:15:47  etf2954
 *     Now the class only lets a room that is not in the database
 *     be added
 *
 *     Revision 1.9  2000/04/20 23:05:50  aec1324
 *     changed attributes from a boolean[]
 *     to an Object[]
 *
 *     Revision 1.8  2000/04/20 21:39:17  etf2954
 *     Added the functionality to add a room with clean input
 *
 *     Revision 1.7  2000/04/19 14:28:38  etf2954
 *     Commented out unworking parts
 *
 *     Revision 1.6  2000/04/19 02:25:52  etf2954
 *     didn't do anything
 *
 *     Revision 1.5  2000/04/18 22:09:24  aec1324
 *     now creates and askes for user info
 *
 *     Revision 1.4  2000/04/18 21:42:05  etf2954
 *     Started functionality, go works :)
 *
 *     Revision 1.3  2000/04/18 01:38:03  p361-45a
 *     added method headers
 *
 *     Revision 1.2  2000/04/17 01:26:36  p361-45a
 *     Action name changed to CRSS Action
 *
 *     Revision 1.1  2000/04/16 22:51:51  p361-45a
 *     Initial revision
 *
 */

import java.io.*;
import java.lang.*;
import java.util.*;
import javax.swing.*;

public class AddRoomAction extends CRSSAction implements
            UndoableAction{
            
    private int _data_source;
    private LinkedList _additions = new LinkedList();
    private LinkedList unAddableRooms = new LinkedList();
    private AddSingleRoomWindow newRoomWindow;
    private Operator _operator;
    
    /**
     * the constructor will create an object of type AddRoomAction
     *
     * @param dataSrc to decide how the rooms are being added
     */
    public AddRoomAction( GUI gui, 
                          CRSSDatabase database, 
                          int dataSrc, 
                          Operator oPerator ) 
        throws IOException, BadActionException{

        _operator = oPerator;
	String[] extensions = { "room" };
	_gooey = gui;
	_dbase = database;
	_data_source = dataSrc;
	
	//
	// If the _data_source is one, then we know that the data is 
	// coming from user input
	//
	if( _data_source == 0 ) {
	    
	    newRoomWindow = new AddSingleRoomWindow( this );
	    
	    
	    promptForNewRoom();
	} 
	
	else if( _data_source == 1 ) {
	    ExtensionFileFilter filter = 
		new ExtensionFileFilter( extensions, "Room Lists (*.room)" );
	    JFileChooser fileChooser = new JFileChooser();
	    fileChooser.addChoosableFileFilter( filter );
	    fileChooser.setFileFilter( filter );
	    int returnVal = fileChooser.showOpenDialog( _gooey ); 
	    if( returnVal == JFileChooser.APPROVE_OPTION ) { 
		readRooms( fileChooser.getSelectedFile() );
	    } // if 
        else{
            throw new BadActionException();
        }
	}

    }
    
    /**
     * This constructor adds rooms to the database from the specified file
     *
     */
    public AddRoomAction( GUI daGui, CRSSDatabase db, File file ){
        try{
	    _gooey = daGui;
	    _dbase = db;
	    readRooms( file );
	} // try
	catch(Exception e){System.out.println("Error while readin in rooms");}

    } // AddRoomAction

    /**
     * This method will send the action to database to do actual changes
     *
     * @exception BadActionException if an error occurs
     */
    public void go() {
		ListIterator iter = _additions.listIterator( 0 );
		while( iter.hasNext() ) {
			_dbase.addRoom( ( Room ) iter.next() );
		}
		refreshRoomList();
        refreshRoomAttrib();
        refreshRoomOcc();
    }

	/**
	 * this will get the room from the Window
	 *
	 * @param room is the room that the window creates
	 */
    public void sendRoom( Room new_room ){
        if( new_room != null ){
            _additions.add(new_room);
            go();
        }

        else{
            _operator.actionFailed();
        }
    }

    /**
     * This course allows the user to check to see if the
     * new room is addable without giving the window access
     * to the database
     *
     * @param   newRoom is the new room in question
     *
     * @return  is the asnwer to the 'isAddable' question
     */
    public boolean isAddable( Room newRoom ) {  
	boolean is_addable = true;
	ListIterator iter = _additions.listIterator( 0 );
	
	if( _dbase.addable( newRoom ) ) {
	    
	    while( iter.hasNext() ) {
		
		if( ( ( Room ) iter.next() ).getRoomNum().
		    equals( newRoom.getRoomNum() ) ) {
		    is_addable = false;
		} // if
	    } // while
	}//if addable 
	
	else {
	    is_addable = false;
	} // else
	
	return is_addable;
	
    } // is_addable
    
	
    /**
     * This is a helper function that will read in the rooms 
     * from the selected file
     *
     * @param file the file the system is reading from
     * @exception is thrown if the file is not found
     */
    public void readRooms( File file ) throws IOException {
	Room tempRoom;
	String roomNum, roomDesc;
	int roomCap;
	Object[] attributes = new Object[7];
	AddListError error;
	     
	//
	// Set the initial values of the attributes
	//
	for( int x = 0 ; x < 7 ; x++ ) {
	    attributes[x] = "0";
	}

	try {
	    //
	    //Initiate the buffer reader, and teh stream tokenizer
	    //
	    BufferedReader in =
		new BufferedReader( new FileReader( file ) );
	    
	    //
	    // Read the line, that is the header for the file
	    // that looks like <ROOMS>
	    //
	    StreamTokenizer st = new StreamTokenizer( in );
	    
	    //
	    // Set the character the will start and end 
	    // the token fields
	    //
	    st.quoteChar( ';' );
	    st.nextToken();
	    
	    //
	    // Start reading file
	    //
	    while( !st.sval.equals( "<ROOMS>" ) ){
		st.nextToken();
	    }
	    
	    //
	    // While we haven't reached the last token read
	    // in all 4 fields and make a room outta every line
	    // found in the file, but stop when you get
	    // to the "<END>" token
	    //
	    st.nextToken();
	    
	    while( !st.sval.equals( "<END>" ) ){
		roomNum = st.sval;
		st.nextToken();
		roomCap = new Integer( st.sval ).intValue();
		st.nextToken();
		roomDesc = st.sval;
		st.nextToken();
		
		for( int i = 0; i < 7 ; i++ ) {
		    attributes[i] = st.sval.substring(i, (i + 1));
		}
		
		st.nextToken();
		tempRoom = new Room( roomNum, ( Object[] ) 
				     attributes.clone() , 
				     roomCap, roomDesc );
		
		if( isAddable( tempRoom ) ) {
		    _additions.add(	tempRoom );
		}
		
		else {                                        
		    unAddableRooms.add ( tempRoom.getRoomNum() );
		} // if
	    } // While
	}
	
	catch ( FileNotFoundException fnfe ) {
	    
	    System.out.println( "That file wasn't found!" );
	} // try/catch
	
	if( unAddableRooms.size() > 0 ) {
	    error = new AddListError( "rooms", unAddableRooms );
	}
	
	go();
    } // readRooms
    

    /**
     * opens the window to get user information for adding a single 
     * room only.
     *
     */
    private void promptForNewRoom() {
    	newRoomWindow.getUserInfo();
    }


    /**
     * method to undo this action
     *
     */
    public void undo(){
        // remove every room in our list of additions.
	
	ListIterator iter = _additions.listIterator( 0 );

	    while( iter.hasNext() ) {
		_dbase.removeRoom( ( Room ) iter.next() );
		}

	refreshRoomList();
        refreshRoomAttrib();
        refreshRoomOcc();
    }

    
}


