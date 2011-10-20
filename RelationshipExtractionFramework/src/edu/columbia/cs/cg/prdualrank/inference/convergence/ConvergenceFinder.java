/**
 * Defines the interface of Convergence methods for Inference in the PRDualRank inference method.
 *
 * <br>
 * This class is used for our implementation of: 
 * <b> "Searching Patterns for Relation Extraction over the Web: Rediscovering the Pattern-Relation Duality" </b>. Y. Fang and K. C.-C. Chang. In WSDM, pages 825-834, 2011.
 * 
 * <br>
 * For further information, 
 * 
 * @see <a href="http://www.wsdm2011.org/"> WSDM 2011 Conference Website </a>
 * @author      Pablo Barrio
 * @author		Goncalo Simoes
 * @version     0.1
 * @since       2011-10-07
 */
package edu.columbia.cs.cg.prdualrank.inference.convergence;

public interface ConvergenceFinder {

	/**
	 * Set the state of the convergence finder to initial.
	 */
	void reset();

	/**
	 * Aks if the method has converged.
	 *
	 * @return true, if converged.
	 */
	boolean converged();

}
