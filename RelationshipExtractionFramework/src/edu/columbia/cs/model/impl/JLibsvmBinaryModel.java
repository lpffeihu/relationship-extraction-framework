package edu.columbia.cs.model.impl;

import java.io.IOException;

import edu.berkeley.compbio.jlibsvm.SolutionModel;
import edu.berkeley.compbio.jlibsvm.binary.BinaryModel;
import edu.columbia.cs.model.Model;
import edu.columbia.cs.og.structure.OperableStructure;

public class JLibsvmBinaryModel extends Model{
	private static final PredictionProperties[] availableStatistics = new PredictionProperties[]{PredictionProperties.CONFIDENCE,PredictionProperties.PROBABILITY_POSITIVE};
	private BinaryModel<String,OperableStructure> svmModel;
	private double lastPositiveProbability;
	private double lastNegativeProbability;
	private double lastConfidence;
	
	public JLibsvmBinaryModel(BinaryModel<String,OperableStructure> binary){
		svmModel=binary;
	}
	
	@Override
	public String getPredictedLabel(OperableStructure s) {
		String predictedLabel=svmModel.predictLabel(s);
		
		lastPositiveProbability=svmModel.getTrueProbability(s);
		lastNegativeProbability=1-lastPositiveProbability;
		lastConfidence = Math.max(lastPositiveProbability, lastNegativeProbability);
		
		return predictedLabel;
	}

	@Override
	protected PredictionProperties[] getAvailablePredictionProperties() {
		return availableStatistics;
	}

	@Override
	protected Object getPredictionPropertyValue(
			PredictionProperties predictionProperties) {
		
		switch (predictionProperties) {
		case CONFIDENCE:
			
			return getConfidenceValue();
			
		case PROBABILITY_POSITIVE:
			
			return getProbability_Positive();
			
		//case PROBABILITY_NEGATIVE:
			
		//	return getProbability_Negative();
			
		default:
			return null;
		}
		
	}

	private Object getProbability_Positive() {
		return lastPositiveProbability;
	}
	
	private Object getProbability_Negative() {
		return lastNegativeProbability;
	}

	private Object getConfidenceValue() {
		return lastConfidence;
	}

	@Override
	public void saveModel(String path) throws IOException {
		svmModel.save(path);
	}
	
	public static JLibsvmBinaryModel loadModel(String path){
		return new JLibsvmBinaryModel((BinaryModel<String,OperableStructure>)SolutionModel.identifyTypeAndLoad(path));
	}
}
