package edu.columbia.cs.evaluation.measures;

import java.util.Map;
import java.util.Map.Entry;

import edu.columbia.cs.cg.relations.RelationshipType;
import edu.columbia.cs.model.Model.PredictionProperties;
import edu.columbia.cs.og.structure.OperableStructure;

public class Accuracy implements Measure {
	
	private Measure correct = new NumberOfCorrectAnswers();

	@Override
	public double getValue(Map<OperableStructure, String> labels,
			Map<OperableStructure, Map<PredictionProperties, Object>> properties) {
		return correct.getValue(labels, properties)/labels.size();
	}

}
