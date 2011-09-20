package edu.columbia.cs.og.features.featureset;

import java.io.Serializable;
import java.util.Arrays;

import edu.columbia.cs.og.structure.OperableStructure;
import edu.columbia.cs.utils.SimpleGraph;

public class GraphFS<N extends Serializable, L extends Serializable> extends FeatureSet {
	private SimpleGraph<N,L> graph;
	
	public GraphFS(SimpleGraph<N,L> graph){
		this.graph=graph;
	}
	
	public SimpleGraph<N,L> getGraph(){
		return graph;
	}
	
	@Override
	public String toString(){
		return graph.toString();
	}

	@Override
	public void enrichMe(OperableStructure operableStructure) {
		operableStructure.add(this);
	}
}
