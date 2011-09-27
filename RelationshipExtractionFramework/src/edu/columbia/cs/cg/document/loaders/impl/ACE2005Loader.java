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
import edu.columbia.cs.utils.SGMFileFilter;

public class ACE2005Loader extends DocumentLoader {
	private SGMFileFilter filter = new SGMFileFilter();
	
	public ACE2005Loader(Set<RelationshipType> relationshipTypes){
		super(relationshipTypes);
	}
	
	@Override
	public Set<Document> load(File file) {
		Set<Document> result = new HashSet<Document>();
		if(filter.accept(file.getAbsoluteFile(), file.getName())){
			ACEDocument aceDoc = new ACEDocument();
			String absPath=file.getAbsolutePath();
			absPath=absPath.substring(0,absPath.length()-4);
			aceDoc.load(absPath);
			List<Segment> plainText=aceDoc.getSegments();
			
			String fileName = file.getName();
			fileName=fileName.substring(0,fileName.length()-4);
			
			Document newDocument = new Document(file.getParent(),fileName,plainText);
			
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
				
				RelationshipType relType = getCompatibleType(relationshipTypes, subType);
				if(relType!=null){
					for(ACERMention men : rel.getMentions()){
						Entity arg1 = newDocument.getEntity(men.getArg1().getId());
						Entity arg2 = newDocument.getEntity(men.getArg2().getId());
						if(arg1!=null && arg2!=null){
							Relationship newRelation = new Relationship(relType);
							newRelation.setRole("Arg-1", arg1);
							newRelation.setRole("Arg-2", arg2);
							newDocument.addRelationship(newRelation);
						}
					} 
				}
			}
			result.add(newDocument);
		}
		
		return result;
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
