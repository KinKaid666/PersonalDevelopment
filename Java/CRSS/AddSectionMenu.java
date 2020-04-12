//File:	$Id: AddSectionMenu.java,v 1.8 2000/05/08 02:45:44 p361-45a Exp $
//Author:	Eric Ferguson
//Contrib:	None
//Descript:	Think about it later
//Revision:	
//		$Log: AddSectionMenu.java,v $
//		Revision 1.8  2000/05/08 02:45:44  p361-45a
//		fixed formatting
//
//		Revision 1.7  2000/04/24 16:34:23  aec1324
//		fixed it so it adds for a seingle section
//		and can add from files
//
//		Revision 1.6  2000/04/20 16:15:56  p361-45a
//		finished method headers
//
//		Revision 1.5  2000/04/18 03:25:34  aec1324
//		updated the interface to the Operator
//
//		Revision 1.4  2000/04/07 13:10:24  etf2954
//		Added the Operator class
//
//		Revision 1.3  2000/04/06 07:00:54  etf2954
//		Getting the GUIinterface to work
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

public class AddSectionMenu extends JMenu {

    private SingleSectionItem singleSectionItem = 
	new SingleSectionItem( new SingleSectionAction() );
    private FromFileItem fromFileItem = 
	new FromFileItem( new FromFileAction() );
    private static Operator operator;
    
    /**
     * constructor to create an object of type AddSectionMenu
     *
     * @param ourGUI a value of type 'JFrame'
     * @param oPerator a value of type 'Operator'
     */
    public AddSectionMenu( JFrame ourGUI, Operator oPerator ) {

	operator = oPerator;
	this.setText( "Add Section" );
       	singleSectionItem.setMnemonic( 'S' );
	this.add( singleSectionItem );
	fromFileItem.setMnemonic( 'F' );
	this.add( fromFileItem );
    }
 
    class SingleSectionItem extends JMenuItem {
	/**
	 * constructor to create an object of type SingleSectionItem 
	 *
	 * @param act the type of action
	 */
	public SingleSectionItem( Action act ) {
	    this.setText( "Single Section" );
	    this.addActionListener( act );
	}
    }
    
    class SingleSectionAction extends AbstractAction {

	/**
	 * method to perform action
	 *
	 * @param event the event to be performed
	 */
	public void actionPerformed( ActionEvent event ) {
	    AddSectionMenu.operator.addSection( 0 );
	}
    }
    
    class FromFileItem extends JMenuItem {

	/**
	 * constructor to create an object of type FromFileItem
	 *
	 * @param act the action taken
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
	    AddSectionMenu.operator.addSection( 1 );
	}
    }
}
