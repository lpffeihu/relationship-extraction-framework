package edu.columbia.cs.ref.model.re.impl;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import cern.colt.Arrays;

import edu.berkeley.compbio.jlibsvm.ImmutableSvmParameterPoint;
import edu.berkeley.compbio.jlibsvm.SolutionModel;
import edu.berkeley.compbio.jlibsvm.binary.BinaryModel;
import edu.berkeley.compbio.jlibsvm.kernel.KernelFunction;
import edu.columbia.cs.ref.model.CandidateSentence;
import edu.columbia.cs.ref.model.StructureConfiguration;
import edu.columbia.cs.ref.model.core.Core;
import edu.columbia.cs.ref.model.core.Kernel;
import edu.columbia.cs.ref.model.core.structure.OperableStructure;
import edu.columbia.cs.ref.model.re.Model;
import edu.columbia.cs.ref.model.relationship.RelationshipType;

public class JLibsvmCompositeBinaryModel extends Model{
	private Set<JLibsvmBinaryModel> models;
	private StructureConfiguration conf;
	private Set<RelationshipType> relationshipTypes;

	public JLibsvmCompositeBinaryModel(StructureConfiguration conf, Set<RelationshipType> relationshipTypes) {
		models = new HashSet<JLibsvmBinaryModel>();
		this.relationshipTypes=relationshipTypes;
		this.conf=conf;
	}

	public void addModel(JLibsvmBinaryModel model){
		models.add(model);
	}

	@Override
	public Set<String> getPredictedLabel(OperableStructure s) {
		Set<String> labels = new HashSet<String>();
		for(JLibsvmBinaryModel model : models){
			labels.addAll(model.getPredictedLabel(s));
		}

		return labels;
	}

	@Override
	protected PredictionProperties[] getAvailablePredictionProperties() {
		return new PredictionProperties[]{};
	}

	@Override
	protected Object getPredictionPropertyValue(
			PredictionProperties predictionProperties) {

		return null;
	}

	private void writeObject(ObjectOutputStream out) throws IOException{
		out.defaultWriteObject();
		out.writeInt(models.size());
		for(JLibsvmBinaryModel model : models){
			out.writeObject(model);
		}
		out.writeObject(conf);
		out.writeObject(relationshipTypes);
	}

	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException{
		in.defaultReadObject();
		int sizeModels = in.readInt();
		models = new HashSet<JLibsvmBinaryModel>();
		for(int i=0; i<sizeModels; i++){
			models.add((JLibsvmBinaryModel) in.readObject());
		}
		conf=(StructureConfiguration) in.readObject();
		relationshipTypes=(Set<RelationshipType>) in.readObject();
	}

	@Override
	public StructureConfiguration getStructureConfiguration() {
		return conf;
	}
	
	@Override
	public Set<RelationshipType> getRelationshipTypes() {
		return relationshipTypes;
	}
}
