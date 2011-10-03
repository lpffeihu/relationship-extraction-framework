package edu.columbia.cs.cg.candidates;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.util.Set;


/**
 * This class provides a static method that can be used to read a set of candidate sentences
 * from a file.
 * 
 * <br>
 * <br>
 * 
 * The file read by this class must be a file that was written with the CandidatesSentenceWriter
 * class.
 * 
 * @author      Pablo Barrio
 * @author		Goncalo Simoes
 * @version     0.1
 * @since       2011-09-27
 */
public class CandidatesSentenceReader {
	
	/**
	 * Method to read a set of candidate sentences from a file. The path to the file is given as input
	 * 
	 * @param input
	 * @return
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public static Set<CandidateSentence> readCandidateSentences(String input) throws IOException, ClassNotFoundException{
		InputStream file = new FileInputStream( input );
		InputStream buffer = new BufferedInputStream( file );
		ObjectInput inputObj = new ObjectInputStream ( buffer );
		Set<CandidateSentence> candidates = (Set<CandidateSentence>)inputObj.readObject();
		inputObj.close();
		return candidates;
	}
}

