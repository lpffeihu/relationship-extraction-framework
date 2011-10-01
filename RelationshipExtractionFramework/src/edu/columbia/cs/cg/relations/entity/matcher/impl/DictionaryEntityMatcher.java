package edu.columbia.cs.cg.relations.entity.matcher.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import edu.columbia.cs.cg.relations.Entity;
import edu.columbia.cs.cg.relations.entity.matcher.EntityMatcher;

public class DictionaryEntityMatcher implements EntityMatcher {

	private String separatorValue;
	private Map<String, Set<String>> dictionary;
	private String separatorRegEx;

	public DictionaryEntityMatcher(File dictionary, String separatorRegEx){
		
		this.separatorRegEx = separatorRegEx;
		
		loadDictionary(dictionary);
		
	}
	
	private void loadDictionary(File dictionaryFile) {
		
		dictionary = new HashMap<String, Set<String>>();
		
		BufferedReader br = new BufferedReader(new FileReader(dictionaryFile));
		
		String line;
		
		while ((line=br.readLine()) != null){
			
			String[] spl = line.split(separatorValue);
			
			
			
		}
		
		br.close();
		
	}

	@Override
	public boolean match(Entity original, Entity entity) {
		
		Set<String> matches = dictionary.get(original.getValue());
		
		if (matches == null)
			return false;
		
		return matches.contains(entity.getValue());
			
		
	}

}
