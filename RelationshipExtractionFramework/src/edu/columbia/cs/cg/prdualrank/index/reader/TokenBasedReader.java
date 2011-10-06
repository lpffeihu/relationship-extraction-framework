package edu.columbia.cs.cg.prdualrank.index.reader;

import java.io.IOException;
import java.io.Reader;

import edu.columbia.cs.utils.Span;

public class TokenBasedReader extends Reader {

	private Span[] spans;
	private String[] strings;

	public TokenBasedReader(Span[] tokenizedSpans, String[] tokenizedString) {
		this.spans = tokenizedSpans;
		this.strings = tokenizedString;
	}

	@Override
	public void close() throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public int read(char[] cbuf, int off, int len) throws IOException {
		// TODO Auto-generated method stub
		return 0;
	}

	public Span[] getSpans() {
		return spans;
	}

	public String[] getStrings() {
		return strings;
	}

}
