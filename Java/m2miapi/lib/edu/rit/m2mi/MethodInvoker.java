//******************************************************************************
//
// File:    MethodInvoker.java
// Package: edu.rit.m2mi
// Unit:    Class edu.rit.m2mi.MethodInvoker
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

/**
 * Class MethodInvoker is the abstract base class for a <EM>method invoker</EM>
 * object. A method invoker implements interface {@link java.lang.Runnable
 * </CODE>Runnable<CODE>}. When its {@link #run() run()} method is called, the
 * method invoker calls a certain <EM>target method</EM> declared in a certain
 * <EM>target interface.</EM> The target method is invoked on a certain
 * <EM>target object.</EM> An actual method invoker class is a synthesized
 * subclass of class MethodInvoker that has been customized for a certain target
 * interface and target method. After constructing a method invoker instance,
 * the M2MI layer specifies the target object and the arguments, if any, for the
 * target method.
 * <P>
 * M2MI uses method invokers to perform the actual M2MI invocations on exported
 * objects. By encapsulating the interface- and method-specific aspects of an
 * invocation in the method invoker, the rest of the M2MI layer can be and is
 * written in a generic fashion to work for any interface and any method.
 * <P>
 * <B>M2MI Library Version 02-Jul-2002</B>
 *
 * @author  Alan Kaminsky
 * @version 22-May-2002
 */
public abstract class MethodInvoker
	implements Runnable
	{

// Hidden data members.

	/**
	 * This method invoker's target object.
	 */
	protected Object myTarget = null;

	/**
	 * Serialized arguments for this method invoker's target method, or null if
	 * there are no arguments.
	 */
	protected byte[] myArguments = null;

// Hidden constructors.

	/**
	 * Construct a new method invoker. The method invoker initially has no
	 * target object and no method call arguments; these must be set before
	 * running the method invoker.
	 */
	protected MethodInvoker()
		{
		}

// Exported operations.

	/**
	 * Invoke this method invoker's target method in the target interface. The
	 * method is invoked on the previously-set target object. The previously-set
	 * method arguments, if any, are used.
	 *
	 * @exception  InvocationException
	 *     (unchecked exception) Thrown if the method invocation was not
	 *     successful.
	 */
	public abstract void run();

// Hidden operations.

	/**
	 * Specify this method invoker's target object.
	 *
	 * @param  theTarget  Target object.
	 */
	void setTarget
		(Object theTarget)
		{
		myTarget = theTarget;
		}

	/**
	 * Specify the arguments, if any, this method invoker will pass to the
	 * target method.
	 *
	 * @param  theArguments
	 *     Byte array containing the serialized arguments for this method
	 *     invoker's target method; or null if there are no arguments.
	 */
	void setArguments
		(byte[] theArguments)
		{
		myArguments = theArguments;
		}

	}
