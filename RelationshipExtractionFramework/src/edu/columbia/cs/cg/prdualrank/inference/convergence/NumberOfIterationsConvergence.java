package edu.columbia.cs.cg.prdualrank.inference.convergence;

public class NumberOfIterationsConvergence implements ConvergenceFinder {
	
	private int iterations;
	private int currentIteration;

	public NumberOfIterationsConvergence(int iterations) {
		this.iterations = iterations;
		this.currentIteration = 0;
	}

	@Override
	public void reset() {
		
		currentIteration = 0;

	}

	@Override
	public boolean converged() {
		
		if (currentIteration >= iterations){
			return true;
		}
		
		currentIteration++;
		
		return false;
	}

}
