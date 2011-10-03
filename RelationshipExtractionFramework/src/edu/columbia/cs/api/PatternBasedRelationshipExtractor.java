package edu.columbia.cs.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import edu.columbia.cs.cg.document.Document;
import edu.columbia.cs.cg.document.TokenizedDocument;
import edu.columbia.cs.cg.pattern.Pattern;
import edu.columbia.cs.cg.prdualrank.pattern.impl.ExtractionPattern;
import edu.columbia.cs.cg.relations.Relationship;

public class PatternBasedRelationshipExtractor<T extends Relationship, D extends TokenizedDocument> implements RelationshipExtractor<TokenizedDocument> {

	private Set<Pattern<Relationship,TokenizedDocument>> patterns;

	public PatternBasedRelationshipExtractor(Set<Pattern<Relationship,TokenizedDocument>> patterns){
		this.patterns = patterns;
		
	}
	
	@Override
	public List<Relationship> extractTuples(TokenizedDocument d) {
		
		List<Relationship> rel = new ArrayList<Relationship>();

		for (Pattern<Relationship,TokenizedDocument> pattern : patterns) {
			
			rel.addAll(pattern.findMatch(d));
			
		}
		
		return rel;
		
	}

	
	
}
