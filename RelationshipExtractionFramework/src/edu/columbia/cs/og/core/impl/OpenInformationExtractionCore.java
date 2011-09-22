package edu.columbia.cs.og.core.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import opennlp.tools.util.InvalidFormatException;

import edu.columbia.cs.cg.candidates.CandidateSentence;
import edu.columbia.cs.og.core.Core;
import edu.columbia.cs.og.features.DependentFeatureGenerator;
import edu.columbia.cs.og.features.FeatureGenerator;
import edu.columbia.cs.og.features.impl.EntitySplitsFG;
import edu.columbia.cs.og.features.impl.KnowItAllChunkingFG;
import edu.columbia.cs.og.features.impl.OpenNLPPartOfSpeechFG;
import edu.columbia.cs.og.features.impl.OpenNLPStringTokenizationFG;
import edu.columbia.cs.og.features.impl.OpenNLPTokenizationFG;
import edu.columbia.cs.og.structure.OperableStructure;
import edu.columbia.cs.og.structure.impl.OpenInformationExtractionOS;

public class OpenInformationExtractionCore extends Core {

	@Override
	protected List<FeatureGenerator> createMandatoryFeatureGenerators() {
		// TODO OpenNLPtoken Chunks entitySplits
		
		List<FeatureGenerator> features = new ArrayList<FeatureGenerator>();
		
		FeatureGenerator tokens;
		try {
			tokens = new OpenNLPStringTokenizationFG("en-token.bin");
			
			FeatureGenerator tokenSpans = new OpenNLPTokenizationFG("en-token.bin");
			
			FeatureGenerator pos = new OpenNLPPartOfSpeechFG("en-pos-maxent.bin");
			
			FeatureGenerator chunk = new KnowItAllChunkingFG();
			
			FeatureGenerator chunker = new DependentFeatureGenerator(chunk, tokens, pos);
			
			FeatureGenerator entitySplits = new EntitySplitsFG();
			
			FeatureGenerator entitySplitFG = new DependentFeatureGenerator(entitySplits, tokens, tokenSpans);
			
			features.add(tokens);
			
			features.add(pos);
			
			features.add(chunker);
			
			features.add(entitySplitFG);
			
			return features;
			
		} catch (InvalidFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
		

	}

	@Override
	protected OperableStructure createOperableStructure(CandidateSentence sent) {
		
		return new OpenInformationExtractionOS(sent);
	
	}

}
