package edu.columbia.cs.utils;

import java.io.Serializable;

public class Span implements Serializable,Comparable<Span> {
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

	@Override
	public int compareTo(Span s) {
		
		if (getStart() < s.getStart()){
			return -1;
		} else if (getStart() == s.getStart()){
			
			if (getEnd() > s.getEnd()){
				return -1;
			} else if (getEnd() < s.getEnd()){
				return 1;
			} else {
				return 0;
			}
			
		} else {
			return 1;
		}
		
	}
	
	public boolean intersects(Span s){
		int sstart = s.getStart();
		
		return this.contains(s) || s.contains(this)
				|| getStart() <= sstart && sstart<getEnd() 
				|| sstart <= getStart() && getStart() < s.getEnd();
	}

	public boolean contains(Span s) {
		return start <= s.getStart() && s.getEnd() <= end;
	}
	
	public String toString(){
		return start + "-" + end;
	}
}
