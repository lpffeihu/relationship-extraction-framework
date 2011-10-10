/**
 * 
 *
 * @author      Pablo Barrio
 * @author		Goncalo Simoes
 * @version     0.1
 * @since       2011-10-07
 */
package edu.columbia.cs.cg.prdualrank.searchengine;

import java.util.List;

import edu.columbia.cs.cg.document.Document;

public interface SearchEngine {

	/**
	 * Issues query into the search engine and retrieves either k_seed or all the documents in the result set, whichever number is the lower.  
	 *
	 * @param query the query to be issued
	 * @param k_seed the number of documents to be retrieved if lower than the total number of hits. All the results are retrieved otherwise.
	 * @return the list of documents contained in the result set.
	 */
	public List<Document> search(String query, int k_seed);

}
