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

public class OpenNLPTokenizationFG extends SentenceFeatureGenerator {

	private Tokenizer tokenizer;
	
	public OpenNLPTokenizationFG(String path) throws InvalidFormatException, IOException{
		InputStream modelIn = new FileInputStream(path);
		TokenizerModel tokModel = new TokenizerModel(modelIn);
		modelIn.close();
		tokenizer = new TokenizerME(tokModel);
	}
	
	private Span[] convertSpans(opennlp.tools.util.Span[] spans){
		int size=spans.length;
		Span[] result = new Span[size];
		for(int i=0; i<size; i++){
			result[i]= new Span(spans[i]);
		}
		return result;
	}
	
	@Override
	protected FeatureSet process(Sentence sentence) {
		String sentenceValue = sentence.getValue();
		Span[] tokenSpans = convertSpans(tokenizer.tokenizePos(sentenceValue));
		
		return new SequenceFS<Span>(tokenSpans);
	}
}
