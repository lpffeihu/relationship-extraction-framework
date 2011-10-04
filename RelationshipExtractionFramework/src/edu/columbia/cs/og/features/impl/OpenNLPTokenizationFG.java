package edu.columbia.cs.og.features.impl;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.util.InvalidFormatException;
import edu.berkeley.compbio.jlibsvm.ImmutableSvmParameterPoint;
import edu.berkeley.compbio.jlibsvm.binary.BinaryModel;
import edu.columbia.cs.cg.sentence.Sentence;
import edu.columbia.cs.model.impl.JLibsvmModelInformation;
import edu.columbia.cs.og.features.FeatureGenerator;
import edu.columbia.cs.og.features.SentenceFeatureGenerator;
import edu.columbia.cs.og.features.featureset.FeatureSet;
import edu.columbia.cs.og.features.featureset.SequenceFS;
import edu.columbia.cs.og.structure.OperableStructure;
import edu.columbia.cs.utils.Span;

//TODO: This should call the tokenizer that is in the other package
public class OpenNLPTokenizationFG extends SentenceFeatureGenerator<SequenceFS<Span>> implements Serializable {

	private transient Tokenizer tokenizer;
	private String path;
	
	public OpenNLPTokenizationFG(String path) throws InvalidFormatException, IOException{
		this.path=path;
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
	protected SequenceFS<Span> extractFeatures(Sentence sentence) {
		String sentenceValue = sentence.getValue();
		Span[] tokenSpans = convertSpans(tokenizer.tokenizePos(sentenceValue));
		
		return new SequenceFS<Span>(tokenSpans);
	}

	@Override
	protected List<FeatureGenerator> retrieveRequiredFeatureGenerators() {
		return new ArrayList<FeatureGenerator>();
	}
	
	private void writeObject(ObjectOutputStream out) throws IOException{
		out.defaultWriteObject();
		out.writeObject(path);
	}
	
	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException{
		in.defaultReadObject();
		path=(String) in.readObject();
		InputStream modelIn = new FileInputStream(path);
		TokenizerModel tokModel = new TokenizerModel(modelIn);
		modelIn.close();
		tokenizer = new TokenizerME(tokModel);
	}
}
