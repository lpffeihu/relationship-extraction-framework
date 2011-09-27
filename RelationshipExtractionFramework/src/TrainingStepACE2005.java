import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.davidsoergel.conja.Parallel;

import edu.columbia.cs.cg.relations.RelationshipType;
import edu.columbia.cs.cg.relations.constraints.roles.EntityTypeConstraint;
import edu.columbia.cs.engine.Engine;
import edu.columbia.cs.engine.impl.JLibSVMBinaryEngine;
import edu.columbia.cs.engine.impl.JLibSVMMulticlassEngine;
import edu.columbia.cs.evaluation.Evaluator;
import edu.columbia.cs.evaluation.measures.FMeasure;
import edu.columbia.cs.evaluation.measures.Measure;
import edu.columbia.cs.evaluation.measures.NumberOfExpectedPositiveAnswers;
import edu.columbia.cs.evaluation.measures.NumberOfPositiveAnswers;
import edu.columbia.cs.evaluation.measures.NumberOfTruePositives;
import edu.columbia.cs.evaluation.measures.Precision;
import edu.columbia.cs.evaluation.measures.Recall;
import edu.columbia.cs.model.Model;
import edu.columbia.cs.og.core.CoreReader;
import edu.columbia.cs.og.core.impl.BagOfNGramsKernel;
import edu.columbia.cs.og.structure.OperableStructure;


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
		RelationshipType relationshipTypeMembership = new RelationshipType("Membership","Arg-1","Arg-2");
		relationshipTypeMembership.setConstraints(new EntityTypeConstraint("ORG"), "Arg-2");
		relationshipTypeMembership.setConstraints(new EntityTypeConstraint("PER"), "Arg-1");
		
		RelationshipType relationshipTypeEmployment = new RelationshipType("Employment","Arg-1","Arg-2");
		relationshipTypeEmployment.setConstraints(new EntityTypeConstraint("ORG"), "Arg-2");
		relationshipTypeEmployment.setConstraints(new EntityTypeConstraint("PER"), "Arg-1");
		
		RelationshipType relationshipTypeFounder = new RelationshipType("Founder","Arg-1","Arg-2");
		relationshipTypeFounder.setConstraints(new EntityTypeConstraint("ORG"), "Arg-2");
		relationshipTypeFounder.setConstraints(new EntityTypeConstraint("PER"), "Arg-1");
		
		RelationshipType relationshipTypeOwnership = new RelationshipType("Ownership","Arg-1","Arg-2");
		relationshipTypeOwnership.setConstraints(new EntityTypeConstraint("ORG"), "Arg-2");
		relationshipTypeOwnership.setConstraints(new EntityTypeConstraint("PER"), "Arg-1");
		
		RelationshipType relationshipTypeStudentAlum = new RelationshipType("Student-Alum","Arg-1","Arg-2");
		relationshipTypeStudentAlum.setConstraints(new EntityTypeConstraint("ORG"), "Arg-2");
		relationshipTypeStudentAlum.setConstraints(new EntityTypeConstraint("PER"), "Arg-1");
		
		RelationshipType relationshipTypeSports = new RelationshipType("Sports-Affiliation","Arg-1","Arg-2");
		relationshipTypeSports.setConstraints(new EntityTypeConstraint("ORG"), "Arg-2");
		relationshipTypeSports.setConstraints(new EntityTypeConstraint("PER"), "Arg-1");
		
		RelationshipType relationshipTypeInvestor = new RelationshipType("Investor-Shareholder","Arg-1","Arg-2");
		relationshipTypeInvestor.setConstraints(new EntityTypeConstraint("ORG"), "Arg-2");
		relationshipTypeInvestor.setConstraints(new EntityTypeConstraint("PER"), "Arg-1");
		
		//relationshipType.setConstraints(new EntitiesOrderNotRelevantConstraint());
		Set<RelationshipType> relationshipTypes = new HashSet<RelationshipType>();
		relationshipTypes.add(relationshipTypeEmployment);
		relationshipTypes.add(relationshipTypeMembership);
		relationshipTypes.add(relationshipTypeFounder);
		relationshipTypes.add(relationshipTypeOwnership);
		relationshipTypes.add(relationshipTypeStudentAlum);
		relationshipTypes.add(relationshipTypeSports);
		relationshipTypes.add(relationshipTypeInvestor);
		
		
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
		int trainFilesSize=(int) (trainFiles.size()*0.4);
		for(int i=0; i<trainFilesSize; i++){
			String s = trainFiles.get(i);
			System.out.println("processing [" + s + "]");
			trainingFiles.addAll(getOperableStructure(pathProc,s));
		}
		
		Engine classificationEngine = new JLibSVMMulticlassEngine(new BagOfNGramsKernel());
		Model svmModel = classificationEngine.train(trainingFiles);
		//edu.columbia.cs.selialization.SerializationHelper.write("/home/goncalo/Desktop/ORG-AFFModel.svm", svmModel);
		//svmModel=null;
		//svmModel = (Model) edu.columbia.cs.selialization.SerializationHelper.read("/home/goncalo/Desktop/ORG-AFFModel.svm");
		
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
