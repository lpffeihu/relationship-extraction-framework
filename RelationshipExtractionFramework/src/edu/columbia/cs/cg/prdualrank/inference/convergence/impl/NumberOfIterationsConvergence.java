/**
 * Convergence method based on the number of iterations executed.
 *
 * @author      Pablo Barrio
 * @author		Goncalo Simoes
 * @version     0.1
 * @since       2011-10-07
 */
package edu.columbia.cs.cg.prdualrank.inference.convergence.impl;

import edu.columbia.cs.cg.prdualrank.inference.convergence.ConvergenceFinder;

public class NumberOfIterationsConvergence implements ConvergenceFinder {
	
	private int iterations;
	private int currentIteration;

	/**
	 * Creates a new instance of the NumberOfIterationsConvergence class.
	 *
	 * @param iterations the number of iterations to be performed before reporting convergence.
	 */
	public NumberOfIterationsConvergence(int iterations) {
		this.iterations = iterations;
		this.currentIteration = 0;
	}

	/* (non-Javadoc)
	 * @see edu.columbia.cs.cg.prdualrank.inference.convergence.ConvergenceFinder#reset()
	 */
	@Override
	public void reset() {
		
		currentIteration = 0;

	}

	/* (non-Javadoc)
	 * @see edu.columbia.cs.cg.prdualrank.inference.convergence.ConvergenceFinder#converged()
	 */
	@Override
	public boolean converged() {
		if (currentIteration >= iterations){
			return true;
		}
		
		currentIteration++;
		
		return false;
	}

}
