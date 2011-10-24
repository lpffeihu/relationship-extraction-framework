package edu.columbia.cs.ref.algorithm.feature.generation.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.sound.midi.Sequence;

import edu.columbia.cs.ref.algorithm.feature.generation.CandidateSentenceFeatureGenerator;
import edu.columbia.cs.ref.algorithm.feature.generation.FeatureGenerator;
import edu.columbia.cs.ref.algorithm.feature.generation.SentenceFeatureGenerator;
import edu.columbia.cs.ref.model.CandidateSentence;
import edu.columbia.cs.ref.model.Sentence;
import edu.columbia.cs.ref.model.core.structure.OperableStructure;
import edu.columbia.cs.ref.model.feature.FeatureSet;
import edu.columbia.cs.ref.model.feature.impl.SequenceFS;
import edu.columbia.cs.utils.Span;
import edu.washington.cs.knowitall.util.DefaultObjects;

public class KnowItAllChunkingFG extends CandidateSentenceFeatureGenerator<SequenceFS<String>> {

	private FeatureGenerator<SequenceFS<String>> tokenizer;
	private FeatureGenerator<SequenceFS<String>> posTagger;
	
	public KnowItAllChunkingFG(FeatureGenerator<SequenceFS<String>> tokenizer,
							   FeatureGenerator<SequenceFS<String>> posTagger){
		this.tokenizer = tokenizer;
		this.posTagger = posTagger;
	}
	
	@Override
	protected SequenceFS<String> extractFeatures(CandidateSentence sentence) {
		// TODO Auto-generated method stub

		SequenceFS<String> tokens = sentence.getFeatures(tokenizer); 
		SequenceFS<String> pos = sentence.getFeatures(posTagger);
		
		String[] tkns = generateArray(tokens);
		String[] post = generateArray(pos);
		
		try {
			String[] chunk = DefaultObjects.getDefaultChunker().chunk(tkns,post);
			return new SequenceFS<String>(chunk);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(1);
		}
		
		return null;
	}

	private String[] generateArray(SequenceFS<String> tokens) {
		
		String[] ret = new String[tokens.size()];
		
		for (int i = 0; i < ret.length; i++) {
			
			ret[i] = tokens.getElement(i);
			
		}
		
		return ret;
		
	}

	@Override
	protected List<FeatureGenerator> retrieveRequiredFeatureGenerators() {
		ArrayList<FeatureGenerator> ret = new ArrayList<FeatureGenerator>();
		
		ret.add(tokenizer);
		ret.add(posTagger);
	
		return ret;
	}


}
