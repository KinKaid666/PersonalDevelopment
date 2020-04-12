//******************************************************************************
//
// File:    UnihandleTransport.java
// Package: edu.rit.m2mi
// Unit:    Class edu.rit.m2mi.UnihandleTransport
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
 * Class UnihandleTransport is used to transport a unihandle in serialized
 * form. When a unihandle is written to an object output stream, the
 * unihandle replaces itself in the stream with an instance of class
 * UnihandleTransport containing all the information needed to reconstitute the
 * unihandle at the far end. When an instance of class UnihandleTransport is
 * read from an object input stream, the unihandle transport object asks the
 * M2MI layer to synthesize the proper unihandle class if necessary and create
 * an instance of the unihandle class, then the unihandle transport class
 * replaces itself in the stream with the newly created unihandle.
 * <P>
 * <B>M2MI Library Version 02-Jul-2002</B>
 *
 * @author  Alan Kaminsky
 * @version 31-May-2002
 */
class UnihandleTransport
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
	 * Construct a new unihandle transport object.
	 *
	 * @param  theEoid
	 *     Exported object identifier (EOID).
	 * @param  theTargetInterfaceList
	 *     Target interface list.
	 */
	public UnihandleTransport
		(Eoid theEoid,
		 TargetInterfaceList theTargetInterfaceList)
		{
		myEoid = theEoid;
		myTargetInterfaceList = theTargetInterfaceList;
		}

// Hidden operations.

	/**
	 * Replace this unihandle transport object with an instance of the proper
	 * unihandle class during object deserialization.
	 *
	 * @return  Replacement object.
	 *
	 * @exception  ObjectStreamException
	 *     Thrown if there was a problem during deserialization.
	 */
	private Object readResolve()
		throws ObjectStreamException
		{
		//*DBG*/System.out.println ("UnihandleTransport.readResolve()");
		try
			{
			return M2MI.createUnihandle (myTargetInterfaceList, myEoid);
			}
		catch (Exception exc)
			{
			/*
			** Modified by etf2954 
			**
			InvalidObjectException exc2 =
				new InvalidObjectException ("Cannot deserialize unihandle");
			exc2.initCause (exc);
			*/
			InvalidObjectException exc2 =
				new InvalidObjectException ("Cannot deserialize unihandle" + " : caused by : " + exc.toString());
			throw exc2;
			}
		}

	}
