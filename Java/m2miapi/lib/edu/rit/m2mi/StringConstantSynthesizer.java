//******************************************************************************
//
// File:    StringConstantSynthesizer.java
// Package: edu.rit.m2mi
// Unit:    Class edu.rit.m2mi.StringConstantSynthesizer
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

import edu.rit.classfile.FieldReference;
import edu.rit.classfile.ListFullException;
import edu.rit.classfile.SynthesizedClassConstantFieldDescription;
import edu.rit.classfile.SynthesizedClassDescription;

import java.util.HashMap;

/**
 * Class StringConstantSynthesizer encapsulates an object for synthesizing
 * string constants in a synthesized class. Each distinct constant string is
 * stored in a private static final field of the synthesized class. These fields
 * are named <TT>"__s1"</TT>, <TT>"__s2"</TT>, and so on. The string constants
 * may then be obtained by reading the static fields.
 * <P>
 * <I>Note:</I> Class StringConstantSynthesizer is not multiple thread safe. Be
 * sure only one thread at a time calls methods on an instance of this class.
 * <P>
 * <B>M2MI Library Version 02-Jul-2002</B>
 *
 * @author  Alan Kaminsky
 * @version 21-May-2002
 */
class StringConstantSynthesizer
	{

// Hidden data members.

	// Synthesized class description to which to add static fields.
	private SynthesizedClassDescription myClassDescription;

	// Map from the string constant's value (type String) to a reference for the
	// corresponding static field (type FieldReference).
	private HashMap myStringMap = new HashMap();

	// Number of string constants generated.
	private int myStringCount = 0;

// Exported constructors.

	/**
	 * Construct a new string constant synthesizer. Static fields for the string
	 * constants will be added to the given synthesized class description as
	 * needed.
	 *
	 * @param  theClassDescription  Synthesized class description.
	 *
	 * @exception  NullPointerException
	 *     (unchecked exception) Thrown if <TT>theClassDescription</TT> is null.
	 */
	public StringConstantSynthesizer
		(SynthesizedClassDescription theClassDescription)
		{
		if (theClassDescription == null)
			{
			throw new NullPointerException();
			}
		myClassDescription = theClassDescription;
		}

// Exported operations.

	/**
	 * Obtain a reference to the static field containing the given string
	 * constant. If necessary, a private static final field for the string
	 * constant is added to the synthesized class description.
	 *
	 * @param  value  String constant's value.
	 *
	 * @return  Field reference.
	 *
	 * @exception  NullPointerException
	 *     (unchecked exception) Thrown if <TT>value</TT> is null.
	 * @exception  ListFullException
	 *     Thrown if the class has too many constant pool entries or too many
	 *     fields.
	 */
	public FieldReference getStringConstant
		(String value)
		throws ListFullException
		{
		if (value == null)
			{
			throw new NullPointerException();
			}
		SynthesizedClassConstantFieldDescription result =
			(SynthesizedClassConstantFieldDescription) myStringMap.get (value);
		if (result == null)
			{
			result = new SynthesizedClassConstantFieldDescription
				(myClassDescription,
				 "__s" + (++ myStringCount),
				 value);
			result.setPrivate();
			result.setFinal();
			myStringMap.put (value, result);
			}
		return result;
		}

	}
