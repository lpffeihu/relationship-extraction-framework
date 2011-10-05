package edu.columbia.cs.cg.prdualrank.index.tokenizer;
import java.io.IOException;

import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.standard.ClassicTokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;

import edu.columbia.cs.utils.Span;


public class SpanBasedTokenizer extends Tokenizer {

	private final CharTermAttribute termAtt = addAttribute(CharTermAttribute.class);
	private final OffsetAttribute offsetAtt = addAttribute(OffsetAttribute.class);
	private final PositionIncrementAttribute posIncrAtt = addAttribute(PositionIncrementAttribute.class);
	
	private Span[] spans;
	private String[] content;
	private int currentIndex;
	
	public SpanBasedTokenizer(Span[] spans, String[] content) {
		
		this.spans = spans;
		this.content = content;
		currentIndex = 0;
		
	}

	@Override
	public boolean incrementToken() throws IOException {
		
		clearAttributes();
		
		if (currentIndex >= spans.length)
			return false;
		
		termAtt.append(content[currentIndex]);
		
		offsetAtt.setOffset(spans[currentIndex].getStart(), spans[currentIndex].getEnd());
		
		posIncrAtt.setPositionIncrement(1);
		
		currentIndex++;
		
		return true;
	}

}
