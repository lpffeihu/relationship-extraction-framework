/**
 * Reader used to retrieve tokens from already saved tokens. It is used by Lucene's interface.
 *
 * @author      Pablo Barrio
 * @author		Goncalo Simoes
 * @version     0.1
 * @since       2011-10-07
 */
package edu.columbia.cs.cg.prdualrank.index.reader;

import java.io.IOException;
import java.io.Reader;

import edu.columbia.cs.utils.Span;

public class TokenBasedReader extends Reader {

	private Span[] spans;
	private String[] strings;

	/**
	 * Instantiates a new token based reader.
	 *
	 * @param tokenizedSpans the spans representing the tokenization of the content.
	 * @param tokenizedString the value of the tokens. Must match the tokenizedSpans.
	 */
	public TokenBasedReader(Span[] tokenizedSpans, String[] tokenizedString) {
		this.spans = tokenizedSpans;
		this.strings = tokenizedString;
	}

	/* (non-Javadoc)
	 * @see java.io.Reader#close()
	 */
	@Override
	public void close() throws IOException {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see java.io.Reader#read(char[], int, int)
	 */
	@Override
	public int read(char[] cbuf, int off, int len) throws IOException {
		// TODO Auto-generated method stub
		return 0;
	}

	/**
	 * Gets the spans.
	 *
	 * @return the stored spans
	 */
	public Span[] getSpans() {
		return spans;
	}

	/**
	 * Gets the strings.
	 *
	 * @return the stored strings
	 */
	public String[] getStrings() {
		return strings;
	}

}
