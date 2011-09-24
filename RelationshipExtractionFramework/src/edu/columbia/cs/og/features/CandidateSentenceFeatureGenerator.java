package edu.columbia.cs.og.features;

import java.util.List;

import edu.columbia.cs.cg.candidates.CandidateSentence;
import edu.columbia.cs.og.features.featureset.FeatureSet;
import edu.columbia.cs.og.structure.OperableStructure;

public abstract class CandidateSentenceFeatureGenerator<E extends FeatureSet> extends FeatureGenerator<E> {

	@Override
	protected final E extractFeatures(OperableStructure s) {
		return extractFeatures(s.getCandidateSentence());
	}

	protected abstract E extractFeatures(CandidateSentence candidateSentence);

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

	protected abstract List<FeatureGenerator> retrieveRequiredFeatureGenerators();
}
