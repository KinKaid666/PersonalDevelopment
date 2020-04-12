//******************************************************************************
//
// File:    ParticleImpl.java
// Package: edu.rit.m2mi.nbodies1
// Unit:    Class edu.rit.nbodies1.ParticleImpl
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
 * Class ParticleImpl encapsulates a charged particle in a two-dimensional N
 * Bodies Simulation.
 * <P>
 * <B>M2MI Library Version 02-Jul-2002</B>
 *
 * @author  Alan Kaminsky
 * @version 25-Apr-2002
 */
public class ParticleImpl
	implements Particle
	{

// Hidden constants.

	private static final double CENTRAL_FORCE = 0.1;

// Hidden data members.

	// Particle id.
	private int myId;

	// Particle charge.
	private double myCharge;

	// Particle position.
	private double myPosX;
	private double myPosY;

	// Particle velocity.
	private double myVelX;
	private double myVelY;

	// Vector sum of forces from other particles.
	private double myForceX = 0.0;
	private double myForceY = 0.0;

	// For sending reports.
	private Particle allParticles;

// Exported constructors.

	/**
	 * Construct a new particle.
	 *
	 * @param  id  Particle ID.
	 * @param  c     Particle charge.
	 * @param  posX  X coordinate of particle initial position.
	 * @param  posY  Y coordinate of particle initial position.
	 * @param  velX  X coordinate of particle initial velocity.
	 * @param  velY  Y coordinate of particle initial velocity.
	 *
	 * @exception  SynthesisException
	 *     Thrown if there was a problem synthesizing handle classes.
	 */
	public ParticleImpl
		(int id,
		 double c,
		 double posX,
		 double poxY,
		 double velX,
		 double velY)
		throws SynthesisException
		{
		myId = id;
		myCharge = c;
		myPosX = posX;
		myPosY = poxY;
		myVelX = velX;
		myVelY = velY;
		allParticles = (Particle) M2MI.getOmnihandle (Particle.class);
		}

// Exported operations inherited and implemented from interface Particle.

	/**
	 * Start the simulation. Report this particle's charge and position.
	 */
	public synchronized void start()
		{
		myForceX = 0.0;
		myForceY = 0.0;
		allParticles.report (myId, myCharge, myPosX, myPosY);
		}

	/**
	 * Report some particle's charge and position. Accumulate that information
	 * into the calculation of this particle's velocity for the next time step.
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
		if (id != myId)
			{
			double dx = x - myPosX;
			double dy = y - myPosY;
			double d = Math.sqrt (dx*dx + dy*dy);
			double f = (-c * myCharge) / (d * d);
			myForceX += f * dx / d;
			myForceY += f * dy / d;
			}
		}

	/**
	 * Take a time step. Move this particle according to its current velocity
	 * for the given amount of time, then report this particle's charge and new
	 * position.
	 *
	 * @param  dt  Size of time step.
	 */
	public synchronized void step
		(double dt)
		{
		myVelX += (myForceX - CENTRAL_FORCE * myPosX) * dt;
		myVelY += (myForceY - CENTRAL_FORCE * myPosY) * dt;
		myPosX += myVelX * dt;
		myPosY += myVelY * dt;
		myForceX = 0.0;
		myForceY = 0.0;
		allParticles.report (myId, myCharge, myPosX, myPosY);
		myForceX = 0.0;
		myForceY = 0.0;
		}

	}
