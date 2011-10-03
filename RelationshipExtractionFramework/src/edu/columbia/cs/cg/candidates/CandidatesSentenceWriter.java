package edu.columbia.cs.cg.candidates;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.Set;


public class CandidatesSentenceWriter {
	/**
	 * Method to write a set of candidate sentences to a file. The path to the file is given as input
	 * 
	 * @param candidates the set of candidate 
	 * @return
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public static void writeCandidateSentences(Set<CandidateSentence> candidates, String output) throws IOException{
		ObjectOutput out = new ObjectOutputStream(new FileOutputStream(output));
		out.writeObject(candidates);
		out.close();
	}
}

