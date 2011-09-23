package edu.columbia.cs.model.impl;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Set;

import cern.colt.Arrays;

import edu.berkeley.compbio.jlibsvm.ImmutableSvmParameterPoint;
import edu.berkeley.compbio.jlibsvm.SolutionModel;
import edu.berkeley.compbio.jlibsvm.binary.BinaryModel;
import edu.columbia.cs.cg.candidates.CandidateSentence;
import edu.columbia.cs.model.Model;
import edu.columbia.cs.og.structure.OperableStructure;

public class JLibsvmBinaryModel extends Model{
	private static final PredictionProperties[] availableStatistics = new PredictionProperties[]{PredictionProperties.CONFIDENCE,PredictionProperties.PROBABILITY_POSITIVE};
	private transient BinaryModel<String,OperableStructure> svmModel;
	private double lastPositiveProbability;
	private double lastNegativeProbability;
	private double lastConfidence;
	private String trueLabel;
	private String falseLabel;
	
	public JLibsvmBinaryModel(){
		
	}
	
	public JLibsvmBinaryModel(BinaryModel<String,OperableStructure> binary){
		svmModel=binary;
		trueLabel=svmModel.getTrueLabel();
		falseLabel=svmModel.getFalseLabel();
	}
	
	@Override
	public String getPredictedLabel(OperableStructure s) {
		float predictedLabel=svmModel.predictValue(s);
		//String predictedLabel=svmModel.predictLabel(s);
		
		//lastPositiveProbability=svmModel.getTrueProbability(s);
		//lastNegativeProbability=1-lastPositiveProbability;
		//lastConfidence = Math.max(lastPositiveProbability, lastNegativeProbability);
		
		return predictedLabel>0 ? trueLabel : falseLabel;
		//return predictedLabel;
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

	private void writeObject(ObjectOutputStream out) throws IOException{
		out.defaultWriteObject();
		//svmModel.compact();
		int numSvs= svmModel.numSVs;
		Object[] objs = svmModel.SVs;
		OperableStructure[] svs = new OperableStructure[numSvs];
		for(int i=0; i<numSvs; i++){
			svs[i]=(OperableStructure) objs[i];
		}
		JLibsvmModelInformation modelInfo = new JLibsvmModelInformation(svmModel.numSVs, svs, svmModel.alphas, svmModel.rho,svmModel.param.kernel,
				trueLabel,falseLabel);
		out.writeObject(modelInfo);
	}
	
	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException{
		in.defaultReadObject();
		BinaryModel<String,OperableStructure> model = new BinaryModel<String,OperableStructure>();
		JLibsvmModelInformation modelInfo = (JLibsvmModelInformation)in.readObject();
		
		model.alphas=modelInfo.getAlpha();
		model.numSVs=modelInfo.getNumSV();
		model.rho=modelInfo.getRho();
		model.SVs=modelInfo.getSupportVectors();
		ImmutableSvmParameterPoint.Builder<String, OperableStructure> builder = new ImmutableSvmParameterPoint.Builder<String, OperableStructure>();
		builder.kernel = modelInfo.getKernel();
		model.param = builder.build();
		
		svmModel=model;
		trueLabel=modelInfo.getTrueLabel();
		falseLabel=modelInfo.getFalseLabel();
	}
}
