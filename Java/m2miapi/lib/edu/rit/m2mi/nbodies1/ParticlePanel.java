//******************************************************************************
//
// File:    ParticlePanel.java
// Package: edu.rit.m2mi.nbodies1
// Unit:    Class edu.rit.nbodies1.ParticlePanel
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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import java.awt.geom.Ellipse2D;

import javax.swing.JPanel;

/**
 * Class ParticlePanel is a UI that displays the positions of charged particles
 * in a two-dimensional N Bodies Simulation. It implements interface {@link
 * Particle </TT>Particle<TT>} and receives particle position reports via M2MI
 * invocations.
 * <P>
 * <B>M2MI Library Version 02-Jul-2002</B>
 *
 * @author  Alan Kaminsky
 * @version 25-Apr-2002
 */
public class ParticlePanel
	extends JPanel
	implements Particle
	{

// Hidden constants.

	private static final double DIAM = 5;
	private static final Color POSITIVE_COLOR = Color.red;
	private static final Color NEGATIVE_COLOR = Color.black;

// Hidden data members.

	private int n;
	private int pixelWidth;
	private int pixelHeight;
	private double minX;
	private double minY;
	private double scaleX;
	private double scaleY;

	private double[] particleC;
	private double[] particleX;
	private double[] particleY;

	private Ellipse2D dot = new Ellipse2D.Double();

// Exported constructors.

	/**
	 * Construct a new particle panel.
	 *
	 * @param  n            Number of particles to display.
	 * @param  pixelWidth   Width of display in pixels.
	 * @param  pixelHeight  Height of display in pixels.
	 * @param  minX         X coordinate of left edge of display.
	 * @param  maxX         X coordinate of right edge of display.
	 * @param  minY         Y coordinate of top edge of display.
	 * @param  maxY         Y coordinate of bottom edge of display.
	 */
	public ParticlePanel
		(int n,
		 int pixelWidth,
		 int pixelHeight,
		 double minX,
		 double maxX,
		 double minY,
		 double maxY)
		{
		super();
		Dimension theSize = new Dimension (pixelWidth, pixelHeight);
		setMinimumSize (theSize);
		setPreferredSize (theSize);

		this.n = n;
		this.pixelWidth = pixelWidth;
		this.pixelHeight = pixelHeight;
		this.minX = minX;
		this.minY = minY;
		this.scaleX = pixelWidth / (maxX - minX);
		this.scaleY = pixelHeight / (maxY - minY);

		particleC = new double [n];
		particleX = new double [n];
		particleY = new double [n];
		}

// Exported operations inherited and implemented from interface Particle.

	/**
	 * Start the simulation.
	 */
	public synchronized void start()
		{
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
		particleC[id] = c;
		particleX[id] = x;
		particleY[id] = y;
		}

	/**
	 * Take a time step.
	 *
	 * @param  dt  Size of time step.
	 */
	public synchronized void step
		(double dt)
		{
		repaint (0L, 0, 0, pixelWidth, pixelHeight);
		}

// Hidden operations.

	/**
	 * Paint this particle panel's display.
	 *
	 * @param  g  Graphics context.
	 */
	protected synchronized void paintComponent
		(Graphics g)
		{
		super.paintComponent (g);
		Graphics2D g2d = (Graphics2D) g.create();
		g2d.setRenderingHint
			(RenderingHints.KEY_ANTIALIASING,
			 RenderingHints.VALUE_ANTIALIAS_ON);
		for (int i = 0; i < n; ++ i)
			{
			g2d.setColor
				(particleC[i] >= 0.0 ? POSITIVE_COLOR : NEGATIVE_COLOR);
			dot.setFrame
				((particleX[i] - minX) * scaleX - DIAM/2,
				 (particleY[i] - minY) * scaleY - DIAM/2,
				 DIAM,
				 DIAM);
			g2d.fill (dot);
			}
		}

	}
