/**
 * Class that perfoms the remove section action
 *
 * @version    $Id: RemSectAction.java,v 1.13 2000/05/08 17:09:10 p361-45a Exp $
 *
 * @author     Christopher Bunk
 * @author     Adam Chipalowsky
 * @author     Dan Cooley
 * @author     Eric Ferguson
 * @author     Phil Light
 *
 * Revisions:
 *     $Log: RemSectAction.java,v $
 *     Revision 1.13  2000/05/08 17:09:10  p361-45a
 *     fixed formatting
 *
 *     Revision 1.12  2000/04/28 17:18:53  etf2954
 *     Fixed null pointer exception when you click on a blank
 *     room, at the beginning of the program
 *
 *     Revision 1.11  2000/04/28 17:05:39  pel2367
 *     wrote undo()
 *
 *     Revision 1.10  2000/04/27 20:06:54  etf2954
 *     removed comments
 *
 *     Revision 1.9  2000/04/27 18:16:50  p361-45a
 *     fixed up some minor problems with an if statement and comments
 *
 *     Revision 1.8  2000/04/27 03:52:55  etf2954
 *     wrote remSecion for when assigned
 *
 *     Revision 1.7  2000/04/26 17:16:28  etf2954
 *     Added the correct refresh calls
 *
 *     Revision 1.6  2000/04/26 16:15:41  etf2954
 *     Added functionality and error msg
 *
 *     Revision 1.5  2000/04/21 15:02:41  etf2954
 *     Commented out dans errors
 *
 *     Revision 1.4  2000/04/21 02:02:16  p361-45a
 *     added code for go method
 *
 *     Revision 1.3  2000/04/18 13:03:31  p361-45a
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

public class RemSectAction extends CRSSAction implements UndoableAction{

    private Section _sectionTaken;
    private Room _formerAssignment = null;
    
    /**
     * constructor to create an object of type RemSectAction
     *
     * @param gui a value of type 'GUI'
     * @param database a value of type 'CRSSDatabase'
     */
    public RemSectAction(GUI gui, CRSSDatabase database){
	String sectionNum = null;
	Room tempRoom;
	_gooey = gui;
	_dbase = database;
	
	// First we need to get the hightligted section
	sectionNum = _gooey.getHighlightedSection();
	
	// We must check to see if the string is null
	// ie, we don't have a section selected
	if( sectionNum != null ) {

	    if( sectionNum.length() != 2 ) {
		_sectionTaken = _dbase.getSection( sectionNum );
		
		// get the room which it's assigned to.  that room might
		// still be null, however.  no worries, because we check
		// for that later.  this is really just a matter of 
		// record-keeping for the sake of undo().
		_formerAssignment = 
		    ( (Room) _dbase.getRoom( _sectionTaken.getAssignment() ) );
		
		if (!( _sectionTaken.getAssignment()).equals( "None" )) {
		    // the section is assigned to a room. let's fix that.
		    
		    tempRoom = _dbase.getRoom( _sectionTaken.getAssignment() );
		    tempRoom.unassign( _sectionTaken.getSectNum() );
		    _formerAssignment = tempRoom;
		} // if
		go();
	    } // if
	} 

	else {
	    JOptionPane error = new JOptionPane();
	    error.showMessageDialog( null,
		"You must select a section before you remove one!",
		"Error", JOptionPane.ERROR_MESSAGE );
		} // if

    } // REmSectAction
    
    /**
     * method to remove a section from the gui and database
     *
     */
    public void go() {
	_dbase.removeSection( _sectionTaken );
	refreshSectList();
	refreshSectionAttrib();
	refreshRoomList();
	refreshRoomAttrib();
	refreshRoomOcc();
    }
    
    /**
     * method to undo a remove section action
     *
     */
    public void undo(){
        _dbase.addSect( _sectionTaken );
        
	if( _formerAssignment != null ){
            _dbase.assign( _sectionTaken, _formerAssignment );
	} // can remake an old assignment?
        
	refreshSectList();
        refreshSectionAttrib();
        refreshRoomList();
        refreshRoomAttrib();
        refreshRoomOcc();
    }
    
} // remove section action class
