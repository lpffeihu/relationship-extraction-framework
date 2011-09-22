package edu.columbia.cs.og.features.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import edu.columbia.cs.cg.candidates.CandidateSentence;
import edu.columbia.cs.cg.relations.Entity;
import edu.columbia.cs.og.features.CandidateSentenceFeatureGenerator;
import edu.columbia.cs.og.features.featureset.FeatureSet;
import edu.columbia.cs.og.features.featureset.SequenceFS;
import edu.columbia.cs.utils.Span;

public class EntitySplitsFG extends
		CandidateSentenceFeatureGenerator {

	@Override
	protected FeatureSet process(CandidateSentence candidateSentence) {
		
		List<Span> spansList = new ArrayList<Span>();
		
		SequenceFS<String> tokens = (SequenceFS<String>)candidateSentence.getSentence().getFeatures(OpenNLPStringTokenizationFG.class);

		SequenceFS<Span> spans = (SequenceFS<Span>)candidateSentence.getSentence().getFeatures(OpenNLPTokenizationFG.class);
		
		int sentenceOffset = candidateSentence.getSentence().getOffset();
		
		Entity[] entities = candidateSentence.getEntities();
		
		Arrays.sort(entities, new Comparator<Entity>() {

			@Override
			public int compare(Entity entity1, Entity entity2) {
				
				return new Integer(entity1.getLength()).compareTo(entity2.getLength());
				
			}
		});
		
		//Has to generate:
		//first
		//index lenght

		int indexStart1 = findIndex(entities[0].getOffset()-sentenceOffset,spans);
		
		spansList.add(new Span(0, (int)Math.max(indexStart1-1,0)));
		
//		String[] beforeWords = new String[indexStart1];
//		
//		int[] beforeIndexes = new int[beforeWords.length];
//		
//		for (int i = 0; i < indexStart1; i++) {
//			
//			beforeWords[i] = tokens.getElement(i);
//			
//			beforeIndexes[i] = i;
//			
//		}
		
		int indexEnd1 = findIndex(entities[0].getOffset() + entities[0].getLength() - sentenceOffset,spans);

		spansList.add(new Span(indexStart1, indexEnd1));
		
//		String[] firstWords = new String[indexEnd1-indexStart1+1];
//		
//		int[] firstIndexes = new int[firstWords.length];
//		
//		for (int i = indexStart1; i <= indexEnd1; i++) {
//			
//			firstIndexes[i-indexStart1] = i;
//			firstWords[i-indexStart1] = tokens.getElement(i);
//			
//		}
		
		//middle 
		//index lenght

		int indexStart2 = findIndex(entities[1].getOffset() - sentenceOffset,spans);

		spansList.add(new Span(indexEnd1+1, indexStart2-1));
		
//		String[] middleWords = new String[indexStart2 - indexEnd1 - 1];
//		int[] middleIndexes = new int[middleWords.length];
//		
//		for (int i = indexEnd1+1; i < indexStart2; i++) {
//			
//			middleIndexes[i-(indexEnd1+1)] = i;
//			
//			middleWords[i-(indexEnd1+1)] = tokens.getElement(i);
//			
//		}
		
		//last
		//index lenght
		
		int indexEnd2 = findIndex(entities[1].getOffset() - sentenceOffset,spans);

		spansList.add(new Span(indexStart2,indexEnd2));
		
//		String[] lastWords = new String[indexEnd2-indexStart2+1];
//		int[] lastIndexes = new int[lastWords.length];		
//		
//		for (int i = indexStart2; i <= indexEnd2; i++) {
//			
//			lastIndexes[i-indexStart2] = i;
//			
//			lastWords[i-indexStart2] = tokens.getElement(i);
//			
//		}
//		
//		String[] finalWords = new String[tokens.size()-indexEnd2];
//		
//		int[] finalIndexes = new int[finalWords.length];
//		
//		for (int i = indexEnd2 + 1; i < finalIndexes.length; i++) {
//			
//			finalIndexes[i - (indexEnd2+1)] = i;
//			finalWords[i-(indexEnd2+1)] = tokens.getElement(i);
//			
//		}
		
		spansList.add(new Span(indexEnd2+1,tokens.size()-1));

		return new SequenceFS<Span>(spansList.toArray(new Span[0]));

	}

	private int findIndex(int index, SequenceFS<Span> spans) {
		
		for (int i = 0; i < spans.size(); i++) {
			if (index>=spans.getElement(i).getStart() && index <=spans.getElement(i).getEnd())
				return i;
		}
		return -1;
	}

}
