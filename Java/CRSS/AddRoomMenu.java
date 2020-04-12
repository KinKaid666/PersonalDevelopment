//File:	$Id: AddRoomMenu.java,v 1.11 2000/05/08 02:26:24 p361-45a Exp $
//Author:	Eric Ferguson
//Contrib:	None
//Descript:	The list of all options under add room in the GUI
//Revision:	
//		$Log: AddRoomMenu.java,v 
//		Revision 1.1  2000/03/28 03:08:25  p361-45a
//		Initial revision
//

import javax.swing.*;
import java.awt.event.*;
import javax.swing.AbstractAction;

public class AddRoomMenu extends JMenu {

    private SingleSectionItem singleSectionItem = 
	new SingleSectionItem( new SingleSectionAction() );
    private FromFileItem fromFileItem = 
	new FromFileItem( new FromFileAction() );
    private static Operator operator;
    
    /**
     * (constuctor) creates an object of type AddRoomMenu
     *
     * @param ourGUI the gui
     * @param oPerator a copy of the operator
     */
    public AddRoomMenu( JFrame ourGUI, Operator oPerator ) {

	operator = oPerator;
	this.setText( "Add Room" );
       	singleSectionItem.setMnemonic( 'S' );
	this.add( singleSectionItem );
	fromFileItem.setMnemonic( 'F' );
	this.add( fromFileItem );
    }
    
    /**
     * inner class to add new rooms one at a time
     */
    class SingleSectionItem extends JMenuItem {

	/**
	 * add single room
	 *
	 * @param act a value of type 'Action'
	 */
	public SingleSectionItem( Action act ) {
	    this.setText( "Single Room" );
	    this.addActionListener( act );
	}
    }

    /**
     *  inner class to add new rooms one at a time
     */
    class SingleSectionAction extends AbstractAction {

	/**
	 * sends action to database
	 *
	 * @param event the actual event to be performed
	 */
	public void actionPerformed( ActionEvent event ) {

	    AddRoomMenu.operator.addRoom( 0 );
	}
    }
   
    class FromFileItem extends JMenuItem {

	/**
	 * constructor to add from file
	 *
	 * @param act the action to take
	 */
	public FromFileItem( Action act ) {

	    this.setText( "From File" );
	    this.addActionListener( act );

	}
    }
    
    class FromFileAction extends AbstractAction {

	/**
	 * method to perform action
	 *
	 * @param event the event to be performed
	 */
	public void actionPerformed( ActionEvent event ) {

	    AddRoomMenu.operator.addRoom( 1 );
	}
    }
}




