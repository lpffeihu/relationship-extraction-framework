package edu.columbia.cs.cg.document.loaders;

import java.io.File;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import edu.columbia.cs.cg.document.Document;
import edu.columbia.cs.cg.relations.RelationshipType;

public abstract class DocumentLoader {
	public Set<Document> load(Collection<File> files, Set<RelationshipType> relationshipTypes){
		Set<Document> documents = new HashSet<Document>();
		for(File f : files){
			documents.add(load(f,relationshipTypes));
		}
		return documents;
	}
	
	public abstract Document load(File file, Set<RelationshipType> relationshipTypes);
}
