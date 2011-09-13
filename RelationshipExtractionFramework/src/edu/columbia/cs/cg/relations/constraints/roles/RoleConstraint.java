package edu.columbia.cs.cg.relations.constraints.roles;

import java.util.Set;

import edu.columbia.cs.cg.relations.Entity;
import edu.columbia.cs.cg.relations.Relationship;

//TODO: Symetric relationships, Types of mentions 
public interface RoleConstraint {
	public boolean checkConstraint(Entity role);
	public Set<Entity> getCompatibleEntities(Set<Entity> entities);
}
