
//File:	$Id: EditMenu.java,v 1.13 2000/05/08 03:42:39 p361-45a Exp $
//Author:	Eric Ferguson
//Contrib:	None
//Descript:	Think about it later
//Revision:	
//		$Log: EditMenu.java,v $
//		Revision 1.13  2000/05/08 03:42:39  p361-45a
//		fixed formatting
//
//		Revision 1.12  2000/05/05 18:22:50  aec1324
//		added undo and redo to the menu
//
//		Revision 1.11  2000/05/03 15:05:52  etf2954
//		nothing
//
//		Revision 1.10  2000/04/24 16:22:20  p361-45a
//		finished method headers
//
//		Revision 1.9  2000/04/18 03:25:34  aec1324
//		updated the interface to the Operator
//
//		Revision 1.8  2000/04/14 02:51:42  etf2954
//		Added the Edit info and add
//
//		Revision 1.7  2000/04/07 13:10:24  etf2954
//		Added the Operator class
//
//		Revision 1.6  2000/04/06 07:00:54  etf2954
//		Getting the GUIinterface to work
//
//		Revision 1.5  2000/04/06 05:55:17  etf2954
//		Make it so all actions go threw GUIinterface
//
//		Revision 1.4  2000/04/06 05:47:29  etf2954
//		Gave the class access to GUIinterface
//
//		Revision 1.3  2000/03/30 05:19:22  etf2954
//		nothing
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

public class EditMenu extends JMenu {
    
    private UndoItem undoItem = new UndoItem( new UndoItemAction() );
    private RedoItem redoItem = new RedoItem( new RedoItemAction() );

    private RemSectionItem remSectionItem = 
	new RemSectionItem( new RemSectionAction() );

    private RemRoomItem remRoomItem =
	new RemRoomItem( new RemRoomAction() );

    private EditSectionItem editSectionItem = 
	new EditSectionItem( new EditSectionAction() );

    private EditRoomItem editRoomItem = 
	new EditRoomItem( new EditRoomAction() );

    private AssignSectionItem assignSectionItem = 
	new AssignSectionItem( new AssignSectionAction() );

    private UnassignSectionItem unassignSectionItem = 
	new UnassignSectionItem( new UnassignSectionAction() );

    private AddSectionMenu addSectionMenu;
    private AddRoomMenu addRoomMenu;
    private static Operator operator;
    
    /**
     * constructor to create an object of type EditMenu
     *
     * @param ourGUI a value of type 'JFrame'
     * @param oPerator a value of type 'Operator'
     */
    public EditMenu( JFrame ourGUI, Operator oPerator ) {
	operator = oPerator;
	this.setText( "Edit" );

	this.add( undoItem );
	this.add( redoItem );
	this.addSeparator();
	
	addSectionMenu = new AddSectionMenu( ourGUI, operator );
	addSectionMenu.setMnemonic( 'A' );
	this.add( addSectionMenu );
		
	addRoomMenu = new AddRoomMenu( ourGUI, operator );
	addRoomMenu.setMnemonic( 'R' );
	this.add( addRoomMenu );
	
	this.addSeparator();
	remSectionItem.setMnemonic( 'S' );
	this.add( remSectionItem );
	remRoomItem.setMnemonic( 'O' );
	this.add( remRoomItem );
	this.addSeparator();
	editSectionItem.setMnemonic( 'D' );
	this.add( editSectionItem );
	editRoomItem.setMnemonic( 'M' );
	this.add( editRoomItem );
	this.addSeparator();
	assignSectionItem.setMnemonic( 'I' );
	this.add( assignSectionItem );
	unassignSectionItem.setMnemonic( 'U' );
	this.add( unassignSectionItem );
    }
    
    class UndoItem extends JMenuItem {

	/**
	 * constructor to create an object of type RemSectionItem
	 *
	 * @param act a value of type 'Action'
	 */
	public UndoItem( Action act ) {
	    this.setText( "Undo" );
	    this.addActionListener( act );
	}
    }
    
    class UndoItemAction extends AbstractAction {

	/**
	 * method to remove section
	 *
	 * @param event a value of type 'ActionEvent'
	 */
	public void actionPerformed( ActionEvent event ) {
	    EditMenu.operator.undo();
	}
    }
    
    class RedoItem extends JMenuItem {

	/**
	 * constructor to create an object of type RemSectionItem
	 *
	 * @param act a value of type 'Action'
	 */
	public RedoItem( Action act ) {
	    this.setText( "Redo" );
	    this.addActionListener( act );
	}
    }
    
    class RedoItemAction extends AbstractAction {

	/**
	 * method to remove section
	 *
	 * @param event a value of type 'ActionEvent'
	 */
	public void actionPerformed( ActionEvent event ) {
	    EditMenu.operator.redo();
	}
    }

    class RemSectionItem extends JMenuItem {

	/**
	 * constructor to create an object of type RemSectionItem
	 *
	 * @param act a value of type 'Action'
	 */
	public RemSectionItem( Action act ) {
	    this.setText( "Remove Section" );
	    this.addActionListener( act );
	}
    }
    
    class RemSectionAction extends AbstractAction {

	/**
	 * method to remove section
	 *
	 * @param event a value of type 'ActionEvent'
	 */
	public void actionPerformed( ActionEvent event ) {
	    EditMenu.operator.removeSection();
	}
    }
    
    class RemRoomItem extends JMenuItem {

	/**
	 * constructor to create an object of type RemRoomItem
	 *
	 * @param act a value of type 'Action'
	 */
	public RemRoomItem( Action act ) {
	    this.setText( "Remove Room" );
	    this.addActionListener( act );
	}
    }
    
    class RemRoomAction extends AbstractAction {

	/**
	 * method to remove room
	 *
	 * @param event a value of type 'ActionEvent'
	 */
	public void actionPerformed( ActionEvent event ) {
	    EditMenu.operator.removeRoom();
	}
    }
    
    class EditSectionItem extends JMenuItem {

	/**
	 * constructor to create an object of type EditSectionItem
	 *
	 * @param act a value of type 'Action'
	 */
	public EditSectionItem( Action act ) {
	    this.setText( "Edit Section" );
	    this.addActionListener( act );
	}
    }
    
    class EditSectionAction extends AbstractAction {

	/**
	 * method to edit section
	 *
	 * @param event a value of type 'ActionEvent'
	 */
	public void actionPerformed( ActionEvent event ) {
	    EditMenu.operator.modifySection();
	}
    }
    
    class EditRoomItem extends JMenuItem {

	/**
	 * constructor to create an object of type EditRoomItem
	 *
	 * @param act a value of type 'Action'
	 */
	public EditRoomItem( Action act ) {
	    this.setText( "Edit Room" );
	    this.addActionListener( act );
	}
    }
    
    class EditRoomAction extends AbstractAction {

	/**
	 * method to edit room
	 *
	 * @param event a value of type 'ActionEvent'
	 */
	public void actionPerformed( ActionEvent event ) {
		operator.modifyRoom();
	}
    }
    
    class AssignSectionItem extends JMenuItem {

	/**
	 * constructor to create an object of type AssignSectionItem
	 *
	 * @param act a value of type 'Action'
	 */
	public AssignSectionItem( Action act ) {
	    this.setText( "Assign Room to Section" );
	    this.addActionListener( act );
	}
    }
    
    class AssignSectionAction extends AbstractAction {

	/**
	 * method to assign section
	 *
	 * @param event a value of type 'ActionEvent'
	 */
	public void actionPerformed( ActionEvent event ) {
	    EditMenu.operator.assign();
	}
    }
    
    class UnassignSectionItem extends JMenuItem {

	/**
	 * constructor to create an object of type UnassignSectionItem
	 *
	 * @param act a value of type 'Action'
	 */
	public UnassignSectionItem( Action act ) {
	    this.setText( "Unassign Section from Room" );
	    this.addActionListener( act );
	}
    }
    
    class UnassignSectionAction extends AbstractAction {

	/**
	 * method to unassign a section
	 *
	 * @param event a value of type 'ActionEvent'
	 */
	public void actionPerformed( ActionEvent event ) {
	    EditMenu.operator.unassign();
	}
    }
}
