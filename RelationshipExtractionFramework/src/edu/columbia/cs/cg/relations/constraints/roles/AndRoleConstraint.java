package edu.columbia.cs.cg.relations.constraints.roles;

import java.util.Set;

import edu.columbia.cs.cg.relations.Entity;
import edu.columbia.cs.cg.relations.Relationship;

public class AndRoleConstraint implements RoleConstraint {
	RoleConstraint[] constraints;
	
	public AndRoleConstraint(RoleConstraint ... constraints){
		this.constraints=constraints;
	}

	@Override
	public boolean checkConstraint(Entity role) {
		for(RoleConstraint c : constraints){
			if(!c.checkConstraint(role)){
				return false;
			}
		}
		return true;
	}

	@Override
	public Set<Entity> getCompatibleEntities(Set<Entity> entities) {
		Set<Entity> currentSet = entities;
		for(RoleConstraint c : constraints){
			currentSet = c.getCompatibleEntities(currentSet);
			if(currentSet.isEmpty()){
				return currentSet;
			}
		}
		return currentSet;
	}
}
