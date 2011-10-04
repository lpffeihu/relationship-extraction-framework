import edu.columbia.cs.cg.document.Document;
import edu.columbia.cs.cg.document.TokenizedDocument;
import edu.columbia.cs.cg.document.loaders.impl.RawDocumentLoader;
import edu.columbia.cs.cg.document.tokenized.tokenizer.Tokenizer;
import edu.columbia.cs.cg.pattern.Pattern;
import edu.columbia.cs.cg.prdualrank.PRDualRank;
import edu.columbia.cs.cg.prdualrank.inference.ranking.RankFunction;
import edu.columbia.cs.cg.prdualrank.searchengine.SearchEngine;
import edu.columbia.cs.cg.prdualrank.searchengine.impl.BingSearchEngine;
import edu.columbia.cs.cg.prdualrank.searchengine.querygenerator.QueryGenerator;
import edu.columbia.cs.cg.prdualrank.searchengine.querygenerator.impl.ConcatQueryGenerator;
import edu.columbia.cs.cg.relations.Relationship;


public class PRDualRankTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		int numberOfPhrases = 2;
		int iterations = 3;
		int window = 20;
		int minsupport = 5;
		int k_nolabel = 50;
		QueryGenerator qg = new ConcatQueryGenerator();
		int k_seed = 500;
		int ngram = 5;
		SearchEngine se = new BingSearchEngine(loader );
		Tokenizer tokenizer;
		RankFunction<Pattern<Relationship, TokenizedDocument>> extractpatternRankFunction;
		RankFunction<Relationship> tupleRankFunction;
		RankFunction<Pattern<Document, TokenizedDocument>> searchpatternRankFunction;
		int extractionPatternLenght;
		new PRDualRank(se, qg, k_seed, ngram, window, minsupport, k_nolabel, iterations, numberOfPhrases, 
				extractionPatternLenght, searchpatternRankFunction, extractpatternRankFunction, tupleRankFunction, tokenizer, rType);
		
	}

}
