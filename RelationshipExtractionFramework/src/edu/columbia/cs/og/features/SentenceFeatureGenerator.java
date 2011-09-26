package edu.columbia.cs.og.features;

import java.util.List;

import edu.columbia.cs.cg.sentence.Sentence;
import edu.columbia.cs.og.features.featureset.FeatureSet;
import edu.columbia.cs.og.structure.OperableStructure;

public abstract class SentenceFeatureGenerator<E extends FeatureSet> extends FeatureGenerator<E> {

	@Override
	protected final E extractFeatures(OperableStructure s) {
		return extractFeatures(s.getCandidateSentence().getSentence());
	}

	protected abstract E extractFeatures(Sentence sentence);
	
	@Override
	protected <E extends FeatureSet> E getFeatures(OperableStructure s,
			FeatureGenerator<E> featureGenerator){
		return s.getCandidateSentence().getSentence().getFeatures(featureGenerator);
	}

	@Override
	protected <E extends FeatureSet> void setFeatures(OperableStructure s,
			FeatureGenerator<E> featureGenerator, E fs){		
		s.getCandidateSentence().getSentence().setFeatures(featureGenerator, fs);
	}

	protected abstract List<FeatureGenerator> retrieveRequiredFeatureGenerators();
}
