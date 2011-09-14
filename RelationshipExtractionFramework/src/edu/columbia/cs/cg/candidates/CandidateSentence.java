package edu.columbia.cs.cg.candidates;

import java.io.Serializable;

import edu.columbia.cs.cg.document.Document;
import edu.columbia.cs.cg.relations.Entity;
import edu.columbia.cs.cg.relations.Relationship;
import edu.columbia.cs.cg.sentence.Sentence;
import edu.columbia.cs.og.features.FeaturableObject;

public class CandidateSentence extends FeaturableObject implements Serializable {
	private Sentence sentence;
	private Relationship relationship;
	
	public CandidateSentence(Sentence sentence,Relationship relationship){
		this.setSentence(sentence);
		this.relationship=relationship;
	}

	public void setSentence(Sentence sentence) {
		this.sentence = sentence;
	}

	public Sentence getSentence() {
		return sentence;
	}
	
	public Entity getRole(String role){
		return relationship.getRole(role);
	}
	
	@Override
	public int hashCode(){
		return relationship.getId().hashCode();
	}
	
	@Override
	public boolean equals(Object o){
		if(o instanceof CandidateSentence){
			return relationship.equals(((CandidateSentence) o).relationship);
		}
		return false;
	}
}
