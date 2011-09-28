package edu.columbia.cs.og.features.impl;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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

	private transient POSTaggerME tagger;
	private FeatureGenerator<SequenceFS<String>> tokenizer;
	private String path;

	public OpenNLPPartOfSpeechFG(String path,FeatureGenerator<SequenceFS<String>> tokenizer) throws InvalidFormatException, IOException{
		InputStream modelIn = null;
		POSModel modelPOS=null;
		modelIn = new FileInputStream(path);
		modelPOS = new POSModel(modelIn);
		modelIn.close();
		tagger = new POSTaggerME(modelPOS);
		this.tokenizer = tokenizer;
		this.path=path;
	}
	
	@Override
	protected SequenceFS<String> extractFeatures(CandidateSentence sentence) {
		
		SequenceFS<String> tokenization = sentence.getFeatures(tokenizer);

		return new SequenceFS<String>(tagger.tag(tokenization.toArray()));
	}

	@Override
	protected List<FeatureGenerator> retrieveRequiredFeatureGenerators() {
		ArrayList<FeatureGenerator> ret = new ArrayList<FeatureGenerator>();
		
		ret.add(tokenizer);
	
		return ret;
	}
	
	private void writeObject(ObjectOutputStream out) throws IOException{
		out.defaultWriteObject();
		out.writeObject(path);
	}
	
	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException{
		in.defaultReadObject();
		path=(String) in.readObject();
		InputStream modelIn = null;
		POSModel modelPOS=null;
		modelIn = new FileInputStream(path);
		modelPOS = new POSModel(modelIn);
		modelIn.close();
		tagger = new POSTaggerME(modelPOS);
	}
}
