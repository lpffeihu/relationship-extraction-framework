package edu.columbia.cs.cg.prdualrank.inference.ranking.impl;

import java.util.Comparator;

import edu.columbia.cs.cg.prdualrank.inference.ranking.RankFunction;

public class RecallBasedRankFunction<T> extends RankFunction<T> {

	@Override
	protected Comparator<T> getComparator() {
		return new Comparator<T>() {

			@Override
			public int compare(T o1, T o2) {
				return recallMap.get(o2).compareTo(recallMap.get(o1));
			}
		};
	}

	@Override
	public boolean requiresRecall() {
		return true;
	}
}
