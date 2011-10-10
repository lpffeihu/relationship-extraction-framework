/**
 * Analyzer for Lucene based on a particular instance of a Tokenizer.
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

import edu.columbia.cs.cg.document.tokenized.tokenizer.Tokenizer;
import edu.columbia.cs.cg.prdualrank.index.reader.TokenBasedReader;
import edu.columbia.cs.cg.prdualrank.index.tokenizer.SpanBasedTokenizer;
import edu.columbia.cs.utils.Span;

public class TokenizerBasedAnalyzer extends Analyzer {

	private Tokenizer tokenizer;
	private Set<String> stopWords;

	/**
	 * Instantiates a new tokenizer based analyzer.
	 *
	 * @param tokenizer the tokenizer to be used to tokenize the stream
	 * @param stopWords the stop words set to be used during indexing.
	 */
	public TokenizerBasedAnalyzer(Tokenizer tokenizer, Set<String> stopWords){
		this.tokenizer = tokenizer;
		this.stopWords = stopWords;
	}
	
	/* (non-Javadoc)
	 * @see org.apache.lucene.analysis.Analyzer#tokenStream(java.lang.String, java.io.Reader)
	 */
	@Override
	public TokenStream tokenStream(String fieldName, Reader reader) {
		
		String content = "";
		
		try {
		
			BufferedReader br = new BufferedReader(reader);
			
			String line = br.readLine();;
				
			if (line != null)
				content = line;
					
			while ((line=br.readLine())!=null){
				
				content = content + "\n" + line;
				
			}
		
			br.close();
		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Span[] spans = tokenizer.tokenize(content);
		
		String[] strings = new String[spans.length];
		
		for (int i = 0; i < spans.length; i++) {
			
			strings[i] = content.substring(spans[i].getStart(),spans[i].getEnd());
			
		}
	
		return new StopFilter(Version.LUCENE_34, new LowerCaseFilter(Version.LUCENE_34, new SpanBasedTokenizer(spans, strings)), stopWords);
		
	}

}
