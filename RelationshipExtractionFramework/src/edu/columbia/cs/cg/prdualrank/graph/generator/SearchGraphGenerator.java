package edu.columbia.cs.cg.prdualrank.graph.generator;

import java.util.Map;

import edu.columbia.cs.cg.document.Document;
import edu.columbia.cs.cg.document.TokenizedDocument;
import edu.columbia.cs.cg.pattern.Pattern;
import edu.columbia.cs.cg.relations.Relationship;

public class SearchGraphGenerator<T extends Document,D extends TokenizedDocument> extends GraphGenerator<Document,TokenizedDocument> {

	@Override
	protected Map<Relationship, Integer> findTuples(TokenizedDocument document,
			Pattern<Document,TokenizedDocument> pattern) {
		// TODO Auto-generated method stub
		return null;
	}


}
