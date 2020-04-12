/**
 * Class that will make it so that you can't enter anything longer than what is on 
 * the space available.
 *
 * @version 	$Id: FixedLengthDocument.java,v 1.3 2000/05/08 17:28:05 p361-45a Exp $
 *
 * @author		Eric Ferguson
 *
 *
 * Revisions
 * 		$Log: FixedLengthDocument.java,v $
 * 		Revision 1.3  2000/05/08 17:28:05  p361-45a
 * 		fixed formatting
 *
 * 		Revision 1.2  2000/05/03 15:05:52  etf2954
 * 		Nothing
 *
 * 		Revision 1.1  2000/04/21 02:47:50  etf2954
 * 		Initial revision
 *
 */

import javax.swing.*;
import javax.swing.text.*;
import java.awt.Toolkit;

public class FixedLengthDocument extends PlainDocument {
    
    private int maxLength;
    
    /**
     * The constructor that sets the max length
     *
     * @param length is what the max Length that the user wants
     *
     */
    public FixedLengthDocument( int length ) {
	
	this.maxLength = length;
    }

    /**
     * This method will check the users input for overflow
     *
     * @param offs the offset if any
     * @param str the string the user is trying to enter
     * @param AttributeSet the attributes of the text if any
     *
     * @exception BadLocationException if there is an error
     */
    public void insertString( int offs, String str, AttributeSet a )
	
	throws BadLocationException {
	
	if( getLength() + str.length() > maxLength ) {
	    
	    Toolkit.getDefaultToolkit().beep();
	} 
	
	else {
	
	    super.insertString( offs, str, a );
	}
	
    } // insertString

} // FixedLengthDocument
