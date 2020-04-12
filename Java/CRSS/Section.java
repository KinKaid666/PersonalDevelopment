/**
 * Class representing information about a Section in a scheduling database
 *
 * @version    $Id: Section.java,v 1.29 2000/05/08 02:16:20 aec1324 Exp $
 *
 * @author     Christopher Bunk
 * @author     Adam Chipalowsky
 * @author     Dan Cooley
 * @author     Eric Ferguson
 * @author     Phil Light
 *
 * Revisions:
 *     $Log: Section.java,v $
 *     Revision 1.29  2000/05/08 02:16:20  aec1324
 *     fixed a constructor to convert a string to an object
 *     in the attributes in the constructor that takes
 *     a file
 *
 *     Revision 1.28  2000/05/06 14:50:15  pel2367
 *     added and removed some debugging, questioned the
 *     authenticity of toSaveString, but it seems to
 *     be alright.
 *
 *     Revision 1.27  2000/05/06 01:15:39  pel2367
 *     had a heck of a time writing the constructor which
 *     takes Integers, etc. and handles the conversions.
 *
 *     Revision 1.26  2000/05/05 17:52:56  pel2367
 *     sorry, don't remember, but i've been working
 *     on copying sections, so it's probably something
 *     like making accessors return copies... oh and i
 *     added the mutators, if that wasn't the previous
 *     revision.
 *
 *     Revision 1.25  2000/05/03 16:10:03  aec1324
 *     removed that annoying time debugging information
 *
 *     Revision 1.24  2000/05/01 14:46:27  pel2367
 *     wrote copy(). doesn't seem to work though.
 *
 *     Revision 1.23  2000/04/29 03:54:06  cmb3548
 *     time shift of 8 added to toSaveString function
 *
 *     Revision 1.22  2000/04/29 03:05:04  cmb3548
 *     toSaveString function and helper functions implemented
 *
 *     Revision 1.21  2000/04/28 21:56:06  aec1324
 *     got rid of two System.out... statements
 *
 *     Revision 1.20  2000/04/28 17:05:57  aec1324
 *     made the attributes return a clone
 *
 *     Revision 1.19  2000/04/28 01:30:57  aec1324
 *     made a little chenge on the accessor for getting the
 *     start and end times
 *
 *     Revision 1.18  2000/04/25 21:10:46  aec1324
 *     added getStartTime and getEndTime to return
 *     in milirary time the time in which
 *     this section meets.  This is so the section
 *     table can easily add the meeting time to its
 *     self and be happy
 *
 *     Revision 1.17  2000/04/24 23:33:17  aec1324
 *     worked on the days array, this thing now has
 *     a method that returns a clone of the boolean
 *     array representing the days the section meets
 *
 *     Revision 1.16  2000/04/24 14:45:12  pel2367
 *     i think all i did was remove some debugging messages.
 *
 *     Revision 1.15  2000/04/24 05:12:03  pel2367
 *     defaulted the assignment to "None"
 *
 *     Revision 1.14  2000/04/24 03:23:09  pel2367
 *     rewriting getTimes()
 *
 *     Revision 1.13  2000/04/23 20:36:55  pel2367
 *     took away the clone() because it wouldn't
 *     compile.
 *
 *     Revision 1.12  2000/04/23 20:03:26  pel2367
 *     wrote assign(), unassign(), made some
 *     methods return clones.
 *
 *     Revision 1.11  2000/04/22 19:38:00  pel2367
 *     I don't know what i did here, i suppose something
 *     minor, or worked with formatted hyphens.
 *
 *     Revision 1.10  2000/04/22 17:21:43  pel2367
 *     changed the interface implemented to comparable instead
 *     of comparator (good one bunk!)
 *     implemented the compareTo method.  whether it is legal or
 *     not, this class can compare itself to other instances of
 *     itself, or a string.  the string is compared to my number.
 *
 *     Revision 1.9  2000/04/22 00:51:39  aec1324
 *     changed Boolean to boolean...again
 *
 *     Revision 1.8  2000/04/20 23:05:50  aec1324
 *     changed the boolean[] to an Object[]
 *
 *     Revision 1.7  2000/04/20 02:27:38  aec1324
 *     added more data members
 *
 *     Revision 1.6  2000/04/19 20:43:58  aec1324
 *     stuff
 *
 *     Revision 1.5  2000/04/19 16:37:22  aec1324
 *     working with the way it talks to a section
 *
 *     Revision 1.4  2000/04/19 14:28:38  etf2954
 *     Added the copy contructor
 *
 *     Revision 1.3  2000/04/18 13:24:45  p361-45a
 *     finished method headers
 *
 *     Revision 1.2  2000/04/17 21:34:57  p361-45a
 *     now implements comparator
 *
 *     Revision 1.1  2000/04/16 22:54:02  p361-45a
 *     Initial revision
 *
 */

import java.io.*;
import java.lang.*;
import java.util.*;

public class Section implements Comparable{

    private String _sectNum;
    private Object[] _attributes;
    private int _enrollment;
    private String _assignment = "None";
    private int _startTime;
    private int _endTime;
    private boolean[] _days;
    private String _name;
    
    /**
     * constructor to create an object of type Section
     *
     */
    public Section(){
    }
    /**
     * constructor to create an object of type Section
     *
     * @param s a value of type 'Section'
     */
    public Section( Section s ){
		_sectNum = s.getSectNum();
		_attributes = s.getAttrib();
		_enrollment = s.getEnrollmt();
		_assignment = s.getAssignment();
		_startTime = s.getStartTime() - 8;
		_endTime = s.getEndTime() - 8;
		_days = s.getDays();
		_name = s.getSectName();

		
    }
    /**
     * constructor to create an object of type Section
     *
     * @param name a value of type 'String'
     * @param attribs a value of type 'Object[]'
     * @param enrollment a value of type 'int'
     * @param sTime a value of type 'int'
     * @param eTime a value of type 'int'
     * @param days a value of type 'String[]'
     */
    public Section( String name,
                    String sectionNumber,
                    String[] attributes, 
                    Integer enrollment,
                    Integer startTime,
                    Integer endTime,
                    String[] days ){	        
	_sectNum = sectionNumber;
	//_attributes = attributes;
	_enrollment = enrollment.intValue();
	_startTime = startTime.intValue() - 8;
	_endTime = endTime.intValue() - 8;
	_name = name;
    _days = new boolean[5];
    for( int day = 0; day < 5; day++ ) {
    	if( days[day].equals( "0" ) ) { 
        	_days[day] = false;
        }
        else if ( days[day].equals( "1" ) ) {
        	_days[day] = true;
        }
        else{
        	throw new IllegalArgumentException();
        }
	
	//convert the attrubutes string to object form
	_attributes = new Object[7];
	for(int i=0; i<7; i++){
	    _attributes[i] = attributes[i];
	}

    } // loop which loads the boolean array _days
    

    } // constructor

    public Section( String name,
                    String sectionNumber,
                    Object[] attributes, 
                    int enrollment,
                    int startTime,
                    int endTime,
                    boolean[] days ){
	_sectNum = sectionNumber;
	_attributes = attributes;
	_enrollment = enrollment;
	_startTime = startTime;
	_endTime = endTime;
	_name = name;
    _days = days;
   
    } // constructor



    /**
     * returns the boolean array of days
     *
     * @return a value of type 'boolean[]'
     */
    public boolean[] getDays(){

	return (boolean[])_days.clone();
    }

    /**
     * method to return section number
     *
     * @return a value of type 'String'
     */
    public String getSectNum(){
        return new String( _sectNum );
    }

    /**
     * returns the name of the section
     *
     * @return name of the section
     */
    public String getSectName(){
	    return new String( _name );

    }

    /**
     * returns the start time for the section in military
     * formatt.
     *
     * @return time the section starts in military
     */
    public int getStartTime(){
	
	return _startTime+8;
    }
    
    /**
     * returns the end time for the section in military
     * formatt
     *
     * @return time the section ends in military
     */
    public int getEndTime(){
	
	return _endTime+8;
    }

    /**
     * method to return section attributes
     *
     * @return a value of type 'Boolean[]'
     */
    public Object[] getAttrib(){
	return (Object[])_attributes.clone();
    }
    /**
     * method to return section enrollment
     *
     * @return a value of type 'Integer'
     */
    public int getEnrollmt(){
        return _enrollment;
    }
    /**
     * method to return assignment
     *
     * @return a value of type 'String'
     */
    public String getAssignment(){
        return new String( _assignment );
    }
    /**
     * method to return the times that the section meets
     *
     * @return a value of type 'Boolean[][]'
     */
    public Object[][] getTimes(){

        String[][] result = { { "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0"},
                              { "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0"},
                              { "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0"},
                              { "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0"},
                              { "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0"} };

	
        for( int day = 0; day < 5; day++ ) {
        	//System.out.println("day: " + day );
           
        	if( _days[day] == true ) {
                for( int time = 0; time < 14 ; time++){
                	
            	    if( time >= _startTime && time < _endTime ) {
                        
                        result[day][time] = "1";
                        
                    }
                    else{
                    	
                    	result[day][time] = "0";
                        	
                    }
                }
            }
        }
        return result;
        
    }
    /**
     * method to assign room to it
     *
     * @param room a value of type 'String'
     */
    public void assign( String room ){
    	_assignment = room;
    }

    
    /**
     * method to unassign room from it
     *
     */
    public void unassign(){
    	_assignment = "None";
    }
    /**
     * method to set section number
     *
     * @param num a value of type 'String'
     */
    public void setSectNum( String num ){
	    _sectNum = num;
    }
    /**
     * method to set attributes
     *
     * @param attribs a value of type 'Boolean[]'
     */
    public void setAttrib( Object[] attribs ){
        _attributes = attribs;
    }
    /**
     * method to set enrollment
     *
     * @param capacity a value of type 'Integer'
     */
    public void setEnroll( int capacity ){
        _enrollment = capacity;
    }
    /**
     * method to set a start time for the section
     *
     * @param sTime a value of type 'Integer'
     */
    public void setStartTime( int sTime ){
        _startTime = sTime;
    }
    /**
     * method to set an end time for the section
     *
     * @param eTime a value of type 'Integer'
     */
    public void setEndTime( int eTime ){
        _endTime = eTime;
    }
    /**
     * method to set the days that the section meets
     *
     * @param days a value of type 'Boolean[]'
     */
    public void setDays( boolean[] days ){
        _days = days;
    }

    /**
     * method to set section name
     *
     * @param name a value of type 'String'
     */
    public void setName( String name ){
        _name = name;
    }
    
    public Section copy(){
        return new Section( this );
    }
    
    /**
     * method to compare two sections
     *
     * @param o1 a value of type 'Object'
     * @param o2 a value of type 'Object'
     * @return a value of type 'int'
     */
    public int compareTo( Object o ){
		int result;
        if ( o instanceof Section ) {
            result = _sectNum.compareTo( ( ( Section ) o ).getSectNum() );
        }
        else if ( o instanceof String ) {
            result = _sectNum.compareTo( (String) o );
        }
        else {
            throw new ClassCastException();
        }
        return result;              	
    }
    
    /**
     * Method to represent a sections attributes in a string for the save file
     *
     * @return a 'String' representing the secitons attributes
     */
    public String toSaveString(){
        
	    String tempString = new String();
        tempString += ";" + _sectNum + ";    ";
        tempString += ";" + _name + ";    ";
        tempString += ";" + daysToIOString(_days) + ";    ";
        tempString += ";" + _enrollment + ";    ";
        tempString += ";" + (_startTime + 8) + ";    ";
        tempString += ";" + (_endTime + 8) + ";    ";
        tempString += ";" + attribsToIOString(_attributes)  + ";    ";
        return tempString;
    
    } // toSaveString
    
    /**
     * method convert an array of boolean objects to a 1's and 0's 'String'
     *
     * @param attribs boolean array representing the attributes of a room
     * @return String representing the array passed in
     */
    
    private String attribsToIOString( Object[] attribs ){
        
	String tempString = new String();
	int i;
	
	for( i = 0; i < attribs.length; i++ ){
	    tempString += attribs[i];
	} // for loop
	return tempString;
    } //attribsToIOString
    
    /**
     * method convert an array of boolean objects to a 1's and 0's 'String'
     *
     * @param attribs boolean array representing the attributes of a room
     * @return String representing the array passed in
     */
    
    private String daysToIOString( boolean[] attribs ){
        
	String tempString = new String();
	int i;
	
	for( i = 0; i < attribs.length; i++ ){
	    if ( attribs[i] ){
	        tempString += "1";
	    }
	    else{
	        tempString += "0";
	    }
	} // for loop
	return tempString;
    } // daysToIOString
} // Section Class
