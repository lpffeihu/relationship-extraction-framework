package edu.columbia.cs.evaluation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.columbia.cs.evaluation.measures.Measure;
import edu.columbia.cs.model.Model;
import edu.columbia.cs.model.Model.PredictionProperties;
import edu.columbia.cs.og.structure.OperableStructure;

public class Evaluator {
	private List<Measure> measures = new ArrayList<Measure>();
	
	private Map<Measure,Double> results = new HashMap<Measure,Double>();
	
	public void addMeasure(Measure measure){
		measures.add(measure);
	}
	
	public double getResult(Measure m){
		return results.get(m);
	}
	
	public void printEvaluationReport(List<OperableStructure> testingFiles, Model m){
		Map<OperableStructure, String> labels = new HashMap<OperableStructure, String>();
		Map<OperableStructure, Map<PredictionProperties, Object>> properties = new HashMap<OperableStructure, Map<PredictionProperties, Object>>();
		
		int testingFilesSize=testingFiles.size();
		for(int i=0; i<testingFilesSize; i++){
			OperableStructure s = testingFiles.get(i);
			String trueLabel = s.getLabel();
			String predicted = m.predictLabel(s);
			System.out.println("i=" + i + ": [" + predicted + "," + trueLabel + "]");
			Map<PredictionProperties, Object> props = m.obtainPredictionPropertyValue(s);
			
			labels.put(s, predicted);
			properties.put(s, props);
		}
		
		for(Measure measure : measures){
			double result = measure.getValue(labels, properties);
			results.put(measure, result);
			System.out.println(measure + ": " + result);
		}
	}
}
