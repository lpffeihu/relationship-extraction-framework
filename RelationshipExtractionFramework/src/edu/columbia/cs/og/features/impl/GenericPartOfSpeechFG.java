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
import edu.columbia.cs.utils.POStoGenericPOSConverter;
import edu.columbia.cs.utils.Span;

public class GenericPartOfSpeechFG extends CandidateSentenceFeatureGenerator {

	@Override
	protected FeatureSet process(CandidateSentence sentence) {
		
		//TODO The part of speech doesn't have to be hard coded
		
		SequenceFS<String> pos = (SequenceFS<String>) sentence.getFeatures(OpenNLPPartOfSpeechFG.class);

		String[] gpos = new String[pos.size()];
		for(int i=0; i<pos.size(); i++){
			gpos[i]=POStoGenericPOSConverter.convertPOS(pos.getElement(i));
		}

		return new SequenceFS<String>(gpos);
	}
}
