package edu.columbia.cs.model.impl;

import java.util.HashSet;
import java.util.Set;

import edu.berkeley.compbio.jlibsvm.binary.BinaryModel;
import edu.berkeley.compbio.jlibsvm.multi.MultiClassModel;
import edu.columbia.cs.cg.relations.RelationshipType;
import edu.columbia.cs.model.Model;
import edu.columbia.cs.model.Model.PredictionProperties;
import edu.columbia.cs.og.structure.OperableStructure;

public class JLibsvmMulticlassModelModel extends Model {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5785782738918799493L;
	private transient MultiClassModel<String,OperableStructure> svmModel;
	
	public JLibsvmMulticlassModelModel(MultiClassModel<String, OperableStructure> model) {
		svmModel=model;
	}

	@Override
	protected PredictionProperties[] getAvailablePredictionProperties() {
		return new PredictionProperties[]{};
	}

	@Override
	protected Object getPredictionPropertyValue(
			PredictionProperties predictionProperties) {

		return null;
	}

	@Override
	protected Set<String> getPredictedLabel(OperableStructure s) {
		Set<String> result = new HashSet<String>();
		String label = svmModel.predictLabel(s);
		if(!label.equals(RelationshipType.NOT_A_RELATIONSHIP)){
			result.add(label);
		}
		return result;
	}

}
