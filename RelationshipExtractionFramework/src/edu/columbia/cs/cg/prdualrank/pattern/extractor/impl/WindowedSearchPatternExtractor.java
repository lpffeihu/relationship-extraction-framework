package edu.columbia.cs.cg.prdualrank.pattern.extractor.impl;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.util.InvalidFormatException;
import opennlp.tools.util.Span;

import edu.columbia.cs.cg.document.Document;
import edu.columbia.cs.cg.pattern.Pattern;
import edu.columbia.cs.cg.prdualrank.pattern.extractor.PatternExtractor;
import edu.columbia.cs.cg.prdualrank.pattern.extractor.TupleContext;
import edu.columbia.cs.cg.prdualrank.pattern.impl.SearchPattern;
import edu.columbia.cs.cg.relations.Entity;
import edu.columbia.cs.cg.relations.Relationship;
import edu.columbia.cs.utils.MegaCartesianProduct;
import edu.columbia.cs.utils.Pair;

public class WindowedSearchPatternExtractor<T extends Document> implements PatternExtractor<Document> {

	private int window;
	private int ngram;
	private TokenizerME tokenizer;
	private int numberOfPhrases;

	public WindowedSearchPatternExtractor(int window, int ngram, int numberOfPhrases) {
		this.window = window;
		this.ngram = ngram;
		this.numberOfPhrases = numberOfPhrases;
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
	public Map<Pattern<Document>, Integer> extractPatterns(Document document,
			Relationship relationship, List<Relationship> matchingRelationships) {
		
		//find the first index, find the last one and make a window around it.
		
		String text = document.getPlainText().get(0).getValue();
		
		Span[] spans = getTokenizer().tokenizePos(text);
		
		String[] tokenizedText = getTokenizer().tokenize(text);
		
		List<TupleContext> contexts = new ArrayList<TupleContext>();
		
		for (Relationship matchingRelationship : matchingRelationships) {

			List<Span> tupleSpans = new ArrayList<Span>();
			
			for (String role : matchingRelationship.getRoles()) {

				Entity entity = matchingRelationship.getRole(role);
				
				if (entity != Entity.NULL_ENTITY){
					//update indexes.
					
					Span auxSpan = getEntitySpan(entity,spans);
					
					tupleSpans.add(auxSpan); //return the indexes whitin the array
					
				}
				
			}
			
			if (tupleSpans.size() == 0)
				continue;
			
			List<Span> realSpans = calculateRealSpans(tupleSpans);

			Collections.sort(realSpans);
			
			TupleContext tc = new TupleContext(realSpans,window);
			
			String[] first = Arrays.copyOfRange(tokenizedText, Math.max(0, realSpans.get(0).getStart() - window), realSpans.get(0).getStart());
			
			tc.addWords(first);
			
			for (int i = 0; i < realSpans.size() - 1; i++) {
				
				String[] middle = Arrays.copyOfRange(tokenizedText, realSpans.get(i).getEnd()+1, Math.max(realSpans.get(i).getEnd()+1, realSpans.get(i+1).getStart()));
			
				tc.addWords(middle);
				
			}
			
			String[] last = Arrays.copyOfRange(tokenizedText, Math.min(realSpans.get(realSpans.size()-1).getEnd()+1, tokenizedText.length-1), Math.min(realSpans.get(realSpans.size()-1).getEnd()+window, tokenizedText.length));
			
			tc.addWords(last);
			
			contexts.add(tc);
		}
		
		return generateSearchPatterns(contexts,ngram,numberOfPhrases);
		
	}

	private Map<Pattern<Document>, Integer> generateSearchPatterns(
			List<TupleContext> contexts, int ngram, int numberOfPhrases) {
		
		Set<String[]> ngrams = new HashSet<String[]>();
		
		for (TupleContext tupleContext : contexts) {
			
			ngrams.addAll(tupleContext.generateNgrams(ngram));
			
		}
		
		Map<Pattern<Document>,Integer> searchPatterns = new HashMap<Pattern<Document>, Integer>();
		
		Map<Integer,Set<String[]>> ngramsMap = new HashMap<Integer, Set<String[]>>();
		
		for (int i = 1; i <= numberOfPhrases; i++) {
			
			ngramsMap.put(i, ngrams);
			
			List<Map<Integer,String[]>> patternwords = MegaCartesianProduct.generateAllPossibilities(ngramsMap);
			
			for (Map<Integer, String[]> patternsWord : patternwords) {
				
				SearchPattern<Document> sp = new SearchPattern<Document>(patternsWord.values()); 
				
				if (sp.isValid()){
					
					updateMap(searchPatterns,sp);
					
				}
				
			}
						
		}
		
		return searchPatterns;
		
	}

	private void updateMap(Map<Pattern<Document>, Integer> searchPatterns,
			SearchPattern<Document> sp) {
		
		Integer freq = searchPatterns.get(sp);
		
		if (freq == null){
			freq = 0;
		}
		
		searchPatterns.put(sp, freq + 1);
		
	}

	private List<Span> calculateRealSpans(List<Span> tupleSpans) {
		// TODO returns the not overlapping spans ... use instersects.
	
		if (tupleSpans.size()<=1)
			return tupleSpans;
		
		List<Span> notIntersecting = new ArrayList<Span>();
		List<Span> intersecting = new ArrayList<Span>();
		
		for (Span span : tupleSpans) {
			
			boolean intersects = false;
			
			for (Span span2 : notIntersecting) {
			
				if (span2.intersects(span)){
					intersects = true;
					break;
				}
				
			}
			
			if (!intersects){
				notIntersecting.add(span);
			}else{
				intersecting.add(span);
			}
			
		}
		
		if (intersecting.isEmpty()){
			return tupleSpans;
		}
	
		Collections.sort(intersecting);
		
		int first = intersecting.get(0).getStart();
		
		int last = intersecting.get(0).getEnd();
		
		List<Span> unified = new ArrayList<Span>();
		
		for (int i = 0; i < intersecting.size()-1; i++) {
			
			for (int j = i+1; j < intersecting.size(); j++) {
				
				Span s1 = intersecting.get(i);
				Span s2 = intersecting.get(j);
				
				if (s1.intersects(s2)){
					
					last = Math.max(s1.getEnd(), s2.getEnd());
					
				} else {
					
					unified.add(new Span(first,last));
					
					first = s2.getStart();
					
					last = s2.getEnd();
					
				}
				
			}
			
		}
		
		unified.add(new Span(first,last));
		
		notIntersecting.addAll(unified);
		
		return notIntersecting;
		
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
