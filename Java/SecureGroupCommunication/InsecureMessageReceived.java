/* File:        $Id: InsecureMessageReceived.java,v 2.2 2003/02/18 22:57:44 jad0883 Exp $
** Author:      p590-99d
** Contributors:Balmos,   Jeremy
**              Chasse,   Shawn
**              Dahlgren, Jeremy
**              Ferguson, Eric
** Description: acts as a "callback" to allow InsecureMessage to communicate
**                      with the implementing object to alert the object
**                      when a message has been received.
** Revisions:
**              use `cvs log <filename>`
*/

package SecureGroupCommunication ;

/**
 * An object using an instance of an insecure channel must implement
 * this interface so it can be notified of an incoming message.
 */
public interface InsecureMessageReceived
{
    /**
     * Receive a message.
     *
     * @param  message  Received message.
     */
    public void insecureMessageReceived( String[] message ) ;
}
