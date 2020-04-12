/**
 * Exception that is throw when a bad aciton is inputted
 *
 * @version    $Id: BadActionException.java,v 1.3 2000/04/21 18:23:33 p361-45a Exp $
 *
 * @author     Christopher Bunk
 * @author     Adam Chipalowsky
 * @author     Dan Cooley
 * @author     Eric Fergesun
 * @author     Phil Light
 *
 * Revisions:
 *     $Log: BadActionException.java,v $
 *     Revision 1.3  2000/04/21 18:23:33  p361-45a
 *     Funcionality added
 *
 *     Revision 1.2  2000/04/18 02:17:56  p361-45a
 *     finished method headers
 *
 *     Revision 1.1  2000/04/16 22:54:20  p361-45a
 *     Initial revision
 *
 */

import java.lang.*;

/**
 * when an action is not ok this is the exception
 */
public class BadActionException extends Exception{

    /**
     * constructor creates BadActionException object
     *
     */
    public BadActionException(){
        super();
    }
    
    /**
     * constructor creates BadActionException objec
     *
     * @param s a value of type 'String'
     */
    public BadActionException(String s){
        super( s );
    }
    
}



