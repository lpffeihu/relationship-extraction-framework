package edu.columbia.cs.cg.sentence;

import java.io.Serializable;

import edu.columbia.cs.cg.document.Document;
import edu.columbia.cs.og.features.FeaturableObject;

public class Sentence extends FeaturableObject implements Serializable {
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
	
	public String getValue(){
		return d.getSubstring(getOffset(), getLength());
	}
	
	@Override
	public String toString(){
		return d.getSubstring(getOffset(), getLength());
	}
	
	public Sentence merge(Sentence nextSentence){
		if(d!=nextSentence.d){
			throw new UnsupportedOperationException();
		}
		
		return new Sentence(d, offset, nextSentence.offset+nextSentence.length-offset);
	}
}
