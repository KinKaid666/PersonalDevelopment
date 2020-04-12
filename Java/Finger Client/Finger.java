/**File:        $Id: Finger.java,v 1.9 2000/10/03 02:29:26 etf2954 Exp etf2954 $
 * Author:      Eric Ferguson
 * Discrip:     A class the emulates the UNIX 'finger' command
 * Revision:
 *              $Log: Finger.java,v $
 *              Revision 1.9  2000/10/03 02:29:26  etf2954
 *              Stopped sending the actual arg to the
 *              server
 *              we were sending @machine name
 *
 *              Revision 1.8  2000/10/02 04:48:56  etf2954
 *              ended every line at 80
 *
 *              Revision 1.7  2000/10/02 03:52:47  etf2954
 *              Took out debugging
 *
 *              Revision 1.6  2000/10/02 03:14:11  etf2954
 *              Caught all exceptions and handled them
 *
 *              Revision 1.5  2000/10/02 03:09:56  etf2954
 *              Got user@machine working, easy
 *
 *              Revision 1.4  2000/10/02 03:04:08  etf2954
 *              got @machine name working
 *
 *              Revision 1.3  2000/10/02 02:57:55  etf2954
 *              got user info working with the /W swtich
 *
 *              Revision 1.2  2000/10/02 02:07:10  etf2954
 *              Done with blank.
 *
 *              Revision 1.1  2000/10/02 00:02:04  etf2954
 *              Initial revision
 *
 */

import java.net.*;
import java.io.*;

/**
 * Main Class
 */
public class Finger 
{

    static int fingerPort = 79;

    /**
     * This method begins the finger process
     *
     * @param args[] is the arguments that the user specifies
     */
    public static void main( String args[] ) 
    {


        InetAddress fingerhost;

        try 
        {

            Socket sock;
            String arg;

            //
            // If the user doesn't specify a argument
            //  we find all the users on the local machine
            
            if( args.length == 0 ) 
            {
                arg = null;
                fingerhost = InetAddress.getLocalHost();
                sock = new Socket( fingerhost.getHostName(), fingerPort );
            } 

            // 
            // If the user specifies a argument we have to 
            //  it
            //
            //  If that argument is a string we try to find
            //          that user on the local machine
            //
            //  if the string starts with a '@' symble
            //          we have to log on to that machine
            //          and find all the users
            //
            //  if the string is a user @ a machine we have
            //          to log onto that machine and find that 
            //          users


            else 
            {
                arg = new String( args[ 0 ] );
                int indexofat = arg.indexOf( '@' );
                String hostarg = null;
                if( indexofat == -1 ) 
                {
                    fingerhost = InetAddress.getLocalHost();

                    //
                    // Must use the "/W" switch to get
                    //  the actual user data, and not
                    //  their log in data again...

                    arg = "/W " + arg;
                    sock = new Socket( fingerhost.getHostName(), fingerPort );
                } 
                else if( indexofat == 0 ) 
                {
                    hostarg = new String( arg.substring( 1 ) );
                    arg = null;
                    fingerhost = InetAddress.getByName( hostarg );
                    sock = new Socket( fingerhost.getHostName(), fingerPort );
                } 
                else 
                {
                    hostarg = new String( arg.substring( indexofat + 1 ) );
                    arg = arg.substring( 0, indexofat );
                    fingerhost = InetAddress.getByName( hostarg );
                    sock = new Socket( fingerhost.getHostName(), fingerPort );
                }
            }

            //
            // Create the approapriate stream readers/writers

            BufferedReader din = new BufferedReader(
                               new InputStreamReader( sock.getInputStream() ) );
            BufferedWriter dout = new BufferedWriter(
                             new OutputStreamWriter( sock.getOutputStream() ) );

            //
            // Send the appropriate txt to the machine
                
            if( arg != null ) 
            {
                dout.write( arg );
            }
            dout.newLine();
            dout.flush();

            //
            // Read what it writes back and print it out

            String socketLine = new String( din.readLine() );
            while( socketLine != null ) 
            {
                System.out.println( socketLine );
                socketLine = din.readLine();
            }
            sock.close();
        }

        //
        // Catch any exception that might have occured
        //      during this 'finger' process

        catch( UnknownHostException E ) 
        {
                System.out.println( "Finger: invalid machine name" );
        }
        catch( IOException E ) 
        {
                System.out.println( "Finger: unable to open socket" );
        }

    } // main

} // Finger
