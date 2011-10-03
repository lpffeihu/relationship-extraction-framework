package edu.columbia.cs.cg.document.tokenized.tokenizer;

import edu.columbia.cs.utils.Span;

public interface Tokenizer {

	public Span[] tokenize(String text);

}
