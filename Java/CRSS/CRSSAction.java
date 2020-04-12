/**
 * Class to be inherited by all actions
 *
 * @version    $Id: CRSSAction.java,v 1.41 2000/05/08 16:42:09 aec1324 Exp $
 *
 * @author     Christopher Bunk
 * @author     Adam Chipalowsky
 * @author     Dan Cooley
 * @author     Eric Ferguson
 * @author     Phil Light
 *
 * Revisions:
 *     $Log: CRSSAction.java,v $
 *     Revision 1.41  2000/05/08 16:42:09  aec1324
 *     nothing
 *
 *     Revision 1.40  2000/05/08 02:16:20  aec1324
 *     added the ability to refresh a blank section attrib list
 *
 *     Revision 1.39  2000/05/06 15:19:34  etf2954
 *     Removed yet another System.out
 *
 *     Revision 1.38  2000/05/06 14:36:39  etf2954
 *     Fixed Phil's errant hiLight calls
 *
 *     Revision 1.37  2000/05/05 01:07:00  pel2367
 *     reworked the refresh to try and highlight the
 *     assigned room
 *
 *     Revision 1.36  2000/05/05 00:57:54  pel2367
 *     made a refresh of the room list select the
 *     assigned room, if there is one.
 *
 *     Revision 1.35  2000/05/03 02:41:08  pel2367
 *     fixed the reversals
 *
 *     Revision 1.34  2000/05/03 01:50:02  pel2367
 *     reversed the sorts for sections, but doesn't
 *     seem to work
 *
 *     Revision 1.33  2000/05/02 17:35:36  etf2954
 *     added functionality so that the room that is highlighted
 *     before a filter, is also highlighted after the filter
 *
 *     Revision 1.32  2000/05/02 17:27:13  etf2954
 *     Added the functionality to keep the current room highlighted
 *
 *     Revision 1.31  2000/04/29 03:39:02  pel2367
 *     got the sorts working, but haven't gone to the
 *     trouble yet of doing the reverse order ones.
 *
 *     Revision 1.30  2000/04/28 22:23:21  pel2367
 *     wrote all (i think) of the comparators which
 *     will be needed to sort.  they've been tested,
 *     and seem to work.  the class will need a reversal routine,
 *     though.
 *
 *     Revision 1.29  2000/04/28 20:39:59  pel2367
 *     don't remember, sorry
 *
 *     Revision 1.28  2000/04/28 16:21:11  pel2367
 *     took away the throws from go().
 *
 *     Revision 1.27  2000/04/28 01:40:27  pel2367
 *     took out a PILE of System.out.println
 *
 *     Revision 1.26  2000/04/28 01:30:57  aec1324
 *     working on the edit and found some display problems,
 *     fixed them...I hope
 *
 *     Revision 1.25  2000/04/27 19:25:32  etf2954
 *     Fixed the refreshRoomOcc() funciton to make sure
 *     that there isn't a null pointer there
 *
 *     Revision 1.24  2000/04/27 03:52:55  etf2954
 *     Rewrote refreshRoomAttribs()
 *
 *     Revision 1.23  2000/04/26 15:08:10  etf2954
 *     Fixed the problem: When you add a list of rooms and then try to
 *     click on a section, core dump!!, but that is fixed
 *
 *     Revision 1.22  2000/04/25 21:09:03  aec1324
 *     added the ability to show meeting times in the
 *     section window.
 *
 *     Revision 1.21  2000/04/25 04:51:08  pel2367
 *     fixed the null pointer error in refreshRoomOcc().
 *
 *     Revision 1.20  2000/04/24 23:33:17  aec1324
 *     added the ability to refresh the days column in
 *     the GUI section list window.  Now the
 *     sections have days associated with them...yeah!!!
 *
 *     Revision 1.19  2000/04/24 14:45:12  pel2367
 *     wrote refreshRoomOccupancy(), though somewhere along the line
 *     we still get an ArrayOutOfBounds exception.  I don't care, i'll
 *     fix it tomorrow.
 *
 *     Revision 1.18  2000/04/24 05:12:03  pel2367
 *     Revised refreshSectionList() to adhere
 *     to "None"
 *
 *     Revision 1.17  2000/04/24 04:41:36  etf2954
 *     Fixed the updateSectAttributes
 *
 *     Revision 1.16  2000/04/24 04:22:06  etf2954
 *     Fixed the refreshRoomAttributes, if there is
 *     no rooms loaded
 *
 *     Revision 1.15  2000/04/24 04:09:40  etf2954
 *     fixed up that things, you know, over...gotta go
 *
 *     Revision 1.14  2000/04/24 02:01:46  aec1324
 *     fixed highlight problems
 *     for the most part
 *
 *     Revision 1.13  2000/04/24 00:55:09  pel2367
 *     we hope that the null pointer bug
 *     at startup has been squashed.
 *
 *     Revision 1.12  2000/04/23 22:32:28  pel2367
 *     error message on room sort.
 *
 *     Revision 1.11  2000/04/23 20:36:55  pel2367
 *     debugged; handled some casting issues.
 *
 *     Revision 1.10  2000/04/23 20:08:06  pel2367
 *     made a little fix.
 *
 *     Revision 1.9  2000/04/23 20:03:26  pel2367
 *     well, screw that refreshing - i've decided instead
 *     to try and implement assignments.  this refreshSectlist()
 *     now puts up the assignment for a given section.
 *
 *     Revision 1.8  2000/04/23 18:16:31  pel2367
 *     for some reason, refreshSectAttrib() causes all kinds
 *     of null pointer errors.
 *
 *     Revision 1.7  2000/04/22 22:05:36  pel2367
 *     still reworking the refresh routines, i don't think they
 *     compile.
 *
 *     Revision 1.6  2000/04/22 19:38:00  pel2367
 *     changed refreshSectList.  it no longer automatically
 *     updates the Section attributes, but it may again someday
 *     if I let it.
 *
 *     Revision 1.5  2000/04/20 23:05:50  aec1324
 *     added the ability to update the attributes
 *     table.
 *
 *     Revision 1.4  2000/04/20 02:27:38  aec1324
 *     added the gave it a refrence to the GUI and
 *     database
 *
 *     Revision 1.3  2000/04/18 02:34:47  p361-45a
 *     finished headers
 *
 *     Revision 1.2  2000/04/17 01:26:36  p361-45a
 *     Action name changed to CRSSAction
 *
 *     Revision 1.1  2000/04/17 01:12:37  etf2954
 *     Initial revision
 *
 *     Revision 1.1  2000/04/16 22:51:51  p361-45a
 *     Initial revision
 *
 */

import java.io.*;
import java.util.*;

public abstract class CRSSAction {
    
    // The GUI and the database of files it uses
    protected GUI _gooey;
    protected CRSSDatabase _dbase;
    // what the action will do
    
    /**
     * if an action can't be performed an exception will be thrown
     *
     * @exception BadActionException if an error occurs
     */
    public abstract void go();
    
    /**
     * This method will change the section list to reflect changes
     *
     */
    public void refreshSectList(  ){

	    Vector tempVector = new Vector();
	    Object[][] formattedData;
	    Section tempSection;
	    String finalSectNum;
	    boolean[] dayInQuestion;
	    char[] days = {' ', ' ', ' ', ' ', ' '};
	    char dayToAdd = ' ';

	    //to update the GUI, we need to first get all information from
	    //the database, organise it in the correct formatt and then 
	    //package it (packaging Science, yeah!) and send to GUI.

	    //get the information from CRSS and place into a vector
	    tempVector = _dbase.getSectList();
        
        switch( _gooey.getSectionSortStyle() ){

            case 1:
                // gui wants things sorted by room number in ascending
                // order.
                // do nothing; this is the default sort from the database.
                break;
            case -1:
                // reverse the default sort - the gui wants rooms sorted
                // in descending order.
                tempVector = reverseVector( tempVector );
                break;
            case 2:
                // the gui has indicated that it wants to be sorted by
                // room description.
                Collections.sort( tempVector, new SectionEnrollmentComparator() );
                break;
            case -2:
                Collections.sort( tempVector, new SectionEnrollmentComparator() );
                tempVector = reverseVector( tempVector );
                break;
            case 3:
                // the gui has indicated that it wants to be sorted by
                // room size.
                Collections.sort(tempVector, new SectionAssignmentComparator());
                break;
            case -3:
                Collections.sort(tempVector, new SectionAssignmentComparator());
                tempVector = reverseVector( tempVector );
                break;
            case 4:
                // gui wants us to sort by assignment #
                Collections.sort(tempVector, new SectionNameComparator());
                break;
            case -4:
                // gui wants us to sort by assignment # (reverse order)
                Collections.sort(tempVector, new SectionNameComparator());
                tempVector = reverseVector( tempVector );
            default:
            	// do nothing.
        }

		// now, if we didn't get any sections to refresh, don't do anything.
        // the gui won't like to be given an empty array.

	    //if ( tempVector.size() > 0 ) { 
        
		    //initialise the formattedData Object to the correct size (thx OO!)
	    	formattedData = new Object[tempVector.size()][6];	        
        	for(int i=0; i< tempVector.size(); i++){
	            //the temp vector is a vector of sections. Take one out and place
	            //its contents into a correctly formatted vector.
	            tempSection = ((Section)tempVector.elementAt(i));
	            for(int j=0; j<6; j++){
			        if(j==0){

		    	        formattedData[i][j] = tempSection.getSectNum();

			        }
			        if(j==1){
		    	        formattedData[i][j] = 
				        new Integer(tempSection.getEnrollmt());
			        }	
			        if(j==2){
		    	        formattedData[i][j] = tempSection.getAssignment();
			        }
			        if(j==3){
		    	        formattedData[i][j] = tempSection.getSectName();
			        }
				    if(j==4){
				    //we need to convert from numbers in the days array of the
				    //section to letters.  M=monday, T=tuesday, etc.
				
				    //grab the list of days it meets
				    dayInQuestion = tempSection.getDays();
				    
				    //start a loop to go through all days
				    for(int y=0; y<5; y++){
					dayToAdd = ' ';
					if( dayInQuestion[y] ){
					   
					    //identify what day is to be added to the array
					    if( y == 0 ){
						dayToAdd = 'M';
					    }
					    else if( y == 1 ){
						dayToAdd = 'T';
					    }
					    else if( y == 2 ){
						dayToAdd = 'W';
					    }
					    else if( y == 3 ){
						dayToAdd = 'R';
					    }
					    else if( y == 4 ){
						dayToAdd = 'F';
					    }
					}
					
					//add that day and reset the value for the
					//the next time through
					days[y] = dayToAdd;

				    }//for loop

				    //convert the char arry to a string and simply, very simply
				    //add it to the array that is to be updated
				    String theDays = new String( days );
				    formattedData[i][j] = theDays;

				}//if

				//grab the meeting time and add it to the formatted
				//object data
				if(j==5){

				    //get the start and end time 
				    //and convert to *real* time

				    int tempStartTime = ( 
					new Integer( (tempSection.getStartTime())%12).intValue());
				    int tempEndTime = (
					new Integer( (tempSection.getEndTime())%12).intValue());

				    //figure out if we are talking
				    //about am or pm here...and use that
				    //coolness tropical short had if statement
				    String ampmStart = (tempSection.getStartTime() < 13 ? "am" : "pm");
				    String ampmEnd = (tempSection.getEndTime() < 13 ? "am" : "pm");
				    
				    //if for some reason, a section
				    //starts at 12 or ends at 12
				    //the mod would have set it to
				    //zero, fix that minor oversight
				    //here by simply setting to 12
				    if( tempStartTime == 0 ){
					tempStartTime = 12;
				    }
				    else if( tempEndTime == 0 ){
					tempEndTime = 12;
				    }
				    
				    if( (tempSection.getStartTime()) == 16){
					tempStartTime = 8;
					ampmStart = "am";
				    }
				    if( (tempSection.getEndTime()) == 17 ){
					tempEndTime = 9;
					ampmEnd = "am";
				    }
				    

				    //set the time in the array to be sent
				    //to be updated
				    formattedData[i][j] = new String(tempStartTime + ampmStart +
								    "-" + tempEndTime + ampmEnd);
				
				}//if
				
	            }//inner for

	        }//outter for

	        //now tell the gui we want it to update its Section window

	        _gooey.updateSectionList(formattedData);
		refreshSectionAttrib();
	    //}
    }
    
    /**
     * This method will change the room list to reflect changes
     *
     */
    public void refreshRoomList(){
	    String sectNum = null;	
		String roomNum = null;
        int filterVal;
	    int sortState;
        int removeTally = 0;
        int startingSize = 0;
        Section tempSect = null;

	    Room tempRoom;
	    Vector tempVector = new Vector();
	    Object[][] formattedData;
        Object[][] finalData;

	    //to update the GUI, we need to first get all information from
	    //the database, organise it in the correct formatt and then 
	    //package it (packaging Science, yeah!) and end to GUI.

	    //get the information from CRSS and place into a vector
	    tempVector = _dbase.getRoomList();
        // now find out how the info should be sorted
        switch( _gooey.getRoomSortStyle() ){

            case 1:
                // gui wants things sorted by room number in ascending
                // order.
                // do nothing; this is the default sort from the database.
                break;
            case -1:
                // reverse the default sort - the gui wants rooms sorted
                // in descending order.
                tempVector = reverseVector( tempVector );
                break;
            case 2:
                // the gui has indicated that it wants to be sorted by
                // room size.
                Collections.sort( tempVector, new RoomCapComparator() );
                break;
            case -2:
                Collections.sort( tempVector, new RoomCapComparator() );
                tempVector = reverseVector( tempVector );
                break;
            case 3:
                // the gui has indicated that it wants to be sorted by
                // room description.
                Collections.sort( tempVector, new RoomNameComparator() );
                break;
            case -3:
                Collections.sort( tempVector, new RoomNameComparator() );
                tempVector = reverseVector( tempVector );
                break;
            default:
            	// do nothing.
        }
            


		    //initialise the formattedData Object to the correct size (thx OO!)
            startingSize = tempVector.size();
		    formattedData = new Object[startingSize][3];            

	        // find out which section and room is selected
            sectNum = _gooey.getHighlightedSection();
			roomNum = _gooey.getHighlightedRoom();

                if( sectNum != null && sectNum.length() != 2 ) {


                    tempSect = _dbase.getSection( sectNum );

	                // set the filterval, the minimum size for
	                // a room which is to be showed.

                    filterVal = ( tempSect.getEnrollmt() );
                    // eliminate any rooms from the tempVector which are too 
                    // small to house the selected section.
                    for( int iter = 0 ; iter < tempVector.size() ; iter++ ) {
    	                if( ( (Room) tempVector.elementAt( iter ) ).getCapac() < filterVal ){
                            tempVector.remove( iter );
                            iter = iter - 1;
                            removeTally = removeTally + 1;
                        }
	                }

                } // is there even a section selected?

	            // here is where we will ask the gui how it would like its "eggs"
                // i.e. sort state, and sort the tempVector accordingly.

                // now that we have tempVector prepared, format it into a 2d array of width
                // 3, and send it to the GUI.

                for(int i=0; i< tempVector.size(); i++){
	                //the temp vector is a vector of rooms. Take one out and place
	                //its contents into a correctly formatted vector.
	                tempRoom = ((Room)tempVector.elementAt(i));
                    for(int j=0; j<4; j++){

                        if(j==0){
                            formattedData[i][j] = tempRoom.getRoomNum();
       	                }
                        if(j==1){
                            formattedData[i][j] = new Integer(tempRoom.getCapac());
                        }
                        if(j==2){
                            formattedData[i][j] = tempRoom.getDescription();
                        }
                    }//inner For
                }//outer for

				// the formattedData contains a number of blank rows now, because
                // we filtered away any rooms which weren't big enough.  those 
                // rows should be removed from the result we're about to send.
                
                finalData = new Object[ startingSize - removeTally ][3];
                for( int iter = 0; iter < ( startingSize - removeTally ) ; iter++ ){
                	finalData[iter][0] = formattedData[iter][0];
                    finalData[iter][1] = formattedData[iter][1];
                    finalData[iter][2] = formattedData[iter][2];
                }
                
	            //now tell the gui we want it to update its Section window                
	            _gooey.updateRoomList( finalData );	    
                
                if( tempSect != null ){
					if( !tempSect.getAssignment().equals( "None" ) ) {
						_gooey.setHiLightedRoom( tempSect.getAssignment() );
					} else {
						if( roomNum != null ) {
							_gooey.setHiLightedRoom( roomNum );
						}
					}
				}
        //} // is there anything to refresh?
    } // refreshRoomList
    
    /**
     * This method will change the room attributes to reflect changes
     *
     */
    public void refreshRoomAttrib(){
		String roomNum = "";
		Room anyRoom;
		Object[] blankAttribs;
		
		roomNum = _gooey.getHighlightedRoom();
		if( roomNum != null ) {
			if( roomNum.length() != 2 ) {
				anyRoom = _dbase.getRoom( roomNum );
				_gooey.updateRoomAttributes( anyRoom.getAttrib() );
			}
		} 
		else {
			blankAttribs = new Object[ 7 ];
			for( int i = 0 ; i < 7 ; i++ ) {
				blankAttribs[ i ] = "0";
			} // for
			_gooey.updateRoomAttributes( blankAttribs );
		}// if
    }// refreshRoomAttrib

    
    /**
     * This method will change the section attributes to reflect changes
     *
     */
    public void refreshSectionAttrib(){
        String sectNum = "";
        Section anySect;
		Object[] blankAttribs;
	
	    sectNum = _gooey.getHighlightedSection();
		if( sectNum != null ) {
			if ( sectNum.length() != 2 ) {
			   anySect = _dbase.getSection( sectNum );
			   _gooey.updateSectionAttributes( anySect.getAttrib() );
			}// if
			else{
				blankAttribs = new Object[ 7 ];
				for( int i = 0 ; i < 7 ; i++ ) {
					blankAttribs[ i ] = "0";
				} // for
				_gooey.updateSectionAttributes( blankAttribs );
			}// if
		} // upper IF
		else{
		    blankAttribs = new Object[ 7 ];
		    for( int i = 0 ; i < 7 ; i++ ) {
			blankAttribs[ i ] = "0";
		    } // for
		    _gooey.updateSectionAttributes( blankAttribs );
		}//else
    } // refreshSectionAttrib()
	



    /**
     * This method will change the room occupancy
     *
     */
    public void refreshRoomOcc(){
    	String roomNum = "";
        Room anyRoom;
		String[][] _occupancy = { { "     ", "     ", "     ", "     ",
								"     ", "     ", "     ", "     ", "     ",
								"     ", "     ", "     ", "     ", "     "},
							  { "     ", "     ", "     ", "     ", "     ",
								"     ", "     ", "     ", "     ", "     ",
							    "     ", "     ", "     ", "     "}, { "     ",
	    						"     ", "     ", "     ", "     ", "     ",
								"     ", "     ", "     ", "     ", "     ",
								"     ", "     ", "     "}, { "     ", "     ",
							    "     ", "     ", "     ", "     ", "     ",
								"     ", "     ", "     ", "     ", "     ",
								"     ", "     "}, { "     ", "     ", "     ",
								"     ", "     ", "     ", "     ", "     ",
								"     ", "     ", "     ", "     ", "     ",
								"     "} };
        roomNum = _gooey.getHighlightedRoom();
		if( roomNum != null ) {
			if( roomNum.length() != 2 ) {
				anyRoom = _dbase.getRoom( roomNum );
				_gooey.updateRoomOccupancy( anyRoom.getOccpcy() );
			} 
		} // if
		else {
			_gooey.updateRoomOccupancy( _occupancy );	
		} // if
    }

    private Vector reverseVector( Vector in ){
        Vector out = new Vector( in.size() );
        for( int iter = 0; iter < in.size() ; iter++ ){
            out.add( iter, in.elementAt( in.size() - iter - 1 ) );
        }
        return out;
    }




    /*
     *
     *  What follows are the comparators which will be used for the various
     *  types of sort.
     *
     *
     */




    /**
     * This class is used to compare room capacities
     */
    private class RoomCapComparator implements Comparator{

        /**
	     * this will compare two rooms capacities
	     *
	     * @param o1 a value of type 'Object'
	     * @param o2 a value of type 'Object'
	     * @return a value of type 'int'
	     */
	    public int compare( Object o1, Object o2){
            int result = 0;
            if( o1 instanceof Room && o2 instanceof Room ){
                if(((Room) o1).getCapac() < ((Room) o2).getCapac()){
                    result = -1;
                }
                else if(((Room) o1).getCapac() ==
                        ((Room) o2).getCapac()){
                    result = 0;
                }
                else{
                    result = 1;
                }
            }
            else{
                throw new ClassCastException();
            }
            return result;
        }
        /**
	     * This method will return true or false depending on if room 
	     * capacities are equal
	     *
	     * @param o1 a value of type 'Object'
	     * @return a value of type 'boolean'
	     */
	    public boolean equals(Object obj){
                boolean result = false;
                if( obj instanceof RoomCapComparator ){
                	result = true;
                }
                return result;
        }
    } // roomCapComparator
    
    /**
     *  This class is used to compare room numbers 
     */
    private class RoomNameComparator implements Comparator{

        /**
         * This method will compare two room's numbers.
         *
         * @param o1 a value of type 'Object'
         * @param o2 a value of type 'Object'
         * @return a value of type 'int'
         */
        public int compare( Object o1, Object o2){
            int result;
            if( o1 instanceof Room && o2 instanceof Room ){
                result = (((Room) o1).getDescription().compareTo(
                          ((Room) o2).getDescription()));
            }
            else{
                throw new ClassCastException();
            }
            return result;
        }
            /**
         * returns whether or not the two rooms are equal
         *
         * @param o1 a value of type 'Object'
         * @return a value of type 'boolean'
         */
        public boolean equals(Object obj){
            boolean result = false;
            if( obj instanceof RoomNameComparator ){
                result = true;
            }
            return result;
        }
    } // roomNumComparator
    
    /**
     * This class is used to compare section enrollments
     */
    private class SectionEnrollmentComparator implements Comparator{
        public int compare( Object o1, Object o2 ){
            int result = 0;
            if( o1 instanceof Section && o2 instanceof Section ){
                if(((Section) o1).getEnrollmt() < 
                   ((Section) o2).getEnrollmt()){
                    result = -1;
                }
                else if(((Section) o1).getEnrollmt() ==
                        ((Section) o2).getEnrollmt()){
                    result = 0;
                }
                else{
                    result = 1;
                }
            }
            else{
                throw new ClassCastException();
            }
            return result;
        }
        /**
	     * This method will return true or false depending on if room 
	     * capacities are equal
	     *
	     * @param o1 a value of type 'Object'
	     * @return a value of type 'boolean'
	     */
	    public boolean equals(Object obj){
            boolean result = false;
            if( obj instanceof SectionEnrollmentComparator ){
                result = true;
            }
            return result;
        }
    } // sectionEnrollComparator
    
    /**
     * Class to compare section names
     */
    private class SectionNameComparator implements Comparator{

        /**
         * This method will compare two room's numbers.
         *
         * @param o1 a value of type 'Object'
         * @param o2 a value of type 'Object'
         * @return a value of type 'int'
         */
        public int compare( Object o1, Object o2){
            int result;
            if( o1 instanceof Section && o2 instanceof Section ){
                result = ((((Section) o1).getSectName()).compareTo(
                           ((Section) o2).getSectName()));
            }
            else{
                throw new ClassCastException();
            }
            return result;
        }
            /**
         * returns whether or not the two rooms are equal
         *
         * @param o1 a value of type 'Object'
         * @return a value of type 'boolean'
         */
        public boolean equals(Object obj){
                boolean result = false;
                if( obj instanceof SectionNameComparator ){
                	result = true;
                }
                return result;
        }
    } // SectionNameComparator    
    
    private class SectionAssignmentComparator implements Comparator{

        /**
         * This method will compare two room's numbers.
         *
         * @param o1 a value of type 'Object'
         * @param o2 a value of type 'Object'
         * @return a value of type 'int'
         */
        public int compare( Object o1, Object o2){
            int result;
            if( o1 instanceof Section && o2 instanceof Section ){
                result = ((((Section) o1).getAssignment()).compareTo(
                           ((Section) o2).getAssignment()));
            }
            else{
                throw new ClassCastException();
            }
            return result;
        }
            /**
         * returns whether or not the two rooms are equal
         *
         * @param o1 a value of type 'Object'
         * @return a value of type 'boolean'
         */
        public boolean equals(Object obj){
                boolean result = false;
                if( obj instanceof SectionAssignmentComparator ){
                	result = true;
                }
                return result;
        }
    } // SectionNameComparator        
    
    /**
     * Class to compare section numbers
     */

    
}
