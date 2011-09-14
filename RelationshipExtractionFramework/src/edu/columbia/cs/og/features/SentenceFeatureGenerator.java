package edu.columbia.cs.og.features;

import edu.columbia.cs.cg.sentence.Sentence;
import edu.columbia.cs.og.features.featureset.FeatureSet;
import edu.columbia.cs.og.structure.OperableStructure;

public abstract class SentenceFeatureGenerator extends FeatureGenerator {

	@Override
	protected final FeatureSet process(OperableStructure s) {
		return process(s.getCandidateSentence().getSentence());
	}

	protected abstract FeatureSet process(Sentence sentence);

	@Override
	protected final FeatureSet getFeatures(OperableStructure s,
			Class<? extends FeatureGenerator> featureGeneratorClass) {
		return s.getCandidateSentence().getSentence().getFeatures(featureGeneratorClass);
	}

	@Override
	protected final void setFeatures(OperableStructure s,
			Class<? extends FeatureGenerator> featureGeneratorClass,
			FeatureSet fs) {
		
		s.getCandidateSentence().getSentence().setFeatures(featureGeneratorClass,fs);
		
	}

}
