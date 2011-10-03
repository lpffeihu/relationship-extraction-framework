package edu.columbia.cs.cg.relations.entity.matcher;

import java.io.Serializable;

import edu.columbia.cs.cg.relations.Entity;

public interface EntityMatcher extends Serializable {

	public boolean match(Entity original, Entity entity);

}
