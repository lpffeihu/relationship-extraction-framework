package edu.columbia.cs.engine.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.tools.ant.filters.StringInputStream;

import weka.classifiers.Classifier;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;

import edu.columbia.cs.engine.Engine;
import edu.columbia.cs.model.Model;
import edu.columbia.cs.model.impl.WekaClassifierModel;
import edu.columbia.cs.og.features.featureset.WekaInstanceFS;
import edu.columbia.cs.og.features.impl.OpenInformationExtractionFG;
import edu.columbia.cs.og.structure.OperableStructure;
import edu.washington.cs.knowitall.extractor.conf.BooleanFeatureSet;
import edu.washington.cs.knowitall.extractor.conf.LabeledBinaryExtraction;
import edu.washington.cs.knowitall.extractor.conf.LabeledBinaryExtractionReader;
import edu.washington.cs.knowitall.extractor.conf.ReVerbFeatures;
import edu.washington.cs.knowitall.extractor.conf.WekaDataSet;
import edu.washington.cs.knowitall.nlp.extraction.ChunkedBinaryExtraction;

public class WekaClassifierEngine implements Engine {

	private Classifier classifier;
	private WekaDataSet<ChunkedBinaryExtraction> dataSet;
	private BooleanFeatureSet<ChunkedBinaryExtraction> featureSet;
	
	public WekaClassifierEngine(Classifier classifier, BooleanFeatureSet<ChunkedBinaryExtraction> featureSet){
		this.classifier = classifier;
		this.featureSet = featureSet;
	}
	@Override
	public Model train(List<OperableStructure> list) {

		Instance sampleInstance = ((WekaInstanceFS)list.get(0).getFeatures(OpenInformationExtractionFG.class)).getInstance();
		
		FastVector attributes = generateAttributeFastVector(sampleInstance);
		
		Instances instances = new Instances("train", attributes, 0);
		
		instances.setClassIndex(featureSet.getNumFeatures());

		for (OperableStructure operableStructure : list) {
		
			Instance instance = ((WekaInstanceFS)operableStructure.getFeatures(OpenInformationExtractionFG.class)).getInstance();
			
			Instance inst = new Instance(instance);
			
			instances.add(inst);
			
			inst.setDataset(instances);
			
		}

		try {
			train(instances, classifier);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return new WekaClassifierModel(classifier);
		
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
	
	/**
	 * @return the data set used to train the classifier
	 */
	public WekaDataSet<ChunkedBinaryExtraction> getDataSet() {
		return dataSet;
	}
	
	/**
	 * @return the trained classifier.
	 */
	public Classifier getClassifier() {
		return classifier;
	}
	
	private void createDataSet(Iterable<LabeledBinaryExtraction> examples, BooleanFeatureSet<ChunkedBinaryExtraction> featureSet) {
		dataSet = new WekaDataSet<ChunkedBinaryExtraction>("train", featureSet);
		for (LabeledBinaryExtraction extr : examples) {
			int label = extr.isPositive() ? 1 : 0;
			dataSet.addInstance(extr, label);
		}
	}

	private void train(Instances instances, Classifier classifier2) throws Exception {
		classifier.buildClassifier(instances);
	}

	
}
