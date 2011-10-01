package edu.columbia.cs.cg.relations.entity.matcher;

import edu.columbia.cs.cg.relations.Entity;

public interface EntityMatcher {

	public boolean match(Entity original, Entity entity);

}
