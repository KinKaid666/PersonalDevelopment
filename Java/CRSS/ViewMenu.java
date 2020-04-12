//File:	$Id: ViewMenu.java,v 1.1 2000/03/28 03:08:25 p361-45a Exp p361-45a 
//Author:	Eric Ferguso
//Contrib:	None
//Descript:	Think about it later
//Revision:	
//		$Log: ViewMenu.java,v $
//		Revision 1.11  2000/05/08 18:56:51  p361-45a
//		fixed formatting
//
//		Revision 1.10  2000/05/03 02:41:08  pel2367
//		added the "show all rooms" item.
//
//		Revision 1.9  2000/05/03 01:50:02  pel2367
//		passed the gui to the two menus so they can
//		change its sort style
//
//		Revision 1.8  2000/04/29 03:39:02  pel2367
//		made a call to the operator on 'show all
//		rooms'
//
//		Revision 1.7  2000/04/25 01:47:53  p361-45a
//		finished method headers
//
//		Revision 1.6  2000/04/14 02:51:42  etf2954
//		Added the Sort Room By feature and "Show All Rooms" item
//
//		Revision 1.5  2000/04/07 13:10:24  etf2954
//		Added the Operator class
//
//		Revision 1.4  2000/04/06 07:00:54  etf2954
//		Getting the GUIinterface to work
//
//		Revision 1.3  2000/04/06 05:49:10  etf2954
//		Gave the class access to the GUIinterface
//
//		Revision 1.2  2000/03/28 03:15:25  p361-45a
//		Skeleton
//
//		Revision 1.1  2000/03/28 03:08:25  p361-45a
//		Initial revision
//

import javax.swing.*;
import java.awt.event.*;
import javax.swing.AbstractAction;

public class ViewMenu extends JMenu {
    
    private SortSectionMenu sortSectionMenu;
    private SortRoomMenu sortRoomMenu;
    private ShowAllRoomsItem showAllRoomsItem = 
	new ShowAllRoomsItem( new ShowAllRoomsAction() );
    
    private static Operator operator;

        /**
         * constructor to create an object of type ViewMenu
         *
         * @param ourGUI a value of type 'JFrame'
         * @param oPerator a value of type 'Operator'
         */
        public ViewMenu( GUI ourGUI, Operator oPerator ) {
	
	    operator = oPerator;
	    this.setText( "View" );
	    
	    sortSectionMenu = new SortSectionMenu( ourGUI, operator );
	    sortSectionMenu.setMnemonic( 'S' );
	    this.add( sortSectionMenu );
	    sortRoomMenu = new SortRoomMenu( ourGUI, operator );
	    sortRoomMenu.setMnemonic( 'R' );
	    this.add( sortRoomMenu );
        
	    this.addSeparator();
	    this.add( showAllRoomsItem );
	}
    
    /**
     * class to unfilter room list
     */
    class ShowAllRoomsItem extends JMenuItem{
	
	/**
	 * constructor to create an object of type ShowAllRoomsItem
	 *
	 * @param act a value of type 'Action'
	 */
	public ShowAllRoomsItem( Action act ) {
	
	    this.setText( "Show all Rooms" );
	    this.addActionListener( act );
	}
    }

    /**
     * class to unfilter room list
     */
    class ShowAllRoomsAction extends AbstractAction{
	
	/**
	 * constructor to create an object of type ShowAllRoomsAction
	 *
	 * @param event a value of type 'ActionEvent'
	 */
	public void actionPerformed( ActionEvent event ) {
            
	    operator.showAllRooms();
	}
    }
    
}

