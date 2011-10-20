/**
 * Convergence method based on the number of iterations executed. After the object is asked N times for the method <b>converged</b>, it will return true, meaning that convergence has been reached.
 * 
 * <br>
 * For this Class, Apache Lucene Engine is required. 
 * @see <a href="http://lucene.apache.org/"> Lucene </a>
 * 
 * <br>
 * This class is used for our implementation of: 
 * <b> "Searching Patterns for Relation Extraction over the Web: Rediscovering the Pattern-Relation Duality" </b>. Y. Fang and K. C.-C. Chang. In WSDM, pages 825-834, 2011.
 * 
 * <br>
 * For further information, 
 * 
 * @see <a href="http://www.wsdm2011.org/"> WSDM 2011 Conference Website </a>
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
