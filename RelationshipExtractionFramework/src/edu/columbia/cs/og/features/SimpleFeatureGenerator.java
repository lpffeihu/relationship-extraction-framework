package edu.columbia.cs.og.features;

import edu.columbia.cs.cg.sentence.Sentence;
import edu.columbia.cs.og.structure.OperableStructure;

public abstract class SimpleFeatureGenerator extends FeatureGenerator {

	@Override
	public void generateFeatures(OperableStructure s) {
		process(s);
	}
}
