package edu.columbia.cs.cg.prdualrank.graph;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import edu.columbia.cs.cg.document.Document;
import edu.columbia.cs.cg.pattern.Pattern;
import edu.columbia.cs.cg.pattern.matchable.Matchable;
import edu.columbia.cs.cg.relations.Relationship;

public class PRDualRankGraph<T extends Matchable,D extends Document> {

	Map<Pattern<T,D>,Integer> patternFrequency;
	Map<Relationship, Integer> tupleFrequency;
	Map<Pattern<T,D>,Map<Relationship,Integer>> patternTupleFrequency;
	Map<Relationship,Map<Pattern<T,D>, Integer>> tuplePatternFrequency;
	
	public PRDualRankGraph(){
		patternFrequency = new HashMap<Pattern<T,D>, Integer>();
		tupleFrequency = new HashMap<Relationship, Integer>();
		patternTupleFrequency = new HashMap<Pattern<T,D>, Map<Relationship,Integer>>();
		tuplePatternFrequency = new HashMap<Relationship, Map<Pattern<T,D>,Integer>>();
	}
	
	
	public void addContext(Pattern<T,D> pattern, Relationship tuple, int frequency) {
		
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


	private <F> void updateFrequency(Map<F, Integer> freqTable,
			F element, int frequency) {
		
		Integer freq = freqTable.get(element);
		
		if (freq == null){
			freq = 0;
		}
		
		freqTable.put(element, freq + frequency);
	}


	public Set<Pattern<T,D>> getPatterns() {
		return patternFrequency.keySet();
	}

	public Set<Relationship> getTuples() {
		return tupleFrequency.keySet();
	}


	public int getMatchingFrequency(Pattern<T,D> pattern, Relationship tuple) {
		
		return patternTupleFrequency.get(pattern).get(tuple);
		
	}


	public Set<Relationship> getMatchingTuples(Pattern<T,D> pattern) {
		return patternTupleFrequency.get(pattern).keySet();
	}


	public double getFreqency(Pattern<T,D> pattern) {
		return patternFrequency.get(pattern);
	}


	public int getFrequency(Relationship tuple) {
		return tupleFrequency.get(tuple);
	}


	public int getMatchingFrequency(Relationship tuple, Pattern<T,D> pattern) {
		return tuplePatternFrequency.get(tuple).get(pattern);
	}


	public Set<Pattern<T,D>> getMatchingPatterns(Relationship tuple) {
		
		return tuplePatternFrequency.get(tuple).keySet();
		
	}
	
}
