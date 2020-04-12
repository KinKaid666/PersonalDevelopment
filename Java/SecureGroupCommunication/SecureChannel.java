/* File:        $Id: SecureChannel.java,v 2.6 2003/02/20 01:23:24 etf2954 Exp $
** Author:      p590-99d
** Contributors:Balmos,   Jeremy
**              Chasse,   Shawn
**              Dahlgren, Jeremy
**              Ferguson, Eric
** Description: SecureChannel: This is a channel used to communicate with the
**                      M2MP protocol layer. all data sent over this channel
**                      will first be encrypted with the key provided to the
**                      channel.
** Revisions:
**              use `cvs log <filename>`
*/

package SecureGroupCommunication ;

// M2MP Comm Files.
import edu.rit.m2mp.Protocol;
import edu.rit.m2mp.IncomingMessageNotifier;
import edu.rit.m2mp.MessageFilter;
import edu.rit.m2mp.IncomingMessage;
import edu.rit.m2mp.OutgoingMessage;
import edu.rit.m2mp.ip.M2MPRouterChannel;

// IO files.
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.math.*;

import javax.crypto.BadPaddingException;

// Random number generator.
import java.util.Random;
import java.math.BigInteger ;

/**
 * SecureChannel encrypts messages using password based encryption with MD5
 * and DES.  The encryption functionality is encapsulated in an instance of
 * the MultiKeyGenerator class.  Packets are sent using M2MP as the underlying
 * message protocol.
 *
 * @author Jeremy Balmos, Shawn Chasse, Jeremy Dahlgren, Eric Ferguson.
 */
public class SecureChannel
{

    // Internal class for receiving messages.
    private static class ReceiverThread extends Thread {
        private SecureChannel parentChannel_ = null;

        // Constructor
        public ReceiverThread(SecureChannel parentChannel) {
            parentChannel_ = parentChannel;
        }

        public void run() {
            try {
            System.out.println("here") ;
                // Set up the notifier to receive messages from our group.
                IncomingMessageNotifier theNotifier = parentChannel_.protocol_.createIncomingMessageNotifier();
                theNotifier.addMessageFilter (new MessageFilter (parentChannel_.prefix_));

                try {
                // Continually wait for a message.
                for(;;) {
                    IncomingMessage msg = theNotifier.accept();
                    InputStream is = msg.openInputStream();
                    DataInputStream theDIS = new DataInputStream(is);

                    // Read the prefix, and ignore.
                    byte[] pfx = new byte[parentChannel_.prefix_.length];
                    theDIS.read(pfx, 0, parentChannel_.prefix_.length);

                    // Read in the length of the message.
                    int length = theDIS.readInt();

                    // Read the message contents.
                    String[] msgArray = new String[length];
                    for(int i = 0; i < length; i++) {
                        msgArray[i] = theDIS.readUTF();
                    }
                    byte[] tba = null;
                    int len = theDIS.readInt();
                    byte[] secretData = new byte[len];
                    theDIS.read(secretData, 0, len);

                    byte[] val = null ;

                    if( parentChannel_.keySet ) {
                        val =  parentChannel_.mkg_.DecryptInformation(secretData);

                        tba = val;

                        if( tba != null )
                        {
                            parentChannel_.ref_.secureMessageReceived(msgArray, tba);
                        }
                        else
                        {
                            System.out.println("MESSAGE RECEIVED:\n\t" +
                                               "SecureChannel decryption " +
                                               "failed, discarding message.");
                        }
                    }
                    else
                    {
                        System.out.println( "MESSAGE RECEIVED:\n\t" +
                                            "SecureChannel " +
                                            "not initialized or key " +
                                            "is being changed, " +
                                            "msg discarded" ) ;

                    }
                    theDIS.close();
                }
                }
                catch( java.io.InterruptedIOException e )
                {
                   /* don't care */
                }
            }
            catch (Throwable exc) {
                System.err.println("SecureChannel: Uncaught exception");
                exc.printStackTrace(System.err);
                System.exit(-1);
            }
        }
    }

    // Other SecureChannel Data Members.
    private byte[]                     prefix_;
    private Protocol                   protocol_ ;
    private SecureMessageReceived      ref_;
    private MultiKeyGenerator          mkg_ ;
    private boolean                    keySet;
    private ReceiverThread             receiverThread_ = null ;

    /**
     * Construct a new SecureChannel.
     *
     * @param  prefix  Prefix used to address messages in the M2MP layer.
     * @param  ref     Reference to an object implementing interface
     *                 SecureMessageReceived.
     */
    public SecureChannel( byte[] prefix, SecureMessageReceived ref)
    {
        keySet = false;
        prefix_ = new byte[Defs.MAX_NAME_LENGTH+1] ;
        for( int i = 0 ; i < Defs.MAX_NAME_LENGTH ; i++ )
            prefix_[i] = prefix[i] ;
        prefix_[Defs.MAX_NAME_LENGTH] = 's' ;
        ref_ = ref;

        mkg_ = new MultiKeyGenerator();

        // Set up the protocol object.
        Random prng = new Random (System.currentTimeMillis());
        protocol_   = new Protocol
            (/*theChannel        */ new M2MPRouterChannel(),
             /*theKey1           */ prng.nextLong(),
             /*theKey2           */ prng.nextLong(),
             /*theBufferCount    */ 100,
             /*theTimeoutInterval*/ 2000L);

        //new ReceiverThread(this).start();
        receiverThread_ = new ReceiverThread(this) ;
        receiverThread_.start() ;
    }

    /**
     * Method used to disable this Secure Channel.  This means the
     * key used for encryption is no longer valid.
     */
    public void voidKey() {
        keySet = false;
    }

    /**
     * Set the new key to be used in data encryption.
     *
     * @param   key     The key to be set.
     *
     * @return          True.
     */
    public boolean setupKey( BigInteger key )
    {
        mkg_.GenerateKey(key.toString().getBytes());
        keySet = true;
        System.out.println( "SecureChannel has been initialized!\n" ) ;
        return true;
    }

    /**
     * Send a message through this secure channel.
     * Messages will be sent through the M2MP protocol.
     *
     * @param  message    Message content not to be encrypted.
     * @param  secretMsg  Part of message to be encrypted.
     */
    public void sendMessage(String[] message, byte[] secretMsg) {
        if( keySet )
        {
            DataOutputStream theDOS = null;
            String[] encryptedMessage = new String[message.length];

            //SecureChannel.printMessage(0, message);

            try {

                byte[] val = mkg_.EncryptInformation( secretMsg );

                // Get an OutputStream from the protocol object.
                OutgoingMessage msg = protocol_.createOutgoingMessage (null);
                theDOS = new DataOutputStream(msg.openOutputStream());

                // Write the group prefix
                theDOS.write(prefix_, 0, prefix_.length);

                // Send the size of the array.
                theDOS.writeInt(message.length);

                // Write the message contents.
                for(int i = 0; i < message.length; i++)
                    theDOS.writeUTF(message[i]);

                // Write the length of the encrypted info
                theDOS.writeInt(val.length);

                // Write the secret data;
                theDOS.write(val, 0, val.length);
                theDOS.close();
            }
            catch (Throwable exc) {
                System.err.println("SecureChannel.SendMessage: Uncaught exception");
                exc.printStackTrace(System.err);
                System.exit(-1);
            }
            finally
            {
                // MAKE SURE THE DATA STREAM IS CLOSED.
                if (theDOS != null) {
                    try {
                        theDOS.close();
                    }
                    catch (IOException exc) {
                    }
                }
            }
        }
        else
        {
            System.out.println( "Error: KEY NOT SETUP for " +
                        "secure communication!");
        }
    }

    public void stop() {
        receiverThread_.interrupt() ;
        receiverThread_ = null ;
    }

}
