//******************************************************************************
//
// File:    OutOfPacketsException.java
// Package: edu.rit.m2mp.packet
// Unit:    Class edu.rit.m2mp.packet.OutOfPacketsException
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

import java.io.IOException;

/**
 * Class OutOfPacketsException is an {@link java.io.IOException
 * </CODE>IOException<CODE>} thrown to indicate that an incoming or outgoing
 * packet cannot be allocated because all byte buffers are in use.
 * <P>
 * So as not to consume unbounded memory on small devices, an M2MP protocol
 * instance has a pool of packet buffers which are used to hold outgoing and
 * incoming packets. The pool has a fixed maximum number of packet buffers. Any
 * of the following may cause the packet pool to run out of packets:
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
 * received, and creating an outgoing message will throw an
 * OutOfPacketsException. Make sure your M2MP application is designed properly
 * and does not get into the above situations.
 * <P>
 * <B>M2MP Library Version 02-Jul-2002</B>
 *
 * @author  Alan Kaminsky
 * @version 01-Jul-2002
 */
public class OutOfPacketsException
	extends IOException
	{

// Exported constructors.

	/**
	 * Construct a new out of packets exception with no detail message.
	 */
	public OutOfPacketsException()
		{
		super();
		}

	/**
	 * Construct a new out of packets exception with the given detail message.
	 *
	 * @param  msg  Detail message.
	 */
	public OutOfPacketsException
		(String msg)
		{
		super (msg);
		}

	}
