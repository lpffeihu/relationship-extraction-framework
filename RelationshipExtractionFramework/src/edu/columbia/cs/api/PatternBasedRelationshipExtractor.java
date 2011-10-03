package edu.columbia.cs.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import edu.columbia.cs.cg.document.TokenizedDocument;
import edu.columbia.cs.cg.pattern.Pattern;
import edu.columbia.cs.cg.relations.Relationship;

/**
 * Implementation of the relationship extractor that uses patterns to extract
 * information from a document
 * 
 * 
 * @author      Pablo Barrio
 * @author		Goncalo Simoes
 * @version     0.1
 * @since       2011-09-27
 */
public class PatternBasedRelationshipExtractor<T extends Relationship, D extends TokenizedDocument> implements RelationshipExtractor<TokenizedDocument> {

	private Set<Pattern<Relationship,TokenizedDocument>> patterns;

	/**
	 * Constructor of the pattern based relationship extractor. It receives as input a set of
	 * patterns to extract relationships from tokenized documents
	 * 
	 * @param patterns a set of patterns to extract relationships from tokenized documents
	 */
	public PatternBasedRelationshipExtractor(Set<Pattern<Relationship,TokenizedDocument>> patterns){
		this.patterns = patterns;
		
	}
	
	/**
	 * 
	 * Implementation of the extract Tuples method that uses all the patterns available to
	 * extract tuples from the input document
	 * 
	 * @param d a tokenized document from which we will extract information
	 * @return list of relationships between entities of the input document
	 */
	@Override
	public List<Relationship> extractTuples(TokenizedDocument d) {
		
		List<Relationship> rel = new ArrayList<Relationship>();

		for (Pattern<Relationship,TokenizedDocument> pattern : patterns) {
			
			rel.addAll(pattern.findMatch(d));
			
		}
		
		return rel;
		
	}

	
	
}
