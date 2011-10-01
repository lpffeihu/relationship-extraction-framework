package edu.columbia.cs.cg.prdualrank.pattern.extractor.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.columbia.cs.cg.document.Document;
import edu.columbia.cs.cg.pattern.Pattern;
import edu.columbia.cs.cg.prdualrank.pattern.extractor.PatternExtractor;
import edu.columbia.cs.cg.relations.Relationship;

public class ExtractionPatternExtractor extends PatternExtractor {

	private int span;

	public ExtractionPatternExtractor(int span) {
		
		this.span = span;
		
	}

	@Override
	protected Map<Pattern, Integer> extract(Document document, Relationship relationship) {
		
		
		
	}

}
