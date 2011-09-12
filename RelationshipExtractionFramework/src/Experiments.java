import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Set;

import edu.columbia.cs.cg.candidates.CandidatesGenerator;
import edu.columbia.cs.cg.document.Document;
import edu.columbia.cs.cg.document.loaders.impl.ACE2005Loader;
import edu.columbia.cs.cg.relations.RelationshipType;
import edu.columbia.cs.cg.sentence.impl.OpenNLPMESplitter;


public class Experiments {

	public static void main(String[] args) throws FileNotFoundException{
		ACE2005Loader l = new ACE2005Loader();
		RelationshipType relationshipType = new RelationshipType("ORG-AFF","Arg-1","Arg-2");
		Set<RelationshipType> relationshipTypes = new HashSet<RelationshipType>();
		relationshipTypes.add(relationshipType);
		Document doc = l.load(new File("/home/goncalo/ACE/data/English/bc/adj/CNN_CF_20030303.1900.00.sgm"), relationshipTypes);
		
		OpenNLPMESplitter splitter = new OpenNLPMESplitter("en-sent.bin");
		
		CandidatesGenerator generator = new CandidatesGenerator(splitter);
		
		generator.generateCandidates(doc, relationshipTypes);
	}

}
