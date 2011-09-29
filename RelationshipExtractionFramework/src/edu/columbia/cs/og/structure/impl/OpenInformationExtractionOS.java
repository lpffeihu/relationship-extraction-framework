package edu.columbia.cs.og.structure.impl;

import java.util.Set;

import weka.core.Instance;

import edu.columbia.cs.cg.candidates.CandidateSentence;
import edu.columbia.cs.model.impl.WekaClassifierModel;
import edu.columbia.cs.og.features.featureset.SequenceFS;
import edu.columbia.cs.og.features.impl.EntitySplitsFG;
import edu.columbia.cs.og.features.impl.KnowItAllChunkingFG;
import edu.columbia.cs.og.features.impl.OpenInformationExtractionFG;
import edu.columbia.cs.og.features.impl.OpenNLPPartOfSpeechFG;
import edu.columbia.cs.og.features.impl.SpansToStringsConvertionFG;
import edu.columbia.cs.og.structure.OperableStructure;
import edu.columbia.cs.og.structure.WekableStructure;
import edu.columbia.cs.utils.Span;

public class OpenInformationExtractionOS extends OperableStructure implements WekableStructure {

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
	private Instance instance;
	
	
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
		
		SequenceFS<String> tokensString = getFeatures(SpansToStringsConvertionFG.class);
		
		tokens = getString(tokensString);
		
		pos = getString(getFeatures(OpenNLPPartOfSpeechFG.class));
		
		chunks = getString(getFeatures(KnowItAllChunkingFG.class));

		SequenceFS<Span> span = getFeatures(EntitySplitsFG.class);
		
		first = getString(span.getElement(1),tokensString);
		
		firstIndexes = getString(span.getElement(1));

		middle = getString(span.getElement(2),tokensString);
		
		middleIndexes = getString(span.getElement(2));

		second = getString(span.getElement(3),tokensString);
		
		secondIndexes = getString(span.getElement(3));
		
		instance=getFeatures(OpenInformationExtractionFG.class).getInstance();
	}

	private String getString(Span span) {
		
		return span.getStart() + " " + (span.getEnd() - span.getStart() + 1);
		
	}

	private String getString(Span element, SequenceFS<String> tokensString) {
		
		int index = element.getStart();
		
		int end = element.getEnd();
		
		String ret = "";
		
		if(end<index){
			return ret;
		}
		
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
			string = tokens + "\n" + pos + "\n" + chunks + "\n" + first + "\n" + firstIndexes + "\n" + middle + "\n" + middleIndexes + "\n" + second + "\n" + secondIndexes;
		}
		return string;
	}

	@Override
	public Instance getInstance() {
		return instance;
	}
}
