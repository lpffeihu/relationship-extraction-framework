package edu.columbia.cs.cg.relations;

import java.util.Collection;
import java.util.Hashtable;
import java.util.Set;

//TODO: Constraints
public class RelationshipType {
	private String type;
	private Hashtable<String,Integer> indexes;
	private int numberEntities;
	
	public RelationshipType(String type, String ... roles){
		indexes=new Hashtable<String,Integer>();
		int rolesSize = roles.length;
		for(int i=0;i<rolesSize; i++){
			indexes.put(roles[i],i);
		}
		setType(type);
		numberEntities=roles.length;
	}
	
	public int getIndex(String role){
		return indexes.get(role);
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}
	
	public int getNumberEntities(){
		return numberEntities;
	}

	public boolean isCompatible(String relType) {
		return type.equals(relType);
	}
	
	public Set<String> getRoles(){
		return indexes.keySet();
	}
}
