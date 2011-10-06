package edu.columbia.cs.cg.prdualrank.inference.ranking;

import java.util.Comparator;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

public abstract class RankFunction <T> {

	private class ObjectAfterRankComparator<T> implements Comparator<T>{

		private Comparator<? super T> comparator;

		public ObjectAfterRankComparator(Comparator<? super T> comparator) {
			this.comparator = comparator;
		}

		@Override
		public int compare(T o1, T o2) {
			
			int comp = comparator.compare(o1, o2);
			
			if (comp == 0){
				return -1;
			}
			
			return 1;
		}
		
	}
	
	protected Map<T, Double> precisionMap;
	protected Map<T, Double> recallMap;
	private Set<T> elements;

	public boolean requiresPrecision() {
		return false;
	}

	public boolean requiresRecall() {
		return false;
	}

	public void setPrecision(Map<T, Double> precisionMap) {

		this.precisionMap = precisionMap;
		this.elements = precisionMap.keySet();
	}

	public void setRecall(Map<T, Double> recallMap) {

		this.recallMap = recallMap;
		this.elements = recallMap.keySet();
	}

	public SortedSet<T> rank() {
		
		SortedSet<T> ret = new TreeSet<T>(new ObjectAfterRankComparator<T>(getComparator()));
		
		for (T element : elements) {
			
			ret.add(element);
			
		}

		return ret;

	}

	protected abstract Comparator<? super T> getComparator();

}
