package edu.columbia.cs.cg.relations.constraints.relations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import edu.columbia.cs.cg.relations.Entity;
import edu.columbia.cs.cg.relations.Relationship;

public class DistanceBetweenEntitiesConstraint implements RelationshipConstraint {

	private int k;
	
	public DistanceBetweenEntitiesConstraint(int k){
		this.k=k;
	}
	
	@Override
	public boolean checkConstraint(Relationship rel) {
		Entity[] entities = rel.getEntities();
		
		List<Integer> starts = new ArrayList<Integer>();
		List<Integer> ends = new ArrayList<Integer>();
		
		for(Entity e : entities){
			starts.add(e.getOffset());
			ends.add(e.getOffset()+e.getLength());
		}
		
		Collections.sort(starts);
		Collections.sort(ends);
		
		int highestStart = starts.get(starts.size()-1);
		int lowestEnd = ends.get(0);
		
		if(highestStart-lowestEnd<=k){
			return true;
		}
		
		return false;
	}

}
