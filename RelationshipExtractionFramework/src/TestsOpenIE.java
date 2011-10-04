import java.io.FileNotFoundException;
import java.io.IOException;

import opennlp.tools.util.InvalidFormatException;

import cern.colt.Arrays;

import edu.columbia.cs.cg.document.Document;
import edu.columbia.cs.cg.document.loaders.impl.RawDocumentLoader;
import edu.columbia.cs.cg.document.preprocessing.impl.HTMLContentKeeper;
import edu.columbia.cs.cg.document.segmentator.impl.SimpleSegmentDocumentSegmentator;
import edu.columbia.cs.cg.document.tokenized.tokenizer.OpenNLPTokenizer;
import edu.columbia.cs.cg.document.tokenized.tokenizer.Tokenizer;
import edu.columbia.cs.cg.sentence.Sentence;
import edu.columbia.cs.cg.sentence.SentenceSplitter;
import edu.columbia.cs.cg.sentence.impl.OpenNLPMESplitter;
import edu.columbia.cs.og.features.impl.OpenNLPTokenizationFG;


public class TestsOpenIE {
	public static void main(String[] args) throws InvalidFormatException, IOException{
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
		
		
		Sentence[] sents = splitter.split(doc);
		for(int i=0; i<sents.length; i++){
			System.out.println(i + ":" + sents[i]);
		}
		
		
	}
}
