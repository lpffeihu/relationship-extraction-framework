package edu.columbia.cs.utils.postags.impl;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.util.InvalidFormatException;
import edu.columbia.cs.utils.Span;
import edu.columbia.cs.utils.postags.POSTagger;

public class OpenNLPPOSTagger implements POSTagger {

	private POSTaggerME tagger = null;	
	private String pathToTokenizer;
	
	public OpenNLPPOSTagger(String path){
		pathToTokenizer=path;
	}
	
	@Override
	public String[] tag(String[] text) {
		return getPOSTagger().tag(text);
	}

	private POSTaggerME getPOSTagger() {
		
		if (tagger == null){
			
			InputStream modelIn;
			
			try {
				
				POSModel modelPOS=null;
				modelIn = new FileInputStream(pathToTokenizer);
				modelPOS = new POSModel(modelIn);
				modelIn.close();
				tagger = new POSTaggerME(modelPOS);
			
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

		return tagger;
	}

}
