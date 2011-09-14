package edu.columbia.cs.utils;

import java.io.Serializable;

public class Span implements Serializable {
	private int start;
	private int end;
	
	public Span(int start, int end){
		this.start=start;
		this.end=end;
	}
	
	public Span(opennlp.tools.util.Span openNLPSpan){
		this.start=openNLPSpan.getStart();
		this.end=openNLPSpan.getEnd();
	}

	public int getStart() {
		return start;
	}

	public int getEnd() {
		return end;
	}
}
