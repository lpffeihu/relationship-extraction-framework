package edu.columbia.cs.og.features;

import edu.columbia.cs.cg.sentence.Sentence;
import edu.columbia.cs.og.structure.OperableStructure;

public abstract class FeatureGenerator {
	public abstract void generateFeatures(OperableStructure s);
	protected abstract void process(OperableStructure s);
}
