//******************************************************************************
//
// File:    SynthesisException.java
// Package: edu.rit.m2mi
// Unit:    Class edu.rit.m2mi.SynthesisException
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

/**
 * Class SynthesisException encapsulates an exception thrown when M2MI is unable
 * to synthesize or instantiate a handle class or a method invoker class. The
 * SynthesisException's detail message and/or chained exception provide further
 * information.
 * <P>
 * <B>M2MI Library Version 02-Jul-2002</B>
 *
 * @author  Alan Kaminsky
 * @version 06-May-2002
 */
public class SynthesisException
	extends Exception
	{

// Exported constructors.

	/**
	 * Construct a new synthesis exception with no detail message and no chained
	 * exception.
	 */
	public SynthesisException()
		{
		super();
		}

	/**
	 * Construct a new synthesis exception with the given detail message and no
	 * chained exception.
	 *
	 * @param  msg  Detail message.
	 */
	public SynthesisException
		(String msg)
		{
		super (msg);
		}

	/*
	** Removed by etf2954
	**
	/**
	 * Construct a new synthesis exception with no detail message and the given
	 * chained exception.
	 *
	 * @param  exc  Chained exception.
	 * /
	public SynthesisException
		(Throwable exc)
		{
		super (exc);
		}
	*/

	/*
	** Removed by etf2954
	**
	/**
	 * Construct a new synthesis exception with the given detail message and the
	 * given chained exception.
	 *
	 * @param  msg  Detail message.
	 * @param  exc  Chained exception.
	 * /
	public SynthesisException
		(String msg,
		 Throwable exc)
		{
		super (msg, exc);
		}
	*/
	}
