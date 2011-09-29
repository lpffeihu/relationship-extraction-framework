package edu.columbia.cs.engine.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Set;

import org.apache.tools.ant.filters.StringInputStream;

import weka.classifiers.Classifier;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;

import edu.columbia.cs.cg.relations.RelationshipType;
import edu.columbia.cs.engine.Engine;
import edu.columbia.cs.model.Model;
import edu.columbia.cs.model.impl.WekaClassifierModel;
import edu.columbia.cs.og.configuration.StructureConfiguration;
import edu.columbia.cs.og.features.featureset.WekaInstanceFS;
import edu.columbia.cs.og.features.impl.OpenInformationExtractionFG;
import edu.columbia.cs.og.structure.OperableStructure;
import edu.columbia.cs.og.structure.WekableStructure;
import edu.washington.cs.knowitall.extractor.conf.BooleanFeatureSet;
import edu.washington.cs.knowitall.extractor.conf.LabeledBinaryExtraction;
import edu.washington.cs.knowitall.extractor.conf.LabeledBinaryExtractionReader;
import edu.washington.cs.knowitall.extractor.conf.ReVerbFeatures;
import edu.washington.cs.knowitall.extractor.conf.WekaDataSet;
import edu.washington.cs.knowitall.nlp.extraction.ChunkedBinaryExtraction;

public class WekaClassifierEngine implements Engine {

	private Classifier classifier;
	
	private BooleanFeatureSet<ChunkedBinaryExtraction> featureSet;
	
	private StructureConfiguration conf;
	private Set<RelationshipType> relationshipTypes;
	
	public WekaClassifierEngine(Classifier classifier, BooleanFeatureSet<ChunkedBinaryExtraction> featureSet, StructureConfiguration conf, Set<RelationshipType> relationshipTypes){
	
		this.classifier = classifier;
		
		this.featureSet = featureSet;
		
		this.conf=conf;
		
		this.relationshipTypes=relationshipTypes;
	}
	@Override
	public Model train(List<OperableStructure> list) {

		for(RelationshipType type : relationshipTypes){
			
			Instance sampleInstance = ((WekableStructure)list.get(0)).getInstance();
			
			FastVector attributes = generateAttributeFastVector(sampleInstance);
			
			Instances instances = new Instances("train", attributes, 0);
			
			instances.setClassIndex(featureSet.getNumFeatures());
	
			for (OperableStructure operableStructure : list) {
			
				Instance instance = ((WekableStructure)operableStructure).getInstance();
				
				instance.setValue((Attribute)attributes.elementAt(featureSet.getNumFeatures()), generateLabel(type,operableStructure));
				
				Instance inst = new Instance(instance);
				
				instances.add(inst);
				
				inst.setDataset(instances);
			}
	
			try {
				classifier.buildClassifier(instances);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return new WekaClassifierModel(classifier, type.getType(), conf, relationshipTypes);
		}
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
