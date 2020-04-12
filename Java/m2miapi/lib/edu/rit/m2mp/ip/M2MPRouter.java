//******************************************************************************
//
// File:    M2MPRouter.java
// Package: edu.rit.m2mp.ip
// Unit:    Class edu.rit.m2mp.ip.M2MPRouter
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

package edu.rit.m2mp.ip;

import java.io.IOException;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
/*
** Removed by etf2954
**
import java.net.InetSocketAddress;
*/
import java.net.MulticastSocket;
/*
** Removed by etf2954
**
import java.net.SocketAddress;
*/
import java.net.UnknownHostException;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Class M2MPRouter is a main program that routes M2MP messages among processes
 * in the local host and in other hosts on the network.
 * <P>
 * The M2MP Router receives M2MP packets from two places:
 * <OL TYPE=1>
 * <P><LI>
 * From processes on the local host, which send M2MP packets as UDP datagrams to
 * the internal IP address (127.0.0.1) and a designated port number (default
 * 5678).
 * <P><LI>
 * From processes on other hosts, which send M2MP packets as UDP datagrams to a
 * designated incoming external IP address and a designated port number (default
 * 5678).
 * </OL>
 * <P>
 * When the M2MP Router receives a packet from the internal IP address, the M2MP
 * Router records the packet's source IP address and port number, which
 * designate a process interested in receiving M2MP traffic. If a process does
 * not send any packets to the M2MP Router for a period of one minute, the M2MP
 * Router forgets that process. If it has not sent an actual M2MP packet for
 * half a minute, each process sends the M2MP Router a zero-length packet so the
 * M2MP Router will not forget the process.
 * <P>
 * When the M2MP Router receives an actual M2MP packet from the internal IP
 * address, the M2MP Router sends a copy of the packet as a UDP datagram to each
 * internal process's IP address and port number, except for the process that
 * originated the packet. The M2MP Router also sends a copy of the packet as a
 * UDP datagram to a designated outgoing external IP address and a designated
 * port number (default 5678).
 * <P>
 * When the M2MP Router receives a packet from the incoming external IP address,
 * the M2MP Router sends a copy of the packet as a UDP datagram to each internal
 * process's IP address and port number. However, if the packet had originally
 * been sent by the M2MP Router itself on the outgoing external IP address, the
 * packet is not sent to the internal processes.
 * <P>
 * There are three possibilities for designating the M2MP Router's incoming and
 * outgoing external IP addresses:
 * <OL TYPE=1>
 * <P><LI>
 * Don't send or receive external packets at all.
 * <P><LI>
 * An IP multicast address in the range 224.0.0.0 to 239.255.255.255 is used
 * both as the incoming external IP address and the outgoing external IP
 * address. In this case packets sent to the external network from one host will
 * be received by all hosts in the multicast group, thus providing a broadcast
 * communication medium between all those hosts.
 * <P><LI>
 * The M2MP Router's host's own IP address is used as the incoming external IP
 * address, and some other host's IP address is used as the outgoing external IP
 * address. In this case packets sent to the external network from one host will
 * be received by the other host, thus providing a broadcast communication
 * medium between just those two hosts (an "M2MP tunnel").
 * </OL>
 * <P>
 * For an application to use the M2MP Router to broadcast M2MP messages, the
 * application must configure its M2MP layer to use an {@link M2MPRouterChannel
 * </CODE>M2MPRouterChannel<CODE>}. The M2MP router channel takes care of all
 * the details of interfacing the M2MP layer with the M2MP Router.
 * <P>
 * The three possible ways to run the M2MP Router program are:
 * <OL TYPE=1>
 * <P><LI>
 * Usage: <TT><B>java edu.rit.m2mp.ip.M2MPRouter</B>
 * [{<B>-p</B>|<B>--port</B>} <I>port</I>]
 * [{<B>-v</B>|<B>--verbose</B>}]</TT>
 * <BR>
 * Internal packets are received from 127.0.0.1:<I>port</I> (default port is
 * 5678) and are broadcast internally. External packets are not received or
 * sent. If the verbose flag is present, each incoming and outgoing datagram is
 * reported on the standard output.
 * <P><LI>
 * Usage: <TT><B>java edu.rit.m2mp.ip.M2MPRouter</B>
 * {<B>-m</B>|<B>--multicastaddr</B>} <I>multicastaddr</I>
 * [{<B>-p</B>|<B>--port</B>} <I>port</I>]
 * [{<B>-t</B>|<B>--ttl</B>} <I>ttl</I>]
 * [{<B>-v</B>|<B>--verbose</B>}]</TT>
 * <BR>
 * The <I>multicastaddr</I> must be a multicast IP address in the range
 * 224.0.0.0 to 239.255.255.255. Internal packets are received from
 * 127.0.0.1:<I>port</I> (default port is 5678), are broadcast internally, and
 * are sent to <I>multicastaddr:port</I> with the time-to-live field set to
 * <I>ttl</I> (default TTL is 1). External packets are received from
 * <I>multicastaddr:port</I>, and are broadcast internally (except external
 * packets sent by this M2MP Router are ignored). If the verbose flag is
 * present, each incoming and outgoing datagram is reported on the standard
 * output.
 * <P><LI>
 * Usage: <TT><B>java edu.rit.m2mp.ip.M2MPRouter</B>
 * {<B>-s</B>|<B>--sendaddr</B>} <I>sendaddr</I>
 * {<B>-r</B>|<B>--recvaddr</B>} <I>recvaddr</I>
 * [{<B>-p</B>|<B>--port</B>} <I>port</I>]
 * [{<B>-v</B>|<B>--verbose</B>}]</TT>
 * <BR>
 * Both the <I>sendaddr</I> and the <I>recvaddr</I> must be unicast IP addresses
 * or hostnames. Internal packets are received from 127.0.0.1:<I>port</I>
 * (default port is 5678), are broadcast internally, and are sent to
 * <I>sendaddr:port</I>. External packets are received from
 * <I>recvaddr:port</I>, and are broadcast internally. If the verbose flag is
 * present, each incoming and outgoing datagram is reported on the standard
 * output.
 * </OL>
 * <P>
 * <B>M2MP Library Version 02-Jul-2002</B>
 *
 * @author  Alan Kaminsky
 * @version 02-Jul-2002
 */
public class M2MPRouter
	{

// Constants.

	private static final int BUFLEN = 508;
	private static final long PROCESS_LEASE_DURATION = 60000L;
	private static final long MSGID_LEASE_DURATION = 5000L;

// Global variables.

	// Address for receiving internal datagrams.
	/*
	** Modified by etf2954
	**
	private static SocketAddress theInternalAddress;
	*/
	private static InetSocketAddress theInternalAddress;

	// Socket for receiving and sending internal datagrams.
	private static DatagramSocket theInternalSocket;

	// Address for receiving external datagrams.
	/*
	** Modified by etf2954
	**
	private static InetSocketAddress xternalReceiveAddress;
	*/
	private static InetSocketAddress theExternalReceiveAddress;

	// Address for sending external datagrams.
	/*
	** Modified by etf2954
	**
	private static SocketAddress theExternalSendAddress;
	*/
	private static InetSocketAddress theExternalSendAddress;

	// Socket for receiving and sending external datagrams.
	private static DatagramSocket theExternalSocket;

	// Map from an internal process's address (type SocketAddress) to the time
	// at which we last received a packet from that process (type Long).
	private static HashMap theInternalProcesses = new HashMap();

	// Map from a message ID (type Integer) to the time at which we last sent an
	// external packet with that message ID (type Long).
	private static HashMap theMessageIDs = new HashMap();

	// Packet counter.
	private static int thePacketCount = 0;

	// Verbose flag.
	private static boolean verbose = false;

// Hidden constructors.

	private M2MPRouter()
		{
		}

// Hidden operations.

	/**
	 * Print a usage message and abort.
	 */
	private static void usage()
		{
		System.err.println ("Usage:");
		System.err.println ("java edu.rit.m2mp.ip.M2MPRouter [{-p|--port} <port>] [{-v|--verbose}]");
		System.err.println ("    -- No external packets");
		System.err.println ("java edu.rit.m2mp.ip.M2MPRouter {-m|--multicastaddr} <multicastaddr> [{-p|--port} <port>] [{-t|--ttl} <ttl>] [{-v|--verbose}]");
		System.err.println ("    -- Multicast external packets");
		System.err.println ("java edu.rit.m2mp.ip.M2MPRouter {-s|--sendaddr} <sendaddr> {-r|--recvaddr} <recvaddr> [{-p|--port} <port>] [{-v|--verbose}]");
		System.err.println ("    -- Unicast external packets");
		System.err.println ("<multicastaddr> = External packet multicast IP address or hostname");
		System.err.println ("<sendaddr> = External packet send IP address or hostname");
		System.err.println ("<recvaddr> = External packet receive IP address or hostname");
		System.err.println ("<port> = Port number, default 5678");
		System.err.println ("<ttl> = Multicast packet time-to-live, default 1");
		System.exit (1);
		}

	/**
	 * Record an interested internal process. Assumes the calling thread has
	 * locked <TT>theInternalProcesses</TT>.
	 *
	 * @param  source  Socket address of the internal process.
	 * @param  now     Time at which a packet was received from the internal
	 *                 process.
	 */
	private static void recordInternalProcess
		/* 
		** Modified by etf2954
		**
		(SocketAddress source,
		*/
		(InetSocketAddress source,
		 long now)
		{
		theInternalProcesses.put (source, new Long (now));
		}

	/**
	 * Broadcast the given packet to all interested internal processes. Also
	 * purge internal processes whose leases have expired. Assumes the calling
	 * thread has locked <TT>theInternalProcesses</TT>.
	 *
	 * @param  buf     Byte buffer containing bytes to send.
	 * @param  len     Number of bytes to send.
	 * @param  source  Source of the packet. If non-null, do not send packet
	 *                 to its own source.
	 * @param  now     Time at which packet was received.
	 * @param  count   Packet count.
	 */
	private static void internalBroadcast
		(byte[] buf,
		 int len,
		 /*
		 ** Modified by etf2954
		 **
		 SocketAddress source,
		 */
		 InetSocketAddress source,
		 long now,
		 int count)
		{
		// Do all internal processes.
		Iterator iter = theInternalProcesses.entrySet().iterator();
		while (iter.hasNext())
			{
			// Get destination socket address and last lease renewal time.
			Map.Entry entry = (Map.Entry) iter.next();
			/*
			** Modified by etf2954
			SocketAddress destination = (SocketAddress) entry.getKey();
			*/
			InetSocketAddress destination = (InetSocketAddress) entry.getKey();
			long lastRenewal = ((Long) entry.getValue()).longValue();

			// If this process's lease has expired, purge it.
			if (now - lastRenewal > PROCESS_LEASE_DURATION)
				{
				iter.remove();
				}

			// Send packet to destination (unless it came from there). Ignore
			// I/O errors.
			else if (! destination.equals (source))
				{
				if (verbose)
					{
					System.out.println
						("Packet " + count + " --> " + destination);
					}
				try
					{
					/*
					** Modifed by etf2954
					theInternalSocket.send
						(new DatagramPacket
							(buf, len, destination));
					*/
					theInternalSocket.send
						(new DatagramPacket
							(buf, len, destination.getAddress(), destination.getPort()));
					}
				catch (IOException exc)
					{
					}
				}
			}
		}

	/**
	 * Extract the message ID (first four bytes) from the given packet.
	 *
	 * @param  buf  Byte buffer.
	 */
	private static int getMessageID
		(byte[] buf)
		{
		return
			((buf[0] & 0xFF) << 24) |
			((buf[1] & 0xFF) << 16) |
			((buf[2] & 0xFF) <<  8) |
			((buf[3] & 0xFF)      );
		}

	/**
	 * Scan the list of message IDs and remove any expired ones. Assumes the
	 * calling thread has locked <TT>theMessageIDs</TT>.
	 *
	 * @param  now  Time now.
	 */
	private static void expireMessageIDs
		(long now)
		{
		Iterator iter = theMessageIDs.entrySet().iterator();
		while (iter.hasNext())
			{
			Map.Entry entry = (Map.Entry) iter.next();
			long lastRenewal = ((Long) entry.getValue()).longValue();
			if (now - lastRenewal > MSGID_LEASE_DURATION)
				{
				iter.remove();
				}
			}
		}

	/**
	 * Send the given packet to the external network.
	 *
	 * @param  buf     Byte buffer containing bytes to send.
	 * @param  len     Number of bytes to send.
	 * @param  now     Time at which packet was received.
	 * @param  count   Packet count.
	 */
	private static void externalSend
		(byte[] buf,
		 int len,
		 long now,
		 int count)
		{
		// Make sure we are sending externally.
		if (theExternalSocket != null)
			{
			synchronized (theMessageIDs)
				{
				// Scan list of message IDs and discard any expired ones.
				expireMessageIDs (now);

				// Record message ID and time it was sent.
				theMessageIDs.put
					(new Integer (getMessageID (buf)),
					 new Long (now));
				}

			// Send packet to external network. Ignore I/O errors.
			if (verbose)
				{
				System.out.println
					("Packet " + count + " --> " + theExternalSendAddress);
				}
			try
				{
				/*
				** Modified by etf2954
				**
				theExternalSocket.send
					(new DatagramPacket
						(buf, len, theExternalSendAddress));
				*/
				theExternalSocket.send
					(new DatagramPacket
						(buf, len, theExternalSendAddress.getAddress(), theExternalSendAddress.getPort()));
				}
			catch (IOException exc)
				{
				}
			}
		}

// Hidden thread.

	/**
	 * Thread for receiving and processing external packets.
	 */
	private static class Receiver
		extends Thread
		{
		public void run()
			{
			try
				{
				// Create buffer for external packets.
				byte[] buf = new byte [BUFLEN];

				// Repeatedly receive and process external packets.
				for (;;)
					{
					// Receive a packet.
					DatagramPacket packet =
						new DatagramPacket (buf, buf.length);
					theExternalSocket.receive (packet);
					long now = System.currentTimeMillis();
					int len = packet.getLength();
					/*
					** Modified by etf2954
					SocketAddress source = packet.getSocketAddress();
					*/
					InetSocketAddress source = new InetSocketAddress( packet.getAddress(), packet.getPort());

					// Log packet arrival.
					int count = ++ thePacketCount;
					if (verbose)
						{
						System.out.println (source + " --> Packet " + count);
						}

					boolean sentByMyself;
					synchronized (theMessageIDs)
						{
						// Scan list of message IDs and discard any expired
						// ones.
						expireMessageIDs (now);

						// Check whether I sent this packet myself.
						sentByMyself =
							theMessageIDs.containsKey
								(new Integer (getMessageID (buf)));
						}

					// Broadcast packet internally, unless I sent it myself.
					if (! sentByMyself)
						{
						synchronized (theInternalProcesses)
							{
							internalBroadcast (buf, len, null, now, count);
							}
						}
					}
				}

			catch (Throwable exc)
				{
				System.err.println ("M2MPRouter.Receiver: Uncaught exception");
				exc.printStackTrace (System.err);
				System.exit (1);
				}
			}
		}

// Exported operations.

	/**
	 * Main program.
	 */
	public static void main
		(String[] args)
		{
		try
			{
			// Set default argument values.
			InetAddress sendaddr = null;
			InetAddress recvaddr = null;
			InetAddress multicastaddr = null;
			Integer portHolder = null;
			Integer ttlHolder = null;

			// Parse arguments.
			int i = 0;
			while (i < args.length)
				{
				if
					(args[i].equals ("-s") ||
					 args[i].equals ("--sendaddr"))
					{
					if (sendaddr != null || i+1 >= args.length)
						{
						usage();
						}
					try
						{
						sendaddr = InetAddress.getByName (args[i+1]);
						if (sendaddr.isMulticastAddress())
							{
							throw new UnknownHostException();
							}
						}
					catch (UnknownHostException exc)
						{
						System.err.println
							("M2MPRouter: Bad sendaddr: \"" +
							 args[i+1] + "\"");
						System.exit (1);
						}
					i += 2;
					}
				else if
					(args[i].equals ("-r") ||
					 args[i].equals ("--recvaddr"))
					{
					if (recvaddr != null || i+1 >= args.length)
						{
						usage();
						}
					try
						{
						recvaddr = InetAddress.getByName (args[i+1]);
						if (recvaddr.isMulticastAddress())
							{
							throw new UnknownHostException();
							}
						}
					catch (UnknownHostException exc)
						{
						System.err.println
							("M2MPRouter: Bad recvaddr: \"" +
							 args[i+1] + "\"");
						System.exit (1);
						}
					i += 2;
					}
				else if
					(args[i].equals ("-m") ||
					 args[i].equals ("--multicastaddr"))
					{
					if (multicastaddr != null || i+1 >= args.length)
						{
						usage();
						}
					try
						{
						multicastaddr = InetAddress.getByName (args[i+1]);
						if (! multicastaddr.isMulticastAddress())
							{
							throw new UnknownHostException();
							}
						}
					catch (UnknownHostException exc)
						{
						System.err.println
							("M2MPRouter: Bad multicastaddr: \"" +
							 args[i+1] + "\"");
						System.exit (1);
						}
					i += 2;
					}
				else if
					(args[i].equals ("-p") ||
					 args[i].equals ("--port"))
					{
					if (portHolder != null || i+1 >= args.length)
						{
						usage();
						}
					try
						{
						portHolder = new Integer (Integer.parseInt (args[i+1]));
						if
							(0 > portHolder.intValue() ||
							 portHolder.intValue() > 65535)
							{
							throw new NumberFormatException();
							}
						}
					catch (NumberFormatException exc)
						{
						System.err.println
							("M2MPRouter: Bad port: \"" +
							 args[i+1] + "\"");
						System.exit (1);
						}
					i += 2;
					}
				else if
					(args[i].equals ("-t") ||
					 args[i].equals ("--ttl"))
					{
					if (ttlHolder != null || i+1 >= args.length)
						{
						usage();
						}
					try
						{
						ttlHolder = new Integer (Integer.parseInt (args[i+1]));
						if
							(1 > ttlHolder.intValue() ||
							 ttlHolder.intValue() > 255)
							{
							throw new NumberFormatException();
							}
						}
					catch (NumberFormatException exc)
						{
						System.err.println
							("M2MPRouter: Bad ttl: \"" +
							 args[i+1] + "\"");
						System.exit (1);
						}
					i += 2;
					}
				else if
					(args[i].equals ("-v") ||
					 args[i].equals ("--verbose"))
					{
					if (verbose)
						{
						usage();
						}
					verbose = true;
					i += 1;
					}
				else
					{
					usage();
					}
				}

			// Set default port and TTL if necessary.
			int port = portHolder == null ? 5678 : portHolder.intValue();
			int ttl = ttlHolder == null ? 1 : ttlHolder.intValue();

			// Establish external addresses and socket.
			if
				(sendaddr == null &&
				 recvaddr == null &&
				 multicastaddr == null)
				{
				// No external communication.
				if (verbose)
					{
					System.out.println ("Not sending or receiving externally");
					}
				}
			else if
				(sendaddr != null &&
				 recvaddr != null &&
				 multicastaddr == null)
				{
				// Send to a single host.
				theExternalReceiveAddress =
					new InetSocketAddress (recvaddr, port);
				theExternalSendAddress =
					new InetSocketAddress (sendaddr, port);
				/*
				** Modified by etf2954
				**
				theExternalSocket =
					new DatagramSocket (theExternalReceiveAddress);
				*/
				theExternalSocket =
					new DatagramSocket (theExternalReceiveAddress.getPort(),
										theExternalReceiveAddress.getAddress());
				if (verbose)
					{
					System.out.println
						("Receiving externally from " +
						 theExternalReceiveAddress);
					System.out.println
						("Sending externally to " +
						 theExternalSendAddress);
					}
				new Receiver().start();
				}
			else if
				(sendaddr == null &&
				 recvaddr == null &&
				 multicastaddr != null)
				{
				// Send to a multicast group.
				theExternalReceiveAddress =
					new InetSocketAddress (multicastaddr, port);
				theExternalSendAddress = theExternalReceiveAddress;
				/*
				** Modified by etf2954
				**
				MulticastSocket mcsocket =
					new MulticastSocket (theExternalReceiveAddress);
				*/
				MulticastSocket mcsocket =
					new MulticastSocket (theExternalReceiveAddress.getPort());
				mcsocket.setInterface (theExternalReceiveAddress.getAddress());
				mcsocket.setTimeToLive (ttl);
				mcsocket.joinGroup (multicastaddr);
				theExternalSocket = mcsocket;
				if (verbose)
					{
					System.out.println
						("Multicasting externally on " +
						 theExternalSendAddress);
					}
				new Receiver().start();
				}
			else
				{
				usage();
				}

			// Establish internal address and socket.
			/*
			** Modified by etf2954
			**
			theInternalAddress = new InetSocketAddress ("127.0.0.1", port);
			*/
			theInternalAddress = new InetSocketAddress (InetAddress.getByName("127.0.0.1"), 
														port);
			/*
			** Modified by etf2954
			**
			theInternalSocket = new DatagramSocket (theInternalAddress);
			*/
			theInternalSocket = new DatagramSocket (theInternalAddress.getPort(),
													theInternalAddress.getAddress());
			if (verbose)
				{
				System.out.println
					("Receiving internally from " + theInternalAddress);
				}

			// Create buffer for internal packets.
			byte[] buf = new byte [BUFLEN];

			// Repeatedly receive and process internal packets.
			for (;;)
				{
				// Receive a packet.
				DatagramPacket packet = new DatagramPacket (buf, buf.length);
				theInternalSocket.receive (packet);
				long now = System.currentTimeMillis();
				int len = packet.getLength();
				/*
				** Modified by etf2954
				**
				SocketAddress source = packet.getSocketAddress();
				*/
				InetSocketAddress source = new InetSocketAddress(packet.getAddress(),packet.getPort());

				// Log packet arrival.
				int count = 0;
				if (len > 0)
					{
					count = ++ thePacketCount;
					if (verbose)
						{
						System.out.println (source + " --> Packet " + count);
						}
					}
				else
					{
					if (verbose)
						{
						System.out.println (source + " --> Present");
						}
					}

				// Renew source process's lease and broadcast packet internally.
				synchronized (theInternalProcesses)
					{
					recordInternalProcess (source, now);
					if (len > 0)
						{
						internalBroadcast (buf, len, source, now, count);
						}
					}

				// Send packet externally.
				if (len > 0)
					{
					externalSend (buf, len, now, count);
					}
				}
			}

		catch (Throwable exc)
			{
			System.err.println ("M2MPRouter: Uncaught exception");
			exc.printStackTrace (System.err);
			System.exit (1);
			}
		}

	}
