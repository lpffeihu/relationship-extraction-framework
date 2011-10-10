package edu.columbia.cs.cg.document.tagger.entity;

import edu.columbia.cs.cg.document.tagger.Taggeable;

public class EntitySpan extends Taggeable{

	private String id;
	private String value;
	private int offset;
	private int length;

	public EntitySpan(String id, String tag,String value, int offset, int length){
		super(tag);
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

	public String toString(){
		return value + "-" + offset;
	}
}