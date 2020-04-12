/**
 * Class that perfoms the New State Action
 *
 * @version    $Id: NewStateAction.java,v 1.10 2000/05/08 16:54:01 p361-45a Exp $
 *
 * @author     Christopher Bunk
 * @author     Adam Chipalowsky
 * @author     Dan Cooley
 * @author     Eric Ferguson
 * @author     Phil Light
 *
 * Revisions:
 *     $Log: NewStateAction.java,v $
 *     Revision 1.10  2000/05/08 16:54:01  p361-45a
 *     fixed formatting
 *
 *     Revision 1.9  2000/05/08 02:16:20  aec1324
 *     added a clear call to the stack so redo is not able
 *     to be used after this action
 *
 *     Revision 1.8  2000/05/06 17:42:08  etf2954
 *     Made it clear the stack when it was done
 *
 *     Revision 1.7  2000/05/06 15:54:43  etf2954
 *     Cleared the undo stack also
 *
 *     Revision 1.6  2000/05/04 21:09:49  etf2954
 *     Took out system.out's
 *
 *     Revision 1.5  2000/05/04 21:07:41  etf2954
 *     Made the refresh calls in the correct order
 *
 *     Revision 1.4  2000/05/03 14:40:20  cmb3548
 *     cut and splice method refered to in operator removed
 *
 *     Revision 1.3  2000/04/18 12:53:58  p361-45a
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
import java.util.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.beans.*; //Property change stuff

public class NewStateAction extends CRSSAction{

    public boolean _newState = false;
	public Operator _operator;
	private int i = 0;
    
    /**
     * constructor to create an object of type NewStateAction
     *
     * @param gui a value of type 'GUI'
     * @param db a value of type 'CRSSDatabase'
     * @param newState a value of type 'boolean'
     * @param op a value of type 'Operator'
     */
    public NewStateAction(GUI gui, CRSSDatabase db, 
	                      boolean newState, Operator op){
        _gooey = gui;
        _dbase = db;
        _newState = newState;
		_operator = op;
        go();
    } // new state constructor
    
    /**
     * method to clear all lists from Gui and database
     *
     */
    public void go(){
	int i = 0;
    	
	// pop up warning box if database has been changed
	//
	try{
	    if ( !_newState ){
		final JOptionPane optionPane = new JOptionPane(
	    	    "This action will destroy any changes made to database"+
				        " since last save, Proceed?"
		    
		    ,JOptionPane.QUESTION_MESSAGE,
		    JOptionPane.YES_NO_OPTION);

		final JDialog dialog = new JDialog(_gooey, "Confirm", true);
		dialog.setContentPane(optionPane);
		optionPane.addPropertyChangeListener(new PropertyChangeListener(){
		    public void propertyChange(PropertyChangeEvent e) {
			String prop = e.getPropertyName();
			
			if (dialog.isVisible() 
			    &&(e.getSource() == optionPane)
			    &&(prop.equals(JOptionPane.VALUE_PROPERTY)||
			       prop.equals(JOptionPane.INPUT_VALUE_PROPERTY))){
			    //If you were going to check something
			    //before closing the window, you'd do
			    //it here.
			    //this this case we should change this so when the
			    //user just closes the window
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
		    _dbase.clearAllLists();
		    _operator.clearUndoStack();
		    refreshSectList();
		    refreshRoomList();
		    refreshSectionAttrib();
		    refreshRoomAttrib();
		    refreshRoomOcc();
        		
		} // if
	    } // stack isn't empty
	   
	    else{
		_dbase.clearAllLists();
		
		//need to check for null here cause in case
		//this class was called using save state
		//the _operator will be null
		if( _operator != null ){
		    _operator.clearUndoStack();
		}
		
		refreshSectList();
		refreshRoomList();
		refreshSectionAttrib();
		refreshRoomAttrib();
                refreshRoomOcc();
	    } // stack is empty
	} // try

	catch( Exception e ){ 
	    System.out.println("Error using confirmation"+e);
	}//catch
    }//go
    
} // NewState Action class
