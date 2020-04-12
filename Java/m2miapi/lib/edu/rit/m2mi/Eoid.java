//******************************************************************************
//
// File:    Eoid.java
// Package: edu.rit.m2mi
// Unit:    Class edu.rit.m2mi.Eoid
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

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.io.Serializable;

import java.util.Random;

/**
 * Class Eoid encapsulates an exported object identifier (EOID). The EOID
 * uniquely identifies an object that has been exported to the M2MI layer.
 * <P>
 * An EOID is a 128-bit value (two 64-bit <TT>long</TT> values). The most
 * significant <TT>long</TT> value can be decomposed into the following unsigned
 * fields:
 * <pre>
 * 0xFFFFFFFF00000000  time_low
 * 0x00000000FFFF0000  time_mid
 * 0x000000000000F000  version
 * 0x0000000000000FFF  time_hi
 * </pre>
 * The least significant <TT>long</TT> value can be decomposed into the
 * following unsigned fields:
 * <pre>
 * 0xC000000000000000  variant
 * 0x3FFF000000000000  clock_seq
 * 0x0000FFFFFFFFFFFF  node
 * </pre>
 * The variant field must be 0x2. The version field must be either 0x1 or 0x4.
 * If the version field is 0x4, then the most significant bit of the node field
 * must be set to 1, and the remaining fields are set to values produced by a
 * cryptographically strong pseudo-random number generator. If the version field
 * is 0x1, then the node field is set to an IEEE 802 MAC address, the clock_seq
 * field is set to a 14-bit random number, and the time_low, time_mid, and
 * time_hi fields are set to the least, middle and most significant bits
 * (respectively) of a 60-bit timestamp measured in 100-nanosecond units since
 * midnight, October 15, 1582 UTC.
 * <P>
 * The <I>wildcard</I> EOID, which designates all objects that implement a
 * certain target interface, has a value of 0.
 * <P>
 * <B>M2MI Library Version 02-Jul-2002</B>
 *
 * @author  Alan Kaminsky
 * @version 03-Apr-2002
 */
public class Eoid
        implements Serializable
        {

// Hidden data members.

        private long msword;
        private long lsword;

// Exported constants.

        /**
         * The wildcard EOID.
         */
        public static final Eoid WILDCARD = new Eoid();

// Hidden constructors.

        /**
         * Construct a new wildcard EOID.
         */
        private Eoid()
                {
                msword = 0L;
                lsword = 0L;
                }

// Exported constructors.

        /**
         * Construct a new version-1 EOID. The node field is set to the 48 least
         * significant bits of <TT>node</TT>, the clock_seq field is set to a random
         * number obtained from <TT>prng</TT>, and the time_low, time_mid, and
         * time_hi fields are set based on <TT>System.currentTimeMillis()</TT>.
         *
         * @param  node  IEEE 802 MAC address.
         * @param  prng  Pseudorandom number generator.
         */
        public Eoid
                (long node,
                 Random prng)
                {
                // Compute time field = (current_time + number_of_milliseconds_from_
                // 15_Oct_1582_to_01_Jan_1970) * (number_of_100_nsec_units_per_1_msec).
                long time = (System.currentTimeMillis() + 12219292800000L) * 10000L;

                // Generate clock_seq field.
                long clock_seq = prng.nextInt();

                // Pack fields.
                msword =
                        (0xFFFFFFFF00000000L & (time << 32)) |
                        (0x00000000FFFF0000L & (time >> 16)) |
                        (0x0000000000001000L               ) |
                        (0x0000000000000FFFL & (time >> 48));
                lsword =
                        (0x8000000000000000L                    ) |
                        (0x3FFF000000000000L & (clock_seq << 48)) |
                        (0x0000FFFFFFFFFFFFL & (node           ));
                }

        /**
         * Construct a new version-4 EOID. The fields are set to random numbers
         * obtained from <TT>prng</TT>.
         *
         * @param  prng  Pseudorandom number generator.
         */
        public Eoid
                (Random prng)
                {
                msword = (prng.nextLong() & 0xFFFFFFFFFFFF0FFFL) | 0x0000000000004000L;
                lsword = (prng.nextLong() & 0x3FFFFFFFFFFFFFFFL) | 0x8000800000000000L;
                }

        /**
         * Construct a new EOID whose value is read from the given data input
         * stream. This method assumes the value was written to the stream by {@link
         * #write(java.io.DataOutput) write()}.
         *
         * @param  theDataInput  Data input stream.
         *
         * @exception  IOException
         *     Thrown if an I/O error occurred.
         */
        public Eoid
                (DataInput theDataInput)
                throws IOException
                {
                msword = theDataInput.readLong();
                lsword = theDataInput.readLong();
                }

// Exported operations.

        /**
         * Determine if this EOID is equal to the given object.
         *
         * @param  obj  Object to test.
         *
         * @return  True if <TT>obj</TT> is a non-null instance of class Eoid with
         *          the same value as this EOID, false otherwise.
         */
        public boolean equals
                (Object obj)
                {
                return
                        obj != null &&
                        obj instanceof Eoid &&
                        this.msword == ((Eoid) obj).msword &&
                        this.lsword == ((Eoid) obj).lsword;
                }

        /**
         * Returns a hash code for this EOID.
         *
         * @return  Hash code.
         */
        public int hashCode()
                {
                return
                        ((int) (msword >> 32)) +
                        ((int) (msword      )) +
                        ((int) (lsword >> 32)) +
                        ((int) (lsword      ));
                }

        /**
         * Returns a string version of this EOID. The field values are displayed in
         * hexadecimal with hyphens in between.
         */
        public String toString()
                {
                StringBuffer result = new StringBuffer();
                result.append (hexdigit[(int)(msword >> 60) & 0xF]);
                result.append (hexdigit[(int)(msword >> 56) & 0xF]);
                result.append (hexdigit[(int)(msword >> 52) & 0xF]);
                result.append (hexdigit[(int)(msword >> 48) & 0xF]);
                result.append (hexdigit[(int)(msword >> 44) & 0xF]);
                result.append (hexdigit[(int)(msword >> 40) & 0xF]);
                result.append (hexdigit[(int)(msword >> 36) & 0xF]);
                result.append (hexdigit[(int)(msword >> 32) & 0xF]);
                result.append ('-');
                result.append (hexdigit[(int)(msword >> 28) & 0xF]);
                result.append (hexdigit[(int)(msword >> 24) & 0xF]);
                result.append (hexdigit[(int)(msword >> 20) & 0xF]);
                result.append (hexdigit[(int)(msword >> 16) & 0xF]);
                result.append ('-');
                result.append (hexdigit[(int)(msword >> 12) & 0xF]);
                result.append ('-');
                result.append (hexdigit[(int)(msword >>  8) & 0xF]);
                result.append (hexdigit[(int)(msword >>  4) & 0xF]);
                result.append (hexdigit[(int)(msword      ) & 0xF]);
                result.append ('-');
                result.append (hexdigit[(int)(lsword >> 62) & 0x3]);
                result.append ('-');
                result.append (hexdigit[(int)(lsword >> 60) & 0x3]);
                result.append (hexdigit[(int)(lsword >> 56) & 0xF]);
                result.append (hexdigit[(int)(lsword >> 52) & 0xF]);
                result.append (hexdigit[(int)(lsword >> 48) & 0xF]);
                result.append ('-');
                result.append (hexdigit[(int)(lsword >> 44) & 0xF]);
                result.append (hexdigit[(int)(lsword >> 40) & 0xF]);
                result.append (hexdigit[(int)(lsword >> 36) & 0xF]);
                result.append (hexdigit[(int)(lsword >> 32) & 0xF]);
                result.append (hexdigit[(int)(lsword >> 28) & 0xF]);
                result.append (hexdigit[(int)(lsword >> 24) & 0xF]);
                result.append (hexdigit[(int)(lsword >> 20) & 0xF]);
                result.append (hexdigit[(int)(lsword >> 16) & 0xF]);
                result.append (hexdigit[(int)(lsword >> 12) & 0xF]);
                result.append (hexdigit[(int)(lsword >>  8) & 0xF]);
                result.append (hexdigit[(int)(lsword >>  4) & 0xF]);
                result.append (hexdigit[(int)(lsword      ) & 0xF]);
                return result.toString();
                }

        private static final char[] hexdigit = new char[]
                {'0', '1', '2', '3', '4', '5', '6', '7',
                 '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

        /**
         * Write this EOID to the given data output stream.
         *
         * @param  theDataOutput  Data output stream.
         *
         * @exception  IOException
         *     Thrown if an I/O error occurred.
         */
        public void write
                (DataOutput theDataOutput)
                throws IOException
                {
                theDataOutput.writeLong (msword);
                theDataOutput.writeLong (lsword);
                }

        }
