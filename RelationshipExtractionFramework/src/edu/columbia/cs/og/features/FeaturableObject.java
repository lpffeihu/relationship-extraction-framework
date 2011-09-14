package edu.columbia.cs.og.features;

import java.util.Hashtable;

import edu.columbia.cs.og.features.featureset.FeatureSet;

public class FeaturableObject {

	private Hashtable<Class<? extends FeatureGenerator>, FeatureSet> featuresTable;

	public FeaturableObject(){
		
		featuresTable = new Hashtable<Class<? extends FeatureGenerator>, FeatureSet>();
		
	}
	
	public FeatureSet getFeatures(Class<? extends FeatureGenerator> featureGeneratorClass){
		
		return featuresTable.get(featureGeneratorClass);
		
	}
	
	 public void setFeatures(Class<? extends FeatureGenerator> featureGeneratorClass, FeatureSet fs){
		 
		 featuresTable.put(featureGeneratorClass, fs);
		 
	 }
	
}
