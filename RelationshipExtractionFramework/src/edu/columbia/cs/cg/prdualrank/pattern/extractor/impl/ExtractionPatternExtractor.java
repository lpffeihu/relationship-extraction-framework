package edu.columbia.cs.cg.prdualrank.pattern.extractor.impl;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.util.InvalidFormatException;
import opennlp.tools.util.Span;

import edu.columbia.cs.cg.document.Document;
import edu.columbia.cs.cg.pattern.Pattern;
import edu.columbia.cs.cg.prdualrank.pattern.extractor.PatternExtractor;
import edu.columbia.cs.cg.relations.Relationship;

public class ExtractionPatternExtractor extends PatternExtractor {

	private int window;

	private TokenizerME tokenizer = null;
	
	public ExtractionPatternExtractor(int window) {
		
		this.window = window;
				
		//use opennlp 
		
	}

	private TokenizerME getTokenizer() {
		
		if (tokenizer == null){
			
			InputStream modelIn;
			
			try {
				
				modelIn = new FileInputStream("en-token.bin");
				
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
	
	@Override
	protected Map<Pattern, Integer> extract(Document document, Relationship relationship) {
		
		//I receive only the relationships that are interesting for me.
		//they were also loaded in such
		
		String text = document.getPlainText().get(0).getValue();
		
		Span[] spans = tokenizer.tokenizePos(text);
		
		//finish ..
		
		//find the index of every entity
		
		return null;
		
	}

}
