//******************************************************************************
//
// File:    MessageOutputStream.java
// Package: edu.rit.m2mp
// Unit:    Class edu.rit.m2mp.MessageOutputStream
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

import edu.rit.m2mp.packet.OutgoingPacket;
import edu.rit.m2mp.packet.PacketPool;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Class MessageOutputStream encapsulates the output stream used to transmit an
 * outgoing Many-to-Many Protocol (M2MP) message.
 * <P>
 * <B>M2MP Library Version 02-Jul-2002</B>
 *
 * @author  Alan Kaminsky
 * @version 23-Oct-2001
 */
class MessageOutputStream
	extends OutputStream
	{

// Hidden data members.

	private Channel myChannel;
	private ChannelAddress myDestination;
	private PacketPool myPacketPool;
	private OutgoingPacket myPacket;
	private int myFragmentCount;

// Hidden constructors.

	/**
	 * Create a new message output stream.
	 *
	 * @param  theChannel      M2MP channel to which this message will go.
	 * @param  theDestination  Destination to which this message will go.
	 * @param  thePacketPool   Packet pool from which to allocate an outgoing
	 *                         packet.
	 * @param  theMessageID    Message ID.
	 *
	 * @exception  IOException
	 *     Thrown if an I/O error occurred.
	 */
	MessageOutputStream
		(Channel theChannel,
		 ChannelAddress theDestination,
		 PacketPool thePacketPool,
		 int theMessageID)
		throws IOException
		{
		myChannel = theChannel;
		myDestination = theDestination;
		myPacketPool = thePacketPool;
		myPacket = thePacketPool.allocateOutgoingPacket();
		myFragmentCount = 0;
		myPacket.reset();
		myPacket.setMessageID (theMessageID);
		}

// Exported operations inherited and overridden from class OutputStream.

	/**
	 * Write a byte to this message output stream.
	 *
	 * @param  b  Byte to write. The least-significant 8 bits are written.
	 *
	 * @exception  IOException
	 *     Thrown if an I/O error occurred.
	 */
	public synchronized void write
		(int b)
		throws IOException
		{
		if (myPacket == null)
			{
			throw new IOException ("Stream closed");
			}
		if (myPacket.getAvailable() == 0)
			{
			sendDatagram (false);
			}
		myPacket.write (b);
		}

	/**
	 * Write a byte array to this message output stream.
	 *
	 * @param  b  Byte array to write.
	 *
	 * @exception  NullPointerException
	 *     (unchecked exception) Thrown if <TT>b</TT> is null.
	 * @exception  IOException
	 *     Thrown if an I/O error occurred.
	 */
	public synchronized void write
		(byte[] b)
		throws IOException
		{
		write (b, 0, b.length);
		}

	/**
	 * Write a portion of a byte array to this message output stream.
	 *
	 * @param  b    Byte array to write.
	 * @param  off  Index of first byte to write.
	 * @param  len  Number of bytes to write.
	 *
	 * @exception  NullPointerException
	 *     (unchecked exception) Thrown if <TT>b</TT> is null.
	 * @exception  IndexOutOfBoundsException
	 *     (unchecked exception) Thrown if <TT>off</TT> is less than 0,
	 *     <TT>len</TT> is less than 0, or <TT>off+len</TT> is greater than the
	 *     length of <TT>b</TT>.
	 * @exception  IOException
	 *     Thrown if an I/O error occurred.
	 */
	public synchronized void write
		(byte[] b,
		 int off,
		 int len)
		throws IOException
		{
		int n;
		if (off < 0 || len < 0 || off+len > b.length)
			{
			throw new IndexOutOfBoundsException();
			}
		if (myPacket == null)
			{
			throw new IOException ("Stream closed");
			}
		while (len > 0)
			{
			if (myPacket.getAvailable() == 0)
				{
				sendDatagram (false);
				}
			n = Math.min (len, myPacket.getAvailable());
			myPacket.write (b, off, n);
			off += n;
			len -= n;
			}
		}

	/**
	 * Flush this message output stream.
	 *
	 * @exception  IOException
	 *     Thrown if an I/O error occurred.
	 */
	public synchronized void flush()
		throws IOException
		{
		if (myPacket == null)
			{
			throw new IOException ("Stream closed");
			}
		}

	/**
	 * Close this message output stream.
	 *
	 * @exception  IOException
	 *     Thrown if an I/O error occurred.
	 */
	public synchronized void close()
		throws IOException
		{
		if (myPacket == null)
			{
			throw new IOException ("Stream closed");
			}
		sendDatagram (true);
		doClose();
		}

// Hidden operations.

	/**
	 * Send the accumulated datagram.
	 */
	private void sendDatagram
		(boolean isLastFragment)
		throws IOException
		{
		try
			{
			myPacket.setFragmentNumber (myFragmentCount);
			if (isLastFragment)
				{
				myPacket.setLastPacket();
				}
			myPacket.setChecksum();
			myChannel.transmitPacket (myPacket, myDestination);
			myPacket.reset();
			++ myFragmentCount;
			}

		// If there was an error sending the datagram, close the stream.
		catch (IOException exc)
			{
			doClose();
			throw exc;
			}
		}

	/**
	 * Perform common actions when closing the stream. Assumes the stream is
	 * still open.
	 */
	private void doClose()
		{
		myPacketPool.release (myPacket);
		myPacket = null;
		}

// Hidden operations inherited and overridden from class Object.

	/**
	 * Finalize this message output stream. The output stream is closed if it is
	 * not closed already.
	 */
	protected void finalize()
		{
		if (myPacket != null)
			{
			doClose();
			}
		}

	}
