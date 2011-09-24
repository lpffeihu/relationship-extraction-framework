package edu.columbia.cs.og.features.impl;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.util.InvalidFormatException;
import edu.columbia.cs.cg.sentence.Sentence;
import edu.columbia.cs.og.features.SentenceFeatureGenerator;
import edu.columbia.cs.og.features.featureset.FeatureSet;
import edu.columbia.cs.og.features.featureset.SequenceFS;
import edu.columbia.cs.utils.Span;

public class OpenNLPStringTokenizationFG extends SentenceFeatureGenerator {

	private Tokenizer tokenizer;
	
	public OpenNLPStringTokenizationFG(String path) throws InvalidFormatException, IOException{
		InputStream modelIn = new FileInputStream(path);
		TokenizerModel tokModel = new TokenizerModel(modelIn);
		modelIn.close();
		tokenizer = new TokenizerME(tokModel);
	}
	
	@Override
	protected FeatureSet extractFeatures(Sentence sentence) {
		
		String sentenceValue = sentence.getValue();
				
		return new SequenceFS<String>(tokenizer.tokenize(sentenceValue));
		
	}

}
