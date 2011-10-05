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
import edu.columbia.cs.cg.prdualrank.index.tokenizer.SpanBasedTokenizer;
import edu.columbia.cs.utils.Span;


public class TokenizerBasedAnalyzer extends Analyzer {

	private Tokenizer tokenizer;
	private Set<String> stopWords;
	
	public TokenizerBasedAnalyzer(Tokenizer tokenizer, Set<String> stopWords) {
		this.tokenizer = tokenizer;
		this.stopWords = stopWords;
	}

	@Override
	public TokenStream tokenStream(String fieldName, Reader reader) {
		
		try {

			StringBuilder sb = new StringBuilder();
			
			BufferedReader br = new BufferedReader(reader);
		
			String line;
			
			while ((line = br.readLine())!=null){
				
				sb.append("\n" + line);
				
			}
			
			String content = sb.toString();
			
			Span[] spans = tokenizer.tokenize(content);
			
			br.close();
		
			return new StopFilter(Version.LUCENE_34, new LowerCaseFilter(Version.LUCENE_34, new SpanBasedTokenizer(spans, null/*content*/)), stopWords);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}

}
