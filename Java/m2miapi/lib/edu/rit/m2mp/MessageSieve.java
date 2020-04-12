//******************************************************************************
//
// File:    MessageSieve.java
// Package: edu.rit.m2mp
// Unit:    Class edu.rit.m2mp.MessageSieve
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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * Class MessageSieve encapsulates an object that examines the first packet
 * of each incoming M2MP message and decides which, if any, incoming message
 * notifiers wish to receive the message.
 * <P>
 * Each incoming message notifier (class {@link IncomingMessageNotifier
 * </TT>IncomingMessageNotifier<TT>}) has one or more associated <B>message
 * filters.</B> An incoming message will be passed on to the incoming message
 * notifier if the message matches any of the associated message filters.
 * <P>
 * Each message filter consists of a <B>message prefix</B> and a <B>channel
 * address.</B> Either or both components of the message filter may be omitted.
 * The message prefix is an array of bytes. The message prefix's length must be
 * less than or equal to 498 bytes, the maximum size of the message fragment in
 * an M2MP packet. The channel address is an instance of interface {@link
 * ChannelAddress </TT>ChannelAddress<TT>}.
 * <P>
 * For an incoming message to match a message filter consisting of only a
 * message prefix, the initial sequence of bytes in the message must be the same
 * as the message prefix. Only the first packet of the incoming message is
 * examined (this is why the message prefix must be at most 498 bytes long).
 * <P>
 * For an incoming message to match a message filter consisting of only a
 * channel address, the channel address of the M2MP channel from which the
 * message came must be the same as the channel address in the message filter,
 * as determined by the channel address object's <TT>equals()</TT> operation.
 * <P>
 * For an incoming message to match a message filter consisting of both a
 * message prefix and a channel address, the message must match both the message
 * prefix and the channel address.
 * <P>
 * Any incoming message matches a blank message filter (one where both the
 * message prefix and the channel address are omitted).
 * <P>
 * Class MessageSieve organizes all the message filters for all the incoming
 * message notifiers so that the task of routing each incoming M2MP message to
 * the proper incoming message notifier or notifiers is performed in an
 * efficient manner. Class MessageSieve has operations to add and remove
 * (message filter, incoming message notifier) associations. Class
 * MessageSieve also has an operation to iterate over all the incoming
 * message notifiers whose message filters match a given incoming message.
 * <P>
 * <B>M2MP Library Version 02-Jul-2002</B>
 *
 * @author  Alan Kaminsky
 * @version 31-May-2002
 */
class MessageSieve
	{

// Hidden helper classes.

	// The message prefixes from all the message filters are organized into a
	// table driven finite state machine (FSM). The bytes of the message prefix
	// are matched in sequence against the states of the FSM. Each state has a
	// table, indexed by the current message prefix byte, that points to the
	// next state. A null next state terminates the matching process.
	//
	// If an incoming message matches a message filter, eventually the matching
	// process will reach a state where the initial bytes of the message have
	// matched all the bytes of the message prefix from the message filter. In
	// that state there is a map from a channel address to a list of incoming
	// message notifiers. Having matched the message prefix, the incoming
	// message's channel address is mapped to the corresponding list of incoming
	// message notifiers. The incoming message notifiers in the union of all
	// these lists are the ones to get notified of the message.

	/**
	 * Data type for a state in the FSM.
	 */
	private static class State
		{

		/**
		 * Table of next states, indexed by current message byte, offset by
		 * myLowerByte. If any element is null, there is no next state for the
		 * corresponding message byte. If the whole table is null, there is no
		 * next state for any message byte.
		 */
		private State[] myNextState;

		/**
		 * Message byte corresponding to the first element in myNextState. There
		 * is no next state for any message byte less than myLowerByte.
		 */
		private int myLowerByte;

		/**
		 * Message byte corresponding to the last element in myNextState. There
		 * is no next state for any message byte greater than myUpperByte.
		 */
		private int myUpperByte;

		/**
		 * Map from a channel address (type ChannelAddress) to a list of
		 * incoming message notifiers (type LinkedList of type
		 * IncomingMessageNotifier).
		 */
		private HashMap myChannelAddressMap;

		/**
		 * Return the next state corresponding to the given message byte in this
		 * state, adding a new next state if necessary.
		 *
		 * @param  theByte   Message byte.
		 *
		 * @return  Next state.
		 */
		public State addNextState
			(byte theByte)
			{
			int b = theByte & 0xFF;
			int index = b - myLowerByte;
			State result;
			State[] newNextState;

			if (myNextState == null)
				{
				myNextState = new State [1];
				result = new State();
				myNextState[0] = result;
				myLowerByte = b;
				myUpperByte = b;
				}

			else if (b < myLowerByte)
				{
				newNextState = new State [myUpperByte - b + 1];
				System.arraycopy
					(myNextState, 0,
					 newNextState, myLowerByte - b,
					 myNextState.length);
				myNextState = newNextState;
				result = new State();
				myNextState[0] = result;
				myLowerByte = b;
				}

			else if (b > myUpperByte)
				{
				newNextState = new State [index + 1];
				System.arraycopy
					(myNextState, 0,
					 newNextState, 0,
					 myNextState.length);
				myNextState = newNextState;
				result = new State();
				myNextState[index] = result;
				myUpperByte = b;
				}

			else if (myNextState[index] == null)
				{
				result = new State();
				myNextState[index] = result;
				}

			else
				{
				result = myNextState[index];
				}

			return result;
			}

		/**
		 * Return the next state corresponding to the given message byte in this
		 * state.
		 *
		 * @param  theByte   Message byte.
		 *
		 * @return  Next state, or null if there is no next state.
		 */
		public State getNextState
			(byte theByte)
			{
			int b = theByte & 0xFF;
			return
				myNextState != null && b >= myLowerByte && b <= myUpperByte ?
					myNextState[b - myLowerByte] :
					null;
			}

		/**
		 * Add an association between the given channel address and the given
		 * incoming message notifier in this state.
		 *
		 * @param  theSource    Channel address (may be null).
		 * @param  theNotifier  Incoming message notifier.
		 */
		public void addNotifier
			(ChannelAddress theSource,
			 IncomingMessageNotifier theNotifier)
			{
			if (myChannelAddressMap == null)
				{
				myChannelAddressMap = new HashMap();
				}
			LinkedList theList = (LinkedList)
				myChannelAddressMap.get (theSource);
			if (theList == null)
				{
				theList = new LinkedList();
				myChannelAddressMap.put (theSource, theList);
				}
			theList.add (theNotifier);
			}

		/**
		 * Remove the association between the given channel address and the
		 * given incoming message notifier in this state.
		 *
		 * @param  theSource    Channel address (may be null).
		 * @param  theNotifier  Incoming message notifier.
		 */
		public void removeNotifier
			(ChannelAddress theSource,
			 IncomingMessageNotifier theNotifier)
			{
			if (myChannelAddressMap != null)
				{
				LinkedList theList = (LinkedList)
					myChannelAddressMap.get (theSource);
				if (theList != null)
					{
					theList.remove (theNotifier);
					}
				}
			}

		/**
		 * Obtain the list of incoming message notifiers associated with the
		 * given channel address in this state.
		 *
		 * @param  theSource  Channel address (may be null).
		 *
		 * @return  List of incoming message notifiers, or an empty list or null
		 *          if there are none.
		 */
		public LinkedList getNotifiers
			(ChannelAddress theSource)
			{
			return
				myChannelAddressMap == null ?
					null :
					(LinkedList) myChannelAddressMap.get (theSource);
			}

		}

// Hidden data members.

	private State myInitialState = new State();

// Exported constructors.

	/**
	 * Construct a new message sieve.
	 */
	public MessageSieve()
		{
		}

// Exported operations.

	/**
	 * Add the given (message filter, incoming message notifier) association
	 * to this message sieve.
	 *
	 * @param  theMessageFilter  Message filter.
	 * @param  theNotifier       Incoming message notifier.
	 */
	public synchronized void add
		(MessageFilter theMessageFilter,
		 IncomingMessageNotifier theNotifier)
		{
		// Run the bytes of the message prefix through the FSM, adding states as
		// we go where necessary, until we reach a state where the whole message
		// prefix has been matched.
		byte[] thePrefix = theMessageFilter.getPrefix();
		int n = thePrefix.length;
		int i = 0;
		State theState = myInitialState;
		while (i < n)
			{
			theState = theState.addNextState (thePrefix[i]);
			++ i;
			}

		// In that state, add an association from the channel address to the
		// incoming message notifier.
		theState.addNotifier (theMessageFilter.getSource(), theNotifier);
		}

	/**
	 * Remove the given (message filter, incoming message notifier) association
	 * from this message sieve.
	 *
	 * @param  theMessageFilter  Message filter.
	 * @param  theNotifier       Incoming message notifier.
	 */
	public synchronized void remove
		(MessageFilter theMessageFilter,
		 IncomingMessageNotifier theNotifier)
		{
		// Run the bytes of the message prefix through the FSM until we reach a
		// state where the whole message prefix has been matched, or until we
		// discover there is no match.
		byte[] thePrefix = theMessageFilter.getPrefix();
		int n = thePrefix.length;
		int i = 0;
		State theState = myInitialState;
		while (i < n && theState != null)
			{
			theState = theState.getNextState (thePrefix[i]);
			++ i;
			}

		// In that state, remove the association from the channel address to the
		// incoming message notifier.
		if (theState != null)
			{
			theState.removeNotifier (theMessageFilter.getSource(), theNotifier);
			}
		}

	/**
	 * Obtain an iterator for all the incoming message notifiers (type
	 * IncomingMessageNotifier) whose message filters match the given incoming
	 * message.
	 *
	 * @param  buf        Buffer containing initial message bytes from the
	 *                    incoming message.
	 * @param  start      Index of first byte in <TT>buf</TT> to examine.
	 * @param  finish     Index of last byte in <TT>buf</TT> to examine.
	 * @param  theSource  Channel address from the incoming message.
	 *
	 * @return  Iterator of matching incoming message notifiers.
	 */
	public synchronized Iterator getMatches
		(byte[] buf,
		 int start,
		 int finish,
		 ChannelAddress theSource)
		{
		HashSet theMatchingNotifiers = new HashSet();

		// Run the bytes of the message through the FSM until we reach the end
		// of the message or the end of the FSM.
		State theState = myInitialState;
		while (start <= finish && theState != null)
			{
			accumulate (theMatchingNotifiers, theSource, theState);
			theState = theState.getNextState (buf[start]);
			++ start;
			}
		if (theState != null)
			{
			accumulate (theMatchingNotifiers, theSource, theState);
			}

		// At this point, we've accumulated all the matching incoming message
		// notifiers. Return an iterator.
		return theMatchingNotifiers.iterator();
		}

// Hidden operations.

	/**
	 * Accumulate, into the given set, all incoming message notifiers that match
	 * either any channel address or the given channel address in the given
	 * state.
	 *
	 * @param  theMatchingNotifiers  Set of incoming message notifiers.
	 * @param  theSource             Channel address.
	 * @param  theState              State.
	 */
	private static void accumulate
		(HashSet theMatchingNotifiers,
		 ChannelAddress theSource,
		 State theState)
		{
		LinkedList theNotifiers;

		theNotifiers = theState.getNotifiers (null);
		if (theNotifiers != null)
			{
			theMatchingNotifiers.addAll (theNotifiers);
			}

		theNotifiers = theState.getNotifiers (theSource);
		if (theNotifiers != null)
			{
			theMatchingNotifiers.addAll (theNotifiers);
			}
		}

	}
