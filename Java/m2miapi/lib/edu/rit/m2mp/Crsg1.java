//******************************************************************************
//
// File:    Crsg1.java
// Package: edu.rit.m2mp
// Unit:    Class edu.rit.m2mp.Crsg1
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

package edu.rit.m2mp;

/**
 * Class Crsg1 encapsulates a collision-resistant sequence generator (CRSG)
 * using the CRSG1 algorithm by Alan Kaminsky.
 * <P>
 * A CRSG generates a sequence of <I>n</I>-bit numbers by stepping sequentially
 * through some permutation of the integers 0 through
 * 2<SUP><I>n</I></SUP>&#150;1. A CRSG is parameterized by a key that determines
 * which particular permutation will be used. If two CRSGs use different keys,
 * the probability that the two CRSGs generate the same integer at the same time
 * is very small, ideally 2<SUP><I>&#150;n</I></SUP>.
 * <P>
 * The CRSG1 algorithm generates 32-bit sequence numbers by hashing successive
 * values of a counter: Hash(0), Hash(1), Hash(2), . . . The hash function is an
 * iterated Feistel network with 32 iterations. Hash(<I>state</I>) is computed
 * as follows:
 * <P><I>left</I><SUB>0</SUB> =
 *     most significant 16 bits of <I>state</I>
 * <BR><I>right</I><SUB>0</SUB> =
 *     least significant 16 bits of <I>state</I>
 * <BR>For <I>i</I> in 0 .. 31 do
 * <BR>&nbsp;&nbsp;&nbsp;&nbsp;<I>left</I><SUB><I>i</I>+1</SUB> =
 *     <I>right</I><SUB><I>i</I></SUB>
 * <BR>&nbsp;&nbsp;&nbsp;&nbsp;<I>right</I><SUB><I>i</I>+1</SUB> =
 *     <I>left</I><SUB><I>i</I></SUB> ^
 *     (<I>right</I><SUB><I>i</I></SUB> +
 *     (<I>roundkey</I><SUB><I>i</I></SUB> ^
 *     <I>roundconstant</I><SUB><I>i</I></SUB>))
 * <BR>Return <I>left</I><SUB>32</SUB> | <I>right</I><SUB>32</SUB>
 * <P>
 * ^ is 16-bit exclusive-or, + is addition modulo 2<SUP>16</SUP>, and | is
 * concatenation of two 16-bit quantities into a 32-bit quantity.
 * <P>
 * The CRSG1 algorithm&#146;s key is 128 bits. The key is divided into eight
 * 16-bit chunks: <I>key</I><SUB>0</SUB>, <I>key</I><SUB>1</SUB>, . . .
 * <I>key</I><SUB>7</SUB>. Then <I>roundkey</I><SUB><I>i</I></SUB> =
 * <I>key</I><SUB>(<I>i</I> mod 8)</SUB>.
 * <P>
 * The round constants each have 8 zero bits and 8 one bits and serve to
 * counteract bit biases in the key. The round constant values are:
 * <P>
 * <CENTER>
 * <TABLE BORDER=0 CELLPADDING=0 CELLSPACING=0>
 * <TR>
 * <TD VALIGN="top"><I>i</I></TD>
 * <TD WIDTH=10></TD>
 * <TD VALIGN="top"><I>roundconstant</I><SUB><I>i</I></SUB></TD>
 * <TD WIDTH=40></TD>
 * <TD VALIGN="top"><I>i</I></TD>
 * <TD WIDTH=10></TD>
 * <TD VALIGN="top"><I>roundconstant</I><SUB><I>i</I></SUB></TD>
 * <TD WIDTH=40></TD>
 * <TD VALIGN="top"><I>i</I></TD>
 * <TD WIDTH=10></TD>
 * <TD VALIGN="top"><I>roundconstant</I><SUB><I>i</I></SUB></TD>
 * <TD WIDTH=40></TD>
 * <TD VALIGN="top"><I>i</I></TD>
 * <TD WIDTH=10></TD>
 * <TD VALIGN="top"><I>roundconstant</I><SUB><I>i</I></SUB></TD>
 * </TR>
 * <TR>
 * <TD>0</TD> <TD></TD><TD>0xE133</TD><TD></TD>
 * <TD>8</TD> <TD></TD><TD>0x2CD3</TD><TD></TD>
 * <TD>16</TD><TD></TD><TD>0x4B1E</TD><TD></TD>
 * <TD>24</TD><TD></TD><TD>0x4E93</TD>
 * </TR>
 * <TR>
 * <TD>1</TD> <TD></TD><TD>0xA7C2</TD><TD></TD>
 * <TD>9</TD> <TD></TD><TD>0x251F</TD><TD></TD>
 * <TD>17</TD><TD></TD><TD>0x65E4</TD><TD></TD>
 * <TD>25</TD><TD></TD><TD>0xDAA2</TD>
 * </TR>
 * <TR>
 * <TD>2</TD> <TD></TD><TD>0x2D65</TD><TD></TD>
 * <TD>10</TD><TD></TD><TD>0xC56C</TD><TD></TD>
 * <TD>18</TD><TD></TD><TD>0x6BA8</TD><TD></TD>
 * <TD>26</TD><TD></TD><TD>0x6743</TD>
 * </TR>
 * <TR>
 * <TD>3</TD> <TD></TD><TD>0xBD14</TD><TD></TD>
 * <TD>11</TD><TD></TD><TD>0xFA06</TD><TD></TD>
 * <TD>19</TD><TD></TD><TD>0x58D3</TD><TD></TD>
 * <TD>27</TD><TD></TD><TD>0x43D3</TD>
 * </TR>
 * <TR>
 * <TD>4</TD> <TD></TD><TD>0x7638</TD><TD></TD>
 * <TD>12</TD><TD></TD><TD>0x385D</TD><TD></TD>
 * <TD>20</TD><TD></TD><TD>0x6E58</TD><TD></TD>
 * <TD>28</TD><TD></TD><TD>0xDE24</TD>
 * </TR>
 * <TR>
 * <TD>5</TD> <TD></TD><TD>0x06EE</TD><TD></TD>
 * <TD>13</TD><TD></TD><TD>0x8DAC</TD><TD></TD>
 * <TD>21</TD><TD></TD><TD>0xD067</TD><TD></TD>
 * <TD>29</TD><TD></TD><TD>0x7A29</TD>
 * </TR>
 * <TR>
 * <TD>6</TD> <TD></TD><TD>0x38B9</TD><TD></TD>
 * <TD>14</TD><TD></TD><TD>0x8C3B</TD><TD></TD>
 * <TD>22</TD><TD></TD><TD>0x8FC2</TD><TD></TD>
 * <TD>30</TD><TD></TD><TD>0x162F</TD>
 * </TR>
 * <TR>
 * <TD>7</TD> <TD></TD><TD>0xD946</TD><TD></TD>
 * <TD>15</TD><TD></TD><TD>0x443F</TD><TD></TD>
 * <TD>23</TD><TD></TD><TD>0x8D63</TD><TD></TD>
 * <TD>31</TD><TD></TD><TD>0x5B0E</TD>
 * </TR>
 * </TABLE>
 * </CENTER>
 * <P>
 * Note that the CRSG1 algorithm resembles a block cipher with a 32-bit block.
 * Such a block cipher would be patently unsafe, and CRSG1 should not be used
 * for encryption. A CRSG does not need to be cryptographically secure, it
 * merely needs to generate different permutations for different keys. The CRSG1
 * algorithm is still under development and its collision resistance properties
 * have not been studied yet.
 * <P>
 * <B><I>Warning:</I></B> Class Crsg1 is not multiple thread safe.
 * <P>
 * <B>M2MP Library Version 02-Jul-2002</B>
 *
 * @author  Alan Kaminsky
 * @version 22-Oct-2001
 */
public class Crsg1
	{

// Hidden data members.

	private static final int KEYCHUNKS = 8;
	private static final int ROUNDS = 32;

	private static final short[] roundConstant = new short[]
		{
		(short) 0xE133,
		(short) 0xA7C2,
		(short) 0x2D65,
		(short) 0xBD14,
		(short) 0x7638,
		(short) 0x06EE,
		(short) 0x38B9,
		(short) 0xD946,
		(short) 0x2CD3,
		(short) 0x251F,
		(short) 0xC56C,
		(short) 0xFA06,
		(short) 0x385D,
		(short) 0x8DAC,
		(short) 0x8C3B,
		(short) 0x443F,
		(short) 0x4B1E,
		(short) 0x65E4,
		(short) 0x6BA8,
		(short) 0x58D3,
		(short) 0x6E58,
		(short) 0xD067,
		(short) 0x8FC2,
		(short) 0x8D63,
		(short) 0x4E93,
		(short) 0xDAA2,
		(short) 0x6743,
		(short) 0x43D3,
		(short) 0xDE24,
		(short) 0x7A29,
		(short) 0x162F,
		(short) 0x5B0E,
		};

	private short[] myKey = new short [ROUNDS];
	private int myState = 0;

// Exported constructors.

	/**
	 * Construct a new CRSG1 collision resistant sequence generator with the key
	 * set to the given value.
	 *
	 * @param  theKey  Key, an array of <TT>short</TT>s.
	 *
	 * @exception  NullPointerException
	 *     (unchecked exception) Thrown if <TT>theKey</TT> is null.
	 * @exception  IllegalArgumentException
	 *     (unchecked exception) Thrown if <TT>theKey</TT>&#146;s length is not
	 *     8.
	 */
	public Crsg1
		(short[] theKey)
		{
		setKey (theKey);
		}

	/**
	 * Construct a new CRSG1 collision resistant sequence generator with the key
	 * set from the two given <TT>long</TT> values.
	 *
	 * @param  theKey1  First key value.
	 * @param  theKey2  Second key value.
	 */
	public Crsg1
		(long theKey1,
		 long theKey2)
		{
		setKey (theKey1, theKey2);
		}

// Exported operations.

	/**
	 * Set this collision resistant sequence generator&#146;s key to the given
	 * value.
	 *
	 * @param  theKey  Key, an array of <TT>short</TT>s.
	 *
	 * @exception  NullPointerException
	 *     (unchecked exception) Thrown if <TT>theKey</TT> is null.
	 * @exception  IllegalArgumentException
	 *     (unchecked exception) Thrown if <TT>theKey</TT>&#146;s length is not
	 *     8.
	 */
	public void setKey
		(short[] theKey)
		{
		if (theKey.length != KEYCHUNKS)
			{
			throw new IllegalArgumentException();
			}
		System.arraycopy (theKey, 0, myKey, 0, KEYCHUNKS);
		expandKey();
		myState = 0;
		}

	/**
	 * Set this collision resistant sequence generator&#146;s key from the two
	 * given <TT>long</TT> values.
	 *
	 * @param  theKey1  First key value.
	 * @param  theKey2  Second key value.
	 */
	public void setKey
		(long theKey1,
		 long theKey2)
		{
		myKey[0] = (short)(theKey1 & 0xFFFFL); theKey1 >>= 16;
		myKey[1] = (short)(theKey1 & 0xFFFFL); theKey1 >>= 16;
		myKey[2] = (short)(theKey1 & 0xFFFFL); theKey1 >>= 16;
		myKey[3] = (short)(theKey1 & 0xFFFFL);
		myKey[4] = (short)(theKey2 & 0xFFFFL); theKey2 >>= 16;
		myKey[5] = (short)(theKey2 & 0xFFFFL); theKey2 >>= 16;
		myKey[6] = (short)(theKey2 & 0xFFFFL); theKey2 >>= 16;
		myKey[7] = (short)(theKey2 & 0xFFFFL);
		expandKey();
		myState = 0;
		}

	/**
	 * Expand the key. Assumes <TT>myKey[0] .. myKey[7]</TT> are filled in with
	 * the user-supplied key and fills in the rest of <TT>myKey</TT>.
	 */
	private void expandKey()
		{
		int i;
		for (i = KEYCHUNKS; i < ROUNDS; ++ i)
			{
			myKey[i] = myKey[i-KEYCHUNKS];
			}
		for (i = 0; i < ROUNDS; ++ i)
			{
			myKey[i] ^= roundConstant[i];
			}
		}

	/**
	 * Generate the next number from this collision resistant sequence
	 * generator.
	 *
	 * @return  Next number.
	 */
	public int next()
		{
		return hash (myState++);
		}

// Hidden operations.

	/**
	 * Hash function.
	 */
	private int hash
		(int state)
		{
		int left  = (state >> 16) & 0xFFFF;
		int right = (state      ) & 0xFFFF;
		int temp;
		for (int i = 0; i < ROUNDS; ++ i)
			{
			temp = right;
			right = (left ^ (right + myKey[i])) & 0xFFFF;
			left = temp;
			}
		return (left << 16) | right;
		}

// Unit test main program.

//	public static void main
//		(String[] args)
//		{
//		int i, j;
//		try
//			{
//			if (args.length < 3)
//				{
//				System.err.println ("Usage: java edu.rit.anhinga.m2mp.Crsg1 <n> <seed1a> <seed1b> [<seed2a> <seed2b> ...]");
//				return;
//				}
//			int n = Integer.parseInt (args[0]);
//			int k = (args.length - 1) / 2;
//			Crsg1[] theCrsg = new Crsg1 [k];
//			for (j = 0; j < k; ++ j)
//				{
//				theCrsg[j] =
//					new Crsg1
//						(Long.parseLong (args[2*j+1]),
//						 Long.parseLong (args[2*j+2]));
//				}
//			for (i = 0; i < n; ++ i)
//				{
//				for (j = 0; j < k; ++ j)
//					{
//					if (j > 0) System.out.print ('\t');
//					System.out.print (theCrsg[j].next());
//					}
//				System.out.println();
//				}
//			}
//		catch (Throwable exc)
//			{
//			System.err.println ("Crsg1: Uncaught exception");
//			exc.printStackTrace (System.err);
//			}
//		}

	}
