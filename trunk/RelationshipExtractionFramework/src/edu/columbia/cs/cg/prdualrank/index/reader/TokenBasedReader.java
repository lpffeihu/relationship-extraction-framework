/**
 * Reader used to retrieve tokens from already saved tokens. It is used by <b>Apache Lucene's<\b> interface.
 *
 * <br>
 * For this Class, Apache Lucene Engine is required. 
 * @see <a href="http://lucene.apache.org/"> Lucene </a>
 * 
 * <br>
 * This class is used for our implementation of: 
 * <b> "Searching Patterns for Relation Extraction over the Web: Rediscovering the Pattern-Relation Duality" </b>. Y. Fang and K. C.-C. Chang. In WSDM, pages 825-834, 2011.
 * 
 * <br>
 * For further information, 
 * 
 * @see <a href="http://www.wsdm2011.org/"> WSDM 2011 Conference Website </a>
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