/**
 * Class that saves the program's state
 *
 * @version    $Id: LoadStateAction.java,v 1.12 2000/05/08 16:36:44 p361-45a Exp $
 *
 * @author     Christopher Bunk
 * @author     Adam Chipalowsky
 * @author     Dan Cooley
 * @author     Eric Fergesun
 * @author     Phil Light
 *  
 * Revisions:
 *		$Log: LoadStateAction.java,v $
 *		Revision 1.12  2000/05/08 16:36:44  p361-45a
 *		fixed formatting
 *
 *		Revision 1.11  2000/05/08 02:16:20  aec1324
 *		fixed a null pointer when load was assigning using
 *		a null operator
 *
 *		Revision 1.10  2000/05/06 17:42:08  etf2954
 *		Made it clear the stack when it was done
 *
 *		Revision 1.9  2000/05/06 14:50:15  pel2367
 *		tried debugging in here, but the problem seems to be
 *		in save or genfile
 *
 *		Revision 1.8  2000/05/04 21:28:59  etf2954
 *		fixed the new state call
 *
 *		Revision 1.7  2000/05/04 20:25:33  etf2954
 *		Make a call to have a new state before the file is loaded.
 *
 *		Revision 1.6  2000/05/03 02:30:11  cmb3548
 *		Now load state if fully working
 *
 *		Revision 1.5  2000/05/02 20:53:26  etf2954
 *		Added a refreshSectList() call at the end of the load
 *		this is because we need to see the assignments that were
 *		made after the sections were added.
 *
 *		Revision 1.4  2000/05/02 20:09:39  etf2954
 *		Debugged this and found error and fixed it
 *		_dbase.getRoom(sectAssignment) and
 *		_dbase.getSection(roomAssignments)
 *		Does that look right??
 *
 *		Revision 1.3  2000/05/02 17:46:02  cmb3548
 *		fully implemented
 *
 *		Revision 1.2  2000/05/02 03:05:24  cmb3548
 *		More implementation added
 *
 *		Revision 1.1  2000/05/02 00:57:05  cmb3548
 *		Initial revision
 *
 */

import java.util.*;
import java.io.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.beans.*; //Property change stuff

public class LoadStateAction extends CRSSAction{

    boolean _newState;
	public Operator _operator;

    /**
     * constructor for Load State object
     *
     * @param gui a value of type 'GUI'
     * @param db a value of type 'CRSSDatabase'
     * @param newState a value of type 'boolean'
     * @param oPerator a value of type 'Operator'
     */
    public LoadStateAction( GUI gui, CRSSDatabase db, boolean newState, 
			    Operator oPerator){
        
	_gooey = gui;
	_dbase = db;
	_newState = newState;
	_operator = oPerator;
	go();
	
    } // constructor
	
    /**
     * method that performs the neccessary work to load the state file
     *
     */
    public void go(){
      
	try{
	    if ( !_newState ){
		final JOptionPane optionPane = new JOptionPane(
	    	        "Loading a file will destroy any changes made since"+
			" last save, Proceed?"
	    		,JOptionPane.QUESTION_MESSAGE,
	    		JOptionPane.YES_NO_OPTION);

	    	final JDialog dialog = new JDialog(_gooey, "Confirm", true);
	    	dialog.setContentPane(optionPane);
	    	optionPane.addPropertyChangeListener( 
	    	    new PropertyChangeListener(){
		    public void propertyChange(PropertyChangeEvent e) {
			String prop = e.getPropertyName();
			if (dialog.isVisible() 
			    &&(e.getSource() == optionPane)
			    &&(prop.equals(JOptionPane.VALUE_PROPERTY)||
			       prop.equals(JOptionPane.INPUT_VALUE_PROPERTY))){
			    // If you were going to check something
	    		    // before closing the window, you'd do
	    		    // it here. this case we should change this so
			    // when the user just closes the window, its the
			    // same as cancel

			    dialog.setVisible(false);

			} // if statement
			
		    } // property change

	    	}); // property change listener

	        //set up and make the window visable
	    	dialog.pack();
	    	dialog.setLocationRelativeTo(_gooey);
	    	dialog.setVisible(true);
                int value = ((Integer)optionPane.getValue()).intValue();
	    	
		if (value == JOptionPane.YES_OPTION) {
	            //NewStateAction nsa = new NewStateAction(_gooey, _dbase);
		    getFile();
        	} // if
	
	    } // stack isn't empty
	    
	    else{
			getFile();
	    } // stack is empty
	} // try

	catch( Throwable e ) {
	}
    } // go
    
    /* 
     * select the file to load from
     *
     */
    public void getFile(){
	
        File file;
	String[] extensions = { "crss" };
	
	//Pop up load file window
        ExtensionFileFilter filter =
	    new ExtensionFileFilter( extensions, "CRSS Save files (*.crss)" );
        JFileChooser fileChooser = new JFileChooser();
	fileChooser.addChoosableFileFilter( filter );
	fileChooser.setFileFilter( filter );
	int returnVal = fileChooser.showOpenDialog( _gooey );
	
	//If the user selects the approve button
	if( returnVal == JFileChooser.APPROVE_OPTION ) {
            file = fileChooser.getSelectedFile();
	    read( file );
	} // if approve is selected
    } // get file
    
    /**
     * Reads in the information from the given file
     *
     * @param file file to be read from
     */
    public void read(File file){
        
	String roomAssignment;
	String sectAssignment;
	int i = 0;
	
	NewStateAction nsa = new NewStateAction( _gooey, _dbase, true, null );
		
	// with the given file read in the sections and rooms
	AddSectAction asa = new AddSectAction( _gooey, _dbase, file );
	
	AddRoomAction ara = new AddRoomAction( _gooey, _dbase, file );
	
	
        // now read in the assignments
	try{
	    BufferedReader in = new BufferedReader( new FileReader( file ) );
	    StreamTokenizer st = new StreamTokenizer( in );
	    
	    //
	    // Set the character the will start and end the token fields
	    //
	    st.quoteChar( ';' );
	    st.nextToken();
	    
	    //
	    // Start reading..
	    //
	    while( !(st.sval.equals( "<ASSIGNMENTS>" )) ){
		st.nextToken();
	    } // while
	    
	    //
	    // While we haven't reached the last token read
	    // in all 4 fields and make a room outta every line
	    // found in the file, but stop when you get
	    // to the "<END>" token
	    //
	    st.nextToken();
	    
	    while( !st.sval.equals( "<END>" ) ){
		sectAssignment = new String(st.sval);
		st.nextToken();
		roomAssignment = new String(st.sval);
		st.nextToken();
		AssignAction aa = new AssignAction( _gooey,
                                                    _dbase,
                                                    sectAssignment,
                                                    roomAssignment);
		aa.go();
	    } // while
	    refreshSectList();
        } // try
	catch( Throwable e ){
	    System.out.println("in the catch");
	}
	_operator.clearUndoStack();
    } // read
    
} // Load State Action
