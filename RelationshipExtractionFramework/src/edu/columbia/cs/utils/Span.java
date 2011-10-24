package edu.columbia.cs.utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
	
	public static List<Span> calculateNotOverlappingSpans(List<Span> spans){
		
		// TODO returns the not overlapping spans ... use instersects.
		
			if (spans.size()<=1)
				return spans;

			List<Span> notIntersecting = new ArrayList<Span>(spans);
			
			List<Span> intersecting = new ArrayList<Span>();
			
			for (int i=0;i<spans.size();i++) {
				
				Span span = spans.get(i);
				
				for (int j=0;j<spans.size();j++) {

					if (i==j)
						continue;
					
					Span span2 = spans.get(j);

					if (span2.intersects(span) || span.getEnd()==span2.getStart() || span2.getEnd()==span.getStart()){
						notIntersecting.remove(span2);
						if (!intersecting.contains(span2))
							intersecting.add(span2);
					}
					
				}
				
			}
			
			if (intersecting.isEmpty()){
				Collections.sort(spans);
				return spans;
			}
		
			Collections.sort(intersecting);
		
			List<Span> unifiedList = new ArrayList<Span>();

			Span unified = intersecting.get(0);

			int last = unified.getEnd();

			
			for (int i = 0; i < intersecting.size()-1; i++) {

				Span s2 = intersecting.get(i+1);
				
				if (unified.intersects(s2) || unified.getEnd()==s2.getStart() || s2.getEnd()==unified.getStart()){
					
					last = Math.max(unified.getEnd(), s2.getEnd());
					
					unified = new Span(unified.getStart(),last);
					
				} else {
					
					unifiedList.add(unified);
					
					unified = new Span(s2.getStart(),s2.getEnd());
					
					last = s2.getEnd();
					
				}
				
			}
			
			unifiedList.add(unified);
			
			notIntersecting.addAll(unifiedList);
			
			Collections.sort(notIntersecting);
			
			return notIntersecting;
		
	}
}
