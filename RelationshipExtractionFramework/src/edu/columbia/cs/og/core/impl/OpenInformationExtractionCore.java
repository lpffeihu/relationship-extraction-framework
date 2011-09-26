package edu.columbia.cs.og.core.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import opennlp.tools.util.InvalidFormatException;

import edu.columbia.cs.cg.candidates.CandidateSentence;
import edu.columbia.cs.og.core.Core;
import edu.columbia.cs.og.features.FeatureGenerator;
import edu.columbia.cs.og.features.SentenceFeatureGenerator;
import edu.columbia.cs.og.features.featureset.SequenceFS;
import edu.columbia.cs.og.features.impl.EntitySplitsFG;
import edu.columbia.cs.og.features.impl.KnowItAllChunkingFG;
import edu.columbia.cs.og.features.impl.OpenNLPPartOfSpeechFG;
import edu.columbia.cs.og.features.impl.SpansToStringsConvertionFG;
import edu.columbia.cs.og.features.impl.OpenNLPTokenizationFG;
import edu.columbia.cs.og.structure.OperableStructure;
import edu.columbia.cs.og.structure.impl.OpenInformationExtractionOS;
import edu.columbia.cs.utils.Span;

public class OpenInformationExtractionCore extends Core {

	@Override
	protected List<FeatureGenerator> createMandatoryFeatureGenerators() {
		// TODO OpenNLPtoken Chunks entitySplits
		
		List<FeatureGenerator> features = new ArrayList<FeatureGenerator>();
		
		try {
			//TODO: SOMETHING WRONG!!!!!!!!!!!!!!!!!!!!!!!!!!!!
			
			FeatureGenerator<SequenceFS<Span>> tokenSpans = new OpenNLPTokenizationFG("en-token.bin");
			
			FeatureGenerator<SequenceFS<String>> tokens = new SpansToStringsConvertionFG(tokenSpans);
			
			FeatureGenerator<SequenceFS<String>> pos = new OpenNLPPartOfSpeechFG("en-pos-maxent.bin",tokens);
			
			FeatureGenerator<SequenceFS<String>> chunk = new KnowItAllChunkingFG(tokens,pos);
			
			//FeatureGenerator<SequenceFS<Span>> entitySplits = new EntitySplitsFG(chunk);
			
			features.add(tokens);
			
			features.add(pos);
			
			features.add(chunk);
			
			//features.add(entitySplits);
			
			return features;
			
		} catch (InvalidFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.exit(1);
		
		return null;
		

	}

	@Override
	protected OperableStructure createOperableStructure(CandidateSentence sent) {
		
		return new OpenInformationExtractionOS(sent);
	
	}

}
