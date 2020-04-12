//******************************************************************************
//
// File:    NBodies1.java
// Package: edu.rit.m2mi.nbodies1
// Unit:    Class edu.rit.nbodies1.NBodies1
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

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import java.util.Random;

import javax.swing.JFrame;

/**
 * Class NBodies1 is the main program for a two-dimensional N Bodies Simulation.
 * The program simulates the motion of N positively charged particles and N
 * negatively charged particles. Each particle is initially located at a random
 * position with random velocity. The particles then start moving as they
 * attract and repel each other.
 * <P>
 * Usage: java edu.rit.m2mi.nbodies1.NBodies1 <I>n r c dt delay seed</I>
 * <BR>
 * <I>n</I> = Number of particles (half positive, half negative)
 * <BR>
 * <I>r</I> = Radius of the display
 * <BR>
 * <I>c</I> = Magnitude of the charge of each particle
 * <BR>
 * <I>dt</I> = Simulated time duration of each simulation step
 * <BR>
 * <I>delay</I> = Wall clock duration of each simulation step
 * <BR>
 * <I>seed</I> = Random seed
 * <P>
 * <B>M2MI Library Version 02-Jul-2002</B>
 *
 * @author  Alan Kaminsky
 * @version 25-Apr-2002
 */
public class NBodies1
	{

	/**
	 * Main program.
	 */
	public static void main
		(String[] args)
		{
		try
			{
			if (args.length != 6)
				{
				System.err.println
					("Usage: java edu.rit.m2mi.nbodies1.NBodies1 <n> <r> <c> <dt> <delay> <seed>");
				return;
				}

			int n = Integer.parseInt (args[0]);
			double r = Double.parseDouble (args[1]);
			double c = Double.parseDouble (args[2]);
			double dt = Double.parseDouble (args[3]);
			long delay = Long.parseLong (args[4]);
			long seed = Long.parseLong (args[5]);

			M2MI.initialize (1234L, null);

			final JFrame theFrame = new JFrame ("NBodies1");
			ParticlePanel thePanel =
				new ParticlePanel (n, 400, 400, -r, r, -r, r);
			theFrame.getContentPane().add (thePanel);
			theFrame.addWindowListener
				(new WindowAdapter()
					{
					public void windowClosing
						(WindowEvent e)
						{
						theFrame.dispose();
						System.exit (0);
						}
					});
			theFrame.pack();
			theFrame.setVisible (true);
			Thread.sleep (1000L);

			Random prng = new Random (seed);
			for (int i = 0; i < n; ++ i)
				{
				double posRho = prng.nextDouble() * r;
				double posTheta = prng.nextDouble() * 2.0 * Math.PI;
				double velRho = prng.nextDouble() * r / 10.0;
				double velTheta = prng.nextDouble() * 2.0 * Math.PI;
				M2MI.export
					(new ParticleImpl
						(/*id  */ i,
						 /*c   */ c,
						 /*posX*/ posRho * Math.cos (posTheta),
						 /*poxY*/ posRho * Math.sin (posTheta),
						 /*velX*/ velRho * Math.cos (velTheta),
						 /*velY*/ velRho * Math.sin (velTheta)),
					 Particle.class);
				}

			M2MI.export (thePanel, Particle.class);

			M2MI.export (new Clock (n, dt, delay), Particle.class);

			Particle allParticles =
				(Particle) M2MI.getOmnihandle (Particle.class);
			allParticles.start();
			}

		catch (Throwable exc)
			{
			System.err.println ("NBodies1: Uncaught exception");
			exc.printStackTrace (System.err);
			}
		}

	}
