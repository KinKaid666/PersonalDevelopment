//******************************************************************************
//
// File:    Channel.java
// Package: edu.rit.m2mp
// Unit:    Interface edu.rit.m2mp.Channel
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
import edu.rit.m2mp.packet.OutgoingPacket;

import java.io.IOException;

/**
 * Interface Channel specifies the interface for a channel in the Many-to-Many
 * Protocol (M2MP). M2MP uses a channel to send messages to and receive messages
 * from a particular underlying communication medium. Several M2MP channel
 * implementations are provided, including:
 * <UL>
 * <P><LI>
 * Class {@link edu.rit.m2mp.ip.M2MPRouterChannel
 * </CODE>M2MPRouterChannel<CODE>} &#151; A channel that broadcasts to all M2MP
 * instances within the same host. Requires the presence of a separate daemon
 * process, class {@link edu.rit.m2mp.ip.M2MPRouter </CODE>M2MPRouter<CODE>},
 * which can also send and receive M2MP messages on an external network if
 * desired. See package {@link edu.rit.m2mp.ip </CODE>edu.rit.m2mp.ip<CODE>} for
 * further information.
 * <P><LI>
 * Class {@link edu.rit.m2mp.ip.IPMulticastChannel
 * </CODE>IPMulticastChannel<CODE>} &#151; A broadcast channel that sends
 * messages to and receives messages from all hosts in a multicast group at a
 * given IP multicast address and port. See package {@link edu.rit.m2mp.ip
 * </CODE>edu.rit.m2mp.ip<CODE>} for further information.
 * <P><LI>
 * Class {@link edu.rit.m2mp.ip.IPUnicastChannel </CODE>IPUnicastChannel<CODE>}
 * &#151; A point-to-point channel that sends messages to and receives messages
 * from another host at a given IP unicast address and port. See package {@link
 * edu.rit.m2mp.ip </CODE>edu.rit.m2mp.ip<CODE>} for further information.
 * </UL>
 * <P>
 * You can write your own M2MP channel implementation that uses a custom
 * communication medium. You can then create an M2MP {@link Protocol
 * </CODE>Protocol<CODE>} instance that uses your custom M2MP channel.
 * <P>
 * You can also set up a &#147;channel pipeline&#148; by hooking your M2MP
 * {@link Protocol </CODE>Protocol<CODE>} instance up to a channel instance
 * which is hooked up to another channel instance, and so on until you reach the
 * final channel instance which hooks up to the underlying communication medium.
 * Outgoing M2MP messages will traverse the channel pipeline in forward order,
 * and incoming M2MP messages will traverse the channel pipeline in reverse
 * order. This capability can be used, for example, to insert debug logging or
 * other debug controls into the incoming and outgoing M2MP message streams. See
 * class {@link edu.rit.m2mp.debug.LoggingChannel </CODE>LoggingChannel<CODE>}
 * for a sample implementation.
 * <P>
 * <B><I>Requirements:</I></B>
 * <UL>
 * <P><LI>
 * A Channel implementation must not pass up to its client on the incoming side
 * any message packets that were sent on the channel's outgoing side.
 * <P><LI>
 * A Channel implementation must be multiple thread safe. That is, multiple
 * clients must be able to receive and transmit packets concurrently.
 * </UL>
 * <P>
 * <B>M2MP Library Version 02-Jul-2002</B>
 *
 * @author  Alan Kaminsky
 * @version 01-Jul-2002
 */
public interface Channel
	{

// Exported operations.

	/**
	 * Returns this channel&#146;s address. This is the source channel address
	 * for packets transmitted via this channel.
	 */
	public ChannelAddress getAddress();

	/**
	 * Receive the given M2MP packet via this channel. This method must:
	 * <OL TYPE=1>
	 * <LI>
	 * Receive the next packet from the communication medium, blocking until one
	 * is available.
	 * <LI>
	 * Fill in <TT>thePacket</TT>&#146;s byte buffer with the contents of the
	 * received packet.
	 * <LI>
	 * Set <TT>thePacket</TT>&#146;s buffer length to the correct value.
	 * <LI>
	 * Return the channel address of the entity that sent the packet.
	 * </OL>
	 * <P>
	 * <I>Note:</I> Some M2MP channel implementations always receive packets
	 * from the same place, such as a broadcast medium, and do not distinguish
	 * among sources. In that case the returned source address is typically this
	 * channel&#146;s address. See the channel implementation documentation for
	 * further information.
	 * <P>
	 * <I>Note:</I> The returned packet must not be one that was sent via
	 * <TT>transmitPacket()</TT> on this channel.
	 * <P>
	 * <I>Note:</I> This method must not release <TT>thePacket</TT>. It is the
	 * caller&#146;s responsibility to release <TT>thePacket</TT>.
	 *
	 * @param  thePacket       M2MP packet to be received.
	 *
	 * @return  Source channel address of the entity that transmitted
	 *          <TT>thePacket</TT>.
	 *
	 * @exception  IOException
	 *     Thrown if an I/O error occurred.
	 */
	public ChannelAddress receivePacket
		(IncomingPacket thePacket)
		throws IOException;

	/**
	 * Send the given M2MP packet via this channel to the given destination.
	 * This method must:
	 * <OL TYPE=1>
	 * <LI>
	 * Extract the valid bytes out of <TT>thePacket</TT>&#146;s byte buffer as
	 * determined by <TT>thePacket</TT>&#146;s buffer length.
	 * <LI>
	 * Transmit those bytes to <TT>theDestination</TT> on the communication
	 * medium.
	 * </OL>
	 * <P>
	 * <I>Note:</I> Some M2MP channel implementations always send packets to the
	 * same place, such as a broadcast medium, and do not distinguish among
	 * destinations. In that case <TT>theDestination</TT> is ignored. See the
	 * channel implementation documentation for further information.
	 * <P>
	 * <I>Note:</I> This method must not release <TT>thePacket</TT>. It is the
	 * caller&#146;s responsibility to release <TT>thePacket</TT>.
	 *
	 * @param  thePacket       M2MP packet to be sent to
	 *                         <TT>theDestination</TT>.
	 * @param  theDestination  Where to send <TT>thePacket</TT>.
	 *
	 * @exception  ClassCastException
	 *     (unchecked exception) Thrown if <TT>theDestination</TT> is not of the
	 *     proper type for this channel implementation.
	 * @exception  IOException
	 *     Thrown if an I/O error occurred.
	 */
	public void transmitPacket
		(OutgoingPacket thePacket,
		 ChannelAddress theDestination)
		throws IOException;

	}
