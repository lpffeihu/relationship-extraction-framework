import java.io.IOException;
import java.util.Set;

import edu.columbia.cs.cg.candidates.CandidateSentence;
import edu.columbia.cs.cg.candidates.CandidatesSentenceReader;
import edu.columbia.cs.cg.candidates.CandidatesSentenceWriter;
import edu.columbia.cs.og.algorithm.StructureGenerator;
import edu.columbia.cs.og.configuration.StructureConfiguration;
import edu.columbia.cs.og.core.impl.SubsequencesKernel;



public class OperableStructuresGenerationACE2005 {

	public static void main(String[] args) throws IOException, ClassNotFoundException {
		Set<CandidateSentence> sents = CandidatesSentenceReader.readCandidateSentences("/home/goncalo/ACEProcessedFlat/AFP_ENG_20030304.0250");
		
		StructureConfiguration conf = new StructureConfiguration(new SubsequencesKernel());
		
		StructureGenerator.generateStructures(sents, conf);
	}

}
