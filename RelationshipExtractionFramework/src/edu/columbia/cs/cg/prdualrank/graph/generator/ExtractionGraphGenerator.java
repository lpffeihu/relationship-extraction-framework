package edu.columbia.cs.cg.prdualrank.graph.generator;

import java.util.Hashtable;

import edu.columbia.cs.cg.document.Document;
import edu.columbia.cs.cg.document.TokenizedDocument;
import edu.columbia.cs.cg.pattern.Pattern;
import edu.columbia.cs.cg.relations.Relationship;

public class ExtractionGraphGenerator<T extends Relationship, D extends TokenizedDocument> extends GraphGenerator<Relationship,TokenizedDocument>{

	@Override
	protected Hashtable<Relationship, Integer> findTuples(Document document,
			Pattern<Relationship,TokenizedDocument> pattern) {
		// TODO Auto-generated method stub
		return null;
	}

}
