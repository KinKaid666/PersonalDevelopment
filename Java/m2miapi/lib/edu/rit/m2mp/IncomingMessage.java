//******************************************************************************
//
// File:    IncomingMessage.java
// Package: edu.rit.m2mp
// Unit:    Class edu.rit.m2mp.IncomingMessage
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

import java.io.InputStream;
import java.io.IOException;

/**
 * Class IncomingMessage encapsulates a message that is received via the
 * Many-to-Many Protocol (M2MP). Incoming message objects are not constructed
 * directly. To receive an incoming message:
 * <OL TYPE=1>
 * <P><LI>
 * Create an incoming message notifier object by calling the {@link
 * Protocol#createIncomingMessageNotifier() createIncomingMessageNotifier()}
 * factory method on an M2MP {@link Protocol </TT>Protocol<TT>} object.
 * <P><LI>
 * Register one or more appropriate {@link MessageFilter
 * </TT>MessageFilter<TT>}s with the incoming message notifier.
 * <P><LI>
 * Call the incoming message notifier&#146;s {@link
 * IncomingMessageNotifier#accept() accept()} method. This will return an
 * incoming message that matched one of the registered message filters.
 * <P><LI>
 * If desired, call the incoming message&#146;s {@link #getSource() getSource()}
 * method to find out which entity sent the incoming message.
 * <P><LI>
 * Call the incoming message&#146;s {@link #openInputStream()
 * openInputStream()} method to get an input stream for the message.
 * <P><LI>
 * Read the message contents from the input stream.
 * <P><LI>
 * Close the input stream.
 * </OL>
 * <P>
 * <B><I>Warning:</I></B> 
 * It is very important to close the input stream when finished with it,
 * including when an exception is thrown while reading the input stream. If you
 * don't close the input stream, packet buffers will leak out of the channel,
 * and eventually the channel will no longer operate because no packet buffers
 * are available.
 * <P>
 * <I>Note:</I> Class IncomingMessage is analogous to an InputConnection in the
 * Java 2 Micro Edition Connected Limited Device Configuration (J2ME CLDC)
 * Generic Connection Framework.
 * <P>
 * <B>M2MP Library Version 02-Jul-2002</B>
 *
 * @author  Alan Kaminsky
 * @version 15-May-2002
 */
public class IncomingMessage
	{

// Hidden data members.

	/**
	 * Address of the entity that sent this incoming message.
	 */
	private ChannelAddress mySource;

	/**
	 * Input stream for reading the contents of this incoming message.
	 */
	private InputStream myInputStream;

// Hidden constructors.

	/**
	 * Construct a new incoming message.
	 *
	 * @param  theSource
	 *     Address of the entity that sent this incoming message.
	 * @param  theInputStream
	 *     Input stream for reading the contents of this incoming message.
	 */
	IncomingMessage
		(ChannelAddress theSource,
		 InputStream theInputStream)
		{
		mySource = theSource;
		myInputStream = theInputStream;
		}

// Exported operations.

	/**
	 * Returns the address of the entity that sent this incoming message.
	 * <P>
	 * <I>Note:</I> Some M2MP channel implementations always receive packets
	 * from the same place, such as a broadcast medium, and do not distinguish
	 * among sources. In that case the returned source address is typically that
	 * of the M2MP protocol instance&#146;s own channel. See the channel
	 * implementation documentation for further information.
	 */
	public ChannelAddress getSource()
		{
		return mySource;
		}

	/**
	 * Open an input stream to read the contents of this incoming message.
	 *
	 * @return  Input stream.
	 *
	 * @exception  IOException
	 *     Thrown if an I/O error occurred. In particular, thrown if an input
	 *     stream is already open for this incoming message.
	 */
	public InputStream openInputStream()
		throws IOException
		{
		if (myInputStream == null)
			{
			throw new IOException ("Input stream already open");
			}
		InputStream result = myInputStream;
		myInputStream = null;
		return result;
		}

	}
