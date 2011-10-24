package edu.columbia.cs.ref.model.feature.impl;

import weka.core.Instance;
import edu.columbia.cs.ref.model.core.structure.OperableStructure;
import edu.columbia.cs.ref.model.feature.FeatureSet;

public class WekaInstanceFS extends FeatureSet {

	private Instance instance;

	public WekaInstanceFS(Instance instance) {
		this.instance = instance;
	}

	@Override
	public void enrichMe(OperableStructure operableStructure) {
		operableStructure.add(this);
	}

	public Instance getInstance(){
		return instance;
	}
}
