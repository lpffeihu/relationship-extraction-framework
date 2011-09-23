package edu.columbia.cs.og.features.featureset;

import weka.core.Instance;
import edu.columbia.cs.og.structure.OperableStructure;

public class WekaInstanceFS extends FeatureSet {

	private Instance instance;

	public WekaInstanceFS(Instance instance) {
		this.instance = instance;
	}

	@Override
	public void enrichMe(OperableStructure operableStructure) {
		operableStructure.add(this);
	}

}
