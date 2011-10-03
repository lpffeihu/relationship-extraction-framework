package edu.columbia.cs.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import edu.columbia.cs.cg.document.Document;
import edu.columbia.cs.cg.pattern.Pattern;
import edu.columbia.cs.cg.prdualrank.pattern.impl.ExtractionPattern;
import edu.columbia.cs.cg.relations.Relationship;

public class PatternBasedRelationshipExtractor<T extends Relationship> implements RelationshipExtractor {

	private Set<Pattern<Relationship>> patterns;

	public PatternBasedRelationshipExtractor(Set<Pattern<Relationship>> patterns){
		this.patterns = patterns;
		
	}
	
	@Override
	public List<Relationship> extractTuples(Document d) {
		
		List<Relationship> rel = new ArrayList<Relationship>();

		for (Pattern<Relationship> pattern : patterns) {
			
			rel.addAll(pattern.findMatch(d));
			
		}
		
		return rel;
		
	}

	
	
}
