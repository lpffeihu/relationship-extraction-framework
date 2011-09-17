import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.davidsoergel.conja.Parallel;

import edu.berkeley.compbio.jlibsvm.ImmutableSvmParameterPoint;
import edu.berkeley.compbio.jlibsvm.binary.BinaryClassificationProblemImpl;
import edu.berkeley.compbio.jlibsvm.binary.BinaryClassificationSVM;
import edu.berkeley.compbio.jlibsvm.binary.BinaryModel;
import edu.berkeley.compbio.jlibsvm.binary.C_SVC;
import edu.berkeley.compbio.jlibsvm.kernel.KernelFunction;
import edu.columbia.cs.engine.Engine;
import edu.columbia.cs.engine.impl.JLibSVMEngine;
import edu.columbia.cs.model.Model;
import edu.columbia.cs.og.core.CoreReader;
import edu.columbia.cs.og.core.impl.BagOfNGramsKernel;
import edu.columbia.cs.og.core.impl.SubsequencesKernel;
import edu.columbia.cs.og.structure.OperableStructure;
import edu.columbia.cs.svm.problem.OperableStructureToBinarySVMproblemConverter;


public class TrainingStepACE2005 {

	private static List<String> getFiles(String path){
		List<String> entries = new ArrayList<String>();

		try {
			BufferedReader in = new BufferedReader(new FileReader(path));
			String input;
			while ((input = in.readLine()) != null) {
				entries.add(input);
			}
		} catch (IOException e) {
		}

		return entries;
	}
	
	private static Set<OperableStructure> getOperableStructure(String directory, String file) throws Exception{
		String path = directory + file;
		
		Set<OperableStructure> fileSentences = CoreReader.readOperableStructures(path);
		
		return fileSentences;
	}

	public static void main(String[] args) throws Exception {
		int numberSplit=Integer.parseInt(args[1]);
		String trainFile = "train-" + numberSplit;
		String testFile = "test-" + numberSplit;
		
		String rootDir=args[0];
		
		String path = rootDir+"ACEsplits/";
		String pathProc = rootDir+"ACEBoNGramsFlat/";
		
		List<String> trainFiles = getFiles(path + trainFile);
		List<OperableStructure> trainingFiles=new ArrayList<OperableStructure>();
		int trainFilesSize=(int) (trainFiles.size()*0.1);
		for(int i=0; i<trainFilesSize; i++){
			String s = trainFiles.get(i);
			System.out.println("processing [" + s + "]");
			trainingFiles.addAll(getOperableStructure(pathProc,s));
		}
		
		Engine classificationEngine = new JLibSVMEngine(new BagOfNGramsKernel());
		Model svmModel = classificationEngine.train(trainingFiles);
		
		List<String> testFiles = getFiles(path + testFile);
		List<OperableStructure> testingFiles=new ArrayList<OperableStructure>();
		for(String s : testFiles){
			testingFiles.addAll(getOperableStructure(pathProc,s));
		}
		System.out.println("Loaded " + testingFiles.size() + " testing sentences");
		
		double[] weights = new double[]{0.2};
		
		for(double w : weights){
			int testingFilesSize=testingFiles.size();
			int numCorrect=0;
			int numAnswers=0;
			int numExpected=0;
			int numOk=0;
			for(int i=0; i<testingFilesSize; i++){
				OperableStructure s = testingFiles.get(i);
				
				String predicted;
				predicted = svmModel.predictLabel(s);
				/*double confidence = svmModel.getTrueProbability(s);
				if(confidence<w){
					predicted="";
				}else{
					predicted="ORG-AFF";
				}*/
				
				String trueLabel = s.getLabel();
				System.out.println("i=" + i + ": [" + predicted + "," + trueLabel + "]");
				if((predicted==null || predicted.equals("")) && trueLabel.equals("")){
					//do nothing
					numOk++;
				}else if(trueLabel.equals(predicted)){
					numCorrect++;
				}
				if(!(predicted==null || predicted.equals(""))){
					numAnswers++;
				}
				if(!trueLabel.equals("")){
					numExpected++;
				}
			}
			
			double recall=(double)numCorrect/(double)numExpected;
			double precision=(double)numCorrect/(double)numAnswers;
			double f1 = 2*(recall*precision)/(recall+precision);
			double acc = (numOk+numCorrect)/(double)testingFilesSize;
			
			System.out.println("Num Correct= " + numCorrect);
			System.out.println("Num Answers= " + numAnswers);
			System.out.println("Num Expected= " + numExpected);
			System.out.println("Accuracy=" + acc);
			System.out.println("Recall= " + recall);
			System.out.println("Precision= " + precision);
			System.out.println("F1=" + f1);
		}
		
		Parallel.shutdown();
	}

}
