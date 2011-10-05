package edu.columbia.cs.cg.relations.constraints.relations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.columbia.cs.cg.document.Document;
import edu.columbia.cs.cg.document.tokenized.tokenizer.Tokenizer;
import edu.columbia.cs.cg.relations.Entity;
import edu.columbia.cs.cg.relations.Relationship;
import edu.columbia.cs.utils.Span;

public class WordDistanceBetweenEntities implements RelationshipConstraint {

	private Tokenizer tokenizer;
	private int Maxdistance;
	private Map<Document, Map<Integer, Integer>> cached;
	private boolean activateCache;

	public WordDistanceBetweenEntities(Tokenizer tokenizer, int Maxdistance, boolean activateCache){
		this.tokenizer = tokenizer;
		this.Maxdistance = Maxdistance;
		this.activateCache = activateCache;
		if (activateCache)
			cached = new HashMap<Document,Map<Integer,Integer>>();
	}
	
	@Override
	public boolean checkConstraint(Relationship rel) {
		
		Document document = null;
		
		List<Entity> entities = new ArrayList<Entity>();
		
		for (String role : rel.getRoles()){
			
			Entity e = rel.getRole(role);
			
			if (e != Entity.NULL_ENTITY){
				
				if (document == null || document.equals(e.getDocument())){
					entities.add(e);
					document = e.getDocument();
				}else{
					return false;
				}
							
			}
			
		}
		
		Collections.sort(entities);
		
		for (int i = 0; i < entities.size()-1; i++) {
			
			int firstEnd = entities.get(i).getOffset() + entities.get(i).getLength();
			
			int secondStart = entities.get(i+1).getOffset();
			Integer minNotAccepted = null;
			if (activateCache){
				minNotAccepted = getCatched(document,firstEnd);
				
				if (minNotAccepted != null){
					if (secondStart > minNotAccepted)
						return false;
				}
			}
				
			if (firstEnd < secondStart){
				
				String betweenText = document.getSubstring(firstEnd, secondStart - firstEnd);
				
				Span[] spans = tokenizer.tokenize(betweenText);
				
				if (spans.length > Maxdistance){
					
					if (activateCache){
						if (minNotAccepted == null){
							minNotAccepted = secondStart;
						}else{
							if (minNotAccepted>secondStart){
								minNotAccepted = secondStart;
							}
						}
						
						save(document,firstEnd,minNotAccepted);
					}
					
					return false;
				}
			}
					
		}
		
		return true;
	}

	private void save(Document document, int firstEnd, Integer minNotAccepted) {
		
		Map<Integer,Integer> map = cached.get(document);
		
		map.put(firstEnd, minNotAccepted);
		
	}

	private Integer getCatched(Document document, int firstEnd) {
		
		Map<Integer,Integer> map = cached.get(document);
		
		if (map == null){
			map = new HashMap<Integer, Integer>();
			cached.put(document, map);
			return null;
		}
		
		return map.get(firstEnd);
		
	}

}
