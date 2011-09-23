package edu.columbia.cs.model;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import edu.columbia.cs.og.structure.OperableStructure;

public abstract class Model implements Serializable{
	public enum PredictionProperties {
		CONFIDENCE,
		PROBABILITY_POSITIVE,
		PROBABILITY_NEGATIVE
	}
	
	private Map<OperableStructure,Map<PredictionProperties,Object>> currentPredictionProperties =
		new HashMap<OperableStructure,Map<PredictionProperties,Object>>();
	
	protected abstract String getPredictedLabel(OperableStructure s);
	
	public String predictLabel(OperableStructure s){
		String result = getPredictedLabel(s);
		PredictionProperties[] pprops = getAvailablePredictionProperties();
		
		for (int i = 0; i < pprops.length; i++) {
			PredictionProperties prop = pprops[i];
			savePredictionPropertyValue(s,prop,getPredictionPropertyValue(prop));
		}
		
		return result;
	}
	
	
	public Map<PredictionProperties,Object> obtainPredictionPropertyValue(OperableStructure s){
		Map<PredictionProperties,Object> result = currentPredictionProperties.get(s);
		if(result==null){
			predictLabel(s);
			return currentPredictionProperties.get(s);
		}
		return result;
	}
	
	protected void savePredictionPropertyValue(
			OperableStructure os,
			PredictionProperties predictionProperties,
			Object predictionPropertyValue) {
		Map<PredictionProperties,Object> props = currentPredictionProperties.get(os);
		if(props==null){
			props=new HashMap<PredictionProperties,Object>();
		}
		props.put(predictionProperties, predictionPropertyValue);
		currentPredictionProperties.put(os, props);
	}
	
	protected abstract Object getPredictionPropertyValue(
			PredictionProperties predictionProperties);
	protected abstract PredictionProperties[] getAvailablePredictionProperties();
}

