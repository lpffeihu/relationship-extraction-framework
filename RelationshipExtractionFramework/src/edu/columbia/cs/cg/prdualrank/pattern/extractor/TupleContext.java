package edu.columbia.cs.cg.prdualrank.pattern.extractor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import edu.columbia.cs.utils.Span;

public class TupleContext {

	private ArrayList<String[]> words;
	private List<Span> spans;

	public TupleContext(List<Span> realSpans, int window){
		this.spans = realSpans;
		words = new ArrayList<String[]>();
	}
	
	public void addWords(String[] newWords) {
		
		words.add(newWords);
		
	}

	public Set<String[]> generateNgrams(int ngram) {
		
		Set<String[]> ngrams = new HashSet<String[]>();
		
		for (String[] segment : words) {

			for (int i = 1; i <= ngram; i++) {
				
				ngrams.addAll(generateNgrams(segment,i));
				
			}
			
			
		}
			
		return ngrams;
	}

	private Set<String[]> generateNgrams(String[] segment,
			int ngram) {
		
		Set<String[]> ngrams = new HashSet<String[]>();
		
		for (int i = 0; i < segment.length - ngram; i++) {

			ngrams.add(Arrays.copyOfRange(segment, i, i + ngram));
			
		}
		
		return ngrams;
	}

	public String toString(){
		
		String ret = "";
		
		for (String[] word : words) {
			ret = ret + " " + Arrays.toString(word);
		}
		
		return ret;
	}
	
}
