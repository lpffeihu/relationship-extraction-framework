package edu.columbia.cs.cg.prdualrank.pattern.impl;

public class SimpleAttributeExtractionPattern extends ExtractionPattern {

	private String role;
	private String[] wordsBefore;
	private String[] wordsAfter;

	public SimpleAttributeExtractionPattern(String role, String[] before,
			String[] after) {
		
		this.role = role;
		this.wordsBefore = before;
		this.wordsAfter = after;
		
	}

}
