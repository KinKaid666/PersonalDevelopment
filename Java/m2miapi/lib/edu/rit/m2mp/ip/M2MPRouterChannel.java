//******************************************************************************
//
// File:    M2MPRouterChannel.java
// Package: edu.rit.m2mp.ip
// Unit:    Class edu.rit.m2mp.ip.M2MPRouterChannel
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
/*
** Added by etf2954
*/
import java.io.InterruptedIOException;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
/*
** Romoved by etf2954
**
import java.net.InetSocketAddress;
*/

/*
** Romoved by etf2954
**
import java.net.SocketTimeoutException;
*/

/*
** Added by etf2954
*/
import java.net.InetAddress;

/**
 * Class M2MPRouterChannel provides a Many-to-Many Protocol (M2MP) {@link
 * edu.rit.m2mp.Channel </CODE>Channel<CODE>} implementation that uses an {@link
 * M2MPRouter </CODE>M2MPRouter<CODE>} running in a separate process to
 * broadcast M2MP messages. The channel sends each outgoing packet as a UDP
 * datagram to IP address 127.0.0.1 and a designated port. The M2MP Router
 * receives the packet, broadcasts it to all other processes in the same host,
 * and broadcasts it on the external network. The channel receives incoming
 * packets back from the M2MP Router. If the channel has not sent an outgoing
 * packet for 30 seconds, the channel sends a zero-length datagram to the M2MP
 * Router to report the channel's continued existence.
 * <P>
 * <B>M2MP Library Version 02-Jul-2002</B>
 *
 * @author  Alan Kaminsky
 * @version 27-Jun-2002
 */
public class M2MPRouterChannel
	implements Channel
	{

// Hidden constants.

	private static final long LEASE_RENEWAL = 30000L;

// Hidden data members.

	private InetSocketAddress mySendAddress;
	private IPChannelAddress myChannelAddress;

	private DatagramSocket mySocket = null;

	private long myLastSendTime = 0L;

// Exported constructors.

	/**
	 * Construct a new M2MP Router channel that sends packets to the default
	 * port number, 5678.
	 */
	public M2MPRouterChannel()
		{
		this (5678);
		}

	/**
	 * Construct a new M2MP Router channel that sends packets to the given port
	 * number.
	 *
	 * @param  thePort  Port number.
	 *
	 * @exception  IllegalArgumentException
	 *     (unchecked exception) Thrown if <TT>thePort</TT> is not in the range
	 *     0 .. 65535.
	 */
	public M2MPRouterChannel
		(int thePort)
		{

		/*
		** Modified by etf2954
		**
		mySendAddress = new InetSocketAddress ("127.0.0.1", thePort);
		*/
		try
		    {
		    mySendAddress = new InetSocketAddress (InetAddress.getByName("127.0.0.1"), thePort);
		    }
		    catch( java.net.UnknownHostException ex )
		    {
		    }
		myChannelAddress = new IPChannelAddress (mySendAddress);
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
	 * <P>
	 * <I>Note:</I> This M2MP channel implementation always receives packets
	 * from the same place, namely the M2MP Router, and does not distinguish
	 * among sources. The returned source address is this
	 * channel&#146;s address.
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
			// Set up to store incoming datagram into packet buffer.
			byte[] buf = thePacket.getBuffer();
			DatagramPacket theDatagram =
				new DatagramPacket (buf, 0, buf.length);

			// Do lease renewals until a packet is received.
			boolean done = false;
			while (! done)
				{
				// If it's time to renew the lease, send a zero-length datagram.
				long now = System.currentTimeMillis();
				if (now - myLastSendTime >= LEASE_RENEWAL)
					{
					myLastSendTime = now;
					mySocket.send
						(new DatagramPacket (buf, 0, 0, mySendAddress.getAddress(),mySendAddress.getPort()));
					}

				// Set timeout for the next least renewal.
				mySocket.setSoTimeout
					(Math.max
						(1, (int) (myLastSendTime + LEASE_RENEWAL - now)));

				// Wait to receive a packet or timeout.
				try
					{
					mySocket.receive (theDatagram);
					done = true;
					}
				/*
				** Modified by etf2954
				**
				catch (SocketTimeoutException exc)
				*/
				catch (InterruptedIOException exc)
					{
					}
				}

			// Set packet length and return.
			thePacket.setBufferLength (theDatagram.getLength());
			return myChannelAddress;
			}

		catch (IOException exc)
			{
			closeSocket();
			throw exc;
			}
		}

	/**
	 * Send the given M2MP packet via this channel to the given destination.
	 * <P>
	 * <I>Note:</I> This M2MP channel implementation always sends packets to the
	 * same place, namely the M2MP Router, and does not distinguish among
	 * destinations. <TT>theDestination</TT> is ignored.
	 *
	 * @param  thePacket       M2MP packet to be sent to the broadcast medium.
	 * @param  theDestination  Ignored.
	 *
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
		openSocketIfNecessary();
		try
			{
			myLastSendTime = System.currentTimeMillis();
			/*
			** Modified by etf2954
			**
			DatagramPacket theDatagram =
				new DatagramPacket
					(thePacket.getBuffer(),
					 0,
					 thePacket.getBufferLength(),
					 mySendAddress);
			*/
			DatagramPacket theDatagram =
				new DatagramPacket
					(thePacket.getBuffer(),
					 0,
					 thePacket.getBufferLength(),
					 mySendAddress.getAddress(),
					 mySendAddress.getPort());
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
				mySocket =
					new DatagramSocket
						(new InetSocketAddress
							("127.0.0.1",
							 0 /*pick an unused port*));
				*/
				InetSocketAddress inet = 
				   new InetSocketAddress( 
					    InetAddress.getByName("127.0.0.1"),
					    0 /*pick an unused port*/);
				mySocket =
					new DatagramSocket
						( inet.getPort(),
						  inet.getAddress() ) ;

				myLastSendTime = 0L;
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
