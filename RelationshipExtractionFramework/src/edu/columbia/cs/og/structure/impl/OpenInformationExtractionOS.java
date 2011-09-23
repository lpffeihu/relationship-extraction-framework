package edu.columbia.cs.og.structure.impl;

import edu.columbia.cs.cg.candidates.CandidateSentence;
import edu.columbia.cs.og.features.featureset.SequenceFS;
import edu.columbia.cs.og.features.impl.EntitySplitsFG;
import edu.columbia.cs.og.features.impl.KnowItAllChunkingFG;
import edu.columbia.cs.og.features.impl.OpenNLPPartOfSpeechFG;
import edu.columbia.cs.og.features.impl.OpenNLPStringTokenizationFG;
import edu.columbia.cs.og.structure.OperableStructure;
import edu.columbia.cs.utils.Span;

public class OpenInformationExtractionOS extends OperableStructure {

	private String tokens;
	private String pos;
	private String chunks;
	private String first;
	private String firstIndexes;
	private String middle;
	private String middleIndexes;
	private String second;
	private String secondIndexes;
	private String string;
	private String label;
	
	
	public OpenInformationExtractionOS(CandidateSentence c) {
		super(c);
		
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -2183612647555553754L;

	@Override
	public void initialize() {
		
		string = null;	
		
		SequenceFS<String> tokensString = (SequenceFS<String>)getFeatures(OpenNLPStringTokenizationFG.class);
		
		tokens = getString(tokensString);
		
		pos = getString((SequenceFS<String>)getFeatures(OpenNLPPartOfSpeechFG.class));
		
		chunks = getString((SequenceFS<String>)getFeatures(KnowItAllChunkingFG.class));

		SequenceFS<Span> span = (SequenceFS<Span>)getFeatures(EntitySplitsFG.class);
		
		first = getString(span.getElement(1),tokensString);
		
		firstIndexes = getString(span.getElement(1));

		middle = getString(span.getElement(2),tokensString);
		
		middleIndexes = getString(span.getElement(2));

		second = getString(span.getElement(3),tokensString);
		
		secondIndexes = getString(span.getElement(3));
		
		label = getLabel();
	}

	private String getString(Span span) {
		
		return span.getStart() + " " + (span.getEnd() - span.getStart() + 1);
		
	}

	private String getString(Span element, SequenceFS<String> tokensString) {
		
		int index = element.getStart();
		
		int end = element.getEnd();
		
		String ret = "";
		
		for (int i = index; i <= end; i++) {
			
			ret = ret + " " + tokensString.getElement(i);
			
		}
		
		return ret.substring(1);

	}

	private String getString(SequenceFS<String> features) {
		
		String ret = "";
		
		for (int i = 0; i < features.size(); i++) {
			
			ret = ret + " " + features.getElement(i);
			
		}
		
		return ret.substring(1);
		
	}

	public String toString(){
		
		return getStringVersion();
	
	}

	private String getStringVersion() {
		
		if (string == null){
			string = tokens + "\n" + pos + "\n" + chunks + "\n" + first + "\n" + firstIndexes + "\n" + middle + "\n" + middleIndexes + "\n" + second + "\n" + secondIndexes + "\n" + label;
		}
		return string;
	}
}
