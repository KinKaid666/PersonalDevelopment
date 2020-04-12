/**
 * Class that perfoms the requested sort
 *
 * @version    $Id: SortAction.java,v 1.6 2000/05/08 17:19:59 p361-45a Exp $
 *
 * @author     Christopher Bunk
 * @author     Adam Chipalowsky
 * @author     Dan Cooley
 * @author     Eric Ferguson
 * @author     Phil Light
 *
 * Revisions:
 *     $Log: SortAction.java,v $
 *     Revision 1.6  2000/05/08 17:19:59  p361-45a
 *     fixed formatting
 *
 *     Revision 1.5  2000/05/03 01:50:02  pel2367
 *     changed the call to sort to not pass
 *     extra data anymore
 *
 *     Revision 1.4  2000/04/29 03:39:02  pel2367
 *     Made it work with unfilter, too
 *
 *     Revision 1.3  2000/04/18 13:26:49  p361-45a
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

public class SortAction extends CRSSAction{

    int _whichWin;

    /**
     * constructor to create an object of type SortAction
     *
     * @param myGUI a value of type 'GUI'
     * @param database a value of type 'CRSSDatabase'
     * @param whichWin a value of type 'int'
     */
    public SortAction( GUI myGUI, 
                       CRSSDatabase database,  
                       int whichWin ){

        _gooey = myGUI;
        _dbase = database;
        _whichWin = whichWin;

        if( _whichWin == 0 ){
            _gooey.unHighlightSections();
        }

        else if( _whichWin != 1 && _whichWin != 2 ){
            throw new IllegalArgumentException();
        }

        go();
    }
    
    /**
     * method to sort classes and rooms
     *
     */
    public void go(){

        if( _whichWin == 1 ){
            refreshSectList();
        }

        else{
            refreshRoomList();
        }
        
    }
    
} // SortAction class
