/**
 * Class that perfoms the Modify room
 * @version    $Id: ModRoomAction.java,v 1.19 2000/05/10 17:09:38 etf2954 Exp $
 *
 * @author     Christopher Bunk
 * @author     Adam Chipalowsky
 * @author     Dan Cooley
 * @author     Eric Ferguson
 * @author     Phil Light
 *
 * Revisions:
 *     $Log: ModRoomAction.java,v $
 *     Revision 1.19  2000/05/10 17:09:38  etf2954
 *     Fixed error of window closing if there is errant input
 *
 *     Revision 1.18  2000/05/09 22:17:00  aec1324
 *     tracing an illegal something or other exception
 *
 *     Revision 1.17  2000/05/08 16:42:40  p361-45a
 *     fixed formatting
 *
 *     Revision 1.16  2000/05/05 17:13:33  aec1324
 *     got rid of 4 system.out's
 *
 *     Revision 1.15  2000/05/05 17:11:15  aec1324
 *     made some changes so this will undo the attributes
 *
 *     Revision 1.14  2000/05/05 14:50:52  pel2367
 *     handles a cancel by accepting a null room in
 *     sendRoom.
 *
 *     Revision 1.13  2000/05/05 01:07:00  pel2367
 *     threw an exception
 *
 *     Revision 1.12  2000/05/05 00:37:19  aec1324
 *     worked on it some
 *
 *     Revision 1.11  2000/05/04 20:25:46  aec1324
 *     fixed a minor bug when the user does an undo
 *
 *     Revision 1.10  2000/05/04 01:10:28  aec1324
 *     implemented undo and set go() and undo() self
 *     suficient so sucessive undo and redo's will work
 *
 *     Revision 1.9  2000/05/03 16:10:03  aec1324
 *     got rid of System.out...'s
 *
 *     Revision 1.8  2000/05/02 16:58:50  aec1324
 *     added an alert to tell the user if the attributs
 *     need to be changed on a section so as to keep
 *     everything inline
 *
 *     Revision 1.7  2000/05/02 03:07:19  aec1324
 *     does the check for attributes
 *
 *     Revision 1.6  2000/05/01 20:15:33  aec1324
 *     rewrote how this class look for capacity conflicts
 *     seems to work and alerts the user
 *
 *     Revision 1.5  2000/04/30 19:51:02  aec1324
 *     started implementing this to conditionaly unassign sections
 *     that no longet fit after the capacity changes
 *     in such a way that some or all sections no
 *     longer are under that capacity
 *
 *     Revision 1.4  2000/04/29 21:09:32  aec1324
 *     started implementation to open the window and
 *
 *     Revision 1.3  2000/04/28 17:05:39  pel2367
 *     made it compile... probably took out the
 *     throws BadActionException from go().
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
public class ModRoomAction extends CRSSAction 
                           implements UndoableAction {
    
    private Room _older_version;
    private Room _new_version;
    private String roomToChange;
    private AddSingleRoomWindow editRoomWindow;
    private boolean doesRoomHaveSections;
    private LinkedList sectionsToUnassign;
    private LinkedList oldVersionsSections;
    private LinkedList newVersionsSections;
    private boolean areThereSectionsToUnassign;
    private boolean assignToNewVersion;
    private boolean wereSectionsUnassigned;
    private boolean conflictsWithAttributes;
    private Section tempSection;

    private int numberOfOldAssignments;
    private LinkedList oldSections;
    private LinkedList sectionList;
    private int roomCap;
    
    private boolean swap;
    private boolean weAreReDoing;
    
    private Operator _operator;

    /**
     * constructor to try and modify a room
     *
     * @param gui a value of type 'GUI'
     * @param database a value of type 'CRSSDatabase'
     * @param oPerator a value of type 'Operator'
     * @exception BadActionException if an error occurs
     */
    public ModRoomAction( GUI gui, CRSSDatabase database, Operator oPerator )
        throws BadActionException{
	
	_operator = oPerator;
	_gooey = gui;
	_dbase = database;

	//start up with assuming the room does not have any
	//assignments
	doesRoomHaveSections = false;
	areThereSectionsToUnassign = false;
	assignToNewVersion = true;
	wereSectionsUnassigned = false;
	conflictsWithAttributes = false;
	weAreReDoing = false;
	
	swap = false;

	//create the linked list
	sectionsToUnassign = new LinkedList();
	oldVersionsSections = new LinkedList();
	newVersionsSections = new LinkedList();
	
	//first we need to get the hilighted section from the gui
	roomToChange = _gooey.getHighlightedRoom();
	
	if ( (roomToChange != null) && !(roomToChange.equals("  ")) ){
	    
	    //now we need to goto the database and grab a copy of it
	    //out so we can send it to the window
	    _older_version = _dbase.getRoom( roomToChange );
	    
	    //now have the window load up the section and get
	    //any user modifications
	    editRoomWindow = new AddSingleRoomWindow( this, _older_version );
	}//if
	else{
	    //we need to popup a window to tell the user that they 
	    //need to select a room before trying to edit one
	    _gooey.defaultErrorMsgWindow("Please select a room to edit");

	    throw new BadActionException();
	}
	
    }//ModRoomAction
    
    /**
     * send the new room to the database
     *
     * @param new_room a value of type 'Room'
     */
    public void sendRoom( Room new_room ){
	
	if( new_room != null ){

	    //do a check to make sure the room number that the user 
	    //edited does not conflict with room already in the 
	    //database
	    if(  _dbase.addable( new_room ) ||
			( new_room.getRoomNum().equals( _older_version.getRoomNum() ) ) ) {
	    

		//set the new room to the one passed in from the edit window
		_new_version = new_room;
		
		//to figure out if the room has sections, get the
		//assignments and ask for the size
		if ( _older_version.getAssignments().size() != 0 ){
		    
		    //set to true so we can handle the room with sections
		    //different
		    doesRoomHaveSections = true;
		}
		
		//if the room has no sections then simply call go
		if ( !doesRoomHaveSections ){

		   
		    //now since we have both the new and old room, simply 
		    //call go to swap the two
		    go();
		    
		}//if
		else if ( doesRoomHaveSections ){
		    
		    //the number of assignments the old room had
		    numberOfOldAssignments = 
			(_older_version.getAssignments()).size();
		    
		    //a linked list of all the sections that were assigned
		    //to the old room
		    oldSections = _older_version.getAssignments();
		    
		    //a linked list of 
		    sectionList = _new_version.getAssignments();
		    
		    //the size of the new room
		    roomCap = _new_version.getCapac();
		    
		    //if we are in here we need to worry about whether the 
		    //attributes and/or capacity has changed in such a way
		    //that the sections can no longer be assigned to it
		    
		    //first we need to fill a linked list filled with all the
		    //assignments from the older_version
		    for(int i=0; i<numberOfOldAssignments; i++){
			tempSection = _dbase.getSection(
						  (String)oldSections.get(i));
			
			//keep track of all the sections that were origionaly
			//assigned for the undo later on if called
			oldVersionsSections.add( tempSection );
			
			//see if the sections need to be unassigned cause of a
			//conflict in capacity and enrollment
			if(tempSection.getEnrollmt()>_new_version.getCapac() ){
			    
			    //place the sections that need to be unassigned into
			    //a linked list
			    sectionsToUnassign.add( tempSection );
			    
			    //alert the tempSect that it just lost its 
			    //assignment
			    tempSection.unassign();
			    
			    //alert that we don't want to assign this section
			    //in conflift to the new version
			    assignToNewVersion = false;
			    wereSectionsUnassigned = true;
			    
			}//inside if
			
			//if there are no conflicts then assign the section that
			//was assigned to the old version to the new version
			if( assignToNewVersion ){
			    
			    //set the sections assignment to the new room
			    tempSection.assign( _new_version.getRoomNum() );
			    
			    //put the section into a linked list for later when
			    //we notify the room of the assignments
			    newVersionsSections.add( tempSection );
			    
			}//if
			
			//reset the variable to default to true for our next time
			//through
			assignToNewVersion = true;
			
		    }//for
		    
		    //if we had to unassign any sections, use the GUI to popup
		    //a window telling the user which sections were unassigned
		    //and why
		    if( wereSectionsUnassigned ){
			_gooey.assignmentsWereLostMsgWin();
		    }//if
		    
		    //now tell the new section to update its assignments to make
		    //them match the old ones assignments 
		    for(int k=0; k<newVersionsSections.size(); k++){
			_new_version.assign((Section)newVersionsSections.get(k));
		    }    
		    
		    //of the sections that are left, we need to alert the
		    //user if there are attribute conflicts.  If there just  
		    //keep the assignments, just alert the user
		    
		    //start a loop that goes through each assignment in the
		    //new_version and comapirs its attributes with the new
		    //rooms attributes
		    int numberOfAssignments = 
			(_new_version.getAssignments()).size();
		    
		    LinkedList newSections = new LinkedList();
		    Object[] tempSectionAttrib = new Object[7];
		    Object[] roomAttrib = _new_version.getAttrib();
		    
		    for(int i=0; i<numberOfAssignments; i++ ){
			
			//get the section out of the linked list
			tempSection = (Section)newVersionsSections.get(i);
			
			//grab the attribute array out of the section
			tempSectionAttrib = tempSection.getAttrib();
			
			//go throught the attribute between the section and
			//the room.  If a difference is found, make note of it
			for( int j=0; j<7; j++ ){
			    
			    //if the section has an attribute that the room
			    //does not have...
			    if( !roomAttrib[j].equals("1") 
				&& tempSectionAttrib[j].equals("1") ){
				
				//set this as true so we can popup an alert for
				//the user
				conflictsWithAttributes = true;
				
			    }//if
			}//inside for loop
			
		    }//for
		    
		    //reset this so we only set it to true if we need to 
		    //next time
		    //through
		    areThereSectionsToUnassign = false;
		    
		}//else if
		
		//tell the user if there are any conflicts with the 
		//attributes here
		if( conflictsWithAttributes ){
		    
		    _gooey.noteSomeAttribDifferencesWin();
		}
		
		
		//set true incase we call redo later on
		weAreReDoing = true;
		
		//everythign should be set so call go to do the sawp database
		go();
		
	    }//large if
	    else{
		//popup a window that tells the user that the
		//room they edited already exists in the
		//database.
		    _gooey.defaultErrorMsgWindow("The room number already"+
						 " exists in the database");
	    }
	    
	}//way large outside if
	else{
	    
	    _operator.actionFailed();
	}
	
    }//sendRoom
    

    /**
     * this method is a helper when undo and redo are called.
     * it simply switches any sections that were assigned to either
     * the new section or the old section so they reflect where they
     * are assigned to
     *
     * @param sw a boolean to identify weather the call was made
     *           from undo or redo
     */
    private void updateRoomSections( boolean sw){
	Section temp;
	//take the linked list that was already defined above and
	//make the assignments

	if( sw ){
	    
	    //now tell the new section to update its assignments to make
	    //them match the old ones assignments 
	    for(int k=0; k<oldVersionsSections.size(); k++){
		temp = (Section)oldVersionsSections.get(k);
		
		//for each section, we need to tell it that it's room
		//has changed
		temp.assign(_older_version.getRoomNum());
		
	    }//for 
	}
	
	else{
	    
	    //now tell the new section to update its assignments to make
	    //them match the old ones assignments 
	    for(int k=0; k<newVersionsSections.size(); k++){
		
		temp = (Section)newVersionsSections.get(k);
		temp.assign(_new_version.getRoomNum());
	    } 
	    
	    //and any that are left over need to be told that they
	    //no longer have assignments
	    for(int k=0; k<sectionsToUnassign.size(); k++){
		temp = (Section)sectionsToUnassign.get(k);
		
		temp.unassign();
	    }
	}//else
	
    }//updateRoomSections


    /**
     * swap out the old room from the database and replace
     * it with the new one
     *
     */
    public void go(){
	Room tempRoom;
	
	//get access to the room to remove
	tempRoom = _dbase.getRoom( _older_version.getRoomNum() );
	
	//remove the old one
	_dbase.removeRoom( tempRoom );
	
	//put the new one in the database
	_dbase.addRoom( _new_version );
	
	//if this is not the first time through (i.e. the thread is 
	//here cause redo was called)
	if( weAreReDoing ){
	    //incase this is called as a redo, we need to run the 
	    //updateRoomSections to reassign any sections to it
	    updateRoomSections( swap );
	    swap = true;
	}
	swap = true;
	//refresh the tables and such
	refreshRoomList();
	refreshSectList();
	refreshRoomOcc();
	refreshRoomAttrib();
	
    }//go
    
    /**
     * does the exactly what go does not do
     *
     */
    public void undo(){
	Room tempRoom;
	//in the undo we need to simply replace the new information 
	//with the old room

	//keep a copy of the current room just incase the user clicks
	//the redo option
	tempRoom = _dbase.getRoom( _new_version.getRoomNum() );

	//remove the current one
	_dbase.removeRoom( tempRoom );

	//put back the origional one
	_dbase.addRoom( _older_version );

	//set the new "new" version to the one that was taken out
	_new_version = tempRoom;
	
	//alert any sections that my have been reassigned that they 
	//have new room
	updateRoomSections( swap );
	swap = false;

	//refresh the tables and such
	refreshRoomList();
	refreshSectList();
	refreshRoomOcc();
	refreshRoomAttrib();

    }//undo

	public boolean isAddable( Room roomInQuestion ) {
		return _dbase.addable( roomInQuestion );
	}
    
} // Modify room action class

