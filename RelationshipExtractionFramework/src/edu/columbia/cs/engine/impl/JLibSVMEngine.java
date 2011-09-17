package edu.columbia.cs.engine.impl;

import java.util.List;

import edu.berkeley.compbio.jlibsvm.ImmutableSvmParameterPoint;
import edu.berkeley.compbio.jlibsvm.binary.BinaryClassificationProblemImpl;
import edu.berkeley.compbio.jlibsvm.binary.BinaryClassificationSVM;
import edu.berkeley.compbio.jlibsvm.binary.C_SVC;
import edu.berkeley.compbio.jlibsvm.kernel.KernelFunction;
import edu.columbia.cs.cg.relations.RelationshipType;
import edu.columbia.cs.engine.Engine;
import edu.columbia.cs.model.Model;
import edu.columbia.cs.model.impl.JLibsvmBinaryModel;
import edu.columbia.cs.og.core.Kernel;
import edu.columbia.cs.og.core.impl.BagOfNGramsKernel;
import edu.columbia.cs.og.structure.OperableStructure;
import edu.columbia.cs.svm.problem.OperableStructureToBinarySVMproblemConverter;

public class JLibSVMEngine implements Engine {

	private Kernel kernel;
	
	public JLibSVMEngine(Kernel k){
		kernel=k;
	}
	
	private String getPositiveClass(List<OperableStructure> train){
		for(OperableStructure struc : train){
			if(!struc.getLabel().equals(RelationshipType.NOT_A_RELATIONSHIP)){
				return struc.getLabel();
			}
		}
		
		//SHOULD THROW EXCEPTION BECAUSE THE TRAINING DATA ONLY HAS NEGATIVE EXAMPLES
		return "ABC";
	}
	
	
	@Override
	public Model train(List<OperableStructure> train) {
		KernelFunction<OperableStructure> kernel = this.kernel;
		ImmutableSvmParameterPoint.Builder<String, OperableStructure> builder = new ImmutableSvmParameterPoint.Builder<String, OperableStructure>();
		builder.C=50;
		builder.kernel=kernel;
		builder.eps=1f;
		builder.redistributeUnbalancedC=true;
		builder.probability=true;
		
		ImmutableSvmParameterPoint<String, OperableStructure> params = builder.build();
		
		//TODO: Select between binary and multiclass classification
		BinaryClassificationProblemImpl<String,OperableStructure> problem = OperableStructureToBinarySVMproblemConverter.convert(train,"ORG-AFF");

		BinaryClassificationSVM<String,OperableStructure> binary = new C_SVC<String,OperableStructure>();
		
		return new JLibsvmBinaryModel(binary.train(problem, params));
	}

}
