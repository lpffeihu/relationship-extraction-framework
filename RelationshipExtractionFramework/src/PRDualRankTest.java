import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.search.Query;

import edu.columbia.cs.cg.document.Document;
import edu.columbia.cs.cg.document.TokenizedDocument;
import edu.columbia.cs.cg.document.loaders.impl.RawDocumentLoader;
import edu.columbia.cs.cg.document.preprocessing.Preprocessor;
import edu.columbia.cs.cg.document.preprocessing.impl.HTMLContentKeeper;
import edu.columbia.cs.cg.document.segmentator.DocumentSegmentator;
import edu.columbia.cs.cg.document.segmentator.impl.SimpleSegmentDocumentSegmentator;
import edu.columbia.cs.cg.document.tagger.Tagger;
import edu.columbia.cs.cg.document.tagger.entity.impl.DictionaryBasedEntityTagger;
import edu.columbia.cs.cg.document.tokenized.tokenizer.OpenNLPTokenizer;
import edu.columbia.cs.cg.document.tokenized.tokenizer.Tokenizer;
import edu.columbia.cs.cg.pattern.Pattern;
import edu.columbia.cs.cg.prdualrank.PRDualRank;
import edu.columbia.cs.cg.prdualrank.index.analyzer.TokenBasedAnalyzer;
import edu.columbia.cs.cg.prdualrank.index.analyzer.TokenizerBasedAnalyzer;
import edu.columbia.cs.cg.prdualrank.inference.convergence.impl.NumberOfIterationsConvergence;
import edu.columbia.cs.cg.prdualrank.inference.quest.QuestCalculator;
import edu.columbia.cs.cg.prdualrank.inference.quest.impl.MapBasedQuestCalculator;
import edu.columbia.cs.cg.prdualrank.inference.ranking.RankFunction;
import edu.columbia.cs.cg.prdualrank.inference.ranking.impl.FScoreBasedRankFunction;
import edu.columbia.cs.cg.prdualrank.pattern.extractor.PatternExtractor;
import edu.columbia.cs.cg.prdualrank.pattern.extractor.impl.ExtractionPatternExtractor;
import edu.columbia.cs.cg.prdualrank.pattern.extractor.impl.WindowedSearchPatternExtractor;
import edu.columbia.cs.cg.prdualrank.searchengine.SearchEngine;
import edu.columbia.cs.cg.prdualrank.searchengine.impl.BingSearchEngine;
import edu.columbia.cs.cg.prdualrank.searchengine.querygenerator.QueryGenerator;
import edu.columbia.cs.cg.prdualrank.searchengine.querygenerator.impl.ConcatQueryGenerator;
import edu.columbia.cs.cg.prdualrank.searchengine.querygenerator.impl.LuceneQueryGenerator;
import edu.columbia.cs.cg.relations.Entity;
import edu.columbia.cs.cg.relations.Relationship;
import edu.columbia.cs.cg.relations.RelationshipType;
import edu.columbia.cs.cg.relations.constraints.relations.RelationshipConstraint;
import edu.columbia.cs.cg.relations.constraints.relations.WordDistanceBetweenEntities;
import edu.columbia.cs.cg.relations.constraints.roles.EntityTypeConstraint;
import edu.columbia.cs.cg.relations.entity.matcher.EntityMatcher;
import edu.columbia.cs.cg.relations.entity.matcher.impl.DictionaryEntityMatcher;
import edu.columbia.cs.model.Model;
import edu.columbia.cs.og.structure.OperableStructure;
import edu.columbia.cs.og.structure.impl.RelationOperableStructure;
import edu.columbia.cs.utils.Dictionary;
import edu.columbia.cs.utils.Words;


public class PRDualRankTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		int extractionPatternLenght = 5;
		int numberOfPhrases = 2;
		int iterations = 3;
		int window = 5;
		int minsupport = 5;
		int k_nolabel = 50;
		int k_seed = 20;
		int ngram = 3;
		int span = 10;

		String locationsFile = "data/location.txt";
		Dictionary locationsdictionary = new Dictionary(new File(locationsFile), ";","location");
		
		//countries Dictionary
		String countriesFile = "data/country.txt";
		Dictionary countriessdictionary = new Dictionary(new File(countriesFile), ";","country");

		//capitals Dictionary
		String capitalsFile = "data/capital.txt";
		Dictionary capitalsdictionary = new Dictionary(new File(capitalsFile), ";","capital");
		
		//Tokenizer used for the tokenization. Ideally the same.
		Tokenizer tokenizer = new OpenNLPTokenizer("en-token.bin");
		
		String countryRole = "country";
		String capitalRole = "capital";
		//Type of tuples to extract
		RelationshipType rType = new RelationshipType("capitalOf", countryRole,capitalRole);
		
		String locationType = "location";
		
		EntityTypeConstraint countryConstraint = new EntityTypeConstraint(locationType);
		rType.setConstraints(countryConstraint, countryRole);
		
		EntityTypeConstraint capitalConstraint = new EntityTypeConstraint(locationType);
		rType.setConstraints(capitalConstraint, capitalRole);
		
		RelationshipConstraint constraint = new WordDistanceBetweenEntities(tokenizer, span);
		
		rType.setConstraints(constraint);
		
		EntityMatcher countryMatcher = new DictionaryEntityMatcher(locationsdictionary);
		rType.setMatchers(countryMatcher, countryRole);
		
		EntityMatcher capitalMatcher = new DictionaryEntityMatcher(locationsdictionary);
		rType.setMatchers(capitalMatcher, capitalRole);
		
		
		Set<RelationshipType> relationshipTypes = new HashSet<RelationshipType>();
		relationshipTypes.add(rType);
		
		//How to segment the html documents
		DocumentSegmentator docSegmentator = new SimpleSegmentDocumentSegmentator();
		
		//Dictionary for Country
		Tagger countryDictionaryTagger = new DictionaryBasedEntityTagger(locationType, countriessdictionary, tokenizer);
		
		//Dictionary for Capitals
		Tagger capitalDictionaryTagger = new DictionaryBasedEntityTagger(locationType, capitalsdictionary, tokenizer );
		
		//Preprocessor for html documents;
		Preprocessor preprocessor = new HTMLContentKeeper();
		
		//Loader from string to Document.
		RawDocumentLoader loader = new RawDocumentLoader(relationshipTypes, preprocessor , docSegmentator, countryDictionaryTagger, capitalDictionaryTagger);
		
		//Generation of queries based on Concatenation
		QueryGenerator<String> qg = new ConcatQueryGenerator();

		//Bing Search Engine
		SearchEngine se = new BingSearchEngine(loader);
		
		//Ranking functions
		double betaextr = 1.0;
		RankFunction<Pattern<Relationship, TokenizedDocument>> extractpatternRankFunction = new FScoreBasedRankFunction<Pattern<Relationship,TokenizedDocument>>(betaextr);
		double betatup = 1.0;
		RankFunction<Relationship> tupleRankFunction = new FScoreBasedRankFunction<Relationship>(betatup);
		double betasearch = 1.0;
		RankFunction<Pattern<Document, TokenizedDocument>> searchpatternRankFunction = new FScoreBasedRankFunction<Pattern<Document,TokenizedDocument>>(betasearch);
				
		Words.initialize(new File("data/stopWords.txt"), null);
		
		//Index And Search.
		
		Set<String> stopW = Words.getStopWords();
		
//		TokenizerBasedAnalyzer myTokenizerAnalyzer = new TokenizerBasedAnalyzer(tokenizer,stopW);

		TokenBasedAnalyzer myAnalyzer = new TokenBasedAnalyzer(stopW);

		QueryGenerator<Query> forIndexQueryGenerator = new LuceneQueryGenerator(myAnalyzer);
		
		PatternExtractor<Document> spe = new WindowedSearchPatternExtractor<Document>(window, ngram, numberOfPhrases);
		
		QuestCalculator<Document, TokenizedDocument> spqc = new MapBasedQuestCalculator<Document,TokenizedDocument>(new NumberOfIterationsConvergence(iterations));
		
		PatternExtractor<Relationship> epe = new ExtractionPatternExtractor<Relationship>(span,extractionPatternLenght,rType);
		
		QuestCalculator<Relationship,TokenizedDocument> epqc = new MapBasedQuestCalculator<Relationship,TokenizedDocument>(new NumberOfIterationsConvergence(iterations));
		
		PRDualRank prDualRank = new PRDualRank(spe, epe, se, qg, k_seed, minsupport, k_nolabel, searchpatternRankFunction, 
				extractpatternRankFunction, tupleRankFunction, tokenizer, rType, myAnalyzer,forIndexQueryGenerator,spqc,epqc);
	
		List<OperableStructure> seeds = new ArrayList<OperableStructure>();
	
		seeds.add(generateOperableStructure(rType,"1",locationType,countryRole,"Canada",capitalRole,"Ottawa"));
//		seeds.add(generateOperableStructure(rType,"2",locationType,countryRole,"China",capitalRole,"Beijing"));
//		seeds.add(generateOperableStructure(rType,"3",locationType,countryRole,"Bulgaria",capitalRole,"Sofia"));
//		seeds.add(generateOperableStructure(rType,"4",locationType,countryRole,"France",capitalRole,"Paris"));
//		seeds.add(generateOperableStructure(rType,"5",locationType,countryRole,"Portugal",capitalRole,"Lisbon"));
		
		Model out = prDualRank.train(seeds);
		
		System.out.println(out.toString());
		
	}

	private static OperableStructure generateOperableStructure(RelationshipType rType, String id, String entityType,String countryRole, String country, String capitalRole, String capital) {
		
		Relationship r1 = new Relationship(rType);
		Entity countryE = new Entity(id, entityType, 0, country.length(), country, null);
		r1.setRole(countryRole, countryE);
		Entity capitalE = new Entity(id, entityType, 0, capital.length(), capital, null);
		r1.setRole(capitalRole, capitalE);
		return new RelationOperableStructure(r1);
		
	}

}
