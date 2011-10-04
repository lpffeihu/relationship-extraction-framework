package edu.columbia.cs.cg.relations.entity.matcher.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import edu.columbia.cs.cg.relations.Entity;
import edu.columbia.cs.cg.relations.entity.matcher.EntityMatcher;
import edu.columbia.cs.utils.Dictionary;

public class DictionaryEntityMatcher implements EntityMatcher {


	private Dictionary dictionary;

	public DictionaryEntityMatcher(Dictionary dictionary){
		this.dictionary = dictionary;
	}
	
	@Override
	public boolean match(Entity original, Entity entity) {
		
		Set<String> matches = dictionary.getAliases(original.getValue());
		
		if (matches == null)
			return false;
		
		return matches.contains(entity.getValue());
			
		
	}

}
