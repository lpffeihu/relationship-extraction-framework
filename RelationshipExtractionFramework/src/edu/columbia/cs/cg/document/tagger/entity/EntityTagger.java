package edu.columbia.cs.cg.document.tagger.entity;

import java.util.List;

import edu.columbia.cs.cg.document.Document;
import edu.columbia.cs.cg.document.tagger.Tagger;
import edu.columbia.cs.cg.relations.Entity;

public abstract class EntityTagger extends Tagger {

	protected class EntitySpan {

		private String id;
		private String value;
		private int offset;
		private int length;

		public EntitySpan(String id, String value, int offset, int length){
			this.id = id;
			this.value = value;
			this.offset = offset;
			this.length = length;
		}
		
		public String getValue() {
			return value;
		}

		public int getLength() {
			return length;
		}

		public int getOffset() {
			return offset;
		}

		public String getId() {

			return id;
		}

	}
	
	public EntityTagger(String tag) {
		super(tag);
	}

	@Override
	public void enrich(Document d) {
		
		List<EntitySpan> spans = findSpans(d);

		for (EntitySpan entitySpan : spans) {
			
			Entity entity = new Entity(entitySpan.getId(), getTag(), entitySpan.getOffset(), entitySpan.getLength(), entitySpan.getValue(), d);
			
			d.addEntity(entity);
			
		}
		
	}

	protected abstract List<EntitySpan> findSpans(Document d);

}
