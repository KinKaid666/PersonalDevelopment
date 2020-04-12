
/**
 * This is the window that popups when the user wants to add one section
 *
 * @version	$Id: AssignAllAction.java,v 1.8 2000/05/10 15:40:39 etf2954 Exp $
 *
 * @author	Phil Light 
 *
 * Revisions:
 *		$Log: AssignAllAction.java,v $
 *		Revision 1.8  2000/05/10 15:40:39  etf2954
 *		Removed System.out's
 *
 *		Revision 1.7  2000/05/03 01:50:02  pel2367
 *		tinkered some more
 *
 *		Revision 1.6  2000/05/01 16:21:01  p361-45a
 *		declared boolean result as false so it would compile
 *
 *		Revision 1.5  2000/05/01 14:46:27  pel2367
 *		tried to figure out why assignments made in
 *		this class show up in the database versions of
 *		the rooms... and why the same thing doesn't
 *		happen for sections... these rooms & sections
 *		are supposed to be COPIES anyway.
 *
 *		Revision 1.4  2000/05/01 03:40:05  pel2367
 *		umm... it makes assignments.  i don't know how,
 *		i don't know why, and they're not the right ones.
 *		i'm going to have fun the next few days.
 *
 *		Revision 1.3  2000/04/30 23:24:01  pel2367
 *		worked on the algorithm, getting the
 *		ClassCastExceptions now in bumpable().
 *
 *		Revision 1.2  2000/04/30 20:25:18  pel2367
 *		initial compilation, for some reason it's getting
 *		a capacity of 1 for every single room.
 *
 *		Revision 1.1  2000/04/29 23:52:00  pel2367
 *		Initial revision
 *
 *		
 *		
 *		
 *		
 */
 
import java.util.*;
import java.io.*;
import java.lang.*;

class AssignAllAction extends CRSSAction implements UndoableAction{

    private Vector _sectionPowers = new Vector();
    private Vector _roomPowers = new Vector();
    private Vector _assignments = new Vector();
    private Vector _unassignables = new Vector();
    private boolean _adhere = true;
    
    public AssignAllAction( GUI gui, CRSSDatabase database, boolean adhere ) 
        throws BadActionException{
        
        _gooey = gui;
        _dbase = database;
        if( _gooey != null && _dbase != null ){
            _sectionPowers = generateSectionPowers();
            _roomPowers = generateRoomPowers();
             
            // now, we need a list of rooms and a list of assignments for
            // this to work. did we get enough data to make any assignments?
            if( _sectionPowers.size() > 0 && _roomPowers.size() > 0 ){
                _assignments = makeAssignments();
                go();
            } // are there assignable sections and rooms to put them in?
            else{
                _gooey.defaultErrorMsgWindow( "Cannot perform assignments " +
                    "without unassigned\nsections and rooms to put them in." );
                throw new BadActionException();
            } // else
        } // do we have a gui and database to work with?
        else{ 
            throw new IllegalArgumentException();
        } // else
    } // constructor
    
    private Vector generateSectionPowers(){
        Vector sections = _dbase.getSectList();
        Section current;
        SectionPower addition;
        int additionLocation;
        Vector result = new Vector( sections.size() );
        
        for( int iter = 0; iter < sections.size(); iter++ ){
            current = (Section) sections.elementAt( iter );
            if( (current.getAssignment()).equals( "None" ) ){
                // the section has no assignment, so this class will try to
                // make one.  add its power to the result.
                addition = new SectionPower( current );
                additionLocation = 
                    Collections.binarySearch( result, addition );
                if( additionLocation < 0 ){
                    // the new addition has no equivalent in the vector.
                    // add it according to the binarySearch's description
                    result.add( -( additionLocation + 1), addition );
                } // was this sectionPower in the vector?
                else{
                    // the new addtion was found in the vector... that means
                    // there's already a sectionPower in the list which has
                    // that value.
                    result.add( additionLocation, addition );
                } // else
            } // will we attempt to assign this secion?
            else{
                // the section has already been assigned by the user. far be it
                // from us to override their wisdom.
                // (do nothing, the sectionPower will not be added, and the
                // iter will just skip over it.)
            }
        } // loop through all the sections in the CRSSDatabase
        
        return result;
    } // generateSectionPowers
    
    private Vector generateRoomPowers(){
        Vector rooms = _dbase.getRoomList();
        Room current;
        RoomPower addition;
        int additionLocation;
        Vector result = new Vector( rooms.size() );
        
        // this is slightly less complicated than generateSectionPowers,
        // because there are no rooms that we want to omit from our collection.
        
        for( int iter = 0; iter < rooms.size(); iter++ ){
            current = (Room) rooms.elementAt( iter );
            addition = new RoomPower( current );
            additionLocation = 
                Collections.binarySearch( result, addition );
            if( additionLocation < 0 ){
                // the new addition has no equivalent in the vector.
                // add it to according to the binarySearch's description
                result.add( -( additionLocation + 1), addition );
            } // was this roomPower in the vector?
            else{
                // the new addition was found in the vector... that means
                // there's already a roomPower in the list which has 
                // that power value.
                result.add( additionLocation, addition );
            } // else
        } // loop through all the rooms in the CRSSDatabase
        
        return result;
    } // generateRoomPowers
    
    private Vector makeAssignments(){
        Vector result = new Vector( _sectionPowers.size() );
        Section currentSect;
        Room currentRoom;
        boolean cont = true;
        int index;
        int roomIndex = 0;
        int sectIndex = _sectionPowers.size() - 1;
        
        // this algorithm begins by attempting to place the biggest (most
        // powerful) section into the smallest possible ( least powerful )
        // room.  these next two lines initialize the section and room to
        // begin the algorithm.


        
        do{
            currentSect = 
                ((SectionPower) _sectionPowers.elementAt( sectIndex )).getSection();
            // the section will begin searching with the smallest room.

       
            do{
                currentRoom = ((RoomPower) _roomPowers.elementAt( roomIndex )).getRoom();
                if( bumpable( currentSect, currentRoom )){
                    // we've found a suitable room for this section.
                    cont = false;
                }
                else{
                    // the currentRoom won't do, let's try the one with the next
                    // index.
                    roomIndex = roomIndex + 1;
                }

                
            } while( roomIndex < _roomPowers.size() && cont == true );   
            
            // that loop basically searched through the rooms for an acceptable.
            // if it found one, then the roomIndex is less than _roomPower.size().
            // in this case, we'll bump the section into that room.
            
            if( roomIndex < _roomPowers.size() ){
                bumpIn( currentSect, currentRoom );
            }
            else{
                _unassignables.add( currentSect );
            }
            
            // reset the room search for the next section.      
            sectIndex = sectIndex - 1;
            roomIndex = 0;
            cont = true;
        } while( sectIndex >= 0 );
        // this loop needs to quit when we have tried to make an 
        // assignment for every section, starting with the most
        // powerful, ending with the least powerful.
        return result;
    } // makeAssignments
    
    private boolean bumpable( Section anySect, Room anyRoom ){
        // this routine determines whether a section will be assignable
        // to this room; if it is, return true.
        // if it's not, see if the section can be made to fit
        // by booting out the conflicting section(s), and finding an 
        // assignment for them.


        boolean result = false;
        int fit;

        if( anyRoom.getCapac() >= anySect.getEnrollmt() ){
            // the room is big enough to assign into here, let's
            // see whether the section fits into this room.
            fit = assignable( anySect, anyRoom );

            if( fit == 1 ){
                // everything matches up.
                // return 1.
                result = true;
            }
            else if( fit == -1 && _adhere == false){
                // the attributes don't 
                // match up, but we don't care because we're not adhering
                // to add-ons in this run.
                result = true;
            }
            else if( fit == -1 && _adhere == true){
                // the attributes dont'
                // meet the requests of the room... since we're
                // trying to schedule only into rooms with all the
                // add-ons, we'll return false.
                result = false;
            }
            else{
                // this is the interesting one... the section has conflicts
                // in the room.  let's find those conflicts, and see
                // if they're bumpable into better rooms.
                Vector conflicts = getConflicts( anySect, anyRoom );
                for( int iter = 0; iter < conflicts.size(); iter++ ){
                    // initialize a boolean, and find out what the
                    // power index of the room currently assigned is.
                    String theConflictNum = (String) conflicts.get( iter );
                    Section theConflict;
                    int find = Collections.binarySearch( _sectionPowers,
                        theConflictNum);
//                    int find = _sectionPowers.indexOf( theConflictNum );
                    if( find < 0 ){
                        // the number of the conflict is not known by this
                        // class. therefore, it was a section scheduled into
                        // this room before we started.  it is not bumpable.
                        result = false;
                    }
                    else{
                        theConflict = 
                            ((SectionPower) _sectionPowers.get( find )).getSection();

                        Room trial = null;
                        boolean cont;
                        int index = Collections.binarySearch( 
                            _roomPowers, 
                            theConflict.getAssignment());
                        index = index + 1;

                        while( index >= _roomPowers.size() || result == true ){
                            trial = 
                                ((RoomPower) _roomPowers.elementAt( iter )).getRoom();
                            if( bumpable( theConflict, trial ) ) {
                                result = true;
                            }
                            else{
                                index = index + 1;
                            }
                        } // while loop to check all the rooms with increased power.
                    } // else
                } // for loop to check all the conflicts
            } // else
        } // is the room big enough?
        else{
            // the room is too small for this section.
            result = false;
        } // else
        return result;
    } // bumpable
    
    private void bumpIn( Section anySect, Room anyRoom ){

        int fit;

        if( anyRoom.getCapac() >= anySect.getEnrollmt() ){
            // the room is big enough to assign into here, let's
            // see whether the section fits into this room.
            fit = assignable( anySect, anyRoom );

            if(( fit == 1 ) || (fit == -1 && _adhere == false )){
                // everything matches up.
                // make an Assignment object, then reflect it by 
                // making the assignment in our neck of the woods.
                int index = Collections.binarySearch( _assignments, 
                                                      anySect.getSectNum() );
                //anyRoom.assign( anySect );
                //anySect.assign( anyRoom.getRoomNum() );
                if( index < 0 ){
                    // that section has never been assigned before.
                    Assignment addition = new Assignment( anySect, anyRoom);
                    _assignments.add( -(index + 1), addition );

 
                }
                else{
                    // we've already assigned that section to a room,
                    // just tell it to take a new room.
                    ((Assignment) _assignments.get( index )).swapRoom( anyRoom );

                }
            } // everything matched.
            else{
                // this is the interesting one... the section has conflicts
                // in the room.  let's find those conflicts, and bump them
                // into the better rooms.
                Vector conflicts = getConflicts( anySect, anyRoom );
                for( int iter = 0; iter < conflicts.size(); iter++ ){
                    // initialize a boolean, and find out what the
                    // power index of the room currently assigned is.
                    boolean cont;
                    String theConflictNum = (String) conflicts.get( iter );
                    Section conflict;

                    int find = Collections.binarySearch( _sectionPowers,
                        theConflictNum);
                    conflict = (
                        (SectionPower) _sectionPowers.get( find )).getSection();
                    
                    int index = Collections.binarySearch( _roomPowers, 
                        conflict.getAssignment());
                    index = index + 1;
                    while( index >= _roomPowers.size() ){

                        Room possibleRoom = 
                            ((RoomPower) _roomPowers.get( iter )).getRoom();
                        if( bumpable( conflict, possibleRoom ) ){
                            //conflict.unassign();
                            //anyRoom.unassign( conflict.getSectNum() );
                            bumpIn( conflict, possibleRoom );
                        }
                        else{
                            index = index + 1;
                        }
                    } // while loop to check all the rooms with increased power.
                } // for loop to check all the conflicts
            } // else
        } // is the room big enough?
        else{
            // the room is too small for this section.
        } // else
    } // bumpIn
    
    private int assignable( Section anySect, Room anyRoom ){
        // this routine must be written because the database does not
        // keep an up-to-date representation of the assignments are
        // considering.  this algorithm is based on making and breaking
        // assignments to achieve the best possible fit.  in the interest
        // of speed, space, and undoability, the assignments currently
        // being considered are kept in the list _assignments.
        int result;
        int fit;
        
/*        for( int iter = 0; iter < _assignments.size(); iter++ ){
            // does this assignment concern the room in question?
            if( ( ( String ) ( ( Room ) ( ( Assignment ) _assignments.elementAt( iter ) ).getRoom() ).getRoomNum() ).equals( anyRoom.getRoomNum() ) ) {
                // make the assignment.
                ( ( Room ) ( ( Assignment ) _assignments.elementAt( iter ) ).getRoom() ).assign( ( Section ) ( ( Assignment ) _assignments.elementAt( iter ) ).getSection() );
            }
        } // for loop
*/        
        // now, the room passed in is up-to-date with all the assignments 
        // we currently have lined up for it.  does the section still fit
        // into the room? we'll ask the database to tell us.
        
        result = _dbase.assignable( anySect, anyRoom );
        
        // ok, you've got your answer... now unassign all those sections
        // because the next time around, those assignments may be different.
/*        
        for( int iter = 0; iter < _assignments.size(); iter++ ){
            // does this assignment concern the room in question?
            if( ( ( String ) ( ( Room ) ( ( Assignment ) _assignments.elementAt( iter ) ).getRoom() ).getRoomNum() ).equals( anyRoom.getRoomNum() ) ) {
                // make the unassignment.
                 ( ( Room ) ( ( Assignment ) _assignments.elementAt( iter ) ).getRoom() ).unassign( ( ( Section ) ( ( Assignment ) _assignments.elementAt( iter ) ).getSection() ).getSectNum() );
            }
        } // for loop
*/        
        return result;        
    } // assignable

    private Vector getConflicts( Section anySect, Room anyRoom ){


        Vector result = new Vector( 0 );
        String[][] roomOcc;
        String[][] secTimes;
        
        
        if( !((String) anySect.getAssignment()).equals( anyRoom.getRoomNum() ) ){
            roomOcc = anyRoom.getOccpcy();
            secTimes = ((String[][]) anySect.getTimes());
            for( int day = 0; day < 5; day++ ){
                for( int time = 0; time < 14; time++ ){
                    if( secTimes[day][time].equals( "1" ) ){
                        // the section meets now.  let's see if
                        // there's a new conflict.
                        if( (roomOcc[day][time]).equals( "     " ) &&
                            !result.contains( roomOcc[day][time] ) ){
                            result.add( roomOcc[day][time] );
                        } // if
                    } // if
                } // for
            } // for
        } // if
        return result;
    } // getConflicts

    public void go(){
        for( int iter = 0; iter < _assignments.size(); iter++ ){
            ((Assignment) _assignments.get( iter )).assign();
        }
        refreshSectList();
        refreshRoomOcc();
        refreshSectionAttrib();
        refreshRoomAttrib();
    } // go
    
    public void undo(){
        for( int iter = 0; iter < _assignments.size(); iter++ ){
            ((Assignment) _assignments.get( iter )).unassign();
        }
        refreshSectList();
        refreshRoomOcc();
        refreshSectionAttrib();
        refreshRoomAttrib();
    } // undo



    private class SectionPower implements Comparable{  
        Section mySection;
        int myPower;
        
        public SectionPower( Section s ){
            mySection = s;
            myPower = s.getEnrollmt();

            if( _adhere == true ){
                Object[] tempArray = mySection.getAttrib();
                for( int i = 0; i < 7; i++ ){
                    if( ((String) tempArray[ i ]).equals( "1" ) ){
                        myPower = myPower +1;
                    } // if
                } // for loop
            } // are we counting attributes for power?
        } // constructor
        
        public Section getSection(){
            return mySection;
        }
        
        public int getPower(){
            return myPower;
        }
        
        public int compareTo( Object obj ){
            int result = 0;
            
            if( obj instanceof SectionPower ){
                if( myPower < ((SectionPower) obj).getPower() ){
                    result = -1;
                }
                else if( myPower == ((SectionPower) obj).getPower() ){
                    if( mySection.getEnrollmt() < 
                        ((Section)((SectionPower)obj).getSection()).getEnrollmt()){
                        result = -1;
                    }
                    else if( mySection.getEnrollmt() == 
                        ((Section)((SectionPower)obj).getSection()).getEnrollmt()){
                        result = 0;
                    }
                    else if( mySection.getEnrollmt() > 
                        ((Section)((SectionPower)obj).getSection()).getEnrollmt()){
                        result = 1;
                    }
                }
                else{
                    result = 1;
                }
            }
            else if( obj instanceof String ){
                result = ((String) mySection.getSectNum()).compareTo(
                    (String) obj );
            }
            else{
                throw new ClassCastException();
            }
            return result;
        }
    } // SectionPower


    private class RoomPower implements Comparable{  
        Room myRoom;
        int myPower;
        
        public RoomPower( Room r ){
            myRoom = r;
            myPower = r.getCapac();

            if( _adhere == true ){
                Object[] tempArray = myRoom.getAttrib();
                for( int i = 0; i < 7; i++ ){
                    if( ((String) tempArray[ i ]).equals( "1" ) ){
                        myPower = myPower +1;
                    } // if
                } // for loop
            } // are we counting attributes for power?
        } // constructor
        
        public Room getRoom(){
            return myRoom;
        }
        
        public int getPower(){
            return myPower;
        }
        
        public int compareTo( Object obj ){
            int result = 0;
            
            if( obj instanceof RoomPower ){
                if( myPower < ((RoomPower) obj).getPower() ){
                    result = -1;
                }
                else if( myPower == ((RoomPower) obj).getPower() ){
                    if( myRoom.getCapac() < 
                        ( (Room)((RoomPower) obj).getRoom()).getCapac() ){
                        result = -1;
                    }
                    else if( myRoom.getCapac() == 
                        ( (Room)((RoomPower) obj).getRoom()).getCapac() ){
                        result = 0;
                    }
                    else if( myRoom.getCapac() > 
                        ( (Room)((RoomPower) obj).getRoom()).getCapac() ){
                        result = 1;
                    }
                }
                else{
                    result = 1;
                }
            }
            else if( obj instanceof String ){
                result = ((String)(myRoom.getRoomNum())).compareTo( 
                    (String) obj );
            }
            else{
                throw new ClassCastException();
            }
            return result;
        } // compareto
    } // RoomPower

    private class Assignment implements Comparable{
        private Section mySection;
        private Room myRoom;
        
        public Assignment( Section s, Room r ){
            mySection = s;
            myRoom = r;
        }
        
        public Section getSection(){
            return mySection;
        }
        
        public Room getRoom(){
            return myRoom;
        }
        
        public void assign(){
            _dbase.assign( mySection, myRoom );
        }
        
        public void unassign(){
            _dbase.unassign( mySection.getSectNum() );
        }
        
        public void swapRoom( Room r ){
            myRoom = r;
        }
        
        public int compareTo( Object obj ){
            int result = 0;
            
            if( obj instanceof Assignment ){
                result =  (mySection.getSectNum()).compareTo( 
                        ((Section) ((Assignment) obj).getSection()).getSectNum() );
            }
            else if( obj instanceof String ){
                result = (mySection.getSectNum()).compareTo( (String) obj );
            }
            else{
                throw new ClassCastException();
            }
            return result;
        }     
    } // class Assignment

} // class AssignAllAction
            
            
            
            
            
            
            
            
            
            
            
            
            
            
            
            
            
            
            
            
            
            
            
            
            
            
            
            
            
            
            
            
            
            
            
            
            
            
            
            
            
            
            
            
            
            
            
            
            
        
