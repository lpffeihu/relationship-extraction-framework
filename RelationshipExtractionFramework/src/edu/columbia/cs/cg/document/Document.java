package edu.columbia.cs.cg.document;

import java.io.File;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.columbia.cs.cg.relations.Entity;
import edu.columbia.cs.cg.relations.Relationship;
import edu.columbia.cs.data.Writable;

public class Document implements Serializable, Writable {
	private String path;
	private String fileName;
	private List<Segment> text;
	private Map<String,Entity> entities;
	private Map<String,Relationship> relationships;
	
	public Document(String path, String fileName, List<Segment> text){
		setPath(path);
		setFilename(fileName);
		setPlainText(text);
		entities = new HashMap<String,Entity>();
		relationships = new HashMap<String,Relationship>();
	}
	
	public List<Segment> getPlainText() {
		return text;
	}
	
	public void setPlainText(List<Segment> text) {
		this.text = text;
	}
	
	public Collection<Entity> getEntities() {
		return entities.values();
	}
	
	public void addEntity(Entity entity) {
		entities.put(entity.getId(),entity);
	}
	
	public Entity getEntity(String id){
		return entities.get(id);
	}
	
	public Collection<Relationship> getRelationships() {
		return relationships.values();
	}
	
	public void addRelationship(Relationship relationship) {
		relationships.put(relationship.getId(),relationship);
	}
	
	public Relationship getRelationship(String id){
		return relationships.get(id);
	}
	
	@Override
	public String toString(){
		StringBuffer buf = new StringBuffer();
		
		buf.append("Segments:\n");
		int i=1;
		for(Segment seg : text){
			buf.append(i + ": " + seg + "\n");
			i++;
		}
		
		return buf.toString();
	}
	
	public String getSubstring(int offset, int length){
		for(Segment seg : text){
			if(seg.contains(offset,length)){
				return seg.getValue(offset,length);
			}
		}
		System.out.println(offset + " " + length);
		throw new UnsupportedOperationException();
	}

	public void setPath(String path){
		this.path=path;
	}
	
	public void setFilename(String f) {
		this.fileName = f;
	}

	public String getFilename() {
		return fileName;
	}
	
	public String getPath() {
		return path;
	}

	@Override
	public String getWritableValue() {
		return fileName;
	}
}
