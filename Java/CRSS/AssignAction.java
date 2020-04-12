/**
 * Class that perfoms the Assign Section action
 *
 * @version    $Id: AssignAction.java,v 1.15 2000/05/08 03:16:42 p361-45a Exp $
 *
 * @author     Christopher Bunk
 * @author     Adam Chipalowsky
 * @author     Dan Cooley
 * @author     Eric Ferguson
 * @author     Phil Light
 *
 * Revisions:
 *     $Log: AssignAction.java,v $
 *     Revision 1.15  2000/05/08 03:16:42  p361-45a
 *     fixed formatting
 *
 *     Revision 1.14  2000/05/06 17:42:08  etf2954
 *     took out System.out's
 *
 *     Revision 1.13  2000/05/06 14:50:15  pel2367
 *     added a debugging message, not for assignment,
 *     but to check the toSaveString in Section.
 *
 *     Revision 1.12  2000/05/02 03:07:19  aec1324
 *     fixed a null error when assigning a nothing
 *     selected
 *
 *     Revision 1.11  2000/04/29 04:01:54  pel2367
 *     fixed an oversight of mine, if the
 *     section is already assigned to something it cannot
 *     be assigned to any other room.
 *
 *     Revision 1.10  2000/04/28 20:39:59  pel2367
 *     changed the refresh slightly, took out some
 *     System.outs
 *
 *     Revision 1.9  2000/04/28 17:05:39  pel2367
 *     wrote undo().
 *
 *     Revision 1.8  2000/04/28 01:24:57  pel2367
 *     Rewrote it so that go() will be redoable
 *
 *     Revision 1.7  2000/04/28 00:48:38  pel2367
 *     now allows the user to override and
 *     add-on mismatch.
 *
 *     Revision 1.6  2000/04/24 14:45:12  pel2367
 *     hmm... the big mystery as to why this wasn't
 *     refreshing correctly was... it wasn't asking for
 *     any refresh at all.  Chalk it up to the end of a
 *     15 hour day.
 *
 *     Revision 1.5  2000/04/24 02:27:52  pel2367
 *     wrote go().
 *
 *     Revision 1.4  2000/04/23 20:03:26  pel2367
 *     wrote its go() routine.
 *
 *     Revision 1.3  2000/04/18 02:00:10  p361-45a
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

public class AssignAction extends CRSSAction implements
    UndoableAction {

    private Section _anySection;
    private Room _anyRoom;    
    private boolean _refresh;
    
    /**
     * creates an object of type AssignAction
     *
     * @param myGUI copy of 'GUI'
     * @param database copy of 'CRSSDatabase'
     * @exception BadActionException if both room and section are not 
     *            highlighted
     */
    public AssignAction( GUI myGUI, CRSSDatabase database ) 
    	throws BadActionException{

    	_gooey = myGUI;
        _dbase = database;
    	String sectNum = "";
        String roomNum = "";        
	
        sectNum = _gooey.getHighlightedSection();
        roomNum = _gooey.getHighlightedRoom();

        if( sectNum != null && roomNum != null 
	    && !sectNum.equals("  ") && !roomNum.equals("  ") ) {

            // find out just which section and room the user was talking about
	    _anySection = _dbase.getSection( sectNum );
	    
	    if( _anySection.getAssignment().equals( "None") ){
                _anyRoom = _dbase.getRoom( roomNum );
                int fit;
                fit = _dbase.assignable( _anySection, _anyRoom );

                // are those two assignable to each other?
                if( fit == 1 ) {
                    go();
                }
     	
		else if( fit == -1 ) {
		    // the room is large enough to hold the section, but does                     // not have all of the requested add-ons.
                    _gooey.askForAssignConfirmation( this );
		}
                
		else{
                    // sorry, the room is occupied when the section wants
                    // to meet
                    _gooey.defaultErrorMsgWindow(
                        "The room is not free during the specified times." );
                    throw new BadActionException();
                } // else
            } // the section doesn't already have an assignment, does it?
          
	    else{
                _gooey.defaultErrorMsgWindow( "The selected section already" +
					      "has an assignment." );
                throw new BadActionException();
            }
        }
  
        else{
	    _gooey.defaultErrorMsgWindow( "Please select both " +
					  "a room and section." );
            throw new BadActionException();
        } // else
    }
    
    public AssignAction( GUI myGUI, 
                         CRSSDatabase database, 
                         String sectNum, 
                         String roomNum ){
        _gooey = myGUI;
        _dbase = database;
        _anySection = _dbase.getSection( sectNum );
        _anyRoom = _dbase.getRoom( roomNum );
    }
    
    /**
     * sends assign action to the database if assignable
     *
     */
    public void go(){

        // first, make the assignment in the database
        _dbase.assign( _anySection, _anyRoom );
        refreshSectList();
        refreshRoomList();
        refreshRoomOcc();

    } // go()
    
    /**
     * undoes an assignment
     *
     */
    public void undo(){
    	
	// first, unassign the two.
        _dbase.unassign( _anySection.getSectNum() );
        refreshSectList();
        refreshRoomList();
        refreshRoomOcc();
    }
    
} // Assign action class

