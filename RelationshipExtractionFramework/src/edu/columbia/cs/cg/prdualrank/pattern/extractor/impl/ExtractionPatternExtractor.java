package edu.columbia.cs.cg.prdualrank.pattern.extractor.impl;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.util.InvalidFormatException;

import edu.columbia.cs.cg.document.Document;
import edu.columbia.cs.cg.document.TokenizedDocument;
import edu.columbia.cs.cg.pattern.Pattern;
import edu.columbia.cs.cg.prdualrank.pattern.extractor.AttributeContext;
import edu.columbia.cs.cg.prdualrank.pattern.extractor.PatternExtractor;
import edu.columbia.cs.cg.relations.Entity;
import edu.columbia.cs.cg.relations.Relationship;
import edu.columbia.cs.cg.relations.RelationshipType;
import edu.columbia.cs.utils.Span;

public class ExtractionPatternExtractor<T extends Relationship> implements PatternExtractor<Relationship> {

	private int individualPatternSize;
	private RelationshipType rType;
	private int span;
	
	public ExtractionPatternExtractor(int span, int individualPatternSize, RelationshipType rType) {
		this.span = span;
		this.rType = rType;
		this.individualPatternSize = individualPatternSize;
	}

	
	@Override
	public Map<Pattern<Relationship,TokenizedDocument>, Integer> extractPatterns(TokenizedDocument document,
			Relationship relationship, List<Relationship> matchingRelationships) {
		// TODO Auto-generated method stub
		
		//I receive only the relationships that are interesting for me.
		//they were also loaded in such
		
		Map<Pattern<Relationship,TokenizedDocument>,Integer> patterns = new HashMap<Pattern<Relationship,TokenizedDocument>, Integer>();
		
		for (Relationship tuple : matchingRelationships) {
			
			AttributeContext context = generateAttributeContexts(tuple,document);			
			
			updateMap(patterns,context.generateExtractionPatterns(individualPatternSize));
			
		}
		
		System.out.println(patterns.toString());
		
		return patterns;
		
	}

	private void updateMap(Map<Pattern<Relationship,TokenizedDocument>, Integer> patterns,
			List<Pattern<Relationship,TokenizedDocument>> extractedPatterns) {
		
		for (Pattern<Relationship,TokenizedDocument> pattern : extractedPatterns) {
			
			Integer freq = patterns.get(pattern);
			
			if (freq == null){
				
				freq = 0;
				
			}
			
			patterns.put(pattern, freq + 1);
			
		}
		
	}

	private AttributeContext generateAttributeContexts(Relationship tuple,
			TokenizedDocument document) {
		
		AttributeContext ac = new AttributeContext(rType);
		
		for (String role : tuple.getRoles()) {
			
			Entity entity = tuple.getRole(role);
			
			if (Entity.NULL_ENTITY != entity){
				
				Span entitySpan = document.getEntitySpan(entity); //return the indexes whitin the array
				
				String[] before = Arrays.copyOfRange(document.getTokenizedString(), Math.max(0, entitySpan.getStart()-span), Math.max(0, entitySpan.getStart()));
				
				String[] after = Arrays.copyOfRange(document.getTokenizedString(), Math.min(entitySpan.getEnd()+1, document.getTokenizedString().length-1), Math.min(entitySpan.getEnd()+1, document.getTokenizedString().length));
				
				ac.addContext(entity,role,before,after);
				
			}
			
		}
		
		return ac;
		
	}

}
