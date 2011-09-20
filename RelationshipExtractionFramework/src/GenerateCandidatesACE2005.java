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
import edu.columbia.cs.cg.relations.constraints.relations.DistanceBetweenEntitiesConstraint;
import edu.columbia.cs.cg.relations.constraints.relations.EntitiesOrderNotRelevantConstraint;
import edu.columbia.cs.cg.relations.constraints.roles.EntityTypeConstraint;
import edu.columbia.cs.cg.sentence.impl.OpenNLPMESplitter;
import edu.columbia.cs.data.Dataset;
import edu.columbia.cs.utils.SGMFileFilter;


public class GenerateCandidatesACE2005 {

	public static void main(String[] args) throws IOException, ClassNotFoundException{
		RelationshipType relationshipType = new RelationshipType("ORG-AFF","Arg-1","Arg-2");
		relationshipType.setConstraints(new EntityTypeConstraint("ORG"), "Arg-2");
		relationshipType.setConstraints(new EntityTypeConstraint("PER"), "Arg-1");
		//relationshipType.setConstraints(new EntitiesOrderNotRelevantConstraint());
		Set<RelationshipType> relationshipTypes = new HashSet<RelationshipType>();
		relationshipTypes.add(relationshipType);
		
		OpenNLPMESplitter splitter = new OpenNLPMESplitter("en-sent.bin");
		CandidatesGenerator generator = new CandidatesGenerator(splitter);
		
		ACE2005Loader l = new ACE2005Loader(relationshipTypes);
		File ACEDir = new File("/home/goncalo/ACEFlat/");
		Dataset<Document> ace2005 = new Dataset<Document>(l,ACEDir,false);
		
		String outputFolder = "/home/goncalo/ACEProcessedFlat/";
		
		for(Document d : ace2005){
			Set<CandidateSentence> candidates = generator.generateCandidates(d, relationshipTypes);	
			CandidatesSentenceWriter.writeCandidateSentences(candidates, outputFolder+d.getFilename());
		}
	}

}
