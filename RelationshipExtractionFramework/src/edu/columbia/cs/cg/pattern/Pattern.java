package edu.columbia.cs.cg.pattern;

import java.util.List;

import edu.columbia.cs.cg.document.Document;

public abstract class Pattern<T> {

	public abstract List<T> findMatch(Document d);
	
}
