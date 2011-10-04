package edu.columbia.cs.utils;

import java.io.IOException;

import weka.classifiers.Classifier;
import edu.washington.cs.knowitall.extractor.conf.BooleanFeatureSet;
import edu.washington.cs.knowitall.extractor.conf.ConfidenceFunctionException;
import edu.washington.cs.knowitall.extractor.conf.ReVerbFeatures;
import edu.washington.cs.knowitall.extractor.conf.WekaClassifierConfFunction;
import edu.washington.cs.knowitall.nlp.extraction.ChunkedBinaryExtraction;
import edu.washington.cs.knowitall.util.DefaultObjects;

public class AlternativeOpenIEConfFunction {
	private Classifier classifier;
	private AlternativeOpenIEFeatures reverbFeatures;
	private BooleanFeatureSet<ChunkedBinaryExtraction> featureSet;
	private WekaClassifierConfFunction<ChunkedBinaryExtraction> func;
	
	public AlternativeOpenIEConfFunction() throws IOException {
		classifier = DefaultObjects.getDefaultConfClassifier();
		reverbFeatures = new AlternativeOpenIEFeatures();
		featureSet = reverbFeatures.getFeatureSet();
		func = new WekaClassifierConfFunction<ChunkedBinaryExtraction>(featureSet, classifier);
	}

	public AlternativeOpenIEConfFunction(Classifier c) throws IOException {
		classifier = c;
		reverbFeatures = new AlternativeOpenIEFeatures();
		featureSet = reverbFeatures.getFeatureSet();
		func = new WekaClassifierConfFunction<ChunkedBinaryExtraction>(featureSet, classifier);
	}
	

	public double getConf(ChunkedBinaryExtraction extr) throws ConfidenceFunctionException {
        try {
            return func.getConf(extr);
        }
        catch (Exception e) {
            throw new ConfidenceFunctionException(e);
        }
	}
}
