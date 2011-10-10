/**
 * Tokenizer for Lucene Search Engine based on the already calculated tokens of the element we want either to index or 
 * search for.
 *
 * @author      Pablo Barrio
 * @author		Goncalo Simoes
 * @version     0.1
 * @since       2011-10-07
 */
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
	
	/**
	 * Instantiates a new span based tokenizer.
	 *
	 * @param spans the spans of the element to be tokenized
	 * @param content the content the splitted content of the element to be tokenized. Must match the spans.
	 */
	public SpanBasedTokenizer(Span[] spans, String[] content) {
		
		this.spans = spans;
		this.content = content;
		currentIndex = 0;
		
	}

	/* (non-Javadoc)
	 * @see org.apache.lucene.analysis.TokenStream#incrementToken()
	 */
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
