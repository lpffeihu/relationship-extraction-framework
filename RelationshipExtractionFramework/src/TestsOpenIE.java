import java.io.IOException;

import opennlp.tools.util.InvalidFormatException;
import edu.columbia.cs.api.OpenIEUnsupervisedRelationshipExtractor;
import edu.columbia.cs.api.RelationshipExtractor;
import edu.columbia.cs.cg.document.Document;
import edu.columbia.cs.cg.document.loaders.impl.RawDocumentLoader;
import edu.columbia.cs.cg.document.preprocessing.impl.HTMLContentKeeper;
import edu.columbia.cs.cg.document.segmentator.impl.SimpleSegmentDocumentSegmentator;
import edu.columbia.cs.cg.document.tokenized.tokenizer.OpenNLPTokenizer;
import edu.columbia.cs.cg.document.tokenized.tokenizer.Tokenizer;
import edu.columbia.cs.cg.sentence.SentenceSplitter;
import edu.columbia.cs.cg.sentence.impl.OpenNLPMESplitter;
import edu.columbia.cs.utils.chucking.Chunker;
import edu.columbia.cs.utils.chucking.OpenNLPChunker;
import edu.columbia.cs.utils.postags.POSTagger;
import edu.columbia.cs.utils.postags.impl.OpenNLPPOSTagger;
import edu.washington.cs.knowitall.extractor.conf.ConfidenceFunctionException;
import edu.washington.cs.knowitall.util.DefaultObjects;


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
		
		RelationshipExtractor<Document> rel = new OpenIEUnsupervisedRelationshipExtractor<Document>(splitter,tokenizer,pos,chunker);
		System.out.println(rel.extractTuples(doc));
	}
}
