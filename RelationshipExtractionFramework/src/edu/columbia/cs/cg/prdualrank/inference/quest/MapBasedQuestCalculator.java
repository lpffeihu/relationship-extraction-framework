package edu.columbia.cs.cg.prdualrank.inference.quest;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import edu.columbia.cs.cg.pattern.Pattern;
import edu.columbia.cs.cg.prdualrank.graph.PRDualRankGraph;
import edu.columbia.cs.cg.prdualrank.inference.convergence.ConvergenceFinder;
import edu.columbia.cs.cg.relations.Relationship;
import edu.columbia.cs.utils.Pair;

public class MapBasedQuestCalculator implements QuestCalculator {
	private Map<Pattern,Pair<Double,Double>> patternTable;
	private Map<Relationship,Pair<Double,Double>> tupleTable;

	private Map<Pattern,Double> patternPrecision = null;
	private Map<Pattern,Double> patternRecall = null;
	private Map<Relationship,Double> tuplePrecision = null;
	private Map<Relationship,Double> tupleRecall = null;
	
	private ConvergenceFinder convergence;
	private Set<Relationship> seeds;

	public MapBasedQuestCalculator(Set<Relationship> seeds, ConvergenceFinder convergence) {
		
		this.convergence = convergence;
		this.seeds = seeds;
		patternTable = new HashMap<Pattern, Pair<Double,Double>>();
		tupleTable = new HashMap<Relationship, Pair<Double,Double>>();

	}

	@Override
	public void runQuestP(PRDualRankGraph gs) {
		convergence.reset();
		
		while(!convergence.converged()){
			
			for (Pattern pattern : gs.getPatterns()) {
				
				double precision = calculatePrecision(pattern,gs);
				setPrecision(pattern,precision,patternTable);
				
			}
			
			for (Relationship tuple: gs.getTuples()){
				
				double precision = calculatePrecision(tuple,gs);
				setPrecision(tuple,precision,tupleTable);
				
			}
			
		}

		
	}

	@Override
	public void runQuestR(PRDualRankGraph gs) {
		convergence.reset();
		
		while(convergence.converged()){
			
			for (Pattern pattern : gs.getPatterns()) {
				
				double recall = calculateRecall(pattern, gs);
				setRecall(pattern,recall,patternTable);
				
			}
			
			for (Relationship tuple: gs.getTuples()){
				
				double recall = calculateRecall(tuple,gs);
				setRecall(tuple,recall,tupleTable);
				
			}
			
		}

		
	}

	private double calculatePrecision(Relationship tuple,PRDualRankGraph gs) {
		
		if (seeds.contains(tuple))
			return getPrecision(tuple);
		
		double precision = 0.0;
		
		for (Pattern pattern : gs.getMatchingPatterns(tuple)) {
			
			precision += getPrecision(pattern)*gs.getMatchingFrequency(tuple, pattern)/gs.getFrequency(tuple);
			
		}
		
		return precision;
		
	}

	private double getPrecision(Pattern pattern) {
		return patternTable.get(pattern).a();
	}

	private <T> void setPrecision(T pattern, double precision, Map<T,Pair<Double,Double>> table) {
		Pair<Double, Double> value = table.get(pattern);
		
		double recall = 0.0;
		
		if (value != null)
			recall = value.b();
			
		table.put(pattern, new Pair<Double,Double>(precision,recall));
		
	}

	private double calculatePrecision(Pattern pattern, PRDualRankGraph gs) {
		
		double precision = 0.0;
		
		for (Relationship tuple : gs.getMatchingTuples(pattern)) {
			
			precision += getPrecision(tuple)*gs.getMatchingFrequency(pattern,tuple)/gs.getFreqency(pattern);
			
		}
		
		return precision;
	}

	private double getPrecision(Relationship tuple) {
		
		if (seeds.contains(tuple)){
			return P0(tuple);
		}
		
		return getPrecision(tuple,tupleTable);
		
	}

	private double P0(Relationship tuple) {
		return 1.0;
	}

	private <T> double getPrecision(T key,
			Map<Relationship, Pair<Double, Double>> table) {
		
		return table.get(key).a();
		
	}

	private double calculateRecall(Relationship tuple,PRDualRankGraph gs) {
		
		double recall = 0.0;
		
		for (Pattern pattern : gs.getMatchingPatterns(tuple)) {
			
			recall += getRecall(pattern,patternTable)*gs.getMatchingFrequency(tuple, pattern)/gs.getFreqency(pattern);
			
		}
		
		return recall;
	}

	private <T> double getRecall(T key, Map<T,Pair<Double,Double>> table) {
		return table.get(key).b();
	}

	private <T> void setRecall(T key, double recall, Map<T,Pair<Double,Double>> table) {
		
		Pair<Double,Double> value = table.get(key);
		
		double precision = 0.0;
		
		if (value != null)	
			precision = value.a();
			
		table.put(key, new Pair<Double,Double>(precision,recall));
		
	}

	private double calculateRecall(Pattern pattern, PRDualRankGraph gs) {
		
		double recall = 0.0;
		
		for (Relationship tuple : gs.getMatchingTuples(pattern)) {

			recall += getRecall(tuple,tupleTable)*gs.getMatchingFrequency(pattern, tuple)/gs.getFrequency(tuple);
			
		}
		
		return recall;
		
	}

	@Override
	public Map<Pattern, Double> getPatternPrecisionMap() {
		
		if (patternPrecision == null){
			generatePatternMaps();
		}
	
		return patternPrecision;
	}

	@Override
	public Map<Relationship, Double> getTuplePrecisionMap() {
		
		if (tuplePrecision == null){
			generateTupleMaps();
		}
		return tuplePrecision;
	}

	private void generateTupleMaps() {
		
		tuplePrecision = new HashMap<Relationship, Double>();
		tupleRecall = new HashMap<Relationship, Double>();
		
		loadMap(tupleTable,tuplePrecision,tupleRecall);
		
	}

	private <T> void loadMap(Map<T, Pair<Double, Double>> table,
			Map<T, Double> precision,
			Map<T, Double> recall) {

		for (Entry<T, Pair<Double, Double>> entry : table.entrySet()) {
			
			precision.put(entry.getKey(), entry.getValue().a());
			recall.put(entry.getKey(), entry.getValue().b());
			
		}

		
	}

	@Override
	public Map<Pattern, Double> getPatternRecallMap() {
		if (patternRecall == null){
			generatePatternMaps();
		}
		return patternRecall;
	}

	private void generatePatternMaps() {
		
		patternPrecision = new HashMap<Pattern, Double>();
		patternRecall = new HashMap<Pattern, Double>();
		
		loadMap(patternTable, patternPrecision, patternRecall);
	}

	@Override
	public Map<Relationship, Double> getTupleRecallMap() {
		
		if (tupleRecall == null){
			generateTupleMaps();
		}
		return tupleRecall;
		
	}
	
}
