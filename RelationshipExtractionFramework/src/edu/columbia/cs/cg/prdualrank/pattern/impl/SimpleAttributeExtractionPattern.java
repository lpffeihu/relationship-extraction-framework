package edu.columbia.cs.cg.prdualrank.pattern.impl;

import java.util.List;

import edu.columbia.cs.cg.document.Document;
import edu.columbia.cs.cg.pattern.Pattern;
import edu.columbia.cs.cg.relations.Entity;

public class SimpleAttributeExtractionPattern<E extends Entity> extends Pattern<Entity> {

	private String entityType;
	private String[] wordsBefore;
	private String[] wordsAfter;

	public SimpleAttributeExtractionPattern(String entityType, String[] before,
			String[] after) {
		
		this.entityType = entityType;
		this.wordsBefore = before;
		this.wordsAfter = after;
		
	}

	@Override
	public List<Entity> findMatch(Document d) {
		// TODO Auto-generated method stub
		
		return null;
	}

}
