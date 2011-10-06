package edu.columbia.cs.cg.pattern.prdualrank;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import edu.columbia.cs.cg.document.Document;
import edu.columbia.cs.cg.document.TokenizedDocument;
import edu.columbia.cs.cg.pattern.Pattern;
import edu.columbia.cs.cg.relations.Entity;
import edu.columbia.cs.utils.Span;

public class SimpleAttributeExtractionPattern<E extends Entity,D extends TokenizedDocument> extends Pattern<Entity,TokenizedDocument> {

	private Set<String> entityTypes;
	private String[] wordsBefore;
	private String[] wordsAfter;
	private String role;

	public SimpleAttributeExtractionPattern(String role, Set<String> entityTypes, String[] before,
			String[] after) {
		
		this.role = role;
		this.entityTypes = entityTypes;
		this.wordsBefore = before;
		this.wordsAfter = after;
		
	}

	@Override
	public List<Entity> findMatch(TokenizedDocument d) {
		
		Collection<Entity> entities = d.getEntities();
	
		List<Entity> filtered = new ArrayList<Entity>();
		
		for (Entity entity : entities) {
			
			if (entityTypes.contains(entity.getEntityType()))
				filtered.add(entity);
			
		}
		
		List<Entity> matches = new ArrayList<Entity>();
		
		for (Entity entity : filtered) {
			
			if (matchesStructure(entity,d))
				matches.add(entity);
			
		}
		
		return matches;
	}

	private boolean matchesStructure(Entity entity, TokenizedDocument d) {
		
		Span entitySpan = d.getEntitySpan(entity);
		
		String[] tokenStrings = d.getTokenizedString();
		
		if (wordsBefore.length > entitySpan.getStart())
			return false;
		if (wordsAfter.length > tokenStrings.length - entitySpan.getEnd() - 1)
			return false;
		
		for (int i = wordsBefore.length-1; i >=0 ; i--) {
			
			if (!wordsBefore[i].equals(tokenStrings[entitySpan.getStart() - (wordsBefore.length - i)])){
				return false;
			}
			
		}
		
		for (int i = 0; i < wordsAfter.length; i++) {
			
			if (!wordsAfter[i].equals(tokenStrings[entitySpan.getEnd() + i + 1])){
				return false;
			}	
		
		}

		return true;
	}

	@Override
	protected int generateHashCode() {
		
		int hashCode = 1;
		
		hashCode = 31*hashCode + role.hashCode();
		
		hashCode = 31*hashCode + entityTypes.hashCode();
	
		hashCode = 31*hashCode + Arrays.hashCode(wordsBefore);
		
		hashCode = 31*hashCode + Arrays.hashCode(wordsAfter);
		
		return hashCode;
	}


	@Override
	protected String generateToString() {
		return Arrays.toString(wordsBefore) + entityTypes.toString() + " as " + role + Arrays.toString(wordsAfter);
		
	}
	
	@Override
	public boolean equals(Object obj) {
		
		SimpleAttributeExtractionPattern<Entity, TokenizedDocument> other = (SimpleAttributeExtractionPattern<Entity, TokenizedDocument>)obj;
		
		if (!role.equals(other.role)){
			return false;
		}
		
		if (!entityTypes.containsAll(other.entityTypes)){
			return false;
		}
		
		if (wordsAfter.length != other.wordsAfter.length){
			return false;
		}
		
		if (wordsBefore.length != other.wordsBefore.length){
			return false;
		}

		if (!Arrays.equals(wordsAfter, other.wordsAfter)){
			return false;
		}
		
		if (!Arrays.equals(wordsBefore, other.wordsBefore)){
			return false;
		}
		
		return true;
	}
}
