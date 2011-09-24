package edu.columbia.cs.og.features.impl;

import java.io.IOException;

import javax.sound.midi.Sequence;

import edu.columbia.cs.cg.sentence.Sentence;
import edu.columbia.cs.og.features.FeatureGenerator;
import edu.columbia.cs.og.features.SentenceFeatureGenerator;
import edu.columbia.cs.og.features.featureset.FeatureSet;
import edu.columbia.cs.og.features.featureset.SequenceFS;
import edu.columbia.cs.og.structure.OperableStructure;
import edu.washington.cs.knowitall.util.DefaultObjects;

public class KnowItAllChunkingFG extends SentenceFeatureGenerator<SequenceFS<String>> {

	@Override
	protected SequenceFS<String> extractFeatures(Sentence sentence) {
		// TODO Auto-generated method stub

		SequenceFS<String> tokens = (SequenceFS<String>)sentence.getFeatures(OpenNLPStringTokenizationFG.class); 
		SequenceFS<String> pos = (SequenceFS<String>)sentence.getFeatures(OpenNLPPartOfSpeechFG.class);
		
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


}
