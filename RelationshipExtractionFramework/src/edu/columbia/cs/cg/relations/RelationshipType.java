package edu.columbia.cs.cg.relations;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;

import edu.columbia.cs.cg.relations.constraints.relations.DummyRelationshipConstraint;
import edu.columbia.cs.cg.relations.constraints.relations.RelationshipConstraint;
import edu.columbia.cs.cg.relations.constraints.roles.NoConstraint;
import edu.columbia.cs.cg.relations.constraints.roles.RoleConstraint;

public class RelationshipType implements Serializable {
	public static final String NOT_A_RELATIONSHIP = "";
	
	private String type;
	private Hashtable<String,Integer> indexes;
	private int numberEntities;
	
	private transient RoleConstraint[] roleConstraints;
	private transient RelationshipConstraint relConstraints;
	
	public RelationshipType(String type, String ... roles){
		indexes=new Hashtable<String,Integer>();
		int rolesSize = roles.length;
		for(int i=0;i<rolesSize; i++){
			indexes.put(roles[i],i);
		}
		setType(type);
		numberEntities=roles.length;
		roleConstraints=new RoleConstraint[rolesSize];
		for(int i=0;i<rolesSize; i++){
			roleConstraints[i]=new NoConstraint();
		}
		relConstraints=new DummyRelationshipConstraint();
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
	
	public Set<String> getRoles(){
		return indexes.keySet();
	}

	public boolean isType(String relType) {
		return relType.equals(type);
	}
	
	public void setConstraints(RoleConstraint constraint, String role){
		roleConstraints[indexes.get(role)]=constraint;
	}
	
	public void setConstraints(RelationshipConstraint constraint){
		relConstraints=constraint;
	}
	
	public RoleConstraint getConstraint(String role){
		return roleConstraints[indexes.get(role)];
	}
	
	public RelationshipConstraint getRelationshipConstraint(){
		return relConstraints;
	}
}
