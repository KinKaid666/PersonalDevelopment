//******************************************************************************
//
// File:    Test01Sender.java
// Package: edu.rit.m2mp.test
// Unit:    Class edu.rit.m2mp.test.Test01Sender
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

import edu.rit.m2mp.OutgoingMessage;
import edu.rit.m2mp.Protocol;

import edu.rit.m2mp.ip.M2MPRouterChannel;

import java.io.OutputStream;
import java.io.PrintStream;

import java.util.Random;

/**
 * Class Test01Sender is a unit test main program for M2MP with an {@link
 * edu.rit.m2mp.ip.M2MPRouterChannel
 * </CODE>M2MPRouterChannel<CODE>}. It continually sends M2MP messages
 * at a given rate. Each message consists of a given prefix, a number <I>n</I>
 * which increases by 1 each message, and a string of <I>n</I> digits. The
 * program runs forever or until killed.
 * <P>
 * Usage: java edu.rit.m2mp.test.Test01Sender <I>prefix delay</I>
 * <BR>
 * <I>prefix</I> = Message prefix string
 * <BR>
 * <I>delay</I> = Delay between messages (msec)
 * <P>
 * <B>M2MP Library Version 02-Jul-2002</B>
 *
 * @author  Alan Kaminsky
 * @version 02-Jul-2002
 */
public class Test01Sender
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
			if (args.length != 2)
				{
				System.err.println ("Usage: java edu.rit.m2mp.test.Test01Sender <prefix> <delay>");
				exitCode = 1;
				return;
				}

			String prefix = args[0];
			long delay = Long.parseLong (args[1]);

			Random prng = new Random (System.currentTimeMillis());

			Protocol theProtocol = new Protocol
				(/*theChannel        */ new M2MPRouterChannel(),
				 /*theKey1           */ prng.nextLong(),
				 /*theKey2           */ prng.nextLong(),
				 /*theBufferCount    */ 100,
				 /*theTimeoutInterval*/ 2000L);

			int seqno = 0;
			for (;;)
				{
				OutgoingMessage msg = theProtocol.createOutgoingMessage (null);
				OutputStream os = msg.openOutputStream();
				PrintStream ps = new PrintStream (os);
				ps.print (prefix);
				ps.print (' ');
				ps.print (++ seqno);
				ps.print (' ');
				for (int i = 1; i <= seqno; ++ i)
					{
					ps.print (i % 10);
					}
				ps.close();
				Thread.sleep (delay);
				}
			}

		catch (Throwable exc)
			{
			System.err.println ("Test01Sender: Uncaught exception");
			exc.printStackTrace (System.err);
			exitCode = 1;
			}

		finally
			{
			System.exit (exitCode);
			}
		}

	}
