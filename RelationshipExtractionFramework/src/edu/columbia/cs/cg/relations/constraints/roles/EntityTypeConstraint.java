package edu.columbia.cs.cg.relations.constraints.roles;

import java.util.HashSet;
import java.util.Set;

import edu.columbia.cs.cg.relations.Entity;

public class EntityTypeConstraint implements RoleConstraint {

	private String type;
	
	public EntityTypeConstraint(String type){
		this.type=type;
	}
	
	@Override
	public boolean checkConstraint(Entity role) {
		return role.getEntityType().equals(type);
	}

	@Override
	public Set<Entity> getCompatibleEntities(Set<Entity> entities) {
		Set<Entity> results = new HashSet<Entity>();
		
		for(Entity ent : entities){
			if(checkConstraint(ent)){
				results.add(ent);
			}
		}
		
		return results;
	}
	
}
