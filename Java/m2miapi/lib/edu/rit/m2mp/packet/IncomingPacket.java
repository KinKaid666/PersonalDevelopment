//******************************************************************************
//
// File:    IncomingPacket.java
// Package: edu.rit.m2mp.packet
// Unit:    Class edu.rit.m2mp.packet.IncomingPacket
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
 * Class IncomingPacket encapsulates an incoming packet in the Many-to-Many
 * Protocol (M2MP).
 * <P>
 * To receive a packet:
 * <OL TYPE=1>
 * <LI>
 * Call {@link PacketPool#allocateIncomingPacket()
 * PacketPool.allocateIncomingPacket()} to get an incoming packet object.
 * <LI>
 * Fill in the packet&#146;s byte buffer; either &#151;
 * <OL TYPE=a>
 * <LI>
 * Call {@link Packet#getBuffer() getBuffer()} and pass the packet&#146;s byte
 * buffer to the underlying network layer to be filled in. Call {@link
 * #setBufferLength(int) setBufferLength()} specifying the number of bytes the
 * network layer filled in.
 * <BR>
 * &#151; or &#151;
 * <LI>
 * Call {@link Packet#copy(Packet) copy()} to make a copy of an existing packet.
 * </OL>
 * <LI>
 * Call {@link #verifyChecksum() verifyChecksum()}.
 * <LI>
 * Call {@link Packet#getMessageID() getMessageID()}.
 * <LI>
 * Call {@link Packet#getFragmentNumber() getFragmentNumber()}.
 * <LI>
 * Call {@link Packet#reset() reset()}.
 * <LI>
 * Call {@link #read() read()} to read the message contents. Call {@link
 * Packet#getAvailable() getAvailable()} to check how many bytes are still
 * available to be read.
 * <LI>
 * Call {@link Packet#isLastPacket() isLastPacket()} to check if this packet
 * contains the last fragment of a message.
 * <LI>
 * When done using the packet, call {@link PacketPool#release(IncomingPacket)
 * PacketPool.release()}.
 * </OL>
 * <P>
 * <B><I>Warning:</I></B> Class IncomingPacket is not multiple thread safe.
 * <P>
 * <B>M2MP Library Version 02-Jul-2002</B>
 *
 * @author  Alan Kaminsky
 * @version 30-May-2002
 */
public class IncomingPacket
	extends Packet
	{

// Hidden constructors.

	/**
	 * Construct a new, empty incoming packet.
	 */
	IncomingPacket()
		{
		super();
		}

// Exported operations.

	/**
	 * Set the length of this incoming packet. Typically, the length comes from
	 * the underlying network layer.
	 *
	 * @param  theLength  Number of valid bytes in this packet&#146;s byte
	 *                    buffer.
	 *
	 * @exception  IllegalArgumentException
	 *     (unchecked exception) Thrown if <TT>theLength</TT> is not in the
	 *     range 11 .. 508, the range of valid lengths for an M2MP packet.
	 */
	public void setBufferLength
		(int theLength)
		{
		if (MIN_BUFFER_LENGTH > theLength || theLength > MAX_BUFFER_LENGTH)
			{
			throw new IllegalArgumentException();
			}
		myBufferLength = theLength;
		}

	/**
	 * Verifies whether this packet&#146;s checksum field is correct.
	 *
	 * @return  True if this packet&#146;s checksum field is correct, false
	 *          otherwise.
	 */
	public boolean verifyChecksum()
		{
		int i = myBufferLength - 2;
		return computeChecksum (myBuffer, 0, i) == getShort (myBuffer, i);
		}

	/**
	 * Reads a byte from this packet. Assumes this packet has at least one
	 * unread byte.
	 *
	 * @return  Byte that was read, an integer in the range 0x00 .. 0xFF.
	 */
	public int read()
		{
		return myBuffer[myByteIndex++] & 0xFF;
		}

	/**
	 * Reads a portion of a byte array from this packet. Assumes this packet has
	 * at least <TT>len</TT> unread bytes.
	 *
	 * @param  b    Byte array in which to place the bytes read.
	 * @param  off  Index of first byte placed in <TT>b</TT>.
	 * @param  len  Number of bytes to read.
	 */
	public void read
		(byte[] b,
		 int off,
		 int len)
		{
		System.arraycopy (myBuffer, myByteIndex, b, off, len);
		myByteIndex += len;
		}

	/**
	 * Skips over a number of bytes in this packet. Assumes this packet has at
	 * least <TT>len</TT> unread bytes.
	 *
	 * @param  len  Number of bytes to skip.
	 */
	public void skip
		(int len)
		{
		myByteIndex += len;
		}

	/**
	 * Returns the index within this packet&#146;s buffer of the first message
	 * byte.
	 */
	public int getStartOffset()
		{
		return FRAGMENT_OFFSET;
		}

	/**
	 * Returns the index within this packet&#146;s buffer of the last message
	 * byte.
	 */
	public int getFinishOffset()
		{
		return myBufferLength - 3;
		}

	}
