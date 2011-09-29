package edu.columbia.cs.model.impl;

import java.util.HashSet;
import java.util.Set;

import weka.classifiers.Classifier;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;
import edu.columbia.cs.cg.relations.RelationshipType;
import edu.columbia.cs.model.Model;
import edu.columbia.cs.og.configuration.StructureConfiguration;
import edu.columbia.cs.og.core.Core;
import edu.columbia.cs.og.core.impl.OpenInformationExtractionCore;
import edu.columbia.cs.og.features.featureset.WekaInstanceFS;
import edu.columbia.cs.og.features.impl.OpenInformationExtractionFG;
import edu.columbia.cs.og.structure.OperableStructure;
import edu.columbia.cs.og.structure.WekableStructure;

public class WekaClassifierModel extends Model {

	public static transient final String POSITIVE_LABEL = "positive";

	public static transient final String NEGATIVE_LABEL = RelationshipType.NOT_A_RELATIONSHIP;
	
	private Classifier classifier;

	public WekaClassifierModel(Classifier classifier, String positiveLabel){
		this.classifier = classifier;
	}
	
	
	@Override
	protected Set<String> getPredictedLabel(OperableStructure s) {
		Set<String> result = new HashSet<String>();
		
		Instance instance = ((WekableStructure)s).getInstance();		
		FastVector attributes = generateAttributeFastVector(instance);
		
		Instances instances = new Instances("train", attributes, 0);
		
		instances.setClassIndex(22);
		
		instance.setDataset(instances);
		
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
		return new PredictionProperties[]{};
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
	
	private String generateLabel(RelationshipType type, OperableStructure operableStructure) {
		Set<String> labels = operableStructure.getLabels();
		if(labels.contains(type.getType())){
			return WekaClassifierModel.POSITIVE_LABEL;
		}
		return WekaClassifierModel.NEGATIVE_LABEL;
	}
	private FastVector generateAttributeFastVector(
			Instance sampleInstance) {
		
		int numFeatures = sampleInstance.numAttributes();

		FastVector attributes = new FastVector(numFeatures); // has space for class attribute already 
		// Construct a numeric attribute for each feature in the set
		for (int i = 0; i < numFeatures; i++) {

			attributes.addElement(sampleInstance.attribute(i));

		}
		
		return attributes;

	}

}
