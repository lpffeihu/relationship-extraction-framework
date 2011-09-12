package edu.columbia.cs.cg.document.loaders.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import edu.columbia.cs.cg.candidates.CandidatesGenerator;
import edu.columbia.cs.cg.document.Document;
import edu.columbia.cs.cg.document.Segment;
import edu.columbia.cs.cg.document.loaders.DocumentLoader;
import edu.columbia.cs.cg.document.loaders.impl.ace2005.ACEDocument;
import edu.columbia.cs.cg.document.loaders.impl.ace2005.ACEEMention;
import edu.columbia.cs.cg.document.loaders.impl.ace2005.ACEEntity;
import edu.columbia.cs.cg.document.loaders.impl.ace2005.ACERMention;
import edu.columbia.cs.cg.document.loaders.impl.ace2005.ACERelation;
import edu.columbia.cs.cg.relations.Entity;
import edu.columbia.cs.cg.relations.Relationship;
import edu.columbia.cs.cg.relations.RelationshipType;
import edu.columbia.cs.cg.sentence.impl.OpenNLPMESplitter;

public class ACE2005Loader extends DocumentLoader {

	@Override
	public Document load(File file, Set<RelationshipType> relationshipTypes) {
		ACEDocument aceDoc = new ACEDocument();
		aceDoc.load(file.getAbsolutePath());
		List<Segment> plainText=aceDoc.getSegments();
		
		Document newDocument = new Document(plainText);
		
		for(ACEEntity ent : aceDoc.getEntities()){
			String entityId = ent.getId();
			String entityType = ent.getType();
			String entitySubType = ent.getSubtype();
			for(ACEEMention men : ent.getMentions()){
				String mentionId=men.getId();
				String mentionType=men.getType();
				int extStartIndex= men.getExtOffset();
				int extLength=men.getExtLength();
				int headStartIndex=men.getHeadOffset();
				int headLength=men.getHeadLength();
				String value=newDocument.getSubstring(headStartIndex, headLength);
				Entity newEntity = new Entity(mentionId, entityType, headStartIndex, headLength,
						value, newDocument);
				newDocument.addEntity(newEntity);
			}
		}
		
		for(ACERelation rel : aceDoc.getRelationships()){
			String relationshipId = rel.getId();
			String type = rel.getType();
			String subType = rel.getSubType();
			
			RelationshipType relType = getCompatibleType(relationshipTypes, type);
			if(relType!=null){
				for(ACERMention men : rel.getMentions()){
					Entity arg1 = newDocument.getEntity(men.getArg1().getId());
					Entity arg2 = newDocument.getEntity(men.getArg2().getId());
					if(arg1!=null && arg2!=null){
						Relationship newRelation = new Relationship(relType,men.getId());
						newRelation.setRole("Arg-1", arg1);
						newRelation.setRole("Arg-2", arg2);
						newDocument.addRelationship(newRelation);
					}
				} 
			}
		}
		
		return newDocument;
	}
	
	private RelationshipType getCompatibleType(Set<RelationshipType> relationshipTypes, String relType){
		for(RelationshipType type : relationshipTypes){
			if(type.isType(relType)){
				return type;
			}
		}
		return null;
	}

}
