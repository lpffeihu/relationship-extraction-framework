package edu.columbia.cs.cg.relations;

import java.io.Serializable;
import java.util.Arrays;

public class Relationship implements Serializable {
	
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
		return getId().hashCode();
	}
	
	@Override
	public boolean equals(Object o){
		if(o instanceof Relationship){
			return getId().equals(((Relationship) o).getId());
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
}
