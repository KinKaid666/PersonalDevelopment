//******************************************************************************
//
// File:    RefInstruction.java
// Package: edu.rit.classfile
// Unit:    Class edu.rit.classfile.RefInstruction
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
 * Class RefInstruction encapsulates a Java bytecode instruction that refers to
 * a constant pool entry. The constant pool index is stored in the second and
 * third bytes of the instruction.
 * <P>
 * <B>RIT Classfile Library Version 02-Jul-2002</B>
 *
 * @author  Alan Kaminsky
 * @version 01-Oct-2001
 */
abstract class RefInstruction
	extends ThreeByteInstruction
	{

// Hidden constructors.

	/**
	 * Construct a new constant pool reference instruction with the given
	 * opcode.
	 *
	 * @param  theByteCode0  Opcode.
	 */
	RefInstruction
		(byte theByteCode0)
		{
		super (theByteCode0, (byte) 0, (byte) 0);
		}

// Hidden operations.

	/**
	 * Add all constant pool entries this instruction needs to the given
	 * constant pool. This method pertains to instructions that have one or more
	 * constant pool indexes. This method finds or creates the proper constant
	 * pool entry(ies) for this instruction in the given constant pool, then
	 * stores the index(es) of the constant pool entry(ies) in this
	 * instruction's bytecode array.
	 *
	 * @param  theConstantPool  Constant pool in which to find or add entries.
	 *
	 * @exception  ListFullException
	 *     Thrown if the requisite constant pool entries could not be added
	 *     because <TT>theConstantPool</TT> is full.
	 */
	void addConstantPoolEntries
		(SynthesizedConstantPool theConstantPool)
		throws ListFullException
		{
		int cpi = getConstantPoolIndex (theConstantPool);
		myByteCode1 = byte1 (cpi);
		myByteCode2 = byte0 (cpi);
		}

// Hidden operations to be implemented in a subclass.

	/**
	 * Returns the constant pool index for this constant pool reference
	 * instruction's constant pool entry.
	 *
	 * @param  theConstantPool  Constant pool in which to find or add entries.
	 *
	 * @exception  ListFullException
	 *     Thrown if the requisite constant pool entries could not be added
	 *     because <TT>theConstantPool</TT> is full.
	 */
	abstract int getConstantPoolIndex
		(SynthesizedConstantPool theConstantPool)
		throws ListFullException;

	}
