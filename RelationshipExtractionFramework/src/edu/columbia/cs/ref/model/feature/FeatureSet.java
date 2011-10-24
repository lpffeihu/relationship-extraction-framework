package edu.columbia.cs.ref.model.feature;

import java.io.Serializable;

import edu.columbia.cs.ref.model.core.structure.OperableStructure;

public abstract class FeatureSet implements Serializable{

	public abstract void enrichMe(OperableStructure operableStructure);
	
}
