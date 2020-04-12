//******************************************************************************
//
// File:    Protocol.java
// Package: edu.rit.m2mp
// Unit:    Class edu.rit.m2mp.Protocol
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

package edu.rit.m2mp;

import edu.rit.m2mp.packet.PacketPool;

import java.util.Timer;

/**
 * Class Protocol encapsulates the Many-to-Many Protocol (M2MP). To use M2MP:
 * <OL TYPE=1>
 * <P><LI>
 * Create an M2MP channel object (an instance of a class that implements
 * interface {@link Channel </CODE>Channel<CODE>}) representing the underlying
 * communication medium.
 * <P><LI>
 * Create an instance of class Protocol, specifying the M2MP channel object.
 * <P><LI>
 * To send an outgoing M2MP message, call {@link
 * #createOutgoingMessage(ChannelAddress) createOutgoingMessage()} to create an
 * outgoing message object. You can create as many outgoing message objects as
 * you want. See class {@link OutgoingMessage </CODE>OutgoingMessage<CODE>} for
 * further instructions.
 * <P><LI>
 * To receive incoming M2MP messages, call {@link
 * #createIncomingMessageNotifier() createIncomingMessageNotifier()} to create
 * an incoming message notifier object. You can create as many incoming message
 * notifier objects as you want. See class {@link IncomingMessageNotifier
 * </CODE>IncomingMessageNotifier<CODE>} for further instructions.
 * </OL>
 * <P>
 * <B>M2MP Library Version 02-Jul-2002</B>
 *
 * @author  Alan Kaminsky
 * @version 28-May-2002
 */
public class Protocol
	{

// Hidden data members.

	/**
	 * M2MP channel to which to send outgoing packets and from which to receive
	 * incoming packets.
	 */
	private Channel myChannel;

	/**
	 * Generator of message IDs for outgoing messages.
	 */
	private Crsg1 myMessageIDGenerator;

	/**
	 * Packet pool for allocating incoming and outgoing packets.
	 */
	private PacketPool myPacketPool;

	/**
	 * Message sieve for matching incoming messages to incoming message
	 * notifiers.
	 */
	private MessageSieve mySieve;

	/**
	 * Maximum time interval between reception of sequential packets in a
	 * message (milliseconds).
	 */
	private long myTimeoutInterval;

	/**
	 * Timer used by the protocol channel listener.
	 */
	private Timer myTimer;

	/**
	 * Protocol channel reader.
	 */
	private ProtocolChannelReader myReader;

// Exported constructors.

	/**
	 * Construct a new M2MP protocol instance. This protocol instance will use
	 * the given M2MP channel to send and receive messages. The sequence
	 * generator for message IDs is seeded with the given key values; for
	 * example, these could be a unique device identifier like an Ethernet MAC
	 * address and a unique timestamp like <TT>System.currentTimeMillis()</TT>.
	 * The maximum number of packet buffers that will be allocated is specified
	 * so as not to consume unbounded storage. The inter-packet timeout interval
	 * &#151; the maximum time interval permitted between reception of
	 * sequential packets in a message &#151; is specified; if a message times
	 * out, an I/O error is reported for that message.
	 *
	 * @param  theChannel
	 *     M2MP channel.
	 * @param  theKey1
	 *     First key value.
	 * @param  theKey2
	 *     Second key value.
	 * @param  theBufferCount
	 *     Maximum number of packet buffers that will be allocated for use by
	 *     this M2MP protocol instance.
	 * @param  theTimeoutInterval
	 *     Inter-packet timeout interval (milliseconds).
	 *
	 * @exception  NullPointerException
	 *     (unchecked exception) Thrown if <TT>theChannel</TT> is null.
	 * @exception  IllegalArgumentException
	 *     (unchecked exception) Thrown if <TT>theBufferCount</TT> is less than
	 *     or equal to 0. Thrown if <TT>theTimeoutInterval</TT> is less than or
	 *     equal to 0.
	 */
	public Protocol
		(Channel theChannel,
		 long theKey1,
		 long theKey2,
		 int theBufferCount,
		 long theTimeoutInterval)
		{
		// Verify preconditions.
		if (theChannel == null)
			{
			throw new NullPointerException();
			}
		if (theBufferCount <= 0 || theTimeoutInterval <= 0L)
			{
			throw new IllegalArgumentException();
			}

		// Record constructor arguments.
		myChannel = theChannel;
		myMessageIDGenerator = new Crsg1 (theKey1, theKey2);
		myPacketPool = new PacketPool (theBufferCount);
		mySieve = new MessageSieve();
		myTimeoutInterval = theTimeoutInterval;
		myTimer = new Timer (true);
		myReader =
			new ProtocolChannelReader
				(myChannel,
				 myPacketPool,
				 mySieve,
				 myTimer,
				 myTimeoutInterval);
		}

// Exported operations.

	/**
	 * Create an outgoing message to be transmitted to the given destination.
	 * The message is sent via this M2MP protocol instance&#146;s channel.
	 * <P>
	 * <I>Note:</I> Some M2MP channel implementations always send packets to the
	 * same place, such as a broadcast medium. In that case
	 * <TT>theDestination</TT> is ignored. See the channel implementation
	 * documentation for further information.
	 *
	 * @param  theDestination  Where to send the outgoing message.
	 *
	 * @return  Outgoing message.
	 */
	public OutgoingMessage createOutgoingMessage
		(ChannelAddress theDestination)
		{
		int theMessageID;
		synchronized (myMessageIDGenerator)
			{
			theMessageID = myMessageIDGenerator.next();
			}
		return new OutgoingMessage
			(myChannel,
			 theDestination,
			 myPacketPool,
			 theMessageID);
		}

	/**
	 * Create an incoming message notifier. Messages will be received via this
	 * M2MP protocol instance&#146;s channel.
	 *
	 * @return  Incoming message notifier.
	 */
	public IncomingMessageNotifier createIncomingMessageNotifier()
		{
		return new IncomingMessageNotifier (myPacketPool, mySieve);
		}

	/**
	 * Returns this M2MP protocol instance&#146;s interpacket timeout interval
	 * (milliseconds).
	 */
	public long getTimeoutInterval()
		{
		return myTimeoutInterval;
		}

	/**
	 * Returns this M2MP protocol instance&#146;s timer. Clients can use this
	 * timer if desired to perform timer tasks.
	 */
	public Timer getTimer()
		{
		return myTimer;
		}

	}
