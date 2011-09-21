package edu.columbia.cs.cg.relations.constraints.roles;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import edu.columbia.cs.cg.relations.Entity;
import edu.columbia.cs.cg.relations.Relationship;

public class OrRoleConstraint implements RoleConstraint {
	RoleConstraint[] constraints;
	
	public OrRoleConstraint(RoleConstraint ... constraints){
		
		this.constraints=constraints;
	}

	@Override
	public boolean checkConstraint(Entity role) {
		for(RoleConstraint c : constraints){
			if(c.checkConstraint(role)){
				return true;
			}
		}
		return false;
	}

	@Override
	public Set<Entity> getCompatibleEntities(Set<Entity> entities) {
		Set<Entity> currentSet = new HashSet<Entity>();
		for(RoleConstraint c : constraints){
			currentSet.addAll(c.getCompatibleEntities(entities));
		}
		return currentSet;
	}
}
