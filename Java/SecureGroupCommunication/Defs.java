/* File:        $Id: Defs.java,v 2.4 2003/02/19 20:44:49 etf2954 Exp $
** Author:      p590-99d
** Contributors:Balmos,   Jeremy
**              Chasse,   Shawn
**              Dahlgren, Jeremy
**              Ferguson, Eric
** Description: All of the definitions used throughout our class
**
** Revisions:
**              use `cvs log <filename>`
*/

package SecureGroupCommunication ;

/**
 * Defs contains packet type definitions as well as default values
 * for timeouts and heartbeat send delays.
 *
 * @author Jeremy Balmos, Shawn Chasse, Jeremy Dahlgren, Eric Ferguson.
 */
public class Defs
{

    /**
     *  Maxiumum length of group name.
     */
    public static final int  MAX_NAME_LENGTH       = 16;

    /**
     *  Number of bits in prime numbers for Diffie-Hellman
     */
    public static final int  BITLENGTH             = 32;

    /**
     *  Join packet identifier
     */
    public static final int  JOIN                  = 0;

    /**
     *  Join acknoledgement packet identifier
     */
    public static final int  JOIN_ACK              = 1;

    /**
     *  Key agreement stage 1 packet identifier
     */
    public static final int  KEY_COMP1             = 2;

    /**
     *  Key agreement stage 2 packet identifier
     */
    public static final int  KEY_COMP2             = 3;

    /**
     *  Key agreement stage 3 packet identifier
     */
    public static final int  KEY_COMP3             = 4;

    /**
     *  Intermediate key packet identifier
     */
    public static final int  INTERM_KEY            = 5;

    /**
     *  Key agreement done packet identifier
     */
    public static final int  DONE_KEYGEN           = 6;

    /* Secure Packets */
    /**
     *  Secure data packet identifier
     */
    public static final int  S_DATA                = 100;

    /**
     *  Secure leave packet identifier
     */
    public static final int  S_LEAVE               = 101;

    /**
     *  Secure expel packet identifier
     */
    public static final int  S_EXPEL               = 102;

    /**
     *  Secure heartbeat packet identifier
     */
    public static final int  S_HEARTBEAT           = 103;

    /* Timeouts */

    /**
     *  Maximum number of milliseconds to wait for a join acknowledge packet
     */
    public static final int  JOIN_TIMEOUT          = 1000;

    /**
     *  Number of milliseconds to delay in between sending out heartbeats
     */
    public static final int  HEARTBEAT_SEND_DELAY  = 1000;

    /**
     *  Number of milliseconds to delay in between checking heartbeat status
     */
    public static final int  HEARTBEAT_CHECK_DELAY = 1000;

    /**
     *  Maximum number of milliseconds to have a member idle before assuming
     *  he is dead, and expel'ing him.
     */
    public static final int  HEARTBEAT_TIMEOUT     = 5 * HEARTBEAT_SEND_DELAY;

    /**
     *  Maximum number of milliseconds to wait during key agreement before
     *  assuming he is dead, and expel'ing him.
     */
    public static final int  KEY_COMP_TIMEOUT      = HEARTBEAT_TIMEOUT;
}
