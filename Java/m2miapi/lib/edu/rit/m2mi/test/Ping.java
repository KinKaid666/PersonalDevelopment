//******************************************************************************
//
// File:    Ping.java
// Package: edu.rit.m2mi.test
// Unit:    Class edu.rit.m2mi.test.Ping
//
// This Java source file is copyright (C) 2002 by the Rochester Institute of
// Technology. All rights reserved. For further information, contact the author,
// Alan Kaminsky, at ark@it.rit.edu.
//
// This Java source file is part of the M2MI Library ("The Library"). The
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

package edu.rit.m2mi.test;

import edu.rit.m2mi.M2MI;
import edu.rit.m2mi.SynthesisException;

/**
 * Class Ping is a main program for testing M2MI. It creates and exports an
 * object that implements interface {@link PingPong </CODE>PingPong<CODE>} which
 * processes pings and ignores pongs. The program runs forever or until killed.
 * After running the Ping program in one process and the {@link Pong
 * </CODE>Pong<CODE>} program in another process, run the {@link StartPingPong
 * </CODE>StartPingPong<CODE>} program in a third process to kick things off.
 * <P>
 * Usage: java edu.rit.m2mi.test.Ping
 * <P>
 * <B>M2MI Library Version 02-Jul-2002</B>
 *
 * @author  Alan Kaminsky
 * @version 31-May-2002
 */
public class Ping
	implements PingPong
	{

// Hidden data members.

	private PingPong allPingPongs;

// Hidden constructors.

	/**
	 * Construct a new Ping object.
	 */
	private Ping()
		throws SynthesisException
		{
		allPingPongs = (PingPong) M2MI.getOmnihandle (PingPong.class);
		}

// Exported operations.

	/**
	 * Do a ping. Print <TT>"Ping <I>i</I>"</TT>, wait one second, and call
	 * <TT>pong(i+1)</TT>.
	 *
	 * @param  i  Ping number.
	 */
	public void ping
		(int i)
		{
		System.out.print ("Ping ");
		System.out.println (i);
		try
			{
			Thread.sleep (1000L);
			}
		catch (InterruptedException exc)
			{
			}
		allPingPongs.pong (i+1);
		}

	/**
	 * Do a pong. Ignored.
	 *
	 * @param  i  Pong number.
	 */
	public void pong
		(int i)
		{
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
			M2MI.initialize (1234L);
			M2MI.export (new Ping(), PingPong.class);
			Thread.currentThread().join();
			}

		catch (Throwable exc)
			{
			System.err.println ("Ping: Uncaught exception");
			exc.printStackTrace (System.err);
			exitCode = 1;
			}

		finally
			{
			System.exit (exitCode);
			}
		}

	}
