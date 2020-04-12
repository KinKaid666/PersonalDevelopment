//******************************************************************************
//
// File:    MessageFilter.java
// Package: edu.rit.m2mp
// Unit:    Class edu.rit.m2mp.MessageFilter
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

/**
 * Class MessageFilter encapsulates the information needed to decide whether an
 * incoming M2MP message should go to an incoming message notifier.
 * <P>
 * Each incoming message notifier (class {@link IncomingMessageNotifier
 * </CODE>IncomingMessageNotifier<CODE>}) has one or more <B>message filters</B>
 * registered with it. An incoming message will be passed on to a certain
 * incoming message notifier if the message matches any of that incoming message
 * notifier's registered message filters.
 * <P>
 * Each message filter consists of a <B>message prefix</B> and a <B>channel
 * address.</B> Either or both components of the message filter may be omitted.
 * The message prefix is an array of bytes. The message prefix's length must be
 * less than or equal to 498 bytes, the maximum size of the message fragment in
 * an M2MP packet. The channel address is an instance of interface {@link
 * ChannelAddress </CODE>ChannelAddress<CODE>}.
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
 * A single message filter can only match one message prefix and/or one channel
 * address. To match multiple message prefixes or multiple channel addresses,
 * create and register multiple message filters with an incoming message
 * notifier.
 * <P>
 * Due to the way it is used within the M2MP layer, an instance of class
 * MessageFilter is immutable once constructed.
 * <P>
 * <B>M2MP Library Version 02-Jul-2002</B>
 *
 * @author  Alan Kaminsky
 * @version 28-May-2002
 */
public class MessageFilter
	{

// Hidden data members.

	private byte[] myPrefix;
	private ChannelAddress mySource;

// Exported constructors.

	/**
	 * Construct a new message filter that will match any incoming message.
	 */
	public MessageFilter()
		{
		this (null, null);
		}

	/**
	 * Construct a new message filter that will match any incoming message from
	 * the given source channel address.
	 *
	 * @param  theSource  Source channel address, or null to match any source.
	 */
	public MessageFilter
		(ChannelAddress theSource)
		{
		this (null, theSource);
		}

	/**
	 * Construct a new message filter that will match any incoming message with
	 * the given message prefix. The length of <TT>thePrefix</TT> must be 498
	 * bytes or less.
	 *
	 * @param  thePrefix  Message prefix, or null or a zero-length byte array to
	 *                    match any prefix.
	 *
	 * @exception  IllegalArgumentException
	 *     (unchecked exception) Thrown if <TT>thePrefix</TT> is longer than 498
	 *     bytes.
	 */
	public MessageFilter
		(byte[] thePrefix)
		{
		this (thePrefix, null);
		}

	/**
	 * Construct a new message filter that will match any incoming message with
	 * the given message prefix from the given source channel address. The
	 * length of <TT>thePrefix</TT> must be 498 bytes or less.
	 *
	 * @param  thePrefix  Message prefix, or null or a zero-length byte array to
	 *                    match any prefix.
	 * @param  theSource  Source channel address, or null to match any source.
	 *
	 * @exception  IllegalArgumentException
	 *     (unchecked exception) Thrown if <TT>thePrefix</TT> is longer than 498
	 *     bytes.
	 */
	public MessageFilter
		(byte[] thePrefix,
		 ChannelAddress theSource)
		{
		if (thePrefix == null)
			{
			myPrefix = new byte [0];
			}
		else if (thePrefix.length <= 498)
			{
			myPrefix = (byte[]) thePrefix.clone();
			}
		else
			{
			throw new IllegalArgumentException();
			}
		mySource = theSource;
		}

// Exported operations.

	/**
	 * Returns the message prefix which an incoming message&#146;s contents must
	 * have in order to match this message filter. The returned value is never
	 * null. If the returned value is a zero-length array, then an incoming
	 * message with any contents will match. Otherwise, all the bytes of the
	 * returned value must equal the corresponding initial bytes of the incoming
	 * message&#146;s contents for the incoming message to match.
	 *
	 * @return  Message prefix to match.
	 */
	public final byte[] getPrefix()
		{
		return myPrefix;
		}

	/**
	 * Returns the source channel address from which an incoming message must
	 * come in order to match this message filter. If the returned value is
	 * null, then an incoming message with any source channel address will
	 * match. If the returned value is not null, then an incoming message whose
	 * source channel address is equal to the returned value (according to the
	 * <TT>equals()</TT> method) will match.
	 *
	 * @return  Source channel address to match, or null.
	 */
	public final ChannelAddress getSource()
		{
		return mySource;
		}

// Exported operations inherited and overridden from class Object.

	/**
	 * Determine if the given object is equal to this message filter. To be
	 * equal, the given object must be a non-null instance of class
	 * MessageFilter that matches the same messages as this message filter.
	 *
	 * @param  obj  Object to test.
	 *
	 * @return  True if <TT>obj</TT> is equal to this message filter, false
	 *          otherwise.
	 */
	public boolean equals
		(Object obj)
		{
		return
			obj != null &&
			obj instanceof MessageFilter &&
			equalsPrefix (this.myPrefix, ((MessageFilter) obj).myPrefix) &&
			equalsSource (this.mySource, ((MessageFilter) obj).mySource);
		}

	/**
	 * Returns a hash code for this message filter.
	 *
	 * @return  Hash code.
	 */
	public int hashCode()
		{
		return
			myPrefix.hashCode() +
			(mySource == null ? 0 : mySource.hashCode());
		}

// Hidden operations.

	/**
	 * Returns true if the two given byte arrays are equal.
	 */
	private static boolean equalsPrefix
		(byte[] prefix1,
		 byte[] prefix2)
		{
		int n1 = prefix1.length;
		int n2 = prefix2.length;
		if (n1 != n2)
			{
			return false;
			}
		else
			{
			for (int i = 0; i < n1; ++ i)
				{
				if (prefix1[i] != prefix2[i])
					{
					return false;
					}
				}
			return true;
			}
		}

	/**
	 * Returns true if the two given channel addresses are equal.
	 */
	private static boolean equalsSource
		(ChannelAddress source1,
		 ChannelAddress source2)
		{
		return
			source1 == null ?
				source2 == null :
				source1.equals (source2);
		}

	}
