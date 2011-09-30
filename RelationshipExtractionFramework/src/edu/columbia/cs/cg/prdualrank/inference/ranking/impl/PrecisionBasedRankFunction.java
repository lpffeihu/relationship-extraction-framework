package edu.columbia.cs.cg.prdualrank.inference.ranking.impl;

import java.util.Comparator;

import edu.columbia.cs.cg.prdualrank.inference.ranking.RankFunction;

public class PrecisionBasedRankFunction<T> extends RankFunction<T> {

	@Override
	protected Comparator<T> getComparator() {
		
		return new Comparator<T>() {

			@Override
			public int compare(T o1, T o2) {
				
				return precisionMap.get(o2).compareTo(precisionMap.get(o1));
				
			}
		};
		
	}

	@Override
	public boolean requiresPrecision() {
		return true;
	}
}
