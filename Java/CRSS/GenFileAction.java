/**
 * Class that generates a file
 *
 * @version    $Id: GenFileAction.java,v 1.16 2000/05/10 15:29:53 etf2954 Exp $
 *
 * @author     Christopher Bunk
 * @author     Adam Chipalowsky
 * @author     Dan Cooley
 * @author     Eric Ferguson
 * @author     Phil Light
 *
 * Revisions:
 *     $Log: GenFileAction.java,v $
 *     Revision 1.16  2000/05/10 15:29:53  etf2954
 *     Removed System.out's
 *
 *     Revision 1.15  2000/05/10 15:23:06  pel2367
 *     fixed the array out of bounds exception for generate
 *     sectionList
 *
 *     Revision 1.14  2000/05/09 05:35:57  pel2367
 *     finished... i could make one or two more changes,
 *     but i need to step away, just step away man.
 *
 *     Revision 1.13  2000/05/09 01:56:48  aec1324
 *     traced the error more
 *
 *     Revision 1.12  2000/05/08 23:19:07  aec1324
 *     am in the middle of tracing an I/O error
 *
 *     Revision 1.11  2000/05/08 22:17:10  pel2367
 *     finished assignment file generation, but can't test it
 *     well because i'm at home right now.
 *
 *     Revision 1.10  2000/05/07 04:21:12  pel2367
 *     12 hours of headaches
 *
 *     Revision 1.9  2000/05/06 21:32:24  pel2367
 *     initial writing of export list with
 *     assignments.
 *
 *     Revision 1.8  2000/05/06 14:50:15  pel2367
 *     probably added some debugging, somewhere along the road here
 *     the times get incremented to military incorrectly.
 *
 *     Revision 1.7  2000/05/05 19:42:22  cmb3548
 *     Generateing section and room lists implemented
 *     for save files
 *     /.
 *
 *     Revision 1.6  2000/04/28 17:27:07  cmb3548
 *     Fully functional Room Export Schedule
 *
 *     Revision 1.5  2000/04/26 15:37:41  cmb3548
 *     File can now be created
 *     Input can be added
 *
 *     Revision 1.4  2000/04/26 06:11:34  cmb3548
 *     Completed partial implementation of GenFileAction
 *
 *     Revision 1.3  2000/04/18 02:59:22  p361-45a
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
import java.beans.*;

public class GenFileAction extends CRSSAction{

    private Integer _typeToCreate;
    private Date timeStamp = new Date( System.currentTimeMillis() );
    
    /**
     * constructor to create an object of type GenFileAction
     *
     * @param type type of file to create(1=rooms schedule, 
     *                                    2=Section List w/ assignment
     *                                    3=section save)
     *                                    4=room save)
     *                                    
     */
    public GenFileAction(Integer type, GUI gui, CRSSDatabase db){
        // Make a java file window appear
	// create a file with the specified name
	// If room printoutRooms Schedule
	
	
	_gooey = gui;
	_dbase = db;
	_typeToCreate = type;
    
    // some checks to make sure the user has supplied some
    // data to output.
    
    int bunkInt = _typeToCreate.intValue();
    
    if( bunkInt == 1 &&
        _gooey.getHighlightedRoom() == null ){
        _gooey.defaultErrorMsgWindow( "Please select a room to " +
            "export." );
    }
    else if( ( bunkInt == 2 || bunkInt == 3 ) &&
             ( _dbase.getSectList()).size() < 1 ){
        _gooey.defaultErrorMsgWindow( "Cannot export without sections." );
    }
    else if( ( bunkInt == 2 || bunkInt == 4 ) &&
             ( _dbase.getRoomList()).size() < 1 ){
        _gooey.defaultErrorMsgWindow( "Cannot export without rooms." );
    }
	else{
        // no problems.
        go();
    }
      	
    }
    
    /**
     * creates a readable ASCII file
     *
     */
    public void go(){
    
	File file;
    File temp_file;
    String extension = "";
    String fileName;

    
    switch( _typeToCreate.intValue() ){
        case 1:
        case 2:
            extension = "";
            break;
        case 3:
            extension = ".section";
            break;
        case 4:
            extension = ".room";
            break;
        default:
    }
    
    
	
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
        
        if( !(fileName.endsWith( extension ) ) ) {
            fileName = fileName.concat( extension );
            temp_file = new File( fileName );
            file = temp_file;
        }
        
      if ( file.exists() ){
        // prompt user for overwrite.
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
			    evaluateType( file );
		        } // overwrite was selected			        
               
          } // does the file exist?
          else{
                    try{
			
			//file = new File( fileName );
		
		        file.createNewFile();
			
                evaluateType( file );
		
		    } //try
		    catch(Exception e){
		        System.out.println( "Error Creating File" + file +e );
		    } // catch
        } //else
      } // if

    } // go
    
   private void evaluateType( File file ){ 
       
		if ( _typeToCreate.intValue() == 1 ){
		    //generate room schedule
		    gererateRoomSch( file );
          
		} // if
		else if ( _typeToCreate.intValue() == 2 ){
		    //generate list of sections with assignments
		    generateSectionList( file );

		   
		} // elseif 2
		else if (_typeToCreate.intValue() == 3){
		    // generate section save list
		    generateSectSaveList( file );

		}
		else if (_typeToCreate.intValue() == 4){
		    // generate room save list
		    generateRoomSaveList( file );
		}
		else{
		    
		}
		

    } // evaluateType
    
    private void gererateRoomSch(File fileToWrite){
        
	Room room;
    
        
	
	
	// get the room who's schedule we want
	room = _dbase.getRoom(_gooey.getHighlightedRoom());
	
	
	if (room == null){
	    _gooey.defaultErrorMsgWindow( "Please Select a Room." );
        }
	else{
	    try{
		int i, j;
		// open file for buffered input
		BufferedWriter out = 
			new BufferedWriter(new FileWriter(fileToWrite));
			
		
		//Initial header lines
        
        addPageHeader( out, 1 );
		out.write( "Room " + room.getRoomNum() +
			   " Schedule" );
		out.newLine();
		out.write( " " );
		repPrntToBuf( out, "_", 78 );
		out.newLine();
		out.write( "| Time  ||   Monday    |   Tuesday   |   Wednesdy  "+
		           "|  Thursday   |   Friday    |");
		out.newLine();
		out.write( "|-------||" );
		for ( i = 0; i < 5; i++ ){
        	    repPrntToBuf( out, "-", 13 );
		    out.write( "|" );
		}
		out.newLine();
		out.flush();
	        
		// for every row 
		//     print the time range and 
		//     for every column print the seciton number or none
		String[][] schMatrix = room.getOccpcy();

		
		for ( i = 0; i < 14; i++ ){
		    out.write( "| "+strTime( i )+"-"+strTime( i + 1 )+" || " );
		    
		    for ( j = 0; j < 5; j++ ){
		        // if there is no assignment then print none instead
			if( schMatrix[j][i] == "     " ){
			    out.write( "   none     | " );
			}
			else{
			    out.write( schMatrix[j][i] + " | " );
			} // if empty
		    } // j for
		    out.newLine();
		    
		} // i for
		out.write( "|_______||" );
		for ( i = 0; i < 5; i++ ){
        	    repPrntToBuf( out, "_", 13 );
		    out.write( "|" );
		}
		out.newLine();
		out.flush();
/*        
        // tack a box on the bottom with a description of each of the
        // assigned sections.
		LinkedList assignments = room.getAssignments(); 
        if( assignments.size() > 0 ){       
            out.newLine();
            out.newLine();
            out.newLine();
            addColumnBreak( out, '-' );
            addSectChartHeader( out );

        
            ListIterator iter = assignments.listIterator( 0 );
         
            while( iter.hasNext() ){
                drawSection( out, (Section) iter.next() );
            }
            addColumnBreak( out, '-' );
        }
        
        addColumnBreak( out, '-' );
*/        
	    }//try
	    catch( Exception e ){
	        System.out.println("Error writing to file" + e );
	    }//catch
	} // room is highlighted
    } //generateRoomSch()
    
    private void generateSectSaveList(File fileToSaveTo){
        
	int i;
	Vector sectList;
	BufferedWriter out;
	
	try{
	    // open file for buffered input
	    out = new BufferedWriter(new FileWriter(fileToSaveTo));

            // get the list of sections and rooms to save
	    sectList = _dbase.getSectList();

	    // header for Sections seciton of save file
	    out.write(";<SECTIONS>;");
	    out.newLine();
	    // print the info for every room into the file
	    for( i = 0; i < sectList.size(); i++ ){
	        out.write( ((Section)sectList.elementAt(i)).toSaveString() );
	        out.newLine();
	    } //for
	    out.write(";<END>;");
	    out.newLine();
	    out.flush();
	} // try
        catch( Exception e ){
            System.out.println( e + " Happened when writing save file" );
        } // catch
    }
    
    private void generateRoomSaveList(File fileToSaveTo){
        int i;
	Vector roomList;
	BufferedWriter out;
	
	try{
	    // open file for buffered input
	    out = new BufferedWriter(new FileWriter(fileToSaveTo));

            // get the list of sections and rooms to save
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
        } // try
	catch( Exception e ){
            System.out.println( e + " Happened when writing save file" );
        } // catch
    } // generate room save list
    //repeated print to buffer
    
    
    private void repPrntToBuf( BufferedWriter out, String s, int n ){
        int i;
	
	try{    
	    for( i=0; i<n; i++ ){
	        out.write( s );
	    }
        out.flush();
	    
	}
	catch(Exception e){System.out.println(e + "happened when repprnt");}  

    } // repPrntToBuf
    
    private String strTime( int i ){

	if ( i < 2 ){
	    return (" " + String.valueOf( i+8 ) );
	}
	else if( i < 5 ){
	    return ( String.valueOf( i+8 ) );
	}
	else if( i < 14 ){
	    return ( " " + String.valueOf( i-4 ) );
	}
	else{
	    return ( String.valueOf( i-4 ) );
	}
    } //strTime	   
    
    private void generateSectionList( File fileToWrite ){
        try{
            BufferedWriter out = 
                new BufferedWriter(new FileWriter(fileToWrite));
                new BufferedWriter( new OutputStreamWriter( System.out ) );
            int currentPage = 1;
            int linesOnThisPage = 0;
            boolean fitsOnCurrentPage = true;
            boolean doneTheWholeThing = false;
            
            

            Vector rooms = _dbase.getRoomList();
            Vector sections = _dbase.getSectList();
            int roomsWrittenSoFar = 0;

            // this long list will deal with pages... so here's 
            // a loop which sticks to 60 lines per page.
            // first, print the rooms in boxes with lists of the sections
            // in each.
	   
            while( roomsWrittenSoFar < rooms.size() && 
                   !doneTheWholeThing){       
                // initialize the current page.
                addPageHeader( out, currentPage );
                addColumnBreak( out, '-' );
                linesOnThisPage = 2;
                fitsOnCurrentPage = true;
                // draw two boxes for rooms.  let's hope no room has more than
                // 54 assignments.
	
                while( fitsOnCurrentPage && !doneTheWholeThing ){
                    // find the next two rooms with assignments.
                    LinkedList leftAssignments = new LinkedList();
                    LinkedList rightAssignments = new LinkedList();
                    Room currentLeftRoom = null;
                    Room currentRightRoom = null;
                    if( roomsWrittenSoFar < rooms.size() ){
                        currentLeftRoom = 
                            (Room) rooms.elementAt( roomsWrittenSoFar );                            
                        leftAssignments = currentLeftRoom.getAssignments();
                        roomsWrittenSoFar += 1;
                    }
                        // ok, found one.
                    if( roomsWrittenSoFar < rooms.size() ){
                        currentRightRoom = 
                            (Room) rooms.elementAt( roomsWrittenSoFar );
                        rightAssignments = currentRightRoom.getAssignments();
                        roomsWrittenSoFar += 1;
                    }





                    // found the second room to be written.    


                    // a room with assignments requires at least 4 lines,
                    // counting the columnBreak at the bottom of its box.
                    // another line will be required for every section after
                    // the first which is assigned to the current room.
                    // will we have enough spaces to write in 
                    // the next two rooms, or should we go to the next 
                    // page?
                                      
                    // evaluate the length of these lists and set which one
                    // is longer to newLines.  this represents the number
                    // of newLines needed to draw the next pair of Room boxes.
                    // newLines starts at 3, and increases by one for every
                    // section assigned to the fuller room. (yeah, fuller)
                    
                    if( currentLeftRoom == null ){
                        doneTheWholeThing = true;
                        out.flush();
                      
                    }

                    if( !doneTheWholeThing ){
                 
                        int newLines = 3;

                        if( leftAssignments.size() > rightAssignments.size() ){
                             
			                newLines += leftAssignments.size(); 
		    
                        }
                        else{
			   
                            newLines += rightAssignments.size();
		    
                        }
			
                        if( linesOnThisPage + newLines <= 60 ){
                            // neither of the current rooms will require another
                            // page...  increment the linesOnThisPage and 
                            // draw the rooms.
                            linesOnThisPage += newLines;

                            drawRooms( out, currentLeftRoom, currentRightRoom );

			    
                        } // do the rooms fit on this page?
                        else{
			    
                            // fill in some newLines until we reach 60 lines
                            // for this page.  decrement the roomsWrittenSoFar,
                            // because these rooms will not be drawn until the
                            // next page.
                            roomsWrittenSoFar = rooms.indexOf( currentLeftRoom );

                            for( int i = linesOnThisPage; i < 60; i++ ){
                                out.newLine();
                            }
                            out.flush();
                            fitsOnCurrentPage = false;
                            currentPage += 1;
                        } // else which bumps to next page.
                    } // are there any more rooms to output?                      
                } // while loop which writes one page.
            } // .loop which prints all the rooms and their assignments

            // ok, that takes care of the room chart, now let's add section
            // details in a big ol' list.
/*            
            if( linesOnThisPage > 0 ){
                // there's something on this page, but how much?
                
                if( linesOnThisPage < 50 ){
                    out.newLine();
                    out.newLine();
                    out.newLine();
                    linesOnThisPage += 3;
                } // how much was on the page?
                else{
                    // its not worth it to put fewer than 5 sections on the
                    // tail end of a page, bump to the next one.
                    for( int i = linesOnThisPage; i < 60; i++ ){
                        out.newLine();
                    }
                    out.flush();
                    currentPage += 1;
                    linesOnThisPage = 1;
                    addPageHeader( out, currentPage );
                } // else
            } // is there anything on this page?
            else{
                addPageHeader( out, currentPage );
                linesOnThisPage = 1;
            } // else
*/

            for( int i = linesOnThisPage; i < 60; i++ ){
                out.newLine();
            }
            currentPage += 1;
            addPageHeader( out, currentPage );
            linesOnThisPage = 1;
                        
            // now, start the top of a section list.
            addColumnBreak( out, '-' );
            addSectChartHeader( out );
            linesOnThisPage += 3;
            
            
            // at this point, we know we're ready to loop through all the
            // sections and draw the info for each.  this one's a lot
            // simpler than the room chart because we're not making boxes,
            // evaluating list sizes and all that hooey.
            
            // just keep going, and if you find yourself at line 59,
            // throw in a page header, etc.

            for( int iter = 0; iter < sections.size(); iter++ ){
                if( linesOnThisPage == 0 ){
                    // add a page header.
                    currentPage += 1;
                    addPageHeader( out, currentPage );
                 
                    // add the top of a section chart.
                    addColumnBreak( out, '-' );
                    addSectChartHeader( out );

                                  
                    // re-start the number of lines to 3
                    linesOnThisPage = 4;
                } // are we at the beginning of a page?
                else if( linesOnThisPage == 59 ){


                    // we've gone far enough on this page. 
                    // close the box and get thee to a nunnery.
                    addColumnBreak( out, '-' );
                    out.newLine();
                    out.newLine();
                    linesOnThisPage = 0;

                } // else
                else{

                    drawSection( out, (Section) sections.elementAt( iter ) );
                    linesOnThisPage += 1;

                } // else
            } // for loop

            addColumnBreak( out, '-' );
            fileToWrite.setLastModified( timeStamp.getTime() );
            out.close();
        }
        catch( IOException io ){
        }
    }

    private void addPageHeader( BufferedWriter out, int pageNumber )
        throws IOException{
        // this function flushes a line out which reads
        // 'CRSS Database page X'           '<current time>'
        // it leaves 28 chars for the current time, at the right
        // side.
        String leftHeader = ( "CRSS Database page " + pageNumber );
        String rightHeader = timeStamp.toString();
        
        out.write( leftHeader );
        
        for( int i = leftHeader.length() ; i < 52; i++ ){
            out.write( " " );
        }
        
        out.write( rightHeader );
        out.newLine();
        out.flush();
        
    } // addSectListHeader
        
    private void addColumnBreak( BufferedWriter out, char spacer )
        throws IOException{    
        // this method chops the current box being drawn by outputting
        // "|------- ... ---|--- ...  -------|"
        
        for( int i = 0; i < 80; i++ ){
            if( i == 0 || i == 39 || i== 79 ){
                out.write( "|" );
            }
            else{
                out.write( spacer );
            }
        }
        out.newLine();
        out.flush();
    } // addColumnBreak
    
    private void drawRooms( BufferedWriter out, Room left, Room right)
        throws IOException{

        // first, clean up the stuff we got, keeping in mind that
        // the right room will be null if there are an odd number of
        // rooms to print, but the left room will never be null.


        LinkedList leftSections = left.getAssignments();
        String rightNum = "       ";
        LinkedList rightSections = new LinkedList();

        if( right != null ){
            rightSections = right.getAssignments();
            rightNum = right.getRoomNum();
        }
        else{
            rightSections.addFirst( "           " );
        }
        
        // the first line is the room numbers, correctly spaced.        
        out.write( "|  Room:        " + 
                   left.getRoomNum() + 
                   "                |  Room:        " +
                   rightNum +
                   "                 |" );
        out.newLine();
        out.flush();

        // if either of these sections has no assignments, we'll want
        // to indicate that by writing "None" for sections assigned.


        
        // next, output a blank line.
        addColumnBreak( out, ' ' );
        // next, output the sections header, and the first section from
        // each room.
        out.write( "|  Sections:    " );
        if( leftSections.size() > 0 ){

            out.write( (String) leftSections.getFirst() );
        }
        else{
            out.write( "None       " );
        }
        
        out.write( "            |  Sections:    " );

        if( rightSections.size() > 0 ){ 
            out.write( (String) rightSections.getFirst() );
        }
        else{
            out.write( "None       " );
        }
        
        out.write( "             |" );
	    out.newLine();
        out.flush();
        
        // now, beginning with the second assignment for each room, output
        // them in a list format.
        for( int i = 1; 
             i < leftSections.size() || i < rightSections.size();
             i++){
             out.write( "|               " );
             if( i < leftSections.size() ){
                 out.write( (String) leftSections.get( i ));
             }
             else{
                 out.write( "           " );
             }
             out.write( "            |               " );
             if( i < rightSections.size() ){
                 out.write( (String) rightSections.get( i ));
             }
             else{
                 out.write( "           " );
             }
             out.write( "             |" );
             out.newLine();
        } // loop which writes all the remaining assignment numbers.
        
        // all done! put the bottom on the box.
        addColumnBreak( out, '-' );
        //out.write( "/n/n/n" );
    } // drawRooms
    
    private void addSectChartHeader( BufferedWriter out )
        throws IOException{
        out.write( "|  Section        Description                  " );
        out.write( "Days     Times          Room    |" );
        out.newLine();
        out.write( "|" );
        repPrntToBuf( out, " ", 78 );
        out.write( "|" );
        out.newLine();    

    } // addSectChartHeader
    
    private void drawSection( BufferedWriter out, Section s )
        throws IOException{
        out.write( "|  " + s.getSectNum() + "    " );
        out.write( s.getSectName() );
        
        // now, add spaces until we get to column 47
        repPrntToBuf( out, " ",  29 - (((String) s.getSectName() ).length()) );
        
        // output the days this class meets.
        boolean[] days = s.getDays();
        for( int i = 0; i < 5; i++ ){
           if( days[i] == true ){
               switch( i ){
                   case 0:
                       out.write( 'M' );
                       break;
                   case 1:
                       out.write( 'T' );
                       break;
                   case 2:
                       out.write( 'W' );
                       break;
                   case 3:
                       out.write( 'R' );
                       break;
                   case 4: 
                       out.write( 'F' );
                       break;
                   default:
                       out.write( '?' );
                }
           } // does the section meet today?
           else{
               // the section does not meet on this day.
               out.write( ' ' );
           }
       } // loop which looks at each day.   
       
       out.write( "    " ); 
       // now, ouput the times.
       
       int startTime = s.getStartTime();
       int endTime = s.getEndTime();
       Integer printableStartTime = new Integer( startTime );
       Integer printableEndTime = new Integer( endTime );
       
       if( startTime < 10 ){
           out.write( ' ' );
       }
       
       out.write( printableStartTime.toString() );
       out.write( ":00-" );
       
       if( endTime < 10 ){
           out.write( ' ' );
       }
       
       out.write( printableEndTime.toString() );
       out.write( ":00    " );
       
       // lastly, output the room which it's assigned to.
       String assignment = s.getAssignment();
       
       out.write( assignment );
       
       if( assignment.equals( "None" )){
           out.write( "   " );
       }
       
       out.write( " |" );
       out.newLine();
       out.flush();

    } // drawSection
         
} // Generate File Action class
