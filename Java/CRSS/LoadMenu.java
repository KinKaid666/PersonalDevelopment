//File:	$Id: LoadMenu.java,v 1.4 2000/05/08 17:33:17 p361-45a Exp $
//Author:	Eric Ferguson
//Contrib:	None
//Descript:	Think about it later
//Revision:	
//		$Log: LoadMenu.java,v $
//		Revision 1.4  2000/05/08 17:33:17  p361-45a
//		                                                                                               fixed formatting
//
//		Revision 1.3  2000/05/03 14:40:20  cmb3548
//		cut and splice method refered to in operator removed
//
//		Revision 1.2  2000/04/18 03:25:34  aec1324
//		updated the interface to the Operator
//
//		Revision 1.1  2000/04/14 02:53:02  aec1324
//		Initial revision
//
//		Revision 1.5  2000/04/07 13:10:24  etf2954
//		Added the Operator class
//
//		Revision 1.4  2000/04/06 07:00:54  etf2954
//		Getting the GUIinterface to work
//
//		Revision 1.3  2000/04/05 00:29:45  etf2954
//		Added more of the AbstractActions
//
//		Revision 1.2  2000/03/30 05:19:22  etf2954
//		created it and added the action listeners
//
//		Revision 1.1  2000/03/30 04:34:09  etf2954
//		Initial revision
//

import javax.swing.*;
import java.awt.event.*;
import javax.swing.AbstractAction;

public class LoadMenu extends JMenu {
  
    private RoomListItem roomListItem = 
	new RoomListItem( new RoomListAction() );
   
    private SectionListItem sectionListItem = 
	new SectionListItem( new SectionListAction() );
    
    private static Operator operator;
    
    /**
     * constructor to create a load menu
     *
     * @param ourGUI a value of type 'JFrame'
     * @param oPerator a value of type 'Operator'
     */
    public LoadMenu( JFrame ourGUI, Operator oPerator ) {

	operator = oPerator;
	this.setText( "Load" );
	
	roomListItem.setMnemonic( 'R' );
	this.add( roomListItem );
	sectionListItem.setMnemonic( 'S' );
	this.add( sectionListItem );
    }
    
    class RoomListItem extends JMenuItem {
	
	public RoomListItem( Action act ) {
	    this.setText( "Room List..." );
	    this.addActionListener( act );
	}
    }
    
    class RoomListAction extends AbstractAction {
	
	public void actionPerformed( ActionEvent event ) {
	    operator.loadState();
	}
    }
    
    class SectionListItem extends JMenuItem {
	
	public SectionListItem( Action act ) {
	    this.setText( "Section List..." );
	    this.addActionListener( act );
	}
    }
    
    class SectionListAction extends AbstractAction {
	
	public void actionPerformed( ActionEvent event ) {
	    // operator.openAndSplice();
	}
    }

}
