package edu.columbia.cs.og.features;

import java.util.List;

import edu.columbia.cs.og.features.featureset.FeatureSet;
import edu.columbia.cs.og.structure.OperableStructure;

public abstract class FeatureGenerator<E extends FeatureSet> {
	
	private List<FeatureGenerator> fgs;
	
	public final void generateFeatures(OperableStructure s){
		
		FeatureSet fs = s.getFeatures(this.getClass());
		
		if (fs == null){
			
			fs = getFeatures(s,this.getClass());
			
			if (fs == null){
				
				fs = process(s);
				
				setFeatures(s,this.getClass(),fs);
				
			}

			s.setFeatures(this.getClass(),fs);
			
		}
		
		
	}
	
	protected abstract void setFeatures(OperableStructure s, Class<? extends FeatureGenerator> featureGeneratorClass,
			FeatureSet fs);

	protected abstract FeatureSet getFeatures(OperableStructure s, Class<? extends FeatureGenerator> featureGeneratorClass);
	
	/*public Class<? extends FeatureGenerator> getKey() {
		return this.getClass();
	}*/
	protected E process(OperableStructure s) {
		int sizeList = getRequiredFeatureGenerators().size();
		for(int i=0; i<sizeList; i++){
			getRequiredFeatureGenerators().get(i).generateFeatures(s);
		}
		return extractFeatures(s);
	}
	
	protected abstract E extractFeatures(OperableStructure s);

	private List<FeatureGenerator> getRequiredFeatureGenerators() {
		
		if (fgs == null){
			fgs = retrieveRequiredFeatureGenerators();
		}
		return fgs;
	}

	protected abstract List<FeatureGenerator> retrieveRequiredFeatureGenerators();
}
