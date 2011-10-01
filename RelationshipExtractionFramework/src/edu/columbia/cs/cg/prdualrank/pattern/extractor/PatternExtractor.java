package edu.columbia.cs.cg.prdualrank.pattern.extractor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.columbia.cs.cg.document.Document;
import edu.columbia.cs.cg.pattern.Pattern;
import edu.columbia.cs.cg.relations.Relationship;

public abstract class PatternExtractor {

	public Map<Pattern,Integer> extractPatterns(List<Document> documents, Relationship relationship) {
		
		Map<Pattern,Integer> patternsFrequency = new HashMap<Pattern, Integer>();
		
		for (Document document : documents) {
			
			updatePatternsFrequency(patternsFrequency,extract(document,relationship));
			
		}
		
		return patternsFrequency;

	}

	private void updatePatternsFrequency(
			Map<Pattern, Integer> patternsFrequency,
			Map<Pattern, Integer> extracted) {
		
		for (Pattern pattern : extracted.keySet()) {
			
			Integer accFreq = patternsFrequency.get(pattern);
			
			if (accFreq == null){
				accFreq = 0;
			}
			
			patternsFrequency.put(pattern, accFreq + extracted.get(pattern));
			
		}
		
	}

	protected abstract Map<Pattern,Integer> extract(Document document, Relationship relationship);

}
