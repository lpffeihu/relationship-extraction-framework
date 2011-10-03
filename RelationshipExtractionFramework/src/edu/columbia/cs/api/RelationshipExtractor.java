package edu.columbia.cs.api;

import java.util.List;
import java.util.Set;

import edu.columbia.cs.cg.document.Document;
import edu.columbia.cs.cg.relations.Relationship;

/**
 * Interface for Relationship Extractor
 * 
 * <br>
 * <br>
 * 
 * A class that implements this interface is responsible for extracting tuples from documents.
 * The method responsible for this task is the extractTuples method.
 * 
 * @author      Pablo Barrio
 * @author		Goncalo Simoes
 * @version     0.1
 * @since       2011-09-27
 */
public interface RelationshipExtractor {

	/**
	 * Given an input document d this method is responsible for extracting all the relationships
	 * between entities of the document
	 * 
	 * @param d the document that contains the information to be extracted
	 * @return a list of relationships between entities present in the document d
	 */
	public List<Relationship> extractTuples(Document d);
	
}
