/**
 * Using the TokenBasedReader, this class tokenizes the stream in order to be either indexed or searched.
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
 *
 * @author      Pablo Barrio
 * @author		Goncalo Simoes
 * @version     0.1
 * @since       2011-10-07
 */
package edu.columbia.cs.cg.prdualrank.index.analyzer;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Set;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.LowerCaseFilter;
import org.apache.lucene.analysis.StopFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.util.Version;

import edu.columbia.cs.cg.prdualrank.index.reader.TokenBasedReader;
import edu.columbia.cs.cg.prdualrank.index.tokenizer.SpanBasedTokenizer;
import edu.columbia.cs.ref.tool.tokenizer.Tokenizer;
import edu.columbia.cs.utils.Span;


public class TokenBasedAnalyzer extends Analyzer {

	private Set<String> stopWords;
	
	/**
	 * Instantiates a new token based analyzer.
	 *
	 * @param stopWords the stop words that are not going to be indexed and therefore searched.
	 */
	public TokenBasedAnalyzer(Set<String> stopWords) {
		this.stopWords = stopWords;
	}

	/* (non-Javadoc)
	 * @see org.apache.lucene.analysis.Analyzer#tokenStream(java.lang.String, java.io.Reader)
	 */
	@Override
	public TokenStream tokenStream(String fieldName, Reader reader) {
		
		TokenBasedReader tbReader = (TokenBasedReader)reader;
		
		Span[] spans = tbReader.getSpans();
		
		String[] strings = tbReader.getStrings();
	
		return new StopFilter(Version.LUCENE_34, new LowerCaseFilter(Version.LUCENE_34, new SpanBasedTokenizer(spans, strings)), stopWords);
			
	}

	/**
	 * Creates an instance of the reader used to create the TokenStream required by Lucene.
	 *
	 * @param tokenizedSpans the spans found in the stream to be indexed or searched. 
	 * @param tokenizedString the token values matching the spans in tokenizedSpans.
	 * @return the reader
	 */
	public Reader getReader(Span[] tokenizedSpans, String[] tokenizedString) {
		
		return new TokenBasedReader(tokenizedSpans, tokenizedString);
	
	}

}