//******************************************************************************
//
// File:    Packet.java
// Package: edu.rit.m2mp.packet
// Unit:    Class edu.rit.m2mp.packet.Packet
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
 * Class Packet is the abstract superclass for a packet in the Many-to-Many
 * Protocol (M2MP). Subclasses are provided for outgoing packets (class {@link
 * OutgoingPacket </CODE>OutgoingPacket<CODE>}) and incoming packets (class
 * {@link IncomingPacket </CODE>IncomingPacket<CODE>}).
 * <P>
 * The format of an M2MP packet is shown below. Multibyte fields are stored in
 * big-endian order (most significant byte first). Bits within a 32-bit field
 * are numbered from 31 (most significant bit) to 0 (least significant bit).
 * Bits within a 16-bit field are numbered from 15 (most significant bit) to 0
 * (least significant bit).
 * <P>
 * <CENTER>
 * <TABLE BORDER=1 CELLPADDING=5 CELLSPACING=0>
 * <TR BGCOLOR="#CCCCCC"><TD><I>Index<BR>(bytes)</I></TD><TD><I>Length<BR>(bytes)</I></TD><TD><I>Bits</I></TD><TD><I>Contents</I></TD></TR>
 * <TR><TD>0</TD><TD>4</TD><TD>31..0</TD><TD>Message ID</TD></TR>
 * <TR><TD ROWSPAN=2>4</TD><TD ROWSPAN=2>4</TD><TD>31</TD><TD>Last-packet flag</TD></TR>
 * <TR><TD>30..0</TD><TD>Fragment number</TD></TR>
 * <TR><TD>8</TD><TD><I>N</I></TD><TD>&nbsp;</TD><TD>Message fragment</TD></TR>
 * <TR><TD>8+<I>N</I></TD><TD>2</TD><TD>15..0</TD><TD>Checksum</TD></TR>
 * </TABLE>
 * </CENTER>
 * <P>
 * The fields&#146; contents are as follows. Note that class Packet and its
 * subclasses provide methods for getting and setting these fields; you should
 * not manipulate a packet&#146;s byte buffer directly.
 * <UL>
 * <P><LI>
 * <B>Message ID</B> &#151; A 32-bit number that uniquely identifies a message.
 * All packets of the same message have the same value in the message ID field.
 * <P><LI>
 * <B>Last-packet flag</B> &#151; 0 if this is not the last packet in the
 * message, 1 if this is the last packet in the message.
 * <P><LI>
 * <B>Fragment number</B> &#151; The number of the message fragment contained in
 * this packet. Fragments of a message are numbered sequentially starting with
 * 0.
 * <P><LI>
 * <I>N</I> &#151; The length of this packet&#146;s message fragment in bytes.
 * The value of <I>N</I> is not carried within the packet, rather it is
 * calculated to be 10 less than the length of the whole packet in bytes. The
 * packet&#146;s length is specified when the packet is sent to or received from
 * the underlying network protocol layer. The maximum value of <I>N</I> is 498,
 * 10 less than the maximum M2MP packet length of 508.
 * <P><LI>
 * <B>Message fragment</B> &#151; The message fragment contained within this
 * packet. Each message fragment except possibly the last is 498 bytes long.
 * <P><LI>
 * <B>Checksum</B> &#151; Calculated as follows: The bytes of the packet are
 * arranged in pairs in big-endian order to form a sequence of 16-bit words. If
 * the message fragment&#146;s length is odd, an extra zero byte is appended to
 * the message fragment for purposes of calculating the checksum (the extra byte
 * is not transmitted within the packet). The words are added up using
 * one&#146;s complement arithmetic, and the result is the checksum.
 * </UL>
 * <P>
 * <B><I>Warning:</I></B> Class Packet is not multiple thread safe.
 * <P>
 * <B>M2MP Library Version 02-Jul-2002</B>
 *
 * @author  Alan Kaminsky
 * @version 30-May-2002
 */
public abstract class Packet
	{

// Hidden constants.

	static final int MIN_BUFFER_LENGTH = 11;
	static final int MAX_BUFFER_LENGTH = 508;

	static final int MESSAGE_ID_OFFSET = 0; // int offset

	static final int LAST_PACKET_FLAG_OFFSET = 4;  // byte offset
	static final int LAST_PACKET_FLAG_MASK = 0x80; // byte mask

	static final int FRAGMENT_NUMBER_OFFSET = 4; // int offset
	static final int FRAGMENT_NUMBER_MASK = 0x7FFFFFFF; // int mask

	static final int FRAGMENT_OFFSET = 8;

// Hidden data members.

	/**
	 * Byte buffer, containing the bytes of the packet starting at index 0.
	 */
	byte[] myBuffer = null;

	/**
	 * Number of valid bytes in myBuffer.
	 */
	int myBufferLength = MAX_BUFFER_LENGTH;

	/**
	 * Index within myBuffer of the next message byte to read or write.
	 */
	int myByteIndex = FRAGMENT_OFFSET;

// Hidden constructors.

	/**
	 * Construct a new, empty packet.
	 */
	Packet()
		{
		}

// Exported operations.

	/**
	 * Returns this packet&#146;s byte buffer.
	 */
	public byte[] getBuffer()
		{
		return myBuffer;
		}

	/**
	 * Returns the number of valid bytes in this packet&#146;s byte buffer.
	 */
	public int getBufferLength()
		{
		return myBufferLength;
		}

	/**
	 * Returns this packet&#146;s message ID field.
	 *
	 * @return  Message ID.
	 */
	public int getMessageID()
		{
		return getInt (myBuffer, MESSAGE_ID_OFFSET);
		}

	/**
	 * Returns this packet&#146;s fragment number field.
	 *
	 * @return  Fragment number.
	 */
	public int getFragmentNumber()
		{
		return getInt (myBuffer, FRAGMENT_NUMBER_OFFSET) & FRAGMENT_NUMBER_MASK;
		}

	/**
	 * Returns true if this packet&#146;s last-packet flag is 1, false
	 * otherwise.
	 */
	public boolean isLastPacket()
		{
		return (myBuffer[LAST_PACKET_FLAG_OFFSET] & LAST_PACKET_FLAG_MASK) != 0;
		}

	/**
	 * Resets this packet to start reading or writing again from the beginning.
	 */
	public void reset()
		{
		myByteIndex = FRAGMENT_OFFSET;
		}

	/**
	 * Returns the number of bytes available in this packet. When writing, this
	 * is the number of bytes you can write before filling the packet. When
	 * reading, this is the number of unread bytes.
	 */
	public int getAvailable()
		{
		return myBufferLength - myByteIndex - 2;
		}

	/**
	 * Set this packet&#146;s contents to be a copy of the given packet&#146;s
	 * contents.
	 *
	 * @param  thePacket  Packet to copy.
	 */
	public void copy
		(Packet thePacket)
		{
		System.arraycopy
			(thePacket.myBuffer, 0,
			 this.myBuffer, 0,
			 thePacket.myBufferLength);
		this.myBufferLength = thePacket.myBufferLength;
		this.myByteIndex = thePacket.myByteIndex;
		}

// Hidden operations.

	/**
	 * Store an <TT>int</TT> in a byte array in big-endian order.
	 *
	 * @param  val  Value to store.
	 * @param  buf  Byte array.
	 * @param  off  Index of the first byte in which to store the value.
	 */
	static void putInt
		(int val,
		 byte[] buf,
		 int off)
		{
		buf[off+3] = (byte)(val & 0xFF); val >>= 8;
		buf[off+2] = (byte)(val & 0xFF); val >>= 8;
		buf[off+1] = (byte)(val & 0xFF); val >>= 8;
		buf[off  ] = (byte)(val & 0xFF);
		}

	/**
	 * Store a <TT>short</TT> in a byte array in big-endian order.
	 *
	 * @param  val  Value to store. The least significant 16 bits are stored.
	 * @param  buf  Byte array.
	 * @param  off  Index of the first byte in which to store the value.
	 */
	static void putShort
		(int val,
		 byte[] buf,
		 int off)
		{
		buf[off+1] = (byte)(val & 0xFF); val >>= 8;
		buf[off  ] = (byte)(val & 0xFF);
		}

	/**
	 * Retrieve an <TT>int</TT> from a byte array in big-endian order.
	 *
	 * @param  buf  Byte array.
	 * @param  off  Index of the first byte from which to retrieve the value.
	 *
	 * @return  Retrieved value, an <TT>int</TT> in the range 0x00000000 ..
	 *          0xFFFFFFFF.
	 */
	static int getInt
		(byte[] buf,
		 int off)
		{
		int result;
		result  = (buf[off  ] & 0xFF); result <<= 8;
		result |= (buf[off+1] & 0xFF); result <<= 8;
		result |= (buf[off+2] & 0xFF); result <<= 8;
		result |= (buf[off+3] & 0xFF);
		return result;
		}

	/**
	 * Retrieve a <TT>short</TT> from a byte array in big-endian order.
	 *
	 * @param  buf  Byte array.
	 * @param  off  Index of the first byte from which to retrieve the value.
	 *
	 * @return  Retrieved value, an <TT>int</TT> in the range 0x0000 .. 0xFFFF.
	 */
	static int getShort
		(byte[] buf,
		 int off)
		{
		int result;
		result  = (buf[off  ] & 0xFF); result <<= 8;
		result |= (buf[off+1] & 0xFF);
		return result;
		}

	/**
	 * Compute the checksum of a buffer.
	 *
	 * @param  buf  Buffer.
	 * @param  off  Index of first byte to include in the checksum.
	 * @param  len  Number of bytes to include in the checksum.
	 *
	 * @return  Checksum, an <TT>int</TT> in the range 0x0000 .. 0xFFFF.
	 */
	static int computeChecksum
		(byte[] buf,
		 int off,
		 int len)
		{
		int sum = 0;
		int word;
		while (len > 0)
			{
			word = buf[off++] & 0xFF;
			word <<= 8;
			-- len;
			if (len > 0)
				{
				word |= buf[off++] & 0xFF;
				-- len;
				}
			sum += word;
			if (sum > 0xFFFF)
				{
				sum -= 0xFFFF;
				}
			}
		return sum;
		}

	}
