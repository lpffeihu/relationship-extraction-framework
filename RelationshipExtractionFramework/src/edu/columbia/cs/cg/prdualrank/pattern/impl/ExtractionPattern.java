package edu.columbia.cs.cg.prdualrank.pattern.impl;

import java.util.List;
import java.util.Map;

import edu.columbia.cs.cg.pattern.Pattern;

public class ExtractionPattern extends Pattern {

	private List<String> roles;
	private Map<String, SimpleAttributeExtractionPattern> simpleAttributeMap;

	public ExtractionPattern(List<String> roles, Map<String, SimpleAttributeExtractionPattern> simpleAttributeMap) {
		
		this.roles = roles;
		this.simpleAttributeMap = simpleAttributeMap;
		
	}

}
