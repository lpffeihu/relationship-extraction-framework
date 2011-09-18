package edu.columbia.cs.evaluation.measures;

import java.util.Map;

import edu.columbia.cs.model.Model.PredictionProperties;
import edu.columbia.cs.og.structure.OperableStructure;

public interface Measure {
	public double getValue(Map<OperableStructure,String> labels,
			Map<OperableStructure,Map<PredictionProperties,Object>> properties);
}
