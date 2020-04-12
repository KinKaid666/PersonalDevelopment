//******************************************************************************
//
// File:    Clock.java
// Package: edu.rit.m2mi.nbodies1
// Unit:    Class edu.rit.nbodies1.Clock
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

import edu.rit.m2mi.M2MI;
import edu.rit.m2mi.SynthesisException;

/**
 * Class Clock encapsulates an object that controls the progress of a
 * two-dimensional N Bodies Simulation. The clock implements interface {@link
 * Particle </TT>Particle<TT>} and keeps track of the particles that have
 * reported their states at each simulation step. Once all particles have
 * reported their states, the clock tells all particles to go to the next
 * simulation step. The clock also inserts a delay at each step to control the
 * real time simulation rate.
 * <P>
 * <B>M2MI Library Version 02-Jul-2002</B>
 *
 * @author  Alan Kaminsky
 * @version 07-May-2002
 */
public class Clock
	implements Particle
	{

// Hidden data members.

	private int myParticleCount;
	private int myReportedCount;
	private double myStepTime;
	private long myDelay;

	private Particle allParticles;

// Exported constructors.

	/**
	 * Construct a new clock object.
	 *
	 * @param  n      Number of particles.
	 * @param  dt     Time duration of each simulation step.
	 * @param  delay  Delay between simulation steps (milliseconds).
	 *
	 * @exception  SynthesisException
	 *     Thrown if there was a problem synthesizing handle classes.
	 */
	public Clock
		(int n,
		 double dt,
		 long delay)
		throws SynthesisException
		{
		myParticleCount = n;
		myStepTime = dt;
		myDelay = delay;
		allParticles = (Particle) M2MI.getOmnihandle (Particle.class);
		}

// Exported operations.

	/**
	 * Start the simulation.
	 */
	public synchronized void start()
		{
		myReportedCount = 0;
		}

	/**
	 * Report some particle's charge and position.
	 *
	 * @param  id  Particle ID.
	 * @param  c   Charge.
	 * @param  x   X position.
	 * @param  y   Y position.
	 */
	public synchronized void report
		(int id,
		 double c,
		 double x,
		 double y)
		{
		++ myReportedCount;
		if (myReportedCount == myParticleCount)
			{
			myReportedCount = 0;
			try
				{
				Thread.sleep (myDelay);
				}
			catch (InterruptedException exc)
				{
				}
			allParticles.step (myStepTime);
			}
		}

	/**
	 * Take a time step.
	 *
	 * @param  dt  Size of time step.
	 */
	public synchronized void step
		(double dt)
		{
		}

	}
