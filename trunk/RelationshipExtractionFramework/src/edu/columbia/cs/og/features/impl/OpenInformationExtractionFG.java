package edu.columbia.cs.og.features.impl;

import java.util.List;

import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import edu.columbia.cs.cg.candidates.CandidateSentence;
import edu.columbia.cs.cg.relations.RelationshipType;
import edu.columbia.cs.model.impl.WekaClassifierModel;
import edu.columbia.cs.og.features.CandidateSentenceFeatureGenerator;
import edu.columbia.cs.og.features.featureset.FeatureSet;
import edu.columbia.cs.og.features.featureset.SequenceFS;
import edu.columbia.cs.og.features.featureset.WekaInstanceFS;
import edu.columbia.cs.utils.Span;
import edu.washington.cs.knowitall.extractor.conf.BooleanFeatureSet;
import edu.washington.cs.knowitall.nlp.ChunkedSentence;
import edu.washington.cs.knowitall.nlp.extraction.ChunkedArgumentExtraction;
import edu.washington.cs.knowitall.nlp.extraction.ChunkedBinaryExtraction;
import edu.washington.cs.knowitall.nlp.extraction.ChunkedExtraction;
import edu.washington.cs.knowitall.util.Range;

public class OpenInformationExtractionFG extends
		CandidateSentenceFeatureGenerator {

	private BooleanFeatureSet<ChunkedBinaryExtraction> featureSet; //= new ReVerbFeatures().getFeatureSet();

	private int numFeatures;

	private FastVector attributes = null;
	
	public OpenInformationExtractionFG(BooleanFeatureSet<ChunkedBinaryExtraction> featureSet){
		
		this.featureSet = featureSet;
		
		numFeatures = featureSet.getNumFeatures();
		
		attributes = new FastVector(numFeatures+1);
		
		List<String> featureNames = featureSet.getFeatureNames();
		for (int i = 0; i < numFeatures; i++) {
			attributes.addElement(new Attribute(featureNames.get(i)));
			
		}		
		
		//just for optimization, we add the class attribute.
		
		FastVector classVals = new FastVector(2);
		
		classVals.addElement(WekaClassifierModel.POSITIVE_LABEL);
		
		classVals.addElement(WekaClassifierModel.NEGATIVE_LABEL);
		
		Attribute classAttr = new Attribute("class", classVals);
		
		attributes.addElement(classAttr);

	}
	
	@Override
	
	protected FeatureSet extractFeatures(CandidateSentence candidateSentence) {
		
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
				
		Instance inst = new Instance(numFeatures + 1);
		double[] featureVals = featureSet.featurizeToDouble(labeled);
		for (int i = 0; i < numFeatures; i++) {
			inst.setValue((Attribute)attributes.elementAt(i), featureVals[i]);
		}

		//just for optimization, we add the class attribute.
		
		inst.setValue((Attribute)attributes.elementAt(numFeatures), generateLabel(candidateSentence.getLabel()));
		
		return new WekaInstanceFS(inst);

	}

	private String generateLabel(String label) {
		
		if (label.equals(RelationshipType.NOT_A_RELATIONSHIP)){
			return WekaClassifierModel.NEGATIVE_LABEL;
		}
	
		return WekaClassifierModel.POSITIVE_LABEL;
		
	}

	private String[] generateArray(SequenceFS<String> sequence) {
		
		return sequence.toArray();
		
	}

	private Range readRange(Span span){
		int start = span.getStart();
		int length = span.getEnd() - span.getStart() + 1;
		return new Range(start, length);
	}

}