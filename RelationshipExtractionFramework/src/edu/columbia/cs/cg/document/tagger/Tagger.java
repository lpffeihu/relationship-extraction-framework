package edu.columbia.cs.cg.document.tagger;

import edu.columbia.cs.cg.document.Document;

/**
 * Tagger is an abstract class that represents objects that can be used to annotate
 * documents.
 * 
 * <br>
 * <br>
 * 
 * In general, defining property of a Tagger is the tag that is assigned to the annotations
 * produced in the document. The method that must be used for a Tagger to annotate a
 * document is the method enrich.
 * 
 *
 * @author      Pablo Barrio
 * @author		Goncalo Simoes
 * @version     0.1
 * @since       2011-09-27
 */
public abstract class Tagger {

	/** The tag. */
	private String tag;

	/**
	 * Instantiates a new tagger.
	 *
	 * @param tag The tag to be assigned to every annotation made by this tagger
	 */
	public Tagger(String tag){
		this.tag = tag;
	}
	
	/**
	 * This is the method that defines the behavior of the Tagger. It receives a document
	 * that has to be annotated and produces all the annotations in that document
	 *
	 * @param d the document to be annotated
	 */
	public abstract void enrich(Document d);
	
	/**
	 * Gets the tag that is assigned when this tagger is used to annotate a document.
	 *
	 * @return the tag
	 */
	protected String getTag(){
		return tag;
	}

}
