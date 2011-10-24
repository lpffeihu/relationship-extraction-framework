package edu.columbia.cs.cg.prdualrank.pattern.extractor.impl;

import java.util.List;
import java.util.Map;

import edu.columbia.cs.cg.prdualrank.pattern.extractor.PatternExtractor;
import edu.columbia.cs.ref.model.Document;
import edu.columbia.cs.ref.model.TokenizedDocument;
import edu.columbia.cs.ref.model.pattern.Pattern;
import edu.columbia.cs.ref.model.relationship.Relationship;

public class DocumentSearchPatternExtractor<T extends Document> implements PatternExtractor<Document> {

	@Override
	public Map<Pattern<Document, TokenizedDocument>, Integer> extractPatterns(
			TokenizedDocument document, Relationship relationship,
			List<Relationship> matchingRelationships) {
		// TODO Auto-generated method stub
		return null;
	}

}
