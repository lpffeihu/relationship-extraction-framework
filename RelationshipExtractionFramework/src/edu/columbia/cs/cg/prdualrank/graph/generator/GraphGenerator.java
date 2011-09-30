package edu.columbia.cs.cg.prdualrank.graph.generator;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;

import edu.columbia.cs.cg.document.Document;
import edu.columbia.cs.cg.pattern.Pattern;
import edu.columbia.cs.cg.prdualrank.graph.PRDualRankGraph;
import edu.columbia.cs.cg.relations.Relationship;

public abstract class GraphGenerator {

	public PRDualRankGraph generateGraph(Set<Relationship> topTuples,
			Set<Pattern> patterns, Set<Document> documents) {
		
		PRDualRankGraph ret = new PRDualRankGraph();
		
		for (Document document : documents) {
			
			for (Pattern pattern : patterns) {
				
				Hashtable<Relationship, Integer> tuples = findTuples(document,pattern);
				
				for (Relationship tuple : tuples.keySet()) {
					
					ret.addContext(pattern,tuple,tuples.get(tuple));
					
				}
				
			}
			
		}
		
		return ret;
		
	}

	protected abstract Hashtable<Relationship, Integer> findTuples(Document document,
			Pattern pattern);

	
}
