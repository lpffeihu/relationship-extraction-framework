package edu.columbia.cs.cg.prdualrank.graph.generator;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;

import edu.columbia.cs.cg.document.Document;
import edu.columbia.cs.cg.pattern.Pattern;
import edu.columbia.cs.cg.pattern.matchable.Matchable;
import edu.columbia.cs.cg.prdualrank.graph.PRDualRankGraph;
import edu.columbia.cs.cg.relations.Relationship;

public abstract class GraphGenerator<T extends Matchable,D extends Document> {

	public PRDualRankGraph<T,D> generateGraph(Set<Relationship> topTuples,
			Set<Pattern<T,D>> patterns, Set<D> documents) {
		
		PRDualRankGraph<T,D> ret = new PRDualRankGraph<T,D>();
		
		for (D document : documents) {
			
			for (Pattern<T,D> pattern : patterns) {
				
				Map<Relationship, Integer> tuples = findTuples(document,pattern);
				
				for (Relationship tuple : tuples.keySet()) {
					
					ret.addContext(pattern,tuple,tuples.get(tuple));
					
				}
				
			}
			
		}
		
		return ret;
		
	}

	protected abstract Map<Relationship, Integer> findTuples(D document,
			Pattern<T,D> pattern);
	
}
