package edu.columbia.cs.cg.candidates;

import java.util.ArrayList;
import java.util.Arrays;
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
import edu.columbia.cs.utils.Span;

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
			Span sentenceSpan = new Span(sent.getOffset(),sent.getOffset()+sent.getLength());
			int entityIndex=currentEntity;
			while(entityIndex<numEntities){
				Entity entity=entities.get(entityIndex);
				int startEntity = entity.getOffset();
				int endEntity = entity.getOffset()+entity.getLength();
				Span entitySpan = new Span(startEntity,endEntity);
				
				if(sentenceSpan.intersects(entitySpan) && !sentenceSpan.contains(entitySpan)){
					throw new UnsupportedOperationException();
				}
				
				entityIndex++;
			}
		}
		
		currentEntity=0;
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

			result.addAll(generateCandidateSentences(doc,sent,
					new HashSet<Entity>(sentenceEntities.values()), sentenceRelationships,
					relationshipTypes));
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
			Set<RelationshipType> relationshipTypes) {
		Set<CandidateSentence> result = new HashSet<CandidateSentence>();

		Map<List<Entity>,List<Relationship>> uniqueMaps = new HashMap<List<Entity>,List<Relationship>>();
		for(RelationshipType t : relationshipTypes){
			Set<String> roles = t.getRoles();
			Map<String,Set<Entity>> candidateEntitiesForRole = new HashMap<String,Set<Entity>>();
			for(String role : roles){
				RoleConstraint roleConstraint = t.getConstraint(role);
				Set<Entity> entitiesForRole = roleConstraint.getCompatibleEntities(ents);
				candidateEntitiesForRole.put(role, entitiesForRole);
			}
			for(Map<String,Entity> candidate : MegaCartesianProduct.generateAllPossibilities(candidateEntitiesForRole)){
				Relationship newRelationship = new Relationship(t);
				for(Entry<String,Entity> entry : candidate.entrySet()){
					newRelationship.setRole(entry.getKey(), entry.getValue());
				}

				if(sentenceRelationships.contains(newRelationship)){
					newRelationship.setLabel(t.getType());
				}else{
					newRelationship.setLabel(RelationshipType.NOT_A_RELATIONSHIP);
				}

				List<Entity> entities = Arrays.asList(newRelationship.getEntities());
				List<Relationship> inputs = uniqueMaps.get(entities);
				if(inputs==null){
					inputs=new ArrayList<Relationship>();
				}
				inputs.add(newRelationship);
				uniqueMaps.put(entities, inputs);
			}
		}
		
		for(Entry<List<Entity>,List<Relationship>> entry : uniqueMaps.entrySet()){
			CandidateSentence cand = new CandidateSentence(sent, entry.getKey());
			for(Relationship rel : entry.getValue()){
				cand.addRelationship(rel);
			}
			result.add(cand);
		}

		return result;
	}
}
