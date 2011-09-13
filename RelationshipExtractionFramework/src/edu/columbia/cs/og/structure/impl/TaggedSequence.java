package edu.columbia.cs.og.structure.impl;

import java.io.Serializable;

import edu.columbia.cs.og.structure.OperableStructure;


public class TaggedSequence extends OperableStructure {
	private String seqId;
	private String[][] information;
	private String[][] fb;
	private String[][] b;
	private String[][] ba;
	private String[] entity1;
	private String[] entity2;
	private String entity1Type;
	private String entity2Type;
	private int entity1Index;
	private int entity2Index;
	private String label;
	private transient boolean hasComputedHash=false;
	private transient int hashCode=-1;
	public transient boolean isNormalized=false;
	public transient int id=-1;
	private static FeaturesDictionary[] fd = null;
	
	public TaggedSequence(String seqId,String[][] information,
			int entity1Index, int entity2Index,String e1Type,String e2Type,
			String label){
		
		//INITIALIZE FEATURES DICTIONARY
		if(fd==null && information.length>0){
			int sizeFeatures=information[0].length;
			fd=new FeaturesDictionary[sizeFeatures];
			for(int i=0; i<sizeFeatures; i++){
				fd[i] = new FeaturesDictionary();
			}
		}
		
		//INITIALIZE ATTRIBUTES
		entity1Type=e1Type;
		entity2Type=e2Type;
		this.information=information;
		if(entity1Index>entity2Index){
			int temp=entity1Index;
			entity1Index=entity2Index;
			entity2Index=temp;
			entity1Type=e2Type;
			entity2Type=e1Type;
		}
		
		this.seqId=seqId;
		
		int startFB=0;
		int endFB=entity1Index;
		int startB=entity1Index+1;
		int endB=entity2Index;
		int startBA=entity2Index+1;
		int endBA=information.length;
		
		entity1=information[entity1Index];
		hideInformation(entity1,entity1Type);
		entity2=information[entity2Index];
		hideInformation(entity2,entity2Type);
		
		fb=new String[endFB-startFB][information[0].length];
		for(int i=startFB; i<endFB; i++){
			fb[i-startFB]=information[i];
		}
		
		b=new String[Math.max(endB-startB,0)][information[0].length];
		for(int i=startB; i<endB; i++){
			b[i-startB]=information[i];
		}

		ba=new String[endBA-startBA][information[0].length];
		for(int i=startBA; i<endBA; i++){
			ba[i-startBA]=information[i];
		}
		
		makeFeaturesUniform();
		
		this.label=label;
	}

	private void hideInformation(String[] entity,String type) {
		entity[0]=type;
		entity[3]=type;
		entity[4]=type;
		entity[5]=type;
	}

	public String[] getEntity1Feats(){
		return entity1;
	}
	
	public String[] getEntity2Feats(){
		return entity2;
	}

	public String[][] getInstanceFB() {
		return fb;
	}

	public String[][] getInstanceBA() {
		return ba;
	}

	public String[][] getInstanceB() {
		return b;
	}

	public String getLabel() {
		return label;
	}
	
	private void makeFeaturesUniform(){
		if(fd==null && information.length>0){
			int sizeFeatures=information[0].length;
			fd=new FeaturesDictionary[sizeFeatures];
			for(int i=0; i<sizeFeatures; i++){
				fd[i] = new FeaturesDictionary();
			}
		}
		uniform(fb);
		uniform(b);
		uniform(ba);
	}
	
	private void uniform(String[][] input){
		for(int i=0;i<input.length; i++){
			for(int j=0; j<input[i].length; j++){
				input[i][j]=fd[j].getFeature(input[i][j]);
			}
		}
	}
	
	@Override
	public boolean equals(Object o){
		if(o instanceof TaggedSequence){
			return seqId.equals(((TaggedSequence) o).seqId);
		}
		return false;
	}
	
	@Override
	public int hashCode(){
		if(!hasComputedHash){
			hashCode=seqId.hashCode();
			hasComputedHash=true;
		}
		return hashCode;
	}
}
