package edu.columbia.cs.cg.relations.constraints.relations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import edu.columbia.cs.cg.document.Document;
import edu.columbia.cs.cg.document.tokenized.tokenizer.Tokenizer;
import edu.columbia.cs.cg.relations.Entity;
import edu.columbia.cs.cg.relations.Relationship;
import edu.columbia.cs.utils.Span;

public class WordDistanceBetweenEntities implements RelationshipConstraint {

	private Tokenizer tokenizer;
	private int Maxdistance;

	public WordDistanceBetweenEntities(Tokenizer tokenizer, int Maxdistance){
		this.tokenizer = tokenizer;
		this.Maxdistance = Maxdistance;
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
			
			if (firstEnd < secondStart){
				
				String betweenText = document.getSubstring(firstEnd, secondStart - firstEnd);
				
				Span[] spans = tokenizer.tokenize(betweenText);
				
				if (spans.length > Maxdistance)
					return false;
				
			}
					
		}
		
		return true;
	}

}
