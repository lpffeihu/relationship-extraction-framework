package edu.columbia.cs.cg.pattern.prdualrank;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import edu.columbia.cs.cg.document.Document;
import edu.columbia.cs.cg.document.TokenizedDocument;
import edu.columbia.cs.cg.pattern.Pattern;
import edu.columbia.cs.utils.Words;

public class SearchPattern<T extends Document, D extends TokenizedDocument> extends Pattern<Document,TokenizedDocument> {

	private List<String[]> phrases;

	public SearchPattern(List<String[]> phrases) {
		this.phrases = phrases;
		
	}

	public boolean isValid() {
		
		for (String[] phrase : phrases) {
		
			if (!isSearchable(phrase))
				return false;
			
		}
		
		for (int i = 0;i < phrases.size()-1;i++) {
		
			String stringPhrase1 = getString(phrases.get(i));

			for (int j=i+1; j<phrases.size();j++) {
				
				if (Arrays.equals(phrases.get(i), phrases.get(j)))
					return false;
					
				String stringPhrase2 = getString(phrases.get(j));

				if (stringPhrase1.contains(stringPhrase2) || stringPhrase2.contains(stringPhrase1))
					return false;
					
			}
			
		}
		
		
		return true;
	}

	private static boolean isSearchable(String[] phrase) {
		
		for (int i = 0; i < phrase.length; i++) {
			
			if (!isSearchable(phrase[i]))
				return false;
			
		}
		
		for (int i = 0; i < phrase.length-1; i++) {
			for (int j = i+1; j < phrase.length; j++) {
				if (phrase[i].equals(phrase[j]))
					return false;
			}
		}
		
		return true;
		
	}

	private static boolean isSearchable(String word) {
		return Words.isSearchable(word);
	}

	private String getString(String[] phrase) {
		
		String ret = "";
		
		if (phrase.length != 0){
			
			for (String string : phrase) {
				
				ret = ret + " " + string;
				
			}
			
			ret = ret.substring(1);
			
		}
		
		return ret.toLowerCase();
		
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
	
	public static boolean isPatternizable(String[] nGram) {
		return isSearchable(nGram);
	}

	public List<String[]> getNGrams(){
		return phrases;
	}

	@Override
	protected String generateToString() {
		String ret = "";
		
		for (String[] phrase : phrases) {
			ret = ret + " - " + Arrays.toString(phrase);
		}
		
		return ret;
	}
}
