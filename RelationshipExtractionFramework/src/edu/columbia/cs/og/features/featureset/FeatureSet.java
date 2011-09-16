package edu.columbia.cs.og.features.featureset;

import java.io.Serializable;

import edu.columbia.cs.og.structure.OperableStructure;

public abstract class FeatureSet implements Serializable{

	public abstract void enrichMe(OperableStructure operableStructure);
	
}
