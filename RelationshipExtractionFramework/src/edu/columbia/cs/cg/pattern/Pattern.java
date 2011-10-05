package edu.columbia.cs.cg.pattern;

import java.util.List;

import edu.columbia.cs.cg.document.Document;
import edu.columbia.cs.cg.document.TokenizedDocument;
import edu.columbia.cs.cg.pattern.matchable.Matchable;
import edu.columbia.cs.cg.pattern.prdualrank.SearchPattern;

public abstract class Pattern<T extends Matchable,D extends Document> {

	private int hashCode = -1;
	
	public abstract List<T> findMatch(D d);
	
	public int hashCode(){
		
		if (hashCode == -1){
			hashCode = generateHashCode();
		}
		
		return hashCode;
	}

	protected abstract int generateHashCode();
	
	public boolean equals(Object obj) {
		Pattern<T, D> other = (Pattern<T, D>) obj;
		return this.hashCode() == other.hashCode();
	}

}
