package edu.columbia.cs.cg.document.preprocessing.impl;

import edu.columbia.cs.cg.document.preprocessing.Preprocessor;

public class CompositeSequentialPreprocessor implements Preprocessor {

	private Preprocessor[] preprocessors;

	public CompositeSequentialPreprocessor (Preprocessor...preprocessors){
		this.preprocessors = preprocessors;
	}
	
	@Override
	public String process(String content) {
		
		for (int i = 0; i < preprocessors.length; i++) {
			
			content = preprocessors[i].process(content);
			
		}
		
		return content;
	}

}
