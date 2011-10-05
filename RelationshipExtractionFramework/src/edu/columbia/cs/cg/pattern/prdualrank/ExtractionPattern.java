package edu.columbia.cs.cg.pattern.prdualrank;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import edu.columbia.cs.cg.document.Document;
import edu.columbia.cs.cg.document.TokenizedDocument;
import edu.columbia.cs.cg.pattern.Pattern;
import edu.columbia.cs.cg.relations.Entity;
import edu.columbia.cs.cg.relations.Relationship;
import edu.columbia.cs.cg.relations.RelationshipType;
import edu.columbia.cs.cg.relations.constraints.relations.RelationshipConstraint;
import edu.columbia.cs.utils.MegaCartesianProduct;

public class ExtractionPattern<T extends Relationship, D extends TokenizedDocument> extends Pattern<Relationship,TokenizedDocument> {

	private List<String> roles;
	private Map<String, SimpleAttributeExtractionPattern<Entity,TokenizedDocument>> simpleAttributeMap;
	private RelationshipType rType;
	private RelationshipConstraint rConstraint;
	private int hashCode;

	public ExtractionPattern(List<String> roles, Map<String, SimpleAttributeExtractionPattern<Entity,TokenizedDocument>> simpleAttributeMap, RelationshipType rType) {
		
		this.roles = roles;
		this.simpleAttributeMap = simpleAttributeMap;
		this.rType = rType;
		this.rConstraint = rType.getRelationshipConstraint();
		hashCode = -1;
	}

	@Override
	public List<Relationship> findMatch(TokenizedDocument d) {
		
		Map<String,Set<Entity>> combinationsMap = new HashMap<String, Set<Entity>>();
		
		for (String role : roles) {
			
			combinationsMap.put(role, new HashSet<Entity>(simpleAttributeMap.get(role).findMatch(d)));
			
		}
		
		List<Map<String, Entity>> combinations = MegaCartesianProduct.generateAllPossibilities(combinationsMap);
		
		List<Relationship> tuples = new ArrayList<Relationship>();
		
		for (Map<String, Entity> map : combinations) {
			
			if (!inOrder(map,roles)){
				
				continue;
				
			}
			
			Relationship r = new Relationship(rType);
			
			for (Entry<String, Entity> entry : map.entrySet()) {
				
				r.setRole(entry.getKey(), entry.getValue());
				
			}
			
			if (rConstraint.checkConstraint(r)){
				
				tuples.add(r);
				
			}
			
		}
		
		return tuples;
		
	}

	private boolean inOrder(Map<String, Entity> map, List<String> roles) {
	
		Entity e = map.get(roles.get(0));
		
		for (int i = 1; i < roles.size(); i++) {
			
			Entity e2 = map.get(roles.get(i));
			
			if (e.getOffset() <= e2.getOffset()){
				
				e = e2;
				
			} else{
				
				return false;
				
			}
			
		}
	
		return true;
		
	}

	protected int generateHashCode() {
		
		int hashCode = 1;
		
		hashCode = 31*hashCode + simpleAttributeMap.hashCode();

		hashCode = 31*hashCode + rType.hashCode();
		
		hashCode = 31*hashCode + rConstraint.hashCode();
		
		return hashCode;
		
	}

	public String toString(){
		return simpleAttributeMap.toString();
	}
}
