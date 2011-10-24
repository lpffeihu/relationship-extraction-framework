package edu.columbia.cs.ref.model.core.structure.impl;

import edu.columbia.cs.ref.model.CandidateSentence;
import edu.columbia.cs.ref.model.core.structure.OperableStructure;
import edu.columbia.cs.ref.model.relationship.Relationship;

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
