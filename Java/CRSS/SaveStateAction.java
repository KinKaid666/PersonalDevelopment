/**
 * Class that saves the program's state
 *
 * @version    $Id: SaveStateAction.java,v 1.9 2000/05/06 14:50:15 pel2367 Exp $
 *
 * @author     Christopher Bunk
 * @author     Adam Chipalowsky
 * @author     Dan Cooley
 * @author     Eric Fergesun
 * @author     Phil Light
 *
 * Revisions:
 *     $Log: SaveStateAction.java,v $
 *     Revision 1.9  2000/05/06 14:50:15  pel2367
 *     added some debugging... and basically shook my
 *     head in confusion, there's a mysterious
 *     military time problem in here.
 *
 *     Revision 1.8  2000/05/06 01:15:39  pel2367
 *     don't know why this is checking in, i didn't
 *     do anything except maybe code clean or some such
 *
 *     Revision 1.7  2000/05/02 20:42:43  etf2954
 *     Added the functionality so that the save will check to
 *     see if the file has a .crss appended to it, and if it doesn't,
 *     it will add it
 *
 *     Revision 1.6  2000/04/30 20:04:12  cmb3548
 *     Assignments secitno now implemented
 *
 *     Revision 1.5  2000/04/29 03:38:59  cmb3548
 *     Full implementation done
 *
 *     Revision 1.4  2000/04/28 19:17:56  cmb3548
 *     Save file as window implemented
 *
 *     Revision 1.3  2000/04/18 13:12:05  p361-45a
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

public class SaveStateAction extends CRSSAction{

    Operator _operator;

    /**
     * constructor to create an object of type SaveStateAction
     *
     */
    public SaveStateAction(GUI gui, 
                           CRSSDatabase db,
                           Operator oPerator ){
        
	    _gooey = gui;
	    _dbase = db;
        _operator = oPerator;
        go();
    }
    
    /**
     * method to save the current state
     *
     */
    public void go(){
    
		String fileName;
		String extension = ".crss";
		File temp_file;
        File file;
	
	    //Pop up save file as window
        JFileChooser fileChooser = new JFileChooser();
	    int returnVal = fileChooser.showSaveDialog( _gooey );
	
	    //If the user selects the approve button
	    if( returnVal == JFileChooser.APPROVE_OPTION ) {

	        //check to see if the file exists
	        //if it does pop up an error box
	        //otherwise create the file
	        //call routines to write desired file
                file = fileChooser.getSelectedFile();
			fileName = file.getPath();

			//
			// Check to see if the file ends with .crss
			// 	and if it doesn't, append that to it
			if( !(fileName.endsWith( extension ) ) ) {
				fileName = fileName.concat( extension );
				temp_file = new File( fileName );
				file = temp_file;
			} // if
	        if ( file.exists() ){
		    // crete a yes no box to see if user wants to overwrite file
		    final JOptionPane optionPane = new JOptionPane(
		            "File specified currently exisits, Do you want to"+
			    " overwrite?", JOptionPane.QUESTION_MESSAGE,
		            JOptionPane.YES_NO_OPTION);

		    final JDialog dialog = new JDialog(_gooey, 
						       "Confirm",
						       true);
		    dialog.setContentPane(optionPane);

		    optionPane.addPropertyChangeListener( 
		            new PropertyChangeListener(){
			        public void propertyChange(PropertyChangeEvent e) {
			            String prop = e.getPropertyName();

			            if (dialog.isVisible() 
				    && (e.getSource() == optionPane)
				    && (prop.equals(JOptionPane.VALUE_PROPERTY) ||
				    prop.equals(JOptionPane.INPUT_VALUE_PROPERTY))) {
				    //If you were going to check something
				    //before closing the window, you'd do
				    //it here.

				    //this this case we should change this so when the
				    //user just closes the window, its the same as cancel
				    dialog.setVisible(false);
			            }
			        }
		            });

		        //set up and make the window visable
		        dialog.pack();
		        dialog.setLocationRelativeTo(_gooey);
		        dialog.setVisible(true);

		        int value = ((Integer)optionPane.getValue()).intValue();
		        if (value == JOptionPane.YES_OPTION) {
			    genSaveFile( file );
		        } // overwrite was selected			
	        }
	        else{ // file doesn't exist
                try{
		            file.createNewFile();
		            genSaveFile( file );
		        } //try
		        catch(Exception e){
		            System.out.println( "Error Creating File" + file );
		        } // catch
            } // file exist?
	    } // user chose to save
    } // go
    private void genSaveFile( File fileToSaveTo ){

	    int i;
	    Vector sectList;
            Vector roomList;
            BufferedWriter out;
	    Section tempSection;

	    try{
            // tell the operator to clear its stack of actions; a save
            // state means there's nothing to undo.
            _operator.clearUndoStack();


	        // open file for buffered input
	        out = new BufferedWriter(new FileWriter(fileToSaveTo));
	    
	        // get the list of sections and rooms to save
	        sectList = _dbase.getSectList();
	        roomList = _dbase.getRoomList();

	        // write header for Rooms Section of save file
                out.write(";<ROOMS>;");
	        out.newLine();
	        // print the info for every section into the 
	        for( i = 0; i < roomList.size(); i++ ){
	            out.write( ((Room)roomList.elementAt(i)).toSaveString() );
	            out.newLine();
	        }
	        // print the ending header
	        out.write(";<END>;");
	        out.newLine();
	        out.flush();


	        // header for Sections seciton of save file
	        out.write(";<SECTIONS>;");
	        out.newLine();
	        // print the info for every room into the file
	        for( i = 0; i < sectList.size(); i++ ){
	            out.write( ((Section)sectList.elementAt(i)).toSaveString() );
	            out.newLine();
	        }
	        out.write(";<END>;");
	        out.newLine();
	        out.flush();

	        // header for assignments section of save file
	        out.write(";<ASSIGNMENTS>;");
	        out.newLine();
                for( i = 0; i < sectList.size(); i++ ){
		    
		    // if the seciton does have an assignment
		    // then write it's section number and then the room num it's
		    // assigned to
		   
		    tempSection = (Section)sectList.elementAt(i);
		   
		    if(! (tempSection.getAssignment()).equals( "None" )){
		        out.write( ";" + 
			tempSection.getSectNum()
		        + ";    ;" + 
	                tempSection.getAssignment()
		        + ";" );
		        out.newLine();
			out.flush();
		    } // if
		} // for loop
		
	        out.write(";<END>;");
	        out.newLine();
	        out.flush();
	    } // try
	    catch( Exception e ){
            System.out.println( e + " Happened when writing save file" );
        } // catch
        
    } // genSaveFile
} // Save State Action
