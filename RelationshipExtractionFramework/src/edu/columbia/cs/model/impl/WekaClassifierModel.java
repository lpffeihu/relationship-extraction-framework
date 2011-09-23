package edu.columbia.cs.model.impl;

import weka.classifiers.Classifier;
import weka.core.Instance;
import edu.columbia.cs.cg.relations.RelationshipType;
import edu.columbia.cs.model.Model;
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
	protected String getPredictedLabel(OperableStructure s) {
		
		Instance instance = ((WekaInstanceFS)s.getFeatures(OpenInformationExtractionFG.class)).getInstance();
		
		try {
			double classification = classifier.classifyInstance(instance);
			
			if (classification == 0.0)
				return NEGATIVE_LABEL;
			
			return POSITIVE_LABEL;
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		return NEGATIVE_LABEL;
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

}
