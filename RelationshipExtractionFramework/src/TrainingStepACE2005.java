import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.davidsoergel.conja.Parallel;
import com.google.gdata.data.spreadsheet.TableEntry;

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
import edu.columbia.cs.evaluation.measures.Measure;
import edu.columbia.cs.evaluation.measures.NumberOfExpectedPositiveAnswers;
import edu.columbia.cs.evaluation.measures.NumberOfPositiveAnswers;
import edu.columbia.cs.evaluation.measures.NumberOfTruePositives;
import edu.columbia.cs.evaluation.measures.Precision;
import edu.columbia.cs.evaluation.measures.Recall;
import edu.columbia.cs.model.Model;
import edu.columbia.cs.model.impl.JLibsvmBinaryModel;
import edu.columbia.cs.og.core.CoreReader;
import edu.columbia.cs.og.core.impl.BagOfNGramsKernel;
import edu.columbia.cs.og.core.impl.DependencyGraphsKernel;
import edu.columbia.cs.og.core.impl.ShortestPathKernel;
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
		//String pathProc = rootDir+"ACEGraphsFlat/ORG-AFF/";
		String pathProc = rootDir+"ACEBoNGramsFlat/";
		//String pathProc = rootDir+"ACESubseqFlat/";
		
		List<String> trainFiles = getFiles(path + trainFile);
		List<OperableStructure> trainingFiles=new ArrayList<OperableStructure>();
		int trainFilesSize=(int) (trainFiles.size()*0.1);
		for(int i=0; i<trainFilesSize; i++){
			String s = trainFiles.get(i);
			System.out.println("processing [" + s + "]");
			trainingFiles.addAll(getOperableStructure(pathProc,s));
		}
		
		//Engine classificationEngine = new JLibSVMEngine(new DependencyGraphsKernel());
		Engine classificationEngine = new JLibSVMEngine(new BagOfNGramsKernel());
		//Engine classificationEngine = new JLibSVMEngine(new SubsequencesKernel());
		Model svmModel = classificationEngine.train(trainingFiles);
		//svmModel.saveModel("/home/goncalo/Desktop/ORG-AFFModel.svm");
		//svmModel=null;
		//svmModel=JLibsvmBinaryModel.loadModel("/home/goncalo/Desktop/ORG-AFFModel.svm");
		weka.core.SerializationHelper.write("/home/goncalo/Desktop/ORG-AFFModel.svm", svmModel);
		svmModel=null;
		svmModel = (Model) weka.core.SerializationHelper.read("/home/goncalo/Desktop/ORG-AFFModel.svm");
		
		List<String> testFiles = getFiles(path + testFile);
		List<OperableStructure> testingFiles=new ArrayList<OperableStructure>();
		for(String s : testFiles){
			testingFiles.addAll(getOperableStructure(pathProc,s));
		}
		System.out.println("Loaded " + testingFiles.size() + " testing sentences");
		
		Evaluator eval = new Evaluator();
		Measure tp = new NumberOfTruePositives();
		eval.addMeasure(tp);
		Measure pa = new NumberOfPositiveAnswers();
		eval.addMeasure(pa);
		Measure epa = new NumberOfExpectedPositiveAnswers();
		eval.addMeasure(epa);
		Measure acc = new Accuracy();
		eval.addMeasure(acc);
		Measure rec = new Recall();
		eval.addMeasure(rec);
		Measure pre = new Precision();
		eval.addMeasure(pre);
		Measure f = new FMeasure(1.0);
		eval.addMeasure(f);
		eval.printEvaluationReport(testingFiles, svmModel);
		
		/*String id = "BoW Split=" + numberSplit;
		GoogleSpreadsheetsAPI api = new GoogleSpreadsheetsAPI("configFiles/googleAPI.properties");
		TableEntry tableEntry = api.getTable("ResultsACEDataset", "TableACEEvaluation");
		api.updateField(tableEntry, id, "NumCorrect", "" + eval.getResult(tp));
		api.updateField(tableEntry, id, "NumAnswers", "" + eval.getResult(pa));
		api.updateField(tableEntry, id, "NumExpected", "" + eval.getResult(epa));
		api.updateField(tableEntry, id, "Accuracy", "" + eval.getResult(acc));
		api.updateField(tableEntry, id, "Recall", "" + eval.getResult(rec));
		api.updateField(tableEntry, id, "Precision", "" + eval.getResult(pre));
		api.updateField(tableEntry, id, "F1", "" + eval.getResult(f));*/
		
		Parallel.shutdown();
	}

}
