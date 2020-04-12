/**
 * Class that contains a database of rooms and sections
 *
 * @version    $Id: CRSSDatabase.java,v 1.28 2000/05/09 23:06:39 aec1324 Exp $
 *
 * @author     Christopher Bunk
 * @author     Adam Chipalowsky
 * @author     Dan Cooley
 * @author     Eric Fergesun
 * @author     Phil Light
 *
 * Revisions:
 *     $Log: CRSSDatabase.java,v $
 *     Revision 1.28  2000/05/09 23:06:39  aec1324
 *     nothing
 *
 *     Revision 1.27  2000/05/09 22:17:00  aec1324
 *     tracing an illegal something or other exception
 *
 *     Revision 1.26  2000/05/08 03:39:40  p361-45a
 *     fixed formatting
 *
 *     Revision 1.25  2000/05/06 01:15:39  pel2367
 *     smote the blank line printed for every new section.
 *
 *     Revision 1.24  2000/05/01 14:46:27  pel2367
 *     attemped to make getSectList and
 *     getRoomList return a "deep" clone.
 *
 *     Revision 1.23  2000/04/28 01:24:57  pel2367
 *     Rewrote it so that go() will be redoable
 *
 *     Revision 1.22  2000/04/27 19:24:06  p361-45a
 *     fixed some errors found during inspection
 *
 *     Revision 1.21  2000/04/26 17:16:28  etf2954
 *     Added the removeSection calls
 *
 *     Revision 1.20  2000/04/25 22:00:45  pel2367
 *     now recognizes "     " instead of "Empty"
 *
 *     Revision 1.19  2000/04/25 04:51:08  pel2367
 *     made a little fix in assignable (room contains the string
 *     "Empty" at the appropriate times now.
 *
 *     Revision 1.18  2000/04/24 18:38:48  p361-45a
 *     Unassign() and clearDatabase() functions implemented
 *
 *     Revision 1.17  2000/04/24 05:12:03  pel2367
 *     reworked (fixed?) assignable, assign.
 *
 *     Revision 1.16  2000/04/24 03:23:09  pel2367
 *     working on assignable()
 *
 *     Revision 1.15  2000/04/23 20:36:55  pel2367
 *     handled casting issues.
 *
 *     Revision 1.14  2000/04/23 20:03:26  pel2367
 *     wrote assignable(), assign()
 *
 *     Revision 1.13  2000/04/22 22:05:36  pel2367
 *     acknowledged a few new functions in the gui, etc.
 *
 *     Revision 1.12  2000/04/22 20:15:11  pel2367
 *     cleaned up addRoom() to make it compile.
 *
 *     Revision 1.11  2000/04/22 19:38:00  pel2367
 *     Automatically sorts the _sectionList by Section number
 *     now.  this was necessary at this point so that the
 *     database could return the selected section.
 *
 *     Revision 1.10  2000/04/22 17:21:43  pel2367
 *     reworked the getSection( string ) and getRoom( string )
 *     methods. they now reflect the compareTo funcion in
 *     section and room.
 *
 *     Revision 1.9  2000/04/21 18:55:02  p361-45a
 *     Get section implemented
 *
 *     Revision 1.8  2000/04/21 15:15:47  etf2954
 *     Added the addable( Room r ) functionality
 *
 *     Revision 1.7  2000/04/20 02:27:38  aec1324
 *     added implementation to actually add to its vectors
 *
 *     Revision 1.6  2000/04/19 20:43:58  aec1324
 *     stuff
 *
 *     Revision 1.5  2000/04/18 21:42:05  etf2954
 *     added functionality for adding a new Room
 *
 *     Revision 1.4  2000/04/18 03:09:21  p361-45a
 *     added ClearAllList function used by Newstate action
 *
 *     Revision 1.3  2000/04/18 02:51:35  p361-45a
 *     finished headers
 *
 *     Revision 1.2  2000/04/17 21:36:03  p361-45a
 *     Comments added
 *
 *     Revision 1.1  2000/04/16 22:53:27  p361-45a
 *     Initial revision
 *
 */

import java.io.*;
import java.lang.*;
import java.util.*;

public class CRSSDatabase {

    private Vector _sectionList;
    private Vector _roomList;
    
    /**
     * contructor to create an object of type CRSSDatabase
     *
     */
    public CRSSDatabase(){
	_sectionList = new Vector();
	_roomList = new Vector();
    }
    
    /**
     * This method returns the section information
     *
     * @param s section number
     * @return the actual section object
     */
    public Section getSection( String s ){

        int n;
        Section result = null;
        n = Collections.binarySearch( _sectionList, s );
	
        if( n >= 0 ) {
	        result = ( (Section) _sectionList.elementAt( n ) );
        }

        return result;
    }

    /**
     * This method adds a room to the database
     *
     * @param newRoom The new 'Room' to be added
     */
    public void addRoom( Room newRoom ) {
	
	//make sure the room can be added
	if( addable( newRoom ) ) {
	   
	    // add the room to its vector.  first, ask the Collections
            // class to tell us where it belongs, since we are keeping the
            // vector sorted by room number.

	    int index;
            index = Collections.binarySearch( _roomList, newRoom );
	   
            // this addition is made based on the results of the binarySearch.
            // if the binarySearch returned a positive index value, then the
            // room is already in the vector, and we should not add it.
            // however, we already checked for that possibility by calling
            // addable( newRoom ) up above.  therefore, we know we can 
            // add the newRoom to the vector at the following location:
	    
            _roomList.add( -( index + 1 ), newRoom );
	    
	}
	else{
	    //if in here, the room is not addable and we should do nothing
	    //throw new IllegalArgumentException();
	}
	
    } // addRoom
    
    /**
     * This method adds a section to the database
     *
     * @param newSect The new 'Section' to be added
     */
    public void addSect( Section newSect ) {
	
	//make sure the section can be added
	if( addable( newSect ) ) {
		
	    //add the section to its vector.  first, ask the Collections
            // class to tell us where it belongs, since we are keeping the
            // vector sorted by section number.

	    int index;
            index = Collections.binarySearch( _sectionList, newSect );
	    
            // this addition is made based on the results of the binarySearch.
            // if the binarySearch returned a positive index value, then the
            // section is already in the vector, and we should not add it.
            // however, we already checked for that possibility by calling
            // addable( newSect ) up above.  therefore, we know we can 
            // add the newSect to the vector at the following location:

            _sectionList.add( -( index + 1 ), newSect );
	}

	else{
	    throw new IllegalArgumentException();
	}
    } // addSect
    
    /**
     * This method removes a room that is passed to it
     *
     * @param	unneededRoom	is the room to be removed
     *
     */
    public void removeRoom( Room unneededRoom ) {
	_roomList.remove( unneededRoom );
    } // remove Room
    
    /**
     * This method removes a section that is passed to it
     *
     * @param	unneededSection	is the section to be removed
     *
     */
    public void removeSection( Section unneededSection ) {
	_sectionList.remove( unneededSection );
    } // remove Section
    
    /**
     * This method returns the room information to an action
     *
     * @param r a value of type 'String'
     * @return a value of type 'Room'
     */
    public Room getRoom( String r ){
	
        int n;
        Room result = null;
        n = Collections.binarySearch( _roomList, r );
        if( n >= 0 ) {
	    result = ( (Room) _roomList.elementAt( n ) );
        }
        return result;
    }
    
    
    /**
     * Returns to action whether or not the section is addable
     *
     * @param s a value of type 'Section'
     * @return a value of type 'Boolean'
     */
    public boolean addable( Section s ){
        
	//check to see if the section is already in the databse.  Do this
	// by searching the database for the section
	
	boolean result = false;
        int n;
        n = Collections.binarySearch( _sectionList, s);
        if (n < 0){
	    result = true;
        }
	return result;
    }
    
    /**
     * Returns to action whether or not the room is addable
     *
     * @param r a value of type 'Room'
     * @return a value of type 'Boolean'
     */
    public boolean addable( Room r ){
			
	//check to see if the room is already in the databse.  Do this
	//by searching the database for the section
	
	boolean result = false;
	int n;
        n = Collections.binarySearch( _roomList, r);
        
	if (n < 0){
	    result = true;
        }
	return result;
    } // addable

    /**
     * returns the complete section list to the action
     *
     * @return a value of type 'Vector'
     */
    public Vector getSectList(){

        Vector result = new Vector( _sectionList.size() );

        for( int iter = 0; iter < _sectionList.size(); iter++ ){
            result.add( iter,  ( (Section) _sectionList.get( iter )).copy() );
        }

        return result;
    }

    /**
     * returns the complete room list to the action
     *
     * @return a value of type 'Vector'
     */
    public Vector getRoomList(){
     
	Vector result = new Vector( _roomList.size() );
        for( int iter = 0; iter < _roomList.size(); iter++ ){
            result.add( iter, ( (Room) _roomList.get( iter )).copy() );
        }
        return result;
    }

    /**
     * returns to action whether or not the room and section are assignable
     *
     * @param s a value of type 'Section'
     * @param r a value of type 'Room'
     * @return a value of type 'Boolean'
     */
    public int assignable( Section s, Room r ){
        int result = 5;
        String[][] roomOccupancy;
        String[][] sectRequest;
        
        if( s != null && r != null ){
	    roomOccupancy = r.getOccpcy();
    	    sectRequest = (String[][]) s.getTimes();
            
            // this nested for loop will cycle through the 2d array which
            // represents a week.  each time slot which the section requests
            // will be checked for availability in the room .
            for( int day = 0; day < 5 && result != 0; day++ ) {
            	
		for( int time = 0; time < 14 && result != 0; time++ ) {

		    if( sectRequest[day][time].equals( "1" ) &&
                    	!roomOccupancy[day][time].equals( "     " ) ) {
                        // the section would like to meet at this time,
                        // but the room is already assigned to another
                        // section.
                        result = 0;

		    } // is this time slot free in the room?
                } // for loop (iterates across the times of the day.
            } // for loop (iterates across the days of the week)
            
            if( result == 5 ) {
            	
		// at this point, we know that the room is free when the
                // section would like to meet.  but does the room have
                // the attributes that the section has requested?  we'll begin
                // by assuming that they fit.
                result = 1;
		Object[] sectionRequests;
		Object[] roomAttrib;

                sectionRequests =  s.getAttrib();
                roomAttrib = r.getAttrib();

		// now, loop through the array of attributes.  if any of them
                // are requested by the section but are not present in the 
                // room, then the return value should be -1.
		
                for( int i = 0; i < 7 ; i++ ) {
		    
		    if( ( (String) sectionRequests[ i ]).equals( "1" ) &&
                    	( (String) roomAttrib[ i ]).equals( "0" ) ){
			result = -1;
		    } // how does this attribute compare?
		    
		} // for loop
            } // was the room available at all the right times?
	    
        } // did we get a room and a section?
	
        else{
	    throw new IllegalArgumentException();
        }
	
        return result;
    } // assignable.
    
    /**
     * assign the room to the section
     *
     * @param s a value of type 'Section'
     * @param r a value of type 'Room'
     */
    public void assign( Section s, Room r ){

    	// the client has decided to assign this section to this room.
        // we'll assume that they know what they're doing.
		if( r != null && s != null ){
	        r.assign( s );
    	    s.assign( r.getRoomNum() );
        } // did we get both a section and a room?

        else{
        	throw new IllegalArgumentException();
        }
    }

    /**
     * unassign the room from the section
     *
     * @param s section to be unassigned
     */
    public void unassign( String s ){
        // get section s from database
        // unassign the room it is assigned to
        // unassign the section
        
        (getRoom( (getSection( s )).getAssignment())).unassign( s);
        (getSection( s )).unassign();
        
    }

    /**
     * This method will clear the database
     *
     */
    public void clearAllLists(){

    	_sectionList.clear();
        _roomList.clear();
    }

} // HiLiteRoom Action class
