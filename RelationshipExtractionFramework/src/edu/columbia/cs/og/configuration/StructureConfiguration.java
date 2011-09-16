package edu.columbia.cs.og.configuration;

import java.util.ArrayList;
import java.util.List;

import edu.columbia.cs.cg.candidates.CandidateSentence;
import edu.columbia.cs.cg.sentence.Sentence;
import edu.columbia.cs.og.core.Core;
import edu.columbia.cs.og.features.FeatureGenerator;
import edu.columbia.cs.og.structure.OperableStructure;

public class StructureConfiguration {
	private List<FeatureGenerator> userFg;
	private Core classificationCore;
	private List<Class<? extends FeatureGenerator>> userFgKeys;
	
	public StructureConfiguration(Core cCore){
		classificationCore=cCore;
		userFg=new ArrayList<FeatureGenerator>();
		userFgKeys=new ArrayList<Class<? extends FeatureGenerator>>();
	}
	
	public void addFeatureGenerator(FeatureGenerator fg){
		userFg.add(fg);
		userFgKeys.add(fg.getKey());
	}
	
	public OperableStructure getOperableStructure(CandidateSentence sent){
		OperableStructure newStructure = classificationCore.getStructure(sent);
		
		int numFeatures = userFg.size();
		for(int i=0; i<numFeatures; i++){
			userFg.get(i).generateFeatures(newStructure);
		}
		newStructure.enrich(userFgKeys);
		
		return newStructure;
	}
}
