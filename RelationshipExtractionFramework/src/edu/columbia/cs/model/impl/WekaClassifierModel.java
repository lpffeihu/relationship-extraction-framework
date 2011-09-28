package edu.columbia.cs.model.impl;

import java.util.HashSet;
import java.util.Set;

import weka.classifiers.Classifier;
import weka.core.Instance;
import edu.columbia.cs.cg.relations.RelationshipType;
import edu.columbia.cs.model.Model;
import edu.columbia.cs.og.configuration.StructureConfiguration;
import edu.columbia.cs.og.core.Core;
import edu.columbia.cs.og.core.impl.OpenInformationExtractionCore;
import edu.columbia.cs.og.features.featureset.WekaInstanceFS;
import edu.columbia.cs.og.features.impl.OpenInformationExtractionFG;
import edu.columbia.cs.og.structure.OperableStructure;

public class WekaClassifierModel extends Model {

	public static transient final String POSITIVE_LABEL = "positive";

	public static transient final String NEGATIVE_LABEL = RelationshipType.NOT_A_RELATIONSHIP;
	
	private Classifier classifier;

	public WekaClassifierModel(Classifier classifier){
		this.classifier = classifier;
	}
	
	
	@Override
	protected Set<String> getPredictedLabel(OperableStructure s) {
		Set<String> result = new HashSet<String>();
		
		Instance instance = (s.getFeatures(OpenInformationExtractionFG.class)).getInstance();
		
		try {
			double classification = classifier.classifyInstance(instance);
			
			if (classification != 0.0){
				result.add(POSITIVE_LABEL);
				return result;
			}
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		return result;
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
