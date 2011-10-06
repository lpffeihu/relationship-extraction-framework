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


public class TokenBasedAnalyzer extends Analyzer {

	private Set<String> stopWords;
	
	public TokenBasedAnalyzer(Set<String> stopWords) {
		this.stopWords = stopWords;
	}

	@Override
	public TokenStream tokenStream(String fieldName, Reader reader) {
		
		TokenBasedReader tbReader = (TokenBasedReader)reader;
		
		Span[] spans = tbReader.getSpans();
		
		String[] strings = tbReader.getStrings();
	
		return new StopFilter(Version.LUCENE_34, new LowerCaseFilter(Version.LUCENE_34, new SpanBasedTokenizer(spans, strings)), stopWords);
			
	}

	public Reader getReader(Span[] tokenizedSpans, String[] tokenizedString) {
		
		return new TokenBasedReader(tokenizedSpans, tokenizedString);
	
	}

}
