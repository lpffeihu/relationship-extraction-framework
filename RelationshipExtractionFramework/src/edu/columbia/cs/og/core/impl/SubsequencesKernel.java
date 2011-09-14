package edu.columbia.cs.og.core.impl;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import opennlp.tools.util.InvalidFormatException;

import edu.columbia.cs.cg.candidates.CandidateSentence;
import edu.columbia.cs.cg.sentence.Sentence;
import edu.columbia.cs.og.core.Core;
import edu.columbia.cs.og.features.FeatureGenerator;
import edu.columbia.cs.og.features.impl.OpenNLPTokenizationFG;
import edu.columbia.cs.og.structure.OperableStructure;
import edu.columbia.cs.og.structure.impl.TaggedSequence;

public class SubsequencesKernel extends Core {

	@Override
	public OperableStructure createOperableStructure(CandidateSentence sent) {
		/*Tokenizer<Word> tokenizer = tokenizerFactory.getTokenizer(new StringReader(sent.getSentence()));
		List<Word> sentenceTokens = tokenizer.tokenize();

		Pair<List<Word>,Pair<Integer,Integer>> normalization = normalizeTokenization(sent, sentenceTokens);
		sentenceTokens=normalization.a();
		Pair<Integer,Integer> entityIndexes=normalization.b();

		Span[] tokens = new Span[sentenceTokens.size()];
		String[] tokensString = new String[sentenceTokens.size()];
		int numTokens = sentenceTokens.size();
		for(int i=0;i<numTokens;i++){
			Word w = sentenceTokens.get(i);
			Span s = new Span(w.beginPosition(),w.endPosition());
			if(w.beginPosition()==-1){

			}
			tokens[i] = s;
			tokensString[i] = w.word();
		}

		String[][] information = new String[sentenceTokens.size()][6];
		for(int i=0; i<sentenceTokens.size(); i++){
			information[i][0]=tokensString[i];
		}

		return new TaggedSequence(sent.getEntity1().getEntityId()+"|"+
				sent.getEntity2().getEntityId(),
				information, entityIndexes.a(), entityIndexes.b(),
				sent.getEntity1().getType(), sent.getEntity2().getType(),
				sent.getLabel());*/
		return new TaggedSequence(sent);
	}

	@Override
	protected List<FeatureGenerator> createMandatoryFeatureGenerators() {
		List<FeatureGenerator> fg = new ArrayList<FeatureGenerator>();

		//TODO: the tokenizer should be received in the constructor
		try {
			fg.add(new OpenNLPTokenizationFG("en-token.bin"));
		} catch (InvalidFormatException e) {
			e.printStackTrace();
			System.exit(1);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
		//fg.add(Chunker);
		
		return fg;
	}

}
