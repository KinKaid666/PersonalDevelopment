/**
 * Class that perfoms the Add Section action
 *
 * @version    $Id: AddSectAction.java,v 1.34 2000/05/10 15:40:39 etf2954 Exp $
 *
 * @author     Christopher Bunk
 * @author     Adam Chipalowsky
 * @author     Dan Cooley
 * @author     Eric Ferguson
 * @author     Phil Light
 *
 * Revisions:
 *     $Log: AddSectAction.java,v $
 *     Revision 1.34  2000/05/10 15:40:39  etf2954
 *     Removed System.out's
 *
 *     Revision 1.33  2000/05/10 14:30:47  pel2367
 *     threw a badActionException when the user cancels the file
 *     browser. this keeps cancelled load from file from
 *     being pushed on the stack
 *
 *     Revision 1.32  2000/05/10 00:04:04  aec1324
 *     worked on the undo a bit
 *
 *     Revision 1.31  2000/05/08 02:42:29  p361-45a
 *     fixed formatting
 *
 *     Revision 1.30  2000/05/08 02:16:20  aec1324
 *     fixed a conversion problem between object and
 *     string in the constructor that takes a file
 *
 *     Revision 1.29  2000/05/06 01:15:39  pel2367
 *     rewrote readSections to work.
 *
 *     Revision 1.28  2000/05/05 17:52:56  pel2367
 *     added copy section.
 *
 *     Revision 1.27  2000/05/05 14:50:52  pel2367
 *     sendsection works with a null argument now.
 *
 *     Revision 1.26  2000/05/05 02:23:55  pel2367
 *     tinkered, fixed some stuff but broke
 *     other stuff
 *
 *     Revision 1.25  2000/05/02 17:46:02  cmb3548
 *     Constructor now takes gui and dbase
 *
 *     Revision 1.24  2000/05/02 03:05:24  cmb3548
 *     New constructor added for adding sections from file
 *
 *     Revision 1.23  2000/04/28 17:05:39  pel2367
 *     wrote undo()
 *
 *     Revision 1.22  2000/04/27 19:25:52  etf2954
 *     Fixed Dan's mistakes...again :) *sigh*
 *
 *     Revision 1.21  2000/04/27 18:33:46  p361-45a
 *     fixed remaining problems found during code inspection
 *
 *     Revision 1.20  2000/04/27 02:01:08  aec1324
 *     changed the way it creates a AddSingleSection
 *     Window to take into account the new constructor
 *     in it.
 *
 *     Revision 1.19  2000/04/26 16:55:57  etf2954
 *     added a file filter
 *
 *     Revision 1.18  2000/04/26 02:44:55  etf2954
 *     Added the popup window when the user tries to add
 *     from a file, but the file contains sections that
 *     are already in the database
 *
 *     Revision 1.17  2000/04/25 04:51:08  pel2367
 *     fixed file input (startTime, endTime) are now
 *     converted to indeces, not military.
 *
 *     Revision 1.16  2000/04/24 17:47:31  aec1324
 *     got rid of any unneeded debuging information
 *     from before when I get the add from file
 *     working
 *
 *     Revision 1.15  2000/04/24 17:20:18  aec1324
 *     got this thing reading in sections from a file
 *
 *     Revision 1.14  2000/04/23 18:16:31  pel2367
 *     kept reworking, managed to get the window to close
 *     again by commenting out the refreshSectAttrib()
 *
 *     Revision 1.13  2000/04/22 22:05:36  pel2367
 *     i don't even know why this file is checking in.
 *
 *     Revision 1.12  2000/04/22 19:38:00  pel2367
 *     took away the auto-formatting which was causing problems
 *     (i.e. hyphens).
 *
 *     Revision 1.11  2000/04/22 18:25:02  pel2367
 *     totally reworked it to make it adhere to at least
 *     the concept of the design.
 *
 *     Revision 1.10  2000/04/22 00:51:39  aec1324
 *     started working with setting up the boolean
 *     meeting time array
 *
 *     Revision 1.9  2000/04/20 23:05:50  aec1324
 *     changed attributes from a boolean[]
 *     to an Object[]
 *
 *     Revision 1.8  2000/04/20 02:27:38  aec1324
 *     sections can now be added and the GUI is updated
 *
 *     Revision 1.7  2000/04/19 20:43:58  aec1324
 *     stuff
 *
 *     Revision 1.6  2000/04/19 16:37:22  aec1324
 *     working with the way it talks to a section
 *
 *     Revision 1.5  2000/04/19 14:28:38  etf2954
 *     Tried to get the popup window to return info
 *
 *     Revision 1.4  2000/04/18 01:46:12  p361-45a
 *     *** empty log message ***
 *
 *     Revision 1.3  2000/04/17 17:31:42  p361-45a
 *     added method headers
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

public class AddSectAction extends CRSSAction 
        implements UndoableAction{

    private LinkedList _additions = new LinkedList();
    private LinkedList unAddableSections = new LinkedList();
    private AddSingleSectionWindow newSectionWindow; 
    private Operator _operator;
	
    
    /**
     * creates object of type AddSectAction 
     *
     * @param datasrc int that specifies where the source is coming from
     */
    public AddSectAction( GUI gui, CRSSDatabase database, int datasrc,
			  Operator oPerator ) throws IOException, 
	                                             BadActionException{
	String[] extensions = { "section" };
	_operator = oPerator;
	_gooey = gui;
	_dbase = database; 
	
	//to figure out whether to get only one section or a bunch from file
	//a integer comparison is made.  If the datasrc that was sent in is
	//zero, then we only want to add one section at a time.  If it's 1
	//then we want to add multable sections from some file somewhere.
	//if it's 2, then we know to copy the section from another section
	if( datasrc == 0 ){
	    
	    //create a addSection window that is used for creating a new
	    //section.  To do this use the first constructor
	    newSectionWindow = new AddSingleSectionWindow(this);
	    
	    newSectionWindow.getUserInfo();
	}

	else if( datasrc == 1 ){

    	    // open a file and add sections from it.
	    ExtensionFileFilter filter =
		new ExtensionFileFilter
		( extensions, "Section Lists (*.section)" );
	    JFileChooser fileChooser = new JFileChooser();
	    fileChooser.addChoosableFileFilter( filter );
		fileChooser.setFileFilter( filter );
		int returnVal = fileChooser.showOpenDialog( _gooey ); 
		
		if( returnVal == JFileChooser.APPROVE_OPTION ) { 
		    readSections( fileChooser.getSelectedFile() );
		    
		} // if
		else{
		    throw new BadActionException();
		    //_operator.actionFailed();
		}

        }
	
	else if( datasrc == 2 ){
        // copy the selected section, incrementing its section number 
	    // by one.
	    copySelected();
        }

	else{
    	    //the constructor has been passed a bad value. throw an exception.
	}
    }

    /**
     * this constructor builds an Add Section Action from a specified file
     *
     * @param ourGui copy of the GUI
     * @param db copy of the CRSSDatabase
     * @param file the file to read from
     */
    public AddSectAction( GUI ourGui, CRSSDatabase db, File file ){

        try{
	    _gooey = ourGui;
	    _dbase = db;
	    readSections( file );
	} // try
	
	catch( Exception e ){
	    System.out.println("Error Reading Sections");
	}
	
    } // AddsectAction
    
    /**
     *  method to add the list
     *
     */
    public void go() {
	
	/**
	 * This routine traverses the list of additions, which was loaded  
	 * by one of the helper routines.  Each item in the list will get 
	 * added to the database.
	 */
        Section element = null;

        //first make sure we have a database to work with
        if (_dbase != null){
            ListIterator iter = _additions.listIterator( 0 );
	    
            while( iter.hasNext() ) {
                element = ( (Section) iter.next() );
                _dbase.addSect( element );
            }
            
        } // do we even have a database?
	
        //update the GUI with the section information 
        refreshSectList();
	
    }//go()
    
    /**
     * this method is called from the window that just collected all the
     * data.  The window passes it a section that it just created via the
     * information the user passed to it
     *
     * @param new_section the section to be added
     */
     public void sendSection( Section new_section ){
        
	//add the section to the database and call go to continue
        if( new_section != null ){
            _additions.add(new_section);
            go();
        }
        else{
            _operator.actionFailed();
        }

    }//sendSection

    /**
     * This course allows the user to check to see if the
     * new section is addable without giving the window access
     * to the database
     *
     * @param   newSection is the new room in question
     *
     * @return  is the asnwer to the 'isAddable' question
     */
    public boolean isAddable( Section newSection ) {
	return _dbase.addable( newSection );
    }//isAddable
    
    /**
     * will undo add section function
     *
     */
    public void undo(){

        Section element;

        if (_dbase != null){
            ListIterator iter = _additions.listIterator( 0 );

            while( iter.hasNext() ) {
                element = ( (Section) iter.next() );
                _dbase.removeSection( element );
            }
        } // if we even have a database?
	
        //update the GUI with the section information 
        refreshSectList();

    }//undo
    
    /**
     * Looks through the file and pick outs all the data fields needed for
     * creating a new section.  Then it creates the new section  
     * NOTE: this does not check for bad input
     *
     * @param file the object "File" that the chooser gives back
     * @exception IOException if an error occurs
     * @exception BadActionException if an error occurs
     */
    public void readSections( File file ) 
        throws IOException, BadActionException {

	Section tempSection = null;
	String sectionNum, sectionDesc;
	Integer sectionEnrol, startTime, endTime;
	String[] attributes = new String[7];
	String[] days = new String[5];
	AddListError error;
	
	//
	// Set the initial values of the attributes and days
	//

	try {
	    //
	    // Initiate the buffer reader, and the stream  tokenizer
	    //
	    BufferedReader in = new BufferedReader( new FileReader( file ) );
	    
	    //
	    // Read the line, that is the header for the file
	    // that looks like <ROOMS>
	    //
	    StreamTokenizer st = new StreamTokenizer( in );
	    
	    //
	    // Set the character the will start and end the token fields
	    //
	    st.quoteChar( ';' );
	    st.nextToken();
	    
	    //
	    // Start reading..
	    //
	    while( !st.sval.equals( "<SECTIONS>" ) ){
		st.nextToken();
	    }

	    //
	    // While we haven't reached the last token read
	    // in all the fields and make a section outta every line	
	    // to the "<END>" token
	    //
	    st.nextToken();

	    while( !st.sval.equals( "<END>" ) ){

            // re-initialize the attributes, days to be false.
	        for( int i = 0; i < 7 ; i++ ) {
	            attributes[i] = "0";
	        }

	        for (int j = 0; j < 5 ; j++){
	            days[j] = "0";
	        }
	    
		//grab the section number 
		sectionNum = st.sval;

		//skip to the next piece of data
		st.nextToken();

		//grab the description
		sectionDesc = st.sval;

		//skip to the next data
		st.nextToken();
		
		//grab the days represented by 1's and 0's.  1 menas meet 
		//that day and zero meand don't meet that day.  The digits are
		//aranged in assending order from Monday through Friday in 
		//the file
		for( int i = 0; i < 5 ; i++ ) {
		    days[i] = (st.sval.substring( i, ( i + 1 ) ));
		}

		//skip to the next data
		st.nextToken();

		//grab the enrollment
		sectionEnrol = new Integer( st.sval );

		//skip to the next data
		st.nextToken();

		//grab the start time, NOTE: at this time, the start and end 
		//time are represented in military in the file, NOT as array 
		//indecies.  Its up to the section to convert if needed
		startTime = new Integer( st.sval);
		
		//skip to the next data
		st.nextToken();

		//grab the end time
		endTime = new Integer( st.sval );

		//skip to the next data
		st.nextToken();


		//start a loop that will gather up all the attributes and 
		//creat an array out of them.
		for( int i = 0; i < 7 ; i++ ) {
		    attributes[i] = (st.sval.substring( i, ( i + 1 ) )); 
		}

		//skip to the next data, this will prep for the next iteration
		//through the data
		st.nextToken();

		//create the section given the dat ajust read in.  Assuming 
		//all day is valid, there shouldn't be any problems
		tempSection = new Section(sectionDesc, 
					  sectionNum, 
					  attributes,
					  sectionEnrol, 
					  startTime, 
					  endTime, 
					  days );

		//check to see if the newly created section will fit, meaning 
		//it is a valid setion that does not exist already

		if( _dbase.addable( tempSection ) ) {

		    //add it to the database
		    _additions.add( tempSection );
		}

		else {

		    //if the section already existes, this will execute and 
		    //the section will not be added
		    unAddableSections.add( tempSection.getSectNum() );
		    //print out some debug info for develepemt time
		} // if

	    } // While
	}

	catch ( FileNotFoundException fnfe ) {
	    _gooey.defaultErrorMsgWindow( "File not found." );

        throw new BadActionException();
	} // try/catch
	
	if( unAddableSections.size() > 0 ) {
	    error = new AddListError( "sections", unAddableSections );
	}

	//call go to continue
	go();

    } // readRooms
    
    /**
     * copies the section which is highlighted in the gui.
     *
     * @exception BadActionException if you try to duplicate past two digits
     */
    private void copySelected()  throws BadActionException{
        String sectNum = _gooey.getHighlightedSection();
        Section oldSect = null;
        Section newSect = null;
        String oldNum;
        String newNum;
        String sectionNum;
        Integer sectionInt;
        boolean cont = true;
        
        if( sectNum != null ){
            oldSect = _dbase.getSection( sectNum );
            oldNum = oldSect.getSectNum();
            // now get the section number, the last two digits, and
            // increment it by one.
            
            sectionNum = oldNum.substring( 9 );
            sectionInt = new Integer( sectionNum );
            
            // this loop will actually perform the increment, and check
            // if a section with that number is addable.  it will add the
            // section with the smallest number possible.
            while( cont ){
 
		// now, increment it one.
                int increment = sectionInt.intValue() + 1;
                if( increment == 100 ){
                    _gooey.defaultErrorMsgWindow("Cannot duplicate beyond" +
						 "\na section ending in 99.");
                    throw new BadActionException();
                }

                sectionInt = new Integer( increment );

                newNum = oldNum.substring( 0, 9 );
                
                //if the section number ends in a number less than ten, 
                //pad it with a zero.  all section numbers are 11 digits long
                if( sectionInt.compareTo( new Integer( 10 ) ) == -1 ){
                    newNum = newNum + "0";
                }
                
                newNum = newNum + sectionInt.toString();
                newSect = new Section( oldSect );
                newSect.setSectNum( newNum );
                if( _dbase.addable( newSect ) ){
                    cont = false;
                }
            } // while
            
            // don't copy the oldSect's (possible) assignment, please.
            newSect.unassign();
            _additions.add( newSect );
            
            //ok, the list is prepared... call go.
            go();
        }
    } // copySelected

} // Add Section action class
