//******************************************************************************
//
// File:    IncomingMessageNotifier.java
// Package: edu.rit.m2mp
// Unit:    Class edu.rit.m2mp.IncomingMessageNotifier
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

import java.io.InterruptedIOException;
import java.io.IOException;

import java.util.Iterator;
import java.util.LinkedList;

/**
 * Class IncomingMessageNotifier encapsulates a queue of messages that are
 * received via the Many-to-Many Protocol (M2MP).
 * To receive incoming messages:
 * <OL TYPE=1>
 * <P><LI>
 * Create an incoming message notifier object by calling the {@link
 * Protocol#createIncomingMessageNotifier() createIncomingMessageNotifier()}
 * factory method on an M2MP {@link Protocol </CODE>Protocol<CODE>} object.
 * <P><LI>
 * Register one or more appropriate {@link MessageFilter
 * </CODE>MessageFilter<CODE>}s with the incoming message notifier.
 * <P><LI>
 * Call the incoming message notifier&#146;s {@link #accept() accept()} method.
 * This will return an incoming message that matched one of the registered
 * message filters. See class {@link IncomingMessage
 * </CODE>IncomingMessage<CODE>} for further instructions.
 * <P><LI>
 * When done receiving incoming messages, call the incoming message
 * notifier&#146;s {@link #close() close()} method.
 * </OL>
 * <P>
 * <B><I>Warning:</I></B> 
 * It is very important to close the incoming message notifier when finished
 * with it. If you stop accepting messages from the incoming message notifier
 * but you don't close the incoming message notifier, packet buffers will leak
 * out of the channel, and eventually the channel will no longer operate because
 * no packet buffers are available.
 * <P>
 * <I>Note:</I> Class IncomingMessageNotifier is analogous to a
 * StreamConnectionNotifier in the Java 2 Micro Edition Connected Limited Device
 * Configuration (J2ME CLDC) Generic Connection Framework.
 * <P>
 * <B>M2MP Library Version 02-Jul-2002</B>
 *
 * @author  Alan Kaminsky
 * @version 31-May-2002
 */
public class IncomingMessageNotifier
	{

// Hidden data members.

	/**
	 * Packet pool.
	 */
	private PacketPool myPacketPool;

	/**
	 * Message sieve with which to register message filters.
	 */
	private MessageSieve mySieve;

	/**
	 * List of registered message filters (type MessageFilter).
	 */
	private LinkedList myFilters = new LinkedList();

	/**
	 * Queue of received incoming messages (type IncomingMessage).
	 */
	private LinkedList myMessageQueue = new LinkedList();

// Hidden constructors.

	/**
	 * Construct a new incoming message notifier.
	 *
	 * @param  thePacketPool  Packet pool.
	 * @param  theSieve       Message sieve.
	 */
	IncomingMessageNotifier
		(PacketPool thePacketPool,
		 MessageSieve theSieve)
		{
		myPacketPool = thePacketPool;
		mySieve = theSieve;
		}

// Exported operations.

	/**
	 * Register the given message filter with this incoming message notifier.
	 * Thereafter, incoming M2MP messages that match the message filter will be
	 * accepted by this incoming message notifier.
	 * <P>
	 * If one or more message filters equal to <TT>theFilter</TT> are already
	 * registered with this incoming message notifier, then <TT>theFilter</TT>
	 * is registered regardless, resulting in an additional copy of the message
	 * filter being registered.
	 *
	 * @param  theFilter  Message filter to register.
	 *
	 * @exception  NullPointerException
	 *     (unchecked exception) Thrown if <TT>theFilter</TT> is null.
	 * @exception  IllegalStateException
	 *     (unchecked exception) Thrown if this incoming message notifier is
	 *     closed.
	 */
	public synchronized void addMessageFilter
		(MessageFilter theFilter)
		{
		if (theFilter == null)
			{
			throw new NullPointerException();
			}
		myFilters.add (theFilter);
		mySieve.add (theFilter, this);
		}

	/**
	 * Deregister the given message filter from this incoming message notifier.
	 * Thereafter, incoming M2MP messages that match the message filter will not
	 * be accepted by this incoming message notifier (unless they match another
	 * still-registered message filter).
	 * <P>
	 * More precisely, if this incoming message notifier has one or more
	 * registered message filters that are equal to <TT>theFilter</TT>, then one
	 * of those registered message filters is deregistered, and the others (if
	 * any) remain registered.
	 *
	 * @param  theFilter  Message filter to deregister.
	 *
	 * @exception  NullPointerException
	 *     (unchecked exception) Thrown if <TT>theFilter</TT> is null.
	 * @exception  IllegalStateException
	 *     (unchecked exception) Thrown if this incoming message notifier is
	 *     closed.
	 */
	public synchronized void removeMessageFilter
		(MessageFilter theFilter)
		{
		if (theFilter == null)
			{
			throw new NullPointerException();
			}
		if (myFilters.remove (theFilter))
			{
			mySieve.remove (theFilter, this);
			}
		}

	/**
	 * Obtain the next incoming message from this incoming message
	 * notifier&#146;s queue of received messages. This method blocks until a
	 * message is available.
	 *
	 * @return  Incoming message.
	 *
	 * @exception  IOException
	 *     Thrown if an I/O error occurred. Specifically, thrown if this
	 *     incoming message notifier is closed, or if the calling thread was
	 *     interrupted while blocked in this method.
	 */
	public synchronized IncomingMessage accept()
		throws IOException
		{
		while (myMessageQueue != null && myMessageQueue.isEmpty())
			{
			try
				{
				wait();
				}
			catch (InterruptedException exc)
				{
				throw new InterruptedIOException();
				}
			}
		if (myMessageQueue == null)
			{
			throw new IOException ("Incoming message notifier closed");
			}
		else
			{
			return (IncomingMessage) myMessageQueue.removeFirst();
			}
		}

	/**
	 * Obtain the next incoming message from this incoming message
	 * notifier&#146;s queue of received messages, with a timeout. This method
	 * blocks until a message is available or until the specified timeout
	 * interval has elapsed, whichever happens first. The timeout interval is
	 * <TT>millis</TT> milliseconds. If <TT>millis</TT> is 0 or less, this
	 * method will immediately return an incoming message if one is available,
	 * or will immediately return null if no incoming message is available.
	 *
	 * @param  millis  Timeout interval (milliseconds).
	 *
	 * @return  Incoming message, or null if a timeout occurred.
	 *
	 * @exception  IOException
	 *     Thrown if an I/O error occurred. Specifically, thrown if this
	 *     incoming message notifier is closed, or if the calling thread was
	 *     interrupted while blocked in this method.
	 */
	public synchronized IncomingMessage accept
		(long millis)
		throws IOException
		{
		long now = System.currentTimeMillis();
		long timeout = now + millis;
		while
			(myMessageQueue != null &&
			 myMessageQueue.isEmpty() &&
			  now < timeout)
			{
			try
				{
				wait (timeout - now);
				now = System.currentTimeMillis();
				}
			catch (InterruptedException exc)
				{
				throw new InterruptedIOException();
				}
			}
		if (myMessageQueue == null)
			{
			throw new IOException ("Incoming message notifier closed");
			}
		else if (myMessageQueue.isEmpty())
			{
			return null;
			}
		else
			{
			return (IncomingMessage) myMessageQueue.removeFirst();
			}
		}

	/**
	 * Close this incoming message notifier. Thereafter, it will accept no
	 * further messages. If already closed, this method has no effect.
	 *
	 * @exception  IOException
	 *     Thrown if an I/O error occurred while closing.
	 */
	public synchronized void close()
		throws IOException
		{
		if (myMessageQueue != null)
			{
			// Deregister all message filters.
			Iterator iter = myFilters.iterator();
			while (iter.hasNext())
				{
				MessageFilter theFilter = (MessageFilter) iter.next();
				mySieve.remove (theFilter, this);
				}

			// Discard all pending incoming messages.
			while (! myMessageQueue.isEmpty())
				{
				IncomingMessage theMessage =
					(IncomingMessage) myMessageQueue.removeFirst();
				theMessage.openInputStream().close();
				}

			// Null out all fields to mark it closed.
			myPacketPool = null;
			mySieve = null;
			myFilters = null;
			myMessageQueue = null;
			notifyAll();
			}
		}

// Hidden operations.

	/**
	 * Create a new incoming message to be returned by this incoming message
	 * notifier's <TT>accept()</TT> method. If this incoming message notifier is
	 * open, the message input stream for the new incoming message is returned.
	 * If this incoming message notifier is closed, null is returned.
	 *
	 * @param  theSource  Channel address of the entity that sent the incoming
	 *                    message.
	 *
	 * @return  Message input stream, or null if this incoming message notifier
	 *          is closed.
	 */
	synchronized MessageInputStream createIncomingMessage
		(ChannelAddress theSource)
		{
		// Return null if closed.
		if (myMessageQueue == null)
			{
			return null;
			}

		// If not closed, set up theMessage and theStream.
		else
			{
			MessageInputStream theStream =
				new MessageInputStream (myPacketPool);
			IncomingMessage theMessage =
				new IncomingMessage (theSource, theStream);
			myMessageQueue.addLast (theMessage);
			notifyAll();
			return theStream;
			}
		}

// Hidden operations inherited and overridden from class Object.

	/**
	 * Finalize this incoming message notifier. The incoming message notifier is
	 * closed if it is not closed already.
	 */
	protected void finalize()
		{
		try
			{
			close();
			}
		catch (IOException exc)
			{
			}
		}

	}
