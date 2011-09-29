package edu.columbia.cs.cg.prdualrank.pattern.extractor.impl;

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

}
