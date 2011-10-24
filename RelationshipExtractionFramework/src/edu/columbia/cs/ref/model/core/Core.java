package edu.columbia.cs.ref.model.core;

import java.io.Serializable;
import java.util.List;

import edu.columbia.cs.ref.algorithm.feature.generation.FeatureGenerator;
import edu.columbia.cs.ref.model.CandidateSentence;
import edu.columbia.cs.ref.model.core.structure.OperableStructure;

public abstract class Core implements Serializable{
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
