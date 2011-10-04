package edu.columbia.cs.cg.prdualrank.searchengine;

import java.util.List;

import edu.columbia.cs.cg.document.Document;

public interface SearchEngine {

	public List<Document> search(String query, int k_seed);

}
