package edu.columbia.cs.cg.relations.constraints.relations;

import java.io.Serializable;
import java.util.Set;

import edu.columbia.cs.cg.relations.Relationship;

public interface RelationshipConstraint extends Serializable{
	public boolean checkConstraint(Relationship rel);
}
