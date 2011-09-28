package edu.columbia.cs.engine.impl;

import java.util.List;
import java.util.Set;

import edu.berkeley.compbio.jlibsvm.ImmutableSvmParameterPoint;
import edu.berkeley.compbio.jlibsvm.binary.BinaryClassificationProblemImpl;
import edu.berkeley.compbio.jlibsvm.binary.BinaryClassificationSVM;
import edu.berkeley.compbio.jlibsvm.binary.C_SVC;
import edu.berkeley.compbio.jlibsvm.kernel.KernelFunction;
import edu.columbia.cs.cg.relations.RelationshipType;
import edu.columbia.cs.engine.Engine;
import edu.columbia.cs.model.Model;
import edu.columbia.cs.model.impl.JLibsvmBinaryModel;
import edu.columbia.cs.model.impl.JLibsvmCompositeBinaryModel;
import edu.columbia.cs.og.configuration.StructureConfiguration;
import edu.columbia.cs.og.core.Core;
import edu.columbia.cs.og.core.Kernel;
import edu.columbia.cs.og.core.impl.BagOfNGramsKernel;
import edu.columbia.cs.og.structure.OperableStructure;
import edu.columbia.cs.svm.problem.OperableStructureToBinarySVMproblemConverter;

public class JLibSVMBinaryEngine implements Engine {

	private StructureConfiguration conf;
	private Set<RelationshipType> relTypes;
	
	public JLibSVMBinaryEngine(StructureConfiguration conf, Set<RelationshipType> relationshipTypes){
		this.conf=conf;
		relTypes=relationshipTypes;
	}
	
	@Override
	public Model train(List<OperableStructure> train) {
		KernelFunction<OperableStructure> kernel = (Kernel)conf.getCore();
		ImmutableSvmParameterPoint.Builder<String, OperableStructure> builder = new ImmutableSvmParameterPoint.Builder<String, OperableStructure>();
		builder.C=50;
		builder.kernel=kernel;
		builder.eps=1f;
		builder.redistributeUnbalancedC=true;
		builder.probability=true;
		
		ImmutableSvmParameterPoint<String, OperableStructure> params = builder.build();
		
		JLibsvmCompositeBinaryModel compositeModel = new JLibsvmCompositeBinaryModel(conf,relTypes);
		
		for(RelationshipType t : relTypes){
			BinaryClassificationProblemImpl<String,OperableStructure> problem = OperableStructureToBinarySVMproblemConverter.convert(train,t);
	
			BinaryClassificationSVM<String,OperableStructure> binary = new C_SVC<String,OperableStructure>();
			
			compositeModel.addModel(new JLibsvmBinaryModel(binary.train(problem, params),conf,relTypes));
		}
		return compositeModel;
	}

}
