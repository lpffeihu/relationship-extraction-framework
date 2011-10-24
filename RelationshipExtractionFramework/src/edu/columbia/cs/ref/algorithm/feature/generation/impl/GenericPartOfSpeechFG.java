package edu.columbia.cs.ref.algorithm.feature.generation.impl;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.util.InvalidFormatException;
import edu.columbia.cs.ref.algorithm.feature.generation.CandidateSentenceFeatureGenerator;
import edu.columbia.cs.ref.algorithm.feature.generation.FeatureGenerator;
import edu.columbia.cs.ref.algorithm.feature.generation.SentenceFeatureGenerator;
import edu.columbia.cs.ref.model.CandidateSentence;
import edu.columbia.cs.ref.model.Sentence;
import edu.columbia.cs.ref.model.core.structure.OperableStructure;
import edu.columbia.cs.ref.model.entity.Entity;
import edu.columbia.cs.ref.model.feature.FeatureSet;
import edu.columbia.cs.ref.model.feature.impl.SequenceFS;
import edu.columbia.cs.utils.POStoGenericPOSConverter;
import edu.columbia.cs.utils.Span;

public class GenericPartOfSpeechFG extends CandidateSentenceFeatureGenerator<SequenceFS<String>> {

	private FeatureGenerator<SequenceFS<String>> posTagger;
	
	public GenericPartOfSpeechFG(FeatureGenerator<SequenceFS<String>> posTagger){
		this.posTagger = posTagger;
	}
	
	@Override
	protected SequenceFS<String> extractFeatures(CandidateSentence sentence) {
		SequenceFS<String> pos = sentence.getFeatures(posTagger);

		String[] gpos = new String[pos.size()];
		for(int i=0; i<pos.size(); i++){
			gpos[i]=POStoGenericPOSConverter.convertPOS(pos.getElement(i));
		}

		return new SequenceFS<String>(gpos);
	}

	@Override
	protected List<FeatureGenerator> retrieveRequiredFeatureGenerators() {
		ArrayList<FeatureGenerator> ret = new ArrayList<FeatureGenerator>();
		
		ret.add(posTagger);
	
		return ret;
	}
}
