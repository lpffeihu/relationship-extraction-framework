package edu.columbia.cs.cg.prdualrank.pattern.impl;

import java.util.Arrays;
import java.util.Collection;

import edu.columbia.cs.cg.pattern.Pattern;

public class SearchPattern extends Pattern {

	private Collection<String[]> phrases;

	public SearchPattern(Collection<String[]> phrases) {
		this.phrases = phrases;
		
	}

	public boolean isValid() {
		
		for (String[] phrase1 : phrases) {
		
			if (!isSearchable(phrase1))
				return false;
			
			String stringPhrase1 = getString(phrase1);
			
			for (String[] phrase2 : phrases) {
				
				if (phrase1 != phrase2){
					
					String stringPhrase2 = getString(phrase2);

					if (stringPhrase1.contains(stringPhrase2))
						return false;
					
				}
				
			}
			
		}
		
		
		return true;
	}

	private boolean isSearchable(String[] phrase) {
		
		for (int i = 0; i < phrase.length; i++) {
			
			if (!isSearchable(phrase[i]))
				return false;
			
		}
		
		return true;
		
	}

	private boolean isSearchable(String word) {
		// TODO see if a word is searchable or not. Word at this time, can be any symbol or word.
		// Makes sense to ask for NO stopwords;
		return false;
	}

	private String getString(String[] phrase) {
		
		String ret = "";
		
		if (phrase.length != 0){
			
			for (String string : phrase) {
				
				ret = ret + " " + string;
				
			}
			
			ret = ret.substring(1);
			
		}
		
		return ret;
		
	}

}
