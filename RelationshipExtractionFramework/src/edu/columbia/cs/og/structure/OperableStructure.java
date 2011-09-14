package edu.columbia.cs.og.structure;

import java.io.Serializable;
import java.util.Hashtable;

import edu.columbia.cs.cg.candidates.CandidateSentence;
import edu.columbia.cs.og.features.FeatureGenerator;
import edu.columbia.cs.og.features.featureset.FeatureSet;

public abstract class OperableStructure implements Serializable {

	private CandidateSentence candidateSentence;
	private Hashtable<Class<? extends FeatureGenerator>, FeatureSet> featuresTable;
	private transient boolean hasComputedHash=false;
	private transient int hashCode=-1;
	
	public OperableStructure(CandidateSentence c){
		this.candidateSentence=c;
		featuresTable = new Hashtable<Class<? extends FeatureGenerator>, FeatureSet>();
	}

	public abstract void initialize();

	public CandidateSentence getCandidateSentence() {
		return candidateSentence;
	}

	public FeatureSet getFeatures(Class<? extends FeatureGenerator> featureGeneratorClass) {
		return featuresTable.get(featureGeneratorClass);
	}

	public void setFeatures(Class<? extends FeatureGenerator> featureGeneratorClass, FeatureSet fs) {
		
		featuresTable.put(featureGeneratorClass,fs);
	}

	public String getLabel() {
		return candidateSentence.getLabel();
	}
	
	@Override
	public boolean equals(Object o){
		if(o instanceof OperableStructure){
			return candidateSentence.getId().equals(((OperableStructure) o).candidateSentence.getId());
		}
		return false;
	}
	
	@Override
	public int hashCode(){
		if(!hasComputedHash){
			hashCode=candidateSentence.getId().hashCode();
			hasComputedHash=true;
		}
		return hashCode;
	}

	public abstract void normalizeFeatures();
}
