package edu.columbia.cs.cg.relations.entity.matcher.impl;

import edu.columbia.cs.cg.relations.Entity;
import edu.columbia.cs.cg.relations.entity.matcher.EntityMatcher;

public class EqualsEntityMatcher implements EntityMatcher {

	@Override
	public boolean match(Entity original, Entity entity) {
		
		return original.equals(entity);
		
	}

}
