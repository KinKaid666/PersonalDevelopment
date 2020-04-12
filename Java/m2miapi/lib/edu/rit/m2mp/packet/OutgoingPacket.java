//******************************************************************************
//
// File:    OutgoingPacket.java
// Package: edu.rit.m2mp.packet
// Unit:    Class edu.rit.m2mp.packet.OutgoingPacket
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
 * Class OutgoingPacket encapsulates an outgoing packet in the Many-to-Many
 * Protocol (M2MP).
 * <P>
 * To transmit a packet:
 * <OL TYPE=1>
 * <LI>
 * Call {@link PacketPool#allocateOutgoingPacket()
 * PacketPool.allocateOutgoingPacket()} to get an outgoing packet object.
 * <LI>
 * Call {@link #setMessageID(int) setMessageID()}.
 * <LI>
 * Call {@link #setFragmentNumber(int) setFragmentNumber()}.
 * <LI>
 * Call {@link #reset() reset()}.
 * <LI>
 * Call {@link #write(int) write()} to write the message contents. Call {@link
 * Packet#getAvailable() getAvailable()} to check how many bytes there&#146;s
 * still room to write.
 * <LI>
 * Call {@link #setLastPacket() setLastPacket()} if this is the last packet of a
 * message.
 * <LI>
 * Call {@link #setChecksum() setChecksum()}.
 * <LI>
 * Call {@link Packet#getBuffer() getBuffer()} and {@link
 * Packet#getBufferLength() getBufferLength()} to get the filled-in
 * packet&#146;s byte buffer and length, respectively. Pass these to the
 * underlying network layer to transmit the packet.
 * <LI>
 * When done using the packet, call {@link PacketPool#release(OutgoingPacket)
 * PacketPool.release()}.
 * </OL>
 * <P>
 * <B><I>Warning:</I></B> Class OutgoingPacket is not multiple thread safe.
 * <P>
 * <B>M2MP Library Version 02-Jul-2002</B>
 *
 * @author  Alan Kaminsky
 * @version 23-Oct-2001
 */
public class OutgoingPacket
	extends Packet
	{

// Hidden constructors.

	/**
	 * Construct a new, empty outgoing packet.
	 */
	OutgoingPacket()
		{
		super();
		}

// Exported operations.

	/**
	 * Sets this packet&#146;s message ID field.
	 *
	 * @param  theMessageID  Message ID.
	 */
	public void setMessageID
		(int theMessageID)
		{
		putInt (theMessageID, myBuffer, MESSAGE_ID_OFFSET);
		}

	/**
	 * Sets this packet&#146;s fragment number field. The last-packet flag is
	 * set to 0.
	 *
	 * @param  theFragmentNumber  Fragment number.
	 */
	public void setFragmentNumber
		(int theFragmentNumber)
		{
		putInt
			(theFragmentNumber & FRAGMENT_NUMBER_MASK,
			 myBuffer,
			 FRAGMENT_NUMBER_OFFSET);
		}

	/**
	 * Resets this packet to start writing again from the beginning. Also makes
	 * the maximum number of bytes (498) available to be written.
	 */
	public void reset()
		{
		super.reset();
		myBufferLength = MAX_BUFFER_LENGTH;
		}

	/**
	 * Writes a byte to this packet. Assumes this packet has room for one byte.
	 *
	 * @param  b  Value to write. The least significant 8 bits are written.
	 */
	public void write
		(int b)
		{
		myBuffer[myByteIndex++] = (byte)(b & 0xFF);
		}

	/**
	 * Writes a portion of a byte array to this packet. Assumes this packet has
	 * room for <TT>len</TT> bytes.
	 *
	 * @param  b    Byte array to write.
	 * @param  off  Index of first byte in <TT>b</TT> to write.
	 * @param  len  Number of bytes to write.
	 */
	public void write
		(byte[] b,
		 int off,
		 int len)
		{
		System.arraycopy (b, off, myBuffer, myByteIndex, len);
		myByteIndex += len;
		}

	/**
	 * Sets this packet&#146;s last-packet flag to 1.
	 */
	public void setLastPacket()
		{
		myBuffer[LAST_PACKET_FLAG_OFFSET] |= LAST_PACKET_FLAG_MASK;
		}

	/**
	 * Sets this packet&#146;s checksum field to the proper value based on the
	 * contents of the rest of the packet. Also sets this packet&#146;s
	 * buffer length to include just the bytes up to and including the
	 * checksum.
	 */
	public void setChecksum()
		{
		putShort
			(computeChecksum (myBuffer, 0, myByteIndex),
			 myBuffer,
			 myByteIndex);
		myBufferLength = myByteIndex + 2;
		}

	}
