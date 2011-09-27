package edu.columbia.cs.og.structure;

import java.io.Serializable;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.columbia.cs.cg.candidates.CandidateSentence;
import edu.columbia.cs.cg.relations.RelationshipType;
import edu.columbia.cs.og.features.FeatureGenerator;
import edu.columbia.cs.og.features.featureset.FeatureSet;
import edu.columbia.cs.og.features.featureset.GraphFS;
import edu.columbia.cs.og.features.featureset.SequenceFS;
import edu.columbia.cs.og.features.featureset.WekaInstanceFS;

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
	
	public <E extends FeatureSet> E getFeatures(Class<? extends FeatureGenerator<E>> featureGenerator){
		return (E) featuresTable.get(featureGenerator);
	}

	public <E extends FeatureSet> E getFeatures(FeatureGenerator<E> featureGenerator){
		return (E) featuresTable.get(featureGenerator.getClass());
	}

	public <E extends FeatureSet> void setFeatures(FeatureGenerator<E> featureGenerator, E fs){
		featuresTable.put(featureGenerator.getClass(),fs);
	}

	public Set<String> getLabels() {
		return candidateSentence.getLabels();
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

	public void enrich(FeatureSet fs){
		fs.enrichMe(this);
	}
	
	public void add(GraphFS<? extends Serializable,? extends Serializable> graph){
		throw new UnsupportedOperationException();
	}
	
	public void add(SequenceFS<? extends Serializable> graph){
		throw new UnsupportedOperationException();
	}

	public void add(WekaInstanceFS wekaInstanceFS) {
		throw new UnsupportedOperationException();
		
	}
}
