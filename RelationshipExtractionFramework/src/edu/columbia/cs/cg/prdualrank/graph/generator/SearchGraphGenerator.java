package edu.columbia.cs.cg.prdualrank.graph.generator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.lucene.search.Query;

import edu.columbia.cs.cg.document.Document;
import edu.columbia.cs.cg.document.TokenizedDocument;
import edu.columbia.cs.cg.pattern.Pattern;
import edu.columbia.cs.cg.pattern.prdualrank.SearchPattern;
import edu.columbia.cs.cg.prdualrank.index.Index;
import edu.columbia.cs.cg.prdualrank.searchengine.querygenerator.QueryGenerator;
import edu.columbia.cs.cg.relations.Entity;
import edu.columbia.cs.cg.relations.Relationship;
import edu.columbia.cs.cg.relations.RelationshipType;
import edu.columbia.cs.cg.relations.constraints.roles.RoleConstraint;
import edu.columbia.cs.utils.MegaCartesianProduct;

public class SearchGraphGenerator<T extends Document,D extends TokenizedDocument> extends GraphGenerator<Document,TokenizedDocument> {

	private RelationshipType rType;
	private Index index;
	private QueryGenerator<Query> queryGenerator;
	private Map<Document,Map<RelationshipType,List<Relationship>>> cachedPotentialRelationships;
	
	public SearchGraphGenerator(RelationshipType rType, Index index, QueryGenerator<Query> queryGenerator){
		this.rType = rType;
		this.index = index;
		this.queryGenerator = queryGenerator;
	}
	@Override
	protected Map<Relationship, Integer> findTuples(Set<TokenizedDocument> documents,
			Pattern<Document,TokenizedDocument> pattern){
	
		List<TokenizedDocument> matchedDocs = index.search(queryGenerator.generateQuery((SearchPattern<Document, TokenizedDocument>)pattern),documents.size());
	
		//matchedDocs are part of documents
		
		Map<Relationship,Integer> tupleMap = new HashMap<Relationship, Integer>();
		
		//TODO: this code should be optimized the get and put of
		//HashMap are being called too many times
		for (TokenizedDocument document : matchedDocs) {

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
		
		List<Relationship> cached = getCatchedPotentialTuples(document,relationshipType); 
		
		if (cached != null)
			return cached;
		
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
		
		putCatchedPotentialTuples(document,rType,matchingTuples);
		
		return matchingTuples;
		
	}
	private void putCatchedPotentialTuples(TokenizedDocument document,
			RelationshipType relationshipType, List<Relationship> matchingTuples) {
		
		getCatchedPotentialTuples(document, relationshipType).addAll(matchingTuples);
		
	}
	private List<Relationship> getCatchedPotentialTuples(
			TokenizedDocument document, RelationshipType relationshipType) {
		
		List<Relationship> byType = getCachedPotentialTuplesbyDocument(document).get(relationshipType);
		
		if (byType == null){
			getCachedPotentialTuplesbyDocument(document).put(relationshipType, new ArrayList<Relationship>());
			return null;
		}
		
		return byType;
	}
	private Map<RelationshipType, List<Relationship>> getCachedPotentialTuplesbyDocument(
			TokenizedDocument document) {
		
		Map<RelationshipType, List<Relationship>> byDoc = getCachedPotentialTuplesTable().get(document);
		
		if (byDoc == null){
			
			byDoc = new HashMap<RelationshipType, List<Relationship>>();
			
			getCachedPotentialTuplesTable().put(document,byDoc);
			
		}
		
		return byDoc;
		
	}
	private Map<Document, Map<RelationshipType, List<Relationship>>> getCachedPotentialTuplesTable() {
		
		if (cachedPotentialRelationships == null){
			cachedPotentialRelationships = new HashMap<Document, Map<RelationshipType,List<Relationship>>>();
		}
		return cachedPotentialRelationships;
	}

}
