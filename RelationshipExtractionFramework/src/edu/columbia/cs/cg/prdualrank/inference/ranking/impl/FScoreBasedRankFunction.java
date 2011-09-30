package edu.columbia.cs.cg.prdualrank.inference.ranking.impl;

import java.util.Comparator;

import edu.columbia.cs.cg.prdualrank.inference.ranking.RankFunction;

public class FScoreBasedRankFunction<T> extends RankFunction<T> {

	private double betasq;

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
				double fMeasure2 = calculateFMeasure(precision1,recall1,betasq);
				
				return Double.compare(fMeasure2, fMeasure1);
				
			}

			private double calculateFMeasure(double precision, double recall,
					double beta) {
				
				return (1 + betasq)*(precision*recall)/(betasq*precision + recall);
				
			}
		};
	}

	@Override
	public boolean requiresPrecision() {
		return true;
	}
	
	@Override
	public boolean requiresRecall() {
		return true;
	}
}
