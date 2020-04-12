//******************************************************************************
//
// File:    IPMulticastChannel.java
// Package: edu.rit.m2mp.ip
// Unit:    Interface edu.rit.m2mp.ip.IPMulticastChannel
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
import java.net.InetAddress;
/*
** Removed by etf2954
**
import java.net.InetSocketAddress;
*/
import java.net.MulticastSocket;

/*
** Removed by etf2954
**
import java.net.SocketTimeoutException;
*/

import java.util.Iterator;

/*
** Modified by etf2954
**
import java.util.LinkedHashMap;
*/
import java.util.HashMap ;

/**
 * Class IPMulticastChannel encapsulates a broadcast {@link edu.rit.m2mp.Channel
 * </CODE>Channel<CODE>} in the Many-to-Many Protocol (M2MP) that uses UDP
 * multicast. All M2MP packets are conveyed as UDP datagrams sent to and
 * received from a specified IP multicast address and UDP port number. Every
 * IPMulticastChannel on this IP address and port will receive every M2MP packet
 * transmitted by every other IPMulticastChannel on this IP address and port,
 * effectively implementing a broadcast communication medium.
 * <P>
 * Because of the way multicasting works, when an IPMulticastChannel's outgoing
 * side sends a packet, its own incoming side may or may not receive that
 * packet. (This behavior depends on the TCP/IP protocol stack's implementation
 * and configuration and has been observed to vary from system to system.)
 * However, an M2MP channel is required not to receive its own outgoing packets.
 * Therefore, the channel looks for incoming packets that were sent by itself
 * and does not pass them on up the protocol stack. Packets sent by other
 * channels are passed on up. (Specifically, the channel keeps a set of the
 * message IDs of the outgoing messages, and if an incoming message has one of
 * those message IDs, the message ID is removed from the set and the incoming
 * message is not passed on up. A message ID is also removed from the set if no
 * incoming message with that ID shows up within 2000 milliseconds.)
 * <P>
 * <B>M2MP Library Version 02-Jul-2002</B>
 *
 * @author  Alan Kaminsky
 * @version 04-Jun-2002
 */
public class IPMulticastChannel
	implements Channel
	{

// Hidden constants.

	// Timeout for removing stale message IDs (milliseconds).
	private static final int TIMEOUT = 2000;

// Hidden data members.

	private IPChannelAddress myChannelAddress;
	private InetAddress myInterface;
	private int myTTL;

	private InetSocketAddress mySendAddress;

	private MulticastSocket mySocket = null;

	// Map from message ID (type Integer) that has been sent by this channel but
	// not yet fully received by this channel, to the time (type Long) at which
	// the outgoing message was sent.
	private HashMap myOutgoingMessageIDs = new HashMap();

// Exported constructors.

	/**
	 * Construct a new IP multicast channel attached to the given IP channel
	 * address. The default network interface and the default time to live (1)
	 * are used. The channel address&#146;s IP address must be an IP multicast
	 * address in the range 224.0.0.0 through 239.255.255.255.
	 *
	 * @param  theAddress
	 *     IP channel address.
	 *
	 * @exception  NullPointerException
	 *     (unchecked exception) Thrown if <TT>theAddress</TT> is null.
	 */
	public IPMulticastChannel
		(IPChannelAddress theAddress)
		{
		this (theAddress, null, 1);
		}

	/**
	 * Construct a new IP multicast channel attached to the given IP channel
	 * address using the given network interface and the given time to live. The
	 * channel address&#146;s IP address must be an IP multicast address in the
	 * range 224.0.0.0 through 239.255.255.255.
	 *
	 * @param  theAddress
	 *     IP channel address.
	 * @param  theInterface
	 *     IP address of the network interface through which this channel&#146;s
	 *     network traffic should flow; or null to use the default network
	 *     interface. Set this argument to null unless you&#146;re running on a
	 *     multihomed host and you need to use a specific network interface.
	 * @param  theTTL
	 *     Time to live value for this channel&#146;s multicast IP datagrams.
	 *     Set this argument to 1 unless your multicast network spans multiple
	 *     segments connected by routers.
	 *
	 * @exception  NullPointerException
	 *     (unchecked exception) Thrown if <TT>theAddress</TT> is null.
	 * @exception  IllegalArgumentException
	 *     (unchecked exception) Thrown if <TT>theTTL</TT> is not in the range 1
	 *     .. 255.
	 */
	public IPMulticastChannel
		(IPChannelAddress theAddress,
		 InetAddress theInterface,
		 int theTTL)
		{
		if (theAddress == null)
			{
			throw new NullPointerException();
			}
		if (1 > theTTL || theTTL > 255)
			{
			throw new IllegalArgumentException();
			}
		myChannelAddress = theAddress;
		myInterface = theInterface;
		myTTL = theTTL;
		mySendAddress = myChannelAddress.getAddress();
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
	 * from the same place, namely the broadcast medium, and does not
	 * distinguish among sources. The returned source address is this
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
			byte[] buf = thePacket.getBuffer();
			boolean done = false;

			while (! done)
				{
				DatagramPacket theDatagram =
					new DatagramPacket (buf, 0, buf.length);
				try
					{
					mySocket.receive (theDatagram);
					thePacket.setBufferLength (theDatagram.getLength());
					Integer theMessageID =
						new Integer (thePacket.getMessageID());
					synchronized (myOutgoingMessageIDs)
						{
						if (! myOutgoingMessageIDs.containsKey (theMessageID))
							{
							done = true;
							}
						else if (thePacket.isLastPacket())
							{
							myOutgoingMessageIDs.remove (theMessageID);
							}
						}
					}
				/*
				** Modified by etf2954
				**
				catch (SocketTimeoutException exc)
				*/
				catch (InterruptedIOException exc)
					{
					}

				synchronized (myOutgoingMessageIDs)
					{
					long now = System.currentTimeMillis();
					Iterator iter = myOutgoingMessageIDs.values().iterator();
					while
						(iter.hasNext() &&
						 now - ((Long) (iter.next())).longValue() > TIMEOUT)
						{
						iter.remove();
						}
					}
				}

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
	 * same place, namely the broadcast medium, and does not distinguish among
	 * destinations. <TT>theDestination</TT> is ignored.
	 *
	 * @param  thePacket       M2MP packet to be sent to
	 *                         <TT>theDestination</TT>.
	 * @param  theDestination  Where to send <TT>thePacket</TT> (ignored).
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
			if (thePacket.getFragmentNumber() == 0)
				{
				synchronized (myOutgoingMessageIDs)
					{
					myOutgoingMessageIDs.put
						(new Integer (thePacket.getMessageID()),
						 new Long (System.currentTimeMillis()));
					}
				}
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
	 * Create mySocket if necessary and set its interface and TTL.
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
				mySocket = new MulticastSocket (mySendAddress.getPort());
				mySocket.setTimeToLive (myTTL);
				mySocket.setSoTimeout (TIMEOUT);
				if (myInterface != null)
					{
					mySocket.setInterface (myInterface);
					}
				mySocket.joinGroup (mySendAddress.getAddress());
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
			try
				{
				mySocket.leaveGroup (mySendAddress.getAddress());
				}
			catch (IOException exc)
				{
				}
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
