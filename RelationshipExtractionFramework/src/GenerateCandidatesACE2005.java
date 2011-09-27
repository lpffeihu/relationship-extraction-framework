import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import edu.columbia.cs.cg.candidates.CandidateSentence;
import edu.columbia.cs.cg.candidates.CandidatesGenerator;
import edu.columbia.cs.cg.candidates.CandidatesSentenceWriter;
import edu.columbia.cs.cg.document.Document;
import edu.columbia.cs.cg.document.loaders.impl.ACE2005Loader;
import edu.columbia.cs.cg.relations.RelationshipType;
import edu.columbia.cs.cg.relations.constraints.roles.EntityTypeConstraint;
import edu.columbia.cs.cg.sentence.impl.OpenNLPMESplitter;
import edu.columbia.cs.data.Dataset;


public class GenerateCandidatesACE2005 {

	public static void main(String[] args) throws IOException, ClassNotFoundException{
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
