package edu.columbia.cs.cg.candidates;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.Set;

public class CandidatesSentenceReader {
	public static Set<CandidateSentence> readCandidateSentences(String output) throws IOException, ClassNotFoundException{
		InputStream file = new FileInputStream( output );
		InputStream buffer = new BufferedInputStream( file );
		ObjectInput input = new ObjectInputStream ( buffer );
		Set<CandidateSentence> candidates = (Set<CandidateSentence>)input.readObject();
		input.close();
		return candidates;
	}
}

