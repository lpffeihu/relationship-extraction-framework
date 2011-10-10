/**
 * Generates a graph based on the extraction patterns and based on the assumption that extraction patterns only match the tuples whose
 * context match the pattern.
 *
 * @author      Pablo Barrio
 * @author		Goncalo Simoes
 * @version     0.1
 * @since       2011-10-07
 */
package edu.columbia.cs.cg.prdualrank.graph.generator;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.columbia.cs.cg.document.Document;
import edu.columbia.cs.cg.document.TokenizedDocument;
import edu.columbia.cs.cg.pattern.Pattern;
import edu.columbia.cs.cg.relations.Relationship;

public class ExtractionGraphGenerator<T extends Relationship, D extends TokenizedDocument> extends GraphGenerator<Relationship,TokenizedDocument>{

	/* (non-Javadoc)
	 * @see edu.columbia.cs.cg.prdualrank.graph.generator.GraphGenerator#findTuples(java.util.Set, edu.columbia.cs.cg.pattern.Pattern)
	 */
	@Override
	protected Map<Relationship, Integer> findTuples(Set<TokenizedDocument> documents,
			Pattern<Relationship,TokenizedDocument> pattern) {
		
		Map<Relationship, Integer> relationshipMap = new HashMap<Relationship, Integer>();
				
		for (TokenizedDocument document : documents) {
			
			List<Relationship> tuples = pattern.findMatch(document);
			
			for (Relationship relationship : tuples) {
				
				Integer freq = relationshipMap.get(relationship);
				
				if (freq == null){
					freq = 0;
				}
				
				relationshipMap.put(relationship, freq + 1);
				
			}

		}
			
		return relationshipMap;
	
	}

}
