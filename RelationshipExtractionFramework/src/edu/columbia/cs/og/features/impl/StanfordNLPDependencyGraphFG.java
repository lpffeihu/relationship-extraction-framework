package edu.columbia.cs.og.features.impl;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import edu.columbia.cs.og.features.featureset.GraphFS;
import edu.columbia.cs.og.features.featureset.SequenceFS;
import edu.columbia.cs.og.structure.OperableStructure;
import edu.columbia.cs.utils.SimpleGraph;
import edu.columbia.cs.utils.Span;
import edu.stanford.nlp.ling.Word;
import edu.stanford.nlp.parser.lexparser.LexicalizedParser;
import edu.stanford.nlp.trees.EnglishGrammaticalStructure;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TypedDependency;

public class StanfordNLPDependencyGraphFG extends CandidateSentenceFeatureGenerator {

	private LexicalizedParser parser;
	
	public StanfordNLPDependencyGraphFG(String path) throws InvalidFormatException, IOException{
		parser = new LexicalizedParser(path);
	}
	
	private List<Word> getTokens(SequenceFS<Span> spans, CandidateSentence sentence){
		String value = sentence.getSentence().getValue();
		List<Word> tokens = new ArrayList<Word>();
		
		for(int i=0; i<spans.size(); i++){
			Span s = spans.getElement(i);
			tokens.add(new Word(value.substring(s.getStart(),s.getEnd())));
		}
		
		return tokens;
	}
	
	private Map<List<Word>,Collection<TypedDependency>> depParses = new HashMap<List<Word>,Collection<TypedDependency>>();

	@Override
	protected FeatureSet process(CandidateSentence sentence) {
		SequenceFS<Span> tokenization = (SequenceFS<Span>) sentence.getFeatures(EntityBasedChunkingFG.class);

		List<Word> tokens = getTokens(tokenization, sentence);
		
		Collection<TypedDependency> dependencies = depParses.get(tokens);
		if(dependencies==null){
			System.out.println("PROCESS");
			parser.parse(tokens);
			Tree parsingTree = parser.getBestPCFGParse();
			EnglishGrammaticalStructure struc = new EnglishGrammaticalStructure(parsingTree);
			dependencies = struc.allTypedDependencies();
			depParses.put(tokens, dependencies);
		}else{
			System.out.println("REUSED");
		}
		
		SimpleGraph<Integer,String> g = new SimpleGraph<Integer,String>(tokenization.size());

		int tokenizationSize=tokenization.size();
		for(int i=0; i<tokenizationSize; i++){
			g.addNode(i, i);
		}
		
		for(TypedDependency dep : dependencies){
			g.addEdge(dep.gov().index()-1, dep.dep().index()-1, dep.reln().getShortName());
		}
		
		return new GraphFS<Integer,String>(g);
	}
}
