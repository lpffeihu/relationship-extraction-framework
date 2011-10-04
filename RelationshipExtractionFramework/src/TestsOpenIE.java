import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import opennlp.tools.util.InvalidFormatException;

import cern.colt.Arrays;

import edu.columbia.cs.cg.document.Document;
import edu.columbia.cs.cg.document.loaders.impl.RawDocumentLoader;
import edu.columbia.cs.cg.document.preprocessing.impl.HTMLContentKeeper;
import edu.columbia.cs.cg.document.segmentator.impl.SimpleSegmentDocumentSegmentator;
import edu.columbia.cs.cg.document.tokenized.tokenizer.OpenNLPTokenizer;
import edu.columbia.cs.cg.document.tokenized.tokenizer.Tokenizer;
import edu.columbia.cs.cg.relations.Entity;
import edu.columbia.cs.cg.relations.Relationship;
import edu.columbia.cs.cg.relations.RelationshipType;
import edu.columbia.cs.cg.sentence.Sentence;
import edu.columbia.cs.cg.sentence.SentenceSplitter;
import edu.columbia.cs.cg.sentence.impl.OpenNLPMESplitter;
import edu.columbia.cs.og.features.impl.OpenNLPTokenizationFG;
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


public class TestsOpenIE {
	public static void main(String[] args) throws InvalidFormatException, IOException, ConfidenceFunctionException{
		String sentStr = 
		"Tech journalists are convening at Apple’s headquarters in Cupertino, " + 
		"Calif., on Tuesday to see what innovations the company has in mind for " + 
		"the iPhone. Logic and history would indicate that the iPhone 5 will be " +
		"thinner and lighter than its predecessor and also include a better " + 
		"camera. Apple is also releasing a new version of its mobile operating " + 
		"system, iOS 5, which will add new functions and interfaces — including " + 
		"what many suspect will be enhanced voice commands. The proceedings begin " +
		"at 10 a.m. local time, 1 p.m. New York time, and I’ll be on hand, " + 
		"live-blogging the event here as Timothy D. Cook, Apple’s newly permanent " +
		"chief executive, takes the stage.";
		
		RawDocumentLoader loader = new RawDocumentLoader(null, new HTMLContentKeeper(), new SimpleSegmentDocumentSegmentator());
		Document doc = loader.load(sentStr);
		
		SentenceSplitter splitter = new OpenNLPMESplitter("en-sent.bin");
		Tokenizer tokenizer = new OpenNLPTokenizer("en-token.bin");
		POSTagger pos = new OpenNLPPOSTagger("en-pos-maxent.bin");
		Chunker chunker = new OpenNLPChunker("en-chunker.bin");
		
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
		
		ReVerbExtractor reverb = new ReVerbExtractor();
        ReVerbConfFunction confFunc = new ReVerbConfFunction();
        
        Map<Span,Integer> entityIds = new TreeMap<Span,Integer>();
		for(ChunkedSentence sent : chunkedSents){
			for (ChunkedBinaryExtraction extr : reverb.extract(sent)) {
				RelationshipType t = new RelationshipType(extr.getRelation().toString(), new String[]{"Arg-1", "Arg-2"});
				double conf = confFunc.getConf(extr);
				Relationship rel = new Relationship(t);
				Span ent1 = new Span(extr.getArgument1().getStart(), extr.getArgument1().getStart()+extr.getArgument1().getLength());
				Integer id1 = entityIds.get(ent1);
				if(id1==null){
					
				}
				Entity arg1 = new Entity("EntityId","UnknownType",extr.getArgument1().getStart(),
						extr.getArgument1().getLength(),extr.getArgument1().toString(),doc);
				
				Span ent2 = new Span(extr.getArgument2().getStart(), extr.getArgument2().getStart()+extr.getArgument2().getLength());
				Entity arg2 = new Entity("EntityId","UnknownType",extr.getArgument2().getStart(),
						extr.getArgument2().getLength(),extr.getArgument2().toString(),doc);
				
				doc.addEntity(arg1);
				doc.addEntity(arg2);
				
				rel.setRole("Arg-1", arg1);
				rel.setRole("Arg-2", arg2);
				rel.setLabel(t.getType());
	            
				System.out.println(rel);
	        }
		}
		
	}
}
