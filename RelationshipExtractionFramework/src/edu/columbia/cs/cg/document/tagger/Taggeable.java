package edu.columbia.cs.cg.document.tagger;

public abstract class Taggeable {

	private String tag;

	public Taggeable(String tag){
		this.tag = tag;
	}
	
	public String getTag(){
		return tag;
	}
	
}
