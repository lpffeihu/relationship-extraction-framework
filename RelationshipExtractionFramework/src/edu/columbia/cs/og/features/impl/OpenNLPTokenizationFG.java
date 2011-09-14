package edu.columbia.cs.og.features.impl;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.util.InvalidFormatException;
import opennlp.tools.util.Span;
import edu.columbia.cs.cg.sentence.Sentence;
import edu.columbia.cs.og.features.SentenceFeatureGenerator;
import edu.columbia.cs.og.features.featureset.FeatureSet;
import edu.columbia.cs.og.features.featureset.SequenceFS;

public class OpenNLPTokenizationFG extends SentenceFeatureGenerator {

	private Tokenizer tokenizer;
	
	public OpenNLPTokenizationFG(String path) throws InvalidFormatException, IOException{
		InputStream modelIn = new FileInputStream(path);
		TokenizerModel tokModel = new TokenizerModel(modelIn);
		modelIn.close();
		tokenizer = new TokenizerME(tokModel);
	}
	
	@Override
	protected FeatureSet process(Sentence sentence) {
		String sentenceValue = sentence.getValue();
		String[] tokenSpans = tokenizer.tokenize(sentenceValue);
		
		return new SequenceFS<String>(tokenSpans);
	}
}
