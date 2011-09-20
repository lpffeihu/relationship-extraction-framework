package edu.columbia.cs.cg.relations.constraints.relations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import edu.columbia.cs.cg.relations.Entity;
import edu.columbia.cs.cg.relations.Relationship;

public class EntitiesOrderNotRelevantConstraint implements RelationshipConstraint {
	
	public Set<Set<String>> previousElements = new HashSet<Set<String>>();
	
	public Set<String> getIds(Entity[] ents){
		Set<String> strings = new HashSet<String>();
		for(Entity ent : ents){
			strings.add(ent.getId());
		}
		return strings;
	}
	
	@Override
	public boolean checkConstraint(Relationship rel) {
		Set<String> strings = getIds(rel.getEntities());
		if(!previousElements.contains(strings)){
			previousElements.add(strings);
			return true;
		}
		
		return false;
	}

}
