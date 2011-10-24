package edu.columbia.cs.ref.model.re.impl;

import java.util.HashSet;
import java.util.Set;

import weka.classifiers.Classifier;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;
import edu.columbia.cs.ref.algorithm.feature.generation.impl.OpenInformationExtractionFG;
import edu.columbia.cs.ref.model.StructureConfiguration;
import edu.columbia.cs.ref.model.core.Core;
import edu.columbia.cs.ref.model.core.impl.OpenInformationExtractionCore;
import edu.columbia.cs.ref.model.core.structure.OperableStructure;
import edu.columbia.cs.ref.model.core.structure.WekableStructure;
import edu.columbia.cs.ref.model.feature.impl.WekaInstanceFS;
import edu.columbia.cs.ref.model.re.Model;
import edu.columbia.cs.ref.model.relationship.RelationshipType;

public class WekaClassifierModel extends Model {

	public static transient final String POSITIVE_LABEL = "positive";

	public static transient final String NEGATIVE_LABEL = RelationshipType.NOT_A_RELATIONSHIP;
	
	private Classifier classifier;
	
	private String positiveLabel;

	private StructureConfiguration conf;
	
	private Set<RelationshipType> relationshipTypes;
	
	public WekaClassifierModel(Classifier classifier, String positiveLabel, StructureConfiguration conf, Set<RelationshipType> relationshipTypes){
		this.classifier = classifier;
		this.positiveLabel=positiveLabel;
		this.conf=conf;
		this.relationshipTypes=relationshipTypes;
	}
	
	
	@Override
	protected Set<String> getPredictedLabel(OperableStructure s) {
		Set<String> result = new HashSet<String>();
		
		Instance instance = ((WekableStructure)s).getInstance();		
		FastVector attributes = generateAttributeFastVector(instance);
		
		Instances instances = new Instances("test", attributes, 0);
		
		instances.setClassIndex(attributes.size()-1);
		
		instance.setDataset(instances);
		
		try {
			double classification = classifier.classifyInstance(instance);
			if (classification != 0.0){
				result.add(positiveLabel);
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
		return relationshipTypes;
	}


	@Override
	public StructureConfiguration getStructureConfiguration() {
		return conf;
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
