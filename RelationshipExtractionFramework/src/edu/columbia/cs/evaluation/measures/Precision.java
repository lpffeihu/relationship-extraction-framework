package edu.columbia.cs.evaluation.measures;

import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import edu.columbia.cs.cg.relations.RelationshipType;
import edu.columbia.cs.model.Model.PredictionProperties;
import edu.columbia.cs.og.structure.OperableStructure;

public class Precision implements Measure {
	
	private Measure truePositives = new NumberOfTruePositives();
	private Measure numberPositives = new NumberOfPositiveAnswers();

	@Override
	public double getValue(Map<OperableStructure, Set<String>> labels,
			Map<OperableStructure, Map<PredictionProperties, Object>> properties) {
		return truePositives.getValue(labels, properties)/numberPositives.getValue(labels, properties);
	}

}
