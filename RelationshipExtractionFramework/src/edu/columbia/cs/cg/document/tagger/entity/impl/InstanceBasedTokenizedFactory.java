package edu.columbia.cs.cg.document.tagger.entity.impl;

import com.aliasi.tokenizer.Tokenizer;
import com.aliasi.tokenizer.TokenizerFactory;

import edu.columbia.cs.utils.Span;

public class InstanceBasedTokenizedFactory implements TokenizerFactory{

	private class SpanBasedTokenizer extends Tokenizer{

		private String str;
		private Span[] spans;
		private int currentIndex;

		public SpanBasedTokenizer(String str, Span[] spans) {
			
			this.str = str;
			this.spans = spans;
			this.currentIndex = 0;
		}

		@Override
		public String nextToken() {
			
			if (currentIndex < spans.length){
			
				String ret = str.substring(spans[currentIndex].getStart(), spans[currentIndex].getEnd());
				currentIndex++;
				return ret;
				
			}
			
			return null;
			
		}
		
	}
	
    public static InstanceBasedTokenizedFactory INSTANCE = null;
	private edu.columbia.cs.cg.document.tokenized.tokenizer.Tokenizer tokenizer;
	
	public InstanceBasedTokenizedFactory(edu.columbia.cs.cg.document.tokenized.tokenizer.Tokenizer tokenizer) {
		this.tokenizer = tokenizer;
	}

	@Override
	public Tokenizer tokenizer(char[] ch, int start, int length) {
		
		String str = new String(ch, start, length);
		
		Span[] spans = null;
		
		synchronized (tokenizer) {
			
			spans = tokenizer.tokenize(str);
			
		}
		
		return new SpanBasedTokenizer(str,spans);
		
	}

	public static TokenizerFactory getInstance(
			edu.columbia.cs.cg.document.tokenized.tokenizer.Tokenizer tokenizer) {
		
		if (INSTANCE == null){
			INSTANCE = new InstanceBasedTokenizedFactory(tokenizer);
		}
		return INSTANCE;
	}

}
