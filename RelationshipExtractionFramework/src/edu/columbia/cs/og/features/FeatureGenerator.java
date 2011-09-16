package edu.columbia.cs.og.features;

import edu.columbia.cs.og.features.featureset.FeatureSet;
import edu.columbia.cs.og.structure.OperableStructure;

public abstract class FeatureGenerator {
	
	public final void generateFeatures(OperableStructure s){
		
		FeatureSet fs = s.getFeatures(this.getKey());
		
		if (fs == null){
			
			fs = getFeatures(s,this.getKey());
			
			if (fs == null){
				
				fs = process(s);
				
				setFeatures(s,this.getKey(),fs);
				
			}

			s.setFeatures(this.getKey(),fs);
			
		}
		
		
	}
	
	protected abstract void setFeatures(OperableStructure s, Class<? extends FeatureGenerator> featureGeneratorClass,
			FeatureSet fs);

	protected abstract FeatureSet getFeatures(OperableStructure s, Class<? extends FeatureGenerator> featureGeneratorClass);
	
	public Class<? extends FeatureGenerator> getKey() {
		return this.getClass();
	}
	protected abstract FeatureSet process(OperableStructure s);
}
