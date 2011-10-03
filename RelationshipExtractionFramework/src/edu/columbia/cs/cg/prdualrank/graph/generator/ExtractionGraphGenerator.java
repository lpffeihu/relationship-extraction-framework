package edu.columbia.cs.cg.prdualrank.graph.generator;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import edu.columbia.cs.cg.document.Document;
import edu.columbia.cs.cg.document.TokenizedDocument;
import edu.columbia.cs.cg.pattern.Pattern;
import edu.columbia.cs.cg.relations.Relationship;

public class ExtractionGraphGenerator<T extends Relationship, D extends TokenizedDocument> extends GraphGenerator<Relationship,TokenizedDocument>{

	@Override
	protected Map<Relationship, Integer> findTuples(TokenizedDocument document,
			Pattern<Relationship,TokenizedDocument> pattern) {
		
		List<Relationship> tuples = pattern.findMatch(document);
		
		Map<Relationship, Integer> relationshipMap = new HashMap<Relationship, Integer>();
		
		for (Relationship relationship : tuples) {
			
			Integer freq = relationshipMap.get(relationship);
			
			if (freq == null){
				freq = 0;
			}
			
			relationshipMap.put(relationship, freq + 1);
			
		}
		
		return relationshipMap;
	
	}

}
