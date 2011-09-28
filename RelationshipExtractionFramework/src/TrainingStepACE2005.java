import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import weka.classifiers.functions.Logistic;
import weka.core.SerializationHelper;

import com.davidsoergel.conja.Parallel;

import edu.columbia.cs.api.RelationshipExtractor;
import edu.columbia.cs.cg.candidates.CandidateSentence;
import edu.columbia.cs.cg.candidates.CandidatesSentenceWriter;
import edu.columbia.cs.cg.document.Document;
import edu.columbia.cs.cg.document.loaders.impl.ACE2005Loader;
import edu.columbia.cs.cg.relations.RelationshipType;
import edu.columbia.cs.cg.relations.constraints.roles.EntityTypeConstraint;
import edu.columbia.cs.cg.sentence.impl.OpenNLPMESplitter;
import edu.columbia.cs.data.Dataset;
import edu.columbia.cs.engine.Engine;
import edu.columbia.cs.engine.impl.JLibSVMBinaryEngine;
import edu.columbia.cs.engine.impl.JLibSVMMulticlassEngine;
import edu.columbia.cs.engine.impl.WekaClassifierEngine;
import edu.columbia.cs.evaluation.Evaluator;
import edu.columbia.cs.evaluation.measures.FMeasure;
import edu.columbia.cs.evaluation.measures.Measure;
import edu.columbia.cs.evaluation.measures.NumberOfExpectedPositiveAnswers;
import edu.columbia.cs.evaluation.measures.NumberOfPositiveAnswers;
import edu.columbia.cs.evaluation.measures.NumberOfTruePositives;
import edu.columbia.cs.evaluation.measures.Precision;
import edu.columbia.cs.evaluation.measures.Recall;
import edu.columbia.cs.model.Model;
import edu.columbia.cs.og.configuration.StructureConfiguration;
import edu.columbia.cs.og.core.CoreReader;
import edu.columbia.cs.og.core.impl.BagOfNGramsKernel;
import edu.columbia.cs.og.structure.OperableStructure;
import edu.washington.cs.knowitall.extractor.conf.ReVerbFeatures;


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
		RelationshipType relationshipType = new RelationshipType("ORG-AFF","Arg-1","Arg-2");
		relationshipType.setConstraints(new EntityTypeConstraint("ORG"), "Arg-2");
		relationshipType.setConstraints(new EntityTypeConstraint("PER"), "Arg-1");
		
		//relationshipType.setConstraints(new EntitiesOrderNotRelevantConstraint());
		Set<RelationshipType> relationshipTypes = new HashSet<RelationshipType>();
		relationshipTypes.add(relationshipType);
		
		
		int numberSplit=Integer.parseInt(args[1]);
		String trainFile = "train-" + numberSplit;
		String testFile = "test-" + numberSplit;
		
		String rootDir=args[0];
		
		String path = rootDir+"ACEsplits/";
		//String pathProc = rootDir+"ACEGraphsFlat/ORG-AFF/";
		String pathProc = rootDir+"ACEBoNGramsFlat/";
		//String pathProc = rootDir+"ACESubseqFlat/";
		
		StructureConfiguration conf = (StructureConfiguration) SerializationHelper.read(pathProc+"Configuration.bin");
		
		List<String> trainFiles = getFiles(path + trainFile);
		List<OperableStructure> trainingFiles=new ArrayList<OperableStructure>();
		int trainFilesSize=(int) (trainFiles.size()*0.1);
		for(int i=0; i<trainFilesSize; i++){
			String s = trainFiles.get(i);
			System.out.println("processing [" + s + "]");
			trainingFiles.addAll(getOperableStructure(pathProc,s));
		}
		Engine classificationEngine = new WekaClassifierEngine(new Logistic(),new ReVerbFeatures().getFeatureSet(),conf,relationshipTypes);
		Model svmModel = classificationEngine.train(trainingFiles);
		edu.columbia.cs.selialization.SerializationHelper.write("/home/goncalo/Desktop/ORG-AFFModel.svm", svmModel);
		svmModel=null;
		svmModel = (Model) edu.columbia.cs.selialization.SerializationHelper.read("/home/goncalo/Desktop/ORG-AFFModel.svm");
		
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
		Measure rec = new Recall();
		eval.addMeasure(rec);
		Measure pre = new Precision();
		eval.addMeasure(pre);
		Measure f = new FMeasure(1.0);
		eval.addMeasure(f);
		eval.printEvaluationReport(testingFiles, svmModel);
		
		
		RelationshipExtractor extractor = new RelationshipExtractor(svmModel, new OpenNLPMESplitter("en-sent.bin"));
		ACE2005Loader l = new ACE2005Loader(relationshipTypes);
		File ACEDir = new File("/home/goncalo/ACEFlat/");
		Dataset<Document> ace2005 = new Dataset<Document>(l,ACEDir,false);
		
		String outputFolder = "/home/goncalo/ACEProcessedFlat/";
		
		for(Document d : ace2005){
			System.out.println(extractor.extractTuples(d));
		}
		
		Parallel.shutdown();
	}

}
