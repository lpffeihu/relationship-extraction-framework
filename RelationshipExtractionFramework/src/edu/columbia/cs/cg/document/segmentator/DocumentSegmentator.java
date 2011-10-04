package edu.columbia.cs.cg.document.segmentator;

import java.util.List;

import edu.columbia.cs.cg.document.Segment;

public interface DocumentSegmentator {

	public List<Segment> segmentate(String documentContent);

}
