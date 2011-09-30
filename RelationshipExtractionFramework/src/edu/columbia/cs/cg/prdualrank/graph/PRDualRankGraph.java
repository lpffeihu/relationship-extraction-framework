package edu.columbia.cs.cg.prdualrank.graph;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import edu.columbia.cs.cg.pattern.Pattern;
import edu.columbia.cs.cg.relations.Relationship;

public class PRDualRankGraph {

	Map<Pattern,Integer> patternFrequency;
	Map<Relationship, Integer> tupleFrequency;
	Map<Pattern,Map<Relationship,Integer>> patternTupleFrequency;
	Map<Relationship,Map<Pattern, Integer>> tuplePatternFrequency;
	
	public PRDualRankGraph(){
		patternFrequency = new HashMap<Pattern, Integer>();
		tupleFrequency = new HashMap<Relationship, Integer>();
		patternTupleFrequency = new HashMap<Pattern, Map<Relationship,Integer>>();
		tuplePatternFrequency = new HashMap<Relationship, Map<Pattern,Integer>>();
	}
	
	
	public void addContext(Pattern pattern, Relationship tuple, int frequency) {
		
		updateFrequency(patternFrequency,pattern,frequency);
		updateFrequency(tupleFrequency,tuple,frequency);
		updateContext(patternTupleFrequency,pattern,tuple,frequency);
		updateContext(tuplePatternFrequency,tuple,pattern,frequency);
		
	}


	private <I,O> void updateContext(
			Map<I, Map<O, Integer>> contextTable,
			I index, O object, int frequency) {
		
		Map<O,Integer> objectFreq = contextTable.get(index);
		
		if (objectFreq == null){
			objectFreq = new HashMap<O, Integer>();
		}
		
		updateFrequency(objectFreq, object, frequency);
		
		contextTable.put(index, objectFreq);
	}


	private <T> void updateFrequency(Map<T, Integer> freqTable,
			T element, int frequency) {
		
		Integer freq = freqTable.get(element);
		
		if (freq == null){
			freq = 0;
		}
		
		freqTable.put(element, freq + frequency);
	}


	public Set<Pattern> getPatterns() {
		return patternFrequency.keySet();
	}

	public Set<Relationship> getTuples() {
		return tupleFrequency.keySet();
	}


	public int getMatchingFrequency(Pattern pattern, Relationship tuple) {
		
		return patternTupleFrequency.get(pattern).get(tuple);
		
	}


	public Set<Relationship> getMatchingTuples(Pattern pattern) {
		return patternTupleFrequency.get(pattern).keySet();
	}


	public double getFreqency(Pattern pattern) {
		return patternFrequency.get(pattern);
	}


	public int getFrequency(Relationship tuple) {
		return tupleFrequency.get(tuple);
	}


	public int getMatchingFrequency(Relationship tuple, Pattern pattern) {
		return tuplePatternFrequency.get(tuple).get(pattern);
	}


	public Set<Pattern> getMatchingPatterns(Relationship tuple) {
		
		return tuplePatternFrequency.get(tuple).keySet();
		
	}
	
}
