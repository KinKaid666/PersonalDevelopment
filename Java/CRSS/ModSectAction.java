/**
 * Class that perfoms the Modify section action
 *
 * @version    $Id: ModSectAction.java,v 1.19 2000/05/10 17:18:47 etf2954 Exp $
 *
 * @author     Christopher Bunk
 * @author     Adam Chipalowsky
 * @author     Dan Cooley
 * @author     Eric Ferguson
 * @author     Phil Light
 *
 * Revisions:
 *     $Log: ModSectAction.java,v $
 *     Revision 1.19  2000/05/10 17:18:47  etf2954
 *     Made the window check for dirty input
 *
 *     Revision 1.18  2000/05/10 16:19:08  etf2954
 *     Correct the error checking to see if the new
 *     Section is addable, but if the section # hasn't changed
 *     the room won't be addable, but we still need to proceed
 *
 *     Revision 1.17  2000/05/09 21:50:01  aec1324
 *     fixed a problem where if the user edits a section and
 *     changes the number to a number that is already in the
 *     database it dies.  Now there is a check to make sure
 *     the section does not already exist
 *
 *     Revision 1.16  2000/05/08 16:50:23  p361-45a
 *     fixed formatting
 *
 *     Revision 1.15  2000/05/06 14:29:34  aec1324
 *     fixed a null pointer.  This now des not try and assign section
 *     or I mean a room if there is no room assignment
 *
 *     Revision 1.14  2000/05/05 14:50:52  pel2367
 *     handles a cancel by accepting a null
 *     section in sendSection.
 *
 *     Revision 1.13  2000/05/05 01:07:00  pel2367
 *     threw an exception
 *
 *     Revision 1.12  2000/05/04 01:10:28  aec1324
 *     refrenced the code for undo in ModRoomAction
 *
 *     Revision 1.11  2000/05/01 23:31:28  aec1324
 *     added the refresh commands to both undo and redo
 *     so now the table is refreshed everytime there is a
 *     change
 *
 *     Revision 1.10  2000/04/30 19:51:02  aec1324
 *     got rid of another System.out...
 *
 *     Revision 1.9  2000/04/29 21:09:32  aec1324
 *     got rid of a null refrence when the user
 *     clicks on edit section when nothing is
 *     selected
 *
 *     Revision 1.8  2000/04/29 19:24:51  aec1324
 *     finished getting all the conditional error popup
 *     windows up and also implemented UNDO.  Everything
 *     seems to work...I hope, even undo works
 *
 *     Revision 1.7  2000/04/28 21:56:06  aec1324
 *     got the newly edited section to take the place
 *     of the old sections assignments room if and
 *     only if the times and days were not changed.
 *     If they are changed, then it is unassigned
 *     and the user is told about it
 *
 *     Revision 1.6  2000/04/28 17:05:57  aec1324
 *     added functionality to go
 *
 *     Revision 1.5  2000/04/28 16:31:19  aec1324
 *     got rid of the exception throwing around go
 *
 *     Revision 1.4  2000/04/27 02:01:08  aec1324
 *     started adding implementation for editing a
 *     section
 *
 *     Revision 1.3  2000/04/18 12:58:37  p361-45a
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

public class ModSectAction extends CRSSAction implements
            UndoableAction{

    private Section _older_version;
    private Section _new_version;
    private String sectionToChange;
    private AddSingleSectionWindow editSectionWindow;
    private CRSSDatabase dataBase;
    private GUI theGUI;
    private Operator _operator;
    private boolean timeAndDaysAreSame;
    private boolean doesThisHaveAnAssignment;
    private boolean doCapAndEnrolMatch; 
    private Room reassignOldSectionRoom;

    /**
     * constructor to create an object of type ModSectAction
     *
     */
    public ModSectAction( GUI gui, CRSSDatabase database, Operator oPerator )
        throws BadActionException{
	
	_operator = oPerator;
	_gooey = gui;
	_dbase = database;

	//set the booleans to assume the sections are the smae there is an
	//assignment that needs to be figurered out
	timeAndDaysAreSame = true;
	doesThisHaveAnAssignment = true;
	doCapAndEnrolMatch = true;
	
	//first we need to get the hilighted section from the gui
	sectionToChange = _gooey.getHighlightedSection();

	//check to make sure the user is not tring to edit a
	//blank section
	if ( (sectionToChange != null) && !sectionToChange.equals("  ") ){
	    
	    //now we need to goto the database and grab a copy of it
	    //out so we can send it to the window
	    _older_version = _dbase.getSection( sectionToChange );
	    
	    //now have the window load up the section and get
	    //any user modifications
	    editSectionWindow = 
		new AddSingleSectionWindow( this, _older_version);
	}
	
	else{
	    //if the user did not select a section, we need to tell them
	    //about it
	    _gooey.defaultErrorMsgWindow("Please select a section to edit");
	
	    throw new BadActionException();
	}
	
    }//ModSectAction
    
    /**
     * the edit window will call this and send it the new section
     *
     * @param new_section a value of type 'Section'
     */
    public void sendSection( Section new_section ){
	Room tempRoom;
	boolean[] oldDays;
	boolean[] newDays;
	int fit = 0;
	Object[] sectionAttrib;
	Object[] roomAttrib;
	if( new_section != null ){
	    _new_version = new_section;

	    //first off check to make sure the section is addable, meaning
	    //that there is no other section in the database that has the 
	    //smae number
	    
	    if ( _dbase.addable( _new_version ) ||  
			( _new_version.getSectNum().equals( _older_version.getSectNum())) ){


		//now we need to worry about if the old section was previously
		//assigned to a room.  If it was we need to make sure the 
		//days and
		//times were not changed.  If they were not changed then go ahead
		//and replace the assignment.  If they differ, prompt the user
		//to do reassign at a later point
		
		//make sure the days are the same
		oldDays = _older_version.getDays();
		newDays = _new_version.getDays();
		
		for(int i=0; i<5; i++){
		    
		if( !(oldDays[i] == newDays[i]) ){
		    timeAndDaysAreSame = false;
	        }
		
		}//for
		//check to make sure the times are the same
		if( !(timeAndDaysAreSame &&
		      (_older_version.getStartTime() == 
		       _new_version.getStartTime()) 
		      && (_older_version.getEndTime() ==  
			  _new_version.getEndTime())) ){
		    
		    timeAndDaysAreSame = false;
		}//if
		
		//confirm that the old section had an assignment
		if( (_older_version.getAssignment()).equals("None")  ){
		    doesThisHaveAnAssignment = false;
		    
		}
		
		//check to make sure the attributes and section size fit the
		//room.  Now is the time to prompt the user if something 
		//is wrong
		if( doesThisHaveAnAssignment ){
		    tempRoom = _dbase.getRoom((_older_version.getAssignment()));
		    
		    //check to make sure the section enrollment still matches 
		    //the
		    //room capacity.  If it doesn't, then we must popup an error
		    //message and tell the user that they cannot go through with
		    //the modification
		    if( (tempRoom.getCapac()) < (_new_version.getEnrollmt()) ){
			
			//if we are in here we know that there is a problem, the
			//user has edited the capacity of the section and the 
			//room
			//in which it is assigned to is no longer valid cause
			//the numbers don't match up.  Must tell the user
			_gooey.defaultErrorMsgWindow("The enrollment is too "+
						     " large for the room. The"+ 
						     " section will be "
						     + "unassigned");
			//make a note for later that there is a problem
			doCapAndEnrolMatch = false;
			
		    }//inner if
		    
		    roomAttrib = tempRoom.getAttrib();
		    sectionAttrib = _new_version.getAttrib();
		    
		    //go through the attributes in the two attributes lists 
		    //and check
		    //to make sure the section and room attributes match up
		    //if not then we want to alert the user about it.
		    for( int i = 0; i < 7 ; i++ ) {
			
			if( ( (String) sectionAttrib[ i ]).equals( "1" ) &&
			    ( (String) roomAttrib[ i ]).equals( "0" ) ){
			    
			    fit = -1;
			} 
			
		    } // for loop
		    
		}//if
		
		if ( (fit == -1) ){   
		    _gooey.askForEditAssignConfirmation(
					   (_new_version.getSectNum()), this );   
		}
		else{
		    Continue( 1 );
		}
	    }
	
	    else{
		
		//popup a window saying that the section is not addable
		//cause it has been assigned the smae number as some other
		//section in the database
		_gooey.defaultErrorMsgWindow("The section number already "+
					     " exists in the database");
	    }//else
	}
    
	else{
	    _operator.actionFailed();
	}

    } // sendSection
    
    /**
     * this keeps the action going,  go is called from here.
     *
     */
    public void Continue(int n) {

	//depending on what the user says, either continue or cancel while we
	//still can
	if(n == 1){

	    //if everything is still the same, go ahead and call go, go will
	    //automaticly replace the old assignment and replace it with the
	    //new one.  
	    go();

	    //if there were any differences has not allowed the section to be
	    //reassigned to the old section's room, alert the user with a popup
	    //window
	    if( doesThisHaveAnAssignment && !timeAndDaysAreSame ){
		_gooey.pleaseAssignLaterMsgWindow(_new_version.getSectNum());
	    }

	}//if
		
    }//Continue
    
    /**
     * replaces the old section in the database with the new one
     *
     */
    public void go(){
	Room tempRoom;
	
	if( doesThisHaveAnAssignment && !doCapAndEnrolMatch ){
	    //keep a record of the room for the undo
	    reassignOldSectionRoom = _dbase.getRoom
		(_older_version.getAssignment());

	    _dbase.unassign( (_older_version.getSectNum()) );
	    
	}

	else if( doesThisHaveAnAssignment && timeAndDaysAreSame ){
	    tempRoom = _dbase.getRoom( _older_version.getAssignment() );
	    reassignOldSectionRoom = tempRoom;
	    _dbase.unassign( (_older_version.getSectNum()) ); 
	    _dbase.assign( _new_version, tempRoom );
	}

	else if( doesThisHaveAnAssignment ){
	    reassignOldSectionRoom = _dbase.getRoom
		(_older_version.getAssignment()); 

	    _dbase.unassign( (_older_version.getSectNum()) ); 
	}

	//update the database with the new and old
	_dbase.removeSection( _older_version );
	_dbase.addSect( _new_version );
	
	//refresh the lists
	refreshSectList();
	refreshRoomOcc();
	refreshSectionAttrib();

    }
    
    /**
     * method to undo the modsectaction
     *
     */
    public void undo(){
	
	//we need to figure out what the state was before and return to that.
	
	//if the old section was simply unassigned, just assign it up again
	if( doesThisHaveAnAssignment && !doCapAndEnrolMatch ){
	    _dbase.assign( _older_version, reassignOldSectionRoom );
	}

	//if the old assignment was replaced, then swap it again
	else if( doesThisHaveAnAssignment && timeAndDaysAreSame ){

	    //replace the new version back with the old version
	    _dbase.unassign( (_new_version.getSectNum()) );
	    _dbase.assign( _older_version, reassignOldSectionRoom );
	}

	else if( doesThisHaveAnAssignment ){

	    //simple assign the old section back
	    _dbase.assign( _older_version, reassignOldSectionRoom );
	}
	
	_dbase.removeSection( _new_version );
	_dbase.addSect( _older_version );
       
	//refresh the list
	refreshSectList();
	refreshRoomOcc();
	refreshSectionAttrib();
	
    }
    
    /**
     * opens up the window and then "does nothing" till the user clicks OK or
     * Cancel in the window
     *
     */
    private void promptForChanges() {
	
    	editSectionWindow.getUserInfo();
    }
    
	public boolean isAddable( Section sectionInQuestion ) {
		return _dbase.addable( sectionInQuestion );
	}

} // Modify section action class
