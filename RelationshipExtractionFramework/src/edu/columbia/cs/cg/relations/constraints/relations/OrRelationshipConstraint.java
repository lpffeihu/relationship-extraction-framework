package edu.columbia.cs.cg.relations.constraints.relations;

import edu.columbia.cs.cg.relations.Relationship;

public class OrRelationshipConstraint implements RelationshipConstraint{

	private RelationshipConstraint[] constraints;

	public OrRelationshipConstraint(RelationshipConstraint ... constraints){
		this.constraints = constraints;
	}
	
	@Override
	public boolean checkConstraint(Relationship rel) {
		
		for (RelationshipConstraint constraint : constraints) {
			
			if (constraint.checkConstraint(rel))
				return true;
			
		}
		
		return false;
		
	}

}
