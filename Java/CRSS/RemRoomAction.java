/**
 * Class that perfoms the remove room action
 *
 * @version    $Id: RemRoomAction.java,v 1.16 2000/05/08 17:05:28 p361-45a Exp $
 *
 * @author     Christopher Bunk
 * @author     Adam Chipalowsky
 * @author     Dan Cooley
 * @author     Eric Ferguson
 * @author     Phil Light
 *
 * Revisions:
 *     $Log: RemRoomAction.java,v $
 *     Revision 1.16  2000/05/08 17:05:28  p361-45a
 *     fixed formatting
 *
 *     Revision 1.15  2000/05/08 00:16:42  etf2954
 *     Fixed the undo()
 *
 *     Revision 1.14  2000/05/05 17:26:34  aec1324
 *     fized a null pointer...yes, there WAS a null
 *     pointer in this file.  Anyway, there was a
 *     problem when the room is removed it tries
 *     to unassign all sections that are associated
 *     with it.  Anyway, if the section was removes
 *     via a remove section earlier, this fails
 *     now it doesn't, I fixed it.  HAs a check for
 *     null...seems like the quickest fix
 *
 *     Revision 1.13  2000/05/05 01:07:00  pel2367
 *     throw an exception when the window is
 *     cancelled
 *
 *     Revision 1.12  2000/04/30 18:26:15  p361-45a
 *     *** empty log message ***
 *
 *     Revision 1.11  2000/04/28 17:18:53  etf2954
 *     Fixed null pointer exception when you click on a blank
 *     room, at the beginning of the program
 *
 *     Revision 1.10  2000/04/28 17:05:39  pel2367
 *     wrote undo()
 *
 *     Revision 1.9  2000/04/28 14:40:36  etf2954
 *     Finished with the code.. now when the user tries to delete
 *     a room that has sections assigned to it, the GUI wil
 *     be called and asked to prompt confirm
 *
 *     Revision 1.8  2000/04/27 21:37:44  etf2954
 *     Working on getting it to ask the user
 *     to confirm deletion of Room if the
 *     room has assignments
 *
 *     Revision 1.7  2000/04/27 21:05:01  etf2954
 *     started to work on it, but bunk still has the lock on
 *     Room.java
 *
 *     Revision 1.6  2000/04/26 17:16:28  etf2954
 *     Added the correct refresh calls
 *
 *     Revision 1.5  2000/04/26 16:15:41  etf2954
 *     Added functionality and error msg
 *
 *     Revision 1.4  2000/04/21 16:31:35  etf2954
 *     Check it out, don't remember what I changed
 *
 *     Revision 1.3  2000/04/18 13:01:07  p361-45a
 *     finished method headers
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

public class RemRoomAction extends CRSSAction implements UndoableAction {

    private Room _room_taken;
    private boolean query;
    private LinkedList _roomAssignments;
    
    /** 
     *constructor to create an object of type RemRoomAction
     *
     * @param gui a value of type 'GUI'
     * @param database a value of type 'CRSSDatabase'
     * @exception BadActionException if an error occurs
     */
    public RemRoomAction( GUI gui, CRSSDatabase database )
        throws BadActionException{
	
	String roomNum = null;
	Section tempSection;
	_gooey = gui;
	_dbase = database;
	query = false;
	
	// First we need to get the highlighted room
	roomNum = _gooey.getHighlightedRoom();
		
	// Check to make sure there is a room highlighted
	if( roomNum != null ) {
	    if( roomNum.length() != 2 ) {
		_room_taken = _dbase.getRoom( roomNum );
		_roomAssignments = _room_taken.getAssignments();
		
		// 
		// Check to make sure that the room doesn't have any
		// assignments
		if( _roomAssignments.size() == 0 ) {
		    query = true;
		    go();
		}
		
		else {
		    _gooey.promptConfirmOnRemoveRoomAction( this );
		} // if
	    }
	}
	
	else {
	    // HAVE THIS CLASS POP UP ERROR LET GUI DO IT LATER
	    JOptionPane error = new JOptionPane();
	    
	    error.showMessageDialog( null, 
	        "You must select a room before you remove one!", 
		"Error", JOptionPane.ERROR_MESSAGE );
	    
	    throw new BadActionException();
	    
	} // if
    }
    
    /**
     * method to remove room from gui and database
     *
     */
    public void go(){ 

	ListIterator iter = _roomAssignments.listIterator( 0 );
	Section tempSection = null;
	
	//
	// Only if someone has set the query to true
	// 	do we want to remove the room
	if( query ) {

	    //
	    // We must unassign all the section before we continue
	    while( iter.hasNext() ) {
	
		tempSection = _dbase.getSection ( ( String ) iter.next() );
		
		if( tempSection != null ){
		    tempSection.unassign();
		}
	    } // while
	    
	    // 
	    // Now we can remove the room and refresh everything
	    _dbase.removeRoom( _room_taken );
	    refreshRoomList();
	    refreshSectList();
	    refreshRoomAttrib();
	    refreshRoomOcc();
	
	} // if
    }
    
    /**
     * sets the new query
     *
     * @param newQuery a value of type 'boolean'
     */
    public void setQuery( boolean newQuery ) {
	query = newQuery;
    }

    /**
     * method to undo remove room
     *
     */
    public void undo(){
	
        // first, put the room back in the database.
        Section tempSection;
        _dbase.addRoom( _room_taken );
        
        // now, remake all of its old assignments, if there
        // were any.
	ListIterator itera = _roomAssignments.listIterator( 0 );

	while( itera.hasNext() ) {
	    tempSection = _dbase.getSection ( ( String ) itera.next() );
	    tempSection.assign( _room_taken.getRoomNum() );
	} // while
        
	refreshSectList();
        refreshRoomList();
        refreshRoomAttrib();
        refreshRoomOcc();
    }
    
} // remove room action class
