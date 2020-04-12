//******************************************************************************
//
// File:    OutgoingMessage.java
// Package: edu.rit.m2mp
// Unit:    Class edu.rit.m2mp.OutgoingMessage
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

import java.io.IOException;
import java.io.OutputStream;

/**
 * Class OutgoingMessage encapsulates a message that is transmitted via the
 * Many-to-Many Protocol (M2MP). Outgoing message objects are not constructed
 * directly. To transmit an outgoing message:
 * <OL TYPE=1>
 * <P><LI>
 * Create an outgoing message object by calling the {@link
 * Protocol#createOutgoingMessage(ChannelAddress) createOutgoingMessage()}
 * factory method on an M2MP {@link Protocol </CODE>Protocol<CODE>} object.
 * <P><LI>
 * Call the outgoing message&#146;s {@link #openOutputStream()
 * openOutputStream()} method to get an output stream for the message.
 * <P><LI>
 * Write the message contents to the output stream.
 * <P><LI>
 * Close the output stream.
 * </OL>
 * <P>
 * <B><I>Warning:</I></B> 
 * It is very important to close the output stream when finished with it,
 * including when an exception is thrown while writing the output stream. If you
 * don't close the output stream, packet buffers will leak out of the channel,
 * and eventually the channel will no longer operate because no packet buffers
 * are available.
 * <P>
 * <B><I>Warning:</I></B> Depending on the channel implementation being used by
 * the M2MP protocol instance, the message may start going out over the M2MP
 * channel as soon as enough bytes have been written to the output stream. If
 * you pause too long while writing the output stream, you risk having the
 * message recipient time out and discard the message.
 * <P>
 * <I>Note:</I> Class OutgoingMessage is analogous to an OutputConnection in the
 * Java 2 Micro Edition Connected Limited Device Configuration (J2ME CLDC)
 * Generic Connection Framework.
 * <P>
 * <B>M2MP Library Version 02-Jul-2002</B>
 *
 * @author  Alan Kaminsky
 * @version 24-Oct-2001
 */
public class OutgoingMessage
	{

// Hidden data members.

	private Channel myChannel;
	private ChannelAddress myDestination;
	private PacketPool myPacketPool;
	private int myMessageID;
	private OutputStream myOutputStream = null;

// Hidden constructors.

	/**
	 * Construct a new outgoing message.
	 *
	 * @param  theChannel      M2MP channel to which this message will go.
	 * @param  theDestination  Destination to which this message will go.
	 * @param  thePacketPool   Packet pool from which to allocate an outgoing
	 *                         packet.
	 * @param  theMessageID    Message ID.
	 */
	OutgoingMessage
		(Channel theChannel,
		 ChannelAddress theDestination,
		 PacketPool thePacketPool,
		 int theMessageID)
		{
		myChannel = theChannel;
		myDestination = theDestination;
		myPacketPool = thePacketPool;
		myMessageID = theMessageID;
		}

// Exported operations.

	/**
	 * Open an output stream to write the contents of this outgoing message.
	 *
	 * @return  Output stream.
	 *
	 * @exception  IOException
	 *     Thrown if an I/O error occurred. In particular, thrown if an output
	 *     stream is already open for this outgoing message, or if no packet
	 *     buffer is available to hold outgoing packets.
	 */
	public OutputStream openOutputStream()
		throws IOException
		{
		if (myOutputStream != null)
			{
			throw new IOException ("Output stream already open");
			}
		myOutputStream =
			new MessageOutputStream
				(myChannel,
				 myDestination,
				 myPacketPool,
				 myMessageID);
		return myOutputStream;
		}

	}
