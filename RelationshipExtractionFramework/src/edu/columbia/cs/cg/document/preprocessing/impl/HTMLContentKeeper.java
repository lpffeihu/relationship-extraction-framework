package edu.columbia.cs.cg.document.preprocessing.impl;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import com.google.gdata.util.common.html.HtmlToText;

import edu.columbia.cs.cg.document.preprocessing.Preprocessor;

public class HTMLContentKeeper implements Preprocessor {

	@Override
	public String process(String content) {
		

		return HtmlToText.htmlToPlainText(content);
		
		
	}

	
}
