package edu.columbia.cs.og.structure.impl;

import edu.columbia.cs.cg.candidates.CandidateSentence;
import edu.columbia.cs.og.features.impl.OpenNLPTokenizationFG;
import edu.columbia.cs.og.structure.OperableStructure;

public class TaggedSequence extends OperableStructure {

	public TaggedSequence(CandidateSentence s){
		super(s);
	}
	
	@Override
	public void initialize() {
		// TODO Auto-generated method stub
		System.out.println(getFeatures(OpenNLPTokenizationFG.class));
	}

}
