package edu.columbia.cs.cg.prdualrank.searchengine.querygenerator.impl;

import java.util.Collection;

import edu.columbia.cs.cg.prdualrank.searchengine.querygenerator.QueryGenerator;
import edu.columbia.cs.cg.relations.Entity;
import edu.columbia.cs.cg.relations.Relationship;

public class ConcatQueryGenerator extends QueryGenerator {

	public String generateQuery(Relationship relationship) {
		
		String ret = "";
		
		Collection<String> roles = relationship.getRoles();
		
		if (roles.size() == 0)
			return "";
		
		for (String role : roles) {
			
			ret = " " + relationship.getRole(role).getValue(); 
			
		}
		
		return ret.substring(1);
		
	}

	public String generateQuery(Entity role) {
		return role.getValue();
	}
	
}
