package edu.columbia.cs.og.features.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import edu.columbia.cs.cg.candidates.CandidateSentence;
import edu.columbia.cs.cg.relations.Entity;
import edu.columbia.cs.og.features.CandidateSentenceFeatureGenerator;
import edu.columbia.cs.og.features.FeatureGenerator;
import edu.columbia.cs.og.features.featureset.FeatureSet;
import edu.columbia.cs.og.features.featureset.SequenceFS;
import edu.columbia.cs.utils.Span;

public class EntitySplitsFG extends
		CandidateSentenceFeatureGenerator<SequenceFS<Span>> {

	private FeatureGenerator<SequenceFS<Span>> tokenizer;

	public EntitySplitsFG(FeatureGenerator<SequenceFS<Span>> tokenizer){
		this.tokenizer = tokenizer;
	}
	
	@Override
	protected SequenceFS<Span> extractFeatures(CandidateSentence candidateSentence) {
		
		List<Span> spansList = new ArrayList<Span>();
		
		SequenceFS<Span> spans = candidateSentence.getSentence().getFeatures(tokenizer);
		
		int sentenceOffset = candidateSentence.getSentence().getOffset();
		
		Entity[] entities = candidateSentence.getEntities();
		
		Arrays.sort(entities, new Comparator<Entity>() {

			@Override
			public int compare(Entity entity1, Entity entity2) {
				
				return new Integer(entity1.getOffset()).compareTo(entity2.getOffset());
				
			}
		});
		
		//Has to generate:
		//first
		//index lenght

		int indexStart1 = findIndex(entities[0].getOffset()-sentenceOffset,spans);
		int indexEnd1 = findIndex(entities[0].getOffset() + entities[0].getLength() - sentenceOffset,spans);
		int indexStart2 = findIndex(entities[1].getOffset() - sentenceOffset,spans);
		int indexEnd2 = findIndex(entities[1].getOffset() - sentenceOffset,spans);
		
		spansList.add(new Span(0, (int)Math.max(indexStart1-1,0)));

		spansList.add(new Span(indexStart1, indexEnd1));
		
		//middle 
		//index lenght
		
		if(indexEnd1<indexStart2){
			spansList.add(new Span(indexEnd1+1, indexStart2-1));
		}else{
			spansList.add(new Span(indexStart2, indexStart2-1));
		}
		
		
		
		//last
		//index lenght
		
		spansList.add(new Span(indexStart2,indexEnd2));
		
		spansList.add(new Span(indexEnd2+1,spans.size()-1));

		return new SequenceFS<Span>(spansList.toArray(new Span[0]));

	}

	private int findIndex(int index, SequenceFS<Span> spans) {
		
		for (int i = 0; i < spans.size(); i++) {
			if (index>=spans.getElement(i).getStart() && index <=spans.getElement(i).getEnd())
				return i;
		}
		return -1;
	}

	@Override
	protected List<FeatureGenerator> retrieveRequiredFeatureGenerators() {
		
		ArrayList<FeatureGenerator> ret = new ArrayList<FeatureGenerator>();
		
		ret.add(tokenizer);
	
		return ret;
	}

}
