/**
 * Ranks objects based on their f-measure.
 *
 * @author      Pablo Barrio
 * @author		Goncalo Simoes
 * @version     0.1
 * @since       2011-10-07
 */
package edu.columbia.cs.cg.prdualrank.inference.ranking.impl;

import java.util.Comparator;

import edu.columbia.cs.cg.prdualrank.inference.ranking.RankFunction;

public class FScoreBasedRankFunction<T> extends RankFunction<T> {

	private double betasq;

	/**
	 * Instantiates a new f score based rank function.
	 *
	 * @param beta the beta
	 */
	public FScoreBasedRankFunction(double beta){
		this.betasq = beta*beta;
	}
	
	@Override
	protected Comparator<T> getComparator() {
		return new Comparator<T>() {

			@Override
			public int compare(T o1, T o2) {
				
				double precision1 = precisionMap.get(o1);
				double precision2 = precisionMap.get(o2);
				double recall1 = recallMap.get(o1);
				double recall2 = recallMap.get(o2);
				
				double fMeasure1 = calculateFMeasure(precision1,recall1,betasq);
				double fMeasure2 = calculateFMeasure(precision2,recall2,betasq);
				
				return Double.compare(fMeasure2, fMeasure1);
				
			}

			private double calculateFMeasure(double precision, double recall,
					double beta) {
				
				return (1 + betasq)*(precision*recall)/(betasq*precision + recall);
				
			}
		};
	}

	/* (non-Javadoc)
	 * @see edu.columbia.cs.cg.prdualrank.inference.ranking.RankFunction#requiresPrecision()
	 */
	@Override
	public boolean requiresPrecision() {
		return true;
	}
	
	/* (non-Javadoc)
	 * @see edu.columbia.cs.cg.prdualrank.inference.ranking.RankFunction#requiresRecall()
	 */
	@Override
	public boolean requiresRecall() {
		return true;
	}
}
