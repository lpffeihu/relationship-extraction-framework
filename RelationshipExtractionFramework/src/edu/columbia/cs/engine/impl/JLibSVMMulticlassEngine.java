package edu.columbia.cs.engine.impl;

import java.util.List;
import java.util.Set;

import edu.berkeley.compbio.jlibsvm.ImmutableSvmParameterPoint;
import edu.berkeley.compbio.jlibsvm.binary.BinaryClassificationProblemImpl;
import edu.berkeley.compbio.jlibsvm.binary.BinaryClassificationSVM;
import edu.berkeley.compbio.jlibsvm.binary.C_SVC;
import edu.berkeley.compbio.jlibsvm.kernel.KernelFunction;
import edu.berkeley.compbio.jlibsvm.multi.MultiClassProblemImpl;
import edu.berkeley.compbio.jlibsvm.multi.MultiClassificationSVM;
import edu.columbia.cs.cg.relations.RelationshipType;
import edu.columbia.cs.engine.Engine;
import edu.columbia.cs.model.Model;
import edu.columbia.cs.model.impl.JLibsvmBinaryModel;
import edu.columbia.cs.model.impl.JLibsvmCompositeBinaryModel;
import edu.columbia.cs.model.impl.JLibsvmMulticlassModelModel;
import edu.columbia.cs.og.core.Kernel;
import edu.columbia.cs.og.core.impl.BagOfNGramsKernel;
import edu.columbia.cs.og.structure.OperableStructure;
import edu.columbia.cs.svm.problem.OperableStructureToBinarySVMproblemConverter;
import edu.columbia.cs.svm.problem.OperableStructureToMulticlassSVMproblemConverter;

public class JLibSVMMulticlassEngine implements Engine {

	private Kernel kernel;
	
	public JLibSVMMulticlassEngine(Kernel k){
		kernel=k;
	}
	
	@Override
	public Model train(List<OperableStructure> train) {
		KernelFunction<OperableStructure> kernel = this.kernel;
		ImmutableSvmParameterPoint.Builder<String, OperableStructure> builder = new ImmutableSvmParameterPoint.Builder<String, OperableStructure>();
		builder.C=50;
		builder.kernel=kernel;
		builder.eps=1;
		
		ImmutableSvmParameterPoint<String, OperableStructure> params = builder.build();
		
		MultiClassProblemImpl<String,OperableStructure> problem = OperableStructureToMulticlassSVMproblemConverter.convert(train);
	
		BinaryClassificationSVM<String,OperableStructure> binary = new C_SVC<String,OperableStructure>();
		
		MultiClassificationSVM<String,OperableStructure> multi = new MultiClassificationSVM<String,OperableStructure>(binary);
		
		return new JLibsvmMulticlassModelModel(multi.train(problem, params));
	}

}
