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
import edu.columbia.cs.cg.prdualrank.pattern.impl.ExtractionPattern;
import edu.columbia.cs.cg.prdualrank.pattern.impl.SimpleAttributeExtractionPattern;
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

	public AttributeContext(RelationshipType relationshipType){

		this.rType = relationshipType;
		entities = new HashMap<String,Entity>();
		roles = new ArrayList<String>();
		previousWords = new HashMap<String,String[]>();
		postWords = new HashMap<String,String[]>();
		
	}
	
	public void addContext(Entity entity, String role, String[] before,
			String[] after) {
		
		roles.add(role);
		entities.put(role,entity);
		
		previousWords.put(role,before);
		postWords.put(role,after);
		
	}

	public List<Pattern<Relationship,TokenizedDocument>> generateExtractionPatterns(int size) {
		
		Map<String,Set<SimpleAttributeExtractionPattern<Entity, TokenizedDocument>>> patterns = new HashMap<String, Set<SimpleAttributeExtractionPattern<Entity, TokenizedDocument>>>();
		
		for (String role : roles) {
			
			patterns.put(role,generateCombinations(role,size,previousWords.get(role),postWords.get(role)));
			
		}
		
		List<Map<String, SimpleAttributeExtractionPattern<Entity, TokenizedDocument>>> combinations = MegaCartesianProduct.generateAllPossibilities(patterns);
		
		List<Pattern<Relationship,TokenizedDocument>> extractionPatterns = new ArrayList<Pattern<Relationship,TokenizedDocument>>();
		
		for (Map<String, SimpleAttributeExtractionPattern<Entity, TokenizedDocument>> map : combinations) {
			
			extractionPatterns.add(new ExtractionPattern<Relationship,TokenizedDocument>(roles,map,rType));
			
		}
		
		return extractionPatterns;
		
	}

	private Set<SimpleAttributeExtractionPattern<Entity, TokenizedDocument>> generateCombinations(String role, int size,
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
				
				patterns.add(new SimpleAttributeExtractionPattern<Entity,TokenizedDocument>(role,before,after));
				
			}
		}

		return patterns;
		
	}

}
