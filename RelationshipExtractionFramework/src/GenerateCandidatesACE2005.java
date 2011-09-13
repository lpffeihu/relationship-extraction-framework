import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import edu.columbia.cs.cg.candidates.CandidateSentence;
import edu.columbia.cs.cg.candidates.CandidatesGenerator;
import edu.columbia.cs.cg.candidates.CandidatesSentenceReader;
import edu.columbia.cs.cg.candidates.CandidatesSentenceWriter;
import edu.columbia.cs.cg.document.Document;
import edu.columbia.cs.cg.document.loaders.impl.ACE2005Loader;
import edu.columbia.cs.cg.relations.RelationshipType;
import edu.columbia.cs.cg.relations.constraints.roles.EntityTypeConstraint;
import edu.columbia.cs.cg.sentence.impl.OpenNLPMESplitter;
import edu.columbia.cs.utils.SGMFileFilter;


public class GenerateCandidatesACE2005 {

	public static void main(String[] args) throws IOException, ClassNotFoundException{
		ACE2005Loader l = new ACE2005Loader();
		RelationshipType relationshipType = new RelationshipType("ORG-AFF","Arg-1","Arg-2");
		relationshipType.setConstraints(new EntityTypeConstraint("PER"), "Arg-1");
		relationshipType.setConstraints(new EntityTypeConstraint("ORG"), "Arg-2");
		Set<RelationshipType> relationshipTypes = new HashSet<RelationshipType>();
		relationshipTypes.add(relationshipType);
		
		OpenNLPMESplitter splitter = new OpenNLPMESplitter("en-sent.bin");
		CandidatesGenerator generator = new CandidatesGenerator(splitter);
		
		File ACEDir = new File("/home/goncalo/ACEFlat/");
		File[] files = ACEDir.listFiles(new SGMFileFilter());
		
		String outputFolder = "/home/goncalo/ACEProcessedFlat/";
		
		for(int i=0; i<files.length; i++){
			System.out.println("Processing file " + (i+1) + " of " + files.length);
			File f = files[i];
			
			String fileName = f.getName();
			fileName = fileName.substring(0,fileName.length()-4);
			Document doc = l.load(f, relationshipTypes);
			
			Set<CandidateSentence> candidates = generator.generateCandidates(doc, relationshipTypes);
			
			CandidatesSentenceWriter.writeCandidateSentences(candidates, outputFolder+fileName);
		}
	}

}
