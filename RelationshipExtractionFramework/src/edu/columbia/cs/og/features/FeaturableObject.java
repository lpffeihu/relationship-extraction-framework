package edu.columbia.cs.og.features;

import java.util.Hashtable;

import edu.columbia.cs.og.features.featureset.FeatureSet;

public class FeaturableObject {

	private Hashtable<Class<? extends FeatureGenerator>, FeatureSet> featuresTable;

	public FeaturableObject(){

		featuresTable = new Hashtable<Class<? extends FeatureGenerator>, FeatureSet>();

	}

	public <E extends FeatureSet> E getFeatures(FeatureGenerator<E> featureGenerator){
		return (E) featuresTable.get(featureGenerator.getClass());
	}

	public <E extends FeatureSet> void setFeatures(FeatureGenerator<E> featureGenerator, E fs){
		featuresTable.put(featureGenerator.getClass(), fs);
	}

}
