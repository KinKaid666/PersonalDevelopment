/**
 * This class will only allow the user to enter #s
 *
 * @version 	$Id: FixedLengthDigitTextField.java,v 1.3 2000/05/08 17:26:39 p361-45a Exp $
 *
 * @author		Eric Ferguson
 *
 *
 * Revisions
 * 		$Log: FixedLengthDigitTextField.java,v $
 * 		Revision 1.3  2000/05/08 17:26:39  p361-45a
 * 		fixed formatting
 *
 * 		Revision 1.2  2000/04/21 13:09:05  etf2954
 * 		Fixed error in the control characters
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

public class FixedLengthDigitTextField extends FixedLengthTextField 
    implements KeyListener {

    /**
     * This constructor makes a digit only text field of length 'length'
     *
     * @param 	length	is the max length of the field
     *
     */
    public FixedLengthDigitTextField( int length ) {
	
	this( null, length );
	addKeyListener( this );
    } // Constructor
    
    /**
     * This constructor makes a digit only text field of length 'length'
     * and with the init text 'str'
     *
     * @param	str		is the init string that the field will have
     * @param 	length	is the max length of the field
     *
     */
    public FixedLengthDigitTextField( String str, int length ) {
	
	super( str, length );
	addKeyListener( this );
    } // Constructor


    //
    // KeyListener is an abstract class and we need to implement the next 
    // 3 function, namely 'keyTyped', 'keyPressed', and 'keyReleased'.
    //

    
    /**
     * This is the method that will evoke when a key is pressed
     *
     * @param 	e	is the event 
     *
     */
    public void keyTyped( KeyEvent e ) {
	
	char temp = e.getKeyChar();
	
	// Check to see if the character is a digit
	// and if it's not, lets beep :)
	// We also need to consume the character so that
	// the screen doesn't get it and the user knows
	// that the input is incorrect
	if( !( Character.isDigit( temp ) ) && temp != '\b'
	    && ( !( Character.isISOControl( temp ) ) ) ) {
	    e.consume();
	    Toolkit.getDefaultToolkit().beep();
	} // if
   
    } // keyTyped

    /**
     * This is what happens when a pressed event occurs
     *
     *
     * @param e is the event 
     *
     */
    public void keyPressed( KeyEvent e ) {

	// This is not used since we are checking everything
	// when the user releases the key.
	// Theoretically it could be done here, but I choose the next.
	} // keyPressed

    /**
     * This is what happens when a key is released
     *
     *
     * @param e	is the event 
     *
     */
    public void keyReleased( KeyEvent e ) {
	
	String temp_str = "";
	char temp_char;
	char temp = Character.toUpperCase( e.getKeyChar() );
	int ascii_code = e.getKeyCode();
	
	if( Character.isISOControl( temp ) ) {
	    
	    if( ascii_code == 86 ) {
		temp_str = this.getText();
		
		//
		// If any characters are not characters, beep
				
		for( int i = 0; i < temp_str.length(); i++ ) {
		    temp_char = temp_str.charAt( i );
		    
		    if( !Character.isDigit( temp_char ) ) {
			Toolkit.getDefaultToolkit().beep();
			this.setText( "" );
		    } // if
		
		} // for
	
	    } // if
	
	} // if
	
    } // keyPressed

} // FixedLengthDigitTextField
