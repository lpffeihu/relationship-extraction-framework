package edu.columbia.cs.cg.prdualrank;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

	public PRDualRank(SearchEngine se, QueryGenerator qg, int k_seed, int span, int ngram, int window, int searchdepth, int minsupport){
		this.se = se;
		this.qg = qg;
		this.k_seed = k_seed;
		this.span = span;
		this.ngram = ngram;
		this.window = window;
		this.searchdepth = searchdepth;
		this.minsupport = minsupport;
	}
	
	@Override
	public Model train(List<OperableStructure> list) {
		
		PatternExtractor spe = new SearchPatternExtractor(window, ngram, searchdepth);
		
		PatternExtractor epe = new ExtractionPatternExtractor(span);
		
		HashMap<Pattern, Integer> Ps = new HashMap<Pattern, Integer>();
		
		HashMap<Pattern, Integer> Pe = new HashMap<Pattern, Integer>();
		
		List<Relationship> seeds = new ArrayList<Relationship>();
		
		for (OperableStructure operableStructure : list) {
			
			seeds.add(((RelationOperableStructure)operableStructure).getRelation());
			
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
					
					updateExtractedTuples(extractedTuples,filterByRole(role,relationship.getRole(role),pbre.extractTuples(document)));
					
				}
								
			}
			
		}
		return null;
		
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

	private Set<Pattern> filter(HashMap<Pattern, Integer> patterns, int minsupport) {
		
		Set<Pattern> filteredPatterns = new HashSet<Pattern>();
		
		for (Pattern pattern : patterns.keySet()) {

			Integer freq = patterns.get(pattern);
			
			if (freq >= minsupport){
				filteredPatterns.add(pattern);
			}
			
		}

		return filteredPatterns;
		
	}

	private void updateMap(HashMap<Pattern, Integer> accPatterns,
			HashMap<Pattern, Integer> extractedPatterns) {
		
		for (Pattern pattern : extractedPatterns.keySet()) {
			
			Integer freq = accPatterns.get(pattern);
			
			if (freq == null){
				
				freq = 0;
				
			}
			
			accPatterns.put(pattern, freq + extractedPatterns.get(pattern));
			
		}
		
	}
	
}
