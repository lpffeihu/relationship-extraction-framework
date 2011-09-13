package edu.columbia.cs.cg.relations.constraints.roles;

import java.util.HashSet;
import java.util.Set;

import edu.columbia.cs.cg.relations.Entity;

public class NoConstraint implements RoleConstraint {

	
	@Override
	public boolean checkConstraint(Entity role) {
		return true;
	}

	@Override
	public Set<Entity> getCompatibleEntities(Set<Entity> entities) {
		return entities;
	}
	
}
