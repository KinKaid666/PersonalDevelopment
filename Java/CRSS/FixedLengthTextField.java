/**
 * This class will setup the interface for the FixedLengthDigit class
 *
 * @version 	$Id: FixedLengthTextField.java,v 1.2 2000/05/08 17:29:37 p361-45a Exp $
 *
 * @author		Eric Ferguson
 *
 *
 * Revisions
 * 		$Log: FixedLengthTextField.java,v $
 * 		Revision 1.2  2000/05/08 17:29:37  p361-45a
 * 		fixed formatting
 *
 * 		Revision 1.1  2000/04/21 02:47:50  etf2954
 * 		Initial revision
 *
 */

import javax.swing.*;
import javax.swing.text.*;
import java.awt.Toolkit;
import java.lang.Character.*;
import java.lang.*;
import java.awt.event.*;


public class FixedLengthTextField extends JTextField {

    /**
     * This is a contructor not specifying what the txt is
     *
     * @param length is to set the max_length
     *
     */
    public FixedLengthTextField( int length ) {
	
	this( null, length );
    } // Contructor
    
    /**
     * This is a contructor that specifies both the length and the 
     *		initial string
     * 
     * @param str is the initial string that is to be in the window
     * @param length sets the max_length
     *
     */
    public FixedLengthTextField( String str, int length ) {
	
	super( new FixedLengthDocument( length ), str, length );
    } //Constructor

} // FixedLengthTextFiedl
