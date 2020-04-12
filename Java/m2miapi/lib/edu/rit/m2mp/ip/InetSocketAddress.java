//******************************************************************************
//
// File:    InetSocketAddress.java
// Package: edu.rit.m2mp.ip
// Unit:    Class edu.rit.m2mp.ip.InetSocketAddress
//
// This Java source file is copyright (C) 2002 by the Rochester Institute of
// Technology. All rights reserved. For further information, contact the author,
// Eric Ferguson, at etf2954@cs.rit.edu.
//
// This Java source file is part of the M2MI Library ("The Library"). The
// Library is free software; you can redistribute it and/or modify it under the
// terms of the GNU General Public License as published by the Free Software
// Foundation; either version 2 of the License, or (at your option) any later
// version.
//
// The Library is distributed in the hope that it will be useful, but WITHOUT
// ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
// FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
// details.
//
// A copy of the GNU General Public License is provided in the file gpl.txt. You
// may also obtain a copy of the GNU General Public License on the World Wide
// Web at http://www.gnu.org/licenses/gpl.html or by writing to the Free
// Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307
// USA.
//
//******************************************************************************

package edu.rit.m2mp.ip;
import java.net.InetAddress ;

/**
* Class InetSocketAddress is simple a wrapper for the objects InetAddress for an IP
* and an int for the port.  This class is made to be in place of the class that will
* come around in java version 1.4.x.
*
* @author	Eric Ferguson
* @version	18-Dec-2002
*/


public class InetSocketAddress 
{

    /* The IP */
    private InetAddress inetaddr_ ;
    /* The port */
    private int 	port_	  ;

    /**
     * Class InetSocketAddress is a class that holds an InetAddress for the IP
     * and a int representing the port.
     */
    public InetSocketAddress( InetAddress inetaddr, int port )
    {
	inetaddr_ = inetaddr ;
	port_ = port ;
    }

    /**
     * Determine if this InetSocketAddress is equal to the given object. To
     * be equal, they must be the same IP Address and socket number.
     *
     * @param	obj	Object to be tested
     *
     * @return	boolean True if object is a InetSocketAddress representing
     *			the same IP and port as this, false otherwise
     */
    public final boolean equals( Object obj )
    {
	return obj != null &&
	       obj instanceof InetSocketAddress &&
	       this.inetaddr_.equals( ((InetSocketAddress)obj).inetaddr_ ) &&
	       this.port_ == ((InetSocketAddress)obj).port_ ;
    }

    /**
     * Determine the port that this InetSocketAddress class represents.
     *
     * @return	The port represented by this
     */
    public final int getPort() { return port_ ; }

    /**
     * Determine the InetAddress that this InetSocketAddress class represents.
     *
     * @return	The InetAddress represented by this
     */
    public final InetAddress getAddress() { return inetaddr_ ; }

    /**
     * Gets the host name for this IP address.
     *
     * @return	The host name
     */
    public final String getHostName() { return inetaddr_.getHostName() ; }

    /**
     * Sets the port for this InetSocketAddress class.
     *
     * @param	port	The new port to be used for this
     */
    public void setPort( int port ) { port_ = port ; }

    /**
     * Sets the InetAddress for this InetSocketAddress class.
     *
     * @param	inetaddr	The new InetAddress to be used for this
     */
    public void setAddress( InetAddress inetaddr ) { inetaddr_ = inetaddr ; }

    /**
     * Returns a string version of this InetSocketAddress in the form of
     * <ip address>:<port>.
     * Ex. 127.0.0.1:65535
     *
     * @return	A string representing this InetSocketAddress
     */
    public String toString() { return inetaddr_.toString() + ":" + port_ ; }

    /**
     * Returns a hashcode for this IP and port.
     *
     * @return	A hash code value of this IP and port
     */
    public int hashCode() { return inetaddr_.hashCode() + port_ ; }
}
