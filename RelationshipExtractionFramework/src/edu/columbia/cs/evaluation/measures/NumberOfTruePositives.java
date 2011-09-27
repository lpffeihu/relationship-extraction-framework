package edu.columbia.cs.evaluation.measures;

import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import edu.columbia.cs.cg.relations.RelationshipType;
import edu.columbia.cs.model.Model.PredictionProperties;
import edu.columbia.cs.og.structure.OperableStructure;

public class NumberOfTruePositives implements Measure {

	@Override
	public double getValue(Map<OperableStructure, Set<String>> labels,
			Map<OperableStructure, Map<PredictionProperties, Object>> properties) {
		int result = 0;
		
		for(Entry<OperableStructure,Set<String>> entry : labels.entrySet()){
			Set<String> realLabel = entry.getKey().getLabels();
			Set<String> predictedLabel = entry.getValue();
			for(String predicted : predictedLabel){
				if(realLabel.contains(predicted)){
					result++;
				}
			}
		}
		
		return result;
	}

}
