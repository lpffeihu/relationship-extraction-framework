package edu.columbia.cs.cg.sentence.impl;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.util.Span;
import edu.columbia.cs.cg.document.Document;
import edu.columbia.cs.cg.document.Segment;
import edu.columbia.cs.cg.sentence.Sentence;
import edu.columbia.cs.cg.sentence.SentenceSplitter;

public class OpenNLPMESplitter implements SentenceSplitter {

	private SentenceDetectorME sentenceDetector;
	
	public OpenNLPMESplitter(String path) throws FileNotFoundException{
		InputStream modelIn = new FileInputStream(path);
		SentenceModel model=null;
		try {
			model = new SentenceModel(modelIn);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			if (modelIn != null) {
				try {
					modelIn.close();
				}
				catch (IOException e) {
				}
			}
		}

		sentenceDetector = new SentenceDetectorME(model);
	}
	
	@Override
	public Sentence[] split(Document d) {
		List<Sentence> sents = new ArrayList<Sentence>();
		for(Segment seg : d.getPlainText()){
			Span[] sentsSpans = sentenceDetector.sentPosDetect(seg.getValue());
			for(Span s : sentsSpans){
				int startSpan = seg.getOffset()+s.getStart();
				int endSpan = seg.getOffset()+s.getEnd();
				Sentence newSent = new Sentence(d, startSpan, endSpan-startSpan);
				sents.add(newSent);
			}
		}
		
		Sentence[] result = new Sentence[sents.size()];
		return sents.toArray(result);
	}

}
