package edu.columbia.cs.cg.prdualrank.pattern.extractor;

import java.util.List;
import java.util.Map;

import edu.columbia.cs.cg.document.Document;
import edu.columbia.cs.cg.pattern.Pattern;
import edu.columbia.cs.cg.relations.Relationship;

public interface PatternExtractor<T> {

	public Map<Pattern<T>,Integer> extractPatterns(Document document, Relationship relationship, List<Relationship> matchingRelationships);
			
}
