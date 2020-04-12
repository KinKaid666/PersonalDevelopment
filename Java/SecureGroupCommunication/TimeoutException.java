/* File:	$Id: TimeoutException.java,v 2.3 2003/02/19 20:44:50 etf2954 Exp $
** Author:	p590-99d
** Contributors:Balmos,	  Jeremy
**		Chasse,   Shawn
**		Dahlgren, Jeremy
**		Ferguson, Eric
** Description: What is thrown when an InsecureChannel timeout occurs.
** Revisions:
**		use `cvs log <filename>`		
*/

package SecureGroupCommunication ;

/**
 * Timeout thrown when a join procedure, key agreement procedure, or other
 * blocking call fails.
 */
public class TimeoutException extends Exception
{

    /**
     *  Construct a new TimeoutException
     *
     *  @param	exc	The message describing the exception.
     */
    public TimeoutException( String exc )
    { 
	super(exc) ;
    }
}
