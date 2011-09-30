package edu.columbia.cs.cg.prdualrank.pattern.extractor.impl;

import java.util.Map;

import edu.columbia.cs.cg.document.Document;
import edu.columbia.cs.cg.pattern.Pattern;
import edu.columbia.cs.cg.prdualrank.pattern.extractor.PatternExtractor;

public class SearchPatternExtractor extends PatternExtractor {

	private int window;
	private int ngram;
	private int searchdepth;

	public SearchPatternExtractor(int window, int ngram, int searchdepth) {
		this.window = window;
		this.ngram = ngram;
		this.searchdepth = searchdepth;
	}

	@Override
	protected Map<Pattern, Integer> extract(Document document) {
		// TODO Auto-generated method stub
		return null;
	}

}
