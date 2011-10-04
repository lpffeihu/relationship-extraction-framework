package edu.columbia.cs.utils.chucking;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import opennlp.tools.chunker.ChunkerME;
import opennlp.tools.chunker.ChunkerModel;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.util.InvalidFormatException;

public class OpenNLPChunker implements Chunker {

	private ChunkerME chunker = null;	
	private String pathToTokenizer;
	
	public OpenNLPChunker(String path){
		pathToTokenizer=path;
	}
	
	@Override
	public String[] chunk(String[] toks, String[] tags) {
		return getChunker().chunk(toks, tags);
	}
	
	private ChunkerME getChunker() {
		
		if (chunker == null){
			
			InputStream modelIn;
			
			try {
				
				ChunkerModel modelModel=null;
				modelIn = new FileInputStream(pathToTokenizer);
				modelModel = new ChunkerModel(modelIn);
				modelIn.close();
				chunker  = new ChunkerME(modelModel);
			
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvalidFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
						
		}

		return chunker;
	}

}
