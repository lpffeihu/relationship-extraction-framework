package edu.columbia.cs.cg.prdualrank.pattern.extractor;

import java.util.List;
import java.util.Map;

import edu.columbia.cs.cg.document.Document;
import edu.columbia.cs.cg.document.TokenizedDocument;
import edu.columbia.cs.cg.pattern.Pattern;
import edu.columbia.cs.cg.pattern.matchable.Matchable;
import edu.columbia.cs.cg.relations.Relationship;

public interface PatternExtractor<T extends Matchable> {

	public Map<Pattern<T,TokenizedDocument>,Integer> extractPatterns(TokenizedDocument document, Relationship relationship, List<Relationship> matchingRelationships);
			
}
