//******************************************************************************
//
// File:    ProtocolPacketRouter.java
// Package: edu.rit.m2mp
// Unit:    Class edu.rit.m2mp.ProtocolPacketRouter
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
import edu.rit.m2mp.packet.OutOfPacketsException;
import edu.rit.m2mp.packet.PacketPool;

import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

/**
 * Class ProtocolPacketRouter encapsulates an object that routes incoming
 * packets received from a {@link Channel </CODE>Channel<CODE>} to the {@link
 * MessageInputStream </CODE>MessageInputStream<CODE>} or streams that are
 * reading the message contents. The protocol packet router does an interpacket
 * timeout on multi-packet messages and times out the message if the interval
 * between two successive packets exceeds the timeout interval. The protocol
 * packet router also deals with packets received out of order &#151; by
 * discarding them, which may then cause a message to time out.
 * <P>
 * M2MP is designed this way assuming the communication medium is mostly
 * reliable but not totally reliable; that is, most of the time, packets are not
 * lost or reordered. So if a packet is occasionally lost or received out of
 * order, it&#146;s simpler for the protocol to abort the message than to try to
 * recover from the error.
 * <P>
 * <B>M2MP Library Version 02-Jul-2002</B>
 *
 * @author  Alan Kaminsky
 * @version 28-May-2002
 */
class ProtocolPacketRouter
	{

// Hidden data members.

	/**
	 * Packet pool from which to allocate incoming packets.
	 */
	private PacketPool myPacketPool;

	/**
	 * Message map that contains this protocol packet router.
	 */
	private Map myMessageMap;

	/**
	 * Message ID (key) which maps to this protocol packet router in
	 * myMessageMap.
	 */
	private Integer myKey;

	/**
	 * Timer with which to schedule timer tasks.
	 */
	private Timer myTimer;

	/**
	 * Inter-packet timeout interval (milliseconds).
	 */
	private long myTimeoutInterval;

	/**
	 * Timer task doing the inter-packet timeout, or null if there is no
	 * timeout.
	 */
	private TimerTask myTimerTask = null;

	/**
	 * List of one or more message input streams reading the contents of this
	 * message.
	 */
	private Vector myInputStreams = new Vector();

	/**
	 * Fragment number of the next fragment expected.
	 */
	private int myFragmentNumber = 0;

// Exported constructors.

	/**
	 * Construct a new M2MP protocol packet router.
	 *
	 * @param  thePacketPool
	 *     Packet pool from which to allocate incoming packets.
	 * @param  theMessageMap
	 *     Message map to which to add this protocol packet router.
	 * @param  theKey
	 *     Message ID (key) which maps to this protocol packet router in
	 *     theMessageMap.
	 * @param  theTimer
	 *     Timer that will trigger this protocol packet router's timed actions.
	 * @param  theTimeoutInterval
	 *     Inter-packet timeout interval (milliseconds).
	 */
	public ProtocolPacketRouter
		(PacketPool thePacketPool,
		 Map theMessageMap,
		 Integer theKey,
		 Timer theTimer,
		 long theTimeoutInterval)
		{
		myPacketPool = thePacketPool;
		myMessageMap = theMessageMap;
		myKey = theKey;
		synchronized (theMessageMap)
			{
			theMessageMap.put (theKey, this);
			}
		myTimer = theTimer;
		myTimeoutInterval = theTimeoutInterval;
		}

// Exported operations.

	/**
	 * Add the given message input stream to this M2MP protocol packet router.
	 *
	 * @param  theInputStream  Message input stream.
	 */
	public synchronized void addInputStream
		(MessageInputStream theInputStream)
		{
		myInputStreams.add (theInputStream);
		}

	/**
	 * Process the given incoming M2MP packet.
	 *
	 * @param  thePacket  Incoming packet.
	 */
	public synchronized void processPacket
		(IncomingPacket thePacket)
		{
		// If it's the expected next packet,
		if (thePacket.getFragmentNumber() == myFragmentNumber)
			{
			// Stop the timeout in progress if any.
			if (myTimerTask != null)
				{
				myTimerTask.cancel();
				myTimerTask = null;
				}

			// Remember whether this is the last packet. (Once we hand the
			// packet off to the message input stream, another thread might read
			// the packet and discard the message buffer, which contains the
			// last-packet flag.)
			boolean isLast = thePacket.isLastPacket();

			// Make copies of the packet and send them to all the message input
			// streams. For the last message input stream, the original packet
			// is sent rather than a copy.
			int n = myInputStreams.size();
			for (int i = 0; i < n; ++ i)
				{
				MessageInputStream theStream = (MessageInputStream)
					myInputStreams.elementAt (i);
				IncomingPacket copyPacket;
				try
					{
					if (i < n-1)
						{
						copyPacket = myPacketPool.allocateIncomingPacket();
						copyPacket.copy (thePacket);
						}
					else
						{
						copyPacket = thePacket;
						}
					theStream.enqueue (copyPacket);
					}
				catch (OutOfPacketsException exc)
					{
					theStream.notifyIOError ("Cannot allocate packet");
					}
				}

			// If no more packets are expected, remove this protocol packet
			// router from the message map.
			if (isLast)
				{
				synchronized (myMessageMap)
					{
					myMessageMap.remove (myKey);
					}
				}

			// If more packets are expected, update the fragment number and
			// start the inter-packet timeout.
			else
				{
				++ myFragmentNumber;
				myTimer.schedule
					(new TimerTask()
						{
						public void run()
							{
							abort ("Message timed out");
							}
						},
					 myTimeoutInterval);
				}
			}

		// If it's not the expected next packet, ignore it.
		else
			{
			myPacketPool.release (thePacket);
			}
		}

	/**
	 * Abort all this protocol packet router's message input streams with an I/O
	 * error.
	 *
	 * @param  theIOError  String denoting the I/O error.
	 */
	public synchronized void abort
		(String theIOError)
		{
		// Notify all the message input streams of the I/O error.
		int n = myInputStreams.size();
		for (int i = 0; i < n; ++ i)
			{
			((MessageInputStream) myInputStreams.elementAt (i))
				.notifyIOError (theIOError);
			}

		// Remove this protocol packet router from the message map.
		synchronized (myMessageMap)
			{
			myMessageMap.remove (myKey);
			}
		}

	}
