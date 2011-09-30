package edu.columbia.cs.cg.prdualrank.inference;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.columbia.cs.cg.pattern.Pattern;
import edu.columbia.cs.cg.prdualrank.graph.PRDualRankGraph;
import edu.columbia.cs.cg.prdualrank.inference.convergence.ConvergenceFinder;
import edu.columbia.cs.cg.relations.Relationship;
import edu.columbia.cs.utils.Pair;

public class InferencePRDualRank {

	private ConvergenceFinder cf;
	private Set<Relationship> seeds;
	private Map<Pattern,Pair<Double,Double>> patternTable;
	private Map<Relationship,Pair<Double,Double>> tupleTable;

	public InferencePRDualRank(ConvergenceFinder cf){
		this.cf = cf;
		patternTable = new HashMap<Pattern, Pair<Double,Double>>();
		tupleTable = new HashMap<Relationship, Pair<Double,Double>>();
	}
	
	public void rank(PRDualRankGraph gs, Set<Relationship> seeds) {
	
		this.seeds = seeds;
		
		for (Relationship relationship : seeds) {
			setPrecision(relationship,1.0,tupleTable);
			setRecall(relationship,1.0/seeds.size(),tupleTable);
		}
		
		runQuestP(gs);
		
		resetConvergence();
		
		runQuestR(gs);
		
		//TODO rank according to precision, recall or F-measure
		
	}

	private void resetConvergence() {
		cf.reset();	
	}

	private void runQuestP(PRDualRankGraph gs) {
		
		while(!reachConvergence()){
			
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

	private <T> double getPrecision(T key,
			Map<Relationship, Pair<Double, Double>> table) {
		
		return table.get(key).a();
		
	}

	private boolean reachConvergence() {
		return cf.converged();
	}

	private void runQuestR(PRDualRankGraph gs) {
		
		while(!reachConvergence()){
			
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


	public List<Relationship> getRankedTuples() {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Pattern> getRankedPatterns() {
		// TODO Auto-generated method stub
		return null;
	}

}
