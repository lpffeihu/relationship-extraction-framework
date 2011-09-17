package edu.columbia.cs.cg.relations.constraints.relations;

import java.util.Set;

import edu.columbia.cs.cg.relations.Relationship;

public class DummyRelationshipConstraint implements RelationshipConstraint {
	public boolean checkConstraint(Relationship rel){
		return true;
	}
}
