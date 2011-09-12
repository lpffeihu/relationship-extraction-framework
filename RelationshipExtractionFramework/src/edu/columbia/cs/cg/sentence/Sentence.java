package edu.columbia.cs.cg.sentence;

import edu.columbia.cs.cg.document.Document;

public class Sentence {
	private Document d;
	private int offset;
	//The length of the sentence comes from the text
	private int length;
	
	public Sentence(Document d, int offset, int length){
		this.d=d;
		this.offset=offset;
		this.length=length;
	}
	
	public void setOffset(int offset) {
		this.offset = offset;
	}
	
	public int getOffset() {
		return offset;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public int getLength() {
		return length;
	}
	
	@Override
	public String toString(){
		return d.getSubstring(getOffset(), getLength());
	}
}
