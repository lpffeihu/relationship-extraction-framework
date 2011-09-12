package edu.columbia.cs.cg.relations;

import java.util.Arrays;

public class Relationship {
	
	private String id;
	private RelationshipType type;
	private Entity[] entities;
	
	public Relationship(RelationshipType type, String id){
		this.type=type;
		this.id=id;
		entities = new Entity[type.getNumberEntities()];
	}
	
	public void setRole(String role, Entity e){
		entities[type.getIndex(role)]=e;
	}
	
	public Entity getRole(String role){
		return entities[type.getIndex(role)];
	}

	public String getId() {
		return id;
	}
	
	public Entity[] getEntities(){
		return entities;
	}
	
	@Override
	public String toString(){
		return type.getType() + Arrays.toString(entities);
	}
}
