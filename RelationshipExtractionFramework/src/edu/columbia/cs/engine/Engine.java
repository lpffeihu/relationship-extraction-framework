package edu.columbia.cs.engine;

import java.util.List;
import java.util.Set;

import edu.columbia.cs.cg.relations.RelationshipType;
import edu.columbia.cs.model.Model;
import edu.columbia.cs.og.structure.OperableStructure;

public interface Engine {
	public Model train(List<OperableStructure> list);
}
