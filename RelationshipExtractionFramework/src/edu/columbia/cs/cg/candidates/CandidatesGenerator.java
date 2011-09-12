package edu.columbia.cs.cg.candidates;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.columbia.cs.cg.document.Document;
import edu.columbia.cs.cg.document.Segment;
import edu.columbia.cs.cg.relations.Entity;
import edu.columbia.cs.cg.relations.Relationship;
import edu.columbia.cs.cg.relations.RelationshipType;
import edu.columbia.cs.cg.sentence.Sentence;
import edu.columbia.cs.cg.sentence.SentenceSplitter;
import edu.columbia.cs.utils.Pair;

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

		for(int sentId=0; sentId<sents.length; sentId++){
			Sentence sent = sents[sentId];
			System.out.println(sent);
			Map<String,Entity> sentenceEntities = new HashMap<String,Entity>();
			List<Relationship> sentenceRelationships = new ArrayList<Relationship>();

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

			System.out.println(sentenceEntities);

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
			
			System.out.println(sentenceRelationships);

			for(RelationshipType t : relationshipTypes){
				result.addAll(generateCandidateSentences(sent,
						sentenceEntities, sentenceRelationships,
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
			Sentence sent, Map<String, Entity> ents,
			List<Relationship> sentenceRelationships,
			RelationshipType relationshipType) {
		Set<Pair<String,String>> alreadyConsideredPairs = new HashSet<Pair<String,String>>();

		List<CandidateSentence> result = new ArrayList<CandidateSentence>();
		
		
		
		return null;
	}
}
