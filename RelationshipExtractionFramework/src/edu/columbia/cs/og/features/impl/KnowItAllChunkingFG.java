package edu.columbia.cs.og.features.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.sound.midi.Sequence;

import edu.columbia.cs.cg.sentence.Sentence;
import edu.columbia.cs.og.features.FeatureGenerator;
import edu.columbia.cs.og.features.SentenceFeatureGenerator;
import edu.columbia.cs.og.features.featureset.FeatureSet;
import edu.columbia.cs.og.features.featureset.SequenceFS;
import edu.columbia.cs.og.structure.OperableStructure;
import edu.columbia.cs.utils.Span;
import edu.washington.cs.knowitall.util.DefaultObjects;

public class KnowItAllChunkingFG extends SentenceFeatureGenerator<SequenceFS<String>> {

	private FeatureGenerator<SequenceFS<String>> tokenizer;
	private FeatureGenerator<SequenceFS<String>> posTagger;
	
	public KnowItAllChunkingFG(FeatureGenerator<SequenceFS<String>> tokenizer,
							   FeatureGenerator<SequenceFS<String>> posTagger){
		this.tokenizer = tokenizer;
		this.posTagger = posTagger;
	}
	
	@Override
	protected SequenceFS<String> extractFeatures(Sentence sentence) {
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
