/**
 * This class represents the text surrounding a tuple. Is used in the generation of search Patterns using Window Generation.
 *
 * @author      Pablo Barrio
 * @author		Goncalo Simoes
 * @version     0.1
 * @since       2011-10-07
 */
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

	/**
	 * Instantiates a new tuple context.
	 *
	 * @param realSpans the non overlapping text segments of a tuple. Overlapping segments were combined in a previous step.
	 * @param window the size of the window used to generate the context.
	 */
	public TupleContext(List<Span> realSpans, int window){
		this.spans = realSpans;
		words = new ArrayList<String[]>();
	}
	
	/**
	 * Adds a new sequence of words to the context. Notice that this text might appear before, in between or after the attributes of a tuple.
	 *
	 * @param newWords the new detected sequence of words between attributes.
	 */
	public void addWords(String[] newWords) {
		
		words.add(newWords);
		
	}

	/**
	 * Generate ngrams of size lower or equal to 'ngram' based on the sequences of words.
	 *
	 * @param ngram the maximum size of ngrams.
	 * @return the set of ngrams extracted from the words surrounding the tuple.
	 */
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

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString(){
		
		String ret = "";
		
		for (String[] word : words) {
			ret = ret + " " + Arrays.toString(word);
		}
		
		return ret;
	}
	
}
