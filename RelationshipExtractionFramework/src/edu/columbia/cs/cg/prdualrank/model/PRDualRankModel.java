package edu.columbia.cs.cg.prdualrank.model;

import java.util.List;
import java.util.Set;
import java.util.SortedSet;

import edu.columbia.cs.cg.pattern.Pattern;
import edu.columbia.cs.cg.relations.Relationship;
import edu.columbia.cs.cg.relations.RelationshipType;
import edu.columbia.cs.model.Model;
import edu.columbia.cs.og.configuration.StructureConfiguration;
import edu.columbia.cs.og.structure.OperableStructure;

public class PRDualRankModel extends Model {

	private SortedSet<Pattern> sPatterns;
	private SortedSet<Pattern> ePatterns;
	private SortedSet<Relationship> sTuples;
	private SortedSet<Relationship> eTuples;

	public PRDualRankModel(SortedSet<Pattern> searchPatterns, SortedSet<Pattern> extractionPatterns, SortedSet<Relationship> searchTuples, SortedSet<Relationship> extractionTuples) {
		
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

	@Override
	public Set<RelationshipType> getRelationshipTypes() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public StructureConfiguration getStructureConfiguration() {
		// TODO Auto-generated method stub
		return null;
	}

}
