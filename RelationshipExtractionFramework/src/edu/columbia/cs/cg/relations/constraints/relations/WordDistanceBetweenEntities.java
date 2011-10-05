package edu.columbia.cs.cg.relations.constraints.relations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.columbia.cs.cg.document.Document;
import edu.columbia.cs.cg.document.TokenizedDocument;
import edu.columbia.cs.cg.document.tokenized.tokenizer.Tokenizer;
import edu.columbia.cs.cg.relations.Entity;
import edu.columbia.cs.cg.relations.Relationship;
import edu.columbia.cs.utils.Span;

public class WordDistanceBetweenEntities implements RelationshipConstraint {

	private Tokenizer tokenizer;
	private int Maxdistance;
	private boolean activateCache;

	public WordDistanceBetweenEntities(Tokenizer tokenizer, int Maxdistance){
		this.tokenizer = tokenizer;
		this.Maxdistance = Maxdistance;
	}
	
	@Override
	public boolean checkConstraint(Relationship rel) {
		
		TokenizedDocument document = null;
		
		List<Entity> entities = new ArrayList<Entity>();
		
		for (String role : rel.getRoles()){
			
			Entity e = rel.getRole(role);
			
			if (e != Entity.NULL_ENTITY){
				
				if (document == null || document.equals(e.getDocument())){
					entities.add(e);
					
					//TODO avoid the cast
					document = (TokenizedDocument)e.getDocument();
				}else{
					return false;
				}
							
			}
			
		}
		
		Collections.sort(entities);
		
		for (int i = 0; i < entities.size()-1; i++) {
			
			Entity e1 = entities.get(i);
			
			Entity e2 = entities.get(i+1);
			
			Span se1 = document.getEntitySpan(e1);
			
			Span se2 = document.getEntitySpan(e2);
			
			if (se1.getEnd() < se2.getStart()){
				
				if (se2.getStart() - se1.getEnd() - 1 > Maxdistance){
					
					return false;
				}
			}
					
		}
		
		return true;
	}

}
