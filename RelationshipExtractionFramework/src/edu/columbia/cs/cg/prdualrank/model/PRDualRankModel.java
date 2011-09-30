package edu.columbia.cs.cg.prdualrank.model;

import java.util.List;
import java.util.Set;

import edu.columbia.cs.cg.pattern.Pattern;
import edu.columbia.cs.cg.relations.Relationship;
import edu.columbia.cs.cg.relations.RelationshipType;
import edu.columbia.cs.model.Model;
import edu.columbia.cs.og.configuration.StructureConfiguration;
import edu.columbia.cs.og.structure.OperableStructure;

public class PRDualRankModel extends Model {

	private List<Pattern> sPatterns;
	private List<Pattern> ePatterns;
	private List<Relationship> sTuples;
	private List<Relationship> eTuples;

	public PRDualRankModel(List<Pattern> sPatterns, List<Pattern> ePatterns, List<Relationship> sTuples, List<Relationship> eTuples) {
		
		this.sPatterns = sPatterns;
		this.ePatterns = ePatterns;
		this.sTuples = sTuples;
		this.eTuples = eTuples;
		
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
