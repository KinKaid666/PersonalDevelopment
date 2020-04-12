/* File:        $Id: InsecureChannel.java,v 2.5 2003/02/20 01:23:24 etf2954 Exp $
** Author:      p590-99d
** Contributors:Balmos,   Jeremy
**              Chasse,   Shawn
**              Dahlgren, Jeremy
**              Ferguson, Eric
** Description: Provides an interface to the M2MP protocol layer so that
**                      communication can be established over an insecure
**                      method. ie: no encryption
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

// Random number generator.
import java.util.Random;

/**
 * InsecureChannel broadcasts messages using M2MP as the underlying
 * message protocol.  The channel filters messages based on a prefix
 * generated from the group name.
 *
 * @author Jeremy Balmos, Shawn Chasse, Jeremy Dahlgren, Eric Ferguson.
 */
public class InsecureChannel
{

    // Internal class for receiving messages.
    private static class ReceiverThread extends Thread {
        private InsecureChannel parentChannel_ = null;
        private boolean continue_ ;

        // Constructor
        public ReceiverThread(InsecureChannel parentChannel) {
            parentChannel_ = parentChannel;
        }

        public void run() {
            try {
                // Set up the notifier to receive messages from our group.
                IncomingMessageNotifier theNotifier = parentChannel_.protocol_.createIncomingMessageNotifier();
                theNotifier.addMessageFilter(new MessageFilter (parentChannel_.prefix_));

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

                    for(int i = 0; i < length; i++) {
                        System.out.println (" received " + msgArray[i] ) ;
                    }
                    if(parentChannel_.waitPending_) {
                        if((Integer.parseInt(msgArray[0]) == Defs.JOIN_ACK) &&
                           (Integer.parseInt(msgArray[1]) ==
                            parentChannel_.address_)) {
                            parentChannel_.joinPacket_ = msgArray;
                            parentChannel_.waitPending_ = false;
                        }
                        else if((Integer.parseInt(msgArray[0]) ==
                                Defs.KEY_COMP3) &&
                                (Integer.parseInt(msgArray[7]) ==
                                 parentChannel_.address_))
                        {
                            parentChannel_.keyExMsg_ = msgArray ;
                            System.out.println("this KC3 should go to= " + msgArray[7]);
                            parentChannel_.waitPending_ = false;
                            theDIS.close();
                            theNotifier.close();
                            return ;
                        }
                        else if((Integer.parseInt(msgArray[0]) ==
                                Defs.DONE_KEYGEN) &&
                                (Integer.parseInt(msgArray[1]) ==
                                 parentChannel_.address_))
                        {
                            System.out.println("this KEYGEN should be for "+ "= " + msgArray[1]);
                            parentChannel_.waitPending_ = false;
                            theDIS.close();
                            theNotifier.close();
                            return ;
                        }
                        else
                        {
                            System.out.println("Channel is blocking...");
                        }
                        theDIS.close();
                    }
                    else {
                        parentChannel_.ref_.insecureMessageReceived(msgArray);
                    }
                }
                }
                catch( java.io.InterruptedIOException e )
                {
                   /* don't care */
                }
            }
            catch (Throwable exc) {
                System.err.println("ReceiverThread: Uncaught exception");
                exc.printStackTrace(System.err);
                System.exit(-1);
            }
        }
    }

    // Other InsecureChannel Data Members.
    private byte[]                     prefix_;
    private Protocol                   protocol_ ;
    private InsecureMessageReceived    ref_;
    private String[]                   joinPacket_ ;
    private String[]                   keyExMsg_ ;
    private boolean                    waitPending_;
    private int                        address_ ;
    private MultiKeyGenerator          mkg_ ;
    private ReceiverThread             receiverThread_ = null ;

    /**
     * Constructor.
     *
     * @param  prefix  Prefix used to address messages in the M2MP layer.
     * @param  ref     Reference to an object implementing interface
     *                 InsecureMessageReceived.
     */
    public InsecureChannel( byte[] prefix, InsecureMessageReceived ref)
    {
        prefix_ = new byte[Defs.MAX_NAME_LENGTH+1] ;
        for( int i = 0 ; i < Defs.MAX_NAME_LENGTH ; i++ )
            prefix_[i] = prefix[i] ;
        prefix_[Defs.MAX_NAME_LENGTH] = 'i' ;
        ref_ = ref;
        waitPending_ = false;

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

        System.out.println("InsecureChannel Ready.");
    }

    /**
     * Block until a join ack message is received.
     * Ensures that the packet was meant for the blocking object
     * by checking the input address.
     *
     * @param  timeOut  Maximum amount of time to wait.
     * @param  address  Address to check packets for.
     *
     * @return          The received packet contents.
     */
    public String[] receiveMessage(long timeOut, int address)
        throws TimeoutException
    {
        System.out.println("Waiting for message") ;
        address_ = address;
        waitPending_ = true;
        long startTime = System.currentTimeMillis();
        while(waitPending_) {
            //System.out.println("msgReceived waitPending_ = " + waitPending_) ;
            if(System.currentTimeMillis() - startTime > timeOut) {
                //System.out.println("going to throw a timeout; waitPending = " + waitPending_) ;
                waitPending_ = false;
                throw new TimeoutException("No JOIN_ACK");
            }
        }
        System.out.println("Got joinPacket message") ;
        waitPending_ = false;

        return joinPacket_;
    }

    /**
     * Block until a Key procedure message is received.
     * Ensures that the packet was meant for the blocking object
     * by checking the input address.
     *
     * @param  timeout  Maximum amount of time to wait.
     * @param  address  Address to check packets for.
     *
     * @return          The received packet contents.
     */
    public String[] receiveKeyComp3(long timeout, int address)
        throws TimeoutException
    {
        new ReceiverThread(this).start();

        address_ = address;
        waitPending_ = true;

        System.out.println("Waiting for a key Exchange message");

        long startTime = System.currentTimeMillis();

        while(waitPending_) {
            //System.out.println("keyComp3 waitPending_ = " + waitPending_) ;
            if(System.currentTimeMillis() - startTime > timeout) {
                waitPending_ = false;
                throw new TimeoutException("No KeyComp3");
            }
        }
        waitPending_ = false;
        System.out.println("Received the needed KEY_COMP3");
        return keyExMsg_;
    }

    /**
     * Block until a Key generation message is received.
     * Ensures that the packet was meant for the blocking object
     * by checking the input address.
     *
     * @param  timeout  Maximum amount of time to wait.
     * @param  address  Address to check packets for.
     */
    public void receiveKeyDoneMsg(long timeout, int address)
        throws TimeoutException
    {
        new ReceiverThread(this).start();

        address_ = address;
        waitPending_ = true;

        System.out.println("Waiting for a key done message");

        long startTime = System.currentTimeMillis();

        while(waitPending_) {
            //System.out.println("keyCompDone waitPending_ = " + waitPending_) ;
            if(System.currentTimeMillis() - startTime > timeout) {
                waitPending_ = false;
                throw new TimeoutException("No KeyComp3");
            }
        }
        waitPending_ = false;
        System.out.println("Received the needed DONE_KEYGEN");
        return;
    }

    /**
     * Send a message through this Insecure Channel.
     * Messages will be sent through the M2MP protocol.
     *
     * @param  message    Message contents to be sent.
     */
    public void sendMessage(String[] message) {
        DataOutputStream theDOS = null;

        //InsecureChannel.printMessage(0, message);

        try {
            // Get an OutputStream from the protocol object.
            OutgoingMessage msg = protocol_.createOutgoingMessage (null);
            theDOS = new DataOutputStream(msg.openOutputStream());

            // Write the group prefix
            theDOS.write(prefix_, 0, prefix_.length);

            // Send the size of the array.
            theDOS.writeInt(message.length);

            // Write the message contents.
            for(int i = 0; i < message.length; i++)
            {
                System.out.println("Insecure Message: " + message[i]) ;
                theDOS.writeUTF(message[i]);
            }

            theDOS.close();
        }
        catch (Throwable exc) {
            System.err.println("InsecureChannel.SendMessage: " +
                               "Uncaught exception");
            exc.printStackTrace(System.err);
            System.exit(-1);
        }
        finally {
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

    public void stop() {
        receiverThread_.interrupt() ;
        receiverThread_ = null ;
    }
}


