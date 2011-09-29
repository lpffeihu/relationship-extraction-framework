package edu.columbia.cs.api;

import java.util.List;
import java.util.Set;

import edu.columbia.cs.cg.document.Document;
import edu.columbia.cs.cg.relations.Relationship;

public interface RelationshipExtractor {

	public List<Relationship> extractTuples(Document d);
	
}
