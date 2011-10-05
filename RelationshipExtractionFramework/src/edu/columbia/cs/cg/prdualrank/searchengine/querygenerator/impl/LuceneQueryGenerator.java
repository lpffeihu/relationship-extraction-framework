package edu.columbia.cs.cg.prdualrank.searchengine.querygenerator.impl;

import java.util.Collection;
import java.util.List;

import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.PhraseQuery;
import org.apache.lucene.search.Query;

import edu.columbia.cs.cg.document.Document;
import edu.columbia.cs.cg.document.TokenizedDocument;
import edu.columbia.cs.cg.pattern.Pattern;
import edu.columbia.cs.cg.pattern.prdualrank.SearchPattern;
import edu.columbia.cs.cg.prdualrank.searchengine.querygenerator.QueryGenerator;
import edu.columbia.cs.cg.relations.Entity;
import edu.columbia.cs.cg.relations.Relationship;

public class LuceneQueryGenerator extends QueryGenerator<Query> {

	@Override
	public Query generateQuery(Relationship relationship) {
		// TODO Implement before trying over local collection
		return null;
	}

	@Override
	public Query generateQuery(Entity role) {
		// TODO Implement before trying over local collection
		return null;
	}

	@Override
	public Query generateQuery(
			SearchPattern<Document, TokenizedDocument> pattern) {
		
		List<String[]> nGrams = pattern.getNGrams();
		
		BooleanQuery bq = new BooleanQuery();
		
		for (String[] strings : nGrams) {
			
			Query nGramQuery = generateNGramQuery(strings);
			
			bq.add(nGramQuery, Occur.MUST);
			
		}
		
		return bq;
	}

	private Query generateNGramQuery(String[] strings) {
		
		PhraseQuery pq = new PhraseQuery();
		
        // Build a Query object
		for (int i = 0; i < strings.length; i++) {
			pq.add(new Term("content",strings[i]));
		}
		
		return pq;
	}



}
