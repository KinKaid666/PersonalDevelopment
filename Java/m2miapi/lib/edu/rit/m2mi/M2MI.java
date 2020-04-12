//******************************************************************************
//
// File:    M2MI.java
// Package: edu.rit.m2mi
// Unit:    Class edu.rit.m2mi.M2MI
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

package edu.rit.m2mi;

import edu.rit.m2mp.IncomingMessageNotifier;
import edu.rit.m2mp.Protocol;

import edu.rit.m2mp.ip.M2MPRouterChannel;

import edu.rit.util.TaskThreadPool;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;

import java.util.Collection;
import java.util.Iterator;
import java.util.Random;

/**
 * Class M2MI encapsulates the M2MI layer. An M2MI-based client program only has
 * to use the static methods in class M2MI. The client should not use directly
 * the other classes in package edu.rit.m2mi.
 * <P>
 * The client program must call one (and only one) of the <TT>initialize()</TT>
 * methods to configure the M2MI layer before calling any other method. The
 * following parameters configure the M2MI layer. The first parameter is
 * mandatory; the remaining parameters may be omitted.
 * <UL>
 * <P><LI>
 * A globally unique address for the M2MI layer. The suggested value is the MAC
 * address of the network interface of the device in which the M2MI layer is
 * running. The M2MI layer uses this to generate globally unique exported object
 * identifiers (EOIDs) for the objects that are exported.
 * <P><LI>
 * A {@link edu.rit.m2mp.Protocol </CODE>Protocol<CODE>} object representing the
 * Many-to-Many Protocol (M2MP) layer which the M2MI layer will use to send M2MI
 * invocations to and receive M2MI invocations from other processes and hosts.
 * The M2MP layer may be null, in which case the M2MI layer will not send or
 * receive any external invocations. If omitted, an M2MP Protocol object with an
 * M2MP Router channel is constructed and used (see package {@link edu.rit.m2mp
 * </CODE>edu.rit.m2mp<CODE>} for further information).
 * <P><LI>
 * A random seed that is different each time the M2MI layer is initialized. The
 * M2MI layer uses this to generate globally unique exported object identifiers
 * (EOIDs) for the objects that are exported. If omitted, the value returned by
 * <TT>System.currentTimeMillis()</TT> is used.
 * <P><LI>
 * The maximum number of concurrent invocations the M2MI layer will perform.
 * Each M2MI invocation is performed by a separate thread. This parameter
 * specifies the maximum number of threads the M2MI layer will spawn, and thus
 * the maximum number of concurrent invocations that can happen. Each thread
 * consumes some amount of memory. To minimize memory usage, you should tailor
 * the maximum number of concurrent invocations to be as small as necessary for
 * each application. If omitted, a value of 1 is used.
 * <P><LI>
 * The parent class loader for all the M2MI layer's internal class loaders. If
 * omitted, the calling thread's context class loader is used.
 * </UL>
 * <P>
 * <B>M2MI Library Version 02-Jul-2002</B>
 *
 * @author  Alan Kaminsky
 * @version 27-Jun-2002
 */
public class M2MI
	{

// Hidden data members.

	/**
	 * The M2MI Layer Itself (a singleton instance); null if not initialized.
	 */
	private static M2MI theM2MILayer = null;

	/**
	 * IEEE 802 MAC address for generating EOIDs.
	 */
	private long myMacAddress;

	/**
	 * M2MP protocol object for external invocations, or null not to do external
	 * invocations.
	 */
	private Protocol myProtocol;

	/**
	 * Pseudorandom number generator for generating EOIDs.
	 */
	private Random myPrng;

	/**
	 * Parent class loader.
	 */
	private ClassLoader myParentClassLoader;

	/**
	 * Handle factory for manufacturing handle objects.
	 */
	private HandleFactory myHandleFactory;

	/**
	 * Method invoker cache containing all the method invoker classes.
	 */
	private MethodInvokerCache myMethodInvokerCache;

	/**
	 * Task thread pool containing task threads for running the method invokers.
	 */
	private TaskThreadPool myTaskThreadPool;

	/**
	 * Incoming message notifier for receiving external M2MI invocations, or
	 * null not to receive external M2MI invocations.
	 */
	private IncomingMessageNotifier myNotifier;

	/**
	 * Export map containing all the exported objects.
	 */
	private ExportMap myExportMap;

	/**
	 * Incoming M2MI message receiver thread.
	 */
	private Receiver myReceiver;

// Hidden helper class.

	/**
	 * Class Receiver is a thread that continually reads and processes incoming
	 * M2MI messages.
	 */
	private class Receiver
		extends Thread
		{
		public void run()
			{
			for (;;)
				{
				try
					{
					receiveInvocationMessage();
					}
				catch (Throwable exc)
					{
					System.err.println ("M2MI.Receiver: Uncaught exception");
					exc.printStackTrace (System.err);
					}
				}
			}
		}

// Hidden constructors.

	/**
	 * Construct a new M2MI layer with the given configuration parameters.
	 *
	 * @param  theMacAddress
	 *     IEEE 802 MAC address that will go into the EOIDs this M2MI layer
	 *     generates. The suggested value is the MAC address of the network
	 *     interface of the host in which this M2MI layer is running.
	 * @param  theProtocol
	 *     M2MP Protocol object this M2MI layer will use to send and receive
	 *     external invocations, or null not to send or receive external
	 *     invocations.
	 * @param  theSeed
	 *     Seed for the random numbers that will go into the EOIDs this M2MI
	 *     layer generates. The suggested value is
	 *     <TT>System.currentTimeMillis()</TT>.
	 * @param  maxInvocations
	 *     The maximum number of concurrent invocations this M2MI layer will
	 *     perform.
	 * @param  theParentClassLoader
	 *     Parent class loader for this M2MI layer's internal class loaders.
	 *
	 * @exception  IllegalArgumentException
	 *     (unchecked exception) Thrown if <TT>theMacAddress</TT> is 0,
	 *     <TT>theSeed</TT> is 0, or <TT>maxInvocations</TT> &lt; 1.
	 * @exception  NullPointerException
	 *     (unchecked exception) Thrown if <TT>theParentClassLoader</TT> is
	 *     null.
	 */
	private M2MI
		(long theMacAddress,
		 Protocol theProtocol,
		 long theSeed,
		 int maxInvocations,
		 ClassLoader theParentClassLoader)
		{
		if (theMacAddress == 0L || theSeed == 0L || maxInvocations < 1)
			{
			throw new IllegalArgumentException();
			}
		if (theParentClassLoader == null)
			{
			throw new NullPointerException();
			}
		myMacAddress = theMacAddress;
		myProtocol = theProtocol;
		myPrng = new Random (theSeed);
		myParentClassLoader = theParentClassLoader;
		myHandleFactory = new HandleFactory (theParentClassLoader);
		myMethodInvokerCache = new MethodInvokerCache (theParentClassLoader);
		myTaskThreadPool = new TaskThreadPool (maxInvocations);
		if (myProtocol == null)
			{
			myNotifier = null;
			}
		else
			{
			myNotifier = myProtocol.createIncomingMessageNotifier();
			myReceiver = new Receiver();
			myReceiver.setDaemon (true);
			myReceiver.start();
			}
		myExportMap = new ExportMap (myNotifier);
		}

// Hidden operations.

	/**
	 * Export the given object with the given target interface(s) via this M2MI
	 * layer.
	 *
	 * @param  theObject      Object.
	 * @param  theInterfaces  Array of one or more target interfaces.
	 *
	 * @exception  NullPointerException
	 *     (unchecked exception) Thrown if <TT>theObject</TT> is null,
	 *     <TT>theInterfaces</TT> is null, or any element of
	 *     <TT>theInterfaces</TT> is null.
	 * @exception  IllegalArgumentException
	 *     (unchecked exception) Thrown if <TT>theInterfaces</TT> is zero
	 *     length.
	 * @exception  ClassCastException
	 *     (unchecked exception) Thrown if <TT>theObject</TT> is not an instance
	 *     of one or more of the given target interfaces.
	 * @exception  InvalidMethodException
	 *     (unchecked exception) Thrown if any target interface is a class
	 *     rather than an interface, if any method in any target interface
	 *     returns a value, or if any method in any target interface throws any
	 *     checked exceptions.
	 * @exception  SynthesisException
	 *     Thrown if there was a problem synthesizing or instantiating the
	 *     omnihandle class.
	 */
	private synchronized void privateExport
		(Object theObject,
		 Class[] theInterfaces)
		throws SynthesisException
		{
		// Verify preconditions.
		validateObject (theObject, theInterfaces);

		// Get an omnihandle, verifying that the target interfaces are callable
		// by M2MI. (The omnihandle is not returned.)
		myHandleFactory.createOmnihandle (theInterfaces);

		// Put the object in the export map with the target interfaces and all
		// superinterfaces thereof.
		exportWithInterfaces (theObject, theInterfaces);
		}

	/**
	 * Validate the given object against the given target interface(s).
	 *
	 * @param  theObject      Object.
	 * @param  theInterfaces  Array of one or more target interfaces.
	 *
	 * @exception  NullPointerException
	 *     (unchecked exception) Thrown if <TT>theObject</TT> is null,
	 *     <TT>theInterfaces</TT> is null, or any element of
	 *     <TT>theInterfaces</TT> is null.
	 * @exception  ClassCastException
	 *     (unchecked exception) Thrown if <TT>theObject</TT> is not an instance
	 *     of one or more of the given target interfaces.
	 */
	private void validateObject
		(Object theObject,
		 Class[] theInterfaces)
		{
		if (theObject == null)
			{
			throw new NullPointerException();
			}
		int n = theInterfaces.length;
		for (int i = 0; i < n; ++ i)
			{
			if (! theInterfaces[i].isInstance (theObject))
				{
				throw new ClassCastException
					("Object is not an instance of interface " +
					 theInterfaces[i].getName());
				}
			}
		}

	/**
	 * Put the given object in this M2MI layer's export map with the target
	 * interfaces and all superinterfaces thereof.
	 *
	 * @param  theObject      Object.
	 * @param  theInterfaces  Array of one or more target interfaces.
	 */
	private void exportWithInterfaces
		(Object theObject,
		 Class[] theInterfaces)
		{
		int n = theInterfaces.length;
		for (int i = 0; i < n; ++ i)
			{
			myExportMap.export (theObject, theInterfaces[i].getName());
			Class[] theSuperinterfaces = theInterfaces[i].getInterfaces();
			exportWithInterfaces (theObject, theSuperinterfaces);
			}
		}

	/**
	 * Unexport the given object in this M2MI layer.
	 *
	 * @param  theObject  Object.
	 *
	 * @exception  NullPointerException
	 *     (unchecked exception) Thrown if <TT>theObject</TT> is null.
	 */
	private synchronized void privateUnexport
		(Object theObject)
		{
		if (theObject == null)
			{
			throw new NullPointerException();
			}
		myExportMap.unexport (theObject);
		}

	/**
	 * Obtain an omnihandle for the given target interface(s) from this M2MI
	 * layer.
	 *
	 * @param  theInterfaces  Array of one or more target interfaces.
	 *
	 * @return  Omnihandle for <TT>theInterfaces</TT>.
	 *
	 * @exception  NullPointerException
	 *     (unchecked exception) Thrown if <TT>theInterfaces</TT> is null or any
	 *     element of <TT>theInterfaces</TT> is null.
	 * @exception  IllegalArgumentException
	 *     (unchecked exception) Thrown if <TT>theInterfaces</TT> is zero
	 *     length.
	 * @exception  InvalidMethodException
	 *     (unchecked exception) Thrown if any target interface is a class
	 *     rather than an interface, if any method in any target interface
	 *     returns a value, or if any method in any target interface throws any
	 *     checked exceptions.
	 * @exception  SynthesisException
	 *     Thrown if there was a problem synthesizing or instantiating the
	 *     omnihandle class.
	 */
	private synchronized Omnihandle privateGetOmnihandle
		(Class[] theInterfaces)
		throws SynthesisException
		{
		return myHandleFactory.createOmnihandle (theInterfaces);
		}

	/**
	 * Obtain a unihandle for the given target interface(s), attached to the
	 * given object in this M2MI layer.
	 *
	 * @param  theObject      Object.
	 * @param  theInterfaces  Array of one or more target interfaces.
	 *
	 * @return  Unihandle for <TT>theObject</TT>.
	 *
	 * @exception  NullPointerException
	 *     (unchecked exception) Thrown if <TT>theObject</TT> is null,
	 *     <TT>theInterfaces</TT> is null, or any element of
	 *     <TT>theInterfaces</TT> is null.
	 * @exception  IllegalArgumentException
	 *     (unchecked exception) Thrown if <TT>theInterfaces</TT> is zero
	 *     length.
	 * @exception  ClassCastException
	 *     (unchecked exception) Thrown if <TT>theObject</TT> is not an instance
	 *     of one or more of the given target interfaces.
	 * @exception  InvalidMethodException
	 *     (unchecked exception) Thrown if any target interface is a class
	 *     rather than an interface, if any method in any target interface
	 *     returns a value, or if any method in any target interface throws any
	 *     checked exceptions.
	 * @exception  SynthesisException
	 *     Thrown if there was a problem synthesizing or instantiating the
	 *     unihandle class.
	 */
	private synchronized Unihandle privateGetUnihandle
		(Object theObject,
		 Class[] theInterfaces)
		throws SynthesisException
		{
		// Verify preconditions.
		validateObject (theObject, theInterfaces);

		// Get a unihandle, verifying that the target interfaces are callable by
		// M2MI.
		Eoid theEoid = getUnusedEoid();
		Unihandle theUnihandle =
			myHandleFactory.createUnihandle (theInterfaces, theEoid);

		// Put the object in the export map with the EOID.
		myExportMap.export (theObject, theEoid);

		// Put the object in the export map with the target interfaces and all
		// superinterfaces thereof.
		exportWithInterfaces (theObject, theInterfaces);

		return theUnihandle;
		}

	/**
	 * Returns an unused EOID.
	 */
	private Eoid getUnusedEoid()
		{
		Eoid theEoid;
		do
			{
			theEoid = new Eoid (myMacAddress, myPrng);
			}
		while (myExportMap.isExported (theEoid));
		return theEoid;
		}

	/**
	 * Obtain a multihandle for the given target interface(s) from this M2MI
	 * layer.
	 *
	 * @param  theInterfaces  Array of one or more target interfaces.
	 *
	 * @return  Multihandle for <TT>theInterfaces</TT>.
	 *
	 * @exception  NullPointerException
	 *     (unchecked exception) Thrown if <TT>theInterfaces</TT> is null or any
	 *     element of <TT>theInterfaces</TT> is null.
	 * @exception  IllegalArgumentException
	 *     (unchecked exception) Thrown if <TT>theInterfaces</TT> is zero
	 *     length.
	 * @exception  InvalidMethodException
	 *     (unchecked exception) Thrown if any target interface is a class
	 *     rather than an interface, if any method in any target interface
	 *     returns a value, or if any method in any target interface throws any
	 *     checked exceptions.
	 * @exception  SynthesisException
	 *     Thrown if there was a problem synthesizing or instantiating the
	 *     multihandle class.
	 */
	private synchronized Multihandle privateGetMultihandle
		(Class[] theInterfaces)
		throws SynthesisException
		{
		return myHandleFactory.createMultihandle
			(theInterfaces, getUnusedEoid());
		}

	/**
	 * Attach the given object to the given unihandle in this M2MI layer.
	 *
	 * @param  theObject     Object.
	 * @param  theUnihandle  Unihandle.
	 *
	 * @exception  IllegalStateException
	 *     (unchecked exception) Thrown if <TT>theUnihandle</TT> is not attached
	 *     to an object exported in this M2MI layer.
	 * @exception  NullPointerException
	 *     (unchecked exception) Thrown if <TT>theObject</TT> is null or
	 *     <TT>theUnihandle</TT> is null.
	 * @exception  ClassCastException
	 *     (unchecked exception) Thrown if <TT>theObject</TT> is not an instance
	 *     of one or more of <TT>theUnihandle</TT>'s target interfaces.
	 */
	private synchronized void privateAttachUnihandle
		(Object theObject,
		 Unihandle theUnihandle)
		{
		// Verify preconditions.
		Eoid theEoid = theUnihandle.getEoid();
		if (! myExportMap.isExported (theEoid))
			{
			throw new IllegalStateException
				("Unihandle not attached to an object exported in this process");
			}
		Class[] theInterfaces =
			 theUnihandle.getClass().getSuperclass().getInterfaces();
		validateObject (theObject, theInterfaces);

		// Disassociate the old object with the unihandle's EOID.
		myExportMap.unexport (theEoid);

		// Associate the new object with the unihandle's EOID.
		myExportMap.export (theObject, theEoid);

		// Put the object in the export map with the target interface(s) and all
		// superinterfaces thereof.
		exportWithInterfaces (theObject, theInterfaces);
		}

	/**
	 * Detach the target object from the given unihandle in this M2MI layer.
	 *
	 * @param  theUnihandle  Unihandle.
	 *
	 * @exception  IllegalStateException
	 *     (unchecked exception) Thrown if <TT>theUnihandle</TT> is not attached
	 *     to an object exported in this M2MI layer.
	 * @exception  NullPointerException
	 *     (unchecked exception) Thrown if <TT>theUnihandle</TT> is null.
	 */
	private synchronized void privateDetachUnihandle
		(Unihandle theUnihandle)
		{
		Eoid theEoid = theUnihandle.getEoid();
		if (! myExportMap.isExported (theEoid))
			{
			throw new IllegalStateException
				("Unihandle not attached to an object exported in this process");
			}
		myExportMap.unexport (theEoid);
		}

	/**
	 * Attach the given object to the given multihandle in this M2MI layer.
	 *
	 * @param  theObject       Object.
	 * @param  theMultihandle  Multihandle.
	 *
	 * @exception  NullPointerException
	 *     (unchecked exception) Thrown if <TT>theObject</TT> is null or
	 *     <TT>theMultihandle</TT> is null.
	 * @exception  ClassCastException
	 *     (unchecked exception) Thrown if <TT>theObject</TT> is not an instance
	 *     of one or more of <TT>theMultihandle</TT>'s target interfaces.
	 */
	private synchronized void privateAttachMultihandle
		(Object theObject,
		 Multihandle theMultihandle)
		{
		// Verify preconditions.
		Class[] theInterfaces =
			 theMultihandle.getClass().getSuperclass().getInterfaces();
		validateObject (theObject, theInterfaces);

		// Associate the object with the multihandle's EOID.
		myExportMap.export (theObject, theMultihandle.getEoid());

		// Put the object in the export map with the target interface(s) and all
		// superinterfaces thereof.
		exportWithInterfaces (theObject, theInterfaces);
		}

	/**
	 * Detach the given object from the given multihandle in this M2MI layer.
	 *
	 * @param  theObject       Object.
	 * @param  theMultihandle  Multihandle.
	 *
	 * @exception  NullPointerException
	 *     (unchecked exception) Thrown if <TT>theObject</TT> is null or
	 *     <TT>theMultihandle</TT> is null.
	 */
	 private synchronized void privateDetachMultihandle
		(Object theObject,
		 Multihandle theMultihandle)
		{
		if (theObject == null || theMultihandle == null)
			{
			throw new NullPointerException();
			}
		myExportMap.unexport (theObject, theMultihandle.getEoid());
		}

	/**
	 * Perform a method invocation, originating from an omnihandle, on the
	 * appropriate objects, if any, in this M2MI layer.
	 *
	 * @param  theEoid
	 *     Exported object identifier.
	 * @param  theInterfaceName
	 *     Fully-qualified name of the target interface.
	 * @param  theMethodName
	 *     Name of the target method.
	 * @param  theMethodDescriptor
	 *     Descriptor for the target method, in the format specified by Section
	 *     4.3.3 of the <I>Java Virtual Machine Specification, Second
	 *     Edition.</I>
	 * @param  theArguments
	 *     Byte array containing the serialized arguments for the target method;
	 *     or null if there are no arguments.
	 *
	 * @exception  NullPointerException
	 *     (unchecked exception) Thrown if <TT>theEoid</TT>,
	 *     <TT>theInterfaceName</TT>, <TT>theMethodName</TT>, or
	 *     <TT>theMethodDescriptor</TT> is null.
	 * @exception  InvocationException
	 *     (unchecked exception) Thrown if the invocation was not successful.
	 *     The detail message and/or chained exception contains further
	 *     information.
	 */
	private synchronized void privateInvokeOmnihandle
		(Eoid theEoid,
		 String theInterfaceName,
		 String theMethodName,
		 String theMethodDescriptor,
		 byte[] theArguments)
		{
		Collection theTargets;

		// Invoke local objects exported with the target interface, if any.
		theTargets = myExportMap.getExportedObjects (theInterfaceName);
		if (theTargets != null)
			{
			setUpInvocations
				(theInterfaceName,
				 theMethodName,
				 theMethodDescriptor,
				 theTargets,
				 theArguments);
			}

		// Send an invocation message so remote objects with the target
		// interface, if any, are invoked.
		if (myProtocol != null)
			{
			sendInvocationMessage
				(theEoid,
				 theInterfaceName,
				 theMethodName,
				 theMethodDescriptor,
				 theArguments);
			}
		}

	/**
	 * Perform a method invocation, originating from a unihandle, on the
	 * appropriate objects, if any, in this M2MI layer.
	 *
	 * @param  theEoid
	 *     Exported object identifier.
	 * @param  theInterfaceName
	 *     Fully-qualified name of the target interface.
	 * @param  theMethodName
	 *     Name of the target method.
	 * @param  theMethodDescriptor
	 *     Descriptor for the target method, in the format specified by Section
	 *     4.3.3 of the <I>Java Virtual Machine Specification, Second
	 *     Edition.</I>
	 * @param  theArguments
	 *     Byte array containing the serialized arguments for the target method;
	 *     or null if there are no arguments.
	 *
	 * @exception  NullPointerException
	 *     (unchecked exception) Thrown if <TT>theEoid</TT>,
	 *     <TT>theInterfaceName</TT>, <TT>theMethodName</TT>, or
	 *     <TT>theMethodDescriptor</TT> is null.
	 * @exception  InvocationException
	 *     (unchecked exception) Thrown if the invocation was not successful.
	 *     The detail message and/or chained exception contains further
	 *     information.
	 */
	private synchronized void privateInvokeUnihandle
		(Eoid theEoid,
		 String theInterfaceName,
		 String theMethodName,
		 String theMethodDescriptor,
		 byte[] theArguments)
		{
		Collection theTargets;

		// Invoke local object exported with the EOID, if any.
		theTargets = myExportMap.getExportedObjects (theEoid);
		if (theTargets != null)
			{
			setUpInvocations
				(theInterfaceName,
				 theMethodName,
				 theMethodDescriptor,
				 theTargets,
				 theArguments);
			}

		// If the target object is not exported locally, it must be a remote
		// object. Send an invocation message.
		if (theTargets == null && myProtocol != null)
			{
			sendInvocationMessage
				(theEoid,
				 theInterfaceName,
				 theMethodName,
				 theMethodDescriptor,
				 theArguments);
			}
		}

	/**
	 * Perform a method invocation, originating from a multihandle, on the
	 * appropriate objects, if any, in this M2MI layer.
	 *
	 * @param  theEoid
	 *     Exported object identifier.
	 * @param  theInterfaceName
	 *     Fully-qualified name of the target interface.
	 * @param  theMethodName
	 *     Name of the target method.
	 * @param  theMethodDescriptor
	 *     Descriptor for the target method, in the format specified by Section
	 *     4.3.3 of the <I>Java Virtual Machine Specification, Second
	 *     Edition.</I>
	 * @param  theArguments
	 *     Byte array containing the serialized arguments for the target method;
	 *     or null if there are no arguments.
	 *
	 * @exception  NullPointerException
	 *     (unchecked exception) Thrown if <TT>theEoid</TT>,
	 *     <TT>theInterfaceName</TT>, <TT>theMethodName</TT>, or
	 *     <TT>theMethodDescriptor</TT> is null.
	 * @exception  InvocationException
	 *     (unchecked exception) Thrown if the invocation was not successful.
	 *     The detail message and/or chained exception contains further
	 *     information.
	 */
	private synchronized void privateInvokeMultihandle
		(Eoid theEoid,
		 String theInterfaceName,
		 String theMethodName,
		 String theMethodDescriptor,
		 byte[] theArguments)
		{
		Collection theTargets;

		// Invoke local objects exported with the EOID, if any.
		theTargets = myExportMap.getExportedObjects (theEoid);
		if (theTargets != null)
			{
			setUpInvocations
				(theInterfaceName,
				 theMethodName,
				 theMethodDescriptor,
				 theTargets,
				 theArguments);
			}

		// Send an invocation message so remote objects with the target
		// interface, if any, are invoked.
		if (myProtocol != null)
			{
			sendInvocationMessage
				(theEoid,
				 theInterfaceName,
				 theMethodName,
				 theMethodDescriptor,
				 theArguments);
			}
		}

	/**
	 * Perform a method invocation, originating from an external invocation
	 * message, on the appropriate objects, if any, in this M2MI layer.
	 *
	 * @param  theEoid
	 *     Exported object identifier.
	 * @param  theInterfaceName
	 *     Fully-qualified name of the target interface.
	 * @param  theMethodName
	 *     Name of the target method.
	 * @param  theMethodDescriptor
	 *     Descriptor for the target method, in the format specified by Section
	 *     4.3.3 of the <I>Java Virtual Machine Specification, Second
	 *     Edition.</I>
	 * @param  theArguments
	 *     Byte array containing the serialized arguments for the target method;
	 *     or null if there are no arguments.
	 *
	 * @exception  NullPointerException
	 *     (unchecked exception) Thrown if <TT>theEoid</TT>,
	 *     <TT>theInterfaceName</TT>, <TT>theMethodName</TT>, or
	 *     <TT>theMethodDescriptor</TT> is null.
	 * @exception  InvocationException
	 *     (unchecked exception) Thrown if the invocation was not successful.
	 *     The detail message and/or chained exception contains further
	 *     information.
	 */
	private synchronized void privateInvokeMessage
		(Eoid theEoid,
		 String theInterfaceName,
		 String theMethodName,
		 String theMethodDescriptor,
		 byte[] theArguments)
		{
		Collection theTargets;

		if (theEoid.equals (Eoid.WILDCARD))
			{
			// Invoke local objects exported with the target interface, if any.
			theTargets = myExportMap.getExportedObjects (theInterfaceName);
			}
		else
			{
			// Invoke local objects exported with the EOID, if any.
			theTargets = myExportMap.getExportedObjects (theEoid);
			}

		if (theTargets != null)
			{
			setUpInvocations
				(theInterfaceName,
				 theMethodName,
				 theMethodDescriptor,
				 theTargets,
				 theArguments);
			}
		}

	/**
	 * Set up a method invocation for every object in the given collection. The
	 * actual invocations will be performed on separate task threads by copies
	 * of the method invoker for the given target interface name, target method
	 * name, and method descriptor.
	 *
	 * @param  theInterfaceName
	 *     Fully-qualified name of the target interface.
	 * @param  theMethodName
	 *     Name of the target method.
	 * @param  theMethodDescriptor
	 *     Descriptor for the target method, in the format specified by Section
	 *     4.3.3 of the <I>Java Virtual Machine Specification, Second
	 *     Edition.</I>
	 * @param  theTargets
	 *     Collection of target objects to be invoked.
	 * @param  theArguments
	 *     Byte array containing the serialized arguments for the target method;
	 *     or null if there are no arguments.
	 *
	 * @exception  NullPointerException
	 *     (unchecked exception) Thrown if <TT>theInterfaceName</TT>,
	 *     <TT>theMethodName</TT>, or <TT>theMethodDescriptor</TT> is null.
	 * @exception  InvocationException
	 *     (unchecked exception) Thrown if the invocation was not successful.
	 *     The detail message and/or chained exception contains further
	 *     information.
	 */
	private void setUpInvocations
		(String theInterfaceName,
		 String theMethodName,
		 String theMethodDescriptor,
		 Collection theTargets,
		 byte[] theArguments)
		{
		try
			{
			// Get the proper method invoker class.
			Class theMethodInvokerClass =
				myMethodInvokerCache.getMethodInvokerClass
					(theInterfaceName,
					 theMethodName,
					 theMethodDescriptor);

			// Set up an invocation for each target object.
			Iterator iter = theTargets.iterator();
			while (iter.hasNext())
				{
				MethodInvoker mi = (MethodInvoker)
					theMethodInvokerClass.newInstance();
				mi.setTarget (iter.next());
				mi.setArguments (theArguments);
				myTaskThreadPool.performTask (mi);
				}
			}

		catch (Exception exc)
			{
			/*
			** Modified by etf2954
			**
			throw new InvocationException ("Cannot set up invocations", exc);
			*/
			throw new InvocationException ("Cannot set up invocations");
			}
		}

	/**
	 * Send an outgoing M2MI invocation message to this M2MI layer's M2MP
	 * protocol instance.
	 *
	 * @param  theEoid
	 *     Exported object identifier.
	 * @param  theInterfaceName
	 *     Fully-qualified name of the target interface.
	 * @param  theMethodName
	 *     Name of the target method.
	 * @param  theMethodDescriptor
	 *     Descriptor for the target method, in the format specified by Section
	 *     4.3.3 of the <I>Java Virtual Machine Specification, Second
	 *     Edition.</I>
	 * @param  theArguments
	 *     Byte array containing the serialized arguments for the target method;
	 *     or null if there are no arguments.
	 *
	 * @exception  InvocationException
	 *     (unchecked exception) Thrown if the invocation was not successful.
	 *     The detail message and/or chained exception contains further
	 *     information.
	 */
	private void sendInvocationMessage
		(Eoid theEoid,
		 String theInterfaceName,
		 String theMethodName,
		 String theMethodDescriptor,
		 byte[] theArguments)
		{
		OutputStream os = null;
		DataOutputStream dos = null;

		try
			{
			// Set up M2MP message.
			os = myProtocol.createOutgoingMessage (null) .openOutputStream();
			dos = new DataOutputStream (os);

			// The following items are written. Items 1 and 2 constitute the
			// M2MP message prefix.

			// 1. Magic number ("M2MI" in ASCII).
			dos.writeInt (0x4D324D49);

			// 2. Hash code of the export map key, either the EOID (if the EOID
			// is not a wildcard) or the target interface name (if the EOID is a
			// wildcard).
			if (theEoid.equals (Eoid.WILDCARD))
				{
				dos.writeInt (theInterfaceName.hashCode());
				}
			else
				{
				dos.writeInt (theEoid.hashCode());
				}

			// 3. EOID.
			theEoid.write (dos);

			// 4. Target interface name.
			dos.writeUTF (theInterfaceName);

			// 5. Target method name.
			dos.writeUTF (theMethodName);

			// 6. Target method descriptor.
			dos.writeUTF (theMethodDescriptor);

			// 7. Number of argument bytes, or 0 if there are no arguments.
			// 8. Argument bytes if any.
			if (theArguments == null)
				{
				dos.writeInt (0);
				}
			else
				{
				dos.writeInt (theArguments.length);
				dos.write (theArguments);
				}

			dos.close();
			}

		catch (Exception exc)
			{
			/*
			** Modified by etf2954
			**
			throw new InvocationException
				("Cannot send invocation message", exc);
			*/
			throw new InvocationException
				("Cannot send invocation message");
			}

		finally
			{
			// Make sure the output stream is closed no matter what happens.
			if (os != null)
				{
				try
					{
					os.close();
					}
				catch (IOException exc)
					{
					}
				}
			}
		}

	/**
	 * Receive one incoming M2MI invocation message from this M2MI layer's M2MP
	 * protocol instance, and perform the invocations.
	 *
	 * @exception  InvocationException
	 *     (unchecked exception) Thrown if the invocation was not successful.
	 *     The detail message and/or chained exception contains further
	 *     information.
	 */
	private void receiveInvocationMessage()
		{
		InputStream is = null;
		DataInputStream dis = null;

		try
			{
			is = myNotifier.accept().openInputStream();
			dis = new DataInputStream (is);

			// The following items are read. Items 1 and 2 constitute the
			// M2MP message prefix.

			// 1. Magic number -- ignored.
			dis.readInt();

			// 2. Hash code of the export map key -- ignored.
			dis.readInt();

			// 3. EOID.
			Eoid theEoid = new Eoid (dis);

			// 4. Target interface name.
			String theInterfaceName = dis.readUTF();

			// 5. Target method name.
			String theMethodName = dis.readUTF();

			// 6. Target method descriptor.
			String theMethodDescriptor = dis.readUTF();

			// 7. Number of argument bytes, or 0 if there are no arguments.
			int n = dis.readInt();

			// 8. Argument bytes if any.
			byte[] theArguments;
			if (n == 0)
				{
				theArguments = null;
				}
			else
				{
				theArguments = new byte [n];
				dis.readFully (theArguments);
				}

			dis.close();

			privateInvokeMessage
				(theEoid,
				 theInterfaceName,
				 theMethodName,
				 theMethodDescriptor,
				 theArguments);
			}

		catch (Exception exc)
			{
			/*
			** Modified by etf2954
			**
			throw new InvocationException
				("Cannot receive invocation message", exc);
			*/
			throw new InvocationException
				("Cannot receive invocation message");
			}

		finally
			{
			// Make sure the input stream is closed no matter what happens.
			if (is != null)
				{
				try
					{
					is.close();
					}
				catch (IOException exc)
					{
					}
				}
			}
		}

	/**
	 * Manufacture an omnihandle for the given target interface list.
	 *
	 * @param  theTargetInterfaceList  Target interface list.
	 *
	 * @return  Omnihandle.
	 *
	 * @exception  NullPointerException
	 *     (unchecked exception) Thrown if <TT>theTargetInterfaceList</TT> is
	 *     null.
	 * @exception  ClassNotFoundException
	 *     Thrown if any interface in the target interface list cannot be found
	 *     in this M2MI Layer's parent class loader.
	 * @exception  InvalidMethodException
	 *     (unchecked exception) Thrown if any target interface is a class
	 *     rather than an interface, if any method in any target interface
	 *     returns a value, or if any method in any target interface throws any
	 *     checked exceptions.
	 * @exception  SynthesisException
	 *     Thrown if there was a problem synthesizing or instantiating the
	 *     omnihandle class.
	 */
	private synchronized Omnihandle privateCreateOmnihandle
		(TargetInterfaceList theTargetInterfaceList)
		throws
			ClassNotFoundException,
			SynthesisException
		{
		return
			myHandleFactory.createOmnihandle
				(theTargetInterfaceList.getInterfaces
					(myParentClassLoader));
		}

	/**
	 * Manufacture a unihandle for the given target interface list and exported
	 * object identifier (EOID).
	 *
	 * @param  theTargetInterfaceList  Target interface list.
	 * @param  theEoid                 EOID.
	 *
	 * @return  Unihandle.
	 *
	 * @exception  NullPointerException
	 *     (unchecked exception) Thrown if <TT>theTargetInterfaceList</TT> is
	 *     null or <TT>theEoid</TT> is null.
	 * @exception  ClassNotFoundException
	 *     Thrown if any interface in the target interface list cannot be found
	 *     in this M2MI Layer's parent class loader.
	 * @exception  InvalidMethodException
	 *     (unchecked exception) Thrown if any target interface is a class
	 *     rather than an interface, if any method in any target interface
	 *     returns a value, or if any method in any target interface throws any
	 *     checked exceptions.
	 * @exception  SynthesisException
	 *     Thrown if there was a problem synthesizing or instantiating the
	 *     unihandle class.
	 */
	private synchronized Unihandle privateCreateUnihandle
		(TargetInterfaceList theTargetInterfaceList,
		 Eoid theEoid)
		throws
			ClassNotFoundException,
			SynthesisException
		{
		return
			myHandleFactory.createUnihandle
				(theTargetInterfaceList.getInterfaces
					(myParentClassLoader),
				 theEoid);
		}

	/**
	 * Manufacture a multihandle for the given target interface list and
	 * exported object identifier (EOID).
	 *
	 * @param  theTargetInterfaceList  Target interface list.
	 * @param  theEoid                 EOID.
	 *
	 * @return  Multihandle.
	 *
	 * @exception  NullPointerException
	 *     (unchecked exception) Thrown if <TT>theTargetInterfaceList</TT> is
	 *     null or <TT>theEoid</TT> is null.
	 * @exception  ClassNotFoundException
	 *     Thrown if any interface in the target interface list cannot be found
	 *     in this M2MI Layer's parent class loader.
	 * @exception  InvalidMethodException
	 *     (unchecked exception) Thrown if any target interface is a class
	 *     rather than an interface, if any method in any target interface
	 *     returns a value, or if any method in any target interface throws any
	 *     checked exceptions.
	 * @exception  SynthesisException
	 *     Thrown if there was a problem synthesizing or instantiating the
	 *     multihandle class.
	 */
	private synchronized Multihandle privateCreateMultihandle
		(TargetInterfaceList theTargetInterfaceList,
		 Eoid theEoid)
		throws
			ClassNotFoundException,
			SynthesisException
		{
		return
			myHandleFactory.createMultihandle
				(theTargetInterfaceList.getInterfaces
					(myParentClassLoader),
				 theEoid);
		}

	/**
	 * Determine if the given object is exported with the given EOID in this
	 * M2MI layer.
	 *
	 * @param  theObject  Object to test.
	 * @param  theEoid    Exported object identifier.
	 *
	 * @return  True if <TT>theObject</TT> is exported with <TT>theEOID</TT>,
	 *          false otherwise.
	 */
	private synchronized boolean privateIsExported
		(Object theObject,
		 Eoid theEoid)
		{
		return myExportMap.isExported (theObject, theEoid);
		}

	/**
	 * Determine if the given object is exported with the given target
	 * interface in this M2MI layer.
	 *
	 * @param  theObject         Object to test.
	 * @param  theInterfaceName  Fully-qualified name of the target interface.
	 *
	 * @return  True if <TT>theObject</TT> is exported with
	 *          <TT>theInterfaceName</TT>, false otherwise.
	 */
	private synchronized boolean privateIsExported
		(Object theObject,
		 String theInterfaceName)
		{
		return myExportMap.isExported (theObject, theInterfaceName);
		}

// Exported static operations.

	/**
	 * Initialize the M2MI layer. A newly constructed M2MP Protocol object with
	 * an M2MP Router channel will be used. The value of
	 * <TT>System.currentTimeMillis()</TT> will be used as the random seed. The
	 * M2MI layer will perform only one invocation at a time. The calling
	 * thread's context class loader will be the parent class loader for the
	 * M2MI layer's internal class loaders.
	 *
	 * @param  theMacAddress
	 *     IEEE 802 MAC address that will go into the EOIDs the M2MI layer
	 *     generates. The suggested value is the MAC address of the network
	 *     interface of the host in which the M2MI layer is running.
	 *
	 * @exception  IllegalStateException
	 *     (unchecked exception) Thrown if the M2MI layer is already
	 *     initialized.
	 * @exception  IllegalArgumentException
	 *     (unchecked exception) Thrown if <TT>theMacAddress</TT> is 0.
	 */
	public static void initialize
		(long theMacAddress)
		{
		long theSeed = System.currentTimeMillis();
		initialize
			(theMacAddress,
			 new Protocol
				(new M2MPRouterChannel(),
				 theMacAddress,
				 theSeed,
				 20,
				 2000L),
			 theSeed);
		}

	/**
	 * Initialize the M2MI layer. The value of
	 * <TT>System.currentTimeMillis()</TT> will be used as the random seed. The
	 * M2MI layer will perform only one invocation at a time. The calling
	 * thread's context class loader will be the parent class loader for the
	 * M2MI layer's internal class loaders.
	 *
	 * @param  theMacAddress
	 *     IEEE 802 MAC address that will go into the EOIDs the M2MI layer
	 *     generates. The suggested value is the MAC address of the network
	 *     interface of the host in which the M2MI layer is running.
	 * @param  theProtocol
	 *     M2MP Protocol object the M2MI layer will use to send and receive
	 *     external invocations, or null not to send or receive external
	 *     invocations.
	 *
	 * @exception  IllegalStateException
	 *     (unchecked exception) Thrown if the M2MI layer is already
	 *     initialized.
	 * @exception  IllegalArgumentException
	 *     (unchecked exception) Thrown if <TT>theMacAddress</TT> is 0.
	 */
	public static void initialize
		(long theMacAddress,
		 Protocol theProtocol)
		{
		initialize
			(theMacAddress,
			 theProtocol,
			 System.currentTimeMillis());
		}

	/**
	 * Initialize the M2MI layer. The M2MI layer will perform only one
	 * invocation at a time. The calling thread's context class loader will be
	 * the parent class loader for the M2MI layer's internal class loaders.
	 *
	 * @param  theMacAddress
	 *     IEEE 802 MAC address that will go into the EOIDs the M2MI layer
	 *     generates. The suggested value is the MAC address of the network
	 *     interface of the host in which the M2MI layer is running.
	 * @param  theProtocol
	 *     M2MP Protocol object the M2MI layer will use to send and receive
	 *     external invocations, or null not to send or receive external
	 *     invocations.
	 * @param  theSeed
	 *     Seed for the random numbers that will go into the EOIDs the M2MI
	 *     layer generates. The suggested value is
	 *     <TT>System.currentTimeMillis()</TT>.
	 *
	 * @exception  IllegalStateException
	 *     (unchecked exception) Thrown if the M2MI layer is already
	 *     initialized.
	 * @exception  IllegalArgumentException
	 *     (unchecked exception) Thrown if <TT>theMacAddress</TT> is 0 or
	 *     <TT>theSeed</TT> is 0.
	 */
	public static void initialize
		(long theMacAddress,
		 Protocol theProtocol,
		 long theSeed)
		{
		initialize
			(theMacAddress,
			 theProtocol,
			 theSeed,
			 1);
		}

	/**
	 * Initialize the M2MI layer. The calling thread's context class loader will
	 * be the parent class loader for the M2MI layer's internal class loaders.
	 *
	 * @param  theMacAddress
	 *     IEEE 802 MAC address that will go into the EOIDs the M2MI layer
	 *     generates. The suggested value is the MAC address of the network
	 *     interface of the host in which the M2MI layer is running.
	 * @param  theProtocol
	 *     M2MP Protocol object the M2MI layer will use to send and receive
	 *     external invocations, or null not to send or receive external
	 *     invocations.
	 * @param  theSeed
	 *     Seed for the random numbers that will go into the EOIDs the M2MI
	 *     layer generates. The suggested value is
	 *     <TT>System.currentTimeMillis()</TT>.
	 * @param  maxInvocations
	 *     The maximum number of concurrent invocations the M2MI layer will
	 *     perform.
	 *
	 * @exception  IllegalStateException
	 *     (unchecked exception) Thrown if the M2MI layer is already
	 *     initialized.
	 * @exception  IllegalArgumentException
	 *     (unchecked exception) Thrown if <TT>theMacAddress</TT> is 0,
	 *     <TT>theSeed</TT> is 0, or <TT>maxInvocations</TT> &lt; 1.
	 */
	public static void initialize
		(long theMacAddress,
		 Protocol theProtocol,
		 long theSeed,
		 int maxInvocations)
		{
		initialize
			(theMacAddress,
			 theProtocol,
			 theSeed,
			 maxInvocations,
			 Thread.currentThread().getContextClassLoader());
		}

	/**
	 * Initialize the M2MI layer.
	 *
	 * @param  theMacAddress
	 *     IEEE 802 MAC address that will go into the EOIDs the M2MI layer
	 *     generates. The suggested value is the MAC address of the network
	 *     interface of the host in which the M2MI layer is running.
	 * @param  theProtocol
	 *     M2MP Protocol object the M2MI layer will use to send and receive
	 *     external invocations, or null not to send or receive external
	 *     invocations.
	 * @param  theSeed
	 *     Seed for the random numbers that will go into the EOIDs the M2MI
	 *     layer generates. The suggested value is
	 *     <TT>System.currentTimeMillis()</TT>.
	 * @param  maxInvocations
	 *     The maximum number of concurrent invocations the M2MI layer will
	 *     perform.
	 * @param  theParentClassLoader
	 *     Parent class loader for the M2MI layer's internal class loaders.
	 *
	 * @exception  IllegalStateException
	 *     (unchecked exception) Thrown if the M2MI layer is already
	 *     initialized.
	 * @exception  IllegalArgumentException
	 *     (unchecked exception) Thrown if <TT>theMacAddress</TT> is 0,
	 *     <TT>theSeed</TT> is 0, or <TT>maxInvocations</TT> &lt; 1.
	 * @exception  NullPointerException
	 *     (unchecked exception) Thrown if <TT>theParentClassLoader</TT> is
	 *     null.
	 */
	public static void initialize
		(long theMacAddress,
		 Protocol theProtocol,
		 long theSeed,
		 int maxInvocations,
		 ClassLoader theParentClassLoader)
		{
		if (theM2MILayer != null)
			{
			throw new IllegalStateException ("M2MI layer already initialized");
			}
		theM2MILayer = new M2MI
			(theMacAddress,
			 theProtocol,
			 theSeed,
			 maxInvocations,
			 theParentClassLoader);
		}

	/**
	 * Export the given object with the given target interface. Afterwards, M2MI
	 * invocations on an omnihandle for the target interface or any
	 * superinterface thereof will be executed by the given object.
	 *
	 * @param  theObject     Object.
	 * @param  theInterface  Target interface.
	 *
	 * @exception  IllegalStateException
	 *     (unchecked exception) Thrown if the M2MI layer is not initialized.
	 * @exception  NullPointerException
	 *     (unchecked exception) Thrown if <TT>theObject</TT> is null or
	 *     <TT>theInterface</TT> is null.
	 * @exception  ClassCastException
	 *     (unchecked exception) Thrown if <TT>theObject</TT> is not an instance
	 *     of the target interface.
	 * @exception  InvalidMethodException
	 *     (unchecked exception) Thrown if the target interface is a class
	 *     rather than an interface, if any method in the target interface
	 *     returns a value, or if any method in the target interface throws any
	 *     checked exceptions.
	 * @exception  SynthesisException
	 *     Thrown if there was a problem synthesizing or instantiating the
	 *     omnihandle class.
	 */
	public static void export
		(Object theObject,
		 Class theInterface)
		throws SynthesisException
		{
		if (theM2MILayer == null)
			{
			throw new IllegalStateException ("M2MI layer not initialized");
			}
		theM2MILayer.privateExport (theObject, new Class[] {theInterface});
		}

	/**
	 * Export the given object with the given target interface(s). Afterwards,
	 * M2MI invocations on an omnihandle for any of the target interfaces or any
	 * superinterfaces thereof will be executed by the given object.
	 *
	 * @param  theObject      Object.
	 * @param  theInterfaces  Array of one or more target interfaces.
	 *
	 * @exception  IllegalStateException
	 *     (unchecked exception) Thrown if the M2MI layer is not initialized.
	 * @exception  NullPointerException
	 *     (unchecked exception) Thrown if <TT>theObject</TT> is null,
	 *     <TT>theInterfaces</TT> is null, or any element of
	 *     <TT>theInterfaces</TT> is null.
	 * @exception  IllegalArgumentException
	 *     (unchecked exception) Thrown if <TT>theInterfaces</TT> is zero
	 *     length.
	 * @exception  ClassCastException
	 *     (unchecked exception) Thrown if <TT>theObject</TT> is not an instance
	 *     of one or more of the given target interfaces.
	 * @exception  InvalidMethodException
	 *     (unchecked exception) Thrown if any target interface is a class
	 *     rather than an interface, if any method in any target interface
	 *     returns a value, or if any method in any target interface throws any
	 *     checked exceptions.
	 * @exception  SynthesisException
	 *     Thrown if there was a problem synthesizing or instantiating the
	 *     omnihandle class.
	 */
	public static void export
		(Object theObject,
		 Class[] theInterfaces)
		throws SynthesisException
		{
		if (theM2MILayer == null)
			{
			throw new IllegalStateException ("M2MI layer not initialized");
			}
		theM2MILayer.privateExport (theObject, theInterfaces);
		}

	/**
	 * Unexport the given object. Afterwards, M2MI invocations on all
	 * omnihandles, unihandles, and multihandles that formerly referred to the
	 * given object, will no longer be executed by the given object. If the
	 * given object was not exported in the M2MI layer, this method does
	 * nothing.
	 *
	 * @param  theObject  Object.
	 *
	 * @exception  IllegalStateException
	 *     (unchecked exception) Thrown if the M2MI layer is not initialized.
	 * @exception  NullPointerException
	 *     (unchecked exception) Thrown if <TT>theObject</TT> is null.
	 */
	public static void unexport
		(Object theObject)
		{
		if (theM2MILayer == null)
			{
			throw new IllegalStateException ("M2MI layer not initialized");
			}
		theM2MILayer.privateUnexport (theObject);
		}

	/**
	 * Obtain an omnihandle for the given target interface. The omnihandle
	 * implements the target interface and can be cast to the target interface
	 * (or any superinterface thereof).
	 *
	 * @param  theInterface  Target interface.
	 *
	 * @return  Omnihandle for <TT>theInterface</TT>.
	 *
	 * @exception  IllegalStateException
	 *     (unchecked exception) Thrown if the M2MI layer is not initialized.
	 * @exception  NullPointerException
	 *     (unchecked exception) Thrown if <TT>theInterface</TT> is null.
	 * @exception  InvalidMethodException
	 *     (unchecked exception) Thrown if <TT>theInterface</TT> is not an
	 *     interface (i.e., it is a class), if any method in
	 *     <TT>theInterface</TT> returns a value, or if any method in
	 *     <TT>theInterface</TT> throws any checked exceptions.
	 * @exception  InvalidMethodException
	 *     (unchecked exception) Thrown if the target interface is a class
	 *     rather than an interface, if any method in the target interface
	 *     returns a value, or if any method in the target interface throws any
	 *     checked exceptions.
	 * @exception  SynthesisException
	 *     Thrown if there was a problem synthesizing or instantiating the
	 *     omnihandle class.
	 */
	public static Omnihandle getOmnihandle
		(Class theInterface)
		throws SynthesisException
		{
		if (theM2MILayer == null)
			{
			throw new IllegalStateException ("M2MI layer not initialized");
			}
		return theM2MILayer.privateGetOmnihandle (new Class[] {theInterface});
		}

	/**
	 * Obtain an omnihandle for the given target interface(s). The omnihandle
	 * implements all of the target interfaces and can be cast to any of the
	 * target interfaces (or any superinterfaces thereof).
	 *
	 * @param  theInterfaces  Array of one or more target interfaces.
	 *
	 * @return  Omnihandle for <TT>theInterfaces</TT>.
	 *
	 * @exception  IllegalStateException
	 *     (unchecked exception) Thrown if the M2MI layer is not initialized.
	 * @exception  NullPointerException
	 *     (unchecked exception) Thrown if <TT>theInterfaces</TT> is null or any
	 *     element of <TT>theInterfaces</TT> is null.
	 * @exception  IllegalArgumentException
	 *     (unchecked exception) Thrown if <TT>theInterfaces</TT> is zero
	 *     length.
	 * @exception  InvalidMethodException
	 *     (unchecked exception) Thrown if any target interface is a class
	 *     rather than an interface, if any method in any target interface
	 *     returns a value, or if any method in any target interface throws any
	 *     checked exceptions.
	 * @exception  SynthesisException
	 *     Thrown if there was a problem synthesizing or instantiating the
	 *     omnihandle class.
	 */
	public static Omnihandle getOmnihandle
		(Class[] theInterfaces)
		throws SynthesisException
		{
		if (theM2MILayer == null)
			{
			throw new IllegalStateException ("M2MI layer not initialized");
			}
		return theM2MILayer.privateGetOmnihandle (theInterfaces);
		}

	/**
	 * Obtain a unihandle for the given target interface, attached to the given
	 * object. The unihandle implements the target interface and can be cast to
	 * the target interface (or any superinterface thereof). Afterwards, M2MI
	 * invocations on the returned unihandle, and M2MI invocations on an
	 * omnihandle for the target interface or any superinterface thereof, will
	 * be executed by the given object.
	 *
	 * @param  theObject     Object.
	 * @param  theInterface  Target interface.
	 *
	 * @return  Unihandle for <TT>theObject</TT>.
	 *
	 * @exception  IllegalStateException
	 *     (unchecked exception) Thrown if the M2MI layer is not initialized.
	 * @exception  NullPointerException
	 *     (unchecked exception) Thrown if <TT>theObject</TT> is null or
	 *     <TT>theInterface</TT> is null.
	 * @exception  ClassCastException
	 *     (unchecked exception) Thrown if <TT>theObject</TT> is not an instance
	 *     of the target interface.
	 * @exception  InvalidMethodException
	 *     (unchecked exception) Thrown if the target interface is a class
	 *     rather than an interface, if any method in the target interface
	 *     returns a value, or if any method in the target interface throws any
	 *     checked exceptions.
	 * @exception  SynthesisException
	 *     Thrown if there was a problem synthesizing or instantiating the
	 *     unihandle class.
	 */
	public static Unihandle getUnihandle
		(Object theObject,
		 Class theInterface)
		throws SynthesisException
		{
		if (theM2MILayer == null)
			{
			throw new IllegalStateException ("M2MI layer not initialized");
			}
		return theM2MILayer.privateGetUnihandle
			(theObject, new Class[] {theInterface});
		}

	/**
	 * Obtain a unihandle for the given target interface(s), attached to the
	 * given object. The unihandle implements all of the target interfaces and
	 * can be cast to any of the target interfaces (or any superinterfaces
	 * thereof). Afterwards, M2MI invocations on the returned unihandle, and
	 * M2MI invocations on an omnihandle for any of the target interfaces or any
	 * superinterfaces thereof, will be executed by the given object.
	 *
	 * @param  theObject      Object.
	 * @param  theInterfaces  Array of one or more target interfaces.
	 *
	 * @return  Unihandle for <TT>theObject</TT>.
	 *
	 * @exception  IllegalStateException
	 *     (unchecked exception) Thrown if the M2MI layer is not initialized.
	 * @exception  NullPointerException
	 *     (unchecked exception) Thrown if <TT>theObject</TT> is null,
	 *     <TT>theInterfaces</TT> is null, or any element of
	 *     <TT>theInterfaces</TT> is null.
	 * @exception  IllegalArgumentException
	 *     (unchecked exception) Thrown if <TT>theInterfaces</TT> is zero
	 *     length.
	 * @exception  ClassCastException
	 *     (unchecked exception) Thrown if <TT>theObject</TT> is not an instance
	 *     of one or more of the given target interfaces.
	 * @exception  InvalidMethodException
	 *     (unchecked exception) Thrown if any target interface is a class
	 *     rather than an interface, if any method in any target interface
	 *     returns a value, or if any method in any target interface throws any
	 *     checked exceptions.
	 * @exception  SynthesisException
	 *     Thrown if there was a problem synthesizing or instantiating the
	 *     unihandle class.
	 */
	public static Unihandle getUnihandle
		(Object theObject,
		 Class[] theInterfaces)
		throws SynthesisException
		{
		if (theM2MILayer == null)
			{
			throw new IllegalStateException ("M2MI layer not initialized");
			}
		return theM2MILayer.privateGetUnihandle (theObject, theInterfaces);
		}

	/**
	 * Obtain a multihandle for the given target interface. The multihandle
	 * implements the target interface and can be cast to the target interface
	 * (or any superinterface thereof). Initially, no objects are attached to
	 * the multihandle. Use the {@link Multihandle#attach(Object) attach()} and
	 * {@link Multihandle#detach(Object) detach()} methods in interface {@link
	 * Multihandle </CODE>Multihandle<CODE>} to attach objects to and detach
	 * objects from the multihandle.
	 *
	 * @param  theInterface  Target interface.
	 *
	 * @return  Multihandle for <TT>theInterface</TT>.
	 *
	 * @exception  IllegalStateException
	 *     (unchecked exception) Thrown if the M2MI layer is not initialized.
	 * @exception  NullPointerException
	 *     (unchecked exception) Thrown if <TT>theInterface</TT> is null.
	 * @exception  InvalidMethodException
	 *     (unchecked exception) Thrown if the target interface is a class
	 *     rather than an interface, if any method in the target interface
	 *     returns a value, or if any method in the target interface throws any
	 *     checked exceptions.
	 * @exception  SynthesisException
	 *     Thrown if there was a problem synthesizing or instantiating the
	 *     multihandle class.
	 */
	public static Multihandle getMultihandle
		(Class theInterface)
		throws SynthesisException
		{
		if (theM2MILayer == null)
			{
			throw new IllegalStateException ("M2MI layer not initialized");
			}
		return theM2MILayer.privateGetMultihandle (new Class[] {theInterface});
		}

	/**
	 * Obtain a multihandle for the given target interface(s). The multihandle
	 * implements all of the target interfaces and can be cast to any of the
	 * target interfaces (or any superinterfaces thereof). Initially, no objects
	 * are attached to the multihandle. Use the {@link
	 * Multihandle#attach(Object) attach()} and {@link
	 * Multihandle#detach(Object) detach()} methods in interface {@link
	 * Multihandle </CODE>Multihandle<CODE>} to attach objects to and detach
	 * objects from the multihandle.
	 *
	 * @param  theInterfaces  Array of one or more target interfaces.
	 *
	 * @return  Multihandle for <TT>theInterfaces</TT>.
	 *
	 * @exception  IllegalStateException
	 *     (unchecked exception) Thrown if the M2MI layer is not initialized.
	 * @exception  NullPointerException
	 *     (unchecked exception) Thrown if <TT>theInterfaces</TT> is null or any
	 *     element of <TT>theInterfaces</TT> is null.
	 * @exception  IllegalArgumentException
	 *     (unchecked exception) Thrown if <TT>theInterfaces</TT> is zero
	 *     length.
	 * @exception  InvalidMethodException
	 *     (unchecked exception) Thrown if any target interface is a class
	 *     rather than an interface, if any method in any target interface
	 *     returns a value, or if any method in any target interface throws any
	 *     checked exceptions.
	 * @exception  SynthesisException
	 *     Thrown if there was a problem synthesizing or instantiating the
	 *     multihandle class.
	 */
	public static Multihandle getMultihandle
		(Class[] theInterfaces)
		throws SynthesisException
		{
		if (theM2MILayer == null)
			{
			throw new IllegalStateException ("M2MI layer not initialized");
			}
		return theM2MILayer.privateGetMultihandle (theInterfaces);
		}

// Hidden static operations.

	/**
	 * Attach the given object to the given unihandle.
	 *
	 * @param  theObject     Object.
	 * @param  theUnihandle  Unihandle.
	 *
	 * @exception  IllegalStateException
	 *     (unchecked exception) Thrown if the M2MI layer is not initialized.
	 *     Thrown if <TT>theUnihandle</TT> is not attached to an object exported
	 *     in the M2MI layer.
	 * @exception  NullPointerException
	 *     (unchecked exception) Thrown if <TT>theObject</TT> is null or
	 *     <TT>theUnihandle</TT> is null.
	 * @exception  ClassCastException
	 *     (unchecked exception) Thrown if <TT>theObject</TT> is not an instance
	 *     of one or more of <TT>theUnihandle</TT>'s target interfaces.
	 */
	static void attachUnihandle
		(Object theObject,
		 Unihandle theUnihandle)
		{
		if (theM2MILayer == null)
			{
			throw new IllegalStateException ("M2MI layer not initialized");
			}
		theM2MILayer.privateAttachUnihandle (theObject, theUnihandle);
		}

	/**
	 * Detach the target object from the given unihandle.
	 *
	 * @param  theUnihandle  Unihandle.
	 *
	 * @exception  IllegalStateException
	 *     (unchecked exception) Thrown if the M2MI layer is not initialized.
	 *     Thrown if <TT>theUnihandle</TT> is not attached to an object exported
	 *     in the M2MI layer.
	 * @exception  NullPointerException
	 *     (unchecked exception) Thrown if <TT>theUnihandle</TT> is null.
	 */
	static void detachUnihandle
		(Unihandle theUnihandle)
		{
		if (theM2MILayer == null)
			{
			throw new IllegalStateException ("M2MI layer not initialized");
			}
		theM2MILayer.privateDetachUnihandle (theUnihandle);
		}

	/**
	 * Attach the given object to the given multihandle.
	 *
	 * @param  theObject       Object.
	 * @param  theMultihandle  Multihandle.
	 *
	 * @exception  IllegalStateException
	 *     (unchecked exception) Thrown if the M2MI layer is not initialized.
	 * @exception  NullPointerException
	 *     (unchecked exception) Thrown if <TT>theObject</TT> is null or
	 *     <TT>theMultihandle</TT> is null.
	 * @exception  ClassCastException
	 *     (unchecked exception) Thrown if <TT>theObject</TT> is not an instance
	 *     of one or more of <TT>theMultihandle</TT>'s target interfaces.
	 */
	static void attachMultihandle
		(Object theObject,
		 Multihandle theMultihandle)
		{
		if (theM2MILayer == null)
			{
			throw new IllegalStateException ("M2MI layer not initialized");
			}
		theM2MILayer.privateAttachMultihandle (theObject, theMultihandle);
		}

	/**
	 * Detach the given object from the given multihandle.
	 *
	 * @param  theObject       Object.
	 * @param  theMultihandle  Multihandle.
	 *
	 * @exception  IllegalStateException
	 *     (unchecked exception) Thrown if the M2MI layer is not initialized.
	 * @exception  NullPointerException
	 *     (unchecked exception) Thrown if <TT>theObject</TT> is null or
	 *     <TT>theMultihandle</TT> is null.
	 */
	static void detachMultihandle
		(Object theObject,
		 Multihandle theMultihandle)
		{
		if (theM2MILayer == null)
			{
			throw new IllegalStateException ("M2MI layer not initialized");
			}
		theM2MILayer.privateDetachMultihandle (theObject, theMultihandle);
		}

	/**
	 * Perform a method invocation, originating from an omnihandle, on the
	 * appropriate objects, if any, in the M2MI layer.
	 *
	 * @param  theEoid
	 *     Exported object identifier.
	 * @param  theInterfaceName
	 *     Fully-qualified name of the target interface.
	 * @param  theMethodName
	 *     Name of the target method.
	 * @param  theMethodDescriptor
	 *     Descriptor for the target method, in the format specified by Section
	 *     4.3.3 of the <I>Java Virtual Machine Specification, Second
	 *     Edition.</I>
	 * @param  theArguments
	 *     Byte array containing the serialized arguments for the target method;
	 *     or null if there are no arguments.
	 *
	 * @exception  IllegalStateException
	 *     (unchecked exception) Thrown if the M2MI layer is not initialized.
	 * @exception  NullPointerException
	 *     (unchecked exception) Thrown if <TT>theEoid</TT>,
	 *     <TT>theInterfaceName</TT>, <TT>theMethodName</TT>, or
	 *     <TT>theMethodDescriptor</TT> is null.
	 * @exception  InvocationException
	 *     (unchecked exception) Thrown if the invocation was not successful.
	 *     The detail message and/or chained exception contains further
	 *     information.
	 */
	static void invokeOmnihandle
		(Eoid theEoid,
		 String theInterfaceName,
		 String theMethodName,
		 String theMethodDescriptor,
		 byte[] theArguments)
		{
		if (theM2MILayer == null)
			{
			throw new IllegalStateException ("M2MI layer not initialized");
			}
		theM2MILayer.privateInvokeOmnihandle
			(theEoid,
			 theInterfaceName,
			 theMethodName,
			 theMethodDescriptor,
			 theArguments);
		}

	/**
	 * Perform a method invocation, originating from a unihandle, on the
	 * appropriate objects, if any, in the M2MI layer.
	 *
	 * @param  theEoid
	 *     Exported object identifier.
	 * @param  theInterfaceName
	 *     Fully-qualified name of the target interface.
	 * @param  theMethodName
	 *     Name of the target method.
	 * @param  theMethodDescriptor
	 *     Descriptor for the target method, in the format specified by Section
	 *     4.3.3 of the <I>Java Virtual Machine Specification, Second
	 *     Edition.</I>
	 * @param  theArguments
	 *     Byte array containing the serialized arguments for the target method;
	 *     or null if there are no arguments.
	 *
	 * @exception  IllegalStateException
	 *     (unchecked exception) Thrown if the M2MI layer is not initialized.
	 * @exception  NullPointerException
	 *     (unchecked exception) Thrown if <TT>theEoid</TT>,
	 *     <TT>theInterfaceName</TT>, <TT>theMethodName</TT>, or
	 *     <TT>theMethodDescriptor</TT> is null.
	 * @exception  InvocationException
	 *     (unchecked exception) Thrown if the invocation was not successful.
	 *     The detail message and/or chained exception contains further
	 *     information.
	 */
	static void invokeUnihandle
		(Eoid theEoid,
		 String theInterfaceName,
		 String theMethodName,
		 String theMethodDescriptor,
		 byte[] theArguments)
		{
		if (theM2MILayer == null)
			{
			throw new IllegalStateException ("M2MI layer not initialized");
			}
		theM2MILayer.privateInvokeUnihandle
			(theEoid,
			 theInterfaceName,
			 theMethodName,
			 theMethodDescriptor,
			 theArguments);
		}

	/**
	 * Perform a method invocation, originating from a multihandle, on the
	 * appropriate objects, if any, in the M2MI layer.
	 *
	 * @param  theEoid
	 *     Exported object identifier.
	 * @param  theInterfaceName
	 *     Fully-qualified name of the target interface.
	 * @param  theMethodName
	 *     Name of the target method.
	 * @param  theMethodDescriptor
	 *     Descriptor for the target method, in the format specified by Section
	 *     4.3.3 of the <I>Java Virtual Machine Specification, Second
	 *     Edition.</I>
	 * @param  theArguments
	 *     Byte array containing the serialized arguments for the target method;
	 *     or null if there are no arguments.
	 *
	 * @exception  IllegalStateException
	 *     (unchecked exception) Thrown if the M2MI layer is not initialized.
	 * @exception  NullPointerException
	 *     (unchecked exception) Thrown if <TT>theEoid</TT>,
	 *     <TT>theInterfaceName</TT>, <TT>theMethodName</TT>, or
	 *     <TT>theMethodDescriptor</TT> is null.
	 * @exception  InvocationException
	 *     (unchecked exception) Thrown if the invocation was not successful.
	 *     The detail message and/or chained exception contains further
	 *     information.
	 */
	static void invokeMultihandle
		(Eoid theEoid,
		 String theInterfaceName,
		 String theMethodName,
		 String theMethodDescriptor,
		 byte[] theArguments)
		{
		if (theM2MILayer == null)
			{
			throw new IllegalStateException ("M2MI layer not initialized");
			}
		theM2MILayer.privateInvokeMultihandle
			(theEoid,
			 theInterfaceName,
			 theMethodName,
			 theMethodDescriptor,
			 theArguments);
		}

	/**
	 * Manufacture an omnihandle for the given target interface list. The
	 * omnihandle class is synthesized and cached if necessary by the M2MI
	 * Layer's handle factory.
	 *
	 * @param  theTargetInterfaceList  Target interface list.
	 *
	 * @return  Omnihandle.
	 *
	 * @exception  IllegalStateException
	 *     (unchecked exception) Thrown if the M2MI layer is not initialized.
	 * @exception  NullPointerException
	 *     (unchecked exception) Thrown if <TT>theTargetInterfaceList</TT> is
	 *     null.
	 * @exception  ClassNotFoundException
	 *     Thrown if any interface in the target interface list cannot be found
	 *     in this M2MI Layer's parent class loader.
	 * @exception  InvalidMethodException
	 *     (unchecked exception) Thrown if any target interface is a class
	 *     rather than an interface, if any method in any target interface
	 *     returns a value, or if any method in any target interface throws any
	 *     checked exceptions.
	 * @exception  SynthesisException
	 *     Thrown if there was a problem synthesizing or instantiating the
	 *     omnihandle class.
	 */
	static Omnihandle createOmnihandle
		(TargetInterfaceList theTargetInterfaceList)
		throws ClassNotFoundException, SynthesisException
		{
		if (theM2MILayer == null)
			{
			throw new IllegalStateException ("M2MI layer not initialized");
			}
		return theM2MILayer.privateCreateOmnihandle (theTargetInterfaceList);
		}

	/**
	 * Manufacture a unihandle for the given target interface list and exported
	 * object identifier (EOID). The unihandle class is synthesized and cached
	 * if necessary by the M2MI Layer's handle factory.
	 *
	 * @param  theTargetInterfaceList  Target interface list.
	 * @param  theEoid                 EOID.
	 *
	 * @return  Unihandle.
	 *
	 * @exception  IllegalStateException
	 *     (unchecked exception) Thrown if the M2MI layer is not initialized.
	 * @exception  NullPointerException
	 *     (unchecked exception) Thrown if <TT>theTargetInterfaceList</TT> is
	 *     null.
	 * @exception  ClassNotFoundException
	 *     Thrown if any interface in the target interface list cannot be found
	 *     in this M2MI Layer's parent class loader.
	 * @exception  InvalidMethodException
	 *     (unchecked exception) Thrown if any target interface is a class
	 *     rather than an interface, if any method in any target interface
	 *     returns a value, or if any method in any target interface throws any
	 *     checked exceptions.
	 * @exception  SynthesisException
	 *     Thrown if there was a problem synthesizing or instantiating the
	 *     unihandle class.
	 */
	static Unihandle createUnihandle
		(TargetInterfaceList theTargetInterfaceList,
		 Eoid theEoid)
		throws
			ClassNotFoundException,
			SynthesisException
		{
		if (theM2MILayer == null)
			{
			throw new IllegalStateException ("M2MI layer not initialized");
			}
		return theM2MILayer.privateCreateUnihandle
			(theTargetInterfaceList, theEoid);
		}

	/**
	 * Manufacture a multihandle for the given target interface list and
	 * exported object identifier (EOID). The multihandle class is synthesized
	 * and cached if necessary by the M2MI Layer's handle factory.
	 *
	 * @param  theTargetInterfaceList  Target interface list.
	 * @param  theEoid                 EOID.
	 *
	 * @return  Multihandle.
	 *
	 * @exception  IllegalStateException
	 *     (unchecked exception) Thrown if the M2MI layer is not initialized.
	 * @exception  NullPointerException
	 *     (unchecked exception) Thrown if <TT>theTargetInterfaceList</TT> is
	 *     null.
	 * @exception  ClassNotFoundException
	 *     Thrown if any interface in the target interface list cannot be found
	 *     in this M2MI Layer's parent class loader.
	 * @exception  InvalidMethodException
	 *     (unchecked exception) Thrown if any target interface is a class
	 *     rather than an interface, if any method in any target interface
	 *     returns a value, or if any method in any target interface throws any
	 *     checked exceptions.
	 * @exception  SynthesisException
	 *     Thrown if there was a problem synthesizing or instantiating the
	 *     multihandle class.
	 */
	static Multihandle createMultihandle
		(TargetInterfaceList theTargetInterfaceList,
		 Eoid theEoid)
		throws
			ClassNotFoundException,
			SynthesisException
		{
		if (theM2MILayer == null)
			{
			throw new IllegalStateException ("M2MI layer not initialized");
			}
		return theM2MILayer.privateCreateMultihandle
			(theTargetInterfaceList, theEoid);
		}

	/**
	 * Determine if the given object is exported with the given EOID in the M2MI
	 * layer.
	 *
	 * @param  theObject  Object to test.
	 * @param  theEoid    Exported object identifier.
	 *
	 * @return  True if <TT>theObject</TT> is exported with <TT>theEOID</TT>,
	 *          false otherwise.
	 */
	static boolean isExported
		(Object theObject,
		 Eoid theEoid)
		{
		if (theM2MILayer == null)
			{
			throw new IllegalStateException ("M2MI layer not initialized");
			}
		return theM2MILayer.privateIsExported (theObject, theEoid);
		}

	/**
	 * Determine if the given object is exported with the given target
	 * interface in the M2MI layer.
	 *
	 * @param  theObject         Object to test.
	 * @param  theInterfaceName  Fully-qualified name of the target interface.
	 *
	 * @return  True if <TT>theObject</TT> is exported with
	 *          <TT>theInterfaceName</TT>, false otherwise.
	 */
	static boolean isExported
		(Object theObject,
		 String theInterfaceName)
		{
		if (theM2MILayer == null)
			{
			throw new IllegalStateException ("M2MI layer not initialized");
			}
		return theM2MILayer.privateIsExported (theObject, theInterfaceName);
		}

	/**
	 * Obtain the message prefix for an M2MP message carrying an M2MI invocation
	 * for the given export map key (target interface name or EOID).
	 *
	 * @param  theKey  Key.
	 *
	 * @return  Message prefix.
	 */
	static byte[] getMessagePrefix
		(Object theKey)
		{
		byte[] prefix = new byte [8];

		// Magic number ("M2MI" in ASCII).
		prefix[0] = (byte) 0x4D;
		prefix[1] = (byte) 0x32;
		prefix[2] = (byte) 0x4D;
		prefix[3] = (byte) 0x49;

		// Hash code of the export map key.
		int hash = theKey.hashCode();
		prefix[4] = (byte) ((hash >> 24) & 0xFF);
		prefix[5] = (byte) ((hash >> 16) & 0xFF);
		prefix[6] = (byte) ((hash >>  8) & 0xFF);
		prefix[7] = (byte) ((hash      ) & 0xFF);

		return prefix;
		}

	}
