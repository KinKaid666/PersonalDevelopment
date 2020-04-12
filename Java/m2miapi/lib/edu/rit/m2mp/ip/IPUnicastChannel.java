//******************************************************************************
//
// File:    IPUnicastChannel.java
// Package: edu.rit.m2mp.ip
// Unit:    Interface edu.rit.m2mp.ip.IPUnicastChannel
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

import edu.rit.m2mp.Channel;
import edu.rit.m2mp.ChannelAddress;

import edu.rit.m2mp.packet.IncomingPacket;
import edu.rit.m2mp.packet.OutgoingPacket;

import java.io.IOException;

import java.net.DatagramPacket;
import java.net.InetAddress;
/*
** Removed by etf2954
**
import java.net.InetSocketAddress;
*/
import java.net.DatagramSocket;

/**
 * Class IPUnicastChannel encapsulates a point-to-point {@link
 * edu.rit.m2mp.Channel </CODE>Channel<CODE>} in the Many-to-Many Protocol
 * (M2MP) that uses UDP. All M2MP packets are conveyed as UDP datagrams. The
 * IPUnicastChannel&#146;s channel address gives the IP address and UDP port
 * number on which the channel will receive incoming packets. Outgoing packets
 * may be sent to any IPUnicastChannel&#146;s channel address (IP address/UDP
 * port number).
 * <P>
 * <B>M2MP Library Version 02-Jul-2002</B>
 *
 * @author  Alan Kaminsky
 * @version 03-Jun-2002
 */
public class IPUnicastChannel
	implements Channel
	{

// Hidden data members.

	private IPChannelAddress myChannelAddress;

	private DatagramSocket mySocket = null;

// Exported constructors.

	/**
	 * Construct a new IP unicast channel attached to the given IP channel
	 * address.
	 *
	 * @param  theAddress  IP channel address.
	 *
	 * @exception  NullPointerException
	 *     (unchecked exception) Thrown if <TT>theAddress</TT> is null.
	 */
	public IPUnicastChannel
		(IPChannelAddress theAddress)
		{
		if (theAddress == null)
			{
			throw new NullPointerException();
			}
		myChannelAddress = theAddress;
		}

// Exported operations.

	/**
	 * Returns this channel&#146;s address. This is the source channel address
	 * for packets transmitted via this channel.
	 */
	public ChannelAddress getAddress()
		{
		return myChannelAddress;
		}

	/**
	 * Receive the given M2MP packet via this channel.
	 *
	 * @param  thePacket  M2MP packet to be received.
	 *
	 * @return  Source channel address of the entity that transmitted
	 *          <TT>thePacket</TT>.
	 *
	 * @exception  IOException
	 *     Thrown if an I/O error occurred.
	 * @exception  SecurityException
	 *     (unchecked exception) Thrown if there is a security manager and it
	 *     doesn&#146;t allow the operation.
	 */
	public ChannelAddress receivePacket
		(IncomingPacket thePacket)
		throws IOException
		{
		openSocketIfNecessary();
		try
			{
			byte[] buf = thePacket.getBuffer();
			DatagramPacket theDatagram =
				new DatagramPacket (buf, 0, buf.length);
			mySocket.receive (theDatagram);
			thePacket.setBufferLength (theDatagram.getLength());
			return
				new IPChannelAddress
					(new InetSocketAddress
						(theDatagram.getAddress(),
						 theDatagram.getPort()));
			}
		catch (IOException exc)
			{
			closeSocket();
			throw exc;
			}
		}

	/**
	 * Send the given M2MP packet via this channel to the given destination.
	 *
	 * @param  thePacket       M2MP packet to be sent to
	 *                         <TT>theDestination</TT>.
	 * @param  theDestination  Where to send <TT>thePacket</TT>. Must be an
	 *                         instance of class {@link IPChannelAddress
	 *                         </CODE>IPChannelAddress<CODE>}.
	 *
	 * @exception  ClassCastException
	 *     (unchecked exception) Thrown if <TT>theDestination</TT> is not an
	 *     instance of class {@link IPChannelAddress
	 *     </CODE>IPChannelAddress<CODE>}.
	 * @exception  IOException
	 *     Thrown if an I/O error occurred.
	 * @exception  SecurityException
	 *     (unchecked exception) Thrown if there is a security manager and it
	 *     doesn&#146;t allow the operation.
	 */
	public void transmitPacket
		(OutgoingPacket thePacket,
		 ChannelAddress theDestination)
		throws IOException
		{
		IPChannelAddress dest = (IPChannelAddress) theDestination;
		openSocketIfNecessary();
		try
			{

			/*
			** Modified by etf2954
			**
			DatagramPacket theDatagram =
				new DatagramPacket
					(thePacket.getBuffer(),
					 0,
					 thePacket.getBufferLength(),
					 dest.getAddress());
			*/
			DatagramPacket theDatagram =
				new DatagramPacket
					(thePacket.getBuffer(),
					 0,
					 thePacket.getBufferLength(),
					 dest.getAddress().getAddress(),
					 dest.getAddress().getPort());
			mySocket.send (theDatagram);
			}
		catch (IOException exc)
			{
			closeSocket();
			throw exc;
			}
		}

// Hidden operations.

	/**
	 * Create mySocket if necessary.
	 *
	 * @exception  IOException
	 *     Thrown if an I/O error occurred.
	 * @exception  SecurityException
	 *     (unchecked exception) Thrown if there is a security manager and it
	 *     doesn&#146;t allow the operation.
	 */
	private synchronized void openSocketIfNecessary()
		throws IOException
		{
		try
			{
			if (mySocket == null)
				{

				/* 
				** Modified by etf2954
				**
				mySocket = new DatagramSocket (myChannelAddress.getAddress());
				*/
				mySocket = new DatagramSocket( 
				    myChannelAddress.getAddress().getPort(), 
				    myChannelAddress.getAddress().getAddress());
				}
			}
		catch (IOException exc)
			{
			closeSocket();
			throw exc;
			}
		}

	/**
	 * Close down mySocket.
	 */
	private synchronized void closeSocket()
		{
		if (mySocket != null)
			{
			mySocket.close();
			mySocket = null;
			}
		}

	/**
	 * Finalize this IP multicast channel.
	 */
	protected void finalize()
		{
		closeSocket();
		}

	}
