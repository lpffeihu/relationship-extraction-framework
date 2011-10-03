package edu.columbia.cs.cg.prdualrank.graph.generator;

import java.util.Hashtable;

import edu.columbia.cs.cg.document.Document;
import edu.columbia.cs.cg.document.TokenizedDocument;
import edu.columbia.cs.cg.pattern.Pattern;
import edu.columbia.cs.cg.relations.Relationship;

public class SearchGraphGenerator<T extends Document,D extends TokenizedDocument> extends GraphGenerator<Document,TokenizedDocument> {

	@Override
	protected Hashtable<Relationship, Integer> findTuples(Document document,
			Pattern<Document,TokenizedDocument> pattern) {
		// TODO Auto-generated method stub
		return null;
	}


}
