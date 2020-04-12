//******************************************************************************
//
// File:    MessageInputStream.java
// Package: edu.rit.m2mp
// Unit:    Class edu.rit.m2mp.MessageInputStream
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
import edu.rit.m2mp.packet.PacketPool;

import java.io.InterruptedIOException;
import java.io.IOException;
import java.io.InputStream;

import java.util.LinkedList;

/**
 * Class MessageInputStream encapsulates the input stream used to receive an
 * incoming Many-to-Many Protocol (M2MP) message.
 * <P>
 * <B>M2MP Library Version 02-Jul-2002</B>
 *
 * @author  Alan Kaminsky
 * @version 29-Oct-2001
 */
class MessageInputStream
	extends InputStream
	{

// Hidden data members.

	/**
	 * Packet pool to which to release incoming packets.
	 */
	private PacketPool myPacketPool;

	/**
	 * The current packet whose contents are being fed out to the input
	 * stream&#146;s caller. If null, we must wait for a new packet to arrive.
	 */
	private IncomingPacket myPacket = null;

	/**
	 * FIFO queue of packets in the incoming message.
	 */
	private LinkedList myPacketQueue = new LinkedList();

	/**
	 * True if end of stream has been encountered.
	 */
	private boolean iamAtEof = false;

	/**
	 * String describing the I/O error that has been encountered on this input
	 * stream, or null if no I/O error has been encountered.
	 */
	private String myIOError = null;

// Hidden constructors.

	/**
	 * Create a new message input stream.
	 *
	 * @param  thePacketPool  Packet pool to which to release incoming packets.
	 */
	MessageInputStream
		(PacketPool thePacketPool)
		{
		myPacketPool = thePacketPool;
		}

// Exported operations inherited and overridden from class InputStream.

	/**
	 * Read a byte from this message input stream. This method blocks until
	 * input data is available, the end of the stream is encountered, or an
	 * exception is thrown.
	 *
	 * @return  Byte read (a value in the range 0 .. 255), or &#150;1 if the end
	 *          of stream is encountered.
	 *
	 * @exception  IOException
	 *     Thrown if an I/O error occurred.
	 */
	public synchronized int read()
		throws IOException
		{
		waitForMyPacket (0);
		if (myIOError != null)
			{
			throw new IOException (myIOError);
			}
		else if (iamAtEof)
			{
			return -1;
			}
		else
			{
			int result = myPacket.read();
			releaseMyPacketIfSpent();
			return result;
			}
		}

	/**
	 * Read a byte array from this message input stream. Bytes are stored in
	 * <TT>b</TT> starting at index 0. At most <TT>b.length</TT> bytes are read.
	 * The actual number of bytes actually read is returned. This method blocks
	 * until input data is available, the end of the stream is encountered, or
	 * an exception is thrown.
	 *
	 * @param  b  Byte array to read.
	 *
	 * @return  Actual number of bytes read, or &#150;1 if the end of stream is
	 *          encountered.
	 *
	 * @exception  NullPointerException
	 *     (unchecked exception) Thrown if <TT>b</TT> is null.
	 * @exception  IOException
	 *     Thrown if an I/O error occurred.
	 */
	public synchronized int read
		(byte[] b)
		throws IOException
		{
		return read (b, 0, b.length);
		}

	/**
	 * Read a portion of a byte array from this message input stream. Bytes are
	 * stored in <TT>b</TT> starting at index <TT>off</TT>. At most <TT>len</TT>
	 * bytes are read. The actual number of bytes actually read is returned.
	 * This method blocks until input data is available, the end of the stream
	 * is encountered, or an exception is thrown.
	 *
	 * @param  b    Byte array to read.
	 * @param  off  Index of first byte to read.
	 * @param  len  Maximum number of bytes to read.
	 *
	 * @return  Actual number of bytes read, or &#150;1 if the end of stream is
	 *          encountered.
	 *
	 * @exception  NullPointerException
	 *     (unchecked exception) Thrown if <TT>b</TT> is null.
	 * @exception  IndexOutOfBoundsException
	 *     (unchecked exception) Thrown if <TT>off</TT> is less than 0,
	 *     <TT>len</TT> is less than 0, or <TT>off+len</TT> is greater than the
	 *     length of <TT>b</TT>.
	 * @exception  IOException
	 *     Thrown if an I/O error occurred.
	 */
	public synchronized int read
		(byte[] b,
		 int off,
		 int len)
		throws IOException
		{
		if (off < 0 || len < 0 || off+len > b.length)
			{
			throw new IndexOutOfBoundsException();
			}
		int totalCount = 0;
		int readCount;
		while (totalCount < len)
			{
			waitForMyPacket (totalCount);
			if (myIOError != null)
				{
				throw new IOException (myIOError);
				}
			else if (iamAtEof)
				{
				break;
				}
			else
				{
				readCount =
					Math.min (len - totalCount, myPacket.getAvailable());
				myPacket.read (b, off, readCount);
				off += readCount;
				totalCount += readCount;
				releaseMyPacketIfSpent();
				}
			}
		return iamAtEof && totalCount == 0 ? -1 : totalCount;
		}

	/**
	 * Skip over and discard a number of bytes from this message input stream.
	 *
	 * @param  n  Maximum number of bytes to skip.
	 *
	 * @return  Actual number of bytes skipped.
	 *
	 * @exception  IOException
	 *     Thrown if an I/O error occurred.
	 */
	public synchronized long skip
		(long n)
		throws IOException
		{
		long totalCount = 0L;
		int skipCount;
		while (totalCount < n)
			{
			waitForMyPacket ((int) totalCount);
			if (myIOError != null)
				{
				throw new IOException (myIOError);
				}
			else if (iamAtEof)
				{
				break;
				}
			else
				{
				skipCount = (int)
					Math.min (n - totalCount, myPacket.getAvailable());
				myPacket.skip (skipCount);
				totalCount += skipCount;
				releaseMyPacketIfSpent();
				}
			}
		return totalCount;
		}

	/**
	 * Returns the number of bytes that can be read (or skipped over) from this
	 * message input stream without blocking by the next caller of a method for
	 * this message input stream. The next caller might be the same thread or or
	 * another thread.
	 *
	 * @return  Number of bytes.
	 *
	 * @exception  IOException
	 *     Thrown if an I/O error occurred.
	 */
	public synchronized int available()
		throws IOException
		{
		if (myIOError != null)
			{
			throw new IOException (myIOError);
			}
		else if (myPacket == null)
			{
			return 0;
			}
		else
			{
			return myPacket.getAvailable();
			}
		}

	/**
	 * Close this message input stream.
	 *
	 * @exception  IOException
	 *     Thrown if an I/O error occurred.
	 */
	public synchronized void close()
		throws IOException
		{
		if (myPacketQueue == null)
			{
			throw new IOException (myIOError);
			}
		doClose();
		}

// Hidden operations.

	/**
	 * Wait until any of the following conditions is true. (1) This input stream
	 * has experienced an I/O error. (2) This input stream has reached EOF. (3)
	 * The next packet is available in myPacket.
	 *
	 * @param  totalCount  Number of bytes successfully transferred so far.
	 *
	 * @exception  InterruptedIOException
	 *     Thrown if the calling thread was interrupted while blocked in this
	 *     method.
	 */
	void waitForMyPacket
		(int totalCount)
		throws InterruptedIOException
		{
		// If no I/O error and not at EOF and a packet is not available, ...
		if (myIOError == null && ! iamAtEof && myPacket == null)
			{
			// ... wait until I/O error or at EOF or a packet is available.
			while (myIOError == null && ! iamAtEof && myPacketQueue.isEmpty())
				{
				try
					{
					wait();
					}
				catch (InterruptedException exc)
					{
					InterruptedIOException exc2 = new InterruptedIOException();
					exc2.bytesTransferred = totalCount;
					throw exc2;
					}
				}

			// If no I/O error and not at EOF, ...
			if (myIOError == null && ! iamAtEof)
				{
				// ... a packet must be available.
				myPacket = (IncomingPacket) myPacketQueue.removeFirst();
				myPacket.reset();
				}
			}
		}

	/**
	 * If all of myPacket&#146;s bytes have been sucked out, release myPacket.
	 * Also note an EOF if this is the last packet of the message.
	 */
	void releaseMyPacketIfSpent()
		{
		if (myPacket.getAvailable() == 0)
			{
			if (myPacket.isLastPacket())
				{
				iamAtEof = true;
				}
			myPacketPool.release (myPacket);
			myPacket = null;
			}
		}

	/**
	 * Append the given incoming packet to myPacketQueue.
	 */
	synchronized void enqueue
		(IncomingPacket thePacket)
		{
		if (myIOError == null)
			{
			myPacketQueue.addLast (thePacket);
			notifyAll();
			}
		else
			{
			myPacketPool.release (thePacket);
			}
		}

	/**
	 * Notify that an I/O error occurred.
	 *
	 * @param  theIOError  Error message.
	 */
	synchronized void notifyIOError
		(String theIOError)
		{
		myIOError = theIOError;
		notifyAll();
		}

	/**
	 * Perform common actions when closing the stream. Assumes the stream is
	 * still open.
	 */
	private void doClose()
		{
		if (myPacket != null)
			{
			myPacketPool.release (myPacket);
			myPacket = null;
			}
		while (! myPacketQueue.isEmpty())
			{
			myPacketPool.release ((IncomingPacket) myPacketQueue.removeFirst());
			}
		myPacketQueue = null;
		myIOError = "Stream closed";
		notifyAll();
		}

// Hidden operations inherited and overridden from class Object.

	/**
	 * Finalize this message input stream. The input stream is closed if it is
	 * not closed already.
	 */
	protected void finalize()
		{
		if (myPacketQueue != null)
			{
			doClose();
			}
		}

	}
