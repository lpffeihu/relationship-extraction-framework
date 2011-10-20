/**
 * This class represents the text surrounding a tuple. Is used in the generation of <b>Search Patterns<\b> using Window Generation. The size of the windows, define the size of the context.
 * 
 * <br>
 * For instance, given a span between entities larger than 9, a windows size of 10 and a sentence (from <a href="http://www.google.com/press/pressrel/google_youtube.html">Google.com</a>): "When the acquisition is complete, YouTube will retain its distinct brand identity, 
 * strengthening and complementing Google’s own fast-growing video business. 
 * YouTube will continue to be based in San Bruno, CA, and all YouTube employees will remain with the company. With Google’s technology, advertiser relationships and global reach, 
 * YouTube will continue to build on its success as one of the world’s most popular services for video entertainment." 
 * 
 * <br>One of the <b>Tuple Context</b> (Considering the first two occurrences of <b>Google</b> and <b>YouTube</b>):
 * 
 * <br>1. ["When","the","acquisition","is","complete"] COMPANY ["will","retain","its","distinct","brand","identity","strengthening","and","complementing"] BUYER ["'s","own","fast-growing","video","business","YouTube","will","continue","to","be"]
* 
 *
 * <br>
 * This class is used for our implementation of: 
 * <b> "Searching Patterns for Relation Extraction over the Web: Rediscovering the Pattern-Relation Duality" </b>. Y. Fang and K. C.-C. Chang. In WSDM, pages 825-834, 2011.
 * 
 * <br>
 * For further information, 
 * 
 * @see <a href="http://www.wsdm2011.org/"> WSDM 2011 Conference Website </a>
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
