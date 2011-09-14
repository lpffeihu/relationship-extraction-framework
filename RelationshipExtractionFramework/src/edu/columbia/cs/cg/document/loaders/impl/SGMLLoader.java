package edu.columbia.cs.cg.document.loaders.impl;

import java.io.File;
import java.util.Set;

import edu.columbia.cs.cg.document.Document;
import edu.columbia.cs.cg.document.loaders.DocumentLoader;
import edu.columbia.cs.cg.relations.RelationshipType;

public class SGMLLoader extends DocumentLoader {

	public SGMLLoader(Set<RelationshipType> relationshipTypes){
		super(relationshipTypes);
	}
	
	@Override
	public Set<Document> load(File file) {
		// TODO Auto-generated method stub
		return null;
	}

}
