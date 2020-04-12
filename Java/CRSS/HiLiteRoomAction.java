/**
 * Class that perfoms the Highlight room action
 *
 * @version    $Id: HiLiteRoomAction.java,v 1.6 2000/05/08 16:29:23 p361-45a Exp $
 *
 * @author     Christopher Bunk
 * @author     Adam Chipalowsky
 * @author     Dan Cooley
 * @author     Eric Ferguson
 * @author     Phil Light
 *
 * Revisions:
 *     $Log: HiLiteRoomAction.java,v $
 *     Revision 1.6  2000/05/08 16:29:23  p361-45a
 *     fixed formatting
 *
 *     Revision 1.5  2000/04/25 04:51:08  pel2367
 *     asks for a refreshRoomOcc() now.
 *
 *     Revision 1.4  2000/04/24 16:51:50  p361-45a
 *     finished method headers
 *
 *     Revision 1.3  2000/04/23 22:32:28  pel2367
 *     wrote this.
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

public class HiLiteRoomAction extends CRSSAction{

    /**
     * constructor to create an object of type HiLiteRoomAction
     *
     * @param myGUI a value of type 'GUI'
     * @param mydbase a value of type 'CRSSDatabase'
     */
    public HiLiteRoomAction( GUI myGUI, CRSSDatabase mydbase ){

    	_gooey = myGUI;
        _dbase = mydbase;
        go();
    }
    
    /**
     * method to refresh attributes and schedule window
     *
     */
    public void go(){

    	refreshRoomAttrib();
        refreshRoomOcc();
    }
    
} // HiLiteRoom Action class
