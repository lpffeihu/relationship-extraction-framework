/**
 * Gives the interface for any pattern extractor in PRDualRank.
 *
 * @author      Pablo Barrio
 * @author		Goncalo Simoes
 * @version     0.1
 * @since       2011-10-07
 */
package edu.columbia.cs.cg.prdualrank.pattern.extractor;

import java.util.List;
import java.util.Map;

import edu.columbia.cs.cg.document.Document;
import edu.columbia.cs.cg.document.TokenizedDocument;
import edu.columbia.cs.cg.pattern.Pattern;
import edu.columbia.cs.cg.pattern.matchable.Matchable;
import edu.columbia.cs.cg.relations.Relationship;

public interface PatternExtractor<T extends Matchable> {

	/**
	 * Extract specific patterns from the document in the parameter list for the specified relationship and other matching relationships in the same document. The definition of matching used in
	 * this project is based on the EntityMatchers contained in the specified relationship.
	 *
	 * @param document the document to be processed.
	 * @param relationship the relationship that the extractor is trying to generate patterns for.
	 * @param matchingRelationships the relationships contained in 'document' that match the specified relationship.
	 * @return the map
	 */
	public Map<Pattern<T,TokenizedDocument>,Integer> extractPatterns(TokenizedDocument document, Relationship relationship, List<Relationship> matchingRelationships);
			
}
