package edu.columbia.cs.cg.prdualrank.searchengine.querygenerator;

import edu.columbia.cs.cg.relations.Entity;
import edu.columbia.cs.cg.relations.Relationship;

public abstract class QueryGenerator {

	public abstract String generateQuery(Relationship relationship);

	public abstract String generateQuery(Entity role);

}
