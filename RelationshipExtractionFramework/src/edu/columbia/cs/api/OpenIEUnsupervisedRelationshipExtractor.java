package edu.columbia.cs.api;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import weka.classifiers.Classifier;

import edu.columbia.cs.cg.candidates.CandidateSentence;
import edu.columbia.cs.cg.candidates.CandidatesGenerator;
import edu.columbia.cs.cg.document.Document;
import edu.columbia.cs.cg.document.tokenized.tokenizer.OpenNLPTokenizer;
import edu.columbia.cs.cg.document.tokenized.tokenizer.Tokenizer;
import edu.columbia.cs.cg.relations.Entity;
import edu.columbia.cs.cg.relations.Relationship;
import edu.columbia.cs.cg.relations.RelationshipType;
import edu.columbia.cs.cg.sentence.Sentence;
import edu.columbia.cs.cg.sentence.SentenceSplitter;
import edu.columbia.cs.cg.sentence.impl.OpenNLPMESplitter;
import edu.columbia.cs.model.Model;
import edu.columbia.cs.og.configuration.StructureConfiguration;
import edu.columbia.cs.og.structure.OperableStructure;
import edu.columbia.cs.utils.AlternativeOpenIEConfFunction;
import edu.columbia.cs.utils.Span;
import edu.columbia.cs.utils.chucking.Chunker;
import edu.columbia.cs.utils.chucking.OpenNLPChunker;
import edu.columbia.cs.utils.postags.POSTagger;
import edu.columbia.cs.utils.postags.impl.OpenNLPPOSTagger;
import edu.washington.cs.knowitall.extractor.ReVerbExtractor;
import edu.washington.cs.knowitall.extractor.conf.ConfidenceFunctionException;
import edu.washington.cs.knowitall.extractor.conf.ReVerbConfFunction;
import edu.washington.cs.knowitall.nlp.ChunkedSentence;
import edu.washington.cs.knowitall.nlp.extraction.ChunkedBinaryExtraction;

public class OpenIEUnsupervisedRelationshipExtractor<D extends Document> implements RelationshipExtractor<Document>{

	private SentenceSplitter splitter;
	private Tokenizer tokenizer;
	private POSTagger pos;
	private Chunker chunker;
	private Classifier cla;
	private double threshold;

	public OpenIEUnsupervisedRelationshipExtractor(SentenceSplitter splitter,
			Tokenizer tokenizer,POSTagger pos,Chunker chunker){
		this(splitter,tokenizer,pos,chunker,null,0.5);
	}
	
	public OpenIEUnsupervisedRelationshipExtractor(SentenceSplitter splitter,
			Tokenizer tokenizer,POSTagger pos,Chunker chunker, Classifier cla){
		this(splitter,tokenizer,pos,chunker,cla,0.5);
	}
	
	public OpenIEUnsupervisedRelationshipExtractor(SentenceSplitter splitter,
			Tokenizer tokenizer,POSTagger pos,Chunker chunker, double threshold){
		this(splitter,tokenizer,pos,chunker,null,threshold);
	}

	public OpenIEUnsupervisedRelationshipExtractor(SentenceSplitter splitter,
			Tokenizer tokenizer,POSTagger pos,Chunker chunker, Classifier cla,
			double threshold){
		this.splitter=splitter;
		this.tokenizer=tokenizer;
		this.pos=pos;
		this.chunker=chunker;
		this.cla=cla;
		this.threshold=threshold;
	}


	public List<Relationship> extractTuples(Document doc){
		Sentence[] sents = splitter.split(doc);
		List<ChunkedSentence> chunkedSents = new ArrayList<ChunkedSentence>();
		for(int i=0; i<sents.length; i++){
			String sentenceValue = sents[i].getValue();
			Span[] tokenization = tokenizer.tokenize(sentenceValue);
			String[] tokenizationStrings = new String[tokenization.length];
			for(int j=0; j<tokenization.length; j++){
				Span s = tokenization[j];
				tokenizationStrings[j]=sentenceValue.substring(s.getStart(),s.getEnd());
			}
			String[] posTags = pos.tag(tokenizationStrings);
			String[] chunks = chunker.chunk(tokenizationStrings, posTags);
			ChunkedSentence sent = new ChunkedSentence(tokenizationStrings, posTags, chunks);
			chunkedSents.add(sent);
		}

		ReVerbExtractor reverb;
		try {
			reverb = new ReVerbExtractor();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
			return null;
		}
		
		AlternativeOpenIEConfFunction confFunc;
		try {
			if(cla==null){
				confFunc = new AlternativeOpenIEConfFunction();
			}else{
				confFunc = new AlternativeOpenIEConfFunction(cla);
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
			return null;
		}

		Map<Span,Integer> entityIds = new TreeMap<Span,Integer>();
		List<Relationship> results = new ArrayList<Relationship>();
		for(ChunkedSentence sent : chunkedSents){
			for (ChunkedBinaryExtraction extr : reverb.extract(sent)) {
				double conf;
				try {
					conf = confFunc.getConf(extr);
				} catch (ConfidenceFunctionException e) {
					e.printStackTrace();
					System.exit(1);
					return null;
				}
				if(conf>threshold){
					RelationshipType t = new RelationshipType(extr.getRelation().toString(), new String[]{"Arg-1", "Arg-2"});
					Relationship rel = new Relationship(t);
					Span ent1 = new Span(extr.getArgument1().getStart(), extr.getArgument1().getStart()+extr.getArgument1().getLength());
					Integer id1 = entityIds.get(ent1);
					if(id1==null){
						id1=entityIds.size();
						entityIds.put(ent1, id1);
					}
					Entity arg1 = new Entity(doc.getFilename() + "-Entity" + id1,"UnknownType",extr.getArgument1().getStart(),
							extr.getArgument1().getLength(),extr.getArgument1().toString(),doc);
	
					Span ent2 = new Span(extr.getArgument2().getStart(), extr.getArgument2().getStart()+extr.getArgument2().getLength());
					Integer id2 = entityIds.get(ent2);
					if(id2==null){
						id2=entityIds.size();
						entityIds.put(ent2, id2);
					}
					Entity arg2 = new Entity(doc.getFilename() + "-Entity" + id2,"UnknownType",extr.getArgument2().getStart(),
							extr.getArgument2().getLength(),extr.getArgument2().toString(),doc);
	
					doc.addEntity(arg1);
					doc.addEntity(arg2);
	
					rel.setRole("Arg-1", arg1);
					rel.setRole("Arg-2", arg2);
					rel.setLabel(t.getType());
	
					results.add(rel);
				}
			}
		}
		return results;
	}
}
