package edu.columbia.cs.og.structure.impl;

import edu.columbia.cs.cg.candidates.CandidateSentence;
import edu.columbia.cs.cg.relations.Relationship;
import edu.columbia.cs.og.structure.OperableStructure;

public class RelationOperableStructure extends OperableStructure {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Relationship relation;

	public RelationOperableStructure(Relationship relation) {
		super(null);
		this.relation = relation;
	}

	@Override
	public void initialize() {
		;
	}

	public Relationship getRelation(){
		return relation;
	}
}
