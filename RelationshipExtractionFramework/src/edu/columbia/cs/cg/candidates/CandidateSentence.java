package edu.columbia.cs.cg.candidates;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import edu.columbia.cs.cg.relations.Entity;
import edu.columbia.cs.cg.relations.Relationship;
import edu.columbia.cs.cg.relations.RelationshipType;
import edu.columbia.cs.cg.sentence.Sentence;
import edu.columbia.cs.og.features.FeaturableObject;

/**
 * A representation of a candidate sentence. A candidate sentence is the basic structure
 * that contains the information that is used for most of the classifier based methods.
 * 
 * <br>
 * <br>
 * 
 * A candidate sentence is composed by a Sentence from a given document, a list of entities
 * that are present in the sentence and the corresponding relationships between entities
 * of the text that are present in the sentence.
 * 
 * @author      Pablo Barrio
 * @author		Goncalo Simoes
 * @version     0.1
 * @since       2011-09-27
 */
public class CandidateSentence extends FeaturableObject implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 90249725464599185L;
	private Sentence sentence;
	private List<Entity> entities;
	private String id=null;
	private Map<RelationshipType,Relationship> relationships;

	public CandidateSentence(Sentence sentence,List<Entity> entities){
		this.setSentence(sentence);
		this.entities=entities;
		relationships = new HashMap<RelationshipType,Relationship>();
	}

	public void addRelationship(Relationship rel){
		relationships.put(rel.getRelationshipType(), rel);
	}

	public void setSentence(Sentence sentence) {
		this.sentence = sentence;
	}

	public Sentence getSentence() {
		return sentence;
	}

	public Entity[] getEntities(){
		Entity[] ents = new Entity[entities.size()];
		return entities.toArray(ents);
	}

	public Set<String> getLabels() {
		Set<String> labels = new HashSet<String>();
		for(Entry<RelationshipType,Relationship> entry : relationships.entrySet()){
			if(!entry.getValue().getLabel().equals(RelationshipType.NOT_A_RELATIONSHIP)){
				labels.add(entry.getValue().getLabel());
			}
		}
		return labels;
	}
	
	public Collection<String> getRoles(RelationshipType t){
		return relationships.get(t).getRoles();
	}

	public Entity getRole(RelationshipType type,String role){
		return relationships.get(type).getRole(role);
	}
	
	public Relationship getRelationship(RelationshipType t){
		return relationships.get(t);
	}

	@Override
	public int hashCode(){
		return entities.hashCode();
	}

	@Override
	public boolean equals(Object o){
		if(o instanceof CandidateSentence){
			return entities.equals(((CandidateSentence) o).entities);
		}
		return false;
	}

	public String getId(){
		if(id==null){
			StringBuffer middleId = new StringBuffer();
			for(int i=0; i<entities.size(); i++){
				if(i==0){
					middleId.append(entities.get(i).getId());
				}else{
					middleId.append("," + entities.get(i).getId());
				}
			}

			id=middleId.toString();
		}
		return id;
	}
}
