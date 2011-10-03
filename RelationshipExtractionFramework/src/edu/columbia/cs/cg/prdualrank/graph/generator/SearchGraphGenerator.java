package edu.columbia.cs.cg.prdualrank.graph.generator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import edu.columbia.cs.cg.document.Document;
import edu.columbia.cs.cg.document.TokenizedDocument;
import edu.columbia.cs.cg.pattern.Pattern;
import edu.columbia.cs.cg.relations.Entity;
import edu.columbia.cs.cg.relations.Relationship;
import edu.columbia.cs.cg.relations.RelationshipType;
import edu.columbia.cs.cg.relations.constraints.roles.RoleConstraint;
import edu.columbia.cs.utils.MegaCartesianProduct;

public class SearchGraphGenerator<T extends Document,D extends TokenizedDocument> extends GraphGenerator<Document,TokenizedDocument> {

	private RelationshipType rType;
	
	public SearchGraphGenerator(RelationshipType rType){
		this.rType = rType;
	}
	@Override
	protected Map<Relationship, Integer> findTuples(TokenizedDocument document,
			Pattern<Document,TokenizedDocument> pattern) {
	
		List<Document> matchedDocs = pattern.findMatch(document);
	
		Map<Relationship,Integer> tupleMap = new HashMap<Relationship, Integer>();
		
		if (!matchedDocs.isEmpty()){
			
			List<Relationship> tuples = retrievePotentialTuples(document,rType);
			
			for (Relationship relationship : tuples) {
				
				Integer freq = tupleMap.get(relationship);
				
				if (freq == null){
					freq = 0;
				}
				
				tupleMap.put(relationship, freq + 1);
			}
			
		}
		
		return tupleMap;
		
	}
	private List<Relationship> retrievePotentialTuples(
			TokenizedDocument document, RelationshipType relationshipType) {
		
		Set<Entity> entities = new HashSet<Entity>(document.getEntities());
		
		Set<String> roles = relationshipType.getRoles();
		
		Map<String,Set<Entity>> candidateEntitiesForRole = new HashMap<String,Set<Entity>>();
		
		for(String role : roles){
			
			RoleConstraint roleConstraint = relationshipType.getConstraint(role);
			
			Set<Entity> entitiesForRole = roleConstraint.getCompatibleEntities(entities);
			
			candidateEntitiesForRole.put(role, entitiesForRole);
		
		}

		List<Relationship> matchingTuples = new ArrayList<Relationship>();
		
		for(Map<String,Entity> candidate : MegaCartesianProduct.generateAllPossibilities(candidateEntitiesForRole)){
			
			Relationship newRelationship = new Relationship(relationshipType);
			
			for(Entry<String,Entity> entry : candidate.entrySet()){

				newRelationship.setRole(entry.getKey(), entry.getValue());
			
			}

			if (relationshipType.getRelationshipConstraint().checkConstraint(newRelationship)){
				
				matchingTuples.add(newRelationship);
				
			}
			
		}
		
		return matchingTuples;
		
	}

}
