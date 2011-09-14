package edu.columbia.cs.og.features;

import java.util.List;

import edu.columbia.cs.cg.sentence.Sentence;
import edu.columbia.cs.og.features.featureset.FeatureSet;
import edu.columbia.cs.og.structure.OperableStructure;

public class DependentFeatureGenerator extends FeatureGenerator {

	private List<FeatureGenerator> fgs;
	private FeatureGenerator fg;
	
	public DependentFeatureGenerator(List<FeatureGenerator> fgs, FeatureGenerator fg){
		this.fgs=fgs;
		this.fg=fg;
	}
	
	@Override
	public FeatureSet process(OperableStructure s) {
		int sizeList = fgs.size();
		for(int i=0; i<sizeList; i++){
			fgs.get(i).generateFeatures(s);
		}
		return fg.process(s);
	}
	
	public Class<? extends FeatureGenerator> getKey(){
		return fg.getClass();
	}

	@Override
	protected FeatureSet getFeatures(OperableStructure s,
			Class<? extends FeatureGenerator> featureGeneratorClass) {
		return fg.getFeatures(s, featureGeneratorClass);
	}

	@Override
	protected void setFeatures(OperableStructure s,
			Class<? extends FeatureGenerator> featureGeneratorClass,
			FeatureSet fs) {
		
		fg.setFeatures(s, featureGeneratorClass, fs);
		
	}
}
