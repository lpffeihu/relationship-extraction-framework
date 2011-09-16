package edu.columbia.cs.og.features.featureset;

import java.io.Serializable;
import java.util.Arrays;

import edu.columbia.cs.og.structure.OperableStructure;

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
}
