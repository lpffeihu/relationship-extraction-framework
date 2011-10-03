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
import opennlp.tools.util.Span;

import edu.columbia.cs.cg.document.Document;
import edu.columbia.cs.cg.pattern.Pattern;
import edu.columbia.cs.cg.prdualrank.pattern.extractor.AttributeContext;
import edu.columbia.cs.cg.prdualrank.pattern.extractor.PatternExtractor;
import edu.columbia.cs.cg.relations.Entity;
import edu.columbia.cs.cg.relations.Relationship;

public class ExtractionPatternExtractor<T extends Relationship> implements PatternExtractor<Relationship> {

	private int span;

	private TokenizerME tokenizer = null;

	private int individualPatternSize;
	
	public ExtractionPatternExtractor(int span, int individualPatternSize) {
		
		this.span = span;
		this.individualPatternSize = individualPatternSize;
	}

	private TokenizerME getTokenizer() {
		
		if (tokenizer == null){
			
			InputStream modelIn;
			
			try {
				
				modelIn = new FileInputStream("en-token.bin");
				
				TokenizerModel tokModel = new TokenizerModel(modelIn);
				
				modelIn.close();
				
				tokenizer = new TokenizerME(tokModel);
			
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvalidFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
						
		}

		return tokenizer;
	}
	
	@Override
	public Map<Pattern<Relationship>, Integer> extractPatterns(Document document,
			Relationship relationship, List<Relationship> matchingRelationships) {
		// TODO Auto-generated method stub
		
		//I receive only the relationships that are interesting for me.
		//they were also loaded in such
		
		Map<Pattern<Relationship>,Integer> patterns = new HashMap<Pattern<Relationship>, Integer>();
		
		String text = document.getPlainText().get(0).getValue();
		
		Span[] spans = getTokenizer().tokenizePos(text);
		
		String[] tokenizedText = getTokenizer().tokenize(text);
		
		for (Relationship tuple : matchingRelationships) {
			
			AttributeContext context = generateAttributeContexts(tuple,spans,tokenizedText,span);			
			
			updateMap(patterns,context.generateExtractionPatterns(individualPatternSize));
			
		}
		
		return patterns;
		
	}

	private void updateMap(Map<Pattern<Relationship>, Integer> patterns,
			List<Pattern<Relationship>> extractedPatterns) {
		
		for (Pattern<Relationship> pattern : extractedPatterns) {
			
			Integer freq = patterns.get(pattern);
			
			if (freq == null){
				
				freq = 0;
				
			}
			
			patterns.put(pattern, freq + 1);
			
		}
		
	}

	private AttributeContext generateAttributeContexts(Relationship tuple,
			Span[] spans, String[] tokenizedText, int span) {
		
		AttributeContext ac = new AttributeContext();
		
		for (String role : tuple.getRoles()) {
			
			Entity entity = tuple.getRole(role);
			
			if (Entity.NULL_ENTITY != entity){
				
				Span entitySpan = getEntitySpan(entity,spans); //return the indexes whitin the array
				
				String[] before = Arrays.copyOfRange(tokenizedText, Math.max(0, entitySpan.getStart()-span), Math.max(0, entitySpan.getStart()));
				
				String[] after = Arrays.copyOfRange(tokenizedText, Math.min(entitySpan.getEnd()+1, tokenizedText.length-1), Math.min(entitySpan.getEnd()+1, tokenizedText.length));
				
				ac.addContext(entity,role,before,after);
				
			}
			
		}
		
		return ac;
		
	}

	private Span getEntitySpan(Entity entity, Span[] spans) {
		
		int firstIndex = binarySearch(spans,entity.getOffset(),0,spans.length-1);
		
		int endIndex = binarySearch(spans,entity.getOffset() + entity.getLength(), firstIndex,spans.length-1);
		
		return new Span(firstIndex,endIndex);
		
	}

	private int binarySearch(Span[] spans,int offset,int low, int high){
		
		//Though this never happens, it's good to have it (real binary search)
		if (high < low)
			return -1; // not found
		int mid = low + (high - low) / 2;
		if (spans[mid].getStart() > offset)
			return binarySearch(spans, offset, low, mid-1);
		else if (spans[mid].getEnd() < offset)
			return binarySearch(spans, offset, mid+1, high);
		else
			return mid; // found
	}

}
