//File:	$Id: SortRoomMenu.java,v 1.5 2000/05/08 17:46:14 p361-45a Exp $
//Author:	Eric Ferguson
//Contrib:	None
//Descript:	Think about it later
//Revision:	
//		$Log: SortRoomMenu.java,v $
//		Revision 1.5  2000/05/08 17:46:14  p361-45a
//		fixed formatting
//
//		Revision 1.4  2000/05/03 02:41:08  pel2367
//		added the reverse sort options.
//		and finished them.
//
//		Revision 1.3  2000/05/03 01:50:02  pel2367
//		started adding the reverse selections
//
//		Revision 1.2  2000/04/29 03:39:02  pel2367
//		placed the calls to the operator in.
//
//		Revision 1.1  2000/04/14 02:51:42  etf2954
//		Initial revision
//

import javax.swing.*;
import java.awt.event.*;
import javax.swing.AbstractAction;

public class SortRoomMenu extends JMenu {
    
    private RoomNumberItem roomNumberItem = 
        new RoomNumberItem( new RoomNumberAction() );
    
    private RoomCapacityItem roomCapacityItem = 
        new RoomCapacityItem( new RoomCapacityAction() );
    
    private RoomNameItem roomNameItem = 
        new RoomNameItem( new RoomNameAction() );
    
    private ReverseNumberItem reverseNumberItem = 
        new ReverseNumberItem( new ReverseNumberAction() );
    
    private ReverseCapacityItem reverseCapacityItem = 
        new ReverseCapacityItem( new ReverseCapacityAction() );
    
    private ReverseNameItem reverseNameItem =
        new ReverseNameItem( new ReverseNameAction() );
        
    private static Operator operator;
    
    private GUI gui;

    /**
     * constructor to make a sort room menu
     *
     * @param ourGUI a value of type 'GUI'
     * @param oPerator a value of type 'Operator'
     */
    public SortRoomMenu( GUI ourGUI, Operator oPerator ) {
	
	operator = oPerator;
        gui = ourGUI;
	this.setText( "Sort Room by" );
	
	roomNumberItem.setMnemonic( 'N' );
	this.add( roomNumberItem );
	roomCapacityItem.setMnemonic( 'C' );
	this.add( roomCapacityItem );
        roomNameItem.setMnemonic( 'D' );
        this.add( roomNameItem );
        
        this.addSeparator();
        
        reverseNumberItem.setMnemonic( 'R' );
        this.add( reverseNumberItem );
        reverseCapacityItem.setMnemonic( 'T' );
        this.add( reverseCapacityItem );
        reverseNameItem.setMnemonic( 'S' );
        this.add( reverseNameItem );
        
	}

    class RoomNameItem extends JMenuItem {
        
	public RoomNameItem( Action act ) {
            this.setText( "Ascending Room Description" );
            this.addActionListener( act );
        }
    }
    
    class RoomNameAction extends AbstractAction {
    
	public void actionPerformed( ActionEvent event ){
            gui.setRoomSortStyle( 3 );
            operator.sortRooms();
        }
    }

    class RoomNumberItem extends JMenuItem {
	 
	public RoomNumberItem( Action act ) {
	    this.setText( "Ascending Room Number" );
	    this.addActionListener( act );
	}
    }

    class RoomNumberAction extends AbstractAction {
	
	public void actionPerformed( ActionEvent event ) {
            gui.setRoomSortStyle( 1 );
            operator.sortRooms();
	}
    }

    class RoomCapacityItem extends JMenuItem {
	
	public RoomCapacityItem( Action act ) {
	    this.setText( "Ascending Room Capacity" );
	    this.addActionListener( act );
	}
    }

    class RoomCapacityAction extends AbstractAction {
	
	public void actionPerformed( ActionEvent event ) {
            gui.setRoomSortStyle( 2 );
            operator.sortRooms();
	}
    }

    class ReverseNameItem extends JMenuItem {
        
	public ReverseNameItem( Action act ) {
            this.setText( "Descending Room Description" );
            this.addActionListener( act );
        }
    }
    
    class ReverseNameAction extends AbstractAction {
     
	public void actionPerformed( ActionEvent event ){
            gui.setRoomSortStyle( -3 );
            operator.sortRooms();
        }
    }

    class ReverseNumberItem extends JMenuItem {
	
	public ReverseNumberItem( Action act ) {
	    this.setText( "Descending Room Number" );
	    this.addActionListener( act );
	}
    }
    
    class ReverseNumberAction extends AbstractAction {
	
	public void actionPerformed( ActionEvent event ) {
            gui.setRoomSortStyle( -1 );
            operator.sortRooms();
	}
    }
    
    class ReverseCapacityItem extends JMenuItem {
	
	public ReverseCapacityItem( Action act ) {
	    this.setText( "Descending Room Capacity" );
	    this.addActionListener( act );
	}
    }
    
    class ReverseCapacityAction extends AbstractAction {
	
	public void actionPerformed( ActionEvent event ) {
            gui.setRoomSortStyle( -2 );
            operator.sortRooms();
	}
    }

}
