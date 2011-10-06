package edu.columbia.cs.cg.prdualrank.pattern.extractor.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.columbia.cs.cg.document.Document;
import edu.columbia.cs.cg.document.TokenizedDocument;
import edu.columbia.cs.cg.pattern.Pattern;
import edu.columbia.cs.cg.pattern.prdualrank.SearchPattern;
import edu.columbia.cs.cg.prdualrank.pattern.extractor.PatternExtractor;
import edu.columbia.cs.cg.prdualrank.pattern.extractor.TupleContext;
import edu.columbia.cs.cg.relations.Entity;
import edu.columbia.cs.cg.relations.Relationship;
import edu.columbia.cs.utils.MegaCartesianProduct;
import edu.columbia.cs.utils.Span;
import edu.columbia.cs.utils.Words;

public class WindowedSearchPatternExtractor<T extends Document> implements PatternExtractor<Document> {

	private int window;
	private int ngram;
	private int numberOfPhrases;

	public WindowedSearchPatternExtractor(int window, int ngram, int numberOfPhrases) {
		this.window = window;
		this.ngram = ngram;
		this.numberOfPhrases = numberOfPhrases;
	}

	@Override
	public Map<Pattern<Document,TokenizedDocument>, Integer> extractPatterns(TokenizedDocument document,
			Relationship relationship, List<Relationship> matchingRelationships) {
		
		//find the first index, find the last one and make a window around it.
		
		List<TupleContext> contexts = new ArrayList<TupleContext>();
		
		for (Relationship matchingRelationship : matchingRelationships) {

			List<Span> tupleSpans = new ArrayList<Span>();
			
			for (String role : matchingRelationship.getRoles()) {

				Entity entity = matchingRelationship.getRole(role);
				
				if (entity != Entity.NULL_ENTITY){
					//update indexes.
					
					Span auxSpan = document.getEntitySpan(entity);
					
					tupleSpans.add(auxSpan); //return the indexes whitin the array
					
				}
				
			}
			
			if (tupleSpans.size() == 0)
				continue;
			
			List<Span> realSpans = calculateRealSpans(tupleSpans);

			Collections.sort(realSpans);
			
			TupleContext tc = new TupleContext(realSpans,window);
			
			String[] first = Arrays.copyOfRange(document.getTokenizedString(), Math.max(0, realSpans.get(0).getStart() - window), realSpans.get(0).getStart());
			
			tc.addWords(first);
			
			for (int i = 0; i < realSpans.size() - 1; i++) {
				
				String[] middle = Arrays.copyOfRange(document.getTokenizedString(), realSpans.get(i).getEnd()+1, Math.max(realSpans.get(i).getEnd(), realSpans.get(i+1).getStart()));
			
				tc.addWords(middle);
				
			}
			
			String[] last = Arrays.copyOfRange(document.getTokenizedString(), Math.min(realSpans.get(realSpans.size()-1).getEnd()+1, document.getTokenizedString().length-1), Math.min(realSpans.get(realSpans.size()-1).getEnd()+window, document.getTokenizedString().length));
			
			tc.addWords(last);
			
			contexts.add(tc);
		}
		
		return generateSearchPatterns(contexts,ngram,numberOfPhrases);
		
	}

	private Map<Pattern<Document,TokenizedDocument>, Integer> generateSearchPatterns(
			List<TupleContext> contexts, int ngram, int numberOfPhrases) {
		
		Set<String[]> ngrams = new HashSet<String[]>();
		
		for (TupleContext tupleContext : contexts) {
						
			Set<String[]> nGrams = tupleContext.generateNgrams(ngram);
			
			for (String[] nGram : nGrams) {
				
				if (SearchPattern.isPatternizable(nGram)){
					
					ngrams.add(nGram);
				}
			}
			
		}
		
		Map<Pattern<Document,TokenizedDocument>,Integer> searchPatterns = new HashMap<Pattern<Document,TokenizedDocument>, Integer>();
		
		Map<Integer,Set<String[]>> ngramsMap = new HashMap<Integer, Set<String[]>>();
		
		for (int i = 1; i <= numberOfPhrases; i++) {
			
			ngramsMap.put(i, ngrams);
			
			List<Map<Integer,String[]>> patternwords = MegaCartesianProduct.generateAllPossibilities(ngramsMap);
			
			for (Map<Integer, String[]> patternsWord : patternwords) {
				
				List<String[]> words = new ArrayList<String[]>(patternsWord.values());
				
				SearchPattern<Document,TokenizedDocument> sp = new SearchPattern<Document,TokenizedDocument>(words); 
				
				if (sp.isValid()){
					
					updateMap(searchPatterns,sp);
					
				}
				
			}
						
		}
		
		return searchPatterns;
		
	}

	private void updateMap(Map<Pattern<Document,TokenizedDocument>, Integer> searchPatterns,
			SearchPattern<Document,TokenizedDocument> sp) {
		
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

		List<Span> notIntersecting = new ArrayList<Span>(tupleSpans);
		
		List<Span> intersecting = new ArrayList<Span>();
		
		for (int i=0;i<tupleSpans.size();i++) {
			
			Span span = tupleSpans.get(i);
			
			for (int j=0;j<tupleSpans.size();j++) {

				if (i==j)
					continue;
				
				Span span2 = tupleSpans.get(j);

				if (span2.intersects(span) || span.getEnd()==span2.getStart() || span2.getEnd()==span.getStart()){
					notIntersecting.remove(span2);
					if (!intersecting.contains(span2))
						intersecting.add(span2);
				}
				
			}
			
		}
		
		if (intersecting.isEmpty()){
			return tupleSpans;
		}
	
		Collections.sort(intersecting);
	
		List<Span> unifiedList = new ArrayList<Span>();

		Span unified = intersecting.get(0);

		int last = unified.getEnd();

		
		for (int i = 0; i < intersecting.size()-1; i++) {

			Span s2 = intersecting.get(i+1);
			
			if (unified.intersects(s2) || unified.getEnd()==s2.getStart() || s2.getEnd()==unified.getStart()){
				
				last = Math.max(unified.getEnd(), s2.getEnd());
				
				unified = new Span(unified.getStart(),last);
				
			} else {
				
				unifiedList.add(unified);
				
				unified = new Span(s2.getStart(),s2.getEnd());
				
				last = s2.getEnd();
				
			}
			
		}
		
		unifiedList.add(unified);
		
		notIntersecting.addAll(unifiedList);
		
		return notIntersecting;
		
	}

	public static void main(String[] args) {
		
		Words.initialize(null, null);
		
		Span t1 = new Span(5, 14);
		Span t2 = new Span(1,15);
		Span t3 = new Span(16,30);
		Span t4 = new Span(31,45);
		
		List<Span> t = new ArrayList<Span>();
		
		t.add(t1);t.add(t2);t.add(t3);t.add(t4);
		
	}
	
}
