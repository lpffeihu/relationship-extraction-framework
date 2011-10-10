/**
 * Defines the interface of Convergence methods for Inference in the PRDualRank inference method.
 *
 * @author      Pablo Barrio
 * @author		Goncalo Simoes
 * @version     0.1
 * @since       2011-10-07
 */
package edu.columbia.cs.cg.prdualrank.inference.convergence;

public interface ConvergenceFinder {

	void reset();

	boolean converged();

}
