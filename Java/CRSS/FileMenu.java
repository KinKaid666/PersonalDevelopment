//File:	$Id: FileMenu.java,v 1.12 2000/05/08 03:56:31 p361-45a Exp $
//Author:	Eric Ferguson
//Contrib:	None
//Descript:	Think about it later
//Revision:	
//		$Log: FileMenu.java,v $
//		Revision 1.12  2000/05/08 03:56:31  p361-45a
//		fixed formatting
//
//		Revision 1.11  2000/05/01 02:56:02  cmb3548
//		Load State option now implemented
//
//		Revision 1.10  2000/04/24 16:38:04  p361-45a
//		finished method headers
//
//		Revision 1.9  2000/04/18 03:25:34  aec1324
//		updated the interface to the Operator
//
//		Revision 1.8  2000/04/14 02:51:42  etf2954
//		Deleted Export and such and made them their own Menu
//
//		Revision 1.7  2000/04/07 13:10:24  etf2954
//		Added the Operator class
//
//		Revision 1.6  2000/04/06 07:00:54  etf2954
//		Getting the GUIinterface to work
//
//		Revision 1.5  2000/04/06 05:47:29  etf2954
//		Gave the class access to GUIinterface
//
//		Revision 1.4  2000/04/04 23:27:29  etf2954
//		Finished making all of the Menu Events
//		Go threw the GUIevent class
//
//		Revision 1.3  2000/04/04 21:04:17  etf2954
//		started to interface the actions of the menubar with the GUIevent class
//
//		Revision 1.2  2000/03/30 05:19:22  etf2954
//		added the load menu
//
//		Revision 1.1  2000/03/28 03:08:25  p361-45a
//		Initial revision
//


import javax.swing.*;
import java.awt.event.*;
import javax.swing.AbstractAction;

/**
 * class for the file drop down menu
 * inner classes are for each specific menu option
 */
public class FileMenu extends JMenu {

    private NewItem newItem = new NewItem( new NewAction() );
    private OpenItem openItem = new OpenItem( new OpenAction() );
    private SaveItem saveItem = new SaveItem( new SaveAction() );
    private ExitItem exitItem = new ExitItem( new ExitAction() );
    private static Operator operator;
    public FileMenu( JFrame ourGUI, Operator oPerator ) {
	operator = oPerator;
	this.setText( "File" );
		
	newItem.setMnemonic( 'N' );
	this.add( newItem );
	openItem.setMnemonic( 'O' );
	this.add( openItem );
	saveItem.setMnemonic( 'S' );
	this.add( saveItem );
	this.addSeparator();
	exitItem.setMnemonic( 'X' );
	this.add( exitItem );
    }
    
    class NewItem extends JMenuItem {
	public NewItem( Action act ) {
	    this.setText( "New" );
	    this.addActionListener( act );
	}
    }
    
    class NewAction extends AbstractAction {
	public void actionPerformed( ActionEvent event ) {
	    FileMenu.operator.newState();
	}
    }
    
    class OpenItem extends JMenuItem {
	public OpenItem( Action act ) {
	    this.setText( "Open..." );
	    this.addActionListener( act );
	}
    }
    
    class OpenAction extends AbstractAction {
	public void actionPerformed( ActionEvent event ) {
	    FileMenu.operator.loadState();
	}
    }
    
    class SaveItem extends JMenuItem {
	public SaveItem( Action act ) {
	    this.setText( "Save..." );
	    this.addActionListener( act );
	}
    }
    
    class SaveAction extends AbstractAction {
	public void actionPerformed( ActionEvent event ) {
	    FileMenu.operator.saveState();
	}
    }
    
    class ExitItem extends JMenuItem {
	public ExitItem( Action act ) {
	    this.setText( "Exit" );
	    this.addActionListener( act );
	}
    }
    
    class ExitAction extends AbstractAction {
	public void actionPerformed( ActionEvent event ) {
	    FileMenu.operator.exit();
	}
    }
}

