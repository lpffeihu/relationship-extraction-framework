package edu.columbia.cs.og.features;

import java.util.List;

import edu.columbia.cs.cg.sentence.Sentence;
import edu.columbia.cs.og.structure.OperableStructure;

public abstract class DependentFeatureGenerator extends FeatureGenerator {

	private List<FeatureGenerator> fg;
	
	public DependentFeatureGenerator(List<FeatureGenerator> fg){
		this.fg=fg;
	}
	
	@Override
	public void generateFeatures(OperableStructure s) {
		int sizeList = fg.size();
		for(int i=0; i<sizeList; i++){
			fg.get(i).generateFeatures(s);
		}
		process(s);
	}
}
