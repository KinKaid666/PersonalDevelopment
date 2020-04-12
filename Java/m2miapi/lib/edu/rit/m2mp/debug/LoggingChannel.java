//******************************************************************************
//
// File:    LoggingChannel.java
// Package: edu.rit.m2mp.debug
// Unit:    Class edu.rit.m2mp.debug.LoggingChannel
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

package edu.rit.m2mp.debug;

import edu.rit.m2mp.Channel;
import edu.rit.m2mp.ChannelAddress;

import edu.rit.m2mp.packet.IncomingPacket;
import edu.rit.m2mp.packet.OutgoingPacket;
import edu.rit.m2mp.packet.Packet;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * Class LoggingChannel provides a Many-to-Many Protocol (M2MP) {@link
 * edu.rit.m2mp.Channel </CODE>Channel<CODE>} implementation that logs M2MP
 * packets sent and received. An instance of class LoggingChannel is intended to
 * be interposed between an M2MP {@link edu.rit.m2mp.Protocol
 * </CODE>Protocol<CODE>} instance and the actual M2MP channel for the
 * communication medium, thus forming a &#147;channel pipeline.&#148; For
 * example:
 * <PRE>
 *     IPMulticastChannel theMedium = new IPMulticastChannel (...);
 *     LoggingChannel theLogger = new LoggingChannel (theMedium);
 *     Protocol theProtocol = new Protocol (theLogger, ...);</PRE>
 * <P>
 * For every M2MP packet transmitted and received, the logging channel dumps a
 * log record (a line of text). For the transmitting side, and separately for
 * the receiving side, you can specify whether logging is performed, the print
 * writer to dump log records on, whether to include the destination or source
 * channel address in the log record, and how much of the packet contents to
 * include in the log record.
 * <P>
 * <B>M2MP Library Version 02-Jul-2002</B>
 *
 * @author  Alan Kaminsky
 * @version 06-Nov-2001
 */
public class LoggingChannel
	implements Channel
	{

// Hidden data members.

	/**
	 * Underlying channel.
	 */
	private Channel myChannel;

	/**
	 * Logger for transmitted packets.
	 */
	private Logger myTxLogger = new Logger();

	/**
	 * Logger for received packets.
	 */
	private Logger myRxLogger = new Logger();

// Hidden helper class.

	private static class Logger
		{
		/**
		 * Table of hex digits.
		 */
		private static final char[] toHex =
			{'0', '1', '2', '3', '4', '5', '6', '7',
			 '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

		/**
		 * Print writer on which to dump log records, or null not to log
		 * packets.
		 */
		public PrintWriter myWriter = null;

		/**
		 * True to include the channel address in the log records.
		 */
		public boolean myIncludeAddress = false;

		/**
		 * Number of bytes of packet contents to include in the log records.
		 */
		public int myByteCount = 0;

		/**
		 * Log the given packet.
		 */
		public void log
			(Packet thePacket,
			 ChannelAddress theAddress,
			 char theDirection)
			{
			PrintWriter pw = myWriter;
			if (pw != null)
				{
				synchronized (pw)
					{
					pw.print (theDirection);
					if (myIncludeAddress)
						{
						pw.print (' ');
						pw.print (theAddress);
						}
					int len =
						Math.min (myByteCount, thePacket.getBufferLength());
					if (len > 0)
						{
						byte[] buf = thePacket.getBuffer();
						for (int i = 0; i < len; ++ i)
							{
							pw.print (' ');
							pw.print (toHex [(buf[i] >> 4) & 0x0F]);
							pw.print (toHex [(buf[i]     ) & 0x0F]);
							}
						}
					pw.println();
					}
				}
			}
		}

// Exported constructors.

	/**
	 * Construct a new logging channel on top of the given underlying channel.
	 *
	 * @param  theChannel  Underlying channel.
	 *
	 * @exception  NullPointerException
	 *     (unchecked exception) Thrown if <TT>theChannel</TT> is null.
	 */
	public LoggingChannel
		(Channel theChannel)
		{
		if (theChannel == null)
			{
			throw new NullPointerException();
			}
		myChannel = theChannel;
		}

// Exported operations.

	/**
	 * Enable logging of transmitted packets on the given print writer, or
	 * disable logging of transmitted packets. Initially, logging of transmitted
	 * packets is disabled.
	 * <P>
	 * <I>Note:</I> This logging channel will acquire <TT>theWriter</TT>&#146;s
	 * synchronization lock before dumping each log record and will release the
	 * lock afterwards. If log records for transmitted and received packets are
	 * being dumped on the same print writer, this ensures each log record
	 * appears on a line by itself.
	 *
	 * @param  theWriter  Print writer on which to log transmitted packets, or
	 *                    null not to log transmitted packets.
	 */
	public void enableTxLogging
		(PrintWriter theWriter)
		{
		myTxLogger.myWriter = theWriter;
		}

	/**
	 * Specify whether to include the destination channel address in log records
	 * for transmitted packets. Initially, the destination channel address is
	 * omitted.
	 *
	 * @param  includeAddress  True to include the destination channel address,
	 *                         false to omit it.
	 */
	public void includeTxAddress
		(boolean includeAddress)
		{
		myTxLogger.myIncludeAddress = includeAddress;
		}

	/**
	 * Specify how many bytes of the packet contents to include in log records
	 * for transmitted packets. Initially, no bytes are included.
	 *
	 * @param  theByteCount  Number of bytes to include. If less than or equal
	 *                       to 0, no bytes are included.
	 */
	public void includeTxBytes
		(int theByteCount)
		{
		myTxLogger.myByteCount = theByteCount;
		}

	/**
	 * Enable logging of received packets on the given print writer, or disable
	 * logging of received packets. Initially, logging of received packets is
	 * disabled.
	 * <P>
	 * <I>Note:</I> This logging channel will acquire <TT>theWriter</TT>&#146;s
	 * synchronization lock before dumping each log record and will release the
	 * lock afterwards. If log records for transmitted and received packets are
	 * being dumped on the same print writer, this ensures each log record
	 * appears on a line by itself.
	 *
	 * @param  theWriter  Print writer on which to log received packets, or null
	 *                    not to log received packets.
	 */
	public void enableRxLogging
		(PrintWriter theWriter)
		{
		myRxLogger.myWriter = theWriter;
		}

	/**
	 * Specify whether to include the source channel address in log records for
	 * received packets. Initially, the source channel address is omitted.
	 *
	 * @param  includeAddress  True to include the source channel address, false
	 *                         to omit it.
	 */
	public void includeRxAddress
		(boolean includeAddress)
		{
		myRxLogger.myIncludeAddress = includeAddress;
		}

	/**
	 * Specify how many bytes of the packet contents to include in log records
	 * for received packets. Initially, no bytes are included.
	 *
	 * @param  theByteCount  Number of bytes to include. If less than or equal
	 *                       to 0, no bytes are included.
	 */
	public void includeRxBytes
		(int theByteCount)
		{
		myRxLogger.myByteCount = theByteCount;
		}

// Exported operations inherited and implemented from interface Channel.

	/**
	 * Returns the channel address of this logging channel&#146;s underlying
	 * channel.
	 */
	public ChannelAddress getAddress()
		{
		return myChannel.getAddress();
		}

	/**
	 * Receive the given M2MP packet via this channel. This method calls the
	 * underlying channel to receive the packet, logs the received packet as
	 * configured by the above methods, and returns the source channel address
	 * that the underlying channel returned.
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
		throws IOException
		{
		ChannelAddress theSource = myChannel.receivePacket (thePacket);
		myRxLogger.log (thePacket, theSource, '<');
		return theSource;
		}

	/**
	 * Send the given M2MP packet via this channel to the given destination.
	 * This method logs the transmitted packet as configured by the above
	 * methods, then calls the underlying channel to send the packet to the
	 * destination.
	 *
	 * @param  thePacket       M2MP packet to be sent to
	 *                         <TT>theDestination</TT>.
	 * @param  theDestination  Where to send <TT>thePacket</TT>.
	 *
	 * @exception  ClassCastException
	 *     (unchecked exception) Thrown if <TT>theDestination</TT> is not of the
	 *     proper type for the underlying channel implementation.
	 * @exception  IOException
	 *     Thrown if an I/O error occurred.
	 */
	public void transmitPacket
		(OutgoingPacket thePacket,
		 ChannelAddress theDestination)
		throws IOException
		{
		myTxLogger.log (thePacket, theDestination, '>');
		myChannel.transmitPacket (thePacket, theDestination);
		}

	}
