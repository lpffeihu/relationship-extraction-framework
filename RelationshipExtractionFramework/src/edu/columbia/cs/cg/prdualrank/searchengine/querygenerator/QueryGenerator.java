package edu.columbia.cs.cg.prdualrank.searchengine.querygenerator;

import edu.columbia.cs.cg.document.Document;
import edu.columbia.cs.cg.document.TokenizedDocument;
import edu.columbia.cs.cg.pattern.Pattern;
import edu.columbia.cs.cg.pattern.prdualrank.SearchPattern;
import edu.columbia.cs.cg.relations.Entity;
import edu.columbia.cs.cg.relations.Relationship;

public abstract class QueryGenerator<Q> {

	public abstract Q generateQuery(Relationship relationship);

	public abstract Q generateQuery(Entity role);

	public abstract Q generateQuery(SearchPattern<Document, TokenizedDocument> pattern);

}
