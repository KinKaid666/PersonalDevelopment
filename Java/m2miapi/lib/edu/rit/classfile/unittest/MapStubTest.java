//******************************************************************************
//
// File:    MapStubTest.java
// Package: edu.rit.classfile.unittest
// Unit:    Class edu.rit.classfile.unittest.MapStubTest
//
// This Java source file is copyright (C) 2001 by the Rochester Institute of
// Technology. All rights reserved. For further information, contact the author,
// Alan Kaminsky, at ark@it.rit.edu.
//
// This Java source file is part of the RIT Classfile Library ("The Library").
// The Library is free software; you can redistribute it and/or modify it under
// the terms of the GNU General Public License as published by the Free Software
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

package edu.rit.classfile.unittest;

import edu.rit.classfile.DirectClassLoader;

import java.io.OutputStream;
import java.io.PrintStream;

import java.util.Map;

/**
 * Class MapStubTest is a main program that exercises the Stub Synthesizer in
 * class {@link StubSynthesizer </CODE>StubSynthesizer<CODE>}. It uses the Stub
 * Synthesizer to synthesize an implementation of interface java.util.Map, loads
 * the synthesized class into the JVM, creates an instance, and calls its
 * methods.
 * <P>
 * Study the <A HREF="doc-files/MapStubTest.html">source code</A> for class
 * MapStubTest.
 * <P>
 * <B>RIT Classfile Library Version 02-Jul-2002</B>
 *
 * @author  Alan Kaminsky
 * @version 09-Oct-2001
 */
public class MapStubTest
	{

	public static void main
		(String[] args)
		{
		try
			{
			PrintStream out = System.out;

			// Set up a direct class loader to load synthesized classes into
			// the JVM.
			DirectClassLoader loader = new DirectClassLoader();

			// Synthesize a stub implementation for interface java.util.Map,
			// writing the classfile into the direct class loader.
			OutputStream os = loader.writeClass ("MapImpl");
			StubSynthesizer.getStub (Map.class, "MapImpl", os);
			os.close();

			// Create an instance of the Map stub. We have to explicitly specify
			// the class loader to use.
			Map map = (Map)
				Class.forName ("MapImpl", true, loader).newInstance();

			// Call some methods on the map. Each one should cause the stub to
			// print out the method being called and its arguments.
			int i;
			boolean b;
			Object o;
			String s;

			out.print ("Calling map.size() [");
			i = map.size();
			out.print ("], returns ");
			out.println (i);

			out.print ("Calling map.isEmpty() [");
			b = map.isEmpty();
			out.print ("], returns ");
			out.println (b);

			out.print ("Calling map.containsKey (new Integer (1)) [");
			b = map.containsKey (new Integer (1));
			out.print ("], returns ");
			out.println (b);

			out.print ("Calling map.containsValue (\"Testing\") [");
			b = map.containsValue ("Testing");
			out.print ("], returns ");
			out.println (b);

			out.print ("Calling map.get (new Integer (1)) [");
			o = map.get (new Integer (1));
			out.print ("], returns ");
			out.println (o);

			out.print ("Calling map.put (new Integer (1), \"Testing\") [");
			o = map.put (new Integer (1), "Testing");
			out.print ("], returns ");
			out.println (o);

			out.print ("Calling map.remove (new Integer (1)) [");
			o = map.remove (new Integer (1));
			out.print ("], returns ");
			out.println (o);

			out.print ("Calling map.putAll (null) [");
			map.putAll (null);
			out.println ("]");

			out.print ("Calling map.clear() [");
			map.clear();
			out.println ("]");

			out.print ("Calling map.keySet() [");
			o = map.keySet();
			out.print ("], returns ");
			out.println (o);

			out.print ("Calling map.values() [");
			o = map.values();
			out.print ("], returns ");
			out.println (o);

			out.print ("Calling map.entrySet() [");
			o = map.entrySet();
			out.print ("], returns ");
			out.println (o);

			out.print ("Calling map.equals (map) [");
			b = map.equals (map);
			out.print ("], returns ");
			out.println (b);

			out.print ("Calling map.hashCode() [");
			i = map.hashCode();
			out.print ("], returns ");
			out.println (i);

			out.print ("Calling map.toString() [");
			s = map.toString();
			out.print ("], returns ");
			out.println (s);
			}

		catch (Throwable exc)
			{
			System.err.println ("MapStubTest: Uncaught exception");
			exc.printStackTrace (System.err);
			}
		}

	}
