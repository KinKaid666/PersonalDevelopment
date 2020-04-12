/**
 * @version	$Id: Operator.java,v 2.5 2000/05/10 14:30:47 pel2367 Exp $
 *
 * @author	Adam E Chipalowsky 
 * @Contrib     Eric Ferguson
 *
 * Revisions:
 *		$Log: Operator.java,v $
 *		Revision 2.5  2000/05/10 14:30:47  pel2367
 *		caught badActionException in AddRoom
 *
 *		Revision 2.4  2000/05/10 00:04:04  aec1324
 *		wored on the action fail a little
 *
 *		Revision 2.3  2000/05/08 23:19:07  aec1324
 *		added the correct hyperlink for the online help
 *
 *		Revision 2.2  2000/05/08 16:59:46  p361-45a
 *		fixed formatting
 *		,
 *
 *		Revision 2.1  2000/05/06 17:42:08  etf2954
 *		passed itself to the load class so that the load
 *		class can clear that stack when it's done
 *
 *		Revision 2.0  2000/05/06 15:02:49  etf2954
 *		WE'RE STARTING 2.0
 *
 *		Revision 1.59  2000/05/06 15:01:07  etf2954
 *		Online Help will now open up a netscape window
 *
 *		Revision 1.58  2000/05/06 14:50:15  pel2367
 *		caught another badActionExcetption
 *
 *		Revision 1.57  2000/05/05 17:52:56  pel2367
 *		caught another exception, i think this one is with
 *		add from file.
 *
 *		Revision 1.56  2000/05/05 14:50:52  pel2367
 *		actionFailed() works.
 *
 *		Revision 1.55  2000/05/05 02:23:55  pel2367
 *		caught a few more exceptions, working on
 *		not adding the cancelled mod/add room/section
 *		to the _actionStack
 *
 *		Revision 1.54  2000/05/05 00:49:37  aec1324
 *		a refrence to this is given to exit so it can call
 *		back and ask to save
 *
 *		Revision 1.53  2000/05/05 00:48:27  pel2367
 *		handling some more exceptions
 *
 *		Revision 1.52  2000/05/05 00:08:16  aec1324
 *		made a minor change to the exit action call
 *
 *		Revision 1.51  2000/05/05 00:06:16  aec1324
 *		changes this so it creates the exit action to exit the
 *              program
 *
 *		Revision 1.50  2000/05/04 17:20:24  aec1324
 *		added more comments so the code is readable
 *
 *		Revision 1.49  2000/05/04 01:10:28  aec1324
 *		added the ModRoomAction to be puched onto the stack
 *
 *		Revision 1.48  2000/05/03 16:33:51  p361-45a
 *		allowed about to be called
 *
 *		Revision 1.47  2000/05/03 14:40:20  cmb3548
 *		wrote new function clearUndoStack and modified NewState 
 *              function to pass a copy of the operator to the class
 *
 *		Revision 1.46  2000/05/03 05:19:44  pel2367
 *		added the roomOccClicked() function and
 *		implemented it.
 *
 *		Revision 1.45  2000/05/03 03:19:26  cmb3548
 *		New State Action Implemented
 *
 *		Revision 1.44  2000/05/03 01:50:02  pel2367
 *		changed the call to sort to not pass
 *		extra data anymore
 *
 *		Revision 1.43  2000/05/03 00:16:31  p361-45a
 *		tried to code, got stuck on syntax
 *
 *		Revision 1.42  2000/05/02 03:05:24  cmb3548
 *		Operator now prooperley creates a LoadStateAction
 *
 *		Revision 1.41  2000/05/01 17:45:18  p361-45a
 *		added constructor call for about (but commented out)
 *
 *		Revision 1.40  2000/05/01 14:46:27  pel2367
 *		un-commented out the call to assignall,
 *		passed in false instead of true
 *
 *		Revision 1.39  2000/04/30 21:41:59  cmb3548
 *		LoadState warning box pops up when stack isn't empty
 *
 *		Revision 1.38  2000/04/30 20:25:18  pel2367
 *		recognized addition of AssignAllAction
 *
 *		Revision 1.37  2000/04/29 21:09:32  aec1324
 *		added the creation of a MoodRoomAction
 *
 *		Revision 1.36  2000/04/29 03:39:02  pel2367
 *		un-commented the saveState creation, and
 *		tinkered with the undo, redo a little
 *
 *		Revision 1.35  2000/04/28 20:39:59  pel2367
 *		made undo and redo unhighlight the tables.
 *		(it's not satisfactory UI, i'll rework it)
 *
 *		Revision 1.34  2000/04/28 19:11:56  pel2367
 *		improved the list management for undo(),
 *		redo()
 *
 *		Revision 1.33  2000/04/28 18:30:51  cmb3548
 *		Constructor for SaveState now passes gui and database
 *
 *		Revision 1.32  2000/04/28 17:05:39  pel2367
 *		wrote do(), redo(), changed where we handle
 *		some exceptions.
 *
 *		Revision 1.31  2000/04/28 01:24:57  pel2367
 *		put unassign() back in.
 *
 *		Revision 1.30  2000/04/27 17:03:31  cmb3548
 *		Different export type functionality added
 *
 *		Revision 1.29  2000/04/27 02:01:08  aec1324
 *		added a little bit of implementation for editing
 *		a section
 *
 *		Revision 1.28  2000/04/26 20:22:26  aec1324
 *		started looking at possable revisions for the
 *		edit section call
 *
 *		Revision 1.27  2000/04/26 15:59:44  etf2954
 *		Added the "remSect" call
 *
 *		Revision 1.26  2000/04/26 15:37:41  cmb3548
 *		constructor of GenFileAction changed
 *
 *		Revision 1.24  2000/04/26 06:11:34  cmb3548
 *		Completed partial implementation of GenFileAction
 *
 *		Revision 1.23  2000/04/25 22:00:45  pel2367
 *		added Unassign()
 *
 *		Revision 1.22  2000/04/24 17:20:18  aec1324
 *		fixed the problem with this not being able to
 *		read in sections from files.  Now it has a
 *		try/catch block around the call to the add
 *		section action
 *
 *		Revision 1.21  2000/04/24 04:09:40  etf2954
 *		WEll you see, I was in the one functin, then..
 *		umm.../me is away. Didn't change anything
 *
 *		Revision 1.20  2000/04/24 02:13:38  p361-45a
 *		More javadoc comments added
 *
 *		Revision 1.19  2000/04/24 01:19:47  pel2367
 *		caught BadActionException in assign()
 *
 *		Revision 1.18  2000/04/23 22:50:03  pel2367
 *		wrote assign().
 *
 *		Revision 1.17  2000/04/22 20:15:11  pel2367
 *		i think i fixed a little typo. at any rate,
 *		it compiles.
 *
 *		Revision 1.16  2000/04/22 16:23:00  aec1324
 *		added a hilitemethod
 *
 *		Revision 1.15  2000/04/21 16:31:35  etf2954
 *		Added the functinality for reading from a file
 *
 *		Revision 1.14  2000/04/20 21:39:17  etf2954
 *		Added the AddRoom functionality
 *
 *		Revision 1.13  2000/04/20 02:27:38  aec1324
 *		added a refrence to the GUI and database
 *
 *		Revision 1.12  2000/04/19 14:28:38  etf2954
 *		Added AddSect funtionality
 *
 *		Revision 1.11  2000/04/19 02:25:52  etf2954
 *		didn't do anything
 *
 *		Revision 1.10  2000/04/18 21:42:05  etf2954
 *		added the new Room fuctnioality
 *
 *		Revision 1.9  2000/04/18 03:33:07  aec1324
 *		got rid of old interface methods
 *
 *		Revision 1.8  2000/04/18 03:25:34  aec1324
 *		added headers to the new methods
 *
 *		Revision 1.7  2000/04/15 21:15:59  aec1324
 *		added more methods for popups
 *
 *		Revision 1.6  2000/04/14 17:46:20  etf2954
 *		Started the code skeletons
 *
 *		Revision 1.5  2000/04/13 02:22:57  aec1324
 *		added another call to popup the add section
 *		window
 *
 *		Revision 1.4  2000/04/13 01:15:13  aec1324
 *		played around with the gui interfaces a little
 *
 *		Revision 1.3  2000/04/09 20:31:38  aec1324
 *		FOR TESTING ONLY: added the ability for the
 *		operator to know about the gui
 *
 *		Revision 1.2  2000/04/07 13:25:12  etf2954
 *		Added the Operator class
 *
 *		Revision 1.1  2000/04/07 13:10:24  etf2954
 *		Initial revision
 *
 *		Revision 1.3  2000/04/06 07:26:00  etf2954
 *		Fixed bug in Add Room Item
 *
 *		Revision 1.2  2000/04/06 07:00:54  etf2954
 *		Getting the GUIinterface to work
 *
 *		Revision 1.1  2000/04/06 05:42:23  etf2954
 *		Initial revision
 *
 *
 */

import java.util.*;
import java.io.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.beans.*; //Property change stuff

public class Operator {
    
    private GUI _theGUI;
    private CRSSDatabase _database;
    private LinkedList _actionStack = new LinkedList();
    private Stack _undone = new Stack();
    
    /**
     * Constructor for operator
     *
     */
    public Operator(){
	
    }
    
    /**
     * Sets the GUI operator will be interacting with
     *
     * @param myElder GUI that operator uses
     */
    public void setMyGUI( GUI myElder ){

	    _theGUI = myElder;
    }

    /**
     * Sets the Database operator will be interacting with
     *
     * @param database Database that operator uses
     */
    public void setMyDataBase( CRSSDatabase database ){

	_database = database;
    }
    
    /**
     * Clears the database and clears the GUI
     *
     */
    public void newState() {
        boolean newState;
	
	if (_actionStack.size() > 0 ){
	    newState = false;
	} // if
	
	else{
	    newState = true;
	}
	
	NewStateAction lsa = new NewStateAction( _theGUI,
	                                         _database,
						 newState,
						 this );
    }  // new state

    /**
     * Desc:	Loading a previous state from a file if the current state has 
     *          been modified (ie something on the stack) the user will be 
     *          prompted for what to do That is weather to save the work or
     *          or discard it
     *
     */
    public void loadState() {
        boolean newState;

	if (_actionStack.size() > 0 ){
	    newState = false;
	} // if
	
	else{
	    newState = true;
	}
	
	LoadStateAction lsa = 
	    new LoadStateAction( _theGUI,_database,newState, this );
    }  // load state
  
    /**
     * Save the current state of the program
     *
     */
    public void saveState() {
    
	SaveStateAction sta = new SaveStateAction( _theGUI , _database, this);
    }//saveState
    
    /**
     * Exites the program.  If the current state of the program
     * has been modified, the user will be prompted.
     *
     */
    public void exit() {
       
	boolean beenChanges = true;
	//check the stack
	
	if (_actionStack.size() == 0 ){
	    beenChanges = false;
	    
	}
	
	//creat the exit action, this may be the end of the line
	ExitAction maybeExitThisProgram = 
	    new ExitAction( _theGUI, beenChanges, this );


    }//exit
    
    /**
     * Addes a section to the database The section will  checked so 
     * that no dublicatesections will be in the databse
     *
     * @param source a value of type 'int' telling the program where the 
     *               input is coming from, i.e. File, User Input, etc.
     */
    public void addSection( int source ) {
        
	try{
	    AddSectAction addSectAction = 
		new AddSectAction( _theGUI, _database, source, this );
	    push( addSectAction );
        }
	
        catch( IOException io ) {
        }
	
        catch( BadActionException bad ) {
        }

    }//addSection
    
    /**
     * creates a highlight action
     *
     * @param nil a value of type '_theGUI'
     * @param nil a value of type 'dataBase'
     */
    public void hiLightSection(){
	
	HiLiteSectAction highlight = 
	    new HiLiteSectAction( _theGUI, _database );
	
    }//hiLightSection
    
    public void roomOccClicked( String sectNum ){

        HiLiteSectAction highlight = 
            new HiLiteSectAction( _theGUI, _database, sectNum );
    }//roomOccClicked
 
    /**
     * creates a highlight action
     *
     * @param nil a value of type '_theGUI'
     * @param nil a value of type 'dataBase'
     */
    public void hiLightRoom(){
	
	HiLiteRoomAction highlight =new HiLiteRoomAction( _theGUI, _database );

    }//hiLiteRoom

    /**
     * Adds a room to the database making sure that the room # does not 
     * conflict with with any other room #
     *
     * @param source 'int' telling ithe program where the input is coming from,
     *               i.e. FIle, User Input,etc.
     */
    public void addRoom( int source ) {
   
	try{
            AddRoomAction addRoomAction = 
		new AddRoomAction( _theGUI, _database, source, this );
            push( addRoomAction );
        }
	
        catch( IOException io ){
        }
        catch( BadActionException bad ){
        }

    }//addRoom
    
    /**
     * This will remove a section from the database
     *
     */
    public void removeSection() {
	
	RemSectAction remSection = 
	    new RemSectAction( _theGUI, _database );
	push( remSection );
    }
    /**
     * This will remove a Room from the database,this will prompt
     * the user if they want to do this if the Room has sections assigned to it
     *
     */
    public void	removeRoom() {

	try{
	    RemRoomAction remRoom = 
		new RemRoomAction( _theGUI, _database );
	    push( remRoom );
        }
	
        catch( BadActionException bad ){
            
        }
    }
    
    /**
     * Changes something in the SEction that is highlighted
     *
     */
    public void modifySection() {
     
	try{
	    ModSectAction modifySectAction =  
	        new ModSectAction( _theGUI, _database, this );
	    push( modifySectAction );
	}
	
        catch( BadActionException bad ){
        }    
    }
    
    /**
     * Modifies something in the room that is highlighted
     *
     */
    public void modifyRoom() {

        try{
            ModRoomAction modRoomAction = 
                new ModRoomAction( _theGUI, _database, this );
            push( modRoomAction );
        }

        catch( BadActionException bad ){
        }
        
    }//modifyRoom

    /**
     * Assigns the section that is highlighted to the room that is highlighted
     *
     */
    public void assign() { 

    	try {
	    AssignAction ass = 
		new AssignAction( _theGUI, _database );
            push( ass );
        }

        catch( BadActionException bad ){
        
        }
    }//assign
    
    /**
     * Unassigns the sectino from the room
     *
     */
    public void unassign() {
    	
	try {
            UnassignAction un_ass = 
		new UnassignAction( _theGUI, _database );
            push( un_ass );
        }
	
        catch( BadActionException bad ){
        
        }
    }//unassign
    
    /**
     * Generates an ASCII text file for the user
     *
     * @param type 'int'what type of file he/she wants I.E. Room Schedule,
     *              Section by Room Schedule
     */
    public void genFile( int type ) {
        
	try{
            GenFileAction gfa = new GenFileAction( new Integer(type), 
	                                               _theGUI, _database );
	}

	catch (Exception bad){
            System.out.println( bad );
        }
    }//genFile
    
    /**
     * Undoes the last state change, not a sort or a click
     *
     */
    public void undo() {

        // fetch the last action off of the _actionStack, push it
        // ondo _undone - this allows a redo() later.
        if( _actionStack.size() > 0 ) {
            UndoableAction lastPerformed;
            lastPerformed =  ( UndoableAction ) _actionStack.removeFirst();
            lastPerformed.undo();
            _undone.push( lastPerformed );
	}

        else{
            Toolkit.getDefaultToolkit().beep();
        }
    } // undo
    
    public void redo() {
        
	// get the action which was most recently placed onto the
        // stack of actions which were undone. tell it to go().
	if ( !_undone.empty() ) {
            CRSSAction temp_action;
            temp_action = ( CRSSAction ) _undone.pop();
            temp_action.go();
            _actionStack.addFirst( temp_action );
        }
	
        else{
            Toolkit.getDefaultToolkit().beep();
        }
    }//redo

    /**
     * creates a sort section action
     *
     */
    public void sortSections() {
     
	SortAction snort = new SortAction( _theGUI, _database, 1 );
    }//sortSections
    
    /**
     * creates a sort room action
     *
     */
    public void sortRooms( ) {
        
	SortAction snort = new SortAction( _theGUI, _database, 2 );
    }//sortRooms
    
    /**
     * creates a show all rooms action
     *
     */
    public void showAllRooms() {
        
	SortAction snort = new SortAction( _theGUI, _database, 0 );
    }//showAllRooms

    /**
     * creates an assign all action
     *
     */
    public void assignAll(){
        
	try{
            AssignAllAction all_ass = new AssignAllAction( _theGUI, 
                                                           _database,
                                                           false );
            push( all_ass );
        }
	
        catch( BadActionException bad ){
        }
    }//assignAll

    /**
     * clears the undo stack
     * set up with when the file was saved.
     *
     */
    public void clearUndoStack(){
	
	int i = 0;
        _undone.clear();
        _actionStack.clear();
    
    }//clearUndoStack
    
    /**
     * opens the about window for general software information and authors
     *
     */
    public void about(){
	
	AboutAction about = new AboutAction( _theGUI );
	
    }//about
    
    /**
     * displays general information about how to go about using the software
     *
     */
    public void help(){
	String command = "netscape http://www.cs.rit.edu/~p361-45a/help.html";
	
	try {
	    Process result = Runtime.getRuntime().exec( command );
	}

	catch( Throwable e ) {
	    System.err.println( e.toString() );
	}
    }//help
    
    public void actionFailed(){
  
	_actionStack.removeFirst();
	
    }
    
    /**
     * pushes actions on a stack so we know a history of actions that
     * where created
     *
     * @param lastPerformed the action to add on the stack
     */
    private void push( CRSSAction lastPerformed ){
    	// handles the management of the _actionStack;
        // we use it as a stack, but it's a LinkedList so that we can
        // keep it from getting too big and bogging down a system.
        // this routine "pushes" the last action performed onto the "stack"
        // but shortens it if there are more than 30 entries.

        _actionStack.addFirst( lastPerformed );        
        if( _actionStack.size() > 30 ){
            _actionStack.removeLast();
        }
        
        // since an action was just created, clear the "redo" stack.
        _undone.clear();
        
    }//push

}//GUIevent

