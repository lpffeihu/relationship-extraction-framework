package edu.columbia.cs.cg.relations.entity.matcher.impl;

import edu.columbia.cs.cg.relations.Entity;
import edu.columbia.cs.cg.relations.entity.matcher.EntityMatcher;

public class OrEntityMatcher implements EntityMatcher {

	private EntityMatcher[] entityMatchers;

	public OrEntityMatcher(EntityMatcher ...entityMatchers){
		this.entityMatchers = entityMatchers;
	}
	
	@Override
	public boolean match(Entity original, Entity entity) {
		
		for (int i = 0; i < entityMatchers.length; i++) {
			
			if (entityMatchers[i].match(original, entity))
				return true;
			
		}
		
		return false;
	}

}
