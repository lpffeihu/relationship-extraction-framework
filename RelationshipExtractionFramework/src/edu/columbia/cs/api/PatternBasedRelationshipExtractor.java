package edu.columbia.cs.api;

import java.util.List;
import java.util.Set;

import edu.columbia.cs.cg.document.Document;
import edu.columbia.cs.cg.pattern.Pattern;
import edu.columbia.cs.cg.relations.Relationship;

public class PatternBasedRelationshipExtractor implements RelationshipExtractor {

	private Set<Pattern> extractionPatterns;

	public PatternBasedRelationshipExtractor(Set<Pattern> extractionPatterns){
		this.extractionPatterns = extractionPatterns;
	}
	
	@Override
	public List<Relationship> extractTuples(Document d) {
		// TODO Auto-generated method stub
		return null;
	}

	
	
}
