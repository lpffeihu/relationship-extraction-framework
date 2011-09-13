package edu.columbia.cs.cg.candidates;

import edu.columbia.cs.cg.document.Document;
import edu.columbia.cs.cg.relations.Entity;
import edu.columbia.cs.cg.relations.Relationship;
import edu.columbia.cs.cg.sentence.Sentence;

//TODO: MAKE ALL THESE SERIALIZABLE
public class CandidateSentence {
	private Sentence sentence;
	private Relationship relationship;
	private Document document;
	
	public CandidateSentence(Sentence sentence,Relationship relationship,
			Document document){
		this.setSentence(sentence);
		this.relationship=relationship;
		this.setDocument(document);
	}

	public void setSentence(Sentence sentence) {
		this.sentence = sentence;
	}

	public Sentence getSentence() {
		return sentence;
	}

	public void setDocument(Document document) {
		this.document = document;
	}

	public Document getDocument() {
		return document;
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
