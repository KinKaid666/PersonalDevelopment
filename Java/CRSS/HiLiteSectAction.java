/**
 * Class that perfoms the Highlight Section action
 *
 * @version    $Id: HiLiteSectAction.java,v 1.15 2000/05/08 16:31:32 p361-45a Exp $
 *
 * @author     Christopher Bunk
 * @author     Adam Chipalowsky
 * @author     Dan Cooley
 * @author     Eric Ferguson
 * @author     Phil Light
 *
 * Revisions:
 *     $Log: HiLiteSectAction.java,v $
 *     Revision 1.15  2000/05/08 16:31:32  p361-45a
 *     fixed formatting
 *
 *     Revision 1.14  2000/05/03 05:19:44  pel2367
 *     created another constructor which highlights
 *     the section number which it was passed before
 *     calling go().
 *
 *     Revision 1.13  2000/05/02 16:37:50  etf2954
 *     Added a refreshRoomOcc() call to the go() function
 *     so then it will update the room Occ when you click
 *     on a section and that in turns hilites a different
 *     room
 *
 *     Revision 1.12  2000/04/27 03:52:55  etf2954
 *     added refreshRoomAttribs() call
 *
 *     Revision 1.11  2000/04/24 16:53:47  p361-45a
 *     finished method headers
 *
 *     Revision 1.10  2000/04/24 04:26:38  etf2954
 *     Got rid of debug msg
 *
 *     Revision 1.9  2000/04/24 02:10:29  aec1324
 *     commented out a refresh to make it work
 *
 *     Revision 1.8  2000/04/24 02:01:46  aec1324
 *     fixed highlight problems
 *     for the most part
 *
 *     Revision 1.7  2000/04/23 22:32:28  pel2367
 *     turned the debugging messages back on.
 *
 *     Revision 1.6  2000/04/23 18:16:31  pel2367
 *     commented out the one remaining line, the refreshSectAttrib().
 *
 *     Revision 1.5  2000/04/22 22:05:36  pel2367
 *     commented out all the guts of the thing here.
 *
 *     Revision 1.4  2000/04/22 19:38:00  pel2367
 *     took out some debugging messages, things seem to be going
 *     well.
 *
 *     Revision 1.3  2000/04/22 16:23:00  aec1324
 *     added partial implementation, calls go
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

public class HiLiteSectAction extends CRSSAction{
   
    /**
     * constructor to create an object of type HiLiteSectAction
     *
     * @param gui a value of type 'GUI'
     * @param database a value of type 'CRSSDatabase'
     */
    public HiLiteSectAction( GUI gui, CRSSDatabase database ){
    
	_gooey = gui;
	_dbase = database;

	go();
   
    }
    
    /**
     * constructor to create an object of type HiLiteSectAction
     *
     * @param gui a value of type 'GUI'
     * @param database a value of type 'CRSSDatabase'
     * @param str a value of type 'String'
     */
    public HiLiteSectAction( GUI gui, CRSSDatabase database, String str ){

        _gooey = gui;
        _dbase = database;
        
        _gooey.setHighlightedSection( str );
        go();
    }
         
    /**
     * method to refresh room window and section attributes
     *
     */
    public void go(){

	refreshRoomList();
	refreshRoomAttrib();
	refreshRoomOcc();
	refreshSectionAttrib();
    }
    
} // HiLiteSection Action class
