package edu.columbia.cs.cg.candidates;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import edu.columbia.cs.cg.document.Document;
import edu.columbia.cs.cg.relations.Entity;
import edu.columbia.cs.cg.relations.Relationship;
import edu.columbia.cs.cg.relations.RelationshipType;
import edu.columbia.cs.cg.relations.constraints.roles.RoleConstraint;
import edu.columbia.cs.cg.sentence.Sentence;
import edu.columbia.cs.cg.sentence.SentenceSplitter;
import edu.columbia.cs.utils.MegaCartesianProduct;

public class CandidatesGenerator {
	private SentenceSplitter splitter;

	public CandidatesGenerator(SentenceSplitter splitter){
		this.splitter=splitter;
	}

	public Set<CandidateSentence> generateCandidates(Document doc, Set<RelationshipType> relationshipTypes){
		List<Entity> entities = new ArrayList<Entity>(doc.getEntities());
		Collections.sort(entities);
		int currentEntity=0;
		int numEntities=entities.size();

		Set<CandidateSentence> result = new HashSet<CandidateSentence>();

		List<Relationship> relationships = new ArrayList<Relationship>(doc.getRelationships());

		Sentence[] sents = splitter.split(doc);

		//TODO: Think about problems with the sentence splitter separating
		//entities
		for(int sentId=0; sentId<sents.length; sentId++){
			Sentence sent = sents[sentId];
			//System.out.println(sent);
			Map<String,Entity> sentenceEntities = new HashMap<String,Entity>();
			Set<Relationship> sentenceRelationships = new HashSet<Relationship>();

			int entityIndex=currentEntity;
			while(entityIndex<numEntities){
				Entity entity=entities.get(entityIndex);
				int startEntity = entity.getOffset();
				int endEntity = entity.getOffset()+entity.getLength();
				if(startEntity>=sent.getOffset() && startEntity<sent.getOffset()+sent.getLength()&&
						endEntity>=sent.getOffset() && endEntity<sent.getOffset()+sent.getLength()){
					sentenceEntities.put(entity.getId(),entity);
				}

				if(startEntity>=sent.getOffset()+sent.getLength()){
					break;
				}
				entityIndex++;
			}
			currentEntity=entityIndex;

			//System.out.println(sentenceEntities);

			int relationshipsSize = relationships.size();
			for(int i=0; i<relationshipsSize; i++){
				Relationship rel = relationships.get(i);
				Entity[] relationshipEntities = rel.getEntities();

				if(containsAllEntities(sentenceEntities, relationshipEntities)){
					sentenceRelationships.add(rel);
					relationships.remove(i);
					relationshipsSize--;
					i--;
				}
			}
			
			//System.out.println(sentenceRelationships);

			for(RelationshipType t : relationshipTypes){
				result.addAll(generateCandidateSentences(doc,sent,
						new HashSet<Entity>(sentenceEntities.values()), sentenceRelationships,
						t));
			}
		}

		return result;
	}

	private boolean containsAllEntities(Map<String,Entity> sentenceEntities, Entity[] entities){
		for(Entity ent : entities){
			if(!sentenceEntities.containsKey(ent.getId())){
				return false;
			}
		}
		return true;
	}
	
	private Set<CandidateSentence> generateCandidateSentences(
			Document doc,
			Sentence sent, Set<Entity> ents,
			Set<Relationship> sentenceRelationships,
			RelationshipType relationshipType) {
		Set<CandidateSentence> result = new HashSet<CandidateSentence>();
		
		Set<String> roles = relationshipType.getRoles();
		Map<String,Set<Entity>> candidateEntitiesForRole = new HashMap<String,Set<Entity>>();
		for(String role : roles){
			RoleConstraint roleConstraint = relationshipType.getConstraint(role);
			Set<Entity> entitiesForRole = roleConstraint.getCompatibleEntities(ents);
			candidateEntitiesForRole.put(role, entitiesForRole);
		}
		
		List<Map<String,Entity>> candidates = MegaCartesianProduct.generateAllPossibilities(candidateEntitiesForRole);
		for(Map<String,Entity> candidate : candidates){
			Relationship newRelationship = new Relationship(relationshipType);
			for(Entry<String,Entity> entry : candidate.entrySet()){
				newRelationship.setRole(entry.getKey(), entry.getValue());
			}
			
			if(sentenceRelationships.contains(newRelationship)){
				newRelationship.setLabel(relationshipType.getType());
			}else{
				newRelationship.setLabel(RelationshipType.NOT_A_RELATIONSHIP);
			}
			
			if(relationshipType.getRelationshipConstraint().checkConstraint(newRelationship)){
				result.add(new CandidateSentence(sent, newRelationship));
			}
		}
		
		
		return result;
	}
}
