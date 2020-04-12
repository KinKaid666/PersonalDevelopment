//******************************************************************************
//
// File:    PacketPool.java
// Package: edu.rit.m2mp.packet
// Unit:    Class edu.rit.m2mp.packet.PacketPool
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

package edu.rit.m2mp.packet;

/**
 * Class PacketPool encapsulates a pool of packets in the Many-to-Many Protocol
 * (M2MP). Instances of class {@link IncomingPacket </CODE>IncomingPacket<CODE>}
 * and {@link OutgoingPacket </CODE>OutgoingPacket<CODE>} are allocated from a
 * packet pool, then are released back to the packet pool when no longer needed.
 * To prevent excessive storage consumption in a small device, the packet pool
 * enforces an upper bound on the number of packets that can be allocated
 * simultaneously. By reusing already-existing packets, the packet pool reduces
 * the overhead that would otherwise be required to allocate fresh packets from
 * the heap and garbage collect them afterwards.
 * <P>
 * Any of the following may cause the packet pool to run out of packets:
 * <UL>
 * <P><LI>
 * Failing to close the output stream when done writing an outgoing message,
 * including if an exception is thrown while writing.
 * <P><LI>
 * Failing to close the input stream when done reading an incoming message,
 * including if an exception is thrown while reading.
 * <P><LI>
 * Failing to accept and read incoming messages from an open incoming message
 * notifier.
 * <P><LI>
 * Failing to close an incoming message notifier when no longer accepting
 * messages from it.
 * <P><LI>
 * Sending outgoing messages at a faster rate than receiving incoming messages.
 * This can happen if the sending thread doesn't let the receiving thread have
 * enough processor time.
 * </UL>
 * <P>
 * When the packet pool runs out of packets, incoming messages will no longer be
 * received, and creating an outgoing message will throw an {@link
 * OutOfPacketsException </CODE>OutOfPacketsException<CODE>}. Make sure your
 * M2MP application is designed properly and does not get into the above
 * situations.
 * <P>
 * <I>Note:</I> Class PacketPool is multiple thread safe.
 * <P>
 * <B>M2MP Library Version 02-Jul-2002</B>
 *
 * @author  Alan Kaminsky
 * @version 01-Jul-2002
 */
public class PacketPool
	{

// Hidden data members.

	/**
	 * Maximum number of byte buffers that can be allocated.
	 */
	int myMaxCount;

	/**
	 * Number of byte buffers that have actually been allocated.
	 */
	int myAllocatedCount;

	/**
	 * Array of available byte buffers.
	 */
	byte[][] myBuffers;

	/**
	 * Number of available byte buffers.
	 */
	int myBufferCount;

	/**
	 * Array of available incoming packets.
	 */
	IncomingPacket[] myIncomingPackets;

	/**
	 * Number of available incoming packets.
	 */
	int myIncomingPacketCount;

	/**
	 * Array of available outgoing packets.
	 */
	OutgoingPacket[] myOutgoingPackets;

	/**
	 * Number of available outgoing packets.
	 */
	int myOutgoingPacketCount;

// Exported constructors.

	/**
	 * Construct a new packet pool.
	 *
	 * @param  theMaxCount  Maximum number of byte buffers that can be allocated
	 *                      from this packet pool.
	 *
	 * @exception  IllegalArgumentException
	 *     (unchecked exception) Thrown if <TT>theMaxCount</TT> is less than or
	 *     equal to 0.
	 */
	public PacketPool
		(int theMaxCount)
		{
		if (theMaxCount <= 0)
			{
			throw new IllegalArgumentException();
			}
		myMaxCount = theMaxCount;
		myAllocatedCount = 0;
		myBuffers = new byte [theMaxCount] [];
		myBufferCount = 0;
		myIncomingPackets = new IncomingPacket [theMaxCount];
		myIncomingPacketCount = 0;
		myOutgoingPackets = new OutgoingPacket [theMaxCount];
		myOutgoingPacketCount = 0;
		}

// Exported operations.

	/**
	 * Allocate an incoming packet from this packet pool (non-blocking version).
	 * If some of this packet pool&#146;s incoming packets are not in use, this
	 * method returns a free incoming packet. If all of this packet pool&#146;s
	 * incoming packets are in use, this method throws an OutOfPacketsException.
	 *
	 * @return  Incoming packet.
	 *
	 * @exception  OutOfPacketsException
	 *     Thrown if all of this packet pool&#146;s incoming packets are in use.
	 */
	public synchronized IncomingPacket allocateIncomingPacket()
		throws OutOfPacketsException
		{
		return allocateIncomingPacket (allocateBuffer());
		}

	/**
	 * Allocate an incoming packet from this packet pool (blocking version). If
	 * some of this packet pool&#146;s incoming packets are not in use, this
	 * method returns a free incoming packet. If all of this packet pool&#146;s
	 * incoming packets are in use, this method blocks until an incoming packet
	 * becomes available, then returns it.
	 *
	 * @return  Incoming packet.
	 *
	 * @exception  InterruptedException
	 *     Thrown if the calling thread was interrupted while blocked in this
	 *     method.
	 */
	public synchronized IncomingPacket waitForIncomingPacket()
		throws InterruptedException
		{
		return allocateIncomingPacket (waitForBuffer());
		}

	/**
	 * Release the given incoming packet back to this packet pool. Assumes
	 * <TT>thePacket</TT> was originally allocated from this packet pool.
	 * <P>
	 * <B><I>Warning:</I></B> After releasing it, do not use <TT>thePacket</TT>
	 * ever again. Instead, allocate a new incoming packet.
	 *
	 * @param  thePacket  Incoming packet.
	 */
	public synchronized void release
		(IncomingPacket thePacket)
		{
		// Release incoming packet.
		myIncomingPackets[myIncomingPacketCount++] = thePacket;

		// Release byte buffer.
		release (thePacket.myBuffer);
		thePacket.myBuffer = null;
		}

	/**
	 * Allocate an outgoing packet from this packet pool (non-blocking version).
	 * If some of this packet pool&#146;s outgoing packets are not in use, this
	 * method returns a free outgoing packet. If all of this packet pool&#146;s
	 * outgoing packets are in use, this method throws an OutOfPacketsException.
	 *
	 * @return  Outgoing packet.
	 *
	 * @exception  OutOfPacketsException
	 *     Thrown if all of this packet pool&#146;s outgoing packets are in use.
	 */
	public synchronized OutgoingPacket allocateOutgoingPacket()
		throws OutOfPacketsException
		{
		return allocateOutgoingPacket (allocateBuffer());
		}

	/**
	 * Allocate an outgoing packet from this packet pool (blocking version). If
	 * some of this packet pool&#146;s outgoing packets are not in use, this
	 * method returns a free outgoing packet. If all of this packet pool&#146;s
	 * outgoing packets are in use, this method blocks until an outgoing packet
	 * becomes available, then returns it.
	 *
	 * @return  Outgoing packet.
	 *
	 * @exception  InterruptedException
	 *     Thrown if the calling thread was interrupted while blocked in this
	 *     method.
	 */
	public synchronized OutgoingPacket waitForOutgoingPacket()
		throws InterruptedException
		{
		return allocateOutgoingPacket (waitForBuffer());
		}

	/**
	 * Release the given outgoing packet back to this packet pool. Assumes
	 * <TT>thePacket</TT> was originally allocated from this packet pool.
	 * <P>
	 * <B><I>Warning:</I></B> After releasing it, do not use <TT>thePacket</TT>
	 * ever again. Instead, allocate a new outgoing packet.
	 *
	 * @param  thePacket  Outgoing packet.
	 */
	public synchronized void release
		(OutgoingPacket thePacket)
		{
		// Release outgoing packet.
		myOutgoingPackets[myOutgoingPacketCount++] = thePacket;

		// Release byte buffer.
		release (thePacket.myBuffer);
		thePacket.myBuffer = null;
		}

// Hidden operations.

	/**
	 * Allocate a byte buffer from this packet pool (non-blocking version).
	 *
	 * @return  Byte buffer.
	 *
	 * @exception  OutOfPacketsException
	 *     Thrown if all of this packet pool&#146;s byte buffers are in use.
	 */
	byte[] allocateBuffer()
		throws OutOfPacketsException
		{
		byte[] theBuffer;
		if (myBufferCount > 0)
			{
			theBuffer = myBuffers[--myBufferCount];
			}
		else if (myAllocatedCount < myMaxCount)
			{
			theBuffer = new byte [Packet.MAX_BUFFER_LENGTH];
			++ myAllocatedCount;
			}
		else
			{
			throw new OutOfPacketsException();
			}
		return theBuffer;
		}

	/**
	 * Allocate a byte buffer from this packet pool (blocking version).
	 *
	 * @return  Byte buffer.
	 *
	 * @exception  InterruptedException
	 *     Thrown if the calling thread was interrupted while blocked in this
	 *     method.
	 */
	byte[] waitForBuffer()
		throws InterruptedException
		{
		byte[] theBuffer;
		if (myBufferCount > 0)
			{
			theBuffer = myBuffers[--myBufferCount];
			}
		else if (myAllocatedCount < myMaxCount)
			{
			theBuffer = new byte [Packet.MAX_BUFFER_LENGTH];
			++ myAllocatedCount;
			}
		else
			{
			while (myBufferCount == 0)
				{
				wait();
				}
			theBuffer = myBuffers[--myBufferCount];
			}
		return theBuffer;
		}

	/**
	 * Release the given byte buffer back to this packet pool. Assumes
	 * <TT>theBuffer</TT> was originally allocated from this packet pool.
	 *
	 * @param  theBuffer  Byte buffer.
	 */
	void release
		(byte[] theBuffer)
		{
		myBuffers[myBufferCount++] = theBuffer;
		notifyAll();
		}

	/**
	 * Allocate an incoming packet from this packet pool using the given byte
	 * buffer.
	 *
	 * @param  theBuffer  Byte buffer to use.
	 *
	 * @return  Incoming packet.
	 */
	IncomingPacket allocateIncomingPacket
		(byte[] theBuffer)
		{
		// Get an incoming packet.
		IncomingPacket thePacket;
		if (myIncomingPacketCount > 0)
			{
			thePacket = myIncomingPackets[--myIncomingPacketCount];
			}
		else
			{
			thePacket = new IncomingPacket();
			}

		// Hook up the incoming packet with the byte buffer.
		thePacket.myBuffer = theBuffer;
		return thePacket;
		}

	/**
	 * Allocate an outgoing packet from this packet pool using the given byte
	 * buffer.
	 *
	 * @param  theBuffer  Byte buffer to use.
	 *
	 * @return  Outgoing packet.
	 */
	OutgoingPacket allocateOutgoingPacket
		(byte[] theBuffer)
		{
		// Get an outgoing packet.
		OutgoingPacket thePacket;
		if (myOutgoingPacketCount > 0)
			{
			thePacket = myOutgoingPackets[--myOutgoingPacketCount];
			}
		else
			{
			thePacket = new OutgoingPacket();
			}

		// Hook up the outgoing packet with the byte buffer.
		thePacket.myBuffer = theBuffer;
		return thePacket;
		}

	}
