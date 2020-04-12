//******************************************************************************
//
// File:    Test02.java
// Package: edu.rit.m2mp.test
// Unit:    Class edu.rit.m2mp.test.Test02
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

package edu.rit.m2mp.test;

import edu.rit.m2mp.IncomingMessage;
import edu.rit.m2mp.IncomingMessageNotifier;
import edu.rit.m2mp.MessageFilter;
import edu.rit.m2mp.OutgoingMessage;
import edu.rit.m2mp.Protocol;

import edu.rit.m2mp.ip.M2MPRouterChannel;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;

import java.util.Random;

/**
 * Class Test02 is a unit test main program for M2MP with an {@link
 * edu.rit.m2mp.ip.M2MPRouterChannel </CODE>M2MPRouterChannel<CODE>}. It spawns
 * two threads. One thread continually sends M2MP messages at a given rate. Each
 * message consists of a given prefix, a number <I>n</I> which increases by 1
 * each message, and a string of <I>n</I> digits. The other thread continually
 * receives M2MP messages that match a given message prefix, and dumps each
 * message on the standard output. Both threads use the same M2MP protocol
 * instance. The program runs forever or until killed.
 * <P>
 * Since M2MP channels are not supposed to receive their own outgoing messages,
 * the receiver thread should not receive and print the sender thread's
 * messages. If multiple copies of the program with the same message prefix run
 * in separate processes, each copy should receive and print the other copies'
 * messages, but not its own messages.
 * <P>
 * Usage: java edu.rit.m2mp.test.Test02 <I>recvprefix sendprefix delay</I>
 * <BR>
 * <I>recvprefix</I> = Message prefix string for received messages
 * <BR>
 * <I>sendprefix</I> = Message prefix string for sent messages
 * <BR>
 * <I>delay</I> = Delay between messages (msec)
 * <P>
 * <B>M2MP Library Version 02-Jul-2002</B>
 *
 * @author  Alan Kaminsky
 * @version 02-Jul-2002
 */
public class Test02
	{

	/**
	 * Sending thread.
	 */
	private static class Sender
		extends Thread
		{
		private String myPrefix;
		private long myDelay;
		private Protocol myProtocol;

		public Sender
			(String thePrefix,
			 long theDelay,
			 Protocol theProtocol)
			{
			myPrefix = thePrefix;
			myDelay = theDelay;
			myProtocol = theProtocol;
			}

		public void run()
			{
			try
				{
				int seqno = 0;
				for (;;)
					{
					OutgoingMessage msg =
						myProtocol.createOutgoingMessage (null);
					OutputStream os = msg.openOutputStream();
					PrintStream ps = new PrintStream (os);
					ps.print (myPrefix);
					ps.print (' ');
					ps.print (++ seqno);
					ps.print (' ');
					for (int i = 1; i <= seqno; ++ i)
						{
						ps.print (i % 10);
						}
					ps.close();
					Thread.sleep (myDelay);
					}
				}

			catch (Throwable exc)
				{
				System.err.println ("Test02.Sender: Uncaught exception");
				exc.printStackTrace (System.err);
				System.exit (1);
				}
			}
		}

	/**
	 * Receiving thread.
	 */
	private static class Receiver
		extends Thread
		{
		private String myPrefix;
		private Protocol myProtocol;

		public Receiver
			(String thePrefix,
			 Protocol theProtocol)
			{
			myPrefix = thePrefix;
			myProtocol = theProtocol;
			}

		public void run()
			{
			try
				{
				IncomingMessageNotifier theNotifier =
					myProtocol.createIncomingMessageNotifier();
				theNotifier.addMessageFilter
					(new MessageFilter (myPrefix.getBytes()));

				int c;
				for (;;)
					{
					IncomingMessage msg = theNotifier.accept();
					InputStream is = msg.openInputStream();
					while ((c = is.read()) != -1)
						{
						System.out.write (c);
						}
					is.close();
					System.out.println();
					}
				}

			catch (Throwable exc)
				{
				System.err.println ("Test02.Receiver: Uncaught exception");
				exc.printStackTrace (System.err);
				System.exit (1);
				}
			}
		}

	/**
	 * Unit test main program.
	 */
	public static void main
		(String[] args)
		{
		int exitCode = 0;

		try
			{
			if (args.length != 3)
				{
				System.err.println ("Usage: java edu.rit.m2mp.test.Test02 <recvprefix> <sendprefix> <delay>");
				exitCode = 1;
				return;
				}

			String recvprefix = args[0];
			String sendprefix = args[1];
			long delay = Long.parseLong (args[2]);

			Random prng = new Random (System.currentTimeMillis());

			Protocol theProtocol = new Protocol
				(/*theChannel        */ new M2MPRouterChannel(),
				 /*theKey1           */ prng.nextLong(),
				 /*theKey2           */ prng.nextLong(),
				 /*theBufferCount    */ 100,
				 /*theTimeoutInterval*/ 2000L);

			new Receiver (recvprefix, theProtocol) .start();
			new Sender (sendprefix, delay, theProtocol) .start();

			Thread.currentThread().join();
			}

		catch (Throwable exc)
			{
			System.err.println ("Test02: Uncaught exception");
			exc.printStackTrace (System.err);
			exitCode = 1;
			}

		finally
			{
			System.exit (exitCode);
			}
		}

	}
