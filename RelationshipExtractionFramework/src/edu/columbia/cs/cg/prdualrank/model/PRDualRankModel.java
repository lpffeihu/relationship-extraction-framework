/**
 * The model used to include PRDualRank in the framework. The model contains all the ranked search and extraction patterns. Also includes additional information (ranking and values) of the tuples extracted during generation
 * of patterns.
 *
 * @author      Pablo Barrio
 * @author		Goncalo Simoes
 * @version     0.1
 * @since       2011-10-07
 */
package edu.columbia.cs.cg.prdualrank.model;

import java.util.List;
import java.util.Set;
import java.util.SortedSet;

import edu.columbia.cs.cg.document.Document;
import edu.columbia.cs.cg.pattern.Pattern;
import edu.columbia.cs.cg.pattern.matchable.Matchable;
import edu.columbia.cs.cg.relations.Relationship;
import edu.columbia.cs.cg.relations.RelationshipType;
import edu.columbia.cs.model.Model;
import edu.columbia.cs.og.configuration.StructureConfiguration;
import edu.columbia.cs.og.structure.OperableStructure;

public class PRDualRankModel<S extends Matchable,E extends Matchable, D extends Document> extends Model {

	private SortedSet<Pattern<S,D>> sPatterns;
	private SortedSet<Pattern<E,D>> ePatterns;
	private SortedSet<Relationship> sTuples;
	private SortedSet<Relationship> eTuples;

	/**
	 * Instantiates a new pR dual rank model.
	 *
	 * @param searchPatterns the search patterns generated during execution.
	 * @param extractionPatterns the extraction patterns generated during execution.
	 * @param searchTuples the search tuples extracted during the generation of search patterns.
	 * @param extractionTuples the extraction tuples extracted during the generation of extraction patterns.
	 */
	public PRDualRankModel(SortedSet<Pattern<S,D>> searchPatterns, SortedSet<Pattern<E,D>> extractionPatterns, SortedSet<Relationship> searchTuples, SortedSet<Relationship> extractionTuples) {
		
		this.sPatterns = searchPatterns;
		this.ePatterns = extractionPatterns;
		this.sTuples = searchTuples;
		this.eTuples = extractionTuples;
		
	}

	@Override
	protected Set<String> getPredictedLabel(OperableStructure s) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Object getPredictionPropertyValue(
			PredictionProperties predictionProperties) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected PredictionProperties[] getAvailablePredictionProperties() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see edu.columbia.cs.model.Model#getRelationshipTypes()
	 */
	@Override
	public Set<RelationshipType> getRelationshipTypes() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see edu.columbia.cs.model.Model#getStructureConfiguration()
	 */
	@Override
	public StructureConfiguration getStructureConfiguration() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {

		String ret = "Search Patterns: ";
		
		for (Pattern<S,D> pattern : this.sPatterns) {
			ret = ret + "\n" + pattern.toString();
		}

		ret = ret + "\nExtraction Patterns: ";
		
		for (Pattern<E,D> pattern : this.ePatterns) {
			ret = ret + "\n" + pattern.toString();
		}

		return ret;
	}
}
