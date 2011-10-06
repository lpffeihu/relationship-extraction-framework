package edu.columbia.cs.cg.prdualrank.pattern.extractor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.columbia.cs.cg.document.Document;
import edu.columbia.cs.cg.document.TokenizedDocument;
import edu.columbia.cs.cg.pattern.Pattern;
import edu.columbia.cs.cg.pattern.prdualrank.ExtractionPattern;
import edu.columbia.cs.cg.pattern.prdualrank.SimpleAttributeExtractionPattern;
import edu.columbia.cs.cg.relations.Entity;
import edu.columbia.cs.cg.relations.Relationship;
import edu.columbia.cs.cg.relations.RelationshipType;
import edu.columbia.cs.utils.MegaCartesianProduct;

public class AttributeContext {

	private HashMap<String, Entity> entities;
	private List<String> roles;
	private HashMap<String, String[]> previousWords;
	private HashMap<String, String[]> postWords;
	private RelationshipType rType;
	private int hashCode = -1;
	private HashMap<String, Set<String>> entityTypes;

	public AttributeContext(RelationshipType relationshipType){

		this.rType = relationshipType;
		entities = new HashMap<String,Entity>();
		roles = new ArrayList<String>();
		previousWords = new HashMap<String,String[]>();
		postWords = new HashMap<String,String[]>();
		entityTypes = new HashMap<String, Set<String>>();
		
	}
	
	public void addContext(Entity entity, String role, String[] before,
			String[] after) {
		
		updateTypes(role,entity.getEntityType());
		roles.add(role);
		entities.put(role,entity);
		previousWords.put(role,before);
		postWords.put(role,after);
		
	}

	private void updateTypes(String role, String entityType) {
		
		Set<String> types = entityTypes.get(role);
		
		if (types == null){
			types = new HashSet<String>();
		}
		
		types.add(entityType);
		
		entityTypes.put(role,types);

		
	}

	public List<Pattern<Relationship,TokenizedDocument>> generateExtractionPatterns(int size) {
		
		Map<String,Set<SimpleAttributeExtractionPattern<Entity, TokenizedDocument>>> patterns = new HashMap<String, Set<SimpleAttributeExtractionPattern<Entity, TokenizedDocument>>>();
		
		for (String role : roles) {
			
			patterns.put(role,generateCombinations(role,entityTypes.get(role),size,previousWords.get(role),postWords.get(role)));
			
		}
			
		List<Map<String, SimpleAttributeExtractionPattern<Entity, TokenizedDocument>>> combinations = MegaCartesianProduct.generateAllPossibilities(patterns);
		
		List<Pattern<Relationship,TokenizedDocument>> extractionPatterns = new ArrayList<Pattern<Relationship,TokenizedDocument>>();
		
		for (Map<String, SimpleAttributeExtractionPattern<Entity, TokenizedDocument>> map : combinations) {
			
			extractionPatterns.add(new ExtractionPattern<Relationship,TokenizedDocument>(roles,map,rType));
			
		}
		
		return extractionPatterns;
		
	}

	private Set<SimpleAttributeExtractionPattern<Entity, TokenizedDocument>> generateCombinations(String role, Set<String> entityTypes, int size,
			String[] wordsBefore, String[] wordsAfter) {
		
		Set<SimpleAttributeExtractionPattern<Entity, TokenizedDocument>> patterns = new HashSet<SimpleAttributeExtractionPattern<Entity,TokenizedDocument>>();

		for (int beforeSize = 0; beforeSize <= wordsBefore.length; beforeSize++) {
			for (int afterSize = 0; afterSize <= wordsAfter.length; afterSize++) {
				
				if (beforeSize == 0 && afterSize == 0)
					continue;
				
				if (beforeSize + afterSize > size)
					continue;
				
				String[] before = new String[0];
				
				if (beforeSize > 0){
					before = Arrays.copyOfRange(wordsBefore,wordsBefore.length-beforeSize,wordsBefore.length);
				}
				
				String[] after = new String[0];
				
				if (afterSize > 0){
					after = Arrays.copyOfRange(wordsAfter, 0, afterSize);
				}
				
				patterns.add(new SimpleAttributeExtractionPattern<Entity,TokenizedDocument>(role,entityTypes,before,after));
				
			}
		}

		return patterns;
		
	}

	@Override
	public String toString() {
		
		return previousWords.toString() + " - " + entities.toString() + " - "+ postWords.toString();
		
	}
	
	@Override
	public int hashCode() {
		
		if (hashCode  == -1){
			
			hashCode = generateHashCode();
			
		}
		return hashCode;
		
	}

	private int generateHashCode() {
		
		int hash = 1;
		
		hash = 31*hash + entities.hashCode();
		
		hash = 31*hash + roles.hashCode();
		
		hash = 31*hash + previousWords.hashCode();
		
		hash = 31*hash + postWords.hashCode();
		
		return hash;
	}
}
