package edu.columbia.cs.og.features.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import opennlp.tools.util.InvalidFormatException;
import edu.columbia.cs.cg.candidates.CandidateSentence;
import edu.columbia.cs.og.features.CandidateSentenceFeatureGenerator;
import edu.columbia.cs.og.features.FeatureGenerator;
import edu.columbia.cs.og.features.featureset.SequenceFS;
import edu.columbia.cs.utils.Span;

public class SpansToStringsConvertionFG extends CandidateSentenceFeatureGenerator<SequenceFS<String>> {

	private FeatureGenerator<SequenceFS<Span>> tokenizer;
	
	public SpansToStringsConvertionFG(FeatureGenerator<SequenceFS<Span>> tokenizer) throws InvalidFormatException, IOException{
		this.tokenizer = tokenizer;
	}
	
	@Override
	protected SequenceFS<String> extractFeatures(CandidateSentence sentence) {
		
		SequenceFS<Span> spans = sentence.getSentence().getFeatures(tokenizer);
		
		return new SequenceFS<String>(getTokens(spans, sentence));
		
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
	protected List<FeatureGenerator> retrieveRequiredFeatureGenerators() {
		
		ArrayList<FeatureGenerator> ret = new ArrayList<FeatureGenerator>();
		
		ret.add(tokenizer);
		
		return ret;
		
	}

}
