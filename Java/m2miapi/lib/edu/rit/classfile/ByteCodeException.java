//******************************************************************************
//
// File:    ByteCodeException.java
// Package: edu.rit.classfile
// Unit:    Class edu.rit.classfile.ByteCodeException
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

package edu.rit.classfile;

/**
 * Class ByteCodeException encapsulates the exception thrown when there was a
 * problem determining an instruction's bytecodes.
 * <P>
 * <B>RIT Classfile Library Version 02-Jul-2002</B>
 *
 * @author  Alan Kaminsky
 * @version 01-Oct-2001
 */
public class ByteCodeException
	extends ClassfileException
	{

// Exported constructors.

	/**
	 * Construct a new bytecode exception with no detail message.
	 */
	public ByteCodeException()
		{
		super();
		}

	/**
	 * Construct a new bytecode exception with the given detail message.
	 *
	 * @param  msg  Detail message.
	 */
	public ByteCodeException
		(String msg)
		{
		super (msg);
		}

	}
