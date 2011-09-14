package edu.columbia.cs.og.core;

import java.util.List;

import edu.columbia.cs.cg.candidates.CandidateSentence;
import edu.columbia.cs.cg.sentence.Sentence;
import edu.columbia.cs.og.features.FeatureGenerator;
import edu.columbia.cs.og.structure.OperableStructure;

public abstract class Core {
	List<FeatureGenerator> mandatoryFG=null;
	
	public List<FeatureGenerator> getMandatoryFeatureGenerators(){
		if(mandatoryFG==null){
			mandatoryFG=createMandatoryFeatureGenerators();
		}
		return mandatoryFG;
	}
	
	protected abstract List<FeatureGenerator> createMandatoryFeatureGenerators();

	public OperableStructure getStructure(CandidateSentence sent){
		OperableStructure newStructure = createOperableStructure(sent);
		for(FeatureGenerator fg : getMandatoryFeatureGenerators()){
			fg.generateFeatures(newStructure);
		}
		newStructure.initialize();
		return newStructure;
	}
	
	protected abstract OperableStructure createOperableStructure(CandidateSentence sent);
}
