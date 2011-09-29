package edu.columbia.cs.api;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import edu.columbia.cs.cg.candidates.CandidateSentence;
import edu.columbia.cs.cg.candidates.CandidatesGenerator;
import edu.columbia.cs.cg.document.Document;
import edu.columbia.cs.cg.relations.Relationship;
import edu.columbia.cs.cg.relations.RelationshipType;
import edu.columbia.cs.cg.sentence.SentenceSplitter;
import edu.columbia.cs.model.Model;
import edu.columbia.cs.og.configuration.StructureConfiguration;
import edu.columbia.cs.og.structure.OperableStructure;

public class ClassifierBasedRelationshipExtractor implements RelationshipExtractor{
	private Model m;
	private CandidatesGenerator generator;
	private StructureConfiguration structGenerator;

	public ClassifierBasedRelationshipExtractor(Model m, SentenceSplitter s){
		this.m=m;
		this.generator = new CandidatesGenerator(s);
		this.structGenerator = m.getStructureConfiguration();
	}

	public Set<Relationship> extractTuples(Document d){
		
		Set<CandidateSentence> sents = generator.generateCandidates(d, m.getRelationshipTypes());
		
		Map<String,RelationshipType> types = new HashMap<String,RelationshipType>();
		for(RelationshipType type : m.getRelationshipTypes()){
			types.put(type.getType(), type);
		}
		
		Set<Relationship> results = new HashSet<Relationship>();
		
		for(CandidateSentence s : sents){
			OperableStructure struc = structGenerator.getOperableStructure(s);
			
			Set<String> labels = m.predictLabel(struc);
			for(String label : labels){
				RelationshipType type = types.get(label);
				Relationship r = struc.getCandidateSentence().getRelationship(type);
				r.setLabel(label);
				results.add(r);
			}
		}
		
		return results;
	}
}
