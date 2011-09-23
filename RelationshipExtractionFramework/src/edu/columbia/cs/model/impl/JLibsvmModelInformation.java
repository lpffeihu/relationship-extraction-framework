package edu.columbia.cs.model.impl;

import java.io.Serializable;

import edu.berkeley.compbio.jlibsvm.ImmutableSvmParameterPoint;
import edu.berkeley.compbio.jlibsvm.kernel.KernelFunction;
import edu.columbia.cs.og.structure.OperableStructure;

public class JLibsvmModelInformation implements Serializable {
	private int numSV;
	private OperableStructure[] supportVectors;
	private double[] alpha;
	private float rho;
	private KernelFunction<OperableStructure> kernel;
	private String trueLabel;
	private String falseLabel;
	
	public JLibsvmModelInformation(int numSV, OperableStructure[] supportVectors,
			double[] alpha, float rho, KernelFunction<OperableStructure> kernel, String trueLabel, String falseLabel){
		this.numSV=numSV;
		this.supportVectors=supportVectors;
		this.alpha=alpha;
		this.rho=rho;
		this.setKernel(kernel);
		this.setFalseLabel(falseLabel);
		this.setTrueLabel(trueLabel);
	}

	public int getNumSV() {
		return numSV;
	}

	public void setNumSV(int numSV) {
		this.numSV = numSV;
	}

	public OperableStructure[] getSupportVectors() {
		return supportVectors;
	}

	public void setSupportVectors(OperableStructure[] supportVectors) {
		this.supportVectors = supportVectors;
	}

	public double[] getAlpha() {
		return alpha;
	}

	public void setAlpha(double[] alpha) {
		this.alpha = alpha;
	}

	public float getRho() {
		return rho;
	}

	public void setRho(float rho) {
		this.rho = rho;
	}

	public void setKernel(KernelFunction<OperableStructure> kernel) {
		this.kernel = kernel;
	}

	public KernelFunction<OperableStructure> getKernel() {
		return kernel;
	}

	public void setFalseLabel(String falseLabel) {
		this.falseLabel = falseLabel;
	}

	public String getFalseLabel() {
		return falseLabel;
	}

	public void setTrueLabel(String trueLabel) {
		this.trueLabel = trueLabel;
	}

	public String getTrueLabel() {
		return trueLabel;
	}
}
