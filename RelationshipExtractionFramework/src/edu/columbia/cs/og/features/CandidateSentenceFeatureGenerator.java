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
	protected <E extends FeatureSet> E getFeatures(OperableStructure s,
			FeatureGenerator<E> featureGenerator){
		return s.getCandidateSentence().getFeatures(featureGenerator);
	}

	@Override
	protected <E extends FeatureSet> void setFeatures(OperableStructure s,
			FeatureGenerator<E> featureGenerator, E fs){		
		s.getCandidateSentence().setFeatures(featureGenerator, fs);
	}

	protected abstract List<FeatureGenerator> retrieveRequiredFeatureGenerators();
}
