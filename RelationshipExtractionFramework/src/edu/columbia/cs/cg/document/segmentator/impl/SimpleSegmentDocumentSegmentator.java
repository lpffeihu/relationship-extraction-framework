package edu.columbia.cs.cg.document.segmentator.impl;

import java.util.ArrayList;
import java.util.List;

import edu.columbia.cs.cg.document.Segment;
import edu.columbia.cs.cg.document.segmentator.DocumentSegmentator;

public class SimpleSegmentDocumentSegmentator implements DocumentSegmentator {

	@Override
	public List<Segment> segmentate(String documentContent) {
		
		List<Segment> segments = new ArrayList<Segment>();
		
		segments.add(new Segment(documentContent, 0));
		
		return segments;
		
	}

}
