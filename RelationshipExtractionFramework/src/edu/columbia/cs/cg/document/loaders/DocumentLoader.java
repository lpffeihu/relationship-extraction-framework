package edu.columbia.cs.cg.document.loaders;

import java.io.File;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import edu.columbia.cs.cg.document.Document;
import edu.columbia.cs.cg.relations.RelationshipType;
import edu.columbia.cs.data.Loader;

public abstract class DocumentLoader extends Loader<Document> {
	
	protected Set<RelationshipType> relationshipTypes;
	
	public DocumentLoader(Set<RelationshipType> relationshipTypes){
		this.relationshipTypes=relationshipTypes;
	}
	
	public abstract Set<Document> load(File file);
}
