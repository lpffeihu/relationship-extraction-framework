/**
 * Query generator based in the simple concatenation of the strings to be searched.
 * 
 * @author      Pablo Barrio
 * @author		Goncalo Simoes
 * @version     0.1
 * @since       2011-10-07
 */
package edu.columbia.cs.cg.prdualrank.searchengine.querygenerator.impl;

import java.util.Collection;

import com.google.gdata.util.NotImplementedException;

import edu.columbia.cs.cg.document.Document;
import edu.columbia.cs.cg.document.TokenizedDocument;
import edu.columbia.cs.cg.pattern.prdualrank.SearchPattern;
import edu.columbia.cs.cg.prdualrank.searchengine.querygenerator.QueryGenerator;
import edu.columbia.cs.cg.relations.Entity;
import edu.columbia.cs.cg.relations.Relationship;

public class ConcatQueryGenerator extends QueryGenerator<String> {

	/* (non-Javadoc)
	 * @see edu.columbia.cs.cg.prdualrank.searchengine.querygenerator.QueryGenerator#generateQuery(edu.columbia.cs.cg.relations.Relationship)
	 */
	public String generateQuery(Relationship relationship) {
		
		String ret = "";
		
		Collection<String> roles = relationship.getRoles();
		
		if (roles.size() == 0)
			return "";
		
		for (String role : roles) {
			
			ret = ret + " " + relationship.getRole(role).getValue(); 
			
		}
		
		return ret.substring(1);
		
	}

	/* (non-Javadoc)
	 * @see edu.columbia.cs.cg.prdualrank.searchengine.querygenerator.QueryGenerator#generateQuery(edu.columbia.cs.cg.relations.Entity)
	 */
	public String generateQuery(Entity role) {
		return role.getValue();
	}

	/* (non-Javadoc)
	 * @see edu.columbia.cs.cg.prdualrank.searchengine.querygenerator.QueryGenerator#generateQuery(edu.columbia.cs.cg.pattern.prdualrank.SearchPattern)
	 */
	@Override
	public String generateQuery(
			SearchPattern<Document, TokenizedDocument> pattern) {
		// TODO Implement before trying over the internet
		return null;
	}
	
}
