package edu.columbia.cs.cg.pattern;

import java.util.List;

import edu.columbia.cs.cg.document.Document;
import edu.columbia.cs.cg.document.TokenizedDocument;
import edu.columbia.cs.cg.pattern.matchable.Matchable;

public abstract class Pattern<T extends Matchable,D extends Document> {

	public abstract List<T> findMatch(D d);
	
}
