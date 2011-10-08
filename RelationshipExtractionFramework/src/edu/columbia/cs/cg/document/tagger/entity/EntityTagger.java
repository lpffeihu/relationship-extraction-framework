package edu.columbia.cs.cg.document.tagger.entity;

import java.util.List;

import edu.columbia.cs.cg.document.Document;
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
public abstract class EntityTagger extends Tagger {

	/**
	 * The Class EntitySpan represents a text span that corresponds to an entity in the text.
	 *
	 * @author      Pablo Barrio
	 * @author		Goncalo Simoes
	 * @version     0.1
	 * @since       2011-09-27
	 */
	protected class EntitySpan {

		/** The id. */
		private String id;
		
		/** The value. */
		private String value;
		
		/** The offset. */
		private int offset;
		
		/** The length. */
		private int length;

		/**
		 * Instantiates a new entity span.
		 *
		 * @param id The id of the entity
		 * @param value The value of the span
		 * @param offset The starting index of the span in the text
		 * @param length The size of the span
		 */
		public EntitySpan(String id, String value, int offset, int length){
			this.id = id;
			this.value = value;
			this.offset = offset;
			this.length = length;
		}
		
		/**
		 * Gets the value of the span
		 *
		 * @return the value of the span
		 */
		public String getValue() {
			return value;
		}

		/**
		 * Gets the length of the span
		 *
		 * @return the length of the span
		 */
		public int getLength() {
			return length;
		}

		/**
		 * Gets the starting index of the span in the document.
		 *
		 * @return the starting index of the span
		 */
		public int getOffset() {
			return offset;
		}

		/**
		 * Gets the unique identifier of the span
		 *
		 * @return the unique identifier of the span
		 */
		public String getId() {

			return id;
		}

		/* (non-Javadoc)
		 * @see java.lang.Object#toString()
		 */
		public String toString(){
			return value + "-" + offset;
		}
	}
	
	/**
	 * Instantiates a new entity tagger.
	 *
	 * @param tag the tag
	 */
	public EntityTagger(String tag) {
		super(tag);
	}

	/* (non-Javadoc)
	 * @see edu.columbia.cs.cg.document.tagger.Tagger#enrich(edu.columbia.cs.cg.document.Document)
	 */
	@Override
	public final void enrich(Document d) {
		
		List<EntitySpan> spans = findSpans(d);

		for (EntitySpan entitySpan : spans) {
			
			Entity entity = new Entity(entitySpan.getId(), getTag(), entitySpan.getOffset(), entitySpan.getLength(), entitySpan.getValue(), d);
			
			d.addEntity(entity);
			
		}
		
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

}
