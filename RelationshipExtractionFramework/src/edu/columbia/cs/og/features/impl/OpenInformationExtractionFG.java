package edu.columbia.cs.og.features.impl;

import java.io.IOException;
import java.util.List;

import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;
import edu.columbia.cs.cg.candidates.CandidateSentence;
import edu.columbia.cs.og.features.CandidateSentenceFeatureGenerator;
import edu.columbia.cs.og.features.featureset.FeatureSet;
import edu.columbia.cs.og.features.featureset.SequenceFS;
import edu.columbia.cs.og.features.featureset.WekaInstanceFS;
import edu.columbia.cs.utils.Span;
import edu.washington.cs.knowitall.extractor.conf.BooleanFeatureSet;
import edu.washington.cs.knowitall.extractor.conf.ReVerbFeatures;
import edu.washington.cs.knowitall.nlp.ChunkedSentence;
import edu.washington.cs.knowitall.nlp.extraction.ChunkedArgumentExtraction;
import edu.washington.cs.knowitall.nlp.extraction.ChunkedBinaryExtraction;
import edu.washington.cs.knowitall.nlp.extraction.ChunkedExtraction;
import edu.washington.cs.knowitall.util.Range;

public class OpenInformationExtractionFG extends
		CandidateSentenceFeatureGenerator {

	//Should receive different types of FeatureSets...
	
	private static BooleanFeatureSet<ChunkedBinaryExtraction> featureSet = new ReVerbFeatures().getFeatureSet();
	private static int numFeatures = featureSet.getNumFeatures();
	private static FastVector attributes = null;
	@Override
	protected FeatureSet process(CandidateSentence candidateSentence) {
		
		if (attributes == null){
			initializeAttributes();
		}
		
		SequenceFS<String> tokensString = (SequenceFS<String>)candidateSentence.getSentence().getFeatures(OpenNLPStringTokenizationFG.class);
		
		String[] tokens = generateArray(tokensString);

		SequenceFS<String> pos = (SequenceFS<String>)candidateSentence.getSentence().getFeatures(OpenNLPPartOfSpeechFG.class);
		
		String[] posTags = generateArray(pos);

		SequenceFS<String> chunks = (SequenceFS<String>)candidateSentence.getSentence().getFeatures(KnowItAllChunkingFG.class);

		String[] npChunkTags = generateArray(chunks); 
		
		ChunkedSentence sent = new ChunkedSentence(tokens, posTags, npChunkTags);

		SequenceFS<Span> span = (SequenceFS<Span>)candidateSentence.getFeatures(EntitySplitsFG.class);
		
		// Next two lines define arg1: first is the tokens, then is the range. Only need 
		// the range to construct the extraction.
		Range arg1Range = readRange(span.getElement(1));
		
		// Same for the relation and arg2
		Range relRange = readRange(span.getElement(2));
		
		Range arg2Range = readRange(span.getElement(3));
		
		// Construct the extraction
		ChunkedExtraction rel = new ChunkedExtraction(sent, relRange);
		ChunkedArgumentExtraction arg1 = new ChunkedArgumentExtraction(sent, arg1Range, rel);
		ChunkedArgumentExtraction arg2 = new ChunkedArgumentExtraction(sent, arg2Range, rel);
		ChunkedBinaryExtraction labeled = new ChunkedBinaryExtraction(rel, arg1, arg2);
				
		Instance inst = new Instance(numFeatures);
		double[] featureVals = featureSet.featurizeToDouble(labeled);
		for (int i = 0; i < numFeatures; i++) {
			inst.setValue((Attribute)attributes.elementAt(i), featureVals[i]);
		}

		return new WekaInstanceFS(inst);

//		private void initializeWekaObjects() {
//			numFeatures = featureSet.getNumFeatures();
//			List<String> featureNames = featureSet.getFeatureNames();
//			attributes = new FastVector(numFeatures + 1); // +1 for class attribute
//			// Construct a numeric attribute for each feature in the set
//			for (int i = 0; i < numFeatures; i++) {
//				Attribute featureAttr = new Attribute(featureNames.get(i));
//				attributes.addElement(featureAttr);
//			}
//			FastVector classVals = new FastVector(2);
//			classVals.addElement("positive");
//			classVals.addElement("negative");
//			Attribute classAttr = new Attribute("class", classVals);
//			attributes.addElement(classAttr);
//			instances = new Instances(INST_NAME, attributes, 0);
//			instances.setClassIndex(numFeatures);
//		}
		
	}

	private String[] generateArray(SequenceFS<String> sequence) {
		
		return sequence.toArray();
		
	}

	private void initializeAttributes() {
		attributes = new FastVector(numFeatures);
		List<String> featureNames = featureSet.getFeatureNames();
		for (int i = 0; i < numFeatures; i++) {
			attributes.addElement(new Attribute(featureNames.get(i)));
		}
	}

	private Range readRange(Span span){
		int start = span.getStart();
		int length = span.getEnd() - span.getStart() + 1;
		return new Range(start, length);
	}

}
