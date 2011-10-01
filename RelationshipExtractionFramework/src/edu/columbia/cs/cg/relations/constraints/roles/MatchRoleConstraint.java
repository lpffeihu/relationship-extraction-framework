package edu.columbia.cs.cg.relations.constraints.roles;

import java.util.HashSet;
import java.util.Set;

import edu.columbia.cs.cg.relations.Entity;
import edu.columbia.cs.cg.relations.entity.matcher.EntityMatcher;

public class MatchRoleConstraint implements RoleConstraint {

	private EntityMatcher entityMatcher;
	private Entity original;

	public MatchRoleConstraint (EntityMatcher entityMatcher, Entity original){
		this.entityMatcher = entityMatcher;
		this.original = original;
	}
	
	@Override
	public boolean checkConstraint(Entity role) {
		return entityMatcher.match(original,role);
	}

	@Override
	public Set<Entity> getCompatibleEntities(Set<Entity> entities) {
		
		Set<Entity> ret = new HashSet<Entity>();
		
		for (Entity entity : entities) {
			
			if(entityMatcher.match(original,entity))
				ret.add(entity);
			
		}
		
		return ret;
	}

}
