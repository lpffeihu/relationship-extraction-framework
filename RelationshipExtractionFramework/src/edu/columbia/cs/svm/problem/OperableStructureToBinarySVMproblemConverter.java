package edu.columbia.cs.svm.problem;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.berkeley.compbio.jlibsvm.binary.BinaryClassificationProblemImpl;
import edu.berkeley.compbio.jlibsvm.scaler.NoopScalingModel;
import edu.columbia.cs.cg.relations.RelationshipType;
import edu.columbia.cs.og.structure.OperableStructure;

public class OperableStructureToBinarySVMproblemConverter {

	public static BinaryClassificationProblemImpl<String, OperableStructure> convert(
			List<OperableStructure> train, String relLabel) {
		Map<OperableStructure,String> examples = new HashMap<OperableStructure,String>();
		Map<OperableStructure,Integer> exampleIds = new HashMap<OperableStructure,Integer>();
		
		Map<String,Integer> numDocs = new HashMap<String,Integer>();
		
		int i=0;
		for(OperableStructure sent : train){
			examples.put(sent, sent.getLabel());
			exampleIds.put(sent, i);
			
			Integer res = numDocs.get(sent.getLabel());
			if(res==null){
				res=0;
			}
			numDocs.put(sent.getLabel(), res+1);
			
			i++;
		}
		
		System.out.println(numDocs);
		
		BinaryClassificationProblemImpl<String,OperableStructure> result = 
			new BinaryClassificationProblemImpl<String,OperableStructure>(String.class, 
					examples, exampleIds, new NoopScalingModel<OperableStructure>(),
					relLabel,RelationshipType.NOT_A_RELATIONSHIP);
		
		return result;
	}

}
