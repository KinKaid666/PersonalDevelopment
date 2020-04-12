//******************************************************************************
//
// File:    Particle.java
// Package: edu.rit.m2mi.nbodies1
// Unit:    Interface edu.rit.nbodies1.Particle
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

package edu.rit.m2mi.nbodies1;

/**
 * Interface Particle specifies the interface for a charged particle in a
 * two-dimensional N Bodies Simulation.
 * <P>
 * <B>M2MI Library Version 02-Jul-2002</B>
 *
 * @author  Alan Kaminsky
 * @version 25-Apr-2002
 */
public interface Particle
	{

// Exported operations.

	/**
	 * Start the simulation. Report this particle's charge and position.
	 */
	public void start();

	/**
	 * Report some particle's charge and position. Accumulate that information
	 * into the calculation of this particle's velocity for the next time step.
	 *
	 * @param  id  Particle ID.
	 * @param  c   Charge.
	 * @param  x   X position.
	 * @param  y   Y position.
	 */
	public void report
		(int id,
		 double c,
		 double x,
		 double y);

	/**
	 * Take a time step. Move this particle according to its current velocity
	 * for the given amount of time, then report this particle's charge and new
	 * position.
	 *
	 * @param  dt  Size of time step.
	 */
	public void step
		(double dt);

	}
