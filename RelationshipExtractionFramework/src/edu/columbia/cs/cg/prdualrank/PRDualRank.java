package edu.columbia.cs.cg.prdualrank;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import edu.columbia.cs.api.PatternBasedRelationshipExtractor;
import edu.columbia.cs.cg.document.Document;
import edu.columbia.cs.cg.pattern.Pattern;
import edu.columbia.cs.cg.prdualrank.pattern.extractor.PatternExtractor;
import edu.columbia.cs.cg.prdualrank.pattern.extractor.impl.ExtractionPatternExtractor;
import edu.columbia.cs.cg.prdualrank.pattern.extractor.impl.SearchPatternExtractor;
import edu.columbia.cs.cg.prdualrank.searchengine.QueryGenerator;
import edu.columbia.cs.cg.prdualrank.searchengine.SearchEngine;
import edu.columbia.cs.cg.relations.Entity;
import edu.columbia.cs.cg.relations.Relationship;
import edu.columbia.cs.engine.Engine;
import edu.columbia.cs.model.Model;
import edu.columbia.cs.og.structure.OperableStructure;
import edu.columbia.cs.og.structure.impl.RelationOperableStructure;

public class PRDualRank implements Engine{

	private SearchEngine se;
	private QueryGenerator qg;
	private int k_seed;
	private int span;
	private int ngram;
	private int window;
	private int searchdepth;
	private int minsupport;
	private int k_nolabel;

	public PRDualRank(SearchEngine se, QueryGenerator qg, int k_seed, int span, int ngram, int window, int searchdepth, int minsupport, int k_nolabel){
		this.se = se;
		this.qg = qg;
		this.k_seed = k_seed;
		this.span = span;
		this.ngram = ngram;
		this.window = window;
		this.searchdepth = searchdepth;
		this.minsupport = minsupport;
		this.k_nolabel = k_nolabel;
	}
	
	@Override
	public Model train(List<OperableStructure> list) {
		
		PatternExtractor spe = new SearchPatternExtractor(window, ngram, searchdepth);
		
		PatternExtractor epe = new ExtractionPatternExtractor(span);
		
		HashMap<Pattern, Integer> Ps = new HashMap<Pattern, Integer>();
		
		HashMap<Pattern, Integer> Pe = new HashMap<Pattern, Integer>();
		
		Set<Relationship> seeds = new HashSet<Relationship>();
		
		Set<Relationship> initial = new HashSet<Relationship>();
		
		for (OperableStructure operableStructure : list) {
			
			seeds.add(((RelationOperableStructure)operableStructure).getRelation());
			
			initial.add(((RelationOperableStructure)operableStructure).getRelation());
			
		}
		
		for (Relationship relationship : seeds) {
			
			List<Document> documents = se.search(qg.generateQuery(relationship), k_seed);

			updateMap(Ps,spe.extractPatterns(documents));
			
			updateMap(Pe,epe.extractPatterns(documents));
			
		}
		
		Set<Pattern> searchPatterns = filter(Ps,minsupport);
		
		Set<Pattern> extractPatterns = filter(Pe,minsupport);
		
		PatternBasedRelationshipExtractor pbre = new PatternBasedRelationshipExtractor(extractPatterns);
		
		HashMap<Relationship,Integer> extractedTuples = new HashMap<Relationship,Integer>();
		
		for (Relationship relationship : seeds) {
			
			for (String role : relationship.getRoles()) {
				
				List<Document> documents = se.search(qg.generateQuery(relationship.getRole(role)), k_seed);
				
				for (Document document : documents) {
					
					updateMap(extractedTuples,filterByRole(role,relationship.getRole(role),pbre.extractTuples(document)));
					
				}
								
			}
			
		}
		
		Set<Relationship> topTuples = filterTopK(extractedTuples,k_nolabel,initial);
		
		List<Document> document = new ArrayList<Document>();
		
		for (Relationship relationship : topTuples) {
			
			document.addAll(se.search(qg.generateQuery(relationship), k_seed));
			
		}
		
		return null;
		
	}

	private class ValueComparator<T> implements Comparator<T>{

		private Map<T, Integer> frequencymap;

		private ValueComparator(Map<T,Integer> frequencymap){
			this.frequencymap = frequencymap;
		}
		
		@Override
		public int compare(T obj1, T obj2) {
			
			return frequencymap.get(obj2).compareTo(frequencymap.get(obj1));
			
		}
		
	}
	
	private <T> Set<T> filterTopK(
			Map<T, Integer> toSelect, int k, Set<T> initial) {
		
		int realLimit = k + initial.size();
		
		SortedMap<T,Integer> sorted = new TreeMap<T, Integer>(new ValueComparator<T>(toSelect));
		
		for (T element : toSelect.keySet()) {
			
			sorted.put(element, toSelect.get(element));
			
		}
		
		for (T element : sorted.keySet()) {
			
			initial.add(element);
			
			if (initial.size() == realLimit)
				break;
		}
		
		return initial;
	}

	private Map<Relationship,Integer> filterByRole(String role,
			Entity value, List<Relationship> extractTuples) {
		
		//TODO I have to use the matcher...
		
		Map<Relationship, Integer> ret = new HashMap<Relationship, Integer>();
		
		for (Relationship relationship : extractTuples) {
			
			if (relationship.getRole(role).equals(value)){
				
				Integer freq = ret.get(relationship);
				
				if (freq == null){
					freq = 0;
				}
				
				ret.put(relationship, freq+1);
			}
			
		}
		
		return ret;
	}

	private <T> Set<T> filter(Map<T, Integer> toFilter, int minsupport) {
		
		Set<T> ret = new HashSet<T>();
		
		for (T pattern : toFilter.keySet()) {

			Integer freq = toFilter.get(pattern);
			
			if (freq >= minsupport){
				ret.add(pattern);
			}
			
		}

		return ret;
		
	}

	private <T> void updateMap(Map<T, Integer> acc,
			Map<T, Integer> actual) {
		
		for (T pattern : actual.keySet()) {
			
			Integer freq = acc.get(pattern);
			
			if (freq == null){
				
				freq = 0;
				
			}
			
			acc.put(pattern, freq + actual.get(pattern));
			
		}
		
	}
	
}
