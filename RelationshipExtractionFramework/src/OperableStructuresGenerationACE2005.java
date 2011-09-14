import java.io.File;
import java.io.IOException;
import java.util.Set;

import edu.columbia.cs.cg.candidates.CandidateSentence;
import edu.columbia.cs.cg.candidates.CandidatesSentenceReader;
import edu.columbia.cs.cg.candidates.CandidatesSentenceWriter;
import edu.columbia.cs.cg.document.Document;
import edu.columbia.cs.og.algorithm.StructureGenerator;
import edu.columbia.cs.og.configuration.StructureConfiguration;
import edu.columbia.cs.og.core.CoreWriter;
import edu.columbia.cs.og.core.impl.SubsequencesKernel;
import edu.columbia.cs.og.structure.OperableStructure;
import edu.columbia.cs.utils.SGMFileFilter;



public class OperableStructuresGenerationACE2005 {

	public static void main(String[] args) throws IOException, ClassNotFoundException {
		StructureConfiguration conf = new StructureConfiguration(new SubsequencesKernel());
		String inputFolder = "/home/goncalo/ACEProcessedFlat/";
		File ACEDir = new File(inputFolder);
		File[] files = ACEDir.listFiles();
		String outputFolder = "/home/goncalo/ACESubseqFlat/";
		for(int i=0; i<files.length; i++){
			System.out.println("Processing file " + (i+1) + " of " + files.length);
			File f = files[i];
			
			String fileName = f.getName();
			Set<CandidateSentence> sents = CandidatesSentenceReader.readCandidateSentences(inputFolder+fileName);
			Set<OperableStructure> structures = StructureGenerator.generateStructures(sents, conf);
			
			CoreWriter.writeOperableStructures(structures, outputFolder+fileName);
		}
	}

}
