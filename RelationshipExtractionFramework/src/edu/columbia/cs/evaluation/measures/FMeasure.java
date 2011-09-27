package edu.columbia.cs.evaluation.measures;

import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import edu.columbia.cs.cg.relations.RelationshipType;
import edu.columbia.cs.model.Model.PredictionProperties;
import edu.columbia.cs.og.structure.OperableStructure;

public class FMeasure implements Measure {
	private Measure recall = new Recall();
	private Measure precision = new Precision();
	private double beta;
	
	public FMeasure(double beta){
		this.beta=beta;
	}

	@Override
	public double getValue(Map<OperableStructure, Set<String>> labels,
			Map<OperableStructure, Map<PredictionProperties, Object>> properties) {
		double recall = this.recall.getValue(labels, properties);
		double precision = this.precision.getValue(labels, properties);
		
		double normalization = (1+beta*beta);
		double result = (recall*precision)/((beta*beta*precision)+recall);
		return normalization*result;
	}

}
