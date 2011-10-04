package edu.columbia.cs.cg.document.tagger;

import edu.columbia.cs.cg.document.Document;

public abstract class Tagger {

	private String tag;

	public Tagger(String tag){
		this.tag = tag;
	}
	
	public abstract void enrich(Document d);
	
	protected String getTag(){
		return tag;
	}

}
