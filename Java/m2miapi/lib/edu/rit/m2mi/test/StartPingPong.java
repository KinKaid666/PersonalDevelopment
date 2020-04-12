//******************************************************************************
//
// File:    StartPingPong.java
// Package: edu.rit.m2mi.test
// Unit:    Class edu.rit.m2mi.test.StartPingPong
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
 * Class StartPingPong is a main program for testing M2MI. It calls
 * <TT>ping(1)</TT> on an omnihandle. After running the {@link Ping
 * </CODE>Ping<CODE>} program in one process and the {@link Pong
 * </CODE>Pong<CODE>} program in another process, run the StartPingPong program
 * in a third process to kick things off.
 * <P>
 * Usage: java edu.rit.m2mi.test.StartPingPong
 * <P>
 * <B>M2MI Library Version 02-Jul-2002</B>
 *
 * @author  Alan Kaminsky
 * @version 31-May-2002
 */
public class StartPingPong
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
			M2MI.initialize (3456L);
			PingPong allPingPongs = (PingPong)
				M2MI.getOmnihandle (PingPong.class);
			allPingPongs.ping (1);
			}

		catch (Throwable exc)
			{
			System.err.println ("StartPingPong: Uncaught exception");
			exc.printStackTrace (System.err);
			exitCode = 1;
			}

		finally
			{
			System.exit (exitCode);
			}
		}

	}
