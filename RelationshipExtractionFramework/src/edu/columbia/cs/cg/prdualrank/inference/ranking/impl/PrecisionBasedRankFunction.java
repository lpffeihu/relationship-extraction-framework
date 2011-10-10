/**
 * Ranks objects according to their precision.
 *
 * @author      Pablo Barrio
 * @author		Goncalo Simoes
 * @version     0.1
 * @since       2011-10-07
 */
package edu.columbia.cs.cg.prdualrank.inference.ranking.impl;

import java.util.Comparator;

import edu.columbia.cs.cg.prdualrank.inference.ranking.RankFunction;

public class PrecisionBasedRankFunction<T> extends RankFunction<T> {

	/* (non-Javadoc)
	 * @see edu.columbia.cs.cg.prdualrank.inference.ranking.RankFunction#getComparator()
	 */
	@Override
	protected Comparator<T> getComparator() {
		
		return new Comparator<T>() {

			@Override
			public int compare(T o1, T o2) {
				
				return precisionMap.get(o2).compareTo(precisionMap.get(o1));
				
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
}
