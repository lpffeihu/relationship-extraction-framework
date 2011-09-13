package edu.columbia.cs.og.algorithm;

import java.util.List;
import java.util.HashSet;
import java.util.Set;

import edu.columbia.cs.cg.candidates.CandidateSentence;
import edu.columbia.cs.og.configuration.StructureConfiguration;
import edu.columbia.cs.og.features.FeatureGenerator;
import edu.columbia.cs.og.structure.OperableStructure;

public class StructureGenerator {
	public static Set<OperableStructure> generateStructures(Set<CandidateSentence> candidates, StructureConfiguration config){
		Set<OperableStructure> result = new HashSet<OperableStructure>();
		for(CandidateSentence sent : candidates){
			result.add(config.getOperableStructure(sent));
		}
		return result;
	}
}
