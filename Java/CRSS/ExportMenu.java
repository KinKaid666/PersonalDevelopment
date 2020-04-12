//File:	$Id: ExportMenu.java,v 1.8 2000/05/08 03:49:33 p361-45a Exp $
//Author:	Eric Ferguson
//Contrib:	None
//Descript:	Think about it later
//Revision:	
//		$Log: ExportMenu.java,v $
//		Revision 1.8  2000/05/08 03:49:33  p361-45a
//		fixed formatting
//
//		Revision 1.7  2000/05/05 19:42:22  cmb3548
//		all four eport options now implemented
//
//		Revision 1.6  2000/04/28 17:27:07  cmb3548
//		Export room now fully functional
//
//		Revision 1.5  2000/04/27 17:04:16  cmb3548
//		Functionality for both types of export exist now
//
//		Revision 1.4  2000/04/26 15:37:41  cmb3548
//		Added Operator call.
//
//		Revision 1.3  2000/04/26 13:49:45  cmb3548
//		Functionality added for room schedule export
//
//		Revision 1.2  2000/04/24 16:35:59  p361-45a
//		finished method headers
//
//		Revision 1.1  2000/04/14 02:51:42  etf2954
//		Initial revision
//


import javax.swing.*;
import java.awt.event.*;
import javax.swing.AbstractAction;

/**
 * class to export lists from the database to a saved file
 * the inner classes specify what type of lists
 */
public class ExportMenu extends JMenu {
    private SectAndAssItem sectAndAssItem = new SectAndAssItem
	( new SectAndAssAction() );
    private RoomOccItem roomOccItem = new RoomOccItem(new RoomOccAction());
    private SectOnlyItem sectOnlyItem = new SectOnlyItem(new SectOnlyAction());
    private RoomOnlyItem roomOnlyItem = new RoomOnlyItem(new RoomOnlyAction());
    private static Operator operator;
    /**
     * constructor to create an object of type ExportMenu
     *
     * @param ourGUI a value of type 'JFrame'
     * @param oPerator a value of type 'Operator'
     */
    public ExportMenu( JFrame ourGUI, Operator oPerator ) {
	operator = oPerator;
	this.setText( "Export" );
	
	sectAndAssItem.setMnemonic( 'S' );
	this.add( sectAndAssItem );
	roomOccItem.setMnemonic( 'R' );
	this.add( roomOccItem );
	sectOnlyItem.setMnemonic( 'E' );
	this.add( sectOnlyItem );
	roomOnlyItem.setMnemonic( 'O' );
	this.add( roomOnlyItem );
    }
    
    class SectAndAssItem extends JMenuItem {
	public SectAndAssItem( Action act ) {
	    this.setText( "Sections and Assignments" );
	    this.addActionListener( act );
	}
	
    }
    
    class SectAndAssAction extends AbstractAction {
	public void actionPerformed( ActionEvent event ) {
	    ExportMenu.operator.genFile( 2 );
	}
    }
    
    class RoomOccItem extends JMenuItem {
	public RoomOccItem( Action act ) {
	    this.setText( "Room Occupancy Sheet" );
	    this.addActionListener( act );
	}
    }
    
    class RoomOccAction extends AbstractAction {
	public void actionPerformed( ActionEvent event ) {
	    ExportMenu.operator.genFile( 1 );
	}
    }
    
    class SectOnlyItem extends JMenuItem {
	public SectOnlyItem( Action act ) {
	    this.setText( "Section Only" );
	    this.addActionListener( act );
	}
    }
    
    class SectOnlyAction extends AbstractAction {
	public void actionPerformed( ActionEvent event ) {
	    ExportMenu.operator.genFile( 3 );
	}
    }
    
    class RoomOnlyItem extends JMenuItem {
	public RoomOnlyItem( Action act ) {
	    this.setText( "Room Only" );
	    this.addActionListener( act );
	}
    }
    
    class RoomOnlyAction extends AbstractAction {
	public void actionPerformed( ActionEvent event ) {
	    ExportMenu.operator.genFile( 4 );
	}
    }
}
