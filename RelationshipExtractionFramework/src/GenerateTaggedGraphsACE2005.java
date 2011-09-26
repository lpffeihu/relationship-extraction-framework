import java.io.File;
import java.io.IOException;
import java.util.Set;

import edu.columbia.cs.cg.candidates.CandidateSentence;
import edu.columbia.cs.cg.candidates.CandidatesSentenceReader;
import edu.columbia.cs.og.algorithm.StructureGenerator;
import edu.columbia.cs.og.configuration.StructureConfiguration;
import edu.columbia.cs.og.core.CoreWriter;
import edu.columbia.cs.og.core.impl.ShortestPathKernel;
import edu.columbia.cs.og.structure.OperableStructure;


public class GenerateTaggedGraphsACE2005 {
	public static void main(String[] args) throws IOException, ClassNotFoundException{
		StructureConfiguration conf = new StructureConfiguration(new ShortestPathKernel());
		
		
		//FeatureGenerator tokenizer = new OpenNLPTokenizationFG("en-token.bin");
		//FeatureGenerator fgChunk = new DependentFeatureGenerator(new EntityBasedChunkingFG(),tokenizer);
		//FeatureGenerator fgPOS = new DependentFeatureGenerator(new OpenNLPPartOfSpeechFG("en-pos-maxent.bin"),fgChunk);
		//FeatureGenerator fgGPOS = new DependentFeatureGenerator(new GenericPartOfSpeechFG(),fgPOS);
		//conf.addFeatureGenerator(fgPOS);
		//conf.addFeatureGenerator(fgGPOS);
		
		String inputFolder = args[1] + args[0] + "/";
		File ACEDir = new File(inputFolder);
		File[] files = ACEDir.listFiles();
		String outputFolder = args[2] + args[0] + "/";
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
