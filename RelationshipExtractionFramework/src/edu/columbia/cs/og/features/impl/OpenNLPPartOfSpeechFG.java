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
import edu.columbia.cs.og.features.FeatureGenerator;
import edu.columbia.cs.og.features.SentenceFeatureGenerator;
import edu.columbia.cs.og.features.featureset.FeatureSet;
import edu.columbia.cs.og.features.featureset.SequenceFS;
import edu.columbia.cs.og.structure.OperableStructure;
import edu.columbia.cs.utils.Span;

public class OpenNLPPartOfSpeechFG extends CandidateSentenceFeatureGenerator<SequenceFS<String>> {

	private POSTaggerME tagger;
	private FeatureGenerator<SequenceFS<Span>> tokenizer;

	public OpenNLPPartOfSpeechFG(String path,FeatureGenerator<SequenceFS<Span>> tokenizer) throws InvalidFormatException, IOException{
		InputStream modelIn = null;
		POSModel modelPOS=null;
		modelIn = new FileInputStream(path);
		modelPOS = new POSModel(modelIn);
		modelIn.close();
		tagger = new POSTaggerME(modelPOS);
		this.tokenizer = tokenizer;
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
	protected SequenceFS<String> extractFeatures(CandidateSentence sentence) {
		
		SequenceFS<Span> tokenization = (SequenceFS<Span>) sentence.getFeatures(tokenizer.getClass());

		String[] tokens = getTokens(tokenization, sentence);

		return new SequenceFS<String>(tagger.tag(tokens));
	}

	@Override
	protected List<FeatureGenerator> retrieveRequiredFeatureGenerators() {
		ArrayList<FeatureGenerator> ret = new ArrayList<FeatureGenerator>();
		
		ret.add(tokenizer);
	
		return ret;
	}
}
