package edu.columbia.cs.engine.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.tools.ant.filters.StringInputStream;

import weka.classifiers.Classifier;

import edu.columbia.cs.engine.Engine;
import edu.columbia.cs.model.Model;
import edu.columbia.cs.model.impl.WekaClassifierModel;
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
	
	public WekaClassifierEngine(Classifier classifier){
		this.classifier = classifier;
	}
	@Override
	public Model train(List<OperableStructure> list) {
		
		StringBuilder sb = new StringBuilder();
		
		if (list.size()>0){
			sb.append(list.get(0).toString());
		}
		
		for (int i = 1; i < list.size(); i++) {
			
			sb.append(list.get(i).toString());
			
		}
		
		InputStream in = new StringInputStream(sb.toString());
		
		LabeledBinaryExtractionReader reader;
		
		try {
			
			reader = new LabeledBinaryExtractionReader(in);
			
			trainClassifier(reader.readExtractions(), classifier);
		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return new WekaClassifierModel(classifier);
		
	}

	private void trainClassifier(Iterable<LabeledBinaryExtraction> examples, Classifier classifier) throws Exception {
		ReVerbFeatures feats = new ReVerbFeatures();
		createDataSet(examples,feats.getFeatureSet());
		this.classifier = classifier;
		train();
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

	private void train() throws Exception {
		classifier.buildClassifier(dataSet.getWekaInstances());
	}

	
}
