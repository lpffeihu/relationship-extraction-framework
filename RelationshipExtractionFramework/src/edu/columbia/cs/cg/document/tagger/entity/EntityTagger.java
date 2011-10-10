package edu.columbia.cs.cg.document.tagger.entity;

import java.util.List;
import java.util.Set;

import edu.columbia.cs.cg.document.Document;
import edu.columbia.cs.cg.document.tagger.Taggeable;
import edu.columbia.cs.cg.document.tagger.Tagger;
import edu.columbia.cs.cg.relations.Entity;

/**
 * The EntityTagger is an abstract class that extends from Tagger.
 * 
 * <br>
 * <br>
 * 
 * Instances of this class are able to annotate entities that are present in the text.
 *
 * @author      Pablo Barrio
 * @author		Goncalo Simoes
 * @version     0.1
 * @since       2011-09-27
 */
public abstract class EntityTagger<S extends EntitySpan,E extends Entity> extends Tagger<EntitySpan,Entity> {
	
	/**
	 * Instantiates a new entity tagger.
	 *
	 * @param tag the tag
	 */
	public EntityTagger(String tag) {
		super(tag);
	}
	
	public EntityTagger(Set<String> tag) {
		super(tag);
	}

	/**
	 * This method defines the behavior of the EntityTagger.
	 * 
	 * <br>
	 * <br>
	 * 
	 * The objective of this method is to find references to entities in document
	 * d that must be annotated.
	 *
	 * @param d the document where the entities are present
	 * @return a list containing all the entities of the input document that this tagger
	 * will annotate
	 */
	protected abstract List<EntitySpan> findSpans(Document d);

	@Override
	protected Entity generateInstance(EntitySpan entitySpan, Document d) {
		return new Entity(entitySpan.getId(), entitySpan.getTag(), entitySpan.getOffset(), entitySpan.getLength(), entitySpan.getValue(), d);
	}
	
	@Override
	protected void updateDocument(Document d, Entity entity) {
		d.addEntity(entity);
	}
}
