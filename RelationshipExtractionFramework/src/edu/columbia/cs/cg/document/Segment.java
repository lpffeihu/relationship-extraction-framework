package edu.columbia.cs.cg.document;

public class Segment {
	private String value;
	private int offset;
	
	public Segment(String value, int offset){
		this.setValue(value);
		this.setOffset(offset);
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	public int getOffset() {
		return offset;
	}
	
	public String toString(){
		return value;
	}

	public boolean contains(int offset, int length) {
		return offset>=getOffset() && length<=value.length()-(offset-getOffset());
	}
	
	public String getValue(int offset, int length){
		return value.substring(offset-getOffset(),offset-getOffset()+length);
	}
}
