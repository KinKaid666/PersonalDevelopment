//File:	$Id: HelpMenu.java,v 1.10 2000/05/08 17:31:20 p361-45a Exp $
//Author:	Eric Ferguson
//Contrib:	None
//Descript:	Think about it later
//Revision:	
//		$Log: HelpMenu.java,v $
//		Revision 1.10  2000/05/08 17:31:20  p361-45a
//		fixed formatting
//
//		Revision 1.9  2000/04/24 17:48:05  aec1324
//		got rid of a "\" that was keeping this file
//		from being happy with the compilier
//
//		Revision 1.8  2000/04/24 16:49:03  p361-45a
//		finished method headers
//
//		Revision 1.7  2000/04/18 03:25:34  aec1324
//		updated the interface to the Operator
//
//		Revision 1.6  2000/04/07 13:25:12  etf2954
//		Added the Operator class
//
//		Revision 1.5  2000/04/07 13:10:24  etf2954
//		Added the Operator class
//
//		Revision 1.4  2000/04/06 07:00:54  etf2954
//		Getting the GUIinterface to work
//
//		Revision 1.3  2000/04/06 05:47:29  etf2954
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

/**
 * class to create a help menu
 * inner classes are for specific types of help
 */
public class HelpMenu extends JMenu {
 
    private OnlineHelpItem onlineHelpItem = new OnlineHelpItem
	( new OnlineHelpAction() );
   
    private AboutItem aboutItem = new AboutItem( new AboutAction() );
    
    private static Operator operator;

    /**
     * constructor to create a help menu
     *
     * @param ourGUI a value of type 'JFrame'
     * @param oPerator a value of type 'Operator'
     */
    public HelpMenu( JFrame ourGUI, Operator oPerator ) {
	
	operator = oPerator;
	this.setText( "Help" );
	onlineHelpItem.setMnemonic( 'O' );
	this.add( onlineHelpItem );
	this.addSeparator();
	aboutItem.setMnemonic( 'A' );
	this.add( aboutItem );
	}

    class OnlineHelpItem extends JMenuItem {

	public OnlineHelpItem( Action act ) {
	    this.setText( "Online Help" );
	    this.addActionListener( act );
	}
    }

    class OnlineHelpAction extends AbstractAction {

	public void actionPerformed( ActionEvent event ) {
	    HelpMenu.operator.help();
	}
    }
    
    class AboutItem extends JMenuItem {

	public AboutItem( Action act ) {
	    this.setText( "About" );
	    this.addActionListener( act );
	}
    }
    
    class AboutAction extends AbstractAction {

	public void actionPerformed( ActionEvent event ) {
	    HelpMenu.operator.about();
	}
    }

}
