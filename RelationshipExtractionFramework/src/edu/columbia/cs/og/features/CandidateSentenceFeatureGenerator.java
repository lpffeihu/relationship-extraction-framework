package edu.columbia.cs.og.features;

import edu.columbia.cs.cg.candidates.CandidateSentence;
import edu.columbia.cs.og.features.featureset.FeatureSet;
import edu.columbia.cs.og.structure.OperableStructure;

public abstract class CandidateSentenceFeatureGenerator extends FeatureGenerator {

	@Override
	protected FeatureSet process(OperableStructure s) {
		return process(s.getCandidateSentence());
	}

	protected abstract FeatureSet process(CandidateSentence candidateSentence);

	@Override
	protected final FeatureSet getFeatures(OperableStructure s,
			Class<? extends FeatureGenerator> featureGeneratorClass) {
		return s.getCandidateSentence().getFeatures(featureGeneratorClass);
	}

	@Override
	protected final void setFeatures(OperableStructure s,
			Class<? extends FeatureGenerator> featureGeneratorClass,
			FeatureSet fs) {
		
		s.getCandidateSentence().setFeatures(featureGeneratorClass, fs);
		
	}

}
