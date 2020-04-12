/**
 * Popdir.java
 *
 * Version:
 *    $Id: Popdir.java,v 1.3 2000/11/17 21:05:37 etf2954 Exp etf2954 $
 *
 * Revisions:
 *    $Log: Popdir.java,v $
 *    Revision 1.3  2000/11/17 21:05:37  etf2954
 *    prints out correct number of new msgs
 *    ./
 *
 *    Revision 1.2  2000/11/16 16:17:15  etf2954
 *    wrote authorization stage
 *
 *    Revision 1.1  2000/11/16 02:52:28  etf2954
 *    Initial revision
 *
 */

import java.util.*;
import java.io.*;
import java.net.*;

/**
 *
 * Blah blah blah
 *
 * @author Eric Ferguson
 */

public class Popdir 
{

    static final int popPort = 110;
    static String userString = "USER ";
    static String passString = "PASS ";
    static String statString = "STAT";
    static String okString = "OK";
    static String errString = "ER";
    static String eolString = ".";

    public static void main( String args[] ) 
    {

        InetAddress popServer = null;
        Socket socket = null;
        BufferedReader NETin = null;
        BufferedReader TERMin = null;
        BufferedWriter NETout = null;
        String command = null;
        String passwd = null;
        int newMsgs = 0;

        /*    check args    */
        if( args.length != 2 ) 
        {
            System.err.println( "usage: java Popdir <mailserver> <username>" );
            System.exit( 1 );
        }

        try 
        {

            popServer = InetAddress.getByName( args[ 0 ] );
            socket = new Socket( popServer.getHostName(), popPort );

            NETin = new BufferedReader( 
                             new InputStreamReader( socket.getInputStream() ) );
            NETout = new BufferedWriter(
                           new OutputStreamWriter( socket.getOutputStream() ) );
            TERMin = new BufferedReader( new InputStreamReader( System.in ) );

            String netLine = null;
            String ret = null;

            command = new String( userString + args[ 1 ] );
            NETout.write( command );
            NETout.newLine();
            NETout.flush();

            netLine = NETin.readLine();
            netLine = NETin.readLine();

            //System.out.println( netLine );
            System.out.print( "Password: " );
            passwd = TERMin.readLine();

            NETout.write( passString + passwd );
            NETout.newLine();
            NETout.flush();

            netLine = NETin.readLine();
            ret = new String( netLine.substring( 1, 3 ) );

            //System.out.println( netLine );
            if( ret.equals( errString ) ) 
            {
                System.err.println( netLine );
                System.exit( 1 );
            }

            NETout.write( statString );
            NETout.newLine();
            NETout.flush();

            netLine = NETin.readLine();
            newMsgs = new Integer( netLine.substring( 4, 5 ) ).intValue();
            //System.out.println( netLine );

            System.out.println( newMsgs + " new Msgs" );
            NETout.write( "QUIT" );
            NETout.newLine();
            NETout.flush();

            try 
            {
                NETin.close();
                NETout.close();
                socket.close();
            } 
            catch( IOException E ) 
            {
                E.printStackTrace();
            }
                
        } 
        catch( UnknownHostException E ) 
        {
            System.err.println( "Popdir: invalid machine name" );
        } 
        catch( IOException E ) 
        {
            System.err.println( "Popdir: unable to open socket" );
        }
    } // main
} // Popdir
