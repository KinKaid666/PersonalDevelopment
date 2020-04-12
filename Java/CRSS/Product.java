//File:	$Id: Product.java,v 1.15 2000/05/08 17:00:37 p361-45a Exp $
//Author:Eric Ferguson
//Contrib:	
//Revition:
//		$Log: Product.java,v $
//		Revision 1.15  2000/05/08 17:00:37  p361-45a
//		fixed formatting
//
//		Revision 1.14  2000/05/05 20:27:20  aec1324
//		changed the placement of the GUI window
//
//		Revision 1.13  2000/05/05 15:29:22  aec1324
//		made a slight change to the position where the
//		GUI opens
//
//		Revision 1.12  2000/05/05 01:37:17  aec1324
//		changed the location of the window opening
//
//		Revision 1.11  2000/05/04 21:11:57  etf2954
//		Made a new State after the program load
//
//		Revision 1.10  2000/04/24 17:31:11  p361-45a
//		finished method headers
//
//		Revision 1.9  2000/04/20 02:27:38  aec1324
//		told the operator about the GUI
//
//		Revision 1.8  2000/04/18 21:42:05  etf2954
//		got rid of all test classes
//
//		Revision 1.7  2000/04/09 20:31:38  aec1324
//		FOR TESTING ONLY: Told the operator about the
//		gui
//
//		Revision 1.6  2000/04/07 13:10:24  etf2954
//		Added the Operator class
//
//		Revision 1.5  2000/04/06 05:47:29  etf2954
//		The Class just creates the Operator and the GUIinterface now
//
//		Revision 1.4  2000/04/05 00:23:27  etf2954
//		Made it so there was a border at bottom
//
//		Revision 1.3  2000/04/01 22:05:13  aec1324
//		had this object create a test object and (sorta)
//		give the test object controll over the program
//
//		Revision 1.2  2000/03/28 03:15:25  p361-45a
//		Skeleton
//
//		Revision 1.1  2000/03/28 03:08:25  p361-45a
//		Initial revision
//

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;

/**
 * main class
 */
public class Product {
    
    private static Operator operator;
    private GUI ourGUI;
    private CRSSDatabase dataBase;
    
    /**
     * this method only opens a new database and a new gui
     *
     * @param args[] a value of type 'String'
     */
    public static void main( String args[] ) {
	
	operator = new Operator();
	GUI ourGUI = new GUI( operator );
	CRSSDatabase dataBase = new CRSSDatabase();
	
	//tell the Operator about the GUI
	operator.setMyGUI(ourGUI);
	operator.setMyDataBase(dataBase);
	ourGUI.setSize( 1024, 768 );
	ourGUI.setLocation( 75, 65 );
	ourGUI.show();
	operator.newState();
	
    }//main

}//Product

