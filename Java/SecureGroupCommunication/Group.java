/* File:        $Id: Group.java,v 2.8 2003/02/20 18:37:58 etf2954 Exp $
** Author:      p590-99d
** Contributors:Balmos,   Jeremy
**              Chasse,   Shawn
**              Dahlgren, Jeremy
**              Ferguson, Eric
** Description: Group Object which contains a binary tree and communicates
**                      over insecure and secure channel. This group object
**                      is the main implementation of the protocol described
**                      in <papername>
** Revisions:
**              use `cvs log <filename>`
*/

package SecureGroupCommunication ;

import SecureGroupCommunication.BinaryTree ;
import SecureGroupCommunication.Defs ;
import SecureGroupCommunication.InsecureChannel ;
import SecureGroupCommunication.InsecureMessageReceived ;
import SecureGroupCommunication.MessageReceived ;
import SecureGroupCommunication.SecureChannel ;
import SecureGroupCommunication.SecureMessageReceived ;
import SecureGroupCommunication.TimeoutException ;

import java.util.Hashtable;

import java.util.Random ;
import java.math.BigInteger ;

/**
 * Class Group is the controlling class in the group key
 * management protocol.  It encapsulates group controller
 * functionality as well as a regular group member's
 * funcitionality.  Any programmer using this protocol will
 * only use the public methods in this class.
 *
 * @author Jeremy Balmos, Shawn Chasse, Jeremy Dahlgren, Eric Ferguson
 */
public class Group implements InsecureMessageReceived, SecureMessageReceived
{
    /**
     * Hidden helper class for a Group object.  An instance of
     * HeartbeatThread periodically sends out a heartbeat packet
     * through a secure channel.
     */
    private static class HeartbeatThread extends Thread {
        private Group group_ = null;
        private int sendDelay_;
        private boolean go_ ;
        private boolean continue_ ;

        // Constructor
        public HeartbeatThread(Group g, int send_Delay) {
            group_ = g;
            sendDelay_ = send_Delay;
            go_ = true;
            continue_ = true ;
        }

        public void setStop()
        {
            continue_ = false ;
        }

        public void run() {
              while(continue_) {
                  if(go_) {
                      try {
                        Thread.currentThread().sleep(sendDelay_);
                        group_.sendHeartBeatMessage();
                    }
                    catch (Throwable exc) {
                        System.err.println("HeartbeatThread.run: " +
                                           "Uncaught exception");
                        exc.printStackTrace(System.err);
                        System.exit(-1);
                    }
                }
            }
        }

        public void halt() {
            go_ = false;
        }

        public void restart() {
            go_ = true;
        }
    }

    /**
     * Hidden helper class for a Group object.  An instance of
     * HeartbeatCheckThread periodically checks the timestamp of
     * the last heartbeat packet received from each group.  If the amount
     * of time passed since the last heartbeat exceeds the heartbeat
     * timeout parameter, that member will be expelled.
     */
    private static class HeartbeatCheckThread extends Thread {
        private Group group_ = null;
        private boolean go_ ;
        private int timeout_ ;
        private int ck_delay_ ;
        private boolean continue_ ;

        // Constructor
        public HeartbeatCheckThread(Group g, int timeout, int delay) {
            group_ = g;
            timeout_ = timeout ;
            ck_delay_ = delay;
            continue_ = true ;
        }

        public void setStop()
        {
            continue_ = false ;
        }

        public void run() {
              while(continue_) {
                  if(go_) {
                      try {
                        Thread.currentThread().sleep(ck_delay_);

                        int[] currMembers = group_.getTreeMembers();

                        for(int i = 0; i < currMembers.length; i++) {
                            if(currMembers[i] == group_.myMemberIndex_)
                                continue;
                            long l = group_.checkIndexTime(currMembers[i]);
                            if(System.currentTimeMillis() - l >
                               timeout_) {
                                /*
                                  System.out.println("Index " +
                                  "currMembers[i] " +
                                  " should be expelled.");
                                */
                                group_.sendExpelMessage(currMembers[i]);
                                group_.processLeave(currMembers[i]);
                            }
                        }

                    }
                    catch (Throwable exc) {
                        System.err.println("HeartbeatCheckThread.run: " +
                                           "Uncaught exception");
                        exc.printStackTrace(System.err);
                        System.exit(-1);
                    }
                }
            }
        }

        public void halt() {
            go_ = false;
        }

        public void restart() {
            go_ = true;
        }
    }

    private String                groupName_ ;
    private BinaryTree            members_ = null;
    private InsecureChannel       insChannel_ ;
    private SecureChannel         secChannel_ ;
    private MessageReceived       msgCallback_ ;
    private boolean               groupLeader_ ;
    private boolean               keyGenerated_ ;
    private int                   myMemberIndex_ ;
    private byte[]                prefix_ ;
    private Random                rng_ ;
    private HeartbeatThread       heartbeat_ = null;
    private HeartbeatCheckThread  checker_ = null;

    // Diffie Hellman variables.
    private BigInteger          DH_pVal;
    private BigInteger          DH_gVal;
    private BigInteger          DH_rVal;
    private BigInteger          DH_xVal;
    private BigInteger          DH_zVal;
    private BigInteger          DH_yVal;
    private BigInteger          DH_GL_p;
    private BigInteger          DH_GL_g;

    private Hashtable           aliveMembers_ = null;

    private int                 grp_join_timeout_ ;
    private int                 grp_HB_Delay_ ;
    private int                 grp_HB_Check_Delay_ ;
    private int                 grp_HB_timeout_ ;
    private int                 grp_Key_Proc_timeout_ ;

    /**
     * Constructor.
     *
     * This group object will be initialized with the default parameters:<BR>
     * JOIN_TIMEOUT              = 3000;<BR>
     * HEARTBEAT_SEND_DELAY      = 5000;<BR>
     * HEARTBEAT_CHECK_DELAY     = 6000;<BR>
     * HEARTBEAT_TIMEOUT         = 5 * HEARTBEAT_DELAY;<BR>
     * KEY_COMP_TIMEOUT          = HEARTBEAT_TIMEOUT;<BR>
     *
     * @param  groupName       Name of the group to join.
     * @param  applicationRef  A reference to the application using this
     *                         group object.  The group will notify this
     *                         object reference when a message is received.
     */
    public Group( String groupName, MessageReceived applicationRef )
    {
        initialize( groupName, applicationRef, Defs.JOIN_TIMEOUT,
                    Defs.HEARTBEAT_SEND_DELAY, Defs.HEARTBEAT_CHECK_DELAY,
                    Defs.HEARTBEAT_TIMEOUT, Defs.KEY_COMP_TIMEOUT );
    }

    /**
     * Constructor.
     *
     * @param  groupName       Name of the group to join.
     * @param  applicationRef  A reference to the application using this
     *                         group object.  The group will notify this
     *                         object reference when a message is received.
     * @param  join_TO         Timeout for the join procedure.
     * @param  HB_delay        Interval for heartbeat messages to be sent.
     * @param  HB_check_delay  Time inbetween checks of last
     *                         heartbeat received.
     * @param  HB_TO           Timeout for the heartbeat checker.
     * @param  Key_TO          Timeout for the key agreement procedure.
     *
     */
    public Group( String groupName, MessageReceived applicationRef,
                  int join_TO, int HB_delay, int HB_check_delay,
                  int HB_TO, int Key_TO ) {

        initialize( groupName, applicationRef, join_TO, HB_delay,
                    HB_check_delay, HB_TO, Key_TO );
    }

    // Set up the rest of the Group object.
    private void initialize( String groupName,
                             MessageReceived applicationRef,
                             int join_TO,
                             int HB_delay,
                             int HB_chk_delay,
                             int HB_TO,
                             int Key_TO ) {
        // Set Variables.
        grp_join_timeout_ = join_TO;
        grp_HB_Delay_ = HB_delay;
        grp_HB_Check_Delay_ = HB_chk_delay;
        grp_HB_timeout_ = HB_TO;
        grp_Key_Proc_timeout_ = Key_TO;

        rng_ = new Random((long)System.currentTimeMillis()) ;

        groupName_ = groupName ;

        // r val for Diffie-Hellman
        DH_rVal = BigInteger.probablePrime( Defs.BITLENGTH - 1, rng_ );

        // Convert the group name to a byte array.
        prefix_ = new byte[Defs.MAX_NAME_LENGTH];
        byte[] name = groupName_.getBytes();
        for(int i = 0; i < Defs.MAX_NAME_LENGTH; i++)
            prefix_[i] = 0;
        for(int i = 0; i < name.length; i++)
            prefix_[i] = name[i];
        myMemberIndex_ = -1;
        msgCallback_ = applicationRef;
        insChannel_ = new InsecureChannel( prefix_, this ) ;
        secChannel_ = new SecureChannel( prefix_, this ) ;
        aliveMembers_ = new Hashtable();
        join() ;
        try {
            while(!keyGenerated_)
                Thread.currentThread().sleep(100);
        }
        catch(InterruptedException ie) {
            ie.toString();
        }
        heartbeat_ = new HeartbeatThread( this, grp_HB_Delay_ );
        heartbeat_.start();
        checker_ = new HeartbeatCheckThread( this, grp_HB_timeout_,
                                             grp_HB_Check_Delay_ );
        checker_.start();
    }

    // Process a join packet message.
    private void processJoin(String[] msg) {
        if(groupLeader_) {
            try {
                Thread.currentThread().sleep(500);
            }
            catch(InterruptedException ie) {
                ie.toString();
            }
            sendJoinACKMessage(msg);
            aliveMembers_.put(new Integer(members_.addNode(null)),
                              new Long(System.currentTimeMillis()));
            members_.printTreeRepresentation();
            System.out.println("My memberIndex = " + myMemberIndex_);

            try {
                Thread.currentThread().sleep(1000);
            }
            catch(InterruptedException ie) {
                ie.toString();
            }

            groupLeaderKeyGenProc();
        }
    }

    // Send a join ack packet (Group Leader Only).
    private void sendJoinACKMessage(String[] msg) {
        System.out.println("Inside sendJoinACKMessage.");

        int[] tree = members_.getTreeRepresentation();
        String[] newMessage = new String[tree.length + 4];

        newMessage[0] = "" + Defs.JOIN_ACK;

        // Tack on the return address.
        newMessage[1] = msg[1];

        // Tree Structure.
        newMessage[2] = "" + members_.getNextMemberIndex() ;
        newMessage[3] = "" + tree.length;
        for(int i = 0; i < tree.length; i++)
        {
            newMessage[i + 4] = "" + tree[i];
        }

        sendMessage(newMessage);
        return;
    }

    // Process a Join ack packet.
    private void setupJoin(String[] msg) {

        groupLeader_ = false ;

        // Obtain Tree Info
        int  nextMemberIndex = Integer.parseInt(msg[2]) ;
        int[] rep = new int[Integer.parseInt(msg[3])] ;
        for( int i = 0 ; i < rep.length ; i++ ) {
            rep[i] = Integer.parseInt(msg[i + 4]) ;
        }

        members_ = new BinaryTree( rep ) ;
        members_.setNextMemberIndex( nextMemberIndex ) ;
        myMemberIndex_ = members_.addNode(null) ;

        // Update alive table.
        int[] currMembers = getTreeMembers();
        for(int i = 0; i < currMembers.length; i++)
            aliveMembers_.put(new Integer(currMembers[i]),
                              new Long(System.currentTimeMillis()));
        // Print the current group.
        members_.printTreeRepresentation();
        System.out.println("My memberIndex = " + myMemberIndex_);


        return;
    }

    // Attempt to join a group.
    private void join()
    {
        long startTime = System.currentTimeMillis();

        try {
            int returnAddr = rng_.nextInt();
            sendJoinMessage(returnAddr);
            while( System.currentTimeMillis() - startTime <
                   grp_join_timeout_ ) {
                String[] msg = insChannel_.receiveMessage(grp_join_timeout_,
                                                          returnAddr);
                setupJoin(msg);
                return;
            }

              System.out.println("join() function received messages, " +
              "but no JOIN_ACK");
            createGroup() ;
        }
        catch(TimeoutException toe) {
              System.out.println("Inside join() method, caught a " +
              "timeout exception.");
            createGroup();
        }
    }

    // Send a join message.
    private void sendJoinMessage(int returnAddress)
    {
        String[] message = new String[2] ;
        message[0] = "" + Defs.JOIN;
        message[1] = "" + returnAddress;
        sendMessage( message ) ;
    }

    // Create a group when the join procedure failed.
    private void createGroup() {
        System.out.println("No response, creating group.");
        members_ = new BinaryTree();
        myMemberIndex_ = members_.addNode(null);
        groupLeader_ = true;
        members_.printTreeRepresentation();
        System.out.println("My memberIndex = " +
                           myMemberIndex_);

        keyGenerated_ = secChannel_.setupKey(new BigInteger("0")) ;
    }

    // Someone Joined the group.
    private void processJoinACK(String[] msg)
    {
        System.out.println("Inside processJoinACK...");

        // We just need to update our tree.
        System.out.println("Someone joined the group.");
        members_.setNextMemberIndex(Integer.parseInt(msg[2])) ;
        aliveMembers_.put(new Integer(members_.addNode(null)),
                          new Long(System.currentTimeMillis()));

        members_.printTreeRepresentation();
        System.out.println("My memberIndex = " + myMemberIndex_);
    }

    // Process a leave packet.  Remove the member from the tree.
    private void processLeave(int index) {
        if(index == myMemberIndex_) {
            System.out.println("I have been expelled.");
            System.exit(0);
        }

        members_.removeNode(index);
        System.out.println("Removed node index = " + index);
        System.out.println("New tree structure:");
        members_.printTreeRepresentation();
        removeIndex(index);

        /*
        ** assign a new group leader
        */
        if( (myMemberIndex_ == members_.getLowestMemberIndex()) &&
            !groupLeader_ ) {
            System.out.println( "I have become the new group leader" ) ;
            groupLeader_ = true ;
        }
        System.out.println("My memberIndex = " + myMemberIndex_);

        // If I'm the group leader, start the key generation.
        if( groupLeader_ ) {
            groupLeaderKeyGenProc();
        }
    }

    // Turn on the heartbeatThread and heartbeat checker after key
    // agreement completes.
    private void heartbeatOn() {
        if(checker_ != null)
            checker_.restart();
        if(heartbeat_ != null)
            heartbeat_.restart();
        System.out.println("Enabling HeartbeatThread, " +
                           "HeartbeatCheckThread ...");
    }

    // Turn off heartbeatThread and heartbeat checker during key
    // agreement procedures.
    private void heartbeatOff() {
        if(checker_ != null)
            checker_.halt();
        if(heartbeat_ != null)
            heartbeat_.halt();
        System.out.println("Disabling HeartbeatThread, " +
                           "HeartbeatCheckThread ...");
        secChannel_.voidKey();
    }

    // Group leader key generation procedure.
    // Coordinates the key agreement.
    private void groupLeaderKeyGenProc() {
        heartbeatOff();

        int[] rep = members_.getTreeRepresentation();

        DH_GL_p = BigInteger.probablePrime( Defs.BITLENGTH, rng_ ) ;
        DH_GL_g = BigInteger.probablePrime( Defs.BITLENGTH / 2, rng_ ) ;

        for( int i = rep.length - 1; i >= 0; i-- ) {
            if( rep[i] == -1)
                keyAgreementProcedure( members_.height() -
                                       BinaryTree.level(i),
                                       i,
                                       BinaryTree.level(i));
        }
        if( rep.length == 1 ) {
            keyGenerated_ = secChannel_.setupKey( new BigInteger("0")) ;
            heartbeatOn();
        System.out.println("Key argreement procedure complete.");
        }
    }

    // Key procedure for an individual round.
    private void keyAgreementProcedure( int j, int v, int l ) {

        /*
          System.out.println("keyAgreementProcedure, j = " +
          j + " v = " + v + " l = " + l);
        */

        int leftIndex = members_.getLeftMember(v);
        int rightIndex = members_.getRightMember(v);
        if(leftIndex == myMemberIndex_ || rightIndex == myMemberIndex_) {
            System.out.println("I'm the group leader, I need to participate");

            String[] msg = new String[7];
            if(leftIndex == myMemberIndex_)
                msg[2] = "" + rightIndex;
            else
                msg[2] = "" + leftIndex;
            msg[0] = "" + Defs.KEY_COMP1;
            msg[1] = "" + myMemberIndex_;
            msg[3] = "" + l;
            msg[4] = DH_GL_p.toString();
            msg[5] = DH_GL_g.toString();
            msg[6] = "" + myMemberIndex_;

            processKeyComp1(msg);
        }
        else {
            try {
                sendKeyComp1(leftIndex, rightIndex, l, DH_GL_p,
                             DH_GL_g, myMemberIndex_);
                // Block to make sure they finished.
                insChannel_.receiveKeyDoneMsg(grp_Key_Proc_timeout_,
                                              myMemberIndex_) ;
            }
            catch( TimeoutException exc ) {
                System.out.println( "Key procedure timed out." ) ;
                System.exit(-1);
            }
        }

        /*
          System.out.println("keyAgreementProcedure, j = " +
          j + " v = " + v + " l = " + l +
          " completed.");
        */
    }

    // Update the last timestamp received from a member's heartbeat.
    synchronized private void updateTime(int i) {
        aliveMembers_.remove(new Integer(i));
        aliveMembers_.put(new Integer(i),
                          new Long(System.currentTimeMillis()));
    }

    // Remove a member from the members that the heartbeat checker monitors.
    synchronized private void removeIndex(int i) {
        aliveMembers_.remove(new Integer(i));
    }

    synchronized private long checkIndexTime(int i) {
        Long l = ((Long)aliveMembers_.get(new Integer(i)));
        if(l == null)
            return (long) (grp_HB_timeout_ + 1);
        else
            return ((Long)aliveMembers_.get(new Integer(i))).longValue();
    }

    private int[] getTreeMembers() {
        return members_.getMemberIndexes();
    }

    private void processHeartbeat(int i) {
        updateTime(i);
        System.out.println("Updating time for index = " + i);
    }

    private void processExpel(int i) {
        System.out.println("Member " + i + " has been expelled.");
        removeIndex(i);
        processLeave(i);
    }

    private void sendExpelMessage( int i ) {
        String[] msg = new String[1];
        msg[0] = "" + Defs.S_EXPEL;
        sendSMessage(msg, (new String("" + i)).getBytes());
    }

    private void sendHeartBeatMessage() {
        String[] msg = new String[1];
        msg[0] = "" + Defs.S_HEARTBEAT;
        sendSMessage(msg, (new String("" + myMemberIndex_)).getBytes());
    }

    private void sendKeyComp1( int member1, int member2, int level,
                               BigInteger valueP, BigInteger valueG,
                               int returnAddress)
    {
        String[] msg = new String[7];
        msg[0] = "" + Defs.KEY_COMP1;
        msg[1] = "" + member1;
        msg[2] = "" + member2;
        msg[3] = "" + level;
        msg[4] = valueP.toString();
        msg[5] = valueG.toString();
        msg[6] = "" + returnAddress;
        sendMessage(msg);
    }

    private void sendKeyComp2( String[] msg )
    {
        String[] newMsg = new String[7];
        for(int i = 0; i < 6; i++)
            newMsg[i] = msg[i];
        newMsg[0] = "" + Defs.KEY_COMP2;
        newMsg[6] = DH_yVal.toString();
        sendMessage(newMsg);
    }

    private void sendKeyComp3( String[] msg )
    {
        String[] newMsg = new String[8];
        for(int i = 0; i < 6; i++)
            newMsg[i] = msg[i];
        newMsg[0] = "" + Defs.KEY_COMP3;
        newMsg[6] = DH_yVal.toString();
        newMsg[7] = msg[1];
        System.out.println("About to send KeyComp3");
        sendMessage(newMsg);
    }

    private void sendDoneKeyGen(String returnAddress)
    {
        String[] msg = new String[2];
        msg[0] = "" + Defs.DONE_KEYGEN;
        msg[1] = returnAddress;
        sendMessage(msg);
    }

    // Process the first part of the key agreement procedure.
    // Compute your 'y' part of DH first, then wait for the other
    // member to do the same.  Quit if timed out.
    // Continue processing if the other member finished correctly.
    private void processKeyComp1( String[] msg )
    {
        heartbeatOff();

        processGenKey(msg);
        long start = System.currentTimeMillis();

        try {
            sendKeyComp2(msg);

            while( System.currentTimeMillis() - start <
                   grp_Key_Proc_timeout_ ) {
                String[] mssg =
                    insChannel_.receiveKeyComp3(grp_Key_Proc_timeout_,
                                                myMemberIndex_) ;
                processKeyComp3( mssg, msg[6] ) ;
                return;
            }
        }
        catch( TimeoutException exc ) {
            System.out.println( "Key procedure timed out." ) ;
            System.exit(-1);
        }
    }

    // Process the second part of the key agreement procedure.
    // Compute your 'y' part of DH, then exchange the keys.
    private void processKeyComp2( String[] msg ) {
        heartbeatOff();

        processGenKey(msg);
        processExchangePublicKeys(msg);

        try {
            Thread.currentThread().sleep(1000);
        }
        catch(InterruptedException ie) {
            ie.toString();
        }
        sendKeyComp3(msg);
        if( Integer.parseInt(msg[3]) == 0 ) {
            keyGenerated_ = secChannel_.setupKey(members_.getKey
                                                 (Integer.parseInt(msg[3]),
                                                  myMemberIndex_ ) );
            heartbeatOn();
        }
    }

    // Process the final part of the key agreement procedure.
    // Notify the group leader if successful.
    private void processKeyComp3( String[] msg , String returnAddress)
    {
        processExchangePublicKeys(msg);
        if(Integer.parseInt(returnAddress) != myMemberIndex_) {
            sendDoneKeyGen(returnAddress);
        }
        if( Integer.parseInt(msg[3]) == 0 ) {
            keyGenerated_ = secChannel_.setupKey(members_.getKey
                                                 (Integer.parseInt(msg[3]),
                                                  myMemberIndex_ ) );
            heartbeatOn();
        }
    }

    // Filter keys generated to the subtrees.
    private void sendIntermKey( int address, BigInteger data, int level)
    {
        String[] msg = new String[4];
        msg[0] = "" + Defs.INTERM_KEY;
        msg[1] = "" + address;
        msg[2] = data.toString();
        msg[3] = "" + level;
        sendMessage(msg);
    }

    // Process an intermediate key message.
    private void processIntermKey( String[] msg ) {
        DH_xVal = members_.getKey(Integer.parseInt(msg[3]) + 1,
                                  myMemberIndex_);
        BigInteger tmpBI = new BigInteger(msg[2]);
        DH_zVal =  tmpBI.modPow( DH_xVal, DH_pVal);
        /*
          System.out.println( "Intermediate key, calculated " +
          "indirectly for level " +
          msg[3] + " is "+ DH_zVal.toString() );
        */
        members_.setKey(DH_zVal, Integer.parseInt(msg[3]), myMemberIndex_);
        if( Integer.parseInt(msg[3]) == 0 ) {
            keyGenerated_ = secChannel_.setupKey( members_.getKey
                                                  (Integer.parseInt(msg[3]),
                                                   myMemberIndex_ ) );
            heartbeatOn();
        }
    }

    // Compute 'y' of Diffie-Hellman
    private void processGenKey( String[] msg )
    {
        DH_pVal = new BigInteger( msg[4] );
        DH_gVal = new BigInteger( msg[5] );

        int level = Integer.parseInt(msg[3]);

        if(( level + 1 ) == members_.getLevel(myMemberIndex_)) {
            members_.setKey(DH_rVal, level + 1, myMemberIndex_);
        }
        DH_xVal = members_.getKey(level + 1, myMemberIndex_);

        DH_yVal = DH_gVal.modPow( DH_xVal, DH_pVal );
    }

    // Actual key computation.  Raise your x to the other member's y.
    private void processExchangePublicKeys( String[] msg )
    {

        // we receive the other users public key, now we have to generate
        // the z value which will be the key between us.
        int level = Integer.parseInt(msg[3]);
        BigInteger tmpBI = new BigInteger(msg[6]);
        DH_zVal =  tmpBI.modPow( DH_xVal, DH_pVal );
        /*
          System.out.println( "My key generated for level " + level + " is "+
          DH_zVal.toString() );
        */
        members_.setKey(DH_zVal, level, myMemberIndex_);

        //Send the other guy's y val to everyone in your subtree.
        if(members_.getLevel(myMemberIndex_) > level + 1) {
            int[] siblings = members_.getSiblings(level + 1, myMemberIndex_);
            /*
              System.out.println("level = " + level + ", length of siblings " +
              "array = " + siblings.length);
            */
            for(int i = 0; i < siblings.length; i++) {
                System.out.println("sending intermkey to " + siblings[i]);
                sendIntermKey(siblings[i], tmpBI, level);
            }
        }
    }

    private void sendMessage( String[] msg )
    {
        insChannel_.sendMessage(msg);
    }
    private void sendSMessage(String[] msg, byte[] data )
    {
        secChannel_.sendMessage( msg, data );
    }

    private void processSData( String msg )
    {
        msgCallback_.messageReceived( msg );
    }

    /**
     * Get current join timeout
     *
     * @return    Timeout int value.
     */
    public int getJoinTimeout() { return grp_join_timeout_ ; }

    /**
     * Adjust the time spend waiting for a join response.
     *
     * @param  t    Timeout int value.
     */
    public void setJoinTimeout( int t ) { grp_join_timeout_ = t; }

    /**
     * Get current heartbeat delay timeout
     *
     * @return    interval before sending another heartbeat message.
     */
    public int getHeartBeatDelay() { return grp_HB_Delay_ ; }

    /**
     * Adjust the delay in between heartbeat sends.
     *
     * @param  t    interval before sending another heartbeat message.
     */
    public void setHeartBeatDelay( int t ) { grp_HB_Delay_ = t; }

    /**
     * Get current heartbeat check delay
     *
     * @return    interval before checking the last heartbeats received.
     */
    public int getHeartBeatCheckDelay()
                            { return grp_HB_Check_Delay_ ; }

    /**
     * Adjust the delay in between heartbeat checks.
     *
     * @param  t    interval before checking the last heartbeats received.
     */
    public void setHeartBeatCheckDelay( int t )
                    { grp_HB_Check_Delay_ = t; }

    /**
     * Get the current time allowed in between heartbeats
     * before a group member is expelled.
     *
     * @return    Timeout int value.
     */
    public int getHeartBeatTimeout() { return grp_HB_timeout_ ; }

    /**
     * Adjust the time allowed in between heartbeats
     * before a group member is expelled.
     *
     * @param  t    Timeout int value.
     */
    public void setHeartBeatTimeout( int t ) { grp_HB_timeout_ = t; }

    /**
     * Get the current key agreement timeout
     *
     * @return    Timeout int value.
     */
    public int getKeyProcTimeout( int t )
                   { return grp_Key_Proc_timeout_ ; }

    /**
     * Adjust the time spent waiting for a key procedure to complete.
     *
     * @param  t    Timeout int value.
     */
    public void setKeyProcTimeout( int t )
                    { grp_Key_Proc_timeout_ = t; }

    /**
     * Notify the rest of the group members that this
     * member is leaving the group.
     */
    public void leave()
    {
        System.out.println("Inside leave(), my index = " + myMemberIndex_);
        String[] msg = new String[1];
        msg[0] = "" + Defs.S_LEAVE;
        sendSMessage(msg, (new String("" + myMemberIndex_)).getBytes());
        heartbeat_.setStop() ;
        checker_.setStop() ;
        insChannel_.stop() ;
        secChannel_.stop() ;
        System.runFinalization() ;
        System.gc() ;

    }

    /**
     * Send a message through the insecure channel.
     * Message data will NOT be encrypted.
     *
     * @param  msg    Message contents to send.
     */
    public void sendInsecureMessage( String msg ) {
        String[] send_array = new String[1];
        send_array[0] = msg;
        sendMessage( send_array );
    }

    /**
     * Send a message through the secure channel.
     * Message data will be encrypted before sending.
     *
     * @param  msg    Message contents to send.
     */
    public void sendSecureMessage( String msg )
    {
        String[] send_array  = new String[1];
        send_array[0] = "" + Defs.S_DATA;
        byte[] tdata = msg.getBytes();
        sendSMessage( send_array , tdata );
    }

    /**
     * Implementation of secureMessageReceived method in interface
     * SecureMessageReceived.  A SecureChannel Object uses this
     * method to forward messages received through the secure
     * channel.<BR>
     * Packets processed are DATA, LEAVE, EXPEL, and HEARTBEAT
     * messages.
     *
     * @param  message    Message type information.
     * @param  data       Decrypted message contents.
     */
    public void secureMessageReceived( String[] message, byte[] data )
    {
        if( message.length > 0 )
        {
            switch( Integer.parseInt(message[0] ) )
                {
                case Defs.S_DATA:
                    System.out.println("Received S_DATA");
                    processSData(new String( data ));
                    break;
                case Defs.S_LEAVE:
                    System.out.println("Received S_LEAVE");
                    processLeave(Integer.parseInt(new String(data)));
                    break;
                case Defs.S_EXPEL:
                    System.out.println("Received S_EXPEL");
                    processExpel(Integer.parseInt(new String(data)));
                    break;
                case Defs.S_HEARTBEAT:
                    if(Integer.parseInt(new String(data)) != myMemberIndex_) {
                        System.out.println("Received HEARTBEAT, index = " +
                             Integer.parseInt(message[1]));
                        processHeartbeat(Integer.parseInt(new String(data)));
                    }
                    break;
                }
        }
    }

    /**
     * Implementation of insecureMessageReceived method in interface
     * InsecureMessageReceived.  An InsecureChannel object uses this
     * method to forward messages received through the insecure
     * channel.<BR>
     * Packets processed are JOIN, JOIN_ACK, KEY_COMP1, KEY_COMP2,
     * KEY_COMP3, INTERM_KEY, and DONE_KEYGEN messages.
     *
     * @param  message    Message type and data information.
     */
    public void insecureMessageReceived( String[] message )
    {
        switch(Integer.parseInt(message[0])) {

        case Defs.JOIN:
            System.out.println("Received JOIN");
            processJoin(message);
            break;

        case Defs.JOIN_ACK:
            System.out.println("Received JOIN_ACK");
            processJoinACK(message);
            break;

        case Defs.KEY_COMP1:
            if( Integer.parseInt(message[1]) == myMemberIndex_) {
                System.out.println( "Received KEY_COMP1" );
                processKeyComp1( message );
            }
            break;

        case Defs.KEY_COMP2:
            if( Integer.parseInt(message[2]) == myMemberIndex_) {
                System.out.println( "Received KEY_COMP2" );
                processKeyComp2( message );
            }
            break;

        case Defs.INTERM_KEY:
            if( Integer.parseInt(message[1]) == myMemberIndex_ ) {
                System.out.println( "Received INTERM_KEY" );
                processIntermKey(message);
            }
            break;

        case Defs.KEY_COMP3:
            //ignore, we would block for this.
            System.out.println("Received KEY_COMP3");
            break;

        case Defs.DONE_KEYGEN:
            //ignore, we would block for this.
            System.out.println("Received DONE_KEYGEN");
            break;

        default:
            System.out.println("Unknown message type received.");

        }
    }
}

