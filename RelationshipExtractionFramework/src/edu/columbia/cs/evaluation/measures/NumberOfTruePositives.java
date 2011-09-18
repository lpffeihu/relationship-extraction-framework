package edu.columbia.cs.evaluation.measures;

import java.util.Map;
import java.util.Map.Entry;

import edu.columbia.cs.cg.relations.RelationshipType;
import edu.columbia.cs.model.Model.PredictionProperties;
import edu.columbia.cs.og.structure.OperableStructure;

public class NumberOfTruePositives implements Measure {

	@Override
	public double getValue(Map<OperableStructure, String> labels,
			Map<OperableStructure, Map<PredictionProperties, Object>> properties) {
		int result = 0;
		
		for(Entry<OperableStructure,String> entry : labels.entrySet()){
			String realLabel = entry.getKey().getLabel();
			String predictedLabel = entry.getValue();
			if(!predictedLabel.equals(RelationshipType.NOT_A_RELATIONSHIP) &&
			   realLabel.equals(predictedLabel)){
				result++;
			}
		}
		
		return result;
	}

}
