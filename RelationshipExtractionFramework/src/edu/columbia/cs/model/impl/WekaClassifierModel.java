package edu.columbia.cs.model.impl;

import java.io.IOException;

import weka.classifiers.Classifier;
import weka.core.SerializationHelper;

import edu.columbia.cs.model.Model;
import edu.columbia.cs.og.structure.OperableStructure;

public class WekaClassifierModel extends Model {

	private Classifier classifier;

	public WekaClassifierModel(Classifier classifier){
		this.classifier = classifier;
	}
	
	@Override
	protected String getPredictedLabel(OperableStructure s) {
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
	public void saveModel(String path) throws IOException {
		try {
			SerializationHelper.write(path, classifier);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
