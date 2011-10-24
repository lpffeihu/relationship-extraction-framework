package edu.columbia.cs.ref.algorithm.feature.generation.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import opennlp.tools.util.InvalidFormatException;
import edu.columbia.cs.ref.algorithm.feature.generation.CandidateSentenceFeatureGenerator;
import edu.columbia.cs.ref.algorithm.feature.generation.FeatureGenerator;
import edu.columbia.cs.ref.model.CandidateSentence;
import edu.columbia.cs.ref.model.feature.impl.GraphFS;
import edu.columbia.cs.ref.model.feature.impl.SequenceFS;
import edu.columbia.cs.utils.SimpleGraph;
import edu.columbia.cs.utils.Span;
import edu.stanford.nlp.ling.Word;
import edu.stanford.nlp.parser.lexparser.LexicalizedParser;
import edu.stanford.nlp.trees.EnglishGrammaticalStructure;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TypedDependency;

public class StanfordNLPDependencyGraphFG extends CandidateSentenceFeatureGenerator<GraphFS<Integer,String>> {

	private LexicalizedParser parser;
	private FeatureGenerator<SequenceFS<Span>> tokenizer;
	
	public StanfordNLPDependencyGraphFG(String path, FeatureGenerator<SequenceFS<Span>> tokenizer) throws InvalidFormatException, IOException{
		parser = new LexicalizedParser(path);
		this.tokenizer = tokenizer;
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
	protected GraphFS<Integer,String> extractFeatures(CandidateSentence sentence) {
		SequenceFS<Span> tokenization = sentence.getFeatures(tokenizer);

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

	@Override
	protected List<FeatureGenerator> retrieveRequiredFeatureGenerators() {
		ArrayList<FeatureGenerator> ret = new ArrayList<FeatureGenerator>();
		ret.add(tokenizer);
		return ret;
	}
}
