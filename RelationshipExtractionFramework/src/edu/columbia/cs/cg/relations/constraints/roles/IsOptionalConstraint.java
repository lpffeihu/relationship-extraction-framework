package edu.columbia.cs.cg.relations.constraints.roles;

import java.util.HashSet;
import java.util.Set;

import edu.columbia.cs.cg.relations.Entity;

public class IsOptionalConstraint implements RoleConstraint {
	
	@Override
	public boolean checkConstraint(Entity role) {
		return true;
	}

	@Override
	public Set<Entity> getCompatibleEntities(Set<Entity> entities) {
		Set<Entity> results = new HashSet<Entity>();
		
		results.add(Entity.NULL_ENTITY);
		for(Entity ent : entities){
			results.add(ent);
		}
		
		return results;
	}
	
}
