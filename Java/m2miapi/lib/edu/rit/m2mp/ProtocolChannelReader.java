//******************************************************************************
//
// File:    ProtocolChannelReader.java
// Package: edu.rit.m2mp
// Unit:    Class edu.rit.m2mp.ProtocolChannelReader
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

import edu.rit.m2mp.packet.IncomingPacket;
import edu.rit.m2mp.packet.PacketPool;

import java.io.IOException;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Timer;
import java.util.Vector;

/**
 * Class ProtocolChannelReader is a thread that receives incoming packets from a
 * {@link Channel </CODE>Channel<CODE>} and processes them. Using the {@link
 * Protocol </CODE>Protocol<CODE>} object's {@link MessageSieve
 * </CODE>MessageSieve<CODE>}, it finds {@link IncomingMessageNotifier
 * </CODE>IncomingMessageNotifier<CODE>}s that match incoming messages and
 * obtains {@link MessageInputStream </CODE>MessageInputStream<CODE>}s from the
 * matching incoming message notifiers. For each incoming message, it
 * encapsulates the message input streams in a {@link ProtocolPacketRouter
 * </CODE>ProtocolPacketRouter<CODE>} and routes each incoming packet to the
 * proper protocol packet router, which replicates the packet and forwards the
 * replicas to the message input streams.
 * <P>
 * <B>M2MP Library Version 02-Jul-2002</B>
 *
 * @author  Alan Kaminsky
 * @version 28-May-2002
 */
class ProtocolChannelReader
	extends Thread
	{

// Hidden data members.

	/**
	 * Channel for receiving incoming packets.
	 */
	private Channel myChannel;

	/**
	 * Packet pool for allocating incoming packets.
	 */
	private PacketPool myPacketPool;

	/**
	 * Message sieve for matching incoming messages to incoming message
	 * notifiers.
	 */
	private MessageSieve mySieve;

	/**
	 * Timer that will trigger this protocol channel reader's timed actions.
	 */
	private Timer myTimer;

	/**
	 * Inter-packet timeout interval (milliseconds).
	 */
	private long myTimeoutInterval;

	/**
	 * A mapping from an incoming message&#146;s message ID (type Integer) to
	 * the protocol packet router (type ProtocolPacketRouter) that is handling
	 * the message.
	 */
	private HashMap myMessageMap = new HashMap();

// Exported constructors.

	/**
	 * Construct a new protocol channel reader.
	 *
	 * @param  theChannel
	 *     Channel for receiving incoming packets.
	 * @param  thePacketPool
	 *     Packet pool for allocating incoming packets.
	 * @param  theSieve
	 *     Message sieve for matching incoming messages to incoming message
	 *     notifiers.
	 * @param  theTimer
	 *     Timer that will trigger this protocol channel listener's timed
	 *     actions.
	 * @param  theTimeoutInterval
	 *     Inter-packet timeout interval (milliseconds).
	 */
	public ProtocolChannelReader
		(Channel theChannel,
		 PacketPool thePacketPool,
		 MessageSieve theSieve,
		 Timer theTimer,
		 long theTimeoutInterval)
		{
		myChannel = theChannel;
		myPacketPool = thePacketPool;
		mySieve = theSieve;
		myTimer = theTimer;
		myTimeoutInterval = theTimeoutInterval;
		setDaemon (true);
		start();
		}

// Exported operations.

	/**
	 * Perform this protocol channel reader thread's processing.
	 */
	public void run()
		{
		try
			{
			for (;;)
				{
				IncomingPacket thePacket = myPacketPool.waitForIncomingPacket();
				try
					{
					ChannelAddress theSource =
						myChannel.receivePacket (thePacket);
					processPacket (thePacket, theSource);
					}
				catch (IOException exc)
					{
					System.err.println
						("ProtocolChannelReader: IOException, continuing");
					exc.printStackTrace (System.err);
					myPacketPool.release (thePacket);
					sleep (1000L);
					}
				}
			}
		catch (Throwable exc)
			{
			System.err.println
				("ProtocolChannelReader: Uncaught exception, stopping");
			exc.printStackTrace (System.err);
			}
		}

// Hidden operations.

	/**
	 * Process an incoming M2MP packet.
	 *
	 * @param  thePacket  M2MP packet to process.
	 * @param  theSource  Channel address of the entity that sent
	 *                    <TT>thePacket</TT>.
	 */
	private void processPacket
		(IncomingPacket thePacket,
		 ChannelAddress theSource)
		{
		if (thePacket.verifyChecksum())
			{
			int n = thePacket.getFragmentNumber();
			ProtocolPacketRouter theRouter = getRouter (thePacket);
			if (n == 0)
				{
				if (theRouter == null)
					{
					// First packet of a new message.
					processFirstPacket (thePacket, theSource);
					}
				else
					{
					// First packet of a new message with the same message ID as
					// a message in progress. We have no way to distinguish
					// packets between the two messages. Abort the message in
					// progress, ignore the new message.
					theRouter.abort
						("Multiple messages with the same message ID");
					myPacketPool.release (thePacket);
					}
				}
			else
				{
				if (theRouter == null)
					{
					// Subsequent packet of a message no one is receiving.
					// Ignore the packet.
					myPacketPool.release (thePacket);
					}
				else
					{
					// Subsequent packet of a message someone is receiving.
					theRouter.processPacket (thePacket);
					}
				}
			}

		// Ignore packets with bad checksums.
		else
			{
			myPacketPool.release (thePacket);
			}
		}

	/**
	 * Returns the protocol packet router that is handling the given packet's
	 * message ID, or null if no one is receiving the given packet's message ID.
	 */
	private ProtocolPacketRouter getRouter
		(IncomingPacket thePacket)
		{
		synchronized (myMessageMap)
			{
			return (ProtocolPacketRouter)
				myMessageMap.get (new Integer (thePacket.getMessageID()));
			}
		}

	/**
	 * Process the first packet of an incoming M2MP message.
	 *
	 * @param  thePacket  M2MP packet to process.
	 * @param  theSource  Channel address of the entity that sent
	 *                    <TT>thePacket</TT>.
	 */
	private void processFirstPacket
		(IncomingPacket thePacket,
		 ChannelAddress theSource)
		{
		ProtocolPacketRouter theRouter = null;

		// Get an iterator of all the incoming message notifiers that match the
		// packet.
		Iterator iter = mySieve.getMatches
			(thePacket.getBuffer(),
			 thePacket.getStartOffset(),
			 thePacket.getFinishOffset(),
			 theSource);

		// Check for matches.
		if (iter.hasNext())
			{
			// There are matches. Create protocol packet router.
			theRouter = new ProtocolPacketRouter
				(myPacketPool,
				 myMessageMap,
				 new Integer (thePacket.getMessageID()),
				 myTimer,
				 myTimeoutInterval);

			// For each matching incoming message notifier, get a message input
			// stream and add it to the protocol packet router.
			while (iter.hasNext())
				{
				IncomingMessageNotifier theNotifier =
					(IncomingMessageNotifier) iter.next();
				MessageInputStream theStream =
					theNotifier.createIncomingMessage (theSource);
				theRouter.addInputStream (theStream);
				}
			}

		// If there were any matching incoming mesage notifiers, deliver the
		// packet to the message input streams, otherwise ignore the packet.
		if (theRouter != null)
			{
			theRouter.processPacket (thePacket);
			}
		else
			{
			myPacketPool.release (thePacket);
			}
		}

	}
