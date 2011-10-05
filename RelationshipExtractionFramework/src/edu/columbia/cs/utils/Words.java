package edu.columbia.cs.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class Words {

	private static final String[] sym = { "&", "_", ".", ",", "?", "!", ":", "-",
		"\\", "/", ">", "<", "'", "\"", "|", ")", "(", ";", "{", "}" , "[" , "]","^","—","·"};
	
	private static final String defaultStopWords = "data/stopWords.txt";
	
	private static Set<String> stopWords = null;
	
	private static Set<String> nonSearchableSymbol = null;
	
	public static void initialize(File stopWordsFile, File nonSearchableSymbols){
		if (nonSearchableSymbol == null){

			if (nonSearchableSymbols==null){
				nonSearchableSymbol = new HashSet<String>();
				
				for (int i = 0; i < sym.length; i++) {
					nonSearchableSymbol.add(sym[i]);
				}
			}else{
				loadNonSearchableSymbols(nonSearchableSymbols);
			}
		}
		if (stopWords == null){
			if (stopWordsFile == null)
				loadStopWords(new File(defaultStopWords));
			else
				loadStopWords(stopWordsFile);
		}
	}
	
	private static void loadStopWords(File stopWordsFile){
		
		stopWords = new HashSet<String>();
		
		try {
			loadContent(stopWordsFile,stopWords);
		} catch (IOException e) {
			stopWords = null;
		}
		
	}
	
	private static void loadContent(File words, Set<String> set) throws IOException {
		
		BufferedReader br = new BufferedReader(new FileReader(words));
		
		String line;
		
		while ((line = br.readLine())!=null){
			set.add(line.toLowerCase());
		}
		
		br.close();
		
	}

	private static void loadNonSearchableSymbols(File nonSearchableSymbols){
		
		nonSearchableSymbol = new HashSet<String>();
		
		try {
			loadContent(nonSearchableSymbols, nonSearchableSymbol);
		} catch (IOException e) {
			nonSearchableSymbol = null;
		}
		
	}

	public static boolean isSearchable(String word) {
				
		String lowerCase = word.toLowerCase();
		
		if (nonSearchableSymbol.contains(lowerCase) || stopWords.contains(lowerCase))
			return false;
		
		return true;
	}

	public static Set<String> getStopWords() {
			
		if (stopWords == null){
			initialize(new File(defaultStopWords), null);
		}
		return stopWords;
	}
	
}
