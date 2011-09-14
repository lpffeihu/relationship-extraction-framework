package edu.columbia.cs.og.features.featureset;

import java.util.Arrays;

public class SequenceFS<E> extends FeatureSet {
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
}
