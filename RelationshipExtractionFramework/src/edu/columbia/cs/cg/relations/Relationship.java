package edu.columbia.cs.cg.relations;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;

import edu.columbia.cs.cg.pattern.matchable.Matchable;
import edu.columbia.cs.cg.relations.entity.matcher.EntityMatcher;

public class Relationship implements Serializable,Matchable {
	
	private int hashCode = -1;
	private String id=null;
	private RelationshipType type;
	private Entity[] entities;
	private String label=null;
	
	public Relationship(RelationshipType type){
		this.type=type;
		entities = new Entity[type.getNumberEntities()];
	}
	
	public void setRelationshipType(RelationshipType type){
		this.type=type;
		entities = new Entity[type.getNumberEntities()];
	}
	
	public void setRole(String role, Entity e){
		entities[type.getIndex(role)]=e;
	}
	
	public Entity getRole(String role){
		return entities[type.getIndex(role)];
	}

	public String getId() {
		if(id==null){
			StringBuffer middleId = new StringBuffer();
			for(int i=0; i<entities.length; i++){
				if(i==0){
					middleId.append(entities[i].getId());
				}else{
					middleId.append("," + entities[i].getId());
				}
			}
			
			id=type.getType() + "(" + middleId + ")";
		}
		return id;
	}
	
	public Entity[] getEntities(){
		return entities;
	}
	
	@Override
	public String toString(){
		return type.getType() + Arrays.toString(entities);
	}
	
	@Override
	public int hashCode(){
		
		if (hashCode == -1){
			
			hashCode = 1;
			
			for (String role : type.getRoles()) {
				
				hashCode = 31*hashCode + getRole(role).hashCode();
				
			}
			
		}
		
		return hashCode;
	}
	
	@Override
	public boolean equals(Object o){

		if (o instanceof Relationship){
			
			Relationship other = (Relationship)o;
			
			if (!type.equals(other.type))
				return false;
			
			for (String role : type.getRoles()) {
				
				EntityMatcher em = type.getMatchers(role);
				Entity e1 = getRole(role);
				Entity e2 = other.getRole(role);
	
				if (!em.match(e1, e2))
					return false;
				
			}
		
			return true;
		}
		
		return false;
	}
	
	public String getLabel(){
		return label;
	}
	
	public void setLabel(String label){
		this.label=label;
	}
	
	public RelationshipType getRelationshipType(){
		return type;
	}

	public Collection<String> getRoles() {
		return type.getRoles();
	}
}
