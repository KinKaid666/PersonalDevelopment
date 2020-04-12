//******************************************************************************
//
// File:    MultihandleTransport.java
// Package: edu.rit.m2mi
// Unit:    Class edu.rit.m2mi.MultihandleTransport
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

import java.io.InvalidObjectException;
import java.io.ObjectStreamException;
import java.io.Serializable;

/**
 * Class MultihandleTransport is used to transport a multihandle in serialized
 * form. When a multihandle is written to an object output stream, the
 * multihandle replaces itself in the stream with an instance of class
 * MultihandleTransport containing all the information needed to reconstitute
 * the multihandle at the far end. When an instance of class
 * MultihandleTransport is read from an object input stream, the multihandle
 * transport object asks the M2MI layer to synthesize the proper multihandle
 * class if necessary and create an instance of the multihandle class, then the
 * multihandle transport class replaces itself in the stream with the newly
 * created multihandle.
 * <P>
 * <B>M2MI Library Version 02-Jul-2002</B>
 *
 * @author  Alan Kaminsky
 * @version 31-May-2002
 */
class MultihandleTransport
	implements Serializable
	{

// Hidden data members.

	/**
	 * Exported object identifier (EOID).
	 */
	private Eoid myEoid;

	/**
	 * Target interface list.
	 */
	private TargetInterfaceList myTargetInterfaceList;

// Exported constructors.

	/**
	 * Construct a new multihandle transport object.
	 *
	 * @param  theEoid
	 *     Exported object identifier (EOID).
	 * @param  theTargetInterfaceList
	 *     Target interface list.
	 */
	public MultihandleTransport
		(Eoid theEoid,
		 TargetInterfaceList theTargetInterfaceList)
		{
		myEoid = theEoid;
		myTargetInterfaceList = theTargetInterfaceList;
		}

// Hidden operations.

	/**
	 * Replace this multihandle transport object with an instance of the proper
	 * multihandle class during object deserialization.
	 *
	 * @return  Replacement object.
	 *
	 * @exception  ObjectStreamException
	 *     Thrown if there was a problem during deserialization.
	 */
	private Object readResolve()
		throws ObjectStreamException
		{
		//*DBG*/System.out.println ("MultihandleTransport.readResolve()");
		try
			{
			return M2MI.createMultihandle (myTargetInterfaceList, myEoid);
			}
		catch (Exception exc)
			{
			/* 
			** Modified by etf2954
			**
			InvalidObjectException exc2 =
				new InvalidObjectException ("Cannot deserialize multihandle");
			exc2.initCause (exc);
			*/
			InvalidObjectException exc2 =
				new InvalidObjectException ("Cannot deserialize multihandle" + " : caused by : " + exc.toString() );
			throw exc2;
			}
		}

	}
