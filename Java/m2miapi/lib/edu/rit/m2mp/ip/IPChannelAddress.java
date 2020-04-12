//******************************************************************************
//
// File:    IPChannelAddress.java
// Package: edu.rit.m2mp.ip
// Unit:    Interface edu.rit.m2mp.ip.IPChannelAddress
//
// This Java source file is copyright (C) 2001, 2002 by the Rochester Institute
// of Technology. All rights reserved. For further information, contact the
// author, Alan Kaminsky, at ark@it.rit.edu.
//
// This Java source file is part of the M2MP Library ("The Library"). The
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

import edu.rit.m2mp.ChannelAddress;

import java.io.Serializable;

/*
** Removed by etf2954
**
import java.net.InetSocketAddress;
*/

/**
 * Class IPChannelAddress encapsulates a channel address for the IP-based
 * channel implementations in the Many-to-Many Protocol (M2MP).
 * <P>
 * <B>M2MP Library Version 02-Jul-2002</B>
 *
 * @author  Alan Kaminsky
 * @version 03-Jun-2002
 */
public class IPChannelAddress
	implements ChannelAddress, Serializable
	{

// Hidden data members.

	private InetSocketAddress myAddress;

// Exported constructors.

	/**
	 * Construct a new IP channel address.
	 *
	 * @param  theAddress  Socket address.
	 *
	 * @exception  NullPointerException
	 *     (unchecked exception) Thrown if <TT>theAddress</TT> is null.
	 */
	public IPChannelAddress
		(InetSocketAddress theAddress)
		{
		if (theAddress == null)
			{
			throw new NullPointerException();
			}
		myAddress = theAddress;
		}

// Exported operations.

	/**
	 * Returns this channel address&#146;s socket address.
	 */
	public InetSocketAddress getAddress()
		{
		return myAddress;
		}

// Exported operations inherited and overridden from class Object.

	/**
	 * Determine if this IP channel address is equal to the given object. To be
	 * equal, the given object must be a non-null instance of class
	 * IPChannelAddress whose socket address is equal to this IP channel
	 * address&#146;s socket address.
	 *
	 * @param  obj  Object to test.
	 *
	 * @return  True if this IP channel address is equal to <TT>obj</TT>, false
	 *          otherwise.
	 */
	public boolean equals
		(Object obj)
		{
		return
			obj != null &&
			obj instanceof IPChannelAddress &&
			this.myAddress.equals (((IPChannelAddress) obj).myAddress);
		}

	/**
	 * Returns a hash code for this IP channel address.
	 */
	public int hashCode()
		{
		return myAddress.hashCode();
		}

	/**
	 * Returns a string version of this IP channel address.
	 */
	public String toString()
		{
		return myAddress.toString();
		}

	}
