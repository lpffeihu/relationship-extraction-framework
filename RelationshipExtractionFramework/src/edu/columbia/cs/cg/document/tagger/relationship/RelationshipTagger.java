package edu.columbia.cs.cg.document.tagger.relationship;

import java.util.List;

import edu.columbia.cs.cg.document.Document;
import edu.columbia.cs.cg.document.tagger.Tagger;
import edu.columbia.cs.cg.relations.Relationship;

public abstract class RelationshipTagger extends Tagger<RelationshipSpan,Relationship> {

	public RelationshipTagger(String tag) {
		super(tag);
	}

	@Override
	protected void updateDocument(Document d, Relationship tuple) {
		d.addRelationship(tuple);		
	}

	@Override
	protected abstract List<RelationshipSpan> findSpans(Document d);

	@Override
	protected Relationship generateInstance(RelationshipSpan container,
			Document d) {
		// TODO Auto-generated method stub
		return null;
	}

}
