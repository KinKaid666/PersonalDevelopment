/**
 * Class representing information about a Room in a scheduling database
 *
 * @version    $Id: Room.java,v 1.24 2000/05/07 04:21:12 pel2367 Exp $
 *
 * @author     Christopher Bunk
 * @author     Adam Chipalowsky
 * @author     Dan Cooley
 * @author     Eric Fergesun
 * @author     Phil Light
 *
 * Revisions:
 *     $Log: Room.java,v $
 *     Revision 1.24  2000/05/07 04:21:12  pel2367
 *     i'd like to give a special shout out to the genius who
 *     decided to add to _assignments when assigning, but
 *     didn't remove a section when unassigning.  keep the
 *     hits comin', buddy.
 *
 *     Revision 1.23  2000/05/05 17:10:27  aec1324
 *     made the attributes accessor return only a clone, not
 *     the real thing.
 *
 *     Revision 1.22  2000/05/05 01:12:13  aec1324
 *     *** empty log message ***
 *
 *     Revision 1.21  2000/05/02 16:58:50  aec1324
 *     played around wtih a clone but then decided to
 *     change everything back to they way it was
 *
 *     Revision 1.20  2000/05/01 14:46:27  pel2367
 *     wrote copy(). doesn't seem to work though.
 *
 *     Revision 1.19  2000/04/30 19:51:02  aec1324
 *     added implementation for replacing the
 *     assignments linked list
 *
 *     Revision 1.18  2000/04/29 03:10:57  cmb3548
 *     toSaveString implemented w/ helper functions
 *
 *     Revision 1.17  2000/04/28 22:23:21  pel2367
 *     i think this was just locked by mistake.
 *
 *     Revision 1.16  2000/04/28 17:27:07  cmb3548
 *     Occupancy array now initialized with a loop and now has 14 rows
 *
 *     Revision 1.15  2000/04/28 01:09:47  etf2954
 *     added getAssignment() functioanlity
 *
 *     Revision 1.14  2000/04/25 22:00:45  pel2367
 *     changed "Empty" to "     ".
 *
 *     Revision 1.13  2000/04/25 04:51:08  pel2367
 *     changed the default string to Empty" instead
 *     of "0"
 *
 *     Revision 1.12  2000/04/24 05:12:03  pel2367
 *     reworked (fixed?) assign.
 *
 *     Revision 1.11  2000/04/23 20:36:55  pel2367
 *     took away the clone() because it wouldn't
 *     compile.
 *
 *     Revision 1.10  2000/04/23 20:03:26  pel2367
 *     wrote assign(), unassign().
 *
 *     Revision 1.9  2000/04/22 19:38:00  pel2367
 *     I don't know what i did here, i suppose something minor,
 *     or worked with formatted hyphens.
 *
 *     Revision 1.8  2000/04/22 17:21:43  pel2367
 *     changed the interface implemented to comparable instead
 *     of comparator (good one bunk!)
 *     implemented the compareTo method.  whether it is legal or
 *     not, this class can compare itself to other instances of
 *     itself, or a string.  the string is compared to my number.
 *
 *     Revision 1.7  2000/04/21 02:24:52  p361-45a
 *     minor coding
 *
 *     Revision 1.6  2000/04/20 23:05:50  aec1324
 *     changed the boolean[] to an Object[]
 *
 *     Revision 1.5  2000/04/20 21:35:33  etf2954
 *     Added _roomDescription and accessor to it
 *
 *     Revision 1.4  2000/04/18 22:09:24  aec1324
 *     got rid of testing information
 *
 *     Revision 1.3  2000/04/18 13:10:07  p361-45a
 *     finished method headers
 *
 *     Revision 1.2  2000/04/17 21:35:25  p361-45a
 *     Now implements comparator
 *
 *     Revision 1.1  2000/04/16 22:53:50  p361-45a
 *     Initial revision
 *
 */

import java.io.*;
import java.lang.*;
import java.util.*;

public class Room implements Comparable{

    private String _roomNum;
    private String[][] _occupancy = new String[5][14];
    private Object[] _attributes;
    private int _capacity;
    private String _roomDescription;
    private LinkedList _assignments = new LinkedList();
    
    /**
     * constructor to create an object of type Room
     *
     */
    public Room(){
    }
    /**
     * constructor to create an object of type Room
     *
     * @param r a value of type 'Room'
     */
    public Room( Room r ){
	    _roomNum = r._roomNum;
	    _occupancy = r._occupancy;
	    _attributes = r._attributes;
	    _capacity = r._capacity;
	    _roomDescription = r._roomDescription;
        _assignments = r._assignments;
	
    }
    /**
     * constructor to create an object of type Room
     *
     * @param num a value of type 'String'
     * @param attribs a value of type 'Boolean[]'
     * @param capacity a value of type 'Integer'
	 * @param roomDescription is the description of the room
     */
    public Room( String num, Object[] attribs, int capacity, 
		 String roomDescription ){
		
	        int i , j;
		_roomNum = num;
		_attributes = attribs;
		_capacity = capacity;
		_roomDescription = roomDescription;
		
		//Initialize the occupancy array
		for( i = 0; i < 5; i++ ){
		    for ( j = 0; j < 14; j++ ){
		        _occupancy[i][j] = "     ";
		    }
		} // array initialization
		
    }
    /**
     * method to return room number
     *
     * @return a value of type 'String'
     */
    public String getRoomNum(){
		return _roomNum;
    }
    /**
     * method to return attributes 
     *
     * @return a value of type 'Boolean[]'
     */
    public Object[] getAttrib(){
	return (Object[])(_attributes.clone());
	
    }
    /**
     * method to return room capacity
     *
     * @return a value of type 'Integer'
     */
    public int getCapac(){
		return _capacity;
    }
    /**
     * method to return 2-D array with occupancies 
     *
     * @return a value of type 'String[][]'
     */
    public String[][] getOccpcy(){
		return _occupancy;
    }
    /**
     * method to return room description
     *
     * @return a value of type 'String'
     */
    public String getDescription(){
		return _roomDescription;
    }
    /**
     * method to return all assignments
     *
     * @return a value of type 'LinkedList'
     */
    public LinkedList getAssignments(){
        return _assignments;
    }
    /**
     * method to set assignment
     *
     * @param s a value of type 'Section'
     */
    public void assign( Section s ){
		String sectNum = s.getSectNum();
        Object[][] times = s.getTimes();
        
		_assignments.add( s.getSectNum() );
        // now we must iterate through the array of assignments
        // and chalk this guy's number up during the
        // correct times.
        for( int day = 0; day < 5; day++ ){
        	for( int time = 0; time < 14; time++ ) {
            	if( ( (String) times[day][time]).equals( "1" ) ){
        			// the section meets now.  whatcha gonna
                    // do about it?
        
                	_occupancy[day][time] = sectNum;
                } // does the section want to meet now?
            } // for loop (travesrses times)
        } // for loop (iterates days)
    } // assign

        
    /**
     * method to unassign
     *
     * @param s a value of type 'String'
     */
    public void unassign( String s ){
    	// string s represents the section's number.  we're
        // going to forget that we ever knew anyone by that
        // name.
        _assignments.remove( s );
        for( int day = 0; day < 5; day++ ) {
        	for( int time = 0; time < 14; time++ ) {
            	if( s.equals( _occupancy[day][time] ) ) {
                	// this is the section we're removing.
                    // reset the string to null.
                    _occupancy[day][time] = "     ";
                } // does the section with this number meet now?
            } // for loop( traverses a day )
        } // for loop( traverses all the days of the week)
    } // unassign
    
    
    /**
     * method to set room number
     *
     * @param num a value of type 'String'
     */
    public void setRoomNum( String num ){
    }
    /**
     * method to set attributes
     *
     * @param attribs a value of type 'Boolean[]'
     */
    public void setAttrib( Boolean[] attribs ){
    }
    

    /**
     * to specify the assignments for this room. Note the linked list
     * must be a list of sections
     *
     * @param newAssignments A linked list full of assignments
     */
    public void setAssignments( LinkedList newAssignments ){
	LinkedList tempAssignments = newAssignments;
    
	//assign this rooms sections to the ones in the passed
	//in linked list
	for(int i=0; i< tempAssignments.size(); i++){
	    assign( (Section)tempAssignments.get(i) );
	}


    }

    /**
     * method to set capacity
     *
     * @param capacity a value of type 'Integer'
     */
    public void setCapac( Integer capacity ){
    }
    /**
     * method to compare two rooms
     *
     * @param o1 a value of type 'Object'
     * @param o2 a value of type 'Object'
     * @return a value of type 'int'
     */
    public int compareTo( Object o ){
		int result;
        if ( o instanceof Room ) {
			result = _roomNum.compareTo( ( ( Room ) o ).getRoomNum() );
        }
        else if ( o instanceof String ) {
        	result = _roomNum.compareTo( (String) o );
        }
        else {
        	throw new ClassCastException();
        }
        return result;           
    } 
    
    public Room copy(){
        return new Room( this );
    }
    
    public String toSaveString(){
        
        String tempString = new String();
	
        tempString += ";" + _roomNum + ";    ";
        tempString += ";" + _capacity + ";    ";
        tempString += ";" + _roomDescription + ";    ";
        tempString += ";" + attribsToIOString(_attributes)  + ";    ";
        return tempString;
    
    } // toSaveString
    
    private String attribsToIOString( Object[] attribs ){
        
        String tempString = new String();
        int i;
        for( i = 0; i < attribs.length; i++ ){
            tempString += attribs[i];
        } // for loop
        return tempString;
     } //attribsToIOString

} // Room Class
