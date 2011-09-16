package edu.columbia.cs.og.features.impl;

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
import edu.columbia.cs.cg.candidates.CandidateSentence;
import edu.columbia.cs.cg.relations.Entity;
import edu.columbia.cs.cg.sentence.Sentence;
import edu.columbia.cs.og.features.CandidateSentenceFeatureGenerator;
import edu.columbia.cs.og.features.SentenceFeatureGenerator;
import edu.columbia.cs.og.features.featureset.FeatureSet;
import edu.columbia.cs.og.features.featureset.SequenceFS;
import edu.columbia.cs.og.structure.OperableStructure;
import edu.columbia.cs.utils.Span;

public class OpenNLPPartOfSpeechFG extends CandidateSentenceFeatureGenerator {

	private POSTaggerME tagger;

	public OpenNLPPartOfSpeechFG(String path) throws InvalidFormatException, IOException{
		InputStream modelIn = null;
		POSModel modelPOS=null;
		modelIn = new FileInputStream(path);
		modelPOS = new POSModel(modelIn);
		modelIn.close();
		tagger = new POSTaggerME(modelPOS);
	}
	
	private String[] getTokens(SequenceFS<Span> spans, CandidateSentence sentence){
		String value = sentence.getSentence().getValue();
		String[] tokens = new String[spans.size()];
		
		for(int i=0; i<spans.size(); i++){
			Span s = spans.getElement(i);
			tokens[i]=value.substring(s.getStart(),s.getEnd());
		}
		
		return tokens;
	}

	@Override
	protected FeatureSet process(CandidateSentence sentence) {
		SequenceFS<Span> tokenization = (SequenceFS<Span>) sentence.getFeatures(EntityBasedChunkingFG.class);

		String[] tokens = getTokens(tokenization, sentence);

		return new SequenceFS<String>(tagger.tag(tokens));
	}
}
