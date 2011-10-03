package edu.columbia.cs.cg.document;

import java.util.ArrayList;
import java.util.List;

import edu.columbia.cs.cg.document.tokenized.tokenizer.Tokenizer;
import edu.columbia.cs.cg.relations.Entity;
import edu.columbia.cs.utils.Span;

public class TokenizedDocument extends Document {

	private Span[] detectedSpans;
	
	private String[] detectedString;
	
	public TokenizedDocument(Document d, Tokenizer tokenizer) {
		
		super(d);
		
		String text = getString(d.getPlainText());
				
		detectedSpans = tokenizer.tokenize(text);
		
		detectedString = generateTokenStrings(detectedSpans,text);
		
	}

	private String getString(List<Segment> plainText) {
		
		StringBuilder sb = new StringBuilder();
		
		for (Segment segment : plainText) {
			
			sb.insert(segment.getOffset(), segment.getValue());
			
		}
		
		return sb.toString();
		
	}

	private String[] generateTokenStrings(Span[] spans, String text) {
		
		String[] tokenStrings = new String[spans.length];
		
		for (int i = 0; i < spans.length; i++) {
			
			tokenStrings[i] = text.substring(spans[i].getStart(), spans[i].getEnd());
			
		}
		
		return tokenStrings;
	}

	public Span getEntitySpan(Entity entity) {
		
		int firstIndex = binarySearch(detectedSpans,entity.getOffset(),0,detectedSpans.length-1);
		
		int endIndex = binarySearch(detectedSpans,entity.getOffset() + entity.getLength(), firstIndex,detectedSpans.length-1);
		
		return new Span(firstIndex,endIndex);
		
	}

	
	private int binarySearch(Span[] spans,int offset,int low, int high){
		
		//Though this never happens, it's good to have it (real binary search)
		if (high < low)
			return -1; // not found
		int mid = low + (high - low) / 2;
		if (spans[mid].getStart() > offset)
			return binarySearch(spans, offset, low, mid-1);
		else if (spans[mid].getEnd() < offset)
			return binarySearch(spans, offset, mid+1, high);
		else
			return mid; // found
	}

	public String[] getTokenizedString() {
		
		return detectedString;
	
	}

}
