//******************************************************************************
//
// File:    NamedClassReference.java
// Package: edu.rit.classfile
// Unit:    Class edu.rit.classfile.NamedClassReference
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
 * Class NamedClassReference is used to create a class reference given the class
 * name. In the documentation below, the term "referenced class" means "the
 * class referred to by this named class reference object."
 * <P>
 * <B>RIT Classfile Library Version 02-Jul-2002</B>
 *
 * @author  Alan Kaminsky
 * @version 12-Sep-2001
 */
public class NamedClassReference
	extends ClassReference
	{

// Exported constructors.

	/**
	 * Construct a new named class reference.
	 *
	 * @param  theClassName
	 *     Referenced class's fully-qualified name. The fully qualified class
	 *     name uses periods, for example: <TT>"com.foo.Bar"</TT>.
	 *
	 * @exception  NullPointerException
	 *     (unchecked exception) Thrown if <TT>theClassName</TT> is null.
	 * @exception  IllegalArgumentException
	 *     (unchecked exception) Thrown if <TT>theClassName</TT> is zero length.
	 */
	public NamedClassReference
		(String theClassName)
		{
		if (theClassName.length() == 0)
			{
			throw new IllegalArgumentException();
			}
		myTypeName = theClassName;
		myClassInternalName = toInternalForm (myTypeName);
		myTypeDescriptor = toDescriptor (myClassInternalName);
		}

// Exported constants.

	/**
	 * A class reference for class <TT>java.lang.Object</TT>.
	 */
	public static final NamedClassReference JAVA_LANG_OBJECT =
		new NamedClassReference ("java.lang.Object");

	/**
	 * A class reference for class <TT>java.lang.String</TT>.
	 */
	public static final NamedClassReference JAVA_LANG_STRING =
		new NamedClassReference ("java.lang.String");

	}
