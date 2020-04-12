/* File:        $Id: MessageReceived.java,v 2.2 2003/02/18 22:57:45 jad0883 Exp $
** Author:      p590-99d
** Contributors:Balmos,   Jeremy
**              Chasse,   Shawn
**              Dahlgren, Jeremy
**              Ferguson, Eric
** Description: MessageReceived interface: acts as a "callback" for the Group
**                      object to communicate with the object that wants to
**                      receive messages.
** Revisions:
**              use `cvs log <filename>`
*/

package SecureGroupCommunication ;

/**
 * An object using an instance of the group class must implement this
 * interface so it can be notified of incoming secure messages.
 */
public interface MessageReceived
{
    /**
     * Receive a message.
     *
     * @param  message  Received message.
     */
    public void messageReceived( String message ) ;
}
