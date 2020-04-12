//******************************************************************************
//
// File:    M2MIWatch.java
// Package: edu.rit.m2mi.test
// Unit:    Class edu.rit.m2mi.test.M2MIWatch
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

import edu.rit.m2mi.Eoid;

import edu.rit.m2mp.IncomingMessageNotifier;
import edu.rit.m2mp.MessageFilter;
import edu.rit.m2mp.Protocol;

import edu.rit.m2mp.ip.M2MPRouterChannel;

import java.io.InputStream;
import java.io.DataInputStream;
import java.io.IOException;

/**
 * Class M2MIWatch is a main program for watching M2MI invocation messages on
 * the broadcast network. It sets up an M2MP protocol instance with an M2MP
 * Router channel (which is the default M2MP layer the M2MI layer uses). Then it
 * receives M2MI invocation messages and dumps each one on the standard output.
 * Each line of output contains these items (separated by commas):
 * <UL>
 * <LI>
 * Magic number for M2MI messages, 1295142217 (0x4D324D49, or "M2MI" in ASCII).
 * <LI>
 * Hash code each M2MI layer uses to decide whether to process the message.
 * <LI>
 * Exported object identifier (EOID).
 * <LI>
 * Target interface name.
 * <LI>
 * Target method name.
 * <LI>
 * Target method descriptor.
 * <LI>
 * Number of bytes of the serialized method arguments.
 * <LI>
 * Bytes of the serialized method arguments, if any.
 * </UL>
 * <P>
 * Usage: java edu.rit.m2mi.test.M2MIWatch
 * <P>
 * <B>M2MI Library Version 02-Jul-2002</B>
 *
 * @author  Alan Kaminsky
 * @version 27-Jun-2002
 */
public class M2MIWatch
	{

	private static final byte[] thePrefix = new byte[]
		{(byte)0x4D, (byte)0x32, (byte)0x4D, (byte)0x49};

	private static final char[] hexdigit = new char[]
		{'0', '1', '2', '3', '4', '5', '6', '7',
		 '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

	public static void main
		(String[] args)
		{
		int exitCode = 0;

		try
			{
			Protocol theProtocol = new Protocol
				(new M2MPRouterChannel(),
				 1234L,
				 5678L,
				 100,
				 2000L);

			IncomingMessageNotifier theNotifier =
				theProtocol.createIncomingMessageNotifier();
			theNotifier.addMessageFilter (new MessageFilter (thePrefix));

			for (;;)
				{
				InputStream theInputStream = null;

				try
					{
					theInputStream = theNotifier.accept().openInputStream();
					DataInputStream dis = new DataInputStream (theInputStream);

					// The following items are read. Items 1 and 2 constitute
					// the M2MP message prefix.

					// 1. Magic number.
					System.out.print (dis.readInt());

					// 2. Hash code of the export map key.
					System.out.print (',');
					System.out.print (dis.readInt());

					// 3. EOID.
					System.out.print (',');
					System.out.print (new Eoid (dis));

					// 4. Target interface name.
					System.out.print (',');
					System.out.print (dis.readUTF());

					// 5. Target method name.
					System.out.print (',');
					System.out.print (dis.readUTF());

					// 6. Target method descriptor.
					System.out.print (',');
					System.out.print (dis.readUTF());

					// 7. Number of argument bytes, or 0 if there are no
					// arguments.
					System.out.print (',');
					System.out.print (dis.readInt());

					// 8. Argument bytes if any.
					int c;
					while ((c = dis.read()) != -1)
						{
						System.out.print (',');
						System.out.print (hexdigit[(c >> 4) & 0xF]);
						System.out.print (hexdigit[(c     ) & 0xF]);
						}

					dis.close();
					System.out.println();
					}

				catch (IOException exc)
					{
					System.out.println();
					exc.printStackTrace (System.out);
					System.out.println();
					}

				finally
					{
					if (theInputStream != null)
						{
						try
							{
							theInputStream.close();
							} 
						catch (IOException exc)
							{
							}
						}
					}
				}
			}

		catch (Throwable exc)
			{
			System.err.println ("M2MIWatch: Uncaught exception");
			exc.printStackTrace (System.err);
			exitCode = 1;
			}

		finally
			{
			System.exit (exitCode);
			}
		}

	}
