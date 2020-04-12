//******************************************************************************
//
// File:    Test01Receiver.java
// Package: edu.rit.m2mp.test
// Unit:    Class edu.rit.m2mp.test.Test01Receiver
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
import edu.rit.m2mp.Protocol;

import edu.rit.m2mp.ip.M2MPRouterChannel;

import java.io.InputStream;

import java.util.Random;

/**
 * Class Test01Receiver is a unit test main program for M2MP with an {@link
 * edu.rit.m2mp.ip.M2MPRouterChannel </CODE>M2MPRouterChannel<CODE>}. It
 * continually receives M2MP messages that match a given message prefix, and
 * dumps each message on the standard output. The program runs forever or until
 * killed.
 * <P>
 * Usage: java edu.rit.m2mp.test.Test01Sender <I>prefix</I>
 * <BR>
 * <I>prefix</I> = Message prefix string
 * <P>
 * <B>M2MP Library Version 02-Jul-2002</B>
 *
 * @author  Alan Kaminsky
 * @version 02-Jul-2002
 */
public class Test01Receiver
	{

	/**
	 * Unit test main program.
	 */
	public static void main
		(String[] args)
		{
		int exitCode = 0;

		try
			{
			if (args.length != 1)
				{
				System.err.println ("Usage: java edu.rit.m2mp.test.Test01Receiver <prefix>");
				exitCode = 1;
				return;
				}

			String prefix = args[0];

			Random prng = new Random (System.currentTimeMillis());

			Protocol theProtocol = new Protocol
				(/*theChannel        */ new M2MPRouterChannel(),
				 /*theKey1           */ prng.nextLong(),
				 /*theKey2           */ prng.nextLong(),
				 /*theBufferCount    */ 100,
				 /*theTimeoutInterval*/ 2000L);

			IncomingMessageNotifier theNotifier =
				theProtocol.createIncomingMessageNotifier();
			theNotifier.addMessageFilter
				(new MessageFilter (prefix.getBytes()));

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
			System.err.println ("Test01Receiver: Uncaught exception");
			exc.printStackTrace (System.err);
			exitCode = 1;
			}

		finally
			{
			System.exit (exitCode);
			}
		}

	}
