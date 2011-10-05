package edu.columbia.cs.cg.pattern.prdualrank;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import edu.columbia.cs.cg.document.Document;
import edu.columbia.cs.cg.document.TokenizedDocument;
import edu.columbia.cs.cg.pattern.Pattern;

public class SearchPattern<T extends Document, D extends TokenizedDocument> extends Pattern<Document,TokenizedDocument> {

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

	@Override
	public List<Document> findMatch(TokenizedDocument d) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected int generateHashCode() {
		
		int hashCode = 1;
		
		for (String[] phrase : phrases) {
			hashCode = 31*hashCode + Arrays.hashCode(phrase);
		}
		
		return hashCode;
		
	}
	
	public String toString() {
		return phrases.toString();
	}

}
