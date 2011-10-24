package edu.columbia.cs.cg.prdualrank.pattern.extractor.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import edu.columbia.cs.cg.prdualrank.pattern.extractor.SearchPatternExtractor;
import edu.columbia.cs.cg.prdualrank.pattern.extractor.resource.TupleContext;
import edu.columbia.cs.ref.model.Document;
import edu.columbia.cs.ref.model.TokenizedDocument;
import edu.columbia.cs.ref.model.entity.Entity;
import edu.columbia.cs.ref.model.relationship.Relationship;
import edu.columbia.cs.utils.Span;

public class DocumentSearchPatternExtractor<T extends Document> extends SearchPatternExtractor<Document> {

	public DocumentSearchPatternExtractor(int ngram, int numberOfPhrases){
		
		super(ngram,numberOfPhrases);
		
	}
	
	@Override
	protected List<TupleContext> getTupleContexts(
			TokenizedDocument document, Relationship relationship,
			List<Relationship> matchingRelationships) {
		
		List<Span> tupleSpans = new ArrayList<Span>();
		
		for (Relationship matchingRelationship : matchingRelationships) {

			for (String role : matchingRelationship.getRoles()) {

				Entity entity = matchingRelationship.getRole(role);
				
				if (entity != Entity.NULL_ENTITY){
					//update indexes.
					
					Span auxSpan = document.getEntitySpan(entity);
					
					tupleSpans.add(auxSpan); //return the indexes whitin the array
					
				}
				
			}
			
		}
		
		if (tupleSpans.isEmpty())
			return new ArrayList<TupleContext>(0);
		
		List<Span> realSpans = Span.calculateNotOverlappingSpans(tupleSpans);

		TupleContext tc = new TupleContext(realSpans);
		
		String[] first = Arrays.copyOfRange(document.getTokenizedString(), 0, realSpans.get(0).getStart());
		
		tc.addWords(first);
		
		for (int i = 0; i < realSpans.size() - 1; i++) {
			
			String[] middle = Arrays.copyOfRange(document.getTokenizedString(), realSpans.get(i).getEnd()+1, Math.max(realSpans.get(i).getEnd(), realSpans.get(i+1).getStart()));
		
			tc.addWords(middle);
			
		}
		
		String[] last = Arrays.copyOfRange(document.getTokenizedString(), Math.min(realSpans.get(realSpans.size()-1).getEnd()+1, document.getTokenizedString().length-1), document.getTokenizedString().length);
		
		tc.addWords(last);
		
		List<TupleContext> ret = new ArrayList<TupleContext>(1);
		
		ret.add(tc);
		
		return ret;
		
	}

}
