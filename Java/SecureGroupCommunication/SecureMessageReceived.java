/* File:	$Id: SecureMessageReceived.java,v 2.2 2003/02/18 22:57:45 jad0883 Exp $		
** Author:	p590-99d
** Contributors:Balmos,	  Jeremy
**		Chasse,   Shawn
**		Dahlgren, Jeremy
**		Ferguson, Eric
** Description:	SecureMessageReceived interface: acts as a "callback" for
**			SecureChannel to communicate with the object that
**			wants to recieve a message
** Revisions:
**		use `cvs log <filename>`		
*/

package SecureGroupCommunication ;

/**
 * An object using an instance of a secure channel must implement
 * this interface so it can be notified of an incoming message.
 */
public interface SecureMessageReceived
{
    /**
     * Receive a message.
     *
     * @param  message  Received message.
     */
    public void secureMessageReceived( String[] message, byte[] Data ) ;
}
