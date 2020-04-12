/**
 * Class that perfoms the Unassign action
 *
 * @version    $Id: UnassignAction.java,v 1.7 2000/05/08 17:22:19 p361-45a Exp $
 *
 * @author     Christopher Bunk
 * @author     Adam Chipalowsky
 * @author     Dan Cooley
 * @author     Eric Fergesun
 * @author     Phil Light
 *
 * Revisions:
 *     $Log: UnassignAction.java,v $
 *     Revision 1.7  2000/05/08 17:22:19  p361-45a
 *     fixed formatting
 *
 *     Revision 1.6  2000/05/05 00:00:05  pel2367
 *     squelched a null pointer or two.
 *
 *     Revision 1.5  2000/04/28 17:05:39  pel2367
 *     initial writing of undo()
 *
 *     Revision 1.4  2000/04/25 22:00:45  pel2367
 *     wrote it, twelve minutes from blank to fully functional.
 *
 *     Revision 1.3  2000/04/18 13:29:00  p361-45a
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

public class UnassignAction extends CRSSAction implements UndoableAction {

    private String _the_room_num;
    private String _the_section_num;
    private Section anySect;
    private Room anyRoom;
    
   
    /**
     * constructor to create an object of type UnassignAction
     *
     * @param myGUI a value of type 'GUI'
     * @param theDatabase a value of type 'CRSSDatabase'
     * @exception BadActionException if an error occurs
     */
    public UnassignAction( GUI myGUI, CRSSDatabase theDatabase )
    	throws BadActionException{
    	_gooey = myGUI;
        _dbase = theDatabase;
        String sectNum = _gooey.getHighlightedSection();
        
	if( sectNum != null ) {
	    anySect = _dbase.getSection( sectNum );
        
	    if( !(anySect.getAssignment()).equals( "None" ) ){
            	anyRoom = _dbase.getRoom( anySect.getAssignment() );
            }
	    
            else{
            	_gooey.defaultErrorMsgWindow("That section has no assignment.");
                throw new BadActionException();
            }
        }
	
        else{
	    _gooey.defaultErrorMsgWindow( "Please select a section." );
            throw new BadActionException();
        }

        go();        
            
    }
    
    /**
     * method to unassign a room from a section
     *
     */
    public void go(){
    
	_dbase.unassign( anySect.getSectNum() );
        refreshRoomOcc();
        refreshSectList();
    }
    
    /**
     * method to undo an unassignment
     *
     */
    public void undo(){
    
	_dbase.assign( anySect, anyRoom );
        refreshRoomOcc();
        refreshSectList();
    }
    
} // Unassign action class
