package edu.columbia.cs.cg.document.loaders.impl;

import java.io.File;
import java.util.Set;

import edu.columbia.cs.cg.document.Document;
import edu.columbia.cs.cg.document.loaders.DocumentLoader;
import edu.columbia.cs.cg.relations.RelationshipType;

/**
 * Loader for the Aimed collection
 * 
 * @author      Pablo Barrio
 * @author		Goncalo Simoes
 * @version     0.1
 * @since       2011-09-27
 */
public class AImedLoader extends DocumentLoader {

	/**
	 * Constructor of the loader
	 * 
	 * @param relationshipTypes Represents the types of relationships to be extracted from the collection
	 * including the constraints that they must fulfill
	 */
	public AImedLoader(Set<RelationshipType> relationshipTypes){
		super(relationshipTypes);
	}
	
	/**
	 * Method that loads a set of documents given a File that represents the directory of the collection
	 * 
	 * @param file Represents the directory of the collection
	 * @return a set of Documents representing the documents of a collection
	 */
	@Override
	public Set<Document> load(File file) {
		// TODO Auto-generated method stub
		return null;
	}

}
