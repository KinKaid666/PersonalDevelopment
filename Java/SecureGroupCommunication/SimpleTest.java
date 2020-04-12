/* File:        $Id: SimpleTest.java,v 2.2 2003/02/18 22:57:45 jad0883 Exp $
** Author:      p590-99d
** Contributors:Balmos,   Jeremy
**              Chasse,   Shawn
**              Dahlgren, Jeremy
**              Ferguson, Eric
** Description:
** Revisions:
**              use `cvs log <filename>`
*/
// Test the Group Object

package SecureGroupCommunication ;
import java.io.*;

import SecureGroupCommunication.Group ;
import SecureGroupCommunication.MessageReceived ;

public class SimpleTest implements MessageReceived {

    // Group instance.
    Group g = null;

    // Initialize the group.
    public SimpleTest(String name) {
        g = new Group(name, this);
    }

    public void quit() {
        g.leave();
    }

    // Receive a message from the channel receiver thread.
    public void messageReceived( String message ) {
        System.out.println("MESSAGE RECEIVED:");
            System.out.println("\t" + message);
    }

    public void sendSDataMessage(String s )
    {
        g.sendSecureMessage(s);
    }

    // Main class.
    public static void main(String args[]) throws IOException {

        // Class instance.
        SimpleTest t = new SimpleTest(args[0]);

        // Setup some input.
        String input = null;
        BufferedReader in = new BufferedReader(new InputStreamReader
                                               (System.in));
        // Loop and send messages.
        while(true) {
            input = in.readLine();
            if(input.equals("exit")) {
                t.quit();
                System.exit(0);
            }
            else if(input.equals("kill")) {
                System.exit(0);
            }
            else
                t.sendSDataMessage(input);
        }
    }
}
