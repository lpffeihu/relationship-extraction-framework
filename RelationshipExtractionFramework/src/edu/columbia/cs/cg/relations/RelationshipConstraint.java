package edu.columbia.cs.cg.relations;

import javax.management.relation.Relation;

public interface RelationshipConstraint {
	public boolean checkConstraint(Relation rel);
}
