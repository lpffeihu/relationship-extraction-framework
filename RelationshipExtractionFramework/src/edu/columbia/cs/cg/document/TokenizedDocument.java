package edu.columbia.cs.cg.document;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.columbia.cs.cg.document.tokenized.tokenizer.Tokenizer;
import edu.columbia.cs.cg.relations.Entity;
import edu.columbia.cs.utils.Span;

public class TokenizedDocument extends Document {

	private Span[] detectedSpans;
	
	private String[] detectedString;

	private Map<Entity, Span> entitySpanTable;
	
	public TokenizedDocument(Document d, Tokenizer tokenizer) {
		
		super(d);
		
		String text = getString(d.getPlainText());
				
		detectedSpans = tokenizer.tokenize(text);
		
		detectedString = generateTokenStrings(detectedSpans,text);
		
		entitySpanTable = new HashMap<Entity, Span>();
		
		//TODO I don't like this...
		
		for (Entity entity : d.getEntities()) {
			
			entity.setDocument(this);
			
		}
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

	/**
	 * Returns the indexes in the tokenization.
	 * @param entity
	 * @return
	 */
	
	public Span getEntitySpan(Entity entity) {
		
		Span ret = entitySpanTable.get(entity);
		
		if (ret!=null)
			return ret;
		
		int firstIndex = binarySearch(detectedSpans,entity.getOffset(),0,detectedSpans.length-1);
		
		int endIndex = binarySearch(detectedSpans,entity.getOffset() + entity.getLength()-1, firstIndex,detectedSpans.length-1);
		
		ret = new Span(firstIndex,endIndex);
		
		entitySpanTable.put(entity,ret);
		
		return ret;
		
	}

	
	private int binarySearch(Span[] spans,int offset,int low, int high){
		
		//Though this never happens, it's good to have it (real binary search)
		if (high < low)
			return -1; // not found
		int mid = low + (high - low) / 2;
		if (spans[mid].getStart() > offset)
			return binarySearch(spans, offset, low, mid-1);
		else if (spans[mid].getEnd() <= offset)
			return binarySearch(spans, offset, mid+1, high);
		else
			return mid; // found
	}

	public String[] getTokenizedString() {
		
		return detectedString;
	
	}
	
	public Span[] getTokenizedSpans() {
		
		return detectedSpans;
	
	}

}
