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
import edu.columbia.cs.evaluation.Evaluator;
import edu.columbia.cs.evaluation.measures.Accuracy;
import edu.columbia.cs.evaluation.measures.FMeasure;
import edu.columbia.cs.evaluation.measures.NumberOfExpectedPositiveAnswers;
import edu.columbia.cs.evaluation.measures.NumberOfPositiveAnswers;
import edu.columbia.cs.evaluation.measures.NumberOfTruePositives;
import edu.columbia.cs.evaluation.measures.Precision;
import edu.columbia.cs.evaluation.measures.Recall;
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
		
		Evaluator eval = new Evaluator();
		eval.addMeasure(new NumberOfTruePositives());
		eval.addMeasure(new NumberOfPositiveAnswers());
		eval.addMeasure(new NumberOfExpectedPositiveAnswers());
		eval.addMeasure(new Accuracy());
		eval.addMeasure(new Recall());
		eval.addMeasure(new Precision());
		eval.addMeasure(new FMeasure(1.0));
		eval.addMeasure(new FMeasure(0.5));
		eval.addMeasure(new FMeasure(2.0));
		eval.printEvaluationReport(testingFiles, svmModel);
		
		Parallel.shutdown();
	}

}
