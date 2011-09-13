package edu.columbia.cs.og.core;

import java.util.List;

import edu.columbia.cs.cg.candidates.CandidateSentence;
import edu.columbia.cs.cg.sentence.Sentence;
import edu.columbia.cs.og.features.FeatureGenerator;
import edu.columbia.cs.og.structure.OperableStructure;

public abstract class Core {
	private List<FeatureGenerator> mandatoryFg;
	
	public void setMandatoryFeatureGenerators(List<FeatureGenerator> mandatoryFg) {
		this.mandatoryFg = mandatoryFg;
	}
	
	public List<FeatureGenerator> getMandatoryFeatureGenerators() {
		return mandatoryFg;
	}

	public abstract OperableStructure getStructure(CandidateSentence sent);
}
