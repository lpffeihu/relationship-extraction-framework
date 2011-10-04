package edu.columbia.cs.cg.document.tokenized.tokenizer;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.util.InvalidFormatException;
import edu.columbia.cs.utils.Span;

public class OpenNLPTokenizer implements Tokenizer {

	private TokenizerME tokenizer = null;	
	private String pathToTokenizer;
	
	public OpenNLPTokenizer(String path){
		pathToTokenizer=path;
	}
	
	@Override
	public Span[] tokenize(String text) {
		
		Span[] ret = convert(getTokenizer().tokenizePos(text));
		
		return ret;
	}

	private Span[] convert(opennlp.tools.util.Span[] tokenizePos) {
		
		Span[] ret = new Span[tokenizePos.length];
		
		for (int i = 0; i < ret.length; i++) {
			
			ret[i] = new Span(tokenizePos[i]);
			
		}
		
		return ret;
	}

	private TokenizerME getTokenizer() {
		
		if (tokenizer == null){
			
			InputStream modelIn;
			
			try {
				
				modelIn = new FileInputStream(pathToTokenizer);
				
				TokenizerModel tokModel = new TokenizerModel(modelIn);
				
				modelIn.close();
				
				tokenizer = new TokenizerME(tokModel);
			
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

		return tokenizer;
	}

	
}
