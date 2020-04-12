/**
 * Class that exits the program
 *
 * @version    $Id: ExitAction.java,v 1.5 2000/05/05 01:12:13 aec1324 Exp $
 *
 * @author     Christopher Bunk
 * @author     Adam Chipalowsky
 * @author     Dan Cooley
 * @author     Eric Ferguson
 * @author     Phil Light
 *
 * Revisions:
 *     $Log: ExitAction.java,v $
 *     Revision 1.5  2000/05/05 01:12:13  aec1324
 *     fixed so it compiles
 *
 *     Revision 1.4  2000/05/05 00:06:16  aec1324
 *     implemented this
 *
 *     Revision 1.3  2000/04/18 02:39:45  p361-45a
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
import java.beans.*; //Property change stuff

public class ExitAction extends CRSSAction{

    private boolean haveChangesBeenMade;
    private Operator parent;
    
    /**
     * constructor that creates ExitAction object
     *
     */
    public ExitAction( GUI theGUI, boolean changes, 
		       Operator myParent){
	_gooey = theGUI;
	parent = myParent;
	
	haveChangesBeenMade = changes;

	go();

    }//ExitAction
    
    /**
     * This method sends an action to the database
     *
     */
    public void go(){

	//popup a conformation window if the user has made changes
	if ( haveChangesBeenMade ){
	    
	    _gooey.exitQuestionWindow(this);
	}
	else{
	    killProgram();
	}//else

    }//go

    /**
     * brings up the save box
     *
     */
    public void saveIt(){
       
	parent.saveState();
	killProgram();

    }//saveIt

    /**
     * kills the program
     *
     */
    public void killProgram(){
	System.out.println("Bye");
	//like phil's "kill -l -l" except only on this program
	System.exit( 0 );
    }//killProgram
} // Exit Action class

