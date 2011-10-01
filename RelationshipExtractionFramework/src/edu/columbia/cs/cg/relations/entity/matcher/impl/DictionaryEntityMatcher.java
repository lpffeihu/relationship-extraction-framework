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
		
		try {
		
			BufferedReader br = new BufferedReader(new FileReader(dictionaryFile));
			
			String line;
			
			while ((line=br.readLine()) != null){
				
				String[] spl = line.split(separatorValue);
				
				Set<String> aliases = new HashSet<String>();
				
				for (int i = 0; i < spl.length; i++) {
					aliases.add(spl[i]);
				}
				
				for (int i = 0; i < spl.length; i++) {
					
					dictionary.put(spl[i], aliases);
					
				}
				
			}
				
			br.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@Override
	public boolean match(Entity original, Entity entity) {
		
		Set<String> matches = dictionary.get(original.getValue());
		
		if (matches == null)
			return false;
		
		return matches.contains(entity.getValue());
			
		
	}

}
