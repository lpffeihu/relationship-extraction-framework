import java.io.File;
import java.io.IOException;
import java.util.Set;

import edu.columbia.cs.cg.candidates.CandidateSentence;
import edu.columbia.cs.cg.candidates.CandidatesSentenceReader;
import edu.columbia.cs.og.algorithm.StructureGenerator;
import edu.columbia.cs.og.configuration.StructureConfiguration;
import edu.columbia.cs.og.core.CoreWriter;
import edu.columbia.cs.og.core.impl.BagOfNGramsKernel;
import edu.columbia.cs.og.features.FeatureGenerator;
import edu.columbia.cs.og.features.featureset.SequenceFS;
import edu.columbia.cs.og.features.impl.EntityBasedChunkingFG;
import edu.columbia.cs.og.features.impl.GenericPartOfSpeechFG;
import edu.columbia.cs.og.features.impl.OpenNLPPartOfSpeechFG;
import edu.columbia.cs.og.features.impl.OpenNLPTokenizationFG;
import edu.columbia.cs.og.features.impl.SpansToStringsConvertionFG;
import edu.columbia.cs.og.structure.OperableStructure;
import edu.columbia.cs.utils.Span;



public class OperableStructuresGenerationACE2005 {

	public static void main(String[] args) throws IOException, ClassNotFoundException {
		StructureConfiguration conf = new StructureConfiguration(new BagOfNGramsKernel());
				
		FeatureGenerator<SequenceFS<Span>> tokenizer = new OpenNLPTokenizationFG("en-token.bin");
		FeatureGenerator<SequenceFS<Span>> fgChunk = new EntityBasedChunkingFG(tokenizer);
		FeatureGenerator<SequenceFS<String>> fgChuckString = new SpansToStringsConvertionFG(fgChunk);
		FeatureGenerator<SequenceFS<String>> fgPOS = new OpenNLPPartOfSpeechFG("en-pos-maxent.bin",fgChuckString);
		FeatureGenerator<SequenceFS<String>> fgGPOS = new GenericPartOfSpeechFG(fgPOS);
		//conf.addFeatureGenerator(fgPOS);
		conf.addFeatureGenerator(fgGPOS);
		
		String inputFolder = "/home/goncalo/ACEProcessedFlat/";
		File ACEDir = new File(inputFolder);
		File[] files = ACEDir.listFiles();
		String outputFolder = "/home/goncalo/ACEBoNGramsFlat/";
		for(int i=0; i<files.length; i++){
			File f = files[i];
			
			String fileName = f.getName();
			Set<CandidateSentence> sents = CandidatesSentenceReader.readCandidateSentences(inputFolder+fileName);
			System.out.println("Processing file " + (i+1) + " of " + files.length + " containing " + sents.size() + " sentences");
			Set<OperableStructure> structures = StructureGenerator.generateStructures(sents, conf);
			
			CoreWriter.writeOperableStructures(structures, outputFolder+fileName);
		}
	}

}
