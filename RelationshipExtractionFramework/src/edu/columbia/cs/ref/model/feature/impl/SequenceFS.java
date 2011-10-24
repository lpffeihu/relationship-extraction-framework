package edu.columbia.cs.ref.model.feature.impl;

import java.io.Serializable;
import java.util.Arrays;

import edu.columbia.cs.ref.model.core.structure.OperableStructure;
import edu.columbia.cs.ref.model.feature.FeatureSet;

public class SequenceFS<E extends Serializable> extends FeatureSet {
	private E[] sequence;
	
	public SequenceFS(E[] sequence){
		this.sequence=sequence;
	}

	public int size(){
		return sequence.length;
	}
	
	public E getElement(int position){
		return sequence[position];
	}
	
	@Override
	public String toString(){
		return Arrays.toString(sequence);
	}
	
	@Override
	public void enrichMe(OperableStructure operableStructure) {
		operableStructure.add(this);
	}

	public E[] toArray() {
		return sequence;
	}
}
