package edu.columbia.cs.cg.prdualrank.inference.ranking;

import java.util.Comparator;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

public abstract class RankFunction <T> {

	
	
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
		
		SortedSet<T> ret = new TreeSet<T>(getComparator());
		
		for (T element : elements) {
			
			ret.add(element);
			
		}
		
		return ret;
	}

	protected abstract Comparator<? super T> getComparator();

}
